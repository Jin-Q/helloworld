package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;

public class FNCQuery{

	public FNCQuery(){
		
	}
	
	
	/**
	 * 读取数据库中的报表科目(FNC_CONF_ITEMS)中的所有报表的定义信息
	 * @param context
	 * @param conn
	 * @return
	 */
	public Map<String, FncConfItems4Query> getItemsFromDB(Context context, Connection conn){

		Map<String, FncConfItems4Query> fncMap = new HashMap<String, FncConfItems4Query>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据START..." );
			String sql = "select ITEM_ID,ITEM_NAME,FNC_CONF_TYP,FNC_NO_FLG,REMARK,FORMULA,ITEM_UNIT " +
						 "from FNC_CONF_ITEMS";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"为什么加载不了ITEM?");
			while(rs.next()){
				FncConfItems4Query fi = new FncConfItems4Query();
				fi.setFncConfTyp(rs.getString("FNC_CONF_TYP"));
				fi.setFncNoFlg(rs.getString("FNC_NO_FLG"));
				fi.setFormula(rs.getString("FORMULA"));
				fi.setItemId(rs.getString("ITEM_ID"));
				fi.setItemName(rs.getString("ITEM_NAME"));
				fi.setRemark(rs.getString("REMARK"));
				fi.setItemUnit(rs.getString("ITEM_UNIT"));
				
			/*	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_NO_FLG:"+rs.getString("FNC_NO_FLG"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FORMULA："+rs.getString("FORMULA"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_ID:"+rs.getString("ITEM_ID"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_NAME:"+rs.getString("ITEM_NAME"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"REMARK:"+rs.getString("REMARK"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_UNIT:"+rs.getString("ITEM_UNIT"));
*/				
				fncMap.put(rs.getString("ITEM_ID"), fi);
			}
		
		
			/*Set<Entry<String, FncConfItems4Query>> set = fncMap.entrySet(); 
			Iterator<Entry<String, FncConfItems4Query>> itor = set.iterator(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************item map *****************");
			while(itor.hasNext()) 
			{ 
			Entry<String, FncConfItems4Query> entry = itor.next(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getItemName()+"---"+(entry.getValue()).getItemId()+"------"+(entry.getValue()).getFormula()); 
			} */
		
			ps.close();
			rs.close();
			//conn.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据失败SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据失败！" );
			e.printStackTrace();

	}
		return fncMap;
	}
	
	
	/**
	 * 读取数据库中的报表样式定义表(FNC_CONF_STYLES)中的所有报表的定义信息
	 * @param context
	 * @param conn
	 * @return
	 */
	public Map<String, FncConfStyles> getAllListFromDB(Context context, Connection conn){

		Map<String, FncConfStyles> fncMap = new HashMap<String, FncConfStyles>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务样式数据START..." );
			
			String sql = "select STYLE_ID,FNC_NAME,FNC_CONF_DIS_NAME,FNC_CONF_TYP,FNC_CONF_DATA_COL,FNC_CONF_COTES, " +
						"HEAD_LEFT,"+   
						"FOOD_RIGHT,"+	
						"FOOD_CENTER,"+	
						"FOOD_LEFT,"+	
						"HEAD_RIGHT,"+	
						"HEAD_CENTER,"+	
						"NO_IND,"+	
						"COM_IND,"+	
						"DATA_DEC1,"+	
						"DATA_DEC2,"+	
						"DATA_DEC3,"+	
						"DATA_DEC4,"+	
						"DATA_DEC5,"+	
						"DATA_DEC6,"+	
						"DATA_DEC7,"+	
						"DATA_DEC8 "+
						" from FNC_CONF_STYLES";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FncConfStyles fs = new FncConfStyles();
				fs.setStyleId(rs.getString(1));
				fs.setFncName(rs.getString(2));
				fs.setFncConfDisName(rs.getString(3));
				fs.setFncConfTyp(rs.getString(4));
				fs.setFncConfDataCol(rs.getInt(5));
				fs.setFncConfCotes(rs.getInt(6));
				fs.setHeadLeft(rs.getString(7));
				fs.setFoodRight(rs.getString(8));
				fs.setFoodCenter(rs.getString(9));
				fs.setFoodLeft(rs.getString(10));
				fs.setHeadRight(rs.getString(11));
				fs.setHeadCenter(rs.getString(12));
				fs.setNoInd(rs.getString(13));
				fs.setComInd(rs.getString(14));
				fs.setDataDec1(rs.getString(15));
				fs.setDataDec2(rs.getString(16));
				fs.setDataDec3(rs.getString(17));
				fs.setDataDec4(rs.getString(18));
				fs.setDataDec5(rs.getString(19));
				fs.setDataDec6(rs.getString(20));
				fs.setDataDec7(rs.getString(21));
				fs.setDataDec8(rs.getString(22));
				//读取数据库中的报表配置定义表中的信息
				List<FncConfDefFormat> list = new ArrayList<FncConfDefFormat>();
				list = getFncConfDefFormatFromDB(context,conn,rs.getString(1));

				fs.setItems(list);
				fncMap.put(rs.getString(1), fs);
			}
			
			ps.close();
			rs.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务样式数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务样式数据失败！" );
				e.printStackTrace();
	
		}
		return fncMap;
	}
	
	/**
	 * 读取数据库中的报表配置定义表(FNC_CONF_DEF_FMT)中的属于"styleID"的报表的所有item的配置信息
	 * 取出来的item在list中的排列顺序是栏位、顺序号
	 * 将结果数据放到list中并返回
	 * @param styleID  报表
	 * @return list
	 */
	public List<FncConfDefFormat> getFncConfDefFormatFromDB(Context context, Connection conn , String styleID){
		
		List<FncConfDefFormat> list = new ArrayList<FncConfDefFormat>();
		PreparedStatement ps = null;
		
		String sql = "select B.STYLE_ID,B.ITEM_ID,B.FNC_CONF_ORDER,B.FNC_CONF_COTES,B.FNC_CONF_ROW_FLG,B.FNC_CONF_INDENT,B.FNC_CONF_PREFIX," +
							"B.FNC_ITEM_EDIT_TYP,B.FNC_CONF_DISP_AMT,B.FNC_CONF_CHK_FRM,B.FNC_CONF_CAL_FRM,B.FNC_CNF_APP_ROW,A.ITEM_NAME " +
					 "from FNC_CONF_ITEMS A,FNC_CONF_DEF_FMT B " +
		             "where A.ITEM_ID = B.ITEM_ID and STYLE_ID = ?  " +
		             "order by B.FNC_CONF_COTES asc, B.FNC_CONF_ORDER asc ";
		
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报样式格式化数据START..." );
			ps = conn.prepareStatement(sql);
			ps.setString(1, styleID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FncConfDefFormat fdf = new FncConfDefFormat();
				fdf.setStyleId(rs.getString(1));
				fdf.setItemId(rs.getString(2));
				fdf.setFncConfOrder(rs.getInt(3));
				fdf.setFncConfCotes(rs.getInt(4));
				fdf.setFncConfRowFlg(rs.getString(5));
				fdf.setFncConfIndent(rs.getInt(6));
				fdf.setFncConfPrefix(rs.getString(7));
				fdf.setFncItemEditTyp(rs.getString(8));
				fdf.setFncConfDispAmt(rs.getDouble(9));
				fdf.setFncConfChkFrm(rs.getString(10));
				fdf.setFncConfCalFrm(rs.getString(11));
				fdf.setFncCnfAppRow(rs.getInt(12));
				fdf.setItemName(rs.getString(13));
				list.add(fdf);
			}
			
			ps.close();
			rs.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报样式格式化数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报样式格式化数据失败..." );
			e.printStackTrace();
		}
		
		return list;
	}

	/**
	 * 读取数据库中的报表模板(FNC_CONF_TEMPLATE)中的所有报表的定义信息
	 * @param context
	 * @param conn
	 * @return
	 */
	public Map<String, FncConfTemplate> getTemplateFromDB(Context context, Connection conn){

		Map<String, FncConfTemplate> fncMap = new HashMap<String, FncConfTemplate>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报模板数据START..." );
			String sql = "select 			" +
							"	FNC_ID		,"+	
							"	FNC_NAME	,"+ 
							"	FNC_BS_STYLE_ID	,"+ 
							"	FNC_PL_STYLE_ID	,"+ 
							"	FNC_CF_STYLE_ID	,"+ 
							"	FNC_FI_STYLE_ID	,"+ 
							"	FNC_RI_STYLE_ID	,"+ 
							"	FNC_SMP_STYLE_ID,"+ 
							"	FNC_STYLE_ID1	,"+ 
							"	FNC_STYLE_ID2	,"+ 
							"	NO_IND		,"+ 
							"	COM_IND		 "+ 
							"from FNC_CONF_TEMPLATE";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FncConfTemplate fct = new FncConfTemplate();
				fct.setFncId(rs.getString("FNC_ID"));
				fct.setFncName(rs.getString("FNC_NAME"));
				fct.setFncBsStyleId(rs.getString("FNC_BS_STYLE_ID"));
				fct.setFncPlStyleId(rs.getString("FNC_PL_STYLE_ID"));
				fct.setFncCfStyleId(rs.getString("FNC_CF_STYLE_ID"));
				fct.setFncFiStyleId(rs.getString("FNC_FI_STYLE_ID"));
				fct.setFncRiStyleId(rs.getString("FNC_RI_STYLE_ID"));
				fct.setFncSmpStyleId(rs.getString("FNC_SMP_STYLE_ID"));
				fct.setFncStyleId1(rs.getString("FNC_STYLE_ID1"));
				fct.setFncStyleId2(rs.getString("FNC_STYLE_ID2"));
				fct.setNoInd(rs.getString("NO_IND"));
				fct.setComInd(rs.getString("COM_IND"));

				fncMap.put(rs.getString("FNC_ID"), fct);
			}
		/*	for(String o:fncMap.keySet()){
				System.out.println("ComInd***"+fncMap.get(o).getComInd());
				System.out.println("FncBsStyleId***"+fncMap.get(o).getFncBsStyleId());
				System.out.println("FncCfStyleId***"+fncMap.get(o).getFncCfStyleId());
				System.out.println("FncFiStyleId***"+fncMap.get(o).getFncFiStyleId());
				System.out.println("FncId***"+fncMap.get(o).getFncId());
				System.out.println("FncName***"+fncMap.get(o).getFncName());
				System.out.println("FncPlStyleId***"+fncMap.get(o).getFncPlStyleId());
				System.out.println("FncRiStyleId***"+fncMap.get(o).getFncRiStyleId());
				System.out.println("FncSmpStyleId***"+fncMap.get(o).getFncSmpStyleId());
				System.out.println("FncStyleId1***"+fncMap.get(o).getFncStyleId1());
				System.out.println("FncStyleId2***"+fncMap.get(o).getFncStyleId2());
			}
			*/
			ps.close();
			rs.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报模板数据END..." );
			//conn.close();
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财报模板数据失败..." );
				e.printStackTrace();
	
		} catch (Exception e) {
			
			e.printStackTrace();

	}
		return fncMap;
	}
		
	
	
}
