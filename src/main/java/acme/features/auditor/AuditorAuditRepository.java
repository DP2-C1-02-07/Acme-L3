
package acme.features.auditor;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.entities.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.id = :auditorId")
	Collection<Audit> findManyAuditsByAuditorId(int auditorId);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

	@Query("select a.course from Audit a where a.auditor.id = :auditorId")
	Collection<Course> findManyCoursesByAuditorId(int auditorId);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.id = :courseId")
	Collection<Course> findOneCourseById(int courseId);

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);
}
