package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.lambda.datamodel.Announcement;
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

public class GetAnnouncementMessage implements RequestHandler<Course, String> {

	@Override
	public String handleRequest(Course course, Context context) {
		context.getLogger().log("Input: " + course);
		DynamoDBMapper mapper;
		Table dynamoDBTable;
		DynamoDB dynamoDB = new DynamoDB(getClient());
		dynamoDBTable = dynamoDB.getTable("Announcement");
		mapper = new DynamoDBMapper(getClient());
		Announcement announcement = new Announcement();
		announcement.setAnnouncementName(course.getcourseName() + " announcement");
		announcement.setAnnouncementText("you are registered for the course " + course.getcourseName());
		String boardName = getCourseNameList(course.getcourseName());
		announcement.setBoardName(boardName);
		mapper.save(announcement);
		return announcement.getAnnouncementText();
	}

	public static String getCourseNameList(String courseName1) {
		List<String> courseNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		ScanRequest scanRequest = new ScanRequest().withTableName("Board");
		ScanResult scanResult = client.scan(scanRequest);
		for (Map<String, AttributeValue> board : scanResult.getItems()) {
			try {
				AttributeValue v = board.get("courseName");
				String courseName = v.getS();
				if (courseName.equals(courseName1)) {
					AttributeValue v1 = board.get("boardName");
					String id = v1.getS();
					return id;

				}

			} catch (Exception e) {
			}
		}
		return "";
	}

	public String createTopic() {
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion("us-west-2").build();
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyTopic");
		final CreateTopicResult createTopicResponse = snsClient.createTopic(createTopicRequest);
		return createTopicResponse.getTopicArn();
	}

	public AmazonDynamoDB getClient() {
		return AmazonDynamoDBClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
				.withRegion("us-west-2").build();
	}

}
