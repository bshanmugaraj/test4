pipeline {
    agent any

    parameters {
        /* choice(name: 'REGION', choices: 'us-west-2a us-east-1', description: 'Select AWS Region') */
        string(name: 'grid-us-west-2a', defaultValue: '50', description: 'Weight for grid-us-west-2a')
        string(name: 'grid-us-east-1', defaultValue: '50', description: 'Weight for grid-us-east-1')
        string(name: 'tim-us-west-2a', defaultValue: '50', description: 'Weight for tim-us-west-2a')
        string(name: 'tim-us-east-1', defaultValue: '50', description: 'Weight for tim-us-east-1')
        string(name: 'AWS_CREDENTIALS_ID', description: 'AWS Credentials ID')
     
    }

    stages {
        stage('Terraform Plan') {
            steps {
                script {
                    withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        credentialsId: params.'7469a313-8421-4018-85f2-5fd390bd6e80',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                    ]]) {
                        
                        // You can now use AWS credentials in this block
                        sh "echo 'AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}'"
                        sh "echo 'AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}'"
                    // Initialize and plan Terraform
                    sh "terraform init"
                    sh "terraform plan -var='grid-us-west-2a=${params.'grid-us-west-2a'}' -var='grid-us-east-1=${params.'grid-us-east-1'}' -var='tim-us-west-2a=${params.'tim-us-west-2a'}' -var='tim-us-east-1=${params.'tim-us-east-1'}'"
                    }    
                }
          
            }
        }
    }
}
