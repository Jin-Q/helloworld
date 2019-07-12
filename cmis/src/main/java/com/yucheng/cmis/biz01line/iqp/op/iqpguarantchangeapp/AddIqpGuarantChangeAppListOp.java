package com.yucheng.cmis.biz01line.iqp.op.iqpguarantchangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class AddIqpGuarantChangeAppListOp extends CMISOperation {
	private final String modelIdAcc = "AccLoan"; 
	private final String modelIdCusManager = "CusManager"; 
 
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    /**查询合同信息*/
			//String condition = "where prd_id in ('400020','400021') and cont_status='200' and cont_no in (select a.cont_no from ctr_loan_cont a, acc_loan b where a.cont_no = b.cont_no) order by cont_no";
			/**查询出账信息*/
			//String conditionAcc = "where cont_no in (select a.cont_no from ctr_loan_cont a, acc_loan b where a.cont_no = b.cont_no and a.prd_id in ('400020','400021') and a.cont_status='200') order by cont_no";
			/**查询客户经理信息*/
			//String conditionCusManager = "where cont_no in (select a.cont_no from ctr_loan_cont a, acc_loan b where a.cont_no = b.cont_no and a.prd_id in ('400020','400021') and a.cont_status='200') order by cont_no";
			
			//String condition = " where prd_id in ('400020','400021') and acc_status='1'";
			String condition = "";
			/** 记录集权限 */ 
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			condition = recordRestrict.judgeQueryRestrict(this.modelIdAcc, condition, context, connection); 
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
            
			IndexedCollection iColl = dao.queryList(modelIdAcc,null ,condition,pageInfo,connection);
			//IndexedCollection iCollAcc = dao.queryList(modelIdAcc,null ,conditionAcc,pageInfo,connection);
			//IndexedCollection iCollCusManager = dao.queryList(modelIdCusManager,null ,conditionCusManager,pageInfo,connection);
			/**数据正确的情况下，上面三个集合查出来的数据条数应是相同的*/
//			for(int i=0;i<iColl.size();i++){
//				KeyedCollection kCollCtr = (KeyedCollection)iColl.get(i);
//				if(iCollAcc.size()>i && iCollCusManager.size()>i){
//					KeyedCollection kCollAcc = (KeyedCollection)iCollAcc.get(i); 
//					KeyedCollection kCollCusManager = (KeyedCollection)iCollCusManager.get(i);
//					if(kCollAcc.getDataValue("cont_no").equals(kCollCtr.getDataValue("cont_no"))){
//						kCollCtr.addDataField("bill_no", kCollAcc.getDataValue("bill_no"));     						
//					}     
//					if(kCollCusManager.getDataValue("cont_no").equals(kCollCtr.getDataValue("cont_no"))){
//						kCollCtr.addDataField("manager_id", kCollCusManager.getDataValue("manager_id")); 		
//					}        
//				}  
//			} 
			iColl.setName(iColl.getName()+"List"); 
			
			String[] args=new String[] {"cus_id","prd_id"};
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
		    SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
		    
		    this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo); 
			
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
