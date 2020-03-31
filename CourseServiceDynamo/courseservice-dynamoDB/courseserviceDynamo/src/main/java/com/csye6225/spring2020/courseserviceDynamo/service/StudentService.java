
package com.csye6225.spring2020.courseserviceDynamo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.csye6225.spring2020.courseserviceDynamo.Exceptions.DataNotFoundException;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Course;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.DynamoDbConnector;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Professor;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Program;
import com.csye6225.spring2020.courseserviceDynamo.datamodel.Student;

public class StudentService {

	ProfessorsService profService = new ProfessorsService();
	CourseService courseService = new CourseService();
	ProgramService programService = new ProgramService();
	Professor prof = new Professor();

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;

	public StudentService() {
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}

	// get all students
	public List<Student> getAllStudents() {
		List<Student> scanResult = mapper.scan(Student.class, new DynamoDBScanExpression());
		((PaginatedList<Student>) scanResult).loadAllResults();
		if (scanResult.size() == 0) {
			throw new DataNotFoundException("No students are available");
		}
		return scanResult;
	}

	// adding a student
	public Student addStudent(String firstName, String secondName, List<Long> courseEnrolled, long programId,
			String image) {

		int count = mapper.count(Student.class, new DynamoDBScanExpression());
		if (programService.checkIfProgramExists(programId) && courseService.validateCourses(courseEnrolled)) {

			long nextAvailableId = count + 1;
			if (mapper.load(Student.class, nextAvailableId) != null) {
				nextAvailableId = nextAvailableId + 1;
			}
			Student student = new Student(nextAvailableId, firstName, secondName, courseEnrolled, programId, image);
			mapper.save(student);
			return student;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : either the program id or the courses  does not exist program Id "
							+ programId + " and the course list  " + courseEnrolled)
					.build());

		}
	}

	// getting a single student
	public Student getStudent(long studId) {
		Student student = mapper.load(Student.class, studId);
		if (student == null) {
			throw new DataNotFoundException(" student id " + studId + " not avialble");
		}
		return student;
	}

	// deleting a student
	public Student deleteStudent(Long studId) {
		Student student = mapper.load(Student.class, studId);
		if (student == null) {
			throw new DataNotFoundException(" student id " + studId + " not avialble");
		}
		mapper.delete(student);
		return student;
	}

	// updating a student information
	public Student updateStudentInformation(long studId, Student stud) {
		if (studId != stud.getStudentId()) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : student id in the path " + studId + " "
							+ "does not match with the existing student id " + stud.getStudentId())
					.build());

		}
		if (!courseService.validateCourses(stud.getCourseEnrolled())) {
			throw new BadRequestException(
					Response.status(Response.Status.BAD_REQUEST).entity("Error message: validate the cours id s "
							+ stud.getCourseEnrolled() + " for the student " + stud.getStudentId()).build());

		}
		if (!programService.checkIfProgramExists(stud.getProgramId())) {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity("Error message: Program "
					+ stud.getProgramId() + " does not exist for the student " + stud.getStudentId()).build());

		}

		Student oldStudent = mapper.load(Student.class, studId);
		oldStudent.setFirstName(stud.getFirstName());
		oldStudent.setSecondName(stud.getSecondName());
		oldStudent.setCourseEnrolled(stud.getCourseEnrolled());
		oldStudent.setProgramId(stud.getProgramId());
		oldStudent.setImage(stud.getImage());
		;
		mapper.save(oldStudent);
		return oldStudent;
	}

	// filter students by course id
	public List<Student> getStudentsByCoursesEnrolled(long courseEnrolled) {
		ArrayList<Student> list = new ArrayList<>();
		List<Student> studInfo = getAllStudents();
		for (int i = 0; i < studInfo.size(); i++) {
			if (studInfo.get(i).getCourseEnrolled().contains(courseEnrolled)) {
				list.add(studInfo.get(i));
			}
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No students are available");
		}
		return list;
	}

	// filter students by program id
	public List<Student> getStudentsByProgram(long programId) {
		if (programService.checkIfProgramExists(programId)) {
			ArrayList<Student> list = new ArrayList<>();
			List<Student> studInfo = getAllStudents();
			for (int i = 0; i < studInfo.size(); i++) {
				if (studInfo.get(i).getProgramId() == (programId)) {
					list.add(studInfo.get(i));
				}
			}

			if (list.isEmpty()) {
				throw new DataNotFoundException("No students are available");
			}
			return list;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : program id does not exist " + programId).build());
		}
	}

	// check if student exist
	public boolean checkIfStudentExists(long studentId)

	{
		Student student = getStudent(studentId);
		if (student != null) {
			return true;
		} else {
			return false;
		}
	}

	// get courses details for a student
	public List<Course> getCoursesForStudent(List<Long> courses) {
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
}
