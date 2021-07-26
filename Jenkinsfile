pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }
        stage('SonarQube Analysis') {
        withSonarQubeEnv() {
          bat "./gradlew sonarqube"
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
