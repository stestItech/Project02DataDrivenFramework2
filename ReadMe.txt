To Run the project

1. Run command line from the folder where docker-compose file is located
2. Run one of these commands to start the container:
docker-compose -f docker-compose-v3.yml up
docker-compose -f docker-compose-v3.yml up --scale chrome=5 --scale firefox=5 --scale edge=5 -d
3. Open http://192.168.0.75:4444/ui# (localhost) to track selenium grid 4 statuses
4. Run the command below to start jenkins from the folder where jenkins jar file is located:
java -Dhudson.model.DirectoryBrowserSupport.CSP="" -jar jenkins.war
5. Open http://192.168.0.75:8080/ (localhost) to get Jenkins web application
5. Log in as admin/default password from .jenkins folder or create a new one

Note 1: In TestBase.class (openBrowser() method) if needed change grid hub address
 (probably just use localhost)
Note 2: Run the command below to shut down the containers:
docker-compose -f docker-compose-v3.yml down