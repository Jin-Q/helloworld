package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtSigLmtDetail4BatchOp extends CMISOperation {

	private final String modelId = "LmtSigLmt";

     /**
      * 批量授信中单个客户进行额度设置
      * 如果客户已经进行了授信--展示授信信息
      * 如果客户没有进行授信--新增一条授信记录
      * */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id_value = null;
		KeyedCollection kColl = null;
		try{			
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			cus_id_value = (String)context.getDataValue("cus_id");
			String serno = (String)context.getDataValue("serno");
			String condition1 = "where cus_id='"+cus_id_value+"'";
			IndexedCollection iColl = dao.queryList("LmtSigLmt", "where cus_id='"+cus_id_value+"'and batch_serno='"+serno+"' and app_cls='02'", connection);
			KeyedCollection kColl_cus =dao.queryFirst("CusSameOrg", null, condition1, connection);
			String same_org_cnname =(String)kColl_cus.getDataValue("same_org_cnname");//同业机构(行)名称
			String same_org_type =(String)kColl_cus.getDataValue("same_org_type");
			if(iColl.size()>0){
				kColl = (KeyedCollection)iColl.get(0);
				String serno_value = (String)kColl.getDataValue("serno");
				kColl.addDataField("same_org_cnname", same_org_cnname);
				kColl.addDataField("same_org_type", same_org_type);
				String condition=" where serno='"+serno_value+"'";
				IndexedCollection iColl_LmtSubApp = dao.queryList("LmtSubApp",condition, connection);
				this.putDataElement2Context(iColl_LmtSubApp, context);
			}else{
				String limit_type = (String)context.getDataValue("limit_type");//额度类型
				String serno_temp =CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);//流水号							
				String input_id = (String)context.getDataValue("currentUserId");			
				String input_br_id = (String)context.getDataValue("organNo");
				String crd_grade =(String)kColl_cus.getDataValue("crd_grade");
				String assets =(String)kColl_cus.getDataValue("assets");
				kColl = new KeyedCollection(modelId);
				kColl.addDataField("input_id", input_id);
				kColl.addDataField("limit_type", limit_type);
				kColl.addDataField("input_br_id", input_br_id);
				kColl.addDataField("serno", serno_temp);
				kColl.addDataField("same_org_cnname", same_org_cnname);
				kColl.addDataField("same_org_type", same_org_type);
				kColl.addDataField("crd_grade", crd_grade);
				kColl.addDataField("app_date", (String)context.getDataValue("OPENDAY"));
				kColl.addDataField("asserts", assets);
				kColl.addDataField("cus_id", cus_id_value);	
				kColl.addDataField("batch_serno", serno);
				kColl.addDataField("manager_br_id", "");
				kColl.addDataField("manager_id", "");
			}
			String manager_br_id = context.getDataValue("manager_br_id").toString();
			String manager_id = context.getDataValue("manager_id").toString();
			kColl.setDataValue("manager_br_id", manager_br_id);
			kColl.setDataValue("manager_id", manager_id);
			//翻译成中文
			SInfoUtils.addSOrgName(kColl, new String []{"input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String []{"input_id","manager_id"});
			this.putDataElement2Context(kColl, context);
			
			
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
