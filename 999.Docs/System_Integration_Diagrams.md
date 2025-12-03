# System Integration Diagrams
## ChangeFlow, Jenkins, Harbor, ArgoCD, GitLab, GitHub

**Created:** 2025  
**Purpose:** Comprehensive diagrams showing integration between all systems

---

## Table of Contents

1. [Complete System Architecture](#1-complete-system-architecture)
2. [ChangeFlow as VCS Flow](#2-changeflow-as-vcs-flow)
3. [External Git Flow (GitLab/GitHub)](#3-external-git-flow-gitlabgithub)
4. [CI/CD Integration Flow](#4-cicd-integration-flow)
5. [Harbor Integration Flow](#5-harbor-integration-flow)
6. [ArgoCD Integration Flow](#6-argocd-integration-flow)
7. [Complete End-to-End Flow](#7-complete-end-to-end-flow)
8. [System Comparison Matrix](#8-system-comparison-matrix)

---

## 1. Complete System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    Complete Enterprise CI/CD Architecture                    │
│              ChangeFlow + Jenkins + Harbor + ArgoCD + GitLab/GitHub         │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Developer  │         │   Developer  │         │   Developer  │
└──────┬───────┘         └──────┬───────┘         └──────┬───────┘
       │                        │                        │
       │ Option 1:              │ Option 2:              │ Option 3:
       │ Check-in to            │ Push to GitLab        │ Push to GitHub
       │ ChangeFlow             │                        │
       │                        │                        │
       ▼                        ▼                        ▼
┌─────────────────┐    ┌──────────────┐         ┌──────────────┐
│   ChangeFlow    │    │   GitLab     │         │   GitHub     │
│   (Internal VCS)│    │   (External) │         │   (External) │
│                 │    │              │         │              │
│  - Source Code  │    │  - Source    │         │  - Source    │
│  - Config Files │    │    Code      │         │    Code      │
│  - Versions     │    │  - CI/CD     │         │  - Actions   │
└────────┬────────┘    └──────┬───────┘         └──────┬───────┘
         │                   │                        │
         │ Event:            │ Webhook:               │ Webhook:
         │ CheckInEvent      │ Pipeline Complete      │ Workflow Complete
         │                   │                        │
         ▼                   ▼                        ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow CI/CD Integration Service              │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Event Listener: CheckInEventListener                     │ │
│  │  Webhook Receivers:                                       │ │
│  │    - /api/v1/jenkins/webhook                             │ │
│  │    - /api/v1/gitlab/webhook                              │ │
│  │    - /api/v1/github/webhook                              │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Build Manager Service                                    │ │
│  │  - Register builds                                        │ │
│  │  - Update build status                                    │ │
│  │  - Store build artifacts                                 │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Deployment Orchestration Service                         │ │
│  │  - Kubernetes (via ArgoCD)                               │ │
│  │  - Traditional servers                                   │ │
│  │  - Multi-platform deployments                            │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────────────┬───────────────────────────────┘
                                │
                ┌───────────────┴───────────────┐
                │                                 │
                ▼                                 ▼
        ┌──────────────┐                 ┌──────────────┐
        │   Jenkins    │                 │  GitLab CI   │
        │   (Build)    │                 │  (Build)    │
        └──────┬───────┘                 └──────┬───────┘
               │                                 │
               │ Build Artifacts                  │ Build Artifacts
               │                                 │
               ▼                                 ▼
        ┌──────────────┐                 ┌──────────────┐
        │    Harbor    │                 │    Harbor    │
        │  (Container  │                 │  (Container  │
        │   Registry)  │                 │   Registry)  │
        └──────┬───────┘                 └──────┬───────┘
               │                                 │
               │ Container Images                │ Container Images
               │                                 │
               └───────────────┬─────────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Harbor Registry     │
                    │  - Docker Images      │
                    │  - Helm Charts        │
                    │  - Security Scans     │
                    └──────────┬────────────┘
                               │
                               │ Image Ready
                               │
                               ▼
                    ┌──────────────────────┐
                    │   ChangeFlow          │
                    │   Deployment Service  │
                    └──────────┬────────────┘
                               │
                ┌──────────────┴──────────────┐
                │                             │
                ▼                             ▼
        ┌──────────────┐             ┌──────────────┐
        │   ArgoCD     │             │  Traditional │
        │  (K8s GitOps)│             │   Servers    │
        └──────┬───────┘             └──────┬───────┘
               │                             │
               │ Syncs from Git              │ Direct Deploy
               │                             │
               ▼                             ▼
        ┌──────────────┐             ┌──────────────┐
        │  Kubernetes  │             │  Linux/Windows│
        │   Cluster    │             │   Servers     │
        └──────────────┘             └───────────────┘
```

---

## 2. ChangeFlow as VCS Flow

```
┌─────────────────────────────────────────────────────────────────┐
│         Flow: ChangeFlow as Version Control System             │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   Developer  │
└──────┬───────┘
       │
       │ 1. Writes code
       │
       ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ChangeFlow (Internal VCS)                    │
│  - Stores source code                                           │
│  - Manages versions                                             │
│  - Tracks changes                                               │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 2. Developer checks in code
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Event System                            │
│  - Publishes CheckInEvent                                       │
│  - Contains: RES_ID, VERSION_ID, COMMIT_ID                    │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 3. Event triggers CI/CD
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│          ChangeFlow CI/CD Integration Service                   │
│  - Checks: Does this need Jenkins build?                        │
│  - Gets CI/CD configuration                                    │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 4. Calls Jenkins API
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Jenkins Build Server                         │
│  - Receives build request                                       │
│  - Checks out code from ChangeFlow (via plugin)                │
│  - Runs build script                                            │
│  - Runs tests                                                   │
│  - Creates artifacts (JAR, WAR, etc.)                           │
│  - Builds Docker image                                          │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 5. Build completes
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Harbor Container Registry                     │
│  - Jenkins pushes Docker image                                  │
│  - Image: myapp:v1.2.3                                         │
│  - Harbor scans for vulnerabilities                            │
│  - Image tagged and stored                                      │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 6. Image ready
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              Jenkins → ChangeFlow Webhook                        │
│  POST /api/v1/jenkins/webhook                                   │
│  {                                                              │
│    "buildId": "JENKINS-123",                                    │
│    "status": "SUCCESS",                                         │
│    "artifactUrl": "harbor.company.com/myapp:v1.2.3",          │
│    "resourceId": "RES001"                                      │
│  }                                                              │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 7. ChangeFlow receives result
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Updates Database                        │
│  - Updates CF_BUILD table                                       │
│  - Status: SUCCESS                                              │
│  - Stores Harbor image URL                                     │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 8. If SUCCESS → Trigger Deployment
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Deployment Service                       │
│  - Checks deployment configuration                              │
│  - For Kubernetes: Updates Git with K8s manifests             │
│  - For Traditional: Direct deployment                          │
└────────┬────────────────────────────────────────────────────────┘
         │
         ├──────────────────┐                    ┌──────────────────┐
         │                  │                    │                  │
         ▼                  ▼                    ▼                  ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   ArgoCD     │    │  Traditional │    │     Cloud     │    │     Other    │
│  (K8s)       │    │   Servers    │    │   Platform    │    │   Platform   │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │                   │
       │ Syncs from Git    │ Direct deploy     │ Cloud API        │ Custom
       │                   │                   │                   │
       ▼                   ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Kubernetes  │    │  Linux/Windows│    │  AWS/Azure   │    │   Platform   │
│   Cluster    │    │   Servers     │    │   /GCP       │    │   Specific   │
└──────────────┘    └───────────────┘    └──────────────┘    └──────────────┘
```

---

## 3. External Git Flow (GitLab/GitHub)

```
┌─────────────────────────────────────────────────────────────────┐
│         Flow: External Git (GitLab/GitHub) as VCS               │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   Developer  │
└──────┬───────┘
       │
       │ 1. Writes code
       │
       ▼
┌─────────────────────────────────────────────────────────────────┐
│              GitLab or GitHub (External VCS)                    │
│  - Stores source code                                           │
│  - Manages Git repositories                                     │
│  - Tracks commits                                               │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 2. Developer pushes code
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│         GitLab CI or GitHub Actions (Automatically)              │
│  - Detects push event                                           │
│  - Reads .gitlab-ci.yml or .github/workflows/*.yml              │
│  - Triggers CI pipeline                                         │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 3. CI Pipeline runs
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              GitLab CI / GitHub Actions Build                    │
│  - Checks out code                                              │
│  - Runs build script                                            │
│  - Runs tests                                                   │
│  - Creates artifacts                                            │
│  - Builds Docker image                                          │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 4. Build completes
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Harbor Container Registry                     │
│  - CI pushes Docker image                                        │
│  - Image: myapp:v1.2.3                                         │
│  - Harbor scans for vulnerabilities                            │
│  - Image tagged and stored                                      │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 5. Image ready
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│    GitLab CI / GitHub Actions → ChangeFlow Webhook              │
│  POST /api/v1/gitlab/webhook  (or /api/v1/github/webhook)     │
│  {                                                              │
│    "pipelineId": "12345",                                       │
│    "status": "SUCCESS",                                         │
│    "commitHash": "abc123def456",                                │
│    "artifactUrl": "harbor.company.com/myapp:v1.2.3",          │
│    "projectId": "PROJ001"                                      │
│  }                                                              │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 6. ChangeFlow receives webhook
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Updates Database                        │
│  - Validates webhook signature                                  │
│  - Updates CF_BUILD table                                       │
│  - Links to ChangeFlow resource (if exists)                    │
│  - Status: SUCCESS                                              │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 7. If SUCCESS → Trigger Deployment
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Deployment Service                       │
│  - Checks deployment configuration                              │
│  - For Kubernetes: Updates Git with K8s manifests             │
│  - For Traditional: Direct deployment                          │
└────────┬────────────────────────────────────────────────────────┘
         │
         ├──────────────────┐                    ┌──────────────────┐
         │                  │                    │                  │
         ▼                  ▼                    ▼                  ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   ArgoCD     │    │  Traditional │    │     Cloud     │    │     Other    │
│  (K8s)       │    │   Servers    │    │   Platform    │    │   Platform   │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │                   │
       │ Syncs from Git    │ Direct deploy     │ Cloud API        │ Custom
       │                   │                   │                   │
       ▼                   ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Kubernetes  │    │  Linux/Windows│    │  AWS/Azure   │    │   Platform   │
│   Cluster    │    │   Servers     │    │   /GCP       │    │   Specific   │
└──────────────┘    └───────────────┘    └──────────────┘    └──────────────┘
```

---

## 4. CI/CD Integration Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    CI/CD Integration Flow                        │
│         ChangeFlow + Jenkins + GitLab CI + GitHub Actions        │
└─────────────────────────────────────────────────────────────────┘

                    ┌──────────────┐
                    │   Developer  │
                    └──────┬───────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  ChangeFlow  │    │   GitLab     │    │   GitHub     │
│  (Check-in)  │    │   (Push)     │    │   (Push)     │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       │ Event             │ Webhook           │ Webhook
       │                   │                   │
       ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow CI/CD Integration Service               │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Event Listener                                           │ │
│  │  - Listens to CheckInEvent                               │ │
│  │  - Determines if build needed                            │ │
│  │  - Calls Jenkins API                                     │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Webhook Receivers                                        │ │
│  │  - /api/v1/jenkins/webhook                               │ │
│  │  - /api/v1/gitlab/webhook                                │ │
│  │  - /api/v1/github/webhook                                │ │
│  │  - Validates signatures                                  │ │
│  │  - Updates build status                                   │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────────────┬───────────────────────────────┘
                                │
                ┌───────────────┴───────────────┐
                │                               │
                ▼                               ▼
        ┌──────────────┐               ┌──────────────┐
        │   Jenkins    │               │  GitLab CI   │
        │              │               │              │
        │  - Builds    │               │  - Builds    │
        │  - Tests     │               │  - Tests     │
        │  - Creates   │               │  - Creates   │
        │    artifacts │               │    artifacts │
        └──────┬───────┘               └──────┬───────┘
               │                               │
               │ Webhook: Build Result         │ Webhook: Pipeline Result
               │                               │
               └───────────────┬───────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   ChangeFlow         │
                    │   Build Manager      │
                    │  - Updates CF_BUILD  │
                    │  - Stores artifacts  │
                    └──────────┬───────────┘
                               │
                               │ If SUCCESS
                               │
                               ▼
                    ┌──────────────────────┐
                    │   ChangeFlow         │
                    │   Deployment Trigger │
                    └──────────┬───────────┘
                               │
                               ▼
                    [Continue to Deployment Flow]
```

---

## 5. Harbor Integration Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    Harbor Container Registry Integration         │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   Build      │
│   System     │
│ (Jenkins/    │
│  GitLab CI/  │
│  GitHub)     │
└──────┬───────┘
       │
       │ 1. Build completes
       │    Creates Docker image
       │
       ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Docker Image Created                          │
│  - Image: myapp:latest                                          │
│  - Tagged: myapp:v1.2.3                                        │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 2. Push to Harbor
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Harbor Container Registry                     │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Image Storage                                            │ │
│  │  - Stores Docker images                                   │ │
│  │  - Manages image versions                                 │ │
│  │  - Example: harbor.company.com/myproject/myapp:v1.2.3    │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Security Scanning                                        │ │
│  │  - Scans for vulnerabilities                             │ │
│  │  - CVE detection                                          │ │
│  │  - Security reports                                       │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Image Signing                                            │ │
│  │  - Content trust                                          │ │
│  │  - Notary integration                                     │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Replication                                              │ │
│  │  - Multi-registry sync                                   │ │
│  │  - Geographic distribution                                │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 3. Image ready and scanned
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Integration                              │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Harbor API Client                                        │ │
│  │  - GET /api/v2.0/projects/{project}/repositories         │ │
│  │  - GET /api/v2.0/projects/{project}/repositories/.../   │ │
│  │      artifacts/{reference}/scan                          │ │
│  │  - Checks image scan status                              │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Database Storage                                         │ │
│  │  - CF_BUILD.ARTIFACT_URL =                               │ │
│  │    harbor.company.com/myapp:v1.2.3                      │ │
│  │  - CF_DEPLOYMENT.HARBOR_IMAGE_URL                        │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Deployment Integration                                  │ │
│  │  - Pulls image from Harbor                               │ │
│  │  - Uses image URL in deployment                          │ │
│  │  - Tracks which image is deployed where                  │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 4. Image used in deployment
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              Deployment Targets                                  │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐    │
│  │  Kubernetes  │    │  Docker      │    │  Cloud       │    │
│  │  (via ArgoCD)│    │  Swarm       │    │  Platforms   │    │
│  │              │    │              │    │              │    │
│  │  Pulls from  │    │  Pulls from  │    │  Pulls from  │    │
│  │  Harbor      │    │  Harbor      │    │  Harbor      │    │
│  └──────────────┘    └──────────────┘    └──────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. ArgoCD Integration Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    ArgoCD GitOps Integration                     │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   ChangeFlow │
│   Deployment │
│   Service    │
└──────┬───────┘
       │
       │ 1. Deployment triggered for Kubernetes
       │
       ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Updates Git Repository                   │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Git Repository (GitLab/GitHub)                            │ │
│  │  - Contains Kubernetes manifests                          │ │
│  │  - deployment.yaml                                        │ │
│  │  - service.yaml                                           │ │
│  │  - configmap.yaml                                         │ │
│  │  - Updated with new image:                                │ │
│  │    image: harbor.company.com/myapp:v1.2.3               │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 2. Git updated
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ArgoCD Application                            │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  ArgoCD Configuration                                     │ │
│  │  - Source: Git repository                                 │ │
│  │  - Path: /k8s/manifests                                  │ │
│  │  - Destination: Kubernetes cluster                        │ │
│  │  - Sync Policy: Auto/Manual                              │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  ArgoCD Sync Process                                      │ │
│  │  - Monitors Git repository                                │ │
│  │  - Detects changes                                        │ │
│  │  - Compares Git vs K8s state                             │ │
│  │  - Syncs changes to Kubernetes                            │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  ArgoCD API Integration                                   │ │
│  │  - ChangeFlow can trigger sync via API                    │ │
│  │  - Monitor deployment status                              │ │
│  │  - Get application health                                 │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 3. ArgoCD syncs to Kubernetes
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              Kubernetes Cluster                                 │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Deployment Created/Updated                              │ │
│  │  - Pulls image from Harbor                               │ │
│  │  - Creates pods                                          │ │
│  │  - Updates service                                       │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Application Running                                      │ │
│  │  - Pods running                                           │ │
│  │  - Service exposed                                        │ │
│  │  - Health checks passing                                  │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ 4. Status reported back
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Updates Status                           │
│  - ArgoCD reports status via API                                │
│  - ChangeFlow updates CF_DEPLOYMENT table                      │
│  - Deployment status: SUCCESS/FAILED                           │
│  - Rollback if needed                                          │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7. Complete End-to-End Flow

```
┌─────────────────────────────────────────────────────────────────┐
│              Complete End-to-End Flow                            │
│    Developer → Code → Build → Registry → Deploy → Monitor       │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   Developer  │
└──────┬───────┘
       │
       │ Step 1: Write code
       │
       ▼
┌─────────────────────────────────────────────────────────────────┐
│              Source Code Management                              │
│                                                                 │
│  Option A: ChangeFlow VCS          Option B: External Git       │
│  ┌──────────────┐                ┌──────────────┐              │
│  │  ChangeFlow  │                │  GitLab/     │              │
│  │  (Internal)  │                │  GitHub      │              │
│  └──────┬───────┘                └──────┬───────┘              │
│         │                                │                      │
│         │ Check-in                       │ Push                 │
│         │                                │                      │
│         └──────────────┬─────────────────┘                      │
│                        │                                        │
│                        ▼                                        │
│         ┌──────────────────────────────────┐                   │
│         │  ChangeFlow Event/Webhook        │                   │
│         └────────┬─────────────────────────┘                   │
└──────────────────┼─────────────────────────────────────────────┘
                   │
                   │ Step 2: Trigger Build
                   │
                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CI/CD Build Systems                          │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐    │
│  │   Jenkins    │    │  GitLab CI   │    │  GitHub      │    │
│  │              │    │              │    │  Actions     │    │
│  │  - Builds    │    │  - Builds    │    │  - Builds    │    │
│  │  - Tests     │    │  - Tests     │    │  - Tests     │    │
│  │  - Creates   │    │  - Creates   │    │  - Creates   │    │
│  │    artifacts │    │    artifacts │    │    artifacts │    │
│  │  - Builds    │    │  - Builds    │    │  - Builds    │    │
│  │    Docker    │    │    Docker    │    │    Docker    │    │
│  │    image     │    │    image     │    │    image     │    │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘    │
│         │                   │                   │            │
│         └───────────┬───────┴───────┬───────────┘            │
│                     │               │                        │
│                     │ Step 3: Push Image                      │
│                     │                                         │
└─────────────────────┼─────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Harbor Container Registry                     │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Image Storage & Management                              │ │
│  │  - Stores: myapp:v1.2.3                                  │ │
│  │  - Security scanning                                     │ │
│  │  - Image signing                                         │ │
│  │  - Version management                                    │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Harbor API                                               │ │
│  │  - ChangeFlow queries scan status                        │ │
│  │  - Gets image metadata                                   │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ Step 4: Build Complete Webhook
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow CI/CD Integration                      │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Webhook Receivers                                        │ │
│  │  - /api/v1/jenkins/webhook                               │ │
│  │  - /api/v1/gitlab/webhook                                │ │
│  │  - /api/v1/github/webhook                                │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Build Manager                                            │ │
│  │  - Updates CF_BUILD table                                 │ │
│  │  - Stores Harbor image URL                               │ │
│  │  - Status: SUCCESS/FAILURE                               │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Deployment Trigger                                       │ │
│  │  - Checks if auto-deploy enabled                         │ │
│  │  - Gets deployment configuration                         │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ Step 5: Deployment Decision
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Deployment Orchestration                │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Deployment Configuration                                │ │
│  │  - Target: Kubernetes / Traditional / Cloud             │ │
│  │  - Environment: Dev / Test / Staging / Prod              │ │
│  │  - Auto-deploy: Yes / No                                │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│         ┌──────────────────┐                    ┌────────────┐
│         │                  │                    │            │
│         ▼                  ▼                    ▼            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Kubernetes  │  │  Traditional │  │   Cloud      │      │
│  │  Deployment  │  │  Deployment  │  │   Platform    │      │
│  │              │  │              │  │              │      │
│  │  Via ArgoCD  │  │  Direct SSH  │  │  AWS/Azure   │      │
│  │  GitOps      │  │  /FTP/etc    │  │  /GCP API     │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                 │              │
│         └─────────┬───────┴─────────────────┘              │
│                   │                                         │
└───────────────────┼─────────────────────────────────────────┘
                    │
                    │ Step 6: Deploy
                    │
                    ▼
┌─────────────────────────────────────────────────────────────────┐
│              Kubernetes Deployment (via ArgoCD)                  │
│                                                                 │
│  ┌──────────────┐                                              │
│  │  ChangeFlow  │                                              │
│  │  Updates Git │                                              │
│  │  with K8s    │                                              │
│  │  manifests   │                                              │
│  └──────┬───────┘                                              │
│         │                                                       │
│         │ Git updated                                          │
│         │                                                       │
│         ▼                                                       │
│  ┌──────────────┐                                              │
│  │   ArgoCD     │                                              │
│  │  - Monitors  │                                              │
│  │    Git       │                                              │
│  │  - Syncs to  │                                              │
│  │    K8s       │                                              │
│  └──────┬───────┘                                              │
│         │                                                       │
│         │ Syncs                                                │
│         │                                                       │
│         ▼                                                       │
│  ┌──────────────┐                                              │
│  │  Kubernetes  │                                              │
│  │  Cluster      │                                              │
│  │  - Pulls from│                                              │
│  │    Harbor    │                                              │
│  │  - Deploys    │                                              │
│  │    pods       │                                              │
│  └──────────────┘                                              │
└─────────────────────────────────────────────────────────────────┘
                    │
                    │ Step 7: Status Update
                    │
                    ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Status Tracking                          │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Database Updates                                         │ │
│  │  - CF_BUILD: Build status                                │ │
│  │  - CF_DEPLOYMENT: Deployment status                     │ │
│  │  - CF_RESOURCE: Source code versions                     │ │
│  │  - Links: Source → Build → Image → Deployment           │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  Audit Trail                                              │ │
│  │  - Who deployed what                                      │ │
│  │  - When deployed                                          │ │
│  │  - What version                                           │ │
│  │  - Deployment status                                      │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │  UI Dashboard                                             │ │
│  │  - Build status                                           │ │
│  │  - Deployment status                                      │ │
│  │  - Image information                                      │ │
│  │  - Rollback options                                       │ │
│  └──────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8. System Comparison Matrix

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    System Roles and Responsibilities                         │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
│   System         │  ChangeFlow  │   Jenkins    │    Harbor    │   ArgoCD    │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Primary Role     │ CM + Deploy  │ Build        │ Registry     │ K8s GitOps  │
│                  │ Orchestration│              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Source Code      │ ✅ Built-in  │ ❌ External  │ ❌ No        │ ❌ External │
│ Management       │              │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Build            │ ⚠️ Triggers  │ ✅ Executes  │ ❌ No        │ ❌ No       │
│                  │              │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Container        │ ❌ No        │ ✅ Creates   │ ✅ Stores    │ ❌ No       │
│ Images           │              │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Image Scanning   │ ⚠️ Queries   │ ❌ No        │ ✅ Scans     │ ❌ No       │
│                  │ Harbor API   │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ K8s Deployment   │ ✅ Orchestrates│ ❌ No      │ ❌ No        │ ✅ Syncs    │
│                  │ (via ArgoCD) │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Traditional      │ ✅ Direct    │ ❌ No        │ ❌ No        │ ❌ No       │
│ Deployment       │              │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Workflow Engine  │ ✅ Built-in  │ ⚠️ Pipelines │ ❌ No        │ ❌ No       │
│                  │              │              │              │             │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Audit Trail      │ ✅ Complete  │ ⚠️ Build logs│ ⚠️ Image     │ ⚠️ Git      │
│                  │              │              │ history      │ history     │
├──────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Multi-Platform   │ ✅ Yes       │ ⚠️ Config    │ ✅ Any       │ ❌ K8s only│
│                  │              │ dependent    │ platform     │             │
└──────────────────┴──────────────┴──────────────┴──────────────┴──────────────┘

┌──────────────────┬──────────────┬──────────────┐
│   System         │   GitLab     │   GitHub     │
├──────────────────┼──────────────┼──────────────┤
│ Primary Role     │ VCS + CI/CD  │ VCS + Actions│
├──────────────────┼──────────────┼──────────────┤
│ Source Code      │ ✅ Built-in  │ ✅ Built-in  │
│ Management       │              │              │
├──────────────────┼──────────────┼──────────────┤
│ Build            │ ✅ CI/CD     │ ✅ Actions   │
├──────────────────┼──────────────┼──────────────┤
│ Container        │ ✅ Can build │ ✅ Can build │
│ Images           │              │              │
├──────────────────┼──────────────┼──────────────┤
│ Image Scanning   │ ⚠️ Basic     │ ⚠️ Basic     │
├──────────────────┼──────────────┼──────────────┤
│ K8s Deployment   │ ⚠️ Can do    │ ⚠️ Can do    │
│                  │ via scripts  │ via scripts  │
├──────────────────┼──────────────┼──────────────┤
│ Traditional      │ ⚠️ Can do    │ ⚠️ Can do    │
│ Deployment       │ via scripts  │ via scripts  │
├──────────────────┼──────────────┼──────────────┤
│ Workflow Engine  │ ⚠️ CI/CD     │ ⚠️ Actions   │
│                  │ pipelines    │ workflows    │
├──────────────────┼──────────────┼──────────────┤
│ Audit Trail      │ ✅ Git       │ ✅ Git       │
│                  │ history      │ history      │
├──────────────────┼──────────────┼──────────────┤
│ Multi-Platform   │ ✅ Yes       │ ✅ Yes       │
└──────────────────┴──────────────┴──────────────┘
```

---

## 9. Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    Data Flow Between Systems                     │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│   Developer  │
└──────┬───────┘
       │
       │ Code
       │
       ▼
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│  ChangeFlow  │◄────────┤   GitLab     │◄────────┤   GitHub     │
│  (VCS)       │  Sync   │  (External)  │  Push   │  (External)  │
└──────┬───────┘         └──────┬───────┘         └──────┬───────┘
       │                        │                        │
       │ Event/Webhook          │ Webhook                │ Webhook
       │                        │                        │
       ▼                        ▼                        ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow CI/CD Service                           │
│  - Receives events/webhooks                                    │
│  - Triggers builds                                             │
│  - Tracks build status                                         │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ API Call / Webhook
         │
         ▼
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Jenkins    │         │  GitLab CI   │         │  GitHub      │
│              │         │              │         │  Actions     │
│  - Builds    │         │  - Builds    │         │  - Builds    │
│  - Tests     │         │  - Tests     │         │  - Tests     │
│  - Creates   │         │  - Creates   │         │  - Creates   │
│    image     │         │    image     │         │    image     │
└──────┬───────┘         └──────┬───────┘         └──────┬───────┘
       │                        │                        │
       │ Push Image              │ Push Image              │ Push Image
       │                        │                        │
       └──────────────┬─────────┴────────┬───────────────┘
                      │                  │
                      ▼                  ▼
              ┌──────────────┐
              │    Harbor    │
              │  - Stores    │
              │  - Scans     │
              │  - Manages   │
              └──────┬───────┘
                     │
                     │ Image URL
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow                                         │
│  - Receives build webhook                                       │
│  - Stores Harbor image URL                                      │
│  - Updates CF_BUILD table                                       │
└────────┬────────────────────────────────────────────────────────┘
         │
         │ Deployment Decision
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChangeFlow Deployment                              │
│                                                                 │
│  For Kubernetes:                For Traditional:               │
│  ┌──────────────┐              ┌──────────────┐              │
│  │ Updates Git  │              │ Direct Deploy│              │
│  │ with K8s     │              │              │              │
│  │ manifests    │              │ - SSH        │              │
│  └──────┬───────┘              │ - FTP        │              │
│         │                      │ - API calls  │              │
│         │ Git updated          └──────┬───────┘              │
│         │                             │                      │
│         ▼                             ▼                      │
│  ┌──────────────┐              ┌──────────────┐             │
│  │   ArgoCD     │              │  Traditional  │             │
│  │  - Syncs     │              │  Servers      │             │
│  │  - Deploys   │              │               │             │
│  └──────┬───────┘              └──────┬───────┘             │
│         │                             │                      │
│         │ Pulls from Harbor           │ Pulls from Harbor    │
│         │                             │                      │
│         ▼                             ▼                      │
│  ┌──────────────┐              ┌──────────────┐             │
│  │  Kubernetes  │              │  Application │             │
│  │  Cluster     │              │  Running     │             │
│  └──────┬───────┘              └──────┬───────┘             │
│         │                             │                      │
│         └──────────────┬──────────────┘                      │
│                        │                                      │
│                        │ Status Update                        │
│                        │                                      │
│                        ▼                                      │
│         ┌──────────────────────────────┐                     │
│         │  ChangeFlow                  │                     │
│         │  - Updates deployment status │                     │
│         │  - Tracks in database        │                     │
│         │  - Provides UI dashboard     │                     │
│         └──────────────────────────────┘                     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 10. Integration Points Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                    Integration Points                            │
└─────────────────────────────────────────────────────────────────┘

ChangeFlow ↔ Jenkins
  ├─> ChangeFlow → Jenkins: API call to trigger build
  ├─> Jenkins → ChangeFlow: Webhook with build results
  └─> ChangeFlow: Stores build status and artifacts

ChangeFlow ↔ GitLab
  ├─> GitLab → ChangeFlow: Webhook with pipeline results
  ├─> ChangeFlow → GitLab: API to update K8s manifests (for ArgoCD)
  └─> ChangeFlow: Tracks GitLab builds and deployments

ChangeFlow ↔ GitHub
  ├─> GitHub → ChangeFlow: Webhook with workflow results
  ├─> ChangeFlow → GitHub: API to update K8s manifests (for ArgoCD)
  └─> ChangeFlow: Tracks GitHub builds and deployments

ChangeFlow ↔ Harbor
  ├─> ChangeFlow → Harbor: API to check image scan status
  ├─> ChangeFlow → Harbor: API to get image metadata
  └─> ChangeFlow: Stores Harbor image URLs in database

ChangeFlow ↔ ArgoCD
  ├─> ChangeFlow → Git: Updates K8s manifests
  ├─> ChangeFlow → ArgoCD: API to trigger sync (optional)
  ├─> ArgoCD → ChangeFlow: API to get deployment status
  └─> ChangeFlow: Orchestrates K8s deployments via ArgoCD

Jenkins/GitLab CI/GitHub Actions → Harbor
  ├─> Build systems push images to Harbor
  ├─> Harbor scans images
  └─> Harbor stores and manages images

ArgoCD → Git
  ├─> ArgoCD monitors Git repository
  ├─> ArgoCD syncs Git → Kubernetes
  └─> Git is source of truth for K8s state
```

---

**Document Version:** 1.0  
**Last Updated:** 2025  
**Author:** AI Code Analysis System

