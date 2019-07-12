package com.yucheng.cmis.pub;

import java.util.HashMap;

public class CcrPubConstant {
	
	/**
	 * 
	 * 模型
	 * 
	 */
	public static final String IND_COMPONENT="ind";
	public static final String CCR_COMPONENT="CcrComponent";
	public static final String CCR_INDEX_KCOLL_NAME="CcrKcoll";
	
	public static final String CCR_AGENT="CcrAgent";
	public static final String CCR_APPINFO="CcrAppInfo";
	public static final String CCR_ADDINF="CcrAddInf";
	public static final String CCR_APPFINGUAR="CcrAppFinGuar";
	public static final String CCR_APPDETAIL="CcrAppDetail";
	public static final String CCR_APPDETAILHIS="CcrAppDetailHis";
	public static final String CCR_BATCHAPPINFO="CcrBatchAppInfo";
	
	public static final String CCR_MODELSCORE = "CcrModelScore";
	public static final String CCR_MGROUPSCORE = "CcrMGroupScore";
	public static final String CCR_MODSCRHIS = "CcrModScrHis";
	public static final String CCR_MODGRPSCRHIS = "CcrModgrpScrHis";
	public static final String CCR_GINDSCORE = "CcrGIndScore";
	public static final String CCR_GRPINDSCRHIS = "CcrGrpindScrHis";
	public static final String TABLENAME_CCR_MODELSCORE = "CCR_MODEL_SCORE";
	public static final String TABLENAME_CCR_MGROUPSCORE = "CCR_M_GROUP_SCORE";
	public static final String TABLENAME_CCR_GINDSCORE = "CCR_G_IND_SCORE";
	public static final String TABLENAME_CCR_MODSCRHIS = "CCR_MOD_SCR_HIS";
	public static final String TABLENAME_CCR_MODGRPSCRHIS = "CCR_MODGRP_SCR_HIS";
	public static final String TABLENAME_CCR_GRPINDSCRHIS = "CCR_GRPIND_SCR_HIS";
	public static final String TABLENAME_CCR_APPDETAIL="CCR_APP_DETAIL";
	
	
	//限制等级
	public static final String CcrLimitGrade = "CcrLimitGrade";
	
	public static final String CCRAPPINFO_SERNO = "ccrAddInfo.serno";
	public static final String CLASSPATH_CCR_MGROUPSCORE="com.yucheng.cmis.biz01line.ccr.domain.CcrMGroupScore"; 
	/**
	 * 指标得分domain的classpath
	 */
	public static final String CLASSPATH_CCR_GINDSCORE="com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore"; 
	/**
	 * 评级明细信息的classPath
	 */
	public static final String CLASSPATH_CCR_APPDETAIL="com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail"; 


	/**
	 * 字典项
	 * 1:未办结
	 * 2:已办结
	 */
	public static final String STD_ZB_END_STA_NOT_END="1"; 
	public static final String STD_ZB_END_STA_END="2"; 
	
	/**
	 * 评级审批提供给流程调用的接口
	 */
	public static final String CCR_APPROVE_INTERFACE_ID="CcrAppInterface"; 
	/**
	 * 评级审批提供给其他模块,获取评级信息的接口
	 */
	public static final String CCR_QUERY_APP_INTERFACE_ID="CcrQueryAppInterface"; 
	

	
	public static final String DEFAULT_DATE = "1900-01-01";
	/*农户批量模型编号*/
	public static final String BATCH_MODEL_NO="M20090300035"; 
	
	/*企业客户类型编号首位*/
	public static final String COM_CUS_TYPE_FIRST_NO="2"; 
	
	/*个人客户类型编号首位*/
	public static final String INDIV_CUS_TYPE_FIRST_NO="1"; 
	
	/*婚姻状况指标编号*/
	public static final String INDIV_MAR_ST = "I20090300120";
	
	/*健康状况指标编号*/
	public static final String INDIV_HEAL_ST = "I20090300128";
	
	/*职务指标编号*/
    public static final String INDIV_COM_JOB_TTL = "I20090300158";
    
    /*年龄指标编号*/
    public static final String CUS_AGE = "I20090300119";
    
    /*学历指标编号*/
    public static final String INDIV_EDT = "I20090300123";
    
    /*工作年限指标编号*/
    
    public static final String INDIV_WORK_JOB_Y = "I20090300160";
    
    
    /*农户供养人口指标编号*/
    public static final String FARMER_PRI_NUM_INDNO= "I20090300333";
    
    /*农户居住时间指标编号*/
    public static final String FARMER_LIVE_TIME_INDNO= "I20090300125";
    /*农户个人品行指标编号*/
    public static final String FARMER_BEHAVI_INDNO= "I20090300396";
    /*农户生产经营能力指标编号*/
    public static final String FARMER_OPER_ABILITY_INDNO= "I20090300335";
    /*农户项目潜质指标编号*/
    public static final String FARMER_PRJ_POTENT_INDNO= "I20090300132";
    
    /*农户税费缴纳情况指标编号*/
    public static final String FARMER_TAX_FEE_INDNO= "I20090600396";
    /*农户个人银行信用记录指标编号*/
    public static final String FARMER_CRD_NOTES_INDNO= "I20090600395";
    /*农户其他信用记录指标编号*/
    public static final String FARMER_OTH_CRD_NOTES_INDNO= "I20090300177";
    /*农户家庭收入水平指标编号*/
    public static final String FARMER_FMLY_INC_LEV_INDNO= "I20090300336";
    /*农户家庭资产负债率指标编号*/
    public static final String FARMER_DEBT_RAT_INDNO= "I20090300337";
    /*农户家庭财产水平指标编号*/
    public static final String FARMER_ASS_LEV_INDNO= "I20090300397";
    /*集团客户类型:子公司类型*/
    public static String GRP_CUS_TYPE_2="2";
    /*企业行业类型与行业标准值对应关系*/
    public static HashMap<String,String> CCR_COM_FLD_TYPE=new HashMap<String,String>();
    /*事业单位行业类型与行业标准值对应关系*/
    public static HashMap<String,String> CCR_FAC_FLD_TYPE = new HashMap<String,String>();
    
    /*嘉兴小企业*/
    public static HashMap<String,String> CCR_COM_SMALL_FLD_TYPE = new HashMap<String,String>();
    public static HashMap<String,String> CCR_COM_FLD_TYPE_JX=new HashMap<String,String>();
    
    
    public static HashMap<String,String> CCR_CAREER_INDEX_MAP = new HashMap<String,String>();
    /*老行业标准                                                                              2012新行业标准                                                           
     *A	农、林、牧、渔业                                             A	农、林、牧、渔业                
      B	采矿业                                                                      B	采矿业                          
      C	制造业                                                                      C	制造业                          
      D	电力、燃气及水的生产和供应业                             D	电力、热力、燃气及水生产和供应业
      E	建筑业                                                                      E	建筑业                          
      F	交通运输、仓储和邮政业                               F	批发和零售业                    
      G	信息传输、计算机服务和软件业                G	交通运输、仓储和邮政业          
      H	批发和零售业                                                       H	住宿和餐饮业                    
      I	住宿和餐饮业                                                     I	信息传输、软件和信息技术服务业  
      J	金融业                                                                    J	金融业                          
      K	房地产业                                                             K	房地产业                        
      L	租赁和商务服务业                                          L	租赁和商务服务业                
      M	科学研究、技术服务和地质勘查业                           M	科学研究和技术服务业            
      N	水利、环境和公共设施管理业                               N	水利、环境和公共设施管理业      
      O	居民服务和其他服务业                                     O	居民服务、修理和其他服务业      
      P	教育                                                                            P	教育                            
      Q	卫生、社会保障和社会福利业                               Q	卫生和社会工作                  
      R	文化、体育和娱乐业                                       R	文化、体育和娱乐业              
      S	公共管理和社会组织                                       S	公共管理、社会保障和社会组织    
      T	国际组织                                                          T	国际组织
      F-I变了
      f-----g
      g-----i
      h-----f
      i-----h
       */   


    static{
    	//企业对应表
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("A", "0001");  //A	农、林、牧、渔业	农林牧渔业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("B", "0009");  //B	采矿业	工业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("C", "0005");  //C	制造业	工业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("D", "0009");  //D	电力、燃气及水的生产和供应业	工业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("E", "0002");  //E	建筑业	建筑业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("G", "0004"); //	铁路运输业	交通运输仓储及邮政业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("I", "0009"); //	电子计算机业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("51", "0006"); //	商贸批发及进出口类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("52", "0007"); //	商贸零售
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("H","0008");   //I	住宿和餐饮业	住宿和餐饮业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("J","0009");   //J	金融业	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("K","0003");   //K	房地产业	房地产业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("L","0009");  //	租赁业	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("M","0009");  //	商务服务业	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("N","0009");  //	研究与试验发展	社会服务业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("O","0009");  //	专业技术服务业	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("P","0009");   //P	教育	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("Q","0009");   //Q	卫生、社会保障和社会福利业	综合类
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("R","0009");  //	新闻出版业	传播与文化业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("S","0009");  //	广播、电视、电影和音像业	传播与文化业
    	CcrPubConstant.CCR_COM_FLD_TYPE_JX.put("T","0009");   //T	国际组织	综合类

    	//小企业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("A", "0010");  //A	农、林、牧、渔业	农林牧渔业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("B", "0010");  //B	采矿业	工业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("C", "0010");  //C	制造业	工业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("D", "0010");  //D	电力、燃气及水的生产和供应业	工业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("E", "0010");  //E	建筑业	建筑业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("G", "0010"); //	铁路运输业	交通运输仓储及邮政业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("I", "0010"); //	电子计算机业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("F", "0011"); //	商贸批发及进出口类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("H","0010");   //I	住宿和餐饮业	住宿和餐饮业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("J","0010");   //J	金融业	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("K","0010");   //K	房地产业	房地产业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("L","0010");  //	租赁业	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("M","0010");  //	商务服务业	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("N","0010");  //	研究与试验发展	社会服务业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("O","0010");  //	专业技术服务业	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("P","0010");   //P	教育	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("Q","0010");   //Q	卫生、社会保障和社会福利业	综合类
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("R","0010");  //	新闻出版业	传播与文化业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("S","0010");  //	广播、电视、电影和音像业	传播与文化业
    	CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put("T","0010");   //T	国际组织	综合类  
    }
    static{
    	/*事业单位财务指标与评级指标映射关系*/
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300392",FNCPubConstant.CAREER_ZCFZL_ID);//资产负债率-事业单位
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300393",FNCPubConstant.CAREER_ZZSRZB_ID);//自主收入占比
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300394",FNCPubConstant.CAREER_HYFZL_ID);//或有负债率
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300391",FNCPubConstant.CAREER_SZJYL_ID);//收支节余率
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300388",FNCPubConstant.CAREER_JFZJL_ID);//经费自给率
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300389",FNCPubConstant.CAREER_ZSRZZL_ID);//总收入增长率
    	CcrPubConstant.CCR_CAREER_INDEX_MAP.put("I20090300390",FNCPubConstant.CAREER_JZCZZL_ID);//净资产增长率
    }
    /**
     * 根据行业类型编号取对应的行业标准
     * @param key
     * @return
     */
    public static String getComLlyType(String key){ 
    	return CCR_COM_FLD_TYPE_JX.get(key);
    }
    
    /**
     * 根据事业单位行业类型编号取对应的行业标准
     * @param key
     * @return
     */
    public static String getFacLlyType(String key){ 
    	return CCR_FAC_FLD_TYPE.get(key);
    }
    
    public static String getSmallComLlyType(String key){
    	return CCR_COM_SMALL_FLD_TYPE.get(key);
    }
   
    // 评级取财报数据的时候 取的报表类型 1:月报 2:季报 3:半年报 4:年报
    public static String CCR_QUERY_FNC_TERM_TYPE = "1";		

    
}
