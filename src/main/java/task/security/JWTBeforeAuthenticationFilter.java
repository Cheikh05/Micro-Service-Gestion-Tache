package task.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/*
 * Cette classe s'éxécutera a chaque requete 
 * pour verifier la vérification de la signature du token
 * */
public class JWTBeforeAuthenticationFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
            throws ServletException, IOException {

        /* Autorisations */

        response.addHeader( "Access-Control-Allow-Origin", "*" );
        response.addHeader( "Access-Control-Allow-Headers",
                "Origin,"
                        + "Accept,"
                        + "X-Requested-With,"
                        + "Content-Type,"
                        + "Access-Control-Request-Method,"
                        + "Access-Control-Request-Headers,"
                        + "Authorization" );

        response.addHeader( "Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,authorization,authorization" );

        if ( request.getMethod().equals( "OPTIONS" ) ) {
            response.setStatus(
                    HttpServletResponse.SC_OK );
        } else {
            String token_jwt = request.getHeader( SecurityContants.HEADER_STRING );
            if ( token_jwt == null || !token_jwt.startsWith( SecurityContants.TOKEN_PREFIX ) ) {
                filterChain.doFilter( request, response );
                return;
            }
            // Récupérer le contenu du token
            Claims claims = Jwts.parser()
                    .setSigningKey( SecurityContants.SECRET )
                    .parseClaimsJws( token_jwt.replace( SecurityContants.TOKEN_PREFIX, "" ) )
                    .getBody();
            // Récupération de l'utilisateur a partir du jwt
            String username = claims.getSubject();
            // Récupération des roles de l'utilisateur a partir du jwt
            ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get( "roles" );
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach( r -> {
                authorities.add( new SimpleGrantedAuthority( r.get( "authority" ) ) );
            } );

            UsernamePasswordAuthenticationToken authenticationUser = new UsernamePasswordAuthenticationToken( username,
                    null,
                    authorities );
            SecurityContextHolder.getContext().setAuthentication( authenticationUser );
            filterChain.doFilter( request, response );
        }

    }

}
