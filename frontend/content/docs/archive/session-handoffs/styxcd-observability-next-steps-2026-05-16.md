# StyxCD Observability Next Steps

Date: 2026-05-16

## Current State

The control plane is now working end-to-end.

Next focus:

```text
Make executions visible, diagnosable, and correlated across:
- orchestrator
- Jenkins
- Grafana
- Loki
- Alloy
```

## Immediate Next Session Goals

1. Revisit Grafana/Loki/Alloy installation.
2. Confirm logs are written to persistent storage.
3. Confirm EC2 stop/start does not lose Loki data.
4. Identify current Loki data directory.
5. Move/configure Loki data path to persistent EBS-backed location if needed.
6. Update orchestrator logging to include execution correlation fields.
7. Update Jenkins callbacks/stage logs to include execution correlation fields.
8. Build Grafana queries around `executionId`.

## Persistence Validation

Check whether Grafana/Loki/Alloy are installed natively or through Docker.

Known assumption from previous work:

```text
Grafana/Loki/Alloy appear to be installed natively on Amazon Linux 2023.
```

Need to verify:

```bash
systemctl status grafana-server
systemctl status loki
systemctl status alloy
```

Find Loki config:

```bash
sudo find /etc -iname '*loki*'
sudo find / -iname 'loki*.yml' 2>/dev/null
```

Find Loki data paths:

```bash
sudo grep -R "path" /etc/loki* /etc/grafana* 2>/dev/null
```

Likely areas to inspect:

```text
/var/lib/loki
/var/lib/grafana
/etc/loki/config.yml
/etc/grafana/grafana.ini
```

## Correlation Fields

Adopt these fields as baseline:

```json
{
  "executionId": "uuid",
  "workflow": "cloud_workflow",
  "stageName": "GradleBuild",
  "stageKey": "GradleBuild@styxcd-jenkins",
  "status": "STARTED",
  "component": "orchestrator",
  "runtime": "jenkins",
  "timestamp": "2026-05-16T20:00:00Z"
}
```

Minimum fields for first observability pass:

```text
executionId
status
component
message
timestamp
```

## Components

### Orchestrator Logs

Should include:

```text
executionId
workflow
status
runner
callback events
Jenkins trigger outcome
```

### Jenkins Logs

Should include:

```text
executionId
stageName
stage status
Jenkins build URL
duration
callback result
```

### Dashboard

Eventually show:

```text
execution id
status
Jenkins build URL
Grafana logs link
execution plan link
stage history
```

## Grafana Links

Future dashboard should build direct links into Grafana using `executionId`.

Example concept:

```text
View logs for executionId=...
```

## Future Event Model

Current callback only mutates execution status.

Later, add an execution event table:

```text
execution_events
```

Possible columns:

```text
id
execution_id
event_type
status
stage_key
stage_name
component
message
payload_jsonb
created_at
```

This would support:

- full lifecycle history
- stage timeline
- dashboard event console
- replay/debugging
- audit trails
