package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddGrtGuaranteeRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "GrtGuarantee";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guarId = null;
		String guarContNo = null;
		try{
			connection = this.getConnection(context);
			//从context中取出sequenceService
			guarId = CMISSequenceService4JXXD.querySequenceFromDB("GT", "fromDate", connection, context);
			//定义新增保证人信息时所需要的kcoll
			KeyedCollection kColl = null;
			//定义新增关联关系所需要的kcoll
			KeyedCollection kCol = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				kColl.setDataValue("guar_id", guarId);
				kColl.remove("cus_id_displayname");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//新增保证人记录
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			//新增关联关系
			kCol = (KeyedCollection)context.getDataElement(modelId);
			kCol.setDataValue("guar_id", guarId);
			kCol.remove("is_spadd");
			kCol.remove("cus_id");
			kCol.remove("guar_amt");
			kCol.remove("guar_type");
			kCol.remove("cus_id_displayname");
			guarContNo = (String) kCol.getDataValue("guar_cont_no");
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("GrtGuarCont", context, connection);
			ggc.insertGrtGuarantyRe(kCol);
			
			//判断是否关联业务，如果关联业务则往申请表中插入担保放大倍数
			/**---------------为业务申请中担保倍数赋值--start---------------------*/
			ggc.getGuarBailMultiple(guarContNo, this.getDataSource(context),connection);
			/**---------------为业务申请中担保倍数赋值--end---------------------*/
			context.addDataField("guar_cont_no", guarContNo);
			context.addDataField("flag","success");
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
