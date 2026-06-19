# Jenkins Planner Legacy Reference

This directory contains the old Jenkins-side planning logic.

It has been removed from the active Jenkins runtime path because planning now belongs to the StyxCD orchestrator.

Keep this code temporarily as reference while re-implementing workflow planning and stage parameter generation in the orchestrator.

Jenkins runtime responsibilities now:
- receive STYXCD_REQUEST
- fetch execution plan from orchestrator
- execute stages
- report callback/logs

Orchestrator responsibilities now:
- parse YML
- validate workflow intent
- build execution plan
- persist execution state
- expose execution plan to Jenkins