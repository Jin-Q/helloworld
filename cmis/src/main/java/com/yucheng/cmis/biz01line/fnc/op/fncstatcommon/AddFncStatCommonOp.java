package com.yucheng.cmis.biz01line.fnc.op.fncstatcommon;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncRptCheckComponent;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;

/**
 *@Classname FncStatCommon.java
 *@Version 1.0
 *@Since 1.0 Oct 14, 2008 10:47:57 AM
 *@Copyright yuchengtech
 *@Author Eric
 *@Description：通用的报表新增操作的后台处理程序
 *@Lastmodified
 *@Author
 */
public class AddFncStatCommonOp extends CMISOperation {

    public String doExecute(Context context) throws EMPException {

        String flagInfo = CMISMessage.DEFEAT;
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String cusId = (String) context.getDataValue("cus_id"); // 客户ID
            String statPrd = (String) context.getDataValue("stat_prd"); // 报表期间
            String statPrdStyle = (String) context
                    .getDataValue("stat_prd_style"); // 报表类型
            String tableName = (String) context.getDataValue("fnc_name"); // 表名
            String styleId = (String) context.getDataValue("style_id"); // 报表样式编号

            int fncConfDataColumn = Integer.parseInt((String) context
                    .getDataValue("fnc_conf_data_col")); // 期数数
            String statFlag = (String) context.getDataValue("state_flg"); // 状态
            /*
             * 创建业务处理组件，并初始化
             */

            FncStatCommonComponent fsCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCSTATCOMMON, context, connection);

            // TODO Auto-generated method stub
            FncConfStyles pfncConfStyles = new FncConfStyles(); // 报表样式信息(包含具体项目信息列表)
            FncStatBase pfncStatBase = new FncStatBase(); // 公司客户报表基本信息
            /*
             * 初始化报表样式信息对象的成员变量
             */
            pfncConfStyles.setFncName(tableName); // 表名
            pfncConfStyles.setFncConfDataCol(fncConfDataColumn); // 期数

            /*
             * 初始化公司客户报表基本信息对象的成员变量
             */
            pfncStatBase.setCusId(cusId); // 客户ID
            pfncStatBase.setStatPrd(statPrd); // 报表期间
            pfncStatBase.setStatPrdStyle(statPrdStyle); // 报表类型
            pfncStatBase.setStateFlg(statFlag); // 状态
            /*
             * 从系统缓存中读取取得报表样式信息对象，得到其中的所有项目列表
             */
            FNCFactory fncFactory = (FNCFactory) context
                    .getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fcs_data = (FncConfStyles) fncFactory
                    .getFNCInstance(styleId);
            List list = fcs_data.getItems();

            // 创建项目信息对象列表
            ArrayList<FncConfDefFormat> arrlist = new ArrayList<FncConfDefFormat>();

            /*
             * 根据缓存中得到的报表样式信息对象，取到页面穿过来的所有项目信息数据，存入项目信息对象中并初始化其 项目ID 和计算，校验公式。
             * 并放入项目信息对象列表中
             */
            for (int i = 0; i < list.size(); i++) {

                FncConfDefFormat fcf = (FncConfDefFormat) list.get(i);
                String editTyp = fcf.getFncItemEditTyp();

                if (3 != Integer.parseInt(editTyp)) {
                    KeyedCollection itemKcol = (KeyedCollection) context
                            .getDataElement(CMISConstance.CMIS_RPTSTYLE + "."
                                    + fcf.getItemId());
                    FncConfDefFormat fcfdFormat = new FncConfDefFormat();

                    fcfdFormat.setItemId(itemKcol.getName());

                    fcfdFormat.setFncConfCalFrm(fcf.getFncConfCalFrm());
                    fcfdFormat.setFncConfChkFrm(fcf.getFncConfChkFrm());
                    fcfdFormat.setItemName(fcf.getItemName());

                    if (1 == fncConfDataColumn) {
                        fcfdFormat.setData1(Double
                                .parseDouble((String) itemKcol
                                        .getDataValue("data1")));
                    } else if (2 == fncConfDataColumn) {
                        fcfdFormat.setData1(Double
                                .parseDouble((String) itemKcol
                                        .getDataValue("data1")));
                        fcfdFormat.setData2(Double
                                .parseDouble((String) itemKcol
                                        .getDataValue("data2")));
                    } else {
                        throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                                "报表样式表中期数数必须为1或者2");
                    }
                    arrlist.add(fcfdFormat);
                }

            }

            // 设置报表项目信息
            pfncConfStyles.setItems(arrlist);
            pfncConfStyles.setFncConfCotes(fcs_data.getFncConfCotes());
            pfncConfStyles.setFncConfDisName(fcs_data.getFncConfDisName());
            pfncConfStyles.setStyleId(fcs_data.getStyleId());
            pfncConfStyles.setFncConfTyp(fcs_data.getFncConfTyp());

            System.out.println("table name is:" + pfncConfStyles.getFncName());

            /*
             * 校验、计算报表数据
             */
            FncRptCheckComponent rptCheckComponent = new FncRptCheckComponent(
                    pfncConfStyles, pfncStatBase);
            rptCheckComponent.setContext(context);

            try {
                rptCheckComponent.check();
                pfncConfStyles = rptCheckComponent.autoCalcRpt();
            } catch (Exception e) {
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e
                        .getMessage()
                        + e.toString());
                throw new CMISException("", e.getMessage());
            }

            System.out.println("table name after is:"
                    + pfncConfStyles.getFncName());
            // 新增一张报表数据
            flagInfo = fsCommonComponent.addFncStat(pfncConfStyles,
                    pfncStatBase);

            /*
             * 设置返回参数
             */

            /**
             * 获取到财报的对象,将财报对象放入到context中
             */
            FncStatBase pp = fsCommonComponent.findOneFncStatBase(pfncStatBase);
            // FncConfStyles fc = fncStatBsComponent.findOneFncConfStyles(cusId,
            // statPrdStyle, statPrd, styleId);

            context.removeDataElement(CMISConstance.CMIS_RPTSTYLE);
            context.addDataField(CMISConstance.CMIS_RPTSTYLE, pfncConfStyles);
            context.addDataField(CMISConstance.CMIS_FNCSTATBASE, pp);

            context.setDataValue("cus_id", cusId);
            context.setDataValue("stat_prd", statPrd);
            context.setDataValue("stat_prd_style", statPrdStyle);
            context.setDataValue("fnc_name", tableName);
            context.setDataValue("style_id", styleId);
            context.setDataValue("fnc_conf_data_col", (String) context
                    .getDataValue("fnc_conf_data_col"));
            context.setDataValue("state_flg", statFlag);

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
