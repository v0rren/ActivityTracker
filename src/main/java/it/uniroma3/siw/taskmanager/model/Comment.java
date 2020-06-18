package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	
	@Column(nullable = false, length = 100)
	private String name;


	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column
	private LocalDateTime date;

	@ManyToOne(fetch = FetchType.EAGER)
	private User owner;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Task task;

	public Comment() {
	};

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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@PrePersist
	protected void onPersist() {
		this.date = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Comment comment = (Comment) o;
		return Objects.equals(name, comment.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name) + Objects.hash(date);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
