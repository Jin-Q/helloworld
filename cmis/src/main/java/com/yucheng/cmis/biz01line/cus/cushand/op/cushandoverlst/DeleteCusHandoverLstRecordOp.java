package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverlst;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.ccr.dao.CcrAppInfoDao;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCusHandoverLstRecordOp extends CMISOperation {

    private final String modelId = "CusHandoverLst";

    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        String flag = null;
        try{
            connection = this.getConnection(context);

            String scopeValue = null;
            
            String cus_id_value = null;
            
            String ser_no_value = null;
            try{   
            	//这个值可以为空，如果 scope 不等 1
            	cus_id_value = (String) context.getDataValue("cus_id");
            }catch (Exception e){}
            
            try{
                scopeValue = (String) context.getDataValue("scope");
            }catch (Exception e){}
            
            try{   
            	ser_no_value = (String) context.getDataValue("serno");
            }catch (Exception e){}

            if(ser_no_value==null || ser_no_value.length()==0){
            	throw new EMPException("参数传递错误！未传递serno的值");
            }
            
            if (scopeValue != null){
            	//传递的范围如果有值，那么需要删除全部，页面传递如果 范围是不等于 1 那么需要删除全部，因为 增加明细的时候也是 不是1全部增加
               
                CcrAppInfoDao dao = new CcrAppInfoDao();
                // 借用CcrAppInfoDao中的deleteScoreBySerno这个方法.删掉CUS_HANDOVER_LST表中的该客户数据.
                dao.deleteScoreBySerno("CUS_HANDOVER_LST", ser_no_value,connection);
            }else{
				if(cus_id_value == null || cus_id_value.length() == 0){
					throw new EMPException("参数传递错误！移交范围不是 按客户移交 需要传递 cus_id值");
				}
                TableModelDAO dao = this.getTableModelDAO(context);
                Map<String,String> pkMap = new HashMap<String, String>();
                pkMap.put("serno", ser_no_value);
                pkMap.put("cus_id", cus_id_value);
                int count = dao.deleteByPks(modelId, pkMap, connection);
                if (count != 1){
                    throw new EMPException("Remove Failed! Records :" + count);
                }
            }
            flag = "删除成功";
        }catch (EMPException ee){
            flag = "删除失败";
            throw ee;
        }catch (Exception e){
            throw new EMPException(e);
        }finally{
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        context.addDataField("flag", flag);
        return "0";
    }
}
