// Corrected Jenkinsfile (Pay close attention to every brace {})

pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome:latest' 
            // 1. Move the user '0' directive and combine it with the Docker run arguments.
            // 2. Remove the complex -v /var/lib/jenkins/.m2 mapping.
            args '--user 0 -v /dev/shm:/dev/shm' // <-- User set via Docker args
        }
    }

    environment {
        APP_URL = 'http://13.60.210.62:80' 
        GH_CREDS = 'github-pat-creds' 
        COLLAB_EMAIL = 'Qasim.Malik@comsats.edu.pk' 
    }

    // CRITICAL: Ensure the following stages block is structurally intact.
    stages { 
        stage('Checkout Test Code') {
            steps {
                echo 'Checking out Selenium test code from GitHub...'
                git branch: 'main', credentialsId: GH_CREDS, url: 'https://github.com/IrfaanDS/flask-app-test-code' 
            }
        }

        stage('Execute Selenium Tests') { // <-- This stage must be inside 'stages {}'
            steps {
                echo "Running containerized Maven tests against ${APP_URL}..."
                // 3. FIX: Instruct Maven to use a writable folder (.repository) inside the workspace.
                sh "mvn clean test -Dapp.url=${APP_URL} -Dmaven.repo.local=./.repository" 
            }
        }

        stage('Publish Test Results') {
            steps {
                echo 'Publishing JUnit test reports...'
                junit 'target/surefire-reports/*.xml' 
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "Build successful. Notifying collaborator at ${COLLAB_EMAIL}."
        }
        failure {
            echo "Build failed. Notifying collaborator at ${COLLAB_EMAIL}."
        }
    }
}