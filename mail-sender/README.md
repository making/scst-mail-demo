Configure `demo.to` and `demo.from` in `application.properties`.

## Run Locally

Configure your SMTPS server in `application-default.properties`.

## Unit Test with Spring Cloud Connectors

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

## Deploy to Pivotal Cloud Foundry

### Create RabbitMQ Message Binder if not exists.

```
cf create-service p-rabbitmq standard rabbit-binder
```

### Create SMTPS Service

#### In case of GMail

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<Gmail Account>:<Gmail Password>@smtp.gmail.com:465"}}'
```

#### In case of Amazon SES

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<SNS Username>:<SNS Password>@email-smtp.us-west-2.amazonaws.com:465"}}'
```

> `demo.from` must be a verified address by Amazon SES


### Create `manifest.yml`

``` yml
applications:
- name: mail-sender
  memory: 1g
  buildpack: java_buildpack_offline
  path: target/mail-sender-0.0.1-SNAPSHOT.jar
  services:
  - rabbit-binder
  - smtp-service
  env:
    demo.from: hello@example.com # change me
    demo.to: noreply@example.com # change me
```

then, `cf push`!!

```
./mvnw clean package
cf push
```


## Deploy to Pivotal Web Services

### Create RabbitMQ Message Binder if not exists.

```
cf create-service cloudamqp lemur rabbit-binder
```

### Create SMTPS Service

#### In case of GMail

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<Gmail Account>:<Gmail Password>@smtp.gmail.com:465"}}'
```

#### In case of Amazon SES

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<SNS Username>:<SNS Password>@email-smtp.us-west-2.amazonaws.com:465"}}'
```

> `demo.from` must be a verified address by Amazon SES

### Create `manifest.yml`

``` yml
applications:
- name: mail-sender
  memory: 1g
  buildpack: java_buildpack
  path: target/mail-sender-0.0.1-SNAPSHOT.jar
  services:
  - rabbit-binder
  - smtp-service
  env:
    demo.from: hello@example.com # change me
    demo.to: noreply@example.com # change me
```

then, `cf push`!!

```
./mvnw clean package
cf push
```