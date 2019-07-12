package com.yucheng.cmis.biz01line.fnc.op.fncotherreceivable;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.detail.component.FncOthRecComponent;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;

/**
 * @Classname DeleteFncOtherReceivableRecordOp.java
 *@Version 1.0
 *@Since 1.0 Oct 8, 2008 2:43:00 PM
 *@Copyright yuchengtech
 *@Author Administrator
 *@Description：
 *@Lastmodified
 *@Author
 */
public class DeleteFncOtherReceivableRecordOp extends CMISOperation {

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // 获取主键值
            String pk1_value = (String) context.getDataValue("cus_id");
            String pk2_value = (String) context.getDataValue("fnc_ym");
            String pk3_value = (String) context.getDataValue("fnc_typ");
            String seq = (String) context.getDataValue("seq");
            int pk4_value = Integer.valueOf(seq); // pk2

            // 构件业务处理类
            FncOthRecComponent thisComponent = (FncOthRecComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            "FncOthRec", context, connection);

            // 删除记录
            flagInfo = thisComponent.removeFncDetOthRec(pk1_value, pk2_value,
                    pk3_value, pk4_value);

            // 失败抛出错误提示信息
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "删除信息失败，请重新操作!");
            }

        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR,
                    0, message);
            throw e;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return flagInfo;
    }
}
