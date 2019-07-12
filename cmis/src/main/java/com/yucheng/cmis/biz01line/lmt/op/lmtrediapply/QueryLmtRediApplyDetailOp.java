package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtRediApplyDetailOp  extends CMISOperation {
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String modelId = "LmtRediApply";
		String res = "comPage"; 
		try{
			connection = this.getConnection(context);
			
			if("COM".equalsIgnoreCase((String)context.getDataValue("lx"))){  //对公复议
				res = "comPage";
				modelId = "LmtRediApply";
			}else{
				res = "indivPage";
				modelId = "LmtAppIndivRedi";
			}
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(modelId, context, connection);
			}
			
			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			
			if(!"COM".equalsIgnoreCase((String)context.getDataValue("lx"))){  //个人复议
				//汇总循环额度、一次性额度
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				KeyedCollection kColl_details = lmtComponent.selectLmtAppIndivAmt(kColl.getDataValue("serno").toString(),"LMT_APP_DETAILS");
				if(null!=kColl_details){
					kColl.addDataField("totl_amt", kColl_details.getDataValue("total_amt"));
					kColl.addDataField("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
					kColl.addDataField("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
				}
				
				//查询客户信息
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cusBase = service.getCusBaseByCusId((String)kColl.getDataValue("cus_id"), context, connection);
				//kColl.addDataField("cus_id_displayname", cusBase.getCusName());
				kColl.addDataField("cus_type", cusBase.getCusType());
				kColl.addDataField("belg_line", cusBase.getBelgLine());
			}else{
				//判断是否是事业法人，为后续tab页传参
				String cus_id = (String) kColl.getDataValue("cus_id");
				KeyedCollection cusKColl = dao.queryDetail("CusBase", cus_id, connection);
				String cus_type = (String) cusKColl.getDataValue("cus_type");
				if(cus_type!=null&&cus_type.startsWith("D")){
					context.addDataField("cus_flag", "1");
				}else{
					context.addDataField("cus_flag", "2");
				}
			}
			
			this.putDataElement2Context(kColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return res;
	}
}
