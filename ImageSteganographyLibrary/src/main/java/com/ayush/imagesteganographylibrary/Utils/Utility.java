package com.ayush.imagesteganographylibrary.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
This is the Utility Class containing some useful methods
 */
public class Utility {

    //Taking the square block size constant
    private static final int SQUARE_BLOCK_SIZE = 512;
    private final static String TAG = Utility.class.getName();

    /**
     * This method calculates the number of square block needed
     *
     * @return : number of Square blocks {Integer}
     * @parameter : number of pixels {Integer}
     */
    public static int squareBlockNeeded(int pixels) {
        int result;

        int quadratic = SQUARE_BLOCK_SIZE * SQUARE_BLOCK_SIZE;
        int divisor = pixels / (quadratic);
        int remainder = pixels % (quadratic);

        result = divisor + (remainder > 0 ? 1 : 0);

        return result;
    }

    /**
     * This method splits the image into many images of ( SQUARE_BLOCK_SIZE * SQUARE_BLOCK_SIZE ) size.
     *
     * @return : List of splitted images {List}
     * @parameter : Image {Bitmap}
     */
    public static List<Bitmap> splitImage(Bitmap bitmap) {

        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<>();

        // Assume like a matrix in which the element is a Small Square block
        //Rows and columns of that matrix
        int rows = bitmap.getHeight() / SQUARE_BLOCK_SIZE;
        int cols = bitmap.getWidth() / SQUARE_BLOCK_SIZE;

        int chunk_height_mod = bitmap.getHeight() % SQUARE_BLOCK_SIZE;
        int chunk_width_mod = bitmap.getWidth() % SQUARE_BLOCK_SIZE;

        if (chunk_height_mod > 0)
            rows++;
        if (chunk_width_mod > 0)
            cols++;


        //x_coordinate and y_coordinate are the pixel positions of the image chunks
        int y_coordinate = 0;

        for (int x = 0; x < rows; x++) {

            int x_coordinate = 0;

            for (int y = 0; y < cols; y++) {

                chunkHeight = SQUARE_BLOCK_SIZE;
                chunkWidth = SQUARE_BLOCK_SIZE;

                if (y == cols - 1 && chunk_width_mod > 0)
                    chunkWidth = chunk_width_mod;

                if (x == rows - 1 && chunk_height_mod > 0)
                    chunkHeight = chunk_height_mod;

                //Adding chunk images to the list
                chunkedImages.add(Bitmap.createBitmap(bitmap, x_coordinate, y_coordinate, chunkWidth, chunkHeight));
                x_coordinate += SQUARE_BLOCK_SIZE;

            }

            y_coordinate += SQUARE_BLOCK_SIZE;

        }

        //returning the list
        return chunkedImages;
    }

    /**
     * This method merge all the chunk image list into one single image
     *
     * @return : Merged Image {Bitmap}
     * @parameter : List {Bitmap}, Original Height {Integer}, Original Width {Integer}
     */
    public static Bitmap mergeImage(List<Bitmap> images, int original_height, int original_width) {

        //Calculating number of Rows and columns of that matrix
        int rows = original_height / SQUARE_BLOCK_SIZE;
        int cols = original_width / SQUARE_BLOCK_SIZE;

        int chunk_height_mod = original_height % SQUARE_BLOCK_SIZE;
        int chunk_width_mod = original_width % SQUARE_BLOCK_SIZE;

        if (chunk_height_mod > 0)
            rows++;
        if (chunk_width_mod > 0)
            cols++;

        //create a bitmap of a size which can hold the complete image after merging
        Log.d(TAG, "Size width " + original_width + " size height " + original_height);
        Bitmap bitmap = Bitmap.createBitmap(original_width, original_height, Bitmap.Config.ARGB_4444);

        //Creating canvas
        Canvas canvas = new Canvas(bitmap);

        int count = 0;

        for (int irows = 0; irows < rows; irows++) {
            for (int icols = 0; icols < cols; icols++) {

                //Drawing all the chunk images of canvas
                canvas.drawBitmap(images.get(count), (SQUARE_BLOCK_SIZE * icols), (SQUARE_BLOCK_SIZE * irows), new Paint());
                count++;

            }
        }

        //returning bitmap
        return bitmap;
    }

    /**
     * This method converts the byte array to an integer array.
     *
     * @return : Integer Array
     * @parameter : b {the byte array}
     */

    public static int[] byteArrayToIntArray(byte[] b) {

        Log.v("Size byte array", b.length + "");

        int size = b.length / 3;

        Log.v("Size Int array", size + "");

        System.runFinalization();
        //Garbage collection
        System.gc();

        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int offset = 0;
        int index = 0;

        while (offset < b.length) {
            result[index++] = byteArrayToInt(b, offset);
            offset = offset + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @return : Integer
     * @parameter :  b {the byte array}
     */
    public static int byteArrayToInt(byte[] b) {

        return byteArrayToInt(b, 0);

    }

    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @return :  Integer
     * @parameter :  b {the byte array}, offset {integer}
     */
    private static int byteArrayToInt(byte[] b, int offset) {

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
     * @return : byte Array representing [rgb] values.
     * @parameter : Integer array representing [argb] values.
     */
    public static byte[] convertArray(int[] array) {

        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }

        return newarray;
    }

    /**
     * This method is used to check whether the string is empty of not
     *
     * @return : true or false {boolean}
     * @parameter : String
     */
    public static boolean isStringEmpty(String str) {
        boolean result = true;

        if (str == null) ;
        else {
            str = str.trim();
            if (str.length() > 0 && !str.equals("undefined"))
                result = false;
        }

        return result;
    }
}
