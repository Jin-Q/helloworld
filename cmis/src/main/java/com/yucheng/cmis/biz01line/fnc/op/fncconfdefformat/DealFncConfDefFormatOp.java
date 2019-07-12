package com.yucheng.cmis.biz01line.fnc.op.fncconfdefformat;


//import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfDefFormatComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DealFncConfDefFormatOp extends CMISOperation {

    @Override
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String styleId = (String) context.getDataValue("style_id");
            String fnc_conf_type = (String) context
                    .getDataValue("fnc_conf_typ");
            // 构件业务处理类
            FncConfDefFormatComponent thisComponent = (FncConfDefFormatComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFDEFFORMAT, context, connection);

            List list = null;
            list = thisComponent.getFormats(styleId);
            context.addDataField("formatList", list);
            /**
             * 得到样式的类型
             */
            String fnc_conf_typ = thisComponent.getFncConfTyp(styleId);
            /**
             * 得到具体一种样式类型下的所有item
             */
            List itemList = thisComponent.getItems(fnc_conf_typ);
            context.addDataField("itemList", itemList);
            context.setDataValue("style_id", styleId);
            context.setDataValue("fnc_conf_typ", fnc_conf_type);
        } catch (EMPException ee) {
            throw ee;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return PUBConstant.SUCCESS;
    }

}
