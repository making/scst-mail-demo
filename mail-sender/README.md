Configure `demo.to` and `demo.from` in `application.properties`.

## Run Locally

Configure your SMTP server in `application-default.properties`.

#### In case of GMail

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<Gmail Account>
spring.mail.password=<Gmail Password>

demo.to=<Your Email>
```

#### In case of Amazon SES

``` properties
spring.mail.host=email-smtp.us-west-2.amazonaws.com
spring.mail.port=25
spring.mail.username=<SNS Username>
spring.mail.password=<SNS Password>

demo.from=<Email in SES Domain>
demo.to=<Your Email>
```

## Unit Test with Spring Cloud Connectors

You can run unit tests using Spring Cloud Connectors

```
export VCAP_SERVICES="{\"smtp\":[{\"credentials\":{\"uri\":\"smtp://user:password@localhost:3025\"},\"name\":\"smtp\"}]}"
export VCAP_APPLICATION="{}"
export SPRING_PROFILES_ACTIVE=cloud
export SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_REQUIRED=false
./mvnw test
```

This configurations is equivalent to:

```
cf create-user-provided-service smtp -p '{"credentials":{"uri":"smtp://user:password@localhost:3025"}}'
cf bind-service your-app smtp
```

## Deploy to Pivotal Cloud Foundry

### Create RabbitMQ Message Binder if not exists.

```
cf create-service p-rabbitmq standard rabbit-binder
```

### Create SMTP Service

#### In case of GMail

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<Gmail Account>:<Gmail Password>@smtp.gmail.com:587"}}'
```

#### In case of Amazon SES

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<SNS Username>:<SNS Password>@email-smtp.us-west-2.amazonaws.com:25"}}'
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
    demo.from: noreply@example.com # change me
    demo.to: hello@example.com # change me
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

### Create SMTP Service

#### In case of GMail

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<Gmail Account>:<Gmail Password>@smtp.gmail.com:587"}}'
```

#### In case of Amazon SES

```
cf create-user-provided-service smtp-service -p '{"credentials":{"uri":"smtp://<SNS Username>:<SNS Password>@email-smtp.us-west-2.amazonaws.com:25"}}'
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
    demo.from: noreply@example.com # change me
    demo.to: hello@example.com # change me
```

then, `cf push`!!

```
./mvnw clean package
cf push
```