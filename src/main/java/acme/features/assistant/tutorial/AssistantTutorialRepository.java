
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("select a from Assistant a where a.id = :id")
	Assistant findOneAssistantById(int id);

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findOneTutorialById(int id);

	@Query("select t from Tutorial t where t.assistant.id = :assistantId")
	Collection<Tutorial> findManyTutorialsByAssistantId(int assistantId);

	@Query("Select t from Tutorial t where t.code = :code")
	Tutorial findOneTutorialByCode(String code);

	@Query("Select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("Select ts from TutorialSession ts where ts.tutorial.id = :tutorialId")
	Collection<TutorialSession> findSessionsByTutorialId(int tutorialId);

	@Query("Select c from Course c where c.draftMode = false")
	Collection<Course> findNotDraftModeCourses();

}
