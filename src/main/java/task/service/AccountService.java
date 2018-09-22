package task.service;

import java.util.List;

import task.entities.AppRole;
import task.entities.AppUser;

public interface AccountService {
    public AppRole addRole( AppRole role );

    public List<AppUser> getAllUsers();

    public AppUser addUser( AppUser user );

    public AppUser getUserByUsername( String username );

    public AppRole getRoleByRoleName( String roleName );

    public void addRoleToUser( String username, String roleName );
}
