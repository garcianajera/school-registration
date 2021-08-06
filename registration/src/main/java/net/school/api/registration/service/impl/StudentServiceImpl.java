package net.school.api.registration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.school.api.registration.entity.Student;
import net.school.api.registration.exception.DuplicateEntityException;
import net.school.api.registration.exception.StudentNotFoundException;
import net.school.api.registration.repository.StudentRepository;
import net.school.api.registration.service.StudentService;

@Service
class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;

	StudentServiceImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	public Student getStudent(Integer id) {
		return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
	}

	@Override
	public Student saveStudent(Student student) {
		if (isDuplicateStudent(student)) {
			throw new DuplicateEntityException(
					"Student " + student.getName() + " (" + student.getEmail() + ") already exists.");
		}
		return studentRepository.save(student);
	}

	@Override
	public Student updateStudent(Integer id, Student student) {

		return studentRepository.findById(id).map(studentFromDb -> {
			if (isDuplicateStudent(student)) {
				throw new DuplicateEntityException(
						"Student " + student.getName() + " (" + student.getEmail() + ") already exists.");
			}
			studentFromDb.setName(student.getName());
			studentFromDb.setEmail(student.getEmail());
			return studentRepository.save(studentFromDb);
		}).orElseThrow(() -> new StudentNotFoundException(id));
	}

	@Override
	public void deleteStudent(Integer id) {
		studentRepository.deleteById(id);
	}

	boolean isDuplicateStudent(Student student) {
		return studentRepository.findByNameAndEmail(student.getName(), student.getEmail()).isPresent();
	}
}
