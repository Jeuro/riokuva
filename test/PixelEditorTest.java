
import riokuva.PpmImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import riokuva.ImageProcessingMain;
import riokuva.PixelEditor;
import static org.junit.Assert.*;

public class PixelEditorTest {

    PpmImage kuva;

    public PixelEditorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        kuva = new PpmImage(3, 3);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void blurSailyttaaMustan() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 0);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 0);
        kuva.setRGB(1, 1, 0);
        kuva.setRGB(1, 2, 0);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 0);
        kuva.setRGB(2, 2, 0);

        assertEquals(0, PixelEditor.blurPixel(kuva, 1, 1));

    }

    @Test
    public void blurSailyttaaValkoisen() {
        kuva.setRGB(0, 0, 16777215);
        kuva.setRGB(0, 1, 16777215);
        kuva.setRGB(0, 2, 16777215);
        kuva.setRGB(1, 0, 16777215);
        kuva.setRGB(1, 1, 16777215);
        kuva.setRGB(1, 2, 16777215);
        kuva.setRGB(2, 0, 16777215);
        kuva.setRGB(2, 1, 16777215);
        kuva.setRGB(2, 2, 16777215);

        assertEquals(16777215, PixelEditor.blurPixel(kuva, 1, 1));

    }

    @Test
    public void blurValkoinenYmparoiMustan() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 0);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 0);
        kuva.setRGB(1, 1, 16777215);
        kuva.setRGB(1, 2, 0);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 0);
        kuva.setRGB(2, 2, 0);

        assertEquals(8355711, PixelEditor.blurPixel(kuva, 1, 1));

    }

    @Test
    public void blurMustaYmparoiValkoisen() {
        kuva.setRGB(0, 0, 16777215);
        kuva.setRGB(0, 1, 16777215);
        kuva.setRGB(0, 2, 16777215);
        kuva.setRGB(1, 0, 16777215);
        kuva.setRGB(1, 1, 0);
        kuva.setRGB(1, 2, 16777215);
        kuva.setRGB(2, 0, 16777215);
        kuva.setRGB(2, 1, 16777215);
        kuva.setRGB(2, 2, 16777215);

        assertEquals(8355711, PixelEditor.blurPixel(kuva, 1, 1));

    }

    @Test
    public void blurSekavari() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 3289650);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 1651275);
        kuva.setRGB(1, 1, 6617650);
        kuva.setRGB(1, 2, 13107200);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 6560075);
        kuva.setRGB(2, 2, 0);

        assertEquals(6327346, PixelEditor.blurPixel(kuva, 1, 1));

    }
    
    // Tästä eteenpäin sharpen testit, jotka testaavat, että homma toimii
    // kirjan antaman kaavan mukaan. Testit eivät ota kantaa siihen,
    // että kaava on ihan susi.

    @Test
    public void sharpenSailyttaaMustan() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 0);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 0);
        kuva.setRGB(1, 1, 0);
        kuva.setRGB(1, 2, 0);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 0);
        kuva.setRGB(2, 2, 0);

        assertEquals(0, PixelEditor.sharpenPixel(kuva, 1, 1));

    }

    @Test
    public void sharpenValkoisestaMusta() {
        kuva.setRGB(0, 0, 16777215);
        kuva.setRGB(0, 1, 16777215);
        kuva.setRGB(0, 2, 16777215);
        kuva.setRGB(1, 0, 16777215);
        kuva.setRGB(1, 1, 16777215);
        kuva.setRGB(1, 2, 16777215);
        kuva.setRGB(2, 0, 16777215);
        kuva.setRGB(2, 1, 16777215);
        kuva.setRGB(2, 2, 16777215);

        assertEquals(0, PixelEditor.sharpenPixel(kuva, 1, 1));

    }

    @Test
    public void sharpenValkoinenYmparoiMustan() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 0);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 0);
        kuva.setRGB(1, 1, 16777215);
        kuva.setRGB(1, 2, 0);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 0);
        kuva.setRGB(2, 2, 0);

        assertEquals(8355711, PixelEditor.sharpenPixel(kuva, 1, 1));;

    }

    @Test
    public void sharpenMustaYmparoiValkoisen() {
        kuva.setRGB(0, 0, 16777215);
        kuva.setRGB(0, 1, 16777215);
        kuva.setRGB(0, 2, 16777215);
        kuva.setRGB(1, 0, 16777215);
        kuva.setRGB(1, 1, 0);
        kuva.setRGB(1, 2, 16777215);
        kuva.setRGB(2, 0, 16777215);
        kuva.setRGB(2, 1, 16777215);
        kuva.setRGB(2, 2, 16777215);

        assertEquals(0, PixelEditor.sharpenPixel(kuva, 1, 1));

    }

    @Test
    public void sharpenSekavari() {
        kuva.setRGB(0, 0, 0);
        kuva.setRGB(0, 1, 3289650);
        kuva.setRGB(0, 2, 0);
        kuva.setRGB(1, 0, 1651275);
        kuva.setRGB(1, 1, 6617650);
        kuva.setRGB(1, 2, 13107200);
        kuva.setRGB(2, 0, 0);
        kuva.setRGB(2, 1, 6560075);
        kuva.setRGB(2, 2, 0);

        assertEquals(224512, PixelEditor.sharpenPixel(kuva, 1, 1));

    }
}
