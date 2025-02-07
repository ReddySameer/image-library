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

	//private static final String AWS_ACCESS_KEY="aws.accessKeyId";
	//private static final String AWS_SECRET_KEY="aws.secretKey";

	
	static {
		System.setProperty(AWS_ACCESS_KEY, "");
		System.setProperty(AWS_SECRET_KEY, "");
	}
	@Bean
	public static AmazonKinesis getAWSKinesisClient() {
		
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials("", "");
	    return AmazonKinesisClientBuilder.standard()
	      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	      .withRegion(Regions.US_EAST_1)
	      .build();	
	}
}
