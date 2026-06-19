# Runtime Agents

## Current runtime

Jenkins is the current StyxCD execution runtime.

It receives an execution id, retrieves the plan from the orchestrator, and executes stage implementations.

## Runtime agent contract

Runtime agents should:

- retrieve a plan from the orchestrator
- execute the stages in order
- emit lifecycle callbacks
- emit structured logs
- preserve stage correlation fields
- fail clearly and report useful diagnostics

Runtime agents should not:

- parse high-level YAML intent
- decide global workflow shape
- invent extra user-visible stages at runtime

## Future runtime model

Future agents may include Kubernetes-native workers, queue consumers, or specialized deployment workers. They should still consume the same execution plan contract where possible.
