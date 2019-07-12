package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateValueManaStatus extends CMISOperation {
	
	private final String modelId = "IqpMortValueMana";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String value_no = "";
			String status = "";
			if(context.containsKey("value_no")){
				value_no = context.getDataValue("value_no").toString();
			}else{
				context.addDataField("msg", "修改押品价格状态失败，失败原因：传入押品目录编号[value_no]为空！");
				context.addDataField("flag","N");
				return "0";
			}
			if(context.containsKey("status")){
				status = context.getDataValue("status").toString();
			}else{
				context.addDataField("msg", "修改押品价格状态失败，失败原因：传入状态信息[status]为空！");
				context.addDataField("flag","N");
				return "0";
			}
			
			CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
			
			if("1".equals(status)){  //变更价格状态为生效
				String catalog_no = "";
				if(context.containsKey("catalog_no")){
					catalog_no = context.getDataValue("catalog_no").toString();
				}else{
					context.addDataField("msg", "修改押品价格状态失败，失败原因：传入押品目录[catalog_no]为空！");
					context.addDataField("flag","N");
					return "0";
				}
				TableModelDAO dao = this.getTableModelDAO(context);
				ArrayList<String> list = new ArrayList<String>();
				list.add("status");
				KeyedCollection kColl = dao.queryFirst("IqpMortCatalogMana", list, " WHERE CATALOG_PATH='"+catalog_no+"' ", connection);
				if(null==kColl || "0".equals(kColl.getDataValue("status"))){  //校验押品目录状态是否为有效
					context.addDataField("msg", "修改押品价格状态失败，失败原因：对应押品目录不是【生效】状态，请到押品目录管理中作生效处理！");
					context.addDataField("flag","N");
					return "0";
				}
			}else if("0".equals(status)){ //变更价格状态为失效
				String catalog_no = "";
				if(context.containsKey("catalog_no")){
					catalog_no = context.getDataValue("catalog_no").toString();
				}else{
					context.addDataField("msg", "修改押品价格状态失败，失败原因：传入押品目录[catalog_no]为空！");
					context.addDataField("flag","N");
					return "0";
				}
				TableModelDAO dao = this.getTableModelDAO(context);
				IndexedCollection iColl = dao.queryList("MortGuarantyBaseInfo", null, " WHERE guaranty_type='"+catalog_no+"' ", connection);
				IndexedCollection iCollMortCargoPledge = dao.queryList("MortCargoPledge", null, " WHERE value_no='"+value_no+"' ", connection);
				if(iColl.size()!=0||iCollMortCargoPledge.size()!=0){  //校验货物登记时，有没有引用过此目录。
					context.addDataField("msg", "修改押品价格状态失败，失败原因：已经存在此押品类型的货物信息，不能对其作失效处理");
					context.addDataField("flag","N");
					return "0";
				}
			}
			
			int count = catalogManaComponent.updateValueManaStatus(value_no, status);
			if(count!=1){
				context.addDataField("msg", "修改押品价格状态失败，更新记录数为"+count);
				context.addDataField("flag","N");
				return "0";
			}
			context.addDataField("flag", "Y");
			context.addDataField("msg", "Y");
		}catch(Exception e){
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
