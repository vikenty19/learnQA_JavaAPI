pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh '''
                docker run --rm \
                  -v $(pwd):/app \
                  -w /app \
                  maven:3.8.4-openjdk-17 \
                  mvn clean compile
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                docker run --rm \
                  -v $(pwd):/app \
                  -w /app \
                  maven:3.8.4-openjdk-17 \
                  mvn test -DincludeTags=smoke
                '''
            }
        }
    }
}