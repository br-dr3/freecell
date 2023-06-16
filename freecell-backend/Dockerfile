FROM amazoncorretto:17
WORKDIR /usr/app/
COPY freecell-backend/target/freecell-backend.jar /usr/app/
ENTRYPOINT [ "/usr/bin/java", "-jar", "-Dspring.profiles.active=docker", "freecell-backend.jar" ]