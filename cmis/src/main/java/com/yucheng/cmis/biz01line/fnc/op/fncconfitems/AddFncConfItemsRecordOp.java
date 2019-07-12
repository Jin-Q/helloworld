package com.yucheng.cmis.biz01line.fnc.op.fncconfitems;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfItemsComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddFncConfItemsRecordOp extends CMISOperation {

    /**
     * ҵ���߼�ִ�еľ���ʵ�ַ���
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        String flg = "";
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            FncConfItems domain = new FncConfItems();// �������

            // ����ҵ������
            FncConfItemsComponent thisComponent = (FncConfItemsComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFITEMS, context, connection);
            // thisComponent.setContext(context);

            /*
             * ��contextȡ����ݶ���
             */

            KeyedCollection kColl = (KeyedCollection) context
                    .getDataElement(PUBConstant.FNCCONFITEMS);
            ComponentHelper componetHelper = new ComponentHelper();
            domain = (FncConfItems) componetHelper.kcolTOdomain(domain, kColl);
            String item_id = domain.getItemId();
            FncConfItems tempItem = thisComponent.findFncConfItems(item_id);

            if (tempItem != null && tempItem.getItemId() == null) {
                // 新增
                flagInfo = thisComponent.addFncConfItems(domain);
                // 新增失败
                if (flagInfo.equals(CMISMessage.DEFEAT)) {
                    throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                            "������Ϣʧ�ܣ������²���!");
                } else {
                    flg = "new";
                }
            } else {
                flg = "update";// 如果存在该项目编号，则进入修改页面
            }
            context.addDataField("item_id", item_id);
            context.addDataField("flag", flg);

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
        return "0";
    }

}
