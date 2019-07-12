package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class CheckGrtLoanRGurOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "GrtLoanRGur";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no ="";
			String serno = "";
			String isCreditChange = "";
			String cont_no = "";
			String condition ="";
			try {
				 guar_cont_no = (String)context.getDataValue("guar_cont_no");
				 serno = (String)context.getDataValue("serno");
				 isCreditChange = (String)context.getDataValue("isCreditChange");
				 cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(guar_cont_no == null || guar_cont_no=="")
				throw new EMPJDBCException("The values to ["+guar_cont_no+"] cannot be empty!");
			
			/**如果是信用证修改中，引入担保合同，则根据合同编号和担保合同编号取检索*/
			if("is".equals(isCreditChange)){  
				condition = "where cont_no='"+cont_no+"' and guar_cont_no='"+guar_cont_no+"'";
			}else{
				condition = "where serno='"+serno+"' and guar_cont_no='"+guar_cont_no+"'";
			}
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/**查询此笔业务下是否已引入此担保合同*/
			IndexedCollection iCollCont =  dao.queryList(modelId, condition, connection);
			if(iCollCont.size()>0){
				context.addDataField("flag","error");  
			}else{
				context.addDataField("flag","success");
			}
			
			
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
