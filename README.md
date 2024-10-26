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

### Software Installation and Configuration

#### Install Required Tools on Jenkins Server
```bash
# Install Docker
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker

# Install Kubernetes Python library for Ansible
pip install kubernetes
```

#### Give Jenkins Access to Docker
```bash
# Add Jenkins user to the Docker group
sudo usermod -a -G docker jenkins
```

### Give Jenkins User Sudo Permission

To give the Jenkins user sudo permission, add the followng line to the sudoers file using `visudo` and add the following line:

```bash
jenkins ALL=(ALL) NOPASSWD: ALL
```

This is the recommended way to safely edit the sudoers file.

However, you can also achieve the same result programmatically by running the following command:

```bash
sudo sed -i '/^root\s\+ALL=(ALL)\s\+ALL/a jenkins ALL=(ALL) NOPASSWD: ALL' /etc/sudoers
```

---

## Setting up the CI/CD Pipeline

### Jenkins Plugins to Install
- **Git**: For source code management.
- **Maven Integration**: For building the Java application.
- **Docker Pipeline**: To integrate Docker into Jenkins jobs.
- **Ansible**: To run Ansible playbooks from Jenkins.
- **Workspace Cleanup**: To ensure a clean workspace before each build.

#### Steps to Install Plugins
1. Go to Jenkins dashboard.
2. Manage Jenkins -> Manage Plugins -> Available.
3. Search for the required plugins and install them:
   - Git
   - Maven Integration
   - Docker Pipeline
   - Ansible
   - Workspace Cleanup

## Jenkins Master-Slave Node Setup

This project uses Jenkins master-slave node distribution to optimize the build and deployment process. The master handles the orchestration, while the slave nodes handle the actual execution of jobs. Here's how to set up a new slave node:

### Steps to Add Slave Nodes:

1. **Configure a new node** in Jenkins (deploy, build):
   - Go to **Manage Jenkins** > **Manage Nodes and Clouds**.
   - Click **New Node** and fill in the configuration (node name, remote root directory, labels, etc.).
   - Set the Remote Root Directory to the path where Jenkins should store data on the slave.
   - Configure the Launch method (e.g., SSH or a JNLP agent).
   - ![alt text](image.png)
   - ![alt text](image-1.png)
   
2. **Assign jobs to specific nodes**:
   - Use the node label to distribute jobs:
     ```groovy
     node('buil') {
         // Build and deployment steps
     }
     ```
This setup ensures optimized pipeline execution by distributing tasks across different nodes, resulting in better resource utilization and load management.

## Setting Up Jenkins Credentials
![alt text](image-2.png)
### Docker Registry Credentials
1. Navigate to **Jenkins Dashboard > Manage Jenkins > Manage Credentials**.
2. Add **DockerHub credentials** (username and password).
3. Store the credentials ID as `dockerhub-credentials-id` in Jenkins.
![alt text](image-3.png)

### Sudo Password
1. Add the Sudo password as a **secret text credential** in Jenkins.
2. Store the credentials ID as `SUDO_PASSWORD`.
![alt text](image-4.png)

### Jenkins Setup for Ansible Vault

To securely use Ansible Vault in your Jenkins pipeline, follow these steps to set up a credentials file:

1. **Create a Jenkins Credential File:**
   - Go to Jenkins → Manage Jenkins → Credentials → System → Global credentials.
   - Add a new credential:
     - Kind: Secret file
     - Upload a file containing the Ansible Vault password (e.g., `vault_pass`).
     - Use a ID `ansible_vault_pass` for this credential
     ![alt text](image-5.png)

2. **Reference the Credential in Jenkins Pipeline:**

In the Jenkinsfile, use the following syntax to load the vault password from the credential:

```groovy
withCredentials([file(credentialsId: 'ansible_vault_pass', variable: 'vault_password')]) {
    ansiblePlaybook(
        playbook: "${env.ANSIBLE_PLAYBOOK_PATH}",
        inventory: "${env.ANSIBLE_INVENTORY_PATH}",
        extras: "--vault-password-file=${vault_password}"
    )
}
```

This ensures that the vault password is not exposed in the logs or hardcoded in the scripts.

---

## Project Setup

### Clone the Repository
```bash
git clone https://github.com/Musawir-ap/Products-pipeline.git
cd Products-pipeline
```

### Jenkinsfile Overview
```groovy
pipeline {
    agent { label 'abdul' }

    tools {
        maven "M3"
    }

    environment {
        // Define environment variables
        // ...
    }

    stages {
        // Checkout Source Code
        // Build and Test
        // Build Docker Image
        // Push to Docker Registry
        // Deploy with Ansible
        // Setup Monitoring
    }

    post {
        success {
            echo 'Build and Deployment successful!'
        }

        failure {
            echo "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }

        always {
            cleanWs()
        }
    }
}

```
---

## Kubernetes Setup

The application is deployed on a **K3s** Kubernetes cluster using Ansible playbooks. The deployment files for Kubernetes are located in the `k8s/` folder, which includes the following resources:
- **ConfigMaps**
- **Deployments**
- **Persistent Volume Claims (PVC)**
- **Secrets**
- **Services**

---

### Ansible Playbooks and Inventory
- **Playbook**: Located in `ansible/playbooks/deploy_k8s.yml`, used to deploy the Docker container to K3s.
- **Playbook**: Located in `ansible/playbooks/setup_monitoring.yml`, used to setup k8s monitoring using Prometheus and Grafana.
- **Inventory**: Located in `ansible/inventory.ini`.

### Ansible Vault and Secrets

The sensitive information like Grafana admin credentials and other secrets are stored securely using Ansible Vault.

Steps to add `secret.yml`:

1. Create your secrets file:

```bash
ansible-vault create ansible/playbooks/vars/secrets.yml
```

2. Add your sensitive data (e.g., Grafana credentials) inside the file and save with vault password (you need to save this to a file to set in ansible credentia).

3. Use the following command to encrypt the file:

```bash
ansible-vault encrypt ansible/playbooks/vars/secrets.yml
```

4. Ensure the playbook is set to include this secrets file:

---

## GitHub Webhook Setup for CI/CD

To trigger Jenkins builds from GitHub, set up a webhook:
1. Navigate to your GitHub repository's **Settings > Webhooks**.
2. Add a new webhook pointing to your Jenkins server.
   - Payload URL: `http://<your-jenkins-url>/github-webhook/`

### Using ngrok for GitHub Webhooks

If your Jenkins server is behind a firewall or on a local network, you can use **ngrok** to temporarily expose Jenkins for GitHub webhooks.

#### Steps to Set Up ngrok:

1. **Install ngrok** by following the [official installation guide](https://ngrok.com/download).
2. Run ngrok to expose Jenkins:
   ```bash
   ngrok http <Jenkins-Server-Port>
   ```
   Example:
   ```bash
   ngrok http 8080
   ```
   ngrok will provide a public URL (e.g., `http://abcd1234.ngrok.io`) that forwards traffic to your local Jenkins instance.
3. Set up a **GitHub webhook**:
   - Go to your GitHub repository settings.
   - Under **Webhooks**, click **Add webhook**.
   - Set the **Payload URL** to the ngrok URL followed by `/github-webhook/`, e.g.:
     ```
     http://abcd1234.ngrok.io/github-webhook/
     ```
   - Set **Content type** to `application/json` and click **Add webhook**.
   
---

## How to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Musawir-ap/Products-pipeline.git
   ```

2. **Install Dependencies**:
   - Install required Jenkins plugins.
   - Configure Docker, Ansible, and Kubernetes.
   - Set up credentials in Jenkins for Docker and Sudo.

3. **Set up Jenkins Pipeline**:
   - Create a new pipeline project in Jenkins.
   - Use the provided `Jenkinsfile` for the pipeline configuration.
   - Trigger the pipeline manually or through GitHub webhooks.

---

### Monitoring Overview

The monitoring solution is fully automated through Ansible playbooks, so no manual steps are required to set up the monitoring stack. Below is an explanation of how to access the Grafana dashboard and interact with the monitoring solution.
![alt text](image-6.png)
![alt text](image-7.png)
![alt text](image-8.png)

### Accessing Grafana Dashboard

Grafana is installed as part of the monitoring setup with a NodePort service. Since the NodePort is dynamically assigned, you can retrieve the Grafana NodePort by running the following command:

```bash
kubectl get svc -n <monitoring-namespace>
```

Example output:
```
NAME       TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)        AGE
grafana    NodePort   10.43.253.90   <none>        80:32758/TCP   10m
```

In this case, Grafana is accessible via port `32758`. You can access the Grafana dashboard using:

```
http://<node-ip>:<nodeport>
```

Example:
```
http://192.168.1.100:32758
```

You can log in with the default credentials provided during the setup.

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