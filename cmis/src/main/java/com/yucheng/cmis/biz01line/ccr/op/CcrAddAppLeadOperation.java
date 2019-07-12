package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CcrAddAppLeadOperation extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = this.getConnection(context);
//		String flagInfo = CMISMessage.DEFEAT;//信息编码
        String cus_id="";//客户码
        String modelno="";//模型编码
        String flag = "";//申请类型标志位
		String manager_id = "";
		String manager_br_id = "";
        try{
        	//申请类型
        	flag = (String)context.getDataValue("flag");
        	//模型ID
			modelno = (String)context.getDataValue("model_no");
			//客户码
			cus_id = (String)context.getDataValue("cus_id");
        	TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection model = dao.queryFirst("IndModel", null, " where model_no = '"+modelno+"'", connection);
			context.addDataField("model_name", model.getDataValue("model_name").toString());
			//调用客户模块接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			if(flag.equals("3")){//同业客户
				KeyedCollection cusSameOrg = service.getCusSameOrgKcoll(cus_id, context, connection);
				context.addDataField("cus_name",cusSameOrg.getDataValue("same_org_cnname"));//客户名称
				context.addDataField("cus_type",cusSameOrg.getDataValue("same_org_type"));//客户类型CRD_GRADE
				context.addDataField("cus_crd_grade",cusSameOrg.getDataValue("crd_grade"));//上次信用评级
				String eval_maturity = (String) cusSameOrg.getDataValue("eval_maturity");
				String lastYear = "";
				if(eval_maturity!=null && !"".equals(eval_maturity)){
					lastYear = Integer.toString((Integer.parseInt(eval_maturity.substring(0,4))-1))+eval_maturity.substring(4);
				}
				context.addDataField("com_crd_dt",lastYear);//上次评级时间	
			}else if(flag.equals("4")){//融资性担保公司
				//从客户表获取相关信息
				CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
				context.addDataField("cus_name", cus.getCusName());//客户名称
				context.addDataField("cus_type", cus.getCusType());//客户类型
				context.addDataField("cus_crd_grade",cus.getGuarCrdGrade());//上次信用评级
				String eval_maturity = cus.getCusCrdDt();
				String lastYear = "";
				if(eval_maturity!=null && !"".equals(eval_maturity)){
					lastYear = Integer.toString((Integer.parseInt(eval_maturity.substring(0,4))-1))+eval_maturity.substring(4);
				}
				context.addDataField("com_crd_dt",lastYear);//上次评级时间
				manager_id = cus.getCustMgr();
				manager_br_id = cus.getMainBrId();
			}else{
				//从客户表获取相关信息
				CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
				context.addDataField("cus_name", cus.getCusName());//客户名称
				context.addDataField("cus_type", cus.getCusType());//客户类型
				context.addDataField("cus_crd_grade",cus.getCusCrdGrade());//上次信用评级
				String eval_maturity = cus.getCusCrdDt();
				String lastYear = "";
				if(eval_maturity!=null && !"".equals(eval_maturity)){
					lastYear = Integer.toString((Integer.parseInt(eval_maturity.substring(0,4))-1))+eval_maturity.substring(4);
				}
				context.addDataField("com_crd_dt",lastYear);//上次评级时间
				manager_id = cus.getCustMgr();
				manager_br_id = cus.getMainBrId();
			}
			
			KeyedCollection kColl = new KeyedCollection("CcrAddInf");
			kColl.put("manager_id", manager_id);
			kColl.put("manager_br_id", manager_br_id);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" });
			this.putDataElement2Context(kColl, context);
		}catch (Exception e) {
			String message = e.getMessage();
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            EMPLog.log( this.getClass().getName(), EMPLog.INFO, 0, e.toString());

			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		
		return PUBConstant.SUCCESS;
	}
}
