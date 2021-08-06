package net.school.api.registration.entity;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Version;

@Entity
public class CourseRegistration {

	@EmbeddedId
	private CourseRegistrationKey id;

	@ManyToOne
	@MapsId("studentId")
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	private Course course;

	@Version
	private Long version;

	public CourseRegistration() {

	}

	public CourseRegistration(CourseRegistrationKey id) {
		this.id = id;
	}

	public CourseRegistrationKey getId() {
		return id;
	}

	public void setId(CourseRegistrationKey id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CourseRegistration other = (CourseRegistration) o;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


}
