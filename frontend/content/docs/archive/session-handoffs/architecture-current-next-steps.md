# StyxCD Current Next Steps

## Validated So Far

The following has been proven:

```text
YML
  -> Orchestrator
  -> Spring/Groovy workflow planner
  -> Java stage param planners
  -> persisted execution plan
  -> Jenkins retrieves plan by executionId
  -> Jenkins execution engine runs unchanged
```

## Immediate Next Steps

### 1. Add execution metadata

Add fields such as:

```text
startedAt
endedAt
updatedAt
jenkinsJobUrl later
jenkinsBuildNumber later
```

### 2. Have orchestrator trigger Jenkins

Flow:

```text
POST /executions
  -> create execution
  -> persist plan
  -> trigger Jenkins job with executionId
```

### 3. Add temporary Jenkins callback

Endpoint:

```text
PATCH /executions/{id}/status
```

Jenkins cleanup/finally calls this with:

```text
SUCCEEDED
FAILED
```

Optionally also:

```text
RUNNING
```

near the beginning.

### 4. Continue porting `getParams(...)`

Move remaining stage parameter generation logic from Jenkins into orchestrator-side stage planner classes.

### 5. Continue porting workflow planners

Move workflow decision logic from Jenkins workflow Groovy files into orchestrator Groovy workflow planner beans.

### 6. Remove planning from Jenkins

Once equivalent orchestrator planning exists, remove:

```text
workflow planning
stage getParams
local execution map generation
```

from the Jenkins shared library.

## Near-Term Infrastructure Work

Sooner rather than later:

```text
https://orchestrator.styxcd.com
https://jenkins.styxcd.com
https://grafana.styxcd.com
```

Use nginx + TLS so stable HTTPS URLs are not repeatedly refactored.

## Observability Work

Future correlated observability:

```text
executionId as primary correlation key
orchestrator logs include executionId
Jenkins logs include executionId
Alloy collects/transforms
Loki stores
Grafana visualizes
```
