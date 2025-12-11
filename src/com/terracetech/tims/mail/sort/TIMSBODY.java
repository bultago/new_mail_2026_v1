package com.terracetech.tims.mail.sort;

import java.io.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

public class TIMSBODY implements Item {

    public static char [] name = {'B','O','D','Y'};

    public int msgno;
    public ByteArray data;
    public String section;
    public int origin = 0;

    /**
     * Constructor
     */
    public TIMSBODY(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();

        r.skipSpaces();

        while (r.readByte() != ']') // skip section
            ;

        if (r.readByte() == '<') { // origin
            origin = r.readNumber();
            r.skip(1); // skip '>';
        }

        data = r.readByteArray();
    }

    public ByteArray getByteArray() {
        return data;
    }

    public ByteArrayInputStream getByteArrayInputStream() {
        if (data != null)
            return data.toByteArrayInputStream();
        else
            return null;
    }
}


