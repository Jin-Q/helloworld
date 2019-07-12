package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatBaseComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateFncStatBaseAdtOp extends CMISOperation {

    private final String pk1 = "cus_id";
    private final String pk2 = "stat_prd_style";
    private final String pk3 = "stat_prd";
    private final String pk4 = "FncStatBase.stat_style";
    private final String pk5 = "fnc_type";

    /**
     * 业务逻辑执行的具体实现方法
     */
    public String doExecute(Context context) throws EMPException {
        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);

            // 构件业务处理类
            FncStatBaseComponent thisComponent = (FncStatBaseComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATBASE, context, connection);

            // 取得页面数据
            KeyedCollection kColl = null;
            try {
                kColl = (KeyedCollection) context.getDataElement(PUBConstant.FNCSTATBASE);
            } catch (Exception e) {
            }
            if (kColl == null || kColl.size() == 0)
                throw new CMISException(CMISMessage.MESSAGEDEFAULT, "The values to update[" + PUBConstant.FNCSTATBASE + "] cannot be empty!");

            /*
             * 从context取出数据组装成对象
             */
            FncStatBase domain = new FncStatBase();
            ComponentHelper componetHelper = new ComponentHelper();
            domain = (FncStatBase) componetHelper.kcolTOdomain(domain, kColl);

            String editUsr=(String)context.getDataValue(PUBConstant.currentUserId);
            String editDate=(String)context.getDataValue(PUBConstant.OPENDAY);

            domain.setLastUpdId(editUsr);
            domain.setLastUpdDate(editDate);

            // 修改信息
            flagInfo = thisComponent.modifyFncStatBase(domain);

            // 失败抛出错误提示信息
            if (flagInfo.equals(CMISMessage.DEFEAT)) {
                context.addDataField("flg", "fail");
                throw new CMISException(CMISMessage.MESSAGEDEFAULT, "修改信息失败，请重新操作!");
            } else {
                context.addDataField("flg", "success");
            }

            /**
             * 设置返回信息
             */

            this.doExecute0(context);

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

    /**
     * 执行查询详细信息操作
     */
    public String doExecute0(Context context) throws EMPException {
        Connection connection = null;
        try { 
            
            connection = this.getConnection(context);
            String pk1_value = (String) context.getDataValue(pk1); //  pk1
            String pk2_value = (String) context.getDataValue(pk2); //  pk2
            String pk3_value = (String) context.getDataValue(pk3); //  pk3
            String pk4_value = (String) context.getDataValue(pk4); //  pk3
            String pk5_value = (String) context.getDataValue(pk5);

            // 构件业务处理类
            FncStatBaseComponent thisComponent = (FncStatBaseComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATBASE, context, connection);

            FncStatBase pfncStatBase = new FncStatBase();
            pfncStatBase.setCusId(pk1_value);
            pfncStatBase.setStatPrdStyle(pk2_value);
            pfncStatBase.setStatPrd(pk3_value);
            pfncStatBase.setStatStyle(pk4_value);
            pfncStatBase.setFncType(pk5_value);
            /*
             * 对应条件的记录，并转换数据结构
             */
            // FncStatBase domain = thisComponent.findFncStatBase(pk1_value,
            // pk2_value,pk3_value);
            FncStatBase domain = thisComponent.findFncStatBase(pfncStatBase);
            ComponentHelper ch = new ComponentHelper();
            KeyedCollection kColl = ch.domain2kcol(domain, PUBConstant.FNCSTATBASE);

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
