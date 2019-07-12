package com.yucheng.cmis.biz01line.fnc.op.fncconftemplate;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncConfTemplateListForCusOp extends CMISOperation {
	
	//��Ҫ����ı�ģ��
	private final String modelId = "FncConfTemplate";
	
	/**
	 * ҵ���߼�ִ�еľ���ʵ�ַ���
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
		//��ò�ѯ�Ĺ������
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			//��ò�ѯ�������������ȷ��ѯ�����Կ�ֵ
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"";
			
			/*if (conditionStr == null || conditionStr.equals("")) {
				conditionStr += " where 1=1 and NO_IND is null";
			}else {
				conditionStr+= " and NO_IND is null";
			}*/
			
			if(conditionStr == null || "".equals(conditionStr)){
				
				conditionStr = " where 1=1 ";
			}
			conditionStr = conditionStr + " and ( NO_IND <> '1' or NO_IND is null )" ;
	
			//��ȡ���Է�ҳ��OracleDao����
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("fnc_id");
			list.add("fnc_name");
			list.add("fnc_bs_style_id");
			list.add("fnc_pl_style_id");
			list.add("fnc_cf_style_id");
			list.add("fnc_fi_style_id");
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,connection);
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
