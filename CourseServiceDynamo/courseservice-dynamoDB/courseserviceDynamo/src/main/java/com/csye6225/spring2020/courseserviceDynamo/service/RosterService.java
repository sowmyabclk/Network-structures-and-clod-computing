
package com.csye6225.spring2020.courseserviceDynamo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.api.QueryApi;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.csye6225.spring2020.courseserviceDynamo.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Roster;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;
import com.csye6225.spring2020.courseserviceDynamo.service.ProfessorsService;

public class RosterService {

	ProfessorsService profService = new ProfessorsService();
	StudentService studService = new StudentService();
	CourseService courseService = new CourseService();

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;
	DynamoDB dynamoDBTable;

	public RosterService() {
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
		dynamoDBTable = new DynamoDB(dynamoDb.getClient());
	}

	// getting list of rosters
	public List<Roster> getRosterDetails() {
		List<Roster> scanResult = mapper.scan(Roster.class, new DynamoDBScanExpression());
		((PaginatedList<Roster>) scanResult).loadAllResults();

		if (scanResult.size() == 0) {
			throw new DataNotFoundException("No rosters are available");
		}
		return scanResult;
	}

// get list of rosters for prof id 
	public List<Roster> getRosterDetailsForProfessor(long profId) {
		List<Roster> list = new ArrayList<>();
		List<Roster> roster = getRosterDetails();
		for (int i = 0; i < roster.size(); i++) {
			if (roster.get(i).getProfId() == profId) {
				list.add(roster.get(i));
			}
		}
		if (list.isEmpty())
			throw new DataNotFoundException("No professors are available for id  " + profId);
		else
			return list;

	}

	// check if roster exists
	public boolean checkifRosterExists(long courseId) {
		Roster roster = mapper.load(Roster.class, courseId);

		if (roster != null) {
			return true;
		} else {
			return false;
		}
	}

	// adding roster details
	public Roster addRoster(long couserId, long profId, String day, String time) {

		if (mapper.load(Roster.class, couserId) != null && mapper.load(Roster.class, couserId).getProfId() == profId) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("prof id " + profId + " with the  course id " + couserId + " already in the Roster")
					.build());
		}
		Course course = courseService.getCourse(couserId);
		if (course == null) {
			throw new BadRequestException(
					Response.status(Response.Status.BAD_REQUEST).entity(couserId + " does not exist").build());

		} else {
			if (courseService.getCourse(couserId).getCourseId() == couserId
					&& courseService.getCourse(couserId).getProfId() == profId) {
				Roster roster = new Roster();
				roster = new Roster(couserId, profId, day, time);
				mapper.save(roster);
				return roster;
			}

			else {
				throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity("prof id " + profId
						+ " with the  course id " + couserId + " does not exist in the main course id table").build());
			}
		}

	}

	// getting a single roster
	public Roster getRoster(long courseId) {
		Roster roster = mapper.load(Roster.class, courseId);
		if (roster != null)
			return roster;
		else {
			throw new DataNotFoundException("roster for course id " + courseId + " not avialble");
		}
	}

	// deleting a single roster

	public Roster deleteRosterDetails(Long courseId) {
		Roster roster = mapper.load(Roster.class, courseId);
		if (roster != null) {
			mapper.delete(roster);
			return roster;
		} else {
			throw new DataNotFoundException("roster for course id " + courseId + " not avialble");
		}
	}

	// updating a roster details
	public Roster updateRosterDetails(long courseId, Roster roster) {
		Roster oldroster = getRoster(courseId);

		if (courseId != roster.getCourseId() || oldroster == null) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message:course Id " + courseId + " " + "in the syntax does not match with the "
							+ roster.getCourseId() + " or cours id " + courseId + " does not exist in roster")
					.build());

		}

		oldroster.setDay(roster.getDay());
		oldroster.setTime(roster.getTime());
		mapper.save(oldroster);
		return oldroster;
	}

}
