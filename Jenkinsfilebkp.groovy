node {
    // Define parameters
    properties([
        parameters([
            choice(name: 'selectedServices', choices: ['ServiceA', 'ServiceB', 'ServiceC'], description: 'Select services', multiSelect: true),
            string(name: 'regionWeight', defaultValue: '1', description: 'Enter weight for the region')
        ])
    ])

    stage('Commit to GitHub') {
        steps {
            script {
                // Assuming GitHub credentials are configured
                checkout scm
                sh 'git config user.name "YourServiceAccount"'
                sh 'git config user.email "service.account@example.com"'
                sh 'git add .'
                sh 'git commit -m "Committing values"'
                sh 'git push origin master'
            }
        }
    }

    stage('Terraform Plan') {
        steps {
            script {
                // Execute Terraform plan
                sh 'terraform plan'
            }
        }
    }

    stage('Approval') {
        steps {
            script {
                // Manual approval step
                input 'Proceed with Terraform apply?'
            }
        }
    }

    stage('Terraform Apply') {
        steps {
            script {
                // Execute Terraform apply
                sh 'terraform apply -auto-approve'
            }
        }
    }

    post {
        success {
            script {
                // Optionally, post to Slack on success
                slackSend(channel: '#your-channel', message: 'Jenkins job succeeded!')
            }
        }
        failure {
            script {
                // Optionally, post to Slack on failure
                slackSend(channel: '#your-channel', message: 'Jenkins job failed!')
            }
        }
    }
}
