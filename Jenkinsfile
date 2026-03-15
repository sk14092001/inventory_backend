pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Checking out code from Git"
                git branch: 'feature/purchase-admin',
                    url: 'https://github.com/sk14092001/inventory_backend.git'
            }
        }

        stage('Build Project') {
            steps {
                echo "Building the project using Maven"
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Running SonarQube Analysis"
                withSonarQubeEnv('SonarQubeServer') {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=inventory_backend \
                    -Dsonar.projectName=inventory_backend \
                    -Dsonar.host.url=http://localhost:9000 \
                    -Dsonar.login=sqa_e530475d252ac7fe6959e0f8249e49cee43b3663
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo "Checking SonarQube Quality Gate"
                timeout(time: 2, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
                        } else {
                            echo "Quality Gate passed: ${qg.status}"
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}