package com.synchrony.image.library.kinesis;

import java.nio.ByteBuffer;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KinesisProducer {

	@Autowired
	AwsKinesisClient kinesisClient;
	
	/*
	 * public static void main(String[] args) {
	 * 
	 * AmazonKinesis kinesisClient = AwsKinesisClient.getAWSKinesisClient();
	 * KinesisProducer producer = new KinesisProducer(); producer.sendData("test" +
	 * new Date(), "testUser"); }
	 */


	
	public PutRecordResult sendData(String jsonString, String userName) { //, AmazonKinesis kinesisClient) {
		PutRecordRequest putRecordRequest = new PutRecordRequest();
		putRecordRequest.setStreamName("user-image");		
		putRecordRequest.setPartitionKey(userName);
		putRecordRequest.setData(ByteBuffer.wrap(jsonString.getBytes()));
		PutRecordResult putRecordResult = kinesisClient.getAWSKinesisClient().putRecord(putRecordRequest);
		System.out.println("Result ShardID: " + putRecordResult.getShardId());
		System.out.println(putRecordResult.getSequenceNumber());
		return putRecordResult;
	}

	
	private PutRecordsRequestEntry createPutRecord(String jsonString, String userName) {
		PutRecordsRequestEntry requestEntry = new PutRecordsRequestEntry();
		requestEntry.setData(ByteBuffer.wrap(jsonString.getBytes()));
		requestEntry.setPartitionKey(userName);
		return requestEntry;		
	}
}
