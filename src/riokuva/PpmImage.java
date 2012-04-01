/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package riokuva;

import java.lang.IllegalArgumentException;

/**
 *
 * @author Jussi Kirjavainen
 */
public class PpmImage implements PixelImage {
    private int[][] image;
    private int width, height, maxcolours;

    public PpmImage(int newWidth, int newHeight) {
        assertDimensionsArePositive(newWidth, newHeight);
        this.image = new int[newWidth][newHeight];
        this.width = newWidth;
        this.height = newHeight;
        this.maxcolours = 255; // Ei osata värisyvyydeltään isompia kuvia.
    }

    @Override
    public int getRGB(int x, int y) {
        assertPixelCoordinatesExist(x, y);
        return this.image[x][y];
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
        assertPixelCoordinatesExist(x, y);
        this.image[x][y] = rgb;
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

    private void assertDimensionsArePositive(int w, int h) {
        if (w < 0 || h < 0) {
            throw new IllegalArgumentException("Image dimensions must be non-negative");
        }
    }

    private void assertPixelCoordinatesExist(int x, int y) {
        if (this.height < y || this.width < x) {
            throw new IllegalArgumentException("Pixel coordinates are out of image bounds");
        }
    }
}
