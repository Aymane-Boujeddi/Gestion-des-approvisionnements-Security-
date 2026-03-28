# API Documentation - Authentication & User Management

## Base URL
```
http://localhost:8080/gestionStock/api/v1
```

## Authentication
All endpoints except `/auth/login` and `/auth/register` require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 1. Authentication Controller (`/auth`)

### 1.1 Login
Authenticate a user and receive a JWT token.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Validation:**
- `username`: Required, cannot be empty
- `password`: Required, cannot be empty

**Success Response (200 OK):**
```json
{
  "message": "string",
  "username": "string",
  "token": "string",
  "role": "string",
  "permissions": ["string"]
}
```

**Example:**
```typescript
// Angular Service Call
login(credentials: { username: string; password: string }) {
  return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, credentials);
}
```

---

### 1.2 Register
Create a new user account.

**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Validation:**
- `username`: Required, 3-50 characters, cannot be empty
- `password`: Required, minimum 6 characters, cannot be empty

**Success Response (200 OK):**
```json
{
  "message": "string",
  "username": "string"
}
```

**Example:**
```typescript
// Angular Service Call
register(userData: { username: string; password: string }) {
  return this.http.post<RegisterResponse>(`${this.baseUrl}/auth/register`, userData);
}
```

---

## 2. User Management Controller (`/users`)

All endpoints require authentication and specific permissions.

### 2.1 Assign Role to User
Assign a role to a specific user. This updates the user's permissions based on the role.

**Endpoint:** `POST /users/assign-role`

**Required Permission:** `ROLE_ASSIGN`

**Request Body:**
```json
{
  "userId": 1,
  "role": "ADMIN"
}
```

**Available Roles:**
- `ADMIN`
- `MANAGER`
- `USER`
- (Check your Role enum for complete list)

**Validation:**
- `userId`: Required, must be a valid Long
- `role`: Required, must be a valid Role enum value

**Success Response (200 OK):**
```json
{
  "userId": 1,
  "username": "string",
  "permissions": ["string"],
  "message": "string"
}
```

**Example:**
```typescript
// Angular Service Call
assignRole(userId: number, role: string) {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${this.getToken()}`
  });
  return this.http.post<RoleAssignmentResponse>(
    `${this.baseUrl}/users/assign-role`,
    { userId, role },
    { headers }
  );
}
```

---

### 2.2 Modify User Permission
Add or remove a specific permission for a user (permission override).

**Endpoint:** `POST /users/modify-permission`

**Required Permission:** `PERMISSION_MANAGE`

**Request Body:**
```json
{
  "userId": 1,
  "permissionName": "USER_CREATE",
  "granted": true
}
```

**Validation:**
- `userId`: Required, must be a valid Long
- `permissionName`: Required, cannot be empty
- `granted`: Required, boolean (true = grant permission, false = revoke permission)

**Success Response (200 OK):**
```json
{
  "userId": 1,
  "username": "string",
  "permissionName": "string",
  "granted": true,
  "effectivePermissions": ["string"],
  "message": "string"
}
```

**Example:**
```typescript
// Angular Service Call
modifyPermission(userId: number, permissionName: string, granted: boolean) {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${this.getToken()}`
  });
  return this.http.post<PermissionModificationResponse>(
    `${this.baseUrl}/users/modify-permission`,
    { userId, permissionName, granted },
    { headers }
  );
}
```

---

### 2.3 Get User Permissions
Retrieve all permissions for a specific user, including role-based and custom overrides.

**Endpoint:** `GET /users/{userId}/permissions`

**Required Permission:** `USER_MANAGE`

**Path Parameters:**
- `userId`: Long - The ID of the user

**Success Response (200 OK):**
```json
{
  "userId": 1,
  "username": "string",
  "roleName": "string",
  "effectivePermissions": ["string"],
  "customOverrides": [
    {
      "permissionName": "string",
      "granted": true
    }
  ]
}
```

**Example:**
```typescript
// Angular Service Call
getUserPermissions(userId: number) {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${this.getToken()}`
  });
  return this.http.get<UserPermissionsResponse>(
    `${this.baseUrl}/users/${userId}/permissions`,
    { headers }
  );
}
```

---

### 2.4 Get All Users
Retrieve a list of all users with their permissions.

**Endpoint:** `GET /users`

**Required Permission:** `USER_MANAGE`

**Success Response (200 OK):**
```json
[
  {
    "userId": 1,
    "username": "string",
    "roleName": "string",
    "effectivePermissions": ["string"],
    "customOverrides": [
      {
        "permissionName": "string",
        "granted": true
      }
    ]
  }
]
```

**Example:**
```typescript
// Angular Service Call
getAllUsers() {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${this.getToken()}`
  });
  return this.http.get<UserPermissionsResponse[]>(
    `${this.baseUrl}/users`,
    { headers }
  );
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
Invalid request data or validation errors.
```json
{
  "message": "string",
  "errors": ["string"]
}
```

### 401 Unauthorized
Missing or invalid authentication token.
```json
{
  "message": "Unauthorized"
}
```

### 403 Forbidden
User doesn't have required permissions.
```json
{
  "message": "Access Denied"
}
```

### 404 Not Found
Resource not found.
```json
{
  "message": "Resource not found"
}
```

### 500 Internal Server Error
Server error.
```json
{
  "message": "Internal server error"
}
```

---

## Angular TypeScript Interfaces

```typescript
// Request Interfaces
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface AssignRoleRequest {
  userId: number;
  role: string;
}

export interface ModifyPermissionRequest {
  userId: number;
  permissionName: string;
  granted: boolean;
}

// Response Interfaces
export interface AuthResponse {
  message: string;
  username: string;
  token: string;
  role: string;
  permissions: string[];
}

export interface RegisterResponse {
  message: string;
  username: string;
}

export interface RoleAssignmentResponse {
  userId: number;
  username: string;
  permissions: string[];
  message: string;
}

export interface PermissionModificationResponse {
  userId: number;
  username: string;
  permissionName: string;
  granted: boolean;
  effectivePermissions: string[];
  message: string;
}

export interface PermissionOverride {
  permissionName: string;
  granted: boolean;
}

export interface UserPermissionsResponse {
  userId: number;
  username: string;
  roleName: string;
  effectivePermissions: string[];
  customOverrides: PermissionOverride[];
}
```

---

## Complete Angular Service Example

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/gestionStock/api/v1';

  constructor(private http: HttpClient) {}

  // Helper method to get token from storage
  private getToken(): string {
    return localStorage.getItem('token') || '';
  }

  // Helper method to create headers with token
  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.getToken()}`
    });
  }

  // Auth endpoints
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, credentials);
  }

  register(userData: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.baseUrl}/auth/register`, userData);
  }

  // User Management endpoints
  assignRole(request: AssignRoleRequest): Observable<RoleAssignmentResponse> {
    return this.http.post<RoleAssignmentResponse>(
      `${this.baseUrl}/users/assign-role`,
      request,
      { headers: this.getAuthHeaders() }
    );
  }

  modifyPermission(request: ModifyPermissionRequest): Observable<PermissionModificationResponse> {
    return this.http.post<PermissionModificationResponse>(
      `${this.baseUrl}/users/modify-permission`,
      request,
      { headers: this.getAuthHeaders() }
    );
  }

  getUserPermissions(userId: number): Observable<UserPermissionsResponse> {
    return this.http.get<UserPermissionsResponse>(
      `${this.baseUrl}/users/${userId}/permissions`,
      { headers: this.getAuthHeaders() }
    );
  }

  getAllUsers(): Observable<UserPermissionsResponse[]> {
    return this.http.get<UserPermissionsResponse[]>(
      `${this.baseUrl}/users`,
      { headers: this.getAuthHeaders() }
    );
  }
}
```

---

## Notes

1. **JWT Token Storage**: Store the JWT token received from login in localStorage or sessionStorage
2. **Token Expiration**: Token expires after 24 hours (86400000 ms)
3. **CORS**: Ensure your Angular app is configured to handle CORS if running on a different port
4. **Permission Names**: Check your backend Permission enum for the complete list of available permissions
5. **Role Names**: Check your backend Role enum for the complete list of available roles
