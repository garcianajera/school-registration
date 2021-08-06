package net.school.api.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.school.api.registration.entity.CourseRegistration;
import net.school.api.registration.entity.CourseRegistrationKey;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, CourseRegistrationKey>{

}
