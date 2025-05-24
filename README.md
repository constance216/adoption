# Pet Adoption System API Documentation

A comprehensive RESTful API for managing a pet adoption system with role-based access control.

## Table of Contents
- [Setup](#setup)
- [Authentication](#authentication)
- [User Management](#user-management)
- [Categories](#categories)
- [Breeds](#breeds)
- [Pets](#pets)
- [Adoptions](#adoptions)
- [Shelters](#shelters)
- [Veterinarians](#veterinarians)

## Setup

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.8+

### Database Configuration
1. Create a PostgreSQL database:
```sql
CREATE DATABASE pets_db;
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pets_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### Running the Application
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Admin User
On first startup, the system creates a default admin user:
- Username: `admin`
- Password: `admin123`

## Authentication

### Register New User

**Endpoint:** `POST /api/auth/signup`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "USER"
}
```

**Response (200 OK):**
```json
{
  "message": "User registered successfully!"
}
```

### Login

**Endpoint:** `POST /api/auth/signin`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 2,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Using the JWT Token

Include the JWT token in the Authorization header for all subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## User Management

### Get All Users (Admin Only)

**Endpoint:** `GET /api/users`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "fullName": "System Administrator",
    "role": "ADMIN",
    "createdAt": "2024-01-10T10:00:00",
    "updatedAt": "2024-01-10T10:00:00"
  },
  {
    "id": 2,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "createdAt": "2024-01-10T10:05:00",
    "updatedAt": "2024-01-10T10:05:00"
  }
]
```

### Update User

**Endpoint:** `PUT /api/users/{id}`

**Headers:**
```
Authorization: Bearer <user_token>
```

**Request Body:**
```json
{
  "email": "newemail@example.com",
  "fullName": "John Updated Doe",
  "password": "newpassword123"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "username": "john_doe",
  "email": "newemail@example.com",
  "fullName": "John Updated Doe",
  "role": "USER",
  "createdAt": "2024-01-10T10:05:00",
  "updatedAt": "2024-01-10T11:00:00"
}
```

## Categories

### Create Category (Admin Only)

**Endpoint:** `POST /api/categories`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "name": "Dogs"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Dogs",
  "createdAt": "2024-01-10T11:00:00"
}
```

### Get All Categories

**Endpoint:** `GET /api/categories`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Dogs",
    "createdAt": "2024-01-10T11:00:00"
  },
  {
    "id": 2,
    "name": "Cats",
    "createdAt": "2024-01-10T11:01:00"
  }
]
```

## Breeds

### Create Breed (Admin Only)

**Endpoint:** `POST /api/breeds`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "name": "Golden Retriever",
  "categoryId": 1,
  "description": "Friendly and intelligent dog breed"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Golden Retriever",
  "category": {
    "id": 1,
    "name": "Dogs",
    "createdAt": "2024-01-10T11:00:00"
  },
  "description": "Friendly and intelligent dog breed",
  "createdAt": "2024-01-10T11:05:00",
  "updatedAt": "2024-01-10T11:05:00"
}
```

### Get Breeds by Category

**Endpoint:** `GET /api/breeds/category/{categoryId}`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Golden Retriever",
    "category": {
      "id": 1,
      "name": "Dogs",
      "createdAt": "2024-01-10T11:00:00"
    },
    "description": "Friendly and intelligent dog breed",
    "createdAt": "2024-01-10T11:05:00",
    "updatedAt": "2024-01-10T11:05:00"
  },
  {
    "id": 2,
    "name": "German Shepherd",
    "category": {
      "id": 1,
      "name": "Dogs",
      "createdAt": "2024-01-10T11:00:00"
    },
    "description": "Loyal and protective dog breed",
    "createdAt": "2024-01-10T11:06:00",
    "updatedAt": "2024-01-10T11:06:00"
  }
]
```

## Pets

### Create Pet (Admin/Shelter Only)

**Endpoint:** `POST /api/pets`

**Headers:**
```
Authorization: Bearer <admin_or_shelter_token>
```

**Request Body:**
```json
{
  "name": "Max",
  "breedId": 1,
  "categoryId": 1,
  "age": 3,
  "description": "Friendly golden retriever looking for a home",
  "image": "https://example.com/max.jpg",
  "gender": "MALE",
  "shelterId": 3
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Max",
  "breed": {
    "id": 1,
    "name": "Golden Retriever"
  },
  "category": {
    "id": 1,
    "name": "Dogs"
  },
  "age": 3,
  "description": "Friendly golden retriever looking for a home",
  "image": "https://example.com/max.jpg",
  "gender": "MALE",
  "status": "ACTIVE",
  "owner": null,
  "shelter": {
    "id": 3,
    "username": "happy_shelter",
    "fullName": "Happy Pet Shelter"
  },
  "veterinarian": null,
  "adoptedBy": null,
  "createdAt": "2024-01-10T12:00:00",
  "updatedAt": "2024-01-10T12:00:00"
}
```

### Get Pets by Status

**Endpoint:** `GET /api/pets/status/{status}`

**Example:** `GET /api/pets/status/ACTIVE`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Max",
    "breed": {
      "id": 1,
      "name": "Golden Retriever"
    },
    "category": {
      "id": 1,
      "name": "Dogs"
    },
    "age": 3,
    "gender": "MALE",
    "status": "ACTIVE",
    "image": "https://example.com/max.jpg"
  }
]
```

### Assign Veterinarian to Pet

**Endpoint:** `POST /api/pets/{petId}/veterinarian/{veterinarianId}`

**Headers:**
```
Authorization: Bearer <admin_or_vet_token>
```

**Example:** `POST /api/pets/1/veterinarian/4`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Max",
  "veterinarian": {
    "id": 4,
    "username": "dr_smith",
    "fullName": "Dr. Sarah Smith"
  },
  // ... other pet fields
}
```

## Adoptions

### Create Adoption Request

**Endpoint:** `POST /api/adoptions`

**Headers:**
```
Authorization: Bearer <user_token>
```

**Request Body:**
```json
{
  "petId": 1,
  "adopterId": 2,
  "notes": "I have a large backyard and experience with dogs"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "pet": {
    "id": 1,
    "name": "Max",
    "breed": "Golden Retriever",
    "category": "Dogs",
    "age": 3,
    "gender": "MALE",
    "status": "ACTIVE",
    "image": "https://example.com/max.jpg"
  },
  "adopter": {
    "id": 2,
    "username": "john_doe",
    "fullName": "John Doe"
  },
  "adoptionDate": "2024-01-10T13:00:00",
  "notes": "I have a large backyard and experience with dogs",
  "status": "PENDING",
  "createdAt": "2024-01-10T13:00:00",
  "updatedAt": "2024-01-10T13:00:00"
}
```

### Update Adoption Status (Admin/Shelter Only)

**Endpoint:** `PUT /api/adoptions/{id}/status`

**Headers:**
```
Authorization: Bearer <admin_or_shelter_token>
```

**Request Body:**
```json
{
  "status": "APPROVED"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "pet": {
    "id": 1,
    "name": "Max",
    "status": "ACTIVE"
  },
  "adopter": {
    "id": 2,
    "username": "john_doe",
    "fullName": "John Doe"
  },
  "status": "APPROVED",
  // ... other fields
}
```

### Complete Adoption

**Endpoint:** `PUT /api/adoptions/{id}/status`

**Request Body:**
```json
{
  "status": "COMPLETED"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "pet": {
    "id": 1,
    "name": "Max",
    "status": "ADOPTED"
  },
  "adopter": {
    "id": 2,
    "username": "john_doe",
    "fullName": "John Doe"
  },
  "status": "COMPLETED",
  // ... other fields
}
```

## Shelters

### Create Shelter Account (Admin Only)

**Endpoint:** `POST /api/shelters`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "username": "happy_shelter",
  "email": "contact@happyshelter.com",
  "password": "shelter123",
  "fullName": "Happy Pet Shelter"
}
```

**Response (200 OK):**
```json
{
  "id": 3,
  "username": "happy_shelter",
  "email": "contact@happyshelter.com",
  "fullName": "Happy Pet Shelter",
  "role": "SHELTER",
  "createdAt": "2024-01-10T10:30:00",
  "updatedAt": "2024-01-10T10:30:00"
}
```

### Get Shelter's Pets

**Endpoint:** `GET /api/shelters/{id}/pets`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Max",
    "breed": {
      "id": 1,
      "name": "Golden Retriever"
    },
    "category": {
      "id": 1,
      "name": "Dogs"
    },
    "age": 3,
    "gender": "MALE",
    "status": "ACTIVE",
    "shelter": {
      "id": 3,
      "username": "happy_shelter",
      "fullName": "Happy Pet Shelter"
    }
  }
]
```

## Veterinarians

### Create Veterinarian Account (Admin Only)

**Endpoint:** `POST /api/veterinarians`

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "username": "dr_smith",
  "email": "sarah.smith@vetclinic.com",
  "password": "vet123",
  "fullName": "Dr. Sarah Smith"
}
```

**Response (200 OK):**
```json
{
  "id": 4,
  "username": "dr_smith",
  "email": "sarah.smith@vetclinic.com",
  "fullName": "Dr. Sarah Smith",
  "role": "VETERINARIAN",
  "createdAt": "2024-01-10T10:35:00",
  "updatedAt": "2024-01-10T10:35:00"
}
```

### Get Veterinarian's Assigned Pets

**Endpoint:** `GET /api/veterinarians/{id}/pets`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Max",
    "breed": {
      "id": 1,
      "name": "Golden Retriever"
    },
    "age": 3,
    "gender": "MALE",
    "status": "ACTIVE",
    "veterinarian": {
      "id": 4,
      "username": "dr_smith",
      "fullName": "Dr. Sarah Smith"
    }
  }
]
```

## Error Responses

### Validation Error
```json
{
  "timestamp": "2024-01-10T14:00:00",
  "message": "Username already exists",
  "details": "uri=/api/auth/signup"
}
```

### Unauthorized
```json
{
  "timestamp": "2024-01-10T14:00:00",
  "message": "Unauthorized: Authentication token was either missing or invalid.",
  "details": "uri=/api/users"
}
```

### Not Found
```json
{
  "timestamp": "2024-01-10T14:00:00",
  "message": "Pet not found with id: 999",
  "details": "uri=/api/pets/999"
}
```

### Forbidden
```json
{
  "timestamp": "2024-01-10T14:00:00",
  "message": "Access denied",
  "details": "uri=/api/admin/users"
}
```

## Role-Based Access Control

| Role         | Permissions                                                       |
|-------------|------------------------------------------------------------------|
| USER        | Create adoption requests, view pets, update own profile          |
| SHELTER     | Manage pets, approve adoptions, view adoption requests           |
| VETERINARIAN| Assign/unassign self to pets, view assigned pets                |
| ADMIN       | Full system access, manage all entities                         |

## API Testing

1. Register new users with different roles
2. Login to get JWT tokens
3. Use tokens to test role-based endpoints
4. Test the full adoption workflow:
   - Create categories and breeds (admin)
   - Create shelter and veterinarian accounts (admin)
   - Add pets (shelter)
   - Assign veterinarians to pets
   - Create adoption requests (users)
   - Process adoptions (shelter/admin)

## Status Codes

- `200`: Success
- `201`: Created
- `400`: Bad Request
- `401`: Unauthorized
- `403`: Forbidden
- `404`: Not Found
- `500`: Internal Server Error

## Additional Notes

- All timestamps are in ISO 8601 format
- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours (configurable)
- Pet statuses: ACTIVE, ADOPTED, UNAVAILABLE
- Adoption statuses: PENDING, APPROVED, COMPLETED, CANCELLED