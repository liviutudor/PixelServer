package liv.cookie;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Cookie generator which uses an incrementing long to generate a cookie id. At
 * each call it increments this long and returns its value as a String.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
public class SequentialCookieGenerator implements CookieGenerator {
    /**
     * The counter we use to generate the cookie. At each step (call to
     * {@link #generateCookie()} we increment this.
     */
    private AtomicLong currentGenerator;

    /**
     * Default constructor. Simply initializes {@link #currentGenerator the
     * counter} and sets it to 0. This is in order to prevent any cookie with a
     * value of zero -- simply because zero (0) typically means "nothing" and we
     * do want our cookies to be meaningful rather than represent "nothing" :-)
     * Setting it to zero ensures that first increment will generate 1 as a
     * value.
     */
    public SequentialCookieGenerator() {
        currentGenerator = new AtomicLong();
        currentGenerator.set(0L); // we don't want any user zero :D
    }

    /**
     * The "beef" in this class. Increments {@link #currentGenerator counter}
     * and returns its value as a string.
     *
     * @return value of our counter <b>after</b> incrementing it (by 1).
     */
    @Override
    public final String generateCookie() {
        return String.valueOf(currentGenerator.incrementAndGet());
    }

}
