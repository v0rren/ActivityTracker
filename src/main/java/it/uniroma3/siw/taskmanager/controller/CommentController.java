package it.uniroma3.siw.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.CommentValidator;
import it.uniroma3.siw.taskmanager.controller.validation.TaskValidator;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class CommentController {

	@Autowired
	CommentService commentService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	ProjectService projectService;

	@Autowired
	TaskService taskService;

	@Autowired
	UserService userService;

	@Autowired
	CommentValidator commentValidator;

	@Autowired
	SessionData sessionData;

	@RequestMapping(value = { "/tasks/{taskId}/comment" }, method = RequestMethod.GET)
	public String addCommenForm(@PathVariable Long taskId, Model model) {

		model.addAttribute("taskId", taskId);
		model.addAttribute("CommentForm", new Comment());
		return "addComment";
	}

	@RequestMapping(value = { "/tasks/{taskId}/comment" }, method = RequestMethod.POST)
	public String addComment( @PathVariable Long taskId,
			@Valid @ModelAttribute("CommentForm") Comment comment, BindingResult commentBindingResult, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Task task = this.taskService.getTask(taskId);

		commentValidator.validate(comment, commentBindingResult);
		if (!commentBindingResult.hasErrors()) {
			commentService.saveComment(comment, loggedUser, task);
			return "redirect:/projects";
		}

		return "addComment";
	}

}
