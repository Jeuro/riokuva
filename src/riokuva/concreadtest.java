/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package riokuva;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 *
 * @author elmerfudd
 */
public class concreadtest extends Thread {

    int height, width, maxcolours;
    int totalBytes, totalPixels;
    Matcher matcher;
    CharBuffer charBuffer;
    int availableProcessors;

    public concreadtest() {
    }

    public PpmImage concReadPpmImage(File in) throws IOException {

        PpmImage image = null;
        try {

            FileChannel fc = new RandomAccessFile(in, "r").getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            charBuffer = Charset.defaultCharset().newDecoder().decode(mbb);
            System.out.println("Charbuffer: " + charBuffer.length());
            Pattern nonWhitespaceOrCommentOrNewline = Pattern.compile("\\S+|(#.*$)|\\n");
            matcher = nonWhitespaceOrCommentOrNewline.matcher(charBuffer);
            readHeader(matcher);

            System.out.println("" + width + " " + height + " " + maxcolours);
            int values = 0;
            int headerOffset = matcher.end();
            int dataBegin;
            int dataEnd;

            Runtime rt = Runtime.getRuntime();
            availableProcessors = rt.availableProcessors();

            int dataBytes = charBuffer.length() - headerOffset;
            int chunkSize = dataBytes / availableProcessors;

            totalPixels = width * height;
            int startPixel;
            int endPixel;
            int pixelsPerChunk = totalPixels / availableProcessors;

            image = new PpmImage(width, height);

            List<PpmReadWorker> workers = new ArrayList<PpmReadWorker>();

            for (int p = 0; p < availableProcessors; p++) {
                dataBegin = p * chunkSize + headerOffset;
                dataEnd = (p + 1) * chunkSize + headerOffset;
                startPixel = p * pixelsPerChunk;
                endPixel = (p + 1) * pixelsPerChunk;

                PpmReadWorker currentWorker = new PpmReadWorker(
                        image,
                        charBuffer,
                        startPixel,
                        endPixel,
                        dataBegin,
                        dataEnd);

                workers.add(currentWorker);
                currentWorker.start();
            }
            // odotetaan että säikeet lopettaa
            for (PpmReadWorker currentWorker : workers) {
                try {
                    currentWorker.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(concreadtest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(concreadtest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return image;

    }

    public static void main(String[] args) throws IOException {
        long starttime = System.nanoTime();
        File in = new File("7976x4480.ppm");
        File out = new File("out.ppm");
        concreadtest crt = new concreadtest();
        PpmImage image = crt.concReadPpmImage(in);
        long endtime = System.nanoTime();
        System.out.println("Read time: " + (endtime - starttime) / 1000000);
        PpmImageParser pip = new PpmImageParser(image);
        System.out.println(pip.toString());

        pip.writePpmImage(out);

    }

    private void readHeader(Matcher matcher) {

        skipComments(matcher);

        if (matcher.group().equalsIgnoreCase("P3")) {
            skipComments(matcher);

        } else {
            System.err.println("Not P3! ... continuing anyway");
        }

        System.err.println("width? " + matcher.group());
        if (Integer.parseInt(matcher.group()) > 0) {
            width = Integer.parseInt(matcher.group());
        }

        skipComments(matcher);

        System.err.println("height? " + matcher.group());
        if (Integer.parseInt(matcher.group()) > 0) {
            height = Integer.parseInt(matcher.group());
        }
        
        skipComments(matcher);

        System.err.println("maxcolours? " + matcher.group());
        if (Integer.parseInt(matcher.group()) > 0) {
            maxcolours = Integer.parseInt(matcher.group());
        }


    }

    private void skipComments(Matcher matcher) {
        matcher.find();
        while (matcher.group().startsWith("#")
                || matcher.group().startsWith("\n")) {
            while (!matcher.group().startsWith("\n")) {
                matcher.find();
            }
            matcher.find();
        }
    }
}
