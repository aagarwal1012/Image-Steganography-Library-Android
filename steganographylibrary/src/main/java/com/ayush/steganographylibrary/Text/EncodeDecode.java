package com.ayush.steganographylibrary.Text;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Utility;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EncodeDecode {

    private static final String TAG = EncodeDecode.class.getName();
    private static int[] binary = {16, 8, 0};
    private static byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static int[] toShift = {6, 4, 2, 0};
    public static String END_MESSAGE_COSTANT = "#!@";
    public static String START_MESSAGE_COSTANT = "@!#";

    /**
     * This method represent the core of LSB on 2 bit (Encoding).
     *
     * @param oneDPix The <b>rgb</b> array.
     * @param imgCols Image width.
     * @param imgRows Image height.
     * @param message Message to encode.
     * @param hand    A handler interface, for the progress bar.
     * @return Encoded message image.
     */

    private static byte[] encodeMessage(int[] oneDPix, int imgCols, int imgRows,
                                        MessageEncodingStatus message, ProgressHandler hand) {

        int channels = 3;
        int shiftIndex = 4;
        //Array.newInstance(Byte.class, imgRows * imgCols * channels);
        byte[] result = new byte[imgRows * imgCols * channels];


        int resultIndex = 0;

        for (int row = 0; row < imgRows; row++) {

            for (int col = 0; col < imgCols; col++) {
                int element = row * imgCols + col;
                byte tmp = 0;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {
                    if (!message.isMessageEncoded()) {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((message.getByteArrayMessage()[message.getCurrentMessageIndex()] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6
                        if (shiftIndex % toShift.length == 0) {
                            message.incrementMessageIndex();
                            if (hand != null)
                                hand.increment(1);
                        }
                        if (message.getCurrentMessageIndex() == message.getByteArrayMessage().length) {
                            message.setMessageEncoded(true);
                            if (hand != null)
                                hand.finished();
                        }
                    }
                    else
                    {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF)));
                    }
                    result[resultIndex++] = tmp;

                }

            }

        }


        return result;

    }

    public static List<Bitmap> encodeMessage(List<Bitmap> splittedImages,
                                             String str, ProgressHandler hand) {
        List<Bitmap> result = new ArrayList<Bitmap>(splittedImages.size());
        str += END_MESSAGE_COSTANT;
        str = START_MESSAGE_COSTANT + str;
        byte[] msg = str.getBytes(Charset.forName("UTF-8"));

        MessageEncodingStatus message = new MessageEncodingStatus();
        message.setMessage(str);
        message.setByteArrayMessage(msg);
        message.setCurrentMessageIndex(0);
        message.setMessageEncoded(false);
        if (hand != null) {
            hand.setTotal(str.getBytes(Charset.forName("UTF-8")).length);
        }
        Log.i(TAG, "Message length " + msg.length);
        for (Bitmap bitm : splittedImages) {
            if (!message.isMessageEncoded()) {
                int width = bitm.getWidth();
                int height = bitm.getHeight();

                int[] oneD = new int[width * height];
                bitm.getPixels(oneD, 0, width, 0, 0, width, height);
                int density = bitm.getDensity();
                byte[] encodedImage = encodeMessage(oneD, width, height, message, hand);

                int[] oneDMod = Utility.byteArrayToIntArray(encodedImage);

                Bitmap destBitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);

                destBitmap.setDensity(density);
                int masterIndex = 0;
                for (int j = 0; j < height; j++)
                    for (int i = 0; i < width; i++) {

                        destBitmap.setPixel(i, j, Color.argb(0xFF,
                                oneDMod[masterIndex] >> 16 & 0xFF,
                                oneDMod[masterIndex] >> 8 & 0xFF,
                                oneDMod[masterIndex++] & 0xFF));
                    /*if(masterIndex%partialProgr==0)
                        handler.post(mIncrementProgress);*/
                    }
                result.add(destBitmap);
            } else
                result.add(bitm.copy(bitm.getConfig(), false));
        }
        Log.d(TAG, "Message current index " + message.getCurrentMessageIndex());

        return result;
    }

    public static String decodeMessage(List<Bitmap> encodedImages) {


        MessageDecodingStatus mesgDecoded = new MessageDecodingStatus();

        for (Bitmap bit : encodedImages) {
            int[] pixels = new int[bit.getWidth() * bit.getHeight()];
            bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(),
                    bit.getHeight());
            byte[] b = null;
            b = Utility.convertArray(pixels);
            decodeMessage(b, bit.getWidth(), bit.getHeight(), mesgDecoded);
            if (mesgDecoded.isEnded())
                break;
        }
        return mesgDecoded.getMessage();
    }

    /**
     * This is the decoding method of LSB on 2 bit.
     *
     * @param oneDPix The byte array image.
     * @param imgCols Image width.
     * @param imgRows Image height.
     * @param mesg    The decoded message.
     */
    private static void decodeMessage(byte[] oneDPix, int imgCols,
                                      int imgRows, MessageDecodingStatus mesg) {

        Vector<Byte> v = new Vector<Byte>();


        int shiftIndex = 4;
        byte tmp = 0x00;
        for (int i = 0; i < oneDPix.length; i++) {
            tmp = (byte) (tmp | ((oneDPix[i] << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));
            if (shiftIndex % toShift.length == 0) {
                v.addElement(Byte.valueOf(tmp));
                byte[] nonso = {(v.elementAt(v.size() - 1)).byteValue()};
                String str = new String(nonso, Charset.forName("UTF-8"));
                // if (END_MESSAGE_COSTANT.equals(str)) {
                if (mesg.getMessage().endsWith(END_MESSAGE_COSTANT)) {
                    Log.i("TEST", "Decoding ended");
                    //fix utf-8 decoding
                    byte[] temp = new byte[v.size()];
                    for (int index = 0; index < temp.length; index++)
                        temp[index] = v.get(index);

                    String stra = new String(temp, Charset.forName("UTF-8"));
                    mesg.setMessage(stra.substring(0, stra.length() - 1));
                    //end fix
                    mesg.setEnded(true);
                    break;
                } else {
                    mesg.setMessage(mesg.getMessage() + str);
                    if (mesg.getMessage().length() == START_MESSAGE_COSTANT.length()
                            && !START_MESSAGE_COSTANT.equals(mesg.getMessage())) {
                        mesg.setMessage(null);
                        mesg.setEnded(true);
                        break;
                    }
                }

                tmp = 0x00;
            }

        }
        if (mesg.getMessage() != null)
            mesg.setMessage(mesg.getMessage().substring(START_MESSAGE_COSTANT.length(), mesg.getMessage()
                    .length()
                    - END_MESSAGE_COSTANT.length()));


    }

    /**
     * Calculate the numbers of pixel needed
     *
     * @param message Message to encode
     * @return The number of pixel
     */
    public static int numberOfPixelForMessage(String message) {
        int result = -1;
        if (message != null) {
            message += END_MESSAGE_COSTANT;
            message = START_MESSAGE_COSTANT + message;
            result = message.getBytes(Charset.forName("UTF-8")).length * 4 / 3;
        }

        return result;
    }


    public interface ProgressHandler {

        public void setTotal(int tot);

        public void increment(int inc);

        public void finished();
    }

    private static class MessageDecodingStatus {

        private String message;
        private boolean ended;

        public MessageDecodingStatus() {
            message = "";
            ended = false;
        }

        public boolean isEnded() {
            return ended;
        }

        public void setEnded(boolean ended) {
            this.ended = ended;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
//            Log.i("TEST",message);
        }


    }

    private static class MessageEncodingStatus {
        private boolean messageEncoded;
        private int currentMessageIndex;
        private byte[] byteArrayMessage;
        private String message;

        public void incrementMessageIndex() {
            currentMessageIndex++;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isMessageEncoded() {
            return messageEncoded;
        }

        public void setMessageEncoded(boolean messageEncoded) {
            this.messageEncoded = messageEncoded;
        }

        public int getCurrentMessageIndex() {
            return currentMessageIndex;
        }

        public void setCurrentMessageIndex(int currentMessageIndex) {
            this.currentMessageIndex = currentMessageIndex;
        }

        public byte[] getByteArrayMessage() {
            return byteArrayMessage;
        }

        public void setByteArrayMessage(byte[] byteArrayMessage) {
            this.byteArrayMessage = byteArrayMessage;
        }
    }

}
