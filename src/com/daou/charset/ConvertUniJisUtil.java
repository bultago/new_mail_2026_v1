package com.daou.charset;
import java.io.*;

public class ConvertUniJisUtil{
        public static InputStream OpenFile(String fname) throws IOException,FileNotFoundException {
            InputStream result=null;
            try{
                result= new FileInputStream(new File(fname));
            }catch(FileNotFoundException e){
            }
            if(result==null)
                result = ConvertUniJisUtil.class.getResourceAsStream(fname);
            if(result!=null) return result;
            throw new java.io.FileNotFoundException(fname+"はディレクトリにもリソースにも含まれていない");
        }

        public static String fromJIS(byte[] src) { 
            return fromJIS(src,0,src.length); 
        }

        public static String fromJIS(byte[] src,int start,int end) { 
            return ConvertUniJis.JISToUnicode(src,start,end); 
        }
}
