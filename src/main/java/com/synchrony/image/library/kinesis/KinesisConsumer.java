package com.synchrony.image.library.kinesis;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.ShardIteratorType;

public class KinesisConsumer {

	 private String IPS_SHARD_ID = "shardId-000000000000";
	  private GetShardIteratorResult shardIterator;
		public static void main(String[] args) {

			KinesisConsumer consumer = new KinesisConsumer();
			AmazonKinesis kinesisClient = AwsKinesisClient.getAWSKinesisClient();
			consumer.buildShardIterator(kinesisClient);
			consumer.consumeKinesisRecords(kinesisClient);
			
			
		}
	private void buildShardIterator( AmazonKinesis kinesisClient) {
	    GetShardIteratorRequest readShardsRequest = new GetShardIteratorRequest();
	    readShardsRequest.setStreamName("user-image");
	    readShardsRequest.setShardIteratorType(ShardIteratorType.TRIM_HORIZON);
	    readShardsRequest.setShardId(IPS_SHARD_ID);

	    this.shardIterator =   kinesisClient.getShardIterator(readShardsRequest);
	}
	
	public void consumeKinesisRecords( AmazonKinesis kinesisClient) {
		 GetRecordsRequest recordsRequest = new GetRecordsRequest();
	        recordsRequest.setShardIterator(shardIterator.getShardIterator());
	        recordsRequest.setLimit(25);

	        GetRecordsResult recordsResult =   kinesisClient.getRecords(recordsRequest);
	        while (!recordsResult.getRecords().isEmpty()) {
	            recordsResult.getRecords()
	                .stream()
	                .map(record -> new String(record.getData()
	                    .array()))
	                .forEach(System.out::println);

	            recordsRequest.setShardIterator(recordsResult.getNextShardIterator());
	            recordsResult =   kinesisClient.getRecords(recordsRequest);
	            System.out.println("RecordsResult size: " + recordsResult.getRecords().size());
	        }
	}
}

