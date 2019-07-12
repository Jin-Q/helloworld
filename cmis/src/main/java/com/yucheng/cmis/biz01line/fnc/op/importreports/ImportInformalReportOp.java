package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class ImportInformalReportOp extends CMISOperation {

	private final String modelId = "ReportInfo";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		KeyedCollection kColl = null;
		String tempFileName = null;
		Connection connection = null;

		TableModelDAO dao = this.getTableModelDAO(context);
		try {
			connection = this.getConnection(context);
			
			try{
				kColl = (KeyedCollection)context.getDataElement(modelId);
            }catch(Exception e){
            	EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "未传递要导入的财报基本信息KCOl");
            }
            if(kColl == null || kColl.size() == 0){
            	throw new EMPJDBCException("未传递需要导入的财报基本信息");
            }
			/*
			 *  读取xls文件路径
			 */
            
             String reportType = (String)kColl.getDataValue("report_type");
             String serno = (String)kColl.getDataValue("serno");
             if(serno==null || "".equals(serno)){
             	EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取流水号为空！");
             	throw new EMPException("获取流水号为空！");
             }
             if(reportType==null || "".equals(reportType)){
            	EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取报表类型为空！");
            	throw new EMPException("获取报表类型为空！");
            }
			String tempPath = (String) context
					.getDataValue("DocBasicinfo__file_path");
			String tempName = (String) context
					.getDataValue("DocBasicinfo__file_name");
			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String tempFileRootPath = res.getString("tempFileRootPath");
			tempFileName = tempFileRootPath +'/' + tempPath + tempName;

			//tempFileName = tempFileRootPath + tempPath + tempName;
			//调用导入数据方法
			//importAssetDepreciationTableData(tempFileName, connection, dao);
			if("01".equals(reportType)){
				importAssetDepreciationTableData(tempFileName,serno, connection, dao);
			}else if("02".equals(reportType)){
				importProfitAndLossTableData(tempFileName,serno,connection, dao);
			}else if("03".equals(reportType)){
				importCurrentFlowTableData(tempFileName,serno,connection, dao);
			}else if("04".equals(reportType)){
				importInvestigationTableData(tempFileName,serno, connection, dao);
			}else if("05".equals(reportType)){
				importPlantingInvestigationTableData(tempFileName, serno,connection, dao);
			}else if("06".equals(reportType)){
				importBreedingInvestigationTableData(tempFileName,serno,connection, dao);
			}else {
				throw new EMPException("导入报表格式有误!");
				
			}
			//importProfitAndLossTableData(tempFileName, connection, dao);
			//importCurrentFlowTableData(tempFileName, connection, dao);
			KeyedCollection redirect = new KeyedCollection("redirect");
			redirect.put("serno", serno);
			redirect.put("report_type", reportType);
			this.putDataElement2Context(redirect, context);
			context.put("serno", serno);
			context.put("report_type", reportType);

		} catch (EMPException ee) {
			throw new EMPException("导入报表格式有误!");
		}catch (Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "", e);
			throw new EMPException("导入报表格式有误!");
		}finally {
			// 导入的时候首先上传文件 所以这个地方判断文件是否存在如果存在直接删除文件 所以有异常不处理
			try {
				File file = new File(tempFileName);
				boolean delFlag = file.delete();
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0,
						"删除对应的文件+" + tempFileName + (delFlag ? "成功" : "失败"));
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0,
						"导入财务报表之后删除上传的临时文件错误！");
			}
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}
	/**
	 * 导入资产负债表数据
	 */
	public void importAssetDepreciationTableData(String filePath,String serno ,Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection iColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));

			int numSheets = 0;
			HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				
				/**
				 * 资产类（150）
				 */
				
				/* 现金和银行账款(row:8,column:2-6)	*/
				int startRow = 7;
				HSSFRow row = null;
				row = Sheet.getRow(2);
				String ass = row.getCell(1).toString();
				if(ass.indexOf("负债") ==-1){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
					throw new EMPException("导入格式错误!");
				}
				while (true) {
					 row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("remark", getCellValue(row.getCell(2)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_flag", "160");	//科目:现金和银行账款
						kColl.put("fnc_type", 150);		//资产类型
						iColl.add(kColl);
					}
				}
				/* 当前行处于应收账款所在行，需要下移两行  */
				startRow += 2;
				
				/* 应收账款*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("begin_date", getCellValue(row.getCell(2)));
						kColl.put("end_date", getCellValue(row.getCell(3)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", "150");//资产类型
						kColl.put("fnc_flag", "161");//科目:应收账款
						iColl.add(kColl);
					}
				}
				/* 当前行处于预付账款所在行，需要下移两行 */
				startRow += 2;
				/*	预付账款	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("remark", getCellValue(row.getCell(2)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", "150");//资产类型
						kColl.put("fnc_flag", "162");//科目:预付账款
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于存货所在行，需要下移两行	*/
				startRow += 2;
				/*  存货  */
				while(true){
					 row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("num", getCellValue(row.getCell(3)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", "150");//资产类型
						kColl.put("fnc_flag", "163");//科目:存货
						iColl.add(kColl);
					}
				}
				/*	当前行处于流动资产总计所在行，需要下移三行	 */
				startRow += 3;
				/* 固定资产	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("fix_asset_details", getCellValue(row.getCell(1)));
						kColl.put("fix_asset_buy_date", getCellValue(row.getCell(2)));
						kColl.put("fix_asset_primary_value", getCellValue(row.getCell(3)));
						kColl.put("fix_asset_depreciation", getCellValue(row.getCell(4)));
						kColl.put("fix_asset_current_value", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", "150");//资产类型
						kColl.put("fnc_flag", "164");//科目:固定资产
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于其他资产所在行，需要下移两行	 */
				startRow += 2;
				/*	 其他资产	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("begin_date", getCellValue(row.getCell(2)));
						kColl.put("end_date", getCellValue(row.getCell(3)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", 150);//资产类型
						kColl.put("fnc_flag", "165");//科目:其他资产
						iColl.add(kColl);
					}
				}
				/*	 当前行处于总资产所在行，需要下移三行	 */
				startRow += 3;
				/* 其他非经营资产或表外资产	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(1)));
						kColl.put("remark", getCellValue(row.getCell(2)));
						kColl.put("amt", getCellValue(row.getCell(5)));
						kColl.put("fnc_type", 150);//资产类型
						kColl.put("fnc_flag", "166");//科目:其他非经营资产或表外资产
						iColl.add(kColl);
					}
				}
				/*	 当前行处于财产情况所在行，需要下移三行	 */
				startRow += 3;
				/* 财产情况——房产	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("车") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("code_num", getCellValue(row.getCell(1)));
						kColl.put("owner_name", getCellValue(row.getCell(2)));
						System.out.println(getCellValue(row.getCell(4)));
						kColl.put("hou_num", getCellValue(row.getCell(4)));
						kColl.put("hou_addr", getCellValue(row.getCell(6)));
						kColl.put("hou_area", getCellValue(row.getCell(9)));
						kColl.put("fnc_type", 150);//资产类型
						kColl.put("fnc_flag", "167");//科目:财产情况——房产
						iColl.add(kColl);
					}
				}

				/*	 当前行处于序号（车辆）所在行，需要下移一行	 */
				startRow += 1;
				/*	财产情况——车辆	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("银行") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("code_num", getCellValue(row.getCell(1)));
						kColl.put("owner_name", getCellValue(row.getCell(2)));
						kColl.put("car_flag_no", getCellValue(row.getCell(4)));
						kColl.put("buy_money", getCellValue(row.getCell(6)));
						kColl.put("cus_id_car", getCellValue(row.getCell(7)));
						kColl.put("fnc_type", 150);//资产类型
						kColl.put("fnc_flag", "168");//科目:财产情况——车辆
						iColl.add(kColl);
					}
				}
				/*	当前行处于序号（银行账户）所在行，需要下移一行		*/
				startRow += 1;
				/* 财产情况——银行账户	*/
				while(true){
					 row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().indexOf("表中") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("code_num", getCellValue(row.getCell(1)));
						kColl.put("accout_name", getCellValue(row.getCell(2)));
						kColl.put("bank_name", getCellValue(row.getCell(4)));
						kColl.put("accout_no", getCellValue(row.getCell(7)));
						kColl.put("fnc_type", 150);//资产类型
						kColl.put("fnc_flag", "169");//科目:财产情况——银行账户
						iColl.add(kColl);
					}
				}
				
				
				/**
				 * 负债（151）
				 */
				/*	将当前行重置为起始行（row:8,cloumn:7-11)	*/
				
				startRow = 7;
				/*	应付供货商账款	*/
				while (true) {
					 row = Sheet.getRow(startRow++);
					if (row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(6).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(6)));
						kColl.put("begin_date", getCellValue(row.getCell(7)));
						kColl.put("end_date", getCellValue(row.getCell(8)));
						kColl.put("amt", getCellValue(row.getCell(10)));
						kColl.put("fnc_type", 151);//负债类型
						kColl.put("fnc_flag", "170");//科目:应付供货商账款
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于预收账款所在行，需要下移两行		*/
				startRow += 2;
				/* 预收账款	*/
				while (true) {
					 row = Sheet.getRow(startRow++);
					if (row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(6).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(6)));
						kColl.put("remark", getCellValue(row.getCell(7)));
						kColl.put("amt", getCellValue(row.getCell(10)));
						kColl.put("fnc_type", 151);//负债类型
						kColl.put("fnc_flag", "171");//科目:预收账款
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于银行借款所在行，需要下移两行		*/
				startRow += 2;
				/* 银行借款	*/
				while (true) {
					 row = Sheet.getRow(startRow++);
					if (row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(6).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(6)));
						kColl.put("bank_name", getCellValue(row.getCell(7)));
						kColl.put("begin_date", getCellValue(row.getCell(8)));
						kColl.put("end_date", getCellValue(row.getCell(9)));
						kColl.put("amt", getCellValue(row.getCell(10)));
						kColl.put("fnc_type", 151);//负债类型
						kColl.put("fnc_flag", "172");//科目:银行借款
						iColl.add(kColl);
					}
				}
				/*	当前行处于应付其他账款所在行，需要下移两行		*/
				startRow += 2;
				/* 应付其他账款	*/
				while (true) {
					 row = Sheet.getRow(startRow++);
					if (row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(6).toString().indexOf("总计") != -1){
						break;			//end of this block
					}else{
						kColl = new KeyedCollection();
						kColl.put("pro_num", getCellValue(row.getCell(6)));
						kColl.put("begin_date", getCellValue(row.getCell(7)));
						kColl.put("end_date", getCellValue(row.getCell(8)));
						kColl.put("amt", getCellValue(row.getCell(10)));
						kColl.put("fnc_type", 151);//负债类型
						kColl.put("fnc_flag", "173");//科目:应付其他账款
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于总负债所在行	*/

				/* 其他	*/
				while(true){
					 row = Sheet.getRow(startRow++);
					if(getCellValue(row.getCell(6)).contains("申请金额")){
						kColl = new KeyedCollection();
						kColl.put("apply_money", getCellValue(row.getCell(7)));
						kColl.put("amount_money", getCellValue(row.getCell(10)));
						kColl.put("fnc_type", 151);//负债类型
						kColl.put("fnc_flag", "174");//科目:其他
						iColl.add(kColl);
						break;
					}
				}
				
				/*	当前行处于【资产负债率】所在行	*/

				/* 制表人+日期	*/
				
				String inserUser = null;
				String inserDate = null;
				while(true){
					 row = Sheet.getRow(startRow++);
					if(getCellValue(row.getCell(6)).contains("制表人")){
						inserUser = getCellValue(row.getCell(7));
						inserDate = getCellValue(row.getCell(9));
						break;
					}
				}
				
				/*	获取表头信息	*/
				startRow = 3;
				 row = Sheet.getRow(startRow);
				String headInfo = getCellValue(row.getCell(1));
				
				String[] headInfoArray = getHeadInfo(headInfo);
				String application = headInfoArray[0];
				String dateString = headInfoArray[1];
				
				
				kColl = new KeyedCollection();
				kColl.put("inser_user", inserUser);
				kColl.put("inser_date", inserDate);
				kColl.put("application", application);
				kColl.put("apply_date", dateString);
				kColl.put("fnc_type", 152);//表信息
				iColl.add(kColl);
				
			}
			/*	插入数据	*/
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncBs");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！");
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件io异常！");
			throw new EMPException("文件io异常！");
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！");
		}
		
	}
	
	/**
	 * 导入损益表数据
	 */
	public void importProfitAndLossTableData(String filePath,String serno,Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection iColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));
			  int numSheets;
			   int num = workbook.getNumberOfSheets();
			   if(1==num){
				   numSheets = 0;
			   }else{
				   numSheets = 1;
			   }
			   HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			   int startRow;
			   HSSFRow row = null;
				/*	获取表头信息	*/
				startRow = 4;
				row = Sheet.getRow(startRow);
		        String name = row.getCell(0).toString();
			   if(name.indexOf("损益表")==-1){
				   EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
					throw new EMPException("导入格式错误!");
			   }
			
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				
				/* 定位到【经营收入】所在行	*/
				 startRow = 2;
				

				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("经营收入")){
						break;
					}
				}
				/*	startRow 位于【经营收入】的下一行,读取经营收入的每一项	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())){
						continue;
					}else if(row.getCell(1).toString().contains("总计")){
						break;	
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "01");//经营收入——01
						kColl.put("category_desc", "经营收入");
						kColl.put("item_no", getCellValue(row.getCell(0)));
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						kColl.put("mon_7", getCellValue(row.getCell(8)));
						kColl.put("mon_8", getCellValue(row.getCell(9)));
						kColl.put("mon_9", getCellValue(row.getCell(10)));
						kColl.put("mon_10", getCellValue(row.getCell(11)));
						kColl.put("mon_11", getCellValue(row.getCell(12)));
						kColl.put("mon_12", getCellValue(row.getCell(13)));
						kColl.put("mon_flag", getCellValue(row.getCell(16)));
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于【可变成本】所在行，需要下移一行	*/
				++startRow;
				
				/* 可变成本	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if(row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())){
						continue;
					}else if(row.getCell(1).toString().contains("总计")){
						break;	
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "02");//可变成本——02
						kColl.put("category_desc", "可变成本");
						kColl.put("item_no", getCellValue(row.getCell(0)));
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						kColl.put("mon_7", getCellValue(row.getCell(8)));
						kColl.put("mon_8", getCellValue(row.getCell(9)));
						kColl.put("mon_9", getCellValue(row.getCell(10)));
						kColl.put("mon_10", getCellValue(row.getCell(11)));
						kColl.put("mon_11", getCellValue(row.getCell(12)));
						kColl.put("mon_12", getCellValue(row.getCell(13)));
						kColl.put("mon_flag", getCellValue(row.getCell(16)));
						iColl.add(kColl);
					}
				}
				
				/*	当前行处于【毛利润】所在行，需要下移两行	*/
				startRow+=2;
				
				/*	工资	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "04");//工资——04
				kColl.put("category_desc", "工资");
				kColl.put("item_name", "工资");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	社会保险费	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "05");//社会保险费——05
				kColl.put("category_desc", "社会保险费");
				kColl.put("item_name", "社会保险费");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	租金	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "06");//租金——06
				kColl.put("category_desc", "租金");
				kColl.put("item_name", "租金");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	交通费用	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "07");//交通费用——07
				kColl.put("category_desc", "交通费用");
				kColl.put("item_name", "交通费用");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	维护	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "08");//维护——08
				kColl.put("category_desc", "维护");
				kColl.put("item_name", "维护");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	水电费	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "09");//水电费——09
				kColl.put("category_desc", "水电费");
				kColl.put("item_name", "水电费");
				
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	存货/原料损失	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "10");//存货/原料损失——10
				kColl.put("category_desc", "存货、原料损失");
				kColl.put("item_name", "存货、原料损失");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	税	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "11");//税——11
				kColl.put("category_desc", "税");
				kColl.put("item_name", "税");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	其他（1）	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "12");//其他（1）——12
				kColl.put("category_desc", "其他（1）");
				kColl.put("item_name", "其他（1）");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	其他（2）	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "13");//其他（2）——13
				kColl.put("category_desc", "其他（2）");
				kColl.put("item_name", "其他（2）");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	当前行处于【总计（3）】所在行，需要下移一行	*/
				++startRow;
				/*	分期付款（4）	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "14");//分期付款（4）——14
				kColl.put("category_desc", "分期付款");
				kColl.put("item_name", "分期付款");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	当前行处于【营业利润】所在行，需要下移一行	*/
				++startRow;
				
				/*	家庭开支	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "16");//家庭开支——16
				kColl.put("category_desc", "家庭开支");
				kColl.put("item_name", "家庭开支");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	其他收入	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "17");//其他收入——17
				kColl.put("category_desc", "其他收入");
				kColl.put("item_name", "其他收入");
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				kColl.put("mon_8", getCellValue(row.getCell(9)));
				kColl.put("mon_9", getCellValue(row.getCell(10)));
				kColl.put("mon_10", getCellValue(row.getCell(11)));
				kColl.put("mon_11", getCellValue(row.getCell(12)));
				kColl.put("mon_12", getCellValue(row.getCell(13)));
				kColl.put("mon_flag", getCellValue(row.getCell(16)));
				iColl.add(kColl);
				
				/*	取制表人和日期	*/
				String charter = null;		//制表人
				String chart_date = null;	//日期
				while(true){
					row = Sheet.getRow(startRow++);
					int i=0;
					for(;i<row.getLastCellNum();i++){
						if(getCellValue(row.getCell(i)).contains("制表人")){
							break;
						}
					}
					if(i < row.getLastCellNum()){
						charter = getCellValue(row.getCell(++i));
						chart_date = getCellValue(row.getCell(i+2));
						break;
					}
				}
				
				/*	获取表头信息	*/
				startRow = 4;
				row = Sheet.getRow(startRow);
				String headInfo = getCellValue(row.getCell(11));
				
				String[] headInfoArray = getHeadInfo(headInfo);
				String application = headInfoArray[0];
				String dateString = headInfoArray[1];
				
				/*	获取月份信息	*/
				startRow = 5;
				row = Sheet.getRow(startRow);
				int i = 2;
				HSSFCell cell = null;
				StringBuilder monthHeads = new StringBuilder();
				String eachMonth = null;
				while(true){
					cell = row.getCell(i++);
					if (cell.toString().contains("月平均")){
						break;
					}else{
						eachMonth = getCellValue(cell);
						eachMonth = eachMonth.substring(0, eachMonth.indexOf("月"));
						if("".equals(eachMonth.trim())){
							EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取月份信息为空！");
							throw new EMPException("获取月份信息为空！");
						}
						monthHeads.append(eachMonth).append(";");
					}
				}
				if(monthHeads.length() > 0){
					monthHeads = new StringBuilder(monthHeads.substring(0, monthHeads.length()-1));
				}else if (application.indexOf("损益表") ==-1){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
					throw new EMPException("导入格式错误！");
				}else{
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取月份信息失败！");
					throw new EMPException("获取月份信息失败！");
				}
				
				String[] monthsArray = monthHeads.toString().split(";");
				
				
				kColl = new KeyedCollection();
				kColl.put("category", "19");//制表人和日期——19
				kColl.put("category_desc", "表信息");
				kColl.put("charter", charter);
				kColl.put("chart_date", chart_date);
				kColl.put("application", application);
				kColl.put("apply_date", dateString);
				kColl.put("mon_1", monthsArray[0]);
				kColl.put("mon_2", monthsArray[1]);
				kColl.put("mon_3", monthsArray[2]);
				kColl.put("mon_4", monthsArray[3]);
				kColl.put("mon_5", monthsArray[4]);
				kColl.put("mon_6", monthsArray[5]);
				kColl.put("mon_7", monthsArray[6]);
				kColl.put("mon_8", monthsArray[7]);
				kColl.put("mon_9", monthsArray[8]);
				kColl.put("mon_10", monthsArray[9]);
				kColl.put("mon_11", monthsArray[10]);
				kColl.put("mon_12", monthsArray[11]);
				iColl.add(kColl);
			}
			
			/*	插入数据	*/
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncPl");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！");
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件导入格式错误！");
			throw new EMPException("文件导入格式错误！");
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！");
		}
		
	}
	
	/**
	 * 导入现金流量表数据
	 */
	public void importCurrentFlowTableData(String filePath,String serno,Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection iColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));
			  int numSheets;
		   int num = workbook.getNumberOfSheets();
		   if(1==num){
			   numSheets = 0;
		   }else{
			   numSheets = 2;
		   }
		   
			
			HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				
				/* 定位到【月初现金】所在行的下一行	*/
				int startRow = 0;
				HSSFRow row = null;
				HSSFRow curr = Sheet.getRow(3);
				String abc = curr.getCell(0).toString();
				if(abc.indexOf("流量表")==-1){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
					throw new EMPException("导入格式错误!");
				}
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("月初现金")){
						break;
					}
				}
				/*	startRow 位于【现金销售额】所在行		*/
				
				/*	现金销售额	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");//现金销售额——01
				kColl.put("category_desc", "现金销售额");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	应收账款回收	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "02");//应收账款回收——02
				kColl.put("category_desc", "应收账款回收");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	客户预付款	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "03");//客户预付款——03
				kColl.put("category_desc", "客户预付款");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	定位到【现金购买原材料、服务、货物】所在行，需要下移一行		*/
				++startRow;
				/*	现金购买原材料、服务、货物		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "04");//现金购买原材料、服务、货物——04
				kColl.put("category_desc", "现金购买原材料、服务、货物");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	应付账款的支付		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "05");//应付账款的支付——05
				kColl.put("category_desc", "应付账款的支付");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	购货的预付款项（原材料、服务费）		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "06");//购货的预付款项（原材料、服务费）——06
				kColl.put("category_desc", "购货的预付款项（原材料、服务费）");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	定位到【工资及劳保】所在行，需要下移一行		*/
				++startRow;
				
				/*	工资及劳保		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "07");//工资及劳保——07
				kColl.put("category_desc", "工资及劳保");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	税收		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "08");//税收——08
				kColl.put("category_desc", "税收");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	交通费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "09");//交通费用——09
				kColl.put("category_desc", "交通费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	租金		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "10");//租金——10
				kColl.put("category_desc", "租金");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	维护费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "11");//维护费用——11
				kColl.put("category_desc", "维护费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	水电费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "12");//水电费用——12
				kColl.put("category_desc", "水电费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	广告费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "13");//广告费用——13
				kColl.put("category_desc", "广告费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	交际费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "14");//交际费用——14
				kColl.put("category_desc", "交际费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	其它费用		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "15");//其它费用——15
				kColl.put("category_desc", "其它费用");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	当前行处于【总固定成本】所在行，需要下移三行	*/
				startRow+=3;
				
				/*	购买新固定资产的支出		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "16");//购买新固定资产的支出——16
				kColl.put("category_desc", "购买新固定资产的支出");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	建设和装修等的支出		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "17");//建设和装修等的支出——17
				kColl.put("category_desc", "建设和装修等的支出");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	其他支出		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "18");//其他支出——18
				kColl.put("category_desc", "其他支出");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	固定资产出售流入		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "19");//固定资产出售流入——19
				kColl.put("category_desc", "固定资产出售流入");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	其他流入		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "20");//其他流入——20
				kColl.put("category_desc", "其他流入");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	当前行处于【投资活动总现金流】所在行，需要下移一行	*/
				++startRow;
				
				/*	银行贷款	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "21");//银行贷款——21
				kColl.put("category_desc", "银行贷款");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	其他借款	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "22");//其他借款——22
				kColl.put("category_desc", "其他借款");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	偿还银行贷款本金以及利息	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "23");//偿还银行贷款本金以及利息——23
				kColl.put("category_desc", "偿还银行贷款本金以及利息");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	偿还其他借款	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "24");//偿还其他借款——24
				kColl.put("category_desc", "偿还其他借款");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	当前行处于【融资活动总现金流】所在行，需要下移一行	*/
				++startRow;
				
				/*	其他现金来源		*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "25");//其他现金来源——25
				kColl.put("category_desc", "其他现金来源");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	家庭开支	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "26");//家庭开支——26
				kColl.put("category_desc", "家庭开支");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	私人使用资金	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "27");//私人使用资金——27
				kColl.put("category_desc", "私人使用资金");
				kColl.put("mon_pre_5", getCellValue(row.getCell(1)));
				kColl.put("mon_pre_4", getCellValue(row.getCell(2)));
				kColl.put("mon_pre_3", getCellValue(row.getCell(3)));
				kColl.put("mon_pre_2", getCellValue(row.getCell(4)));
				kColl.put("mon_pre_1", getCellValue(row.getCell(5)));
				kColl.put("mon_pre_0", getCellValue(row.getCell(6)));
				kColl.put("mon_aft_0", getCellValue(row.getCell(7)));
				kColl.put("mon_aft_1", getCellValue(row.getCell(8)));
				kColl.put("mon_aft_2", getCellValue(row.getCell(9)));
				kColl.put("mon_aft_3", getCellValue(row.getCell(10)));
				kColl.put("mon_aft_4", getCellValue(row.getCell(11)));
				kColl.put("mon_aft_5", getCellValue(row.getCell(12)));
				kColl.put("mon_aft_6", getCellValue(row.getCell(13)));
				kColl.put("mon_aft_7", getCellValue(row.getCell(14)));
				kColl.put("mon_aft_8", getCellValue(row.getCell(15)));
				kColl.put("mon_aft_9", getCellValue(row.getCell(16)));
				kColl.put("mon_aft_10", getCellValue(row.getCell(17)));
				kColl.put("mon_aft_11", getCellValue(row.getCell(18)));
				kColl.put("mon_aft_12", getCellValue(row.getCell(19)));
				iColl.add(kColl);
				
				/*	取制表人和日期	*/
				String charter = null;		//制表人
				String chart_date = null;	//日期
				while(true){
					row = Sheet.getRow(startRow++);
					int i=0;
					for(;i<row.getLastCellNum();i++){
						if(getCellValue(row.getCell(i)).contains("制表人")){
							break;
						}
					}
					if(i < row.getLastCellNum()){
						charter = getCellValue(row.getCell(++i));
						chart_date = getCellValue(row.getCell(i+2));
						break;
					}
					
				}
				
				/*	获取月份信息	*/
				startRow = 7;
				row = Sheet.getRow(startRow);
				int i = 1;
				HSSFCell cell = null;
				StringBuilder monthHeads = new StringBuilder();
				String eachMonth = null;
				while(true){
					cell = row.getCell(i++);
					if ("".equals(cell.toString())){
						break;
					}else{
						eachMonth = getCellValue(cell);
						if("".equals(eachMonth.trim())){
							EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取月份信息为空！");
							throw new EMPException("获取月份信息为空！");
						}
						monthHeads.append(eachMonth).append(";");
					}
				}
				if(monthHeads.length() > 0){
					monthHeads = new StringBuilder(monthHeads.substring(0, monthHeads.length()-1));
				}else{
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取月份信息失败！");
					throw new EMPException("获取月份信息失败！");
				}
				
				String[] monthsArray = monthHeads.toString().split(";");
				for(int k=0;k<monthsArray.length;++k){
					monthsArray[k] = getYearAndMonth(monthsArray[k]);
				}
				
				/*	获取表头信息	*/
				startRow = 3;
				row = Sheet.getRow(startRow);
				String headInfo = getCellValue(row.getCell(0));
				
				String[] headInfoArray = getHeadInfo(headInfo);
				String application = headInfoArray[0];
				String dateString = headInfoArray[1];
				
				kColl = new KeyedCollection();
				kColl.put("category", "28");//表信息——19
				kColl.put("category_desc", "制表人和日期");
				kColl.put("charter", charter);
				kColl.put("chart_date", chart_date);
				kColl.put("application", application);
				kColl.put("apply_date", dateString);
				kColl.put("mon_pre_5", monthsArray[0]);
				kColl.put("mon_pre_4", monthsArray[1]);
				kColl.put("mon_pre_3", monthsArray[2]);
				kColl.put("mon_pre_2", monthsArray[3]);
				kColl.put("mon_pre_1", monthsArray[4]);
				kColl.put("mon_pre_0", monthsArray[5]);
				kColl.put("mon_aft_0", monthsArray[6]);
				kColl.put("mon_aft_1", monthsArray[7]);
				kColl.put("mon_aft_2", monthsArray[8]);
				kColl.put("mon_aft_3", monthsArray[9]);
				kColl.put("mon_aft_4", monthsArray[10]);
				kColl.put("mon_aft_5", monthsArray[11]);
				kColl.put("mon_aft_6", monthsArray[12]);
				kColl.put("mon_aft_7", monthsArray[13]);
				kColl.put("mon_aft_8", monthsArray[14]);
				kColl.put("mon_aft_9", monthsArray[15]);
				kColl.put("mon_aft_10", monthsArray[16]);
				kColl.put("mon_aft_11", monthsArray[17]);
				kColl.put("mon_aft_12", monthsArray[18]);
				iColl.add(kColl);
			}
			
			/*	插入数据	*/
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncCf");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！");
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件io异常！");
			throw new EMPException("文件io异常！");
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！");
		}
		
	}
	
	/**
	 * 导入抵好贷产品调查表数据
	 * @param filePath
	 * @param connection
	 * @param dao
	 * @throws EMPException
	 */
	public void importInvestigationTableData(String filePath,String serno,Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection mainIColl = new IndexedCollection();
		IndexedCollection mtgIColl = new IndexedCollection();
		IndexedCollection partyIColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));

			int numSheets = 0;
			HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				
				/* 定位到【负责信贷员】所在行的下一行	*/
				int startRow = 0;
				HSSFRow row = null;
				String inser = Sheet.getRow(0).getCell(0).toString();
				if(inser.indexOf("抵好贷")==-1){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
					throw new EMPException("导入格式错误!");
				}
				while(true){
					
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("负责信贷员")){
						break;
					}
				}
				
				kColl = new KeyedCollection();
				
				/*	负责信贷员和制表日期	*/
				String headInfo = getCellValue(row.getCell(0));
				String chargeMan = headInfo.substring(headInfo.indexOf("负责信贷员")+6, headInfo.indexOf("制表日期")).trim();
				String chartDate = headInfo.substring(headInfo.indexOf("制表日期")+5, headInfo.indexOf("单位")).trim();
				
				kColl.put("charge_man", chargeMan);
				kColl.put("chart_date", chartDate);
				
				
				/*	当前行位于【贷款申请】所在行，下移一行到【申请金额】	*/
				++startRow;
				
				row = Sheet.getRow(startRow++);
				kColl.put("apply_amount", getCellValue(row.getCell(1)));//申请金额
				kColl.put("apply_term", getCellValue(row.getCell(3)));//贷款期限
				kColl.put("repayment_mode", getCellValue(row.getCell(5)));//还款方式
				kColl.put("apply_date", getCellValue(row.getCell(7)));//申请日期
				
				row = Sheet.getRow(startRow++);
				kColl.put("loan_aim", getCellValue(row.getCell(1)));//贷款用途
				
				/*	当前行位于【基本情况】，下移一行	*/
				++startRow;
				
				row = Sheet.getRow(startRow++);
				kColl.put("apply_name", getCellValue(row.getCell(1)));//借款人名称
				kColl.put("employee_num", getCellValue(row.getCell(4)));//雇员人数
				kColl.put("gender", getCellValue(row.getCell(7)));//性别
				
				row = Sheet.getRow(startRow++);
				kColl.put("education", getCellValue(row.getCell(1)));//文化程度
				kColl.put("project", getCellValue(row.getCell(4)));//经营项目
				kColl.put("prj_period", getCellValue(row.getCell(7)));//经营年限
				
				row = Sheet.getRow(startRow++);
				kColl.put("house_info", getCellValue(row.getCell(1)));//房产情况
				kColl.put("current_address", getCellValue(row.getCell(4)));//现住址

				row = Sheet.getRow(startRow++);
				kColl.put("family_info", getCellValue(row.getCell(1)));//家庭情况
				
				/*	当前行位于【基本情况】，下移一行	*/
				++startRow;
				
				row = Sheet.getRow(startRow++);
				kColl.put("current_deposit", getCellValue(row.getCell(3)));//现金及银行储蓄
				kColl.put("account_payable", getCellValue(row.getCell(6)));//应付账款
				
				row = Sheet.getRow(startRow++);
				kColl.put("accounts_receivable", getCellValue(row.getCell(3)));//应收账款
				kColl.put("account_prereceivable", getCellValue(row.getCell(6)));//预收账款
				
				row = Sheet.getRow(startRow++);
				kColl.put("account_prepayable", getCellValue(row.getCell(3)));//预付账款
				kColl.put("short_loan", getCellValue(row.getCell(6)));//短期借款
				
				row = Sheet.getRow(startRow++);
				kColl.put("stock", getCellValue(row.getCell(3)));//存货
				
				row = Sheet.getRow(startRow++);
				kColl.put("total_flow_debt", getCellValue(row.getCell(6)));//流动负债合计
				
				row = Sheet.getRow(startRow++);
				kColl.put("total_flow_assets", getCellValue(row.getCell(3)));//流动资产合计
				kColl.put("long_loan", getCellValue(row.getCell(6)));//长期借款
				
				row = Sheet.getRow(startRow++);
				kColl.put("fixed_assets", getCellValue(row.getCell(3)));//固定资产
				kColl.put("total_debt", getCellValue(row.getCell(6)));//负债合计 
				
				row = Sheet.getRow(startRow++);
				kColl.put("other_run_assets", getCellValue(row.getCell(3)));//其他经营资产
				kColl.put("owner_equity", getCellValue(row.getCell(6)));//所有者权益 
				
				row = Sheet.getRow(startRow++);
				kColl.put("total_assets", getCellValue(row.getCell(3)));//资产合计
				kColl.put("debt_equity", getCellValue(row.getCell(6)));//负债加权益
				
				row = Sheet.getRow(startRow++);
				kColl.put("other_nontable_assets", getCellValue(row.getCell(3)));//其他非表内资产
				kColl.put("other_nontable_debt", getCellValue(row.getCell(6)));//其他非表内负债
				
				/*	当前行位于【损益情况分析】，下移两行	*/
				startRow+=2;
				
				row = Sheet.getRow(startRow++);
				kColl.put("mon_12", getCellValue(row.getCell(1)));
				kColl.put("mon_11", getCellValue(row.getCell(2)));
				kColl.put("mon_10", getCellValue(row.getCell(3)));
				kColl.put("mon_9", getCellValue(row.getCell(4)));
				kColl.put("mon_8", getCellValue(row.getCell(5)));
				kColl.put("mon_7", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				
				startRow++;
				
				row = Sheet.getRow(startRow++);
				kColl.put("mon_5", getCellValue(row.getCell(1)));
				kColl.put("mon_4", getCellValue(row.getCell(2)));
				kColl.put("mon_3", getCellValue(row.getCell(3)));
				kColl.put("mon_2", getCellValue(row.getCell(4)));
				kColl.put("mon_1", getCellValue(row.getCell(5)));
				kColl.put("mon_sum", getCellValue(row.getCell(6)));
				kColl.put("mon_avg", getCellValue(row.getCell(7)));
				
				row = Sheet.getRow(startRow++);
				
				String rate = getCellValue(row.getCell(1)).trim();
                 if(rate.indexOf("%")!=-1){
                	 rate = "0";
                 }
			  
			
				kColl.put("gross_rate", rate);//毛利率
				String net= getCellValue(row.getCell(5)).trim();
			
				  if(net.indexOf("%")!=-1){
	                	 net = "0";
	                 }
				  
				kColl.put("net_rate",net );//净利率
				
				/*	当前行位于【费用】，下移一行	*/
				++startRow;
				
				row = Sheet.getRow(startRow++);
				kColl.put("rent", getCellValue(row.getCell(1)));//租金
				kColl.put("fee_sum", getCellValue(row.getCell(4)));//费用合计
				String other_income_analyse=getCellValue(row.getCell(6)).trim();
				other_income_analyse = other_income_analyse.substring(9);
				kColl.put("other_income_analyse", other_income_analyse);
				
				row = Sheet.getRow(startRow++);
				kColl.put("wage", getCellValue(row.getCell(1)));//工资
				kColl.put("family_spending", getCellValue(row.getCell(4)));//家庭开支
				
				row = Sheet.getRow(startRow++);
				kColl.put("traff", getCellValue(row.getCell(1)));//交通
				kColl.put("other_income", getCellValue(row.getCell(4)));//其他收入
				
				row = Sheet.getRow(startRow++);
				kColl.put("phone", getCellValue(row.getCell(1)));//电话
				
				row = Sheet.getRow(startRow++);
				kColl.put("national_local_tax", getCellValue(row.getCell(1)));//国地税
				
				row = Sheet.getRow(startRow++);
				kColl.put("other", getCellValue(row.getCell(1)));//其他
				kColl.put("mon_dominate_income", getCellValue(row.getCell(4)));//月可支配收入
				
				/*	定位到【经营历史及现状】的下一行	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("经营历史")){
						break;
					}
				}
				row = Sheet.getRow(startRow++);
				kColl.put("history_status", getCellValue(row.getCell(0)));
				
				/*	定位到【权益的交叉检验】的下一行	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("权益的交叉检验")){
						break;
					}
				}
				row = Sheet.getRow(startRow++);
				kColl.put("equity_check", getCellValue(row.getCell(0)));
				
				/*	定位到【抵押物描述】的下一行	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("抵押物描述")){
						break;
					}
				}
				KeyedCollection mtgKcoll1 = new KeyedCollection();
				KeyedCollection mtgKcoll2 = new KeyedCollection();
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("owner", getCellValue(row.getCell(1)));//所有者
				mtgKcoll2.put("owner", getCellValue(row.getCell(4)));//所有者
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("address", getCellValue(row.getCell(1)));//地址
				mtgKcoll2.put("address", getCellValue(row.getCell(4)));//地址
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("current_status", getCellValue(row.getCell(1)));//目前使用状态
				mtgKcoll2.put("current_status", getCellValue(row.getCell(4)));//目前使用状态
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("build_area", getCellValue(row.getCell(1)));//建筑面积
				mtgKcoll2.put("build_area", getCellValue(row.getCell(4)));//建筑面积
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("house_type", getCellValue(row.getCell(1)));//房屋性质
				mtgKcoll2.put("house_type", getCellValue(row.getCell(4)));//房屋性质
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("used_time", getCellValue(row.getCell(1)));//使用年限
				mtgKcoll2.put("used_time", getCellValue(row.getCell(4)));//使用年限
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("market_value", getCellValue(row.getCell(1)));//市场价值
				mtgKcoll2.put("market_value", getCellValue(row.getCell(4)));//市场价值
				row = Sheet.getRow(startRow++);
				mtgKcoll1.put("bank_evaluation", getCellValue(row.getCell(1)));//银行估价
				mtgKcoll2.put("bank_evaluation", getCellValue(row.getCell(4)));//银行估价
				
				mtgIColl.add(mtgKcoll1);
				mtgIColl.add(mtgKcoll2);
				
				
				/*	当前行位于【贷款业务当事人】，下移两行	*/
				startRow+=2;
				
				/*	贷款业务当事人	*/
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().indexOf("备注") != -1){
						break;			//end of this block
					}else{
						KeyedCollection kColl1 = new KeyedCollection();
						kColl1.put("name", getCellValue(row.getCell(0)));
						kColl1.put("relation_type", getCellValue(row.getCell(1)));
						kColl1.put("company_position", getCellValue(row.getCell(2)));
						kColl1.put("income", getCellValue(row.getCell(5)));
						System.out.println(getCellValue(row.getCell(6)));
						kColl1.put("phone", getCellValue(row.getCell(6)));
						partyIColl.add(kColl1);
					}
				}
				
				/*	当前行位于【备注】所在行的下一行	*/
				--startRow;
				StringBuilder memo = new StringBuilder();
				for(int i = 0;i<6;++i){
					row = Sheet.getRow(startRow++);
					for(int j = 0;j<row.getLastCellNum();++j){
						memo.append(getCellValue(row.getCell(j)));
					}
				}
				String memoString = memo.toString().trim();
				memoString = memoString.substring(3);
				
				kColl.put("memo", memoString);//备注
				mainIColl.add(kColl);
				
			}
			
			/*	插入数据	*/
			for (int i = 0; i < mainIColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) mainIColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncDi");
				dao.insert(kColl1, connection);
			}
			for (int i = 0; i < mtgIColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) mtgIColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncDiMtg");
				dao.insert(kColl1, connection);
			}
			for (int i = 0; i < partyIColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) partyIColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncDiParty");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！",e);
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件io异常！");
			throw new EMPException("文件io异常！",e);
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！",e);
		}
		
	}
	
	/**
	 * 导入种植情况调查表数据
	 * @param filePath
	 * @param connection
	 * @param dao
	 * @throws EMPException
	 */
	public void importPlantingInvestigationTableData(String filePath,String serno, Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection iColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));

			int numSheets = 0;
			HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				
				/* 定位到【过去一年种植情况】所在行的下一行	*/
				int startRow = 0;
				HSSFRow row = null;
				String paln = Sheet.getRow(0).getCell(0).toString();
				if(paln.indexOf("种植")==-1){
						EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
						throw new EMPException("导入格式错误!");
				}
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("过去一年种植情况")){
						break;
					}
				}
				
				/*	当前行位于【种植物名称】所在行	*/
				kColl = new KeyedCollection();
				row = Sheet.getRow(startRow++);
				
				kColl.put("category", "01");//非成本和未来一年预计种植收入情况的记录
				kColl.put("item_name", getCellValue(row.getCell(0)));//种植物名称
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	种植场地	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", "种植场地");//明地、工棚、温室
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	种植亩数	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	播种时间	*/	
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	当前行位于【名称】，下移一行	*/
				++startRow;
				/*	主要成本	*/
				while (true) {
					row = Sheet.getRow(startRow++);
					if(HSSFCell.CELL_TYPE_BLANK==row.getCell(1).getCellType() || "".equals(getCellValue(row.getCell(1)))){
						continue;
					}else if (getCellValue(row.getCell(1)).contains("小计")){
						break;
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "02");//主要成本 
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						iColl.add(kColl);
					}
				}
				/*	定位到【小计】下一行	*/
				--startRow;
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().contains("小计")){
						break;
					}
				}
				/*	收成时间	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	产量/亩	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	销售时间	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	销售收入/亩	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	毛利/亩	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	年收入	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				iColl.add(kColl);
				
				/*	当前行位于【种植收入合计】，下移一行	*/
				++startRow;
				
				/*	未来一年预计种植收入情况	*/
				while (true) {
					row = Sheet.getRow(startRow++);
					if(HSSFCell.CELL_TYPE_BLANK==row.getCell(1).getCellType() || row.getCell(1).toString().contains("预计收入合计")){
						break;
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "03");//未来一年预计种植收入情况
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						iColl.add(kColl);
					}
				}
				
				/*	取制表人和日期	*/
				String tableFiller = null;		//填表人
				String fillingDate = null;	//日期
				while(true){
					row = Sheet.getRow(startRow++);
					int i=0;
					for(;i<row.getLastCellNum();i++){
						if(getCellValue(row.getCell(i)).contains("填表人")){
							break;
						}
					}
					if(i < row.getLastCellNum()){
						tableFiller = getCellValue(row.getCell(++i));
						fillingDate = getCellValue(row.getCell(i+2));
						break;
					}
					
				}
				
				kColl = new KeyedCollection();
				kColl.put("category", "04");//表信息
				kColl.put("table_filler", tableFiller);//填表人
				kColl.put("filling_date", fillingDate);//日期
				iColl.add(kColl);
				
			}
			
			/*	插入数据	*/
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncPlant");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！");
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件io异常！");
			throw new EMPException("文件io异常！");
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！");
		}
		
	}
	/**
	 * 导入养殖情况调查表数据
	 * @param filePath
	 * @param connection
	 * @param dao
	 * @throws EMPException
	 */
	public void importBreedingInvestigationTableData(String filePath,String serno, Connection connection,TableModelDAO dao) throws EMPException{
		
		/**
		 * 开始处理数据导入
		 */
		KeyedCollection kColl = null;
		IndexedCollection iColl = new IndexedCollection();
		
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));

			int numSheets = 0;
			HSSFSheet Sheet = workbook.getSheetAt(numSheets);
			//HSSFSheet Sheet = workbook.getSheet("sheet name");
			if (Sheet != null) {
				String breed = Sheet.getRow(0).getCell(0).toString();	
				if(breed.indexOf("养殖")==-1){
						EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "导入格式错误！");
						throw new EMPException("导入格式错误!");
				}
				/* 定位到【过去一年养殖情况】所在行的下一行	*/
				int startRow = 0;
				HSSFRow row = null;
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(0)==null || row.getCell(0).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(0).toString())) {
						continue;		//blank row
					}else if(row.getCell(0).toString().contains("过去一年养殖情况")){
						break;
					}
				}
				
				/*	当前行位于【养殖品种】所在行	*/
				kColl = new KeyedCollection();
				row = Sheet.getRow(startRow++);
				
				kColl.put("category", "01");//非 养殖成本和未来一年预计养殖收入情况的记录
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	销售时间	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	销售数量	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	销售收入	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	购买成本	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				
				/*	当前行位于【名称】，下移一行	*/
				++startRow;
				/*	主要养殖成本	*/
				while (true) {
					row = Sheet.getRow(startRow++);
					if(HSSFCell.CELL_TYPE_BLANK==row.getCell(1).getCellType() || "".equals(getCellValue(row.getCell(1)))){
						continue;
					}else if (getCellValue(row.getCell(1)).contains("小计")){
						break;
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "02");//主要成本 
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						kColl.put("mon_7", getCellValue(row.getCell(8)));
						iColl.add(kColl);
					}
				}
				/*	定位到【小计】下一行	*/
				--startRow;
				while(true){
					row = Sheet.getRow(startRow++);
					if (row.getCell(1).getCellType()==HSSFCell.CELL_TYPE_BLANK
							|| "".equals(row.getCell(1).toString())) {
						continue;		//blank row
					}else if(row.getCell(1).toString().contains("小计")){
						break;
					}
				}
				/*	毛利	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	年收入	*/
				row = Sheet.getRow(startRow++);
				kColl = new KeyedCollection();
				kColl.put("category", "01");
				kColl.put("item_name", getCellValue(row.getCell(0)));
				kColl.put("mon_1", getCellValue(row.getCell(2)));
				kColl.put("mon_2", getCellValue(row.getCell(3)));
				kColl.put("mon_3", getCellValue(row.getCell(4)));
				kColl.put("mon_4", getCellValue(row.getCell(5)));
				kColl.put("mon_5", getCellValue(row.getCell(6)));
				kColl.put("mon_6", getCellValue(row.getCell(7)));
				kColl.put("mon_7", getCellValue(row.getCell(8)));
				iColl.add(kColl);
				
				/*	当前行位于【养殖收入合计】，下移一行	*/
				++startRow;
				
				/*	未来一年预计养殖收入情况	*/
				while (true) {
					row = Sheet.getRow(startRow++);
					if(HSSFCell.CELL_TYPE_BLANK==row.getCell(1).getCellType() || row.getCell(1).toString().contains("预计收入合计")){
						break;
					}else{
						kColl = new KeyedCollection();
						kColl.put("category", "03");//未来一年预计养殖收入情况
						kColl.put("item_name", getCellValue(row.getCell(1)));
						kColl.put("mon_1", getCellValue(row.getCell(2)));
						kColl.put("mon_2", getCellValue(row.getCell(3)));
						kColl.put("mon_3", getCellValue(row.getCell(4)));
						kColl.put("mon_4", getCellValue(row.getCell(5)));
						kColl.put("mon_5", getCellValue(row.getCell(6)));
						kColl.put("mon_6", getCellValue(row.getCell(7)));
						kColl.put("mon_7", getCellValue(row.getCell(8)));
						iColl.add(kColl);
					}
				}
				
				/*	取制表人和日期	*/
				String tableFiller = null;		//填表人
				String fillingDate = null;	//日期
				while(true){
					row = Sheet.getRow(startRow++);
					int i=0;
					for(;i<row.getLastCellNum();i++){
						if(getCellValue(row.getCell(i)).contains("填表人")){
							break;
						}
					}
					if(i < row.getLastCellNum()){
						tableFiller = getCellValue(row.getCell(++i));
						fillingDate = getCellValue(row.getCell(i+2));
						break;
					}
					
				}
				
				kColl = new KeyedCollection();
				kColl.put("category", "04");//表信息
				kColl.put("table_filler", tableFiller);//填表人
				kColl.put("filling_date", fillingDate);//日期
				iColl.add(kColl);
				
			}
			
			/*	插入数据	*/
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				kColl1.put("serno", serno);
				kColl1.setName("IqpMeFncBreed");
				dao.insert(kColl1, connection);
			}
			
		} catch (FileNotFoundException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件路径错误！");
			throw new EMPException("文件路径错误！");
		} catch (IOException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "文件io异常！");
			throw new EMPException("文件io异常！");
		} catch (EMPJDBCException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "插入数据失败！");
			throw new EMPException("插入数据失败！");
		}
		
	}
	
	
	/**
	 * 返回单元格的值
	 * @param cell
	 * @return
	 */
	public String getCellValue(HSSFCell cell){
		if(cell == null || "".equals(cell.toString())){
			return "";
		}
		Object cellValue = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if(cell.toString().contains("/") || (cell.toString().contains("-") && cell.toString().trim().indexOf("-")!=0)){
				cellValue = dateFormatter(cell.getDateCellValue());
			}else{
				cellValue = cell.getNumericCellValue();
				if(cellValue.toString().trim().indexOf(".0")!=-1){
					cellValue =	cellValue.toString().trim().substring(0,cellValue.toString().trim().indexOf("."));
				}
				cellValue = cellValue.toString();
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			cellValue=cell.getRichStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			cellValue="";
			break;
		default:
			cellValue = "";
			break;
		}
		return String.valueOf(cellValue);
	}
	/**
	 * 返回日期的字符串表示
	 * @param date
	 * @return
	 */
	public String dateFormatter(java.util.Date date){
		if(date == null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	/**
	 * 获取表头信息：申请人+日期
	 * @param headInfo
	 * @throws EMPException 
	 */
	public String[] getHeadInfo(String headInfo) throws EMPException{
		String application = null;
		String dateString = null;
		try{
			headInfo = headInfo.substring(headInfo.lastIndexOf("-")+1, headInfo.length());
			headInfo=headInfo.trim();
			application = headInfo.substring(0, headInfo.indexOf(" "));
			headInfo = headInfo.substring(headInfo.indexOf(" ")).trim();
			String year = headInfo.substring(0,headInfo.indexOf("年"));
			String month = headInfo.substring(headInfo.indexOf("年")+1,headInfo.indexOf("月"));
			String day = headInfo.substring(headInfo.indexOf("月")+1,headInfo.indexOf("日"));
			dateString = year + "-" + month + "-" + day;
		}catch (Exception e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取表头信息错误，请检查录入格式！");
			throw new EMPException("获取表头信息错误，请检查录入格式！");
		}
		if(application == null || "".equals(application)){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取表头信息错误，请检查录入格式！");
			throw new EMPException("获取表头信息错误，请检查录入格式！");
		}
		if(dateString == null || "".equals(dateString)){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取表头信息错误，请检查录入格式！");
			throw new EMPException("获取表头信息错误，请检查录入格式！");
		}
		
		return new String[]{application,dateString};
	}
	
	public String getYearAndMonth(String date) throws EMPException{
		if(!date.contains("-")){
			return date;
		}
		String[] calender = date.split("-");
		
		if(calender.length != 3){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "日期字符串非xxxx-xx-xx格式！");
			throw new EMPException("日期字符串非xxxx-xx-xx格式！");
		}
		return calender[0]+calender[1];
	}
}
