pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
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
                sh 'mvn clean test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                echo "Generating JaCoCo coverage report"
                sh 'mvn jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Running SonarQube Analysis"

                withSonarQubeEnv('MySonarQubeServer') {

                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=inventory_backend \
                    -Dsonar.projectName=inventory_backend \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                    -Dsonar.login=$SONAR_TOKEN
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
            echo 'Pipeline failed. Check logs.'
        }

        always {
            cleanWs()
        }
    }
}