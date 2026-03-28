# Fournisseur (Supplier) API Documentation for Angular Frontend Integration

## Base URL
```
http://localhost:8080/gestionStock/api/v1/fournisseurs
```

## Authentication
All endpoints require authentication via JWT token or Keycloak token in the `Authorization` header:
```
Authorization: Bearer <your-token>
```

---

## API Endpoints

### 1. Create Fournisseur

**Endpoint:** `POST /fournisseurs`

**Description:** Creates a new supplier (fournisseur) in the system. Validates for duplicate email, telephone, and ICE (Identifiant Commun de l'Entreprise).

**Required Permission:** `FOURNISSEUR_CREATE`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Request Body:**
```typescript
interface FournisseurCreateRequest {
  nom: string;              // Required - Supplier name (2-50 characters)
  adresse: string;          // Required - Address (max 100 characters)
  personneContact: string;  // Required - Contact person name
  email: string;            // Required - Valid email format
  telephone: string;        // Required - Phone number
  ville: string;            // Required - City
  raisonSociale: string;    // Required - Company legal name
  ICE: string;              // Required - Company identifier (exactly 15 characters)
}
```

**Validation Rules:**
- `nom`: Must be between 2 and 50 characters
- `adresse`: Maximum 100 characters
- `email`: Must be valid email format and unique in the system
- `telephone`: Must be unique in the system
- `ICE`: Must be exactly 15 characters and unique in the system
- All fields are required (cannot be blank)

**Request Example:**
```json
{
  "nom": "Fournisseur ABC",
  "adresse": "123 Rue Mohammed V, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123"
}
```

**Response (200 OK):**
```typescript
interface FournisseurResponse {
  nom: string;
  adresse: string;
  personneContact: string;
  email: string;
  telephone: string;
  ville: string;
  raisonSociale: string;
  ICE: string;
  createdAt: string;  // ISO 8601 datetime format
}
```

**Response Example:**
```json
{
  "nom": "Fournisseur ABC",
  "adresse": "123 Rue Mohammed V, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123",
  "createdAt": "2026-01-27T14:30:00"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have FOURNISSEUR_CREATE permission
- `400 Bad Request` - Validation errors or duplicate resource
  ```json
  {
    "timestamp": "2026-01-27T14:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Duplicate resource: email already exists",
    "path": "/gestionStock/api/v1/fournisseurs"
  }
  ```

**Possible Duplicate Error Messages:**
- "Duplicate resource: email already exists"
- "Duplicate resource: telephone already exists"
- "Duplicate resource: ICE already exists"

---

### 2. Get Fournisseur by ID

**Endpoint:** `GET /fournisseurs/{id}`

**Description:** Retrieves a specific supplier by their ID.

**Required Permission:** `FOURNISSEUR_READ`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the supplier

**Request Example:**
```
GET /fournisseurs/5
```

**Response (200 OK):**
```typescript
interface FournisseurResponse {
  nom: string;
  adresse: string;
  personneContact: string;
  email: string;
  telephone: string;
  ville: string;
  raisonSociale: string;
  ICE: string;
  createdAt: string;
}
```

**Response Example:**
```json
{
  "nom": "Fournisseur ABC",
  "adresse": "123 Rue Mohammed V, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123",
  "createdAt": "2026-01-27T14:30:00"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have FOURNISSEUR_READ permission
- `404 Not Found` - Supplier not found

---

### 3. Get All Fournisseurs

**Endpoint:** `GET /fournisseurs`

**Description:** Retrieves a list of all suppliers in the system.

**Required Permission:** `FOURNISSEUR_READ`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Request Example:**
```
GET /fournisseurs
```

**Response (200 OK):**
```typescript
type FournisseursListResponse = FournisseurResponse[];
```

**Response Example:**
```json
[
  {
    "nom": "Fournisseur ABC",
    "adresse": "123 Rue Mohammed V, Casablanca",
    "personneContact": "Ahmed Benjelloun",
    "email": "contact@fournisseur-abc.ma",
    "telephone": "0522123456",
    "ville": "Casablanca",
    "raisonSociale": "ABC Supply Company SARL",
    "ICE": "001234567890123",
    "createdAt": "2026-01-27T14:30:00"
  },
  {
    "nom": "Fournisseur XYZ",
    "adresse": "456 Avenue Hassan II, Rabat",
    "personneContact": "Fatima El Amrani",
    "email": "info@xyz-supply.ma",
    "telephone": "0537654321",
    "ville": "Rabat",
    "raisonSociale": "XYZ Trading SA",
    "ICE": "002345678901234",
    "createdAt": "2026-01-20T10:15:00"
  }
]
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have FOURNISSEUR_READ permission

---

### 4. Update Fournisseur

**Endpoint:** `PUT /fournisseurs/{id}`

**Description:** Updates an existing supplier's information.

**Required Permission:** `FOURNISSEUR_UPDATE`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the supplier to update

**Request Body:**
```typescript
interface FournisseurUpdateRequest {
  nom: string;              // Required - Supplier name (2-50 characters)
  adresse: string;          // Required - Address (max 100 characters)
  personneContact: string;  // Required - Contact person name
  email: string;            // Required - Valid email format
  telephone: string;        // Required - Phone number
  ville: string;            // Required - City
  raisonSociale: string;    // Required - Company legal name
  ICE: string;              // Required - Company identifier (exactly 15 characters)
}
```

**Request Example:**
```
PUT /fournisseurs/5

{
  "nom": "Fournisseur ABC Updated",
  "adresse": "789 Boulevard Zerktouni, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123"
}
```

**Response (200 OK):**
```typescript
interface FournisseurResponse {
  nom: string;
  adresse: string;
  personneContact: string;
  email: string;
  telephone: string;
  ville: string;
  raisonSociale: string;
  ICE: string;
  createdAt: string;
}
```

**Response Example:**
```json
{
  "nom": "Fournisseur ABC Updated",
  "adresse": "789 Boulevard Zerktouni, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123",
  "createdAt": "2026-01-27T14:30:00"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have FOURNISSEUR_UPDATE permission
- `404 Not Found` - Supplier not found
- `400 Bad Request` - Validation errors

---

### 5. Delete Fournisseur

**Endpoint:** `DELETE /fournisseurs/{id}`

**Description:** Deletes a supplier from the system.

**Required Permission:** `FOURNISSEUR_DELETE`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the supplier to delete

**Request Example:**
```
DELETE /fournisseurs/5
```

**Response (200 OK):**
```typescript
type DeleteResponse = string;
```

**Response Example:**
```
"Fournisseur with the id 5 is deleted successfully"
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have FOURNISSEUR_DELETE permission
- `404 Not Found` - Supplier not found

---

## Angular Service Implementation

### TypeScript Interfaces

```typescript
// Request DTO
export interface FournisseurCreateRequest {
  nom: string;
  adresse: string;
  personneContact: string;
  email: string;
  telephone: string;
  ville: string;
  raisonSociale: string;
  ICE: string;
}

// Response DTO
export interface FournisseurResponse {
  nom: string;
  adresse: string;
  personneContact: string;
  email: string;
  telephone: string;
  ville: string;
  raisonSociale: string;
  ICE: string;
  createdAt: string;
}
```

### Angular Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FournisseurCreateRequest, FournisseurResponse } from './fournisseur.models';

@Injectable({
  providedIn: 'root'
})
export class FournisseurService {
  private baseUrl = 'http://localhost:8080/gestionStock/api/v1/fournisseurs';

  constructor(private http: HttpClient) {}

  /**
   * Create a new supplier
   * Requires FOURNISSEUR_CREATE permission
   */
  createFournisseur(fournisseur: FournisseurCreateRequest): Observable<FournisseurResponse> {
    return this.http.post<FournisseurResponse>(this.baseUrl, fournisseur);
  }

  /**
   * Get a supplier by ID
   * Requires FOURNISSEUR_READ permission
   */
  getFournisseurById(id: number): Observable<FournisseurResponse> {
    return this.http.get<FournisseurResponse>(`${this.baseUrl}/${id}`);
  }

  /**
   * Get all suppliers
   * Requires FOURNISSEUR_READ permission
   */
  getAllFournisseurs(): Observable<FournisseurResponse[]> {
    return this.http.get<FournisseurResponse[]>(this.baseUrl);
  }

  /**
   * Update a supplier
   * Requires FOURNISSEUR_UPDATE permission
   */
  updateFournisseur(id: number, fournisseur: FournisseurCreateRequest): Observable<FournisseurResponse> {
    return this.http.put<FournisseurResponse>(`${this.baseUrl}/${id}`, fournisseur);
  }

  /**
   * Delete a supplier
   * Requires FOURNISSEUR_DELETE permission
   */
  deleteFournisseur(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}
```

### Usage Examples in Angular Components

#### 1. Create Fournisseur Form

```typescript
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FournisseurService } from './fournisseur.service';

@Component({
  selector: 'app-create-fournisseur',
  template: `
    <form [formGroup]="fournisseurForm" (ngSubmit)="onSubmit()">
      <div>
        <label>Nom:</label>
        <input formControlName="nom" />
        <span *ngIf="fournisseurForm.get('nom')?.invalid && fournisseurForm.get('nom')?.touched">
          Nom is required (2-50 characters)
        </span>
      </div>

      <div>
        <label>Adresse:</label>
        <input formControlName="adresse" />
      </div>

      <div>
        <label>Personne Contact:</label>
        <input formControlName="personneContact" />
      </div>

      <div>
        <label>Email:</label>
        <input formControlName="email" type="email" />
        <span *ngIf="fournisseurForm.get('email')?.invalid && fournisseurForm.get('email')?.touched">
          Valid email is required
        </span>
      </div>

      <div>
        <label>Téléphone:</label>
        <input formControlName="telephone" />
      </div>

      <div>
        <label>Ville:</label>
        <input formControlName="ville" />
      </div>

      <div>
        <label>Raison Sociale:</label>
        <input formControlName="raisonSociale" />
      </div>

      <div>
        <label>ICE (15 caractères):</label>
        <input formControlName="ICE" maxlength="15" />
        <span *ngIf="fournisseurForm.get('ICE')?.invalid && fournisseurForm.get('ICE')?.touched">
          ICE must be exactly 15 characters
        </span>
      </div>

      <button type="submit" [disabled]="fournisseurForm.invalid">Create Fournisseur</button>
    </form>

    <div *ngIf="errorMessage" class="error">{{ errorMessage }}</div>
    <div *ngIf="successMessage" class="success">{{ successMessage }}</div>
  `
})
export class CreateFournisseurComponent {
  fournisseurForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private fournisseurService: FournisseurService
  ) {
    this.fournisseurForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      adresse: ['', [Validators.required, Validators.maxLength(100)]],
      personneContact: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
      ville: ['', Validators.required],
      raisonSociale: ['', Validators.required],
      ICE: ['', [Validators.required, Validators.minLength(15), Validators.maxLength(15)]]
    });
  }

  onSubmit() {
    if (this.fournisseurForm.valid) {
      this.fournisseurService.createFournisseur(this.fournisseurForm.value).subscribe({
        next: (response) => {
          this.successMessage = `Fournisseur ${response.nom} created successfully!`;
          this.errorMessage = '';
          this.fournisseurForm.reset();
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Error creating fournisseur';
          this.successMessage = '';
        }
      });
    }
  }
}
```

#### 2. List All Fournisseurs

```typescript
import { Component, OnInit } from '@angular/core';
import { FournisseurService } from './fournisseur.service';
import { FournisseurResponse } from './fournisseur.models';

@Component({
  selector: 'app-fournisseurs-list',
  template: `
    <h2>Liste des Fournisseurs</h2>
    
    <table>
      <thead>
        <tr>
          <th>Nom</th>
          <th>Email</th>
          <th>Téléphone</th>
          <th>Ville</th>
          <th>ICE</th>
          <th>Créé le</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let fournisseur of fournisseurs">
          <td>{{ fournisseur.nom }}</td>
          <td>{{ fournisseur.email }}</td>
          <td>{{ fournisseur.telephone }}</td>
          <td>{{ fournisseur.ville }}</td>
          <td>{{ fournisseur.ICE }}</td>
          <td>{{ fournisseur.createdAt | date:'short' }}</td>
          <td>
            <button (click)="viewDetails(fournisseur)">View</button>
            <button (click)="editFournisseur(fournisseur)">Edit</button>
            <button (click)="deleteFournisseur(fournisseur)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div *ngIf="fournisseurs.length === 0">No suppliers found.</div>
  `
})
export class FournisseursListComponent implements OnInit {
  fournisseurs: FournisseurResponse[] = [];

  constructor(private fournisseurService: FournisseurService) {}

  ngOnInit() {
    this.loadFournisseurs();
  }

  loadFournisseurs() {
    this.fournisseurService.getAllFournisseurs().subscribe({
      next: (data) => {
        this.fournisseurs = data;
      },
      error: (error) => {
        console.error('Error loading fournisseurs:', error);
      }
    });
  }

  viewDetails(fournisseur: FournisseurResponse) {
    console.log('View details:', fournisseur);
    // Navigate to details page or show modal
  }

  editFournisseur(fournisseur: FournisseurResponse) {
    console.log('Edit fournisseur:', fournisseur);
    // Navigate to edit page or show edit modal
  }

  deleteFournisseur(fournisseur: FournisseurResponse) {
    if (confirm(`Are you sure you want to delete ${fournisseur.nom}?`)) {
      // Assuming we have an ID field, you might need to get it from somewhere
      // For now, this is a placeholder
      const id = 1; // Replace with actual ID
      
      this.fournisseurService.deleteFournisseur(id).subscribe({
        next: (message) => {
          console.log(message);
          this.loadFournisseurs(); // Reload the list
        },
        error: (error) => {
          console.error('Error deleting fournisseur:', error);
        }
      });
    }
  }
}
```

#### 3. Update Fournisseur

```typescript
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FournisseurService } from './fournisseur.service';

@Component({
  selector: 'app-update-fournisseur',
  template: `
    <h2>Update Fournisseur</h2>
    
    <form [formGroup]="fournisseurForm" (ngSubmit)="onUpdate()">
      <!-- Same form fields as create component -->
      <div>
        <label>Nom:</label>
        <input formControlName="nom" />
      </div>

      <div>
        <label>Adresse:</label>
        <input formControlName="adresse" />
      </div>

      <div>
        <label>Personne Contact:</label>
        <input formControlName="personneContact" />
      </div>

      <div>
        <label>Email:</label>
        <input formControlName="email" type="email" />
      </div>

      <div>
        <label>Téléphone:</label>
        <input formControlName="telephone" />
      </div>

      <div>
        <label>Ville:</label>
        <input formControlName="ville" />
      </div>

      <div>
        <label>Raison Sociale:</label>
        <input formControlName="raisonSociale" />
      </div>

      <div>
        <label>ICE:</label>
        <input formControlName="ICE" maxlength="15" />
      </div>

      <button type="submit" [disabled]="fournisseurForm.invalid">Update</button>
      <button type="button" (click)="cancel()">Cancel</button>
    </form>
  `
})
export class UpdateFournisseurComponent implements OnInit {
  fournisseurForm: FormGroup;
  fournisseurId: number = 0;

  constructor(
    private fb: FormBuilder,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.fournisseurForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      adresse: ['', [Validators.required, Validators.maxLength(100)]],
      personneContact: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
      ville: ['', Validators.required],
      raisonSociale: ['', Validators.required],
      ICE: ['', [Validators.required, Validators.minLength(15), Validators.maxLength(15)]]
    });
  }

  ngOnInit() {
    this.fournisseurId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadFournisseur();
  }

  loadFournisseur() {
    this.fournisseurService.getFournisseurById(this.fournisseurId).subscribe({
      next: (fournisseur) => {
        this.fournisseurForm.patchValue(fournisseur);
      },
      error: (error) => {
        console.error('Error loading fournisseur:', error);
      }
    });
  }

  onUpdate() {
    if (this.fournisseurForm.valid) {
      this.fournisseurService.updateFournisseur(this.fournisseurId, this.fournisseurForm.value).subscribe({
        next: (response) => {
          console.log('Fournisseur updated successfully:', response);
          this.router.navigate(['/fournisseurs']);
        },
        error: (error) => {
          console.error('Error updating fournisseur:', error);
        }
      });
    }
  }

  cancel() {
    this.router.navigate(['/fournisseurs']);
  }
}
```

---

## Testing with Postman

### 1. Create Fournisseur
```
POST http://localhost:8080/gestionStock/api/v1/fournisseurs
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "nom": "Fournisseur ABC",
  "adresse": "123 Rue Mohammed V, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123"
}
```

### 2. Get Fournisseur by ID
```
GET http://localhost:8080/gestionStock/api/v1/fournisseurs/5
Headers:
  Authorization: Bearer <your-token>
```

### 3. Get All Fournisseurs
```
GET http://localhost:8080/gestionStock/api/v1/fournisseurs
Headers:
  Authorization: Bearer <your-token>
```

### 4. Update Fournisseur
```
PUT http://localhost:8080/gestionStock/api/v1/fournisseurs/5
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "nom": "Fournisseur ABC Updated",
  "adresse": "789 Boulevard Zerktouni, Casablanca",
  "personneContact": "Ahmed Benjelloun",
  "email": "contact@fournisseur-abc.ma",
  "telephone": "0522123456",
  "ville": "Casablanca",
  "raisonSociale": "ABC Supply Company SARL",
  "ICE": "001234567890123"
}
```

### 5. Delete Fournisseur
```
DELETE http://localhost:8080/gestionStock/api/v1/fournisseurs/5
Headers:
  Authorization: Bearer <your-token>
```

---

## Validation Rules Summary

| Field | Rules |
|-------|-------|
| nom | Required, 2-50 characters |
| adresse | Required, max 100 characters |
| personneContact | Required |
| email | Required, valid email format, unique |
| telephone | Required, unique |
| ville | Required |
| raisonSociale | Required |
| ICE | Required, exactly 15 characters, unique |

---

## Error Handling

### Common Validation Errors

```typescript
interface ValidationError {
  timestamp: string;
  status: 400;
  error: "Bad Request";
  message: string;
  path: string;
  details?: {
    field: string;
    message: string;
  }[];
}
```

### Example Validation Error Response
```json
{
  "timestamp": "2026-01-27T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/gestionStock/api/v1/fournisseurs",
  "details": [
    {
      "field": "nom",
      "message": "Name must between 2 and 50 characters"
    },
    {
      "field": "ICE",
      "message": "ICE must be 15 characters"
    }
  ]
}
```

---

## Notes

1. **ICE Number**: The ICE (Identifiant Commun de l'Entreprise) is a unique 15-character identifier for Moroccan companies.

2. **Unique Constraints**: Email, telephone, and ICE must be unique across all suppliers. Attempting to create/update with duplicate values will result in a 400 error.

3. **Required Permissions**:
   - Create: `FOURNISSEUR_CREATE`
   - Read: `FOURNISSEUR_READ`
   - Update: `FOURNISSEUR_UPDATE`
   - Delete: `FOURNISSEUR_DELETE`

4. **Date Format**: The `createdAt` field returns ISO 8601 datetime format (e.g., "2026-01-27T14:30:00").

5. **Response on Delete**: Returns a plain text message, not JSON.

This documentation provides everything needed to integrate the Fournisseur API into your Angular frontend application.
