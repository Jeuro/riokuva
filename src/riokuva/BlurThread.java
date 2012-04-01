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

    @Override
    public void run() {
        for (int y = this.startingRow; y < this.image.getHeight() - 1; y += this.numOfThreads) {
            for (int x = 1; x < this.image.getWidth() - 1; x++) {
                this.newImage.setRGB(x, y, PixelEditor.blurPixel(this.image, x, y));
            }
        }
    }
}