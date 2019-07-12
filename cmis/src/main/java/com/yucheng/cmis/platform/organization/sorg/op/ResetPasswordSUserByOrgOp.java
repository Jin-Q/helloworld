package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.MD5;
import com.yucheng.cmis.util.TableModelUtil;

public class ResetPasswordSUserByOrgOp extends CMISOperation {

    // 所要操作的表模型
    private final String modelId = "SUser";

    // 所要操作的表模型的主键
    private  String orgno = "orgno";

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);         
            try {
                orgno = (String)context.getDataValue("orgno");
            } catch (Exception e) {
            }
            if (orgno == null || orgno.length() == 0)
                throw new EMPJDBCException("机构号[" + orgno + "] 不存在!");

            KeyedCollection queryData = null;

            try {
                queryData = (KeyedCollection) context
                        .getDataElement(this.modelId);
            } catch (Exception e) {
            }

            String conditionStr = TableModelUtil.getQueryCondition(
                    this.modelId, queryData, context, false, true, false);
            conditionStr+="where orgid in (select organno from s_org where  locate like '%"+orgno+"%')";
            TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
            IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
            iColl.setName(iColl.getName() + "List");

         
            KeyedCollection kColl = null;
            for (int i = 0; i < iColl.size(); i++) {
                // 获得查询需要的主键信息
                kColl = (KeyedCollection) iColl.get(i);

                String user = (String) kColl.getDataValue("actorno");
    
                MD5.md5Str2Kcol(kColl, user);
                int count = dao.update(kColl, connection);
                if (count != 1) {                
                    EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "用户：" + kColl.getDataValue("actorno") + "密码重置失败！操作影响了" + count + "条记录");
                    context.addDataField("flag", "failure");
        			context.addDataField("msg", "用户：" + kColl.getDataValue("actorno") + "密码重置失败！操作影响了" + count + "条记录");
        			return null;
                    // throw new EMPException("重置失败！操作影响了"+count+"条记录");
                }
                // this.putDataElement2Context(kColl, context);
                // System.err.println(">>>>\n" + kColl.toString());
            }
            context.addDataField("flag", "success");
			context.addDataField("msg", "");
        } catch (EMPException ee) {
        	context.addDataField("flag", "failure");
			context.addDataField("msg", "操作失败！失败原因："+ee.getMessage());
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
