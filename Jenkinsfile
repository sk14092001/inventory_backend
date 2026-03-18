pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Checking out code from Git"
                git branch: 'feature/purchase-admin',
                    url: 'https://github.com/sk14092001/inventory_backend.git'
            }
        }

        stage('Build and Test') {
            steps {
                echo "Building project and running tests"
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Running SonarQube analysis"

                withSonarQubeEnv('MySonarQubeServer') {

                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=inventory_backend \
                    -Dsonar.projectName=inventory_backend \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """

                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo "Waiting for SonarQube Quality Gate"

                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check logs.'
        }

        always {
            cleanWs()
        }
    }
}