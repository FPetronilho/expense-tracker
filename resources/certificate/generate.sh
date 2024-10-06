#!/bin/bash

service="expense-tracker"
CN="Expense Tracker"
pwd="Portfolio2k24!"

dev_dns="localhost"
sat_dns="localhost"
pre_dns="localhost"
prod_dns="localhost"

rm -f $service.p12
rm -f $service.p12

# generate the .p12 file: private and public certificate
keytool -genkeypair \
  -alias $service \
  -keyalg RSA \
  -keysize 4096 \
  -sigalg SHA512withRSA \
  -storetype PKCS12 \
  -validity 3650 \
  -keystore $service.p12 \
  -keypass $pwd \
  -storepass $pwd \
  -dname "CN=$CN, OU=Software Engineering, O=Portfolio, L=Braga, ST=Braga, C=Portugal" \
  -ext SAN=DNS:$dev_dns,DNS:$sat_dns,DNS:$pre_dns,DNS:$prod_dns

# export the public certificate from the .p12 file
keytool -exportcert \
    -alias $service \
    -keystore $service.p12 \
    -file $service.cer \
    -rfc \
    -storepass $pwd

# force delete certificate from Java Virtual Machine
keytool -delete \
  -alias $service \
  -cacerts \
  -noprompt

# install certificate in Java Virtual Machine
keytool -import \
  -alias $service \
  -cacerts \
  -file $service.cer \
  -noprompt

# copy certificate to Spring Boot App class path
cp -f $service.p12 ../../*-application/src/main/resources/certificate/