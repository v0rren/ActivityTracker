package it.uniroma3.siw.taskmanager.controller.validation;


import org.springframework.validation.Validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;

@Component
public class ProjectValidator implements Validator{

	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;

	
    @Override
    public void validate(Object o, Errors errors) {
    	Project project = (Project) o;
    	String name = project.getName().trim();
    	String description = project.getDescription();


        if (name.isBlank())
            errors.rejectValue("name", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("name", "size");

        else if (description.length() > MAX_DESCRIPTION_LENGTH)
            errors.rejectValue("description", "size");
        


    }
    
    public void validateMember(User member, Project project, Errors errors) {
        if(member.equals(project.getOwner()))
            errors.rejectValue("userName", "isOwner");
        if(project.getMembers().contains(member))
            errors.rejectValue("userName", "duplicate");
    }

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}
	
}
