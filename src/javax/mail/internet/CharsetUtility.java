package javax.mail.internet;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
 
import com.daou.charset.ConvertUniJisUtil;
import com.ibm.icu.charset.CharsetDecoderICU;
import com.ibm.icu.charset.CharsetEncoderICU;
import com.ibm.icu.charset.CharsetICU;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
 
public class CharsetUtility {
 
        public static byte[] convertStrToByte(String str, String charset) throws CharacterCodingException, UnsupportedEncodingException {
                ByteBuffer bb = null;
       Charset cs = CharsetICU.forNameICU(charset);
       CharsetEncoderICU encoder = (CharsetEncoderICU) cs.newEncoder();
       if (!encoder.canEncode(str)) {
               CharsetDetector cd = new CharsetDetector();
               if ("gb2312".equalsIgnoreCase(charset)) {
                       cd.setText(str.getBytes());
                       cs = CharsetICU.forNameICU("GB18030");   
                       encoder = (CharsetEncoderICU) cs.newEncoder();
                       if (encoder.canEncode(str)) {
                               bb = encoder.encode(CharBuffer.wrap(str));
                       }
               }
               else {
                       bb = ByteBuffer.wrap(str.getBytes(charset));
               }
                } else {                        
                        bb = encoder.encode(CharBuffer.wrap(str));
                }
       byte[] data = new byte[bb.remaining()];
       bb.get(data, 0, data.length);
       bb.clear();
       
       return data;
        }
        
        public static String convertByteToStr(byte[] data, String charset) throws IOException {
                String str = null;
                CharsetDetector cd = new CharsetDetector();
                
                int limitLength = (data.length > 1024 * 7) ? 1024 * 7 : data.length;
                byte[] searchByte = new byte[limitLength];
                System.arraycopy(data, 0, searchByte, 0, limitLength);
                cd.setText(searchByte);
                cd.enableInputFilter(true);
                CharsetMatch cm = cd.detect();
                if ((!cm.getName().equalsIgnoreCase(charset)) && "gb2312".equalsIgnoreCase(charset)) {
                        boolean isFind = false;
                        for (CharsetMatch cm1 : cd.detectAll()) {
                            if (!"gb18030".equalsIgnoreCase(cm1.getName())) {
                                continue;
                            }
                            Charset cs = CharsetICU.forNameICU("GB18030");
                            CharsetDecoderICU decoder = (CharsetDecoderICU) cs.newDecoder();
                            CharBuffer cb = decoder.decode(ByteBuffer.wrap(data));
                            str = cb.toString();
                            isFind = true;
                            break;
                        }
                        if (!isFind) {
                            str = new String(data, charset);
                        }
                } else {
                    if ("JISAutoDetect".equals(charset) || "iso-2022-jp".equalsIgnoreCase(charset)) {
                        try {
                            str = ConvertUniJisUtil.fromJIS(data);
                        } catch (Exception e) {
                            str = convertDefaultCase(data, charset);
                        }
                    } else {
                        str = convertDefaultCase(data, charset);
                    }
                }
                return str;
        }
        
        public static String convertDefaultCase(byte[] data, String charset) throws IOException {
            char buf[] = new char[1024 * 8];
           InputStreamReader is = null;
           
           try {
                   is = new InputStreamReader(new ByteArrayInputStream(data), charset);
           } catch (UnsupportedEncodingException e) {
                   is = new InputStreamReader(new ByteArrayInputStream(data), MimeUtility.getDefaultJavaCharset());
           }
           int count;
           StringBuffer sb = new StringBuffer();

           while ((count = is.read(buf, 0, buf.length)) != -1) {
                   sb.append(buf, 0, count);
           }
           return sb.toString();
        }
        
        public static String convertByteToStr(InputStream in, String charset) throws IOException {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int count;
                byte buf[] = new byte[1024 * 8];
                while ((count = in.read(buf, 0, buf.length)) != -1) {
                        bos.write(buf, 0, count);
                }
                 return convertByteToStr(bos.toByteArray(), charset);
        }
}
