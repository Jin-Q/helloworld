package com.yucheng.cmis.biz01line.fnc.op.fncconfitems;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncConfItemsListOp extends CMISOperation {
	
	//��Ҫ����ı�ģ��
	private final String modelId = "FncConfItems";
	
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
			
			//添加模糊查询的功能
			conditionStr = StringUtil.transConditionStr(conditionStr, "item_name");
			
			int size = 15;
			//����ֻ�ڵ�һ�β�ѯ�ܼ�¼��
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//��ȡ���Է�ҳ��OracleDao����
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("item_id");
			list.add("item_name");
			list.add("fnc_conf_typ");
			list.add("fnc_no_flg");
			list.add("remark");
			list.add("formula");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
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
