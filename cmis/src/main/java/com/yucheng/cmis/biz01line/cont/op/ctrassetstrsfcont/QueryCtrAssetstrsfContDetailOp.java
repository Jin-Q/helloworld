package com.yucheng.cmis.biz01line.cont.op.ctrassetstrsfcont;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCtrAssetstrsfContDetailOp  extends CMISOperation {
	
	private final String modelId = "CtrAssetstrsfCont";
	

	private final String cont_no_name = "cont_no";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
    		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
			this.putDataElement2Context(kColl, context);
			
//			/** 查询剩余额度 */
//			BigDecimal total_amt = null;//总的授信额度
//			BigDecimal remain_amount = null;//剩余授信额度
//			String limit_ind = (String)kColl.getDataValue("limit_ind");
//			if(limit_ind==null){
//				limit_ind = "0";
//			}
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//	    	LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//		    //循环剩余额度和一次性剩余额度
//	    	if(limit_ind.equals("2") || limit_ind.equals("3")){
//		    	String limit_acc_no = (String)kColl.getDataValue("limit_acc_no");
//		    	//获取总的授信额度
//		    	total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "02", connection));
//		    	//获取占用授信额度
//		    	KeyedCollection kCollRel = serviceIqp.getAgrUsedInfoByArgNo(limit_acc_no, "02", connection, context);//02-同业授信
//		    	remain_amount = caculate(total_amt, kCollRel);
//		    	context.addDataField("remain_amount", remain_amount);
//		    }
//	    	/**查询剩余额度结束*/
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
			
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
	
	/**计算剩余额度公用方法*/
	public BigDecimal caculate(BigDecimal total_amt,KeyedCollection kCollRel){
		BigDecimal lmt_amt = null;
		BigDecimal remain_amount = null;
		try {
			lmt_amt = new BigDecimal(kCollRel.getDataValue("lmt_amt")+"");
			remain_amount = total_amt.subtract(lmt_amt);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return remain_amount;
	}
}
