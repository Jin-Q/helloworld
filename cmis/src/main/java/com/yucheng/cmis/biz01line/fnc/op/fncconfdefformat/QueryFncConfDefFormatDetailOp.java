package com.yucheng.cmis.biz01line.fnc.op.fncconfdefformat;


import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfDefFormatComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryFncConfDefFormatDetailOp extends CMISOperation {

    private final String pk1 = "style_id";
    private final String pk2 = "item_id";

    /**
     * 执行查询详细信息操作
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String pk1_value = (String) context.getDataValue(pk1); // pk1
            String pk2_value = (String) context.getDataValue(pk2); // pk2

            String fnc_conf_type = (String) context
                    .getDataValue("fnc_conf_typ");

            // 构件业务处理类
            FncConfDefFormatComponent thisComponent = (FncConfDefFormatComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFDEFFORMAT, context, connection);

            /*
             * 对应条件的记录，并转换数据结构
             */
            FncConfDefFormat domain = thisComponent.findFncConfDefFormat(
                    pk1_value, pk2_value);
            ComponentHelper ch = new ComponentHelper();
            KeyedCollection kColl = ch.domain2kcol(domain,
                    PUBConstant.FNCCONFDEFFORMAT);

            // 将参数放入context
            this.putDataElement2Context(kColl, context);

            List list = null;
            list = thisComponent.getFormats(pk1_value);
            context.addDataField("formatList", list);

            /**
             * 得到样式的类型
             */
            String fnc_conf_typ = thisComponent.getFncConfTyp(pk1_value);
            /**
             * 得到具体一种样式类型下的所有item
             */
            List itemList = thisComponent.getItems(fnc_conf_typ);
            context.addDataField("itemList", itemList);
            context.setDataValue("style_id", pk1_value);

            context.setDataValue("fnc_conf_typ", fnc_conf_type);

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
