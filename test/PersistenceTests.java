import static org.fest.assertions.Assertions.assertThat;
import models.Course;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class PersistenceTests extends WithApplication {

	Course testCourse;

	@Before
	public void setUp() throws Exception {
		start();
		testCourse = new Course("Basics of Computers", 6);
		testCourse.save();
	}

	@Test
	public void testCoursesCanBeFound() {

		Course course = Course.find.byId(testCourse.id);

		assertThat(course).isEqualTo(testCourse);
	}

	@Test
	public void testCourseCanBeSaved() {
		Course course = new Course("Programming basics", 6);
		course.save();

		Course foundCourse = Course.find.byId(course.id);

		assertThat(foundCourse).isEqualTo(course);
	}
}
