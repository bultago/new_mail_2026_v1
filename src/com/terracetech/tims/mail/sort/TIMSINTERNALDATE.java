package com.terracetech.tims.mail.sort;

import java.util.*;
import java.text.*;
import jakarta.mail.internet.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

public class TIMSINTERNALDATE implements Item {

    public static char [] name = {'I','N','T','E','R','N','A','L','D','A','T','E'};
    public int msgno;
    protected Date date;

    // Used to parse dates
    private static MailDateFormat mailDateFormat = new MailDateFormat();

    /**
     * Constructor
     */
    public TIMSINTERNALDATE(XAllSortResponse r) throws ParsingException {
        msgno = r.getNumber();
        r.skipSpaces();
        String s = r.readString();
        try {
            date = mailDateFormat.parse(s);
        } catch (java.text.ParseException pex) {
            throw new ParsingException("INTERNALDATE parse error");
        }
    }

    public Date getDate() {
        return date;
    }

    // INTERNALDATE formatter

    private static SimpleDateFormat df =
        // Need Locale.US, the "MMM" field can produce unexpected values
        // in non US locales !
        new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss ", Locale.US);
    private static String tz_offset; // tz offset in INTERNALDATE format

    // compute offset string, cache it.
    static {
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset(); // get raw offset from GMT
        StringBuffer sb = new StringBuffer();
        if (offset < 0) {
            sb.append('-');
            offset = (-offset);
        } else
            sb.append('+');

        int rawOffsetInMins = offset / 60 / 1000; // offset from GMT in mins
        int offsetInHrs = rawOffsetInMins / 60;
        int offsetInMins = rawOffsetInMins % 60;

        sb.append(Character.forDigit((offsetInHrs/10), 10));
        sb.append(Character.forDigit((offsetInHrs%10), 10));
        sb.append(Character.forDigit((offsetInMins/10), 10));
        sb.append(Character.forDigit((offsetInMins%10), 10));

        tz_offset = sb.toString();
    }

    /**
     * Format given Date object into INTERNALDATE string
     */
    public static String format(Date d) {
        StringBuffer sb = new StringBuffer();
        df.format(d, sb, new FieldPosition(0));
        sb.append(tz_offset); // append TZ offset
        return sb.toString();
    }
}

