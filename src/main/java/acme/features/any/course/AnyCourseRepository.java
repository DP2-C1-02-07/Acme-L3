
package acme.features.any.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.Lecture;
import acme.framework.components.datatypes.Money;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyCourseRepository extends AbstractRepository {

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findAllAvailableCourses();

	@Query("select l from Lecture l left join CourseLecture cl on l = cl.lecture left join Course c on cl.course = c where c.id = :courseId")
	Collection<Lecture> findManyLecturesByCourseId(int courseId);

	@Query("select sc.systemCurrency from SystemConfiguration sc")
	String findSystemCurrencyBySystemConfiguration();

	@Query("select c.retailPrice from Course c where c.id = :id")
	Money findRetailPriceByCourseId(int id);
}
