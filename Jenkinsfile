// Stage 2: Define Environment Variables
    environment {
        // CRITICAL: Replace YOUR_EC2_PUBLIC_IP with 13.60.210.62
        APP_URL = 'http://13.60.210.62:80' 
        GH_CREDS = 'github-pat-creds' 
        // Use the instructor's email address as the collaborator's email
        COLLAB_EMAIL = 'Qasim.Malik@comsats.edu.pk' // Replace with the actual instructor email
    }

    // ... (rest of the stages and post section remain the same) ...

    post {
        // ...
        success {
            echo "Build successful. Notifying ${COLLAB_EMAIL}."
            // UNCOMMENT LATER: mail to: env.COLLAB_EMAIL, subject: "SUCCESS: Pipeline Test - ${env.JOB_NAME}", body: "Tests passed. See report: ${env.BUILD_URL}/testReport"
        }
        failure {
            echo "Build failed. Notifying ${COLLAB_EMAIL}."
            // UNCOMMENT LATER: mail to: env.COLLAB_EMAIL, subject: "FAILURE: Pipeline Test - ${env.JOB_NAME}", body: "Tests failed. Review report: ${env.BUILD_URL}/testReport"
        }
    }