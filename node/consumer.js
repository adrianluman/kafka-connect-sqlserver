
var kafka = new KafkaRest({ 'url': 'http://localhost:8082' });
var format = "avro";
var consumerConfig = {
	"format": format,
};
if (process.env.FROM_BEGINNING) {
	consumerConfig['auto.offset.reset'] = 'smallest';
}
var topicName = "my-timestamp-user";
var consumerGroup = topicName;

kafka.consumer(consumerGroup).join(consumerConfig, function(err, consumer_instance) {
	if (err) return console.log("Failed to create instance in consumer group: " + err);

	console.log("Consumer instance initialized: " + consumer_instance.toString());
	var stream = consumer_instance.subscribe(topicName);
	stream.on('data', function(msgs) {
		process_msg(msgs);
		async function process_msg(msgs) {
			for(var i = 0; i < msgs.length; i++) {
				if (format == "binary") {
					// Messages keys (if available) and values are decoded from base64 into Buffers. You'll need to decode based
					// on whatever serialization format you used. By default here, we just try to decode to text.
					// console.log(msgs[i].value.toString('utf8'));
					console.error("Incorrect format binary ", msgs[i].value.toString('utf8'));
					// Also available: msgs[i].key, msgs[i].partition
				} else {
					console.log(JSON.stringify(msgs[i].value));
				}
			}
		}
	});
	stream.on('error', function(err) {
		console.log("Consumer instance reported an error: " + err);
		console.log("Attempting to shut down consumer instance...");
		consumer_instance.shutdown(logShutdown);
	});
	stream.on('end', function() {
		console.log("Consumer stream closed.");
	});

	// Events are also emitted by the parent consumer_instance, so you can either consume individual streams separately
	// or multiple streams with one callback. Here we'll just demonstrate the 'end' event.
	consumer_instance.on('end', function() {
		console.log("Consumer instance closed.");
	});

	// Also trigger clean shutdown on Ctrl-C
	process.on('SIGINT', function() {
		console.log("Attempting to shut down consumer instance...");
		consumer_instance.shutdown(logShutdown);
	});

});

function logShutdown(err) {
	if (err)
		console.log("Error while shutting down: " + err);
	else
		console.log("Shutdown cleanly.");
	process.exit();
}
