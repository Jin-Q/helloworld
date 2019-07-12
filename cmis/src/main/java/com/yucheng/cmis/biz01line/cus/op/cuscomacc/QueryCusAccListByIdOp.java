package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusAccListByIdOp extends CMISOperation {
	// ��Ҫ����ı�ģ��
	private String modelId = "CusComAcc";

	private boolean updateCheck = false;

	/**
	 * ִ�в�ѯ��ϸ��Ϣ����
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			String flag_entr = "";// 委托标志
			String cusId = null;
//			String accType = ""; // 1 ： 结算账号， 2: 保证金账号 3: 委托
			String cont_no = "";
			String entrust_fee_payer = "";// 委托贷款手续费支付主体
			TableModelDAO dao = this.getTableModelDAO(context);

			try {
				flag_entr = (String) context.getDataValue("flag_entr");
//				accType = (String) context.getDataValue("accType");
				if ("2".equals(flag_entr)) {
					cusId = (String) context.getDataValue("cusId");

				/**
				 * 委托客户ID,根据合同号从委托贷款从表信息中获取 
				 * 委托存款帐号 -委托人
				 * 委托贷款手续费账号：根据支付主体判断是委托人支付还是借款人支付
				 * 
				 */
				} else if ("1".equals(flag_entr)) {
					cont_no = (String) context.getDataValue("contNo");
					IndexedCollection iColl_entr = dao.queryList(
							"CtrComLoanEntr", "where cont_no ='" + cont_no
									+ "'", connection);

					if (iColl_entr == null || iColl_entr.size() <= 0) {
						throw new EMPException("无委托人信息！");
					} else if (iColl_entr.size() > 1) {
						throw new EMPException("数据异常：委托合同" + cont_no
								+ "向下存在多个委托人情况!");
					} else {
						/*if ("1".equals(accType)) {
							entrust_fee_payer = (String) context
									.getDataValue("entrust_fee_payer");
							cusId = "01".equals(entrust_fee_payer) ? ((KeyedCollection) iColl_entr
									.get(0)).getDataValue("principal_cus_id")
									.toString()
									: (String) context.getDataValue("cusId");
						} else {// 委托存款帐号
							cusId = (String)((KeyedCollection) iColl_entr.get(0))
									.getDataValue("principal_cus_id");
						}*/
					}
				}

			} catch (Exception e) {
			}
			if (cusId == null || cusId.equals(""))
				throw new EMPJDBCException("客户码不可以为空！");

			/*
			 * CusComAccComponent cusComAccComponent = (CusComAccComponent)
			 * CMISComponentFactory
			 * .getComponentFactoryInstance().getComponentInstance(
			 * modelId,context,connection); ComponentHelper componetHelper = new
			 * ComponentHelper(); List<CMISDomain> cusComAccList
			 * =cusComAccComponent.findCusAccListDomain(cusId);
			 * IndexedCollection iColl=new IndexedCollection("CusComAccList");
			 * if(cusComAccList==null||cusComAccList.size()<=0){ throw new
			 * EMPException("请先在客户 "+cusId+" 的综合信息中维护该客户的结算帐户登记簿!"); }else{
			 * CusComAcc cusComAcc=null; KeyedCollection kColl=new
			 * KeyedCollection(modelId);
			 * 
			 * for(int i=0;i<cusComAccList.size();i++){ cusComAcc=(CusComAcc)
			 * cusComAccList.get(i); kColl=
			 * componetHelper.domain2kcol(cusComAcc, modelId);
			 * iColl.addDataElement(kColl); } }
			 * 
			 * 
			 * this.putDataElement2Context(iColl, context);
			 */

			String condition = " where cus_id='" + cusId + "' ";
			IndexedCollection iColl = dao.queryList(modelId, condition,
					connection);
			iColl.setName(iColl.getName() + "List");
			this.putDataElement2Context(iColl, context);
			SInfoUtils.addSOrgName(iColl, new String[]{"acc_open_org","acc_org"});

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
