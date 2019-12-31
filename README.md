# Yomenik

Online Mon-Fri 9:00-15:00: http://ec2-52-56-215-176.eu-west-2.compute.amazonaws.com or http://52.56.215.176
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

 - - Services run order:
 graph LR 
 A[Config] --> B[Discovery]
 B --> C[User-Service]
 C --> D[Gatewayix]
 D --> E((Remaining services))

 - **Frontend**
 - - cd frontapp
 - - yarn build
 - - serve -s build
 
 https://github.com/typescript-cheatsheets/react-typescript-cheatsheet#reacttypescript-cheatsheets

# Running tips and caveats
- Each time *MapStruct* DTOs are being changed, do **mvn clean**
- Use profile **local** to run services without cloud support
- To create user database profiles, run whole cloud env. *Gateway* will ultimately create these