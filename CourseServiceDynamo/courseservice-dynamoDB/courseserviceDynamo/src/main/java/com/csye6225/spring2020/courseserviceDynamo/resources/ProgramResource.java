package com.csye6225.spring2020.courseserviceDynamo.resources;

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

import com.csye6225.spring2020.courseserviceDynamo.service.CourseService;
import com.csye6225.spring2020.courseserviceDynamo.service.ProgramService;
import com.csye6225.spring2020.courseserviceDynamo.service.StudentService;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;

@Path("programs")

public class ProgramResource {

	ProgramService programService = new ProgramService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Program> getPrograms(@QueryParam("courseId") long courseId) {
		if (courseId > 0) {
			return programService.getProgramByCourseId(courseId);

		}
		return programService.getAllPrograms();
	}

	@GET
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Program getProgram(@PathParam("programId") long programId) {
		return programService.getProgram(programId);
	}

	@GET
	@Path("/{programId}/courses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCoursesForProgram(@PathParam("programId") long programId) {
		return programService.getCoursesForProgram(programService.getProgram(programId).getCourses());
	}

	@DELETE
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Program deleteProgram(@PathParam("programId") long programId) {
		return programService.deleteProgram(programId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Program addProgram(Program program) {
		return programService.addProgram(program.getProgramName(), program.getCourses(), program.getDuration());
	}

	@PUT
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Program updateProgram(@PathParam("programId") long programId, Program program) {
		return programService.updateProgramInformation(programId, program);
	}

}
