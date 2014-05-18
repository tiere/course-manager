package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class CourseTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void testCourseCanBeSavedAndFound() {
		Course course = new Course("Learning Java", 4);
		course.save();

		Course foundCourse = Course.find.byId(course.id);

		assertThat(foundCourse).isEqualTo(course);
	}
}
