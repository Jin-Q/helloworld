package com.yucheng.cmis.biz01line.lmt.op.lmtindusagr;

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
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtIndusPopOp extends CMISOperation {

    private final String modelId = "LmtIndusAgr";

    public String doExecute(Context context) throws EMPException {
            
           Connection connection = null;
            try{
                connection = this.getConnection(context);
                
                KeyedCollection queryData = null;
                try {
                    queryData = (KeyedCollection)context.getDataElement(this.modelId);
                } catch (Exception e) {}       
    			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);			
    			if(conditionStr.indexOf("WHERE")==-1){
    				conditionStr = "where to_char(add_months(to_date(end_date,'yyyy-mm-dd'),6),'yyyy-mm-dd')>(select openday from pub_sys_info)  " ;
    			}else{
    				conditionStr = conditionStr + " and to_char(add_months(to_date(end_date,'yyyy-mm-dd'),6),'yyyy-mm-dd')>(select openday from pub_sys_info)  " ;
    			}
                
                int size = 10;        
                PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));        
                TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
        
                List<String> list = new ArrayList<String>();
                list.add("serno");
                list.add("agr_no");
                list.add("indus_type");
                list.add("manager_id");
                list.add("manager_br_id");
                list.add("is_list_mana");
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