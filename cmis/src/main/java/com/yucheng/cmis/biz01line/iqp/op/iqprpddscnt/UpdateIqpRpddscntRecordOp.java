package com.yucheng.cmis.biz01line.iqp.op.iqprpddscnt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateIqpRpddscntRecordOp extends CMISOperation {
	

	private final String modelId = "IqpRpddscnt";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			//添加授信关系
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			String serno = (String) kColl.getDataValue("serno");
			String limit_ind = (String) kColl.getDataValue("limit_ind");
			String rpddscnt_type = (String) kColl.getDataValue("rpddscnt_type");//转贴现方式
			KeyedCollection kCollRel =new KeyedCollection();
			
			String bill_type = (String) kColl.getDataValue("bill_type");//票据类型
			String batch_no = (String) kColl.getDataValue("batch_no");//批次号
			if(limit_ind!=null&&!limit_ind.equals("")){
				if(limit_ind.equals("2")||limit_ind.equals("3")){
					String agr_no = (String) kColl.getDataValue("limit_acc_no");
					iqpLoanAppComponent.doLmtRelation("01", serno, agr_no);
				}else if("4".equals(limit_ind)){
					if(bill_type.equals("100")){//判断是否为银票
						if(!"01".equals(rpddscnt_type) && !"04".equals(rpddscnt_type)){//!'01':'买入买断', !'04':'卖出回购'
							String agr_no = (String) kColl.getDataValue("limit_credit_no");
							iqpLoanAppComponent.doLmtRelation("02", serno, agr_no);
						}else{
			        		//删除关系表数据
							iqpLoanAppComponent.deleteLmtRelation(serno);
			        		String lmt_type="90";//占用承兑行
			        		//直接根据批次号就可以查询批次下票据对应的承兑人
			        		String condition = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no in (select a.porder_no from iqp_batch_bill_rel a where a.batch_no = '"+batch_no+"')))";
			        		IndexedCollection iCollCus = dao.queryList("CusSameOrg", condition, connection);
			        		if(iCollCus.size()==0){
			        			throw new Exception("请检查该批次包是否录入票据信息或票据承兑行总行行号不存在!");
			        		}
			        		for(int i=0;i<iCollCus.size();i++){
			        			KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(i);
			        			String cus_id = (String)kCollCus.getDataValue("cus_id");//总行客户码
			        			if(null!=cus_id && !"".equals(cus_id)){
			        				 String openDay = (String)context.getDataValue("OPENDAY");
			        				 KeyedCollection kCollLmtBank = dao.queryFirst("LmtIntbankAcc",null, "where cus_id='"+cus_id+"' and end_date>='"+openDay+"' and lmt_status='10'", connection);
			        				 String agr_no = (String)kCollLmtBank.getDataValue("agr_no");
			        				 if(agr_no == null || "".equals(agr_no)){
			        					 throw new EMPException("请检查票据承兑行总行是否存在有效授信!");
			        				 }
			        				 KeyedCollection kCollCreditRel = new KeyedCollection("RBusLmtcreditInfo");
			        				 kCollCreditRel.put("agr_no", agr_no);
			        				 kCollCreditRel.put("lmt_type", lmt_type);
			        				 kCollCreditRel.put("serno", serno);
			        				 kCollCreditRel.put("cont_no", "");
			        				 HashMap<String,String> map = new HashMap<String,String>();
			        				 map.put("agr_no", agr_no);
			        				 map.put("serno", serno);
			        				 kCollRel = dao.queryDetail("RBusLmtcreditInfo", map, connection);
			        				 String selectSerno = (String)kCollRel.getDataValue("serno");
			        				 if(selectSerno == null || "".equals(selectSerno)){
			        					 dao.insert(kCollCreditRel, connection);
			        				 }else{
			        					 dao.update(kCollCreditRel, connection);
			        				 }
			        			}else{
			        				throw new EMPException("承兑行总行行号不存在!");
			        			}
			        		}
						}
					}else{
						String agr_no = (String) kColl.getDataValue("limit_credit_no");
						iqpLoanAppComponent.doLmtRelation("02", serno, agr_no);
					}
				}else{
					iqpLoanAppComponent.deleteLmtRelation(serno);
				}
	        }
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
			context.addDataField("msg", ee.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch(Exception e){
			context.addDataField("flag", "failed");
			context.addDataField("msg", e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ee) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
