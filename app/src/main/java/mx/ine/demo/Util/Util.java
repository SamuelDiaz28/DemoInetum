package mx.ine.demo.Util;

public class Util {
    public static String TOKEN;

    /**
     * Auxiliares para determinar que una petición necesita enviar el token
     */

    public static final String AUTH_KEY = "@AUTH" + ": YES";

    public static final String AUTH_IPSIDY = "@IPSIDY" + ": YES";
    public static final String AUTH_IECISA = "@IECISA" + ": YES";
    public static final String NO_AUTH = "@NOAUTH" + ": YES";

    /*public static final String AUTH_IPSIDY = "IPSIDY";
    public static final String AUTH_IECISA = "IECISA";*/

    public final static String EMAIL_IPSIDY = "ricardo.sandoval2901@gmail.com";
    public final static String PASSWORD_IPSIDY = "hd26YScnR5PB";

    public final static String BASE_IPSYDY = EMAIL_IPSIDY + ":" + PASSWORD_IPSIDY;

    public final static String EMAIL_IECISA = "poc@inetum.world";
    public final static String PASSWORD_IECISA = "S@abritas123";

    public final static String BASE_IECISA = EMAIL_IECISA + ":" + PASSWORD_IECISA;

}
