package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.dao.ExportXLDao;
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
 * @Classname ExportXLOp.java
 *@Version 1.0
 *@Since 1.0 2008-10-20 下午04:44:40
 *@Copyright yuchengtech
 *@Author Yu
 *@Description：读取数据表内容，导出到Excel中
 *@Lastmodified
 *@Author
 */
public class ExportXLOp extends CMISOperation {
    // 得到联合主键
    private final String pk1 = "cus_id";

    private final String pk2 = "stat_prd_style";

    private final String pk3 = "stat_prd";

    private final String pk4 = "stat_style";// 报表口径

    public String doExecute(Context context) throws EMPException {

        Connection connection = null;
        try {
            connection = this.getConnection(context);
            String flagInfo = CMISMessage.DEFEAT;
            // 取联合主键值
            String cusId = (String) context.getDataValue(pk1); // pk1
            String statPrdStyle = (String) context.getDataValue(pk2); // pk2
            String statPrd = get_6charsTime((String) context.getDataValue(pk3)); // pk
            String statStyle = get_6charsTime((String) context
                    .getDataValue(pk4)); // pk
            // 取得报表样式的ID
            String statBsStyleId = (String) context
                    .getDataValue("stat_bs_style_id");
            String statPlStyleId = (String) context
                    .getDataValue("stat_pl_style_id");
            String statCfStyleId = (String) context
                    .getDataValue("stat_cf_style_id");
            String statFiStyleId = (String) context
                    .getDataValue("stat_fi_style_id");
            String statSoeStyleId = (String) context
                    .getDataValue("stat_soe_style_id");
            String statSlStyleId = (String) context
                    .getDataValue("stat_sl_style_id");

            String statEditUsr = (String) context.getDataValue("currentUserId");
            String regOrgId = (String) context.getDataValue("organNo");

            List<String> listId = new ArrayList<String>();// 样式列表

            // 资产样式编号
            if (statBsStyleId != null || statBsStyleId.length() != 0) {
                listId.add(statBsStyleId);
            }
            // 损益表编号
            if (statPlStyleId != null || statPlStyleId.length() != 0) {
                listId.add(statPlStyleId);
            }
            // 现金流量表编号
            if (statCfStyleId != null || statCfStyleId.length() != 0) {
                listId.add(statCfStyleId);
            }
            // 财务指标表编号
            if (statFiStyleId != null || statFiStyleId.length() != 0) {
                listId.add(statFiStyleId);
            }
            // 财务简表编号
            /*
             * if (statSlStyleId != null || statSlStyleId.length() != 0) {
             * listId.add(statSlStyleId); } //所有者权益变动表编号 if (statSoeStyleId !=
             * null || statSoeStyleId.length() != 0) {
             * listId.add(statSoeStyleId); }
             */
            if (listId.size() == 0) {
                EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
                        "得到的报表类型为空");
                return PUBConstant.FAIL;
            }

            // 构件业务处理类
            FncStatCommonComponent fCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(
                            PUBConstant.FNCSTATCOMMON, context, connection);
            // fCommonComponent.setContext(context);
            FncConfStyles pfncConfStyles = null;

            // 获取sheets数组
            SheetVO[] sheets = new SheetVO[listId.size()];
            SheetVO sheet;

            for (int iId = 0; iId < listId.size(); iId++) {
                String styleId = (String) listId.get(iId);

                // 从数据库中读取样式 得到其中的所有项目列表
                FNCFactory fncFactory = (FNCFactory) context
                        .getService(CMISConstance.ATTR_RPTSERVICE);
                FncConfStyles fcs_fromDb = (FncConfStyles) fncFactory
                        .getFNCInstance(styleId);
                // 得到报表名称
                String tableName = fcs_fromDb.getFncName();
                String disName = fcs_fromDb.getFncConfDisName();
                // 得到报表的类型
                String fncConfTyp = fcs_fromDb.getFncConfTyp();

                // 从数据库中取得一种报表的全部数据
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                		"+++++++++++anakinTest cusId:" + cusId
                        + " statPrdStyle" + statPrdStyle + " statPrd:"
                        + statPrd + " styleId:" + styleId + " tableName:"
                        + tableName + " fncConfTyp:" + fncConfTyp);
                pfncConfStyles = fCommonComponent.findOneFncConfStyles(cusId,
                        statPrdStyle, statPrd, styleId, tableName, fncConfTyp,
                        statStyle);
                // 得到报表所属种类
                sheet = new SheetVO();
                CellVO[][] cells = null;
                CellVO cvo;
                List itemList = fcs_fromDb.getItems();

                int row = 0;
                int clo = 0;
                ExportXLDao exportXLDao = new ExportXLDao();
                int counts = 1;
                /**
                 * 根据不同的报表种类进行不同的设置 (01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表
                 * 06财务简表)
                 */
                if (fncConfTyp.equals("01")) {
                    // 资产负债表
                    int firstLAN_Count = exportXLDao.queryCount(styleId, 1,
                            connection);
                    int seconfLAN_Count = exportXLDao.queryCount(styleId, 2,
                            connection);
                    if (firstLAN_Count > seconfLAN_Count) {
                        row = firstLAN_Count + 5;
                    } else {
                        row = seconfLAN_Count + 5;
                    }
                    clo = 9;
                    cells = new CellVO[row][clo];
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < clo; k++) {
                            cvo = new CellVO();
                            // cvo.celltype=1;
                            cvo.cellrownum = j;
                            cvo.cellcolnum = k;
                            cells[j][k] = cvo;
                        }
                    }
                    if (statPrdStyle.equals("1")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（月报）";
                    } else if (statPrdStyle.equals("2")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（季报）";
                    } else if (statPrdStyle.equals("3")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（半年报）";
                    } else if (statPrdStyle.equals("4")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（年报）";
                    }

                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --开始");

                    cells[1][1].cellvalue = statPrd.substring(0, 4) + "年"
                            + statPrd.substring(4, 6) + "月";
                    cells[2][0].cellvalue = "编制单位：福建农信项目组- " + regOrgId;
                    // cells[row-1][5].cellvalue="制表人 ： "+statEditUsr;
                    cells[3][0].cellvalue = "项目";
                    cells[3][1].cellvalue = "行次";
                    cells[3][2].cellvalue = "期初数";
                    cells[3][3].cellvalue = "期末数";
                    cells[3][4].cellvalue = "项目";
                    cells[3][5].cellvalue = "行次";
                    cells[3][6].cellvalue = "期初数";
                    cells[3][7].cellvalue = "期末数";
                    for (int i = 0; i < itemList.size(); i++) {
                        FncConfDefFormat item = (FncConfDefFormat) itemList
                                .get(i); // 为报表的一行
                        int itemCote = item.getFncConfCotes(); // 项目的栏位，1，2
                        int order = item.getFncConfOrder(); // 顺序编号
                        int Typ = Integer.parseInt(item.getFncItemEditTyp());
                        if (3 != Typ) {
                            FncConfDefFormat ojb;
                            ojb = (FncConfDefFormat) pfncConfStyles.getItems()
                                    .get(i);
                            if (itemCote == 1) {
                                cells[4 + i][0].cellvalue = item.getItemName();
                                if ("1".equals(item.getFncConfRowFlg())) {
                                    cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                    counts++;
                                }
                                cells[4 + i][2].cellvalue = ojb.getData1() + "";
                                cells[4 + i][3].cellvalue = ojb.getData2() + "";
                            } else {
                                cells[4 + i - firstLAN_Count][4].cellvalue = item
                                        .getItemName();
                                if ("1".equals(item.getFncConfRowFlg())) {
                                    cells[4 + i - firstLAN_Count][5].cellvalue = counts
                                            + "";// 写入行次
                                    counts++;
                                }
                                cells[4 + i - firstLAN_Count][6].cellvalue = ojb
                                        .getData1()
                                        + "";
                                cells[4 + i - firstLAN_Count][7].cellvalue = ojb
                                        .getData2()
                                        + "";
                            }
                        } else {
                            if (itemCote == 1) {
                                cells[4 + i][0].cellvalue = item.getItemName();
                            } else {
                                cells[4 + i - firstLAN_Count][4].cellvalue = item
                                        .getItemName();
                            }
                        }
                    }

                  
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --结束");
                } else if (fncConfTyp.equals("02")) {
                    // 损益表
                    int lan = exportXLDao.queryCount(styleId, 1, connection);
                    row = lan + 5;
                    clo = 5;
                    cells = new CellVO[row][clo];
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < clo; k++) {
                            cvo = new CellVO();
                            // cvo.setCelltype(1);
                            cvo.cellrownum = j;
                            cvo.cellcolnum = k;
                            cells[j][k] = cvo;
                        }
                    }
                    if (statPrdStyle.equals("1")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（月报）";
                    } else if (statPrdStyle.equals("2")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（季报）";
                    } else if (statPrdStyle.equals("3")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（半年报）";
                    } else if (statPrdStyle.equals("4")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（年报）";
                    }

                   
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --开始");
                    cells[1][1].cellvalue = statPrd.substring(0, 4) + "年"
                            + statPrd.substring(4, 6) + "月";
                    cells[2][0].cellvalue = "编制单位：福建农信项目组 " + regOrgId;
                    // cells[row-1][3].cellvalue="制表人 ： "+statEditUsr;
                    cells[3][0].cellvalue = "项目";
                    cells[3][1].cellvalue = "行次";
                    cells[3][2].cellvalue = "期初数";
                    cells[3][3].cellvalue = "期末数";
                    for (int i = 0; i < itemList.size(); i++) {
                        FncConfDefFormat itemFmt = (FncConfDefFormat) itemList
                                .get(i); // 为报表的一行

                        int confCote = itemFmt.getFncConfCotes(); // 项目的栏位，1，2
                        int order = itemFmt.getFncConfOrder(); // 顺序编号
                        int Typ = Integer.parseInt(itemFmt.getFncItemEditTyp());
                        if (3 != Typ) {
                            FncConfDefFormat ojb;
                            ojb = (FncConfDefFormat) pfncConfStyles.getItems()
                                    .get(i);
                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                            cells[4 + i][2].cellvalue = ojb.getData1() + "";
                            cells[4 + i][3].cellvalue = ojb.getData2() + "";
                        } else {
                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                        }
                    }

                    //System.out.println("封装sheet:" + cells[0][1] + " --结束");
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --结束");

                } else if (fncConfTyp.equals("03")) {
                    // 现金流量表
                    int lan = exportXLDao.queryCount(styleId, 1, connection);
                    row = lan + 5;
                    clo = 4;
                    cells = new CellVO[row][clo];
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < clo; k++) {
                            cvo = new CellVO();
                            // cvo.setCelltype(1);
                            cvo.cellrownum = j;
                            cvo.cellcolnum = k;
                            cells[j][k] = cvo;
                        }
                    }
                    if (statPrdStyle.equals("1")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（月报）";
                    } else if (statPrdStyle.equals("2")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（季报）";
                    } else if (statPrdStyle.equals("3")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（半年报）";
                    } else if (statPrdStyle.equals("4")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（年报）";
                    }
               
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --开始");
                    cells[1][1].cellvalue = statPrd.substring(0, 4) + "年"
                            + statPrd.substring(4, 6) + "月";
                    cells[2][0].cellvalue = "编制单位：福建农信项目组 " + regOrgId;
                    // cells[row-1][2].cellvalue="制表人 ： "+statEditUsr;
                    cells[3][0].cellvalue = "项目";
                    cells[3][1].cellvalue = "行次";
                    cells[3][2].cellvalue = "金额";
                    for (int i = 0; i < itemList.size(); i++) {
                        FncConfDefFormat itemFmt = (FncConfDefFormat) itemList
                                .get(i); // 为报表的一行
                        int confCote = itemFmt.getFncConfCotes(); // 项目的栏位，1，2
                        int order = itemFmt.getFncConfOrder(); // 顺序编号
                        int Typ = Integer.parseInt(itemFmt.getFncItemEditTyp());
                        if (3 != Typ) {
                            FncConfDefFormat ojb;
                            ojb = (FncConfDefFormat) pfncConfStyles.getItems()
                                    .get(i);

                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                            cells[4 + i][2].cellvalue = ojb.getData1() + "";
                        } else {
                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                        }
                    }

                    
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --结束");

                } else {
                    // 财务指标表编号
                    int lan = exportXLDao.queryCount(styleId, 1, connection);
                    row = lan + 5;
                    clo = 4;
                    cells = new CellVO[row][clo];
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < clo; k++) {
                            cvo = new CellVO();
                            // cvo.setCelltype(1);
                            cvo.cellrownum = j;
                            cvo.cellcolnum = k;
                            cells[j][k] = cvo;
                        }
                    }

                    if (statPrdStyle.equals("1")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（月报）";
                    } else if (statPrdStyle.equals("2")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（季报）";
                    } else if (statPrdStyle.equals("3")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（半年报）";
                    } else if (statPrdStyle.equals("4")) {
                        cells[0][1].cellvalue = fcs_fromDb.getFncConfDisName()
                                + "（年报）";
                    }

                   
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --开始");

                    cells[1][1].cellvalue = statPrd.substring(0, 4) + "年"
                            + statPrd.substring(4, 6) + "月";
                    cells[2][0].cellvalue = "编制单位：福建农信项目组 " + regOrgId;
                    // cells[row-1][2].cellvalue="制表人 ： "+statEditUsr;
                    cells[3][0].cellvalue = "项目";
                    cells[3][1].cellvalue = "行次";
                    cells[3][2].cellvalue = "金额";
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"itemList.size()" + itemList.size());
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"pfncConfStyles.getItems().size"
                            + pfncConfStyles.getItems().size());
                    for (int i = 0; i < itemList.size(); i++) {
                        FncConfDefFormat itemFmt = (FncConfDefFormat) itemList
                                .get(i); // 为报表的一行

                        int confCote = itemFmt.getFncConfCotes(); // 项目的栏位，1，2
                        int order = itemFmt.getFncConfOrder(); // 顺序编号
                        int Typ = Integer.parseInt(itemFmt.getFncItemEditTyp());
                        FncConfDefFormat ojb;
                        ojb = (FncConfDefFormat) pfncConfStyles.getItems().get(
                                i);
                        if (3 != Typ) {
                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                            cells[4 + i][2].cellvalue = ojb.getData1() + "";
                        } else {// 标题项
                            cells[4 + i][0].cellvalue = itemFmt.getItemName();
                            if ("1".equals(itemFmt.getFncConfRowFlg())) {
                                cells[4 + i][1].cellvalue = counts + "";// 写入行次
                                counts++;
                            }
                        }
                    }
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"封装sheet:" + cells[0][1] + " --结束");


                }

                sheet.cells = cells;
                sheet.rownum = row;
                sheet.colnum = clo;
                sheet.sheetname = styleId;
                // sheet.setFncName(tableName);
                sheets[iId] = sheet;
            }
            // EXCEL文件指定路径
            String XLSFile = (String) context.getDataValue("address");

            if (XLSFile != null && !XLSFile.equals("")) {
                try {
                    XLSFile = new String(XLSFile.getBytes("ISO-8859-1"),
                            "UTF-8")
                            + "/" + System.currentTimeMillis() + ".xls";
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                ResourceBundle res = ResourceBundle.getBundle("cmis");
                String dir = res.getString("permission.file.path").replace("permissions", "");  
                XLSFile = dir+"temp.xls";
                File file = new File(XLSFile);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // 生成一个EXCEL对象
            ExcelVO evo = new ExcelVO();
            evo.sheets = sheets;
            evo.sheetnum = evo.sheets.length;

            boolean b;
            try {
                b = ExcelTreat.writeExcel(evo, XLSFile);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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