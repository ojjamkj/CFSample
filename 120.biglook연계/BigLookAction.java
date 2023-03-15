package test.biglook;

import java.util.HashMap;
import java.util.List;

import jspeed.base.util.StringHelper;

import com.gtone.cf.daemon.cmd.BaseCommand;
import com.gtone.cf.rt.connect.ConnectorFactory;
import com.gtone.cf.rt.file.FileTransfer;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.cf.web.CFAppInit;
import com.gtone.express.server.actions.ExpressAction;

public class BigLookAction extends ExpressAction
{

	@Override
	public HashMap execute(HashMap input) throws Exception {
		 String callType = (String)input.get("CALL_TYPE");
		 HashMap outputHash = new HashMap();
		 HashMap<String, Object> param = new HashMap<String, Object>();
		 HashMap globalMap = new HashMap();
		 
		 try {
			 input.put("SESS_LANG_TYPE", this.getLangCD());
			 String instId = (String)input.get("INST_ID");
			 
			 
			 //기본값 세팅
			 String BIGLOOK_TARGET_IP = "123.235.235.235";
			 String BIGLOOK_TARGET_PORT = "30502";
			 String BIGLOOK_TARGET_ROOT_PATH = "/app/biglook";
			 
			 FileTransfer transfer = FileTransfer.getInstance();
			 BaseCommand command = null; 
			 
			 //파일전송
			 List<HashMap> srcList =  CFAppInit.getResourceManager().findResourceListByInstIdForGrid(input);
			 for(HashMap srcMap : srcList)
			 {
				 if(StringHelper.isNull(StringHelper.evl(srcMap.get("VERSION_ID"), ""))) continue;
				 
				 String targetPath = BIGLOOK_TARGET_ROOT_PATH + "/" + srcMap.get("INST_ID") + "/" + srcMap.get("COLLECT_NAME") +"/" + srcMap.get("RES_PATH") +"/" + srcMap.get("RES_NAME");
				 srcMap.put("TARGET_IP", BIGLOOK_TARGET_IP);
				 srcMap.put("TARGET_PORT", BIGLOOK_TARGET_PORT);
				 command = transfer.copyRepositorySrcToRemote(globalMap, srcMap, targetPath);

				 if(!command.isSuccess()){
					 throw new Exception(command.getErrorMessage());
				 }
		 	 }	 
			 
			 
			 //biglook call
			 //input.put("BIGLOOK_PRJ_CD","12131213135");
			 BigLookCall.call(input);
			 		 
		 } catch ( Exception e ) {
				param.put("MESSAGE", e.getMessage());
				this.genParamHash(outputHash, param);
				ExceptionHandler.handleException(e);
		 } finally{
			 ConnectorFactory.getInstance().allConnectorClose(globalMap);
		 }
		 
		 return outputHash;
	}
}