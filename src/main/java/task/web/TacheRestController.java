package task.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import task.dao.TachRepository;
import task.entities.Tache;

@RestController
public class TacheRestController {
    @Autowired
    private TachRepository taskRepository;

    @GetMapping( "/taches" )
    public List<Tache> tasks() {
        return taskRepository.findAll();
    }

    @PostMapping( "/taches" )
    public Tache save( @RequestBody Tache t ) {
        return taskRepository.save( t );
    }

    @GetMapping( "/tache/{id}" )
    public Tache tache( @PathVariable Long id ) {
        return taskRepository.findById( id ).get();
    }

}
