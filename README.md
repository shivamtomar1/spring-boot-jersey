# NAV Application

Nav Application is a self contained deployable REST microservice which will return Net Asset Value(NAV) for all holdings and all holdings data for a particular date

## Introduction

I have used spring-boot with jersey framework for this project.

I used spring-boot to have an embedded server. Only requirement to run this is you have Java 1.8 and maven installed on your machine.

Jersey framework is used as a robust JAX-RS framework with easy management of REST Api Paths.

GSON is used along with DAOs with backend API respose schema and response to frontend schema defined in %APP_ROOT%/src/main/java/com/microservice/nav/dao.

okhttp client used to make backend http calls, seperate utility class written in %APP_ROOT%/src/main/java/com/microservice/nav/utils.

Custom Error handlers in %APP_ROOT%/src/main/java/com/microservice/nav/ErrorHandling.

All endpoints to be defined in %APP_ROOT%/src/main/java/com/microservice/nav/endpoints.

log file generated in %APP_ROOT%/logs/server.log.


## Installation

Use the package manager [mvn](https://maven.apache.org/) to install application.

```bash
mvn clean install
```

## Usage

Distributable JAR will be located in %APP_ROOT%/target/nav-0.0.1-SNAPSHOT.jar

We can either run the project by running this command at APP_ROOT:

```bash
mvn spring-boot:run
```

OR

Running following command at JAR location

```bash
java -jar nav-0.0.1-SNAPSHOT.jar
```

endpoint to access nav value:

```bash
curl -i localhost:8080/nav/getNavValue?date=20190101
```
Date to be provided in YYYYMMDD format

Default value for date is today's date if none provided

sample output:

```
{"date":"20190101","securities":{"Apples and Oranges":{"totalQty":336,"price":472.36,"assetsValue":158712.96},"ABC Corporation":{"totalQty":980,"price":666.63,"assetsValue":653297.4}},"nav":812010.36}
```


## Exceptions

FormatException: Thrown if date is alphanumeric or length of date != 8
sample:
```
{"errorCode":"-2262518668984742328","errorMessage":"com.microservice.nav.ErrorHandling.FormatException: Format of date is incorrect","errorType":"FormatException"}
```

DateNotFoundException: Thrown if data for requested date is not found
Sample:
```
{"errorCode":"-8300052716576709995","errorMessage":"com.microservice.nav.ErrorHandling.DateNotFoundException: Date not found","errorType":"DateNotFoundException"}
```

Generic Exception: for all other exceptions(timeouts, etc)
```
Something bad happened. Please try again !!
```

## Contributing
Raw mock data for holding and pricing provided by https://raw.githubusercontent.com/arcjsonapi
https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/holding
https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/pricing
