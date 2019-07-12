package com.yucheng.cmis.biz01line.fnc.op.fncstatcommon;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
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
 *@Classname TempFncStatCommon.java
 *@Version 1.0
 *@Since 1.0 Oct 15, 2008 11:02:53 AM
 *@Copyright yuchengtech
 *@Author Eric
 *@Description：
 *@Lastmodified
 *@Author
 */
public class TempFncStatCommonOp extends CMISOperation {

    public String doExecute(Context context) throws EMPException {
        // TODO Auto-generated method stub
        String flagInfo = CMISMessage.DEFEAT;
        // System.out.println(context.toString());
        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String cusId = (String) context.getDataValue("cus_id"); // 客户ID
            String statPrd = (String) context.getDataValue("stat_prd"); // 报表期间
            String statPrdStyle = (String) context.getDataValue("stat_prd_style"); // 报表类型
            String tableName = (String) context.getDataValue("fnc_name"); // 表名
            String styleId = (String) context.getDataValue("style_id"); // 报表样式编号
            String statFlag = (String) context.getDataValue("state_flg"); // 状态
            String stat_style = (String) context.getDataValue("stat_style"); // 报表口径
            String fnc_type = (String) context.getDataValue("fnc_type"); // 报表类型

            String fnc_conf_typ = (String) context.getDataValue("fnc_conf_typ"); // 报表类型

            int fncConfDataColumn = Integer.parseInt((String) context.getDataValue("fnc_conf_data_col")); // 期数数

            /*
             * 创建业务处理组件，并初始化
             */

            FncStatCommonComponent fsCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATCOMMON, context, connection);

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
            pfncStatBase.setStatStyle(stat_style); // 报表口径
            pfncStatBase.setFncType(fnc_type); // 财报类型
            /*
             * 从系统缓存中读取取得报表样式信息对象，得到其中的所有项目列表
             */
            // FNCFactory fncFactory = (FNCFactory)
            // context.getService(CMISConstance.ATTR_RPTSERVICE);
            FncConfStyles fcs_data = (FncConfStyles) FNCFactory.getFNCInstance(styleId);
            List list = fcs_data.getItems();

            // 创建项目信息对象列表
            ArrayList<FncConfDefFormat> arrlist = new ArrayList<FncConfDefFormat>();

            /*
             * 根据缓存中得到的报表样式信息对象，取到页面穿过来的所有项目信息数据。 并放入项目信息对象列表中
             */
            for (int i = 0; i < list.size(); i++) {

                FncConfDefFormat fcf = (FncConfDefFormat) list.get(i);
                String editTyp = fcf.getFncItemEditTyp();

                if (3 != Integer.parseInt(editTyp)) {
                    KeyedCollection itemKcol = (KeyedCollection) context
                            .getDataElement(CMISConstance.CMIS_RPTSTYLE + "."+ fcf.getItemId());
                    FncConfDefFormat fcfdFormat = new FncConfDefFormat();

                    fcfdFormat.setItemId(itemKcol.getName());
                    // Object date1Value = itemKcol.getDataValue("data1");
                    String date1Value = null;
                    String date2Value = null;

                    if (1 == fncConfDataColumn) {
                        date1Value = (String)itemKcol.getDataValue("data1");
                        if (date1Value == null || date1Value.trim().equals("")) {
                            fcfdFormat.setData1(0.0);
                        } else {
                            fcfdFormat.setData1(Double.parseDouble((String) itemKcol.getDataValue("data1")));
                        }
                    } else if (2 == fncConfDataColumn) {
                        date1Value = (String)itemKcol.getDataValue("data1");
                        if (date1Value == null || date1Value.trim().equals("")) {
                            fcfdFormat.setData1(0.0);
                        } else {
                            fcfdFormat.setData1(Double.parseDouble((String) itemKcol.getDataValue("data1")));
                        }
                        date2Value = (String)itemKcol.getDataValue("data2");
                        if (date2Value == null || date2Value.trim().equals("")) {
                            fcfdFormat.setData2(0.0);
                        } else {
                            fcfdFormat.setData2(Double.parseDouble((String) itemKcol.getDataValue("data2")));
                        }

                    } else if (8 == fncConfDataColumn) {// 处理所有者权益表数据
                        double[] dataA = this.getData(itemKcol, "A");
                        double[] dataB = this.getData(itemKcol, "B");
                        fcfdFormat.setDataA(dataA);
                        fcfdFormat.setDataB(dataB);
                    } else {
                        throw new CMISException(CMISMessage.MESSAGEDEFAULT, "报表样式表中期数数必须为1或者2");
                    }

                    fcfdFormat.setFncConfCalFrm(fcf.getFncConfCalFrm());
                    fcfdFormat.setFncConfChkFrm(fcf.getFncConfChkFrm());
                    fcfdFormat.setFncItemEditTyp(editTyp);
                    arrlist.add(fcfdFormat);
                }

            }

            // 设置报表项目信息
            pfncConfStyles.setItems(arrlist);
            pfncConfStyles.setFncConfCotes(fcs_data.getFncConfCotes());
            pfncConfStyles.setFncConfDisName(fcs_data.getFncConfDisName());
            pfncConfStyles.setStyleId(fcs_data.getStyleId());
            pfncConfStyles.setFncConfTyp(fcs_data.getFncConfTyp());

            // 新增一张报表数据(原来的)
            // flagInfo = fsCommonComponent.TempAddFncStat(pfncConfStyles,
            // pfncStatBase);

            // ////////////////////////////计算开始////////////////////////////////////
            /**
             * 处理计算公式
             */
            List arrList = null;
            List errList = null;

            FncStatBase tempBase = fsCommonComponent.getOneFncStatBase(pfncStatBase, connection);

            if ("01".equals(fnc_conf_typ) || "02".equals(fnc_conf_typ)
                    || "05".equals(fnc_conf_typ) || "06".equals(fnc_conf_typ)) {// 处理资产负债表或损益表
                arrList = fsCommonComponent.calculateBbRptData(pfncConfStyles);
            } else {// 处理损益表或财务指标表
                if ("03".equals(fnc_conf_typ)) {
                    arrList = fsCommonComponent.calculateRptData(pfncConfStyles, tempBase, fnc_conf_typ);
                } else if ("04".equals(fnc_conf_typ)) {
                    arrList = fsCommonComponent.calculateRptData(pfncConfStyles, tempBase, fnc_conf_typ);
                }
            }

            pfncConfStyles.setItems(arrList);
            flagInfo = fsCommonComponent.TempAddFncStat(pfncConfStyles, pfncStatBase);
            // ///////////////////////////计算结束//////////////////////////////////////////

            /*
             * 设置返回参数
             */
            /**
             * 获取到财报的对象,将财报对象放入到context中
             */
            FncStatBase pp = fsCommonComponent.findOneFncStatBase(pfncStatBase);
            // FncConfStyles fc = fncStatBsComponent.findOneFncConfStyles(cusId,
            // statPrdStyle, statPrd, styleId);

            context.clear();

            context.removeDataElement(CMISConstance.CMIS_RPTSTYLE);
            context.addDataField(CMISConstance.CMIS_RPTSTYLE, pfncConfStyles);
            context.addDataField(CMISConstance.CMIS_FNCSTATBASE, pp);

            context.setDataValue("cus_id", cusId);
            context.setDataValue("stat_prd", statPrd);
            context.setDataValue("stat_prd_style", statPrdStyle);
            context.setDataValue("fnc_name", tableName);
            context.setDataValue("style_id", styleId);
            context.setDataValue("fnc_conf_data_col", (String)context.getDataValue("fnc_conf_data_col"));
            context.setDataValue("state_flg", statFlag);
            context.addDataField("editFlag", "noedit");
            context.setDataValue("stat_style", stat_style);
            context.put("fnc_type", fnc_type);
        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR,
                    0, message);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }

        return flagInfo;
    }

    /**
     * 处理所有者权益表数据
     * 
     * @param itemKcol
     * @return Object[] 当str为A是表示获取期初数据的数组 当str为B是表示获取期末数据的数组
     */
    public double[] getData(KeyedCollection itemKcol, String str) {
        double[] dataValue = new double[8];
        for (int i = 0; i < dataValue.length; i++) {
            try {
                Object data = itemKcol.getDataValue("data" + str + "[" + i
                        + "]");
                if (data == null || data.equals("")) {
                    dataValue[i] = 0.0;
                } else {
                    dataValue[i] = Double.parseDouble((String) data);
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        return dataValue;
    }

}
