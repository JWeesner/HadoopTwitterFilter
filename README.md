# HadoopTwitterFilter
1. To create avro schema java files perform(note, these files are prepackaged for you in the TweetJSON java project included):

java -jar $PATH_TO_JAR/avro-tools-1.8.0.jar compile schema user.avsc tweet.avsc .

2. To create kafka topic:

-Ensure Kafka service is running(bin/kafka-server-start.sh config/server.properties)

-Create topic using kafka topics shell script(bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic $TOPIC_NAME_HERE)
*The current included topic name is "Ravens". If you wish to change this you need to change "Ravens" to new topic at line #105 in TweetProducer and line #48 in TweetConsumer

3. Create required HDFS folders:
-hdfs dfs -mkdir /user
-hdfs dfs -mkdir /user/username(enter your username here)
-hdfs dfs -mkdir /user/username/in
-hdfs dfs -mkdir /user/username/in/tweets
-chown the /user/username/in/tweets directory

4. Create runnable JAR file of TweetJSON. Import the project into eclipse and export as runnable JAR file.
*Note, needed JARs for compilation are not included due to size constraints. 

Needed jars are as follows:
*avro-1.8.0.jar
*avro-tools-1.8.0.jar
*kafka_2.11-0.9.0.0-sources.jar
*kafka_2.11-0.9.0.0.jar
*kafka-clients-0.9.0.0.jar
*kafka-tools-0.9.0.0.jar
*scala-library-2.11.7.jar
*twitter4j-core-4.0.4.jar
*twitter4j-stream-4.0.4.jar


5. To start the producer:
-Run command: java -jar TweetJSON.jar producer

6. To start the consumer:
-Run command: java -jar TweetJSON.jar consumer numTweets numSeconds username

where numTweets is the number of tweets before rolling to new HDFS file and numSeconds is the number of seconds between file creation
username will be appended to /user/$username/in/tweets/$HDFSFILENAME to create hdfs file in proper folder. If the program outputs a permissions exception you need to chown the /user/username/in/tweets directory

7. Check to make sure tweets are properly writing to HDFS:
-hdfs dfs -ls -R /user/username/in/tweets/
-hdfs dfs -cat /user/username/in/tweets/$ANY_FILENAME_FOUND_IN_LS_ABOVE