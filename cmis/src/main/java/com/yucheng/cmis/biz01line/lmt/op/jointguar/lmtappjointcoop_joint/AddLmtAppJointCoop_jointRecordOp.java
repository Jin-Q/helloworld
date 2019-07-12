package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAppJointCoop_jointRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppJointCoop";
	private final String modelIdNameList = "LmtAppNameList";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String serno = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//若含有serno则是修改，否则是新增
			if(!kColl.containsKey("serno")){//判断是否存在业务编号
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				kColl.addDataField("serno", serno);
				dao.insert(kColl, connection);
				
				//插入联保组长信息
				KeyedCollection kCollList = new KeyedCollection(modelIdNameList);
				kCollList.addDataField("serno", serno);
				kCollList.addDataField("cus_id", kColl.getDataValue("cus_id"));
				kCollList.addDataField("is_limit_set", "1");  //联保小组下默认是进行额度设置
				kCollList.addDataField("sub_type","03"); //分项类别为  03  联保
				//add a record
				dao.insert(kCollList, connection);
				context.addDataField("serno", serno);
			}else{
				dao.update(kColl, connection);
			}
			if(!context.containsKey("serno")){
				context.addDataField("serno", "");
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
			this.putDataElement2Context(kColl, context);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
