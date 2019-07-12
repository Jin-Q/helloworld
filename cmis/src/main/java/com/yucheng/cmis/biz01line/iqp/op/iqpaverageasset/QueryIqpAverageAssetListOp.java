package com.yucheng.cmis.biz01line.iqp.op.iqpaverageasset;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.component.IqpAverageAssetComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAverageAssetListOp extends CMISOperation {

	private final String modelId = "IqpAverageAsset";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
//			String approve_status = "";
//			if(queryData!=null && !queryData.getDataValue("average_status").equals("")){
//				approve_status = queryData.getDataValue("average_status").toString();
//				approve_status = "and serno in (select serno from Iqp_Average_Asset_app where approve_status = '"+approve_status+"')";
//			}
			
			String conditionStr = "";
			String conditionSelect = "";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			/**modified by lisj 2014-12-9 需求编号：【XD141107075】资产转让改造,修复卖出资产查询功能 begin**/
			if(queryData !=null && !"".equals(queryData)){
			String bill_no  = (String) queryData.getDataValue("bill_no");
			String cont_no  = (String) queryData.getDataValue("cont_no");
			String cus_id = (String) queryData.getDataValue("cus_id");
			String average_status = (String) queryData.getDataValue("average_status");
			conditionSelect = "and aver.bill_no like '%"+bill_no+"%'"+"and aver.cont_no like '%"+cont_no+"%'"
							  +"and aver.average_status like '%"+average_status+"%'"+"and aver.cus_id like '%"+cus_id+"%'";
			}
			/**modified by lisj 2014-12-9 需求编号：【XD141107075】资产转让改造,修复卖出资产查询功能 end**/
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		    
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			IqpAverageAssetComponent iqpAverageAssetComponent = (IqpAverageAssetComponent)CMISComponentFactory.getComponentFactoryInstance()
			                                                     .getComponentInstance("IqpAverageAssetComponent", context, connection);
			IndexedCollection iColl = iqpAverageAssetComponent.getIqpAverageAssetList(conditionStr,conditionSelect, pageInfo, dataSource);
			iColl.setName("IqpAverageAssetList");
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			String[] resultName = new String[] { "cus_id_displayname"};
		    //详细信息翻译时调用
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
		}catch (EMPException ee) {
			ee.printStackTrace();
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
