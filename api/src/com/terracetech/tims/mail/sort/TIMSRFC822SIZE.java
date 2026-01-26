package com.terracetech.tims.mail.sort;

import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

public class TIMSRFC822SIZE implements Item {

    public static char [] name = {'R','F','C','8','2','2','.','S','I','Z','E'};
    public int msgno;

    public int size;

    /**
     * Constructor
     * @param port      portnumber to connect to
     */
    public TIMSRFC822SIZE(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();
        r.skipSpaces();
        size = r.readNumber();
    }
}

