package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAccAccpShowBasicOp extends CMISOperation {

	private final String modelId = "IqpAccAccp";
	private final String modelIdCont = "CtrLoanCont";
	private final String modelIdContSub = "CtrLoanContSub";
	private final String modelIdContDisc = "CtrDiscCont";
	private final String modelIdPvp = "PvpLoanApp";

	private final String serno_name = "serno";
	private final String cont_no_name = "cont_no";
	private final String cus_id = "cus_id";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";
			String conditionSub="";

			String serno_value = null;
			String cont_no_value = null;
			String cus_id = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				cont_no_value = (String)context.getDataValue(cont_no_name);
				cus_id = (String)context.getDataValue(cus_id);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			//KeyedCollection kCollCont = dao.queryAllDetail(modelId, cont_no_value, connection);
			condition = " where serno = '"+serno_value+"'";
			conditionSub = " where cont_no = '"+cont_no_value+"'";
			KeyedCollection kCollCont = dao.queryFirst(modelIdCont, null, condition, connection);
			KeyedCollection kCollContSub = dao.queryFirst(modelIdContSub, null, conditionSub, connection);
			KeyedCollection kCollContDisc = dao.queryFirst(modelIdContDisc, null, conditionSub, connection);
			KeyedCollection kCollPvp = dao.queryFirst(modelIdPvp, null, conditionSub, connection);
			
			//翻译还款方式
			String[] args_repay=new String[] {"repay_type"};
			String[] modelIds_repay=new String[]{"PrdRepayMode"};
			String[]modelForeign_repay=new String[]{"repay_mode_id"};
			String[] fieldName_repay=new String[]{"repay_mode_dec"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kCollContSub, args_repay, SystemTransUtils.ADD, context, modelIds_repay,modelForeign_repay, fieldName_repay);
			
			//翻译客户
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kCollCont, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kCollCont, context);
			this.putDataElement2Context(kCollContSub, context);
			this.putDataElement2Context(kCollContDisc, context);
			this.putDataElement2Context(kCollPvp, context);
			
//			condition=" where serno='"+serno_value+"'";
//			IndexedCollection iColl_IqpAccpDetail = dao.queryList("IqpAccpDetail",condition, connection);
//			this.putDataElement2Context(iColl_IqpAccpDetail, context);
			
			String prd_id = (String) kCollCont.getDataValue("prd_id");
			context.put("prd_id", prd_id);
			
			//如果为保函业务则需要查询保函信息表   2014-09-22 唐顺岩
			if("400020".equals(prd_id) || "400021".equals(prd_id)){
				KeyedCollection IqpGuarantInfo_kcoll = dao.queryFirst("IqpGuarantInfo", null, condition, connection);
				this.putDataElement2Context(IqpGuarantInfo_kcoll, context);
			}
			
			//贴现业务时查询利息支付方式
			if("300020".equals(prd_id) || "300021".equals(prd_id)){
				KeyedCollection kcoll = (KeyedCollection)SqlClient.queryFirst("queryDscntIntPayModeByContNo", cont_no_value, null, connection);
				if(null!=kcoll){
					int cont = Integer.parseInt(kcoll.getDataValue("cont")+"");
					if(cont==1){  //如果返回的条数等于1则表示利息支付方式相同，直接将利息支付方式返回。
						kCollContDisc.put("dscnt_int_pay_mode", kcoll.getDataValue("dscnt_int_pay_mode"));
					}
				}
				
			}
			
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
