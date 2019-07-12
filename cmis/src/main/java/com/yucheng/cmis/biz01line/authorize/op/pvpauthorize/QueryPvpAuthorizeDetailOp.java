package com.yucheng.cmis.biz01line.authorize.op.pvpauthorize;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPvpAuthorizeDetailOp  extends CMISOperation {
	
	private final String modelId = "PvpAuthorize";
	private final String modelIdCtr = "CtrLoanCont";

	private final String transerno_name = "tran_serno";
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);
			
			String tran_serno = null;
			try {
				tran_serno = (String)context.getDataValue(transerno_name);
			} catch (Exception e) {}
			if(tran_serno == null || tran_serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+transerno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, tran_serno, connection);
			
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"}; 
			String[] fieldName=new String[]{"prdname"};    
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","in_acct_br_id"});
			
			String cont_no = (String)kColl.getDataValue("cont_no");
			String prd_id = (String)kColl.getDataValue("prd_id");
			String biz_type = null;
			if(!"600020".equals(prd_id) && !"300022".equals(prd_id) && !"300023".equals(prd_id) && !"300024".equals(prd_id)){
				KeyedCollection contKColl = dao.queryAllDetail(modelIdCtr, cont_no, connection);
				biz_type = (String)contKColl.getDataValue("biz_type");
			    context.put("biz_type", biz_type);
			}
			/**add by lisj 2015-3-23 需求编号:【XD150303017】关于资产证券化的信贷系统改造需求  begin**/
			if("600022".equals(prd_id)){
				KeyedCollection kCollIqpAsset = dao.queryFirst("AccLoan", null, "WHERE BILL_NO='"+kColl.getDataValue("bill_no")+"'", connection);
				String cus_id = (String)kCollIqpAsset.getDataValue("cus_id");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
				kColl.setDataValue("cus_id", cus_id);
				kColl.setDataValue("cus_name", cus.getCusName());
			}
			/**add by lisj 2015-3-23 需求编号:【XD150303017】关于资产证券化的信贷系统改造需求  end**/
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
