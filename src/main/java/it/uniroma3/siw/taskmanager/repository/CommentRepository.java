package it.uniroma3.siw.taskmanager.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;

public interface CommentRepository extends CrudRepository<Comment,Long>{

	
	@Transactional
	public Comment findByOwner(User user);
	
    public List<Comment> findByTask(Task task);

}
