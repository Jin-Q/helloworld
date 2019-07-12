package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryYbGrtLoanRGurListOp extends CMISOperation {
	private final String modelId = "GrtLoanRGur";
	private final String modelIdGuarCont = "GrtGuarCont";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
            String serno="";
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			
			String conditionStr ="where serno='"+serno+"' order by guar_cont_no desc";
			int size = 10; 
			int sizePage = 2; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			PageInfo pageInfoYb = TableModelUtil.createPageInfo(context, "one", String.valueOf(sizePage));
			PageInfo pageInfoZge = TableModelUtil.createPageInfo(context, "one", String.valueOf(sizePage));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List list = new ArrayList(); 
			list.add("pk_id");
			list.add("serno");
			list.add("guar_cont_no");
			list.add("cont_no");
			list.add("guar_amt");
			list.add("is_per_gur");
			//页面上显示一般担保合同记录和最高担保额记录
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			IndexedCollection iCollYB = new IndexedCollection();
			IndexedCollection iCollZGE = new IndexedCollection();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				KeyedCollection GuarContkColl = (KeyedCollection)dao.queryDetail(modelIdGuarCont, guar_cont_no, connection);
				String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
				GuarContkColl.addDataField("pk_id", kColl.getDataValue("pk_id"));
				GuarContkColl.addDataField("is_per_gur", kColl.getDataValue("is_per_gur"));
				GuarContkColl.addDataField("this_guar_amt", kColl.getDataValue("guar_amt"));
				//判断担保合同类型
				if(guar_cont_type.equals("00")){
					iCollYB.addDataElement(GuarContkColl);
				}else if(guar_cont_type.equals("01")){
					iCollZGE.addDataElement(GuarContkColl);  
				} 
			}
			iCollYB.setName("GrtLoanRGurListYb");
			iCollZGE.setName("GrtLoanRGurListZge");
			pageInfoYb.setRecordSize(String.valueOf(iCollYB.size()));
			pageInfoZge.setRecordSize(String.valueOf(iCollZGE.size()));
			this.putDataElement2Context(iCollYB, context);
			this.putDataElement2Context(iCollZGE, context);
			TableModelUtil.parsePageInfo(context, pageInfoYb);
			TableModelUtil.parsePageInfo(context, pageInfoZge);

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
