package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusComListOpforGrp extends CMISOperation {

	// ��Ҫ����ı�ģ��
	private final String modelId = "CusCom";

	/**
	 * ҵ���߼�ִ�еľ���ʵ�ַ���
	 */
	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);

			// ��ò�ѯ�Ĺ������
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context
						.getDataElement(this.modelId);
			} catch (Exception e) {
			}
			String openFlag = "";
			try {
				openFlag = (String) context.getDataValue("openFlag");
				
				String cus_id = (String) queryData.getDataValue("cus_id");
				String cus_name = (String) queryData.getDataValue("cus_name");
				String cert_type = (String) queryData.getDataValue("cert_type");
				String cert_code = (String) queryData.getDataValue("cert_code");
				String main_br_id = (String) queryData.getDataValue("main_br_id");
				context.addDataField("cus_id", cus_id);
				context.addDataField("cus_name", cus_name);
				context.addDataField("cert_type", cert_type);
				context.addDataField("cert_code", cert_code);
				context.addDataField("main_br_id", main_br_id);

				queryData = null;
			} catch (Exception e) {
			}
			if (openFlag != null && openFlag.equals("open"))
			{
				// context.setAttribute("openFlag", openFlag);
			}
			// ��ò�ѯ�������������ȷ��ѯ�����Կ�ֵ
			String conditionStr = TableModelUtil.getQueryCondition(
					this.modelId, queryData, context, false, false, false);

			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_id");
			conditionStr = StringUtil.transConditionStr(conditionStr,
					"cert_code");
			if (conditionStr == null || conditionStr.trim().equals(""))
				conditionStr = " where 1=1 ";

			String currId = (String) context.getDataValue("currentUserId");

			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("actorno", currId);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,
					conditionStr, context, connection);
			boolean isFXB = false;				//检查是否该用户有角色
			if(conditionStr.trim().equals("")){
				conditionStr = " where";
			}else {
				conditionStr = conditionStr + " and  ";
			}
			
			conditionStr += "  cus_id not in (select parent_cus_id from Cus_Grp_Info_Apply) " +
					"and cus_id not in " +" (select cus_id from CUS_GRP_MEMBER_APPLY )  " +
					"and  cus_id in (select cus_id from cus_com ) " ;	
			if(isFXB){												//如果是风险管理，则能看到全行的数据
				conditionStr += " " ;
			}else{
				conditionStr +=  " and CUST_MGR='"+currId+"' ";
			}
			int size = 10;
			// ����ֻ�ڵ�һ�β�ѯ�ܼ�¼��
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			// ��ȡ���Է�ҳ��OracleDao����
			TableModelDAO dao = this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
	
			IndexedCollection iColl = dao.queryList(modelId, list,
					conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

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
