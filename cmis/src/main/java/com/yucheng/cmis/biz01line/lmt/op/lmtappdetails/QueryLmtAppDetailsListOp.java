package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppDetailsListOp extends CMISOperation {

	private final String modelIdApp = "LmtAppDetails";
	private final String modelIdAppHis = "LmtAppDetailsHis";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iCollAgr = new IndexedCollection();
			String serno = (String)context.getDataValue("serno");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			if(context.containsKey("app_type") && "02".equals(context.getDataValue("app_type"))){
				context.addDataField("div_name", "变更额度分项");
			}else{
				context.addDataField("div_name", "新增额度分项");
			}
			
			/** 查询客户原有有效授信台账记录，包括小微条线、公司条线、供应链   **/
			String conditionStrAgr =  "WHERE SERNO ='"+ serno+"' AND SUB_TYPE IN('01','05')";
			iCollAgr = dao.queryList(modelIdAppHis,null ,conditionStrAgr,connection);
			/** END */
			
			/**翻译额度名称**/
			String[] args=new String[] { "limit_name" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iCollAgr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iCollAgr.setName("LmtAgrDetailsList");
			this.putDataElement2Context(iCollAgr, context);
			//申请表
			String conditionStrApp = "";
			conditionStrApp = " where serno ='" + serno+"'";
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("sub_type");
			list.add("limit_code");
			list.add("org_limit_code");
			list.add("lmt_type");
			list.add("limit_type");
			list.add("guar_type");
			list.add("crd_amt");
			list.add("term_type");
			list.add("term");
			list.add("update_flag");
			list.add("limit_name");
			list.add("cur_type");
			list.add("froze_amt");
			list.add("lrisk_type");
			IndexedCollection iColl = dao.queryList(modelIdApp,list ,conditionStrApp,connection);
			iColl.setName(iColl.getName()+"List");
			/**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
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
