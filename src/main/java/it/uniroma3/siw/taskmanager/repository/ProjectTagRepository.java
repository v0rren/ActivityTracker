package it.uniroma3.siw.taskmanager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.taskmanager.model.ProjectTag;

@Repository
public interface ProjectTagRepository  extends CrudRepository<ProjectTag, Long>{
	
	
    public List<ProjectTag> findByName(String name);


}
