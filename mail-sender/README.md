
You can run unit tests using Spring Cloud Connectors

```
export VCAP_SERVICES="{\"smtp\":[{\"credentials\":{\"uri\":\"smtp://user:password@localhost:3465\"},\"name\":\"smtp\"}]}"
export VCAP_APPLICATION="{}"
export SPRING_PROFILES_ACTIVE=cloud
./mvnw test
```

This configurations is equivalent to:

```
cf create-user-provided-service smtp -p '{"credentials":{"uri":"smtp://user:password@localhost:3465"}}'
cf bind-service your-app smtp
```