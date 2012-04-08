package riokuva;

public class PixelEditor {
    
    
    // Palauttaa terävöitetyn kuvan. Kaava on susi, joten
    // palauttaa lähes aina mustaa
    
    public static int sharpenPixel(PixelImage kuva, int x, int y) {
        //hae kuvasta sharpenPixel kohde ja sen naapurit
        int keski = kuva.getRGB(x, y);
        int yla = kuva.getRGB(x, (y + 1));
        int ala = kuva.getRGB(x, (y - 1));
        int vasen = kuva.getRGB((x - 1), y);
        int oikea = kuva.getRGB((x + 1), y);

        // Jokaiselle väriarvolle R, G, B sharpenPixel erikseen.

        int r = (4 * Bitop.getR(keski) - Bitop.getR(yla) - Bitop.getR(ala)
                - Bitop.getR(oikea) - Bitop.getR(vasen)) / 8;
        int g = (4 * Bitop.getG(keski) - Bitop.getG(yla) - Bitop.getG(ala)
                - Bitop.getG(oikea) - Bitop.getG(vasen)) / 8;
        int b = (4 * Bitop.getB(keski) - Bitop.getB(yla) - Bitop.getB(ala)
                - Bitop.getB(oikea) - Bitop.getB(vasen)) / 8;

        // sharpenPixel vaatii varautumisen, että väriarvo tippuu alle nollan

        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }

        // palauta sharpenettu pikseli
        return Bitop.makePixel(r, g, b);

    }

        // Palauttaa blurratun pikselin
    
    public static int blurPixel(PixelImage luettavaKuva, int x, int y) {
        //hae kuvasta blurPixel kohde ja sen naapurit
        int keski = luettavaKuva.getRGB(x, y);
        int yla = luettavaKuva.getRGB(x, (y + 1));
        int ala = luettavaKuva.getRGB(x, (y - 1));
        int vasen = luettavaKuva.getRGB((x - 1), y);
        int oikea = luettavaKuva.getRGB((x + 1), y);

        //jokaiselle väriarvolle R, G, B blurPixel erikseen.
        int r = (4 * Bitop.getR(keski) + Bitop.getR(yla) + Bitop.getR(ala)
                + Bitop.getR(oikea) + Bitop.getR(vasen)) / 8;
        int g = (4 * Bitop.getG(keski) + Bitop.getG(yla) + Bitop.getG(ala)
                + Bitop.getG(oikea) + Bitop.getG(vasen)) / 8;
        int b = (4 * Bitop.getB(keski) + Bitop.getB(yla) + Bitop.getB(ala)
                + Bitop.getB(oikea) + Bitop.getB(vasen)) / 8;

        //palauta blurrattu pikseli
        return Bitop.makePixel(r, g, b);

    }
}
