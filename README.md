## About

This was a code challenge I implemented months ago with a Spring Boot service that authenticates and creates users that have bank accounts and can transfer money to other bank accounts.
<br>
<br>
I didn't have much time to do this and never got any feedback from the people who asked for it so, it may have bugs that weren't covered by the tests ;)

## Basic Requirements 

- Users are allowed a maximum of 3 transactions per day;
- Account needs to have the funds for the transactions;
- Each transaction has a fee of 2% when value <= 100 and 5% when value > 100;
- Return the amount transferred in the CAD currency requesting to fixer.io client;
- Use Spring Data and Security.

### Rest api

These are the following endpoints:
- /api/login
- /api/user/save
- /api/role/save
- /api/role/add/user
- /api/token/refresh
- /api/transfer

### Implementation details

The chosen technologies for the challenge apart from the Spring Framework (Boot, Web, Data, Security, Webflux) were Postgres for the database and auth0 for the JWT authentication.
<br>
The authentication was implemented using JWT. Services were created for the management of users and roles. The application provides two preloaded users and two roles (admin and user roles). The admin users can insert roles and new users while the users with the user role can use the transfer methods.
<br>
The currency was mapped in the database and is validated in the requests. The tax was mapped into a database table since it is something likely to change in the future and prone to new ranges of taxes. This could be improved in the future with initial and end date for the tax so it could be linked to the transfers, but for this challenge the tax table was opted to display this possibility.
<br>
The account record is locked during the transfer to avoid concurrent modification.
<br>
The basic attributes' validation was implemented using the basic validation annotations from the Data framework. There was also a custom annotation for validation of the foreign key attributes sent in requests (like account id for instance).
<br>
All the requests were mapped into objects (as so the response objects), that are parsed into the entity objects of the application.
<br>
Custom business validations were implemented with the Spring exception handling mechanism. A base class named BusinessException was created for the exceptions InsufficientFundsException and MaxTransactionsExceededException. These exceptions are handled in the class ApiExceptionHandler making exception handling centralized.
<br>
The fixer client was implemented with the WebClient provided by the Spring Webflux framework.
<br>
Tests were implemented for the repositories, services and controller. Mockito was used along with usual JUnit methods to test these layers.
<br>
Docker was used to load a postgres image and postgres client and avoid the need to install the database.
<br>
The environment variables responsible for the jwt secret and the fixer api was implemented so the operational system can define them but there is also the possibility of setting them on the application.yml file.
<br>
After the login, the user sends the token bearer to all the requests. If the token expires, the user can refresh the token with the refresh url and the refresh token received in the login method.

### Sample requests

The file BANK.postman_collection contains sample requests for the API.

## Install

Create a fixer account https://fixer.io/ account and update the application.ym with the token (access_key).

The 
Run command to install database:

`docker-compose up`

Run command to clear volumes from docker:
`docker-compose down --volumes` or
`docker-compose down --rmi all --volumes`
If needed, there is a postgres client available at 

http://localhost:5050/

If you need to access the database with a client go do 'Add Server' at the home page and use the following settings
```
host: 172.17.0.1
user: bank
pass: bankpass
```

The environment variables can be created in the operational system:
```
fixer.access_key
fixer.url
jwt.secret
```
Or they can be updated with in the application.yml with the names:
```
fixer.dev.access_key
fixer.dev.url 
jwt.dev.secret
```
They correspond to the fixer client and the jwt secret is any string to generate the 
jwt encryption.

Finally run BankApplication.java

## Suggested Improvements

- Use flyway or liquibase to build the initial database and records (like tax/roles/admin user);
- Run the entire project with docker and remove the environment secrets from the application.yml;
- Separate the fixer client in a project because it tends to be used in other projects (even though it consists of a single class);
- Separate database for tests. Right now hibernate is creating the database on startup;
- Implement more tests. Very low code coverage right now;
- Implement more custom validation methods for the requests.
- Document with Swagger.