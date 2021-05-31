//import util.properties packages
import java.util.Properties;
//import simple producer packages
import org.apache.kafka.clients.producer.Producer;
//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;
//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;
//header files
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//Create java class named “SimpleProducer”
public class SimpleProducer {
   
   public static void main(String[] args) throws Exception{
      /*//Assign topicName to string variable
      String topicName = args[0].toString();
      */

      // create instance for properties to access producer configs   
      Properties props = new Properties();
      //Assign localhost id
      props.put("bootstrap.servers", "localhost:9092");
      //Set acknowledgements for producer requests.      
      props.put("acks", "all");
      //If the request fails, the producer can automatically retry,
      props.put("retries", 0);
      //Specify buffer size in config
      props.put("batch.size", 16384);
      //Reduce the no of requests less than 0   
      props.put("linger.ms", 1);
      //The buffer.memory controls the total amount of memory available to the producer for buffering.   
      props.put("buffer.memory", 33554432);
      props.put("key.serializer", 
         "org.apache.kafka.common.serialization.StringSerializer");
      props.put("value.serializer", 
         "org.apache.kafka.common.serialization.StringSerializer");
      
      Producer<String, String> producer = new KafkaProducer<String, String>(props);

      String fileName = "test.csv";
      Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
                  // loop until all lines are read
                  while (line != null) {

                     // use string.split to load a string array with the values from
                     // each line of
                     // the file, using a comma as the delimiter
                     System.out.println('W');
                     String[] attributes = line.split(",");
                     //SEND RECORD TO PRODUCER
                     //producer.send(new ProducerRecord<String, String>("pub_log", attributes[0], attributes[1]));
                     System.out.println("about to send...");
                     producer.send(new ProducerRecord<String, String>("pub_log", "a", "b"));
                     System.out.println("sent");
                     // read next line before looping
                     // if end of file reached, line would be null
                     line = br.readLine();
                     System.out.println('c');
                 }
                 producer.close();
     
             } catch (IOException ioe) {
                 ioe.printStackTrace();
             }
               
   }
}