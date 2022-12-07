FROM tomcat:9.0-jdk11-openjdk-buster
COPY ./backend/target/main-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war