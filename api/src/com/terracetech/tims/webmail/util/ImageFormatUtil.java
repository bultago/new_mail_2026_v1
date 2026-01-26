package com.terracetech.tims.webmail.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageFormatUtil {

	// Returns the format of the image in the file 'f'.
    // Returns null if the format is not known.
    public static String getFormatInFile(File f) {
        return getFormatName(f);
    }
    
    // Returns the format of the image in the input stream 'is'.
    // Returns null if the format is not known.
    public static String getFormatFromStream(InputStream is) {
        return getFormatName(is);
    }
    
    // Returns the format name of the image in the object 'o'.
    // 'o' can be either a File or InputStream object.
    // Returns null if the format is not known.
    private static String getFormatName(Object o) {
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);
    
            // Find all image readers that recognize the image format
            Iterator iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }
    
            // Use the first reader
            ImageReader reader = (ImageReader)iter.next();
    
            // Close stream
            iis.close();
    
            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
        }
        // The image could not be read
        return null;
    }
}
