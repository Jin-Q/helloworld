package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;

public class LmtAppIndivFrozenFlowImpl extends CMISComponent implements BIZProcessInterface {

	private static final String LMTAPPDETAILS = "LmtAppDetails";//授信分项申请表
	private static final String LMTAGRDETAILS = "LmtAgrDetails";//授信分项台账表
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();

			String serno_value = wfiMsg.getPkValue();
			String table = wfiMsg.getTableName();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(table, serno_value, connection);
			String app_type = (String) kColl.getDataValue("app_type");
			
			String condition = "where serno = '"+serno_value+"'";
			IndexedCollection iColl = dao.queryList(LMTAPPDETAILS, condition, connection);
			
			if(app_type.equals("03")){//冻结
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					if(detkColl.getDataValue("froze_amt")!=null&&!"".equals(detkColl.getDataValue("froze_amt"))){
						String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
						BigDecimal det_froze_amt = new BigDecimal(detkColl.getDataValue("froze_amt")+"");
						KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
						String froze_amt_str = (String) argKcoll.getDataValue("froze_amt");
						BigDecimal froze_amt = new BigDecimal("0");
						if(froze_amt_str!=null&&!froze_amt_str.equals("")){
							froze_amt = new BigDecimal(froze_amt_str);
						}
						froze_amt=froze_amt.add(det_froze_amt);
						argKcoll.setDataValue("froze_amt", froze_amt);
						dao.update(argKcoll, connection);
					}
				}
			}else if(app_type.equals("04")){//解冻
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					if(detkColl.getDataValue("unfroze_amt")!=null&&!"".equals(detkColl.getDataValue("unfroze_amt"))){
						String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
						BigDecimal det_unfroze_amt = new BigDecimal(detkColl.getDataValue("unfroze_amt")+"");
						KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
						BigDecimal froze_amt = new BigDecimal(argKcoll.getDataValue("froze_amt")+"");
						froze_amt=froze_amt.subtract(det_unfroze_amt);
						argKcoll.setDataValue("froze_amt", froze_amt);
						dao.update(argKcoll, connection);
					}
				}
			}else{
				throw new EMPException("非冻结解冻类型！");
			}
			
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("单一法人授信流程审批报错，错误描述："+e.getMessage());
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
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		Map<String, String> param = new HashMap<String, String>();
		param.put("cus_id", cus_id);
		param.put("orgid", manager_br_id);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("bizline", "BL300");
		return param;
	}

}
