package it.uniroma3.siw.taskmanager.controller.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.ProjectTag;
import it.uniroma3.siw.taskmanager.service.CommentService;

@Component
public class CommentValidator  implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	
	@Autowired
	CommentService commentService;
	
	@Override
    public void validate(Object o, Errors errors) {
		Comment comment = (Comment) o;
    	String name = comment.getName().trim();

        if (name.isBlank())
            errors.rejectValue("name", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("name", "size");
	}
	
	
    @Override
    public boolean supports(Class<?> clazz) {
        return Comment.class.equals(clazz);
    }
}
