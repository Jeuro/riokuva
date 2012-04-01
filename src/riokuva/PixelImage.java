package riokuva;

public interface PixelImage {
    public int getRGB(int x, int y);
    public void setRGB(int x, int y, int rgb);
    public int getWidth();
    public int getHeight();    
}
