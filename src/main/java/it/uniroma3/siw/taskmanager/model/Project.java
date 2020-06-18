package it.uniroma3.siw.taskmanager.model;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Project is an activity managed by the TaskManager.
 * It is generated and owned by a specific User, that can grant visibility over it to multiple other ones.
 * It can contain one or multiple individual Tasks.
 */
@Entity
public class Project {

    /**
     * Unique identifier for this Project
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Name for this Project
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Description for this Project
     */
    @Column
    private String description;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate date;

    /**
     * Name for this Project
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    /**
     * Name for this Project
     */
    @ManyToMany(fetch = FetchType.LAZY)                                // fetch is LAZY by default
    private List<User> members;

    /**
     * Tasks that this project contains
     */
    @OneToMany(mappedBy="project",fetch = FetchType.EAGER,        // whenever a Project is retrieved, always retrieve its tasks too
            cascade = CascadeType.ALL)   // if a Project is deleted, all its tasks must be deleted too
    private Set<Task> tasks;
    

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name="project_id")
    private List<ProjectTag> tags;		//lista dei tag del progetto
    
    public Project() {
        this.members = new ArrayList<>();
        this.tasks = new HashSet<>();
        this.tags = new ArrayList<>();
    }

    public Project(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public void addMember(User user) {
        if (!this.members.contains(user))
            this.members.add(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task) {
    	this.tasks.add(task);
    }

    
    public List<ProjectTag> getTags() {
		return tags;
	}

	public void setTags(List<ProjectTag> tags) {
		this.tags = tags;
	}
	
	public void addTag(ProjectTag tag) {
		this.tags.add(tag);
	}

	@Override
    public String toString() {

        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    // this is a semplification
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
