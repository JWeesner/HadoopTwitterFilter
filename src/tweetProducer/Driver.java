package tweetProducer;

import java.io.IOException;

public class Driver {

	public static void main(String[] args) throws IOException{
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("Producer")) {
				TweetProducer.produce();
			} else if (args[0].equalsIgnoreCase("Consumer")) {
				if (args.length == 4) {
					TweetConsumer.consume(args[1], args[2], args[3]);
				} else {
					System.out.println("Incorrect number of consumer arguments, please type Consumer followed by the number of messages then number of seconds for file rollover");
				}
			} else {
				System.out.println("Incorrect first argument, please type Consumer or Producer to determine program functionality");
			}
		} else {
			System.out.println("Insufficient command line arguments. Please specify Consumer or Producer to determine program functionality");
		}
	}
}
