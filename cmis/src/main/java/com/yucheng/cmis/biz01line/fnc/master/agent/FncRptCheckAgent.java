package com.yucheng.cmis.biz01line.fnc.master.agent;


/**
 *@Classname	FncRptCheckAgent.java
 *@Version 1.0	
 *@author Daniel Z 
 *@Copyright 	yuchengtech
 *@Since 
 *@Description：
 *@Lastmodified
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.yucheng.cmis.biz01line.fnc.master.dao.FncRptCheckDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.util.StringUtil;


public class FncRptCheckAgent extends CMISAgent{
	
	private StringUtil util = new StringUtil();
	private Statement st;
	private ResultSet rs;
	
	/**根据{[].[].[]}式中第三个[]中标志位和月份确定对应的报表列名
	 * @param String flag
	 * @param String month
	 * @return String field
	 * */
	public String queryField(String flag,String month,String statPrdStyle) throws AgentException{
		
		String field = "";
		
		if(statPrdStyle.equals("2")){
			if(flag.equals("01")){
				if(month.equals("03")){
					field = "stat_item_amt_q1";
				}
				else if(month.equals("06")){
					field = "stat_item_amt_q2";
				}
				else if(month.equals("09")){
					field = "stat_item_amt_q3";
				}
				else if(month.equals("12")){
					field = "stat_item_amt_q4";
				}
				else{
					throw new AgentException("输入的月份不符，季报对应的会计期间应为03、06、09、12");
				}
			}
			else if(flag.equals("02")){
				if(month.equals("03")){
					field = "stat_end_amt_q1";
				}
				else if(month.equals("06")){
					field = "stat_end_amt_q2";
				}
				else if(month.equals("09")){
					field = "stat_end_amt_q3";
				}
				else if(month.equals("12")){
					field = "stat_end_amt_q4";
				}
				else{
					throw new AgentException("输入的月份不符，季报对应的会计期间应为03、06、09、12");
				}
			}
			
		}
		else if(statPrdStyle.equals("3")){
			if(flag.equals("01")){
				if(month.equals("06")){
					field = "stat_item_amt_y1";
				}
				else if(month.equals("12")){
					field = "stat_item_amt_y2";
				}
				else{
					throw new AgentException("输入的月份不符，半年报对应的会计期间应为06,12");
				}
			}
			else if(flag.equals("02")){
				if(month.equals("06")){
					field = "stat_end_amt_y1";
				}
				else if(month.equals("12")){
					field = "stat_end_amt_y2";
				}
				else{
					throw new AgentException("输入的月份不符，半年报对应的会计期间应为06,12");
				}
					
			}
		}
		else if(statPrdStyle.equals("4")){
			if(flag.equals("01")){
				if(month.equals("12"))
					field = "stat_item_amt_y";
				else 
					throw new AgentException("输入的月份不符，年报对应的会计期间应为12");
			}
			else if(flag.equals("02")){
				if(month.equals("12"))
					field = "stat_end_amt_y";
				else
					throw new AgentException("输入的月份不符，年报对应的会计期间应为12");
					
			}
		}
		else if(statPrdStyle.equals("1")){
			if(flag.equals("01")){
				if(Integer.parseInt(month)>12 || Integer.parseInt(month)<1){
					throw new AgentException("输入的月份不符，月报对应的会计期间应在1~12月之间");
				}
				else
					field = "stat_item_amt"+Integer.parseInt(month);
				
			}
			else if(flag.equals("02")){
				if(Integer.parseInt(month)>12 || Integer.parseInt(month)<1){
					throw new AgentException("输入的月份不符，月报对应的会计期间应在1~12月之间");
				}
				else
					field = "stat_end_amt"+Integer.parseInt(month);
				
			}
		}	
				
		return field;
		
	}
	
	/**根据传入的月份获得对应的报表列名
	 * @param String month
	 * @return  String field
	 * */
	public String queryField(String month,String statPrdStyle) throws AgentException{
		String field = "";
		
		if(statPrdStyle.equals("2")){
			if(month.equals("03")){
				field = "stat_item_amt_q1";
			}
			else if(month.equals("06")){
				field = "stat_item_amt_q2";
			}
			else if(month.equals("09")){
				field = "stat_item_amt_q3";
			}
			else if(month.equals("12")){
				field = "stat_item_amt_q4";
			}
			else
				throw new AgentException("输入的月份不符，季报对应的会计期间应为03、06、09、12");
			
		}
		else if(statPrdStyle.equals("3")){
			if(month.equals("06")){
				field = "stat_item_amt_y1";
			}
			else if(month.equals("12")){
				field = "stat_item_amt_y2";
			}
			else 
				throw new AgentException("输入的月份不符，半年报对应的会计期间应为06、12");
		}
		else if(statPrdStyle.equals("4")){
			if(month.equals("12")){
				field = "stat_item_amt_y";
			}
			else
				throw new AgentException("输入的月份不符，年报对应的会计期间应为12");
				
		}
		else if(statPrdStyle.equals("1")){
			
			if(Integer.parseInt(month)>12 || Integer.parseInt(month)<1){
				throw new AgentException("输入的月份不符，月报对应的会计期间应在1~12月之间");
			}
			else
				field = "stat_item_amt"+Integer.parseInt(month);
			
		}
				
		return field;
	}
	
	/**根据报表类型获取报表表名
	 * @param String fncConfTyp
	 * @return String tablename 
	 * */
	public String queryTablename(String fncConfTyp){
		String tablename = "";
		if(fncConfTyp.equals("01")){
			tablename = "fnc_stat_bs";
		}
		else if(fncConfTyp.equals("02")){
			tablename = "fnc_stat_is";
		}
		else if(fncConfTyp.equals("03")){
			tablename = "fnc_stat_cfs";
		}
		else if(fncConfTyp.equals("04")){
			tablename = "fnc_stat_soe";
		}
		else if(fncConfTyp.equals("05")){
			tablename = "fnc_index_rpt";
		}
		else if(fncConfTyp.equals("06")){
			tablename = "fnc_stat_sl";
		}
		else
			System.out.println("对应的表不存在");
		
		return tablename;
	}
	
	/**根据传入的月份获取对应的报表期初和期末项列名
	 * @param String month
	 * @return String[] field 
	 * */
	public String[] queryFields(String month,String statPrdStyle) throws AgentException{
		String[] field = new String[2];
		
		if(statPrdStyle.equals("2")){
			if(month.equals("03")){
				field[0] = "stat_item_amt_q1";
				field[1] = "stat_end_amt_q1";
			}
			else if(month.equals("06")){
				field[0] = "stat_item_amt_q2";
				field[1] = "stat_end_amt_q2";
			}
			else if(month.equals("09")){
				field[0] = "stat_item_amt_q3";
				field[1] = "stat_end_amt_q3";
			}
			else if(month.equals("12")){
				field[0] = "stat_item_amt_q4";
				field[1] = "stat_end_amt_q4";
			}
			else{
				throw new AgentException("输入的月份不符，季报对应的会计期间为03、06、09、12");
			}
		}
		else if(statPrdStyle.equals("3")){
			if(month.equals("06")){
				field[0] = "stat_item_amt_y1";
				field[1] = "stat_end_amt_y1";
			}
			else if(month.equals("12")){
				field[0] = "stat_item_amt_y2";
				field[1] = "stat_end_amt_y2";
			}
			else{
				throw new AgentException("输入的月份不符，半年报对应的会计期间应为06、12");
			}
		}
		else if(statPrdStyle.equals("4")){
			if(month.equals("12")){
				field[0] = "stat_item_amt_y";
				field[1] = "stat_end_amt_y";
			}
			else{
				throw new AgentException("输入的月份不符，年报对应的会计期间应为12");
			}
		}
		else if(statPrdStyle.equals("1")){
			
			if(Integer.parseInt(month)>12 || Integer.parseInt(month)<1){
				throw new AgentException("输入的月份不符，月报对应的会计期间应在1~12月之间");
			}
			else{
				field[0] = "stat_item_amt" + Integer.parseInt(month);
				field[1] = "stat_end_amt" + Integer.parseInt(month); 
			}			
			
		}
		
		return field;
	}
	
	
	/**根据传入的FncStatBase对象和itemInfo查询报表中item对应的值
	 * @param FncStatBase fsb 财务报表基表对象
	 * @param String itemInfo 公式中的计算项，格式为[flag1].[itemId].[flag2]
	 * @param char leftFlag 
	 * @param char rightFlag 
	 * @exception SQLException、AgentException
	 * @return String value 
	 * */
	public String getItemValue(FncStatBase fsb,String itemInfo,char leftFlag,char rightFlag){
		String value = "";
		
		//获取iteminfo中的两个标志位间的子项
		List items = util.getParamList(itemInfo, leftFlag, rightFlag);
		String flag1 = (String)items.get(0);
		String itemId = (String)items.get(1);
		String flag2 = (String)items.get(2);
		
		StringBuffer sql = new StringBuffer();
		String cusId = fsb.getCusId();
		String period = fsb.getStatPrd();
		String year = period.substring(0,4);
		String month = period.substring(4);		
		
		
		
		/*flag1 01表示资产负债表 02表示损益表 03表示现金流量表（如果需要可以添加其他相关的表的操作）
		 *判断查询出的结果集是否为空，不为空时获取列(field)对应的值
		 * 为空时则赋值"null"
		 */
		try{
			
//			获取报表列名
			String field = this.queryField(flag2, month);
			FncRptCheckDao rptCheckDao = new FncRptCheckDao(this.getConnection());
			
			
			if(flag1.equals("01")){
				sql = sql.append("select * from fnc_stat_bs t where t.cus_id =")
					  .append(cusId+" and stat_year ="+year)
					  .append(" and stat_item_id ="+itemId);
				
				value = rptCheckDao.query(sql.toString(), field);
			}
			else if(flag1.equals("02")){
				
				sql = sql.append("select * from fnc_stat_is t where t.cus_id =")
					  .append(cusId+" and stat_year ="+year)
					  .append(" and stat_item_id ="+itemId);					  				
								
				rptCheckDao.query(sql.toString(), field);
			}
			else if(flag1.equals("03")){
				
				sql = sql.append("select * from fnc_stat_cfs t where t.cus_id =")
					  .append(cusId+" and stat_year ="+year)
					  .append(" and stat_item_id ="+itemId);					  
				
				rptCheckDao.query(sql.toString(),field);		
			}
			else{
				System.out.println("相关表不存在");
				value = "null";
			}		
		}		
		catch(AgentException e1){
			e1.printStackTrace();
		}
		
		return value;
	}
			
	/**新增或更新现金流量表、股东权益变动表、指标表、财务简表单条item记录
	 * @param FncStatBase fsb 财务报表主表对象
	 * @param String fncConfTyp 财务报表类型
	 * @param String itemId 报表列表项
	 * @param String result 报表列表项值	  
	 * @exception SQLException、AgentException
	 * @return
	 * */
	public void  saveRecord(FncStatBase fsb,String fncConfTyp,String itemId,String result) throws AgentException{
		
		String cusId = fsb.getCusId();
		String period = fsb.getStatPrd();
		String statPrdSty = fsb.getStatPrdStyle();
		String year = period.substring(0,4);
		String month = period.substring(4);
		StringBuffer sql = new StringBuffer();
		double d1 = Double.parseDouble(result);
		
		//获取财务报表表名
		String tablename = this.queryTablename(fncConfTyp);
		
		/*当报表类型对应的表存在时，查询该表中itemId对应的当年的记录是否存在
		 * 若不存在则在向数据库中插入一条记录
		 * 若存在则更新对应的记录
		 * 保存后close ResultSet和Statement
		 * 当报表类型对应的表不存在时，返回 "报表不存在"信息 
		 * */
		if(!tablename.equals("")){		
			sql.append("select * from "+tablename)
				.append(" where cus_id = "+cusId+" and stat_year = "+year+" and stat_item_id = "+itemId);
			
			String querySql = sql.toString();
			
			String columnname = this.queryField(month,statPrdSty);
			
			sql.delete(0, sql.length()-1);
			
			sql.append("insert into "+tablename+"(cus_id,stat_year,stat_item_id,"+columnname+")")
			.append(" values('"+cusId+"','"+year+"','"+itemId+","+d1+",");
			
			String insertSql = sql.toString();
			
			sql.delete(0, sql.length()-1);
			
			sql.append("update "+tablename)
			.append("set "+columnname+"="+d1)
			.append(" where cus_id = "+cusId+" and stat_year = "+year+" and stat_item_id = "+itemId); 
			
			String updateSql = sql.toString();
			
			FncRptCheckDao rptCheckDao = new FncRptCheckDao(this.getConnection());
			
			rptCheckDao.saveRecord(querySql, updateSql, insertSql);							
			
		}
		else
			throw new AgentException("报表不存在");
		
	}
	
	/**新增或更新资产负债表、损益表单条item记录
	 * @param FncStatBase fsb 财务报表主表对象
	 * @param String fncConfTyp 财务报表类型
	 * @param String itemId 报表列表项
	 * @param String results 报表列表项值
	 * @exception SQLException、AgentException
	 * @return
	 * */
	public void saveRecord(FncStatBase fsb,String fncConfTyp,String itemId,String[]result) throws AgentException{
		String cusId = fsb.getCusId();
		String period = fsb.getStatPrd();
		String statPrdSty = fsb.getStatPrdStyle();
		String year = period.substring(0,4);
		String month = period.substring(4);
		double d1 = Double.parseDouble(result[0]);
		double d2 = Double.parseDouble(result[1]);
		
		//获取报表表名
		String tablename = this.queryTablename(fncConfTyp);
		
		StringBuffer sql = new StringBuffer();
		
		/*当报表类型对应的表存在时，查询该表中itemId对应的当年的记录是否存在
		 * 若不存在则在向数据库中插入一条记录
		 * 若存在则更新对应的记录
		 * 保存后close ResultSet和Statement
		 * 当报表类型对应的表不存在时，返回 "报表不存在"信息 
		 * */
		if(!tablename.equals("")){
			
			
			try{
				
				//获取列名数组
				String columnnames[] = this.queryFields(month,statPrdSty);
				
				sql.append("select * from "+tablename)
				.append(" where cus_id = ")
				.append(cusId+" and stat_year = "+year+" and stat_item_id = "+itemId);				
				String querySql = sql.toString();
				
				sql.delete(0, sql.length()-1);				
				sql.append("insert into "+tablename+"(cus_id,stat_year,stat_item_id"+columnnames[1]+columnnames[2]+")")
				.append(" values('"+cusId+"','"+year+"','"+itemId+"','"+d1+"','"+d2+"'");				
				String insertSql = sql.toString();
				
				sql.delete(0, sql.length()-1);
				sql.append("update "+tablename)
				.append("set "+columnnames[0]+"="+d1+","+columnnames[1]+"="+d2)
				.append("where cus_id = "+cusId+" and stat_year = "+year+" and stat_item_id = "+itemId) ;
				String updateSql = sql.toString();
				
				FncRptCheckDao rptCheckDao = new FncRptCheckDao(this.getConnection());				
				rptCheckDao.saveRecord(querySql, updateSql, insertSql);
				
				
			}		
			catch(AgentException e){
				e.printStackTrace();
			}
			
		}
		else
			throw new AgentException("报表不存在");
		
	}
	
	
	
}
