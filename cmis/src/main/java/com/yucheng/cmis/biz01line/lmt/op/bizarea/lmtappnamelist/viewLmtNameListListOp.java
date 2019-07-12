package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class viewLmtNameListListOp extends CMISOperation {


	private final String modelId = "LmtNameList";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String biz_area_no = (String)context.getDataValue("biz_area_no");
			
			String conditionStr1 = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			String conditionStr2 = null;
			if( "".equals(conditionStr1 )){
				conditionStr2 = " where serno = '" + biz_area_no + "'" + "order by cus_id desc";
			}else
				conditionStr2 = " and serno = '" + biz_area_no + "'" + "order by cus_id desc";
			KeyedCollection kColl = null;
			String cus_id = null;
			CusBaseComponent cusBaseComp = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
			CusBase cusBase = null;
			
			int size = 15;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr2,pageInfo,connection);
			//得到客户码   从而获取客户名称客户类别
			for(int i=0; i<iColl.size(); i++){
				kColl = (KeyedCollection)iColl.get(i);
				cus_id = (String) kColl.getDataValue("cus_id");
				cusBase = cusBaseComp.getCusBase(cus_id);
				kColl.addDataField("cus_name", cusBase.getCusName());
				kColl.addDataField("cus_type", cusBase.getCusType());
				iColl.remove(i);
				iColl.add(i, kColl);
			}
			
			iColl.setName(iColl.getName()+"List");
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
