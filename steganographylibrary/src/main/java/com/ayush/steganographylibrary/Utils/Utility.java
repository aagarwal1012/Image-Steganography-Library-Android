package com.ayush.steganographylibrary.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {

    private final static String TAG=Utility.class.getName();
    public static final int SQUARE_BLOCK = 512;


    public static File createImageFile(Context context) throws IOException{
        // Create an image file name
        String mCurrentPhotoPath=null;
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ITALY).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(
        //      Environment.DIRECTORY_PICTURES);
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =  image.getAbsolutePath();
        Log.d(TAG,"Image file created in "+mCurrentPhotoPath);
        return image;
    }

    public static int squareBlockNeeded(int pixels) {
        int result = 0;
        int quadratic = SQUARE_BLOCK * SQUARE_BLOCK;
        int divisor = pixels / (quadratic);
        int remainder = pixels % (quadratic);
        result = divisor + (remainder > 0 ? 1 : 0);
        return result;
    }

    public static List<Bitmap> splitImage(Bitmap bitmap) {

        //For the number of rows and columns of the grid to be displayed


        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>();


        int rows = bitmap.getHeight() / SQUARE_BLOCK;
        int cols = bitmap.getWidth() / SQUARE_BLOCK;

        int chunkH_mod = bitmap.getHeight() % SQUARE_BLOCK;
        int chunkW_mod = bitmap.getWidth() % SQUARE_BLOCK;


        if (chunkH_mod > 0)
            rows++;
        if (chunkW_mod > 0)
            cols++;


        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int x = 0; x < rows; x++) {
            int xCoord = 0;
            for (int y = 0; y < cols; y++) {
                chunkHeight = SQUARE_BLOCK;
                chunkWidth = SQUARE_BLOCK;

                if (y == cols - 1 && chunkW_mod > 0)
                    chunkWidth = chunkW_mod;

                if (x == rows - 1 && chunkH_mod > 0)
                    chunkHeight = chunkH_mod;

                chunkedImages.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += SQUARE_BLOCK;
            }
            yCoord += SQUARE_BLOCK;
        }


        return chunkedImages;
    }

    public static Bitmap mergeImage(List<Bitmap> images, int originalHeight, int originalWidth) {

        int rows = originalHeight / SQUARE_BLOCK;
        int cols = originalWidth / SQUARE_BLOCK;

        int chunkH_mod = originalHeight % SQUARE_BLOCK;
        int chunkW_mod = originalWidth % SQUARE_BLOCK;


        if (chunkH_mod > 0)
            rows++;
        if (chunkW_mod > 0)
            cols++;

        //create a bitmap of a size which can hold the complete image after merging
        Log.d(TAG, "Size width " + originalWidth + " size height " + originalHeight);
        Bitmap bitmap = Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        int count = 0;
        int chunkWidth = SQUARE_BLOCK;
        int chunkHeight = SQUARE_BLOCK;

        for (int irows = 0; irows < rows; irows++) {
            for (int icols = 0; icols < cols; icols++) {

                canvas.drawBitmap(images.get(count), (chunkWidth * icols), (chunkHeight * irows), null);
                count++;

            }
        }

        return bitmap;
    }

    /**
     * Convert the byte array to an int array.
     *
     * @param b The byte array.
     * @return The int array.
     */

    public static int[] byteArrayToIntArray(byte[] b) {
        Log.v("Size byte array", b.length + "");
        int size = b.length / 3;
        Log.v("Size Int array", size + "");
        System.runFinalization();
        System.gc();
        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int off = 0;
        int index = 0;
        while (off < b.length) {
            result[index++] = byteArrayToInt(b, off);
            off = off + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @param b The byte array
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @param b      The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0x00000000;
        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }
        value = value & 0x00FFFFFF;
        return value;
    }

    /**
     * Convert integer array representing [argb] values to byte array
     * representing [rgb] values
     *
     * @param array Integer array representing [argb] values.
     * @return byte Array representing [rgb] values.
     */

    public static byte[] convertArray(int[] array) {
        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

			/*
             * newarray[i * 3] = (byte) ((array[i]) & 0xFF); newarray[i * 3 + 1]
			 * = (byte)((array[i] >> 8)& 0xFF); newarray[i * 3 + 2] =
			 * (byte)((array[i] >> 16)& 0xFF);
			 */

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }
        return newarray;
    }

    public static boolean isStringEmpty(String str)
    {
        boolean result=true;
        if(str==null);
        else
        {
            str=str.trim();
            if(str.length()>0 && !str.equals("undefined"))
                result=false;
        }

        return result;
    }
}
