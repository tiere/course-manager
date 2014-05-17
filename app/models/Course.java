package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Course extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Constraints.Required
	@Constraints.MinLength(2)
	@Constraints.MaxLength(30)
	public String name;

	@Constraints.Required
	@Constraints.Max(30)
	public int points;

	public static Finder<Long, Course> find = new Finder<Long, Course>(
			Long.class, Course.class);

	public Course(String name, int points) {
		super();
		this.name = name;
		this.points = points;
	}
}
