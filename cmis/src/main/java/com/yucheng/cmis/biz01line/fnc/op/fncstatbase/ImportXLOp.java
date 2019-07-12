package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.dao.ExportXLDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

/**
 * 特别说明：这个类暂时没有用到
 * 
 * @Classname ImportXLOp.java
 *@Version 1.0
 *@Since 1.0 2008-10-20 下午04:45:24
 *@Copyright yuchengtech
 *@Author Yu
 *@Description：把excel中的数据导入到数据库中
 *@Lastmodified
 *@Author
 */
public class ImportXLOp extends CMISOperation {

    /**
     * 客户代码
     */
    private final String pk1 = "FncStatBase.cus_id1";
    /**
     * 报表周期类型 月报 1 季报 2 半年报 3 年报 4
     */
    private final String pk2 = "FncStatBase.stat_prd_style1";

    /**
     * 报表期间 例如200804
     */
    private final String pk3 = "FncStatBase.stat_prd1";

    public String doExecute(Context context) throws EMPException {

        Connection connection = null;
        try {
            connection = this.getConnection(context);
            // KeyedCollection koll =
            // (KeyedCollection)context.getDataElement("FncStatBase");
            String flagInfo = CMISMessage.DEFEAT;
            // 取联合主键值
            String cusId = (String) context.getDataValue(pk1); // 客户代码
            String statPrdStyle = (String) context.getDataValue(pk2); // 报表周期类型
            String statPrd = get_6charsTime((String) context.getDataValue(pk3)); // 报表期间

            /*
             * // 取得报表样式的ID --- 已经废弃 by xuyp 注释 String statBsStyleId = (String)
             * context .getDataValue("stat_bs_style_id");// 类似
             * SZ01,SX03,SC01,SL03 String statPlStyleId = (String) context
             * .getDataValue("stat_pl_style_id"); String statCfStyleId =
             * (String) context .getDataValue("stat_cf_style_id"); String
             * statFiStyleId = (String) context
             * .getDataValue("stat_fi_style_id"); String statSoeStyleId =
             * (String) context .getDataValue("stat_soe_style_id"); String
             * statSlStyleId = (String) context
             * .getDataValue("stat_sl_style_id");
             */

            // EXCEL文件指定路径pFile
            // String XLSFile = (String) context.getDataValue("address");
            // String XLSFile = (String)
            // context.getDataValue("FncStatBase.pFile");
            String tempFilePath = (String) context.getDataValue("pFile");
            int post1 = tempFilePath.indexOf("tmpFileName=");
            int post2 = tempFilePath.indexOf(" srcFileName=");
            String tempFileName = tempFilePath.substring(post1 + 12, post2);

            // 编码
            if (tempFileName != null) {
                try {
                    tempFileName = new String(tempFileName
                            .getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            /*
             * 创建业务处理组件，并初始化
             */
            FncStatCommonComponent fsCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCSTATCOMMON, context, connection);

            /********************************************/
            FncConfStyles pfncConfStyles = new FncConfStyles(); // 报表样式信息(包含具体项目信息列表)
            FncStatBase pfncStatBase = new FncStatBase(); // 公司客户报表基本信息
            /********************************************/

            pfncStatBase.setCusId(cusId); // 客户ID
            pfncStatBase.setStatPrd(statPrd); // 报表期间
            pfncStatBase.setStatPrdStyle(statPrdStyle); // 报表类型
            pfncStatBase.setStateFlg("999999999"); // 状态

            CellVO cvo;
            // 读取文件
            ExcelVO evo = null;
            try {
                evo = ExcelTreat.readExcel(tempFileName);
                SheetVO[] sheets = evo.sheets;
                SheetVO sheet = null;

                for (int i = 0; i < sheets.length; i++) {

                    ExportXLDao exportXLDao = new ExportXLDao();
                    sheet = sheets[i];
                    // 现在是把sheetname作为报表的类型, 也就是statPrdStyle
                    String _styleId = sheet.sheetname;
                    /*
                     * System.out.println("读取sheet :" + sheet.sheetname);
                     * System.out.println(sheet.sheetname + "的第一栏有" +
                     * exportXLDao.queryCount(_styleId, 1, connection) + "项");
                     * System.out.println(sheet.sheetname + "的第二栏有" +
                     * exportXLDao.queryCount(_styleId, 2, connection) + "项");
                     */

                    /**
                     * 取得报表样式 从FNC_CONF_STYLES里面取得 关键是通过这次取得
                     * 知道要把从EXCEL里面读取的信息放到哪张物理报表去 还有就是取得item的信息
                     */
                    FncConfStyles fcs_fromDb = (FncConfStyles) FNCFactory
                            .getFNCInstance(_styleId);
                    if (fcs_fromDb == null) {
                        throw new CMISException(CMISMessage.MESSAGEDEFAULT,
                                "报表样式取不到");
                    }
                    // 得到样式表中的 数据列数 如果是2列的话 那么就是期初值和期末值
                    int fncConfDataColumns_defInDB = fcs_fromDb
                            .getFncConfDataCol();

                    // 得到样式表中的 显示名称
                    String fncConfDisName = fcs_fromDb.getFncConfDisName();

                    // 得到固定样式中所有的项目item的信息，这些具体的信息来自FNC_CONF_DEF_FMT
                    List itemslist_defInDB = fcs_fromDb.getItems();
                    // 创建项目信息对象列表
                    ArrayList<FncConfDefFormat> arrlist = new ArrayList<FncConfDefFormat>();

                    CellVO[][] cells;
                    cells = sheet.cells;
                    int row = sheet.rownum;
                    int col = sheet.colnum;
                    for (int j = 0; j < itemslist_defInDB.size(); j++) {

                        /**
                         * 而这个类是用excel文件里面的某个item的信息封装起来的，一会要用来做"数据库写入"的
                         */
                        FncConfDefFormat fncConfDefFormat = new FncConfDefFormat();

                        /**
                         * 这个是用来取得数据库里面对该item的定义用的
                         */
                        FncConfDefFormat fcf_DefInDB = (FncConfDefFormat) itemslist_defInDB
                                .get(j);

                        //
                        String editTyp = fcf_DefInDB.getFncItemEditTyp();

                        // 格式表中的栏位 , 来自数据库的定义
                        int fncConfCote_defInDatabase = fcf_DefInDB
                                .getFncConfCotes();
                        /*
                         * System.out.println("数据库里面定义第" + j + "项item在第" +
                         * fncConfCote_defInDatabase + "栏");
                         */
                        if (3 != Integer.parseInt(editTyp)) {// 如果不是标题项
                            fncConfDefFormat.setFncConfCalFrm(fcf_DefInDB
                                    .getFncConfCalFrm());
                            fncConfDefFormat.setFncConfChkFrm(fcf_DefInDB
                                    .getFncConfChkFrm());
                            fncConfDefFormat.setItemId(fcf_DefInDB.getItemId());
                            fncConfDefFormat.setItemName(fcf_DefInDB
                                    .getItemName());

                            // 设置editType
                            fncConfDefFormat.setFncItemEditTyp(editTyp);
                            int first_lan_count = exportXLDao.queryCount(
                                    _styleId, 1, connection);

                            if (1 == fncConfCote_defInDatabase) {// 如果该item在第一栏
                                if (1 == fncConfDataColumns_defInDB) {
                                    String cellValue = (String) cells[j + 4][2].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                    } else {
                                        /*
                                         * System.out .println(cells[j +
                                         * 4][0].cellvalue + "  " + cells[j +
                                         * 4][1].cellvalue + "(行次)  " + cells[j
                                         * + 4][2].cellvalue);
                                         */
                                        fncConfDefFormat.setData1(Double
                                                .parseDouble(cellValue));
                                    }
                                } else if (2 == fncConfDataColumns_defInDB) {

                                    /***************** 设置期初值 **********************************/
                                    String cellValue = (String) cells[j + 4][2].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                    } else {

                                        /*
                                         * System.out.print(cells[j + 4][0] +
                                         * " " + cells[j + 4][1] + "(行次) " +
                                         * cells[j + 4][2] + "(期初) ");
                                         */
                                        fncConfDefFormat.setData1(Double
                                                .parseDouble(cellValue));
                                    }
                                    /***************** 设置期初值-结束 **********************************/
                                    /***************** 设置期末值 **********************************/
                                    String cellValue2 = (String) cells[j + 4][3].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                    } else {
                                        /*
                                         * System.out.print(" " + cells[j +
                                         * 4][3] + "(期末) ");
                                         * System.out.println();
                                         */
                                        fncConfDefFormat.setData2(Double
                                                .parseDouble(cellValue2));
                                    }
                                    /***************** 设置期初值-结束 *********************************/
                                }
                            } else if (2 == fncConfCote_defInDatabase) {// 如果该item在第二栏
                                if (1 == fncConfDataColumns_defInDB) {

                                    // 目前这里的情况暂时不会出现
                                    String cellValue = (String) cells[j + 4
                                            - first_lan_count][6].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                        /*
                                         * System.out .println("here is right+"
                                         * + cells[j + 4 -
                                         * first_lan_count][6].cellvalue);
                                         */
                                        continue;
                                    }
                                    /*
                                     * System.out.println(cells[j +
                                     * 4][0].cellvalue + "  " + cells[j +
                                     * 4][1].cellvalue + "(行次)  " + cells[j +
                                     * 5][2].cellvalue);
                                     */

                                    fncConfDefFormat.setData1(Double
                                            .parseDouble(cellValue));
                                } else if (2 == fncConfDataColumns_defInDB) {
                                    /******************* 设置期初值 ****************************************/
                                    String cellValue = (String) cells[j + 4
                                            - first_lan_count][6].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                    } else {

                                        /*
                                         * System.out .print(cells[j + 4 -
                                         * first_lan_count][4].cellvalue + " " +
                                         * cells[j + 4 -
                                         * first_lan_count][5].cellvalue +
                                         * "(行次)  " + cells[j + 4 -
                                         * first_lan_count][6].cellvalue);
                                         */
                                        fncConfDefFormat.setData1(Double
                                                .parseDouble(cellValue));
                                    }
                                    /******************* 设置"期初值"-结束 *****************************************/
                                    /******************* 设置"期末值" *****************************************/
                                    String cellValue2 = (String) cells[j + 4
                                            - first_lan_count][7].cellvalue;
                                    if (cellValue.trim() == null
                                            || cellValue.trim().length() == 0) {
                                        /*
                                         * System.out .println("there is right+"
                                         * + cells[j + 4 -
                                         * first_lan_count][7].cellvalue);
                                         * System.out.println();
                                         */
                                    } else {
                                        /*
                                         * System.out .print(" " + cells[j + 4 -
                                         * first_lan_count][7].cellvalue);
                                         * System.out.println();
                                         */
                                        fncConfDefFormat.setData2(Double
                                                .parseDouble(cellValue2));
                                    }
                                    /******************** 设置"期末值"-结束 ****************************************/
                                }
                            } else {
                                throw new CMISException(
                                        CMISMessage.MESSAGEDEFAULT,
                                        "报表格式表中栏位必须为1或者2");
                            }
                            arrlist.add(fncConfDefFormat);
                        } else {
                            // 如果是标题项的话
                            fncConfDefFormat.setItemId(fcf_DefInDB.getItemId());
                            fncConfDefFormat.setItemName(fcf_DefInDB
                                    .getItemName());
                            // 设置editType
                            fncConfDefFormat.setFncItemEditTyp(editTyp);

                            arrlist.add(fncConfDefFormat);

                        }

                    }
                    // 设置报表项目信息
                    pfncConfStyles.setItems(arrlist);
                    pfncConfStyles
                            .setFncConfDataCol(fncConfDataColumns_defInDB);
                    pfncConfStyles
                            .setFncConfCotes(fcs_fromDb.getFncConfCotes());
                    pfncConfStyles.setFncConfDisName(fcs_fromDb
                            .getFncConfDisName());
                    pfncConfStyles.setFncName(fcs_fromDb.getFncName());
                    pfncConfStyles.setStyleId(fcs_fromDb.getStyleId());
                    pfncConfStyles.setFncConfTyp(fcs_fromDb.getFncConfTyp());
                    flagInfo = fsCommonComponent.addFncStat(pfncConfStyles,
                            pfncStatBase);

                    /*
                     * System.out.println("pfncStatBase.getStateFlg():"+pfncStatBase
                     * .getStateFlg()); System.out.println("读取sheet :" +
                     * sheet.sheetname+"完毕");
                     */
                }
                context.addDataField("cus_id", cusId);
            } catch (Exception e) {
                throw e;
            }
        } catch (EMPException ee) {
            throw ee;
        } catch (Exception e) {
            throw new EMPException(e);
        } finally {
            if (connection != null)
                this.releaseConnection(context, connection);
        }

        return null;
    }

    /**
     * 如果碰到 20084 的情况 则将其变成 200804
     * 
     * @param param
     * @return
     */
    private String get_6charsTime(String param) {
        String result = param;

        if (6 > param.length()) {
            result = param.substring(0, 4) + "0" + param.substring(4);
        }

        return result;
    }

}
