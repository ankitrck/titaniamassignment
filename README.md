# titaniamassignment

Titanium Assignment
•	RESTFUL API endpoints to connect to an application layer
•	Interact with DB layer for GET and POST data from and to data layer
•	Simulate multizone DB layer redundancy.

Architecture

<img width="452" alt="image" src="https://user-images.githubusercontent.com/12032640/194120576-c8666c6d-4796-4cf2-8e39-cd0e6df24452.png">
 

The above diagram shows the current implementation of system and its components

API layer
•	Controller with GET and POST api endpoints to connect to server and application
Application layer
•	Services and backend structure to define endpoint interfaces.
Streaming service
•	To communication asynchronously and handle large requests between microservices
DB layer
•	A infra consisting of Repositories and database configurations to control and commit between multiple data sources, in this case – 2 sqlite databases.
