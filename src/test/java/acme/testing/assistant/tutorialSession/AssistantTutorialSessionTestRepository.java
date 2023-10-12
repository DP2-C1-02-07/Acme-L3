
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.framework.repositories.AbstractRepository;

public interface AssistantTutorialSessionTestRepository extends AbstractRepository {

	@Query("Select ts from TutorialSession ts where ts.tutorial.assistant.userAccount.username = :username")
	Collection<TutorialSession> findManyTutorialSessionsByAssistantUsername(String username);

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :username")
	Collection<Tutorial> findManyTutorialsByAssistantUsername(String username);
}
