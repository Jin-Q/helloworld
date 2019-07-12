package com.yucheng.cmis.biz01line.fnc.master.domain;


import com.yucheng.cmis.pub.CMISDomain;

/**
 *@Classname	RptItemData.java
 *@Version 1.0	
 *@Since   1.0 	Oct 13, 2008 2:05:36 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：item信息VO |用户操作具体的报表时，组装sql语句用的数据对象
 *@Lastmodified 
 *@Author
 */
public class RptItemData implements CMISDomain{
	
	  private String cusId		= null;			//客户ID
	  private String tableName	= null;			//表名
	  
	  /**
	   * 报表周期类型 1--月 2--季 3--半年 4--年
	   */
	  private String statPrdStyle = null;	
	  
	  /**
	   * 数据列数
	   * 1 ：代表数据只有一列值
	   * 2 ：代表数据有两列值 分别是期初和期末
	   */
	  private int	 dataColumn = 0;			//期数数
	  private String rptYear	   = null;		//年
	  private String rptMonth	   = null;		//月
	  private String itemId	       = null;   	//项目编号
	  
	  private String fncConfStyle  = null;      //报表类型01资产02损益03现金04财务指标05所有者权益
	  
	  private double data1 = 0.0;				//数值(期初数或者期数)
	  private double data2 = 0.0;				//数值(期末数)
	  
	  private double[] dataA;
	  private double[] dataB;
	  
	  private String[] dateFieldA = new String[8];
	  private String[] dateFieldB = new String[8];

	  /**
	   * 表字段名称(期初数或者期数)
	   */
	  private String dataField1	= null;	
	  /**
	   * 表字段名称(期末数)
	   */
	  private String dataField2 = null;			
	  
	  /**
	   * 报表口径STAT_STYLE
	   * 09-06-27 新增联合主键
	   * 1 本部报表
	   * 2 联合报表
	   * 9 未知
	   */
	 private String statStyle=null;
	 
	  public String getStatStyle() {
		return statStyle;
	}

	public void setStatStyle(String statStyle) {
		this.statStyle = statStyle;
	}

	
	  
	  public RptItemData(){};
	  
	  /**
	   * 构造函数，用户初始化数据
	   * @param cusId			客户ID	
	   * @param tableName		报表名
	   * @param rptCycleType 	报表期间类型 1--月 2--季 3--半年 4--年
	   * @param rptYear			报表年份
	   * @param rptMonth		报表月份
	   * @param itemId			项目编号
	   * @param statPrdStyle	期数数  
	   */
	  public RptItemData(String cusId, String tableName, String rptCycleType,
			  String rptYear, String rptMonth, String itemId, int statPrdStyle,String fncConfStyle){
		  this.cusId = cusId;
		  this.tableName = tableName;
		  this.statPrdStyle = rptCycleType;
		  this.rptYear = rptYear;
		  this.rptMonth = rptMonth;
		  this.itemId = itemId;
		  this.dataColumn = statPrdStyle;
		  this.fncConfStyle = fncConfStyle;
	  };
	  
	  
	  
	  /**
	   * 构造函数，用户初始化数据 wqgang 2009-6-27新增
	   * @param cusId			客户ID	
	   * @param tableName		报表名
	   * @param rptCycleType 	报表期间类型 1--月 2--季 3--半年 4--年
	   * @param rptYear			报表年份
	   * @param rptMonth		报表月份
	   * @param itemId			项目编号
	   * @param statPrdStyle	期数数  
	   * @param statStyle	    报表口径 1-本部 2-合并 9-其他
	   */
	  public RptItemData(String cusId, String tableName, String rptCycleType,
			  String rptYear, String rptMonth, String itemId,String statStyle, 
			  int statPrdStyle,String fncConfStyle){
		  this.cusId = cusId;
		  this.tableName = tableName;
		  this.statPrdStyle = rptCycleType;
		  this.rptYear = rptYear;
		  this.rptMonth = rptMonth;
		  this.itemId = itemId;
		  this.dataColumn = statPrdStyle;
		  this.fncConfStyle = fncConfStyle;
		  this.statStyle=statStyle;
	  };
	  
	  /**
	   * 
	   * @param cusId
	   * @param tableName
	   * @param rptCycleType
	   * @param rptYear
	   * @param rptMonth
	   * @param itemId
	   * @param statPrdStyle
	   */
	  public RptItemData(String cusId, String tableName, String rptCycleType,
			  String rptYear, String rptMonth, String itemId, int statPrdStyle){
		  this.cusId = cusId;
		  this.tableName = tableName;
		  this.statPrdStyle = rptCycleType;
		  this.rptYear = rptYear;
		  this.rptMonth = rptMonth;
		  this.itemId = itemId;
		  this.dataColumn = statPrdStyle;
	  };
	  
	  /**
	   * wqgang 2009-6-27新增
	   * @param cusId
	   * @param tableName
	   * @param rptCycleType
	   * @param rptYear
	   * @param rptMonth
	   * @param itemId
	   * @param statStyle
	   * @param statPrdStyle
	   */
	  public RptItemData(String cusId, String tableName, String rptCycleType,
			  String rptYear, String rptMonth, String itemId,String statStyle, int statPrdStyle){
		  this.cusId = cusId;
		  this.tableName = tableName;
		  this.statPrdStyle = rptCycleType;
		  this.rptYear = rptYear;
		  this.rptMonth = rptMonth;
		  this.itemId = itemId;
		  this.dataColumn = statPrdStyle;
		  this.statStyle=statStyle;
	  };
	  
	  
	  
	  /**
	   * 根据设置的成员变量拼装一个项目信息的新增sql语句
	   * @return sql 例如： INSERT INTO fnc_stat_bs(cus_id,stat_year,stat_item_id,stat_item_amt) 
	   *  VALUES('01','01','01',0.0);
	   * @throws Exception
	   */
	  public String AssembleInsertSql() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth 
				  || (null == this.itemId) || 0 == this.dataColumn){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  if(null == this.dataField1){
			  this.initdataField();
		  }
		  
		  StringBuffer sb = new StringBuffer();
		  if("05".equals(this.fncConfStyle)){
			  sb.append("INSERT INTO " + this.tableName + " (cus_id,stat_year,stat_item_id,stat_prd,stat_prd_style," +this.dataField1);
		  }else{
			  sb.append("INSERT INTO " + this.tableName + " (cus_id,stat_year,stat_item_id," +this.dataField1);
		  }
		  
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 start
		   */
		  if(null!=this.statStyle){
			  sb.append(",stat_style");  
		  }
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 end
		   */
		  
		  
		  if(null != this.dataField2){
			  sb.append("," + this.dataField2);
		  }
		  sb.append(")").append(" VALUES('" + this.cusId);
		  if("05".equals(this.fncConfStyle)){
			  sb.append("','" + this.rptYear + "','" + this.itemId + "','"+this.rptYear+this.rptMonth+"','"+this.statPrdStyle+"'," + this.data1);
		  }else{
			  sb.append("','" + this.rptYear + "','" + this.itemId + "'," + this.data1);
		  }
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 start
		   */
		  if(null!=this.statStyle){
			  sb.append(","+this.statStyle);  
		  }
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 end
		   */
		  
		  
		  if(null != this.dataField2){
			  sb.append("," + this.data2);
		  }
		  sb.append(")");

		  return sb.toString();
		  
	  }
	  
	  public String AssembleInsertSql8() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth 
				  || (null == this.itemId) || 0 == this.dataColumn){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  //if(null == this.dateFieldA){
			  this.initdataField();
		 // }
		  
		  StringBuffer sb = new StringBuffer();

		  sb.append("INSERT INTO " + this.tableName + " (cus_id,stat_year,stat_item_id,stat_prd,stat_prd_style");
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 start
		   */
		  if(null!=this.statStyle){
			  sb.append(",stat_style");  
		  }
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 end
		   */
		  for(int i=0;i<this.dateFieldA.length;i++){
			  sb.append("," + dateFieldA[i]);
		  }
		 // if(null != this.dateFieldB){
			  for(int j=0;j<this.dateFieldB.length;j++){
				  sb.append("," + dateFieldB[j]);
			  }
		 // }
		  sb.append(")").append(" VALUES('" + this.cusId);

		  sb.append("','" + this.rptYear + "','" + this.itemId + "','"+this.rptYear+this.rptMonth+"','"
				  +this.statPrdStyle + "'");
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 start
		   */
		  if(null!=this.statStyle){
			  sb.append(this.statStyle+",");  
		  }
		  
		  /*
		   * wqgang 2009-6-27  新增报表口径主键处理 end
		   */
		  
		  
		  for(int m=0;m<this.dataA.length;m++){
			  sb.append("," + dataA[m]);
		  }
		  for(int n=0;n<this.dataB.length;n++){
			  sb.append("," + dataB[n]);
		  }

		  sb.append(")");

		  return sb.toString();
		  
	  }
	  
	  /**
	   * 根据设置的成员变量拼装一个项目信息的更新sql语句
	   * @return sql 例如： UPDATE fnc_stat_bs SET stat_item_amt=0.0 
	   * WHERE cus_id='01' AND stat_year='01' AND stat_item_id='01'
	   * @throws Exception
	   */
	  public String AssembleUpdateSql() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth 
				  || (null == this.itemId) || 0 == this.dataColumn|| null==this.statStyle){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  if(null == this.dataField1){
			  this.initdataField();
		  }
		  
		  StringBuffer sb = new StringBuffer();
		  sb.append("UPDATE " + this.tableName);
		  sb.append(" SET " + this.dataField1 + "=" + this.data1);
		  if(null != this.dataField2){
			  sb.append("," + this.dataField2 + "=" + this.data2);
		  }
		  sb.append(" WHERE cus_id='" + this.cusId + "' AND stat_year='" + this.rptYear)
		  	.append("' AND stat_item_id='" + this.itemId+"' AND stat_style='" + this.statStyle).append("'");
		  
		  return sb.toString();  
	  }
	  public String AssembleUpdateSql8() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth 
				  || (null == this.itemId) || 0 == this.dataColumn|| null==this.statStyle){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  //if(null == this.dateFieldA){
			  this.initdataField();
		  //}
		  
		  StringBuffer sb = new StringBuffer();
		  sb.append("UPDATE " + this.tableName);
		  sb.append(" SET ");
		  for(int m=0; m<this.dateFieldA.length; m++){
			  if(m ==this.dateFieldA.length-1){
				  sb.append(this.dateFieldA[m] + "=" + this.dataA[m]);
			  }else{
				  sb.append(this.dateFieldA[m] + "=" + this.dataA[m] + ",");
			  }
		  }
		  //if(null != this.dateFieldB){
			  for(int n=0; n<this.dateFieldB.length; n++){
				  sb.append("," + this.dateFieldB[n] + "=" + this.dataB[n]);
			  } 
		  //}
		  sb.append(" WHERE cus_id='" + this.cusId + "' AND stat_year='" + this.rptYear)
		  	.append("' AND stat_item_id='" + this.itemId+"' AND stat_style='" + this.statStyle).append("'");
		  
		  return sb.toString();  
	  }
	  
	  /**
	   * 根据设置的成员变量拼装一个项目信息的查询客户ID的sql语句，
	   * @return SELECT cus_id FROM  fnc_stat_bs WHERE 
	   * cus_id='01' AND stat_year='01' AND stat_item_id='01'
	   * @throws Exception
	   */
	  public String AssembleQuerySql() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth 
				  || (null == this.itemId) || 0 == this.dataColumn|| null==this.statStyle){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  if(null == this.dataField1){
			  this.initdataField();
		  }
		  StringBuffer sb = new StringBuffer();
		  sb.append("SELECT cus_id FROM " + this.tableName);
		  sb.append(" WHERE cus_id='" + this.cusId + "' AND stat_year='" + this.rptYear)
		  	.append("' AND stat_item_id='" + this.itemId+"' AND stat_style='" + this.statStyle).append("'");
		  
		  return sb.toString();
	  }
	  
	  /**
	   * 根据设置的成员变量拼装一个项目信息的查询客户ID的sql语句，
	   * @return UPDATE fnc_stat_bs SET stat_item_amt=0.0 
	   * WHERE cus_id='01' AND stat_year='01'
	   * @throws Exception
	   */
	  public String AssembleFncStatSql() throws Exception{
		  if((null == this.cusId) ||(null == this.tableName) || (null == this.statPrdStyle)
				  || (null) == this.rptYear || (null) == this.rptMonth
				  || 0 == this.dataColumn|| null==this.statStyle){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  if(null == this.dataField1){
			  this.initdataField();
		  }
		  
		  StringBuffer sb = new StringBuffer();
		  sb.append("UPDATE " + this.tableName);
		  sb.append(" SET " + this.dataField1 + "=" + 0.0);
		  if(null != this.dataField2){
			  sb.append("," + this.dataField2 + "=" + 0.0);
		  }
		  sb.append(" WHERE cus_id='" + this.cusId + "' AND stat_year='" 
				  + this.rptYear+"' AND stat_style='" + this.statStyle)
		  	.append("'");
		  
		  return sb.toString();
	  }
	  
	  /**
	   * 根据成员变量的报表口径，和期数数确定要操作的字段名称并初始化
	   * 成员变量 dataField1 和 dataField2
	   * 
	   *  暂时废弃不用  暂时废弃不用  暂时废弃不用  暂时废弃不用 
	   *  
	   * @throws Exception
	   */
	  public void initdataField_() throws Exception{
		  
		  if((null == this.statPrdStyle) || (null) == this.rptMonth 
				  || 0 == this.dataColumn){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  int iMonth = Integer.parseInt(this.rptMonth);		//用于存储报表月份
		  
		  if("1".equals(this.statPrdStyle)){	//月
			  
			  if(1 == this.dataColumn){	//期数1
				  this.dataField1 = "STAT_INIT_AMT" + iMonth;
			  }else if(2 == this.dataColumn){	//期数2
				  this.dataField1 = "STAT_INIT_AMT" + iMonth;
				  this.dataField2 = "STAT_END_AMT" + iMonth;
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
			  
		  }else if("2".equals(this.statPrdStyle)){ //季度

			  if(1 == this.dataColumn){	//期数1
				  if(3 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q1";
				  }else if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q2";
				  }else if(9 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q3";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q4";
				  }else{
					  throw new Exception("输入的月份不符，季报对应的会计期间为03、06、09、12!");
				  }
			  }else if(2 == this.dataColumn){	//期数2
				  if(3 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q1";
					  this.dataField2 = "STAT_END_AMT_Q1";
				  }else if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q2";
					  this.dataField2 = "STAT_END_AMT_Q2";
				  }else if(9 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q3";
					  this.dataField2 = "STAT_END_AMT_Q3";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q4";
					  this.dataField2 = "STAT_END_AMT_Q4";
				  }else{
					  throw new Exception("输入的月份不符，季报对应的会计期间为03、06、09、12!");
				  }
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
		  }else if("3".equals(this.statPrdStyle)){	//半年
			  if(1 == this.dataColumn){	//期数1
				  if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y1";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y1";
				  }else{
					  throw new Exception("输入的月份不符，半年报对应的会计期间应为06、12!");
				  }
			  }else if(2 == this.dataColumn){	//期数2
				  if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y1";
					  this.dataField2 = "STAT_END_AMT_Y1";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y2";
					  this.dataField2 = "STAT_END_AMT_Y2";
				  }else{
					  throw new Exception("输入的月份不符，半年报对应的会计期间应为06、12!");
				  }
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
		  }else if("4".equals(this.statPrdStyle)){	//年
			  	
			  if(1 == this.dataColumn){	//期数1
				  this.dataField1 = "STAT_INIT_AMT_Y";
			  }else if(2 == this.dataColumn){	//期数2
				  this.dataField1 = "STAT_INIT_AMT_Y" ;
				  this.dataField2 = "STAT_END_AMT_Y";
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
			  
		  }
		 
	  }
	  
	  /**
	   * 根据成员变量的报表口径，和期数数确定要操作的字段名称并初始化
	   * 成员变量 dataField1 和 dataField2 
	   * @throws Exception
	   */
	  public void initdataField() throws Exception{
		  
		  if((null == this.statPrdStyle) || (null) == this.rptMonth 
				  || 0 == this.dataColumn){
			  
			  throw new Exception("初始化对象的数据不完整，不能生成指定sql");
			  
		  }
		  
		  int iMonth = Integer.parseInt(this.rptMonth);		//用于存储报表月份
		  
		  if("1".equals(this.statPrdStyle)){	//月
			  
			  if(1 == this.dataColumn){	//期数1
				  this.dataField1 = "STAT_INIT_AMT" + iMonth;
			  }else if(2 == this.dataColumn){	//期数2
				  this.dataField1 = "STAT_INIT_AMT" + iMonth;
				  this.dataField2 = "STAT_END_AMT" + iMonth;
			  }else if(8 == this.dataColumn){
				  dateFieldA[7] = "STAT_INIT_AMT" + iMonth;
				  dateFieldB[7] = "STAT_END_AMT" + iMonth;
				  
				  dateFieldA[0] = "STAT_INIT_AMTA" + iMonth;
				  dateFieldB[0] = "STAT_END_AMTA" + iMonth;
				  
				  dateFieldA[1] = "STAT_INIT_AMTB" + iMonth;
				  dateFieldB[1] = "STAT_END_AMTB" + iMonth;
				  
				  dateFieldA[2] = "STAT_INIT_AMTC" + iMonth;
				  dateFieldB[2] = "STAT_END_AMTC" + iMonth;
				  
				  dateFieldA[3] = "STAT_INIT_AMTD" + iMonth;
				  dateFieldB[3] = "STAT_END_AMTD" + iMonth;
				  
				  dateFieldA[4] = "STAT_INIT_AMTE" + iMonth;
				  dateFieldB[4] = "STAT_END_AMTE" + iMonth;
				  
				  dateFieldA[5] = "STAT_INIT_AMTF" + iMonth;
				  dateFieldB[5] = "STAT_END_AMTF" + iMonth;
				  
				  dateFieldA[6] = "STAT_INIT_AMTG" + iMonth;
				  dateFieldB[6] = "STAT_END_AMTG" + iMonth;
				  
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
			  
		  }else if("2".equals(this.statPrdStyle)){ //季度

			  if(1 == this.dataColumn){	//期数1
				  if(3 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q1";
				  }else if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q2";
				  }else if(9 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q3";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q4";
				  }else{
					  throw new Exception("输入的月份不符，季报对应的会计期间为03、06、09、12!");
				  }
			  }else if(2 == this.dataColumn){	//期数2
				  if(3 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q1";
					  this.dataField2 = "STAT_END_AMT_Q1";
				  }else if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q2";
					  this.dataField2 = "STAT_END_AMT_Q2";
				  }else if(9 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q3";
					  this.dataField2 = "STAT_END_AMT_Q3";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Q4";
					  this.dataField2 = "STAT_END_AMT_Q4";
				  }else{
					  throw new Exception("输入的月份不符，季报对应的会计期间为03、06、09、12!");
				  }
			  }else if(8 == this.dataColumn){
				  if(3 == iMonth || 6 == iMonth || 9 == iMonth || 12 == iMonth){
					  dateFieldA[7] = "STAT_INIT_AMT_Q" + iMonth/3;
					  dateFieldB[7] = "STAT_END_AMT_Q" + iMonth/3;
					  
					  dateFieldA[0] = "STAT_INIT_AMTA_Q" + iMonth/3;
					  dateFieldB[0] = "STAT_END_AMTA_Q" + iMonth/3;
					  
					  dateFieldA[1] = "STAT_INIT_AMTB_Q" + iMonth/3;
					  dateFieldB[1] = "STAT_END_AMTB_Q" + iMonth/3;
					  
					  dateFieldA[2] = "STAT_INIT_AMTC_Q" + iMonth/3;
					  dateFieldB[2] = "STAT_END_AMTC_Q" + iMonth/3;
					  
					  dateFieldA[3] = "STAT_INIT_AMTD_Q" + iMonth/3;
					  dateFieldB[3] = "STAT_END_AMTD_Q" + iMonth/3;
					  
					  dateFieldA[4] = "STAT_INIT_AMTE_Q" + iMonth/3;
					  dateFieldB[4] = "STAT_END_AMTE_Q" + iMonth/3;
					  
					  dateFieldA[5] = "STAT_INIT_AMTF_Q" + iMonth/3;
					  dateFieldB[5] = "STAT_END_AMTF_Q" + iMonth/3;
					  
					  dateFieldA[6] = "STAT_INIT_AMTG_Q" + iMonth/3;
					  dateFieldB[6] = "STAT_END_AMTG_Q" + iMonth/3;
				  }else{
					  throw new Exception("输入的月份不符，季报对应的会计期间为03、06、09、12!");
				  }
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
		  }else if("3".equals(this.statPrdStyle)){	//半年
			  if(1 == this.dataColumn){	//期数1
				  if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y1";
				  }else if(12 == iMonth){
				  // this.dataField1 = "STAT_INIT_AMT_Y1";
				  // modified by yangdong 2009-11-21    下半年期初数 
					  this.dataField1 = "STAT_INIT_AMT_Y2";
				  }else{
					  throw new Exception("输入的月份不符，半年报对应的会计期间应为06、12!");
				  }
			  }else if(2 == this.dataColumn){	//期数2
				  if(6 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y1";
					  this.dataField2 = "STAT_END_AMT_Y1";
				  }else if(12 == iMonth){
					  this.dataField1 = "STAT_INIT_AMT_Y2";
					  this.dataField2 = "STAT_END_AMT_Y2";
				  }else if(8 == this.dataColumn){
					  if(6 == iMonth || 12 == iMonth){
						  dateFieldA[7] = "STAT_INIT_AMT_Y" + iMonth/6;
						  dateFieldB[7] = "STAT_END_AMT_Y" + iMonth/6;
						  
						  dateFieldA[0] = "STAT_INIT_AMTA_Y" + iMonth/6;
						  dateFieldB[0] = "STAT_END_AMTA_Y" + iMonth/6;
						  
						  dateFieldA[1] = "STAT_INIT_AMTB_Y" + iMonth/6;
						  dateFieldB[1] = "STAT_END_AMTB_Y" + iMonth/6;
						  
						  dateFieldA[2] = "STAT_INIT_AMTC_Y" + iMonth/6;
						  dateFieldB[2] = "STAT_END_AMTC_Y" + iMonth/6;
						  
						  dateFieldA[3] = "STAT_INIT_AMTD_Y" + iMonth/6;
						  dateFieldB[3] = "STAT_END_AMTD_Y" + iMonth/6;
						  
						  dateFieldA[4] = "STAT_INIT_AMTE_Y" + iMonth/6;
						  dateFieldB[4] = "STAT_END_AMTE_Y" + iMonth/6;
						  
						  dateFieldA[5] = "STAT_INIT_AMTF_Y" + iMonth/6;
						  dateFieldB[5] = "STAT_END_AMTF_Y" + iMonth/6;
						  
						  dateFieldA[6] = "STAT_INIT_AMTG_Y" + iMonth/6;
						  dateFieldB[6] = "STAT_END_AMTG_Y" + iMonth/6;
					  }else{
						  throw new Exception("输入的月份不符，半年报对应的会计期间应为06、12!");
					  }
				  }else{
					  throw new Exception("输入的月份不符，半年报对应的会计期间应为06、12!");
				  }
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
		  }else if("4".equals(this.statPrdStyle)){	//年
			  	
			  if(1 == this.dataColumn){	//期数1
				  this.dataField1 = "STAT_INIT_AMT_Y";
			  }else if(2 == this.dataColumn){	//期数2
				  this.dataField1 = "STAT_INIT_AMT_Y" ;
				  this.dataField2 = "STAT_END_AMT_Y";
			  }else if(8 == this.dataColumn){
				  if(12 == iMonth){
					  dateFieldA[7] = "STAT_INIT_AMT_Y";
					  dateFieldB[7] = "STAT_END_AMT_Y";
					  
					  dateFieldA[0] = "STAT_INIT_AMTA_Y";
					  dateFieldB[0] = "STAT_END_AMTA_Y";
					  
					  dateFieldA[1] = "STAT_INIT_AMTB_Y";
					  dateFieldB[1] = "STAT_END_AMTB_Y";
					  
					  dateFieldA[2] = "STAT_INIT_AMTC_Y";
					  dateFieldB[2] = "STAT_END_AMTC_Y";
					  
					  dateFieldA[3] = "STAT_INIT_AMTD_Y";
					  dateFieldB[3] = "STAT_END_AMTD_Y";
					  
					  dateFieldA[4] = "STAT_INIT_AMTE_Y";
					  dateFieldB[4] = "STAT_END_AMTE_Y";
					  
					  dateFieldA[5] = "STAT_INIT_AMTF_Y";
					  dateFieldB[5] = "STAT_END_AMTF_Y";
					  
					  dateFieldA[6] = "STAT_INIT_AMTG_Y";
					  dateFieldB[6] = "STAT_END_AMTG_Y";
				  }else{
					  throw new Exception("输入的月份不符，半年报对应的会计期间应为12!");
				  }
			  }else{
				  throw new Exception("表fnc_conf_styles中fnc_conf_data_column字段值非法只能是1或者2！");
			  }
			  
		  }
		 
	  }
	 

	/**
	 * @return the statPrdStyle
	 */
	public int getStatPrdStyle() {
		return dataColumn;
	}

	/**
	 * @param statPrdStyle the statPrdStyle to set
	 */
	public void setStatPrdStyle(int statPrdStyle) {
		this.dataColumn = statPrdStyle;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the cusId
	 */
	public String getCusId() {
		return cusId;
	}
	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	/**
	 * @return the data1
	 */
	public double getData1() {
		return data1;
	}
	/**
	 * @param data1 the data1 to set
	 */
	public void setData1(double data1) {
		this.data1 = data1;
	}
	/**
	 * @return the data2
	 */
	public double getData2() {
		return data2;
	}
	/**
	 * @param data2 the data2 to set
	 */
	public void setData2(double data2) {
		this.data2 = data2;
	}
	/**
	 * @return the dataField1
	 */
	public String getDataField1() {
		return dataField1;
	}
	/**
	 * @param dataField1 the dataField1 to set
	 */
	public void setDataField1(String dataField1) {
		this.dataField1 = dataField1;
	}
	/**
	 * @return the dataField2
	 */
	public String getDataField2() {
		return dataField2;
	}
	/**
	 * @param dataField2 the dataField2 to set
	 */
	public void setDataField2(String dataField2) {
		this.dataField2 = dataField2;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the rptCycleType
	 */
	public String getRptCycleType() {
		return statPrdStyle;
	}
	/**
	 * @param rptCycleType the rptCycleType to set
	 */
	public void setRptCycleType(String rptCycleType) {
		this.statPrdStyle = rptCycleType;
	}
	/**
	 * @return the rptMonth
	 */
	public String getRptMonth() {
		return rptMonth;
	}
	/**
	 * @param rptMonth the rptMonth to set
	 */
	public void setRptMonth(String rptMonth) {
		this.rptMonth = rptMonth;
	}
	/**
	 * @return the rptYear
	 */
	public String getRptYear() {
		return rptYear;
	}
	/**
	 * @param rptYear the rptYear to set
	 */
	public void setRptYear(String rptYear) {
		this.rptYear = rptYear;
	}
	  
	  
	public int getDataColumn() {
		return dataColumn;
	}

	public void setDataColumn(int dataColumn) {
		this.dataColumn = dataColumn;
	}

	public String getFncConfStyle() {
		return fncConfStyle;
	}

	public void setFncConfStyle(String fncConfStyle) {
		this.fncConfStyle = fncConfStyle;
	}

	public void setStatPrdStyle(String statPrdStyle) {
		this.statPrdStyle = statPrdStyle;
	}
	
	public double[] getDataA() {
		return dataA;
	}

	public void setDataA(double[] dataA) {
		this.dataA = dataA;
	}

	public double[] getDataB() {
		return dataB;
	}

	public void setDataB(double[] dataB) {
		this.dataB = dataB;
	}
	
	

	public String[] getDateFieldA() {
		return dateFieldA;
	}

	public void setDateFieldA(String[] dateFieldA) {
		this.dateFieldA = dateFieldA;
	}

	public String[] getDateFieldB() {
		return dateFieldB;
	}

	public void setDateFieldB(String[] dateFieldB) {
		this.dateFieldB = dateFieldB;
	}

	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 


}
