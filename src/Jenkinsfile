pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        SONAR_HOST = "http://localhost:9000"
        SONAR_TOKEN = "sqa_7637bb9fd8ffd9b2284ecdbf724965cb77170677"
        IMAGE_NAME = "springboot-demo"
        CONTAINER_NAME = "springboot-container"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/yourusername/springboot-project.git'
            }
        }

        stage('Build Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Scan') {
            steps {
                sh """
                mvn sonar:sonar \
                -Dsonar.projectKey=springboot-demo \
                -Dsonar.host.url=${SONAR_HOST} \
                -Dsonar.login=${SONAR_TOKEN}
                """
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