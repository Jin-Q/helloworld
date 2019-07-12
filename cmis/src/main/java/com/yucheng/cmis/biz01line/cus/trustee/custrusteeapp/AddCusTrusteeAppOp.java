package com.yucheng.cmis.biz01line.cus.trustee.custrusteeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddCusTrusteeAppOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusTrusteeApp";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = getTableModelDAO(context);
			
			KeyedCollection kColl = new KeyedCollection(modelId);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("TRUS", "all", connection, context);
			kColl.addDataField("serno",serno);
			kColl.addDataField("input_id", context.getDataValue(PUBConstant.loginuserid));
			kColl.addDataField("input_br_id", context.getDataValue(PUBConstant.loginorgid));
			kColl.addDataField("consignor_type","1");
			
			IndexedCollection iColl = dao.queryList("SDutyuser", " where dutyno in ('S0200','S0226') and  actorno = '" + context.getDataValue(PUBConstant.loginuserid) + "'", connection);
			if(iColl!=null&&iColl.size()>0){
				kColl.addDataField("is_provid_accredit","1");
			}
			
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String [] {"input_br_id"});
			
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
