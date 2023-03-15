public void deleteRemoteFile( String targetFile ) throws Exception{
  
  com.gtone.cf.daemon.cmd.BaseCommand resultCmd = null;
 
  HashMap inHash = new HashMap();
  inHash.put(ICFConstants.TARGET_IP, "127.0.0.1");
  inHash.put(ICFConstants.TARGET_PORT, "30502");
  inHash.put(ICFConstants.MACHINE_TYPE, "S"); //윈도우,unix
  inHash.put(ICFConstants.CONNECT_TYPE, "A"); //에이전트
  inHash.put(ICFConstants.TARGET_FILE, targetFile); //삭제할 파일
  
  resultCmd = com.gtone.cf.rt.file.FileTransfer.getInstance().deleteSrcToRemote(inHash, inHash, targetFile) ;

  //연결 해제
  ConnectorFactory.getInstance().allConnectorClose(inHash);
  
  boolean deleteSuccess = (resultCmd == null || !resultCmd.isSuccess()) ? false :  true;
  if (resultCmd == null) {
   System.out.println("error");
  } else if(!deleteSuccess) {
   System.out.println(targetFile + " : " + StringHelper.evl(resultCmd.getErrorMessage(), ""));
  }
 
 }