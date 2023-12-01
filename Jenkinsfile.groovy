node {
    stage ("Checkout"){
    projectRootDirectory = pwd() 
    println ("project root: " + projectRootDirectory)
    println ("cleaning project root: " + projectRootDirectory)
    sh "sudo rm -rf ${projectRootDirectory} || true ; mkdir -p ${projectRootDirectory}"
    checkout scm
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY'),
                     string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_KEY')]){



    stage('Update File') {
        echo "update file stage"
    }
      
    stage('Plan') {
      sh 'terraform plan'
    }
      
    stage('Approval') {
      // Manual approval step
      input 'Proceed with Terraform apply?'
    }
        

  /*  stage('Terraform Apply') {
      // Execute Terraform apply
      sh 'set +e; terraform apply; echo \$? > status.apply'
      def applyExitCode = readFile('status.apply').trim()
      if (applyExitCode == "0") {
         slackSend channel: "${env.SLACK_CHANNEL_NAME}", color: 'good', message: "@here Changes Applied ${env.JOB_NAME} - ${env.BUILD_NUMBER} (<${env.BUILD_URL}console|Open>)"
      } else 
         slackSend channel: "${env.SLACK_CHANNEL_NAME}", color: 'danger', message: "@here Apply Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER} (<${env.BUILD_URL}console|Open>)"
         currentBuild.result = 'FAILURE'
    }*/
    }
    }
}
