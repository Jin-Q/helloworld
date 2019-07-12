package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryNextIqpAppOverseeOrgPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try
		{
			connection = this.getConnection(context);	
			TableModelDAO dao = this.getTableModelDAO(context);
			String oversee_org_id = null;
			KeyedCollection kc = (KeyedCollection)context.getDataElement("IqpAppOverseeOrg");
			try{
				oversee_org_id = (String)kc.getDataValue("oversee_org_id");
			}catch(Exception e){
				throw new Exception("监管机构编号为空,请检查!");
			}
			String condition = "where oversee_org_id='"+oversee_org_id+"' and oversee_org_status = '01'";//监管机构信息表中状态为'有效'的监管机构
			IndexedCollection iColl = dao.queryList("IqpOverseeOrg", condition, connection);
			if( iColl.size()<=0 || iColl==null){//此监管机构不存在状态为'有效'的信息
				putDataElement2Context(kc, context);
			}else{//存在，则把信息表中的历史数据赋值到申请表中
				KeyedCollection kCollNew = (KeyedCollection) iColl.get(0);
				kCollNew.putAll(kc);
				kCollNew.remove("input_date");
				kCollNew.remove("oversee_org_status");
				kCollNew.addDataField("approve_status", "000");
				kCollNew.addDataField("flow_type", "01");
				kCollNew.setName("IqpAppOverseeOrg");
				putDataElement2Context(kCollNew, context);			
			}
		}catch(EMPException ee) {
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
