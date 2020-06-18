package it.uniroma3.siw.taskmanager.repository;
import it.uniroma3.siw.taskmanager.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is a CrudRepository for repository operations on Tasks.
 *
 * @see Task
 */
@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

}

