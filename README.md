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
Please note that the swagger-ui suggests you to use id through POST, PUT and PATCH requests but giving the id in the request body will not hava any meaning as the id is already defined by the path variable (route).
To avoid confusion I implemented a UniqueIdentifierModificationException that will throw an error on PUT and PATCH requests when the id in the path variable and request body don't match up.


