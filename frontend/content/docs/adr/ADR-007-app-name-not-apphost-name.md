# ADR-007: Rename APPHOST_NAME to APP_NAME in the common contract

## Status
Draft / Accepted

## Context
APPHOST_NAME came from the old PCF model where the app host was used for route/DNS and certificate CN behavior unless overridden.

In the new cloud/GKE model, the YAML uses application name, and the common contract should not carry PCF-specific terminology.

## Decision
Use APP_NAME as the common application identifier.

Do not preserve APPHOST_NAME as a compatibility alias in the new common contract.

If a platform needs derived values, use explicit platform fields such as:

- DNS_HOSTNAME
- CERT_CN
- K8S_DEPLOYMENT_NAME
- K8S_SERVICE_NAME
- IMAGE_NAME

## Consequences
This is a breaking cleanup, but the project is early enough that a hard rename is preferred over safety aliases that would propagate the old naming mistake.
