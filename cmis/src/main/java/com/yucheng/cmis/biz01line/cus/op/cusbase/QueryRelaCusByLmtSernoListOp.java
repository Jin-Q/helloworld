package com.yucheng.cmis.biz01line.cus.op.cusbase;

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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * 查询关联客户POP框（个人额度申请专用，包含主借款人及配偶、共同债务人及配偶、保证人及配偶）
 * 
 * @author zhaozq
 *
 */
public class QueryRelaCusByLmtSernoListOp extends CMISOperation {
		
		private final String modelId = "CusBase";
	
        public String doExecute(Context context) throws EMPException {
                
                Connection connection = null;
                String lmt_serno = "";
                KeyedCollection queryData = null;
                try{
                    connection = this.getConnection(context);
                    
                    try {
                      queryData = (KeyedCollection)context.getDataElement(this.modelId);
                    } catch (Exception e) {}
                    
                    try {
                    	lmt_serno = (String) context.getDataValue("lmt_serno");
                    } catch (Exception e) {
                    	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到额度申请流水号", null);}
                    
                    String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
                    
                    int size = 15;
                    PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
            
                    DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
                    
                    String sql_select =SqlClient.joinQuerySql("queryRelaCusIdByLmtSerno","'"+lmt_serno+"'",null);
                    sql_select = "select * from ("+sql_select+")"+conditionStr;
        			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
                    
                    iColl.setName("CusBaseList");
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
                return "0";
        }

}
