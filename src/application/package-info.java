/**
 * Package with <i>"application"</i> classes that implement the {@link Runnable}
 * interface. The {@link Runtime} class selects, instantiates and launches one
 * class calling its {@code run(Properties appProperties, String[] args)} method.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
package application;


/**
 * Class with variables referenced by javadoc.
 */
class package_info {

    /**
     * Author attribute to appear in javadoc.
     */
    static final String Author = "sgraupner";

    /**
     * Version attribute to appear in javadoc.
     */
    static final String Version = "1.0.0-SNAPSHOT";

    /**
     * Explicit private default constructor to avoid javadoc warning
     * when compiled with the 'mk javadoc -private' option to include
     * protected and private methods).
     */
    private package_info() { }
}