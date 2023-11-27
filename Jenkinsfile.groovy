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
        
    }


    stage('Plan') {
            
        }
    
    stage('Apply'){

    }
    stage('Slack update'){
        
    } 
    stage('Commit'){

    }
    }    
}
}
