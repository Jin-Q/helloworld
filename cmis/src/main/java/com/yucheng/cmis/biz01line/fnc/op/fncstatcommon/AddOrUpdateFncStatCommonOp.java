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

public class AddOrUpdateFncStatCommonOp extends CMISOperation {

    public String doExecute(Context context) throws EMPException {

        String flagInfo = CMISMessage.DEFEAT;
        String fg = "";
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

            int fncConfDataColumn = Integer.parseInt((String) context.getDataValue("fnc_conf_data_col")); // 数据列数

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
            pfncConfStyles.setFncConfDataCol(fncConfDataColumn); // 数据列数

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
            FncConfStyles fcs_data = (FncConfStyles) FNCFactory
                    .getFNCInstance(styleId);
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
                            .getDataElement(CMISConstance.CMIS_RPTSTYLE + "." + fcf.getItemId());
                    FncConfDefFormat fcfdFormat = new FncConfDefFormat();

                    fcfdFormat.setItemId(itemKcol.getName());
                    fcfdFormat.setFncConfCalFrm(fcf.getFncConfCalFrm());
                    fcfdFormat.setFncConfChkFrm(fcf.getFncConfChkFrm());
                    fcfdFormat.setItemName(fcf.getItemName());

                    // Object date1Value = itemKcol.getDataValue("data1");
                    Object date1Value = null;
                    Object date2Value = null;

                    if (1 == fncConfDataColumn) {
                        date1Value = itemKcol.getDataValue("data1");
                        if (date1Value == null || date1Value .equals("")) {
                            fcfdFormat.setData1(0.0);
                        } else {
                            fcfdFormat.setData1(Double.parseDouble((String) itemKcol.getDataValue("data1")));
                        }
                    } else if (2 == fncConfDataColumn) {
                        date1Value = itemKcol.getDataValue("data1");
                        date2Value = itemKcol.getDataValue("data2");
                        if (date1Value == null || date1Value .equals("")) {
                            fcfdFormat.setData1(0.0);
                        } else {
                            fcfdFormat.setData1(Double.parseDouble((String) itemKcol.getDataValue("data1")));
                        }
                        if (date2Value == null || date2Value .equals("")) {
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
                    fcfdFormat.setFncItemEditTyp(editTyp);
                    fcfdFormat.setFncConfCalFrm(fcf.getFncConfCalFrm());
                    fcfdFormat.setFncConfChkFrm(fcf.getFncConfChkFrm());
                    arrlist.add(fcfdFormat);
                }

            }

            // 设置报表项目信息
            pfncConfStyles.setItems(arrlist);
            pfncConfStyles.setFncConfCotes(fcs_data.getFncConfCotes());
            pfncConfStyles.setFncConfDisName(fcs_data.getFncConfDisName());
            pfncConfStyles.setStyleId(fcs_data.getStyleId());
            pfncConfStyles.setFncConfTyp(fcs_data.getFncConfTyp());

            /**
             * 处理计算公式和检查公式
             */
            List arrList = null;
            List errList = null;

            /**
             * 获得客户的财务基表信息
             */
            FncStatBase tempBase = fsCommonComponent.getOneFncStatBase(pfncStatBase, connection);
            // 从缓存中获得现金流量表的信息(原来的做法)
            // FncConfStyles cf_data = (FncConfStyles)
            // FNCFactory.getFNCInstance(tempBase.getStatCfStyleId());
            // 从缓存中获得财务指标表的信息(原来的做法)
            // FncConfStyles fi_data = (FncConfStyles)
            // FNCFactory.getFNCInstance(tempBase.getStatFiStyleId());

            // 新的做法
            FncConfStyles cf_data = fsCommonComponent.getData(tempBase, this
                    .getConnection(context), "03", "fnc_stat_cfs");
            FncConfStyles fi_data = fsCommonComponent.getData(tempBase, this
                    .getConnection(context), "04", "fnc_index_rpt");

            if ("01".equals(fnc_conf_typ) || "02".equals(fnc_conf_typ)) {// 处理资产负债表或损益表或所有者权益表
                arrList = fsCommonComponent.calculateBbRptData(pfncConfStyles);

                // 检查公式
                errList = fsCommonComponent.validateRpt(pfncConfStyles);
                boolean errFlag = true;
                if (errList != null && errList.size() != 0) {
                    // if("01".equals(fnc_conf_typ)){
                    if (!"[报表已打平，保存成功！]".equals(errList.toString())) {
                        errFlag = false;
                    }
                    // }
                } else {
                    errList = null;
                }

                pfncConfStyles.setItems(arrList);
                context.addDataField("errList", errList);

                // 更新一张报表数据 或者 新增一张报表信息（资产负债表或损益表或现金流量表或财务指标表）
                /**
                 * 判断是新增 还是 修改 操作数据库
                 */
                int count = fsCommonComponent.getFncConfStyles(pfncConfStyles, pfncStatBase);
                if (count == 0) {
                    flagInfo = fsCommonComponent.addFncStat(pfncConfStyles, pfncStatBase);
                } else {
                    flagInfo = fsCommonComponent.updateFncStat(pfncConfStyles, pfncStatBase, errFlag);
                }
                String str = "";
                String tempStatFlg = statFlag;
                String tempflagInfo = flagInfo;
                int post = tempflagInfo.indexOf("|");
                if (post != -1) {
                    flagInfo = tempflagInfo.substring(0, post);
                    tempStatFlg = tempflagInfo.substring(post + 1);
                    str = tempStatFlg;
                }                
                if ("01".equals(fnc_conf_typ) || "02".equals(fnc_conf_typ)) {
                    /**
                     * 在资产负债表和损益表完成的基础上，处理现金流量表信息为暂存状态
                     */
                    /**
                     * 计算现金流量表
                     */
                    List tempList = new ArrayList();
                    String biFlg = tempStatFlg.substring(0, 2);// 获得资产，损益表的状态,是否为22
                    fg = tempStatFlg;
                    if ("22".equals(biFlg) && tempBase.getStatCfStyleId()!=null && !"".equals(tempBase.getStatCfStyleId())) {
                        tempList = fsCommonComponent.calculateRptData(cf_data, tempBase, "03");// 现金流量表
                        cf_data.setItems(tempList);
                        /**
                         * 判断是新增 还是 修改 操作数据库
                         */
                        int count1 = fsCommonComponent.getFncConfStyles(cf_data, tempBase);
                        if (count1 == 0) {
                            flagInfo = fsCommonComponent.addFncStat(cf_data, tempBase);
                        } else {
                            flagInfo = fsCommonComponent.updateFncStat(cf_data, tempBase, errFlag);
                        }
                        String tempFlag = "22" + fsCommonComponent.setStatFlag("fnc_index_rpt", statFlag, '1').substring(2);
                        str = tempFlag;
                    }
                }
                flagInfo = fsCommonComponent.updateFncStatFlg(str, this.getConnection(context), tempBase);
                int p = flagInfo.indexOf("|");
                if (p != -1) {
                    fg = flagInfo.substring(p + 1);
                }
            } else if ("03".equals(fnc_conf_typ) || "04".equals(fnc_conf_typ)) {// 处理损益表或财务指标表
                boolean errFlag = true;
                // if("03".equals(fnc_conf_typ)){
                arrList = fsCommonComponent.calculateRptData(pfncConfStyles, tempBase, fnc_conf_typ);

                // 检查公式
                errList = fsCommonComponent.validateRpt(pfncConfStyles);
                if (errList != null && errList.size() != 0) {
                    if (!"[报表已打平，保存成功！]".equals(errList.toString())) {
                        errFlag = false;
                    }else{
                    	errList = null;
                    }
                }else{
                	errList = null;
                }
                /*
                 * }else if("04".equals(fnc_conf_typ)){ arrList =
                 * fsCommonComponent.calculateRptData(pfncConfStyles, tempBase,
                 * fnc_conf_typ); }
                 */
                pfncConfStyles.setFncConfDataCol(fncConfDataColumn);
                pfncConfStyles.setItems(arrList);

                context.addDataField("errList", errList);

                // 更新一张报表数据 或者 新增一张报表信息（资产负债表或损益表或现金流量表或财务指标表）
                /**
                 * 判断是新增 还是 修改 操作数据库(原来的做法)
                 */
                int count = fsCommonComponent.getFncConfStyles(pfncConfStyles, pfncStatBase);
                if (count == 0) {
                    flagInfo = fsCommonComponent.addFncStat(pfncConfStyles, pfncStatBase);
                } else {
                    flagInfo = fsCommonComponent.updateFncStat(pfncConfStyles, pfncStatBase, errFlag);
                }

                /**
                 * 当是处理现金流表完成信息时，同时要暂存财务指标表信息
                 */
                if ("03".equals(fnc_conf_typ)) {
                    /**
                     * 处理现金流量的临时状态
                     */
                    List tempList = new ArrayList();
                    String tempStatFlg = statFlag;
                    String tempflagInfo = flagInfo;
                    int post = tempflagInfo.indexOf("|");
                    if (post != -1) {
                        flagInfo = tempflagInfo.substring(0, post);
                        tempStatFlg = tempflagInfo.substring(post + 1);
                    }
                    String bicFlg = tempStatFlg.substring(0, 3);// 获得资产，损益,现金流量表的状态，是否为222
                    if ("222".equals(bicFlg)) {
                        tempList = fsCommonComponent.calculateRptData(fi_data, tempBase, "04");// 财务指标表
                        fi_data.setItems(tempList);
                        /**
                         * 判断是新增 还是 修改 操作数据库
                         */
                        pfncStatBase.setStateFlg(tempStatFlg);
                        int count2 = fsCommonComponent.getFncConfStyles(fi_data, pfncStatBase);
                        if (count2 == 0) {
                            flagInfo = fsCommonComponent.addFncStat(fi_data,pfncStatBase);
                        } else {
                            flagInfo = fsCommonComponent.updateFncStat(fi_data,pfncStatBase, errFlag);
                        }

                        String tempFlag = "222" + fsCommonComponent.setStatFlag("fnc_index_rpt", statFlag, '1').substring(3);
                        flagInfo = fsCommonComponent.updateFncStatFlg(tempFlag, this.getConnection(context), tempBase);
                    }
                }

                /**
                 * 更新最终的报表状态 判断前4位状态是否是“2222”，是，置最后以为位“2”
                 */
                int p = flagInfo.indexOf("|");
                String strFlg = "";
                String tempFlagInfo = tempBase.getStateFlg();
                if (p != -1) {
                    strFlg = flagInfo.substring(0, p);
                    tempFlagInfo = flagInfo.substring(p + 1);
                }
                fg = tempFlagInfo;

            } else if ("05".equals(fnc_conf_typ)) {
                arrList = fsCommonComponent.calculateBbRptData(pfncConfStyles);

                // 检查公式
                errList = fsCommonComponent.validateRpt(pfncConfStyles);
                boolean errFlag = true;
                if (errList != null && errList.size() != 0) {
                    // if("01".equals(fnc_conf_typ)){
                    if (!"[报表已打平，保存成功！]".equals(errList.toString())) {
                        errFlag = false;
                    }
                    // }
                } else {
                    errList = null;
                }

                pfncConfStyles.setItems(arrList);
                context.addDataField("errList", errList);

                // 更新一张报表数据 或者 新增一张报表信息（资产负债表或损益表或现金流量表或财务指标表）
                /**
                 * 判断是新增 还是 修改 操作数据库
                 */
                int count = fsCommonComponent.getFncConfStyles(pfncConfStyles, pfncStatBase);
                if (count == 0) {
                    flagInfo = fsCommonComponent.addFncStat(pfncConfStyles, pfncStatBase);
                } else {
                    flagInfo = fsCommonComponent.updateFncStat(pfncConfStyles, pfncStatBase, errFlag);
                }
                String str = "";
                String tempStatFlg = statFlag;
                String tempflagInfo = flagInfo;
                int post = tempflagInfo.indexOf("|");
                if (post != -1) {
                    flagInfo = tempflagInfo.substring(0, post);
                    tempStatFlg = tempflagInfo.substring(post + 1);
                    str = tempStatFlg;
                }
            }else if("06".equals(fnc_conf_typ)){
            	  //boolean errFlag = true;
            	 arrList = fsCommonComponent.calculateBbRptData(pfncConfStyles);

                 // 检查公式
                 errList = fsCommonComponent.validateRpt(pfncConfStyles);
                 boolean errFlag = true;
                 if (errList != null && errList.size() != 0) {
                     // if("01".equals(fnc_conf_typ)){
                     if (!"[报表已打平，保存成功！]".equals(errList.toString())) {
                         errFlag = false;
                     }
                     // }
                 } else {
                     errList = null;
                 }
            	
            	  int count6 = fsCommonComponent.getFncConfStyles(pfncConfStyles, pfncStatBase);
            	  if(count6 == 0){ 
            		  flagInfo = fsCommonComponent.addFncStat(pfncConfStyles, pfncStatBase);
            		  
            	  }else{ 
            		  flagInfo = fsCommonComponent.updateFncStat(pfncConfStyles,pfncStatBase,true); 
            	  }
            	  
                      /**
                       * 处理财务简表的临时状态
                       */
                      List tempList = new ArrayList();
                      String tempStatFlg = statFlag;
                      String tempflagInfo = flagInfo;
                      String str = "";
                      int post = tempflagInfo.indexOf("|");
                      if (post != -1) {
                          flagInfo = tempflagInfo.substring(0, post);
                          tempStatFlg = tempflagInfo.substring(post + 1);
                          str = tempStatFlg;
                      }
                      String bicFlg = tempStatFlg.substring(0, 3)+tempStatFlg.substring(5, 6);// 获得资产，损益,现金流量表的状态，是否为222
                      if ("2222".equals(bicFlg)) {
                          tempList = fsCommonComponent.calculateRptData(fi_data, tempBase, "04");// 财务指标表
                          fi_data.setItems(tempList);
                          /**
                           * 判断是新增 还是 修改 操作数据库
                           */
                          pfncStatBase.setStateFlg(tempStatFlg);
                          int count2 = fsCommonComponent.getFncConfStyles(fi_data, pfncStatBase);
                          if (count2 == 0) {
                              flagInfo = fsCommonComponent.addFncStat(fi_data, pfncStatBase);
                          } else {
                              flagInfo = fsCommonComponent.updateFncStat(fi_data, pfncStatBase, errFlag);
                          }

                          String tempFlag = "222"
                                  + fsCommonComponent.setStatFlag("fnc_index_rpt", statFlag, '1')
                                          .substring(3,5)+'2'+fsCommonComponent.setStatFlag("fnc_index_rpt", statFlag, '1').substring(6);
                          flagInfo = fsCommonComponent.updateFncStatFlg(tempFlag, this.getConnection(context), tempBase);
                          str = tempFlag;
                      }
                      
                  flagInfo = fsCommonComponent.updateFncStatFlg(str, this.getConnection(context), tempBase);
                  int p = flagInfo.indexOf("|");
                  if (p != -1) {
                      fg = flagInfo.substring(p + 1);
                  }
            	 // System.err.println();
            }
             

            /**
             * 更新整套报表的状态位，即最后一位标志位 查看前面8位是不是包含0，1，如果不包含就把最后一位设为2 否则，设为0 0 为存储 1
             * 暂存 2已保存
             */
            String t = fg.substring(0, 8);
            int p0 = t.indexOf("0");
            int p1 = t.indexOf("1");
            if (p0 == -1 && p1 == -1) {
                flagInfo = fsCommonComponent.updateFncStatFlg(t + "2", this.getConnection(context), tempBase);
            } else {
                flagInfo = fsCommonComponent.updateFncStatFlg(t + "0", this.getConnection(context), tempBase);
            }
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
            context.setDataValue("fnc_conf_data_col", (String) context.getDataValue("fnc_conf_data_col"));
            context.setDataValue("state_flg", statFlag);
            context.addDataField("editFlag", "noedit");
        } catch (CMISException e) {
            e.printStackTrace();
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
            String message = CMISMessageManager.getMessage(e);
            CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR,
                    0, message);
            throw e;
        } catch (Exception e) {
        	EMPLog.log(this.getClass().getName(),EMPLog.ERROR, 0, "", e);
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
                if (data == null || data .equals("")) {
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
