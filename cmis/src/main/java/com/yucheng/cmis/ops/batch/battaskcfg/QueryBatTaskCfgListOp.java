package com.yucheng.cmis.ops.batch.battaskcfg;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryBatTaskCfgListOp extends CMISOperation {

	/**
	 * 查询跑批任务列表op
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String sql = "";
			sql = "select * from rircp_ym.Bat_Task_Cfg";
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			IndexedCollection iColl = TableModelUtil.buildPageData(null, dataSource, sql);
			iColl.setName("BatTaskCfgList");
			this.putDataElement2Context(iColl, context);
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
