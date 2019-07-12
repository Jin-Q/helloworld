package com.yucheng.cmis.biz01line.iqp.op.iqpgreendeclinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpGreenDeclInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpGreenDeclInfo";
	private final String modelIdLmt = "LmtAgrDetails";
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			String limit_acc_no = null;
			String cont_no = "";
			if(context.containsKey("serno")){
				serno_value = (String)context.getDataValue(serno_name);
			} 
			if(context.containsKey("cont_no")){
		    	cont_no = (String)context.getDataValue("cont_no");
		    }
            String condition = "";
            if(!"".equals(cont_no) && cont_no != null){
            	condition = "where cont_no= '"+cont_no+"'"; 
		    }else{
		    	condition = "where serno= '"+serno_value+"'"; 
		    }     
			
			try {
				limit_acc_no = (String)context.getDataValue("limit_acc_no");
			} catch (Exception e) {}
			if(limit_acc_no == null || limit_acc_no.length() == 0){
				throw new EMPJDBCException("The value limit_acc_no cannot be null!");
			}
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kCollLmt = dao.queryDetail(modelIdLmt, limit_acc_no, connection);
			String green_indus = (String)kCollLmt.getDataValue("green_indus");
			KeyedCollection kColl = dao.queryFirst(modelId, null, condition, connection);
			kColl.put("green_indus", green_indus);
			
			if(green_indus!=null && !"".equals(green_indus)){
				/** 翻译字典项 */
				Map<String,String> map = new HashMap<String, String>();
				map.put("green_indus", "STD_ZB_GREEN_INDUS");
				
				CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				SInfoUtils.addPopName(kColl, map, service);
			}else{
				kColl.put("green_indus", "2");
				kColl.put("green_indus_displayname", "否");
			}
			//如果IqpGreenDeclInfo中不存在数据，则保存
			String serno = (String)kColl.getDataValue("serno");
			if(("".equals(serno) || serno == null) && (green_indus == null || "".equals(green_indus) || "2".equals(green_indus))){
				kColl.put("green_indus", "2");
				kColl.put("serno", serno_value);
				if(cont_no != null && !"".equals(cont_no)){
					kColl.put("cont_no", cont_no);
				}
				dao.insert(kColl, connection);
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
