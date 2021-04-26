## eCommerce's points bank microservice

### System Requirements
- Java 8+
- MySql 5.7+
    - Change DB configuration under spring.datasource in src/main/resources/application.yml
    
### How torun
- Run by `gradlew bootRun`
- OpenAPI document and try calling Rest API at `http://127.0.0.1:8080/swagger-ui/`

### Key design choices
- Use MySQL JSON data fields to achieve schema-less transaction table
- Using optimistic lock to prevent race condition when creating transaction
- Pagination statement

### To be improved
- Retry transaction in certain use case when optimistic lock exception thrown
