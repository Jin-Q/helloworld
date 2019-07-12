package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryImportIqpBatchListOp extends CMISOperation {
	
	private final String batchModel = "IqpBatchMng";
	/**
	 * 查询可导入的批次信息：
     *
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(batchModel);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition(batchModel, queryData, context, false, false, false);
			if(context.containsKey("restrictUsed")&&"false".equals(context.getDataValue("restrictUsed"))){
				
			}else{
				//添加记录级权限	
				if(conditionStr.indexOf("WHERE") != -1){
					conditionStr = (RestrictUtil.getNewRestrictSelf(this.batchModel, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
				}else {
					conditionStr  = (RestrictUtil.getNewRestrictSelf(this.batchModel, connection, context)+conditionStr);
				}
			}
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//登记中且没有被其他业务引用，才进行显示
			if(conditionStr==null || conditionStr.length() == 0){
				conditionStr = " where status ='01' and serno is null ";
			}
			
			//增加产品过滤
			String prd_id = "";
			if(context.containsKey("prd_id")){
				prd_id = (String) context.getDataValue("prd_id");
			}
			if("300021".equals(prd_id)||"300020".equals(prd_id)){//贴现产品，只显示直贴
				conditionStr = conditionStr + " and biz_type = '07' ";
			}else{
				conditionStr = conditionStr + " and biz_type <> '07' ";
			}
			
			conditionStr = conditionStr+"order by batch_no desc";
			
			/**
			String conditionStr = " where porder_no not in (select distinct(porder_no) from iqp_batch_bill_rel) or " +
					"porder_no in (select distinct(t1.porder_no) " +
					"from iqp_batch_bill_rel t1,iqp_batch_mng t2 where t1.batch_no=t2.batch_no and t2.status='')";
			*/
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			IndexedCollection iColl = dao.queryList(batchModel,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
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
