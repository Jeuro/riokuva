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

        // Jokaiselle väriarvolle R, G, B sharpen erikseen.
        int r = sharpen(Bitop.getR(keski), Bitop.getR(yla), Bitop.getR(ala), Bitop.getR(oikea), Bitop.getR(vasen));
        int g = sharpen(Bitop.getG(keski), Bitop.getG(yla), Bitop.getG(ala), Bitop.getG(oikea), Bitop.getG(vasen));
        int b = sharpen(Bitop.getB(keski), Bitop.getB(yla), Bitop.getB(ala), Bitop.getB(oikea), Bitop.getB(vasen));

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

        //jokaiselle väriarvolle R, G, B blur erikseen.
        int r = blur(Bitop.getR(keski), Bitop.getR(yla), Bitop.getR(ala), Bitop.getR(oikea), Bitop.getR(vasen));
        int g = blur(Bitop.getG(keski), Bitop.getG(yla), Bitop.getG(ala), Bitop.getG(oikea), Bitop.getG(vasen));
        int b = blur(Bitop.getB(keski), Bitop.getB(yla), Bitop.getB(ala), Bitop.getB(oikea), Bitop.getB(vasen));

        //palauta blurrattu pikseli
        return Bitop.makePixel(r, g, b);
    }

    private static int sharpen(int keski, int yla, int ala, int oikea, int vasen) {
        int arvo = (4 * keski - yla - ala - oikea - vasen) / 8;

        //arvo ei saa mennä alle nollan
        return Math.max(arvo, 0);
    }

    private static int blur(int keski, int yla, int ala, int oikea, int vasen) {
        return (4 * keski + yla + ala + oikea + vasen) / 8;
    }
}
