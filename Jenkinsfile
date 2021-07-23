pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh "gradle clean build"
            }
        }
        stage('Test'){
            steps {
                sh "gradle test "
            }
        }
    }
}
