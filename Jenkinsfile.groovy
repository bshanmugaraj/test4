node {
    stage "Checkout"
    projectRootDirectory = pwd() 
    println ("project root: " + projectRootDirectory)
    println ("cleaning project root: " + projectRootDirectory)
    sh "sudo rm -rf ${projectRootDirectory} || true ; mkdir -p ${projectRootDirectory}"
    checkout scm
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY'),
                     string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_KEY')]) {
        parameters {
            string(name: 'TARGET_NAME', defaultValue: 'www-rr', description: 'Target Name of the CNAME')
            string(name: 'TARGET_CH2', defaultValue: 'prod.example.com', description: 'Target Record')
            string(name: 'TARGET_HO2', defaultValue: 'nonprod.example.com', description: 'Target Record')
            string(name: 'NEW_WEIGHT', defaultValue: '0', description: 'New Weight for record change')
        }
        
        stage('Plan') {
            // Enforce a 5 min timeout on init. TF init has a tendency to hang trying to download the aws provider plugin.
            timeout(5) {
                // init the configured s3 backend
                sh "terraform init -backend=true"
            }
            // get the current remote state:
            sh "terraform get"
            // Sample data representing DNS records
            def dnsRecords = [
                [name: 'www-rr', records: ['prod.example.com'], weight: 0],
                [name: 'www-rr', records: ['nonprod.example.com'], weight: 100],
            ]
            
            // Find the record matching the criteria
            def targetRecord = dnsRecords.find { it.name == params.TARGET_NAME && it.records.contains(params.TARGET_CH2) }
            // Check if the target record was found
            if (targetRecord) {
                // Change the weight to the user-provided value
                targetRecord.weight = Integer.parseInt(params.NEW_WEIGHT)
                
                // Print the updated DNS records
                echo "Updated DNS Records:"
                dnsRecords.each { record ->
                    echo "${record.name}: ${record.records} - Weight: ${record.weight}"
                    sh "set +e; terraform plan -out=plan.out -detailed-exitcode; echo \$? > status"
                    def exitCode = readFile('status').trim()
                    echo "Terraform Plan Exit Code: ${exitCode}"
                }
                    
                    if (exitCode == "0") {
                        currentBuild.result = 'SUCCESS'
                    
                
                
                } else {
                    // If the record is not found, print a message
                    echo "Record with name='${params.TARGET_NAME}' and records='${params.TARGET_CH2}' not found."
                }
            }
        }
    }
}
