package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddCtrLimitAppRecordOp extends CMISOperation {
	private final String modelId = "CtrLimitApp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/** 查询是否存在在途的业务申请信息，存在不允许新增 */
			String cus_id = (String)kColl.getDataValue("cus_id");
			KeyedCollection queryKColl = (KeyedCollection)dao.queryFirst(modelId, null, " where cus_id = '"+cus_id+"' and approve_status not in ('990','997','998') ", connection);
			String cus_id_sel = (String)queryKColl.getDataValue("cus_id");
			if(cus_id_sel != null && !"".equals(cus_id_sel)){
				SInfoUtils.addSOrgName(queryKColl, new String[] { "input_br_id" });
				SInfoUtils.addUSerName(queryKColl, new String[] { "input_id" });
				String inputName = (String)queryKColl.getDataValue("input_id_displayname");
				String inputBrName = (String)queryKColl.getDataValue("input_br_id_displayname");
				String message = "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下存在在途的新增申请业务，不能重复发起。";
				context.addDataField("flag", "failed");
				context.addDataField("serno", "");
				context.addDataField("message", message);
			}else {
				String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				if(serno == null || serno.trim().length() == 0){
					throw new EMPException("生成业务流水号失败！");
				}
				kColl.setDataValue("serno", serno);
				/** add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造 begin **/
				CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
				 CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				 CusBase cusBase = csi.getCusBaseByCusId(cus_id, context, connection);
				 String main_br_id = cusBase.getMainBrId();
				 kColl.put("manager_br_id", main_br_id);
				/** add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造 end **/
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("serno", serno);
				context.addDataField("message", "");
			}
			
			
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			context.addDataField("serno", "");
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
