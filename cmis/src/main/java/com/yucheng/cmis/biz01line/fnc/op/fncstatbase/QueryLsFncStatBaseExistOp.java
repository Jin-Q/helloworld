package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryLsFncStatBaseExistOp extends CMISOperation {

    private final String pk1 = "cus_id";
    private final String pk2 = "stat_prd_style";
    private final String pk3 = "stat_prd";
    private final String pk4 = "stat_style";

    /**
     * 执行查询详细信息操作
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String type = (String) context.getDataValue("type");
            String cusId = (String) context.getDataValue(pk1); // pk1
            String statPrdStyle = (String) context.getDataValue(pk2); // pk2
            String statPrd = (String) context.getDataValue(pk3); // pk3
            String statStyle = (String) context.getDataValue(pk4); // pk3
            String returnFlag = "";
            
            TableModelDAO dao = this.getTableModelDAO(context);
            String condition = "";
            IndexedCollection iCollls = null;
            if("lsYear".equals(type)){//取上年年报
            	condition = "where cus_id='"+cusId+"' and stat_prd_style='"+statPrdStyle+"' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
            	iCollls = dao.queryList("FncStatBase", condition, connection);
            	if(iCollls.size()>0){
            		returnFlag = PUBConstant.EXISTS;
            	}else {
            		returnFlag = PUBConstant.NOTEXISTS;
            	}
            }else{//损益表取上年同期
            	condition = "where cus_id='"+cusId+"' and stat_prd_style='"+statPrdStyle+"' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
            	iCollls = dao.queryList("FncStatBase", condition, connection);
            	if(iCollls.size()>0){
            		returnFlag = PUBConstant.EXISTS;
            	}else{
            		condition = "where cus_id='"+cusId+"' and stat_prd_style='4' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
                	iCollls = dao.queryList("FncStatBase", condition, connection);
                	if(iCollls.size()>0){
                		returnFlag = PUBConstant.EXISTS;
                	}else{
                		condition = "where cus_id='"+cusId+"' and stat_prd_style='3' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
                    	iCollls = dao.queryList("FncStatBase", condition, connection);
                    	if(iCollls.size()>0){
                    		returnFlag = PUBConstant.EXISTS;
                    	}else{
                    		condition = "where cus_id='"+cusId+"' and stat_prd_style='2' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
                        	iCollls = dao.queryList("FncStatBase", condition, connection);
                        	if(iCollls.size()>0){
                        		returnFlag = PUBConstant.EXISTS;
                        	}else{
                        		condition = "where cus_id='"+cusId+"' and stat_prd_style='1' and stat_prd='"+statPrd+"' and stat_style='"+statStyle+"' and state_flg like '%2'";
                            	iCollls = dao.queryList("FncStatBase", condition, connection);
                            	if(iCollls.size()>0){
                            		returnFlag = PUBConstant.EXISTS;
                            	}else{
                            		returnFlag = PUBConstant.NOTEXISTS;
                            	}
                        	}
                    	}
                	}
            	}
            }
            context.addDataField("flag", returnFlag);
        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            throw e;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return "";
    }

}
