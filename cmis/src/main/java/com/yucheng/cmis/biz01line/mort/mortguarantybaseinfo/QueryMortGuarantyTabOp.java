package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryMortGuarantyTabOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guarantyContNo = null;
		try{
			guarantyContNo = (String) context.getDataValue("guar_cont_no");
			connection = this.getConnection(context);
			//构建查询查询关联信息的组件
			MortCommenOwnerComponent mort = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			IndexedCollection iColl = mort.queryMortTabList(guarantyContNo);
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用	
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iColl.setName("MortGuarantyBaseInfoList");
			/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
			String lrisk_type="";		
			IndexedCollection iColl4RLAGC = SqlClient.queryList4IColl("queryIsLowRiskByGCN", guarantyContNo, connection);
			//modify by jiangcuihua 2019-03-13,查询结果可能为空
			if(iColl4RLAGC.size()>0){
				lrisk_type = (String) ((KeyedCollection)iColl4RLAGC.get(0)).getDataValue("lrisk_type");
			}
			context.put("lrisk_type", lrisk_type);
			/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
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
