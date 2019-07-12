package com.yucheng.cmis.biz01line.report;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author user
*@time 2015-7-17
*@description TODO 需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更
*@version v1.0
*
 */
public class GetGuarantyInfo4CusBaseListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cus_id = null;
			String manager_id = null;
			String cus_properties = null;
			
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");  
			}
			if(context.containsKey("manager_id")){
				manager_id = (String)context.getDataValue("manager_id");  
			}
			if(context.containsKey("cus_properties")){
				cus_properties = (String)context.getDataValue("cus_properties");  
			}
			
			context.put("cus_id", cus_id);
			context.put("manager_id", manager_id);
			context.put("cus_properties", cus_properties);
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection GuarantyInfo4CusBaseList = new IndexedCollection();
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conditionSelect = "SELECT DISTINCT GUAR_CONT_NO,"
			+"                GUAR_CONT_CN_NO,                        "
			+"                GUAR_START_DATE,                        "
			+"                GUAR_END_DATE,                          "
			+"                GUAR_CONT_STATE,                        "
			+"                MANAGER_ID,                             "
			+"                MANAGER_BR_ID                           "
			+"  FROM (SELECT P1.GUAR_CONT_NO,                         "
			+"               P1.GUAR_CONT_CN_NO,                      "
			+"               P1.GUAR_START_DATE,                      "
			+"               P1.GUAR_END_DATE,                        "
			+"               P1.GUAR_CONT_STATE,                      "
			+"               P1.MANAGER_ID,                           "
			+"               P1.MANAGER_BR_ID                         "
			+"          FROM GRT_GUAR_CONT P1, R_LMT_GUAR_CONT P2     "
			+"         WHERE P1.GUAR_CONT_NO = P2.GUAR_CONT_NO        "
			+"           AND P1.GUAR_CONT_STATE = '01'                "
			+"           AND P2.AGR_NO IN                             "
			+"               (SELECT L.AGR_NO                         "
			+"                  FROM LMT_NAME_LIST L                  "
			+"                 WHERE L.CUS_ID = '"+cus_id+"')   "
			+"        UNION ALL                                       "
			+"        SELECT P1.GUAR_CONT_NO,                         "
			+"               P1.GUAR_CONT_CN_NO,                      "
			+"               P1.GUAR_START_DATE,                      "
			+"               P1.GUAR_END_DATE,                        "
			+"               P1.GUAR_CONT_STATE,                      "
			+"               P1.MANAGER_ID,                           "
			+"               P1.MANAGER_BR_ID                         "
			+"          FROM GRT_GUAR_CONT P1, R_LMT_GUAR_CONT P2     "
			+"         WHERE P1.GUAR_CONT_NO = P2.GUAR_CONT_NO        "
			+"           AND P1.GUAR_CONT_STATE = '01'                "
			+"           AND P2.AGR_NO IN                             "
			+"               (SELECT T1.AGR_NO                        "
			+"                  FROM LMT_AGR_INFO T1, LMT_AGR_GRP T2  "
			+"                 WHERE T1.GRP_AGR_NO = T2.GRP_AGR_NO    "
			+"                   AND T1.CUS_ID = '"+cus_id+"')  "
			+"        UNION ALL                                       "
			+"        SELECT P1.GUAR_CONT_NO,                         "
			+"               P1.GUAR_CONT_CN_NO,                      "
			+"               P1.GUAR_START_DATE,                      "
			+"               P1.GUAR_END_DATE,                        "
			+"               P1.GUAR_CONT_STATE,                      "
			+"               P1.MANAGER_ID,                           "
			+"               P1.MANAGER_BR_ID                         "
			+"          FROM GRT_GUAR_CONT P1, GRT_GUARANTY_RE P2     "
			+"         WHERE P1.GUAR_CONT_NO = P2.GUAR_CONT_NO        "
			+"           AND P1.GUAR_CONT_STATE = '01'          "
			+"           AND P2.GUARANTY_ID IN                        "
			+"               (SELECT GUARANTY_NO                      "
			+"                  FROM MORT_GUARANTY_BASE_INFO          "
			+"                 WHERE CUS_ID = '"+cus_id+"')     "
			+"        UNION ALL                                       "
			+"        SELECT P1.GUAR_CONT_NO,                         "
			+"               P1.GUAR_CONT_CN_NO,                      "
			+"               P1.GUAR_START_DATE,                      "
			+"               P1.GUAR_END_DATE,                        "
			+"               P1.GUAR_CONT_STATE,                      "
			+"               P1.MANAGER_ID,                           "
			+"               P1.MANAGER_BR_ID                         "
			+"          FROM GRT_GUAR_CONT P1, GRT_GUARANTY_RE P2     "
			+"         WHERE P1.GUAR_CONT_NO = P2.GUAR_CONT_NO        "
			+"           AND P1.GUAR_CONT_STATE = '01'          "
			+"           AND P2.GUARANTY_ID IN                        "
			+"               (SELECT GUAR_ID                          "
			+"                  FROM GRT_GUARANTEE                    "
			+"                 WHERE CUS_ID = '"+cus_id+"'))    ";
			GuarantyInfo4CusBaseList = TableModelUtil.buildPageData(pageInfo, dataSource, conditionSelect);
			GuarantyInfo4CusBaseList.setName("GuarantyInfo4CusBaseList");
			this.putDataElement2Context(GuarantyInfo4CusBaseList, context);  
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
