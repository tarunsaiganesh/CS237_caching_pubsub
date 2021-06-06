import java.util.Properties;
import java.util.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Subscriber_module{

    public static void addToList(HashMap<String, ArrayList<String>> items, String mapKey, String myItem) {
        ArrayList<String> itemsList = items.get(mapKey);
    
        // if list does not exist create it
        if(itemsList == null) {
             itemsList = new ArrayList<String>();
             itemsList.add(myItem);
             items.put(mapKey, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(myItem)) itemsList.add(myItem);
        }
        for(Map.Entry m : items.entrySet()){    
            System.out.println(m.getKey()+" "+m.getValue());    
        }
    }

    public static ArrayList<String> getFromList(HashMap<String, ArrayList<String>> items, String mapKey) {
        ArrayList<String> itemsList = new ArrayList<String>();
        if(items.get(mapKey) != null){
            itemsList = items.get(mapKey);
        }
        System.out.println("mapkey: " + mapKey);
        System.out.println(items.get(mapKey));
        return itemsList;
    }

	private static String writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
		String value = "";

        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            //subid = resultSet.getString("SUBID");
            //seqno = resultSet.getString("SEQNO");
            value = resultSet.getString("VALUE");
            
            //System.out.println("Sub ID: " + subid);
            //System.out.println("Seq No: " + seqno);
            System.out.println("Value: " + value);
            
        }
		return value;
			
    }


    public static void main(String[] args) {

        // Starting zookeeper, broker and create topics
        try{
        String zoo_cmd = "kafka_2.12-2.5.0/bin/zookeeper-server-start.sh kafka_2.12-2.5.0/config/zookeeper.properties";
        String broker_cmd = "kafka_2.12-2.5.0/bin/kafka-server-start.sh kafka_2.12-2.5.0/config/server.properties";
        String delete_pub_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic pub_log";
        String delete_sub_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic sub_log";
		String delete_datab_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic database_log";        
		String publication_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic pub_log";
        String subscription_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic sub_log";
		String database_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic database_log";        
		String consumer1_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic consumer1";
        String consumer2_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic consumer2";
        String consumer3_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic consumer3";
        //String consumer4_topic = "kafka_2.12-2.5.0/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic consumer4";
        Runtime run = Runtime.getRuntime();
        //run.exec(zoo_cmd);
        //run.exec(broker_cmd);
        Process del = run.exec(delete_pub_topic);
        del.waitFor();
        Process del2 = run.exec(delete_sub_topic);
        del2.waitFor();
		Process del3 = run.exec(delete_datab_topic);
        del3.waitFor();
        Process pr = run.exec(publication_topic);
        pr.waitFor();
        Process sr = run.exec(subscription_topic);
        sr.waitFor();
		Process dr = run.exec(database_topic);
		dr.waitFor();
        Process c1 = run.exec(consumer1_topic);
        c1.waitFor();
        Process c2 = run.exec(consumer2_topic);
        c2.waitFor();
        Process c3 = run.exec(consumer3_topic);
        c3.waitFor();
        //Process c4 = run.exec(consumer4_topic);
        //c4.waitFor();
		}
        catch (Exception e) {
            System.out.println(e);
        }

        // Create hash tables
        HashMap<String, ArrayList<String>> items = new HashMap<String, ArrayList<String>>();
        Random rand = new Random();

        /*addToList(items, "00000", String.valueOf(rand.nextInt(4)));
        addToList(items, "10000", String.valueOf(rand.nextInt(4)));
        addToList(items, "00100", String.valueOf(rand.nextInt(4)));
        addToList(items, "00010", String.valueOf(rand.nextInt(4)));
        */
        System.out.println("ready...");
        for(Map.Entry m : items.entrySet()){    
            System.out.println(m.getKey()+" "+m.getValue());    
           }

        //Consumer properties

        Properties consumer_props = new Properties();
      
        consumer_props.put("bootstrap.servers", "localhost:9092");
        consumer_props.put("group.id", "test1");
        consumer_props.put("auto.offset.reset", "earliest"); //added to config
        consumer_props.put("enable.auto.commit", "true");
        consumer_props.put("auto.commit.interval.ms", "1000");
        consumer_props.put("session.timeout.ms", "30000");
        consumer_props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer_props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> pub_poller = new KafkaConsumer<String, String>(consumer_props);
        consumer_props.put("group.id", "test2");
        KafkaConsumer<String, String> sub_poller = new KafkaConsumer<String, String>(consumer_props);
		consumer_props.put("group.id", "test3");
		KafkaConsumer<String, String> database_poller = new KafkaConsumer<String, String>(consumer_props);
        
        sub_poller.subscribe(Arrays.asList("sub_log"));
        pub_poller.subscribe(Arrays.asList("pub_log"));
		database_poller.subscribe(Arrays.asList("database_log"));

        //Produer properties
        Properties props = new Properties();
           
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
		Threshold cache_function = new Threshold();
		boolean isCached = false;
		int no_of_subs = 0;
		int seq_no;
		ArrayList<Integer> lastSeqNo = new ArrayList<Integer>();
      	lastSeqNo.add(0);
      	lastSeqNo.add(0);
      	lastSeqNo.add(0);

		Connection connect = null;
    	Statement statement = null;
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
		try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/datab?"+ "user=root&password=radhit");

			preparedStatement = connect.prepareStatement("truncate table food_pss");
			preparedStatement.executeUpdate();
			
		
                
        //System.out.println("a");   
    while (true){
			
            //System.out.println("b");
           //System.out.println("inside while loop");
           ConsumerRecords<String, String> sub_records = sub_poller.poll(1);
           //System.out.println(records.isEmpty());
           //System.out.println("x");
           for (ConsumerRecord<String, String> record : sub_records){
                // print the offset,key and value for the consumer records.
                System.out.printf("sub offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                addToList(items, record.value(), record.key());
           }
           //System.out.println("c");
           ConsumerRecords<String, String> pub_records = pub_poller.poll(1);
           //System.out.println(records.isEmpty());
           //System.out.println("d");
           for (ConsumerRecord<String, String> record : pub_records){
                // print the offset,key and value for the consumer records.
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
				ArrayList<String> list_of_sub = getFromList(items, record.value());
				System.out.println(list_of_sub);
				no_of_subs = list_of_sub.size();
				isCached = cache_function.update(no_of_subs);
				for(String s : list_of_sub){
					seq_no = lastSeqNo.get(Integer.parseInt(s)-1); 
					lastSeqNo.set(Integer.parseInt(s)-1, seq_no + 1);
			   	}


				if(isCached){
                      
                	for(String s : list_of_sub){
							
                	        System.out.println(s);
                	        System.out.println("consumer"+s);
                	        producer.send(new ProducerRecord<String, String>("consumer"+s, lastSeqNo.get(Integer.parseInt(s)-1).toString(), record.value()));
                	        System.out.println("Message sent successfully");
               		}
				}
				else{
					for(String s : list_of_sub){
						preparedStatement = connect.prepareStatement("insert into  datab.food_pss values (default, ?, ?, ?)");
	            		// "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
	            		// Parameters start with 1
	            		preparedStatement.setString(1, s);
	            		preparedStatement.setString(2, lastSeqNo.get(Integer.parseInt(s)-1).toString());
    	        		preparedStatement.setString(3, record.value());
    	        		preparedStatement.executeUpdate();
					}
				}
           }
           //System.out.println("looping");
			ConsumerRecords<String, String> database_records = database_poller.poll(1);
			for (ConsumerRecord<String, String> record : database_records){
                // print the offset,key and value for the consumer records.
                System.out.printf("sub offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                //addToList(items, record.value(), record.key());
				//Fetch from database and write to corresponding cache log
				statement = connect.createStatement();
           		// Result set get the result of the SQL query
            	resultSet = statement.executeQuery("select value from food_pss where subid='" + record.key() +"' and seqno='" + record.value() +"'");
            	String value = writeResultSet(resultSet);
				
				producer.send(new ProducerRecord<String, String>("consumer"+record.key(), record.value(), value));
           }
        }}catch (Exception e) {
			System.out.println(e);
            System.out.println("Error. Terminating....");
			System.out.println("Terminated.");
        }   
    }
}
