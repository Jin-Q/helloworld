package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLegalPersonFamilyListOp extends CMISOperation {

	private final String modelId = "CusIndivSocRel";
	private final String modelId4Mmg = "CusComManager";	//高管关联表

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String cus_id = "";		//对公客户id
		String legal_id = "";	//法定代表人id
		try {
			connection = this.getConnection(context);

//			KeyedCollection queryData = null;
//			try {
//				queryData = (KeyedCollection) context.getDataElement(this.modelId);
//			} catch (Exception e) {
//			}
			
			cus_id = (String) context.getDataValue("cus_id");
			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			
			String condStr = " where cus_id='"+cus_id+"' and com_mrg_typ='02'";
			KeyedCollection kColl4Mng = dao.queryFirst(modelId4Mmg, null, condStr,  connection);
			if(kColl4Mng.size()>0 && kColl4Mng.getDataValue("cus_id_rel")!=null){
				legal_id = kColl4Mng.getDataValue("cus_id_rel").toString();
			}

			String conditionStr = " where cus_id='"+legal_id+"' and indiv_cus_rel in('1','2','3','9') order by cus_id desc";

			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",String.valueOf(size));
			
			if(!"".equals(legal_id) && legal_id != null){
				List<String> list = new ArrayList<String>();
				list.add("cus_id");
				list.add("cus_id_rel");
				list.add("indiv_cus_rel");
				list.add("input_br_id");
				IndexedCollection iColl = dao.queryList(modelId, list,conditionStr, pageInfo, connection);
				iColl.setName(iColl.getName() + "List");

				// 加载component 如果前面已经有实例从工厂中加载，请使用改实例的getComponent(comId)
				// 如cusBaseComponent.getComponent(comId)，以保证事务一致
				CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context,connection);

				// 需要从客户信息中获取的字段mapping关系map
				Map<String,String> colMap = new HashMap<String,String>();
				colMap.put("indiv_rel_cus_name", "cus_name");
				colMap.put("indiv_rel_cert_typ", "cert_type");
				colMap.put("indiv_rl_cert_code", "cert_code");

				//需要从客户明细表中取的字段mapping关系map
				Map<String,String> indMap = new HashMap<String,String>();
				indMap.put("indiv_sex", "indiv_sex");
				indMap.put("indiv_rel_job", "indiv_occ");
				indMap.put("indiv_rel_com_name", "indiv_com_name");
				indMap.put("indiv_rel_duty", "indiv_com_job_ttl");
				
				cusBaseComponent.getICollCusById(iColl, colMap, indMap,"cus_id_rel");
				this.putDataElement2Context(iColl, context);
				context.addDataField("ComCusId", cus_id);
				TableModelUtil.parsePageInfo(context, pageInfo);
			}

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
