package com.terracetech.tims.mail.sort;

import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

public class TIMSUID implements Item {

    public final static char [] name = {'U','I','D'};
    public int msgno;

    public long uid;

    public TIMSUID(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();
        r.skipSpaces();
        uid = r.readLong();
    }
}