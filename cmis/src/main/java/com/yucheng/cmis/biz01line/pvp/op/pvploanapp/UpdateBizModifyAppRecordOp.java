package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class UpdateBizModifyAppRecordOp extends CMISOperation {

	private final String modelId = "CtrLoanContTmp";
	private final String modelHisId = "CtrLoanContHis";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			String repay_type = "";//还款方式
			if(context.containsKey("repay_type")){
				repay_type = (String)context.getDataValue("repay_type");
			}
			String pay_type = "";//还款方式
			if(context.containsKey("pay_type")){
				pay_type = (String)context.getDataValue("pay_type");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//保存前先备份ctr_loan_cont_his,ctr_loan_cont_sub_his

			String modify_rel_serno  = (String) context.getDataValue("modify_rel_serno");
			KeyedCollection kColl4His = dao.queryDetail(modelId, modify_rel_serno, connection);
			kColl4His.setName(modelHisId);
			KeyedCollection kColl4SubHis = dao.queryDetail("CtrLoanContSubTmp", modify_rel_serno, connection);
			kColl4SubHis.setName("CtrLoanContSubHis");
			//查询是否已存在备份信息
			IndexedCollection checkCLCH = dao.queryList(modelHisId, "where modify_rel_serno ='"+modify_rel_serno+"'", connection);
			if(checkCLCH ==null || checkCLCH.size()<=0){
				try {
					dao.insert(kColl4His, connection);
					dao.insert(kColl4SubHis, connection);
				} catch (Exception e) {}
			}
			KeyedCollection kCollSubTmp = (KeyedCollection)kColl.getDataElement("CtrLoanContSubTmp");
			//新增当更改期限需初始化还款计划提醒信息
			if(repay_type!=null && !"".equals(repay_type) && "A001".equals(repay_type)){//自由还款法
				KeyedCollection kColl4CLC = dao.queryAllDetail(modelId, modify_rel_serno, connection);
				KeyedCollection kColl4CLCS = (KeyedCollection) kColl4CLC.getDataElement("CtrLoanContSubTmp");
				String pre_cont_term = (String) kColl4CLCS.getDataValue("cont_term");
				String pre_term_type = (String) kColl4CLCS.getDataValue("term_type");
				String cont_term = (String) kCollSubTmp.getDataValue("cont_term");
				String term_type = (String) kCollSubTmp.getDataValue("term_type");
				if(pre_cont_term.equals(cont_term)  && pre_term_type.equals(term_type)){
					context.addDataField("msg", "unchanged");
				}else{
					context.addDataField("msg", "changed");
				}
			}else{
				KeyedCollection delKColl = new KeyedCollection("delKColl");
				delKColl.addDataField("modify_rel_serno", modify_rel_serno);
				SqlClient.delete("deleteIqpFreedomPayInfoTmpByM", delKColl, connection);
				
				context.addDataField("msg", "unchanged");
			}
			//执行打回业务信息修改操作，仅更新可修改的字段信息
			try {
				int count = dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("执行打回业务信息修改（缓存主表）操作出错，失败行数: " + count);
				}
				kCollSubTmp.put("modify_rel_serno", modify_rel_serno);
				int count4sub = dao.update(kCollSubTmp, connection);
				if(count4sub!=1){
					throw new EMPException("执行打回业务信息修改（缓存从表）操作出错，失败行数: " + count4sub);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//校验支付方式当为受托支付变更为自主支付方式时，需删除账户信息中的受托账号信息
			if(!"".equals(pay_type) && "0".equals(pay_type)){//如果为自主支付，则剔除受托账户
				IndexedCollection iColl4ICAT = dao.queryList("IqpCusAcctTmp", "where modify_rel_serno='"+modify_rel_serno+"'", connection);
				if(iColl4ICAT!=null && iColl4ICAT.size()>0){
					//先清空缓存账户信息
					KeyedCollection delKColl = new KeyedCollection("delKColl");
					delKColl.addDataField("modify_rel_serno", modify_rel_serno);
					try {
						SqlClient.delete("deleteIqpCusAcctTmpByM", delKColl, connection);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					for(int i=0;i<iColl4ICAT.size();i++){
						KeyedCollection temp = (KeyedCollection) iColl4ICAT.get(i);
						String acct_attr = (String) temp.getDataValue("acct_attr");
						if(!"04".equals(acct_attr)){//受托账户
							dao.insert(temp, connection);
						}
					}
				}	
			}

			context.addDataField("flag", "success");
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
