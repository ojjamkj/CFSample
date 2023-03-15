import com.gtone.cf.rt.run.RealBuildDeployRun;

public class SiteRealBuildDeployRun extends RealBuildDeployRun {
	
	// 정상 종료시
	protected void onEndAfterCommit()
	{
		super.onEndAfterCommit();
		if(super.inHash.get("RESULT") != null) {
			String result = (String) super.inHash.get("RESULT");
			if(result.equals("S")) {
				
			}else {
				
			}
		}
		
	}
	
	//에러시
	protected void errorHandle(Exception e) {
		super.errorHandle(e);
		
		//사이트 로직 처리
	}
}
