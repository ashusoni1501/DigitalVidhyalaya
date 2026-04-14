# Manual Testing Guide

This guide covers the current implemented flow:

1. Create a school
2. Save school settings
3. Create a user for that school
4. Log in with `schoolId + username + password`

## 1. Start with Docker

From the `backend` directory:

```bash
docker-compose up --build
```

This starts:

- PostgreSQL on `localhost:5432`
- the application on `localhost:8080`

If you only want PostgreSQL and prefer running the app locally, use:

```bash
docker-compose up -d postgres
```

## 2. Start the application locally

Skip this section if you started the app with Docker already.

From the `backend` directory:

```bash
./mvnw spring-boot:run
```

Default connection values are already configured for local testing:

- database: `digital_vidhyalaya`
- username: `postgres`
- password: `postgres`

If you want custom values, export them first:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/digital_vidhyalaya
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
./mvnw spring-boot:run
```

Base URL:

```text
http://localhost:8080
```

## 3. Postman flow

### Create school

`POST /api/v1/tenants/schools`

```json
{
  "name": "Springfield Public School",
  "code": "SPS",
  "addressLine1": "Main Road",
  "addressLine2": "Near Market",
  "country": "India",
  "state": "Delhi",
  "district": "New Delhi",
  "city": "Delhi",
  "pincode": "110001",
  "primaryPhone": "9876543210",
  "primaryEmail": "info@sps.edu.in"
}
```

Copy the generated `schoolId` from the response.

### Save school settings

`PUT /api/v1/tenants/schools/{schoolId}/settings`

Example:

```json
{
  "requireAdmissionApproval": true,
  "requireStudentEditApproval": true,
  "allowCashPayment": true,
  "allowUpiPayment": true,
  "receiptPrefix": "RCPT",
  "admissionPrefix": "ADM",
  "defaultCountry": "India"
}
```

### Create admin user

`POST /api/v1/users`

```json
{
  "schoolId": "DV-DELDELSPRI-001",
  "username": "schooladmin",
  "password": "admin123",
  "fullName": "School Admin",
  "email": "admin@sps.edu.in",
  "phone": "9999999999",
  "roles": ["ADMIN"]
}
```

You can also create:

- `["STAFF"]`
- `["TEACHER"]`
- `["STAFF", "TEACHER"]`

### List users by school

`GET /api/v1/users?schoolId={schoolId}`

### Login

`POST /api/v1/auth/login`

```json
{
  "schoolId": "DV-DELDELSPRI-001",
  "username": "schooladmin",
  "password": "admin123"
}
```

## 4. Suggested test scenarios

### Normal admin login

- create school
- create admin
- log in successfully

### Suspended school

1. suspend school
2. try admin login
3. verify response has `readOnlyAccess = true`

### Suspended school staff

1. create staff user
2. suspend school
3. try staff login
4. verify login is blocked

### Deactivated school

1. deactivate school
2. try login
3. verify login is blocked

## 5. Useful endpoints

- `POST /api/v1/tenants/schools`
- `GET /api/v1/tenants/schools`
- `GET /api/v1/tenants/schools/{schoolId}`
- `PUT /api/v1/tenants/schools/{schoolId}`
- `PATCH /api/v1/tenants/schools/{schoolId}/suspend`
- `PATCH /api/v1/tenants/schools/{schoolId}/deactivate`
- `GET /api/v1/tenants/schools/{schoolId}/settings`
- `PUT /api/v1/tenants/schools/{schoolId}/settings`
- `POST /api/v1/users`
- `GET /api/v1/users?schoolId={schoolId}`
- `PUT /api/v1/users/{userId}/status`
- `POST /api/v1/auth/login`
