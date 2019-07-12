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
public class DeleteBizModifyAppRecordOp extends CMISOperation {

	private final String modelId = "PvpBizModifyRel";
	private final String contHisMId = "CtrLoanContHis";
	private final String contSubHisMId = "CtrLoanContSubHis";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			String modify_rel_serno = null;
			String serno = null;
			String cont_no = null;
			String biz_cate = null;
			String prd_id = null;
			try {
				cont_no = (String)context.getDataValue("cont_no");
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
				serno = (String)context.getDataValue("serno");
				biz_cate = (String)context.getDataValue("biz_cate");
				prd_id = (String)context.getDataValue("prd_id");
			} catch (Exception e) {}
			if(modify_rel_serno == null || modify_rel_serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+modify_rel_serno+"] cannot be null!");
			if(cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no+"] cannot be null!");
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno+"] cannot be null!");
			if(biz_cate == null || biz_cate.length() == 0)
				throw new EMPJDBCException("The value of pk["+biz_cate+"] cannot be null!");
			if(prd_id == null || prd_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+prd_id+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);			
			//删除打回业务修改审批流程关联表信息
			int count=dao.deleteByPk(modelId, modify_rel_serno, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			if("016".equals(biz_cate)){
				//删除合同缓存表信息
				int count4IEAT=dao.deleteByPk("IqpExtensionAgrTmp", modify_rel_serno, connection);
				if(count4IEAT!=1){
					throw new EMPException("Remove Failed! Records :"+count4IEAT);
				}
				
				IndexedCollection IEA = dao.queryList("IqpExtensionAgrHis", " where  agr_no='"+cont_no+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				if(IEA!=null && IEA.size()>0){					
					//删除合同备份表信息
					int count4IEA=dao.deleteByPk("IqpExtensionAgrHis", modify_rel_serno, connection);
					if(count4IEA!=1){
						throw new EMPException("Remove Failed! Records :"+count4IEA);
					}
				}		
			}else{		
				//银行承兑汇票明细，账户信息，附加条款信息
				if(prd_id!=null && !"".equals(prd_id) && "200024".equals(prd_id)){
					IndexedCollection IADH = dao.queryList("IqpAccpDetailHis", " where modify_rel_serno='"+modify_rel_serno+"'", connection);					
					IndexedCollection PBIH = dao.queryList("PubBailInfoHis", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
					IndexedCollection IATH = dao.queryList("IqpAppendTermsHis", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
					IndexedCollection IADT = dao.queryList("IqpAccpDetailTmp", " where modify_rel_serno='"+modify_rel_serno+"'", connection);					
					IndexedCollection PBIT = dao.queryList("PubBailInfoTmp", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
					IndexedCollection IATT = dao.queryList("IqpAppendTermsTmp", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
					
					//清空汇票明细缓存信息
					if(IADT!=null && IADT.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deleteIqpAccpDetailTmpByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}						
					}
					
					//清空汇票明细备份信息
					if(IADH!=null && IADH.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deleteIqpAccpDetailHisByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//清空保证金缓存信息
					if(PBIT!=null && PBIT.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deletePubBailInfoTmpByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//清空保证金备份信息
					if(PBIH!=null && PBIH.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deletePubBailInfoHisByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//清空附加条款缓存信息
					if(IATT!=null && IATT.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deleteIqpAppendTermsTmpByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//清空附加条款备份信息
					if(IATH!=null && IATH.size()>0){
						KeyedCollection delKColl = new KeyedCollection("delKColl");
						delKColl.addDataField("modify_rel_serno", modify_rel_serno);
						try {
							SqlClient.delete("deleteIqpAppendTermsHisByM", delKColl, connection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				IndexedCollection IFPIT = dao.queryList("IqpFreedomPayInfoTmp", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
				IndexedCollection ICAT = dao.queryList("IqpCusAcctTmp", " where modify_rel_serno='"+modify_rel_serno+"'", connection);
				
				//清空还款计划登记缓存信息
				if(IFPIT!=null && IFPIT.size()>0){
					KeyedCollection delKColl = new KeyedCollection("delKColl");
					delKColl.addDataField("modify_rel_serno", modify_rel_serno);
					try {
						SqlClient.delete("deleteIqpFreedomPayInfoTmpByM", delKColl, connection);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						SqlClient.delete("deleteIqpFreedomPayInfoHisByM", delKColl, connection);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//清空账户信息缓存信息
				if(ICAT!=null && ICAT.size()>0){
					KeyedCollection delKColl = new KeyedCollection("delKColl");
					delKColl.addDataField("modify_rel_serno", modify_rel_serno);
					try {
						SqlClient.delete("deleteIqpCusAcctTmpByM", delKColl, connection);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						SqlClient.delete("deleteIqpCusAcctHisByM", delKColl, connection);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				IndexedCollection CLCT = dao.queryList("CtrLoanContTmp", " where  cont_no='"+cont_no+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				IndexedCollection CLCST = dao.queryList("CtrLoanContSubTmp", " where cont_no='"+cont_no+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				if(CLCT!=null && CLCT.size()>0){				
					//删除合同缓存表信息
					int count4CLCT=dao.deleteByPk("CtrLoanContTmp", modify_rel_serno, connection);
					if(count4CLCT!=1){
						throw new EMPException("Remove Failed! Records :"+count4CLCT);
					}
				}
				if(CLCST!=null && CLCST.size()>0){
					int count4CLCS=dao.deleteByPk("CtrLoanContSubTmp", modify_rel_serno, connection);
					if(count4CLCS!=1){
						throw new EMPException("Remove Failed! Records :"+count4CLCS);
					}
				}
				
				IndexedCollection CLCH = dao.queryList(contHisMId, " where  cont_no='"+cont_no+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				IndexedCollection CLCSH = dao.queryList(contSubHisMId, " where cont_no='"+cont_no+"' and modify_rel_serno='"+modify_rel_serno+"'", connection);
				if(CLCH!=null && CLCH.size()>0){				
					//删除合同备份表信息
					int count4CLC=dao.deleteByPk(contHisMId, modify_rel_serno, connection);
					if(count4CLC!=1){
						throw new EMPException("Remove Failed! Records :"+count4CLC);
					}
				}
				if(CLCSH!=null && CLCSH.size()>0){
					int count4CLCS=dao.deleteByPk(contSubHisMId, modify_rel_serno, connection);
					if(count4CLCS!=1){
						throw new EMPException("Remove Failed! Records :"+count4CLCS);
					}
				}
			}			
			context.addDataField("del", "success");			
		}catch (EMPException ee) {
			context.addDataField("del", "error"); 
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
