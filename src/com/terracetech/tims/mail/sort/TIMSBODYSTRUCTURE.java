package com.terracetech.tims.mail.sort;

import java.util.*;
import jakarta.mail.internet.ParameterList;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

@SuppressWarnings("unchecked")
public class TIMSBODYSTRUCTURE implements Item {

    public static char [] name = {'B','O','D','Y','S','T','R','U','C','T','U','R','E'};
    public int msgno;

    public String type;         // Type
    public String subtype;      // Subtype
    public String encoding;     // Encoding
    public int lines = -1;      // Size in lines
    public int size = -1;       // Size in bytes
    public String disposition;  // Disposition
    public String id;           // Content-ID
    public String description;  // Content-Description
    public String md5;          // MD-5 checksum
    public String attachment;   // Attachment name
    public ParameterList cParams; // Body parameters
    public ParameterList dParams; // Disposition parameters
    public String[] language;   // Language
    public TIMSBODYSTRUCTURE[] bodies; // array of BODYSTRUCTURE objects
                                   //  for multipart & message/rfc822
    public TIMSENVELOPE envelope;   // for message/rfc822

    private static int SINGLE   = 1;
    private static int MULTI    = 2;
    private static int NESTED   = 3;
    private int processedType;  // MULTI | SINGLE | NESTED

    public TIMSBODYSTRUCTURE(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();

        r.skipSpaces();

        if (r.readByte() != '(')
            throw new ParsingException(
                "BODYSTRUCTURE parse error: missing ``('' at start");

        if (r.peekByte() == '(') { // multipart
            type = "multipart";
            processedType = MULTI;
            ArrayList v = new ArrayList(1);            
            do {
                v.add(new TIMSBODYSTRUCTURE(r));
            } while (r.peekByte() == '(');

            // setup bodies.
            bodies = new TIMSBODYSTRUCTURE[v.size()];
            v.toArray(bodies);

            subtype = r.readString(); // subtype                        

            if (r.readByte() == ')') // done
                return;

            // Else, we have extension data

            // Body parameters
            cParams = parseParameters(r);
            if (r.readByte() == ')') // done
                return;

            // Disposition
            byte b = r.readByte();
            if (b == '(') {
               disposition = r.readString();
                dParams = parseParameters(r);
                if (r.readByte() != ')') // eat the end ')'
                    throw new ParsingException(
                        "BODYSTRUCTURE parse error: " +
                        "missing ``)'' at end of disposition in multipart");
            } else if (b == 'N' || b == 'n')
                r.skip(2); // skip 'NIL'

            // Language is required, unlike single part
            if (r.readByte() != ' ')
                throw new ParsingException(
                    "BODYSTRUCTURE parse error: " +
                    "missing space after disposition");

            // Language
            if (r.peekByte() == '(') // a list follows
                language = r.readStringList();
            else {
                String l = r.readString();
                if (l != null) {
                    String[] la = { l };
                    language = la;
                }
            }

            // Throw away any further extension data
            while (r.readByte() == ' ')
                parseBodyExtension(r);
        }
        else { // Single part
            type = r.readString();
            processedType = SINGLE;
            subtype = r.readString();

            // SIMS 4.0 returns NIL for a Content-Type of "binary", fix it here
            if (type == null) {
                type = "application";
                subtype = "octet-stream";
            }
            cParams = parseParameters(r);
            id = r.readString();
            description = r.readString();
            encoding = r.readString();
            size = r.readNumber();
            if (size < 0)
                throw new ParsingException(
                            "BODYSTRUCTURE parse error: bad ``size'' element");

            // "text/*" & "message/rfc822" types have additional data ..
            if (type.indexOf("text") > -1) {
                lines = r.readNumber();
                if (lines < 0)
                    throw new ParsingException(
                            "BODYSTRUCTURE parse error: bad ``lines'' element");
            } else if (type.equalsIgnoreCase("message") &&
                     subtype.equalsIgnoreCase("rfc822")) {
                // Nested message
                processedType = NESTED;
                envelope = new TIMSENVELOPE(r);
                TIMSBODYSTRUCTURE[] bs = { new TIMSBODYSTRUCTURE(r) };
                bodies = bs;
                lines = r.readNumber();
                if (lines < 0)
                    throw new ParsingException(
                            "BODYSTRUCTURE parse error: bad ``lines'' element");
            }

            if (r.readByte() == ')')
                return; // done

            // Optional extension data

            // MD5
            md5 = r.readString();
            if (r.readByte() == ')')
                return; // done

            // Disposition
            byte b = r.readByte();
            if (b == '(') {
                disposition = r.readString();
                dParams = parseParameters(r);
                if (r.readByte() != ')') // eat the end ')'
                    throw new ParsingException(
                        "BODYSTRUCTURE parse error: " +
                        "missing ``)'' at end of disposition");
            } else if (b == 'N' || b == 'n')
                r.skip(2); // skip 'NIL'

            if (r.readByte() == ')')
                return; // done

            // Language
            if (r.peekByte() == '(') // a list follows
                language = r.readStringList();
            else { // protocol is unnessarily complex here
                String l = r.readString();
                if (l != null) {
                    String[] la = { l };
                    language = la;
                }
            }

            // Throw away any further extension data
            while (r.readByte() == ' ')
                parseBodyExtension(r);
        }
    }

    public boolean isMulti() {
        return processedType == MULTI;
    }

    public boolean isSingle() {
        return processedType == SINGLE;
    }

    public boolean isNested() {
        return processedType == NESTED;
    }

    private ParameterList parseParameters(Response r)
                        throws ParsingException {
        r.skipSpaces();

        ParameterList list = null;
        byte b = r.readByte();
        if (b == '(') {
            list = new ParameterList();
            do {
                String name = r.readString();
                String value = r.readString();                
                list.set(name, value);
            } while (r.readByte() != ')');
        } else if (b == 'N' || b == 'n')
            r.skip(2);
        else
            throw new ParsingException("Parameter list parse error");

        return list;
    }

    private void parseBodyExtension(Response r) throws ParsingException {
        r.skipSpaces();

        byte b = r.peekByte();
        if (b == '(') {
            r.skip(1); // skip '('
            do {
                parseBodyExtension(r);
            } while (r.readByte() != ')');
        } else if (Character.isDigit((char)b)) // number
            r.readNumber();
        else // nstring
            r.readString();
    }
}

