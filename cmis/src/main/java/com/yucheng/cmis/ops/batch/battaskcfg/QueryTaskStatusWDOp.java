package com.yucheng.cmis.ops.batch.battaskcfg;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryTaskStatusWDOp extends CMISOperation {
	
	/**
	 * 查询所有跑批任务的状态
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String status = "success";
		try{
			connection = this.getConnection(context);
			String sql = "";
			sql = "select USE_FLAG from rircp_ym.Bat_Task_Cfg";		
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql);
			iColl.setName("BatTaskCfgList");
			//启用标志为N时不能跑批
			for(int i=0;i<iColl.size();i++){
				KeyedCollection record = (KeyedCollection) iColl.get(i);
				String use = (String) record.getDataValue("use_flag");
				if("N".equals(use) ){
					status = "failed";
					break;
				}
			}
			//this.putDataElement2Context(iColl, context);
			context.put("status", status);
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
