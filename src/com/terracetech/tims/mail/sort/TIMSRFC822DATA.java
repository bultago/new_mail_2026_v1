package com.terracetech.tims.mail.sort;

import java.io.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

public class TIMSRFC822DATA implements Item {

    public static char [] name = {'R','F','C','8','2','2'};
    public int msgno;
    public ByteArray data;

    /**
     * Constructor
     * @param port      portnumber to connect to
     */
    public TIMSRFC822DATA(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();
        r.skipSpaces();
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

