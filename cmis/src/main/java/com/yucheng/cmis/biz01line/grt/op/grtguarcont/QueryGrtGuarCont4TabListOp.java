package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtGuarCont4TabListOp extends CMISOperation {

	private final String modelId = "GrtGuarCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no = null;
		try{
			connection = this.getConnection(context);
			try {
				if(context.containsKey("drfpo_no")){
					drfpo_no = (String) context.getDataValue("drfpo_no");
				}else if(context.containsKey("po_no")){
					drfpo_no = (String) context.getDataValue("po_no");
				}
			} catch (Exception e) {}
			
			String conditionStr = " where guar_cont_no in (select guar_cont_no from grt_guaranty_re where guaranty_id = '"+drfpo_no+"') order by guar_cont_type desc,guar_cont_no desc";
			
			int size = 15;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(iColl, context);

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
