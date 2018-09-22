package task.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import task.entities.AppRole;

public interface RoleRepository extends JpaRepository<AppRole, Long> {
    public AppRole findByRole( String role );
}
