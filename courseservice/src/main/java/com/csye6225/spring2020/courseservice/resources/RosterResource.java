package com.csye6225.spring2020.courseservice.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.csye6225.spring2020.courseservice.service.RosterService;
import com.csye6225.spring2020.courseservice.datamodel.Course;
import com.csye6225.spring2020.courseservice.datamodel.InMemoryDatabase;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.ProgramName;
import com.csye6225.spring2020.courseservice.datamodel.Roster;
import com.csye6225.spring2020.courseservice.datamodel.Student;

@Path("rosters")

public class RosterResource {
	static HashMap<Long, Course> course_Map = InMemoryDatabase.getCourseDB();

	RosterService rosterService = new RosterService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Roster> getAllRosters(@QueryParam("profId") long profId) {
		if (profId > 0) {
			return rosterService.getRosterDetailsForProfessor(profId);
		}
		return rosterService.getRosterDetails();

	}

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Roster getRosterDetails(@PathParam("courseId") long courseId) {
		return rosterService.getRoster(courseId);
	}

	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Roster deleteRosterDetails(@PathParam("courseId") long courseId) {
		return rosterService.deleteRosterDetails(courseId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Roster addRosterDetails(Roster roster) {
		return rosterService.addRoster(roster.getCourseId(), roster.getProfId(), roster.getDay(), roster.getTime());
	}

	@PUT
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Roster updateRosterDetails(@PathParam("courseId") long courseId, Roster roster) {
		return rosterService.updateRosterDetails(courseId, roster);
	}

}
