package controllers;

import java.util.List;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CourseController extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static Result courses() {
		List<models.Course> courses = models.Course.find.all();
		JsonNode result = Json.toJson(courses);
		return ok(result);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result addCourse() {
		JsonNode json = request().body().asJson();
		ObjectNode response = Json.newObject();

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
}
