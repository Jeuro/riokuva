/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package riokuva;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author elmerfudd
 */
public class PpmReadWorker extends Thread {
    Matcher matcher;
    PpmImage image;
    CharBuffer charBuffer;
    int startPixel, endPixel;
    int width, height;
    int firstX, firstY, lastX, lastY;
    /*
     * nonWhitespace.matcher( charBuffer.subSequence( dataBegin, dataEnd) )
     */

    public PpmReadWorker(PpmImage image,
            CharBuffer charBuffer,
            int startPixel,
            int endPixel,
            int dataBegin,
            int dataEnd) {
        this.startPixel = startPixel;
        this.endPixel = endPixel;
        Pattern nonWhitespace = Pattern.compile("\\S+");
        this.matcher = nonWhitespace.matcher(charBuffer.subSequence(dataBegin, dataEnd));
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.charBuffer = charBuffer;

        calculateCoordinates();
    }

    @Override
    public void run() {
        int x = firstX;
        int y = firstY;
        int r, g, b, temp;
        while (x <= lastX || y <= lastY) {
            temp = 0;

            if (matcher.find()) {
                temp = Integer.parseInt(matcher.group());
            }
            r = temp;

            if (matcher.find()) {
                temp = Integer.parseInt(matcher.group());
            }
            g = temp;

            if (matcher.find()) {
                b = Integer.parseInt(matcher.group());

                image.setRGB(x, y, Bitop.makePixel(r, g, b));
            }

            x++;
            if (x == width) {
                x = 0;
                y++;
            }
        }
    }

    private void calculateCoordinates() {
        firstX = startPixel % width;
        firstY = startPixel / width;
        lastX = endPixel % width;
        lastY = endPixel / width;
    }
}
