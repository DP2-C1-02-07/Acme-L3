
package acme.features.lecturer.lecturerDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface LecturerDashboardRepository extends AbstractRepository {

	@Query("select count(l) from Lecture l where l.type = acme.entities.enums.Type.THEORETICAL AND l.lecturer.id = :lecturerId")
	Integer totalNumberOfTheoryLectures(int lecturerId);

	@Query("select count(l) from Lecture l where l.type = acme.entities.enums.Type.HANDS_ON AND l.lecturer.id = :lecturerId")
	Integer totalNumberOfHandsOnLectures(int lecturerId);

	@Query("select avg(l.learningTime) from Lecture l where l.lecturer.id = :lecturerId")
	Double averageLearningTimeOfLectures(int lecturerId);

	@Query("select stddev(l.learningTime) from Lecture l where l.lecturer.id = :lecturerId")
	Double deviationLearningTimeOfLectures(int lecturerId);

	@Query("select min(l.learningTime) from Lecture l where l.lecturer.id = :lecturerId")
	Double minimumLearningTimeOfLectures(int lecturerId);

	@Query("select max(l.learningTime) from Lecture l where l.lecturer.id = :lecturerId")
	Double maximumLearningTimeOfLectures(int lecturerId);
}
