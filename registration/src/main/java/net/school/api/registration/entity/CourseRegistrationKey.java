package net.school.api.registration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CourseRegistrationKey implements Serializable {

	private static final long serialVersionUID = 5377824809060431149L;

	@Column(name = "student_id")
	private Integer studentId;

	@Column(name = "course_id")
	private Integer courseId;

	public CourseRegistrationKey() {

	}

	public CourseRegistrationKey(Integer studentId, Integer courseId) {
		this.studentId = studentId;
		this.courseId = courseId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

}
