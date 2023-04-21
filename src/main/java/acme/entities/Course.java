
package acme.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.enums.CourseType;
import acme.entities.enums.Type;
import acme.framework.components.datatypes.Money;
import acme.framework.data.AbstractEntity;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Pattern(regexp = "[A-Z]{1,3}[0-9]{3}")
	@NotBlank
	@Column(unique = true)
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			anAbstract;

	// Custom retailPrice constraint in the validate method of the LecturerCourseCreateService, LecturerCourseUpdateService,
	// LecturerCoursePublishService. Also maximum value included, defined in the discussion board
	@NotNull
	@Valid
	protected Money				retailPrice;

	@URL
	protected String			furtherInformation;

	protected boolean			draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public CourseType courseType(final Collection<Lecture> lectures) {
		CourseType result = CourseType.BALANCED;

		if (lectures != null && !lectures.isEmpty()) {
			final Map<Type, Integer> map = new HashMap<>();

			for (final Lecture l : lectures) {
				final Type type = l.getType();
				if (map.containsKey(type))
					map.put(type, map.get(type) + 1);
				else
					map.put(type, 1);
			}

			if (map.containsKey(Type.HANDS_ON) && map.containsKey(Type.THEORETICAL))
				if (map.get(Type.HANDS_ON) > map.get(Type.THEORETICAL))
					result = CourseType.HANDS_ON;
				else if (map.get(Type.HANDS_ON) < map.get(Type.THEORETICAL))
					result = CourseType.THEORETICAL;
				else
					result = CourseType.BALANCED;

			if (!map.containsKey(Type.HANDS_ON) && map.containsKey(Type.THEORETICAL))
				result = CourseType.THEORETICAL;
			if (map.containsKey(Type.HANDS_ON) && !map.containsKey(Type.THEORETICAL))
				result = CourseType.HANDS_ON;

		}

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Lecturer lecturer;

}
