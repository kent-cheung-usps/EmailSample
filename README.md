# EmailSample
POP3 and IMAP sample. Read the latest email in the inbox.
## Requirement
- Open JDK version 17
- Maven
- External email account that provide POP3 or IMAP access
## Steps
1. Clone the project
2. mvn clean install
3. java -jar target/emailsample-1.0-SNAPSHOT-jar-with-depedencies.jar pop
---
## Github Action
```
mvn -B package --file pom.xml
```
