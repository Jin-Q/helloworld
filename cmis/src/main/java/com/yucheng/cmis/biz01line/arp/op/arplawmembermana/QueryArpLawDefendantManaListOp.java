package com.yucheng.cmis.biz01line.arp.op.arplawmembermana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpLawDefendantManaListOp extends CMISOperation {


	private final String modelId = "ArpLawMemberMana";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			int i = 0;
			
			/*** 1.取其他被告、被告现状 ***/
			String case_no = context.getDataValue("case_no").toString();
			KeyedCollection kColl = dao.queryDetail("ArpLawLawsuitInfo", case_no, connection);
			this.putDataElement2Context(kColl, context);
			
			/*** 2.取所有被告客户，翻译其条线 ***/					
			String conditionStr = " where member_type = '001' and  case_no = '"+case_no+"' ";
			String order = "order by pk_serno desc";
			List<String> list = new ArrayList<String>();
			list.add("pk_serno");
			list.add("case_no");
			list.add("member_type");
			list.add("cus_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr+order,connection);			
			String[] args = new String[] { "cus_id","cus_id","cus_id","cus_id"};
			String[] modelIds = new String[] { "CusBase","CusBase","CusBase","CusBase"};
			String[] modelForeign = new String[] { "cus_id","cus_id","cus_id","cus_id"};
			String[] fieldName = new String[] { "belg_line","cus_name","cert_type","cert_code" };
			String[] resultName = new String[] { "belg_line","cus_name","cert_type","cert_code" };
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			/*** 3.分对公与个人处理成两个列表，构成其条件 ***/
			IndexedCollection iColl_indiv = new IndexedCollection("ArpLawMemberInfoIndivList");
			IndexedCollection iColl_com = new IndexedCollection("ArpLawMemberInfoComList");
			for(i=0;i<iColl.size();i++){
				KeyedCollection list_kColl = (KeyedCollection) iColl.getElementAt(i);
				if(list_kColl.getDataValue("belg_line").equals("BL300")){	//处理成个人列表
					iColl_indiv.addDataElement(list_kColl);
				}else{
					iColl_com.addDataElement(list_kColl);
				}
			}
			
			/*** 4.各种翻译 ***/
			args = new String[] {"cus_id"};
			modelIds = new String[] {"CusCom"};
			modelForeign = new String[] {"cus_id"};
			fieldName = new String[] {"acu_addr" };
			resultName = new String[] {"acu_addr"};
			SystemTransUtils.dealPointName(iColl_com, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);

			args = new String[] {"cus_id","cus_id","cus_id","cus_id"};
			modelIds = new String[] {"CusIndiv","CusIndiv","CusIndiv","CusIndiv"};
			modelForeign = new String[] {"cus_id","cus_id","cus_id","cus_id"};
			fieldName = new String[] {"indiv_sex","indiv_ntn","indiv_rsd_addr","indiv_dt_of_birth" };
			resultName = new String[] {"indiv_sex","indiv_ntn","indiv_rsd_addr","indiv_dt_of_birth"};
			SystemTransUtils.dealPointName(iColl_indiv, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("acu_addr", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl_com, map, service);
			
			map.clear();
			map.put("indiv_rsd_addr", "STD_GB_AREA_ALL");
			SInfoUtils.addPopName(iColl_indiv, map, service);

			this.putDataElement2Context(iColl_indiv, context);
			this.putDataElement2Context(iColl_com, context);
			
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
