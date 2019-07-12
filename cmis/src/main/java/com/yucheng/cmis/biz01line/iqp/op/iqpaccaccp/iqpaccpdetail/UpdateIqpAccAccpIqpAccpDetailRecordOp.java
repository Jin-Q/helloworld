package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateIqpAccAccpIqpAccpDetailRecordOp extends CMISOperation {
	
	private final String modelId = "IqpAccpDetail";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			int compare = 0;
			String is_elec_bill = null;	
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				is_elec_bill = (String)context.getDataValue("is_elec_bill");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			
			kColl.put("clt_acct_no",kColl.getDataValue("clt_acct_no").toString().trim());
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String cont="";
			if(context.containsKey("cont")){
				cont = (String)context.getDataValue("cont");
			}
			String modify_rel_serno = "";
			
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			
			//修改前备份明细信息
			if(cont!=null && !"".equals(cont) && "modify".equals(cont)){
				IndexedCollection backUpIColl = dao.queryList("IqpAccpDetailTmp", "where serno ='"+(String) kColl.getDataValue("serno")+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);//原票据明细列表
				IndexedCollection IADH = dao.queryList("IqpAccpDetailHis", " where modify_rel_serno ='"+modify_rel_serno+"'", connection);
				if(IADH == null || IADH.size()<=0){
					if(backUpIColl !=null && backUpIColl.size()>0){
						for(int i=0;i<backUpIColl.size();i++){
							KeyedCollection temp = (KeyedCollection) backUpIColl.get(i);
							temp.put("modify_rel_serno", modify_rel_serno);
							temp.setName("IqpAccpDetailHis");
							dao.insert(temp, connection);
						}
					}
				}
				kColl.setName("IqpAccpDetailTmp");
			}
			String term_type = (String)kColl.getDataValue("term_type");
			if("2".equals(is_elec_bill)){
				if("003".equals(term_type)){
					String systemDate = (String)context.getDataValue("OPENDAY");
					int term = Integer.parseInt(kColl.getDataValue("term").toString());
					String input = this.compareDate(systemDate, term);
					String sys = this.compareMou(systemDate);
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date inputDate = dateFormat.parse(input);
					Date sysDate = dateFormat.parse(sys);
					compare = inputDate.compareTo(sysDate);
				}
			}
			if(compare <= 0){
            	context.addDataField("message", "success");
            	if(cont!=null && !"".equals(cont) && "modify".equals(cont)){
            		Map<String,String> ValueMap = new HashedMap();
    				ValueMap.put("clt_person", (String) kColl.getDataValue("clt_person"));
    				ValueMap.put("term_type", (String) kColl.getDataValue("term_type"));
    				ValueMap.put("term", (String) kColl.getDataValue("term"));
    				KeyedCollection paramKcoll = new KeyedCollection();
					paramKcoll.put("modify_rel_serno", modify_rel_serno);
					paramKcoll.put("pk1", (String)kColl.getDataValue("pk1"));
    				try {
    					int count = SqlClient.update("updateIqpAccpDetailTmpByM", paramKcoll, ValueMap, null, connection);
    					if(count!=1){
    						throw new EMPException("Update Failed! Record Count: " + count);
    					}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
            	}else{
            		int count=dao.update(kColl, connection);
        			if(count!=1){
        				throw new EMPException("Remove Failed! Record Count: " + count);
        			}
            	}        	
    			context.addDataField("flag", "success");
            }else{
            	context.addDataField("flag", "error");
            	context.addDataField("message", "非电子票据，票据期限应在6个月以内！");
            }	
		
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("message", ee.getMessage());
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	public String compareDate(String systemDate,int term) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar fromCal = Calendar.getInstance();
	    
	    Date date = dateFormat.parse(systemDate);
	    fromCal.setTime(date);
	    fromCal.add(Calendar.DATE, term);
	    String da = dateFormat.format(fromCal.getTime());
		return da;
	}
	public String compareMou(String systemDate) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar fromCal = Calendar.getInstance();
		
		Date date = dateFormat.parse(systemDate);
		fromCal.setTime(date);
		fromCal.add(Calendar.MONTH, 6);
		String daa = dateFormat.format(fromCal.getTime());
		return daa;
	}
}
