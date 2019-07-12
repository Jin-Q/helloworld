package com.yucheng.cmis.biz01line.cus.op.cuscogniz.cuscognizapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusCognizApplyRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusCognizApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		String returnInfo = PUBConstant.SUCCESS;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			/**
			 * 保存前先查询该客户是否已经是该次认定类别
			 */
			String cus_id = (String)kColl.getDataValue("cus_id");
			CusComComponent ccComp = (CusComComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM, context,connection);
			CusCom cusCom = ccComp.getCusCom(cus_id);
			
			String scale_type = (String)kColl.getDataValue("scale_type");//认定类别
			if("01".equals(scale_type)){//房地产开发商
				String hou_exp = cusCom.getHouExp();
				if("1".equals(hou_exp)){
					returnInfo = "该客户已经是房地产开发商不需要重新认定！";
					context.addDataField("flag", returnInfo);
					return "0";
				}
			}else{//政府融资平台
				String gover_fin_ter = cusCom.getGoverFinTer();
				if("1".equals(gover_fin_ter)){
					returnInfo = "该客户已经是政府融资平台不需要重新认定！";
					context.addDataField("flag", returnInfo);
					return "0";
				}
			}
			
			serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ","all", connection, context);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);
			context.addDataField("flag", returnInfo);
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
