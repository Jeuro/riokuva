package riokuva;

public class BlurThread extends Thread {
    private PixelImage image;
    private PixelImage newImage;
    private int numOfThreads;
    private int startingRow;

    public BlurThread(PixelImage image, PixelImage newImage, int startingRow, int numOfThreads) {
        this.image = image;
        this.newImage = newImage;
        this.numOfThreads = numOfThreads;
        this.startingRow = startingRow;
    }
    
    // Käydään läpi prosessille osoitetut rivit kuvasta ja annetaan kyseisten rivien pikseleille uudet pehmennetyt arvot
    @Override
    public void run() {
        // Aloitusrivin jälkeen hypätään yli niin monta kuvan riviä kuin on muita suorittavia prosesseja
        for (int y = this.startingRow; y < this.image.getHeight() - 1; y += this.numOfThreads) {
            for (int x = 1; x < this.image.getWidth() - 1; x++) {
                this.newImage.setRGB(x, y, PixelEditor.blurPixel(this.image, x, y));
            }
        }
    }
}