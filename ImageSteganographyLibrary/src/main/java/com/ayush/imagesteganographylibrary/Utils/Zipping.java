package com.ayush.imagesteganographylibrary.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*
This class has methods used to compress and decompress encrypted message.
 */

class Zipping {

    final static String TAG = Zipping.class.getName();

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

        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);

        GZIPInputStream gis = new GZIPInputStream(bis);

        BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.ISO_8859_1));

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        gis.close();
        bis.close();

        return sb.toString();
    }

}
