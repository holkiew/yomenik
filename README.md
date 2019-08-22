# Yomenik

Online Mon-Fri 9:00-15:00: [AWS-LINK]
## Stack
 - Webflux, Cloud (Eureka, Config, Gateway), Reactive Data mongodb (Java 12 w/o Switch expr. )
 - Mongodb 
 - React, Typescript
 - Tests: Mockito and Spock (Guava)

## Tests
Exemplary tests: 
[https://github.com/holkiew/yomenik/tree/master/battlesim-service/src/test/java/com/holkiew/yomenik/battlesim/simulator](https://github.com/holkiew/yomenik/tree/master/battlesim-service/src/test/java/com/holkiew/yomenik/battlesim/simulator)
## Run
 - **Backend**
 - - mvn clean install
 - - ./runScript

Services run order:
 ```mermaid
graph LR
A[Config] --> B[Discovery]
B --> C[User-Service]
C --> D[Gatewayix]
D --> E((Remaining services))
```
 - **Frontend**
 - - cd frontapp
 - - yarn build
 - - serve -s build