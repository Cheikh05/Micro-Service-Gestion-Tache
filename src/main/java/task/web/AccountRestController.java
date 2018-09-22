package task.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import task.entities.AppUser;
import task.entities.RegisterForm;
import task.service.AccountService;

@RestController
public class AccountRestController {
    @Autowired
    private AccountService service;

    @PostMapping( "/register" )
    public AppUser saveUser( @RequestBody RegisterForm userForm ) {
        if ( !userForm.getPassword().equals( userForm.getRepassword() ) )
            throw new RuntimeException( "Les deux mot de passes ne sont pas égaux" );
        AppUser usr = service.getUserByUsername( userForm.getUsername() );
        if ( usr != null )
            throw new RuntimeException( "Cet Username existe deja" );
        AppUser user = new AppUser();
        user.setUsername( userForm.getUsername() );
        user.setEmail( userForm.getEmail() );
        user.setPassword( userForm.getPassword() );

        service.addUser( user );
        service.addRoleToUser( user.getUsername(), "USER" );
        return user;
    }

}
