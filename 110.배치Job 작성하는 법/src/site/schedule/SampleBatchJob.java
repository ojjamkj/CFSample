package site.schedule;
 
import com.gtone.cf.schedule.IMMJobNode;
import com.gtone.cf.schedule.ScheduleModel;
import com.gtone.cfclient.schedule.service.IQuartzJobService;

import jspeed.base.log.LogLevel;
import jspeed.base.log.Logger;
 
public class SampleBatchJob implements IQuartzJobService, IMMJobNode{
 
	private Logger logger = LogService.getInstance().getLogger();
	private final String logPrefix = "* SampleBatchJob : ";
	int idx=0;
	boolean interrupted=false;
	ScheduleModel model;
 
	//이 메서드를 구현하세요.
	public void executeJob() {
		try{
			logger.println(LogLevel.INFO, logPrefix+"Start Job");
 
			for(int i=0;i<10; i++)
			{
				//수행 로직을 작성하세요.
 
				idx++; //DB결과에 저장될 배치건수
			}
 
			logger.println(LogLevel.INFO, logPrefix+"End Job");
		}catch(Exception e){
			logger.println(LogLevel.ERROR, logPrefix+ e.getMessage());
		}finally{
 
		}
	}
 
	public void run(ScheduleModel model) throws Exception {
		this.model = model;
		executeJob();
	}
 
	public void interrupt() {
		interrupted = true;
 
	}
 
	public long getCheckCount() {
		return idx;
	}
 
 
}