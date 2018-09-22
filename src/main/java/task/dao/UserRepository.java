package task.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import task.entities.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    public AppUser findByUsername( String username );
}
