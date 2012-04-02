package riokuva;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PpmImageParser {

    private PrintWriter pw;
    private int width, height, maxcolours;
    private PpmImage image;
    Matcher matcher;
    CharBuffer charBuffer;
    int availableProcessors;

    PpmImageParser(File passedFile) {
        Runtime rt = Runtime.getRuntime();
        availableProcessors = rt.availableProcessors();
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(passedFile, "r").getChannel();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        MappedByteBuffer mbb = null;
        try {
            mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        } catch (IOException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            charBuffer = Charset.defaultCharset().newDecoder().decode(mbb);
        } catch (CharacterCodingException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        Pattern nonWhitespaceOrCommentOrNewline = Pattern.compile("\\S+|(#.*$)|\\n");
        matcher = nonWhitespaceOrCommentOrNewline.matcher(charBuffer);
        readHeader(matcher);
    }

    PpmImageParser(PpmImage passedImage) {
        this.image = passedImage;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.maxcolours = image.getMaxcolours();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxcolours() {
        return maxcolours;
    }

    public void writePpmImage(File passedFile) {
        this.pw = openPrintWriterForImage(passedFile);
        writePpmData();
    }

    @Override
    public String toString() {
        return "Width: " + this.getWidth()
                + ", height: " + this.getHeight()
                + ", maxcolours: " + this.getMaxcolours();
    }

    // Uusi versio headerin lukijasta, ottaa huomioon kommentit ja toimii rinnakkaisen
    // lukijan kanssa.
    private void readHeader(Matcher matcher) {

        skipComments(matcher);

        if (matcher.group().equalsIgnoreCase("P3")) {
            skipComments(matcher);

        } else {
            System.err.println("Not P3! ... continuing anyway");
        }

        if (Integer.parseInt(matcher.group()) > 0) {
            width = Integer.parseInt(matcher.group());
        }

        skipComments(matcher);

        if (Integer.parseInt(matcher.group()) > 0) {
            height = Integer.parseInt(matcher.group());
        }

        skipComments(matcher);

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

    public PpmImage concReadPpmImage() {

        int headerOffset = matcher.end();
        int dataBegin;
        int dataEnd;
        int dataBytes = charBuffer.length() - headerOffset;
        int chunkSize = dataBytes / availableProcessors;

        int totalPixels = width * height;
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
        return image;
    }

    private PrintWriter openPrintWriterForImage(File passedFile) {
        PrintWriter newPw = null;
        try {
            newPw = new PrintWriter(new FileWriter(passedFile));
        } catch (IOException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newPw;
    }

    private void writePpmData() {
        int x, y;
        int r, g, b;

        writePpmHeader();

        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                pw.print(Bitop.getR(image.getRGB(x, y)));
                pw.print(" ");

                pw.print(Bitop.getG(image.getRGB(x, y)));
                pw.print(" ");

                pw.print(Bitop.getB(image.getRGB(x, y)));
                if (x == width - 1) {
                    pw.print("\n");
                } else {
                    pw.print(" ");
                }
            }
        }
        pw.flush();
        pw.close();
    }

    private void writePpmHeader() {
        pw.println("P3");
        pw.println("" + width + " " + height);
        pw.println("" + maxcolours);
    }
}
