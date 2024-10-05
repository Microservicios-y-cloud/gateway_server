# Imagen para java, el proyecto tiene la version 17
FROM openjdk:17-jdk-alpine

#Localizacion del .jar que genera el proyecto
ARG JAR_FILE=target/*.jar

# Copia el jar al principio del proyecto y lo llama app.jar
#El nombre del .jar se genera de acuerdo a las etiquetas <artifactId> y <version> del POM
COPY ./target/gateway_server-0.0.1-SNAPSHOT.jar app.jar

# Comando de ejecucion
ENTRYPOINT ["java", "-jar", "app.jar"]

#Intrucciones para correr docker    :
    #Limpiar /target                :   mvn clean
    #Crear /target                  :   mvn install
    #Construccion del contenedor    :   docker build -t gateway-server .
    #Correr un contenedor           :   docker run -p 8080:8080 gateway-server

