
package com.csye6225.spring2020.courseserviceDynamo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseserviceDynamo.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Roster;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;

public class CourseService {

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;

	public CourseService() {
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}

	Professor prof = new Professor();
	Student stud = new Student();

	public List<Course> getAllCourses() {
		List<Course> scanResult = mapper.scan(Course.class, new DynamoDBScanExpression());
		((PaginatedList<Course>) scanResult).loadAllResults();
		if (scanResult.size() == 0) {
			throw new DataNotFoundException("No Courses are available");
		}
		return scanResult;
	}

	public Course addCourse(String courseName, long profId, long programId, String startDate, String endDate, long TAId,
			String courseMaterial, @Context UriInfo uriInfo) throws UnsupportedEncodingException {
		int count = mapper.count(Course.class, new DynamoDBScanExpression());
		long nextAvailableId = count + 1;
		if (mapper.load(Course.class, nextAvailableId) != null) {
			nextAvailableId = nextAvailableId + 1;
		}
		Course course = new Course(nextAvailableId, courseName, profId, programId, startDate, endDate, TAId,
				courseMaterial);
		mapper.save(course);
		return course;

	}

	// getting a single course

	public Course getCourse(long courseId) {
		Course course = mapper.load(Course.class, courseId);
		if (course == null) {
			throw new DataNotFoundException("Course id " + courseId + " not avialble");
		}

		return course;
	}

	// deleting a course
	public Course deleteCourse(long courseId) {
		Course course = mapper.load(Course.class, courseId);
		if (course == null) {
			throw new DataNotFoundException("Course id " + courseId + " not avialble");
		}
		mapper.delete(course);
		return course;
	}

	// updating a course

	public Course updateCourseInformation(long courseId, Course course) {

		if (courseId == course.getCourseId()) {
			Course oldCourse = mapper.load(Course.class, courseId);
			oldCourse.setCourseId(course.getCourseId());
			oldCourse.setCourseName(course.getCourseName());
			oldCourse.setEndDate(course.getEndDate());
			oldCourse.setProfId(course.getProfId());
			oldCourse.setProgramId(course.getProgramId());
			oldCourse.setStartDate(course.getStartDate());
			oldCourse.setTAId(course.getTAId());
			oldCourse.setCourseMaterial(course.getCourseMaterial());

			mapper.save(oldCourse);
			return oldCourse;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : course id in the path " + courseId + " "
							+ "does not match with the existing course id " + course.getCourseId())
					.build());
		}
	}

	// getting course list for a program id
	public List<Course> getCoursesByProgram(long programId) {
		List<Course> list = new ArrayList<>();
		List<Course> course = getAllCourses();
		for (int i = 0; i < course.size(); i++) {
			if (course.get(i).getProgramId() == programId) {
				list.add(course.get(i));
			}

		}
		if (list.isEmpty())
			throw new DataNotFoundException("No courses are available for program  id  " + programId);
		else
			return list;
	}

	// function to validate courses
	public boolean validateCourse(long courseId) {
		Course course = mapper.load(Course.class, courseId);
		if (course == null) {
			return false;
		}
		return true;
	}

	public boolean validateCourses(List<Long> courses) {
		for (int i = 0; i < courses.size(); i++) {
			if (mapper.load(Course.class, courses.get(i)) == null) {
				return false;
			}

		}
		return true;
	}
}
