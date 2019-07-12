package com.yucheng.cmis.ops.lmt.wfispnamelist;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiSpNameListListOp extends CMISOperation {


	private final String modelId = "WfiSpNameList";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String conditionStr = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				String cusName="";
				String cusIdList="";
				IndexedCollection iCollSelect = new IndexedCollection();
				DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
				if(queryData !=null && !"".equals(queryData)){
					cusName = (String) queryData.getDataValue("cus_name");
					if(cusName !=null && !"".equals(cusName)){
						String conSelect ="select c.cus_id from cus_base c where c.cus_name like '%"+cusName+"%'";
						iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
						if(iCollSelect !=null && iCollSelect.size() > 0){
							for(int i=0;i<iCollSelect.size();i++){
								KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
								cusIdList += kColl.getDataValue("cus_id")+",";
							}
							conditionStr += "and instr('"+ cusIdList+"', cus_id)>0";
						}
					}
				}
				
			} catch (Exception e) {
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			conditionStr =conditionStr +" and 1=1 order by pk_id desc";
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List list = new ArrayList();
			list.add("cus_id");
			list.add("sp_right_type");
			list.add("manager_br_id");
			list.add("app_date");
			list.add("memo");
			list.add("pk_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] {"manager_br_id" });
			iColl.setName(iColl.getName()+"List");
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
