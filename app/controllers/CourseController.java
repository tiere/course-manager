package controllers;

import static helpers.HelperMethodsAndVariables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import models.Course;
import play.libs.Json;
import play.data.Form;
import play.data.validation.*;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
		response = Json.newObject();

		Form<Course> courseForm = Form.form(Course.class).bind(
				request().body().asJson().findPath("course"));

		if (courseForm.hasErrors()) {
			response.put("errors", courseForm.errorsAsJson());
			return status(422, response);
		}
		Course course = courseForm.bind(
				request().body().asJson().findPath("course")).get();
		course.save();
		ObjectNode responseCourse = Json.newObject();
		responseCourse.put("name", course.name);
		responseCourse.put("points", course.points);
		response.put("course", responseCourse);
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

	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		response = Json.newObject();
		Course course = Course.find.byId(id);

		if (course == null) {
			response.put(status, fail);
			response.put(message, courseUpdateNotFoundMessage(id));
			return notFound(response);
		}

		Form<Course> courseForm = Form.form(Course.class).bindFromRequest(
				request());

		if (courseForm.hasErrors()) {
			response.put(status, fail);
			response.put(message, courseForm.errorsAsJson());
			return forbidden(response);
		}

		Course newCourse = courseForm.bindFromRequest(request()).get();

		course.name = newCourse.name;
		course.points = newCourse.points;

		course.save();

		response.put(status, success);
		response.put(message, courseUpdatedSuccessMessage);

		return ok(response);
	}
}
