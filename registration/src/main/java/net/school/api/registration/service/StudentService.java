package net.school.api.registration.service;

import java.util.List;

import net.school.api.registration.entity.Student;

public interface StudentService {
		
	List<Student> getAllStudents();

	Student getStudent(Integer id);

	Student saveStudent(Student student);

	Student updateStudent(Integer id, Student student);

	void deleteStudent(Integer id);

}
