package it.uniroma3.siw.taskmanager.controller.validation;


import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.ProjectTag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.ProjectTagService;

@Component
public class ProjectTagValidator  implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Autowired
	ProjectTagService projectTagService;

	
    @Override
    public void validate(Object o, Errors errors) {
    	ProjectTag projectTag = (ProjectTag) o;
    	String name = projectTag.getName().trim();
    	String description = projectTag.getDescription().trim();

        if (name.isEmpty())
            errors.rejectValue("name", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("name", "size");
        
        if (description.length() > MAX_DESCRIPTION_LENGTH)
            errors.rejectValue("description", "size");
    }
    
    
    
    
    public void validateAddTagInProject(ProjectTag pt, Project p, Errors errors) {
    	if(this.projectTagService.getProjectTagInProject(pt.getName(), p) != null){//il nome è gia usato
    		errors.rejectValue("name", "duplicate");
    	}
    }
    
    
    
    
    public void validateAddTagInTask(ProjectTag pt, Task t, Errors errors) {
    	if(this.projectTagService.getProjectTagInTask(pt.getName(), t) != null){//il nome è gia usato
    		errors.rejectValue("name", "duplicate");
    	}
    }

    
    
    
	@Override
	public boolean supports(Class<?> clazz) {
		return ProjectTag.class.equals(clazz);
	}
}
