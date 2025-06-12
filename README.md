## XQ-MS-TEST-PLAN `{version 0.0.1}`
[CHANGELOG](./CHANGELOG.md)
### Start service
`./gradlew clean bootRun`

### Test service
```
./gradlew xqStartApp
./gradlew msCompTest
./gradlew xqStopApp
```

### View service
Example request
```curl
curl --request POST \
  --url http://localhost:8080/api/requirements/create \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "xq001",
    "email": "xq001@gmail.com",
    "mobileNumber": "0123456777"
}'
```
Go to [Open API definition](http://localhost:8080/swagger-ui/index.html) to view API