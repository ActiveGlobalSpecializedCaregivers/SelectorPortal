# To install new certificates (assuming the same private key already installed on the server is used)
```shell
# switch to the root user
sudo -s 

# go to current tomcat installation config directory
cd cd /usr/share/tomcat-9.0.64/conf/

# make directory for manipulations
mkdir ssh-<date>
cd ssh-<date>

# copy archive with certificates
cp ~/selector-2.zip .
unzip selector-2.zip

# combine pem certificates in one file
cat 347cc8ee5da92b96.pem  gdig2.crt.pem > import.pem

# copy private key to this directory
cp ../activeglobalcaregiver.key .

# create pkcs12 keystore 
openssl pkcs12 -export -in import.pem -inkey activeglobalcaregiver.key -name tomcat > server.p12

# import this keystore into java keystore. Use password from conf/server.xml as destination keystore password to match current one
keytool -importkeystore -srckeystore server.p12 -destkeystore keystore.tomcat.p12 -srcstoretype pkcs12 -alias tomcat

# go back to config directory, backup current file and copy new keystore
cd ..
mv keystore.tomcat.p12 keystore.tomcat.p12.backup
cp ssh-date/keystore.tomcat.p12 .

# restart tomcat

``` 