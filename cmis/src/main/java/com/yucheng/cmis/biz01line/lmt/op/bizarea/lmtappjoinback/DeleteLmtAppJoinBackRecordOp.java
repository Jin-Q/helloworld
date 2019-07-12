package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappjoinback;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtAppJoinBackRecordOp extends CMISOperation {

	private final String modelId = "LmtAppJoinBack";
	private final String modelIdAppNl = "LmtAppNameList";
	
//	private final String modelIdJc = "LmtAppJointCoop";
//	private final String modelIdPb = "PubBailInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno))
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
//			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);//入/退圈申请
//			String app_flag = (String)kColl.getDataValue("app_flag");//申请类型
//			String agr_no = (String)kColl.getDataValue("agr_no");//圈商协议编号
			int count=dao.deleteByPk(modelId, serno, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("LmtPubComponent", context,connection);
			
//			if("0".equals(app_flag)){//入圈申请
//				String condition = " where agr_no='"+agr_no+"' and approve_status not in ('997','998')";
//				IndexedCollection iCollJc = dao.queryList(modelIdJc, condition, connection);
//				if(iCollJc.size()>0){
//					for(int i=0;i<iCollJc.size();i++){
//						KeyedCollection kCollJc = (KeyedCollection)iCollJc.get(i);
//						String serno = (String)kCollJc.getDataValue("serno");
//						//删除保证金信息
//						Map<String,String> refFields = new HashMap<String,String>();
//			            refFields.put("serno", serno);
//						lmtComponent.deleteByField(modelIdPb, refFields);
//						dao.deleteByPk(modelIdJc, serno, connection);
//					}
//				}
//			}
			
			//删除申请关联名单信息
			Map<String,String> refFields = new HashMap<String,String>();
            refFields.put("serno", serno);
			lmtComponent.deleteByField(modelIdAppNl, refFields);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
