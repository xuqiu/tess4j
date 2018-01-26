package xuqiu.github.io;

import com.sun.jna.Pointer;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.util.ImageIOHelper;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class OCRUtil {
    ITessAPI.TessBaseAPI handle;
    public static String getString(BufferedImage image, String lang) throws Exception {
        ITessAPI.TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPIInit3(handle, "src/main/resources", lang);
        String result;

            TessAPI1.TessBaseAPISetPageSegMode(handle, 7);
            Pointer utf8Text = TessAPI1.TessBaseAPIRect(handle, buf, bytespp, bytespl, 0, 0, image.getWidth(), image.getHeight());
            result = utf8Text.getString(0);
            TessAPI1.TessDeleteText(utf8Text);

        TessAPI1.TessBaseAPIDelete(handle);
        return result;
    }

}
