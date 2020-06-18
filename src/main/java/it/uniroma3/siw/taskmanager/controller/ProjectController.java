package it.uniroma3.siw.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

import javax.validation.Valid;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;

	@Autowired
	ProjectValidator projectValidator;

	@Autowired
	SessionData sessionData;

	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);

		return "myOwnedProjects";
	}

	@RequestMapping(value = { "/projects/{projectId}" }, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		User loggedUser = sessionData.getLoggedUser();
		if (project == null)
			return "redirect:/projects";
		List<User> members = userService.getMembers(project);
		if (!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		if (!project.getOwner().equals(loggedUser))
			return "sharedProject";
		else return "project";
	}

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";
	}

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		projectValidator.validate(project, projectBindingResult);
		if (!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";
	}

	@RequestMapping(value = { "/projects/remove/{projectId}" }, method = RequestMethod.GET)
	public String deleteProject(@PathVariable Long projectId, Model model) {
		Project projectToDelete = this.projectService.getProject(projectId);
		this.projectService.deleteProject(projectToDelete);
		return "redirect:/projects";

	}

	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.GET)
	public String updateProjectForm(@PathVariable Long projectId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();

		if (loggedUser.getId() == projectService.getProject(projectId).getOwner().getId()) {
			model.addAttribute("projectForm", projectService.getProject(projectId));
			return "updateProject";
		} else
			return "noPermission";
	}

	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.POST)
	public String updateProject(@PathVariable Long projectId, @Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult, Model model) {

		projectValidator.validate(project, projectBindingResult);
		if (!projectBindingResult.hasErrors()) {
			Project projectToUpdate = this.projectService.getProject(projectId);
			projectToUpdate.setDate(project.getDate());
			projectToUpdate.setDescription(project.getDescription());
			projectToUpdate.setName(project.getName());
			this.projectService.saveProject(projectToUpdate);
			return "redirect:/projects";
		}
		return "redirect:/projects/update/" + projectId;

	}

	@RequestMapping(value = { "/projects/sharedProjects" }, method = RequestMethod.GET)
	public String sharedProjects(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		List<Project> sharedProjectsList = this.projectService.retrieveVisibleProjects(loggedUser);
		model.addAttribute("sharedProjectsList", sharedProjectsList);
		return "sharedProjects";
	}

	@RequestMapping(value = { "/project/share/{projectId}" }, method = RequestMethod.GET)
	public String shareProjectForm(@PathVariable Long projectId, Model model) {
		model.addAttribute("memberForm", new Credentials());
		return "addMember";
	}

	@RequestMapping(value = { "/project/share/{projectId}" }, method = RequestMethod.POST)
	public String shareProject(@PathVariable Long projectId,
			@Valid @ModelAttribute("memberForm") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {
		Project project = this.projectService.getProject(projectId);
		Credentials c = this.credentialsService.getCredentials(credentials.getUserName());
		if (c == null)
			credentialsBindingResult.rejectValue("userName", "notExists");
		else {
			User u = c.getUser();
			this.projectValidator.validateMember(u, project, credentialsBindingResult);
			if (!credentialsBindingResult.hasErrors()) {
				this.projectService.shareProjectWithUser(project, u);
				return "redirect:/projects";
			}
		}
		return "addMember";
	}

}
