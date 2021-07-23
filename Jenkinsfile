pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                bat './gradlew test'
            }
        }
        stage('deploy') {
            steps {
                bat './gradlew heroku:deploy'
            }
        }
    }
}
