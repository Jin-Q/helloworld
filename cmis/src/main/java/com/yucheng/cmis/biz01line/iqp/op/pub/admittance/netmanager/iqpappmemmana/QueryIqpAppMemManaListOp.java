package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappmemmana;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAppMemManaListOp extends CMISOperation {

	private final String modelId = "IqpAppMemMana";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno="";
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {
				throw new Exception("业务流水号为空!");
			}
		    
		    String conditionStr = "where serno='"+serno+"' order by mem_cus_id desc";
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("mem_cus_id");
			list.add("mem_manuf_type");
			list.add("term");
			list.add("lmt_type");
			list.add("status");
			list.add("lmt_quota");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			//详细信息翻译时调用	
			String[] args=new String[] {"mem_cus_id"};
		    String[] modelIds=new String[]{"CusBase"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"cus_name"};
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
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
