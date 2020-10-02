package liv.pixelserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import liv.cookie.CookieGenerator;

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
public final class HomeController
{
    /**
     * Default buffer size used when reading the 1x1 pixel file (to cache in
     * memory). We don't expect the GIF image to be larger than 1k so initial
     * buffer will be 1k.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * Content type to be served for the 1x1 transparent pixel. We use GIF
     * image.
     */
    private static final String GIF_CONTENT_TYPE    = "image/gif";

    /** Caches the gif image (1x1) transparent pixel. */
    private byte[]  pixelBytes   = null;

    /** Used to generate a cookie for new users. */
    private CookieGenerator cookieGenerator;

    /**
     * The main entry point into this application. Provides the
     * <code>/pixel</code> entry point for HTTP calls.
     *
     * @param session
     *            HTTP session
     * @param request
     *            HTTP request
     * @param response
     *            HTTP response
     * @return always returns null
     * @throws IOException
     *             if any I/O errors occur
     */
    @RequestMapping(value = "/pixel", method = RequestMethod.GET)
    public ModelAndView pixel(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException 
    {
        preventClientCaching(response);
        cookieManagement(request, response);
        writePixelToResponse(response);
        return null;
    }

    /**
     * Sends to the client all the necessary HTTP headers to prevent the client
     * from caching the request.
     *
     * @param response
     *            HTTP request object
     */
    private void preventClientCaching(HttpServletResponse response) 
    {
        response.addHeader("Cache-control", "no-cache,must-revalidate");
        response.addHeader("Expires", "-1");
        response.setHeader("Pragma", "no-cache");
    }

    /**
     * This deals with cookie-ing the user: if the user doesn't have a cookie
     * from us then set it.
     *
     * @param request
     *            HTTP request where we check if the user has a cookie or not
     * @param response
     *            HTTP response where we set the cookie if needed
     */
    private void cookieManagement(HttpServletRequest request, HttpServletResponse response) {

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
        if (pixelBytes == null) 
        {
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
     * @throws IOException
     *             if any I/O errors occur reading the pixel file
     */
    private byte[] readResourcePixel(Resource resourceLocation) throws IOException {
        ByteArrayOutputStream baos = null;
        if (resourceLocation != null) 
        {
            baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
            try (DataInputStream in = new DataInputStream(new FileInputStream(resourceLocation.getFile()))) {
                while (in.available() != 0) 
                {
                    baos.write(in.readByte());
                }
                baos.flush();
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

    /**
     * Sets the cookie generator to use when returning a response to new users.
     * For each new user we need to generate a cookie and send it in the
     * response and this instance will be used to generate the cookie value.
     *
     * @param cookieGenerator
     *            Cookie generator to use on new users
     */
    public void setCookieGenerator(CookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }
}
