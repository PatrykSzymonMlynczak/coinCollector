pipeline {
    agent any

    stages {

        stage('Build') {
            agent {
                docker {
                    image 'openjdk:11'
                    args '-v "$PWD":/app'
                    reuseNode true
                }
            }
            steps {
                bat './gradlew clean build'
            }
        }
        stage('Test') {
                steps {
                    bat './gradlew test'
                }
            }
    }
}
