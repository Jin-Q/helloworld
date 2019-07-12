package com.yucheng.cmis.biz01line.fnc.op.fncconftemplate;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfTemplateComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateFncConfTemplateRecordOp extends CMISOperation {

    /**
     * ҵ���߼�ִ�еľ���ʵ�ַ���
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {

            connection = this.getConnection(context);
            // ����ҵ������
            FncConfTemplateComponent thisComponent = (FncConfTemplateComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFTEMPLATE, context, connection);
            // thisComponent.setContext(context);

            // ȡ��ҳ�����
            KeyedCollection kColl = null;
            try {
                kColl = (KeyedCollection) context
                        .getDataElement(PUBConstant.FNCCONFTEMPLATE);
            } catch (Exception e) {
            }
            if (kColl == null || kColl.size() == 0)
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "The values to update[" + PUBConstant.FNCCONFTEMPLATE
                                + "] cannot be empty!");

            /*
             * ��contextȡ�������װ�ɶ���
             */
            FncConfTemplate domain = new FncConfTemplate();
            ComponentHelper componetHelper = new ComponentHelper();
            domain = (FncConfTemplate) componetHelper.kcolTOdomain(domain,
                    kColl);

            // �޸���Ϣ
            flagInfo = thisComponent.modifyFncConfTemplate(domain);

            // ʧ���׳������ʾ��Ϣ
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "�޸���Ϣʧ�ܣ������²���!");
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
