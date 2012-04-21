package riokuva;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageEditor {
    private int numOfThreads;
    private ArrayList<Thread> threads;

    public ImageEditor(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }
    
    public void sharpen(PixelImage image, PixelImage newImage) {
        this.threads = new ArrayList<Thread>();

        //aloitetaan numOfThreadsin mukainen määrä sharpen-prosesseja
        for (int i = 0; i < this.numOfThreads; i++) {
            Thread thread = new SharpenThread(image, newImage, i + 1, this.numOfThreads);
            this.threads.add(thread);
            thread.start();
        }

        this.joinThreads();
    }

    public void blur(PixelImage image, PixelImage newImage) {
        this.threads = new ArrayList<Thread>();

        //aloitetaan numOfThreadsin mukainen määrä blur-prosesseja
        for (int i = 0; i < this.numOfThreads; i++) {
            Thread thread = new BlurThread(image, newImage, i + 1, this.numOfThreads);
            this.threads.add(thread);
            thread.start();
        }
        
        this.joinThreads();
    }
    
    //odotetaan että kaikki prosessit ovat valmiita
    private void joinThreads() {
        for (Thread thread : this.threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImageEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
