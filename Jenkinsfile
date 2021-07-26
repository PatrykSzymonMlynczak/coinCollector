pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }
        stage('SonarQube Analysis') {
          environment {
            SCANNER_HOME = tool 'SonarQube'
          }
            steps{
                withSonarQubeEnv() {
                  bat "./gradlew sonarqube"
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
