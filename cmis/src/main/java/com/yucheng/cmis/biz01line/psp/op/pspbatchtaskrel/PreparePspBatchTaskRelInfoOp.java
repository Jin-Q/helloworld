package com.yucheng.cmis.biz01line.psp.op.pspbatchtaskrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 
*@author lisj
*@time 2015-1-12
*@description 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能）
*@version v1.0
*
 */
public class PreparePspBatchTaskRelInfoOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			//生成主任务编号
			String major_task_id = "M" + CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			
			String currentUserId=(String)context.getDataValue("currentUserId");
			String organNo =(String)context.getDataValue("organNo");
			String userName =(String)context.getDataValue("loginusername");
			String organName =(String)context.getDataValue("organName");
			context.addDataField("major_task_id", major_task_id);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造,注释默认值  begin**/
			//context.addDataField("manager_id", currentUserId);
			//context.addDataField("manager_br_id", organNo);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造,注释默认值  end**/
			context.addDataField("user_name", userName);
			context.addDataField("organ_name", organName);
			context.addDataField("input_id", currentUserId);
			context.addDataField("input_br_id", organNo);
			context.addDataField("input_name", userName);
			context.addDataField("input_br_name", organName);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
