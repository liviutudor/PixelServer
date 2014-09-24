package liv.cookie;

/**
 * Simple interface used to generate a cookie ID.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
public interface CookieGenerator {
    /**
     * The main function in this interface which returns an ID to be used in a
     * cookie. Note that there are no requirements that the generated cookie
     * should be unique (across the system or in a timespan etc).
     *
     * @return String containing a cookie ID
     */
    String generateCookie();
}
