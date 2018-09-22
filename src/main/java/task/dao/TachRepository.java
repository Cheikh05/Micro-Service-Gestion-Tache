package task.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import task.entities.Tache;

public interface TachRepository extends JpaRepository<Tache, Long> {

}
