package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;    
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusBaseListOp extends CMISOperation {

    private final String modelId = "CusBase";

    public String doExecute(Context context) throws EMPException {
            
        Connection connection = null;
        String cus_type = (String)context.getDataValue("cus_type");
        try{
            connection = this.getConnection(context);
    
            KeyedCollection queryData = null;
            try {
                    queryData = (KeyedCollection)context.getDataElement(this.modelId);
            } catch (Exception e) {}
            
            String conditionStr = null;
            if("110".equals(cus_type)){
            	conditionStr = " where cus_type = '110' order by cus_id desc";
            }else{
            	conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false)
                                                            +"order by cus_id desc";
            }
            int size = 15;
    
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
    
    
            TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
    
            List<String> list = new ArrayList<String>();
            list.add("cus_id");
            list.add("cus_name");
            list.add("cus_short_name");
            list.add("cus_type");
            list.add("cert_type");
            list.add("cert_code");
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
