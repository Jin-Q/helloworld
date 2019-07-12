package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpAccAccpIqpAccpDetailRecordOp extends CMISOperation {
	 
	private final String modelId = "IqpAccpDetail";
	private final String modelIdAcc = "IqpAccAccp";

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
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values cannot be empty!");
			
			kColl.put("clt_acct_no",kColl.getDataValue("clt_acct_no").toString().trim());
			
			TableModelDAO dao = this.getTableModelDAO(context);
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
            	/*往银行承兑汇票中插入汇票金额和汇票数量*/
    			String serno = (String)kColl.getDataValue("serno");
    			String condition = "where serno='"+serno+"'";
    			dao.insert(kColl, connection);
    			
    			IndexedCollection iColl =  dao.queryList(modelId, condition, connection);
    			KeyedCollection kCollAcc = dao.queryDetail(modelIdAcc, serno, connection);
    			String sernoSelect = (String)kCollAcc.getDataValue("serno");
    			if(sernoSelect!=null && !"".equals(sernoSelect)){
    				kCollAcc.setDataValue("bill_qty", (iColl.size())); 
    				dao.update(kCollAcc, connection); 
    			}else{
    				throw new EMPException("先录入票据信息，再录入票据明细!");
    			}
    			context.put("flag", "success");
    			context.put("message", "success");
            }else{
            	context.put("flag", "error");
            	context.put("message", "非电子票据，票据期限应在6个月以内！");
            }
			
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
