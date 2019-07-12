package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.acc.component.AccComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
 * <pre> 
 * Title:贷后业务信息
 * Description: 贷后业务信息
 * </pre>
 * @author yangzy
 * 创建日期：2014/10/17
 * @version 1.00.00
 * <pre>
 *    修改后版本:        修改人：         修改日期:              修改内容: 
 * </pre>
 */

public class QueryPspAccViewPop extends CMISOperation {
	private final String modelId = "AccLoan";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
        String condition = "";
        try{
            connection = this.getConnection(context);
            
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement(this.modelId);
            } catch (Exception e) {}
            String cus_id = "";
            String task_id = "";
            if(context.containsKey("cus_id")&&context.getDataValue("cus_id")!=null&&!"".equals(context.getDataValue("cus_id"))){
            	cus_id = (String)context.getDataValue("cus_id");
            }
            if(context.containsKey("task_id")&&context.getDataValue("task_id")!=null&&!"".equals(context.getDataValue("task_id"))){
            	task_id = (String)context.getDataValue("task_id");
            }
            context.put("cus_id", cus_id);
            context.put("task_id", task_id);
            String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
            
            if("".equals(conditionStr)||conditionStr==null){
            	conditionStr = " where 1=1 " ;
            }
            conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
            String conditionStr1 = conditionStr + " and bill_no in (select bill_no from psp_check_task_rel where task_id = '"+task_id+"')";
            String conditionStr2 = conditionStr + " and cus_id = '"+cus_id+"' and status = '1' ";
            
            int size = 15;
            
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
    
            DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
    		AccComponent accComponent = (AccComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("AccComponent", context, connection);
            
    		IndexedCollection iColl = accComponent.getPvpContForPsp(conditionStr1,conditionStr2,cus_id,task_id, dataSource, pageInfo);
            iColl.setName("AccLoanList");
            
            String[] args=new String[] {"cus_id","prd_id" };
            String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
            String[]modelForeign=new String[]{"cus_id","prdid"};
            String[] fieldName=new String[]{"cus_name","prdname"};
            //详细信息翻译时调用			
            SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
            SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
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