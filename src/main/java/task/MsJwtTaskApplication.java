package task;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import task.dao.TachRepository;
import task.entities.AppRole;
import task.entities.AppUser;
import task.entities.Tache;
import task.service.AccountService;

@SpringBootApplication
public class MsJwtTaskApplication implements CommandLineRunner {
    @Autowired
    private TachRepository taskRepository;
    @Autowired
    private AccountService accountService;

    public static void main( String[] args ) {
        SpringApplication.run( MsJwtTaskApplication.class, args );
    }

    @Bean
    public BCryptPasswordEncoder getBCPE() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run( String... args ) throws Exception {
        AppRole rAdmin = accountService.addRole( new AppRole( null, "ADMIN" ) );
        AppRole rUser = accountService.addRole( new AppRole( null, "USER" ) );
        AppUser user1 = accountService
                .addUser( new AppUser( null, "cheikh", "cheikh", "cheikh@gail.com", null ) );
        AppUser user2 = accountService.addUser( new AppUser( null, "kheush", "kheush", "kheush@gail.com", null ) );
        accountService.addRoleToUser( "cheikh", rAdmin.getRole() );
        accountService.addRoleToUser( "cheikh", rUser.getRole() );
        accountService.addRoleToUser( "kheush", rUser.getRole() );
        Stream.of( "Tache1", "Tache2", "Tache3", "Tache4" )
                .forEach( ( t ) -> {
                    taskRepository.save( new Tache( null, t ) );
                } );
        taskRepository.findAll().forEach( ( t ) -> {
            System.out.println( t.getNameTask() );
        } );

        accountService.getAllUsers().forEach( ( user ) -> {
            System.out.println( user.getUsername() + " : " + user.getPassword() );
        } );

    }
}
