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
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;

public class BoardFunctionHandler implements RequestHandler<Course, String> {

	@Override
	public String handleRequest(Course course, Context context) {
		context.getLogger().log("Input: " + course);
		DynamoDBMapper mapper;
		Table dynamoDBTable;
		context.getLogger().log("Input: " + course);
		DynamoDB dynamoDB = new DynamoDB(getClient());
		dynamoDBTable = dynamoDB.getTable("Board");
		mapper = new DynamoDBMapper(getClient());
		Board board = new Board();
		board.setCourseName(course.getcourseName());
		board.setBoardName(course.getcourseName() + " board");
		mapper.save(board);
		getCrse(course, board);
		return board.getBoardName();
	}

	public static void getCourseExist(Course course, Board board) {
		List<String> courseNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		DynamoDBMapper mapper;
		mapper = new DynamoDBMapper(getClient());
		Course crse1 = mapper.load(Course.class, course.getcourseName());
		crse1.setBoardName(board.getBoardName());
		String topicArn = createTopic();
		crse1.setNotificationTopic(topicArn);
		mapper.save(course);
	}

	public static void getCrse(Course course, Board board) {
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		DynamoDBMapper mapper;
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		mapper = new DynamoDBMapper(getClient());
		ScanRequest scanRequest = new ScanRequest().withTableName("Course");
		ScanResult scanResult = client.scan(scanRequest);
		Course courseInfo = null;
		for (Map<String, AttributeValue> courseItem : scanResult.getItems()) {
			try {
				AttributeValue v = courseItem.get("courseName");
				String crseName = v.getS();
				if (crseName.equalsIgnoreCase(course.getcourseName())) {
					v = courseItem.get("id");
					String id = v.getS();
					courseInfo = mapper.load(Course.class, id);
					courseInfo.setBoardName(board.getBoardName());
					String topicArn = createTopic();
					courseInfo.setNotificationTopic(topicArn);
					mapper.save(courseInfo);
					break;
				} else {
				}
			} catch (Exception e) {
			}
		}
	}

	public static String createTopic() {
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion("us-west-2").build();
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyTopic");
		final CreateTopicResult createTopicResponse = snsClient.createTopic(createTopicRequest);
		return createTopicResponse.getTopicArn();
	}

	public static AmazonDynamoDB getClient() {
		return AmazonDynamoDBClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
				.withRegion("us-west-2").build();
	}

}
