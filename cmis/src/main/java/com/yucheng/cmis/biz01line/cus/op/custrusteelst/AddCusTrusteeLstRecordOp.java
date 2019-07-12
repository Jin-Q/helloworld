package com.yucheng.cmis.biz01line.cus.op.custrusteelst;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCusTrusteeLstRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusTrusteeLst";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kCollToDb = new KeyedCollection(modelId);
			KeyedCollection kColl = new KeyedCollection("CusLoanRelList");
			IndexedCollection iColl=null;
			
			
			String trustee_scope = (String)context.getDataValue("trustee_scope");        //托管范围
			String consignor_br_id = (String)context.getDataValue("consignor_br_id");  //委托机构
			String consignor_id = (String)context.getDataValue("consignor_id");        //委托人
			String trustee_br_id = (String)context.getDataValue("trustee_br_id");      //托管机构
			String trustee_id = (String)context.getDataValue("trustee_id");            //托管人
			String serno = (String)context.getDataValue("serno");                      //业务流水号
			String retract_date = (String)context.getDataValue("retract_date");        //收回日期
			String trustee_date = (String)context.getDataValue("trustee_date");        //托管日期
			
			kCollToDb.addDataField("consignor_br_id", consignor_br_id);
			kCollToDb.addDataField("consignor_id", consignor_id);
			kCollToDb.addDataField("trustee_br_id", trustee_br_id);
			kCollToDb.addDataField("trustee_id", trustee_id);
			kCollToDb.addDataField("serno", serno);
			kCollToDb.addDataField("retract_date", retract_date);
			kCollToDb.addDataField("trustee_date", trustee_date);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//按客户托管，客户码是来自页面。
			if(trustee_scope.equals("1")){
				/*try {
					iColl = (IndexedCollection)context.getDataElement("CusLoanRelList");
				} catch (Exception e) {}
				if(iColl == null || iColl.size() == 0)
					throw new EMPJDBCException("提交数据不能为空！");*/
				
				iColl = (IndexedCollection)context.getDataElement("CusLoanRelList");
			//- 按客户经理方式提交，需要先查询出来。因为页面只有一部分。
			}else if(trustee_scope.equals("2")){
//				CusLoanRelComponent cusLoanRelComponent = (CusLoanRelComponent) CMISComponentFactory
//				.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSLOANREL,context,connection);
//				List<CMISDomain> cusLoanRelList = new ArrayList<CMISDomain>();
//				cusLoanRelList=cusLoanRelComponent.findCusLoanRelListByCurrentuser((String)context.getDataValue("currentUserId"));
//				ComponentHelper cHelper = new ComponentHelper();
//				iColl = cHelper.domain2icol(cusLoanRelList,"CusLoanRel",CMISConstance.CMIS_LIST_IND);
			//- 按区域方式提交，需要先查询出来。因为页面只有一部分。
			}else if(trustee_scope.equals("3")){
//				CusLoanRelComponent cusLoanRelComponent = (CusLoanRelComponent) CMISComponentFactory
//				.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSLOANREL,context,connection);
//				List<CMISDomain> cusLoanRelList = new ArrayList<CMISDomain>();//
//				cusLoanRelList=cusLoanRelComponent.findCusLoanRelListByAreaCode((String)context.getDataValue("currentUserId"),(String)context.getDataValue("area_code"));
//				ComponentHelper cHelper = new ComponentHelper();
//				iColl = cHelper.domain2icol(cusLoanRelList,"CusLoanRel",CMISConstance.CMIS_LIST_IND);
			}
			for(int i=0;i<iColl.size();i++){
				kColl=(KeyedCollection)iColl.get(i);
				//判断新增客户是否已经存在
				IndexedCollection icoll = dao.queryList(modelId, " where serno ='"+serno+"'", connection);
				for(int j=0;j<icoll.size();j++){
					KeyedCollection kcoll = (KeyedCollection) icoll.get(j);
					String cusId = (String) kcoll.getDataValue("cus_id");
					if(cusId.equals(kColl.getDataValue("cus_id"))){
						throw new EMPException("该客户已经存在托管明细中，不能重复添加！！！");
					}
				}
				//客户码
				try{
					kCollToDb.addDataField("cus_id", (String)kColl.getDataValue("cus_id"));
				} catch (Exception ex) {
					kCollToDb.removeDataElement("cus_id");
					kCollToDb.addDataField("cus_id", (String)kColl.getDataValue("cus_id"));
				}
				//客户名称
				try{
					kCollToDb.addDataField("cus_name",(String)kColl.getDataValue("cus_name"));
				} catch (Exception ex) {
					kCollToDb.removeDataElement("cus_name");
					kCollToDb.addDataField("cus_name", (String)kColl.getDataValue("cus_name"));
				}			
				
				//add a record			
				dao.insert(kCollToDb, connection);
			}	
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
