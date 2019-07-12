package com.yucheng.cmis.biz01line.iqp.op.iqpasset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAssetstrsfInfoListOp extends CMISOperation {

	private final String modelIdIqp = "IqpAssetstrsf";
	private final String modelIdCtr = "CtrAssetstrsfCont";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    IndexedCollection iCollCtr = new IndexedCollection("CtrLoanCont");
		    IndexedCollection iCollIqp = new IndexedCollection("IqpLoanApp");
			String asset_no = "";
			if(context.containsKey("asset_no") && null!=context.getDataValue("asset_no") && !"".equals(context.getDataValue("asset_no"))){
				asset_no = (String)context.getDataValue("asset_no");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String condition = "where asset_no='"+asset_no+"'";
            IndexedCollection iColl4Iqp = dao.queryList(modelIdIqp, condition, connection);
            if(iColl4Iqp.size()>0){
            		KeyedCollection kColl4Iqp = (KeyedCollection)iColl4Iqp.get(0);
                	String serno = (String)kColl4Iqp.getDataValue("serno");
                	if(serno != null && !"".equals(serno)){
                		String condition4Ctr = "where serno='"+serno+"'";
                		IndexedCollection iColl4Ctr = dao.queryList(modelIdCtr, condition4Ctr, connection);
                		if(iColl4Ctr.size()>0){
                			iCollCtr = iColl4Ctr;
                		}else{
                			iCollIqp = iColl4Iqp;
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
