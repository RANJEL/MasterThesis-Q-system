# About
This repository contains the implementation and supporting materials for my **Master’s Thesis**.  
The work was carried out in cooperation with my employer at the time.  

To comply with academic and professional requirements, both the **thesis text** and the **source code** have been **anonymized**. Any references to company-specific details, internal systems, or sensitive information have been removed or replaced with neutral placeholders.  

The repository serves as a record of the research and development completed during the thesis project and is shared here for academic and personal reference.

## Thesis text
Full thesis is here: thesis/text/DP_Lejnar_Jan_2020.pdf

## Abstract
This thesis targets designing a model query system, which will provide to endusers
a web interface to query an existing information system, which otherwise
provides only a complex API for querying. The resulting product is intended
mainly for smaller organizations that do not have the necessary resources
to build their own solution, or as a model solution for larger organizations,
which will be able to create their own query system based on this model. The
application was successfully created according to the standard software process,
from requirement gathering, through analysis, design, implementation,
to testing, and documentation. All system requirements have been met. The
application enables its users to build various types of queries, send them to
the API and represent obtained results via a web interface.

### Keywords:
query system, user interface, queries building, query transformation, presentation of results

# Launching application

### Prerequisites

* OpenJDK - Java 11 (e.g. 11.0.2) : https://jdk.java.net/archive/
* Maven
* There are correctly set paths to qsystem-config files in application.properties 

### Windows

```
# build
cmd> mvn clean install

# start application
cmd> java -jar -Dserver.port=8180 ^
   -Djavax.net.ssl.trustStore=./qsystem-security/isaaa-server-certificate.p12 ^
   -Djavax.net.ssl.trustStorePassword=kokosak ^
   -Djavax.net.ssl.keyStore=./qsystem-security/qsystem-certificate.p12 ^
   -Djavax.net.ssl.keyStorePassword=kokosak ^
  --add-opens java.base/java.lang=ALL-UNNAMED qsystem-1.0.0.jar

# start browser
cmd> chrome.exe http://localhost:8080
```

### Authentication
Q-system should authenticate against Active Directory.
For testing purposes I downloaded and configured local ApacheDS server.
Current application.properties #LDAP configuration is mapped to this testing ApacheDS server.

Download server: https://directory.apache.org/apacheds/downloads.html 

Configure through Apache Directory Studio: https://directory.apache.org/studio/users-guide/2.0.0.v20180908-M14/ldap_browser/gettingstarted_create_connection.html

Finally with Apache Directory Studio import my testing ldif file: src\test\java\cz\komix\qsystem\backend\persistence\config\qsystem-test-ldif.ldif

=> login: Lejnar, password: admin (ROLE_ADMIN, ROLE_DOTAZOVATEL)

=> login: ben, password: benspassword (ROLE_DOTAZOVATEL)

# Proof of Concept [qsystem]
Combination of Spring Boot && JSF (Primefaces)

### JSF and bean management
JSF are created based on resources/META-INF/resources/*.xhtml
Expression Language uses CDI, to provide beans inside JSF. In order to do that, beans have to have @Named annotation.

### Rewrite
Rewrite is used in this project, so users don't have to write URL /helloworld.xhtml but use REST like URL e.g. /helloworld (annotation @Join)

### Lombok
Lombok project is used to generate toString, equals, hashCode, getters on all fields, and setters on all non-final fields, and constructor using @Data annotation

### Java 11
Project is buildable using Java 11. There is a known Springframework causing this warning on startup:Project is buildable using Java 11. There is a known Springframework causing this warning on startup:
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils (file:~/.m2/repository/org/springframework/spring-core/5.1.6.RELEASE/spring-core-5.1.6.RELEASE.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
https://stackoverflow.com/questions/46671472/illegal-reflective-access-by-org-springframework-cglib-core-reflectutils1

I suppose it will be fixed soon. For now just add this parameters when running java -jar or in IDEA (VM options):
``
--add-opens java.base/java.lang=ALL-UNNAMED 
``

### spring-boot-devtools
Project also use spring-boot-devtools that provide Automatic restart, when classpath change (make sure your IDEA performs automatic build if you change your sources) and also provide Live reload that force browser to be up-to-date.

https://www.baeldung.com/spring-boot-devtools
