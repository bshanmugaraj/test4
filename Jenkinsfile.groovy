node {
    stage ("Checkout"){
    projectRootDirectory = pwd() 
    println ("project root: " + projectRootDirectory)
    println ("cleaning project root: " + projectRootDirectory)
    sh "sudo rm -rf ${projectRootDirectory} || true ; mkdir -p ${projectRootDirectory}"
    checkout scm
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY'),
                     string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_KEY')]){


def updateContent(String filePath, String recordName, String weightValue){
        def contents = new File( filePath ).text.readLines()
        def newContents = new ArrayList();
        for(int i=0;i<contents.size();i++){
            newContents.add(i, contents.get(i));
            if (contents.get(i).contains(recordName)){
                for(int j = i; j>=0;j--){
                    if(contents.get(j).contains("weight")){
                        String str = contents.get(j);
                        String[] weight = extractInts(str);
                        str = contents.get(j).replace(weight[0], weightValue);
                        newContents.set(j, str);
                        break;
                    }
                }
            }
        
        }
        File f = new File(filePath)
        PrintWriter writer = new PrintWriter(f)
        newContents.each { id -> writer.println(id) }
        writer.close()
        }
def extractInts(String input){
        input.findAll( /\d+/ )*.toInteger()
        }
    stage('Update File') {
        println "${env.SERVICE}"
        println "$SERVICE"
        if ("${dev.example.com}" != "") {
            updateContent("${SERVICE}.tf","dev.example.com","${dev.example.com}");
        }
        else if ("${prod.example.com}" != "") {
            updateContent("${SERVICE}.tf","prod.example.com","${prod.example.com}");
        }
        else
             println "No weights was assigned";
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
