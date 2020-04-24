package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.lambda.datamodel.Board;
import com.amazonaws.lambda.datamodel.Course;
import com.amazonaws.lambda.datamodel.DynamoDbConnector;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class NewCourseFunction implements RequestHandler<Course, String> {

	@Override
	public String handleRequest(Course course, Context context) {
		context.getLogger().log("Input: " + course);
		DynamoDBMapper mapper;
		Table dynamoDBTable;
		Course courseInfo = new Course();
		context.getLogger().log("Input: " + course);
		DynamoDB dynamoDB = new DynamoDB(getClient());
		mapper = new DynamoDBMapper(getClient());
		boolean profExist = doesProfessorExist(course.getProfessorId().toLowerCase());
		if (profExist) {
			courseInfo.setcourseName(course.getcourseName());
			courseInfo.setBoardName(" ");
			courseInfo.setRegisteredStudents(new ArrayList<String>());
			courseInfo.setProfessorId(course.getProfessorId());
			courseInfo.setStudentTA(" ");
			courseInfo.setDepartment(course.getDepartment());
			courseInfo.setNotificationTopic(" ");
			mapper.save(courseInfo);
		} else {
			
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity("professor doesn't exist ").build());
        }
		

		return courseInfo.getcourseName();
	}

	public static boolean doesProfessorExist(String prof) {
		List<String> professors = getProfNameList();
		return professors.contains(prof);
	}

	public static List<String> getProfNameList() {
		List<String> profNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		ScanRequest scanRequest = new ScanRequest().withTableName("Professor");
		ScanResult scanResult = client.scan(scanRequest);
		for (Map<String, AttributeValue> profs : scanResult.getItems()) {
			try {
				AttributeValue v = profs.get("professorId");
				String profName = v.getS();
				profNames.add(profName.toLowerCase());
			} catch (Exception e) {
			}
		}
		return profNames;
	}

	public AmazonDynamoDB getClient() {
		return AmazonDynamoDBClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
				.withRegion("us-west-2").build();
	}

}
