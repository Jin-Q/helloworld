package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySDutyUsrOp extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try{
			connection = this.getConnection(context);
			String usrCde = (String)context.getDataValue("usr_cde");
			String doubt = (String)context.getDataValue("doubt");
			//通过组件服务实例化业务组件
//			RscTaskInfoComponent comp = (RscTaskInfoComponent)CMISFactory.getComponent(RscTaskInfoConstance.RSC_TASK_INFO_ID);			
			
            int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			//声明分页信息，默认读取10条记录
//			int pageSize = 5;
//			PageInfo pageInfo = EUIUtil.assemblePageInfo(context, pageSize);
//			//查询出用户已配置的岗位信息
//			IndexedCollection iColl = comp.querySelectedDutyList(usrCde, instuCde,doubt, connection);
			String dutyNoList = (String) context.getDataValue("dutyNoList");
			String[] dutyNoArray = dutyNoList.split(",");
			for(String s : dutyNoArray){
				if("D0103".equals(s)){
					KeyedCollection sDuty = new KeyedCollection();
					sDuty.put("duty_cde","Q0000");
					sDuty.put("duty_desc","已完成"); 
					iColl.add(sDuty);
				}
			}
			this.putDataElement2Context(iColl, context);
				
		} catch (EMPException ee) {
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
