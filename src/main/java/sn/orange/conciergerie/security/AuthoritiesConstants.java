package sn.orange.conciergerie.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CLIENT = "ROLE_CLIENT";

    public static final String PARTENAIRE = "ROLE_PARTENAIRE";

    public static final String SUPERVISEUR = "ROLE_SUPERVISEUR";
    
    private AuthoritiesConstants() {}
}
