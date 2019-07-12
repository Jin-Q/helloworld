package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryQryTempletListOp extends CMISOperation {

    private final String modelId = "QryTemplet";

    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
//        String organNo = null;
        try {
            connection = this.getConnection(context);

            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection) context.getDataElement(this.modelId);
            } catch (Exception e) {
            }
//            try {
//            	organNo = (String) context.getDataValue("organNo");
//            } catch (Exception e) {
//                throw new ComponentException("读取机构信息失败");
//            }
//            TableModelDAO orgDao = (TableModelDAO) this.getTableModelDAO(context);
//            KeyedCollection orgInfo = orgDao.queryDetail("SOrg", organNo, connection);
            //机构级别
//            String organlevel = (String) orgInfo.getDataValue("organlevel");
            String orgConditionStr="";

            String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
            
            conditionStr = StringUtil.transConditionStr(conditionStr, "temp_name");
            conditionStr = StringUtil.transConditionStr(conditionStr, "templet_type");
//            
//            if (queryData == null) {
//                orgConditionStr = " where ORGANLEVEL like '%" + organlevel + "%' ";
//            } else {
//            	if(conditionStr == null || "".equals(conditionStr)) {
//            		orgConditionStr = " where ORGANLEVEL like '%" + organlevel + "%' ";
//            	} else {
//            		orgConditionStr = " and ORGANLEVEL like '%" + organlevel + "%' ";
//            	}
//            }
            conditionStr +=  orgConditionStr + "order by templet_type,temp_name desc";//"order by to_number(order_id),temp_no desc"
            int size = 15;
            
            PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
    
            TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
            IndexedCollection iColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);
            iColl.setName(iColl.getName() + "List");
            
            //翻译机构
			SInfoUtils.addPrdPopName("SOrg",iColl, "organlevel", "organno", "organname", ",", connection, dao);  //翻译适用机构
            
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
