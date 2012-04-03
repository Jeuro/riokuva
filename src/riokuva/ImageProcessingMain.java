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
    private static int availableProcessors;

    public static void main(String[] args) throws IOException {

        checkArgs(args);

        final String infile = args[0];
        final String outfile = args[1];

        checkInfileExists(infile);
        checkOutfileDoesNotExist(outfile);

        Runtime r = Runtime.getRuntime();
        availableProcessors = r.availableProcessors();
        System.out.println("VM reports " + availableProcessors + " available processors");

        PpmImage kuva = readImageAndReportTime(infile);
        
        kuva = processImageAndReportTime(kuva);
        
        writeImageAndReportTime(kuva, outfile);
    }

    private static void showUsage() {
        System.err.println("Usage: java -jar -Xmx1500m ImageProcessingMain infile outfile");
    }

    private static PpmImage readImageAndReportTime(String filename) {
        final long ReadEndTime;
        final long ReadStartTime;
        PpmImage kuva;
        System.out.println("Reading image...");
        ReadStartTime = System.nanoTime();

        PpmImageParser pip = new PpmImageParser(new File(filename));
        kuva = pip.concReadPpmImage();
        ReadEndTime = System.nanoTime();
        long readTime = (ReadEndTime - ReadStartTime) / 1000000;
        System.out.println("Read time (ms): " + readTime);

        return kuva;
    }

    private static PpmImage processImageAndReportTime(PpmImage kuva) {
        final long ReadEndTime;
        final long ReadStartTime;
        System.out.println("Now processing image...");
        ReadStartTime = System.nanoTime();
        PpmImage kuva2 = new PpmImage(kuva.getWidth(), kuva.getHeight());

        ImageEditor e = new ImageEditor(availableProcessors);

        e.blur(kuva, kuva2);
        e.blur(kuva2, kuva);
        e.blur(kuva, kuva2);

        e.sharpen(kuva2, kuva);
        e.sharpen(kuva, kuva2);
        e.sharpen(kuva2, kuva);
       
        ReadEndTime = System.nanoTime();
        long readTime = (ReadEndTime - ReadStartTime) / 1000000;
        System.out.println("Processing time (ms): " + readTime);

        return kuva;
    }

    private static void writeImageAndReportTime(PpmImage image, String filename) {
        final long WriteEndTime;
        final long WriteStartTime;
        System.out.println("Writing image...");
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
            System.err.println("Lähdetiedostoa ei ole olemassa!");
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