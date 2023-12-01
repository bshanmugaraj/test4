node {
    stage ("Checkout")
    projectRootDirectory = pwd() 
    println ("project root: " + projectRootDirectory)
    println ("cleaning project root: " + projectRootDirectory)
    sh "sudo rm -rf ${projectRootDirectory} || true ; mkdir -p ${projectRootDirectory}"
    checkout scm
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY'),
                     string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_KEY')]){
        parameters {

        }
    stage('Update File') {
        echo "update file stage"
    }
    stage('Plan') {
    // Enforce a 5 min timeout on init. TF init has a tendency to hang trying to download the aws provider plugin.
    timeout(5) {
           // init the configured s3 backend
           sh "terraform init -backend=true"
           }
           // get the current remote state:
           sh "terraform get"
           
           // run a plan, save its output in a file, exit with detailed 0,1,2 codes
           sh "set +e; terraform plan -out=plan.out -detailed-exitcode; echo \$? > status"
           def exitCode = readFile('status').trim()
           echo "Terraform Plan Exit Code: ${exitCode}"


    }
  }
}    
