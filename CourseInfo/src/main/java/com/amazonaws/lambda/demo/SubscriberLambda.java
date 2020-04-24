package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.lambda.datamodel.Announcement;
import com.amazonaws.lambda.datamodel.Course;
import com.amazonaws.lambda.datamodel.DynamoDbConnector;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SubscriberLambda implements RequestHandler<Announcement, String> {

	@Override
	public String handleRequest(Announcement announcement, Context context) {
		String topicArn = getTopicForBoard(announcement.getBoardName());
		String profId = getProfessorId(announcement.getBoardName());
		String emailId = getProfNameList(profId);
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion("us-west-2").build();

		final SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", emailId);
		snsClient.subscribe(subscribeRequest);
		context.getLogger().log("Input: " + emailId);
		return emailId;
	}

	public static String getProfNameList(String prof) {
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

				if (profName.equalsIgnoreCase(prof)) {
					AttributeValue v1 = profs.get("emailId");
					String id = v1.getS();
					return id;

				}

			} catch (Exception e) {
			}
		}
		return "";
	}

	public static String getProfessorId(String boardName) {
		List<String> profNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		ScanRequest scanRequest = new ScanRequest().withTableName("Course");
		ScanResult scanResult = client.scan(scanRequest);
		for (Map<String, AttributeValue> profs : scanResult.getItems()) {
			try {
				AttributeValue v = profs.get("boardName");
				String brdName = v.getS();

				if (brdName.equalsIgnoreCase(boardName)) {
					AttributeValue v1 = profs.get("professorId");
					String id = v1.getS();
					return id;

				}

			} catch (Exception e) {
			}
		}
		return "";
	}

	public static String getTopicForBoard(String boardName) {
		List<String> courseNames = new ArrayList<>();
		DynamoDbConnector dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector = new DynamoDbConnector();
		dynamoDbConnector.init();
		AmazonDynamoDB client = dynamoDbConnector.getClient();
		ScanRequest scanRequest = new ScanRequest().withTableName("Course");
		ScanResult scanResult = client.scan(scanRequest);
		for (Map<String, AttributeValue> course : scanResult.getItems()) {
			try {
				AttributeValue v = course.get("boardName");
				String courseName = v.getS();
				if (courseName.equalsIgnoreCase(boardName)) {
					AttributeValue v1 = course.get("notificationTopic");
					String id = v1.getS();
					return id;

				}

			} catch (Exception e) {
			}
		}
		return "";
	}

}