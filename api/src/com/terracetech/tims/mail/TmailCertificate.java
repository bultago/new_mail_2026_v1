package com.terracetech.tims.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;

public class TmailCertificate {

    private X509Certificate certificate = null;

    public TmailCertificate(byte[] certData) throws CertificateException,
        IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(certData);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        certificate = (X509Certificate) cf.generateCertificate(bais);
        bais.close();
    }

    public int getBasicConstraints() {
        return certificate.getBasicConstraints();
    }

    public String getIssuer() {
        return certificate.getIssuerDN().getName();
    }

    public String getNotAfter() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy.MM.dd EEEE a h:mm:ss");

        return sdf.format(certificate.getNotAfter());
    }

    public String getNotBefore() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy.MM.dd EEEE a h:mm:ss");

        return sdf.format(certificate.getNotBefore());
    }

    public String getPublicKey() {
        String sigalg = certificate.getSigAlgName();
        int i = sigalg.indexOf("with");

        return sigalg.substring(i + 4);
    }

    public String getSerialNumber() {
        return certificate.getSerialNumber().toString();
    }

    public String getSigAlgName() {
        return certificate.getSigAlgName();
    }

    public String getSubject() {
        return certificate.getSubjectDN().getName();
    }

    public String getSubjectAlternativeNames()
        throws CertificateParsingException {

        StringBuffer sb = new StringBuffer();
        Collection c = certificate.getSubjectAlternativeNames();
        Iterator i = c.iterator();

        while (i.hasNext()) {
            sb.append(((List) i.next()).get(1));
        }

        return sb.toString();
    }

    public String getVersion() {
        return "V" + certificate.getVersion();
    }

}
