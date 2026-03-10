DEVELOPMENT_NOTES.md

Overall Approach
Developed a RESTful service using Spring Boot to handle loan eligibility processing based on applicant profile and loan details. The architecture follows a standard layered approach (Controller, Service, Repository, Model).

Key Design Decisions
Immutable DTOs: Used Java record types to ensure data integrity and thread safety across layers.

Conditional JSON: Utilized @JsonInclude(JsonInclude.Include.NON_NULL) to cleanly toggle between offer and rejectionReasons based on the application status.

Persistence: Used Spring Data JPA with a MySQL backend for audit and record-keeping requirements.

Trade-offs
In-memory Calculation: Implemented EMI logic internally to minimize latency and avoid dependencies on external rate-lookup services.

Data Persistence Strategy: Chose to store the rejectionReasons as a comma-separated string to maintain a simple, single-table schema instead of creating a complex one-to-many relationship.

Assumptions
Interest Rates: Assumed base interest is 12% and varies linearly based on risk band and employment type as per business documentation.

Currency/Precision: Used BigDecimal for all monetary values to prevent rounding errors inherent in double or float types.

Environment: Assumed a standard MySQL instance is available on localhost:3306.

Improvements
Global Exception Handling: Implement @ControllerAdvice to replace default Spring error responses with custom, descriptive JSON errors.

Security: Integrate Spring Security to protect endpoints and implement basic authentication or JWT.

Database Migrations: Implement Liquibase or Flyway to manage schema versioning instead of relying on ddl-auto=update.