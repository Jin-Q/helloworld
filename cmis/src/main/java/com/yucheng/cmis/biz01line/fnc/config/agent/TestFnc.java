package com.yucheng.cmis.biz01line.fnc.config.agent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class TestFnc extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		String styleId = "SZ03  ";
		Connection con = this.getConnection(context);
		List list = this.getFormat(styleId,con);
		
		Map map = this.getItems(list, con);
		
		context.addDataField("formatList", list);
		//context.addDataField("itemMap", map);
		//context.addDataField("styleId", styleId);
		
		return PUBConstant.SUCCESS;
	}

	public List getFormat(String styleId,Connection con){
		List list = new ArrayList();
		String sql = "select B.STYLE_ID,B.ITEM_ID,B.FNC_CONF_ORDER,B.FNC_CONF_COTES,B.FNC_CONF_ROW_FLG,B.FNC_CONF_INDENT,B.FNC_CONF_PREFIX," +
						"B.FNC_ITEM_EDIT_TYP,B.FNC_CONF_DISPLAY_AMT,B.FNC_CONF_CHECK_FORMULA,B.FNC_CONF_CAL_FORMULA," +
						"B.FNC_CONF_APPEND_ROW,A.ITEM_NAME from FNC_CONF_ITEMS A,FNC_CONF_DEF_FORMAT B " +
						"where A.ITEM_ID = B.ITEM_ID and STYLE_ID = '"+styleId+"'  " +
								"order by B.FNC_CONF_COTES asc, B.FNC_CONF_ORDER asc ";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				FncConfDefFormat fdf = new FncConfDefFormat();
				/*fm.setStyleId(styleId);
				fm.setItemId(rs.getString("ITEM_ID"));
				fm.setFncConfOrder(rs.getInt("FNC_CONF_ORDER"));
				fm.setFncConfCotes(rs.getInt("FNC_CONF_COTES"));
				fm.setFncConfRowFlg(rs.getString("FNC_CONF_ROW_FLG"));
				fm.setFncConfIndent(rs.getInt("FNC_CONF_INDENT"));
				fm.setFncConfAppendRow(rs.getInt("fnc_conf_append_row"));
				fm.setFncConfCalFormula(rs.getString("fnc_conf_cal_formula"));
				fm.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fm.setItemName(rs.getString("item_name"));
				fm.setFncConfRowFlg(rs.getString("Fnc_Conf_Row_Flg"));*/
				fdf.setStyleId(rs.getString(1));
				fdf.setItemId(rs.getString(2));
				fdf.setFncConfOrder(rs.getInt(3));
				fdf.setFncConfCotes(rs.getInt(4));
				fdf.setFncConfRowFlg(rs.getString(5));
				fdf.setFncConfIndent(rs.getInt(6));
				fdf.setFncConfPrefix(rs.getString(7));
				fdf.setFncItemEditTyp(rs.getString(8));
				//fdf.setFncConfDisplayAmt(rs.getDouble(9));
				//fdf.setFncConfCheckFormula(rs.getString(10));
				//fdf.setFncConfCalFormula(rs.getString(11));
				//fdf.setFncConfAppendRow(rs.getInt(12));
				fdf.setItemName(rs.getString(13));
				
				
				list.add(fdf);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public Map getItems(List list,Connection con){
		
		Map map = new HashMap();
		String itemId = "";
		
		for(int i=0;i<list.size();i++){
			FncConfDefFormat f = (FncConfDefFormat)list.get(i);
			itemId = f.getItemId();
			
			String sql = "select * from FNC_CONF_ITEMS where ITEM_ID='"+itemId+"'";
			try {
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()){
					FncConfItems fm = new FncConfItems();
					fm.setFncConfTyp(rs.getString("Fnc_Conf_Typ"));
					fm.setItemId(itemId);
					fm.setItemName(rs.getString("Item_Name"));
					map.put(itemId, fm);
				}
				rs.close();
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
}
