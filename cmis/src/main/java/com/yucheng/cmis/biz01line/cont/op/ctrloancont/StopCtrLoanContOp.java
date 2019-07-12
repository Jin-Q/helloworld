package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;
import java.sql.SQLException;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 
 * @author jwang
 *  需求编号:XD141222087,法人账户透支需求变更 
 */
public class StopCtrLoanContOp extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no_value = null;
			String cus_id=null;
			String prd_id=null;
			try {
				cont_no_value = (String)context.getDataValue("cont_no");
				cus_id = (String)context.getDataValue("cus_id");
				prd_id = (String)context.getDataValue("prd_id");
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of [cont_no] cannot be null!");
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of [cus_id]  cannot be null!");
			if(prd_id == null || prd_id.length() == 0)
				throw new EMPJDBCException("The value of [prd_id]  cannot be null!");
			
			KeyedCollection resqkColl=new KeyedCollection();
			resqkColl.addDataField("CLIENT_NO",cus_id);
			resqkColl.addDataField("OPERATION_TYPE","30");//10-冻结  20-解冻 30-终止
			resqkColl.addDataField("CONTRACT_NO",cont_no_value);
			
			if("100051".equals(prd_id)){
				resqkColl.addDataField("APPLY_TYPE","003");//001 个人自助 002 小微自助  003 法人透支
			/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 begin*/
			}else if("100088".equals(prd_id)){
				resqkColl.addDataField("APPLY_TYPE","002");//001 个人自助 002 小微自助  003 法人透支
			/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 end*/
			}else{
				throw new Exception("该合同不能做合同终止！");
			}
			
			/**调用ESB接口，发送报文*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 begin*/
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			if ("100088".equals(prd_id)) {
				IndexedCollection pvpIColl = dao.queryList("PvpLoanApp"," where cont_no='" + cont_no_value+ "' and approve_status='997'", connection);// 查询出账信息
				if (pvpIColl != null && pvpIColl.size() > 0) {
					KeyedCollection retKColl = serviceRel.trade0200200000502(resqkColl, context, connection);
					if (TagUtil.haveSuccess(retKColl, context)) {
						KeyedCollection back_kColl = (KeyedCollection) retKColl.getDataElement(TradeConstance.ESB_BODY);
						String loanStatus = TagUtil.replaceNull4String(back_kColl.getDataValue("LOAN_STATUS"));
						if ("40".equals(loanStatus)) {
							KeyedCollection cont = dao.queryDetail(modelId,cont_no_value, connection);
							cont.put("cont_status", "900");
							dao.update(cont, connection);
							context.put("flag", "success");
							context.put("msg", "合同终止成功!");
						} else {
							context.put("flag", "error");
							context.put("msg", "合同终止失败!原因：合同到期但未还清！");
						}
					}
				}else{
					context.put("flag", "error");
					context.put("msg", "合同终止失败!该合同不存在出账成功的出账申请！");
				}
			} else {
				KeyedCollection retKColl = serviceRel.trade0200200000503(resqkColl, context, connection);
				if (TagUtil.haveSuccess(retKColl, context)) {
					KeyedCollection back_kColl = (KeyedCollection) retKColl.getDataElement(TradeConstance.ESB_BODY);
					String loanStatus = TagUtil.replaceNull4String(back_kColl.getDataValue("LOAN_STATUS"));
					if ("40".equals(loanStatus)) {
						KeyedCollection cont = dao.queryDetail(modelId,cont_no_value, connection);
						cont.put("cont_status", "900");
						dao.update(cont, connection);
						context.put("flag", "success");
						context.put("msg", "合同终止成功!");
					} else {
						context.put("flag", "error");
						context.put("msg", "合同终止失败!原因：合同到期但未还清！");
					}

				}
				/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 end*/
			}

		} catch(Exception e){
			context.put("flag", "error");
			context.put("msg", "合同终止失败!原因:"+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
