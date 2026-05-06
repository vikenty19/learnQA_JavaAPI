pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                sh '''
                docker run --rm \
                  -u $(id -u):$(id -g) \
                  -v $(pwd):/app \
                  -w /app \
                  maven:3.8.4-openjdk-17 \
                  mvn clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                docker run --rm \
                  -u $(id -u):$(id -g) \
                  -v $(pwd):/app \
                  -w /app \
                  maven:3.8.4-openjdk-17 \
                  mvn test -DincludeTags=smoke
                '''
            }
        }
    }
}