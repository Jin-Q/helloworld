package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdateLmtAgrDetailsRecordOp extends CMISOperation {

	private final String modelId = "LmtAgrDetails";
	private final String modelIdApply = "LmtApply";
	private final String modelIdApp = "LmtAppDetails";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String org_limit_code = null;
			String belg_line = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			
			if(context.containsKey("belg_line")){
				belg_line = context.getDataValue("belg_line").toString();
			}
			
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//判断申请信息是否已经保存
			String serno = kColl.getDataValue("serno").toString();
			String condStr = " where serno='"+serno+"'";
			
			IndexedCollection iCollApp = null;
			if(null!=belg_line && "BL300".equals(belg_line)){  //零售条线
				iCollApp = dao.queryList("LmtAppIndiv", condStr, connection);
			}else{  //对公或小微
				iCollApp = dao.queryList(modelIdApply, condStr, connection);
			}
			if(iCollApp.size()==0){
				context.addDataField("msg", "未找到对应的主申请信息，不能新增分项信息！");
				throw new Exception("Insert Failed! 失败原因：未找到对应的主申请信息，不能新增分项信息！");
			}
			
			org_limit_code = (String) kColl.getDataValue("limit_code");
			String condition = "WHERE ORG_LIMIT_CODE = '"+org_limit_code+"' AND SERNO='"+serno+"'";
			
			KeyedCollection kcollTemp = dao.queryFirst(modelIdApp, null, condition, connection);
			//判断申请表中是否存在相同的数据  
			if(null != kcollTemp.getDataValue("limit_code") && !"".equals(kcollTemp.getDataValue("limit_code"))){
				//kColl4App.addDataField("limit_code",kColl.getDataValue("limit_code"));
				//int count=dao.update(kColl4App, connection);
				//if(count!=1){
					//context.addDataField("msg", "失败原因：分项明细中已存在该笔数据！");
					//context.addDataField("flag","failed");
					throw new Exception("原因：该授信分项已进行过调整，如需更正请至【现有分项额度】中进行修改！");
				//}
			}else{
				//把台账数据过渡到申请分项表中
				KeyedCollection kColl4App = new KeyedCollection("LmtAppDetails");
				kColl4App.addDataField("serno",kColl.getDataValue("serno"));
				kColl4App.addDataField("cus_id",kColl.getDataValue("cus_id"));
				kColl4App.addDataField("org_limit_code",kColl.getDataValue("limit_code"));
				kColl4App.addDataField("sub_type",kColl.getDataValue("sub_type"));
				kColl4App.addDataField("limit_type",kColl.getDataValue("limit_type"));
				kColl4App.addDataField("limit_name",kColl.getDataValue("limit_name"));
				kColl4App.addDataField("prd_id",kColl.getDataValue("prd_id"));
				kColl4App.addDataField("cur_type",kColl.getDataValue("cur_type"));
				kColl4App.addDataField("crd_amt",kColl.getDataValue("crd_amt"));
				kColl4App.addDataField("guar_type",kColl.getDataValue("guar_type"));
				kColl4App.addDataField("term_type",kColl.getDataValue("term_type"));
				kColl4App.addDataField("term",kColl.getDataValue("term"));
				kColl4App.addDataField("is_adj_term",kColl.getDataValue("is_adj_term"));
				kColl4App.addDataField("is_pre_crd",kColl.getDataValue("is_pre_crd"));
				kColl4App.addDataField("start_date",kColl.getDataValue("start_date"));
				kColl4App.addDataField("end_date",kColl.getDataValue("end_date"));
				kColl4App.addDataField("update_flag","02");
				
				String limit_Code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all",connection, context);
				
				kColl4App.addDataField("limit_code",limit_Code);
				int count = dao.insert(kColl4App, connection);
				if(count!=1){
					throw new Exception("Insert Failed! Record Count: " + count);
				}
				
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				//分项变更时将担保合同与授信台账的关系也变更过去
				lmtComponent.createRLmtAppGuarContRecord(limit_Code, org_limit_code, serno);
				
				//更新授信申请中的金额
				if(null!=belg_line && "BL300".equals(belg_line)){  //零售条线
					lmtComponent.updateLmtAppIndivAmt(kColl.getDataValue("serno").toString());//根据流水号更新个人授信申请基表数据
				}else{   //对公或小微
					lmtComponent.updateLmtApplyAmt(kColl.getDataValue("serno").toString());  //根据流水号更新授信申请基表数据
				}
			}
			
			context.addDataField("flag","success");
			context.addDataField("msg","success");
		}catch(Exception e){
			if(!context.containsKey("msg")){
				context.addDataField("msg", e.getMessage());
			}
			context.addDataField("flag","failed");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}