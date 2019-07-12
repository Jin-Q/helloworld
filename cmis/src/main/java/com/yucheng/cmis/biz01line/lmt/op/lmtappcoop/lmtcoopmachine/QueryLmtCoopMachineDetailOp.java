package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopmachine;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class QueryLmtCoopMachineDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtCoopMachine";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String serno_value = "";
			String agr_no = "";
			if(context.containsKey("serno")){
				serno_value = (String)context.getDataValue("serno");
			}
			if(context.containsKey("agr_no")){
				agr_no = (String)context.getDataValue("agr_no");
			}
			if((null == serno_value || serno_value.length() == 0)&&(null == agr_no || agr_no.length() == 0)){
				context.addDataField("message", "传入的合作方流水号、协议编号都为空！");
				throw new EMPException("传入的合作方流水号、协议编号都为空！");
			}

			String condition = "";
			if(serno_value!=null&&!"".equals(serno_value)){
				condition = " WHERE SERNO='"+serno_value+"'";
			}else{
				condition = " WHERE AGR_NO='"+agr_no+"'";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryFirst(modelId, null, condition, connection);
			
			if(null == kColl || null == kColl.getDataValue("pro_no") ||"".equals(kColl.getDataValue("pro_no"))){
				kColl.setDataValue("serno", serno_value);
				
				//生成合作方项目编号
				String pro_no = CMISSequenceService4JXXD.querySequenceFromDB("PROJECT", "all",connection, context);
				kColl.setDataValue("pro_no", pro_no);
				
				context.addDataField("action", "addLmtCoopMachineRecord.do");
			}else{
				context.addDataField("action", "updateLmtCoopMachineRecord.do");
				
				//根据合作方机械设备项目编号得到拟按揭设备信息
				IndexedCollection iColl = dao.queryList("LmtSchedEquip", " WHERE PRO_NO='"+kColl.getDataValue("pro_no")+"'", connection);
				iColl.setName("LmtSchedEquipList");
				this.putDataElement2Context(iColl, context);
			}
			
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
