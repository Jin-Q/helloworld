package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoDetailOp  extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = new KeyedCollection();
			String serno  = null;
			try {
				serno  = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno  == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");
			//通过组件服务实例化业务组件
			kColl = dao.queryDetail(modelId, serno, connection);
			//查询数据 
					//comp.queryRscTaskInfoDetail(domain, connection);
			if(context.containsKey("op")){
				if("view".equals((String)context.getDataValue("op"))){	 
					KeyedCollection param = new KeyedCollection();
					param.put("serno",serno);
     				param.put("identy_duty",(String)context.getDataValue("dutyNo"));
					param = (KeyedCollection)SqlClient.queryFirst("queryRscTaskInfoSubLike", param, new String[]{"condition1","condition2","condition3","condition4"}, connection);
					if(param != null){
						kColl.put("remark",param.getDataValue("remark"));
					}
				}
			}
			
			//将查询结果放入Context中以便前端获取
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			String[] args=new String[] {"cus_id","prd_id"};
			String[] modelIds=new String[]{"CusBase" ,"PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid" };
			String[] fieldName=new String[]{"cus_name" ,"prdname"};
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context); 
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
