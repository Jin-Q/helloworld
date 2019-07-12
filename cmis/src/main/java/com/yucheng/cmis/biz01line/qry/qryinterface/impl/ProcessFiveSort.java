package com.yucheng.cmis.biz01line.qry.qryinterface.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.biz01line.qry.qryinterface.qryInterface;

public class ProcessFiveSort extends qryInterface {

	public void analyseData(HashMap<String, String> conditionMap,
			HashMap<String, String> operationMap, Context context,
			Connection con) throws EMPException {
		String nf  =  "";
		String yf  =  "";
		String jgm =  "";
		
		String tzrq = "";
		
		Statement st = null;
		
		String sql = "";
		String oper = "";
		
		String organcode = "";
		int max = 1000;
		
		try {
		    nf = conditionMap.get("nf_VALUE");
			yf = conditionMap.get("yf_VALUE");
			jgm = conditionMap.get("jgm_VALUE");
			oper = operationMap.get("jgm_OPER");
			
			if( nf == null || yf == null ){
				throw new CMISException("请选择年份，月份!");
			}
			
			organcode = (String)context.getDataValue("S_organcode");
			max = Integer.parseInt((String)context.getDataValue("INT_COUNT"));
			
			tzrq = this.getLastDayOfMonth(Integer.parseInt(nf), Integer.parseInt(yf));
			
			yf = Integer.parseInt(yf)+"";
			
			if( Integer.parseInt(yf) > 1 ){
				sql = "insert into CRD_WJFLMXCX select a.jgm as jgm,b.dqbm as dqbm,b.khmc as jkrmc,'"+tzrq+"' as tzrq," +
					  "a.jjye"+yf+" as jyje,a.wjfl"+(Integer.parseInt(yf)-1)+" as swjfl,a.wjfl"+yf+" as wjfl, " +
					  "b.dkqx as dkqx,b.qdrq as qdrq,b.zdrq as zdrq,b.dkzh as dkzh,b.kmh as kmh from" +
					  " crd_jjyzz a inner join crd_dktz b on a.jjbh=b.jjbh " +
					  " where a.wjfl"+yf+" is not null and a.nf="+nf+" and a.wjfl"+yf+"!=a.wjfl"+(Integer.parseInt(yf)-1);
			}else{
				sql = "insert into CRD_WJFLMXCX select a.jgm as jgm,b.dqbm as dqbm,b.khmc as jkrmc,'"+tzrq+"' as tzrq," +
				      "c.jjye"+yf+" as jyje,a.wjfl12 as swjfl,a.wjfl1 as wjfl, " +
				      "b.dkqx as dkqx,b.qdrq as qdrq,b.zdrq as zdrq,b.dkzh as dkzh,b.kmh as kmh from" +
				      " crd_jjyzz a inner join crd_dktz b on a.jjbh=b.jjbh " +
				      " inner join crd_jjyzz c on a.jjbh=c.jjbh and a.nf=c.nf+1 where a.nf="+nf+" and a.wjfl"+yf+" is not null and a.wjfl1!=c.wjfl12";
			}
			
			if( QryPubConstant.SYS_DIC_OPER_1.equals(oper) ){
				sql += " and a.jgm='"+jgm+"'";
			}else if( QryPubConstant.SYS_DIC_OPER_8.equals(oper) ){
				String code = this.getOrgancode(jgm, con);
				if( !code.startsWith(organcode) ){
				     organcode = code;
				}
				sql += " and exists (select ORGANNO from s_org where organno=a.jgm and ORGANCODE like '"+organcode+"%') ";
			}
			
			sql += " and rownum<"+max;
			
			st = con.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException(e);
		} finally{
		    try {
				if( st != null ) st.close();
			} catch (Exception e) {}	
		}

	}
	
	private String getOrgancode(String jgm, Connection con) throws CMISException{
		String organcode = "";
		
		PreparedStatement ps = null;
 	    ResultSet rs = null;
 	   
 	    try {
 	    	  ps = con.prepareStatement("select ORGANCODE from s_org where ORGANNO=?");
			  ps.clearParameters();
			  
			  ps.setString(1, jgm);
			  rs = ps.executeQuery();
			  if( rs!= null && rs.next() ){
				  organcode = rs.getString(1);
			  }else{
				  throw new CMISException("查询机构失败『"+jgm+"』");
			  }
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException(e);
		} finally{
			
		}
		return organcode;
	}
	
	
	private String getLastDayOfMonth(int year, int month) throws CMISException{
		String rq = "";
		int day = 31;
		
		try {
			switch (month){
				case 2:  if( ((year%4==0) && (year%100!=0))||(year%400==0) ){
					          day = 29;
				         }else{
				        	  day = 28;
				         }
					     break;
				case 4:  day = 30;
					     break;
				case 6:  day = 30;
					     break;
				case 9:  day = 30;
					     break;
				case 11: day = 30;
					     break;
			    default: break;
			}
			if( month < 10 ){
				rq = year + "0"+month;
			}else{
				rq = year + "" + month;
			}
			
			if( day < 10 ){
				rq += "0" + day;
			}else{
				rq += day;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException(e);
		}
		return rq;
	}

}
