package com.yucheng.cmis.ops.cfg.cfgriskclass;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCfgRiskClassListOp extends CMISOperation {


	private final String modelId = "CfgRiskClass";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			int size = 10;	
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			String sql = "SELECT T.OVDUE_DAYS,"
					+ "MAX( decode(t.guar_way, '100', t.risk_class)) grt100,"
					+ "MAX(  decode(t.guar_way, '200', t.risk_class)) grt200,"
					+ "MAX(  decode(t.guar_way, '300', t.risk_class)) grt300,"
					+ "MAX(  decode(t.guar_way, '400', t.risk_class)) grt400 "
					+ "FROM CFG_RISK_CLASS T group by t.OVDUE_DAYS order by t.OVDUE_DAYS";
			
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql);

			iColl.setName("CfgRiskClassList");
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
