package com.terracetech.tims.webmail.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	/**
     * Reader source of the CSV fields to be read.
     */
	private BufferedReader reader;
	/**
	 * quote character, usually '\"' '\'' for SOL used to enclose fields
	 * containing a separator character.
	 */
	private char quoteChar;

	/**
	 * field separator character, usually ',' in North America, ';' in Europe
	 * and sometimes '\t' for tab.
	 */
	private char separatorChar;

	public CSVReader(final Reader r, final char separatorChar, final char quoteChar) {
		
		/* default buffer size is 8K */
		this.reader = ( r instanceof BufferedReader ) ? ( BufferedReader ) r : new BufferedReader( r );    
		this.separatorChar = separatorChar;
		this.quoteChar = quoteChar;
	}
	
	public String[] getAllFieldsInLine() throws IOException{
		List<String> list = new ArrayList<String>();
		
		String line = "";
		StringBuffer sb = null;
		while ((line = reader.readLine()) != null) {
			if(line.length()!=0){
				if(line.indexOf(quoteChar) > 0 || line.charAt(0)== quoteChar){
					
					while (line.indexOf(quoteChar) > 0 || line.charAt(0)== quoteChar) {
						sb = new StringBuffer();

						int startPos = line.indexOf(quoteChar);
						int endPos = line.indexOf(quoteChar, startPos + 1);
						if(endPos <0){
							line = StringUtils.replace(line, String.valueOf(separatorChar), "");
							sb.append(StringUtils.replace(line, String.valueOf(quoteChar), ""));
						}else{
							String middleStr = line.substring(startPos + 1, endPos);
							sb.append(line.substring(0, startPos));
							sb.append(StringUtils.replace(middleStr, String.valueOf(separatorChar), ""));
							sb.append(line.substring(endPos+1, line.length()));	
						}
						line = sb.toString();
					}
					list.add(line);	
				}else{
					list.add(line);	
				}
			}
			
			sb = null;
		}
		
		return list.toArray(new String[list.size()]);
	}

	public void close() {
		try {
			reader.close();
		} catch (Exception e) {
		}
	}
}
