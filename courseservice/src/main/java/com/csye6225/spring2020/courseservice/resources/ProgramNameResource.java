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

import com.csye6225.spring2020.courseservice.service.CourseService;
import com.csye6225.spring2020.courseservice.service.ProgramNameService;
import com.csye6225.spring2020.courseservice.service.StudentService;
import com.csye6225.spring2020.courseservice.datamodel.Course;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.ProgramName;
import com.csye6225.spring2020.courseservice.datamodel.Student;

@Path("programs")

public class ProgramNameResource {

	ProgramNameService programNameService = new ProgramNameService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProgramName> getProgramNames(@QueryParam("courseId") long courseId) {
		if (courseId > 0) {
			return programNameService.getProgramByCourseId(courseId);

		}
		return programNameService.getAllProgramNames();
	}

	@GET
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProgramName getProgramName(@PathParam("programId") long programId) {
		System.out.println("Program Resource: Looking for: " + programId);
		return programNameService.getProgramName(programId);
	}

	@GET
	@Path("/{programId}/courses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCoursesForProgram(@PathParam("programId") long programId) {
		return programNameService.getCoursesForProgram(programNameService.getProgramName(programId).getCourses());
	}

	@DELETE
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProgramName deleteProgramName(@PathParam("programId") long programId) {
		return programNameService.deleteProgramName(programId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ProgramName addProgramname(ProgramName programName) {
		return programNameService.addProgramName(programName.getProgramName(), programName.getCourses(),
				programName.getDuration());
	}

	@PUT
	@Path("/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ProgramName updateProgramname(@PathParam("programId") long programId, ProgramName programName) {
		return programNameService.updateProgramInformation(programId, programName);
	}

}
