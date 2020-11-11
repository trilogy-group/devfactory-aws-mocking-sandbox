pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
        }
    }
    stages {
        stage('Build smockin') {
            steps {
                sh 'mvn -B clean package'
            }
        },
        stage('Build docker') {
            steps {
                dir ("docker") {
                    sh 'docker build -t registry.devfactory.com/devfactory/smockin-sandbox .'
                }
            }
        }
        stage('Publish docker image') {
            steps {
                dir ("docker") {
                    sh 'docker push registry.devfactory.com/devfactory/smockin-sandbox'
                }
            }
        }
    }
}