package riokuva;

public class BlurThread extends Thread {
    private PixelImage image;
    private PixelImage newImage;
    private int numOfRows;
    private int startingRow;

    public BlurThread(PixelImage image, PixelImage newImage, int numOfRows, int startingRow) {
        this.image = image;
        this.newImage = newImage;
        this.numOfRows = numOfRows;
        this.startingRow = startingRow;
    }
 
    @Override
    public void run() {
    }
}
