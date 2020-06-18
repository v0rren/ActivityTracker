package it.uniroma3.siw.taskmanager.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class ProjectTag {

	/**
	 * Unique identifier for this ProjectTag
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Name for this ProjectTag
	 */
	@Column(nullable = false, length = 100)
	private String name;

	/**
	 * Description for this ProjectTag
	 */
	@Column
	private String description;

	@Column
	private String color;
	
	
    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    
    @ManyToMany
    private List<Task> tasks;
    

	public ProjectTag() {
	}
	
	 public ProjectTag(String name, String description, String color) {
	        this();
	        this.name = name;
	        this.description = description;
	        this.color = color;

	    }
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
	@Override
	public String toString() {
		return "ProjectTag [name=" + name + ", color=" + color + ", project=" + project + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProjectTag pt = (ProjectTag) o;
		return Objects.equals(name, pt.name) && Objects.equals(description, pt.description)
				&& Objects.equals(color, pt.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, color, id);
	}

}
