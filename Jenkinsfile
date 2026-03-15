pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        SONAR_HOST = "http://host.docker.internal:9000"
        IMAGE_NAME = "inventory-backend"
        CONTAINER_NAME = "inventory-container"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'feature/purchase-admin',
                url: 'https://github.com/sk14092001/inventory_backend.git'
            }
        }

        stage('Build Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=inventory-backend'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Run Container') {
            steps {
                sh """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true
                docker run -d -p 8081:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}
                """
            }
        }
    }
}