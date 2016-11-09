FROM ubuntu:xenial

# Install java
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    rm -rf /var/cache/oracle-jdk8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

COPY ./kafkaToInflux.jar /
COPY ./docker-entrypoint.sh /

ENTRYPOINT ["./docker-entrypoint.sh"]



