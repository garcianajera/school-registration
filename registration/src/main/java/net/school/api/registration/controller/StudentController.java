package net.school.api.registration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.school.api.registration.entity.Student;
import net.school.api.registration.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	private final StudentService studentService;

	StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping()
	List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

	@GetMapping("/{id}")
	Student getStudent(@PathVariable Integer id) {
		return studentService.getStudent(id);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	Student addStudent(@RequestBody Student student) {
		return studentService.saveStudent(student);
	}

	@PutMapping("/{id}")
	Student updateStudent(@PathVariable Integer id, @RequestBody Student student) {
		return studentService.updateStudent(id, student);
	}

	@DeleteMapping("/{id}")
	void deleteStudent(@PathVariable Integer id) {
		studentService.deleteStudent(id);
	}

}
