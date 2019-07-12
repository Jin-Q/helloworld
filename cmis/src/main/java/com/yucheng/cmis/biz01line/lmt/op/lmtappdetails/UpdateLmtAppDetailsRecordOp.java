package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateLmtAppDetailsRecordOp extends CMISOperation {
	
	private final String modelId = "LmtAppDetails";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			String belgLine = "";//所属条线
			String app_type = "";//申请类型
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","保存授信分项信息失败，保存的模型数据为空。");
				return "0";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			if(kColl.containsKey("app_type")){
				app_type = (String) kColl.getDataValue("app_type");
			}
			
			//冻结解冻时不需要更新基表数据
			if(!"03".equals(app_type)&&!"04".equals(app_type)){
				//如果是变更并且将是否调整期限改为否，需从原台账实时查询原有的授信期限
				if("02".equals(kColl.getDataValue("update_flag")) && "2".equals(kColl.getDataValue("is_adj_term"))){
					KeyedCollection kcoll_agrdetails = dao.queryDetail("LmtAgrDetails", (String)kColl.getDataValue("org_limit_code"), connection);
					kColl.setDataValue("term_type", kcoll_agrdetails.getDataValue("term_type"));
					kColl.setDataValue("term", kcoll_agrdetails.getDataValue("term"));
				}
				
				//所属条线
				belgLine = kColl.getDataValue("belg_line").toString();
				//更新授信申请中的金额
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				if(belgLine!=null&&"BL300".equals(belgLine)){
					lmtComponent.updateLmtAppIndivAmt(kColl.getDataValue("serno").toString());//根据流水号更新个人授信申请基表数据
				}else{
					//lmtComponent.updateLmtApplyAmt(kColl.getDataValue("serno").toString(),(String)kColl.getDataValue("lmt_type"),(String)kColl.getDataValue("lrisk_type"));  //根据流水号更新授信申请基表数据
					lmtComponent.updateLmtApplyAmt(kColl.getDataValue("serno").toString(),(String)kColl.getDataValue("lrisk_type"));  //根据流水号更新授信申请基表数据
				}
			}
			context.addDataField("flag","success");
			context.addDataField("msg","success");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("msg","保存授信分项信息失败，错误描述："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			//throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
