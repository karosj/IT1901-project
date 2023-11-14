# ScheduleLog REST API Documentation

## Overview

This documentation provides details for the RESTful services provided by the rest package in ScheduleLog, which includes endpoints for managing activity records and supporting operations like retrieving and adding activities. 

We have decided to use the Spring Boot framework for creating a REST API.

## Base URL
The base URL for these endpoints is typically http://localhost:8080/.

## Cross-Origin Requests
This API supports cross-origin requests from http://localhost:5173.

## Endpoints

### 1. Get Activities

- URL: /activities
- Method: GET
- Description: Retrieves all activities.
- Request Body: None
- Responses:
    - 200 OK: Returns a JSON string representing all activities.
    - 500 Internal Server Error: In case of server errors.
### 2. Add Activity
- URL: /addActivity
- Method: POST
- Description: Adds a new activity based on the provided Activity object.
- Request Body:
```
{
  "subjects": [{"code": "String", ...}],
  "startTime": "DateTime",
  "endTime": "DateTime",
  "description": "String"
}
```
- Responses:
    - 200 OK: Activity added successfully.
    - 400 Bad Request: Returns a description of validation errors (e.g., missing fields, invalid data).
    - 500 Internal Server Error: In case of server errors.

## Validation
The API performs validation checks on the input data for the addActivity endpoint. It checks for the presence and correctness of fields such as subjects, start time, end time, and description. Error messages are provided in the response body for invalid inputs.

## Error Handling
Errors are communicated through standard HTTP response codes: 400 for client errors, 500 for server errors, and descriptive messages in the response body.

## Data Models
### Activity
- Represents an activity with fields like subjects, start time, end time, and description.
### Subject
- Represents a subject associated with an activity.

## Serialization
The API uses Jackson for JSON serialization and deserialization, including support for Java 8 Date & Time API.

