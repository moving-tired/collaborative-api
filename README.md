# Collaborative Helper API

## Key Features

* 

## Prerequisites

- Java 1.11+ (use OpenJDK)
  * Running on linux you may encounter ssl cert problems, consider doing something like [this](https://github.com/travis-ci/travis-ci/issues/9368#issuecomment-395354755)
  * For Mac users use:
    - `brew tap AdoptOpenJDK/openjdk`
    - `brew cask install adoptopenjdk`
- Gradle 5.+
- Docker (only to run integration tests)

## Quick Start

First you need to start your local dev environment, the easieast way to do that you should run the next command:

```
.make start-devenv
```

And then start the application with:
```
make run-app
```

If you want to run the application using your preferred IDE, just
right-click in App.kt and run the main method
that you can find there.

In case that you want to remove your local dev environment (the images started by docker compose) you can just run:

```
make clear-devenv
```

### Running the project

In order to run the project you need to execute the following command:

```
./gradlew bootRun
```

- Also you can run a small local environment using docker compose:
`sudo docker-compose -f ./docker-compose-local.yml up`

- Also aws clients needs the credentials, you can add a file into `~/.aws/credentials`:
```
[default]
aws_access_key_id=test
aws_secret_access_key=test
```

### Running the tests

In order to run the project tests you need to execute the following command:

```
./gradlew test
```

If you want to see the code coverage reports generated by jacoco you can:
```
./gradlew jacocoTestReport
and check folder ./build/jacocoHtml to see the html report
```
Also building the project will generate a .exec file with the code coverage to be used by tools like sonar.

### Build Project

```
./gradlew clean build
./docker build .
```

This will generate the artifact and create a docker for you to deploy your application

#### Show Code Coverage

To help developers improve code coverage we've added a new task:
- ```./gradlew jacocoRootReport```
- This will generate an executable into `build/jacoco` that you can use to see the code coverage inside your repository

### Development

We recommend to use intelliJ and configure it to use Java 11 compiling the code to Java 8.

For tests:
- Change IDEA configuration to use IDEA test runner. This will fix display names for junit5, which will improve a lot the readability of your reports.

#### Security Issues

If you run `./gradlew dependencyCheckAnalyze` you can see if you have any issues in your dependencies. We advice to run it at least once per week