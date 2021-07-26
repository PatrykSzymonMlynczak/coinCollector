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
                    bat "${scannerHome}/bin/sonar-scanner -D sonar.host.url=http://http://192.168.99.100:9000 -D sonar.login=admin -D sonar.password=admin -D sonar.projectKey=coinCollector -D sonar.sources=. -D sonar.java.binaries=build/classes"
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
