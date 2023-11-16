pipeline {
    agent any

    parameters {
        choice(name: 'service1_www-dev', choices: '10 20 30 40 50 60 70 80 90', description: 'Weight for service1_www-dev')
        choice(name: 'service1_www-live', choices: '10 20 30 40 50 60 70 80 90', description: 'Weight for service1_www-live')
        choice(name: 'service2_www-dev2', choices: '10 20 30 40 50 60 70 80 90', description: 'Weight for service2_www-dev2')
        choice(name: 'service2_www-live2', choices: '10 20 30 40 50 60 70 80 90', description: 'Weight for service2_www-live2')
    }

    stages {
        stage('Terraform Plan') {
            steps {
                script {
                    // Initialize and plan Terraform
                    sh "terraform init"
                    sh "terraform plan -var='service1_www-dev=${params.service1_www-dev}' -var='service1_www-live=${params.service1_www-live}' -var='service2_www-dev2=${params.service2_www-dev2}' -var='service2_www-live2=${params.service2_www-live2}'"
                }
            }
        }
    }
}
