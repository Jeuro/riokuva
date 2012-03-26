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
        System.out.println("VM reports "+ap+" available processors");
         
        PpmImage kuva = readImageAndReportTime(infile);
        
        writeImageAndReportTime(kuva, outfile);
        
        
    }
     
    
    // Alla olevat metodit siirretään samaan luokkaan,
    // jossa sijaitsee for-looppi joka käy kuvan läpi
    // blurraten tai sharpentaen. Väliaikaisesti tässä.
    
    // Ei testattu vielä. Palauttaa blurratun pikselin integerinä,
    // kutsu for-loopissa esim. kirjoitettavaKuva.setRGB(blur(luettavaKuva, i, j);
    public static int blur(PixelImage luettavaKuva, int x, int y){
        //hae kuvasta blur kohde ja sen naapurit
        int keski = luettavaKuva.getRGB(x, y);
        int yla = luettavaKuva.getRGB(x, (y+1));
        int ala = luettavaKuva.getRGB(x, (y-1));
        int vasen = luettavaKuva.getRGB((x-1), y);
        int oikea = luettavaKuva.getRGB((x+1), y);
        
        //jokaiselle väriarvolle R, G, B blur erikseen.
        int r = (4*Bitop.getR(keski) + Bitop.getR(yla) + Bitop.getR(ala) 
                + Bitop.getR(oikea) + Bitop.getR(vasen)) / 8;
        int g = (4*Bitop.getG(keski) + Bitop.getG(yla) + Bitop.getG(ala) 
                + Bitop.getG(oikea) + Bitop.getG(vasen)) / 8;
        int b = (4*Bitop.getB(keski) + Bitop.getB(yla) + Bitop.getB(ala) 
                + Bitop.getB(oikea) + Bitop.getB(vasen)) / 8;
        
        //palauta blurrattu pikseli
        return Bitop.makePixel(r, g, b);
        
    }
    
    // Ei testattu vielä. Palauttaa terävoitetyn pikselin integerinä,
    // kutsu for-loopissa esim. kirjoitettavaKuva.setRGB(sharpen(luettavaKuva, i, j);
    public static int sharpen(PixelImage kuva, int x, int y){
        //hae kuvasta sharpen kohde ja sen naapurit
        int keski = kuva.getRGB(x, y);
        int yla = kuva.getRGB(x, (y+1));
        int ala = kuva.getRGB(x, (y-1));
        int vasen = kuva.getRGB((x-1), y);
        int oikea = kuva.getRGB((x+1), y);
        
        // Jokaiselle väriarvolle R, G, B sharpen erikseen.
        
        int r = (4*Bitop.getR(keski) - Bitop.getR(yla) - Bitop.getR(ala) 
                - Bitop.getR(oikea) - Bitop.getR(vasen)) / 8;
        int g = (4*Bitop.getG(keski) - Bitop.getG(yla) - Bitop.getG(ala) 
                - Bitop.getG(oikea) - Bitop.getG(vasen)) / 8;  
        int b = (4*Bitop.getB(keski) - Bitop.getB(yla) - Bitop.getB(ala) 
                - Bitop.getB(oikea) - Bitop.getB(vasen)) / 8;
        
        // sharpen vaatii varautumisen, että väriarvo tippuu alle nollan
        
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        
        // palauta sharpenettu pikseli
        return Bitop.makePixel(r, g, b);
        
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
        long readTime = (ReadEndTime - ReadStartTime)/1000000;
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
        long writeTime = (WriteEndTime - WriteStartTime)/1000000;
        System.out.println("Write time (ms): " + writeTime);
    }
    
    private static void checkArgs(String[] args) {
        if (args.length != 2) {
            showUsage();
            System.exit(1);
        }
    }
    private static void checkInfileExists(String infile) {
        if(!new File(infile).exists()) {
            System.err.println("Syötetiedostoa ei ole olemassa!");
            System.exit(2);
        }
    }

    private static void checkOutfileDoesNotExist(String outfile) {
         if(new File(outfile).exists()) {
            System.err.println("Kohdetiedosto on jo olemassa!");
            System.exit(2);
        }
    }
}