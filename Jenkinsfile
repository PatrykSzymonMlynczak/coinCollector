pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "Gradle"
    }

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
