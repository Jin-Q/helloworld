package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetCusInfoOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		String flagInfo = CMISMessage.DEFEAT;  //错误编码信息
		KeyedCollection kc = null;
		Connection connection=null;
	try{
		connection =this.getConnection(context);
		String cusType = (String)context.getDataValue("cus_type");
		String cus_id = (String)context.getDataValue("cus_id");
		
		TableModelDAO dao = this.getTableModelDAO(context);
		 
		if("110".equals(cusType)||"120".equals(cusType)||"130".equals(cusType)){
			kc=dao.queryDetail( PUBConstant.CUSINDIV, cus_id, connection);
			kc.setDataValue("indiv_ntn", "01");
			//查询客户核心客户码,如果不存在则需要页面中修改输入
			KeyedCollection kCollBase = dao.queryDetail("CusBase", cus_id, connection);
			if(null==kCollBase||kCollBase.size()<1){
				throw new ComponentException("未查询到客户基本信息表信息");
			}
			flagInfo = CMISMessage.SUCCESS;
		}else{
			if("211".equals(cusType)||"212".equals(cusType)||"260".equals(cusType)
					||"280".equals(cusType)||"290".equals(cusType)){
				flagInfo = "com";
			}else if("250".equals(cusType)){
				flagInfo = "town";
			}else{
				flagInfo = "ass";
			}
			kc=dao.queryDetail( PUBConstant.CUSCOM, cus_id, connection);
			
			KeyedCollection kCollBase = dao.queryDetail("CusBase", cus_id, connection);
			if(null==kCollBase||kCollBase.size()<1){
				throw new ComponentException("未查询到客户基本信息表信息");
			}
			//String trans_cus_id = (String)kCollBase.getDataValue("trans_cus_id");
			String trans_cus_id = (String)kCollBase.getDataValue("cert_code");
			if(null==trans_cus_id||"".equals(trans_cus_id)){
				kc.addDataField("cus_id", "");
			}else{
				kc.addDataField("cus_id", trans_cus_id);
			}
			
			SInfoUtils.addSOrgName(kc, new String[]{"main_br_id"});
			SInfoUtils.addUSerName(kc, new String[]{"cust_mgr"});
			}
		//将参数放入context
			this.putDataElement2Context(kc, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flagInfo;
	}
}
