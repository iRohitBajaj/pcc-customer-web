#!/bin/bash
echo "starting to create .jks from instance.crt"
# export .crt plus .key into a .pfx
openssl pkcs12 -export -out $HOME/META-INF/instance.pfx -inkey /etc/cf-instance-credentials/instance.key -in /etc/cf-instance-credentials/instance.crt -password pass:changeit
# convert .pfx into a .jks
$HOME/.java-buildpack/open_jdk_jre/bin/keytool -importkeystore -srckeystore $HOME/META-INF/instance.pfx -srcstoretype pkcs12 -srcstorepass changeit -destkeystore $HOME/META-INF/keystore.jks -deststoretype jks -deststorepass changeit
$HOME/.java-buildpack/open_jdk_jre/bin/keytool -v -list -keystore $HOME/META-INF/keystore.jks -storepass changeit

# Convert platform certs to jks format
PEM_FILE=/etc/ssl/certs/ca-certificates.crt
PASSWORD=changeit
KEYSTORE=$HOME/WEB-INF/classes/truststore.jks
# number of certs in the PEM file
CERTS=$(grep 'END CERTIFICATE' $PEM_FILE| wc -l)

# For every cert in the PEM file, extract it and import into the JKS keystore
# awk command: step 1, if line is in the desired cert, print the line
#              step 2, increment counter when last line of cert is found
for N in $(seq 0 $(($CERTS - 1))); do
  ALIAS="${PEM_FILE%.*}-$N"
  cat $PEM_FILE |
    awk "n==$N { print }; /END CERTIFICATE/ { n++ }" |
    $HOME/.java-buildpack/open_jdk_jre/bin/keytool -noprompt -import -trustcacerts \
            -alias $ALIAS -keystore $KEYSTORE -storepass $PASSWORD
done

sed -i 's|</pool>|</pool>\n\t<pdx>\n\t\t<pdx-serializer>\n\t\t\t<class-name>org.apache.geode.pdx.ReflectionBasedAutoSerializer</class-name>\n\t\t\t<parameter name="classes"><string>io.pivotal.bookshop.domain.*</string></parameter>\n\t\t</pdx-serializer>\n\t</pdx>|g' $HOME/.java-buildpack/tomcat/conf/cache-client.xml
sed -i "s\enableLocalCache='true'\enableLocalCache='false'\g" $HOME/.java-buildpack/tomcat/conf/context.xml


cat $HOME/.java-buildpack/tomcat/conf/cache-client.xml