package controllers;

import static helpers.HelperMethodsAndVariables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import models.Course;
import play.libs.Json;
import play.data.validation.*;
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
		int points = json.findPath("points").asInt();

		models.Course course = new models.Course(name, points);

		Set<ConstraintViolation<Course>> errors = Validation.getValidator()
				.validate(course);

		if (errors.size() > 0) {
			response.put(status, fail);
			List<String> errorMessages = new ArrayList<String>();
			for (ConstraintViolation<Course> constraintViolation : errors) {
				errorMessages.add(constraintViolation.getMessage());
			}
			response.put(message, Json.toJson(errorMessages));
			return badRequest(response);
		}

		Ebean.save(course);
		response.put(status, success);
		response.put("message", courseAddedSuccessMessage);
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
