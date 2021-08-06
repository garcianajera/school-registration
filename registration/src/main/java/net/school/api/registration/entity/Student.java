package net.school.api.registration.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Student implements Serializable {

	private static final long serialVersionUID = -1832561186501425333L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 3, max = 80, message = "Name length should be min 3 and max 80")
	private String name;

	@Email
	@NotNull(message = "Email cannot be null")
	private String email;

	@OneToMany(mappedBy = "student")
	@JsonIgnore
	private Set<CourseRegistration> courses;

	@Version
	@JsonIgnore
	private Long version;

	public Student() {
	}

	public Student(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Student(Integer id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<CourseRegistration> getCourses() {
		return courses;
	}

	public void setCourses(Set<CourseRegistration> courses) {
		this.courses = courses;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
