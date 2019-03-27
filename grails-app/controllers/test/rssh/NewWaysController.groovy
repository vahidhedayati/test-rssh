package test.rssh

import grails.converters.JSON
import grails.plugin.remotessh.SSHUtil
import ch.ethz.ssh2.Connection

class NewWaysController {

	def sshUtilService
	
	def index() {
		/**
		 * This is the latest 0.10 using lots of new ways methods features.
		 * 
		 * String username, String password, String host, int port, boolean singleInstance=false
		 * 
		 * Within the service and SSHUtil class there are other ways of loading things up in a more manual way
		 * refer to test09 below this controller - and go through the service to see how else things could be done
		 * i.e. more parameterised versions of these methods available
		 * @return
		 */
		
		//by setting singleInstance to false we are telling ssh drivers to keep connection retained after it 
		// runs the first command if set true then immediate disconnection from ssh server should happen after 1st run
		boolean singleInstance = false
		
		
		//Various ways of initialising sshUtil - if all in file use this simpler version
		//SSHUtil sshUtil = sshUtilService.initialise
		//SSHUtil sshUtil = sshUtilService.initialise('localhost',22,singleInstance)
		//SSHUtil sshUtil = sshUtilService.initialise('localhost',22) //where this is false meaning remains open
		//SSHUtil sshUtil = sshUtilService.initialise('username','password','localhost',22,singleInstance)
		//SSHUtil sshUtil = sshUtilService.initialise('username','keyfile','keyFilePass','localhost',22,singleInstance)
		SSHUtil sshUtil = sshUtilService.initialise('username','password','localhost',22 as int,singleInstance)
		
		// Or.... instantiate SSHUtil Directly like above but 
		// SSHUtil sshUtil = new SSHUtil().initialise
		// SSHUtil sshUtil = new SSHUtil().initialise('username','password','localhost',22,singleInstance)
		
		
		//Now we have sshUtil it is equipped to either do things with the class loaded or through service:
		
		Map output=[:]
		
		//Either now like this
		output.ranCommand = sshUtilService.execute(sshUtil,'mkdir /tmp/backup-test-new')
		
		output.ranCommand = sshUtilService.execute(sshUtil,'mkdir /tmp/remote-got')
		output.ranCommand = sshUtilService.execute(sshUtil,'mkdir /tmp/remote-got2')
		
		output.ranCommand2 = sshUtilService.execute(sshUtil,'echo "hi there" > /tmp/Kispálés.txt')
		
		//returned files ie will scp them down
		output.ranCommand2 = sshUtilService.execute(sshUtil,'echo "hi there return" > /tmp/backup-test-new/remote-file-example.txt')
		output.ranCommand2 = sshUtilService.execute(sshUtil,'echo "hi there return" > /tmp/backup-test-new/remote-file-example2.txt')
		
		//OR direct now you have sshUtil class:
		output.ranCommand3 = sshUtil.execute('mkdir /tmp/backup-sshUtil')
		output.ranCommand4 = sshUtil.execute('echo "hi there from sshUtil" > /tmp/test2.txt')
		output.ranCommand5 = sshUtil.execute('echo "hi there from sshUtil" > /tmp/Kispálés2.txt')
				
		output.ranCommand6 = sshUtil.execute('echo "hi there from sshUtil test3" > /tmp/test3.txt')
		output.ranCommand7 = sshUtil.execute('echo "hi there from sshUtil test3" > /tmp/Kispálés3.txt')
		
		output.ranCommand10 = sshUtil.execute('echo "hi there from sshUtil test4" > /tmp/test4.txt')
		output.ranCommand11 = sshUtil.execute('echo "hi there from sshUtil test5" > /tmp/test5.txt')
		
		output.ranCommand12 = sshUtil.execute('echo "hi there from sshUtil test4" > /tmp/test6.txt')
		output.ranCommand13 = sshUtil.execute('echo "hi there from sshUtil test5" > /tmp/test7.txt')
		
		
		output.readFile1 = sshUtilService.readRemoteFile(sshUtil,'/tmp/test.txt')
		output.readFile2 = sshUtil.readRemoteFile('/tmp/test2.txt')
		
		output.readFile3 = sshUtilService.readFile(sshUtil,'/tmp/Kispálés.txt')
		output.readFile4 = sshUtil.readFile('/tmp/Kispálés2.txt')
		
		
		//These are voids no output
		output.writeFile1 =sshUtilService.writeFile(sshUtil,'/tmp/Kispálés.txt','/tmp/backup-test-new/')
		output.writeFile2 =sshUtil.writeFile('/tmp/Kispálés2.txt','/tmp/backup-test-new/')
		
		output.writeFile1 =sshUtilService.writeFileWithName(sshUtil,'/tmp/Kispálés.txt','/tmp/backup-test-new/ahha1.txt')
		output.writeFile2 =sshUtil.writeFileWithName('/tmp/Kispálés.txt','/tmp/backup-test-new/ahha2.txt')
		
		output.writeFile4 =sshUtilService.putFile(sshUtil,'/tmp/Kispálés3.txt','/tmp/backup-test-new/')
		output.writeFile5 =sshUtil.putFile('/tmp/test3.txt','/tmp/backup-test-new/')
		output.writeFile6 =sshUtilService.putFile(sshUtil,'/tmp/Kispálés3.txt','/tmp/backup-test-new/')

		//uploads  a bunch of files 
		output.writeFile7 =sshUtilService.putFiles(sshUtil,['/tmp/test4.txt','/tmp/test5.txt'],'/tmp/backup-test-new/')
		output.writeFile7 =sshUtil.putFiles(['/tmp/test6.txt','/tmp/test7.txt'],'/tmp/backup-test-new/')
		
		//Pretending to get remoteFile and storing locally
		output.getRemoteFile1 =sshUtilService.getFile(sshUtil,'/tmp/backup-test-new/remote-file-example.txt','/tmp/')
		output.getRemoteFile2 =sshUtil.getFile('/tmp/backup-test-new/remote-file-example2.txt','/tmp/')
		
		//gets a bunch of files 
		output.getRemoteFile3 =sshUtilService.getFiles(sshUtil,['/tmp/backup-test-new/remote-file-example2.txt',
			'/tmp/backup-test-new/remote-file-example.txt'],'/tmp/remote-got')
	
		output.getRemoteFile4 =sshUtil.getFiles(['/tmp/backup-test-new/remote-file-example2.txt',
			'/tmp/backup-test-new/remote-file-example.txt'],'/tmp/remote-got2')
	
		
		//Boolean check if a file exists
		output.fileExists1= sshUtilService.fileExists(sshUtil,'/tmp/backup-test-new/remote-file-example.txt')
		output.fileExists2= sshUtilService.fileExists(sshUtil,'/tmp/backup-test-new/remote-file-example22.txt')
		output.fileExists3= sshUtil.fileExists('/tmp/backup-test-new/remote-file-example.txt')
		
		//Delete remote file 
		output.deleteFile1= sshUtilService.deleteRemoteFile(sshUtil,'/tmp/backup-test-new/remote-file-example.txt')
		output.deleteFile1= sshUtil.deleteRemoteFile('/tmp/backup-test-new/remote-file-example2.txt')
		
		
		output.remoteFileSize1=sshUtilService.remoteFileSize(sshUtil,'/tmp/test5.txt')
		output.remoteFileSize2=sshUtil.remoteFileSize('/tmp/test5.txt')
		
		output.createDir1=sshUtilService.createRemoteDirs(sshUtil,'/tmp/remote/1/2/3')
		output.createDir2=sshUtil.createRemoteDirs('/tmp/remote/3/4/5')
		
		render output as JSON
	}
	
	def test09() {
		
		/**
		 * 
		 * this was for 0.9 revision of plugin
		 * please note we are getting a connection but in default case where everything including host is configured
		 * in the configuration file you don't even need a connection 
		 * 
		 * Please note by establishing a connection once in any call means it can be reused
		 * 
		 * if it is not provided and end command is run then a new connection is launched upon each call
		 * 
		 * 
		 * in our example we are going to reuse the one connection that we start with
		 * 
		 * This is ran under Linux and it uses the utility to create and read / write files under /tmp
		 *
		 */
		
		//----------------------- CONNECTION ------------------------------------ //
		
		//If all set in config you can call like this
		// Connection connection = sshUtilService.openConnection
		
		//Or
		// Connection connection = sshUtilService.openConnection("localhost")
		
		//Or
		// Connection connection = sshUtilService.openConnection("localhost",22)
		
		//Or  ---- this is what we are using
		Connection connection = sshUtilService.openConnection("localhost", 22, 'username','password')
		
		//Or
		//Connection connection = sshUtilService.openConnection("localhost", 22, 'username','keyfile','keyfilePass')
		
		
		//Now that you have connection we are going to reuse it to do many things;
		
		String ranCommand = sshUtilService.execute(connection,'mkdir /tmp/backup-test')
		String ranCommand1 = sshUtilService.execute(connection,'echo "hi there" > /tmp/test.txt')
		String ranCommand2 = sshUtilService.execute(connection,'echo "hi there" > /tmp/Kispálés.txt')
		//or 
		//ranCommand = sshUtilService.execute('mkdir /tmp/backup-test && echo "hi testing" > /tmp/test.txt')
		//where connection is created as per config and on each call 
		println "Ran command ${ranCommand} ${ranCommand1} "
		
		String readRemoteFile = sshUtilService.readRemoteFile('/tmp/test.txt',connection)
		//if we did not want to initiate connection and get it to trigger connection based on config
		//sshUtilService.readRemoteFile('/tmp/test.txt')
		
		println "Read readRemoteFile a file ${readRemoteFile}"
		
		
		sshUtilService.writeFile('/tmp/test.txt','/tmp/backup-test/',connection)
		sshUtilService.writeFile('/tmp/Kispálés.txt','/tmp/backup-test/',connection)
		
		sshUtilService.writeFileWithName('/tmp/test.txt','/tmp/backup-test/happy.txt',connection)
		
		sshUtilService.writeFileWithName('/tmp/test.txt','/tmp/backup-test/Kispálés-happy.txt',connection)
		
		//sshUtilService.writeFileWithName('/tmp/test.txt','/tmp/backup-test/Kispálés.txt',connection)
		
		
		//Please NOTE READ REMOTE FILE closes can connection
		String readACopiedRemoteFile = sshUtilService.readRemoteFile('/tmp/backup-test/test.txt',connection)
		println "Read copied file as Remote File ${readACopiedRemoteFile}"
		
		
		//String readACopiedRemoteFile1 = sshUtilService.readRemoteFile('/tmp/backup-test/Kispálés.txt',connection)
		//println "Read copied file as Remote File ${readACopiedRemoteFile1} /tmp/backup-test/Kispálés.txt"
		//add true to close connection
		//String readACopiedRemoteFile = sshUtilService.readRemoteFile('/tmp/backup/test.txt',connection,true)
		//println "Read copied file as Remote File ${readACopiedRemoteFile}"
		
		
		sshUtilService.connClose(connection)
		
		render "${[readRemoteFile:readRemoteFile,readACopiedRemoteFile:readACopiedRemoteFile ]}" 		
		
	}
	
}
