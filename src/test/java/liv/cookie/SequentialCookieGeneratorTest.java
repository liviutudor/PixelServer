package liv.cookie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for {@link SequentialCookieGenerator}.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
public class SequentialCookieGeneratorTest {
    @Test
    public void checkSequentialNumbers() {
        // test we generate sequential integers
        int curr = 1;
        SequentialCookieGenerator gen = new SequentialCookieGenerator();
        String val = null;
        while (curr < 100) {
            val = gen.generateCookie();
            assertEquals(val, String.valueOf(curr));
            curr++;
        }
    }

    @Test
    public void checkTwoInstancesDontUseSameSeed() {
        // ensure that 2 separate instances don't interfere with each other
        SequentialCookieGenerator gen1 = new SequentialCookieGenerator();
        SequentialCookieGenerator gen2 = new SequentialCookieGenerator();
        int start1 = 11;
        int start2 = 23;
        // increment first 10 times and verify it's ok
        for (int i = 0; i < start1 - 1; i++) {
            gen1.generateCookie();
        }
        String v1 = gen1.generateCookie();
        assertEquals(v1, String.valueOf(start1));

        // increment second 22 times and verify it's ok
        for (int i = 0; i < start2 - 1; i++) {
            gen2.generateCookie();
        }
        String v2 = gen2.generateCookie();
        assertEquals(v2, String.valueOf(start2));

        /*
         * now proceed to verify incrementing one by one doesn't affect each
         * other
         */
        for (int i = 1; i <= 100; i++) {
            v1 = gen1.generateCookie();
            v2 = gen2.generateCookie();
            assertEquals(v1, String.valueOf(start1 + i));
            assertEquals(v2, String.valueOf(start2 + i));
        }
    }

    @Test
    public void checkFirstEntryIsOne() {
        // ensure first id is 1 not 0
        SequentialCookieGenerator gen = new SequentialCookieGenerator();
        String val = gen.generateCookie();
        assertEquals(val, "1");
    }
}
