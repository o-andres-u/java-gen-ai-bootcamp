# Instructions to test this application
This is the practical application used across the GenAI Practical Application Development using Java course.

## Environment variables
Create the following environment variables for the application to execute successfully.
```
AZURE_OPEN_AI_KEY={your provided key}
AZURE_OPEN_AI_DEPLOYMENT_NAME={The deployment/model name to be used by default}
```

## Running the application
Execute the `GenAiTrainingApplication` class using Java 17. This will create a REST application locally.

## Testing the Open AI Chat endpoint
**Important: You need to be connected to the EPAM VPN.**
- Using postman or any other tool of your preference, send a POST request to this endpoint: http://localhost:8080/chat-bot/prompt
- The request body should be a JSON following this schema:
```
{
    "input": "Hello bot!",
    "model": "gpt-4-turbo",
    "temperature": 0.5,
    "max-tokens": 1000
}
```
- The application should respond with a format like this:
```
{
    "response": "Model response"
}
```
