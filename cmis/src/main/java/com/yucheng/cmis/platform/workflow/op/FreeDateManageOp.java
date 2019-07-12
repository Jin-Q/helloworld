package com.yucheng.cmis.platform.workflow.op;

import java.util.Calendar;
import com.ecc.echain.ext.FreeDate;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * <p>流程工作日历管理</p>
 * @author liuhw
 *
 */
public class FreeDateManageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		
		String subType = (String) context.getDataValue("subType");//getFreeDate,init,setFreeDate,setWorkDate
		Calendar c=Calendar.getInstance();
		if(subType==null||subType.equals("getFreeDate")){
			String year = (String) context.get("year");
			if(year==null||year.equals(""))
				year=String.valueOf(c.get(c.YEAR));
			String month = (String) context.get("month");
			if(month==null||month.equals(""))
				month=String.valueOf(c.get(c.MONTH)+1);		
			context.put("freeDate",new FreeDate().getFreeDate(year));
			context.put("year", year);
			context.put("month", month);
		}
		else if(subType.equals("init")){
			String year = (String) context.getDataValue("year");
			context.put("freeDate",new FreeDate().init(year));
			context.put("year", year);
			context.put("month", "01");
		}
		else if(subType.equals("setFreeDate")){
			String curDate = (String) context.get("curDate");
			if(curDate==null||curDate.length()==0||curDate.length()!=8){
				throw new EMPException("无效的参数For：curDate="+curDate);
			}
			String year=curDate.substring(0, 4);
			String month=curDate.substring(4, 6);
			context.put("freeDate",new FreeDate().setFreeDate(curDate));
			context.put("year", year);
			context.put("month", month);
		}
		else if(subType.equals("setWorkDate")){
			String curDate = (String) context.get("curDate");
			if(curDate==null||curDate.length()==0||curDate.length()!=8){
				throw new EMPException("无效的参数For：curDate="+curDate);
			}
			String year=curDate.substring(0, 4);
			String month=curDate.substring(4, 6);
			context.put("freeDate",new FreeDate().setWorkDate(curDate));
			context.put("year", year);
			context.put("month", month);
		}
		return null;
	}

}
