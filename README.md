# Springit
Simple Go application that take connects to a Postgres database and generates a multi-maven Spring Boot REST API based on the tables.

The resulting API has: 
* CRUD functionality 
* HATEOS/HAL support for hypermedia controls as described in the Richardson REST maturity model: https://martinfowler.com/articles/richardsonMaturityModel.html
* Connection configuration to the Postgres database. 
