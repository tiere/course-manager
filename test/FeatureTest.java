import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;

import java.util.ArrayList;
import java.util.List;

import models.Course;

import org.junit.Before;
import org.junit.Test;

import play.libs.ws.WS;
import play.test.WithServer;

import com.fasterxml.jackson.databind.JsonNode;

public class FeatureTest extends WithServer {
	int timeout;
	Course testCourse;
	List<Course> testCourses;

	@Before
	public void setUp() throws Exception {
		start(fakeApplication(), 3333);
		timeout = 3000;

		testCourses = new ArrayList<Course>();

		for (int i = 0; i < 20; i++) {
			Course course = new Course(String.format("Testing %s", i), i);
			course.save();
			testCourses.add(course);

		}
	}

	@Test
	public void testListingAllCoursesWorks() {
		JsonNode response = WS.url("http://localhost:3333/courses").get()
				.get(timeout).asJson();

		for (Course course : testCourses) {
			boolean found = false;
			for (JsonNode jsonNode : response) {
				if (jsonNode.findPath("name").textValue().equals(course.name)
						&& jsonNode.findPath("points").asInt() == course.points) {
					found = true;
				}
			}

			assertThat(found);
		}
	}
}
