



#  Wod Core Gym

## Wod Core Gym είναι ένα demo Gym Management App χτισμένο με Java Spring Boot + Gradle.

##   Τεχνολογίες

#### - Backend: Java 17, Spring Boot 3.x

#### - Database: MySQL

#### - Authentication: JWT

####  -API Docs: Swagger (Springdoc OpenAPI)
 

## Features

#### - Authentication / Authorization

#### - Login / Register


#### - JWT-based security

#### - Default role για νέο χρήστη: CUSTOMER

#### - Roles

#### - CUSTOMER: μπορεί να κάνει bookings

#### - TRAINER: μπορεί να φτιάξει Workout Sessions, να κάνει bookings, να διαγράψει χρήστες

#### - ADMIN: πλήρη δικαιώματα

#### - Workout Sessions

#### - Δημιουργία, λίστα, booking

#### - Αυτόματο demo dataset (1 admin, 1 trainer, demo sessions 7 ημερών)

#### - User Management

#### - Διαγραφή χρήστη (εκτός Admin)

#### - Promote από CUSTOMER ➝ TRAINER (μόνο από Admin ή Trainer)



## Run Locally

 - ` git clone git@github.com:bafatosuko/gym_app_backend.git`
 - Φτιάξε μια βάση SQL με DEFAULT VALUES:
 - MYSQL_HOST=localhost
 - MYSQL_PORT=3306
 - MYSQL_DB=wodcoregym
 - MYSQL_USER=springuser
 - MYSQL_PASSWORD=12345
 - RUN SERVER `./gradlew bootRun`
 - SWAGGER http://localhost:8080/swagger-ui.html
