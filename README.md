<p align="center">
  <img src="https://user-images.githubusercontent.com/19668606/126791551-18894842-2c19-4ffb-986b-6a7495529adb.png" alt="logo" />
</p>

# Let'sGG Platform API ![Build and Test Pipeline](https://github.com/letsgg-platform/letsgg-platform-api/workflows/Build%20and%20Test/badge.svg?branch=main)

## Prerequisites

- [JDK](https://www.oracle.com/java/technologies/javase-downloads.html) **>= 1.8**
- [Maven](https://maven.apache.org/download.cgi) **>= 3.6.3**
- [PostgreSQL](https://www.postgresql.org/download/) **>= 13**

---

## Quick Local Startup

### Running DB Migration Scripts

You may want to run DB migration scripts to define/create DB schema of yours (_required only once_). <br/>

```bash
mvn liquibase:update -Ddatasource-jdbc-url=<env> -Ddatasource-username=<env> -Ddatasource-password=<env> -Dliquibase-context=<env>
```

After you've finished running DB migration scripts, you can proceed with starting the application.

To bootstrap with the default profile/env variables:

```bash
mvn spring-boot:run
```

Alternatively, you may want to bootstrap with specific profile:

- **dev** (default)
- **production**
- **test**
  <br/>

To run app w/ any profile besides default one, just add ```-P<profile>``` flag; e.g.:

```bash
mvn spring-boot:run -Pproduction
```

---

### Used Environment Variables

- datasource-jdbc-url (_defaults to_ `jdbc:postgresql://localhost:5433/letsgg`)
- datasource-host (_defaults to_ `localhost`)
- datasource-username (_defaults to_ `postgres`)
- datasource-password
- liquibase-context (_defaults to_ `dev`)
- oauth-github-client-id
- oauth-github-client-secret
- oauth-google-client-id
- oauth-google-client-secret
- jwt-secret
- cors-allowed-origins
- test_email
- test_email_pwd

## Running Tests

```bash
mvn clean verify
```

_Notes: Runs tests w/ **test** profile_

---

## Contacts

- email: romantupis _at_ gmail _dot_ com
- telegram: [@romm1](https://t.me/romm1)