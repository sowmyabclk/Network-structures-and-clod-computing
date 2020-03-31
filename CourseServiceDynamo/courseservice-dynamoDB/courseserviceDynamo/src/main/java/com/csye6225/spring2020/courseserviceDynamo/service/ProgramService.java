package com.csye6225.spring2020.courseserviceDynamo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseserviceDynamo.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;
import com.csye6225.spring2020.courseserviceDynamo.service.ProfessorsService;

public class ProgramService {

	CourseService courseService = new CourseService();
	Professor prof = new Professor();
	Student stud = new Student();
	Course course = new Course();

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;

	public ProgramService() {
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}

	// getting list of programs
	public List<Program> getAllPrograms() {
		List<Program> scanResult = mapper.scan(Program.class, new DynamoDBScanExpression());
		((PaginatedList<Program>) scanResult).loadAllResults();
		if (scanResult.size() == 0) {
			throw new DataNotFoundException("No programs are available");
		}
		return scanResult;
	}

	// adding a program
	public Program addProgram(String programName, List<Long> course, String duration) {
		int count = mapper.count(Program.class, new DynamoDBScanExpression());
		long nextAvailableId = count + 1;
		if (mapper.load(Program.class, nextAvailableId) != null) {
			nextAvailableId = nextAvailableId + 1;
		}
		Program newProgram = new Program(nextAvailableId, programName, course, duration);
		mapper.save(newProgram);
		return newProgram;

	}

	// getting single program details

	public Program getProgram(Long programId) {
		Program program = mapper.load(Program.class, programId);
		if (program == null) {
			throw new DataNotFoundException("program id " + programId + " not avialble");
		}
		return program;
	}

	// delete program details

	public Program deleteProgram(long programId) {
		Program program = mapper.load(Program.class, programId);
		if (program == null) {
			throw new DataNotFoundException("program id " + programId + " not avialble");
		}
		mapper.delete(program);
		return program;

	}

	// updating program information

	public Program updateProgramInformation(long programId, Program program) {
		if (programId == program.getProgramId()) {
			Program oldProgram = mapper.load(Program.class, programId);
			oldProgram.setCourses(program.getCourses());
			oldProgram.setProgramName(program.getProgramName());
			oldProgram.setDuration(program.getDuration());
			mapper.save(oldProgram);
			return oldProgram;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id in the path " + programId + " "
							+ "does not match with the existing program id " + program.getProgramId())
					.build());
		}
	}

	// get list of programs by course id

	public List<Program> getProgramByCourseId(long courseId) {
		ArrayList<Program> list = new ArrayList<>();
		List<Program> programInfo = getAllPrograms();
		for (int i = 0; i < programInfo.size(); i++) {
			if (programInfo.get(i).getCourses().contains(courseId)) {
				list.add(programInfo.get(i));
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No programs are available for cours id " + courseId);
		}
		return list;
	}

	// get course details for a program
	public List<Course> getCoursesForProgram(List<Long> courses) {
		List<Course> courseList = new ArrayList<>();
		for (int i = 0; i < courses.size(); i++) {
			Course course = courseService.getCourse(courses.get(i));
			courseList.add(course);
		}
		if (courseList.isEmpty()) {
			throw new DataNotFoundException("No Courses are available");
		}
		return courseList;

	}

	// checking if program exists
	public boolean checkIfProgramExists(long programId) {
		Program pgm = getProgram(programId);
		if (pgm != null) {
			return true;
		} else {
			return false;
		}
	}

}