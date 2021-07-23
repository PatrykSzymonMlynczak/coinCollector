pipeline {
    agent {docker {image 'gradle:jdk11-openj9'}}

    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
    }
}
