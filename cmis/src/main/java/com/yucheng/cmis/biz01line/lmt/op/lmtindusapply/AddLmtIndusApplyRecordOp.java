package com.yucheng.cmis.biz01line.lmt.op.lmtindusapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtIndusApplyRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtIndusApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;

		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String manager_br_id = kColl.getDataValue("manager_br_id").toString();
			String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			
			if(((String) context.getDataValue("menuId")).equals("indus_crd_change")){
				String agr_no = (String) context.getDataValue("agr_no");
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,connection);
				lmtComponent.doVirtualSubmit("indus_apply_change",agr_no,serno);
				
				/*** 行业授信处理授信分项，复制原分项信息 ***/
/*				String condition = "where limit_code in (select limit_code from Lmt_agr_Details where agr_no = '"+agr_no+"')";
				IndexedCollection insert_iColl = dao.queryList("LmtAppDetails", condition, connection);
				lmtComponent.doVirtualSubmit("indus_detail_change",agr_no,null);
				for(int i=0 ; i<insert_iColl.size() ; i++){
					KeyedCollection insert_kColl = (KeyedCollection) insert_iColl.get(i);
					insert_kColl.setDataValue("limit_code", CMISSequenceService4JXXD.
							querySequenceFromED("ED", "all", connection, context));//哪一天额度编码不再是唯一索引了，可以注释这一行代码
					insert_kColl.setDataValue("serno", serno);
					dao.insert(insert_kColl, connection);
				}*/
			}
			
			kColl.removeDataElement("agr_no");
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);
			context.addDataField("serno", serno);
			context.addDataField("flag", "success");
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