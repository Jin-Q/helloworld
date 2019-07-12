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
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryFncStatBaseDetailOp extends CMISOperation {

    private final String pk1 = "cus_id";
    private final String pk2 = "stat_prd_style";
    private final String pk3 = "stat_prd";
    private final String pk4 = "stat_style";
    private final String pk5 = "fnc_type";

    /**
     * 执行查询详细信息操作
     */
    public String doExecute(Context context) throws EMPException {
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String pk1_value = (String) context.getDataValue(pk1);
            String pk2_value = (String) context.getDataValue(pk2);
            String pk3_value = (String) context.getDataValue(pk3);
            String pk4_value = (String) context.getDataValue(pk4);
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
            SInfoUtils.addUSerName(kColl, new String[]{"input_id","last_upd_id"});
            // 将参数放入context
            this.putDataElement2Context(kColl, context);

            // 将财报状态放入到context中
            context.addDataField("state_flg", domain.getStateFlg());
        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
            throw e;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        context.addDataField("flg", "");
        return PUBConstant.SUCCESS;
    }

}
