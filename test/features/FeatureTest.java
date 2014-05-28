package features;

import static helpers.HelperMethodsAndVariables.*;
import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

import java.util.ArrayList;
import java.util.List;

import models.Course;

import org.junit.Before;
import org.junit.Test;

import play.libs.Json;
import play.libs.ws.WS;
import play.test.WithServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	// GET /courses
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

	// GET /courses/1
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

	// DELETE /courses/1
	@Test
	public void testRemovingCourseWorks() {
		for (Course course : testCourses) {
			JsonNode response = WS
					.url(String.format("%s/courses/%s", host, course.id))
					.delete().get(timeout).asJson();

			assertThat(Course.find.byId(course.id)).isNull();
			assertThat(response.toString()).isEqualToIgnoringCase("{}");
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

	// POST /courses
	@Test
	public void testAddingNewCourseWorks() {
		Course newCourse = new Course("Figuring out Web Servers", 6);

		ObjectNode request = Json.newObject();
		request.put("course", Json.toJson(newCourse));

		JsonNode response = WS.url(String.format("%s/courses", host))
				.post(request).get(timeout).asJson();

		List<Course> courses = Course.find.where().eq("name", newCourse.name)
				.findList();
		assertThat(courses.size()).isGreaterThan(0);
		assertThat(response.findPath("name").textValue())
				.isEqualToIgnoringCase(newCourse.name);
		assertThat(response.findPath("points").asInt()).isEqualTo(
				newCourse.points);
	}

	@Test
	public void testTryingToAddCourseWithTooLongNameIsHandled() {
		Course newCourse = new Course("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 6);

		ObjectNode request = Json.newObject();
		request.put("course", Json.toJson(newCourse));

		JsonNode response = WS.url(String.format("%s/courses", host))
				.post(request).get(timeout).asJson();

		List<Course> courses = Course.find.where().eq("name", newCourse.name)
				.findList();

		assertThat(courses.size()).isEqualTo(0);
		assertThat(
				response.findPath("errors").findPath("name").get(0).textValue())
				.containsIgnoringCase(courseNameTooLongMessage);
	}

	// PUT /courses/1
	@Test
	public void testUpdatingCourseWorks() {
		Course updatedCourse = new Course("Fun with Java", 8);

		ObjectNode request = Json.newObject();
		request.put("course", Json.toJson(updatedCourse));

		JsonNode response = WS
				.url(String.format("%s/courses/%s", host, testCourses.get(0).id))
				.put(request).get(timeout).asJson();

		assertThat(Course.find.byId(testCourses.get(0).id).name)
				.isEqualToIgnoringCase("Fun with Java");
		assertThat(response.findPath("course").findPath("name").textValue())
				.isEqualToIgnoringCase(updatedCourse.name);
		assertThat(response.findPath("course").findPath("points").asInt())
				.isEqualTo(updatedCourse.points);
	}

	@Test
	public void testUpdatingWithTooHighPointsIsHandled() {
		Course newCourse = new Course("Fun with Java", 31);

		ObjectNode request = Json.newObject();
		request.put("course", Json.toJson(newCourse));

		JsonNode response = WS
				.url(String.format("%s/courses/%s", host, testCourses.get(0).id))
				.put(request).get(timeout).asJson();

		assertThat(Course.find.byId(testCourses.get(0).id).name)
				.doesNotContain("Fun with Java");
		assertThat(response.findPath(status).textValue()).containsIgnoringCase(
				fail);
		assertThat(response.findPath(message).toString()).containsIgnoringCase(
				coursePointsTooHighMessage);
	}

	@Test
	public void testTryingToUpdateNonExistingCourseIsHandled() {
		ObjectNode updatedCourse = Json.newObject();
		updatedCourse.put("name", "Fun with Java");
		updatedCourse.put("points", 20);

		JsonNode response = WS.url(String.format("%s/courses/%s", host, 1337))
				.put(updatedCourse).get(timeout).asJson();

		assertThat(Course.find.byId(1337L)).isNull();
		assertThat(response.findPath(status).textValue()).containsIgnoringCase(
				fail);
		assertThat(response.findPath(message).textValue())
				.containsIgnoringCase(courseUpdateNotFoundMessage(1337L));
	}
}
