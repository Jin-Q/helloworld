package com.yucheng.cmis.biz01line.fnc.op.fncstatcommon;


import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.FNCQuery;

public class TestOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
	    Connection connection = null;
	        try {
	            connection = this.getConnection(context);
		FNCFactory fc = FNCFactory.getFNCFactoryInstance();
		FNCQuery fncQuery = new FNCQuery();
		Map<String,FncConfStyles> fncMap = fncQuery.getAllListFromDB(context,connection);
		Iterator iter = fncMap.values().iterator();
		EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "--------------------log TestOp start-----------------------");
		//System.out.println(this.getClass().getName()+"--------------------log TestOp start-----------------------");
		while(iter.hasNext()){
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "{  ");
			FncConfStyles f = (FncConfStyles)iter.next();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "["+f.getStyleId()+"]" + f.getFncName());
			//System.out.println(this.getClass().getName()+"["+f.getStyleId()+"]" + f.getFncName());
			
			List list = f.getItems();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[item is count:" + list.size()+"]");
			//System.out.println(this.getClass().getName()+"[item is count:" + list.size()+"]");
			for(int i=0;i<list.size();i++){
				FncConfDefFormat fmt = (FncConfDefFormat)list.get(i);
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "["+fmt.getItemId()+"]" 
						+ fmt.getItemName()+"--[data1:" + fmt.getData1()+ "]--[data2:" + fmt.getData2()+"]");
				
				//System.out.println(this.getClass().getName()+"["+fmt.getItemId()+"]" 
				//		+ fmt.getItemName()+"--[data1:" + fmt.getData1()+ "]--[data2:" + fmt.getData2()+"]");
				
			}
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "   }");
			
		}
	        } catch (EMPException ee) {
	            throw ee;
	        } catch(Exception e){
	                throw new EMPException(e);
	        } finally {
	                if (connection != null)
	                        this.releaseConnection(context, connection);
	        }
		EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "--------------------log TestOp end-----------------------");
		//System.out.println(this.getClass().getName()+"--------------------log TestOp end-----------------------");
		return "0";
	}

}
