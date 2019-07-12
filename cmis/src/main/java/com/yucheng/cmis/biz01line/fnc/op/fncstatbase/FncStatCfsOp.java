package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;

/**
 *@Classname FncStatCfsOp.java
 *@Version 1.0
 *@Since 1.0 Oct 13, 2008 5:28:47 PM
 *@Copyright yuchengtech
 *@Author an
 *@Description：
 *@Lastmodified
 *@Author
 */
public class FncStatCfsOp extends CMISOperation {

    public String doExecute(Context context) throws EMPException {

        FncStatBase pfncStatBase = null; // 财报对象
        FncConfStyles pfncConfStyles = null; // 带有数据的样式对象
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // 构件业务处理类
            FncStatCommonComponent fCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCSTATCOMMON, context, connection);

            /**
             * 从context中取出cusId(客户编号),statPrdStyle(报表周期类型),statPrd(报表期间
             * 格式：YYYYMM)
             */
            String cusId = (String) context.getDataValue("cus_id");
            if (cusId == null || cusId.length() == 0) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的客户编号为空");
                return PUBConstant.FAIL;
            }
            String statPrdStyle = (String) context
                    .getDataValue("stat_prd_style");
            if (statPrdStyle == null || statPrdStyle.length() == 0) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的报表周期类型为空");
                return PUBConstant.FAIL;
            }
            String statPrd = (String) context.getDataValue("stat_prd");
            if (statPrd == null || statPrd.length() == 0) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的报表期间类型为空");
                return PUBConstant.FAIL;
            }
            String stat_style = (String) context.getDataValue("stat_style");
            if (stat_style == null || stat_style.length() == 0) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的报表口径为空");
                return PUBConstant.FAIL;
            }
            /**
             * 获取到财报的对象,将财报对象放入到context中
             */
            FncStatBase tempFB = new FncStatBase();
            tempFB.setCusId(cusId);
            tempFB.setStatPrdStyle(statPrdStyle);
            tempFB.setStatPrd(statPrd);

            pfncStatBase = fCommonComponent.findOneFncStatBase(tempFB);
            if (pfncStatBase == null) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的财报对象为空");
                return PUBConstant.FAIL;
            }
            context.addDataField("state_flg", pfncStatBase.getStateFlg());
            context.addDataField(CMISConstance.CMIS_FNCSTATBASE, pfncStatBase);

            /**
             * 获取到标签样式对象(带数据的)的对象,将该标签样式对象对象放入到context中
             */
            // 从财报对象中得到现金流动表的id
            String styleId = pfncStatBase.getStatCfStyleId();
            /**
             * 从系统缓存中读取取得报表样式信息对象，得到其中的所有项目列表
             */
            FNCFactory fncFactory = (FNCFactory) context
                    .getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fcs = (FncConfStyles) fncFactory
                    .getFNCInstance(styleId);
            String tableName = fcs.getFncName();
            String fncConfTyp = fcs.getFncConfTyp();

            pfncConfStyles = fCommonComponent.findOneFncConfStyles(cusId,
                    statPrdStyle, statPrd, styleId, tableName, fncConfTyp,
                    stat_style);
            if (pfncConfStyles == null) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的标签样式对象(带数据的)的对象为空");
                return PUBConstant.FAIL;
            }
            context.addDataField("style_id", pfncConfStyles.getStyleId());
            context.addDataField("fnc_conf_data_col", pfncConfStyles
                    .getFncConfDataCol()
                    + "");
            context.addDataField("fnc_name", pfncConfStyles.getFncName());
            context
                    .addDataField("fnc_conf_typ", pfncConfStyles
                            .getFncConfTyp());
            context.addDataField(CMISConstance.CMIS_RPTSTYLE, pfncConfStyles);

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
