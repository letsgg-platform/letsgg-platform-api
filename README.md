# letsgg-platform-api
Backend API for let'sGG Platform 
### ![build](https://github.com/letsgg-platform/letsgg-platform-api/workflows/build/badge.svg?branch=main) ![service tests](https://github.com/letsgg-platform/letsgg-platform-api/workflows/service%20tests/badge.svg?branch=main)

## Requirements
- **JDK >= 1.8**
- **Maven >= 3.6.3**
- **MongoDB >= 4.4**

## Build

```bash
mvn clean package -DskipTests
```

## Run
```bash
mvn spring-boot:run
```

## Test
```bash
mvn clean verify
```
