package com.yucheng.cmis.biz01line.fnc.op.fncotherreceivable;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.detail.component.FncOthRecComponent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOthReceive;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * @Classname QueryFncOtherReceivableDetailOp.java
 *@Version 1.0
 *@Since 1.0 Oct 8, 2008 2:44:00 PM
 *@Copyright yuchengtech
 *@Author Administrator
 *@Description：
 *@Lastmodified
 *@Author
 */
public class QueryFncOtherReceivableDetailOp extends CMISOperation {

    /**
     * 执行查询详细信息操作
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String pk1_value = (String) context.getDataValue("cus_id"); // pk1
            String pk2_value = (String) context.getDataValue("fnc_ym"); // pk2
            String pk3_value = (String) context.getDataValue("fnc_typ");
            String pk4_val = (String) context.getDataValue("seq");
            int pk4_value = Integer.valueOf(pk4_val);

            // 构件业务处理类
            FncOthRecComponent thisComponent = (FncOthRecComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            "FncOthRec", context, connection);

            /*
             * 对应条件的记录，并转换数据结构
             */
            FncOthReceive domain = thisComponent.findFncDetAccPay(pk1_value,
                    pk2_value, pk3_value, pk4_value);
            ComponentHelper ch = new ComponentHelper();
            KeyedCollection kColl = ch.domain2kcol(domain,
                    PUBConstant.FNCOTHERRECEIVABLE);

            // 将参数放入context
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
