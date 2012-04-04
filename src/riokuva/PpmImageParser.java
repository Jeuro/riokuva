package riokuva;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PpmImageParser {

    private BufferedReader br;
    private PrintWriter pw;
    private int width, height, maxcolours;
    private PpmImage image;

    PpmImageParser(File passedFile) {
        this.br = openBufferedReaderForFile(passedFile);
        readPpmHeader();
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

    public PpmImage readPpmImage() {
        readPpmData();
        return image;
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

    private void readPpmHeader() {
        int headerTemp;

        try {
            StreamTokenizer tok = new StreamTokenizer(br);
            tok.parseNumbers();
            tok.commentChar(35); // ASCII 35 = '#'

            tok.nextToken();
            if (!tok.sval.equalsIgnoreCase("P3")) {
                throw new IOException("Väärä magic number, osataan vain ASCII PPM (P3)");
            }

            tok.nextToken();
            headerTemp = (int) (tok.nval);
            if (headerTemp > 0) {
                width = headerTemp;
            } else {
                throw new IOException("Leveysarvo on negatiivinen!");
            }

            tok.nextToken();
            headerTemp = (int) (tok.nval);
            if (headerTemp > 0) {
                height = headerTemp;
            } else {
                throw new IOException("Korkeusarvo on negatiivinen!");
            }

            tok.nextToken();
            headerTemp = (int) (tok.nval);
            if (headerTemp > 0) {
                maxcolours = headerTemp;
            } else {
                throw new IOException("Maxcolours on negatiivinen!");
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private BufferedReader openBufferedReaderForFile(File passedFile) {
        BufferedReader newBr = null;
        try {
            newBr = new BufferedReader(new FileReader(passedFile), 64 * 1024 * 1024);
        } catch (IOException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newBr;
    }

    private void readPpmData() {
        String s;
        int x;
        int y;
        int r, g, b;
        image = new PpmImage(width, height);

        try {

            StreamTokenizer tok = new StreamTokenizer(br);
            tok.parseNumbers();
            tok.commentChar(35); // ASCII 35 = '#'


            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    tok.nextToken();
                    r = (int) (tok.nval);
                    tok.nextToken();
                    g = (int) (tok.nval);
                    tok.nextToken();
                    b = (int) (tok.nval);

                    image.setRGB(x, y, Bitop.makePixel(r, g, b));
                }
            }

            br.close();

        } catch (IOException e) {
            System.err.println(e);
        }

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
