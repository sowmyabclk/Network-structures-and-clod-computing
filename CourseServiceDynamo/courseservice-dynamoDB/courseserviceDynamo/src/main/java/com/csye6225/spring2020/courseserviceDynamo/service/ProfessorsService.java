package com.csye6225.spring2020.courseserviceDynamo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.csye6225.spring2020.courseserviceDynamo.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;

public class ProfessorsService {

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;

	public ProfessorsService() {
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}

	CourseService courseService = new CourseService();
	ProgramService pgmService = new ProgramService();

	// get list of professors
	public List<Professor> getAllProfessors() {
		List<Professor> scanResult = mapper.scan(Professor.class, new DynamoDBScanExpression());
		((PaginatedList<Professor>) scanResult).loadAllResults();
		if (scanResult.size() == 0) {
			throw new DataNotFoundException("No professors are available");
		}
		return scanResult;
	}

	// add a professor
	public Professor addProfessor(long profId, long courseId, String joiningDate, String firstName, String secondName,
			long programId) {
		int count = mapper.count(Professor.class, new DynamoDBScanExpression());

		if (pgmService.checkIfProgramExists(programId) && courseService.validateCourse(courseId)) {

			long nextAvailableId = count + 1;
			if (mapper.load(Professor.class, nextAvailableId) != null) {
				nextAvailableId = nextAvailableId + 1;
			}
			Professor prof = new Professor(nextAvailableId, courseId, joiningDate, firstName, secondName, programId);
			mapper.save(prof);
			return prof;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : either the program id or the courses  does not exist for the program Id "
							+ programId + " and the course  " + courseId)
					.build());

		}
	}

	// getting one professor
	public Professor getProfessor(long profId) {
		Professor prof2 = mapper.load(Professor.class, profId);
		if (prof2 == null) {
			throw new DataNotFoundException("professor id " + profId + " not avialble");

		}
		return prof2;
	}

	// deleting a professor
	public Professor deleteProfessor(Long profId) {
		Professor prof2 = mapper.load(Professor.class, profId);
		if (prof2 == null) {
			throw new DataNotFoundException("professor id " + profId + " not avialble");
		}

		mapper.delete(prof2);
		return prof2;
	}

	// updating a professor
	public Professor updateProfessorInformation(long profId, Professor prof) {
		if (!pgmService.checkIfProgramExists(prof.getProgramId())) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id  " + prof.getProgramId() + " does not exist").build());
		}

		if (profId == prof.getProfId()) {
			Professor oldProf = mapper.load(Professor.class, profId);
			oldProf.setCourseId(prof.getCourseId());
			oldProf.setJoiningDate(prof.getJoiningDate());
			oldProf.setFirstName(prof.getFirstName());
			oldProf.setSecondName(prof.getSecondName());
			oldProf.setProgramId(prof.getProgramId());
			mapper.save(oldProf);
			return oldProf;
		} else {
			throw new BadRequestException(
					Response.status(Response.Status.BAD_REQUEST).entity("errorMessage : prof id in the path " + profId
							+ " " + "does not match with the existing professor id " + prof.getProfId()).build());
		}
	}

	// get professors by department id and size
	public List<Professor> getProfessorsByDepartment(long programId, int size) {
		int noOfvalues = 1;
		List<Professor> list = new ArrayList<>();
		List<Professor> profInfo = getAllProfessors();
		if (pgmService.checkIfProgramExists(programId)) {
			for (int i = 0; i < profInfo.size(); i++) {
				if (size != 0) {
					if (profInfo.get(i).getProgramId() == programId && noOfvalues <= size) {
						list.add(profInfo.get(i));
						noOfvalues++;
					}
				} else {
					if (profInfo.get(i).getProgramId() == programId)
						list.add(profInfo.get(i));

				}
			}

			if (list.isEmpty()) {
				throw new DataNotFoundException("No professors available for program id " + programId);
			}
			return list;
		}

		else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id does not exist " + programId).build());
		}

	}

	// checking if professor exists
	public boolean checkIfProfessorExists(long professorId) {
		Professor prof = getProfessor(professorId);
		if (prof != null) {
			return true;
		} else {
			return false;
		}
	}

	// getting professor list by year and size limit
	public List<Professor> getProfessorsByYear(String year, int size) throws ParseException {
		ArrayList<Professor> list = new ArrayList<>();
		int noOfvalues = 1;
		List<Professor> profInfo = getAllProfessors();
		for (int i = 0; i < profInfo.size(); i++) {
			SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = sd.parse(profInfo.get(i).getJoiningDate());
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (size != 0) {
				if (localDate.getYear() == Integer.parseInt(year) && noOfvalues <= size) {
					list.add(profInfo.get(i));
					noOfvalues++;
				}
			} else {
				if (localDate.getYear() == Integer.parseInt(year)) {
					list.add(profInfo.get(i));
				}
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No professors available for year " + year);
		}
		return list;
	}

}