pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }
        stage('Sonarqube') {
            environment {
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=coinCollector -Dsonar.sources=. -Dsonar.java.binaries=/target/classes"
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Test') {
            steps {
                bat './gradlew test'
            }
        }
        stage('deploy') {
            steps {
                bat './gradlew build deployHeroku'
            }
        }
    }
}
