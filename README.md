TEnmo Money Transaction Application
Welcome to the TEnmo Money Transaction Application! This project was developed as part of Merit America Capstone #2, where I had the privilege of leading a squad to bring this application to life. We learned a lot from each other throughout the development process and are excited to share our work with you. Feel free to explore the application and contribute, especially by helping us finish writing functional unit tests.

Features
User Registration and Login
User Registration: New users can register with a username and password. Each new user starts with an initial balance of 1,000 TE Bucks.
User Login: Registered users can log in using their username and password, receiving an Authentication Token to be included with subsequent system interactions.
Account Management
View Account Balance: Authenticated users can view their current account balance.
Transfers
Send TE Bucks:

Authenticated users can send TE Bucks to other registered users.
Users can choose from a list of users to send money to.
Users cannot send money to themselves.
The amount sent cannot exceed the sender’s account balance and must be positive.
Sending a transfer decreases the sender's balance and increases the receiver's balance.
The transfer status is initially set to Approved.
View Transfers: Authenticated users can view transfers they have sent or received.

Transfer Details: Authenticated users can view details of any transfer using the transfer ID.

Request TE Bucks:

Authenticated users can request TE Bucks from other registered users.
Users can choose from a list of users to request money from.
Users cannot request money from themselves.
The amount requested must be positive.
Request transfers have an initial status of Pending.
No account balance changes until the request is approved.
View Pending Requests: Authenticated users can view their pending transfer requests.

Approve or Reject Requests:

Authenticated users can approve or reject pending transfer requests.
The request can only be approved if the user has enough TE Bucks.
Approved requests result in a balance increase for the requester and a balance decrease for the approver.
Rejected requests result in no account balance changes.
Sample Screens
Current Balance
swift
Copy code
Your current account balance is: $9999.99
Send TE Bucks
markdown
Copy code
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are sending to (0 to cancel):
Enter amount:
View Transfers
markdown
Copy code
-------------------------------------------
Transfers
ID          From/To                 Amount
-------------------------------------------
23          From: Bernice          $ 903.14
79          To:    Larry           $  12.55
---------
Please enter transfer ID to view details (0 to cancel):
Transfer Details
yaml
Copy code
--------------------------------------------
Transfer Details
--------------------------------------------
 Id: 23
 From: Bernice
 To: Me Myselfandi
 Type: Send
 Status: Approved
 Amount: $903.14
Requesting TE Bucks
markdown
Copy code
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are requesting from (0 to cancel):
Enter amount:
Pending Requests
markdown
Copy code
-------------------------------------------
Pending Transfers
ID          To                     Amount
-------------------------------------------
88          Bernice                $ 142.56
147         Larry                  $  10.17
---------
Please enter transfer ID to approve/reject (0 to cancel):
Approve or Reject Pending Transfer
yaml
Copy code
1: Approve
2: Reject
0: Don't approve or reject
---------
Please choose an option:
Database Schema
The database schema includes the following tables:

tenmo_user Table
Stores login information for users.

Field	Description
user_id	Unique identifier of the user
username	String that identifies the name of the user; used as part of the login process
password_hash	Hashed version of the user's password
role	Name of the user's role
account Table
Stores user accounts.

Field	Description
account_id	Unique identifier of the account
user_id	Foreign key to the tenmo_user table; identifies the account owner
balance	Amount of TE bucks currently in the account
transfer_type Table
Stores types of transfers.

Field	Description
transfer_type_id	Unique identifier of the transfer type
transfer_type_desc	Description of the transfer type
transfer_status Table
Stores statuses of transfers.

Field	Description
transfer_status_id	Unique identifier of the transfer status
transfer_status_desc	Description of the transfer status
transfer Table
Stores details of transfers.

Field	Description
transfer_id	Unique identifier of the transfer
transfer_type_id	Foreign key to the transfer_type table; identifies type of transfer
transfer_status_id	Foreign key to the transfer_status table; identifies status of transfer
account_from	Foreign key to the account table; identifies the account from which funds are being transferred
account_to	Foreign key to the account table; identifies the account to which funds are being transferred
amount	Amount of TE bucks being transferred
How to Set Up the Database
Create a new Postgres database called tenmo.
Run the database/tenmo.sql script in pgAdmin to set up the database.
Datasource Configuration
Datasource settings are configured in /src/resources/application.properties.

bash
Copy code
# datasource connection properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tenmo
spring.datasource.name=tenmo
spring.datasource.username=postgres
spring.datasource.password=postgres1
Using JdbcTemplate
In /src/main/java/com/techelevator/dao, JdbcUserDao demonstrates how to get an instance of JdbcTemplate in your DAOs. Spring automatically injects an instance if you declare a field of type JdbcTemplate and add it as an argument to the constructor.

java
Copy code
@Service
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
Testing
DAO Integration Tests
com.techelevator.dao.BaseDaoTests is provided as a base class for DAO integration tests, initializing a Datasource for testing and managing database changes rollback between tests.
com.techelevator.dao.JdbcUserDaoTests is an example of how to write DAO integration tests.
Testing uses a copy of the real database, with schema and data defined in /src/test/resources/test-data.sql.

Authentication
User registration and authentication are implemented. After successful authentication, an instance of AuthenticatedUser is stored in the currentUser member variable of App. The JWT can be accessed via currentUser.getToken(). When use cases refer to an "authenticated user," it means including the token as a header in requests.

Enjoy using TEnmo and feel free to contribute!
