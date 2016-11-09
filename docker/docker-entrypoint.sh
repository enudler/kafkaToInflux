#!/usr/bin/env bash

exec java -jar kafkaToInflux.jar $kafkaUrl $influxUrl $topicName