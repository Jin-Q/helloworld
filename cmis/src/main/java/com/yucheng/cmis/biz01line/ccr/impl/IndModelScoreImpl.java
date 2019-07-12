package com.yucheng.cmis.biz01line.ccr.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.yucheng.cmis.biz01line.ind.interfaces.IndModelScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 信用评级模型得分实现类
 * @author Administrator
 *
 */
public class IndModelScoreImpl implements IndModelScoreIface {

	private static final Logger logger = Logger.getLogger(IndModelScoreImpl.class);
	/**
	 * 取信用评级模型得分
	 * @param modelno 模型编号
	 * @param hm 模型下组得分
	 * @param conn 连接
	 * 
	 */
	public String getModelScore(String modelno, HashMap hm,ArrayList list ,Connection conn)
			throws ComponentException { 
		Statement st=null;
		Statement stt=null;
		ResultSet rs=null; 
		ResultSet rss=null; 
		String cusStatus=null;
		String retVal="";
		BigDecimal dModScore=new BigDecimal(0);
		CcrIndexValDao dao=null;
		String cusid=""; //客户号
		String com_grp_type="";//集团客户类型
		String grp_crd_grade="";//母公司评级 ;
		try{
			st=conn.createStatement();
			stt=conn.createStatement();
			String sql="select group_no,weight from ind_model_group where model_no='"+modelno+"'";
			rs=st.executeQuery(sql);
			dao=new CcrIndexValDao();
			cusid=(String)hm.get("cusid");
			String sqls = "select cus_status from cus_com where cus_id = '"+cusid+"'";
			rss = stt.executeQuery(sqls);
			while(rss!=null&&rss.next()){
				cusStatus = rss.getString("cus_status");
			}
			com_grp_type=(String)hm.get("com_grp_mode");
			grp_crd_grade=(String)hm.get("grp_crd_grade");
			while(rs!=null&&rs.next()){
				String grpno=rs.getString("group_no");
				BigDecimal fweight=new BigDecimal(rs.getFloat("weight"));
				String sGrpScore=(String)hm.get(grpno);
				BigDecimal dGrpScore;
				if(sGrpScore!=null&&!sGrpScore.trim().equals("")){
					
					
					try{
						dGrpScore=new BigDecimal(sGrpScore);
					}catch(Exception e){
						logger.error("取指标:"+grpno+"的得分不是数字:"+sGrpScore);
						dGrpScore=new BigDecimal("0");
					}
					dModScore=dModScore.add(dGrpScore.multiply(fweight));
				}
			} 

			logger.info("原始得分:"+dModScore.toString());
			if(-1==dModScore.compareTo(new BigDecimal("0"))){
				dModScore=new BigDecimal("0");
			}else{
				BigDecimal big=new BigDecimal("1");
				dModScore=dModScore.divide(big,2,BigDecimal.ROUND_HALF_UP); 
			}
			retVal=dModScore.toPlainString();
		}catch(Exception e){ 
			logger.error("取信用评级模型得分出错:>>>>="+e.toString(),e);  
		}finally{
			if(st!=null){
				  try {
					st.close();
					stt.close();
				} catch (SQLException e) { 
					logger.error("取信用评级模型得分关闭Statement出错:>>>>="+e.toString(),e); 
					}
			  }
		}
		
		//String serno      = (String)hm.get("serno");			//申请业务流水号
		String newComFlag = (String)hm.get("new_com_flag");		//新增企业标识
		
		//System.out.println("Msize:"+list.size());
		/*限制其信用等级*/
		//retVal = this.getModelLimitValue(modelno,retVal,serno,list,newComFlag,conn);
		
		String grade = this.modelScore2Grade(retVal,newComFlag,modelno,cusStatus);
		/**
		logger.info("集团客户类型:"+com_grp_type+",母公司评级:"+grp_crd_grade+",当前评级:"+grade);
		if(com_grp_type!=null&&com_grp_type.equals("2")&&!grp_crd_grade.equals("00")){
			//集团客户子公司 
			int iGrpCrdGrade=Integer.parseInt(grp_crd_grade);
			int iGrade=Integer.parseInt(grade);
			if(iGrade<iGrpCrdGrade){//当前评级比母公司还要高
				logger.info("当前评级比集团母公司还高，当前:"+grade+"，母公司:"+grp_crd_grade);
				grade=grp_crd_grade;//强制等于母公司结果
			}
		}*/
		logger.info("modelno="+modelno+",score="+retVal+",grade="+grade);
		retVal = retVal+"#"+grade;
		
		return retVal;
	}

	/**
	 * 通过模型得分
	 * @param score
	 * @newComFlag 1 -- 新增客户 2-- 存量客户 
	 * @return
	 */
	public String modelScore2Grade(String score,String newComFlag,String modelno,String cusStatus){
		String retValue = "16";
		Double scoreD = Double.valueOf(score);
		
		/*新增客户 */
		if("1".equals(newComFlag)){
			if("M20091200021".equals(modelno) || "M20091200019".equals(modelno)){  //小企业
				
				//新增小企业要扣掉信誉状况10分
				if(scoreD-80>-0.00001){
					retValue = "11";
				}else if(scoreD-70>-0.00001){
					retValue = "12";
				}else if(scoreD-60>-0.00001){
					retValue = "13";
				}else if(scoreD-50>-0.00001){
					retValue = "14";
				}else if(scoreD-40>-0.00001){
					retValue = "15";
				}else{
					retValue = "16";
				}
			}else{						//一般企业
				if(scoreD-65>-0.00001){
					retValue = "11";
				}else if(scoreD-55>-0.00001){
					retValue = "12";
				}else if(scoreD-45>-0.00001){
					retValue = "13";
				}else if(scoreD-35>-0.00001){
					retValue = "14";
				}else if(scoreD-30>-0.00001){
					retValue = "15";
				}else{
					retValue = "16";
				}
			}		
		}else if("3".equals(newComFlag)){
			/*
			 * 保证人
			 */
			if("M20091200021".equals(modelno) || "M20091200019".equals(modelno)){  //小企业
				
				//保证小企业要扣掉信誉状况10分
				if(scoreD-80>-0.00001){
					retValue = "11";
				}else if(scoreD-70>-0.00001){
					retValue = "12";
				}else if(scoreD-60>-0.00001){
					retValue = "13";
				}else if(scoreD-50>-0.00001){
					retValue = "14";
				}else if(scoreD-40>-0.00001){
					retValue = "15";
				}else{
					retValue = "16";
				}
			}else
			{						//一般企业
				if(scoreD-65>-0.00001){
					retValue = "11";
				}else if(scoreD-55>-0.00001){
					retValue = "12";
				}else if(scoreD-45>-0.00001){
					retValue = "13";
				}else if(scoreD-35>-0.00001){
					retValue = "14";
				}else if(scoreD-30>-0.00001){
					retValue = "15";
				}else{
					retValue = "16";
				}
			}
		} else{ 
			/*存量客户 */
			if("M20091200021".equals(modelno) || "M20091200019".equals(modelno)){  //小企业
				
					if(scoreD-90>-0.00001){
						retValue = "11";
					}else if(scoreD-80>-0.00001){
						retValue = "12";
					}else if(scoreD-70>-0.00001){
						retValue = "13";
					}else if(scoreD-60>-0.00001){
						retValue = "14";
					}else if(scoreD-50>-0.00001){
						retValue = "15";
					}else{
						retValue = "16";
					}
				
				
			}else
			{						//一般企业
				if(scoreD-85>-0.00001){
					retValue = "11";
				}else if(scoreD-75>-0.00001){
					retValue = "12";
				}else if(scoreD-65>-0.00001){
					retValue = "13";
				}else if(scoreD-55>-0.00001){
					retValue = "14";
				}else if(scoreD-45>-0.00001){
					retValue = "15";
				}else{
					retValue = "16";
				}
			}
		}
		return retValue;
		
	}
	

}
