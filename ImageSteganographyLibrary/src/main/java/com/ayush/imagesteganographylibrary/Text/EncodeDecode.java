package com.ayush.imagesteganographylibrary.Text;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.ayush.imagesteganographylibrary.Utils.Utility;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class EncodeDecode {

    private static final String TAG = EncodeDecode.class.getName();
    //start and end message constants
    private static final String END_MESSAGE_COSTANT = "#!@";
    private static final String START_MESSAGE_COSTANT = "@!#";
    private static final int[] binary = {16, 8, 0};
    private static final byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static final int[] toShift = {6, 4, 2, 0};

    /**
     * This method represent the core of 2 bit Encoding
     *
     * @return : byte encoded pixel array
     * @parameter :  integer_pixel_array {The integer RGB array}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     * @parameter : messageEncodingStatus {object}
     * @parameter : progressHandler {A handler interface, for the progress bar}
     */

    private static byte[] encodeMessage(int[] integer_pixel_array, int image_columns, int image_rows,
                                        MessageEncodingStatus messageEncodingStatus, ProgressHandler progressHandler) {

        //denotes RGB channels
        int channels = 3;

        int shiftIndex = 4;

        //creating result byte_array
        byte[] result = new byte[image_rows * image_columns * channels];

        int resultIndex = 0;

        for (int row = 0; row < image_rows; row++) {

            for (int col = 0; col < image_columns; col++) {

                //2D matrix in 1D
                int element = row * image_columns + col;

                byte tmp;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {

                    if (!messageEncodingStatus.isMessageEncoded()) {

                        // Shifting integer value by 2 in left and replacing the two least significant digits with the message_byte_array values..
                        tmp = (byte) ((((integer_pixel_array[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((messageEncodingStatus.getByteArrayMessage()[messageEncodingStatus.getCurrentMessageIndex()] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6

                        if (shiftIndex % toShift.length == 0) {

                            messageEncodingStatus.incrementMessageIndex();

                            if (progressHandler != null)
                                progressHandler.increment(1);

                        }

                        if (messageEncodingStatus.getCurrentMessageIndex() == messageEncodingStatus.getByteArrayMessage().length) {

                            messageEncodingStatus.setMessageEncoded();

                            if (progressHandler != null)
                                progressHandler.finished();

                        }
                    } else {
                        //Simply copy the integer to result array
                        tmp = (byte) ((((integer_pixel_array[element] >> binary[channelIndex]) & 0xFF)));
                    }

                    result[resultIndex++] = tmp;

                }

            }

        }


        return result;

    }

    /**
     * This method implements the above method on the list of chunk image list.
     *
     * @return : Encoded list of chunk images
     * @parameter : splitted_images {list of chunk images}
     * @parameter : encrypted_message {string}
     * @parameter : progressHandler {Progress bar handler}
     */
    public static List<Bitmap> encodeMessage(List<Bitmap> splitted_images,
                                             String encrypted_message, ProgressHandler progressHandler) {

        //Making result method

        List<Bitmap> result = new ArrayList<>(splitted_images.size());


        //Adding start and end message constants to the encrypted message
        encrypted_message = encrypted_message + END_MESSAGE_COSTANT;
        encrypted_message = START_MESSAGE_COSTANT + encrypted_message;


        //getting byte array from string
        byte[] byte_encrypted_message = encrypted_message.getBytes(Charset.forName("ISO-8859-1"));

        //Message Encoding Status
        MessageEncodingStatus message = new MessageEncodingStatus(byte_encrypted_message, encrypted_message);

        //Progress Handler
        if (progressHandler != null) {
            progressHandler.setTotal(encrypted_message.getBytes(Charset.forName("ISO-8859-1")).length);
        }

        //Just a log to get the byte message length
        Log.i(TAG, "Message length " + byte_encrypted_message.length);

        for (Bitmap bitmap : splitted_images) {

            if (!message.isMessageEncoded()) {

                //getting bitmap height and width
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                //Making 1D integer pixel array
                int[] oneD = new int[width * height];
                bitmap.getPixels(oneD, 0, width, 0, 0, width, height);

                //getting bitmap density
                int density = bitmap.getDensity();

                //encoding image
                byte[] encodedImage = encodeMessage(oneD, width, height, message, progressHandler);

                //converting byte_image_array to integer_array
                int[] oneDMod = Utility.byteArrayToIntArray(encodedImage);

                //creating bitmap from encrypted_image_array
                Bitmap encoded_Bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                encoded_Bitmap.setDensity(density);

                int masterIndex = 0;

                //setting pixel values of above bitmap
                for (int j = 0; j < height; j++)
                    for (int i = 0; i < width; i++) {

                        encoded_Bitmap.setPixel(i, j, Color.argb(0xFF,
                                oneDMod[masterIndex] >> 16 & 0xFF,
                                oneDMod[masterIndex] >> 8 & 0xFF,
                                oneDMod[masterIndex++] & 0xFF));

                    }

                result.add(encoded_Bitmap);

            } else {
                //Just add the image chunk to the result
                result.add(bitmap.copy(bitmap.getConfig(), false));
            }
        }

        return result;
    }

    /**
     * This is the decoding method of 2 bit encoding.
     *
     * @return : Void
     * @parameter : byte_pixel_array {The byte array image}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     * @parameter : messageDecodingStatus {object}
     */
    private static void decodeMessage(byte[] byte_pixel_array, int image_columns,
                                      int image_rows, MessageDecodingStatus messageDecodingStatus) {

        //encrypted message
        Vector<Byte> byte_encrypted_message = new Vector<>();

        int shiftIndex = 4;

        byte tmp = 0x00;


        for (byte aByte_pixel_array : byte_pixel_array) {


            //get last two bits from byte_pixel_array
            tmp = (byte) (tmp | ((aByte_pixel_array << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));

            if (shiftIndex % toShift.length == 0) {
                //adding temp byte value
                byte_encrypted_message.addElement(tmp);


                //converting byte value to string
                byte[] nonso = {byte_encrypted_message.elementAt(byte_encrypted_message.size() - 1)};
                String str = new String(nonso, Charset.forName("ISO-8859-1"));

                if (messageDecodingStatus.getMessage().endsWith(END_MESSAGE_COSTANT)) {

                    Log.i("TEST", "Decoding ended");

                    //fixing ISO-8859-1 decoding
                    byte[] temp = new byte[byte_encrypted_message.size()];

                    for (int index = 0; index < temp.length; index++)
                        temp[index] = byte_encrypted_message.get(index);


                    String stra = new String(temp, Charset.forName("ISO-8859-1"));


                    messageDecodingStatus.setMessage(stra.substring(0, stra.length() - 1));
                    //end fixing

                    messageDecodingStatus.setEnded();

                    break;
                } else {
                    //just add the decoded message to the original message
                    messageDecodingStatus.setMessage(messageDecodingStatus.getMessage() + str);

                    //If there was no message there and only start and end message constant was there
                    if (messageDecodingStatus.getMessage().length() == START_MESSAGE_COSTANT.length()
                            && !START_MESSAGE_COSTANT.equals(messageDecodingStatus.getMessage())) {

                        messageDecodingStatus.setMessage("");
                        messageDecodingStatus.setEnded();

                        break;
                    }
                }

                tmp = 0x00;
            }

        }

        if (!Utility.isStringEmpty(messageDecodingStatus.getMessage()))
            //removing start and end constants form message

            try {
                messageDecodingStatus.setMessage(messageDecodingStatus.getMessage().substring(START_MESSAGE_COSTANT.length(), messageDecodingStatus.getMessage()
                        .length()
                        - END_MESSAGE_COSTANT.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    /**
     * This method takes the list of encoded chunk images and decodes it.
     *
     * @return : encrypted message {String}
     * @parameter : encodedImages {list of encode chunk images}
     */

    public static String decodeMessage(List<Bitmap> encodedImages) {

        //Creating object
        MessageDecodingStatus messageDecodingStatus = new MessageDecodingStatus();

        for (Bitmap bit : encodedImages) {
            int[] pixels = new int[bit.getWidth() * bit.getHeight()];

            bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(),
                    bit.getHeight());

            byte[] b;

            b = Utility.convertArray(pixels);

            decodeMessage(b, bit.getWidth(), bit.getHeight(), messageDecodingStatus);

            if (messageDecodingStatus.isEnded())
                break;
        }

        return messageDecodingStatus.getMessage();
    }

    /**
     * Calculate the numbers of pixel needed
     *
     * @return : The number of pixel {integer}
     * @parameter : message {Message to encode}
     */
    public static int numberOfPixelForMessage(String message) {
        int result = -1;
        if (message != null) {
            message += END_MESSAGE_COSTANT;
            message = START_MESSAGE_COSTANT + message;
            result = message.getBytes(Charset.forName("ISO-8859-1")).length * 4 / 3;
        }

        return result;
    }

    //Progress handler class
    public interface ProgressHandler {

        void setTotal(int tot);

        void increment(int inc);

        void finished();
    }

    private static class MessageDecodingStatus {

        private String message;
        private boolean ended;

        MessageDecodingStatus() {
            message = "";
            ended = false;
        }

        boolean isEnded() {
            return ended;
        }

        void setEnded() {
            this.ended = true;
        }

        String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }


    }

    private static class MessageEncodingStatus {
        private boolean messageEncoded;
        private int currentMessageIndex;
        private byte[] byteArrayMessage;
        private String message;

        MessageEncodingStatus(byte[] byteArrayMessage, String message) {
            this.messageEncoded = false;
            this.currentMessageIndex = 0;
            this.byteArrayMessage = byteArrayMessage;
            this.message = message;
        }

        void incrementMessageIndex() {
            currentMessageIndex++;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        boolean isMessageEncoded() {
            return messageEncoded;
        }

        void setMessageEncoded() {
            this.messageEncoded = true;
        }

        int getCurrentMessageIndex() {
            return currentMessageIndex;
        }

        public void setCurrentMessageIndex(int currentMessageIndex) {
            this.currentMessageIndex = currentMessageIndex;
        }

        byte[] getByteArrayMessage() {
            return byteArrayMessage;
        }

        public void setByteArrayMessage(byte[] byteArrayMessage) {
            this.byteArrayMessage = byteArrayMessage;
        }
    }

}
