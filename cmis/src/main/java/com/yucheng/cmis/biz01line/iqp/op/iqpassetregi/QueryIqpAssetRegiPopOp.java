package com.yucheng.cmis.biz01line.iqp.op.iqpassetregi;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.component.IqpAverageAssetComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAssetRegiPopOp extends CMISOperation {

	private final String modelId = "IqpAssetRegi";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			IndexedCollection iColl = new IndexedCollection();
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String regi_type = "";
			if(context.containsKey("regi_type")){
				regi_type = (String) context.getDataValue("regi_type");
			}
			
			String conditionStr = "where 1=1";
			if(!regi_type.equals("")){
				conditionStr = conditionStr + " and regi_type = '"+regi_type+"' ";
			}
			if(queryData != null){
		    	String bill_no = (String)queryData.getDataValue("bill_no");
		    	String asset_status = (String)queryData.getDataValue("asset_status");
		    	if(bill_no != null && !"".equals(bill_no)){
		    		conditionStr = conditionStr + " and a.bill_no = '"+bill_no+"'";
		    	}
		    	if(asset_status != null && !"".equals(asset_status)){
		    		conditionStr = conditionStr + " and b.asset_status='"+asset_status+"'";	
		    	}
		    }
		   
			int size = 10;
			
			/*根据资管员条线显示Pop列表信息，隐藏掉其他条线业务   2014-08-21   王青  start */
			String roleno =(String)context.getDataValue("roleNoList");//一个用户可能存在多种角色
			String condition = null;
			if(roleno.indexOf("1034")>0){
				condition = " and cus_id in ( select cus_id from cus_base where belg_line = 'BL300') ";//对公条线
				conditionStr = conditionStr + condition ;
			}else if(roleno.indexOf("1035")>0){
				condition = " and cus_id in ( select cus_id from cus_base where belg_line = 'BL100') ";//对公条线
				conditionStr = conditionStr + condition ;
			}if(roleno.indexOf("1036")>0){
				condition = " and cus_id in ( select cus_id from cus_base where belg_line = 'BL200') ";//对公条线
				conditionStr = conditionStr + condition ;
			}
			
			/*根据资管员条线显示Pop列表信息，隐藏掉其他条线业务   2014-08-21   王青  end */
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			DataSource dataSource = this.getDataSource(context);
			IqpAverageAssetComponent iqpAverageAssetComponent = (IqpAverageAssetComponent)CMISComponentFactory.getComponentFactoryInstance()
			                                                     .getComponentInstance("IqpAverageAssetComponent", context, connection);
			iColl = iqpAverageAssetComponent.getIqpAssetRegiPop(conditionStr, pageInfo, dataSource);
		    
			
			
			iColl.setName(modelId+"List");
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","fina_br_id"});
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
