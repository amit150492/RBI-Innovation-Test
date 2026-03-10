# RBI-Innovation-Test

# Development Notes

## Overall Approach
Developed a RESTful service using Spring Boot to handle loan eligibility processing based on applicant profile and loan details.

## Key Design Decisions
- Used Java `record` types for immutable DTOs.
- Implemented `@JsonInclude(NON_NULL)` to handle conditional response fields.

## Trade-offs
- Used in-memory EMI calculation to prioritize speed over external service integration.

## Improvements
- Add dedicated Exception handling for validation failures.
- Implement Spring Security for endpoint protection.