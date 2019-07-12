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
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryFncConfTemplateDetailOp extends CMISOperation {

    private final String pk_name = "fnc_id";

    /**
     * ִ�в�ѯ��ϸ��Ϣ����
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // j�����1
            String pk1 = (String) context.getDataValue(pk_name);

            // ����ҵ������
            FncConfTemplateComponent thisComponent = (FncConfTemplateComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFTEMPLATE, context, connection);
            // thisComponent.setContext(context);

            /*
             * ��Ӧ����ļ�¼����ת����ݽṹ
             */
            FncConfTemplate domain = thisComponent.findFncConfTemplate(pk1);
            ComponentHelper ch = new ComponentHelper();
            KeyedCollection kColl = ch.domain2kcol(domain,
                    PUBConstant.FNCCONFTEMPLATE);

            context.addDataField("fnc_bs_style_id", domain.getFncBsStyleId());
            context.addDataField("fnc_cf_style_id", domain.getFncCfStyleId());
            context.addDataField("fnc_fi_style_id", domain.getFncFiStyleId());
            context.addDataField("fnc_pl_style_id", domain.getFncPlStyleId());
            context.addDataField("fnc_ri_style_id", domain.getFncRiStyleId());
            context.addDataField("fnc_smp_style_id", domain.getFncSmpStyleId());

            // ���������context
            this.putDataElement2Context(kColl, context);

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

        return PUBConstant.SUCCESS;
    }

}
