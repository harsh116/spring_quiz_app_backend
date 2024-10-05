# spring_quiz_app_backend
&nbsp;

### Pre-requisite
1. OpenJDK(17+)
2. Maven
3. MySQL installed if using locally

### Configuration
- After creating database say 'quiz', create its schema using SQL script from `scripts`
  ```bash
  mysql -h <HOST> -u <USERNAME> -P <PORT> -p<PASSWORD> <DATABASE_NAME> < scripts/quiz_schema_bkp.sql
  ```
  if running locally then
  HOST is `localhost`
  USER is usually `root`
  PORT is `3306`
- Add some users and make sure one such is admin too. Following sample can be used too.
  ```sql
  INSERT INTO `users` VALUES
    (1,'user1','$2a$12$ydb8FpCMQ4JVwDXmOAhmE.JciHGDqVosn5FnQ6CDFhw2cwnFB1dN2','User'),
    (2,'user2','$2a$12$Od.hcSehXW9n8JtdDU/eneQBWbirFqxLkI1U6zEJidpqR5W91Wyiq','User'),
    (3,'admin1','$2a$12$KP.pggECXc9ZpdemguFALOlbnuTnAsFhq7nZHexf/3oAKYcBWPEhm','Admin');
  ```
    password is stored in hash form, real password is pass1,pass2,adminpass

### Run project
```bash
mvn spring-boot:run
```

### Packaging Application
```bash
mvn package
```
Now running packaged jar
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```
