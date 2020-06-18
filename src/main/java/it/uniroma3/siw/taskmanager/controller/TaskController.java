package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

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
import it.uniroma3.siw.taskmanager.controller.validation.TaskValidator;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class TaskController {

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	ProjectService projectService;

	@Autowired
	TaskService taskService;
	@Autowired
	CommentService commentService;

	@Autowired
	UserService userService;

	@Autowired
	TaskValidator taskValidator;

	@Autowired
	SessionData sessionData;

	@RequestMapping(value = { "/projects/tasks/{taskId}" }, method = RequestMethod.GET)
	public String task(Model model, @PathVariable Long taskId) {
		Task task = this.taskService.getTask(taskId);
        List<Comment> commentsList = commentService.retrieveCommentsOwnedBy(task);

		if (task == null)
			return "redirect:/projects";
		model.addAttribute("task", task);
		model.addAttribute("commentsList", commentsList);
		return "task";
	}

	@RequestMapping(value = { "/projects/{projectId}/tasks/add" }, method = RequestMethod.GET)
	public String createTaskForm(Model model, @PathVariable Long projectId) {
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("taskForm", new Task());
		model.addAttribute("projectId", projectId);
		return "addTask";
	}

	@RequestMapping(value = { "/projects/{projectId}/tasks/add" }, method = RequestMethod.POST)
	public String createTask(@Valid @ModelAttribute("taskForm") Task task, @PathVariable Long projectId,
			BindingResult taskBindingResult, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project currentProject = projectService.getProject(projectId);
		taskValidator.validate(task, taskBindingResult);
		if (!taskBindingResult.hasErrors()) {
			currentProject.addTask(task);
			task.setProject(currentProject);
			this.taskService.saveTask(task);
			return "redirect:/projects/tasks/" + task.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addTask";
	}
	
	@RequestMapping(value = { "/tasks/remove/{taskId}" }, method = RequestMethod.GET)
	public String deleteTask(@PathVariable Long taskId, Model model) {
		Task taskToDelete = this.taskService.getTask(taskId);
		
		this.taskService.deleteTask(taskToDelete,taskToDelete.getProject());
		return "redirect:/projects";

	}

	@RequestMapping(value = { "/tasks/update/{taskId}" }, method = RequestMethod.GET)
	public String updateTaskForm(@PathVariable Long taskId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Task task = taskService.getTask(taskId);
		if (loggedUser.getId() == task.getProject().getOwner().getId()) {
			model.addAttribute("taskForm", task);
			return "updateTask";
		} else
			return "noPermission";
	}

	@RequestMapping(value = { "/tasks/update/{taskId}" }, method = RequestMethod.POST)
	public String updateTask(@PathVariable Long taskId, @Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult, Model model) {
		taskValidator.validate(task, taskBindingResult);
		if (!taskBindingResult.hasErrors()) {
			Task taskToUpdate = this.taskService.getTask(taskId);
			taskToUpdate.setDescription(task.getDescription());
			taskToUpdate.setName(task.getName());
			taskToUpdate.setCompleted(task.isCompleted());
			this.taskService.saveTask(taskToUpdate);
			return "redirect:/projects";
		}
		return "redirect:/tasks/update/" + taskId;

	}

	@RequestMapping(value = { "/tasks/assignuser/{taskId}" }, method = RequestMethod.GET)
	public String assignTaskForm(@PathVariable Long taskId, Model model) {

		model.addAttribute("taskid", taskId);
		model.addAttribute("memberForm", new Credentials());

		return "assignTask";
	}


	@RequestMapping(value = { "/tasks/assignuser/{taskId}" }, method = RequestMethod.POST)
	public String assignTask(@PathVariable Long taskId, @Valid @ModelAttribute("memberForm") Credentials credentials,
			BindingResult credentialsBindingResult, Model model) {
		Credentials c = this.credentialsService.getCredentials(credentials.getUserName());

		if (c == null) {
			credentialsBindingResult.rejectValue("userName", "notExists");
			model.addAttribute("taskid", taskId);
			return "assignTask";

		}
		Task task = this.taskService.getTask(taskId);
		List<User> members = task.getProject().getMembers();
		User user = credentialsService.getCredentials(credentials.getUserName()).getUser();
		if (members.contains(user)) {
			task.setAssignedTo(user);
			this.taskService.saveTask(task);
			return "redirect:/projects";
		} else {
			credentialsBindingResult.rejectValue("userName", "notExistsShared");
			model.addAttribute("taskid", taskId);
			return "assignTask";
		}
	}
}
