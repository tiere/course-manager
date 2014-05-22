package helpers;

import java.util.List;

import models.Course;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class HelperMethodsAndVariables {

	public static ObjectNode response;
	public static int timeout;
	public static List<Course> testCourses;
	public static String host = "http://localhost:3333";
	public static String success = "success";
	public static String fail = "fail";
	public static String status = "status";
	public static String message = "message";

	public static String deleteSuccessMessage(Long id) {
		return String
				.format("Record with an id of %s successfully deleted", id);
	}

	public static String deleteErrorMessage(Long id) {
		return String
				.format("Record with an id of %s couldn't be found and thus was not deleted",
						id);
	}
}
