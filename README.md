# LTSE API Starter

This project can be used to build LTSE API services in [Java Play framework](https://www.playframework.com/documentation/2.6.x/Home).

It exposes two APIs:
```
GET /persons
    Returns 200, with a list of all users in the database
```

```
POST /person 
    Request body: {"name": "sample user"}
    Returns 200
```
  
## Requirements

- [Install Java 8](https://www.playframework.com/documentation/2.6.x/Installing#Prerequisites).
- [Install sbt](https://www.scala-sbt.org/download.html)
- [Install Postgres](https://www.postgresql.org/download/) and create a new database. You will need to set the credentials to the database during Configuration.
- Download the repo locally - `git clone https://github.com/captable/api-starter` 

## Configuration

Set your local DB URL and auth credentials in conf/application.conf:
```
  default.url = "jdbc:postgresql://<host>:<port>/<database>"
  default.username = <user>
  default.password = "<password>"
```

## Running the server

In the root of the repo - `sbt run`


## Import in IntelliJ using sbt

https://www.jetbrains.com/help/idea/sbt-support.html#import_sbt


## Testing

sbt test
