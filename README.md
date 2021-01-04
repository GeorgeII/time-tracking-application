## The application is written in Scala (and some JS) via Play Framework.

<img src="https://raw.githubusercontent.com/GeorgeII/time-tracking-application/master/readme-pictures/1.png?raw=true" />
<img src="https://raw.githubusercontent.com/GeorgeII/time-tracking-application/master/readme-pictures/2.png?raw=true" />

## Prerequisites

You need PostgreSQL installed on you machine. You have to choose only one option of the following two: 

1) Create two databases called 'time-tracking-system' and 'time-tracking-system-test'. They both should have 'public' scheme. Create user 'postgres' with 'postgres' password and give this user privileges it requires.

2) Create whatever databases and users you like but you will have to set it in conf/application.conf file (2 blocks at the very bottom of the file).

Note that the tables will be created automatically during the server start-up stage.

## Running

Run this using [sbt](http://www.scala-sbt.org/).

```bash
sbt run
```

And then go to <http://localhost:9000> to see the running web application.

## Technologies, techniques, features

1. Play Framework. It maps GET and POST requests. Also, input forms are written in Play.

2. Slick - a database query and access library for Scala - as a DAO layer. I used it to store users, their passwords, activities, session tokens, etc., and map Scala classes to database tables.

3. Passwords are stored in a database as salted hashes (PBKDF2 with Hmac-SHA256 algorithm).

4. Every time a user logs in a new session token is generated and gets saved into cookies for him. This token is passed as a parameter in some GET and POST requests in order to make them RESTful.

5. Play has another token to prevent CSRF attacks.

6. Plain JavaScript used on the client-side for timer, some requests, and animation. Chart.js allowed to visualize statistics as a pie-chart.
