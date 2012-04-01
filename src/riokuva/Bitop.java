package riokuva;

// Tämä luokka sisältää bittioperaatiot, joita tarvitsee kuvan pikseleiden
// väriarvojen lukemiseen
public class Bitop {
    private static final int BITMASK = 0xFF;

    // Parametrinä pikselin punaisuus, vihreys ja sinisyys (0-255)
    // tallennetaan yhteen integer-muuttujaan. Väärien parametriarvojen
    // antaminen tuottaa varsin mielenkiintoisia arvoja.
    public static int makePixel(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    // Parametrinä pikseli, josta halutaan erottaa sen punaisuus (0-255)
    public static int getR(int pixel) {
        return ((pixel >> 16) & BITMASK);
    }

    // Parametrinä pikseli, josta halutaan erottaa sen vihreys (0-255)
    public static int getG(int pixel) {
        return ((pixel >> 8) & BITMASK);
    }

    // Parametrinä pikseli, josta halutaan erottaa sen sinisyys (0-255)
    public static int getB(int pixel) {
        return (pixel & BITMASK);
    }
}
