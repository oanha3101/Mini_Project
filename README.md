# Employee Management System

Spring Boot mini project for the first three learning modules. Requires Java 17+ and Maven.

## Run

```bash
mvn spring-boot:run
```

## API

- `GET /hello` — module 1 health check.
- `GET /api/employees` — list in-memory employees; optional `?department=Engineering` filter.
- `GET /api/employees/{id}` — find one employee.
- `POST /api/employees` — add an employee (returns `201 Created` and a `Location` header).

Example request:

```json
{
  "fullName": "Nguyen Van An",
  "department": "Engineering",
  "email": "an@example.com"
}
```

The first employee is assigned code `EMP1001`. Data is held in memory and resets when the application restarts.

## Module mapping

1. Spring Boot entry point and `/hello` REST endpoint.
2. `UtilityService` is a component managed by Spring; `EmployeeCodeGenerator` is a custom `@Bean`, both injected using constructor injection.
3. Employee list/detail/create REST APIs use path variables, request parameters, validated request bodies, and `ResponseEntity`.
