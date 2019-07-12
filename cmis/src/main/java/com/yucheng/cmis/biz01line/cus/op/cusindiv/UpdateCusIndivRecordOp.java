package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusIndivRecordOp extends CMISOperation {

	private final String modelId = "CusIndiv";
	private final String modelIdBase = "CusBase";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			KeyedCollection kCollBase = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollBase = (KeyedCollection) context.getDataElement(modelIdBase);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0 ||kCollBase==null||kCollBase.size()==0)
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
			

			String cusId = (String) kCollBase.getDataValue("cus_id");
			kColl.addDataField("cus_id", cusId);
			//=========修改日志记录 add by xukaixi========================
			TableModelDAO dao = this.getTableModelDAO(context);

			KeyedCollection oldKColl = dao.queryAllDetail(modelId, cusId, connection);
			KeyedCollection oldKCollBase = dao.queryAllDetail(modelIdBase, cusId, connection);
			//历史记录
			ModifyHistoryComponent historyComponent = (ModifyHistoryComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT,context, connection);
			HttpServletRequest request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
			String userIP = request.getRemoteAddr();
			//保存indiv表历史
			KeyedCollection oldKColl4Save = historyComponent.getCfgedKColl(modelId, (KeyedCollection)oldKColl.clone());
			KeyedCollection newKColl4Save = historyComponent.getCfgedKColl(modelId, (KeyedCollection)kColl.clone());
			if((oldKColl4Save!=null&&oldKColl4Save.size()>0)||(newKColl4Save!=null&&newKColl4Save.size()>0)){
				historyComponent.recordHistoryModify(oldKColl4Save, newKColl4Save, modelId, userIP);
			}
			//保存base表历史
			KeyedCollection oldKCollBase4Save = historyComponent.getCfgedKColl(modelIdBase, (KeyedCollection)oldKCollBase.clone());
			KeyedCollection newKCollBase4Save = historyComponent.getCfgedKColl(modelIdBase, (KeyedCollection)kCollBase.clone());
			if((oldKCollBase4Save!=null&&oldKCollBase4Save.size()>0)||(newKCollBase4Save!=null&&newKCollBase4Save.size()>0)){
				historyComponent.recordHistoryModify(oldKCollBase4Save, newKCollBase4Save, modelIdBase, userIP);
			}
//			historyComponent.recordHistoryModify((KeyedCollection) oldKColl
//			.clone(), (KeyedCollection) kColl.clone(), modelId, userIP);
			//============end  add====================

			kCollBase.addDataField("last_update_date", context.getDataValue("OPENDAY"));
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			ComponentHelper cHelper = new ComponentHelper();
			CusIndiv cusIndiv = new CusIndiv();
			
			cusIndiv = (CusIndiv) cHelper.kcolTOdomain(cusIndiv, kColl);
			
			/**
			 * 如果客户是离婚或者丧偶，无条件删除CUS_INDIV_SOC_REL的信息
			 */
//			CusLoanRelComponent cusLoanRelComponent = (CusLoanRelComponent) CMISComponentFactory
//					.getComponentFactoryInstance().getComponentInstance(
//							PUBConstant.CUSLOANREL, context, connection);
//			
//			String loanrelMainBrId =  cusIndiv.getMainBrId();
//			if(loanrelMainBrId==null) loanrelMainBrId="";
//			if(loanrelMainBrId.equals("")) loanrelMainBrId=(String)context.getDataValue(PUBConstant.organNo);
//			CusLoanRel cusLoanRel = cusLoanRelComponent.findCusLoanRel(cusIndiv.getInnerCusId(),loanrelMainBrId);
//			if (cusLoanRel == null) {
//				cusLoanRelComponent.addCusLoanRel(cusIndiv, modelId);
//			} else {
//				cusLoanRel.setBankFlg("1");
//				cusLoanRel.setAgriFlg(cusIndiv.getAgriFlg());// 是否农户
//				cusLoanRel.setCusName(cusIndiv.getCusName());// 客户名称
//				cusLoanRel.setCusType(cusIndiv.getCusType());// 客户类型
//				cusLoanRel.setCertType(cusIndiv.getCertType());// 证件类型
//				cusLoanRel.setCertCode(cusIndiv.getCertCode());// 证件号码
//				cusLoanRel.setMainCusMgr(cusIndiv.getCustMgr());// 主管客户经理
//				cusLoanRel.setTruCusMgr(cusIndiv.getCustMgr());// 共享客户经理
//				cusLoanRel.setAreaCode(cusIndiv.getAreaCode());// 区域编号
//				cusLoanRel.setAreaName(cusIndiv.getAreaName());// 区域名称
//				cusLoanRel.setOutCusId(cusIndiv.getCusId());// 外部客户代码
//				cusLoanRel.setCusStatus(cusIndiv.getCusStatus());// 客户操作状态
//				cusLoanRelComponent.updateCusLoanRel(cusLoanRel, modelId);
//			}
			
			//===========add by xukaixi===========================
			
//			TableModelDAO daoo = this.getTableModelDAO(context);
//			KeyedCollection cus_base = new KeyedCollection();
//			cus_base.addDataField("cus_id", cusIndiv.getInnerCusId());
//			cus_base.addDataField("cus_type", cusIndiv.getCusType());
//			//cus_base.addDataField("open_date", context.getDataValue(PUBConstant.OPENDAY));
//
//			cus_base.setName("CusBase");
//			daoo.update(cus_base, connection);
//			KeyedCollection cusIndiv2 = new KeyedCollection();
//			cusIndiv2.addDataField("cus_id", cusIndiv.getInnerCusId());
//			cusIndiv2.addDataField("main_br_id", context.getDataValue(PUBConstant.organNo));
//			cusIndiv2.addDataField("input_id", context.getDataValue(PUBConstant.loginuserid));
//			cusIndiv2.addDataField("cust_mgr", context.getDataValue(PUBConstant.currentUserId));
//			cusIndiv2.setName("CusIndiv");
//			daoo.update(cusIndiv2, connection);
			//===============end add=====================================
			flag = "success";
			context.addDataField("flag", flag);
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
