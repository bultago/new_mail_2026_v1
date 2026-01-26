package com.terracetech.tims.mail;

import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@SuppressWarnings("all")
public class MessageComparator implements Comparator{
    protected int sortCol;
    protected boolean sortAsc;

    public MessageComparator(int col, boolean asc){
        sortCol = col;
        sortAsc = asc;
    }

    public int compare(Object o1, Object o2){
		TMailMessage msg1 =(TMailMessage)o1;
		TMailMessage msg2 =(TMailMessage)o2;

        int result = 0;
		try{
        switch(sortCol){
			// subject
            case 0:
                String str1 = msg1.getSubject();
                String str2 = msg2.getSubject();
				if(str1 == null )
					result = -1;
				else if (str2 == null)
					result = 1;
				else 
                	result = str1.compareTo(str2);
                break;
			// sender
            case 1:
				InternetAddress[] from1 = msg1.getFrom();
				InternetAddress[] from2 = msg2.getFrom();
				str1 = from1[0].toUnicodeString();
				str2 = from2[0].toUnicodeString();	
                result = str1.compareTo(str2);
                break;
			//recdate
            case 3:
                Date date1 = msg1.getReceivedDate();
                Date date2 = msg2.getReceivedDate();
                result = (date1.before(date2)) ? -1 : 1;
                break;
			//size
            case 4:
                int size1 = msg1.getSize();
                int size2 = msg2.getSize();
                result = size1 - size2;
                break;
			// folder
            case 5:
                str1 = (msg1.getFolder()).getName();
                str2 = (msg2.getFolder()).getName();
                result = str1.compareTo(str2);
                break;
        }
		}catch(MessagingException e){}

    if (!sortAsc)
        result = -result;
    return result;
    }
}
