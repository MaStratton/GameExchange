
#!/bin/sh

KT="/opt/bitnami/kafka/bin/kafka-topics.sh"

echo "Waiting for kafka..."
"$KT" --bootstrap-server localhost:9092 --list

echo "Creating kafka topics"
"$KT" --bootstrap-server localhost:9092 --create --if-not-exists --topic password_change --replication-factor 1 --partitions 3
"$KT" --bootstrap-server localhost:9092 --create --if-not-exists --topic offer_created --replication-factor 1 --partitions 3
"$KT" --bootstrap-server localhost:9092 --create --if-not-exists --topic offer_updated --replication-factor 1 --partitions 3

echo "Successfully created the following topics:"
"$KT" --bootstrap-server localhost:9092 --list
