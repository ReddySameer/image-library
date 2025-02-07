package com.synchrony.image.library.kinesis;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;

@Service
public class AwsKinesisClient {

	
	@Bean
	public static AmazonKinesis getAWSKinesisClient() {
		
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIAQ4J5X2AKAW56CZM3", "O7/YB6NFKKPWU3WLWx3zJ0u6LQEVZi/a7o7WauGi");
	    return AmazonKinesisClientBuilder.standard()
	      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	      .withRegion(Regions.US_EAST_1)
	      .build();	
	}
}
