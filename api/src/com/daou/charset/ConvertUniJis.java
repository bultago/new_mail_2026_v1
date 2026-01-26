package com.daou.charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ConvertUniJis {
    private static ByteBuffer decodejis_bb = ByteBuffer.allocate(16);
    private static byte[] jis212_escape = new byte[] { 0x1b, '$', 'D' };
    private static final byte[] ary_BrokenJISToX0213_2 = new byte[] { 1, 8, 3, 4, 5, 12, 13, 14, 15, 78, 79, 80, 81,
        82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, };
    public static char[] ary_JIS208ToUnicode = new char[65536];
    static CharsetDecoder jis212_decorder;
    static CharsetDecoder x201_decorder;
    static CodeStringTable j2safe = new CodeStringTable();

    static int FirstCall = 0;

    public static void Setup() throws IOException {
        if (FirstCall == 0) {
            FirstCall = 1;

            j2safe.load("/META-INF/j2safe.dat");

            for (int i = 0; i < ary_JIS208ToUnicode.length; ++i) {
                ary_JIS208ToUnicode[i] = 0;
            }
            Reader isr = new BufferedReader(new InputStreamReader(ConvertUniJisUtil.OpenFile("/META-INF/j2u208.dat"),
                    "UTF-16BE"));
            for (;;) {
                int c1 = isr.read();
                int c2 = isr.read();
                if (c1 == -1 || c2 == -1) {
                    break;
                }
                ary_JIS208ToUnicode[c1] = (char) c2;
            }
            isr.close();
          
            try {
                x201_decorder = Charset.forName("JIS0201").newDecoder();
            } catch (Throwable e) {}
        }
    }

    private final static void decodeX201(StringBuffer sb, int b, char mode, boolean shift_out) {
        b &= 255;

        if (b < 0x80) {
            if (mode == 'J' && !shift_out) {
                sb.append((char) b);
                return;
            }
        }

        if (x201_decorder != null) {
            decodejis_bb.clear();
            decodejis_bb.put((byte) (b | 0x80));
            decodejis_bb.flip();
            try {
                CharBuffer cb = x201_decorder.decode(decodejis_bb);
                sb.append(cb.array(), cb.position(), cb.remaining());
                return;
            } catch (Throwable e) {
            }
        }
        sb.append("(x201%" + Integer.toHexString(b) + ")");
    }

    private final static void decodeJIS212(StringBuffer sb, byte b1, byte b2) {
        if (jis212_decorder != null) {
            decodejis_bb.clear();
            decodejis_bb.put(jis212_escape);
            decodejis_bb.put(b1);
            decodejis_bb.put(b2);
            decodejis_bb.flip();
            try {
                CharBuffer cb = jis212_decorder.decode(decodejis_bb);
                String s = new String(cb.array(), cb.position(), cb.remaining());
                if (s != null && s.length() > 0) {
                    sb.append(s);
                    return;
                }
            } catch (Throwable e) {
            }
        }
        sb.append("(jis212%" + Integer.toHexString(b1 & 255) + "%" + Integer.toHexString(b2 & 255) + ")");
    }

    public static String JISToUnicode(byte[] ba) {
        return JISToUnicode(ba, 0, ba.length);
    }

    public static String JISToUnicode(byte[] ba, int arg_start, int end) {
        int i = arg_start;
        StringBuffer sb = new StringBuffer();

        LOOP: while (i < end) {
            int b, b2;
            char u;
            while (i < end) {
                b = 255 & ba[i];
                if (b == 0x1B) {
                    break;
                }
                ++i;
                if (b < 0x80) {
                    sb.append((char) b);
                    continue;
                } // ascii
                if (b >= 0xa1 && b <= 0xdf) {
                    decodeX201(sb, b, 'b', false);
                    continue;
                } // x201

                // sjis ?
                if (i < end) {
                    b2 = 255 & ba[i]; // sjis trail byte
                    if (b >= 0x81 && b <= 0xfc && b2 >= 0x40 && b2 <= 0xfc && b2 != 0x7f) {
                        ++i;
                        int jis = (b << 9) + (b <= 0x9f ? -(0xe1 << 8) : -(0x161 << 8)) + b2
                                + (b2 <= 0x7e ? -0x1f : b2 <= 0x9e ? -0x20 : 256 - 0x7e);
                        u = ary_JIS208ToUnicode[jis];
                        if (u != 0) {
                            sb.append(u);
                        } else {
                            String s = j2safe.find(jis);
                            if (s != null) {
                                sb.append(s);
                            } else {
                                sb.append("(SJIS-0x" + Integer.toHexString(b) + Integer.toHexString(b2) + ")");
                            }
                        }
                        continue;
                    }
                }

                // control char
                sb.append("%" + Integer.toHexString(b));
            }

            int maxescape = end - 3;
            int page = 0;
            for (; i < end; ++i) {
                switch (ba[i]) {
                case 0x20:
                    page = 0;
                    continue LOOP;

                case 0x0E: // shift out
                    if (page == 72010) {
                        page = 72011;
                        continue;
                    }
                    if (page == 82010) {
                        page = 82011;
                        continue;
                    }
                    page = 0;
                    continue LOOP;

                case 0x0F: // shift in
                    if (page == 72011) {
                        page = 72010;
                        continue;
                    }
                    if (page == 82011) {
                        page = 82010;
                        continue;
                    }
                    page = 0;
                    continue LOOP;

                case 0x1B:
                    if (i <= maxescape) {
                        switch (ba[i + 1]) {
                        case '(':
                            switch (ba[i + 2]) {
                            case 'B':
                                page = 0;
                                i += 2;
                                continue; // ASCII ESC (B
                            case 'I':
                                page = 72010;
                                i += 2;
                                continue; // JIS X 0201
                            case 'J':
                                page = 82010;
                                i += 2;
                                continue; // JIS X0201(LH) ESC (J
                            }
                            break;
                        case '$':
                            switch (ba[i + 2]) {
                            case '@':
                                page = 2131;
                                i += 2;
                                continue; // ESC $@ JIS X 0213��1(include JIS
                                          // X0208('78))
                            case 'B':
                                page = 208;
                                i += 2;
                                continue; // JIS X0208('83)
                            case 'I':
                                page = 201;
                                i += 2;
                                continue; // 8bit
                            case '(':
                                if (i < maxescape) {
                                    switch (ba[i + 3]) {
                                    case 'O':
                                        page = 2131;
                                        i += 3;
                                        continue; // JIS X 0213 1
                                    case 'P':
                                        page = 2132;
                                        i += 3;
                                        continue; // JIS X 0213 2
                                    case 'D':
                                        page = 212;
                                        i += 3;
                                        continue; // JIS X0212
                                    }
                                }
                                break;
                            }
                            break;
                        }
                    }
                    sb.append(ba[i++]);

                    continue LOOP;
                }
                switch (page) {
                default:
                    continue LOOP;

                case 201:
                    decodeX201(sb, ba[i], ' ', false);
                    continue;
                    // roman ESC (J
                case 82010:
                    decodeX201(sb, ba[i], 'J', false);
                    continue;
                case 82011:
                    decodeX201(sb, ba[i], 'J', true);
                    continue;
                    // junet ESC (I
                case 72010:
                    decodeX201(sb, ba[i], 'I', false);
                    continue;
                case 72011:
                    decodeX201(sb, ba[i], 'I', true);
                    continue;

                case 2131: // 0213-1
                case 208: // 0213-1 -> 0208
                {
                    int c1, c2;
                    if (i == end - 1 || (c1 = (255 & ba[i])) <= 0x20 || (c2 = (255 & ba[i + 1])) <= 0x20) {
                        continue LOOP;
                    }

                    int jis = c2 | (c1 << 8);
                    u = ary_JIS208ToUnicode[jis];
                    if (u != 0) {
                        sb.append(u);
                    } else {
                        String s = j2safe.find(jis);
                        if (s != null) {
                            sb.append(s);
                        } else {
                            sb.append("(JIS1��-0x" + Integer.toHexString(c2 | (c1 << 8)) + ")");
                        }
                    }
                }
                    ++i;
                    continue;
                case 2132: // 0213-2
                {
                    int c1, c2;
                    if (i == end - 1 || (c1 = (255 & ba[i])) <= 0x20 || (c2 = (255 & ba[i + 1])) <= 0x20) {
                        continue LOOP;
                    }

                    int k = -1;
                    for (int j = 0; j < ary_BrokenJISToX0213_2.length; ++j) {
                        if (ary_BrokenJISToX0213_2[j] == c1 - 0x20) {
                            k = j + 0x7f;
                            break;
                        }
                    }
                    if (k == -1) {
                        continue LOOP;
                    }

                    u = ary_JIS208ToUnicode[c2 | (k << 8)];
                    if (u != 0) {
                        sb.append(u);
                    } else {
                        sb.append("(JIS-0x" + Integer.toHexString(c1) + Integer.toHexString(c2) + ")");
                    }
                }
                    ++i;
                    continue;

                case 212:
                    if (i == end - 1 || ba[i] <= 0x20) {
                        continue LOOP;
                    }
                    decodeJIS212(sb, ba[i], ba[i + 1]);
                    ++i;
                    continue;
                }
            }
        }
        return sb.toString();
    }
}
