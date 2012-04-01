/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package riokuva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jussi Kirjavainen
 */
public class ImageProcessingMain {
    public static void main(String[] args) throws IOException {

        checkArgs(args);

        final String infile = args[0];
        final String outfile = args[1];

        checkInfileExists(infile);
        checkOutfileDoesNotExist(outfile);

        Runtime r = Runtime.getRuntime();
        final int ap = r.availableProcessors();
        System.out.println("VM reports " + ap + " available processors");

        PpmImage kuva = readImageAndReportTime(infile);
        PpmImage kuva2 = new PpmImage(kuva.getWidth(), kuva.getHeight());

        ImageEditor e = new ImageEditor(ap);

        e.blur(kuva, kuva2);
        e.blur(kuva2, kuva);
        e.blur(kuva, kuva2);

        e.sharpen(kuva2, kuva);
        e.sharpen(kuva, kuva2);
        e.sharpen(kuva2, kuva);

        writeImageAndReportTime(kuva, outfile);
    }

    private static void showUsage() {
        System.err.println("Käyttö: java ImageProcessingMain /polku/ppm-tiedostoon");
    }

    private static PpmImage readImageAndReportTime(String filename) {
        final long ReadEndTime;
        final long ReadStartTime;
        ReadStartTime = System.nanoTime();

        PpmImageParser pip = new PpmImageParser(new File(filename));
        System.out.println(pip.toString());

        PpmImage kuva = pip.readPpmImage();

        ReadEndTime = System.nanoTime();
        long readTime = (ReadEndTime - ReadStartTime) / 1000000;
        System.out.println("Read time (ms): " + readTime);

        return kuva;
    }

    private static void writeImageAndReportTime(PpmImage image, String filename) {
        final long WriteEndTime;
        final long WriteStartTime;
        WriteStartTime = System.nanoTime();

        PpmImageParser pip = new PpmImageParser(image);
        System.out.println(pip.toString());
        File outfile = new File(filename);

        pip.writePpmImage(outfile);

        WriteEndTime = System.nanoTime();
        long writeTime = (WriteEndTime - WriteStartTime) / 1000000;
        System.out.println("Write time (ms): " + writeTime);
    }

    private static void checkArgs(String[] args) {
        if (args.length != 2) {
            showUsage();
            System.exit(1);
        }
    }

    private static void checkInfileExists(String infile) {
        if (!new File(infile).exists()) {
            System.err.println("Syötetiedostoa ei ole olemassa!");
            System.exit(2);
        }
    }

    private static void checkOutfileDoesNotExist(String outfile) {
        if (new File(outfile).exists()) {
            System.err.println("Kohdetiedosto on jo olemassa!");
            System.exit(2);
        }
    }
}