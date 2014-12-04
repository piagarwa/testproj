package apps.training.components.content.sightlycomp;

import com.adobe.cq.sightly.WCMUse;

public class TrainingMarkupService  extends WCMUse{

	String pagePath;
	@Override
	public void activate() throws Exception {
		// TODO Auto-generated method stub
	pagePath=getCurrentPage().getPath();
	}
	
	public String getPagePath(){
		
		return pagePath;
	}
}
