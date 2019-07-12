package com.yucheng.cmis.biz01line.ccr.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.yucheng.cmis.biz01line.ccr.agent.CcrLimitGradeAgent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	CcrLimitGradeComponent.java
 *@Version 1.0	
 *@Since   1.0 	Jan 8, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：计算评级限制等级
 *@Lastmodified 
 *@Author	    
 */
public class CcrLimitGradeComponent extends CMISComponent {
	/**
	 * 根据输入的得分 和 其业务流水号 计算其最高得分
	 * @param inputScore 得分
	 * @param serno		 流水号
	 * @param list       指标得分集
	 * @param newComFlag 是否小企业标识
	 * @param conn   	 数据库连接
	 * @return
	 * @throws ComponentException
	 */
	public HashMap<String,String> getModelLimitValue(String modelNo,String autoGrade,String serno,ArrayList list,String newComFlag) throws ComponentException{
//		String retValue = "";
		String grade = autoGrade;
		String indexNo = "";
		String indexScore = "";
		String groupNo = "";
		String indexValue = "";
		String indStdScore = "";
		String indOrgVal = "";
		HashMap<String,String> map =new HashMap<String,String>();
		HashMap<String,String> hm= new HashMap<String,String>();
		
		//农业
		if("M20091210000".equals(modelNo)){
				
				//资产负债率
				indexNo ="I20091200100";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				
				map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore);
				//经营性活动现金流量净额
				indexNo ="I20091200106";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map);				
				System.out.println("经营性活动现金流量净"+map.get("limit_grade")+" "+map.get("reason"));	
				
				if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
					//贷款资产形态
					indexNo ="I20091200400";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexValue = hm.get("optvalue");				
					map = this.getLimitLoanPropertyPattern(indexValue,map);
				
					//到期信用偿还记录
					indexNo ="I20091200401";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexScore = hm.get("score");
					groupNo = hm.get("groupno");
					indexValue = hm.get("optvalue");
					indOrgVal = hm.get("orgvalue");			
					indStdScore = this.getFullScore(groupNo, indexNo);
					map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal);
				}
				//近三年利润情况
				indexNo ="I20091200501";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indOrgVal = hm.get("orgvalue");			
				map = this.getLimitLastThreeYearProfitState(map, indOrgVal);
				System.out.println("近三年利润情况"+map.get("limit_grade")+" "+map.get("reason"));	
		}else if("M20091200005".equals(modelNo)){    //建筑
			
				//资产负债率
				indexNo ="I20091200036";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");				
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore);
								
				//经营性活动现金流量净额
				indexNo ="I20091200042";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map);				
				
				if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
					//贷款资产形态
					indexNo ="I20091200053";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexValue = hm.get("optvalue");				
					map = this.getLimitLoanPropertyPattern(indexValue, map );
				
					//到期信用偿还记录
					indexNo ="I20091200054";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexScore = hm.get("score");
					groupNo = hm.get("groupno");
					indexValue = hm.get("optvalue");
					indOrgVal = hm.get("orgvalue");			
					indStdScore = this.getFullScore(groupNo, indexNo);
					map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
				}
					
				//近三年利润情况
				indexNo ="I20091200056";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indOrgVal = hm.get("orgvalue");			
				map = this.getLimitLastThreeYearProfitState(map, indOrgVal);
				
				//资质等级
				indexNo ="I20091200060";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");
				map = this.getLimitGrade(indexValue, map );
				
				//工程质量优良率
				indexNo ="I20091200061";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");		
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitRateOfProjectQualityExcellent(indStdScore, indexScore,  map );
				
		}else if("M20091200010".equals(modelNo)){    //房地产
			
				///资产负债率
				indexNo ="I20091200064";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");				
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore);
								
				//经营性活动现金流量净额
				indexNo ="I20091200070";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map );				
				if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
					//贷款资产形态
					indexNo ="I20091200076";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexValue = hm.get("optvalue");				
					map = this.getLimitLoanPropertyPattern(indexValue, map );
				
					//到期信用偿还记录
					indexNo ="I20091200077";
					hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
					indexScore = hm.get("score");
					groupNo = hm.get("groupno");
					indexValue = hm.get("optvalue");
					indOrgVal = hm.get("orgvalue");			
					indStdScore = this.getFullScore(groupNo, indexNo);
					map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal);
				}
				//资质等级
				indexNo ="I20091200083";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");
				map = this.getLimitGrade(indexValue, map );
			
		}else if("M20091200011".equals(modelNo)){    //交通及运输
		
			///资产负债率
			indexNo ="I20091200087";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
							
			//经营性活动现金流量净额
			indexNo ="I20091200093";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map );				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
				//贷款资产形态
				indexNo ="I20091200099";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map );
			
				//到期信用偿还记录
				indexNo ="I20091200700";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}	
			//近三年利润情况
			indexNo ="I20091200110";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
		}else if("M20091200014".equals(modelNo)){    //生产制造
		
			///资产负债率
			indexNo ="I20091200119";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
							
			//经营性活动现金流量净额
			indexNo ="I20091200125";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map );				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
				//贷款资产形态
				indexNo ="I20091200131";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map);
			
				//到期信用偿还记录
				indexNo ="I20091200132";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}
			//近三年利润情况
			indexNo ="I20091200134";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
		}else if("M20091200015".equals(modelNo)){    //商贸批发及进出口
		
			///资产负债率
			indexNo ="I20091200143";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
							
			//经营性活动现金流量净额
			indexNo ="I20091200149";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map);				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户	
				//贷款资产形态
				indexNo ="I20091200155";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map);
			
				//到期信用偿还记录
				indexNo ="I20091200156";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}	
			//近三年利润情况
			indexNo ="I20091200158";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
		}else if("M20091200016".equals(modelNo)){    //商贸零售
		
			//资产负债率
			indexNo ="I20091200168";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
							
			//经营性活动现金流量净额
			indexNo ="I20091200174";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore,map);				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
				//贷款资产形态
				indexNo ="I20091200181";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map);
			
				//到期信用偿还记录
				indexNo ="I20091200182";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}	
			
			//近三年利润情况
			indexNo ="I20091200184";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
		}else if("M20091200017".equals(modelNo)){    //酒店服务
			 
			//资产负债率
			indexNo ="I20091200192";

			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			
			indStdScore = this.getFullScore(groupNo, indexNo);
			
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
			 			
			
			//经营性活动现金流量净额
			indexNo ="I20091200198";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map);				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
				//贷款资产形态
				indexNo ="I20091200209";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map);
			
				//到期信用偿还记录
				indexNo ="I20091200210";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}
				
			//近三年利润情况
			indexNo ="I20091200212";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
			//企业声誉
			indexNo ="I20091200217";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitFameOfCompany(indStdScore, indexScore, map);
			
		}else if("M20091200018".equals(modelNo)){    //综合
		
			//资产负债率
			indexNo ="I20091200221";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");
			indexValue = hm.get("optvalue");				
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitProptyRateDebt(indStdScore, grade, indexValue, indexScore );
							
			//经营性活动现金流量净额
			indexNo ="I20091200227";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indexScore = hm.get("score");
			groupNo = hm.get("groupno");			
			indStdScore = this.getFullScore(groupNo, indexNo);
			map = this.getLimitManagerActivityCashFlow(indStdScore, indexScore, map);				
			
			if("2".equals(newComFlag)){ //2是存量客户  1是新增客户 3是保证客户
				//贷款资产形态
				indexNo ="I20091200233";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexValue = hm.get("optvalue");				
				map = this.getLimitLoanPropertyPattern(indexValue, map);
			
				//到期信用偿还记录
				indexNo ="I20091200234";
				hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
				indexScore = hm.get("score");
				groupNo = hm.get("groupno");
				indexValue = hm.get("optvalue");
				indOrgVal = hm.get("orgvalue");			
				indStdScore = this.getFullScore(groupNo, indexNo);
				map = this.getLimitMatureHonorPayRecord(indStdScore, indexScore, map, indOrgVal );
			}	
			
			//近三年利润情况
			indexNo ="I20091200236";
			hm = this.getOptValueAndOrgValueByIndexNo(indexNo, list);
			indOrgVal = hm.get("orgvalue");			
			map = this.getLimitLastThreeYearProfitState(map, indOrgVal );
			
		}else if("M20091200019".equals(modelNo) || "M20091200021".equals(modelNo)){    //工业企业小企业和商贸企业小企业
			
			System.out.println("//////"+map.get("limit_grade")+map.get("reason"));
			
		}
		
		return map;
	}
	
	/**
	 * 根据指标编号 在 
	 * @param IndexNo
	 * @param list 指标得分列表
	 * @return  key=optvalue (选项值)
	 * 			key=orgvalue (原始值)
	 * 			key=score	 (指标得分)
	 * 			key=groupno  (组编号)
	 */
	public HashMap<String,String> getOptValueAndOrgValueByIndexNo(String indexNo,ArrayList list){
		HashMap<String,String> hm = new HashMap<String,String>();
		Iterator ccrGIndScoreListIter = list.iterator();
		while(ccrGIndScoreListIter.hasNext()){
			CcrGIndScore ccrGIndScore = (CcrGIndScore)ccrGIndScoreListIter.next();
			if(indexNo.equals(ccrGIndScore.getIndexNo())){
				hm.put("orgvalue", ccrGIndScore.getIndOrgVal()) ;
				hm.put("optvalue", ccrGIndScore.getIndexValue()) ;
				hm.put("score", ccrGIndScore.getIndexScore());
				hm.put("groupno", ccrGIndScore.getGroupNo());
				break;
			}
		}
		return hm;
	}
	
	/**
	 * 根据 得到满分值
	 * @param groupNo
	 * @param indexNo
	 * @param conn
	 * @return
	 */
	public String getFullScore(String groupNo,String indexNo) throws ComponentException{
		CcrLimitGradeAgent clgAgent = (CcrLimitGradeAgent)this.getAgentInstance(CcrPubConstant.CcrLimitGrade);
		
		return clgAgent.getFullScore(groupNo, indexNo);
	}
	/**
	 *	对企业声誉限制等级,AAA、AA、A级均需满分,这里只有酒店服务类 
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分
	 * @param indStdScore 指标选项得分为满分值
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitFameOfCompany(String indStdScore,String indexScore,HashMap<String,String> map){
		
		HashMap<String,String> retValue=new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indStdScored = Double.parseDouble(indStdScore);
		double indexScored = Double.parseDouble(indexScore);
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
			 
				if(graded <=13 && graded >10){
					if(indexScored<indStdScored){

						retValue.put("limit_grade", "14");
						retValue.put("reason", "企业声誉,AAA、AA、A级均需满分");
					}
				}
				retValue.put("grade", (String) map.get("grade"));
				retValue =this.contrastHm(map, value);
		return retValue;
	}
	/**
	 *	对工程质量优良率限制等级,AAA、AA级需满分,这里只有建筑类  
	 * @param indStdScore 指标选项得分为满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitRateOfProjectQualityExcellent(String indStdScore,String indexScore,HashMap<String,String> map){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indStdScored = Double.parseDouble(indStdScore);
		double indexScored = Double.parseDouble(indexScore);
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
			 
				if(graded <=12 && graded >10){
					if(indexScored<indStdScored){

						value.put("limit_grade", "13");
						value.put("reason", "工程质量优良率,AAA、AA级需满分");
					}
				}
				value.put("grade", (String) map.get("grade"));
				retValue =this.contrastHm(map, value);
		return retValue;
	}
	/**
	 *	对资质等级的限制等级,标准:(建筑类)AAA级在一级及以上(房地产)AAA级需在二级及以上
	 * @param indStdScore 指标选项得分为满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值,建筑类二级和房地产类三级对应的选项值都为3
	 * @param indexScore  指标选项得分
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitGrade(String indexValue,HashMap<String,String> map){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indexValued = Double.parseDouble(indexValue);
		String grade =(String)map.get("grade");
			 
				if("11".equals(grade)){
					if(indexValued>2){

						value.put("limit_grade", "12");
						value.put("reason", "资质等级，(建筑类)AAA级在一级及以上，(房地产类)AAA级需在二级及以上");
					}
				}
				value.put("grade", (String)map.get("grade"));
				
				retValue =this.contrastHm(map, value);
				
		return retValue;
	}
	/**
	 *	对所有近三年利润情况的限制等级,连续三年均亏损,信用等级不得超过A级
	 * @param indStdScore 对应的满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分
	 * @param indOrgVal  这里连续三年亏损的标志，如果值为100为连续三年亏损，0则不是
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitLastThreeYearProfitState(HashMap<String,String> map,String indOrgVal){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indOrgVald = Double.parseDouble(indOrgVal);
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
		
				if(graded <=12 && graded >10){
					if(100==indOrgVald){

						value.put("limit_grade", "13");
						value.put("reason", "近三年利润,连续三年均亏损,信用等级不得超过A级");
					}
				}
				
				value.put("grade", (String) map.get("grade"));
				retValue =this.contrastHm(map, value);
		
		return retValue;
	}
	/**
	 *	对所有到期信用偿还记录的限制等级,(1)AAA、AA、A级均需满分
	 *(2)逾期超过1个月的,信用等级降为 BBB（含）级以下,超过3个月的,信用等级降为 BB（含）级以下	
	 * @param indStdScore 对应的满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分 
	 * @param indStdScored 指标选项得分为满分
	 * @param indOrgVal  这里为逾期的天数
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitMatureHonorPayRecord(String indStdScore,String indexScore,HashMap<String,String> map,String indOrgVal){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indStdScored = Double.parseDouble(indStdScore);
		double indexScored = Double.parseDouble(indexScore);
		double indOrgVald = Double.parseDouble(indOrgVal);
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
		
				if(graded <=13 && graded >10){
					if(indexScored < indStdScored || indOrgVald >30 && indOrgVald <=90){

						value.put("limit_grade", "14");
						value.put("reason", "到期信用偿还记录，AAA、AA、A级均需满分");
					}
				}else if(graded ==14){
					if(indOrgVald >90){
						value.put("limit_grade", "15");
						value.put("reason", "到期信用偿还记录，超过3个月的,信用等级降为 BB（含）级以下");
					}
				}
				value.put("grade", (String) map.get("grade"));
				retValue =this.contrastHm(map, value);
		return retValue;
	}
	/**
	 *	对所有贷款资产形态的限制等级,AAA级、AA级需为正常类,A级需为关注及以上。	
	 * @param indStdScore 对应的满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String, String> getLimitLoanPropertyPattern(String indexValue,HashMap<String, String> map){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indexValued = Double.parseDouble(indexValue);
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
		
				if(graded <=12 && graded >10){
					if(indexValued >1){
						
						value.put("limit_grade", "13");
						value.put("reason", "贷款资产形态，AAA级、AA级需为正常类");
					}
				}else if(13 ==graded){
					if(indexValued >2){
						
						value.put("limit_grade", "14");
						value.put("reason", "贷款资产形态，AAA级、AA级需为正常类");
					}
				}
				
				value.put("grade", (String) map.get("grade"));
				retValue=this.contrastHm(map, value);
		return retValue;
	}
	/**
	 *	对所有经营性活动现金流量净额的限制等级,AAA、AA级需满分
	 * @param indStdScore 对应的满分值
	 * @param map  输入的模型等级
	 * @param indexValue  指标选项值 
	 * @param indexScore  指标选项得分 
	 * @param indStdScored 指标选项得分为满分
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitManagerActivityCashFlow(String indStdScore,String indexScore,HashMap<String, String> map){
		
		HashMap<String,String> retValue =new HashMap<String,String>();
		HashMap<String,String> value =new HashMap<String,String>();
		double indexScored = Double.parseDouble(indexScore);
		
		String limit_grade =(String) map.get("grade");
		int graded;
		if("".equals(limit_grade) || limit_grade==null){
			graded =0;
		}else{
			graded =Integer.parseInt(limit_grade);
		}
		
		double indStdScored = Double.parseDouble(indStdScore);
		
				if(graded <=12 && graded >10){
					if(indexScored < indStdScored){
						
						value.put("limit_grade", "13");
						value.put("reason", "经营性活动现金流量净额，AAA、AA级需满分");
					}
				}
				value.put("grade", (String) map.get("grade"));
				retValue =this.contrastHm(map, value);
		return retValue;
	}
	/**
	 *	对所有资产负债率的限制等级,(1)AAA级需满分(2)AA级需小于75%,A级需小于85%;(3)如资产负债率大于等于90%信用等级不得超过BBB级。	
	 * @param indStdScore 对应的满分值
	 * @param grade  输入的模型等级
	 * @param indexValue  指标选项值
	 * @param indexScore  指标选项得分
	 * @param indStdScored 指标选项得分为满分
	 * @return retValue
	 * @throws ComponentException
	 */
	public HashMap<String,String> getLimitProptyRateDebt(String indStdScore,String grade,String indexValue,String indexScore){
			
			HashMap<String,String> retValue=new HashMap<String,String>();
			double indStdScored=Double.parseDouble(indStdScore);
			double indexValued=Double.parseDouble(indexValue);
			double indexScored=Double.parseDouble(indexScore);

				if("11".equals(grade)){
					if(indexScored < indStdScored){
						retValue.put("limit_grade", "12");
						retValue.put("reason", "资产负债率，AAA级需满分");
					}
				}else if("12".equals(grade)){
					if(indexValued >=75){
						retValue.put("limit_grade", "13");
						retValue.put("reason", "资产负债率，AA级需小于75%");
					}
				}else if("13".equals(grade)){
					if(indexValued >=85){
						retValue.put("limit_grade", "14");
						retValue.put("reason", "资产负债率，A级需小于85%");
					}
				}
				
				retValue.put("grade", grade);
				
//				if(retValue.get("limit_grade")==null || "".equals(retValue.get("limit_grade"))){
//					retValue.put("limit_grade", "00");
//					retValue.put("reason", "");
//				}
				
			return retValue;
	}		
	
	/*
	 * 对于两个指标限制等级的比较，对应的grade指标值越大，则其信用等级越差
	 */
	public HashMap<String,String> contrastHm(HashMap<String,String> map1,HashMap<String,String> map2){
		
		String grade1s =(String) map1.get("limit_grade");
		String grade2s =(String) map2.get("limit_grade");
		int grade1,grade2;
		if(grade1s !=null || "".equals(grade1s)){
			grade1 =Integer.parseInt(grade1s);
		}else{
			grade1 =0;
		}
		if(grade2s !=null || "".equals(grade2s)){
			grade2 =Integer.parseInt(grade2s);
		}else{
			grade2 =0;
		}
		if(grade1 >=grade2){
			return map1;
		}else{
			return map2;
		}
	}
}
