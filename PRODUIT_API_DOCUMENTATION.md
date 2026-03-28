# Produit (Product) API Documentation for Angular Frontend Integration

## Base URL
```
http://localhost:8080/gestionStock/api/v1/produits
```

## Authentication
All endpoints require authentication via JWT token or Keycloak token in the `Authorization` header:
```
Authorization: Bearer <your-token>
```

---

## API Endpoints

### 1. Create Produit

**Endpoint:** `POST /produits`

**Description:** Creates a new product in the system.

**Required Permission:** `PRODUIT_CREATE`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Request Body:**
```typescript
interface ProduitCreateRequest {
  reference: string;      // Required - Product reference code
  nom: string;            // Required - Product name
  description: string;    // Required - Product description
  prixUnitaire: number;   // Required - Unit price (must be positive)
  categorie: string;      // Required - Product category
  stockActuel: number;    // Required - Current stock level (min: 0)
  pointCommande: number;  // Required - Reorder point (min: 0)
  uniteMesure: string;    // Required - Unit of measurement (e.g., kg, pcs, liters)
}
```

**Validation Rules:**
- `reference`: Cannot be blank
- `nom`: Cannot be blank
- `description`: Cannot be blank
- `prixUnitaire`: Must be a positive number
- `categorie`: Cannot be blank
- `stockActuel`: Must be greater than or equal to 0
- `pointCommande`: Must be greater than or equal to 0
- `uniteMesure`: Cannot be blank

**Request Example:**
```json
{
  "reference": "PROD-001",
  "nom": "Clavier Mécanique RGB",
  "description": "Clavier gaming avec rétroéclairage RGB et switches mécaniques",
  "prixUnitaire": 599.99,
  "categorie": "Informatique",
  "stockActuel": 50,
  "pointCommande": 10,
  "uniteMesure": "pcs"
}
```

**Response (201 Created):**
```typescript
interface ProduitResponse {
  nom: string;
  description: string;
  prixUnitaire: number;
  categorie: string;
  stockActuel: number;
  pointCommande: number;
  UniteMesure: string;  // Note: Capital 'U' in response
}
```

**Response Example:**
```json
{
  "nom": "Clavier Mécanique RGB",
  "description": "Clavier gaming avec rétroéclairage RGB et switches mécaniques",
  "prixUnitaire": 599.99,
  "categorie": "Informatique",
  "stockActuel": 50,
  "pointCommande": 10,
  "UniteMesure": "pcs"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PRODUIT_CREATE permission
- `400 Bad Request` - Validation errors
  ```json
  {
    "timestamp": "2026-01-27T15:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/gestionStock/api/v1/produits"
  }
  ```

---

### 2. Get Produit by ID

**Endpoint:** `GET /produits/{id}`

**Description:** Retrieves a specific product by its ID.

**Required Permission:** `PRODUIT_READ`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the product

**Request Example:**
```
GET /produits/5
```

**Response (200 OK):**
```typescript
interface ProduitResponse {
  nom: string;
  description: string;
  prixUnitaire: number;
  categorie: string;
  stockActuel: number;
  pointCommande: number;
  UniteMesure: string;
}
```

**Response Example:**
```json
{
  "nom": "Clavier Mécanique RGB",
  "description": "Clavier gaming avec rétroéclairage RGB et switches mécaniques",
  "prixUnitaire": 599.99,
  "categorie": "Informatique",
  "stockActuel": 50,
  "pointCommande": 10,
  "UniteMesure": "pcs"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PRODUIT_READ permission
- `404 Not Found` - Product not found

---

### 3. Get All Produits

**Endpoint:** `GET /produits`

**Description:** Retrieves a list of all products in the system.

**Required Permission:** `PRODUIT_READ`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Request Example:**
```
GET /produits
```

**Response (200 OK):**
```typescript
type ProduitsListResponse = ProduitResponse[];
```

**Response Example:**
```json
[
  {
    "nom": "Clavier Mécanique RGB",
    "description": "Clavier gaming avec rétroéclairage RGB et switches mécaniques",
    "prixUnitaire": 599.99,
    "categorie": "Informatique",
    "stockActuel": 50,
    "pointCommande": 10,
    "UniteMesure": "pcs"
  },
  {
    "nom": "Souris Sans Fil",
    "description": "Souris ergonomique sans fil avec batterie rechargeable",
    "prixUnitaire": 249.99,
    "categorie": "Informatique",
    "stockActuel": 100,
    "pointCommande": 20,
    "UniteMesure": "pcs"
  },
  {
    "nom": "Écran LED 27 pouces",
    "description": "Moniteur Full HD avec technologie IPS",
    "prixUnitaire": 1999.99,
    "categorie": "Informatique",
    "stockActuel": 25,
    "pointCommande": 5,
    "UniteMesure": "pcs"
  }
]
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PRODUIT_READ permission

---

### 4. Update Produit

**Endpoint:** `PUT /produits/{id}`

**Description:** Updates an existing product's information.

**Required Permission:** `PRODUIT_UPDATE`

**Request Headers:**
```typescript
{
  'Content-Type': 'application/json',
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the product to update

**Request Body:**
```typescript
interface ProduitUpdateRequest {
  reference: string;      // Required - Product reference code
  nom: string;            // Required - Product name
  description: string;    // Required - Product description
  prixUnitaire: number;   // Required - Unit price (must be positive)
  categorie: string;      // Required - Product category
  stockActuel: number;    // Required - Current stock level (min: 0)
  pointCommande: number;  // Required - Reorder point (min: 0)
  uniteMesure: string;    // Required - Unit of measurement
}
```

**Request Example:**
```
PUT /produits/5

{
  "reference": "PROD-001",
  "nom": "Clavier Mécanique RGB Pro",
  "description": "Clavier gaming professionnel avec rétroéclairage RGB et switches mécaniques Cherry MX",
  "prixUnitaire": 699.99,
  "categorie": "Informatique",
  "stockActuel": 45,
  "pointCommande": 15,
  "uniteMesure": "pcs"
}
```

**Response (200 OK):**
```typescript
interface ProduitResponse {
  nom: string;
  description: string;
  prixUnitaire: number;
  categorie: string;
  stockActuel: number;
  pointCommande: number;
  UniteMesure: string;
}
```

**Response Example:**
```json
{
  "nom": "Clavier Mécanique RGB Pro",
  "description": "Clavier gaming professionnel avec rétroéclairage RGB et switches mécaniques Cherry MX",
  "prixUnitaire": 699.99,
  "categorie": "Informatique",
  "stockActuel": 45,
  "pointCommande": 15,
  "UniteMesure": "pcs"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PRODUIT_UPDATE permission
- `404 Not Found` - Product not found
- `400 Bad Request` - Validation errors

---

### 5. Delete Produit

**Endpoint:** `DELETE /produits/{id}`

**Description:** Deletes a product from the system.

**Required Permission:** `PRODUIT_DELETE`

**Request Headers:**
```typescript
{
  'Authorization': 'Bearer <token>'
}
```

**Path Parameters:**
- `id` (number) - The ID of the product to delete

**Request Example:**
```
DELETE /produits/5
```

**Response (200 OK):**
```typescript
type DeleteResponse = string;
```

**Response Example:**
```
"Produit with id 5 is deleted successfully"
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - User doesn't have PRODUIT_DELETE permission
- `404 Not Found` - Product not found

---

## Angular Service Implementation

### TypeScript Interfaces

```typescript
// Request DTO
export interface ProduitCreateRequest {
  reference: string;
  nom: string;
  description: string;
  prixUnitaire: number;
  categorie: string;
  stockActuel: number;
  pointCommande: number;
  uniteMesure: string;
}

// Response DTO
export interface ProduitResponse {
  nom: string;
  description: string;
  prixUnitaire: number;
  categorie: string;
  stockActuel: number;
  pointCommande: number;
  UniteMesure: string;  // Note: Capital 'U'
}

// Common product categories (examples)
export enum ProductCategory {
  INFORMATIQUE = 'Informatique',
  BUREAU = 'Bureau',
  ELECTRONIQUE = 'Electronique',
  MOBILIER = 'Mobilier',
  CONSOMMABLES = 'Consommables'
}

// Common units of measurement
export enum UniteMesure {
  PIECES = 'pcs',
  KILOGRAM = 'kg',
  LITER = 'L',
  METER = 'm',
  BOX = 'box',
  PACK = 'pack'
}
```

### Angular Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProduitCreateRequest, ProduitResponse } from './produit.models';

@Injectable({
  providedIn: 'root'
})
export class ProduitService {
  private baseUrl = 'http://localhost:8080/gestionStock/api/v1/produits';

  constructor(private http: HttpClient) {}

  /**
   * Create a new product
   * Requires PRODUIT_CREATE permission
   */
  createProduit(produit: ProduitCreateRequest): Observable<ProduitResponse> {
    return this.http.post<ProduitResponse>(this.baseUrl, produit);
  }

  /**
   * Get a product by ID
   * Requires PRODUIT_READ permission
   */
  getProduitById(id: number): Observable<ProduitResponse> {
    return this.http.get<ProduitResponse>(`${this.baseUrl}/${id}`);
  }

  /**
   * Get all products
   * Requires PRODUIT_READ permission
   */
  getAllProduits(): Observable<ProduitResponse[]> {
    return this.http.get<ProduitResponse[]>(this.baseUrl);
  }

  /**
   * Update a product
   * Requires PRODUIT_UPDATE permission
   */
  updateProduit(id: number, produit: ProduitCreateRequest): Observable<ProduitResponse> {
    return this.http.put<ProduitResponse>(`${this.baseUrl}/${id}`, produit);
  }

  /**
   * Delete a product
   * Requires PRODUIT_DELETE permission
   */
  deleteProduit(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  /**
   * Get products with low stock (stock below reorder point)
   */
  getLowStockProducts(): Observable<ProduitResponse[]> {
    return this.http.get<ProduitResponse[]>(this.baseUrl).pipe(
      map(products => products.filter(p => p.stockActuel <= p.pointCommande))
    );
  }
}
```

### Usage Examples in Angular Components

#### 1. Create Produit Form

```typescript
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProduitService } from './produit.service';
import { ProductCategory, UniteMesure } from './produit.models';

@Component({
  selector: 'app-create-produit',
  template: `
    <h2>Créer un Nouveau Produit</h2>
    
    <form [formGroup]="produitForm" (ngSubmit)="onSubmit()">
      <div>
        <label>Référence:</label>
        <input formControlName="reference" />
        <span *ngIf="produitForm.get('reference')?.invalid && produitForm.get('reference')?.touched">
          Reference is required
        </span>
      </div>

      <div>
        <label>Nom:</label>
        <input formControlName="nom" />
        <span *ngIf="produitForm.get('nom')?.invalid && produitForm.get('nom')?.touched">
          Name is required
        </span>
      </div>

      <div>
        <label>Description:</label>
        <textarea formControlName="description" rows="3"></textarea>
        <span *ngIf="produitForm.get('description')?.invalid && produitForm.get('description')?.touched">
          Description is required
        </span>
      </div>

      <div>
        <label>Prix Unitaire (MAD):</label>
        <input formControlName="prixUnitaire" type="number" step="0.01" min="0" />
        <span *ngIf="produitForm.get('prixUnitaire')?.invalid && produitForm.get('prixUnitaire')?.touched">
          Price must be positive
        </span>
      </div>

      <div>
        <label>Catégorie:</label>
        <select formControlName="categorie">
          <option value="">-- Sélectionner --</option>
          <option *ngFor="let cat of categories" [value]="cat">{{ cat }}</option>
        </select>
      </div>

      <div>
        <label>Stock Actuel:</label>
        <input formControlName="stockActuel" type="number" min="0" />
        <span *ngIf="produitForm.get('stockActuel')?.invalid && produitForm.get('stockActuel')?.touched">
          Stock must be 0 or greater
        </span>
      </div>

      <div>
        <label>Point de Commande:</label>
        <input formControlName="pointCommande" type="number" min="0" />
        <span *ngIf="produitForm.get('pointCommande')?.invalid && produitForm.get('pointCommande')?.touched">
          Reorder point must be 0 or greater
        </span>
      </div>

      <div>
        <label>Unité de Mesure:</label>
        <select formControlName="uniteMesure">
          <option value="">-- Sélectionner --</option>
          <option *ngFor="let unit of units" [value]="unit.value">{{ unit.label }}</option>
        </select>
      </div>

      <button type="submit" [disabled]="produitForm.invalid">Créer Produit</button>
    </form>

    <div *ngIf="errorMessage" class="error">{{ errorMessage }}</div>
    <div *ngIf="successMessage" class="success">{{ successMessage }}</div>
  `
})
export class CreateProduitComponent {
  produitForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  categories = Object.values(ProductCategory);
  units = [
    { value: UniteMesure.PIECES, label: 'Pièces (pcs)' },
    { value: UniteMesure.KILOGRAM, label: 'Kilogramme (kg)' },
    { value: UniteMesure.LITER, label: 'Litre (L)' },
    { value: UniteMesure.METER, label: 'Mètre (m)' },
    { value: UniteMesure.BOX, label: 'Boîte' },
    { value: UniteMesure.PACK, label: 'Pack' }
  ];

  constructor(
    private fb: FormBuilder,
    private produitService: ProduitService
  ) {
    this.produitForm = this.fb.group({
      reference: ['', Validators.required],
      nom: ['', Validators.required],
      description: ['', Validators.required],
      prixUnitaire: [0, [Validators.required, Validators.min(0.01)]],
      categorie: ['', Validators.required],
      stockActuel: [0, [Validators.required, Validators.min(0)]],
      pointCommande: [0, [Validators.required, Validators.min(0)]],
      uniteMesure: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.produitForm.valid) {
      this.produitService.createProduit(this.produitForm.value).subscribe({
        next: (response) => {
          this.successMessage = `Produit ${response.nom} créé avec succès!`;
          this.errorMessage = '';
          this.produitForm.reset();
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Erreur lors de la création du produit';
          this.successMessage = '';
        }
      });
    }
  }
}
```

#### 2. List All Produits with Stock Alert

```typescript
import { Component, OnInit } from '@angular/core';
import { ProduitService } from './produit.service';
import { ProduitResponse } from './produit.models';

@Component({
  selector: 'app-produits-list',
  template: `
    <h2>Liste des Produits</h2>
    
    <!-- Filter options -->
    <div class="filters">
      <button (click)="showAll()">Tous ({{ produits.length }})</button>
      <button (click)="showLowStock()" class="warning">
        Stock Faible ({{ getLowStockCount() }})
      </button>
    </div>

    <table>
      <thead>
        <tr>
          <th>Nom</th>
          <th>Catégorie</th>
          <th>Prix Unitaire</th>
          <th>Stock Actuel</th>
          <th>Point Commande</th>
          <th>Unité</th>
          <th>Statut</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let produit of filteredProduits" 
            [class.low-stock]="produit.stockActuel <= produit.pointCommande">
          <td>{{ produit.nom }}</td>
          <td>{{ produit.categorie }}</td>
          <td>{{ produit.prixUnitaire | currency:'MAD' }}</td>
          <td>{{ produit.stockActuel }}</td>
          <td>{{ produit.pointCommande }}</td>
          <td>{{ produit.UniteMesure }}</td>
          <td>
            <span *ngIf="produit.stockActuel <= produit.pointCommande" class="badge warning">
              ⚠️ Stock Faible
            </span>
            <span *ngIf="produit.stockActuel > produit.pointCommande" class="badge success">
              ✓ OK
            </span>
          </td>
          <td>
            <button (click)="viewDetails(produit)">Voir</button>
            <button (click)="editProduit(produit)">Modifier</button>
            <button (click)="deleteProduit(produit)">Supprimer</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div *ngIf="filteredProduits.length === 0">Aucun produit trouvé.</div>
  `,
  styles: [`
    .low-stock {
      background-color: #fff3cd;
    }
    .badge.warning {
      background-color: #ffc107;
      color: #000;
      padding: 3px 8px;
      border-radius: 3px;
    }
    .badge.success {
      background-color: #28a745;
      color: #fff;
      padding: 3px 8px;
      border-radius: 3px;
    }
  `]
})
export class ProduitsListComponent implements OnInit {
  produits: ProduitResponse[] = [];
  filteredProduits: ProduitResponse[] = [];

  constructor(private produitService: ProduitService) {}

  ngOnInit() {
    this.loadProduits();
  }

  loadProduits() {
    this.produitService.getAllProduits().subscribe({
      next: (data) => {
        this.produits = data;
        this.filteredProduits = data;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des produits:', error);
      }
    });
  }

  showAll() {
    this.filteredProduits = this.produits;
  }

  showLowStock() {
    this.filteredProduits = this.produits.filter(
      p => p.stockActuel <= p.pointCommande
    );
  }

  getLowStockCount(): number {
    return this.produits.filter(p => p.stockActuel <= p.pointCommande).length;
  }

  viewDetails(produit: ProduitResponse) {
    console.log('View details:', produit);
    // Navigate to details page
  }

  editProduit(produit: ProduitResponse) {
    console.log('Edit produit:', produit);
    // Navigate to edit page
  }

  deleteProduit(produit: ProduitResponse) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer ${produit.nom}?`)) {
      const id = 1; // Replace with actual ID
      
      this.produitService.deleteProduit(id).subscribe({
        next: (message) => {
          console.log(message);
          this.loadProduits();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
        }
      });
    }
  }
}
```

#### 3. Update Produit

```typescript
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProduitService } from './produit.service';

@Component({
  selector: 'app-update-produit',
  template: `
    <h2>Modifier Produit</h2>
    
    <form [formGroup]="produitForm" (ngSubmit)="onUpdate()">
      <!-- Same fields as create component -->
      <div>
        <label>Référence:</label>
        <input formControlName="reference" />
      </div>

      <div>
        <label>Nom:</label>
        <input formControlName="nom" />
      </div>

      <div>
        <label>Description:</label>
        <textarea formControlName="description" rows="3"></textarea>
      </div>

      <div>
        <label>Prix Unitaire:</label>
        <input formControlName="prixUnitaire" type="number" step="0.01" />
      </div>

      <div>
        <label>Catégorie:</label>
        <input formControlName="categorie" />
      </div>

      <div>
        <label>Stock Actuel:</label>
        <input formControlName="stockActuel" type="number" />
      </div>

      <div>
        <label>Point de Commande:</label>
        <input formControlName="pointCommande" type="number" />
      </div>

      <div>
        <label>Unité de Mesure:</label>
        <input formControlName="uniteMesure" />
      </div>

      <button type="submit" [disabled]="produitForm.invalid">Mettre à jour</button>
      <button type="button" (click)="cancel()">Annuler</button>
    </form>
  `
})
export class UpdateProduitComponent implements OnInit {
  produitForm: FormGroup;
  produitId: number = 0;

  constructor(
    private fb: FormBuilder,
    private produitService: ProduitService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.produitForm = this.fb.group({
      reference: ['', Validators.required],
      nom: ['', Validators.required],
      description: ['', Validators.required],
      prixUnitaire: [0, [Validators.required, Validators.min(0.01)]],
      categorie: ['', Validators.required],
      stockActuel: [0, [Validators.required, Validators.min(0)]],
      pointCommande: [0, [Validators.required, Validators.min(0)]],
      uniteMesure: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.produitId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduit();
  }

  loadProduit() {
    this.produitService.getProduitById(this.produitId).subscribe({
      next: (produit) => {
        this.produitForm.patchValue({
          ...produit,
          uniteMesure: produit.UniteMesure // Map UniteMesure to uniteMesure
        });
      },
      error: (error) => {
        console.error('Erreur lors du chargement du produit:', error);
      }
    });
  }

  onUpdate() {
    if (this.produitForm.valid) {
      this.produitService.updateProduit(this.produitId, this.produitForm.value).subscribe({
        next: (response) => {
          console.log('Produit mis à jour:', response);
          this.router.navigate(['/produits']);
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour:', error);
        }
      });
    }
  }

  cancel() {
    this.router.navigate(['/produits']);
  }
}
```

#### 4. Stock Alert Dashboard Component

```typescript
import { Component, OnInit } from '@angular/core';
import { ProduitService } from './produit.service';
import { ProduitResponse } from './produit.models';

@Component({
  selector: 'app-stock-dashboard',
  template: `
    <h2>Tableau de Bord - Stock</h2>
    
    <div class="stats">
      <div class="stat-card">
        <h3>Total Produits</h3>
        <p class="stat-value">{{ totalProducts }}</p>
      </div>
      
      <div class="stat-card warning">
        <h3>⚠️ Stock Faible</h3>
        <p class="stat-value">{{ lowStockProducts.length }}</p>
      </div>
      
      <div class="stat-card">
        <h3>Valeur Totale Stock</h3>
        <p class="stat-value">{{ getTotalStockValue() | currency:'MAD' }}</p>
      </div>
    </div>

    <h3>Produits à Réapprovisionner</h3>
    <table *ngIf="lowStockProducts.length > 0">
      <thead>
        <tr>
          <th>Produit</th>
          <th>Stock Actuel</th>
          <th>Point Commande</th>
          <th>À Commander</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let produit of lowStockProducts">
          <td>{{ produit.nom }}</td>
          <td class="low-stock">{{ produit.stockActuel }}</td>
          <td>{{ produit.pointCommande }}</td>
          <td>{{ produit.pointCommande - produit.stockActuel }}</td>
          <td>
            <button (click)="createOrder(produit)">Commander</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div *ngIf="lowStockProducts.length === 0" class="success">
      ✓ Tous les produits ont un stock suffisant
    </div>
  `,
  styles: [`
    .stats {
      display: flex;
      gap: 20px;
      margin-bottom: 30px;
    }
    .stat-card {
      flex: 1;
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .stat-card.warning {
      background: #fff3cd;
      border-left: 4px solid #ffc107;
    }
    .stat-value {
      font-size: 2em;
      font-weight: bold;
      margin: 10px 0 0 0;
    }
    .low-stock {
      color: #dc3545;
      font-weight: bold;
    }
  `]
})
export class StockDashboardComponent implements OnInit {
  allProducts: ProduitResponse[] = [];
  lowStockProducts: ProduitResponse[] = [];
  totalProducts: number = 0;

  constructor(private produitService: ProduitService) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.produitService.getAllProduits().subscribe({
      next: (products) => {
        this.allProducts = products;
        this.totalProducts = products.length;
        this.lowStockProducts = products.filter(
          p => p.stockActuel <= p.pointCommande
        );
      },
      error: (error) => {
        console.error('Erreur lors du chargement:', error);
      }
    });
  }

  getTotalStockValue(): number {
    return this.allProducts.reduce(
      (total, product) => total + (product.stockActuel * product.prixUnitaire),
      0
    );
  }

  createOrder(produit: ProduitResponse) {
    console.log('Create order for:', produit);
    // Navigate to create order page with product pre-filled
  }
}
```

---

## Testing with Postman

### 1. Create Produit
```
POST http://localhost:8080/gestionStock/api/v1/produits
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "reference": "PROD-001",
  "nom": "Clavier Mécanique RGB",
  "description": "Clavier gaming avec rétroéclairage RGB",
  "prixUnitaire": 599.99,
  "categorie": "Informatique",
  "stockActuel": 50,
  "pointCommande": 10,
  "uniteMesure": "pcs"
}
```

### 2. Get Produit by ID
```
GET http://localhost:8080/gestionStock/api/v1/produits/5
Headers:
  Authorization: Bearer <your-token>
```

### 3. Get All Produits
```
GET http://localhost:8080/gestionStock/api/v1/produits
Headers:
  Authorization: Bearer <your-token>
```

### 4. Update Produit
```
PUT http://localhost:8080/gestionStock/api/v1/produits/5
Headers:
  Content-Type: application/json
  Authorization: Bearer <your-token>
Body (JSON):
{
  "reference": "PROD-001",
  "nom": "Clavier Mécanique RGB Pro",
  "description": "Clavier gaming professionnel",
  "prixUnitaire": 699.99,
  "categorie": "Informatique",
  "stockActuel": 45,
  "pointCommande": 15,
  "uniteMesure": "pcs"
}
```

### 5. Delete Produit
```
DELETE http://localhost:8080/gestionStock/api/v1/produits/5
Headers:
  Authorization: Bearer <your-token>
```

---

## Validation Rules Summary

| Field | Rules |
|-------|-------|
| reference | Required, cannot be blank |
| nom | Required, cannot be blank |
| description | Required, cannot be blank |
| prixUnitaire | Required, must be positive (> 0) |
| categorie | Required, cannot be blank |
| stockActuel | Required, must be >= 0 |
| pointCommande | Required, must be >= 0 |
| uniteMesure | Required, cannot be blank |

---

## Business Logic Notes

1. **Stock Management**: 
   - `stockActuel`: Current quantity in stock
   - `pointCommande`: Reorder point - when stock falls to or below this level, it's time to reorder

2. **Low Stock Alert**: 
   - Products where `stockActuel <= pointCommande` should trigger reorder alerts
   - Frontend should highlight these products for attention

3. **Price Format**: 
   - `prixUnitaire` is stored as a decimal number
   - Display in Moroccan Dirham (MAD) format in the UI

4. **Response Field Naming**: 
   - Note that the response uses `UniteMesure` (capital U) while the request uses `uniteMesure` (lowercase u)
   - Handle this mapping in your Angular service/components

5. **Required Permissions**:
   - Create: `PRODUIT_CREATE`
   - Read: `PRODUIT_READ`
   - Update: `PRODUIT_UPDATE`
   - Delete: `PRODUIT_DELETE`

6. **Response on Delete**: Returns a plain text message, not JSON

---

## Common Use Cases

### 1. Inventory Dashboard
Display total products, low stock alerts, and total inventory value.

### 2. Reorder Management
Filter and display products that need reordering (stock at or below reorder point).

### 3. Category-based Filtering
Group products by category for better organization.

### 4. Stock Updates
Update stock levels when products are received or sold.

### 5. Price Management
Update product prices and track price history.

This documentation provides everything needed to integrate the Produit API into your Angular frontend application.
