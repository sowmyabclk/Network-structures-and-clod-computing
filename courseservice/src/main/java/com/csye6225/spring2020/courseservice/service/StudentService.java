package com.csye6225.spring2020.courseservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

public class StudentService {

	static HashMap<Long, Student> stud_Map = InMemoryDatabase.getStudentDB();

	ProfessorsService profService = new ProfessorsService();
	CourseService courseService = new CourseService();
	ProgramNameService programService = new ProgramNameService();
	Professor prof = new Professor();

	public StudentService() {
	}

	public List<Student> getAllStudents() {
		ArrayList<Student> list = new ArrayList<>();
		for (Student stud : stud_Map.values()) {
			list.add(stud);
		}
		if (list.isEmpty()) {
			throw new DataNotFoundException("No students are available");
		}
		return list;
	}

	public Student addStudent(String firstName, String lastName, String img, List<Long> courseEnrolled,
			long programId) {
		if (programService.checkIfProgramExists(programId) && courseService.validateCourses(courseEnrolled)) {
			long nextAvailableId = stud_Map.size() + 1;

			Student stud = new Student(nextAvailableId, firstName, lastName, img, courseEnrolled, programId);

			stud_Map.put(nextAvailableId, stud);
			return stud;
		} else {
			throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
					.entity("errorMessage : either the program id or the courses  does not exist program Id "
							+ programId + " and the course list  " + courseEnrolled)
					.build());

		}
	}

	public Student getStudent(Long studId) {
		if (stud_Map.containsKey(studId)) {

			Student stud = stud_Map.get(studId);

			return stud;
		} else {
			throw new DataNotFoundException(" student id " + studId + " not avialble");
		}
	}

	public Student deleteStudent(Long studId) {
		if (stud_Map.containsKey(studId)) {
			Student deletedProfDetails = stud_Map.get(studId);
			stud_Map.remove(studId);
			return deletedProfDetails;
		} else {
			throw new DataNotFoundException(" student id " + studId + " not avialble");
		}
	}

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

		Student oldProfObject = stud_Map.get(studId);
		oldProfObject.setFirstName(stud.getFirstName());
		oldProfObject.setLastName(stud.getLastName());
		oldProfObject.setImg(stud.getImg());
		oldProfObject.setCourseEnrolled(stud.getCourseEnrolled());
		oldProfObject.setProgramId(stud.getProgramId());
		return oldProfObject;
	}

	public List<Student> getStudentsByCoursesEnrolled(long courseEnrolled) {
		ArrayList<Student> list = new ArrayList<>();
		for (Student stud : stud_Map.values()) {
			if (stud.getCourseEnrolled().contains(courseEnrolled)) {
				list.add(stud);
			}
		}
		System.out.println(list);
		if (list.isEmpty()) {
			throw new DataNotFoundException("No students are available");
		}
		return list;
	}

	public List<Student> getStudentsByProgram(long programId) {
		if (programService.checkIfProgramExists(programId)) {
			ArrayList<Student> list = new ArrayList<>();
			for (Student stud : stud_Map.values()) {
				if (stud.getProgramId() == (programId)) {
					list.add(stud);
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

	public List<Student> getStudentsByProfessors(long professorId) {
		ArrayList<Student> studs = new ArrayList<>();
		boolean isExists = profService.checkIfProfessorExists(professorId);
		if (isExists) {
			prof = profService.getProfessor(professorId);
			List<Long> profCourses = prof.getCourses();
			for (Student stud : stud_Map.values()) {
				List<Long> studentCourses = stud.getCourseEnrolled();
				for (Long studentCourse : studentCourses) {
					for (Long profCourse : profCourses) {
						if (profCourse.equals(studentCourse)) {
							if (!studs.contains(stud))
								studs.add(stud);
							break;
						}
					}

				}
			}
			if (studs.isEmpty()) {
				throw new DataNotFoundException("student id's are not found for professor id " + professorId);
			} else {
			}
		} else {
			throw new WebApplicationException(
					Response.status(Response.Status.NOT_FOUND).entity("prof id does not exist " + professorId).build());

		}
		return studs;

	}

	public boolean checkIfStudentExists(long professorId) {
		if (stud_Map.containsKey(professorId)) {
			return true;
		} else {
			return false;
		}
	}

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