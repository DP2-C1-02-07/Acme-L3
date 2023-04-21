
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Audit;
import acme.entities.AuditingRecords;
import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.entities.Enrolment;
import acme.entities.Lecture;
import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.entities.Workbook;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerCourseRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.id = :lecturerId")
	Collection<Course> findManyCoursesByLecturerId(int lecturerId);

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select c from Course c where c.code = :code")
	Course findOneCourseByCode(String code);

	@Query("select l from Lecture l left join CourseLecture cl on l = cl.lecture left join Course c on cl.course = c where c.id = :courseId")
	Collection<Lecture> findManyLecturesByCourseId(int courseId);

	@Query("select count(l) from Lecture l left join CourseLecture cl on l = cl.lecture left join Course c on cl.course = c where c.id = :courseId and l.draftMode = false")
	Integer findManyLecturesByDraftModeAndCourseId(int courseId);

	@Query("select count(l) from Lecture l left join CourseLecture cl on l = cl.lecture left join Course c on cl.course = c where c.id = :courseId and l.type = acme.entities.enums.Type.THEORETICAL")
	Integer findManyLecturesByTheoreticalAndCourseId(int courseId);

	@Query("select cl from CourseLecture cl where cl.course.id = :courseId")
	Collection<CourseLecture> findManyCourseLectureByCourseId(int courseId);

	@Query("select a from Audit a where a.course.id = :courseId")
	Collection<Audit> findManyAuditsByCourseId(int courseId);

	@Query("select ar from AuditingRecords ar where ar.audit.id = :auditId")
	Collection<AuditingRecords> findManyAuditingRecordsByAuditId(int auditId);

	@Query("select t from Tutorial t where t.course.id = :courseId")
	Collection<Tutorial> findManyTutorialsByCourseId(int courseId);

	@Query("select tr from TutorialSession tr where tr.tutorial.id = :tutorialId")
	Collection<TutorialSession> findManyTutorialSessionsByTutorialId(int tutorialId);

	@Query("select e from Enrolment e where e.course.id = :courseId")
	Collection<Enrolment> findManyEnrolmentsByCourseId(int courseId);

	@Query("select wb from Workbook wb where wb.enrolment.id = :enrolmentId")
	Collection<Workbook> findManyWorkbooksByEnrolmentId(int enrolmentId);

}
