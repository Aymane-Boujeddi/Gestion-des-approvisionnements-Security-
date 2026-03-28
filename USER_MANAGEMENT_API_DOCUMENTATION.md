# User Management API Documentation for Angular Frontend Integration

## Base URL
```
http://localhost:8080/gestionStock/api/v1/users
```

## Authentication
All endpoints require authentication via JWT token or Keycloak token in the `Authorization` header:
```
Authorization: Bearer <your-token>
```

---

## API Endpoints

### 1. Assign Role to User

**Endpoint:** `POST /users/assign-role`

**Description:** Assigns a specific role to a user. This endpoint replaces the user's current role with the new one.

**Required Permission:** `ROLE_ASSIGN`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Request Body:**
```typescript
interface AssignRoleRequest {
  userId: number;      // Required - The ID of the user to assign role to
  role: Role;          // Required - The role to assign (ADMIN | MAGASINIER | CHEF_ATELIER | RESPONSABLE_ACHAT)
}
```

**TypeScript Enum:**
```typescript
enum Role {
  ADMIN = 'ADMIN',
  MAGASINIER = 'MAGASINIER',
  CHEF_ATELIER = 'CHEF_ATELIER',
  RESPONSABLE_ACHAT = 'RESPONSABLE_ACHAT'
}
```

**Request Example:**
```json
{
  "userId": 5,
  "role": "ADMIN"
}
```

**Response (200 OK):**
```typescript
interface RoleAssignmentResponse {
  userId: number;
  username: string;
  permissions: string[];  // Array of permission names granted by the role
  message: string;        // Success message
}
```

**Response Example:**
```json
{
  "userId": 5,
  "username": "john.doe",
  "permissions": [
    "USER_MANAGE",
    "ROLE_ASSIGN",
    "PERMISSION_MANAGE",
    "STOCK_READ",
    "STOCK_CREATE",
    "STOCK_UPDATE",
    "STOCK_DELETE"
  ],
  "message": "Role ADMIN successfully assigned to user john.doe"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have ROLE_ASSIGN permission
- `404 Not Found` - User not found
- `400 Bad Request` - Validation errors (missing userId or role)

---

### 2. Modify User Permission

**Endpoint:** `POST /users/modify-permission`

**Description:** Adds or removes a specific permission for a user, overriding the default role permissions.

**Required Permission:** `PERMISSION_MANAGE`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Request Body:**
```typescript
interface ModifyPermissionRequest {
  userId: number;           // Required - The ID of the user
  permissionName: string;   // Required - The permission to modify (e.g., 'STOCK_CREATE')
  granted: boolean;         // Required - true to add permission, false to remove it
}
```

**Common Permission Names:**
- `USER_MANAGE` - Manage users
- `ROLE_ASSIGN` - Assign roles to users
- `PERMISSION_MANAGE` - Modify user permissions
- `STOCK_READ` - View stock information
- `STOCK_CREATE` - Create stock entries
- `STOCK_UPDATE` - Update stock entries
- `STOCK_DELETE` - Delete stock entries
- `COMMANDE_READ` - View orders
- `COMMANDE_CREATE` - Create orders
- `COMMANDE_UPDATE` - Update orders
- `COMMANDE_DELETE` - Delete orders
- `FOURNISSEUR_READ` - View suppliers
- `FOURNISSEUR_CREATE` - Create suppliers
- `FOURNISSEUR_UPDATE` - Update suppliers
- `FOURNISSEUR_DELETE` - Delete suppliers
- `BON_SORTIE_READ` - View delivery notes
- `BON_SORTIE_CREATE` - Create delivery notes
- `BON_SORTIE_UPDATE` - Update delivery notes
- `BON_SORTIE_DELETE` - Delete delivery notes

**Request Example:**
```json
{
  "userId": 5,
  "permissionName": "STOCK_DELETE",
  "granted": false
}
```

**Response (200 OK):**
```typescript
interface PermissionModificationResponse {
  userId: number;
  username: string;
  permissionName: string;         // The permission that was modified
  granted: boolean;               // The new state of the permission
  effectivePermissions: string[]; // All current effective permissions for the user
  message: string;                // Success message
}
```

**Response Example:**
```json
{
  "userId": 5,
  "username": "john.doe",
  "permissionName": "STOCK_DELETE",
  "granted": false,
  "effectivePermissions": [
    "USER_MANAGE",
    "ROLE_ASSIGN",
    "PERMISSION_MANAGE",
    "STOCK_READ",
    "STOCK_CREATE",
    "STOCK_UPDATE"
  ],
  "message": "Permission STOCK_DELETE has been removed from user john.doe"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PERMISSION_MANAGE permission
- `404 Not Found` - User or permission not found
- `400 Bad Request` - Validation errors (missing fields or invalid permission name)

---

### 3. Get User Permissions

**Endpoint:** `GET /users/{userId}/permissions`

**Description:** Retrieves all effective permissions for a specific user, including role-based permissions and custom overrides.

**Required Permission:** `USER_MANAGE`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `userId` (number) - The ID of the user

**Request Example:**
```
GET /users/5/permissions
```

**Response (200 OK):**
```typescript
interface UserPermissionsResponse {
  userId: number;
  username: string;
  roleName: string;                // The user's current role
  effectivePermissions: string[];  // All effective permissions (role + overrides)
  customOverrides: PermissionOverride[];  // Custom permission modifications
}

interface PermissionOverride {
  permissionName: string;
  granted: boolean;  // true = permission was added, false = permission was removed
}
```

**Response Example:**
```json
{
  "userId": 5,
  "username": "john.doe",
  "roleName": "ADMIN",
  "effectivePermissions": [
    "USER_MANAGE",
    "ROLE_ASSIGN",
    "PERMISSION_MANAGE",
    "STOCK_READ",
    "STOCK_CREATE",
    "STOCK_UPDATE"
  ],
  "customOverrides": [
    {
      "permissionName": "STOCK_DELETE",
      "granted": false
    }
  ]
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have USER_MANAGE permission
- `404 Not Found` - User not found

---

### 4. Get All Users

**Endpoint:** `GET /users`

**Description:** Retrieves a list of all users with their permissions and roles.

**Required Permission:** `USER_MANAGE`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Request Example:**
```
GET /users
```

**Response (200 OK):**
```typescript
type AllUsersResponse = UserPermissionsResponse[];

interface UserPermissionsResponse {
  userId: number;
  username: string;
  roleName: string;
  effectivePermissions: string[];
  customOverrides: PermissionOverride[];
}
```

**Response Example:**
```json
[
  {
    "userId": 1,
    "username": "admin",
    "roleName": "ADMIN",
    "effectivePermissions": [
      "USER_MANAGE",
      "ROLE_ASSIGN",
      "PERMISSION_MANAGE",
      "STOCK_READ",
      "STOCK_CREATE",
      "STOCK_UPDATE",
      "STOCK_DELETE"
    ],
    "customOverrides": []
  },
  {
    "userId": 5,
    "username": "john.doe",
    "roleName": "MAGASINIER",
    "effectivePermissions": [
      "STOCK_READ",
      "STOCK_CREATE",
      "STOCK_UPDATE"
    ],
    "customOverrides": [
      {
        "permissionName": "STOCK_DELETE",
        "granted": true
      }
    ]
  }
]
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have USER_MANAGE permission

---

## Angular Service Implementation

### TypeScript Interfaces

```typescript
// Enums
export enum Role {
  ADMIN = 'ADMIN',
  MAGASINIER = 'MAGASINIER',
  CHEF_ATELIER = 'CHEF_ATELIER',
  RESPONSABLE_ACHAT = 'RESPONSABLE_ACHAT'
}

// Request DTOs
export interface AssignRoleRequest {
  userId: number;
  role: Role;
}

export interface ModifyPermissionRequest {
  userId: number;
  permissionName: string;
  granted: boolean;
}

// Response DTOs
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

### Angular Service Example

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  AssignRoleRequest, 
  ModifyPermissionRequest,
  RoleAssignmentResponse,
  PermissionModificationResponse,
  UserPermissionsResponse 
} from './user-management.models';

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {
  private baseUrl = 'http://localhost:8080/gestionStock/api/v1/users';

  constructor(private http: HttpClient) {}

  /**
   * Assign a role to a user
   * Requires ROLE_ASSIGN permission
   */
  assignRole(request: AssignRoleRequest): Observable<RoleAssignmentResponse> {
    return this.http.post<RoleAssignmentResponse>(
      `${this.baseUrl}/assign-role`,
      request
    );
  }

  /**
   * Modify a specific permission for a user
   * Requires PERMISSION_MANAGE permission
   */
  modifyPermission(request: ModifyPermissionRequest): Observable<PermissionModificationResponse> {
    return this.http.post<PermissionModificationResponse>(
      `${this.baseUrl}/modify-permission`,
      request
    );
  }

  /**
   * Get permissions for a specific user
   * Requires USER_MANAGE permission
   */
  getUserPermissions(userId: number): Observable<UserPermissionsResponse> {
    return this.http.get<UserPermissionsResponse>(
      `${this.baseUrl}/${userId}/permissions`
    );
  }

  /**
   * Get all users with their permissions
   * Requires USER_MANAGE permission
   */
  getAllUsers(): Observable<UserPermissionsResponse[]> {
    return this.http.get<UserPermissionsResponse[]>(this.baseUrl);
  }
}
```

### Usage Examples in Angular Components

#### 1. Assign Role to User

```typescript
import { Component } from '@angular/core';
import { UserManagementService } from './user-management.service';
import { Role } from './user-management.models';

@Component({
  selector: 'app-assign-role',
  template: `
    <form (submit)="assignRole()">
      <input [(ngModel)]="userId" type="number" placeholder="User ID" name="userId" />
      <select [(ngModel)]="selectedRole" name="role">
        <option *ngFor="let role of roles" [value]="role">{{ role }}</option>
      </select>
      <button type="submit">Assign Role</button>
    </form>
  `
})
export class AssignRoleComponent {
  userId: number = 0;
  selectedRole: Role = Role.MAGASINIER;
  roles = Object.values(Role);

  constructor(private userManagementService: UserManagementService) {}

  assignRole() {
    this.userManagementService.assignRole({
      userId: this.userId,
      role: this.selectedRole
    }).subscribe({
      next: (response) => {
        console.log('Role assigned successfully:', response.message);
        console.log('User permissions:', response.permissions);
      },
      error: (error) => {
        console.error('Error assigning role:', error);
      }
    });
  }
}
```

#### 2. Modify User Permission

```typescript
import { Component } from '@angular/core';
import { UserManagementService } from './user-management.service';

@Component({
  selector: 'app-modify-permission',
  template: `
    <form (submit)="modifyPermission()">
      <input [(ngModel)]="userId" type="number" placeholder="User ID" name="userId" />
      <input [(ngModel)]="permissionName" placeholder="Permission Name" name="permission" />
      <label>
        <input [(ngModel)]="granted" type="checkbox" name="granted" />
        Grant Permission
      </label>
      <button type="submit">Modify Permission</button>
    </form>
  `
})
export class ModifyPermissionComponent {
  userId: number = 0;
  permissionName: string = '';
  granted: boolean = true;

  constructor(private userManagementService: UserManagementService) {}

  modifyPermission() {
    this.userManagementService.modifyPermission({
      userId: this.userId,
      permissionName: this.permissionName,
      granted: this.granted
    }).subscribe({
      next: (response) => {
        console.log(response.message);
        console.log('Effective permissions:', response.effectivePermissions);
      },
      error: (error) => {
        console.error('Error modifying permission:', error);
      }
    });
  }
}
```

#### 3. Display User Permissions

```typescript
import { Component, OnInit } from '@angular/core';
import { UserManagementService } from './user-management.service';
import { UserPermissionsResponse } from './user-management.models';

@Component({
  selector: 'app-user-permissions',
  template: `
    <div *ngIf="userPermissions">
      <h2>{{ userPermissions.username }}</h2>
      <p>Role: {{ userPermissions.roleName }}</p>
      
      <h3>Effective Permissions:</h3>
      <ul>
        <li *ngFor="let permission of userPermissions.effectivePermissions">
          {{ permission }}
        </li>
      </ul>
      
      <h3>Custom Overrides:</h3>
      <ul>
        <li *ngFor="let override of userPermissions.customOverrides">
          {{ override.permissionName }} - {{ override.granted ? 'ADDED' : 'REMOVED' }}
        </li>
      </ul>
    </div>
  `
})
export class UserPermissionsComponent implements OnInit {
  userPermissions: UserPermissionsResponse | null = null;
  userId: number = 5;

  constructor(private userManagementService: UserManagementService) {}

  ngOnInit() {
    this.loadUserPermissions();
  }

  loadUserPermissions() {
    this.userManagementService.getUserPermissions(this.userId).subscribe({
      next: (response) => {
        this.userPermissions = response;
      },
      error: (error) => {
        console.error('Error loading user permissions:', error);
      }
    });
  }
}
```

#### 4. Display All Users

```typescript
import { Component, OnInit } from '@angular/core';
import { UserManagementService } from './user-management.service';
import { UserPermissionsResponse } from './user-management.models';

@Component({
  selector: 'app-users-list',
  template: `
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Role</th>
          <th>Permissions Count</th>
          <th>Custom Overrides</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let user of users">
          <td>{{ user.userId }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.roleName }}</td>
          <td>{{ user.effectivePermissions.length }}</td>
          <td>{{ user.customOverrides.length }}</td>
          <td>
            <button (click)="viewDetails(user.userId)">View Details</button>
          </td>
        </tr>
      </tbody>
    </table>
  `
})
export class UsersListComponent implements OnInit {
  users: UserPermissionsResponse[] = [];

  constructor(private userManagementService: UserManagementService) {}

  ngOnInit() {
    this.loadAllUsers();
  }

  loadAllUsers() {
    this.userManagementService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response;
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }

  viewDetails(userId: number) {
    // Navigate to user details page or show modal
    console.log('View details for user:', userId);
  }
}
```

---

## Error Handling

### Common Error Response Format

```typescript
interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  details: any | null;
}
```

### Error Handling Service

```typescript
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  handleError(error: HttpErrorResponse): string {
    if (error.status === 401) {
      return 'Unauthorized. Please login again.';
    } else if (error.status === 403) {
      return 'You do not have permission to perform this action.';
    } else if (error.status === 404) {
      return 'Resource not found.';
    } else if (error.status === 400) {
      return error.error?.message || 'Invalid request data.';
    } else {
      return 'An unexpected error occurred. Please try again later.';
    }
  }
}
```

---

## HTTP Interceptor for Authentication

```typescript
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Get token from localStorage or your auth service
    const token = localStorage.getItem('authToken');
    
    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(cloned);
    }
    
    return next.handle(req);
  }
}
```

### Register Interceptor in App Module

```typescript
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';

@NgModule({
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class AppModule {}
```

---

## Testing with Postman

### 1. Assign Role
```
POST http://localhost:8080/gestionStock/api/v1/users/assign-role
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "userId": 5,
  "role": "ADMIN"
}
```

### 2. Modify Permission
```
POST http://localhost:8080/gestionStock/api/v1/users/modify-permission
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "userId": 5,
  "permissionName": "STOCK_DELETE",
  "granted": false
}
```

### 3. Get User Permissions
```
GET http://localhost:8080/gestionStock/api/v1/users/5/permissions
Headers:
  Authorization: Bearer <your-token>
```

### 4. Get All Users
```
GET http://localhost:8080/gestionStock/api/v1/users
Headers:
  Authorization: Bearer <your-token>
```

---

## Notes

1. **Authentication**: All endpoints require a valid JWT or Keycloak token in the Authorization header.

2. **Permissions**: Each endpoint has specific permission requirements. Make sure the authenticated user has the required permissions before calling the endpoint.

3. **Role vs Permission System**: 
   - Users are assigned a **role** which comes with default permissions
   - Individual **permissions** can be added or removed to override role defaults
   - The `effectivePermissions` array shows the final combined permissions

4. **CORS**: If calling from a different domain, ensure CORS is properly configured on the backend.

5. **Environment Configuration**: Update the `baseUrl` in the Angular service based on your environment (development, staging, production).

6. **Token Refresh**: Implement token refresh logic to handle expired tokens automatically.

---

## Complete Angular Module Setup

```typescript
// user-management.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { UserManagementService } from './user-management.service';
import { AssignRoleComponent } from './assign-role.component';
import { ModifyPermissionComponent } from './modify-permission.component';
import { UserPermissionsComponent } from './user-permissions.component';
import { UsersListComponent } from './users-list.component';

@NgModule({
  declarations: [
    AssignRoleComponent,
    ModifyPermissionComponent,
    UserPermissionsComponent,
    UsersListComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    UserManagementService
  ],
  exports: [
    AssignRoleComponent,
    ModifyPermissionComponent,
    UserPermissionsComponent,
    UsersListComponent
  ]
})
export class UserManagementModule {}
```

This documentation provides everything needed to integrate the User Management API into your Angular frontend application.
