package com.yucheng.cmis.biz01line.fnc.op.fncconfitems;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfItemsComponent;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteFncConfItemsRecordOp extends CMISOperation {

    private final String pk_name = "item_id";

    /**
     * ҵ���߼�ִ�еľ���ʵ�ַ���
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // 登录人
            String currentUserId = (String) context
                    .getDataValue("currentUserId");
            // 机构
            String organno = (String) context.getDataValue("organNo");
            String openDate = (String) context.getDataValue("OPENDAY");

            String pk1 = (String) context.getDataValue(pk_name); // pk1
            String item_name = (String) context.getDataValue("item_name");
            // ����ҵ������
            FncConfItemsComponent thisComponent = (FncConfItemsComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFITEMS, context, connection);
            // thisComponent.setContext(context);

            // ɾ���¼
            flagInfo = thisComponent.removeFncConfItems(pk1);
            EMPLog
                    .log(this.getClass().getName(), EMPLog.INFO, 0,
                            "-----------------------delete item  start----------------------");
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "该["
                    + organno + "]机构的" + "用户：[" + currentUserId + "]在"
                    + openDate + "时删除该项目[" + pk1 + "]" + item_name);
            EMPLog
                    .log(this.getClass().getName(), EMPLog.INFO, 0,
                            "-----------------------delete item  end------------------------");

            // ʧ���׳������ʾ��Ϣ
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "ɾ����Ϣʧ�ܣ������²���!");
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
