
package acme.features.student.workbook;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Enrolment;
import acme.entities.Workbook;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentWorkbookRepository extends AbstractRepository {

	@Query("select a from Workbook a where a.id = :id")
	Workbook findOneWorkbookById(int id);

	@Query("select e from Enrolment e where e.id = :id")
	Enrolment findOneEnrolmentById(int id);

	@Query("select a.enrolment from Workbook a where a.id = :id")
	Enrolment findOneEnrolmentByWorkbookId(int id);

	@Query("select s from Student s where s.id = :id")
	Student findOneStudentById(int id);

	@Query("select a from Workbook a where a.enrolment.student.id = :studentId")
	Collection<Workbook> findWorkbookByStudentId(int studentId);

	@Query("select e from Enrolment e where e.student.id = :studentId and e.cardEnd is not null and e.cardHolder is not null")
	Collection<Enrolment> findFinalisedEnrolmentsByStudentId(int studentId);



}