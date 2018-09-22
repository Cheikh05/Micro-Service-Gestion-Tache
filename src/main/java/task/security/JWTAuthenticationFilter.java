package task.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import task.entities.AppUser;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManagement;

    public JWTAuthenticationFilter( AuthenticationManager authManagement ) {
        super();
        this.authManagement = authManagement;
    }

    /*
     * Cette methode est exécuter juste avant l'authentification par Spring
     * Sécurité
     */
    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response )
            throws AuthenticationException {
        AppUser user = null;
        try {
            user = new ObjectMapper().readValue( request.getInputStream(), AppUser.class );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        return authManagement
                .authenticate( new UsernamePasswordAuthenticationToken( user.getUsername(), user.getPassword() ) );
    }

    /*
     * Cette méthode est utiliser si l'authentification est valider par Spring
     */
    @Override
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult ) throws IOException, ServletException {
        // Récupération de l'utilisateur connecter
        User springUser = (User) authResult.getPrincipal();
        // Génération du token jwt
        String jwt = Jwts.builder()
                .setSubject( springUser.getUsername() )
                .setExpiration( new Date( System.currentTimeMillis() + SecurityContants.EXPIRATION_TIME ) )
                .signWith( SignatureAlgorithm.HS256, SecurityContants.SECRET )
                .claim( "roles", springUser.getAuthorities() )
                .compact();
        // Enregistrer le token dans l'entete de la réponse
        response.setHeader( SecurityContants.HEADER_STRING, SecurityContants.TOKEN_PREFIX + jwt );
    }
}
