# advantageDatabaseServerConnectionError

Sample project to document the Advantage Database Server Error in a small and simple project to reproduce the Bug in adsjdbc11.10.jar and adsjdbc12.jar.

To reproduce the error the Advantage Database Server langage has configured to "GERMAN"

![language encoding settings](/resources/images/language.png)

>com.extendedsystems.jdbc.advantage.ADSException: [iAnywhere Solutions][Advantage JDBC]windows-850
>        at rentasad.library.db.AdsConnectionTest.encodingCheckQuarterAddress(AdsConnectionTest.java:69)


## ADS Server Preparation
- use a Windows Ads Server
- create a path c:\ads
- share this folder with full access to everyone
- copy the test dbf file from /testdata in c:\ads

## Setup

- clone project in a path of your choice

### Add adsjdbc[version].jars to your local maven repository

- go to resources\adsJdbc

run the following mvn commands to add Jars to your local mvn rep:
```
mvn install:install-file -Dfile=adsjdbc11.0.jar -DgroupId=com.extendedsystems.jdbc.advantage -DartifactId=adsjdbc -Dversion=11.0 -Dpackaging=jar
mvn install:install-file -Dfile=adsjdbc11.10.jar -DgroupId=com.extendedsystems.jdbc.advantage -DartifactId=adsjdbc -Dversion=11.10 -Dpackaging=jar
mvn install:install-file -Dfile=adsjdbc12.0.jar -DgroupId=com.extendedsystems.jdbc.advantage -DartifactId=adsjdbc -Dversion=12.0 -Dpackaging=jar
```

### setup your config for a successfully connect to ADS

edit the file 
> src\test\java\rentasad\library\db\AdsConnectionTest.java

and setup the initial String parameter variables:
```
String host = "//192.168.111.30";
String socket = "6262";
String databaseDictionaty = "/vs4/TMPF01/";
String lockType = "proprietary";
String charType = "OEM";
String tableType = "cdx";
 ```
 
### Add sample table to ads
run sql on SQL Server to produce the following table:

 ```
CREATE TABLE adsexample(
  lastname char(20)
  );

INSERT INTO adsexample (lastname) VALUES ('Müller');
 ```
 
## Run Tests

in the pom.xml you can change the Version from theJDBC Driver:
 ```
       <dependency>
            <groupId>com.extendedsystems.jdbc.advantage</groupId>
            <artifactId>adsjdbc</artifactId>
            <version>11.0</version>
        </dependency>
 ```
 **Version 11:** Works
 
 **Version 11.10:** Failed
 
 **Version 12:** Failed
 
 
 with 
 > mvn test

you should start the test to reproduce the error
 
