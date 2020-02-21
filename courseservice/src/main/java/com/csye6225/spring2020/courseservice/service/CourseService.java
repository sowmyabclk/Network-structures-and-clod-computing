package com.csye6225.spring2020.courseservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseservice.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseservice.datamodel.Course;
//import com.csye6225.spring2020.courseservice.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseservice.datamodel.InMemoryDatabase;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.ProgramName;
import com.csye6225.spring2020.courseservice.datamodel.Student;

public class CourseService {

	public CourseService() {
	}

	static HashMap<Long, Course> course_Map = InMemoryDatabase.getCourseDB();

	Professor prof = new Professor();
	Student stud = new Student();

	public List<Course> getAllCourses() {
		ArrayList<Course> list = new ArrayList<>();
		for (Course course : course_Map.values()) {
			System.out.println("in for " + stud);
			list.add(course);
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No Courses are available");
		}
		return list;
	}

	public Course addCourse(String courseName, long profId, long programId, String startDate, String endDate,
			String courseMaterial, long studentTA) {
		System.out.println(profId + " " + programId + " " + studentTA);

		long nextAvailableId = course_Map.size() + 1;
		Course course = new Course(nextAvailableId, courseName, profId, programId, startDate, endDate, courseMaterial,
				studentTA);
		course_Map.put(nextAvailableId, course);
		return course;

	}

	public Course getCourse(Long courseId) {
		if (course_Map.containsKey(courseId)) {
			Course course = course_Map.get(courseId);
			return course;
		} else {
			throw new DataNotFoundException("Course id " + courseId + " not avialble");
		}

	}

	public Course deleteCourse(Long courseId) {
		if (course_Map.containsKey(courseId)) {

			Course deletedProfDetails = course_Map.get(courseId);
			course_Map.remove(courseId);
			return deletedProfDetails;
		} else {
			throw new DataNotFoundException("Course id " + courseId + " not avialble");
		}
	}

	public Course updateCourseInformation(long courseId, Course course) {
		if (courseId != course.getCourseId()) {
			throw new BadRequestException(
					Response.status(Response.Status.BAD_REQUEST).entity("errorMessage : prof id in the path " + courseId
							+ " " + "does not match with the existing professor id " + course.getCourseId()).build());

		}
		Course oldcourseCourseObject = course_Map.get(courseId);
		oldcourseCourseObject.setCourseName(course.getCourseName());
		oldcourseCourseObject.setProgramId(course.getProgramId());
		oldcourseCourseObject.setProfId(course.getProfId());
		oldcourseCourseObject.setStartDate(course.getStartDate());
		oldcourseCourseObject.setEndDate(course.getEndDate());
		oldcourseCourseObject.setCourseMaterial(course.getCourseMaterial());
		return oldcourseCourseObject;
	}

	public List<Course> getCoursesByProgram(long programId) {
		ArrayList<Course> courseList = new ArrayList<>();
		for (Course course : course_Map.values()) {
			if (course.getProgramId() == programId) {
				courseList.add(course);
			}
		}
		if (courseList.isEmpty()) {
			throw new DataNotFoundException("No Courses are available for program id " + programId);
		}
		return courseList;

	}

	public boolean validateCourses(List<Long> courses) {
		for (int i = 0; i < courses.size(); i++) {
			if (!course_Map.containsKey(courses.get(i))) {
				return false;
			}

		}
		return true;
	}

}