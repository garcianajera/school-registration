package net.school.api.registration.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course implements Serializable {

	private static final long serialVersionUID = 9103205185356306063L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 3, max = 50, message = "Name length should be min 3 and max 50")
	private String name;

	@OneToMany(mappedBy = "course")
	@JsonIgnore
	private Set<CourseRegistration> students;

	@Version
	@JsonIgnore
	private Long version;

	public Course() {
	}

	public Course(String name) {
		this.name = name;
	}

	public Course(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<CourseRegistration> getStudents() {
		return students;
	}

	public void setStudents(Set<CourseRegistration> students) {
		this.students = students;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
