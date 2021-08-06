package net.school.api.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.school.api.registration.entity.Course;
import net.school.api.registration.entity.Student;
import net.school.api.registration.repository.CourseRepository;
import net.school.api.registration.repository.StudentRepository;

@Configuration
public class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(StudentRepository studentRepository, CourseRepository courseRepository) {

		return args -> {
			log.info("Preloading Student 1" + studentRepository.save(new Student("Juan Garcia", "garcia@myemail.com")));
			log.info("Preloading Student 2" + studentRepository.save(new Student("John Wick", "wick@thismail.com")));

			log.info("Preloading Course 1" + courseRepository.save(new Course("Math")));
			log.info("Preloading Course 2" + courseRepository.save(new Course("Spanish")));

		};
	}

}
