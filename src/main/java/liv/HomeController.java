package liv;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The main (and only) controller in our app.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
@Controller
public class HomeController {
    /**
     * Default buffer size used when reading the 1x1 pixel file (to cache in
     * memory). We don't expect the GIF image to be larger than 4k so initial
     * buffer will be 4k.
     */
    private static final int    DEFAULT_BUFFER_SIZE = 4096;

    /**
     * Content type to be served for the 1x1 transparent pixel. We use GIF
     * image.
     */
    private static final String GIF_CONTENT_TYPE    = "image/gif";

    /** Caches the gif image (1x1) transparent pixel. */
    private byte[]              pixelBytes          = null;

    @RequestMapping(value = "/pixel", method = RequestMethod.GET)
    public ModelAndView pixel(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        writePixelToResponse(response);
        return null;
    }

    /**
     * Writes the actual bytes of the transparent pixel to the output stream.
     * Needs to have {@link #pixelBytes} set otherwise, a
     * <code>NullPointerException</code> will be thrown.
     *
     * @param response
     *            HTTP response object
     * @throws IOException
     *             thrown if any I/O errors occur sending the data
     */
    private void writePixelToResponse(HttpServletResponse response) throws IOException {
        if (pixelBytes == null) {
            throw new NullPointerException("No transparent pixel found");
        }
        response.setContentType(GIF_CONTENT_TYPE);
        response.setContentLength(pixelBytes.length);
        ServletOutputStream out = response.getOutputStream();
        out.write(pixelBytes);
        out.flush();
    }

    /**
     * Used internally to read the 1x1 transparent gif. NOTE: We know upfront
     * that this GIF file is small enough to be kept in memory, so we can afford
     * to limit the memory allocation to a decent size. This will be done once,
     * typically when the servlet is initialized in the Spring context.
     *
     * @param resourceLocation
     *            Name of the gif file (file path)
     * @return a byte array containing the contents of the GIF file.
     */
    private byte[] readResourcePixel(Resource resourceLocation) throws IOException {
        ByteArrayOutputStream baos = null;
        if (resourceLocation != null) {
            baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
            FileInputStream fstream = null;
            try {
                fstream = new FileInputStream(resourceLocation.getFile());
                DataInputStream in = new DataInputStream(fstream);
                while (in.available() != 0) {
                    baos.write(in.readByte());
                }
                baos.flush();
                in.close();
            } finally {
                /* Close stream at the end as a good practice. */
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (IOException e) {
                    // nothing to do here
                }
            }
        } else {
            throw new IOException("No gifLocation given!");
        }
        return baos.toByteArray();
    }

    /**
     * Sets the path to the transparent 1x1 pixel. This triggers a call to
     * {@link #readResourcePixel(Resource)}.
     *
     * @param pixelResource
     *            Path/URL to transparent 1x1 gif.
     * @throws IOException
     *             if errors occur reading the image file
     */
    public void setPixelResource(Resource pixelResource) throws IOException {
        pixelBytes = readResourcePixel(pixelResource);
    }

}