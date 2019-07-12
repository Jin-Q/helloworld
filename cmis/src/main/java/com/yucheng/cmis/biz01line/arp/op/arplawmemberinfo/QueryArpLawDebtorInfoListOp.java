package com.yucheng.cmis.biz01line.arp.op.arplawmemberinfo;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpLawDebtorInfoListOp extends CMISOperation {

	private final String modelId = "ArpLawMemberInfo";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = context.getDataValue("serno").toString();
			String conditionStr = " where member_type = '002' and  serno = '"+serno+"' order by pk_serno desc";
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);		
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("pk_serno");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args = new String[] { "cus_id","cus_id","cus_id" };
			String[] modelIds = new String[] { "CusBase","CusBase","CusBase" };
			String[] modelForeign = new String[] { "cus_id","cus_id","cus_id" };
			String[] fieldName = new String[] { "cus_name","cert_type","cert_code" };
			String[] resultName = new String[] { "cus_name","cert_type","cert_code" };
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
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