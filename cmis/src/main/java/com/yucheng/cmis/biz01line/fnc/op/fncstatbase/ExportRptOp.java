package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.dao.ExportXLDao;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelTreat;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SheetVO;
/**
 * 财务报表的导出
 * @author liuxin
 *
 */
public class ExportRptOp extends CMISOperation {
	// 得到联合主键
	private final String pk1 = "cus_id";
	private final String pk2 = "stat_prd_style";
	private final String pk3 = "stat_prd";
	private final String pk4 = "stat_style";//报表口径
	private final String pk5 = "fnc_type";//报表类型
	
	protected String identStr = "  ";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flagInfo = CMISMessage.DEFEAT;
		try{
			connection = this.getConnection(context);
			
			//构件业务处理类
			FncStatCommonComponent fCommonComponent = (FncStatCommonComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.FNCSTATCOMMON, context, connection);
			
			/**
			 * 从context中获取要导出一套报表的基本信息
			 */
			// 取联合主键值
			String cusId = (String) context.getDataValue(pk1); // pk1
			String statPrdStyle = (String) context.getDataValue(pk2); // pk2
			String statPrd = get_6charsTime((String) context.getDataValue(pk3)); // pk
			String statStyle = (String) context.getDataValue(pk4); // pk
			String fncType = (String) context.getDataValue(pk5);
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
			
			List<String> listId = new ArrayList<String>();//样式列表
			
			//财务简表
			if(statSlStyleId != null && statSlStyleId.length() != 0){
				listId.add(statSlStyleId);
			}
			
			// 资产样式编号
			if (statBsStyleId != null && statBsStyleId.length() != 0) {
				listId.add(statBsStyleId);
			}
			// 损益表编号
			if (statPlStyleId != null && statPlStyleId.length() != 0) {
				listId.add(statPlStyleId);
			}
			// 现金流量表编号
			if (statCfStyleId != null && statCfStyleId.length() != 0) {
				listId.add(statCfStyleId);
			}
			
			// 财务指标表编号
			if (statFiStyleId != null && statFiStyleId.length() != 0) {
				listId.add(statFiStyleId);
			}
			//所有者权益
			if(statSoeStyleId != null && statSoeStyleId.length() != 0){
				listId.add(statSoeStyleId);
			}
			
			if (listId.size() == 0) {
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的报表类型为空");
				return PUBConstant.FAIL;
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**
			 * 根据客户编号到对公客户基表中获取客户的"财务报表类型(COM_FIN_REP_TYPE)"
			 */
			String finRepType = fCommonComponent.getComFinRepType(cusId,connection);
			KeyedCollection finRepTypeKcoll = null;
			String finRepTypeName = "";
			if(finRepType!=null&&!finRepType.equals("")){
				finRepTypeKcoll = dao.queryDetail("FncConfTemplate", finRepType, connection);
				finRepTypeName = (String)finRepTypeKcoll.get("fnc_name");
			}
			Map map = new HashMap();
			map.put("cus_id", cusId);
			map.put("stat_prd_style", statPrdStyle);
			map.put("stat_prd", statPrd);
			map.put("stat_style", statStyle);
			map.put("fnc_type", fncType);
			KeyedCollection fncStatBaseKcoll =  dao.queryDetail("FncStatBase", map, connection);
			SInfoUtils.addUSerName(fncStatBaseKcoll, new String[]{"input_id"});
			SInfoUtils.addSOrgName(fncStatBaseKcoll, new String[]{"input_br_id"});
			String editUserName = (String)fncStatBaseKcoll.get("input_id_displayname");//编辑用户
			String editOrganName = (String)fncStatBaseKcoll.get("input_br_id_displayname");//填报机构
			String editDate = (String)fncStatBaseKcoll.get("input_date");//填报日期
 
			FncConfStyles pfncConfStyles_data = null;
			
			// 获取sheets数组和单元格的信息
			SheetVO[] sheets = new SheetVO[listId.size()];
			SheetVO sheet;
			CellVO[][] cells = null;
			CellVO cvo;
			
			for (int iId = 0; iId < listId.size(); iId++) {
				//new一个新的sheet
				sheet = new SheetVO();
				
				String styleId = (String) listId.get(iId);
				//从缓存中获取样式
				FncConfStyles fcs_fromCh = (FncConfStyles) FNCFactory.getFNCInstance(styleId);
				// 得到报表名称
				String tableName = fcs_fromCh.getFncName();
				String disName = fcs_fromCh.getFncConfDisName();
				// 得到报表的类型
				String fncConfTyp = fcs_fromCh.getFncConfTyp();
				//得到该样式的栏位
				int fncCont = fcs_fromCh.getFncConfCotes();
				//数据列数
				int dataCol = fcs_fromCh.getFncConfDataCol();
				
				//获取到带有数据的样式
				pfncConfStyles_data = fCommonComponent.findOneFncConfStyles(cusId,
						statPrdStyle, statPrd, styleId, tableName, fncConfTyp,statStyle);
				
				//得到该样式下的所有的项目的list
				List itemList = fcs_fromCh.getItems();
				List itemList_data = pfncConfStyles_data.getItems();
				for(int m=0;m<itemList.size();m++){
					FncConfDefFormat item_ch = (FncConfDefFormat)itemList.get(m);
					String itemId_ch = item_ch.getItemId();
					for(int n=0;n<itemList_data.size();n++){
						FncConfDefFormat item_data = (FncConfDefFormat)itemList_data.get(n);
						
						String itemId_data = item_data.getItemId();
						if(itemId_ch.equals(itemId_data)){
							item_ch.setData1(item_data.getData1());
							item_ch.setData2(item_data.getData2());
							
							item_ch.setDataA(item_data.getDataA());
							item_ch.setDataB(item_data.getDataB());
							break;
						}
					}
				}

				int row = 0;//标示导出文件的行数
				int clo = fncCont*dataCol+2*fncCont;//标示导出文件的列数
				if(dataCol==8){
					clo = fncCont*dataCol*2+2*fncCont;//标示导出文件的列数
				}
				ExportXLDao exportXLDao = new ExportXLDao();
				int counts = 1;
				
				int firstLAN_Count = 0;//标示栏位1的项目的个数
				int seconfLAN_Count = 0;//标示栏位2的项目的个数
				int thLAN_Count = 0;//标示栏位3的项目的个数
				int fLAN_Count = 0;//标示栏位4的项目的个数
				
				int d = 0;
				int d1 = 0;
				int d2 = 0;
				int d3 = 0;
				/**
				 * 根据栏位计算项目的个数,根据个数最大的来确定导出文件的行数 
				 */
				if(fncCont==1){
					firstLAN_Count = exportXLDao.queryCount(styleId, 1, connection);
					row = firstLAN_Count + 5;
				}else if(fncCont==2){
					firstLAN_Count = exportXLDao.queryCount(styleId, 1, connection);
					seconfLAN_Count = exportXLDao.queryCount(styleId, 2, connection);
					if (firstLAN_Count >= seconfLAN_Count) {
						row = firstLAN_Count + 5;
					} else {
						row = seconfLAN_Count + 5;
						d = seconfLAN_Count - firstLAN_Count;//计算两个栏位的差值
					}
				}else if(fncCont==3){
					firstLAN_Count = exportXLDao.queryCount(styleId, 1, connection);
					seconfLAN_Count = exportXLDao.queryCount(styleId, 2, connection);
					thLAN_Count = exportXLDao.queryCount(styleId, 3, connection);
					if(firstLAN_Count>=seconfLAN_Count && firstLAN_Count>=thLAN_Count){
						row = firstLAN_Count + 5;
						d2 = 0-seconfLAN_Count;
					}else if(seconfLAN_Count>=firstLAN_Count && seconfLAN_Count>=thLAN_Count){
						row = seconfLAN_Count + 5;
						d1 = seconfLAN_Count - firstLAN_Count;
						d2=0-firstLAN_Count;
					}else if(thLAN_Count>=firstLAN_Count && thLAN_Count>=seconfLAN_Count){
						row = thLAN_Count + 5;
						d1 = thLAN_Count - firstLAN_Count;
						d2 = seconfLAN_Count-thLAN_Count - d1;
					}else{
						row = firstLAN_Count + 5;
					}
				}else if(fncCont==4){
					firstLAN_Count = exportXLDao.queryCount(styleId, 1, connection);
					seconfLAN_Count = exportXLDao.queryCount(styleId, 2, connection);
					thLAN_Count = exportXLDao.queryCount(styleId, 3, connection);
					fLAN_Count = exportXLDao.queryCount(styleId, 4, connection);
					if(firstLAN_Count>=seconfLAN_Count && firstLAN_Count>=thLAN_Count
							&& firstLAN_Count>=fLAN_Count){
						row = firstLAN_Count + 5;
						d2 = 0 - seconfLAN_Count;
						d3 = 0 - seconfLAN_Count - thLAN_Count;
					}else if(seconfLAN_Count>=firstLAN_Count && seconfLAN_Count>=thLAN_Count
							&& seconfLAN_Count>=fLAN_Count){
						row = seconfLAN_Count + 5;
						d1 = seconfLAN_Count - firstLAN_Count;
						d2 = 0-firstLAN_Count;
						d3 = 0-firstLAN_Count-thLAN_Count;
					}else if(thLAN_Count>=firstLAN_Count && thLAN_Count>=seconfLAN_Count
							&& thLAN_Count>=fLAN_Count){
						row = thLAN_Count + 5;
						d1 = thLAN_Count-firstLAN_Count;
						d2 = seconfLAN_Count-thLAN_Count;
						d3 = 0-firstLAN_Count-seconfLAN_Count;
					}else if(fLAN_Count>=firstLAN_Count && fLAN_Count>=seconfLAN_Count
							&& fLAN_Count>=thLAN_Count){
						row = fLAN_Count + 5;
						d1 = fLAN_Count - firstLAN_Count;
						d2 = fLAN_Count - seconfLAN_Count-firstLAN_Count;
						d3 = fLAN_Count - thLAN_Count- seconfLAN_Count-firstLAN_Count;
					}else{
						row = firstLAN_Count + 5;
					}
				}else{
					return PUBConstant.FAIL;
				}
				//CellVO 
				cells = new CellVO[row][clo];
				for (int j = 0; j < row; j++) {
					for (int k = 0; k < clo; k++) {
						cvo = new CellVO();
						cvo.cellrownum = j;
						cvo.cellcolnum = k;
						cells[j][k] = cvo;
					}
				}
				//组合文件头
				if (statPrdStyle.equals("1")) {
					cells[0][0].cellvalue = fcs_fromCh.getFncConfDisName()
							+ "（月报）";
				} else if (statPrdStyle.equals("2")) {
					cells[0][0].cellvalue = fcs_fromCh.getFncConfDisName()
							+ "（季报）";
				} else if (statPrdStyle.equals("3")) {
					cells[0][0].cellvalue = fcs_fromCh.getFncConfDisName()
							+ "（半年报）";
				} else if (statPrdStyle.equals("4")) {
					cells[0][0].cellvalue = fcs_fromCh.getFncConfDisName()
							+ "（年报）";
				}
				cells[1][0].cellvalue = statPrd.substring(0, 4) + "年"
				+ statPrd.substring(4, 6) + "月";
				
				
				cells[2][0].cellvalue = "编制单位:" + editOrganName;
				KeyedCollection tmpKcoll = dao.queryDetail("FncConfStyles",styleId, connection);
				String styleName = (String)tmpKcoll.get("fnc_conf_dis_name");//报表样式名称
				String tmpUnit = null;//报表数据单位
				String[] tmpTitle = styleName.split("\\|");
				if(tmpTitle.length==2){
					styleName = tmpTitle[0];
					tmpUnit = tmpTitle[1];
				}else{
					styleName = tmpTitle[0];
					tmpUnit = "单位：元";
				}
				
				cells[2][1].cellvalue = "报表类型:"+finRepType+"-"+styleId;
				
				cells[2][2].cellvalue = tmpUnit;//单位
				
				cells[row-1][clo-2].cellvalue = "填报日期:"+editDate.substring(0, 4) + "年"
				+ editDate.substring(5, 7) + "月"+editDate.substring(8,10)+"日";
				
				cells[row-1][clo-1].cellvalue = "制表人:"+editUserName;
				//根据报表类型形成表头int clo = fncCont*dataCol+2*fncCont;//标示导出文件的列数
				if ("01".equals(fncConfTyp)) {
					for(int c=0;c<fncCont;c++){
						if(c==0){							
							cells[3][0].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][3].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][0].cellvalue = "项目";
							cells[3][1].cellvalue = "行次";
							cells[3][2].cellvalue = "期初数";
							cells[3][3].cellvalue = "期末数";
							
							cells[3][0].cellwidth = 8000;
							cells[3][1].cellwidth = 2000;
							cells[3][2].cellwidth = 4000;
							cells[3][3].cellwidth = 4000;
							
						}else{
							
							cells[3][2*c+c*dataCol].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+3].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][2*c+c*dataCol].cellvalue = "项目";
							cells[3][2*c+c*dataCol+1].cellvalue = "行次";
							cells[3][2*c+c*dataCol+2].cellvalue = "期初数";
							cells[3][2*c+c*dataCol+3].cellvalue = "期末数";
							
							cells[3][2*c+c*dataCol].cellwidth = 8000;
							cells[3][2*c+c*dataCol+1].cellwidth = 2000;
							cells[3][2*c+c*dataCol+2].cellwidth = 4000;
							cells[3][2*c+c*dataCol+3].cellwidth = 4000;
							
						}
					}
				}else if("02".equals(fncConfTyp)){
					for(int c=0;c<fncCont;c++){
						if(c==0){
							
							cells[3][0].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][3].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][0].cellvalue = "项目";
							cells[3][1].cellvalue = "行次";
							cells[3][2].cellvalue = "上年同期数";
							cells[3][3].cellvalue = "本年累计数";
							
							cells[3][0].cellwidth = 8000;
							cells[3][1].cellwidth = 2000;
							cells[3][2].cellwidth = 4000;
							cells[3][3].cellwidth = 4000;							
						}else{
							
							cells[3][2*c+c*dataCol].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+3].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][2*c+c*dataCol].cellvalue = "项目";
							cells[3][2*c+c*dataCol+1].cellvalue = "行次";
							cells[3][2*c+c*dataCol+2].cellvalue = "上年同期数";
							cells[3][2*c+c*dataCol+3].cellvalue = "本年累计数";
							
							cells[3][2*c+c*dataCol].cellwidth = 8000;
							cells[3][2*c+c*dataCol+1].cellwidth = 2000;
							cells[3][2*c+c*dataCol+2].cellwidth = 4000;
							cells[3][2*c+c*dataCol+3].cellwidth = 4000;							
						}
					}
				}else if("03".equals(fncConfTyp)||"06".equals(fncConfTyp)){
					for(int c=0;c<fncCont;c++){
						if(c==0){
							cells[3][0].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][0].cellvalue = "项目";
							cells[3][1].cellvalue = "行次";
							cells[3][2].cellvalue = "金额";
							
							cells[3][0].cellwidth = 8000;
							cells[3][1].cellwidth = 2000;
							cells[3][2].cellwidth = 4000;
														
						}else{
							cells[3][2*c+c*dataCol].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][2*c+c*dataCol].cellvalue = "项目";
							cells[3][2*c+c*dataCol+1].cellvalue = "行次";
							cells[3][2*c+c*dataCol+2].cellvalue = "金额";
							
							cells[3][2*c+c*dataCol].cellwidth = 8000;
							cells[3][2*c+c*dataCol+1].cellwidth = 2000;
							cells[3][2*c+c*dataCol+2].cellwidth = 4000;							
						}
					}
				}else if("04".equals(fncConfTyp)){
					for(int c=0;c<fncCont;c++){
						if(c==0){
							cells[3][0].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][0].cellvalue = "项目";
							cells[3][1].cellvalue = "行次";
							cells[3][2].cellvalue = "指标比率(%)";
							
							cells[3][0].cellwidth = 8000;
							cells[3][1].cellwidth = 2000;
							cells[3][2].cellwidth = 4000;							
						}else{
							cells[3][2*c+c*dataCol].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+1].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							cells[3][2*c+c*dataCol+2].cellbgcolor = CellVO.LIGHT_TURQUOISE;
							
							cells[3][2*c+c*dataCol].cellvalue = "项目";
							cells[3][2*c+c*dataCol+1].cellvalue = "行次";
							cells[3][2*c+c*dataCol+2].cellvalue = "指标比率(%)";
							
							cells[3][2*c+c*dataCol].cellwidth = 8000;
							cells[3][2*c+c*dataCol+1].cellwidth = 2000;
							cells[3][2*c+c*dataCol+2].cellwidth = 4000;								
						}
					}
				}else if("05".equals(fncConfTyp)){
					for(int c=0;c<fncCont;c++){
						if(c==0){
							cells[3][0].cellvalue = "项目";
							cells[3][0].cellrownum = 2;
							cells[3][1].cellvalue = "行次";
							cells[3][1].cellrownum = 2;
							//cells[2][2].cellvalue = "上年同期数";
							cells[3][2].cellvalue = "本年累计数";
							cells[3][2].cellcolnum = 7;
							//cells[2][9].cellvalue = "本年累计数";
							cells[3][9].cellvalue = "";
							cells[3][9].cellcolnum = 7;
							
							cells[4][2].cellvalue = "实收资本（股本）";
							cells[4][3].cellvalue = "资本公积";
							cells[4][4].cellvalue = "减：库存股";
							cells[4][5].cellvalue = "盈余公积";
							cells[4][6].cellvalue = "未分配利润";
							cells[4][7].cellvalue = "其他";
							cells[4][8].cellvalue = "所有者权益合计";
							
							/*cells[3][9].cellvalue = "实收资本（股本）";
							cells[3][10].cellvalue = "资本公积";
							cells[3][11].cellvalue = "减：库存股";
							cells[3][12].cellvalue = "盈余公积";
							cells[3][13].cellvalue = "未分配利润";
							cells[3][14].cellvalue = "其他";
							cells[3][15].cellvalue = "所有者权益合计";*/
							
							cells[4][9].cellvalue = "";
							cells[4][10].cellvalue = "";
							cells[4][11].cellvalue = "";
							cells[4][12].cellvalue = "";
							cells[4][13].cellvalue = "";
							cells[4][14].cellvalue = "";
							cells[4][15].cellvalue = "";

						}else{
							int df = dataCol-2; 
							cells[3][c*df+2*fncCont-2].cellvalue = "项目";
							cells[3][c*df+2*fncCont-2].cellrownum = 2;
							cells[3][c*df+2*fncCont-1].cellvalue = "行次";
							cells[3][c*df+2*fncCont-1].cellrownum = 2;
							//cells[2][c*df+2*fncCont].cellvalue = "上年同期数";
							cells[3][c*df+2*fncCont].cellvalue = "本年累计数";
							cells[3][c*df+2*fncCont].cellcolnum = 7;
							//cells[2][c*df+2*fncCont+1].cellvalue = "本年累计数";
							cells[3][c*df+2*fncCont+1].cellvalue = "";
							cells[3][c*df+2*fncCont+1].cellcolnum = 7;
							
							cells[4][c*df+2*fncCont].cellvalue = "实收资本（股本）";
							cells[4][c*df+2*fncCont+1].cellvalue = "资本公积";
							cells[4][c*df+2*fncCont+2].cellvalue = "减：库存股";
							cells[4][c*df+2*fncCont+3].cellvalue = "盈余公积";
							cells[4][c*df+2*fncCont+4].cellvalue = "未分配利润";
							cells[4][c*df+2*fncCont+5].cellvalue = "其他";
							cells[4][c*df+2*fncCont+6].cellvalue = "所有者权益合计";
							
							/*cells[3][c*df+2*fncCont+7].cellvalue = "实收资本（股本）";
							cells[3][c*df+2*fncCont+8].cellvalue = "资本公积";
							cells[3][c*df+2*fncCont+9].cellvalue = "减：库存股";
							cells[3][c*df+2*fncCont+10].cellvalue = "盈余公积";
							cells[3][c*df+2*fncCont+11].cellvalue = "未分配利润";
							cells[3][c*df+2*fncCont+12].cellvalue = "其他";
							cells[3][c*df+2*fncCont+13].cellvalue = "所有者权益合计";*/
							
							cells[4][c*df+2*fncCont+7].cellvalue = "";
							cells[4][c*df+2*fncCont+8].cellvalue = "";
							cells[4][c*df+2*fncCont+9].cellvalue = "";
							cells[4][c*df+2*fncCont+10].cellvalue = "";
							cells[4][c*df+2*fncCont+11].cellvalue = "";
							cells[4][c*df+2*fncCont+12].cellvalue = "";
							cells[4][c*df+2*fncCont+13].cellvalue = "";
						}
					}
				}
				HashMap<String,String> LsFormual = new HashMap<String,String>();
				//开始输入数据行
				for(int i = 0; i < itemList.size(); i++){
						FncConfDefFormat item = (FncConfDefFormat) itemList.get(i); // 为报表的一行
						int itemCote = item.getFncConfCotes(); // 项目的栏位，1，2
						int order = item.getFncConfOrder(); // 顺序编号
						//label，包括label中的前缀和缩进(前缀的个数也要算到缩进的个数里)
						String tempIdentStr = "";
						int indent = item.getFncConfIndent();
						String prefix = item.getFncConfPrefix();
						if(prefix == null){
							prefix = "";
						}	
						
						if(item.getFncConfCalFrm() != null){
						    LsFormual.put(item.getItemId(), item.getFncConfCalFrm());
						}
						for (int idx = 0; idx < indent - prefix.length(); idx++) {// 缩进
							tempIdentStr = this.identStr + tempIdentStr;
						}
						int Typ = Integer.parseInt(item.getFncItemEditTyp());
						if (3 != Typ) {
							DecimalFormat df = new DecimalFormat("#########0.00");
							for(int c=0;c<fncCont;c++){
								if(c==0 && c == itemCote-1){
									if(dataCol==8){
										cells[4 + i][0].cellvalue = tempIdentStr + prefix + item.getItemName() + "||" + item.getItemId();//lm 为通过报表项目ID定位，而临时加上 || itemid
										if("1".equals(item.getFncConfRowFlg())){
											cells[4 + i][1].cellvalue = counts + "";//写入行次
											counts++;
										}
										double[] dataA = item.getDataA();//对应期初数
										double[] dataB = item.getDataB();//对应期末数据
										/*for(int a=0;a<dataA.length;a++){
										if(a!=6){
											if(a==7){
												cells[4 + i][a+2-1].cellvalue = String.valueOf(df.
														format(new BigDecimal(dataA[a])));
												break;
											}else{
												cells[4 + i][a+2].cellvalue = String.valueOf(df.
														format(new BigDecimal(dataA[a])));
											}
											
										}
									}
									for(int b=0;b<dataA.length;b++){
										if(b!=6){
											if(b==7){
												cells[4 + i][b+2+7-1].cellvalue = String.valueOf(df.
														format(new BigDecimal(dataB[b])));
												break;
											}else{
												cells[4 + i][b+2+7].cellvalue = String.valueOf(df.
														format(new BigDecimal(dataB[b])));
											}
										}
									}*/
										for(int a=0;a<dataB.length;a++){
											if(a!=6){
												if(a==7){
													cells[4 + i][a+2-1].celltype = CellVO.CELL_TYPE_NUMERIC;
													cells[4 + i][a+2-1].cellvalue = String.valueOf(df.
															format(new BigDecimal(dataB[a])));
													break;
												}else{
													cells[4 + i][a+2].celltype = CellVO.CELL_TYPE_NUMERIC;
													cells[4 + i][a+2].cellvalue = String.valueOf(df.
															format(new BigDecimal(dataB[a])));
												}
												
											}
										}
										for(int b=0;b<dataA.length;b++){
											if(b!=6){
												if(b==7){
													cells[4 + i][b+2+7-1].cellvalue = "";
													break;
												}else{
													cells[4 + i][b+2+7].cellvalue = "";
												}
											}
										}
										
									}else{
										cells[4 + i][0].cellvalue = tempIdentStr + prefix + item.getItemName() + "||" + item.getItemId();//LM   为通过报表项目ID定位，而临时加上 || itemid
										if("1".equals(item.getFncConfRowFlg())){
											cells[4 + i][1].cellvalue = counts + "";//写入行次
											counts++;
										}
										cells[4 + i][2].celltype = CellVO.CELL_TYPE_NUMERIC;
										cells[4 + i][2].cellvalue = String.valueOf(df.
																format(new BigDecimal(item.getData1())));
										if(dataCol==2){
											cells[4 + i][3].celltype = CellVO.CELL_TYPE_NUMERIC;
											cells[4 + i][3].cellvalue = String.valueOf(df.
																format(new BigDecimal(item.getData2())));
										}
										break;
									}
								}else if(c == itemCote-1){
									if(dataCol==8){
											//默认的只是一栏位
									}else{
										if(fncCont==3 && c==1 && itemCote==2){
											d = d1;
										}else if(fncCont==3 && c==2 && itemCote==3){
											d =d2;
										}
										if(fncCont==4 && c==1 && itemCote==2){
											d = d1;
										}else if(fncCont==4 && c==2 && itemCote==3){
											d =d2;
										}else if(fncCont==4 && c==3 && itemCote==4){
											d =d3;
										}
										cells[4 + i-row+5+d][2*c+c*dataCol].cellvalue = tempIdentStr + prefix + item.getItemName() + "||" + item.getItemId();// 为通过报表项目ID定位，而临时加上 || itemId
										if("1".equals(item.getFncConfRowFlg())){
											cells[4 + i-row+5+d][2*c+c*dataCol+1].cellvalue = counts + "";//写入行次
											counts++;
										}
										cells[4 + i-row+5+d][2*c+c*dataCol+2].celltype = CellVO.CELL_TYPE_NUMERIC;
										cells[4 + i-row+5+d][2*c+c*dataCol+2].cellvalue = String.valueOf(df.
															format(new BigDecimal(item.getData1())));
										if(dataCol==2){
											cells[4 + i-row+5+d][2*c+c*dataCol+3].celltype = CellVO.CELL_TYPE_NUMERIC;
											cells[4 + i-row+5+d][2*c+c*dataCol+3].cellvalue = String.valueOf(df.
															format(new BigDecimal(item.getData2())));
										}
										break;
									}
								}
							}
		
						} else {
							for(int c=0;c<fncCont;c++){
								if(c==0 && c == itemCote-1){
									cells[4 + i][0].cellvalue = tempIdentStr + prefix + item.getItemName() + "||" + item.getItemId();	// 为通过报表项目ID定位，而临时加上  || itemid
									break;
								}else if(c == itemCote-1){
									if(fncCont==3 && c==1){
										d = d1;
									}else if(fncCont==3 && c==2){
										d =d2;
									}
									if(fncCont==4 && c==1){
										d = d1;
									}else if(fncCont==4 && c==2){
										d =d2;
									}else if(fncCont==4 && c==3){
										d =d3;
									}
									cells[4 + i-row+5+d][2*c+c*dataCol].cellvalue = tempIdentStr + prefix + item.getItemName() + "||" + item.getItemId();	// 为通过报表项目ID定位，而临时加上 || itemid
									break;
								}
							}
						}
						/*//扩展的行数
						int appendRow = item.getFncCnfAppRow();
						for (int j = 0; j < appendRow; j++) {
							for(int c=0;c<fncCont;c++){
								if(c==0 && c == itemCote-1){
									cells[4 + i][0].cellvalue = tempIdentStr + prefix + item.getItemName();	
									break;
								}else if(c == itemCote-1){
									cells[4 + i-row+5][dataCol+2+c-1].cellvalue = tempIdentStr + prefix + item.getItemName();	
									break;
								}
							}
						}*/
					}
				
				
				sheet.cells = cells;
				sheet.rownum = row;
				sheet.colnum = clo;
				//sheet.sheetname = "["+finRepType+"|"+styleId+"]"+disName;
				
				sheet.sheetname = this.getSheetNameByFncConfTyp(fncConfTyp);
				sheets[iId] = sheet;
				
				
				for(Iterator<String> itr=LsFormual.keySet().iterator(); itr.hasNext();){
					   String itemId = itr.next();
					   String formual = (String)LsFormual.get(itemId);
					   CellVO _itemCell = this.seekCellByItemId(sheet, itemId);
					   if(_itemCell == null){
						   continue;
					   }					   
					   _itemCell.cellbgcolor = CellVO.CELL_YELLOW;
					   
					   CellVO _formualcell1 = this.seekCellByItemId(sheet, itemId, "1");
					   if(_formualcell1 == null){
						   continue;
					   }
					   _formualcell1.cellbgcolor = CellVO.CELL_YELLOW;
					   
					    String excelFormual = "";
						if("01".equals(fncConfTyp)){
							// "资产负债表";
							
							excelFormual = this.digestCalExp(formual, "1", sheets, iId); //期初
							
							_formualcell1.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell1.cellvalue = excelFormual;
							
							CellVO _formualcell2 = this.seekCellByItemId(sheet, itemId, "2"); //期末
							if(_formualcell2 == null){
								   continue;
							 }
							_formualcell2.cellbgcolor = CellVO.CELL_YELLOW;
							excelFormual = this.digestCalExp(formual, "2", sheets, iId);
							_formualcell2.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell2.cellvalue = excelFormual;
							
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, itemId + ": " + _formualcell2.cellvalue, null);
							
						}else if("02".equals(fncConfTyp)){
							// "损益表";
							excelFormual = this.digestCalExp(formual, "1", sheets, iId); //上年同期
							_formualcell1.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell1.cellvalue = excelFormual;
							
							CellVO _formualcell2 = this.seekCellByItemId(sheet, itemId, "2");//本年累计
							if(_formualcell2 == null){
								   continue;
							 }
							_formualcell2.cellbgcolor = CellVO.CELL_YELLOW;							
							excelFormual = this.digestCalExp(formual, "2", sheets, iId);
							_formualcell2.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell2.cellvalue = excelFormual;
							
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, itemId + ": " + _formualcell2.cellvalue, null);
						}else if("03".equals(fncConfTyp)){
							// "现金流量表";
							excelFormual = this.digestCalExp(formual, "1", sheets, iId);
							_formualcell1.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell1.cellvalue = excelFormual;
							
						}else if("04".equals(fncConfTyp)){
							// "财务指标表";
							excelFormual = this.digestCalExp(formual, "1", sheets, iId);
							_formualcell1.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell1.cellvalue = excelFormual;
							
						}else if("05".equals(fncConfTyp)){
							// "所有者权益表";
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "对所有者权益表尚无处理！！！", null);
						}else if("06".equals(fncConfTyp)){
							//sheet.sheetname = "("+finRepType+"-"+styleId+")"+"财务简表";
							// "财务简表";
							excelFormual = this.digestCalExp(formual, "1", sheets, iId);
							_formualcell1.celltype = HSSFCell.CELL_TYPE_FORMULA;
							_formualcell1.cellvalue = excelFormual;
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "对财务简表尚无处理！！！", null);
						}else{
							EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "未知的报表类型：" + fncConfTyp, null);
						}
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, itemId + ": " + _formualcell1.cellvalue, null);
						
				}
			}
 
			//清理为定位而临时在加的
			this.clearItemIdAtCell(sheets);
			// EXCEL文件指定路径
			String XLSFile = (String) context.getDataValue("address");

			if (XLSFile != null && !XLSFile.equals("")) {
				try {
					XLSFile = new String(XLSFile.getBytes("ISO-8859-1"), "UTF-8")+"/"+System.currentTimeMillis()+".xls";
				} catch (UnsupportedEncodingException e1) {
					EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, e1.toString());
				}
			}else{
				
				ResourceBundle res = ResourceBundle.getBundle("cmis");
							
		        String dir = res.getString("permission.file.path");
		        if(dir != null && dir.toLowerCase().startsWith("classpath:")){
			        URL url =  Thread.currentThread().getContextClassLoader().getResource("");
			        dir = url.getPath().replace("classes", "");		        	
		        }else{
		        	dir = dir.replace("permissions", "");
		        }
		        
		        
		        XLSFile = dir+System.currentTimeMillis()+"temp.xls";
				File file = new File(XLSFile);
				try {
					file.createNewFile();
				} catch (IOException e) {
					EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, e.toString());
				}
			}
			
			//把生成的 文件带路径的 目录放置在  context中 下载页面需要  那这个下载，下载完成之后要删除该文件
			context.put("filePath", XLSFile);
			// 生成一个EXCEL对象
			ExcelVO evo = new ExcelVO();
			evo.sheets = sheets;
			evo.sheetnum = evo.sheets.length;

			boolean b;
			try {
				b = ExcelTreat.writeExcel(evo, XLSFile);
				
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, e.toString());
			}

			
		}catch(CMISException e){
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw e;
		}catch(Exception e){
	                throw new EMPException(e);
	        } finally {
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
	
	/**
	 * 将数据库中的计算公式配置转换成EXCEL格式的公式
	 * @param sheetName  公式中表格Sheet名
	 * @param ItemId     报表项ID ，其值为在FNC_CONF_ITEMS中定义的项
	 * @param SubitemId  报表子项ID（例如：货币资金.期初数）,其值为 1、2、...
	 * @param sheet     目标EXCEL表格
	 * @return 转换后的EXCEL公式
	 */
	private String transCalExp2ExcelFormula(String fncConfTyp, String sheetName, 
			String ItemId, String SubitemId, SheetVO[] sheet, int curSheet) throws EMPException {
		
		int sheetIdx = -1;
		int statSlIdx = -1;//财务简表
		int statBsIdx = -1;//资产负债表
		int statIsIdx = -1;//损益表
		int statCfsIdx = -1;//现金流量表
		int indexRpt = -1;//财务指标表
		int statSoeRpt = -1;//所有权益表
		
		if(sheet.length==4){
			statBsIdx = 0;
			statIsIdx = 1;
			statCfsIdx = 2;
			indexRpt = 3;
		}
		if(sheet.length==5){
			statSlIdx = 0;
			statBsIdx = 1;
			statIsIdx = 2;
			statCfsIdx = 3;
			indexRpt = 4;
			
		}
		
		if("01".equals(fncConfTyp)){
			// "资产负债表";
			sheetIdx = statBsIdx;
		}else if("02".equals(fncConfTyp)){
			// "损益表";
			sheetIdx = statIsIdx;
		}else if("03".equals(fncConfTyp)){
			// "现金流量表";
			sheetIdx = statCfsIdx;
		}else if("04".equals(fncConfTyp)){
			// "财务指标表";
			sheetIdx = indexRpt;
		}else if("05".equals(fncConfTyp)){
			// "所有者权益表";
			sheetIdx = 5;
		}else if("06".equals(fncConfTyp)){
			// "财务简表";
			sheetIdx = statSlIdx;
		}else{
		    sheetIdx = curSheet;
		}
		
		
		/**  根据报表项ID与报表子项ID确定当前公式所在Sheets中的Cell */
		//根据报表项ID得到相对位置
		CellVO itemCell = this.seekCellByItemId(sheet[sheetIdx], ItemId);
		if(itemCell == null){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "未能根据报表项ID【" + ItemId + "】在【" + sheetName + "】中定位到报表项", null);
			return null;
		}
		
		int row = itemCell.cellrownum + 1;
		int col = itemCell.cellcolnum + 1;
		
		//将0、1、2、...转成对应Excel的A、B、C、....   注：AB、AC...  这种格式不支持
		col += Integer.parseInt(SubitemId);
		char col4Excel = 'A' ;
		col4Excel += col;
		
		if(sheetName == null ||  sheetName.trim().equals("")){
		   return col4Excel + "" + row;
		} else {
		   return "'"+ sheetName+"'!" + col4Excel + "" + row;
		}
	}
	
	/**
	 * 根据报表项ID，在EXCEL中定位其对应的CELL（对应EXCEL中 ‘项目’ 所在的CELL）
	 * @param sheet 表单SHEET
	 * @param ItemId 项目ID（在FNC_CONF_ITEM）
	 * @return 对应EXCEL中 ‘项目’ 所在的CELL
	 */
	private CellVO seekCellByItemId(SheetVO sheet, String ItemId){
		
		for(int row=0; row<sheet.cells.length; row++){
			for(int col=0; col<sheet.cells[row].length; col++){
				
				if(sheet.cells[row][col].cellvalue != null 
						&& ((String)sheet.cells[row][col].cellvalue).endsWith(ItemId)){
					
					
					return sheet.cells[row][col];
				}
			}
		}
		///定位失败
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0, "通过" + ItemId + "在" + sheet.sheetname + "中定位失败", null);
		return null;
	}	
	
	/**
	 * 根据报表项ID，在EXCEL中定位其对应的CELL（对应EXCEL中 ‘项目’ 所在的CELL）
	 * @param sheet 表单SHEET
	 * @param ItemId 项目ID（在FNC_CONF_ITEM）
	 * @return 对应EXCEL中 ‘项目’ 所在的CELL
	 */	
	private CellVO seekCellByItemId(SheetVO sheet, String ItemId, String SubItemId){
	
		for(int row=0; row<sheet.cells.length; row++){
			for(int col=0; col<sheet.cells[row].length; col++){
				
				if(sheet.cells[row][col].cellvalue != null 
						&& ((String)sheet.cells[row][col].cellvalue).endsWith(ItemId)){
					
					
					return sheet.cells[row][col + 1 + Integer.parseInt(SubItemId)];
				}
			}
		}
		///定位失败
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "通过" + ItemId + "与" + SubItemId + "在" + sheet.sheetname + "中定位失败", null);
		return null;		
	}
	
	/**
	 * 清除在EXCEL的所有SHEET中‘项目’中的itemId（为通过itemId定位时加的）
	 * @param sheets
	 */
	private void clearItemIdAtCell(SheetVO[] sheets){
		
		for(int s=0; s<sheets.length; s++){
			SheetVO _sheet = sheets[s];
			for(int row=0; row<_sheet.cells.length; row++){
				for(int col=0; col<_sheet.cells[row].length; col++){
					
					if(_sheet.cells[row][col].cellvalue != null 
							&& ((String)_sheet.cells[row][col].cellvalue).lastIndexOf("||")> 0){
						int _idx = ((String)_sheet.cells[row][col].cellvalue).lastIndexOf("||");
						String _val = (String)_sheet.cells[row][col].cellvalue;
						_sheet.cells[row][col].cellvalue = _val.substring(0,_idx);
					}
				}
			}
		}
 
	}	
	
	//解释以下表达式
	//{[L08010000]可供投资者分配的利润}-{[L08020000]应付优先股股利}-{[L08030000]提取任意盈余公积}-{[L08040000]应付普通股股利}-{[L08050000]转作资本的普通股股利}-{[L08060000]其他}
	//或者
	//{[03]现金流量.[XX7030100]现金的期末余额.[1]金额}-{[03]现金流量.[XX7030200]现金的期初余额.[1]金额}+{[03]现金流量.[XX7030300]现金等价物的期末余额.[1]金额}-{[03]现金流量.[XX7030400]现金等价物的期初余额.[1]金额}
	public String digestCalExp(String CalExp, String subItemId, SheetVO[] sheet, int curSheet) throws EMPException{
		StringBuffer _retExp = new StringBuffer(); 
		/** 分解并得到表达式中每个项 */
		//String[] _Exp = CalExp.split(""); //jdk1.8对比jdk1.7、jdk1.6空字符串截取得出结果不同，jdk1.6、jdk1.7会在字符串开头添加空字符串得出的长度会比jdk8大1
		char[] ch = CalExp.toCharArray();		
		String[] _Exp = new String[ch.length];
		for(int i=0;i<_Exp.length;i++){
			_Exp[i] = String.valueOf(ch[i]);
		}
		for(int n=0; n<_Exp.length; n++){
			
			if(_Exp[n].equals("{")){
				int _r = CalExp.indexOf("}", n);
				String itemExp = CalExp.substring(n,_r);
 
				String fncConfTyp = peekFncConfTyp(itemExp);
	 
				String sheetName = "";
				if(fncConfTyp != null && !fncConfTyp.trim().equals("")){
				   sheetName = this.getSheetNameByFncConfTyp(fncConfTyp);
				}else{
					fncConfTyp = "";
				}
								
				String itemId = peekItemId(itemExp);
 
				String _subItemId = peekSubItemId(itemExp);
                if(_subItemId != null && !_subItemId.trim().equals("")){ //公式中有指定则用优先用公式中
                	subItemId = _subItemId;
                }
				
				String excelExp = this.transCalExp2ExcelFormula(fncConfTyp, sheetName, itemId, subItemId, sheet, curSheet);
				if(excelExp == null || excelExp.trim().equals("")){
					//如果有一个公式项转换失败，则整个公式转换失败
					return null;
				}
				_retExp.append(excelExp);
				n=_r;
			}else{
				_retExp.append(_Exp[n]);
			}
		}
		
		return _retExp.toString();
	}
	
    private static String peekFncConfTyp(String ItemExp){
    	
    	String[] items = ItemExp.split("\\.");
    	if(items.length == 3){
    		return items[0].substring(items[0].indexOf("[")+1, items[0].indexOf("]"));
    	} else if(items.length == 1){
    		return "";
    	}
    	
    	return null;
    }
	
    private static String peekItemId(String ItemExp){
    	
    	String[] items = ItemExp.split("\\.");
    	if(items.length == 3){
    		return items[1].substring(items[1].indexOf("[")+1, items[1].indexOf("]"));
    	}else if(items.length == 1){
    		return items[0].substring(items[0].indexOf("[")+1, items[0].indexOf("]"));
    	}
    	
    	return null;
    }    
    
    private static String peekSubItemId(String ItemExp){
    	
    	String[] items = ItemExp.split("\\.");
    	if(items.length == 3 ){
    		return items[2].substring(items[2].indexOf("[")+1, items[2].indexOf("]"));
    	} else if(items.length == 1){
    		return "";
    	}
    	
    	return null;
    }
    
    private String getSheetNameByFncConfTyp(String fncConfTyp){
		if("01".equals(fncConfTyp)){
			return "资产负债表";
		}else if("02".equals(fncConfTyp)){
			return "损益表";
		}else if("03".equals(fncConfTyp)){
			return "现金流量表";
		}else if("04".equals(fncConfTyp)){
			return "财务指标表";
		}else if("05".equals(fncConfTyp)){
			return "所有者权益表";
		}else if("06".equals(fncConfTyp)){
			//sheet.sheetname = "("+finRepType+"-"+styleId+")"+"财务简表";
			return "财务简表";
		}else{
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "未知的报表类型：" + fncConfTyp, null);
			return null;
		}
    }
    
	public static void main(String[] args) {
		
	}
}
