/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package riokuva;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author Jussi Kirjavainen
 */
public class ImageProcessingMain {
        
        // ImageParser siirretty kokonaisuudessaan omaan luokkaansa
    
    public static void main(String[] args) throws IOException {
        final long endTime;
        final long startTime = System.nanoTime();
        final String filename = args[0]; 

        Runtime r = Runtime.getRuntime();
        int ap = r.availableProcessors();
        System.out.println("VM reports "+ap+" available processors");
        
        BufferedReader br = new BufferedReader(new FileReader(filename));
        PpmImageParser pip = new PpmImageParser(br);
        System.out.println(pip.toString());
        
        int[] kuva = pip.getPpmImageData();

        endTime = System.nanoTime();
        long readTime = (endTime - startTime)/1000000;
        System.out.println("read time (ms): " + readTime);
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
    

}