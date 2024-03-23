#!/bin/bash

# Create the keystore and truststore directories
mkdir -p secrets

echo "password" > secrets/password

# Generate the private key and self-signed certificate
openssl req -newkey rsa:2048 -nodes -keyout secrets/kafka.key -x509 -days 365 -out secrets/kafka.crt -subj "/CN=kafka"

# Create the keystore and import certificates
keytool -keystore secrets/kafka.keystore.jks -storepass password -alias kafka -certreq -file kafka.csr
keytool -keystore secrets/kafka.keystore.jks -storepass password -alias CARoot -import -file secrets/kafka.crt
keytool -keystore secrets/kafka.keystore.jks -storepass password -alias localhost -import -file secrets/kafka.crt
keytool -keystore secrets/kafka.keystore.jks -storepass password -alias CARoot -import -file secrets/kafka.crt

# Generate the private key and self-signed certificate for truststore
openssl req -newkey rsa:2048 -nodes -keyout secrets/truststore.key -x509 -days 365 -out secrets/truststore.crt -subj "/CN=kafka"

# Create the truststore
keytool -keystore secrets/kafka.truststore.jks -storepass password -alias CARoot -import -file secrets/truststore.crt