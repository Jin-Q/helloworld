package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.component.ContComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 额度合同业务申请审批结束业务业务逻辑处理：
 * @author Pansq
 * 生成合同所需的合同表单信息，此处暂不使用产品，需手动填值，
 * 并且需要考虑是新增、还是变更，如果变更则更新合同项下信息，
 * 否则生成新的合同信息
 */

public class CtrLimitBizFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/** 流程审批通过执行的业务逻辑 */
		String serno = null;
		try {
			serno = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection appKColl = dao.queryDetail("CtrLimitApp", serno, this.getConnection());
			if(appKColl != null){
				String appType = (String)appKColl.getDataValue("app_type");
				if(appType != null && appType.trim().length() > 0){
					ContComponent contComponent = (ContComponent)CMISComponentFactory.getComponentFactoryInstance()
						.getComponentInstance("ContComponent", this.getContext(), this.getConnection());
					if("01".equals(appType)){//新增
						contComponent.addCtrLimitCont(appKColl,this.getContext(),this.getConnection(),dao);
					}else if("02".equals(appType)){//变更
						contComponent.updateCtrLimitCont(appKColl,this.getContext(),this.getConnection(),dao);
					}else {
						throw new EMPException("不属于额度合同所属业务类型，请检查字典项控制是否正确！");
					}
				}else {
					throw new EMPException("通过业务流【"+serno+"】水号获取额度合同业务申请表单数据申请类型失败！");
				}
			}else {
				throw new EMPException("通过业务流【"+serno+"】水号获取额度合同业务申请表单数据失败！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		Map<String, String> param = new HashMap<String, String>();
		String manager_id = null;
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String condition = "where is_main_manager='1' and serno='"+pkVal+"'";
		IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
		if(iqpIColl.size()>0){
			KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
			manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
		} 
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
		String IsTeam="";
		KeyedCollection kColl4STO = new KeyedCollection();
		try {
			kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
		} catch (SQLException e) {}
		if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
			IsTeam="yes";
		}else{
			IsTeam="no";
		}
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		param.put("IsTeam", IsTeam);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		for(int i=0;i<kc.size();i++){
			DataElement element = (DataElement)kc.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				String value = "";
				if(aField.getValue() == null){
					value = "";
				}else {
					value = aField.getValue().toString();
				}
				param.put(aField.getName(), value);
			}
		}
		
		return param;
	}
}
