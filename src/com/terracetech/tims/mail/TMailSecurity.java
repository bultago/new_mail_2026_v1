package com.terracetech.tims.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.X509Extension;
import java.security.cert.X509Certificate;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.eclipse.angus.mail.util.BASE64EncoderStream;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESigned;

public class TMailSecurity {

    private SMIMESigned signed = null;

    /* ������ ���� */
    
    public String subaltnam;  // ������ ������ ���
    public boolean intact;    // ������� ���� ����
    public boolean verified;  // ���� Ȯ��
    public boolean requested; // ���� Ȯ�� ���� ��û
    public boolean revoked;   // ������ ID ���� Ȯ��
    public String status;     // ���� ����
    public String label;      // ���� ���̺�

    /* ��ȣȭ */

    public boolean encrypted; // ��ȣȭ�� ���� �� ÷�� ����
    public String method;     // ��ȣȭ ���

    static {
        Security.addProvider(
            new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    TMailSecurity(MimeMessage msg) throws IOException, MessagingException {
        try {
            if (msg.isMimeType("multipart/signed")) {
                signed = new SMIMESigned((MimeMultipart) msg.getContent());
            }
            else if (msg.isMimeType("application/pkcs7-mime") ||
                msg.isMimeType("application/x-pkcs7-mime")) {

                signed = new SMIMESigned(msg);
            }
            else {
                throw new MessagingException("No S/MIME message");
            }
        }
        catch (CMSException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (SMIMEException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public String getCertificate() throws IOException, MessagingException {
        StringBuffer sb = new StringBuffer();

        try {
            CertStore certs = signed.getCertificatesAndCRLs("Collection", "BC");
            SignerInformationStore signers = signed.getSignerInfos();

            Collection c = signers.getSigners();
            Iterator it = c.iterator();

            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();
                Collection certCollection =
                    certs.getCertificates(signer.getSID());

                Iterator certIt = certCollection.iterator();
                X509Certificate cert = (X509Certificate) certIt.next();

                byte[] encoded = cert.getEncoded();
                byte[] b64encoded = BASE64EncoderStream.encode(encoded);
                
                sb.append("-----BEGIN CERTIFICATE-----");
                sb.append('\n');
                sb.append(new String(b64encoded));
                sb.append('\n');
                sb.append("-----END CERTIFICATE-----");

                return sb.toString();
            }
        }
        catch (CertificateEncodingException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (CertStoreException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (CMSException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (NoSuchAlgorithmException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (NoSuchProviderException e) {
            throw new MessagingException(e.getMessage(), e);
        }

        return null;
    }

    public String getSigner() {
        return subaltnam;
    }

    public boolean isIntact() {
        return intact;
    }

    public boolean isVerified() {
        return verified;
    }

    public void verify() throws IOException, MessagingException {
        try {
            //
            // extract the information to verify the signatures.
            //

            //
            // certificates and crls passed in the signature
            //
            CertStore certs = signed.getCertificatesAndCRLs("Collection", "BC");

            //
            // SignerInfo blocks which contain the signatures
            //
            SignerInformationStore signers = signed.getSignerInfos();

            Collection c = signers.getSigners();
            Iterator it = c.iterator();

            //
            // check each signer
            //
            while (it.hasNext()) {;

            ////////////////////
            ////////////////////
            Collection allcerts = certs.getCertificates(null);
            ArrayList al = new ArrayList();
            Iterator i = allcerts.iterator();

            while (i.hasNext()) {
                X509Certificate x509 = (X509Certificate) i.next();

                // remove the trust anchor
                if (!x509.getSubjectDN().equals(x509.getIssuerDN())) {
                    al.add(x509);
                }
            }

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertPath cp = cf.generateCertPath(al);
            CertPathValidator cpv = CertPathValidator.getInstance("PKIX", "BC");

            FileInputStream fis =
                new FileInputStream(System.getProperty("java.home") +
                "/lib/security/cacerts");
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(fis, "changeit".toCharArray());

            PKIXParameters params = new PKIXParameters(ks);
            
            try {
                PKIXCertPathValidatorResult result =
                    (PKIXCertPathValidatorResult) cpv.validate(cp, params);
                PolicyNode policyTree = result.getPolicyTree();
            }
            catch (CertPathValidatorException cpve) {
                if (cpve.getMessage().indexOf("no valid CRL") != -1) {
                    params.setRevocationEnabled(false);

                    try {
                       cpv.validate(cp, params);
                    }
                    catch (CertPathValidatorException cpve2) {
                        cpve2.printStackTrace();
                        System.out.println("Validation failure, cert["
                            + cpve2.getIndex() + "] :" + cpve2.getMessage());
                    }
                }
                else {
                    cpve.printStackTrace();
                    System.out.println("Validation failure, cert["
                        + cpve.getIndex() + "] :" + cpve.getMessage());
                }
            }
            ////////////////////
            ////////////////////

                SignerInformation signer = (SignerInformation) it.next();
                Collection certCollection =
                    certs.getCertificates(signer.getSID());

                Iterator certIt = certCollection.iterator();
                X509Certificate cert = (X509Certificate) certIt.next();

                //
                // verify that the sig is correct and that it was generated
                // when the certificate was current
                //
                if (signer.verify(cert, "BC")) {
                    Collection sans = cert.getSubjectAlternativeNames();
                    
                    if (sans != null) {
                        Iterator j = sans.iterator();

                        while (j.hasNext()) {
                            subaltnam = (String) ((List) j.next()).get(1);
                        }
                    }

                    intact = true;
                    verified = true;
                    return;
                }
            }
        }
        catch (CertificateException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (CertStoreException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (CMSException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (InvalidAlgorithmParameterException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (KeyStoreException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (NoSuchAlgorithmException e) {
            throw new MessagingException(e.getMessage(), e);
        }
        catch (NoSuchProviderException e) {
            throw new MessagingException(e.getMessage(), e);
        }
    }

}
