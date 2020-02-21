package com.csye6225.spring2020.courseservice.service;

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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseservice.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseservice.datamodel.Course;
//import com.csye6225.spring2020.courseservice.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseservice.datamodel.InMemoryDatabase;
import com.csye6225.spring2020.courseservice.datamodel.Professor;
import com.csye6225.spring2020.courseservice.datamodel.ProgramName;
import com.csye6225.spring2020.courseservice.datamodel.Student;
import com.csye6225.spring2020.courseservice.service.ProfessorsService;

public class ProgramNameService {

	static HashMap<Long, ProgramName> program_Map = InMemoryDatabase.getProgramNameDB();
	CourseService courseService = new CourseService();
	Professor prof = new Professor();
	Student stud = new Student();
	Course course = new Course();

	public ProgramNameService() {
	}

	//getting list of programs
	public List<ProgramName> getAllProgramNames() {
		ArrayList<ProgramName> list = new ArrayList<>();
		for (ProgramName programName : program_Map.values()) {
			list.add(programName);
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No programs are available");
		}
		return list;
	}
	
	
	//adding a program 
	public ProgramName addProgramName(String programName, List<Long> courses, String duration) {

		long nextAvailableId = program_Map.size() + 1;
		ProgramName pgmName = new ProgramName(nextAvailableId, programName, courses, duration);
		program_Map.put(nextAvailableId, pgmName);
		return pgmName;

	}
	
	//getting single program details
	
	public ProgramName getProgramName(Long programId) {
		if (program_Map.containsKey(programId)) {
			ProgramName pgmName = program_Map.get(programId);

			return pgmName;
		} else {
			throw new DataNotFoundException("program id " + programId + " not avialble");
		}
	}

	//delete program details
	
	public ProgramName deleteProgramName(Long programId) {
		if (program_Map.containsKey(programId)) {
			ProgramName deletedProfDetails = program_Map.get(programId);
			program_Map.remove(programId);
			return deletedProfDetails;
		} else {
			throw new DataNotFoundException("program id " + programId + " not avialble");
		}
	}

	//updating program information
	
	public ProgramName updateProgramInformation(long programId, ProgramName programName) {
		if (programId != programName.getProgramId()) {
			throw new BadRequestException(Response
					.status(Response.Status.BAD_REQUEST).entity("errorMessage : program id in the path " + programId
							+ " " + "does not match with the existing program id " + programName.getProgramId())
					.build());

		}
		ProgramName oldcourseCourseObject = program_Map.get(programId);
		oldcourseCourseObject.setProgramName(programName.getProgramName());
		oldcourseCourseObject.setCourses(programName.getCourses());
		;
		oldcourseCourseObject.setDuration(programName.getDuration());
		return oldcourseCourseObject;
	}
	
	//get list of programs by course id

	public List<ProgramName> getProgramByCourseId(long courseId) {
		ArrayList<ProgramName> list = new ArrayList<>();
		for (ProgramName pgm : program_Map.values()) {
			if (pgm.getCourses().contains(courseId)) {
				list.add(pgm);
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No programs are available for cours id " + courseId);
		}
		return list;
	}

	//get course details for a program
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

	//checking if program exists
	public boolean checkIfProgramExists(long professorId) {
		if (program_Map.containsKey(professorId)) {
			return true;
		} else {
			return false;
		}
	}

}