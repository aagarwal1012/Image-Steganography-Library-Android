package com.ayush.steganographylibrary.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*
This class has methods used to compress and decompress encrypted message.
 */

public class Zipping {

    /*
    @parameter : Encrypted message {String}
    @return : Compressed byte array
     */

    public static byte[] compress(String string) throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());

        GZIPOutputStream gos = new GZIPOutputStream(os);

        gos.write(string.getBytes());
        gos.close();

        byte[] compressed = os.toByteArray();
        os.close();

        return compressed;
    }


    /*
    @parameter : byte array
    @return : Uncompressed encrypted_message {String}
     */
    public static String decompress(byte[] compressed) throws Exception {

        final int BUFFER_SIZE = 32;

        ByteArrayInputStream is = new ByteArrayInputStream(compressed);

        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);

        StringBuilder string = new StringBuilder();

        byte[] data = new byte[BUFFER_SIZE];

        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }

        gis.close();
        is.close();

        return string.toString();
    }

}
