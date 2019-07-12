package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * 获取客户移交明细列表
 * 按照单个客户移交的时候选取客户pop
 * @Version bsbcmis
 * @author wuming 2012-3-29 
 * Description:
 */
public class GetCusListForHandoverOp extends CMISOperation {


	private final String modelId = "CusBase";
	

	public String doExecute(Context context) throws EMPException {
		String str = null;
		Connection connection = null;
		try{
			String handover_id = "";
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			//移出客户经理
			try {
				handover_id = (String) context.getDataValue("handover_id");
			} catch (Exception e) {}
			
			if(handover_id == null || handover_id.length() == 0){
				throw new EMPJDBCException("错误！未传递移出客户经理的id");
			}

			/**
			 * 移交客户选择，只能选择自己的客户，根据客户状态正常 并且 客户经理是自己
			 * STD_ZB_CHANGE_TYP 10正常 20托管 30取消托管 40共享 50取消共享
			 */
            /**客户移交申请增加临时客户    modefied by zhaoxp 2015-02-10 start*/
		    String condiTmp =" cus_status in ('00','20','04') and cust_mgr='"+handover_id+"'";
		    /**客户移交申请增加临时客户    modefied by zhaoxp 2015-02-10 end*/
		    TableModelUtil.setCustomizeQueryConditionB(condiTmp, context);
            String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
                                  +" order by cus_id desc";
			//模糊查询
			conditionStr = StringUtil.transConditionStr(conditionStr,"cus_name");

			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("cus_name");
			list.add("cus_type");
			list.add("cert_type");
			list.add("cert_code");
			list.add("cust_mgr");
			list.add("main_br_id");
			list.add("belg_line");
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
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
		return str;
	}

}

