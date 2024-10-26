pipeline {
    agent none

    tools {
        maven "M3"
    }

    environment {
        DOCKER_REGISTRY = 'nalghawi'
        DOCKER_IMAGE = 'products-app'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials-id'
        DEPLOY_K8S_PLAYBOOK = 'ansible/playbooks/deploy_k8s.yml'
        K8S_MONITORING_PLAYBOOK = 'ansible/playbooks/setup_monitoring.yml'
        ANSIBLE_INVENTORY_PATH = 'ansible/inventory.ini'
        GIT_REPO_URL = 'https://github.com/NALGHAWIi/masters_project.git'
        SUDO_PASSWORD = credentials('SUDO_PASSWORD')
    }

    stages {
        stage('Checkout Source Code') {
            agent { label 'built-in' }
            steps {
                git branch: 'main', url: "${env.GIT_REPO_URL}"
            }
        }

        stage('Maven Install and Test') {
            agent { label 'build' }
            steps {
                script {
                    sh 'mvn clean install -Dmaven.test.skip=true' 
                    sh 'mvn test' 
                }
            }
        }

        stage('Build') {
            agent { label 'build' }
            steps {
                script {
                    sh 'mvn clean package -Dmaven.test.skip=true' 
                }
            }
        }

        stage('Build Docker Image') {
            agent { label 'build' }
            steps {
                script {
                    dockerImage = docker.build("${env.DOCKER_REGISTRY}/${env.DOCKER_IMAGE}:latest")
                }
            }
        }

        stage('Push to Docker Registry') {
            agent { label 'deploy' }
            steps {
                script {
                    docker.withRegistry('', "${env.DOCKER_CREDENTIALS_ID}") {
                        dockerImage.push('latest')
                    }
                }
            }
        }

        stage('Deploy with Ansible') {
            agent { label 'deploy' }
            steps {
                ansiblePlaybook(
                    playbook: "${env.DEPLOY_K8S_PLAYBOOK}",
                    inventory: "${env.ANSIBLE_INVENTORY_PATH}",
                    extraVars: [
                            ansible_become_pass: "${SUDO_PASSWORD}" 
                        ],
                    extras: '-v'
                )
            }
        }

        stage('Setup Monitoring') {
            agent { label 'deploy' }
            steps {
                withCredentials([file(credentialsId: 'vault-password-id', variable: 'VAULT_PASSWORD_FILE')]) {
                    ansiblePlaybook(
                        playbook: "${env.K8S_MONITORING_PLAYBOOK}",
                        inventory: "${env.ANSIBLE_INVENTORY_PATH}",
                        extraVars: [
                            ansible_become_pass: "${SUDO_PASSWORD}" ,
                        ],
                        extras: "--vault-password-file=${VAULT_PASSWORD_FILE}"
                    )
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Deployment successful!'
        }

        failure {
            script {
                // emailext subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                //         body: "Build #${env.BUILD_NUMBER} has failed. Check the logs: ${env.BUILD_URL}",
                //         to: 'youremail@example.com'
                echo "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            }
        }

        always {
            node('built-in') {
                cleanWs()
            }
        }
    }
}
