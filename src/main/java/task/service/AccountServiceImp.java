package task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import task.dao.RoleRepository;
import task.dao.UserRepository;
import task.entities.AppRole;
import task.entities.AppUser;

@Service
@Transactional
public class AccountServiceImp implements AccountService {
    @Autowired
    private UserRepository        userRepository;
    @Autowired
    private RoleRepository        roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppRole addRole( AppRole role ) {

        return roleRepository.save( role );
    }

    @Override
    public AppUser addUser( AppUser user ) {
        String hashPW = bCryptPasswordEncoder.encode( user.getPassword() );
        user.setPassword( hashPW );
        return userRepository.save( user );
    }

    @Override
    public List<AppUser> getAllUsers() {
        // TODO Auto-generated method stub
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserByUsername( String username ) {
        // TODO Auto-generated method stub
        return userRepository.findByUsername( username );
    }

    @Override
    public AppRole getRoleByRoleName( String roleName ) {
        // TODO Auto-generated method stub
        return roleRepository.findByRole( roleName );
    }

    @Override
    public void addRoleToUser( String username, String roleName ) {
        AppUser user = userRepository.findByUsername( username );
        AppRole role = roleRepository.findByRole( roleName );
        user.getRoles().add( role );

    }

}
