package com.csye6225.spring2020.courseserviceDynamo.resources;

import java.text.ParseException;
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

import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;
import com.csye6225.spring2020.courseserviceDynamo.service.ProfessorsService;

@Path("professors")

public class ProfessorsResource {

	ProfessorsService profService = new ProfessorsService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Professor> getProfessors(@QueryParam("programId") long programId, @QueryParam("year") String year,
			@QueryParam("size") int size) throws ParseException {

		if (programId > 0) {
			return profService.getProfessorsByDepartment(programId, size);
		}
		if (year != null) {
			return profService.getProfessorsByYear(year, size);
		} else
			return profService.getAllProfessors();
	}

	@GET
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Professor getProfessor(@PathParam("professorId") long profId) {
		return profService.getProfessor(profId);
	}

	@DELETE
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Professor deleteProfessor(@PathParam("professorId") long profId) {
		return profService.deleteProfessor(profId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Professor addProfessor(Professor prof) {
		return profService.addProfessor(prof.getProfId(), prof.getCourseId(), prof.getJoiningDate(),
				prof.getFirstName(), prof.getSecondName(), prof.getProgramId());

	}

	@PUT
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Professor updateProfessor(@PathParam("professorId") long profId, Professor prof) {
		return profService.updateProfessorInformation(profId, prof);
	}

}
