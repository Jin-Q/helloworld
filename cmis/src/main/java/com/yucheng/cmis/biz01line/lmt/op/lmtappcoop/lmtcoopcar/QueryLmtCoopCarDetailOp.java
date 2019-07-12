package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopcar;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtCoopCarDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtCoopCar";
	
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
				
				context.addDataField("action", "addLmtCoopCarRecord.do");
			}else{
				context.addDataField("action", "updateLmtCoopCarRecord.do");
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("agency_addr1", "STD_GB_AREA_ALL");
				map.put("agency_addr2", "STD_GB_AREA_ALL");
				map.put("agency_addr3", "STD_GB_AREA_ALL");
				
				CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				SInfoUtils.addPopName(kColl, map, service);
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
