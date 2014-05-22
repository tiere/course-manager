package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import static helpers.HelperMethodsAndVariables.*;

@Entity
public class Course extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Column(length = 30)
	@MaxLength(value = 30, message = courseNameTooLongMessage)
	@Required(message = "Error, name is required")
	public String name;

	@Max(value = 30, message = "Error, max value for points is 30 characters")
	@Required(message = "Error, points are required")
	@Min(value = 1, message = "Error, min value for points is 1")
	public int points;

	public static Finder<Long, Course> find = new Finder<Long, Course>(
			Long.class, Course.class);

	public Course(String name, int points) {
		super();
		this.name = name;
		this.points = points;
	}
}
