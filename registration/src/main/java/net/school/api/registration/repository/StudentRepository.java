package net.school.api.registration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.school.api.registration.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	Optional<Student> findByNameAndEmail(String name, String email);

	List<Student> findByCoursesIsEmpty();

}
