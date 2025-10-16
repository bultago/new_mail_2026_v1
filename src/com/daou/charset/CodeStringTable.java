package com.daou.charset;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

// char => String
public class CodeStringTable {
    char[] codes;
    String[] texts;
    int count;

    static int readChar(InputStream i) throws IOException {
        int a = i.read();
        int b = i.read();
        return ((a & 255) << 8) + (b & 255);
    }

    void load(String file) throws IOException {
        InputStream is = new BufferedInputStream(ConvertUniJisUtil.OpenFile(file));
        count = readChar(is);
        codes = new char[count];
        texts = new String[count];
        for (int i = 0; i < count; ++i) {
            codes[i] = (char) readChar(is);
            int length = is.read();
            byte[] tmp = new byte[length];
            int offset = 0;
            while (length > 0) {
                int delta = is.read(tmp, offset, length);
                if (delta > 0) {
                    length -= delta;
                    offset += delta;
                }
            }
            texts[i] = new String(tmp, "UTF-8");
        }
        is.close();
    }

    String find(int key) {
        int low = 0;
        int width = count;
        int mid;
        int r;
        while (width > 0) {
            mid = (width >> 1);
            int sn = codes[low + mid];
            if (0 == (r = key - sn)) {
                return texts[low + mid];
            }
            if (r < 0) {
                width = mid;
            } else {
                low += ++mid;
                width -= mid;
            }
        }
        return null;
    }
};
