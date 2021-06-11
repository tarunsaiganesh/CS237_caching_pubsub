import java.util.Properties;
import java.util.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.lang.*;

import java.io.FileWriter;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors


public class Consumer_Interface {
   public static void main(String[] args) throws Exception {
      //Produer properties
	  String ts_file = args[0];
	  FileWriter myWriter = new FileWriter(ts_file);
      Properties subscriber_props = new Properties();
	  //FileWriter myWriter = new FileWriter("timestamp.txt");
           
      subscriber_props.put("bootstrap.servers", "localhost:9092");
      subscriber_props.put("acks", "all");
      subscriber_props.put("retries", 0);
      subscriber_props.put("batch.size", 16384);
      subscriber_props.put("linger.ms", 1);
      subscriber_props.put("buffer.memory", 33554432);
      subscriber_props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      subscriber_props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
      Producer<String, String> sub_producer = new KafkaProducer<String, String>(subscriber_props);

      //send hard-coded sub data
      System.out.println("Sending subscriptions...");
      sub_producer.send(new ProducerRecord<String, String>("sub_log", "1", "10001"));
      sub_producer.send(new ProducerRecord<String, String>("sub_log", "2", "00110"));
      sub_producer.send(new ProducerRecord<String, String>("sub_log", "3", "10001"));
	  //sub_producer.send(new ProducerRecord<String, String>("sub_log", "4", "11000"));
      System.out.println("...sent");
      sub_producer.close();

      //Kafka consumer configuration settings
      Properties consumer_props = new Properties();
      
      consumer_props.put("bootstrap.servers", "localhost:9092");
      consumer_props.put("group.id", "test1");
      consumer_props.put("auto.offset.reset", "earliest"); //added to config
      consumer_props.put("enable.auto.commit", "true");
      consumer_props.put("auto.commit.interval.ms", "1000");
      consumer_props.put("session.timeout.ms", "30000");
      consumer_props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      consumer_props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      KafkaConsumer<String, String> c1_poller = new KafkaConsumer<String, String>(consumer_props);
      consumer_props.put("group.id", "test2");
      KafkaConsumer<String, String> c2_poller = new KafkaConsumer<String, String>(consumer_props);
      consumer_props.put("group.id", "test3");
      KafkaConsumer<String, String> c3_poller = new KafkaConsumer<String, String>(consumer_props);
      
      //Kafka Consumer subscribes list of topics here.
      c1_poller.subscribe(Arrays.asList("consumer1"));
      c2_poller.subscribe(Arrays.asList("consumer2"));
      c3_poller.subscribe(Arrays.asList("consumer3"));
	  //c4_poller.subscribe(Arrays.asList("consumer3"));
      
      //print the topic names
      System.out.println("Subscribed to topics: " + "consumer1_topic, " + "consumer2_topic, " + "consumer3_topic");
      //create new datab_producer object
      Producer<String, String> datab_producer = new KafkaProducer<String, String>(subscriber_props);
      //create two ArrayLists
      ArrayList<Integer> lastSeen = new ArrayList<Integer>();
      lastSeen.add(0);
      lastSeen.add(0);
      lastSeen.add(0);
      ArrayList<Integer> consumerID = new ArrayList<Integer>();
      consumerID.add(1);
      consumerID.add(2);
      consumerID.add(3);
	  ArrayList<Long> latency = new ArrayList<Long>();
      latency.add(0L);
      latency.add(0L);
      latency.add(0L);
	  ArrayList<Integer> dummySeqNo = new ArrayList<Integer>();
      dummySeqNo.add(-1);
      dummySeqNo.add(-1);
      dummySeqNo.add(-1);
      //initalize consumer iterable
      int j;
	  
      while (true) 
      {
         j = 0;

         //consumer1 
         ConsumerRecords<String, String> consumer1 = c1_poller.poll(1);
         for (ConsumerRecord<String, String> record : consumer1)
            {
			if(Integer.parseInt(record.key()) == dummySeqNo.get(j)){
				System.out.println("Consumer " + j + " finished...");
			}
			if(!(record.value().equals(""))){ 
				String[] token = record.value().split(";");
				latency.set(j, latency.get(j) + System.currentTimeMillis() - Long.parseLong(token[1]));
			}
			else{
				dummySeqNo.set(j, Integer.parseInt(record.key()) - 1);
			}
            // print the offset,key and value for the consumer records.
            //System.out.println("---consumer1---" );
            //System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            if (Integer.parseInt(record.key()) != lastSeen.get(j) + 1 && Integer.parseInt(record.key())>lastSeen.get(j))
               {
                  int i;
                  for(i = lastSeen.get(j)+1;i<Integer.parseInt(record.key());i++)
                  {
                     datab_producer.send(new ProducerRecord<String, String>("database_log", consumerID.get(j).toString(), ""+i));
                  }
               }
            if(Integer.parseInt(record.key())>lastSeen.get(j))
               {
                  lastSeen.set(j, Integer.parseInt(record.key()));
               }
            }
         j++;

         //consumer2
         ConsumerRecords<String, String> consumer2 = c2_poller.poll(1);
         for (ConsumerRecord<String, String> record : consumer2)
         {
			if(Integer.parseInt(record.key()) == dummySeqNo.get(j)){
				System.out.println("Consumer " + j + " finished...");
			}
			if(!(record.value().equals(""))){
				//System.out.println("Record Value: " + record.value());	 
				String[] token = record.value().split(";");
				latency.set(j, latency.get(j) + System.currentTimeMillis() - Long.parseLong(token[1]));
			}
			else{
				dummySeqNo.set(j, Integer.parseInt(record.key()) - 1);
			}
            // print the offset,key and value for the consumer records.
            //System.out.println("---consumer2---" );
            //System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            if (Integer.parseInt(record.key()) != lastSeen.get(j) + 1 && Integer.parseInt(record.key())>lastSeen.get(j))
            {
               int i;
               for(i = lastSeen.get(j)+1;i<Integer.parseInt(record.key());i++)
               {
                  datab_producer.send(new ProducerRecord<String, String>("database_log", consumerID.get(j).toString(), ""+i));
               }
            }
            if(Integer.parseInt(record.key())>lastSeen.get(j))
            {
               lastSeen.set(j, Integer.parseInt(record.key()));
            }
         }
         j++;
         
         //consumer3
         ConsumerRecords<String, String> consumer3 = c3_poller.poll(1);
         for (ConsumerRecord<String, String> record : consumer3)
         {
			if(Integer.parseInt(record.key()) == dummySeqNo.get(j)){
				System.out.println("Consumer " + j + " finished...");
			}
			if(!(record.value().equals(""))){ 
				String[] token = record.value().split(";");
				latency.set(j, latency.get(j) + System.currentTimeMillis() - Long.parseLong(token[1]));
			}
			else{
				dummySeqNo.set(j, Integer.parseInt(record.key()) - 1);
			}
            // print the offset,key and value for the consumer records.
            //System.out.println("---consumer3---" );
            //System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            if (Integer.parseInt(record.key()) != lastSeen.get(j) + 1 && Integer.parseInt(record.key())>lastSeen.get(j))
            {
               int i;
               for(i = lastSeen.get(j)+1;i<Integer.parseInt(record.key());i++)
               {
                  datab_producer.send(new ProducerRecord<String, String>("database_log", consumerID.get(j).toString(), ""+i));
               }
            }
            if(Integer.parseInt(record.key())>lastSeen.get(j))
            {
               lastSeen.set(j, Integer.parseInt(record.key()));
            }
         }
		
		 long ts = latency.get(0) + latency.get(1) + latency.get(2);
		 myWriter.write("Average Latency: " + ts + "\n");
      				
		  
      }
		//myWriter.close();
   }
   
}
