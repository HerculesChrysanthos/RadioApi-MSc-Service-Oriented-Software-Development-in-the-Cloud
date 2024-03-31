#Create image
cd ..
cd content-service

mvn clean package -Dquarkus.container-image.build=true

## if you want to create locally container
#docker run -i  -p 8080:8080 hercules/user:1.0