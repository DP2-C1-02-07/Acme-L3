
package acme.features.auditor.audit;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.entities.AuditingRecords;
import acme.entities.Course;
import acme.entities.enums.Marks;
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

	@Query("select ar from AuditingRecords ar where ar.audit.id = :auditId")
	Collection<AuditingRecords> findManyAuditingRecordsByAuditId(int auditId);

	@Query("select ar.mark from AuditingRecords ar where ar.audit.id = :auditId")
	List<Marks> findMarksByAuditId(int auditId);
}
