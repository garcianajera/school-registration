# School Registration System

School registration service is a group of APIS to create remove and update students and course, it also allows to register students to courses with these rules:
 * A student can register to multiple courses but cannot register multiple time to same course
 * A student can register to 5 course maximum
 * We can add more students but we can not add same user multiple times user is duplicate if name and email is the same
 * A course has 50 students maximum  
 * A course can register to multiple students but cannot register multiple time to same student
 * We can add more courses but we cannot add same course multiple times


This application was built on Springboot using:
 * restcontroller
 * JPA
 * swagger
 * JUnit 
 * Mockito
 * Docker compose
 * Mysql 8

It uses Optimistic lock and register operation is transactionals to ensure we do not exceed max number of students in one course   

## How to set up project
Application can be deployed using docker-compose (it was tested with docker-compose version 1.17.1)
* build jar, go to project directory and run : gradlew build
* Docker: I'm using image openjdk:11 and Mysql 8, app is running on 8080 and mapped to port 80
* got to root folder and run: docker-compose up 
* open a browser and go to http://localhost/swagger-ui.html
* OpenApi definition: http://localhost/v3/api-docs


## Endpoints and payloads
##### Students CRUD endpoints

* GET /api​/students​/{id}
* PUT /api​/students​/{id}
* DELETE /api​/students​/{id}
* GET /api​/students
* POST ​/api​/students

Request body example value

	{
	  "id": 0,
	  "name": "string",
	  "email": "string"
	}

##### Course CRUD endpoints
* GET /api​/courses​/{id}
* PUT /api​/courses​/{id}
* DELETE /api​/courses​/{id}
* GET ​/api​/courses
* POST /api​/courses

Request body example value

	{
	  "id": 0,
	  "name": "string"
	}

##### Registration endpoint
* POST /api​/registration

Request body example value

	{
      "studentId": 0,
      "courseId": 0
	}

##### Registration endpoins
* GET /api​/reports​/students​/{id}​/courses
* GET ​/api​/reports​/students​/courses​/empty
* GET ​/api​/reports​/courses​/{id}​/students
* GET ​/api​/reports​/courses​/students​/empty

### Additional Links
Application is running on:

* [School](https://school.testmycloud.net/swagger-ui.html)

