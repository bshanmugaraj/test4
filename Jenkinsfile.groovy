pipeline {
    agent any

    parameters {
        choice(name: 'REGION', choices: 'us-west-2a us-east-1', description: 'Select AWS Region')
        string(name: 'grid-us-west-2a', defaultValue: '50', description: 'Weight for grid-us-west-2a')
        choice(name: 'grid-us-east-1', defaultValue: '50', description: 'Weight for grid-us-east-1')
        choice(name: 'tim-us-west-2a', defaultValue: '50', description: 'Weight for tim-us-west-2a')
        choice(name: 'tim-us-east-1', defaultValue: '50', description: 'Weight for tim-us-east-1')
    }

    stages {
        stage('Terraform Plan') {
            steps {
                script {
                    // Initialize and plan Terraform
                    sh "terraform init"
                    sh "terraform plan -var='grid-us-west-2a=${params.grid-us-west-2a}' -var='grid-us-east-1=${params.grid-us-east-1}' -var='tim-us-west-2a=${params.tim-us-west-2a}' -var='tim-us-east-1=${params.tim-us-east-1}'"
                }
            }
        }
    }
}
