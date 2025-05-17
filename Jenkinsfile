pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = 'docker-cred'
        IMAGE_PREFIX = 'nganga23'  // Docker Hub username
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: '28-backend-implement-centralized-config-service-with-spring-cloud-config', url: 'https://github.com/nguynnga23/SUBJECT-PROJECT__Software-Architecture-and-Design.git' // Đặt URL repo của bạn
            }
        }

        stage('Build & Push Docker Images') {
            parallel {
                stage('Config Service') {
                    steps {
                        script {
                            buildAndPush('config-service')
                        }
                    }
                }

                stage('Discovery Service') {
                    steps {
                        script {
                            buildAndPush('discovery-service')
                        }
                    }
                }

                stage('Gateway Service') {
                    steps {
                        script {
                            buildAndPush('gateway-service')
                        }
                    }
                }

                stage('User Service') {
                    steps {
                        script {
                            buildAndPush('user-service')
                        }
                    }
                }

                stage('Book Service') {
                    steps {
                        script {
                            buildAndPush('book-service')
                        }
                    }
                }

                stage('Borrowing Service') {
                    steps {
                        script {
                            buildAndPush('borrowing-service')
                        }
                    }
                }

                stage('Inventory Service') {
                    steps {
                        script {
                            buildAndPush('inventory-service')
                        }
                    }
                }

                stage('Notification Service') {
                    steps {
                        script {
                            buildAndPush('notification-service')
                        }
                    }
                }

                stage('Recommendation Service') {
                    steps {
                        script {
                            buildAndPush('recommendation-service')
                        }
                    }
                }
            }
        }

        stage('Deploy to Docker Compose') {
            steps {
                script {
                    sh """
                    docker-compose down
                    docker-compose pull
                    docker-compose up -d --build
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Cleaning up local Docker images..."
                sh "docker system prune -f"
            }
        }
    }
}

def buildAndPush(service) {
    withDockerRegistry(credentialsId: DOCKER_HUB_CREDENTIALS) {
        sh """
        docker build -t ${IMAGE_PREFIX}/${service}:latest ${service}
        docker push ${IMAGE_PREFIX}/${service}:latest
        """
    }
}
