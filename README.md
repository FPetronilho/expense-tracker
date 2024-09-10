# Functional and non functional requirements 
This project consists on an application that **manages** expenses:
    • Insert; 
    • Consult;
    • Update;
    • Delete.
    
The user may consult the expenses and **order them by 2 criteria**: 
    • Date;
    • Amount.
    
The user can as well **indicate the direction of the ordering** for each criteria:
    • Ascending;
    • Descecnding.
    
The user **must categorize** their expenses.
The application suggets a list of pre-defined categories.
The user can, however, **insert and delete personalized categories**.

The user **may share** their expenses with another user. 
    • Sharing method is through email.
    
The user **must create an account and login** in order to use the application.
    • Login is made via SSO – Single Sign-On;
    • IdP is Microsoft.


# Requirements by components
## 1. Expense Tracker
Manages expenses:
    • Insert - **REQ-F-001**; (done)
    • Consult by ID - **REQ-F-002**; (done)
    • List by criteira - **REQ-F-003**;
    • Update - **REQ-F-004**; (done)
    • Delete - **REQ-F-005**. (done)

Order expenses:
    • By date - **REQ-F-006**;
    • By amount - **REQ-F-007**; (done)

Indicate the direction of the ordering for each criteria:
    • Ascending - **REQ-F-008**; (done)
    • Descending - **REQ-F-009**; (done)

The application suggests a list of pre-defined categories - **REQ-F-010**.

The user may manage personalized categories:
    • Create - **REQ-F-011**; (done)
    • Delete - **REQ-F-012**; (done)


## 2. Auth8 - Auth Infinity (Authorization Server)
Authorization server is the service that will **emit the security token**. 
In this project the token will be JWT.

The user must:
    • Create an account - **REQ-F-013**;
    • Login - **REQ-F-014**;
    • Login is done by SSO - **REQ-F-013, REQ-F-014**;
    • IdP is Microsoft - **REQ-F-013, REQ-F-014**;
    • JWT has an expiracy date if 12h.


## 3. Portfolio Management
A portfolio contains a list of assets of the user.
An asset is an entity that belongs to the user.
In this domain, an expense is an asset of the user, as well as a category.

This service manages assets:
    • Inserts - **REQ-F-015**;
    • Consults by ID - **REQ-F-016**;
    • Lists by multiple criteria - **REQ-F-017**;
    • Updates - **REQ-F-018**;
    • Deletes - **REQ-F-019**.

User:
    • ID (internal, e.g., UUID);
    • IdP ID (in JWT, this claim is called sub) mandatory;
      REQ-F-013, REQ-F-014
    • Email (contact, mandatory);
    • Phone number (contacto, optional);
    • Name (nickname (mandatory), first and last name are optional);
    • Date of birth (optional).
    
Asset:
    • ID (internal, e.g., UUID);
    • Creation date;
    • Update date.
