## Coster.io - Notification service

Microservice responsible for sending out email notifications and reports to users.
Developed in spring-boot.

### Build the app:
* Prerequisites: Maven, JDK11
* `mvn clean install -Pdocker` - if you have docker engine
* `mvn clean install` - if not
    
### REST Interface:
- Swagger UI: localhost:9003/swagger-ui.html

### Actuator endpoints:
- Health: localhost:9003/actuator/health
- Beans: localhost:9003/actuator/beans
- Status: localhost:9003/actuator/status
