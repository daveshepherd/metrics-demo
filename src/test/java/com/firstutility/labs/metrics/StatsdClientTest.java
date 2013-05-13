package com.firstutility.labs.metrics;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import org.junit.Test;

public class StatsdClientTest {

	@Test
	public void test() throws UnknownHostException, IOException, InterruptedException {

		StatsdClient client = new StatsdClient("10.11.12.53", 8125);
		// increment by 1
		
		for (int i=0;i<60; i++) {
			client.increment("foo.bar.baz");
			Thread.sleep(1000l);
		}
		
		// increment by 10
		client.increment("foo.bar.baz", 10);
		// sample rate
		client.increment("foo.bar.baz", 10, .1);
		// increment multiple keys by 1
		client.increment(1, .1, "foo.bar.baz", "foo.bar.boo", "foo.baz.bar");
		// increment multiple keys by 10 -- yeah, it's "backwards"
		client.increment(10, .1, "foo.bar.baz", "foo.bar.boo", "foo.baz.bar");
		// multiple keys with a sample rate
		client.increment(10, .1, "foo.bar.baz", "foo.bar.boo", "foo.baz.bar");

		int timerValue = Math.abs(new Random().nextInt() % 1000);
		client.timing("foo.bar.time", timerValue);

		int gaugeValue = Math.abs(new Random().nextInt() % 100);
		client.gauge("foo.bar.gauge", gaugeValue);
		
		// To enable multi metrics (aka more than 1 metric in a UDP packet)
		// (disabled by default)
		client.enableMultiMetrics(true); // disable by passing in false
		// To fine-tune udp packet buffer size (default=1500)
		client.setBufferSize((short) 1500);
		// To force flush the buffer out (good idea to add to your shutdown
		// path)
		client.flush();
	}
}
