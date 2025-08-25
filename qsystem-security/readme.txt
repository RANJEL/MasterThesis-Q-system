keystore:
qsystem-certificate.p12
password: pomeranc
+ isaaa_development_CA.cer add to trustStore

=> (in IDEA: run configuration -> add to VM options)
=> or add command line args:

java -jar <jar file name>
    --add-opens java.base/java.lang=ALL-UNNAMED
    -Djavax.net.ssl.trustStore=./qsystem-security/isaaa-server-certificate.p12
    -Djavax.net.ssl.trustStorePassword=pomeranc
    -Djavax.net.ssl.keyStore=./qsystem-security/qsystem-certificate.p12
    -Djavax.net.ssl.keyStorePassword=pomeranc
