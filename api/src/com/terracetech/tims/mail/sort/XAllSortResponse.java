package com.terracetech.tims.mail.sort;

import java.io.*;
import java.util.*;
import org.eclipse.angus.mail.iap.*;
import org.eclipse.angus.mail.imap.protocol.*;

@SuppressWarnings("unchecked")
public class XAllSortResponse extends IMAPResponse {

    private String foldername = null;
    private String[] tagnames = null;
    private String preview = null;
    private Item[] items;   

	public XAllSortResponse(IMAPResponse r)
        throws IOException, ProtocolException {
        super(r);
        parse();    	
	}
	
    public String getFolderName() {
        return foldername;
    }
    
    public String getPriview() {
        return preview;
    }
    
    public String[] getTagNames() {
        return tagnames;
    }

    public int getItemCount() {
	return items.length;
    }

    public Item getItem(int index) {
	return items[index];
    }

    private final static char[] HEADER = {'.','H','E','A','D','E','R'};
    private final static char[] TEXT = {'.','T','E','X','T'};

    private void parse() throws ParsingException {
        skipSpaces();
        foldername = BASE64MailboxDecoder.decode(readString());
        String tname = readAtom();        
        if("x-tag".equalsIgnoreCase(tname))        
        	tagnames = readStringList();
        tname = readAtom(); 
        String[] previewTmp=null;
        if("x-preview".equalsIgnoreCase(tname)){        	
        	previewTmp = readStringList();        	
        	preview = (previewTmp != null)?previewTmp[0]:null;
        }

	skipSpaces();
	if (buffer[index] != '(')
	    throw new ParsingException(
		"error in FETCH parsing, missing '(' at index " + index);

	ArrayList v = new ArrayList();
	Item i = null;
	do {
	    index++; // skip '(', or SPACE

	    if (index >= size)
		throw new ParsingException(
		"error in FETCH parsing, ran off end of buffer, size " + size);

	    switch(buffer[index]) {
	    case 'E': 
		if (match(TIMSENVELOPE.name)) {
		    index += TIMSENVELOPE.name.length; // skip "ENVELOPE"
		    try {
		    	i = new TIMSENVELOPE(this);
			} catch (ParsingException e) {}		    
		}
		break;
	    case 'F': 
		if (match(FLAGS.name)) {
		    index += FLAGS.name.length; // skip "FLAGS"
		    i = new FLAGS((IMAPResponse)this);
		}
		break;
	    case 'I': 
		if (match(TIMSINTERNALDATE.name)) {
		    index += TIMSINTERNALDATE.name.length; // skip "INTERNALDATE"
		    i = new TIMSINTERNALDATE(this);
		}
		break;
	    case 'B': 
		if (match(TIMSBODY.name)) {
		    if (buffer[index+4] == '[') {
			index += TIMSBODY.name.length; // skip "BODY"
			i = new TIMSBODY(this);
		    }
		    else {
			if (match(BODYSTRUCTURE.name))
			    index += TIMSBODYSTRUCTURE.name.length;
			    // skip "BODYSTRUCTURE"
			else
			    index += BODY.name.length; // skip "BODY"
			i = new TIMSBODYSTRUCTURE(this);
		    }
		}
		break;
	    case 'R':
		if (match(RFC822SIZE.name)) {
		    index += TIMSRFC822SIZE.name.length; // skip "RFC822.SIZE"
		    i = new TIMSRFC822SIZE(this);
		}
		else {
		    if (match(RFC822DATA.name)) {
			index += TIMSRFC822DATA.name.length;
			if (match(HEADER))
			    index += HEADER.length; // skip ".HEADER"
			else if (match(TEXT))
				index += TEXT.length; // skip ".TEXT"
			i = new TIMSRFC822DATA(this);
		    }
		}
		break;
	    case 'U': 
		if (match(UID.name)) {
		    index += TIMSUID.name.length;
		    i = new TIMSUID(this);
		}
		break;
	    default: 
	    }
	    if (i != null)
		v.add(i);
	}
        while (buffer[index] != ')');

	index++; // skip ')'
	items = new Item[v.size()];
	v.toArray(items);
    }

    private boolean match(char[] itemName) {
	int len = itemName.length;
	for (int i = 0, j = index; i < len;) {
	    if (Character.toUpperCase((char)buffer[j++]) != itemName[i++]) {
		return false;
            }
        }
	return true;
    }
}

