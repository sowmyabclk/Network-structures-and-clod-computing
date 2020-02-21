package com.csye6225.spring2020.courseservice.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class InMemoryDatabase {

	private static HashMap<Long, Professor> professorDB = new HashMap<>();
	private static HashMap<Long, Student> studentDB = new HashMap<>();
	private static HashMap<Long, Course> courseDB = new HashMap<>();
	private static HashMap<Long, ProgramName> programNameDB = new HashMap<>();
	private static HashMap<Long, Roster> rosterDB = new HashMap<>();

	public static HashMap<Long, Professor> getProfessorDB() {
		professorDB.put((long) 1000,
				new Professor(1000, "Adrian", "Rodriguez", 1, Arrays.asList(6000L, 6001L), "09-Nov-2019"));
		professorDB.put((long) 1001, new Professor(1001, "James", "scott", 2, Arrays.asList(6002L), "09-Dec-2019"));
		professorDB.put((long) 1002, new Professor(1002, "Amit", "Ganguly", 1, Arrays.asList(6000L), "09-oct-2018"));
		professorDB.put((long) 1003, new Professor(1003, "Mary", "Beth", 1, Arrays.asList(6000L), "09-oct-2019"));
		System.out.println("DB for professor is " + professorDB);
		return professorDB;
	}

	public static HashMap<Long, Student> getStudentDB() {
		studentDB.put((long) 1, new Student(1, "John", "Green",
				"http://localhost:8080/courseservice/webapi/students/image/john.png", Arrays.asList(6000L, 6001L), 1));
		studentDB.put((long) 2,
				new Student(2, "Sham", "Rao", "http://localhost:8080/courseservice/webapi/students/image/sham.png",
						Arrays.asList(6000L, 6001L, 6002L), 2));
		studentDB.put((long) 3,
				new Student(3, "stephany", "cox",
						"http://localhost:8080/courseservice/webapi/students/image/stephany.png",
						Arrays.asList(6000L, 6001L), 1));
		System.out.println("DB for student is " + studentDB);
		return studentDB;
	}

	public static HashMap<Long, Course> getCourseDB() {
		courseDB.put((long) 6000, new Course(6000, "Network Structures", 1000, 1, "08-Sep-2019", "15-Dec-2019",
				"http://localhost:8080/courseservice/webapi/courses/materials/1000_Network+Structures.txt", 1));
		courseDB.put((long) 6001, new Course(6001, "Algorithms", 1000, 2, "08-Sep-2019", "15-Dec-2019",
				"http://localhost:8080/courseservice/webapi/courses/materials/1000_Algorithms.txt", 2));
		courseDB.put((long) 6002, new Course(6002, "OS", 1001, 1, "08-Sep-2019", "15-Dec-2019",
				"http://localhost:8080/courseservice/webapi/courses/materials/1001_OS.txt", 1));
		courseDB.put((long) 6003, new Course(6003, "OS", 1003, 1, "08-Sep-2019", "15-Dec-2019",
				"http://localhost:8080/courseservice/webapi/courses/materials/1003_OS.txt", 1));
		System.out.println("DB for course is " + courseDB);
		return courseDB;
	}

	public static HashMap<Long, ProgramName> getProgramNameDB() {
		programNameDB.put((long) 1, new ProgramName(1, "Information System", Arrays.asList(6000L, 6001L), "2 yrs"));
		programNameDB.put((long) 2, new ProgramName(2, "Computer Systems", Arrays.asList(6000L), "3 yrs"));
		programNameDB.put((long) 3, new ProgramName(3, "Computer Systems", Arrays.asList(6002L), "2 yrs"));
		System.out.println("DB for ProgramName is " + programNameDB);
		return programNameDB;
	}

	public static HashMap<Long, Roster> getRosterDB() {
		rosterDB.put((long) 6000, new Roster((long) 6000, (long) 1000, "Monday", "2:00"));
		rosterDB.put((long) 6001, new Roster((long) 6001, (long) 1001, "Tuesday", "15:30"));
		rosterDB.put((long) 6002, new Roster((long) 6002, (long) 1002, "Wednesday", "12:00"));
		System.out.println("DB for Roster is " + rosterDB);
		return rosterDB;
	}
}