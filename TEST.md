# Pet Adoption System - API Testing Quick Reference

## 1. Initial Setup - Admin Login

```bash
# Login as admin
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Save the JWT token from response for admin operations.

## 2. Create Test Users

```bash
# Create a shelter account
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "city_shelter",
    "email": "shelter@city.com",
    "password": "shelter123",
    "fullName": "City Animal Shelter",
    "role": "SHELTER"
  }'

# Create a veterinarian account
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dr_jones",
    "email": "jones@vetclinic.com",
    "password": "vet123",
    "fullName": "Dr. Michael Jones",
    "role": "VETERINARIAN"
  }'

# Create a regular user account
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "sarah_connor",
    "email": "sarah@email.com",
    "password": "user123",
    "fullName": "Sarah Connor",
    "role": "USER"
  }'
```

## 3. Create Categories and Breeds (Admin)

```bash
# Create dog category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "name": "Dogs"
  }'

# Create cat category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "name": "Cats"
  }'

# Create dog breeds
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "name": "Labrador Retriever",
    "categoryId": 1,
    "description": "Friendly, outgoing, and active breed"
  }'

# Create cat breeds
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "name": "Persian",
    "categoryId": 2,
    "description": "Long-haired breed with a gentle temperament"
  }'
```

## 4. Login as Shelter and Add Pets

```bash
# Login as shelter
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "city_shelter",
    "password": "shelter123"
  }'

# Add a dog
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <shelter_token>" \
  -d '{
    "name": "Buddy",
    "breedId": 1,
    "categoryId": 1,
    "age": 2,
    "description": "Energetic and loving Labrador looking for a forever home",
    "image": "https://example.com/buddy.jpg",
    "gender": "MALE",
    "shelterId": 2
  }'

# Add a cat
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <shelter_token>" \
  -d '{
    "name": "Mittens",
    "breedId": 2,
    "categoryId": 2,
    "age": 4,
    "description": "Calm Persian cat, great with children",
    "image": "https://example.com/mittens.jpg",
    "gender": "FEMALE",
    "shelterId": 2
  }'
```

## 5. Assign Veterinarian to Pets

```bash
# Login as veterinarian
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dr_jones",
    "password": "vet123"
  }'

# Assign veterinarian to pet
curl -X POST http://localhost:8080/api/pets/1/veterinarian/3 \
  -H "Authorization: Bearer <vet_token>"
```

## 6. User Adopts a Pet

```bash
# Login as user
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "sarah_connor",
    "password": "user123"
  }'

# View available pets
curl -X GET http://localhost:8080/api/pets/status/ACTIVE \
  -H "Authorization: Bearer <user_token>"

# Create adoption request
curl -X POST http://localhost:8080/api/adoptions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <user_token>" \
  -d '{
    "petId": 1,
    "adopterId": 4,
    "notes": "I have experience with dogs and a fenced yard"
  }'
```

## 7. Process Adoption (Shelter)

```bash
# Get pending adoptions
curl -X GET http://localhost:8080/api/adoptions/status/PENDING \
  -H "Authorization: Bearer <shelter_token>"

# Approve adoption
curl -X PUT http://localhost:8080/api/adoptions/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <shelter_token>" \
  -d '{
    "status": "APPROVED"
  }'

# Complete adoption
curl -X PUT http://localhost:8080/api/adoptions/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <shelter_token>" \
  -d '{
    "status": "COMPLETED"
  }'
```

## 8. View Results

```bash
# Check pet status (should be ADOPTED)
curl -X GET http://localhost:8080/api/pets/1 \
  -H "Authorization: Bearer <any_token>"

# View user's adopted pets
curl -X GET http://localhost:8080/api/adoptions/adopter/4 \
  -H "Authorization: Bearer <user_token>"
```

## Common Test Scenarios

### Test User Permissions
```bash
# Try to create category as regular user (should fail)
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <user_token>" \
  -d '{"name": "Birds"}'
```

### Test Duplicate Prevention
```bash
# Try to create duplicate category (should fail)
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{"name": "Dogs"}'
```

### Test Adoption Workflow
1. Create pet (shelter)
2. User requests adoption
3. Shelter approves request
4. Shelter completes adoption
5. Verify pet status is ADOPTED
6. Verify pet has adoptedBy field set

### Test Error Handling
```bash
# Invalid pet ID
curl -X GET http://localhost:8080/api/pets/999 \
  -H "Authorization: Bearer <any_token>"

# Missing required field
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <shelter_token>" \
  -d '{
    "name": "Rex",
    "age": 3
  }'
```

## Postman Collection Structure

```
Pet Adoption System
├── Auth
│   ├── Register User
│   ├── Login
│   └── Validate Token
├── Users
│   ├── Get All Users
│   ├── Get User by ID
│   ├── Update User
│   └── Delete User
├── Categories
│   ├── Create Category
│   ├── Get All Categories
│   ├── Get Category by ID
│   └── Update Category
├── Breeds
│   ├── Create Breed
│   ├── Get All Breeds
│   ├── Get Breeds by Category
│   └── Update Breed
├── Pets
│   ├── Create Pet
│   ├── Get All Pets
│   ├── Get Pets by Status
│   ├── Assign Shelter
│   └── Assign Veterinarian
├── Adoptions
│   ├── Create Adoption Request
│   ├── Get All Adoptions
│   ├── Update Adoption Status
│   └── Delete Adoption
├── Shelters
│   ├── Create Shelter
│   ├── Get All Shelters
│   └── Get Shelter Pets
└── Veterinarians
    ├── Create Veterinarian
    ├── Get All Veterinarians
    └── Get Assigned Pets
```

## Environment Variables for Postman

```json
{
  "base_url": "http://localhost:8080",
  "admin_token": "",
  "shelter_token": "",
  "vet_token": "",
  "user_token": "",
  "current_pet_id": "",
  "current_adoption_id": ""
}
```