# Products Inventory Management - CI/CD Pipeline

## Overview

This project automates the deployment of a **Maven-based Java web application** that manages product inventory, with **MySQL** as the backend database. The CI/CD pipeline is built using **Jenkins**, **Docker**, **Ansible**, and **Kubernetes (K3s)**. The goal is to streamline the entire development lifecycle, from code checkout to deployment on a Kubernetes cluster.

The application is tested, built, containerized, and deployed using Ansible playbooks to a Kubernetes cluster. Jenkins handles automation, while Docker is used for containerization.

## Project Architecture & Tools

The project flow is divided into the following key stages:
1. **Source Code Management**: GitHub is used for version control.
2. **Build and Test**: Maven is used to build and test the project.
3. **Containerization**: Docker is used to package the application into a container.
4. **Deployment**: Ansible deploys the application on Kubernetes (K3s).
5. **Continuous Integration & Delivery**: Jenkins automates the entire pipeline.

### Tools & Technologies
- **Jenkins**: For CI/CD automation.
- **GitHub**: Source code repository.
- **Maven**: For building and testing the Java web application.
- **Docker**: For containerizing the application.
- **Ansible**: For automating the deployment process.
- **Kubernetes (K3s)**: For orchestrating application containers.
- **MySQL**: Database for product data storage.

---

## Prerequisites

### Hardware/Software Requirements
- Jenkins server (running on RHEL or a similar system)
- Docker installed on the Jenkins server
- Kubernetes (K3s) cluster for deployment
- Ansible installed on Jenkins (or a node accessible by Jenkins)
- Kubernetes Python library installed (```pip install kubernetes```)

---

## Project Directory Structure

```
Products-pipeline/
├── ansible/
│   ├── inventory.ini
│   └── playbooks/
│       └── deploy_k8s.yml
├── Dockerfile
├── Jenkinsfile
├── k8s/
│   ├── configmaps/
│   ├── deployments/
│   ├── pvc/
│   ├── secrets/
│   └── services/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       └── webapp/
└── README.md
```

## Project Flow

```
+-----------------------------+
| Start: Code in GitHub Repo   |
+-------------+---------------+
              |
              v
+-----------------------------+
| Checkout Source Code         |
| Jenkins: Pull from GitHub    |
+-------------+---------------+
              |
              v
+-----------------------------+
| Build & Test using Maven     |
| Jenkins: mvn clean package   |
| Jenkins: mvn test            |
+-------------+---------------+
              |
              v
+-----------------------------+
| Build Docker Image           |
| Jenkins: docker build        |
+-------------+---------------+
              |
              v
+-----------------------------+
| Push to Docker Registry      |
| Jenkins: docker push         |
+-------------+---------------+
              |
              v
+-----------------------------+
| Deploy with Ansible          |
| Run Ansible playbook         |
| ansible-playbook deploy_k8s.yml |
+-------------+---------------+
              |
              v
+-----------------------------+
| Kubernetes Deployment        |
| Deploy services to K3s       |
+-------------+---------------+
              |
              v
+-----------------------------+
| Monitoring Setup             |
| Prometheus & Grafana         |
+-------------+---------------+
              |
              v
+-----------------------------+
| End: Application Running     |
| & Monitored in Kubernetes    |
+-----------------------------+

```

## Final Notes

This project automates the deployment and monitoring of a Kubernetes-based application using modern DevOps tools like Ansible and Jenkins. By leveraging Ansible playbooks for infrastructure management and CI/CD pipelines through Jenkins, we achieve a seamless and efficient deployment process. Additionally, the integration of Prometheus and Grafana for monitoring allows for real-time insights into resource utilization, ensuring the application runs optimally in production.

Key benefits of this project include:
- **Full Automation**: From deployment to monitoring, everything is automated, reducing manual effort and potential errors.
- **Scalability**: The use of Kubernetes ensures that the application can scale based on demand, while Jenkins' master-slave setup allows for efficient resource utilization in build and deployment tasks.
- **Security**: The use of Ansible Vault for sensitive information like credentials ensures that secrets are handled securely within the CI/CD pipeline.
- **Visibility**: Grafana provides an intuitive dashboard for monitoring key metrics, ensuring visibility into the health and performance of the application at all times.

This project serves as a robust foundation for deploying and managing containerized applications in a scalable, secure, and monitored environment. By following best practices and leveraging automation, it minimizes operational overhead while maximizing reliability.
