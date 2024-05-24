# Zoo RESTful API

Eine RESTful API für die Verwaltung von Tieren und ihren Pflegern in einem Zoo.

## Beschreibung

Für das Abschlussprojekt in M295 habe ich eine RESTful API für eine Datenbank meiner Wahl erstellt.
Meine Datenbank Zoo besteht aus zwei Tabellen, Animal und Keeper, die in einer 1-n-Beziehung stehen.
Mit meiner Anwendung kann ich die Datenbank über die Endpoints Lesen, Schreiben, Bearbeiten und Löschen verwalten.

## Visuals

### Datenbankdiagramm

- ![Datenbankdiagramm](../../media/Zoo_DatabaseDiagram.png)

### Klassendiagramm

- ![Klassendiagramm](../../media/Zoo_ClassDiagram.png)

### Screenshot der Testdurchführung

- ![Screenshot](Link-zum-Bild)

## Validierungsregeln

Die folgenden Validierungsregeln wurden für die Entitäten definiert:

### Tabelle Animal

- Name: `name`
    - Regel: Darf maximal 100 Stellen lang sein und darf nicht null sein.
- Name: `species`
    - Regel: Darf maximal 100 Stellen lang sein und darf nicht null sein.
- Name: `dateAcquired`
    - Regel: Muss ein Datum Heute oder in der Vergangenheit sein.
- Name: `weight`
    - Regel: Muss eine Kommazahl sein.
- Name: `habitat`
    - Regel: Darf maximal 100 Stellen lang sein.

### Tabelle Keeper

- Name: `firstname`
    - Regel: Darf maximal 50 Stellen lang sein und darf nicht null sein.
- Name: `lastname`
    - Regel: Darf maximal 50 Stellen lang sein und darf nicht null sein.

## Berechtigungsmatrix

| Rolle                 | Endpoint                   | Aktion                                             |
|-----------------------|----------------------------|----------------------------------------------------|
| User, Employee, Admin | animals/get/{animalId}     | Ein Tier anhand seiner Id abrufen                  |
| User, Employee, Admin | animals/exists/{animalId}  | Überprüfen, ob ein Tier anhand seiner Id existiert |
| User, Employee, Admin | animals/getAll             | Alle Tiere abrufen                                 |
| User, Employee, Admin | animals/filter/date/{date} | Tiere anhand eines Datumsfilters abrufen           |
| User, Employee, Admin | animals/filter/text/{text} | Tiere anhand eines Textfilters abrufen             |
| User, Employee, Admin | animals/count              | Anzahl der Tiere abrufen                           |
| Admin                 | animals/createTables       | Tabellen für den Zoo erstellen                     |
| Employee, Admin       | animals/create             | Ein einzelnes Tier erstellen                       |
| Employee, Admin       | animals/createMultiple     | Mehrere Tiere erstellen                            |
| Employee, Admin       | animals/update/{animalId}  | Ein Tier aktualisieren                             |
| Admin                 | animals/delete/{animalId}  | Ein Tier anhand seiner Id löschen                  |
| Admin                 | animals/deleteAll          | Alle Tiere löschen                                 |

## OpenAPI Dokumentation der Services (Resourcen)

```yaml
openapi: 3.0.0
info:
  title: Zoo API
  description: API für die Verwaltung von Tieren und ihren Pflegern in einem Zoo.
  version: 1.0.0
servers:
  - url: 'http://localhost:8080/webapp/resources/animals'
paths:
  /get/{animalId}:
    get:
      summary: Ein Tier anhand seiner Id abrufen
      parameters:
        - name: animalId
          in: path
          required: true
          description: Die eindeutige Id des Tiers
          schema:
            type: integer
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Animal'
        '404':
          description: Tier nicht gefunden
  /exists/{animalId}:
    get:
      summary: Überprüfen, ob ein Tier anhand seiner Id existiert
      parameters:
        - name: animalId
          in: path
          required: true
          description: Die eindeutige Id des Tiers
          schema:
            type: integer
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                type: boolean
        '500':
          description: Interner Serverfehler
  /getAll:
    get:
      summary: Alle Tiere abrufen
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Animal'
        '204':
          description: Keine Tiere gefunden
        '500':
          description: Interner Serverfehler
  /filter/date/{date}:
    get:
      summary: Tiere anhand eines Datumsfilters abrufen
      parameters:
        - name: date
          in: path
          required: true
          description: Datum im Format YYYY-MM-DD
          schema:
            type: string
            format: date
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Animal'
        '204':
          description: Keine Tiere gefunden
        '500':
          description: Interner Serverfehler
  /filter/text/{text}:
    get:
      summary: Tiere anhand eines Textfilters abrufen
      parameters:
        - name: text
          in: path
          required: true
          description: Text zur Filterung von Namen, Arten oder Lebensräumen
          schema:
            type: string
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Animal'
        '204':
          description: Keine Tiere gefunden
        '500':
          description: Interner Serverfehler
  /count:
    get:
      summary: Anzahl der Tiere abrufen
      responses:
        '200':
          description: Erfolgreiche Abfrage
          content:
            application/json:
              schema:
                type: integer
        '500':
          description: Interner Serverfehler
  /createTables:
    post:
      summary: Tabellen für den Zoo erstellen (nur für Administratoren)
      security:
        - basicAuth: [ ]
      responses:
        '200':
          description: Tabellen erfolgreich erstellt
          content:
            text/plain:
              schema:
                type: string
        '500':
          description: Interner Serverfehler
  /create:
    post:
      summary: Ein einzelnes Tier erstellen (nur für Administratoren und Angestellte)
      security:
        - basicAuth: [ ]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AnimalRequest'
      responses:
        '201':
          description: Tier erfolgreich erstellt
        '400':
          description: Ungültige Anforderung
        '500':
          description: Interner Serverfehler
  /createMultiple:
    post:
      summary: Mehrere Tiere erstellen (nur für Administratoren und Angestellte)
      security:
        - basicAuth: [ ]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: array
              items:
                $ref: '#/components/schemas/AnimalRequest'
      responses:
        '201':
          description: Tiere erfolgreich erstellt
        '400':
          description: Ungültige Anforderung
        '500':
          description: Interner Serverfehler
  /update/{animalId}:
    put:
      summary: Ein Tier aktualisieren (nur für Administratoren und Angestellte)
      security:
        - basicAuth: [ ]
      parameters:
        - name: animalId
          in: path
          required: true
          description: Die eindeutige Id des zu aktualisierenden Tiers
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AnimalRequest'
      responses:
        '200':
          description: Tier erfolgreich aktualisiert
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Animal'
        '400':
          description: Ungültige Anforderung
        '404':
          description: Tier nicht gefunden
        '500':
          description: Interner Serverfehler
  /delete/{animalId}:
    delete:
      summary: Ein Tier anhand seiner Id löschen (nur für Administratoren)
      security:
        - basicAuth: [ ]
      parameters:
        - name: animalId
          in: path
          required: true
          description: Die eindeutige Id des zu löschenden Tiers
          schema:
            type: integer
      responses:
        '204':
          description: Tier erfolgreich gelöscht
        '404':
          description: Tier nicht gefunden
        '500':
          description: Interner Serverfehler
  /deleteAll:
    delete:
      summary: Alle Tiere löschen (nur für Administratoren)
      security:
        - basicAuth: [ ]
      responses:
        '204':
          description: Alle Tiere erfolgreich gelöscht
        '404':
          description: Keine Tiere gefunden
        '500':
          description: Interner Serverfehler
components:
  securitySchemes:
    basicAuth:
      type: http
      scheme: basic
  schemas:
    Animal:
      type: object
      properties:
        animalId:
          type: integer
        name:
          type: string
        species:
          type: string
        dateAcquired:
          type: string
          format: date
        weight:
          type: string
          format: decimal
        habitat:
          type: string
        isEndangered:
          type: boolean
        keeper:
          $ref: '#/components/schemas/Keeper'
    Keeper:
      type: object
      properties:
        keeperId:
          type: integer
        firstname:
          type: string
        lastname:
          type: string
    AnimalRequest:
      type: object
      properties:
        name:
          type: string
        species:
          type: string
        dateAcquired:
          type: string
          format: date
        weight:
          type: string
          format: decimal
        habitat:
          type: string
        isEndangered:
          type: boolean
        keeperId:
          type: integer
      required:
        - name
        - species
```

## Voraussetzungen

Damit das Projekt für Sie funktioniert, müssen Sie folgende Dinge beachten

### Requests senden
- Damit ihre requests mit Basic Auth funktionieren, verwenden sie folgenden User:
  - "admin" & "0000"
  - "employee" & "1111"

### Datenbank

- Erstellen sie die folgenden Datenbank. Bei bedarf auch die folgenden Testdaten
```sql
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema zoo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `zoo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `zoo` ;

-- -----------------------------------------------------
-- Table `zoo`.`keeper`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zoo`.`keeper` (
  `keeperId` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(50) NOT NULL,
  `lastname` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`keeperId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `zoo`.`animal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zoo`.`animal` (
  `animalId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `species` VARCHAR(100) NOT NULL,
  `dateAcquired` DATE NULL DEFAULT NULL,
  `weight` DECIMAL(10,2) NULL DEFAULT NULL,
  `habitat` VARCHAR(100) NULL DEFAULT NULL,
  `isEndangered` TINYINT(1) NULL DEFAULT NULL,
  `keeperId` INT NULL DEFAULT NULL,
  PRIMARY KEY (`animalId`),
  INDEX `keeperId` (`keeperId` ASC) VISIBLE,
  CONSTRAINT `animal_ibfk_1`
    FOREIGN KEY (`keeperId`)
    REFERENCES `zoo`.`keeper` (`keeperId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


USE zoo;

-- Sample data for `zoo`.`keeper` table
INSERT INTO `zoo`.`keeper` (`firstname`, `lastname`) VALUES
                                                       ('John', 'Smith'),
                                                       ('Emily', 'Johnson'),
                                                       ('Michael', 'Williams'),
                                                       ('Jessica', 'Jones');

-- Sample data for `zoo`.`animal` table
INSERT INTO `zoo`.`animal` (`name`, `species`, `dateAcquired`, `weight`, `habitat`, `isEndangered`, `keeperId`) VALUES
                                                                                                                  ('Leo', 'Lion', '2024-05-05', 200.50, 'Savanna', 1, 1),
                                                                                                                  ('Milo', 'Elephant', '2024-05-04', 1500.75, 'Jungle', 0, 2),
                                                                                                                  ('Bella', 'Tiger', '2024-05-03', 180.25, 'Rainforest', 1, 1),
                                                                                                                  ('Charlie', 'Giraffe', '2024-05-02', 900.00, 'Savanna', 0, 3),
                                                                                                                  ('Luna', 'Panda', '2024-05-01', 300.00, 'Bamboo forest', 1, 2);
```

## Autor

- Name: Tiago León Steinemann
- E-Mail: [steinemannt@bzz.ch](mailto:steinemannt@bzz.ch)
- GitHub: [steinemannt-bzz](https://github.com/steinemannt-bzz)

## Zusammenfassung

Alles in allem hat dieses Projekt viel Spass gemacht, auch wenn ich nicht bei bester Gesundheit war ;)
Mein größtes Interesse galt der Fehlerbehandlung, Validierung und Sicherheit.
Alle Endpoints wurden ausgiebig getestet und dokumentiert.
