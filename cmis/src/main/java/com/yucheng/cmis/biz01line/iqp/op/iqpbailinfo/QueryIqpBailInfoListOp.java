package com.yucheng.cmis.biz01line.iqp.op.iqpbailinfo;

import java.sql.Connection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpBailInfoListOp extends CMISOperation {
	private final String modelId = "IqpBailInfo";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String serno = (String)context.getDataValue("serno");
			if(serno == null || serno.trim().length() == 0){
				throw new EMPException("获取业务流水号失败，请检查流水号是否传递！");
			}
		
			String conditionStr = " where serno = '"+serno+"' order by serno desc,bail_acct_no desc";
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("serno");
			list.add("bail_acct_no");
			list.add("cus_id");
			list.add("cur_type");
			list.add("rate");
			list.add("dep_term");
			list.add("open_org");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			/** 组织机构、登记机构翻译 */
			SInfoUtils.addSOrgName(iColl, new String[]{"open_org"});
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
