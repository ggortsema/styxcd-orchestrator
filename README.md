# orchestrator

Spring Boot 3.4.4 + Java 21 + Maven + Groovy-Eclipse + Drools + static-exported Next.js frontend.

## Package

`org.mycroftai.styxcd.orchestrator`

## What it does

- `GET /hello?name=world` returns `Hello, world!`
- `GET /hello?name=john` returns `goodbye!` via Drools
- `/` and `/dashboard/` are static Next.js pages served by Spring Boot

## Build

```bash
mvn clean package
```

That Maven build will:
- install Node and npm locally for the project
- run `npm install` in `frontend/`
- run the Next.js build/export
- copy the exported frontend into Spring Boot's static output
- build the jar

## Run

```bash
java -jar target/orchestrator-0.0.1-SNAPSHOT.jar
```

Or, after the project has been built once:

```bash
mvn spring-boot:run
```

## Endpoints

- http://localhost:8080/hello?name=world
- http://localhost:8080/hello?name=john
- http://localhost:8080/
- http://localhost:8080/dashboard/
