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
	public static String invalidCourseInformationMessage = "name and points are required and points must be greater than 0 and in number format";
	public static String courseAddedSuccessMessage = "Course added successfully";
	public static final String courseNameTooLongMessage = "Error, max length for name is 30 characters";
	public static String courseUpdatedSuccessMessage = "Course updated successfully";
	public static String coursePointsTooHighMessage = "Error, max value for points is 30";

	public static String deleteSuccessMessage(Long id) {
		return String
				.format("Record with an id of %s successfully deleted", id);
	}

	public static String deleteErrorMessage(Long id) {
		return String
				.format("Record with an id of %s couldn't be found and thus was not deleted",
						id);
	}

	public static String courseUpdateNotFoundMessage(Long id) {
		return String
				.format("Record with and id of %s couldn't be found and thus was not updated",
						id);
	}
}
