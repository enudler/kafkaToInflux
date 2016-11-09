
cd ..
mvn clean compile assembly:single

cp target/kafkaToInflux-1.0-jar-with-dependencies.jar docker/
cd docker
mv kafkaToInflux-1.0-jar-with-dependencies.jar kafkaToInflux.jar

docker build . -t registry-dev.zooz.co:5000/kafka-to-influx