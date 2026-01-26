package com.terracetech.tims.mail.sort;

import java.util.*;
import jakarta.mail.internet.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

@SuppressWarnings("unchecked")
public class TIMSENVELOPE implements Item {

    // IMAP item name
    public static char[] name = {'E','N','V','E','L','O','P','E'};
    public int msgno;

    public Date date = null;
    public String subject;
    public InternetAddress[] from;
    public InternetAddress[] sender;
    public InternetAddress[] replyTo;
    public InternetAddress[] to;
    public InternetAddress[] cc;
    public InternetAddress[] bcc;
    public String inReplyTo;
    public String messageId;

    // Used to parse dates
    private static MailDateFormat mailDateFormat = new MailDateFormat();

    public TIMSENVELOPE(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();

        r.skipSpaces();

        if (r.readByte() != '('){        	
            throw new ParsingException("ENVELOPE parse error");
        }

        String s = r.readString();
        if (s != null) {
            try {
                date = mailDateFormat.parse(s);
            } catch (Exception pex) {
                // We need to be *very* tolerant about bogus dates (and
                // there's lot of 'em around), so we ignore any
                // exception (including RunTimeExceptions) and just let
                // date be null.
            }
        }

        subject = r.readString();
        from = parseAddressList(r);
        sender = parseAddressList(r);
        replyTo = parseAddressList(r);
        to = parseAddressList(r);
        cc = parseAddressList(r);
        bcc = parseAddressList(r);
        inReplyTo = r.readString();
        messageId = r.readString();

        if (r.readByte() != ')'){       	
            //throw new ParsingException("ENVELOPE parse error");
        	r.skipSpaces();
        }
    }

    private InternetAddress[] parseAddressList(Response r)
                throws ParsingException {
        r.skipSpaces(); // skip leading spaces

        byte b = r.readByte();        
        if (b == '(') {
            Vector v = new Vector();
            InternetAddress[] a = null;
            boolean isError = false;
            do {
            	try {
            		v.addElement(new IMAPAddress(r));
				} catch (Exception e) {
					isError = true;
					break;
				}
                
            } while (r.peekByte() != ')');
            
            if(!isError){
	            // skip the terminating ')' at the end of the addresslist
	            r.skip(1);            
            }            
            a = new InternetAddress[v.size()];
            v.copyInto(a);
            return a;
        } else if (b == 'N' || b == 'n') { // NIL
            r.skip(2); // skip 'NIL'
            return null;
        } else {        	
        	r.readByte();
        	return null;
        }
    }
}

class IMAPAddress extends InternetAddress {
    IMAPAddress(Response r) throws ParsingException {
        r.skipSpaces(); // skip leading spaces
        
        
    	if (r.readByte() != '(')
            throw new ParsingException("ADDRESS parse error");

        encodedPersonal = r.readString();        

        r.readString(); // throw away address_list
        address = r.readString() + "@" + r.readString();

        if (r.readByte() != ')') // skip past terminating ')'
            throw new ParsingException("ADDRESS parse error");
        
    }
}

