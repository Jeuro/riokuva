package riokuva;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PpmImageParser {

    private BufferedReader br;
    private PrintWriter pw;
    private int width, height, maxcolours;
    private PpmImage image;

    // konstruktori tiedostossa olevalle kuvalle, eli kuvan lukemiseen
    PpmImageParser(File passedFile) {
        this.br = openBufferedReaderForFile(passedFile);
        // luetaan header heti konstruktorissa, että saadaan kuvan koko
        readPpmHeader();
    }
    // konstruktori muistissa olevalle kuvalle, eli kuvan kirjoittamiseen
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
        writePpm();
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
            // alustetaan uusi merkkijonon pilkkoja
            StreamTokenizer tok = new StreamTokenizer(br);
            tok.parseNumbers();
            // ASCII 35 = '#' -- aloittaa PPM-formaatissa kommenttirivit:
            tok.commentChar(35); 

            // seuraavissa luetaan headerin arvot yksi kerrallaan
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
    // avataan puskuroitu merkkijonolukija kuvatiedostolle
    private BufferedReader openBufferedReaderForFile(File passedFile) {
        BufferedReader newBr = null;
        try {
            // viimeinen argumentti on puskurin koko tavuina. Iso puskuri isoille kuville
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
            // Alustetaan uusi pilkkoja
            StreamTokenizer tok = new StreamTokenizer(br);
            tok.parseNumbers();
            tok.commentChar(35); // ASCII 35 = '#'

            // luetaan kuvan RGB-arvot suoraan pikselikoordinaatteihin (x, y)
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    tok.nextToken();
                    r = (int) (tok.nval);
                    tok.nextToken();
                    g = (int) (tok.nval);
                    tok.nextToken();
                    b = (int) (tok.nval);
                    // tässä varsinaisesti kootaan yksi pikseli
                    image.setRGB(x, y, Bitop.makePixel(r, g, b));
                }
            }

            br.close();

        } catch (IOException e) {
            System.err.println(e);
        }

    }
    // kuvan kirjoittaminen
    // avataan ensin uusi PrintWriter, jolla voi kirjoittaa tiedostoon print-metodeilla
    private PrintWriter openPrintWriterForImage(File passedFile) {
        PrintWriter newPw = null;
        try {
            newPw = new PrintWriter(new FileWriter(passedFile));
        } catch (IOException ex) {
            Logger.getLogger(PpmImageParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newPw;
    }

    private void writePpm() {
        int x, y;
        int r, g, b;
        
        writePpmHeader();
        // varsinaisen kuvadatan kirjoitus: luetaan pikselidata (x,y)-koordinaateista
        // ja puretaan ne RGB-osiin, jotka kirjoitetaan välilyönnillä erotettuina
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
        // pidetään huolta, että puskuri on kirjoitettu levylle 
        // ja suljetaan kirjoittaja
        pw.flush();
        pw.close();
    }

    private void writePpmHeader() {
        // huomaa, että println tuottaa rivinvaihdot 
        pw.println("P3");
        pw.println("" + width + " " + height);
        pw.println("" + maxcolours);
    }
}
