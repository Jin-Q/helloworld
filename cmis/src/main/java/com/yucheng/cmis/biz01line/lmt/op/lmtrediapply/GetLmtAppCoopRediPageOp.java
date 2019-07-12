package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtAppCoopRediPageOp  extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoopRedi";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			//RecordRestrict recordRestrict = this.getRecordRestrict(context);
			//recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String serno_value = null;
			String isShow = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			try {
				isShow = (String)context.getDataValue("isShow");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);  //查询复议表是否存在
			if("N".equals(isShow)){
				if(null!=kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno")) && "000".equals(kColl.getDataValue("approve_status"))){
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------复议申请表中已经存在待发起申请，直接转发-------", null);
				}else{
					kColl = dao.queryDetail("LmtAppJointCoop", serno_value, connection);  //先查询出授信申请信息

					kColl.setName(modelId);
					kColl.setDataValue("approve_status", "000");
					dao.insert(kColl, connection);
				}
			}
			
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(kColl, new String[]{"input_id","manager_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"});
			if("2".equals((String)kColl.getDataValue("share_range"))){
				SystemTransUtils.containCommaORG2CN(kColl,context);
			}
			//添加字段为名单列表传值用
			kColl.addDataField("joint_serno",kColl.getDataValue("serno"));
			
			this.putDataElement2Context(kColl, context);
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
