package com.csye6225.spring2020.courseservice.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.csye6225.spring2020.courseservice.service.CourseService;
import com.csye6225.spring2020.courseservice.service.StudentService;
import com.csye6225.spring2020.courseservice.datamodel.Course;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.Student;

@Path("courses")

public class CourseResource {

	CourseService courseService = new CourseService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCourses(@QueryParam("programId") long programId) {
		if (programId > 0) {
			return courseService.getCoursesByProgram(programId);
		}
		return courseService.getAllCourses();
	}

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@PathParam("courseId") long courseId) {
		return courseService.getCourse(courseId);
	}

	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course deleteCourse(@PathParam("courseId") long courseId) {
		return courseService.deleteCourse(courseId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course addCourse(Course course, @Context UriInfo uriInfo) throws UnsupportedEncodingException {
		course.setCourseMaterial(uriInfo.getAbsolutePathBuilder() + "/materials/" + course.getProfId() + "_"
				+ URLEncoder.encode(course.getCourseName(), "UTF-8") + ".txt");
		return courseService.addCourse(course.getCourseName(), course.getProfId(), course.getProgramId(),
				course.getStartDate(), course.getEndDate(), course.getCourseMaterial(), course.getStudentTA());
	}

	@PUT
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course updateCourse(@PathParam("courseId") long courseId, Course course) {
		return courseService.updateCourseInformation(courseId, course);
	}

}
