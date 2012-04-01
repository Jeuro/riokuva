package riokuva;

import java.util.ArrayList;

public class ImageEditor {
    private int numOfThreads;
    private ArrayList<Thread> threads;

    public ImageEditor(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }
    
    public PixelImage sharpen(PixelImage image, PixelImage newImage) {
        this.threads = new ArrayList();
        
        for (int i = 0; i < this.numOfThreads; i++) {
            
        }
        
        return newImage;
    }
    
    public PixelImage blur(PixelImage image, PixelImage newImage) {
        this.threads = new ArrayList(); 
        
        for (int i = 0; i < this.numOfThreads; i++) {
            
        }
        
        return newImage;
    }    
}
