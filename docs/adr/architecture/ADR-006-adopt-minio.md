# ADR-0002 - Adopt MinIO for Local Object Storage

**Date:** 2026-08-17
**Status:** Accepted
**Author:** Pedro Tomáz

---

## Context

The Marketplace module requires file storage for resources such as Reward images.

The application should not be directly coupled to a specific storage provider, since the storage solution used during development and testing may differ from the one used in production.

For local development and testing, the project needs an object storage solution that can be executed locally and integrated with the application through Docker.

---

## Decision

The project will adopt **MinIO** as the object storage solution for development and testing environments.

File storage will be abstracted through the `FileStorage` interface in the application layer, keeping the Marketplace module independent from the storage provider.

The MinIO-specific implementation will remain in the infrastructure layer.

For production, **Supabase Storage** will be used as the storage provider while maintaining the same `FileStorage` abstraction.

The `Reward` entity will store only the reference to the stored image rather than the image itself.

---

## Rationale

This approach was chosen because it:

* Provides local object storage for development and testing.
* Allows MinIO to run through Docker.
* Prevents the application from being coupled to a specific storage provider.
* Allows different storage providers to be used in different environments.
* Keeps storage infrastructure separated from application and domain logic.
* Avoids storing image binaries directly in the PostgreSQL database.

---

## Consequences

### Positive

* Local development without requiring an external storage service.
* Reproducible storage environment through Docker.
* Clear separation between application logic and storage infrastructure.
* Easier replacement of the storage provider.
* Production can use Supabase Storage without changing the application-level storage abstraction.
* Images are stored externally while the database keeps only their references.

### Negative

* Adds an additional infrastructure component to development and testing environments.
* Requires different storage configurations between development and production.
* Storage failures may affect operations involving file uploads.
* Requires handling potential inconsistencies between database persistence and file storage.
