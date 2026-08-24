# IDoc Parser

A service that parses SAP IDoc definitions (WE60 exports) into standard **JSON Schema (draft-07)** documents.

Feed it a fixed-length IDoc definition (`BEGIN_IDOC` / `BEGIN_SEGMENT` / `BEGIN_FIELDS`) and it generates a complete JSON Schema — including segment hierarchy, field types/lengths, groups, and optional schema metadata (specification, version, document type, etc.).

## Features

- Parses SAP WE60 IDoc definitions into JSON Schema draft-07
- Supports flat and grouped segment structures (`LOOPMAX` > 1 → `"type": "array"`)
- Optional schema metadata projection (id, version, specification, spec version, customer name, …)
- XSD projection validation with detailed error output
- Inline IDoc execution endpoint for quick testing
- Built-in web UI for interactive parsing
- Health and config-schema endpoints

## Requirements

- **Java 25** (mandatory)
- Maven 3.9+

## Getting Started

```bash
# build the multi-module project
mvn clean install

# start in dev mode (hot reload + web UI)
cd quarkus-app
mvn quarkus:dev
```

The service starts on **http://localhost:8000**.

## Web UI

Open http://localhost:8000 in your browser:

- Paste a WE60 export, upload a `.txt` file, or load one of the bundled samples
- Optionally fill in schema metadata fields (fetched live from the service)
- Generate the schema and compare input/output side by side
- Copy the resulting JSON Schema with one click

## REST API

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/parser-service/execute` | Parse an IDoc from a JSON body (`{ "input": "...", "schemaMetadata": { ... } }`) |
| `POST` | `/api/parser-service/parse` | Parse an uploaded IDoc file (`multipart/form-data`, field `file`) |
| `GET`  | `/api/parser-service/health` | Service health + version info |
| `GET`  | `/api/parser-service/config-schema` | Supported metadata fields and their descriptions |

Example:

```bash
curl -X POST http://localhost:8000/api/parser-service/parse \
  -F "file=@samples/ORDERS05-basic.txt" \
  -F "specification=IDoc" \
  -F "specVersion=ORDERS05"
```

## Project Structure

```
idoc-parser/
├── core/           # Pure Java parsing logic (no framework dependencies)
├── quarkus-app/    # REST API + web UI (Quarkus)
├── samples/        # Sample WE60 IDoc files + API test scripts
└── screenshots/    # Local screenshots (not committed)
```

## Testing

```bash
mvn test                # run all tests
```

Tests gracefully skip when sample files are not present.
