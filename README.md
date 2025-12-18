# Flashcard Manager

## Database setup

This project uses PostgreSQL and `psql` to set up the database schema, sample data, and views used for testing.

### Prerequisites
- PostgreSQL installed
- `psql` available in PATH

**Default PostgreSQL credentials:**
- user: postgres
- password: 123
- port: 5432


---

### Run  database setup

#### Step 1: Create the database

```bash
psql -U postgres -p 5432 -c "CREATE DATABASE flashcarddb;"
```

#### Step 2: Initialize schema, data, and views

```bash
psql -U postgres -p 5432 -d flashcarddb -f src/main/resources/db/runsetup.sql
```

This script ask for a password, input '123'. If sucessful it will:
- Reset the database
- Create all tables
- Provide some sample data


---

### Important note

⚠️ **Warning:** Running `runsetup.sql` will reset all existing data in the database.

---

### Verification

If the setup was successful, you should see output from test queries such as:
- `SELECT * FROM Users;`
- `SELECT * FROM Decks;`

These are included at the end of the setup script for quick verification.


