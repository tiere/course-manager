package features;

import static helpers.HelperMethodsAndVariables.*;
import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

import java.util.ArrayList;

import models.Course;

import org.junit.Before;
import org.junit.Test;

import play.libs.ws.WS;
import play.test.WithServer;

import com.fasterxml.jackson.databind.JsonNode;

public class FeatureTest extends WithServer {

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

	@Test
	public void testShowingOneCourseWorks() {
		for (Course course : testCourses) {
			JsonNode response = WS
					.url(String.format("%s/courses/%s", host, course.id)).get()
					.get(timeout).asJson();

			assertThat(course.name).isEqualTo(
					response.findPath("name").textValue());
		}
	}

	@Test
	public void testCourseNotFoundIsHandledProperly() {
		JsonNode response = WS.url("http://localhost:3333/courses/1234").get()
				.get(timeout).asJson();

		assertThat("not found").isEqualTo(
				response.findPath("status").textValue());
		assertThat(response.findPath("message").textValue()).contains("1234");
	}

	@Test
	public void testRemovingCourseWorks() {
		for (Course course : testCourses) {
			JsonNode response = WS
					.url(String.format("%s/courses/%s", host, course.id))
					.delete().get(timeout).asJson();

			assertThat(Course.find.byId(course.id)).isNull();
			assertThat(response.findPath(status).textValue())
					.containsIgnoringCase(success);
			assertThat(response.findPath(message).textValue())
					.containsIgnoringCase(deleteSuccessMessage(course.id));
		}
	}

	@Test
	public void testTryingToDeleteNonExistingRecordIsHandled() {
		long id = 1337;
		JsonNode response = WS.url(String.format("%s/courses/%s", host, id))
				.delete().get(timeout).asJson();

		assertThat(response.findPath(status).textValue()).containsIgnoringCase(
				fail);
		assertThat(response.findPath(message).textValue())
				.containsIgnoringCase(deleteErrorMessage(id));
	}

}