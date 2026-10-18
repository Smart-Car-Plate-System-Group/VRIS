# Smart Car Plate Identification System (VRIS)

A Java 17+ Swing prototype based on the supplied project-design PDF.

## Run on Windows
1. Install **Java JDK 17 or later**, including `javac`.
2. Extract this ZIP.
3. Double-click `run.bat`. Enter a plate number and select **SEARCH VEHICLE**.

## Run from terminal
```bash
javac -encoding UTF-8 SmartCarPlateSystem.java
java SmartCarPlateSystem
```

For command-line demonstration (also works on headless machines):
```bash
java SmartCarPlateSystem --lookup "ND 123 GP"
```

## Sample plate numbers
- `ND 123 GP`
- `CA 456 WC`
- `KZN 789 EC`

Records are stored in `vehicles.csv`, created automatically on first run in the program's working folder. The simple CSV contains nine comma-separated columns (see its header). It is suitable for demo records but **not secure storage** for real owner data.

## Scope and limitations
- Shows make, model, year, colour, fictional owner name and contact, registration status, and licence expiry by entered plate.
- No actual government database connectivity, camera/OCR, login, SMS, or PostgreSQL integration.
- Fictional identities only. No real people's personal data.
- Before deployment, add server-side authentication, proper access control, audited searches, an encrypted database, authorised data source, and POPIA safeguards.
