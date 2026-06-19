# Execution Plan Schema

The execution plan is the contract between the orchestrator and the Jenkins shared library.

## Minimal shape

| Field | Meaning |
| --- | --- |
| executionId | Orchestrator execution identifier |
| stages | Ordered list of planned stages |
| params | Stage-specific execution parameters |

## Example

```json
{
  "executionId": "example-123",
  "stages": [
    { "name": "GkeCreateNamespace", "status": "PLANNED" }
  ]
}
```
