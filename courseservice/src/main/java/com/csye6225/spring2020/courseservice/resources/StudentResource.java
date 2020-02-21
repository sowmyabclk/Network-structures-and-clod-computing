package com.csye6225.spring2020.courseservice.resources;

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

import com.csye6225.spring2020.courseservice.service.StudentService;
import com.csye6225.spring2020.courseservice.datamodel.Course;
import com.csye6225.spring2020.courseservice.datamodel.Student;

@Path("students")

public class StudentResource {

	StudentService studService = new StudentService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getStudents(@QueryParam("programId") long programId,
			@QueryParam("professorId") long professorId, @QueryParam("courseId") long courseId) {
		if (programId > 0) {
			return studService.getStudentsByProgram(programId);
		}
		if (professorId > 0) {
			return studService.getStudentsByProfessors(professorId);
		}
		if (courseId > 0) {
			return studService.getStudentsByCoursesEnrolled(courseId);
		}

		return studService.getAllStudents();
	}

	@GET
	@Path("/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent(@PathParam("studentId") long studId) {
		return studService.getStudent(studId);
	}

	@GET
	@Path("/{studentId}/courses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCoursesForStudent(@PathParam("studentId") long studId) {
		return studService.getCoursesForStudent(studService.getStudent(studId).getCourseEnrolled());
	}

	@DELETE
	@Path("/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Student deleteStudent(@PathParam("studentId") long studId) {
		return studService.deleteStudent(studId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Student addStudent(Student stud, @Context UriInfo uriInfo) {
		stud.setImg(uriInfo.getAbsolutePathBuilder() + "/image/" + stud.getFirstName() + ".png");
		return studService.addStudent(stud.getFirstName(), stud.getLastName(), stud.getImg(), stud.getCourseEnrolled(),
				stud.getProgramId());
	}

	@PUT
	@Path("/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Student updateStudentInformation(@PathParam("studentId") long studId, Student stud) {
		return studService.updateStudentInformation(studId, stud);
	}

}
