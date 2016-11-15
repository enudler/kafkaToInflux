#!/usr/bin/env bash

exec java -jar kafkaToInflux.jar $KAFKAURL $INFLUXURL $TOPICNAME