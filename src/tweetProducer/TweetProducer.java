package tweetProducer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import hw2.avro.Tweet;
import twitter4j.StallWarning;
import twitter4j.StatusDeletionNotice;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class TweetProducer {
	
	public static void produce(){
		ConfigurationBuilder cbld = new ConfigurationBuilder();
		cbld.setDebugEnabled(true)
		  .setOAuthConsumerKey("0SnceU35QxAZV3ou7zSzP0AKG")
		  .setOAuthConsumerSecret("8up8FHjNiAr98Mg7NVQqDfrMp7X5G3BPFlgkWjFITP2muhJnSI")
		  .setOAuthAccessToken("76229908-IkHhXvWabZrZuGKr4RXEAmnl6bU6XkHXxmu6sqW0n")
		  .setOAuthAccessTokenSecret("hUIGUwJdGa2XEGqufZr8iaCu95bkGFC3GqcizHsp6cD5L");
		TwitterStream stream = new TwitterStreamFactory(cbld.build()).getInstance();
		
		Properties props = new Properties();
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("bootstrap.servers", "localhost:9092");
		props.put("metadata.broker.list", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayerializer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		final KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
		
		stream.addListener(new StatusListener() {
			
			public void onStatus(Status status) {
				//remove spammy ass twitter bot
				if (!status.getUser().getScreenName().equals("jasminveilleux2")) {
					Tweet tweet = new Tweet();
					tweet.setDescription(status.getUser().getDescription());
					tweet.setFollowersCount(status.getUser().getFollowersCount());
					tweet.setGeoEnabled(status.getUser().isGeoEnabled());
					tweet.setId(status.getUser().getId());
					tweet.setLang(status.getUser().getLang());
					tweet.setLocation(status.getUser().getLocation());
					tweet.setScreenName(status.getUser().getScreenName());
					tweet.setStatusesCount(status.getUser().getStatusesCount());
					tweet.setCreatedAt(status.getCreatedAt().toString());
					tweet.setFavoriteCount(status.getFavoriteCount());
					tweet.setId(status.getId());
					tweet.setRetweetCount(status.getRetweetCount());
					tweet.setSource(status.getSource());
					tweet.setText(status.getText());
					
					HashtagEntity[] tags = status.getHashtagEntities();
					List<CharSequence> hashtags = new ArrayList<CharSequence>();
					for(HashtagEntity tag : tags) {
						hashtags.add(tag.getText());
					}
					tweet.setHashtags(hashtags);
					
					URLEntity[] links = status.getURLEntities();
					List<CharSequence> urls = new ArrayList<CharSequence>();
					for(URLEntity link : links) {
						urls.add(link.getText());
					}
					tweet.setUrls(urls);
					
					UserMentionEntity[] mentions = status.getUserMentionEntities();
					List<CharSequence> user_mentions = new ArrayList<CharSequence>();
					for(UserMentionEntity mention : mentions) {
						user_mentions.add(mention.getText());
					}
					tweet.setMentions(user_mentions);
					
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
					
					DatumWriter<Tweet> tweetWriter = new SpecificDatumWriter<Tweet>(Tweet.getClassSchema());
					System.out.println(tweet);
					
					try {
						tweetWriter.write(tweet, encoder);
						encoder.flush();
						outputStream.close();
						byte[] bytes = outputStream.toByteArray();
						ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("Ravens", bytes);
						producer.send(record);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				System.out.println("User: " + arg0.getUserId() + " deleted status: " + arg0.getStatusId()); 
				
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});	
		FilterQuery filter = new FilterQuery();
		filter.track(new String[]{"Joe Flacco", "Terrell Suggs", "Ravens", "Baltimore Ravens", "John Harbaugh", "SSSr",
				"Marshal Yanda", "Elvis Dumervil", "Laquon Treadwell", "Jalen Ramsey", "Vernon Hargreaves", "Kelechi Osemele", 
				"Deforest Buckner", "Alex Ovechkin", "Washington Capitals", "Evgeny Kuznetsov", "Nicklas Backstrom", 
				"Braden Holtby", "TJ Oshie", "Andre Burakovsky", "Staind", "Aaron Lewis", "Caps"}); 
		filter.language(new String[]{"en"});
		stream.filter(filter);
	}
}
