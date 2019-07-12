package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpInfoListOp extends CMISOperation {

	private final String modelIdIqp = "IqpLoanApp";
	private final String modelIdCtr = "CtrLoanCont";
	
	private final String modelIdIqpRpdd = "IqpRpddscnt";
	private final String modelIdCtrRpdd = "CtrRpddscntCont";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    IndexedCollection iCollCtr = new IndexedCollection("CtrLoanCont");
		    IndexedCollection iCollIqp = new IndexedCollection("IqpLoanApp");
			String batch_no = "";
			String isRpsscnt = "";
			if(context.containsKey("batch_no") && null!=context.getDataValue("batch_no") && !"".equals(context.getDataValue("batch_no"))){
				batch_no = (String)context.getDataValue("batch_no");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
            KeyedCollection kCollBatchMng = dao.queryDetail("IqpBatchMng", batch_no, connection);
            String serno = (String)kCollBatchMng.getDataValue("serno");
            String cont_no = (String)kCollBatchMng.getDataValue("cont_no");
            if(serno != null && !"".equals(serno)){
                if(cont_no != null && !"".equals(cont_no)){
                	String condition = "where cont_no = '"+cont_no+"'";
                	iCollCtr = dao.queryList(modelIdCtr, condition, connection);
                	if(iCollCtr.size()<=0){
                		iCollCtr = dao.queryList(modelIdCtrRpdd, condition, connection);
                		isRpsscnt = "is";
                	}
                }else{
                	String condition = "where serno = '"+serno+"'";
                	iCollIqp = dao.queryList(modelIdIqp, condition, connection);
                	if(iCollIqp.size()<=0){
                		iCollIqp = dao.queryList(modelIdIqpRpdd, condition, connection);
                		isRpsscnt = "is";
                	}
                }	
            }
            iCollCtr.setName(iCollCtr.getName()+"List");
            iCollIqp.setName(iCollIqp.getName()+"List");
            
            String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iCollCtr, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SystemTransUtils.dealName(iCollIqp, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iCollCtr, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iCollCtr, new String[]{"manager_br_id"});
			
			SInfoUtils.addUSerName(iCollIqp, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iCollIqp, new String[]{"manager_br_id"});
            
            
            this.putDataElement2Context(iCollCtr, context);
			this.putDataElement2Context(iCollIqp, context);
			context.put("isRpsscnt", isRpsscnt);
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
