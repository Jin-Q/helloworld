package com.yucheng.cmis.biz01line.fnc.op.fncconfdefformat;


import java.sql.Connection;

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
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateFncConfDefFormatRecordOp extends CMISOperation {

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {

            connection = this.getConnection(context);
            // 构件业务处理类
            FncConfDefFormatComponent thisComponent = (FncConfDefFormatComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCCONFDEFFORMAT, context, connection);

            // 取得页面数据
            KeyedCollection kColl = null;
            try {
                kColl = (KeyedCollection) context
                        .getDataElement(PUBConstant.FNCCONFDEFFORMAT);
            } catch (Exception e) {
            }
            if (kColl == null || kColl.size() == 0)
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "The values to update[" + PUBConstant.FNCCONFDEFFORMAT
                                + "] cannot be empty!");

            /*
             * 从context取出数据组装成对象
             */
            FncConfDefFormat domain = new FncConfDefFormat();
            ComponentHelper componetHelper = new ComponentHelper();
            domain = (FncConfDefFormat) componetHelper.kcolTOdomain(domain,
                    kColl);

            // 修改信息
            flagInfo = thisComponent.modifyFncConfDefFormat(domain);

            // 失败抛出错误提示信息
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                        "修改信息失败，请重新操作!");
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
