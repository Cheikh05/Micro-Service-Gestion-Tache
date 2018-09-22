package task.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class TaskSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService    userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        // Definit la facon de récupération des users

        /*
         * Cette méthode permet de déléguer la gestion de l'authenfication au
         * Service UserDetailsService et va faire appelle a sa methode
         * loadUserByUsername pour charger l'utilisateur et ses roles. On va
         * creer une classe implémentant cette Interface et on va redefinir la
         * méthode loadUserByUsername
         */

        auth.userDetailsService( userDetailsService )
                .passwordEncoder( bCryptPasswordEncoder );

    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        // http.formLogin();
        // Authentication JWT
        http.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );
        http.addFilter( new JWTAuthenticationFilter( authenticationManager() ) );
        http.addFilterBefore( new JWTBeforeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );
        /*
         * Permet de désactiver la verification du token csrf que le serveur a
         * generé
         */
        http.csrf().disable();
        http.authorizeRequests().antMatchers( "/login/**", "/register/**" ).permitAll();
        http.authorizeRequests().antMatchers( HttpMethod.GET, "/taches" ).hasAuthority( "USER" );
        http.authorizeRequests().antMatchers( HttpMethod.POST, "/taches/**" ).hasAuthority( "ADMIN" );

        /*
         * Indique a spring sécurity que toutes les requétes nécéssite une
         * authentification
         */
        http.authorizeRequests().anyRequest().authenticated();
    }
}
