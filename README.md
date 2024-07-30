## Library Application ##
This application will provide functionality to loan books from a library (up to 5 per member). 
For more information have a look at the REQUIREMENTS.md.

## Running the application
You can start the application with `docker-compose up` through the docker-compose.yml file. 
After startup, you can access the swagger-ui through http://localhost:8080/swagger-ui/index.html 
and the open api 3.0 specification through http://localhost:8080/v3/api-docs

Please make sure you have docker compose 2 installed: https://docs.docker.com/compose/install/
Older versions (pre v2) will not be able to start the containers. For more information visit https://docs.docker.com/compose/migrate/

## Assumptions
For the data model there was only one clear unique value which is the username of a member. 
Loan, book and Author don't really have a unique identifier therefore I chose to implement them with an id as primary index.
This also has the advantage that this solution is quite easy to extend for future purposes (e.g. having more than one of each book).


