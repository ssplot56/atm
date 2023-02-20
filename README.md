# 🏧 ATM service 🏧 

### Project description 💵:
This project is an implementation of an ATM (Automated Teller Machine). 
The backend provides an API that enables a user to perform various 
operations such as depositing money to their account, withdrawing money from their account, 
and transferring money to another user's account. The app is implemented using n-tier architecture, built
on Spring Boot using Spring Data, Spring Web,Spring Security and JPA/Hibernate. The application
is connected to the H2 database.

### Features:
Endpoints available for __everyone__:
* POST `/register` - available for everyone;

Endpoints available for __User/Admin__:
* GET `/accounts` - get a list of user accounts;
* POST `/accounts/new-account` - create new user account;
* POST `/accounts/transfer` - transfer money from one account to another;
* GET `/atms` - get a list of available atms;
* POST `/atms/{atmId}/deposit` - put money on your account;
* POST `/atms/{atmId}/withdraw` - withdraw money from your account;

Endpoints available only for __Admin__:
* POST `/atms/new-atm` - create new atm;
* POST `/atms"/{atmId}/denomination` - add denominations to atm;
* PUT `/atms/{atmId}/denomination/{denominationId}` - update denomination;

The application logic will not allow you to withdraw funds if there are insufficient 
funds on your account or at an ATM. You will also not be able to transfer funds 
if the specified amount is not available on your account.

### Structure:
> The project consists of three layers:
* __Controller__ - provides for the processing of user requests
* __Service__ - provides all the business-logic;
* __Repository__ - provides all database operations;

### Technology Stack:
* Java 11
* Apache Maven;
* Spring Boot
* Spring JPA
* Spring Security
* Hibernate, Jpa;
* H2 database

### How to launch:
1. Clone the repository to your local machine.
2. Run the application by executing the main method.
3. The application will be running on http://localhost:8080.

