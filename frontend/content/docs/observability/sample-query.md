# Sample Loki Query for Correlated StyxCD Execution Logs

This document shows how StyxCD can use Loki and Grafana to search for a specific workflow execution across Jenkins, orchestrator callbacks, stage logs, and future platform events.

The key idea is that every stage and tool participating in a StyxCD execution should emit structured logs containing the same `executionId`. That single ID becomes the correlation handle across the deployment workflow.

## Why This Matters

A StyxCD execution may involve several systems:

- StyxCD Orchestrator
- Jenkins shared library stages
- Kubernetes or cloud provider stages
- Grafana and Loki
- Future validation or RAG tooling

Without a shared correlation ID, troubleshooting requires manually jumping between systems and guessing which logs belong together.

With a shared `executionId`, the operational flow becomes:

```text
Execution ID
  -> Jenkins stage logs
  -> Structured Loki events
  -> Grafana Explore query
  -> Deployment timeline
  -> Future troubleshooting context
```

## Sample Execution ID

```text
af4842f9-3f23-4e9d-9d09-8a2c92b14661
```

## Sample Loki Query

```logql
{job="styxcd-jenkins"}
| json
| executionId="af4842f9-3f23-4e9d-9d09-8a2c92b14661"
```

## Sample Grafana Explore Link

[Open sample execution logs in Grafana](http://grafana.styxcd.com:3000/explore?schemaVersion=1&panes=%7B%22wdp%22%3A%7B%22datasource%22%3A%22efjse4j9cx0cgf%22%2C%22queries%22%3A%5B%7B%22refId%22%3A%22A%22%2C%22expr%22%3A%22%7Bjob%3D%5C%22styxcd-jenkins%5C%22%7D%5Cn%7C+json%5Cn%7C+executionId%3D%5C%22af4842f9-3f23-4e9d-9d09-8a2c92b14661%5C%22%22%2C%22queryType%22%3A%22range%22%2C%22datasource%22%3A%7B%22type%22%3A%22loki%22%2C%22uid%22%3A%22efjse4j9cx0cgf%22%7D%2C%22editorMode%22%3A%22code%22%2C%22direction%22%3A%22backward%22%7D%5D%2C%22range%22%3A%7B%22from%22%3A%22now-1h%22%2C%22to%22%3A%22now%22%7D%2C%22panelsState%22%3A%7B%22logs%22%3A%7B%22sortOrder%22%3A%22Descending%22%7D%7D%2C%22compact%22%3Afalse%7D%7D&orgId=1)

## Expected Result

The query should return structured Jenkins/StyxCD log events for the selected execution. These events can be used to reconstruct the workflow timeline, including stage start, success, failure, and diagnostic messages.

## Future Direction

This pattern should eventually support:

- Dashboard links from the StyxCD operations UI
- Per-execution Grafana links
- Jenkins build links
- Stage-specific Loki links
- Error-focused troubleshooting links
- RAG ingestion for historical deployment context

The dashboard should eventually expose a direct link from each StyxCD execution to the matching Grafana Explore view.
