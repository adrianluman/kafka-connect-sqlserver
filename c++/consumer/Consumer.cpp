#include <stdexcept>
#include <iostream>
#include <csignal>
#include <boost/program_options.hpp>
#include <cppkafka/consumer.h>
#include <cppkafka/configuration.h>
#include <librdkafka/rdkafka.h>
extern "C"
{
	#include <libserdes/serdes-avro.h>
}

using std::string;
using std::exception;
using std::cout;
using std::endl;

using cppkafka::Consumer;
using cppkafka::Configuration;
using cppkafka::Message;
using cppkafka::TopicPartitionList;

namespace po = boost::program_options;

bool running = true;

int main(int argc, char* argv[]) {

    // Stop processing on SIGINT
    signal(SIGINT, [](int) { running = false; });

    // Construct the configuration
    Configuration config = {
        { "metadata.broker.list", "127.0.0.1:9092" },
        { "group.id", "foo" }
        // Disable auto commit
        // { "enable.auto.commit", false }
    };

    // Create the consumer
    Consumer consumer(config);

    // Subscribe to the topic
    consumer.subscribe({ "my-timestamp-user" });

    cout << "Consuming messages from topic " << "my-timestamp-user" << endl;

    char errstr[512];
    serdes_conf_t *sconf;

    sconf = serdes_conf_new(NULL, 0,
                                /* Default URL */
                                "schema.registry.url", "http://localhost:8081",
                                NULL);

    serdes_t *serdes = serdes_new(sconf, errstr, sizeof(errstr));
    if (!serdes)
    {
        fprintf((stderr), "%% Failed to create serdes handle: %s\n", errstr);
        exit(1);
    }

    serdes_schema_t *schema;
    schema = serdes_schema_get(serdes, "my-timestamp-user-value", 41,
                           errstr, sizeof(errstr));
    if (!schema)
    {
        fprintf((stderr), "%% Failed to get serdes schema: %s\n", errstr);
        exit(1);
    }

    // Now read lines and write them into kafka
    while (running) {
        // Try to consume a message
        Message msg = consumer.poll();
        if (msg) {
            // If we managed to get a message
            if (msg.get_error()) {
                // Ignore EOF notifications from rdkafka
                if (!msg.is_eof()) {
                    cout << "[+] Received error notification: " << msg.get_error() << endl;
                }
            }
            else {
                // Print the key (if any)
                if (msg.get_key()) {
                    cout << msg.get_key() << " -> ";
                }
                // Print the payload
                cout << msg.get_payload() << endl;
                // Now commit the message

                avro_value_t avro;
                char *as_json;
                serdes_err_t err = serdes_deserialize_avro(serdes, &avro, &schema,
                                                           msg.get_handle()->payload, msg.get_handle()->len,
                                                           errstr, sizeof(errstr));
                if (err) {
                    fprintf(stderr, "%% serdes_deserialize_avro failed: %s\n", errstr);
                    exit(1);
                }

                if (avro_value_to_json(&avro, 1, &as_json))
                    fprintf(stderr, "%% avro_to_json failed: %s\n",
                            avro_strerror());
                else {
                        printf("%s\n", as_json);
                        free(as_json);
                }

                consumer.commit(msg);
            }
        }
    }
}
