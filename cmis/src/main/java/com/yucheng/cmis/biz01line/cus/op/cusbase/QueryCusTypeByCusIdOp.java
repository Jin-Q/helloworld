package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;    
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusTypeByCusIdOp extends CMISOperation {

    private final String modelId = "CusBase";
    private final String cusIndivModel = "CusIndiv";
    private final String cusComModel = "CusCom";

    public String doExecute(Context context) throws EMPException {
        String flag = "";
        String cusId = "";
        Connection connection = null;
        try{
            connection = this.getConnection(context);
            
            Map<String,Object> cusBaseMap = new HashMap<String,Object>();
            
            if(context.containsKey("cus_id")){
            	
            	cusBaseMap.put("cus_id", context.getDataValue("cus_id"));
            	TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
                
                KeyedCollection cusIndivkColl = dao.queryAllDetail(cusIndivModel, cusBaseMap, connection);
                KeyedCollection cusComkColl = dao.queryDetail(cusComModel, cusBaseMap, connection);

                if(cusIndivkColl.containsKey("cus_id")){
                	try{
                		cusId = (String)cusIndivkColl.getDataValue("cus_id");
                		if(!"".equals(cusId)&&null!=cusId){
                			flag = "1";
                		}
                	}catch(Exception e){}
                }else if(cusComkColl.containsKey("cus_id")){
                	try{
                		cusId = (String)cusComkColl.getDataValue("cus_id");
                		if(!"".equals(cusId)&&null!=cusId){
                			flag = "2";
                		}
                	}catch(Exception e){}
                }
                
            }
            
			context.addDataField("flag", flag);
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
