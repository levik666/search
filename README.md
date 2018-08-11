# Simple Search Engine Service

Public REST API of the upload small documents where each document contains a series of tokens (words) separated by spaces.
This service is based on the spring-boot framework and uses emebedded tomcat to serve static and dynamic content.

## How to build

- Compile boot jar
```bash
./gradlew clean build
```

## Run project

```bash
java -jar build/libs/*.jar or ./gradlew bootRun
```

To change the port of application by default 5555

```bash
export PORT=5555
```

## Demo 

- simple search engine deployed on heroku https://levik.herokuapp.com/
- CI/CD for test and deploy use travis https://travis-ci.org/levik666/search



## API documentation:

- Application health
```bash
curl -X GET \
  http://127.0.0.1:5555/actuator/health \
  -H 'accept: application/json' \
  -H 'content-type: application/json'

```

- Upload document 
```bash
curl -X POST \
  http://127.0.0.1:5555/api/v1/document/ \
  -H 'accept: application/json' \
  -H 'content-type: application/json' \
  -d '{
	"key" : "1",
	"tokens" : "test rrr"
}'
```

```bash
Response:  201 Created
```

```bash
error codes

Response: 400

Payload:

{
    "timestamp": "2018-08-11T06:35:49.979+0000",
    "status": 400,
    "error": "Bad Request",
    "errors": [
        {
            "codes": [
                "NotBlank.documentRequest.key",
                "NotBlank.key",
                "NotBlank.java.lang.String",
                "NotBlank"
            ],
            "arguments": [
                {
                    "codes": [
                        "documentRequest.key",
                        "key"
                    ],
                    "arguments": null,
                    "defaultMessage": "key",
                    "code": "key"
                }
            ],
            "defaultMessage": "must not be blank",
            "objectName": "documentRequest",
            "field": "key",
            "rejectedValue": "",
            "bindingFailure": false,
            "code": "NotBlank"
        },
        {
            "codes": [
                "NotBlank.documentRequest.tokens",
                "NotBlank.tokens",
                "NotBlank.java.lang.String",
                "NotBlank"
            ],
            "arguments": [
                {
                    "codes": [
                        "documentRequest.tokens",
                        "tokens"
                    ],
                    "arguments": null,
                    "defaultMessage": "tokens",
                    "code": "tokens"
                }
            ],
            "defaultMessage": "must not be blank",
            "objectName": "documentRequest",
            "field": "tokens",
            "rejectedValue": "",
            "bindingFailure": false,
            "code": "NotBlank"
        }
    ],
    "message": "Validation failed for object='documentRequest'. Error count: 2",
    "path": "/api/v1/document/"
} 

```

- Get document By key 

```bash
curl -X GET \
  http://127.0.0.1:5555/api/v1/document/1
```

```bash
Response:  200 OK

{
    "tokens": [
        "rrr",
        "test"
    ]
}
```

In case of key not found will return empty tokens response.

- Search by tokens


```bash
curl -X GET \
  'http://127.0.0.1:5555/api/v1/search?tokens=test'
```

```bash
Response:  200 OK

{
    "keys": [
        "1"
    ]
}
```

In case of key not found will return empty keys response.
