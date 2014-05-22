package controllers;

import static helpers.HelperMethodsAndVariables.*;

import java.util.List;

import models.Course;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

public class CourseController extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static Result courses() {

		List<Course> courses = models.Course.find.all();
		response = Json.newObject();
		JsonNode result = Json.toJson(courses);
		response.put("courses", result);
		return ok(response);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result addCourse() {
		JsonNode json = request().body().asJson();
		response = Json.newObject();

		String name = json.findPath("name").textValue();
		String points = json.findPath("points").textValue();

		if (name == null || points == null) {
			response.put("status", "fail");
			response.put("message", "name and points are required");
			return badRequest(response);
		}

		int pointsInt;
		try {
			pointsInt = Integer.parseInt(points);
		} catch (NumberFormatException e) {
			response.put("status", "fail");
			response.put("message", "points need to be in number format");
			return badRequest(response);
		}

		models.Course course = new models.Course(name, pointsInt);
		Ebean.save(course);
		response.put("message", "Course added successfully");
		return ok(response);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result show(long id) {
		Course course = Course.find.byId(id);
		response = Json.newObject();

		if (course != null) {
			response.put("course", Json.toJson(course));
			return ok(response);
		} else {
			response.put("status", "not found");
			response.put("message", String.format(
					"Course with and id of %s couldn't be found", id));
			return notFound(response);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result delete(long id) {
		Course course = Course.find.byId(id);

		response = Json.newObject();

		if (course == null) {
			response.put(status, fail);
			response.put(message, deleteErrorMessage(id));
			return notFound(response);
		} else {
			course.delete();
			response.put(status, success);
			response.put(message, deleteSuccessMessage(id));
			return ok(response);
		}

	}
}
