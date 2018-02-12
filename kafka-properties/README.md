# Confluent Kafka Connect properties
Using Confluent Kafka Connect Source and Sink to connect two database

## How to Run:

Load the connectors

- confluent load {name} -d ./source-sqlserver-user.properties
- confluent load {name} -d ./sink-postgres-user.properties

To check if the connectors is running

- confluent status connector
- confluent log connect

## More complex configurations

- https://docs.confluent.io/current/connect/connect-jdbc/docs/source_config_options.html
- https://docs.confluent.io/current/connect/connect-jdbc/docs/sink_config_options.html
