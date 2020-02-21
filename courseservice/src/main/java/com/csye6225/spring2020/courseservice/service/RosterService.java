package com.csye6225.spring2020.courseservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.csye6225.spring2020.courseservice.datamodel.Roster;
import com.csye6225.spring2020.courseservice.datamodel.Student;
import com.csye6225.spring2020.courseservice.service.ProfessorsService;

public class RosterService {

	static HashMap<Long, Roster> Roster_Map = InMemoryDatabase.getRosterDB();
	ProfessorsService profService = new ProfessorsService();
	StudentService studService = new StudentService();
	CourseService courseService = new CourseService();

	public RosterService() {
	}

	//getting list of rosters
	public List<Roster> getRosterDetails() {
		ArrayList<Roster> list = new ArrayList<>();
		for (Roster roster : Roster_Map.values()) {
			list.add(roster);
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No professors are available");
		}
		return list;
	}

	// get list of rosters for prof id
	public List<Roster> getRosterDetailsForProfessor(long profId) {
		ArrayList<Roster> list = new ArrayList<>();
		for (Roster roster : Roster_Map.values()) {
			if (roster.getProfId() == profId) {
				list.add(roster);
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No professors are available");
		}
		return list;
	}

	//check if roster exists
	public boolean checkifRosterExists(long courseId) {
		if (Roster_Map.containsKey(courseId)) {
			return true;
		} else {
			return false;
		}
	}

	//adding roster details
	public Roster addRoster(long couserId, long profId, String day, String time) {
		Roster roster = null;
		if (courseService.getCourse(couserId).getCourseId() == couserId
				&& courseService.getCourse(couserId).getProfId() == profId) {
			if (checkifRosterExists(couserId)) {
				throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
						.entity("prof id " + profId + " with the  course id " + couserId + " already in the Roster")
						.build());
			} else {
				roster = Roster_Map.get(couserId);
				roster = new Roster(couserId, profId, day, time);
				Roster_Map.put(couserId, roster);

			}

		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity("prof id " + profId
					+ " with the  course id " + couserId + " does not exist in the main course id table").build());
		}

		return roster;

	}

	//getting a single roster
	public Roster getRoster(long courseId) {
		if (Roster_Map.containsKey(courseId)) {

			Roster roster = Roster_Map.get(courseId);

			return roster;
		} else {
			throw new DataNotFoundException("roster for course id " + courseId + " not avialble");
		}
	}
	
	//deleting a single roster

	public Roster deleteRosterDetails(Long courseId) {
		if (Roster_Map.containsKey(courseId)) {
			Roster deletedRosterDetails = Roster_Map.get(courseId);
			Roster_Map.remove(courseId);
			return deletedRosterDetails;
		} else {
			throw new DataNotFoundException("roster for course id " + courseId + " not avialble");
		}
	}

	//updating a roster details
	public Roster updateRosterDetails(long courseId, Roster roster) {
		if (courseId != roster.getCourseId() || !Roster_Map.containsKey(courseId)) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(
					"Error Message:course Id " + courseId + " " + "in the syntax does not match with the " + roster.getCourseId()+" or cours id "+courseId+" does not exist in roster")
					.build());

		}
		
		Roster oldRosterObject = Roster_Map.get(courseId);
		oldRosterObject.setDay(roster.getDay());
		oldRosterObject.setTime(roster.getTime());
		return oldRosterObject;
	}

}