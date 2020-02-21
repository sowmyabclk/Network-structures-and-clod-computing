package com.csye6225.spring2020.courseservice.service;

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
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseservice.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseservice.datamodel.Course;
//import com.csye6225.spring2020.courseservice.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseservice.datamodel.InMemoryDatabase;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.ProgramName;
import com.csye6225.spring2020.courseservice.datamodel.Student;

public class ProfessorsService {

	public ProfessorsService() {
	}

	static HashMap<Long, Professor> prof_Map = InMemoryDatabase.getProfessorDB();

	CourseService courseService = new CourseService();
	ProgramNameService pgmService = new ProgramNameService();

	public List<Professor> getAllProfessors() {
		ArrayList<Professor> list = new ArrayList<>();
		for (Professor prof : prof_Map.values()) {
			list.add(prof);
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No professors are available");
		}
		return list;
	}

	public Professor addProfessor(String firstName, String lastName, long programId, List<Long> courses,
			String joiningDate) {
		if (pgmService.checkIfProgramExists(programId) && courseService.validateCourses(courses)) {
			long nextAvailableId = prof_Map.size() + 1;
			Professor prof = new Professor(nextAvailableId, firstName, lastName, programId, courses, joiningDate);
			prof_Map.put(nextAvailableId, prof);
			return prof;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : either the program id or the courses  does not exist program Id "
							+ programId + " and the course list  " + courses)
					.build());

		}
	}

	public Professor getProfessor(Long profId) {
		if (prof_Map.containsKey(profId)) {
			Professor prof2 = prof_Map.get(profId);

			return prof2;
		} else {
			throw new DataNotFoundException("professor id " + profId + " not avialble");
		}
	}

	public Professor deleteProfessor(Long profId) {
		if (prof_Map.containsKey(profId)) {
			Professor deletedProfDetails = prof_Map.get(profId);
			prof_Map.remove(profId);
			return deletedProfDetails;
		} else {
			throw new DataNotFoundException("professor id " + profId + " not avialble");
		}
	}

	public Professor updateProfessorInformation(long profId, Professor prof) {
		if (profId != prof.getProfessorId()) {
			throw new BadRequestException(
					Response.status(Response.Status.BAD_REQUEST)
							.entity("errorMessage : prof id in the path " + profId + " "
									+ "does not match with the existing professor id " + prof.getProfessorId())
							.build());
		}
		if (!pgmService.checkIfProgramExists(prof.getProgramId())) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id  " + prof.getProgramId() + " does not exist").build());
		}

		Professor oldProfObject = prof_Map.get(profId);
		profId = oldProfObject.getProfessorId();
		oldProfObject.setProgramId(prof.getProgramId());
		oldProfObject.setFirstName(prof.getFirstName());
		oldProfObject.setLastName(prof.getLastName());
		oldProfObject.setCourses(prof.getCourses());
		oldProfObject.setJoiningDate(prof.getJoiningDate());
		return oldProfObject;
	}

	public List<Professor> getProfessorsByDepartment(long programId, int size) {
		int noOfvalues = 1;
		ArrayList<Professor> list = new ArrayList<>();
		if (pgmService.checkIfProgramExists(programId)) {
			for (Professor prof : prof_Map.values()) {
				if (size != 0) {
					if (prof.getProgramId() == programId && noOfvalues <= size) {
						list.add(prof);
						noOfvalues++;
					}
				} else {
					if (prof.getProgramId() == programId) {
						System.out.println(1);
						list.add(prof);

					}
				}
			}
			if (list.isEmpty()) {
				throw new DataNotFoundException("No professors available for program id " + programId);
			}
			return list;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id does not exist " + programId).build());
		}
	}

	public boolean checkIfProfessorExists(long professorId) {
		if (prof_Map.containsKey(professorId)) {
			return true;
		} else {
			return false;
		}
	}

	public List<Professor> getProfessorsByYear(String year, int size) throws ParseException {
		ArrayList<Professor> list = new ArrayList<>();
		int noOfvalues = 1;
		for (Professor prof : prof_Map.values()) {
			SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = sd.parse(prof.getJoiningDate());
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (size != 0) {
				if (localDate.getYear() == Integer.parseInt(year) && noOfvalues <= size) {
					list.add(prof);
					noOfvalues++;
				}
			} else {
				if (localDate.getYear() == Integer.parseInt(year)) {
					list.add(prof);
				}
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No professors available for year " + year);
		}
		return list;
	}

}