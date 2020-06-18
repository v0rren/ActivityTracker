package it.uniroma3.siw.taskmanager.controller;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.UserRepository;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserValidator userValidator;
    
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;
    
    @Autowired
    CredentialsService credentialsService;
    
    @Autowired
    ProjectService projectService;

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials c = sessionData.getLoggedCredentials();
        model.addAttribute("userCredentials", c);
        model.addAttribute("loggedUser", loggedUser);
        return "home";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("user", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    
    @RequestMapping(value = {"/admin/users"}, method = RequestMethod.GET)
    public String usersList(Model model) {
    	User loggedUser = this.sessionData.getLoggedUser();
    	List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("credentialsList", allCredentials);
    	return "allUsers";
    }
    
    
    @RequestMapping(value = {"/admin/users/{username}/delete"}, method = RequestMethod.POST)
    public String removeUser(Model model, @PathVariable String username) {
    	Credentials c = this.credentialsService.getCredentials(username);
    	User u = c.getUser();
    	this.userService.deleteMemberFromProjects(u);
    	this.credentialsService.deleteCredentials(username);
    	return "redirect:/admin/users";
    }

    @RequestMapping(value={"/admin/users/{userId}/projects"}, method = RequestMethod.GET)
    public String allUserProjects(Model model, @PathVariable Long userId) {
    	model.addAttribute("projectsList", this.userService.getUser(userId).getOwnedProjects());
    	model.addAttribute("userId", userId);
    	return "userOwnedProjects";
    }
    
    @RequestMapping(value={"/admin/users/{userId}/projects/{projectId}/remove"}, method = RequestMethod.GET)
    public String deleteUserProject(Model model, @PathVariable Long userId,
    								@PathVariable Long projectId) {
    	model.addAttribute("userId", userId);
    	model.addAttribute("projectId", projectId);
    	this.projectService.deleteProject(this.projectService.getProject(projectId));
    	return "redirect:/admin/users/" + userId + "/projects";
    }
    
}
