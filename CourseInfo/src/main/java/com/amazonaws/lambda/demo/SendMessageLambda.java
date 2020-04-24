package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.lambda.datamodel.Announcement;
import com.amazonaws.lambda.datamodel.Board;
import com.amazonaws.lambda.datamodel.Course;
import com.amazonaws.lambda.datamodel.DynamoDbConnector;
import com.amazonaws.lambda.datamodel.Registrar;
import com.amazonaws.regions.Regions;
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
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SendMessageLambda implements RequestHandler<Announcement, String> {

	@Override
	public String handleRequest(Announcement announcement, Context context) {
		String announcementText = announcement.getAnnouncementText();
		String topicArn = getTopicForBoard(announcement.getBoardName());
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
		PublishRequest publishRequest = new PublishRequest(topicArn, announcementText);
		PublishResult publishResult = snsClient.publish(publishRequest);
		return announcementText;
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
