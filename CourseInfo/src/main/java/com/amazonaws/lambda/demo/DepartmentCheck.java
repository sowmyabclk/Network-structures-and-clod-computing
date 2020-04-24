package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.lambda.datamodel.Board;
import com.amazonaws.lambda.datamodel.Course;
import com.amazonaws.lambda.datamodel.DynamoDbConnector;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DepartmentCheck implements RequestHandler<Course, Boolean> {

	@Override
	public Boolean handleRequest(Course course, Context context) {
		boolean value = getCourseNameList(course.getcourseName().toLowerCase());
		return value;
	}

	public boolean getCourseNameList(String courseName1) {
		List<String> courseNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		boolean flag = true;
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		ScanRequest scanRequest = new ScanRequest().withTableName("Course");
		ScanResult scanResult = client.scan(scanRequest);
		for (Map<String, AttributeValue> course : scanResult.getItems()) {
			try {
				AttributeValue v = course.get("courseName");
				String courseName = v.getS();
				AttributeValue v1 = course.get("department");
				String departmentName = v1.getS();

				if (courseName.equalsIgnoreCase(courseName1)
						&& departmentName.toLowerCase().equalsIgnoreCase("seminar")) {
					flag = false;
					return flag;
				}
			} catch (Exception e) {
			}
		}
		return flag;
	}

}
