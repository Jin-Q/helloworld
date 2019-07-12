package com.yucheng.cmis.biz01line.fnc.master.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.FncNumber;
import com.yucheng.cmis.pub.exception.AgentException;

public class FncStatBsDao extends CMISDao {

	
	/**
	 * 组装标签样式对象(带数据的)
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @param styleId
	 * @return
	 */
	public FncConfStyles queryDetailFncConfStyles(String cusId,String statPrdStyle,String statPrd,String styleId,Connection conn){
		FncConfStyles fncConfStyles = null;
		PreparedStatement ps = null;
		StringBuffer sb = new StringBuffer();				//用于存放拼成的sql
		String postfix = null;								//字段属性后缀的标示
		try {
			sb.append("SELECT f.*,n.item_name,b.STAT_INIT_AMT");
			
			//拆分报表期间statPrd
			String year = statPrd.substring(0, 4);
			String month = statPrd.substring(4);

			/**
			 * 根据报表周期类型判断该报表的类型.进行拼sql
			 */
			if("1".equals(statPrdStyle)){					//月报 _Amt6
				postfix = month;
				if(postfix.charAt(0)==0){
					postfix = postfix.substring(1);
				}	
			}else if("2".equals(statPrdStyle)){				//季报 _Amt_Q2
				postfix = FncNumber.getJibao(month);
			}else if("3".equals(statPrdStyle)){				//半年报 _Amt_Y1
				postfix = FncNumber.getBanNianBao(month);
			}else if("4".equals(statPrdStyle)){				//年报 _Amt_Y
				postfix = FncNumber.getNianBao(month);
			}
			
			sb.append(postfix + "  as data1, b.STAT_END_AMT" + postfix + "  as data2,m.Style_Id,m.Fnc_Name,m.Fnc_Conf_Dis_Name,m.Fnc_Conf_Typ,m.Fnc_Conf_Data_Column from Fnc_Conf_Def_Format f");
			sb.append(" left join Fnc_Stat_Bs b on b.Stat_Item_Id=f.item_id and b.cus_Id = ? AND b.Stat_Year=? ");
			sb.append(" left join Fnc_Conf_Items n on f.item_id=n.item_id ");
			sb.append(" left join Fnc_Conf_Styles m on f.style_Id=m.style_Id ");
			sb.append(" WHERE f.style_Id = ? and f.fnc_item_edit_typ in ('0','1','2')  order by f.FNC_CONF_COTES asc, f.FNC_CONF_ORDER asc ");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, cusId);
			ps.setString(2, year);
			ps.setString(3, styleId);
			ResultSet rs = ps.executeQuery();
			fncConfStyles = new FncConfStyles();
			List items = new ArrayList();
			FncConfDefFormat fdf = null;
			String fnc_name = null;
			String fnc_dis_name = null;
			String fnc_conf_typ = null;
			String style_id = null;
			int fnc_conf_data_column = 0;
			while(rs.next()){
				fdf = new FncConfDefFormat();
				fdf.setItemId(rs.getString("item_id"));
				fdf.setItemName(rs.getString("item_name"));
				fdf.setData1(rs.getDouble("data1"));
				fdf.setData2(rs.getDouble("data2"));
				items.add(fdf);
				
				style_id = rs.getString("style_id");
				fnc_name = rs.getString("fnc_name");
				fnc_dis_name = rs.getString("fnc_conf_dis_name");
				fnc_conf_typ = rs.getString("fnc_conf_typ");
				fnc_conf_data_column = rs.getInt("fnc_conf_data_col");
			}
			fncConfStyles.setStyleId(style_id);
			fncConfStyles.setFncName(fnc_name);
			fncConfStyles.setFncConfDisName(fnc_dis_name);
			fncConfStyles.setFncConfTyp(fnc_conf_typ);
			fncConfStyles.setFncConfDataCol(fnc_conf_data_column);
			fncConfStyles.setItems(items);
			

			if(rs != null){
				rs.close();
				rs = null;
			}
			if(ps !=null){
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
		}
		return fncConfStyles;
	}
	
	/**
	 * 根据客户号，指标编号，年号获取客户资产负债表中此项指标的年末值
	 * @param cusId
	 * @param statItemId
	 * @param statYear
	 * @return
	 * @throws AgentException
	 */
	public String getYearEndValue(String cusId,String statItemId,String statYear ,Connection conn){
		
		PreparedStatement ps = null;
		String value="";
		try {
			String sql = "select stat_end_amt_y from fnc_stat_bs where cus_id = ? and stat_item_id = ? and stat_year=? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, statItemId);
			ps.setString(3, statYear);

			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				value=rs.getString("stat_end_amt_y");
			}
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(ps !=null){
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
		}
		return value;
	}
}
