package com.yucheng.cmis.biz01line.psp.op.pspschcatrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPspSchCatRelListOp extends CMISOperation {


	private final String modelId = "PspSchCatRel";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			String scheme_id = null;
			try {
				scheme_id = (String)context.getDataValue("scheme_id");
			} catch (Exception e) {}
			
		
			String conditionStr = "where scheme_id='"+scheme_id+"' order by scheme_id desc,catalog_id desc";
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			String[] args=new String[] {"scheme_id","catalog_id" };
			String[] modelIds=new String[]{"PspCheckScheme","PspCheckCatalog"};
			String[]modelForeign=new String[]{"scheme_id","catalog_id"};
			String[] fieldName=new String[]{"scheme_name","catalog_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
