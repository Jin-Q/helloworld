package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.dao.ExportXLDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

/**
 * 财务报表的导入
 * 
 * @author liuxin
 */
public class ImportRptOp extends CMISOperation {

    private final String modelId = "FncStatBaseTmp";
    private final String modelIdTemp = "FncConfTemplate";

    public String doExecute(Context context) throws EMPException {
        String tempFileName = null;
        Connection connection = null;
        String flagInfo = CMISMessage.DEFEAT;
        KeyedCollection kCol = null;//选择要导入的财务信息基本信息
        try {
            connection = this.getConnection(context);
            try{
            	kCol = (KeyedCollection)context.getDataElement(modelId);
            }catch(Exception e){
            	EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "未传递要导入的财报基本信息KCOl");
            }
            if(kCol == null || kCol.size() == 0){
            	throw new EMPJDBCException("未传递需要导入的财报基本信息");
            }
            
            //转化为 domain 操作
            FncStatBase fncStatBase = new FncStatBase();
            ComponentHelper compHelper = new ComponentHelper();
            compHelper.kcolTOdomain(fncStatBase, kCol);
            
            // 取联合主键值
            String cusId = fncStatBase.getCusId(); // 客户代码
            String statPrdStyle = fncStatBase.getStatPrdStyle(); // 报表周期类型
            String statPrd = get_6charsTime(fncStatBase.getStatPrd()); // 报表期间
            String statStyle = fncStatBase.getStatStyle(); // 报表口径
            String fncType = fncStatBase.getFncType();//财报类型
            
            Map map = new HashMap();
           
            if(fncStatBase.getStatBsStyleId() !=null && !"".equals(fncStatBase.getStatBsStyleId())){
            	map.put(fncStatBase.getStatBsStyleId(), fncStatBase.getStatBsStyleId());
            }
            
            if(fncStatBase.getStatPlStyleId() !=null && !"".equals(fncStatBase.getStatPlStyleId())){
            	map.put(fncStatBase.getStatPlStyleId(), fncStatBase.getStatPlStyleId());
            }
            
            if(fncStatBase.getStatCfStyleId() !=null && !"".equals(fncStatBase.getStatCfStyleId())){
            	map.put(fncStatBase.getStatCfStyleId(), fncStatBase.getStatCfStyleId());
            }
            
            if(fncStatBase.getStatFiStyleId() !=null && !"".equals(fncStatBase.getStatFiStyleId())){
            	map.put(fncStatBase.getStatFiStyleId(), fncStatBase.getStatFiStyleId());
            }
          
            if(fncStatBase.getStatSoeStyleId() !=null && !"".equals(fncStatBase.getStatSoeStyleId())){
            	map.put(fncStatBase.getStatSoeStyleId(), fncStatBase.getStatSoeStyleId());
            }
         
            if(fncStatBase.getStatSlStyleId() !=null && !"".equals(fncStatBase.getStatSlStyleId())){
            	map.put(fncStatBase.getStatSlStyleId(), fncStatBase.getStatSlStyleId());
            }

             String tempPath =(String)context.getDataValue("DocBasicinfo__file_path");
             String tempName =(String)context.getDataValue("DocBasicinfo__file_name");

             ResourceBundle res = ResourceBundle.getBundle("cmis");
             String tempFileRootPath = res.getString("tempFileRootPath");

             if(tempFileRootPath.endsWith("/")){   //如果路径以/结束
             	tempFileName = tempFileRootPath + tempPath + tempName;
             }else{
             	tempFileName = tempFileRootPath +'/' + tempPath + tempName;
             }

            // 编码
            if (tempFileName != null) {
                try {
                    tempFileName = new String(tempFileName.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,"[文件路径：]" + tempFileName + " 错误信息："+ e1.toString());
                    context.addDataField("cus_id", cusId);
                    context.addDataField("errorInfo", "errorInfo");
                    return flagInfo;
                }
            } else {
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：]"+ tempFileName);
                context.addDataField("cus_id", cusId);
                context.addDataField("errorInfo", "errorInfo");
                return flagInfo;
            }
            /*
             * 创建业务处理组件，并初始化
             */
            FncStatCommonComponent fsCommonComponent = (FncStatCommonComponent) CMISComponentFactory
                    .getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATCOMMON, context, connection);

            /********************************************/
            FncConfStyles pfncConfStyles = new FncConfStyles(); // 报表样式信息(包含具体项目信息列表)
            FncStatBase pfncStatBase = new FncStatBase(); // 公司客户报表基本信息
            /********************************************/

            pfncStatBase.setCusId(cusId); // 客户ID
            pfncStatBase.setStatPrd(statPrd); // 报表期间
            pfncStatBase.setStatPrdStyle(statPrdStyle); // 报表类型
            pfncStatBase.setStateFlg("999999999"); // 状态
            pfncStatBase.setStatStyle(statStyle);
            pfncStatBase.setFncType(fncType);

            CellVO cvo;
            // 读取文件
            ExcelVO evo = null;
            try {
                try {
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,"[文件路径：" + tempFileName + "]");
                    evo = ExcelTreat.readExcel(tempFileName);
                } catch (Exception e) {
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "[文件路径：" + tempFileName + "]" + "错误信息：" + e.toString());
                    context.addDataField("cus_id", cusId);
                    context.addDataField("errorInfo", "errorInfo");
                    return flagInfo;
                }
                SheetVO[] sheets = evo.sheets;
                SheetVO sheet = null;
                /**
                 * 判断报表sheet 数量 是否 符合 标准
                 */
                if (sheets.length != map.size()) {
                    context.addDataField("cus_id", cusId);
                    context.addDataField("errorInfo", "errorInfo");
                    return flagInfo;
                }

                /**
                 * 判断导入的报表与客户报表基表的报表是不是一致
                 */
                for (int m = 0; m < sheets.length; m++) {
                    sheet = sheets[m];
                    String sheetname = sheet.sheetname;
                    int p1 = sheetname.indexOf("-");
                    int p2 = sheetname.indexOf(")");
                    if (p1 != -1 && p2 != -1) {
                        String styleId = sheetname.substring(p1 + 1, p2);
                        if (!map.containsKey(styleId)) {
                            context.addDataField("cus_id", cusId);
                            context.addDataField("errorInfo", "errorInfo");
                            return flagInfo;
                        }
                    }
                }
                String name1 = "";
                String name = (String)sheets[0].cells[0][0].cellvalue;
                KeyedCollection kCollTemp = this.getTableModelDAO(context).queryDetail(modelIdTemp, fncType, connection);
//                String name1 = (String)kCol.getDataValue("fnc_type_displayname");
                if(kCollTemp!=null && kCollTemp.containsKey("fnc_name")){
                	name1 = (String)kCollTemp.getDataValue("fnc_name");
                	if(name.indexOf(name1) ==-1){
    					throw new CMISException("导入报表类型不符!");
                    }
                }else {
                	throw new CMISException("报表类型["+fncType+"]不存在!");
                }
                
                /**
                 * 开始处理数据导入
                 */
                for (int i = 0; i < sheets.length; i++) {

                    ExportXLDao exportXLDao = new ExportXLDao();
                    sheet = sheets[i];
                    ////从当前表单上 取得报表样式类型////
                    String _styleId = "";
                    String _repType = (String)sheet.cells[2][1].cellvalue;
                    if(_repType == null){
                    	throw new EMPException("没有从上传的EXCEL取得报表样式，请核查B3单元格");
                    }
                    String[] ary_repType = _repType.split(":");
                    if(ary_repType == null || ary_repType.length < 2){
                    	throw new EMPException("上传的EXCEL取得报表样式格式错误，请核查B3单元格");
                    }
                    _styleId = ary_repType[1].split("-")[1];
                    if(_styleId == null || _styleId.trim().equals("")){
                    	throw new EMPException("上传的EXCEL取得报表样式格式错误，请核查B3单元格");
                    }                    
                    /**
                     * 取得缓存中获取到报表样式 从FNC_CONF_STYLES里面取得 关键是通过这次取得
                     * 知道要把从EXCEL里面读取的信息放到哪张物理报表去 还有就是取得item的信息
                     */
                    FncConfStyles fcs_fromCh = (FncConfStyles) FNCFactory.getFNCInstance(_styleId);
                    if (fcs_fromCh == null) {
                        throw new CMISException(CMISMessage.MESSAGEDEFAULT, "报表样式取不到");
                    }
                    // 得到样式表中的 数据列数 如果是2列的话 那么就是期初值和期末值
                    int dataCol = fcs_fromCh.getFncConfDataCol();
                    // 得到样式表中的 显示名称
                    String fncConfDisName = fcs_fromCh.getFncConfDisName();
                    // 得到固定样式中所有的项目item的信息，这些具体的信息来自FNC_CONF_DEF_FMT
                    // 获取到样式的栏位数
                    int fncCote = fcs_fromCh.getFncConfCotes();
                    List itemslist_defInCh = fcs_fromCh.getItems();

                    int firstLAN_Count = 0;// 标示栏位1的项目的个数
                    int seconfLAN_Count = 0;// 标示栏位2的项目的个数
                    int thLAN_Count = 0;// 标示栏位3的项目的个数
                    int fLAN_Count = 0;// 标示栏位4的项目的个数
                    if (fncCote == 1) {
                        firstLAN_Count = exportXLDao.queryCount(_styleId, 1, connection);
                    } else if (fncCote == 2) {
                        firstLAN_Count = exportXLDao.queryCount(_styleId, 1, connection);
                        seconfLAN_Count = exportXLDao.queryCount(_styleId, 2, connection);
                    } else if (fncCote == 3) {
                        firstLAN_Count = exportXLDao.queryCount(_styleId, 1, connection);
                        seconfLAN_Count = exportXLDao.queryCount(_styleId, 2, connection);
                        thLAN_Count = exportXLDao.queryCount(_styleId, 3, connection);
                    } else if (fncCote == 4) {
                        firstLAN_Count = exportXLDao.queryCount(_styleId, 1, connection);
                        seconfLAN_Count = exportXLDao.queryCount(_styleId, 2, connection);
                        thLAN_Count = exportXLDao.queryCount(_styleId, 3, connection);
                        fLAN_Count = exportXLDao.queryCount(_styleId, 4, connection);
                    }

                    // 创建项目信息对象列表
                    ArrayList<FncConfDefFormat> arrlist = new ArrayList<FncConfDefFormat>();

                    CellVO[][] cells;
                    cells = sheet.cells;
                    int row = sheet.rownum;
                    int col = sheet.colnum + 1;
                    for (int j = 0; j < itemslist_defInCh.size(); j++) {

                        /**
                         * \]2 而这个类是用excel文件里面的某个item的信息封装起来的，一会要用来做"数据库写入"的
                         */
                        FncConfDefFormat fncConfDefFormat = new FncConfDefFormat();

                        /**
                         * 这个是用来取得缓存中获取到具体的item
                         */
                        FncConfDefFormat fcf_DefInCh = (FncConfDefFormat) itemslist_defInCh
                                .get(j);
                        String editTyp = fcf_DefInCh.getFncItemEditTyp();
                        int fCote = fcf_DefInCh.getFncConfCotes();// 具体的一个项目所在的栏位

                        // 格式表中的栏位 , 来自数据库的定义
                        // int fncConfCote_defInDatabase =
                        // fcf_DefInCh.getFncConfCotes();
                        if (3 != Integer.parseInt(editTyp)) {// 如果不是标题项
                            fncConfDefFormat.setFncConfCalFrm(fcf_DefInCh.getFncConfCalFrm());
                            fncConfDefFormat.setFncConfChkFrm(fcf_DefInCh.getFncConfChkFrm());
                            fncConfDefFormat.setItemId(fcf_DefInCh.getItemId());
                            fncConfDefFormat.setItemName(fcf_DefInCh.getItemName());

                            // 设置editType
                            fncConfDefFormat.setFncItemEditTyp(editTyp);
                            int first_lan_count = exportXLDao.queryCount(_styleId, 1, connection);

                            /**
                             * 根据从缓存中获取的样式的栏位数来读取sheet中单元格中的数据
                             */
                            int post = 0;
                            if (fncCote == 1) {
                                if (j >= firstLAN_Count) {
                                    post = j - firstLAN_Count;
                                } else {
                                    post = j;
                                }
                            } else if (fncCote == 2) {
                                if (j >= firstLAN_Count && j < firstLAN_Count + seconfLAN_Count) {
                                    post = j - firstLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count;
                                } else {
                                    post = j;
                                }
                            } else if (fncCote == 3) {
                                if (j >= firstLAN_Count && j < firstLAN_Count + seconfLAN_Count) {
                                    post = j - firstLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count
                                        && j < firstLAN_Count + seconfLAN_Count + thLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count + thLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count - thLAN_Count;
                                } else {
                                    post = j;
                                }
                            } else if (fncCote == 4) {
                                if (j >= firstLAN_Count && j < firstLAN_Count + seconfLAN_Count) {
                                    post = j - firstLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count
                                        && j < firstLAN_Count + seconfLAN_Count + thLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count + thLAN_Count
                                        && j < firstLAN_Count + seconfLAN_Count + thLAN_Count + fLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count - thLAN_Count;
                                } else if (j >= firstLAN_Count + seconfLAN_Count + thLAN_Count + fLAN_Count) {
                                    post = j - firstLAN_Count - seconfLAN_Count - thLAN_Count - fLAN_Count;
                                } else {
                                    post = j;
                                }
                            }
                            // 根据数据的列数取到具体的数据
                            /**
                             * fCote*2+fCote*dataCol-dataCol 是计算单元格的从坐标使用的
                             * 其中的2是指“项目”+“行次”的一个纵行数
                             */
                            if (dataCol == 8) {
                                // double[] dataA = new double[8];
                                double[] dataA = new double[] { 0, 0, 0, 0, 0,
                                        0, 0, 0 };
                                double[] dataB = new double[8];

                                // 如果有上期数据，就使用该注销部分
                                /*
                                 * for(int a=0;a<8;a++){ if(a==6){ dataA[a] =
                                 * 0.0; }else{ if(a==7){ String cellValue =
                                 * (String) cells[post +
                                 * 4][fCote*2+fCote*dataCol
                                 * -dataCol+a-1].cellvalue;
                                 * if(!cellValue.trim().equals("")){ dataA[a] =
                                 * Double.parseDouble(cellValue); }else{
                                 * dataA[a] = 0.0; } }else{ String cellValue =
                                 * (String) cells[post +
                                 * 4][fCote*2+fCote*dataCol
                                 * -dataCol+a].cellvalue;
                                 * if(!cellValue.trim().equals("")){ dataA[a] =
                                 * Double.parseDouble(cellValue); }else{
                                 * dataA[a] = 0.0; } } } } for(int b=0;b<8;b++){
                                 * if(b==6){ dataB[b] = 0.0; }else{ if(b==7){
                                 * String cellValue = (String) cells[post +
                                 * 4][fCote*2+fCote*dataCol-dataCol
                                 * +b-1+7].cellvalue;
                                 * if(!cellValue.trim().equals("")){ dataB[b] =
                                 * Double.parseDouble(cellValue); }else{
                                 * dataB[b] = 0.0; } }else{ String cellValue =
                                 * (String) cells[post +
                                 * 4][fCote*2+fCote*dataCol
                                 * -dataCol+b+7].cellvalue;
                                 * if(!cellValue.trim().equals("")){ dataB[b] =
                                 * Double.parseDouble(cellValue); }else{
                                 * dataB[b] = 0.0; } } } }
                                 */

                                for (int a = 0; a < 8; a++) {
                                    if (a == 6) {
                                        dataB[a] = 0.0;
                                    } else {
                                        if (a == 7) {
                                            String cellValue = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol + a - 1].cellvalue;
                                            if (!cellValue.trim().equals("")) {
                                                dataB[a] = Double.parseDouble(cellValue);
                                            } else {
                                                dataB[a] = 0.0;
                                            }
                                        } else {
                                            String cellValue = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol + a].cellvalue;
                                            if (!cellValue.trim().equals("")) {
                                                dataB[a] = Double.parseDouble(cellValue);
                                            } else {
                                                dataB[a] = 0.0;
                                            }
                                        }
                                    }
                                }
                                for (int b = 0; b < 8; b++) {
                                    if (b == 6) {
                                        dataA[b] = 0.0;
                                    } else {
                                        if (b == 7) {
                                            String cellValue = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol + b - 1 + 7].cellvalue;
                                            if (!cellValue.trim().equals("")) {
                                                dataA[b] = Double.parseDouble(cellValue);
                                            } else {
                                                dataA[b] = 0.0;
                                            }
                                        } else {
                                            String cellValue = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol + b + 7].cellvalue;
                                            if (!cellValue.trim().equals("")) {
                                                dataA[b] = Double.parseDouble(cellValue);
                                            } else {
                                                dataA[b] = 0.0;
                                            }
                                        }
                                    }
                                }
                                fncConfDefFormat.setDataA(dataA);
                                fncConfDefFormat.setDataB(dataB);

                            } else {
                                String cellValue = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol].cellvalue;
                                if (!cellValue.trim().equals("")) {
                                    fncConfDefFormat.setData1(Double.parseDouble(cellValue));
                                } else {
                                    fncConfDefFormat.setData1(0);
                                }
                                if (dataCol == 2) {
                                    String cellValue2 = (String) cells[post + 4][fCote * 2 + fCote * dataCol - dataCol + 1].cellvalue;
                                    if (!cellValue2.trim().equals("")) {
                                        fncConfDefFormat.setData2(Double.parseDouble(cellValue2));
                                    } else {
                                        fncConfDefFormat.setData2(0);
                                    }
                                }
                            }
                            arrlist.add(fncConfDefFormat);
                        } else {
                            // 如果是标题项的话
                            fncConfDefFormat.setItemId(fcf_DefInCh.getItemId());
                            fncConfDefFormat.setItemName(fcf_DefInCh.getItemName());
                            // 设置editType
                            fncConfDefFormat.setFncItemEditTyp(editTyp);

                            arrlist.add(fncConfDefFormat);
                        }
                    }
                    // 设置报表项目信息
                    pfncConfStyles.setItems(arrlist);
                    pfncConfStyles.setFncConfDataCol(dataCol);
                    pfncConfStyles.setFncConfCotes(fcs_fromCh.getFncConfCotes());
                    pfncConfStyles.setFncConfDisName(fcs_fromCh.getFncConfDisName());
                    pfncConfStyles.setFncName(fcs_fromCh.getFncName());
                    pfncConfStyles.setStyleId(fcs_fromCh.getStyleId());
                    pfncConfStyles.setFncConfTyp(fcs_fromCh.getFncConfTyp());
                    flagInfo = fsCommonComponent.addFncStat(pfncConfStyles, pfncStatBase);
                }

            } catch (EMPException e) {
                EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "错误信息："
                        + e.toString());
                throw e;
            }

            context.addDataField("cus_id", cusId);
            context.addDataField("stat_prd_style", statPrdStyle);
            context.addDataField("stat_prd", statPrd);
            context.addDataField("stat_style", statStyle);
            context.put("fnc_type", fncType);
            context.addDataField("errorInfo", "successInfo");
        } catch (NumberFormatException ee) {
            throw new EMPException("导入财报数据不合法，请检查导入报表信息："+ee.getMessage());
        } catch (Exception e) {
            throw new EMPException("财报导入错误，错误信息为："+e.getMessage());
        } finally {
        	
        	//导入的时候首先上传文件 所以这个地方判断文件是否存在如果存在直接删除文件 所以有异常不处理
        	try{
        		File file = new File(tempFileName);
        		boolean delFlag = file.delete();       		
        		EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "删除对应的文件+"+tempFileName+(delFlag?"成功":"失败"));
        	}catch(Exception e){
        		EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "导入财务报表之后删除上传的临时文件错误！");
        	}
        	
            if (connection != null)
                this.releaseConnection(context, connection);
        }
        return flagInfo;
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
