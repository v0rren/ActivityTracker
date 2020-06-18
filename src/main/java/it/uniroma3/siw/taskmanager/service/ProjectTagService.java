package it.uniroma3.siw.taskmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.ProjectTag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.repository.ProjectTagRepository;

@Service
public class ProjectTagService {
	
	@Autowired
	protected ProjectTagRepository projectTagRepository;
	
    @Transactional
    public ProjectTag getProjectTag(long id) {
        Optional<ProjectTag> result = this.projectTagRepository.findById(id);
        return result.orElse(null);
    }
    
    @Transactional
    public ProjectTag getProjectTagInProject(String name, Project project) {
    	List<ProjectTag> result = this.projectTagRepository.findByName(name);
    	for(ProjectTag t : result) {
    		if(project.getTags().contains(t))
    			return t;
    	}
    	return null;
    }
    
    
    @Transactional
    public ProjectTag getProjectTagInTask(String name, Task task) {
    	List<ProjectTag> result = this.projectTagRepository.findByName(name);
    	for(ProjectTag t : result) {
    		if(task.getTags().contains(t))
    			return t;
    	}
    	return null;
    }
	   
	   
    /**
     * This method saves a Task in the DB.
     * @param task the Task to save into the DB
     * @return the saved Task
     */
    @Transactional
    public ProjectTag saveProjectTag(ProjectTag projectTag) {
        return this.projectTagRepository.save(projectTag);
    }


    @Transactional
    public ProjectTag saveProjectTagWithProject(ProjectTag projectTag, Project project) {
    	projectTag.setProject(project);
        return this.projectTagRepository.save(projectTag);
    }

    @Transactional
    public ProjectTag addTaskToTag(ProjectTag projectTag, Task task) {
    	projectTag.getTasks().add(task);
        return this.projectTagRepository.save(projectTag);
        
    }
    
    /**
     * This method deletes a Task from the DB.
     * @param task the Task to delete from the DB
     */
    @Transactional
    public void deleteProjectTag(ProjectTag projectTag) {
        this.projectTagRepository.delete(projectTag);
    }
}
