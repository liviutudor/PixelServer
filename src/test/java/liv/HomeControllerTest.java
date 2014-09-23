package liv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

public class HomeControllerTest {
    @Test
    public void testSimpleStuff() {
        assertEquals("1", "1");
    }

    public static HomeController prepareController() throws IOException {
        HomeController hc = new HomeController();
        Resource resImg = new ClassPathResource("img/1x1.gif");
        hc.setPixelResource(resImg);
        return hc;
    }

    @Test
    public void checkNoView() throws Exception {
        HomeController hc = prepareController();
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ModelAndView pixel = hc.pixel(session, request, response);
        assertNull(pixel);
    }
}
