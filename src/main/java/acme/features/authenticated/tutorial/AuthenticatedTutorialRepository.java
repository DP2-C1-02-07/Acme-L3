
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTutorialRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findOneTutorialByTutorialId(int id);

	@Query("select t from Tutorial t where t.draftMode = false")
	Collection<Tutorial> findTutorialNotDraftMode();

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("Select c from Course c")
	Collection<Course> findAllCourses();

}
