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

import it.uniroma3.siw.taskmanager.controller.validation.ProjectTagValidator;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.ProjectTag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.ProjectTagService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class ProjectTagController {

	@Autowired
	ProjectService projectService;

	@Autowired
	ProjectTagService projectTagService;

	@Autowired
	ProjectTagValidator projectTagValidator;

	@Autowired
	TaskService taskService;

	
	
	
	
	
	
	@RequestMapping(value = "/projects/{projectId}/tag/{tagId}", method = RequestMethod.GET)
	public String tag(@PathVariable Long projectId, @PathVariable Long tagId, Model model) {
		ProjectTag tag = this.projectTagService.getProjectTag(tagId);
		model.addAttribute("tag", tag);
		model.addAttribute("projectId", projectId);
		return "tag";
	}
	
	
	

	
	
	
	@RequestMapping(value = "/project/{projectId}/addTag", method = RequestMethod.GET)
	public String addTagForm(@PathVariable Long projectId, Model model) {
		model.addAttribute("tagForm", new ProjectTag());
		return "addTag";
	}

	
	@RequestMapping(value = "/project/{projectId}/addTag", method = RequestMethod.POST)
	public String addTag(@PathVariable Long projectId, @Valid @ModelAttribute("tagForm") ProjectTag projectTag,
			BindingResult projectTagBindingResult, Model model) {
		Project project = this.projectService.getProject(projectId);
		this.projectTagValidator.validate(projectTag, projectTagBindingResult);
		if (!projectTagBindingResult.hasErrors()) { // Se il projectTag è valido
													// allora controlla che non sia duplicato
			this.projectTagValidator.validateAddTagInProject(projectTag, project, projectTagBindingResult);
			if (!projectTagBindingResult.hasErrors()) {
				project.addTag(projectTag);
				this.projectTagService.saveProjectTagWithProject(projectTag, project);
				return "redirect:/projects/" + projectId;
			}
		}
		return "addTag";
	}
	
	
	
	
	
	
	

	@RequestMapping(value = { "/projects/{projectId}/tasks/{taskId}/addTag" }, method = RequestMethod.GET)
	public String addTagToTaskForm(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		model.addAttribute("tagForm", new ProjectTag());
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId", taskId);
		return "addTagToTask";
	}

	
	@RequestMapping(value = { "/projects/{projectId}/tasks/{taskId}/addTag" }, method = RequestMethod.POST)
	public String addTagToTask(@PathVariable Long projectId, @PathVariable Long taskId,
			@Valid @ModelAttribute("tagForm") ProjectTag projectTag, BindingResult projectTagBindingResult,
			Model model) {
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);

		// Risalgo al tag a partire dal nome (sono sicuro che non ci sono tag con lo
		//									 (stesso nome all'interno di un project.)
		ProjectTag tag = this.projectTagService.getProjectTagInProject(projectTag.getName(), project);
		
		if (!project.getTags().contains(tag)) { // se il progetto non contiene il tag
			projectTagBindingResult.rejectValue("name", "notBelongTo");
		}
		else {
			this.projectTagValidator.validate(tag, projectTagBindingResult);
			if (!projectTagBindingResult.hasErrors()) {// se il tag è valido
														// allora verifica che non sia già nel task
				this.projectTagValidator.validateAddTagInTask(tag, task, projectTagBindingResult);
				if (!projectTagBindingResult.hasErrors()) {
					this.taskService.addTagToTask(task, tag);
					this.projectTagService.addTaskToTag(tag, task);
					return "redirect:/projects/tasks/" + taskId;
				}
			}
		}
		return "addTagToTask";
	}
	
	@RequestMapping(value = { "/project/{projectid}/deleteTag/{tagid}" }, method = RequestMethod.GET)
	public String deletaTag( @PathVariable Long projectid ,@PathVariable Long tagid) {
		
		ProjectTag tag= this.projectTagService.getProjectTag(tagid);
		
		List<Task> tasks = tag.getTasks();

		for (Task task : tasks) {
		     task.getTags().remove(tag);
		}
		this.projectTagService.deleteProjectTag(tag);
		
		
		
		
		return "redirect:/projects/" + projectid;
	}
}
