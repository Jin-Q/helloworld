package com.yucheng.cmis.biz01line.lmt.op.lmtindusagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtIndusAgrDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtIndusAgr";
	

	private final String agr_no_name = "agr_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String agr_no_value = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, agr_no_value, connection);
			if(kColl.getDataValue("belg_org") == null){
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			}else{
				SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			}
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id",});
			SInfoUtils.getPrdPopName(kColl, "suit_prd", connection);  //翻译产品
			
			String indus_amt = kColl.getDataValue("indus_amt").toString();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollTemp = service.getAgrUsedInfoByArgNo(agr_no_value, "03", connection, context);
			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
			double bal_amt = Double.parseDouble(indus_amt) - Double.parseDouble(lmt_amt);
			kColl.addDataField("bal_amt", bal_amt);			
			
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
