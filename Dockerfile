FROM tomcat:jdk21-openjdk-slim
RUN mkdir /usr/local/tomcat/foto
COPY ./web/target/ROOT.war /usr/local/tomcat/webapps/