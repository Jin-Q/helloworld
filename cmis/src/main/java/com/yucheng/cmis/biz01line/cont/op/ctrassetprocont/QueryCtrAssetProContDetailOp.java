package com.yucheng.cmis.biz01line.cont.op.ctrassetprocont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCtrAssetProContDetailOp  extends CMISOperation {
	
	private final String modelId = "CtrAssetProCont";
	

	private final String cont_no_name = "cont_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
			KeyedCollection kColl =null;
			if(context.containsKey("cont_flag") && (context.getDataValue("cont_flag"))!=null && !"".equals(context.getDataValue("cont_flag")) 
					&& "n".equals((String) context.getDataValue("cont_flag"))){
				String prd_id = (String) context.getDataValue("prd_id");
				String bill_no = (String) context.getDataValue("bill_no");
				if(prd_id !=null && !"".equals(prd_id) && "600022".equals(prd_id)){
					KeyedCollection temp = dao.queryDetail("AccAssetTrans", bill_no, connection);
					if((temp.getDataValue("cont_no"))!=null && !"".equals(temp.getDataValue("cont_no"))){
						kColl = dao.queryDetail(modelId, temp.getDataValue("cont_no").toString().trim(), connection);
					}
				}else{
					kColl = dao.queryDetail(modelId, cont_no_value, connection);
				}
			}else{
				 kColl = dao.queryDetail(modelId, cont_no_value, connection);
			}
			/**modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造 end**/
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","pro_org"});
			
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
