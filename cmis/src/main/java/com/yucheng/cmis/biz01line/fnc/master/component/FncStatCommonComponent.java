package com.yucheng.cmis.biz01line.fnc.master.component;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import bsh.EvalError;
import bsh.Interpreter;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncStatCommonAgent;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatCommonDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.RptItemData;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.SheetVO;
import com.yucheng.cmis.pub.util.StringUtil;

/**
 *@Classname	FncStatCommonComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 10, 2008 10:17:32 AM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：通用的报表数据信息业务代理类，对报表数据信息的检验、计算和存取操作。
 *@Lastmodified 
 *@Author
 */
public class FncStatCommonComponent extends CMISComponent{
	private final int FLAGLENGTH = 9; 
	private final char NOADD = '0';
	private final char ADD = '2';
	private final char TEMPADD = '1';
	
	
	/**
	 * 新增一份报表记录，根据传入的参数。初始化具体的报表项目信息并从数据库中取出已经存在的改报表的所有项目信息
	 * 与之比对，如果不存在这新增一条项目信息，存在则更新该条项目信息
	 * @param pfncConfStyles	用excel文件中的一个sheet封装的一张报表信息
	 * @param pfncStatBase		报表基本信息
	 * @return	信息标志(具体参考CMISMessage中定义)
	 * @throws ComponentException
	 */
	public String addFncStat(FncConfStyles pfncConfStyles,FncStatBase pfncStatBase 
			) throws ComponentException{
		
		String flagInfo = CMISMessage.DEFEAT;	//默认失败
		
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		/*
		 * 从传入参数中得到需要的数据
		 */
		String cusId 	= pfncStatBase.getCusId();							//客户ID		
		String statYear   = pfncStatBase.getStatPrd().substring(0,4);		//报表年
		String statMonth 	  = pfncStatBase.getStatPrd().substring(4, 6);	//月份
		String statPrdStyle   = pfncStatBase.getStatPrdStyle();				//报表类型
		String tableName 	  = pfncConfStyles.getFncName();				//数据库物理表名
		int	fncConfDataColumn = pfncConfStyles.getFncConfDataCol();		    //表的列数				
		String statFlag = pfncStatBase.getStateFlg();						//报表状态 
		String statStyle = pfncStatBase.getStatStyle();						//报表口径
		String fncType = pfncStatBase.getFncType();							//报表类型
		ArrayList<String> sqlList = new ArrayList<String>();				//存储生成的多条sql
		
		//从数据库中查询这个客户该年的所有的项目信息
		List itemListFromDb = fsCommonAgent.getFncStatItemIdList(cusId, statYear, tableName,statStyle);
		
		ArrayList arrList = (ArrayList) pfncConfStyles.getItems();
		
		for(int i=0; i<arrList.size(); i++){
			
			//取出item对象
			FncConfDefFormat item = (FncConfDefFormat) arrList.get(i);
			
			String  itemId = item.getItemId();								//itemID
			
			//初始化对象
			RptItemData riData = new RptItemData(cusId, tableName, statPrdStyle, statYear,
					statMonth, itemId,statStyle, fncConfDataColumn,pfncConfStyles.getFncConfTyp());
			/*
			 * 赋值
			 */
			riData.setData1(item.getData1());
			riData.setData2(item.getData2());
			
			riData.setDataA(item.getDataA());
			riData.setDataB(item.getDataB());
			
			if( !("3".equals(item.getFncItemEditTyp())) ){
				String sql = this.getSql(riData, itemListFromDb,pfncConfStyles.getFncConfTyp());
			
				sqlList.add(sql);
			}else{
				
			}
		}
		
		//执行新增操作
		flagInfo = fsCommonAgent.optBatchSql(sqlList);
		
		// 0代表新增加 1 代表暂存 2 代表完成 9 不用的或者扩展位 
		String newstatFlag = this.setStatFlag(tableName, statFlag, '0');
		pfncStatBase.setStateFlg(newstatFlag);
		
		System.out.println("处理"+tableName+"时的statFlag:"+newstatFlag);
		flagInfo = fsCommonAgent.updateFncStatBaseInfo(cusId, statPrdStyle,
					pfncStatBase.getStatPrd(), newstatFlag,statStyle,fncType);
		return flagInfo;
	}
	
	/**
	 * 暂存一条报表记录，根据传入的参数。初始化具体的报表项目信息并从数据库中取出已经存在的改报表的所有项目信息
	 * 与之比对，如果不存在这新增一条项目信息，存在则更新该条项目信息
	 * @param pfncConfStyles	报表样式信息
	 * @param pfncStatBase		公司客户报表基本信息
	 * @return	信息标志(具体参考CMISMessage中定义)
	 * @throws ComponentException
	 */
	public String TempAddFncStat(FncConfStyles pfncConfStyles,FncStatBase pfncStatBase 
			) throws ComponentException,Exception{
		
		String flagInfo = CMISMessage.DEFEAT;	//默认失败
		
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
					getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		/*
		 * 从传入参数中得到需要的数据
		 */
		String cusId 	= pfncStatBase.getCusId();							//客户ID		
		String statYear   = pfncStatBase.getStatPrd().substring(0,4);		//报表年
		String statMonth 	  = pfncStatBase.getStatPrd().substring(4, 6);	//月份
		String statPrdStyle   = pfncStatBase.getStatPrdStyle();				//报表类型
		String tableName 	  = pfncConfStyles.getFncName();				//表名
		int	fncConfDataColumn = pfncConfStyles.getFncConfDataCol();			//期数数				
		String statFlag 	=	pfncStatBase.getStateFlg();					//报表状态
		String statStyle = pfncStatBase.getStatStyle();						//报表口径
		String fncType = pfncStatBase.getFncType();							//报表类型
		ArrayList<String> sqlList = new ArrayList<String>();				//存储生成的多条sql
		
		List itemListFormDb = fsCommonAgent.getFncStatItemIdList(cusId, statYear, tableName,statStyle);
		ArrayList arrList = (ArrayList) pfncConfStyles.getItems();
		
		for(int i=0; i<arrList.size(); i++){
			//从LIST中取出数据项对象
			FncConfDefFormat dfDef = (FncConfDefFormat) arrList.get(i);
			
			String  itemId = dfDef.getItemId();								//项目ID
			
			//初始化对象
			RptItemData riData = new RptItemData(cusId, tableName, statPrdStyle, statYear,
					statMonth, itemId,statStyle, fncConfDataColumn,pfncConfStyles.getFncConfTyp());
			/*
			 * 赋值
			 */
			riData.setData1(dfDef.getData1());
			riData.setData2(dfDef.getData2());
			
			riData.setDataA(dfDef.getDataA());
			riData.setDataB(dfDef.getDataB());
			
			String sql = this.getSql(riData, itemListFormDb,pfncConfStyles.getFncConfTyp());
			sqlList.add(sql);
		}
		
		String newstatFlag = this.setStatFlag(tableName, statFlag, TEMPADD);
		//执行新增操作
		flagInfo = fsCommonAgent.optBatchSql(sqlList);
		flagInfo = fsCommonAgent.updateFncStatBaseInfo(cusId, statPrdStyle, 
					pfncStatBase.getStatPrd(), newstatFlag,statStyle,fncType);
		return flagInfo;
	}
	
	/**
	 * 修改一条报表记录，根据传入的参数。初始化具体的报表项目信息更新具体的报表项目。
	 * @param pfncConfStyles	报表样式信息
	 * @param pfncStatBase		公司客户报表基本信息
	 * @param pfncConfDefFormat	报表配置定义信息
	 * @return	信息标志(具体参考CMISMessage中定义)
	 * @throws ComponentException
	 */
	public String updateFncStat(FncConfStyles pfncConfStyles,FncStatBase pfncStatBase,boolean errFlag
			) throws ComponentException{
		
		String flagInfo = CMISMessage.DEFEAT;	//默认失败
		String newstatFlag = "";
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		/*
		 * 从传入参数中得到需要的数据
		 */
		String cusId 	= pfncStatBase.getCusId();							//客户ID		
		String statYear   = pfncStatBase.getStatPrd().substring(0,4);		//报表年月
		String statMonth 	  = pfncStatBase.getStatPrd().substring(4, 6);	//月份
		String statPrdStyle   = pfncStatBase.getStatPrdStyle();				//报表类型
		String tableName 	  = pfncConfStyles.getFncName();				//表名
		int	fncConfDataColumn = pfncConfStyles.getFncConfDataCol();			//期数数		
		String statFlag 	=	pfncStatBase.getStateFlg();					//报表状态
		String statStyle = pfncStatBase.getStatStyle();						//报表口径
		String fncType = pfncStatBase.getFncType();							//报表类型
		ArrayList arrList = (ArrayList) pfncConfStyles.getItems();
		
		for(int i=0; i<arrList.size(); i++){
			//从LIST中取出数据项对象
			FncConfDefFormat dfDef = (FncConfDefFormat) arrList.get(i);
			String  itemId = dfDef.getItemId();								//项目ID
			//初始化对象
			RptItemData riData = new RptItemData(cusId, tableName, statPrdStyle, statYear,
					statMonth, itemId,statStyle, fncConfDataColumn,pfncConfStyles.getFncConfTyp());
			riData.setData1(dfDef.getData1());
			riData.setData2(dfDef.getData2());
			
			riData.setDataA(dfDef.getDataA());
			riData.setDataB(dfDef.getDataB());
			
			newstatFlag = statFlag;
			if(errFlag){
				newstatFlag = this.setStatFlag(tableName, statFlag, ADD);
			}else{
				newstatFlag = this.setStatFlag(tableName, statFlag, TEMPADD);
			}
			//执行新增操作
			fsCommonAgent.updateFncItemRecord(riData);
			flagInfo = fsCommonAgent.updateFncStatBaseInfo(cusId, statPrdStyle,
					pfncStatBase.getStatPrd(), newstatFlag,statStyle,fncType);
		}
			
		return flagInfo+"|"+newstatFlag;
	}
	
	/**
	 * 删除一条报表记录，根据传入的参数,初始化具体的报表项目信息更新具体的报表项目的值为0.0。
	 * 传入对象需要初始化其cusId(客户ID)、statPrdStyle(报表类型)、statYear(报表年份)、
	 * statMonth(报表月份)、fncConfDataColumn(期数数)
	 * @riData	报表项目信息对象
	 * @return	信息标志(具体参考CMISMessage中定义)
	 * @throws ComponentException
	 */
	public String removeFncStat(RptItemData riData) throws ComponentException{
		
		String flagInfo = CMISMessage.DEFEAT;	//默认失败
		
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
					getAgentInstance(PUBConstant.FNCSTATCOMMON);
			
		//执行删除操作
		flagInfo = fsCommonAgent.removeFncStatRecord(riData);
			
		return flagInfo;
	}
	
	/**
	 * 从项目信息对象中得到项目ID，并与数据库中该报表的项目ID比较如果存在
	 * 则生成更新sql，不存在则生成新增sql 
	 * @param riData	报表项目信息 需要初始化的成员变量为cusId(客户ID)、statPrdStyle(报表类型)、statYear(报表年份)、
	 * 						statMonth(报表月份)、fncConfDataColumn(期数数)、itemId(项目ID)、data1
	 * 					(有data2的初始化data2)
	 * @param itemlist_alreadyExitsInDatabase	数据库里面已经存在的该客户的该年度的itemID列表
	 * @return
	 * @throws ComponentException
	 */
	public String getSql(RptItemData riData, List itemlist_alreadyExitsInDatabase,String fncConfTyp)throws ComponentException{
		String sql = null;
		
		try {
			if(itemlist_alreadyExitsInDatabase == null){
				return riData.AssembleInsertSql();
			}
			String itemId = riData.getItemId().trim();
			
			
			
			/**
			 * 1--该item在数据库中未存在,组装"新增语句" 
			 * 2--该item在数据库中存在,组装"更新语句"
			 */
			int  mode = 1;	
			for(int i=0; i<itemlist_alreadyExitsInDatabase.size(); i++){
				if(itemId.equals(itemlist_alreadyExitsInDatabase.get(i))){
					mode = 2;
				}
			}
			if("05".equals(fncConfTyp)){
				if(mode == 1){
					sql = riData.AssembleInsertSql8();
				}else{
					sql = riData.AssembleUpdateSql8();
				}
			}else{
				if(mode == 1){
					sql = riData.AssembleInsertSql();
				}else{
					sql = riData.AssembleUpdateSql();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"生成sql语句是发生错误");
		}
		
		return sql;
	}
	
	
	/**
	 * 根据传入的参数条件取得一条报表基本信息
	 * @param pfncStatBase 报表基本信息对象 需要初始化成员变量 cusId(客户ID)、
	 * 			statPrdStyle(报表类型)、statPrd(报表期间)
	 * @return	信息标志(具体参考CMISMessage中定义)
	 * @throws EMPException 
	 */
	public FncStatBase findOneFncStatBase(FncStatBase pfncStatBase)throws EMPException{
		
		//创建业务代理类
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent) this
				.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		
		FncStatBase fsBase = fsCommonAgent.queryDetailFncStatBase(pfncStatBase);
		
		return fsBase;
	}
	
	/**
	 * (此处参数写死，需要该表名的时候换需要从用代码的修改此处)
	 * 此函数的功能是：根据表名确定其在statFlag中的位置,根据state更新在statFlag中的状态
	 * @param tableName	表名
	 * @param statFlag 报表状态信息(具体含义参考数据库中定义)
	 * @param state 0 代表新增加 1 代表暂存 2 代表完成 9 不用的或者扩展位 
	 * @return
	 */
	public String setStatFlag(String tableName, String statFlag, char state)
						throws ComponentException{
		int count = -1;
		
		String table = tableName.toLowerCase().trim();
		/*
		 * 根据表名判断其在statFlag中的位置
		 */
		if("fnc_stat_bs".equals(table)){  // 资产样式编号
			count = 0 ;
		}else if("fnc_stat_is".equals(table)){ // 损益表编号
			count = 1; 
		}else if("fnc_stat_cfs".equals(table)){// 现金流量表编号
			count = 2; 
		}else if("fnc_stat_soe".equals(table)){// 所有者权益变动表编号
			count = 4; 
		}else if("fnc_stat_sl".equals(table)){ // 财务简表编号
			count = 5; 
		}else if("fnc_index_rpt".equals(table)){// 财务指标表编号
			count = 3; 
		}else{
			throw new IllegalArgumentException("不支持的财务报表类型");
		}
		
		if(count == -1){
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"无法识别的报表名称");
		}else if(statFlag.length() != FLAGLENGTH){
			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"该期报表中的状态位长度应为9个字符");
		}
		
		StringUtil sUtil = StringUtil.getInstance();
		String tempState = null;
		try {
			tempState = StringUtil.changeCharInString(statFlag, count, state);
			tempState = this.checkAllState(tempState);
		} catch (Exception e) {

			throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"参数错误");
		}
		return tempState;
		
	}
	
	/**
	 * (此处参数写死，需要该表名的时候换需要从用代码的修改此处)(这里的参数标识由上面得到)
	 * 检查财务基表中的状态字段，如果需要的报表都已经保存
	 * 则把状态字段中的最后一位也置成'2'
	 * @param source	输入的状态字符串
	 * @return 输入'222220000' ->'222220002'
	 */
	public String checkAllState(String source){
		char count = ADD ;					//标识器
		char [] sourceList = source.toCharArray();
		for(int i=0; i<source.length(); i++){
			if(i==0 || i==1 || i==2 || i==3 || i==4){// 0资产负债 1损益 3财务指标表 4所有者权益表
				if(ADD != sourceList[i]){
					count = NOADD;
				}
			}
		}

		String des = source.substring(0, 8) + count;

		return des;
	}

	/**
	 * 组装标签样式对象(带数据的)
	 * @param 客户编号 cusId
	 * @param 报表周期类型 statPrdStyle (1:月报 2:季报 3:半年报 4:年报)  
	 * @param 报表期间 statPrd 格式：YYYYMM
	 * @param 表名 tableName 
	 * @param 报表种类 fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
	 * @return FncConfStyles
	 * @throws ComponentException
	 */
	public FncConfStyles findOneFncConfStyles(String cusId,String statPrdStyle,String statPrd,
						String styleId,String tableName,String fncConfTyp,String statStyle)throws ComponentException{
		FncConfStyles pfcConfStyles = null;
		//创建业务代理类
		FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this
				.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		pfcConfStyles = fStatCommonAgent.queryDetailFncConfStyles(cusId, statPrdStyle,
													statPrd,styleId,tableName,fncConfTyp,statStyle);
		return pfcConfStyles;
	}
	
	public List QueryItemsList(String cusId,String statPrdStyle,String statPrd,
			String styleId,String tableName,String fncConfTyp,String stat_style)throws ComponentException{
		//创建业务代理类
		FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this
				.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		List itemList = fStatCommonAgent.QueryItemsList(cusId, statPrdStyle, statPrd, styleId,tableName,fncConfTyp,stat_style);
		
		return itemList;
		
	}
	
	/**
	 * 根据返回的值,来判断是新增数据还是修改数据
	 * @param pfncConfStyles
	 * @param pfncStatBase
	 * @return int 有返回>0,没有为0
	 * @throws ComponentException
	 */
	public int getFncConfStyles(FncConfStyles pfncConfStyles,FncStatBase pfncStatBase)throws ComponentException{
		
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
					getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		/*
		 * 从传入参数中得到需要的数据
		 */
		String cusId 	= pfncStatBase.getCusId();							//客户ID		
		String statYear   = pfncStatBase.getStatPrd().substring(0,4);		//报表年
		String tableName 	  = pfncConfStyles.getFncName();				//表名

		ArrayList arrList = (ArrayList) pfncConfStyles.getItems();
		int count = 0;
		int num = 0;
		int tempCount = fsCommonAgent.findOneFncConfStyles(tableName,cusId,statYear,pfncStatBase.getStatStyle());
		if(tempCount==0){
			return tempCount;
		}else{
			for(int i=0; i<arrList.size(); i++){
				//从LIST中取出数据项对象
				FncConfDefFormat dfDef = (FncConfDefFormat) arrList.get(i);
				String  itemId = dfDef.getItemId();								//项目ID
				num = fsCommonAgent.queryOneFncConfStyles(tableName,cusId,statYear,itemId,pfncStatBase.getStatStyle());
				if(num==0){
					String statMonth 	  = pfncStatBase.getStatPrd().substring(4, 6);	//月份
					String statPrdStyle   = pfncStatBase.getStatPrdStyle();				//报表类型
					int	fncConfDataColumn = pfncConfStyles.getFncConfDataCol();		    //表的列数
					//初始化对象
					RptItemData riData = new RptItemData(cusId, tableName, statPrdStyle, statYear,
							statMonth, itemId,pfncStatBase.getStatStyle(),
							fncConfDataColumn,pfncConfStyles.getFncConfTyp());
					riData.setData1(dfDef.getData1());
					riData.setData2(dfDef.getData2());
					String sql = this.getSql(riData, null,pfncConfStyles.getFncConfTyp());
					num = fsCommonAgent.addOneFncConfStyles(sql);
				}
				count = count + num;
			}
		}
			
		return count;

	}
	
	public FncStatBase getOneFncStatBase(FncStatBase fncStatBase,Connection conn)throws ComponentException{
		FncStatBase tempBase = null;
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
					getAgentInstance(PUBConstant.FNCSTATCOMMON);
		
		tempBase = fsCommonAgent.getOneFncStatBase(fncStatBase,conn);
		
		return tempBase;
	}
	
	public FncConfStyles getData(FncStatBase tempBase,Connection conn,String styleType,String tableName)throws ComponentException{
		FncConfStyles temp = null;
		//业务代理类初始化
		FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
					getAgentInstance(PUBConstant.FNCSTATCOMMON);
		temp = fsCommonAgent.getData(tempBase,conn,styleType,tableName);
		return temp;
	}
	
	/**
	 * 校验公式----开始
	 */
	
	
	//校验
	  private Hashtable valiFormalu = new Hashtable();
	  private Hashtable valueMap = new Hashtable();
	  private Hashtable reValueMap = new Hashtable();
	  private Hashtable opt = new Hashtable();
	  
	  private Hashtable valiFormaluTemp = new Hashtable();
	  
	  public List validateRpt(FncConfStyles rptStyle) throws Exception {
	    int col = rptStyle.getFncConfDataCol();
	    List items = rptStyle.getItems() ;
	    valiFormalu.clear() ;
	    valueMap.clear() ;
	    reValueMap.clear() ;
	    opt.clear() ;

	    for(int i=0;i<items.size() ;i++){
	    	FncConfDefFormat rfd = (FncConfDefFormat)items.get(i);
	      if(!rfd.getFncItemEditTyp().equalsIgnoreCase("3")){
	        if("05".equals(rptStyle.getFncConfTyp())){
	        	Object[] obj = new Object[2];
	        	String[] dataA = new String[8];
	        	String[] dataB = new String[8];
	        	double[] data1 = rfd.getDataA();
	        	   for(int m=0;m<data1.length;m++){
	        		   dataA[m] = String.valueOf(data1[m]);
	        	   }
	        	   double[] data2 = rfd.getDataB();
	        	   for(int n=0;n<data2.length;n++){
	        		   dataB[n] = String.valueOf(data2[n]);
	        	   }
	        	obj[0] = dataA;
	        	obj[1] = dataB;
	        	valueMap.put(rfd.getItemId() ,obj);
	        }else{
	        	String[] valueData = new String[2];
		        valueData[0] = String.valueOf(rfd.getData1() );
		        valueData[1] = String.valueOf(rfd.getData2() );
		        valueMap.put(rfd.getItemId() ,valueData);
	        }

	        String cf =rfd.getFncConfChkFrm() ;
	        if(cf != null && !cf.equalsIgnoreCase("") && cf.length() >2){
	          if("05".equals(rptStyle.getFncConfTyp())){
	        	  String[] checkFormula = new String[16];
		          String[] checkFormulaTemp = new String[16];
		          if(!cf.startsWith("@OPER("))
		            throw new Exception("操作符没有配置");
		          cf = StringUtil.replace(cf,"@OPER","");
		          String oper = StringUtil.getParamString(cf,'(',')');
		          cf = StringUtil.replace(cf,"("+oper+")","");

		          checkFormula[0] = cf;
		          checkFormula[1] = cf;
		          checkFormula[2] = cf;
		          checkFormula[3] = cf;
		          checkFormula[4] = cf;
		          checkFormula[5] = cf;
		          checkFormula[6] = cf;
		          checkFormula[7] = cf;
		          checkFormula[8] = cf;
		          checkFormula[9] = cf;
		          checkFormula[10] = cf;
		          checkFormula[11] = cf;
		          checkFormula[12] = cf;
		          checkFormula[13] = cf;
		          checkFormula[14] = cf;
		          checkFormula[15] = cf;
		          
		          
		          checkFormulaTemp[0] = cf;
		          checkFormulaTemp[1] = cf;
		          checkFormulaTemp[2] = cf;
		          checkFormulaTemp[3] = cf;
		          checkFormulaTemp[4] = cf;
		          checkFormulaTemp[5] = cf;
		          checkFormulaTemp[6] = cf;
		          checkFormulaTemp[7] = cf;
		          checkFormulaTemp[8] = cf;
		          checkFormulaTemp[9] = cf;
		          checkFormulaTemp[10] = cf;
		          checkFormulaTemp[11] = cf;
		          checkFormulaTemp[12] = cf;
		          checkFormulaTemp[13] = cf;
		          checkFormulaTemp[14] = cf;
		          checkFormulaTemp[15] = cf;
		          String[] operName = new String[2];
		          operName[0] = oper;
		          operName[1] = rfd.getItemName() ;
		          opt.put(rfd.getItemId(),operName);
		          valiFormalu.put(rfd.getItemId() ,checkFormula);
		          valiFormaluTemp.put(rfd.getItemId() ,checkFormulaTemp);
	          }else{
	        	  String[] checkFormula = new String[2];
		          String[] checkFormulaTemp = new String[2];
		          if(!cf.startsWith("@OPER("))
		            throw new Exception("操作符没有配置");
		          cf = StringUtil.replace(cf,"@OPER","");
		          String oper = StringUtil.getParamString(cf,'(',')');
		          cf = StringUtil.replace(cf,"("+oper+")","");

		          checkFormula[0] = cf;
		          checkFormula[1] = cf;
		          checkFormulaTemp[0] = cf;
		          checkFormulaTemp[1] = cf;
		          String[] operName = new String[2];
		          operName[0] = oper;
		          operName[1] = rfd.getItemName() ;
		          opt.put(rfd.getItemId(),operName);
		          valiFormalu.put(rfd.getItemId() ,checkFormula);
		          valiFormaluTemp.put(rfd.getItemId() ,checkFormulaTemp);
	          }
	        }
	      }
	    }

	    calCheckFormula(rptStyle.getFncConfTyp());
	    return validateRptData(col,rptStyle.getFncConfTyp());
	  }


	  private void calCheckFormula(String fncConfTyp) throws Exception {
	    int cs =0;
	    while (!valiFormalu.isEmpty() ){
	      Enumeration en = valiFormalu.keys() ;
	      while(en.hasMoreElements() ){
	        String itemId = en.nextElement().toString();
	        String[] formula = (String[])valiFormalu.get(itemId);

	        while (!StringUtil.getParamString(formula[0], '{', '}').equals("")){
	            String param = StringUtil.getParamString(formula[0], '{', '}');
	            String[] str = StringUtil.strToArray(param,'.');

	            if (str.length == 1) {
	              String tmpId = StringUtil.getParamString(str[0], '[', ']');
	              if(valueMap.containsKey(tmpId.trim())){
	                if("05".equals(fncConfTyp)){
	            		 Object[] obj = (Object[])valueMap.get(tmpId);
	            		 String[] dataA = (String[])obj[0];
	            		 String[] dataB = (String[])obj[1];
	            		 formula[0] = StringUtil.replace(formula[0], '{' + param + '}', dataA[0]);
	            		 formula[1] = StringUtil.replace(formula[1], '{' + param + '}', dataA[1]);
	            		 formula[2] = StringUtil.replace(formula[2], '{' + param + '}', dataA[2]);
	            		 formula[3] = StringUtil.replace(formula[3], '{' + param + '}', dataA[3]);
	            		 formula[4] = StringUtil.replace(formula[4], '{' + param + '}', dataA[4]);
	            		 formula[5] = StringUtil.replace(formula[5], '{' + param + '}', dataA[5]);
	            		 formula[6] = StringUtil.replace(formula[6], '{' + param + '}', dataA[6]); 
	            		 formula[7] = StringUtil.replace(formula[7], '{' + param + '}', dataA[7]);
	            		 
	            		 formula[8] = StringUtil.replace(formula[8], '{' + param + '}', dataB[0]);
	            		 formula[9] = StringUtil.replace(formula[9], '{' + param + '}', dataB[1]);
	            		 formula[10] = StringUtil.replace(formula[10], '{' + param + '}', dataB[2]);
	            		 formula[11] = StringUtil.replace(formula[11], '{' + param + '}', dataB[3]);
	            		 formula[12] = StringUtil.replace(formula[12], '{' + param + '}', dataB[4]);
	            		 formula[13] = StringUtil.replace(formula[13], '{' + param + '}', dataB[5]);
	            		 formula[14] = StringUtil.replace(formula[14], '{' + param + '}', dataB[6]);
	            		 formula[15] = StringUtil.replace(formula[15], '{' + param + '}', dataB[7]);
	            		 
	            	 }else{
	            		 String[] valueArray = (String[])valueMap.get(tmpId.trim());
	 	                formula[0] = StringUtil.replace(formula[0], '{' + param + '}', valueArray[0]);
	 	                formula[1] = StringUtil.replace(formula[1], '{' + param + '}', valueArray[1]);
	            	 }
	                
	                
	              }else{
	                throw new Exception("本报表中没有此项目：" + param);
	              }
	            }else{
	              throw new Exception("错误的校验公式配置：" + param);
	            }

	        }
	        
	        if("05".equals(fncConfTyp)){
	        	 Object[] obj = new Object[2];
	        	 String[] dataA = new String[8];
	        	 String[] dataB = new String[8];
	        	 dataA[0] = StringUtil.getFormulaValue4Fnc(formula[0]);
	        	 dataA[1] = StringUtil.getFormulaValue4Fnc(formula[1]);
	        	 dataA[2] = StringUtil.getFormulaValue4Fnc(formula[2]);
	        	 dataA[3] = StringUtil.getFormulaValue4Fnc(formula[3]);
	        	 dataA[4] = StringUtil.getFormulaValue4Fnc(formula[4]);
	        	 dataA[5] = StringUtil.getFormulaValue4Fnc(formula[5]);
	        	 dataA[6] = StringUtil.getFormulaValue4Fnc(formula[6]);
	        	 dataA[7] = StringUtil.getFormulaValue4Fnc(formula[7]);
	        	 
	        	 dataB[0] = StringUtil.getFormulaValue4Fnc(formula[8]);
	        	 dataB[1] = StringUtil.getFormulaValue4Fnc(formula[9]);
	        	 dataB[2] = StringUtil.getFormulaValue4Fnc(formula[10]);
	        	 dataB[3] = StringUtil.getFormulaValue4Fnc(formula[11]);
	        	 dataB[4] = StringUtil.getFormulaValue4Fnc(formula[12]);
	        	 dataB[5] = StringUtil.getFormulaValue4Fnc(formula[13]);
	        	 dataB[6] = StringUtil.getFormulaValue4Fnc(formula[14]);
	        	 dataB[7] = StringUtil.getFormulaValue4Fnc(formula[15]);
	        	 
	        	 obj[0] = dataA;
	        	 obj[1] = dataB;
	        	 valiFormalu.remove(itemId);
	        	 reValueMap.put(itemId,obj);
	        	 valueMap.put(itemId,obj);
	        	 
	         }else{
	        	String[] reValue = new String[2];
	 	        reValue[0] = StringUtil.getFormulaValue4Fnc(formula[0]);
	 	        reValue[1] = StringUtil.getFormulaValue4Fnc(formula[1]);
	 	        valiFormalu.remove(itemId);
	 	        reValueMap.put(itemId,reValue);
	         }
	        
	      }

	      if(cs > 5){
	        throw new Exception("校验公式嵌套层次错误");
	      }
	    }
	  }

	  private List validateRptData(int cols,String fncConfTyp) {
	    ArrayList errAl =  new ArrayList();
	    Interpreter interpreter=new Interpreter();
	    if (!opt.isEmpty() ){
	      Enumeration en = opt.keys();
	      while (en.hasMoreElements()) {
	        String itemId = en.nextElement().toString();
	        String[] operName = (String[])opt.get(itemId) ;

	        Object[] valueArray = (Object[])valueMap.get(itemId);
	        Object[] calValue = (Object[])reValueMap.get(itemId);
	        String[] checkFormulaTemp = (String[])valiFormaluTemp.get(itemId);
	        while (!StringUtil.getParamString(checkFormulaTemp[0], '{', '}').equals("")){
	            String param = StringUtil.getParamString(checkFormulaTemp[0], '{', '}');
	            String[] str = StringUtil.strToArray(param,'.');
	            if (str.length == 1) {
	            	 int post = param.indexOf("]");
		              String tmpName = param.substring(post+1);
		              if("05".equals(fncConfTyp)){
		            	  checkFormulaTemp[0] = StringUtil.replace(checkFormulaTemp[0], '{' + param + '}', tmpName);
			              checkFormulaTemp[1] = StringUtil.replace(checkFormulaTemp[1], '{' + param + '}', tmpName);
			              checkFormulaTemp[2] = StringUtil.replace(checkFormulaTemp[2], '{' + param + '}', tmpName);
			              checkFormulaTemp[3] = StringUtil.replace(checkFormulaTemp[3], '{' + param + '}', tmpName);
			              checkFormulaTemp[4] = StringUtil.replace(checkFormulaTemp[4], '{' + param + '}', tmpName);
			              checkFormulaTemp[5] = StringUtil.replace(checkFormulaTemp[5], '{' + param + '}', tmpName);
			              checkFormulaTemp[6] = StringUtil.replace(checkFormulaTemp[6], '{' + param + '}', tmpName);
			              checkFormulaTemp[7] = StringUtil.replace(checkFormulaTemp[7], '{' + param + '}', tmpName);
			              checkFormulaTemp[8] = StringUtil.replace(checkFormulaTemp[8], '{' + param + '}', tmpName);
			              checkFormulaTemp[9] = StringUtil.replace(checkFormulaTemp[9], '{' + param + '}', tmpName);
			              checkFormulaTemp[10] = StringUtil.replace(checkFormulaTemp[10], '{' + param + '}', tmpName);
			              checkFormulaTemp[11] = StringUtil.replace(checkFormulaTemp[11], '{' + param + '}', tmpName);
			              checkFormulaTemp[12] = StringUtil.replace(checkFormulaTemp[12], '{' + param + '}', tmpName);
			              checkFormulaTemp[13] = StringUtil.replace(checkFormulaTemp[13], '{' + param + '}', tmpName);
			              checkFormulaTemp[14] = StringUtil.replace(checkFormulaTemp[14], '{' + param + '}', tmpName);
			              checkFormulaTemp[15] = StringUtil.replace(checkFormulaTemp[15], '{' + param + '}', tmpName);
		              }else{
		            	  checkFormulaTemp[0] = StringUtil.replace(checkFormulaTemp[0], '{' + param + '}', tmpName);
			              checkFormulaTemp[1] = StringUtil.replace(checkFormulaTemp[1], '{' + param + '}', tmpName);
		              }
		            }
	        }
	        
	        boolean tt = false; 
	        switch (cols){
	          case 1:
	            try {
	              interpreter.eval("try { bl=(" + valueArray[0] + operName[0] + calValue[0] +
	                               ");}catch(ArithmeticException e){bl=false;}");
	              boolean b = ((Boolean)interpreter.get("bl")).booleanValue() ;
	              if(!b){
	            	errAl.clear();
	            	String oper = operName[0];
	            	String gsyz = "";
	            	if(oper.equals("==")){
	            		gsyz = "=" ;	            		
	            	}
	            	else if (oper.equals(">=")){
	            		gsyz = ">=" ;	            		
	            	}
	            	else if (oper.equals("<=")){
	            		gsyz = "<=" ;	            		
	            	}
	            	else if (oper.equals(">")){
	            		gsyz = ">" ;	            		
	            	}
	            	else if (oper.equals("<")){
	            		gsyz = "<" ;
	            	}
	                errAl.add(operName[1]+"验证错误！\n即"+operName[1]+gsyz+checkFormulaTemp[0]);
	                return errAl;
	              }else{
	            	 //errAl.add("报表已打平，保存成功！"); 
	            	  tt = true;
	              }
	            }
	            catch (EvalError ex) {
	              errAl.add(operName[1]+"验证错误:"+ex.getMessage() );
	            }
	            break;
	          case 2:
	            try {
	              interpreter.eval("try { bl=(" + valueArray[0] + operName[0] + calValue[0] +
	                               ");}catch(ArithmeticException e){bl=false;}");
	              boolean b = ((Boolean)interpreter.get("bl")).booleanValue() ;
	              if(!b){
	            	errAl.clear();
	            	String oper = operName[0];
	            	String gsyz = "";
	            	if(oper.equals("==")){
	            		gsyz = "=" ;	            		
	            	}
	            	else if (oper.equals(">=")){
	            		gsyz = ">=" ;	            		
	            	}
	            	else if (oper.equals("<=")){
	            		gsyz = "<=" ;	            		
	            	}
	            	else if (oper.equals(">")){
	            		gsyz = ">" ;	            		
	            	}
	            	else if (oper.equals("<")){
	            		gsyz = "<" ;
	            	}
	                errAl.add(operName[1]+"的第1列验证错误！\n\n即:"+operName[1]+gsyz+checkFormulaTemp[0]+"\n\n");
	                return errAl;
	              }

	              interpreter.eval("try { bl=(" + valueArray[1] + operName[0] + calValue[1] +
	                               ");}catch(ArithmeticException e){bl=false;}");
	              boolean bb = ((Boolean)interpreter.get("bl")).booleanValue() ;
	              if(b && !bb){
	            	  errAl.clear();
	            	  String oper = operName[0];
		            	String gsyz = "";
		            	if(oper.equals("==")){
		            		gsyz = "=" ;	            		
		            	}
		            	else if (oper.equals(">=")){
		            		gsyz = ">=" ;	            		
		            	}
		            	else if (oper.equals("<=")){
		            		gsyz = "<=" ;	            		
		            	}
		            	else if (oper.equals(">")){
		            		gsyz = ">" ;	            		
		            	}
		            	else if (oper.equals("<")){
		            		gsyz = "<" ;
		            	}
	                errAl.add(operName[1]+"的第2列验证错误！\n\n即:"+operName[1]+gsyz+checkFormulaTemp[1]);
	                return errAl;
	              }
	              if(!b && !bb){
	            	  errAl.clear();
	            	  String oper = operName[0];
		            	String gsyz = "";
		            	if(oper.equals("==")){
		            		gsyz = "=" ;	            		
		            	}
		            	else if (oper.equals(">=")){
		            		gsyz = ">=" ;	            		
		            	}
		            	else if (oper.equals("<=")){
		            		gsyz = "<=" ;	            		
		            	}
		            	else if (oper.equals(">")){
		            		gsyz = ">" ;	            		
		            	}
		            	else if (oper.equals("<")){
		            		gsyz = "<" ;
		            	}
		              errAl.add(operName[1]+"验证错误！\n\n即:"+operName[1]+gsyz+checkFormulaTemp[0]+"\n\n");
		              return errAl;
	              }
	              
	              if(b && bb){
	            	  //errAl.add("报表已打平，保存成功！"); 
	            	  tt = true;
	              }

	            }
	            catch (EvalError ex) {
	              errAl.add(operName[1]+"验证错误:"+ex.getMessage() );
	            }
	            break;
	            
	          case 8:
	        	  String[] dataA = (String[])calValue[0];
	        	  String[] dataB = (String[])calValue[1];
	        	  
	        	  String[] dataA1 = (String[])valueArray[0];
	        	  String[] dataB1 = (String[])valueArray[1];
	        	  
	        	  
	        	  try {
	        		  boolean b = true;
	        		  boolean bb = true;
	        		  for(int i=0;i<dataA.length;i++){
	        			  interpreter.eval("try { bl=(" + dataA1[i] + operName[0] + dataA[i] +
                          ");}catch(ArithmeticException e){bl=false;}");
				         b = ((Boolean)interpreter.get("bl")).booleanValue() ;
				         if(!b){
					           errAl.clear();
					           errAl.add(operName[1]+"的第"+i+"列验证错误！\n\n即:"+operName[1]+">="
					        		   											+checkFormulaTemp[0]+"\n\n");
					           return errAl;
				         }
	        		  }
	        		  for(int i=0;i<dataA.length;i++){
	        			  interpreter.eval("try { bl=(" + dataB1[i] + operName[1] + dataB[i] +
                          ");}catch(ArithmeticException e){bl=false;}");
				         bb = ((Boolean)interpreter.get("bl")).booleanValue() ;
				         if(!bb){
					           errAl.clear();
					           int p = i+9;
					           errAl.add(operName[1]+"的第"+p+"列验证错误！\n\n即:"+operName[1]+">="
					        		   											+checkFormulaTemp[0]+"\n\n");
					           return errAl;
				         }
	        		  }
	        		 
		              if(!b && !bb){
		            	  errAl.clear();
			              errAl.add(operName[1]+"验证错误！\n\n即:"+operName[1]+">="+checkFormulaTemp[0]+"\n\n");
			              return errAl;
		              }
		              
		              if(b && bb){
		            	  //errAl.add("报表已打平，保存成功！"); 
		            	  tt = true;
		              }

		            }
		            catch (EvalError ex) {
		              errAl.add(operName[1]+"验证错误:"+ex.getMessage() );
		            }
	        	break;
	            
	          default:
	        }
	        if(tt){
	        	errAl.clear();
	        	errAl.add("报表已打平，保存成功！"); 
	        }
	      }
	    }
	    return errAl;

	  }
	  
	  /**
	   * 校验公式----结束
	   */

	  
	  
	  /**
	   * 计算公式----开始
	   */
	  private TreeMap reItemData ;
	  private HashMap bsMap;             //资产负债表数据
	  private HashMap plMap;             //损益表数据
	  private HashMap cfMap;             //现金流量表数据
	  private HashMap jbMap;             //财务简表数据
	  private HashMap fiMap;             //财务指标表数据

		//自动计算资产负债表和损益表
	    public List calculateBbRptData(FncConfStyles pfncConfStyles) throws Exception {
	    	List items = null;
		   if(pfncConfStyles != null){
		     items = pfncConfStyles.getItems() ;
		     valiFormalu.clear() ;//校验公式
		     valueMap.clear() ;//数据库对应项目值
		     reValueMap.clear() ;//值

		     for(int i=0;i<items.size() ;i++){//取样式中的格式化对象
		    	FncConfDefFormat fncConfDefFormat = (FncConfDefFormat)items.get(i); 
		       if(!fncConfDefFormat.getFncItemEditTyp().equalsIgnoreCase("3")){//非标题项
		         if(fncConfDefFormat.getFncItemEditTyp().equalsIgnoreCase("2")){//自动计算
		           String cf =fncConfDefFormat.getFncConfCalFrm() ;//获得计算公式
		           if(cf != null && !cf.equalsIgnoreCase("")){
		             if("05".equals(pfncConfStyles.getFncConfTyp())){
		            	 String[] checkFormula = new String[16];
			             checkFormula[0] = cf;
			             checkFormula[1] = cf;
			             checkFormula[2] = cf;
			             checkFormula[3] = cf;
			             checkFormula[4] = cf;
			             checkFormula[5] = cf;
			             checkFormula[6] = cf;
			             checkFormula[7] = cf;
			             
			             checkFormula[8] = cf;
			             checkFormula[9] = cf;
			             checkFormula[10] = cf;
			             checkFormula[11] = cf;
			             checkFormula[12] = cf;
			             checkFormula[13] = cf;
			             checkFormula[14] = cf;
			             checkFormula[15] = cf;
			             valiFormalu.put(fncConfDefFormat.getItemId() ,checkFormula);//存入hashtable中
		             }else{
		            	 String[] checkFormula = new String[2];

			             checkFormula[0] = cf;
			             checkFormula[1] = cf;
			             valiFormalu.put(fncConfDefFormat.getItemId() ,checkFormula);//存入hashtable中
		             }
		           }
		         }else{//标题项
		           if("05".equals(pfncConfStyles.getFncConfTyp())){
		        	   Object[] valueData8 = new Object[2];
		        	   String[] valueDataA = new String[8];
		        	   String[] valueDataB = new String[8];
		        	   double[] data1 = fncConfDefFormat.getDataA();
		        	   double tempDataA = 0.0;
		        	   double tempDataB = 0.0;
		        	   for(int m=0;m<data1.length;m++){
		        		   valueDataA[m] = String.valueOf(data1[m]);
		        	   }
		        	   //计算最后一列的值
		        	   tempDataA = data1[0] + data1[1] - data1[2] + data1[3]
		        	                         + data1[4] + data1[5] + data1[6];
		        	   valueDataA[7] = String.valueOf(tempDataA);
		        	   
		        	   double[] data2 = fncConfDefFormat.getDataB();
		        	   for(int n=0;n<data2.length;n++){
		        		   valueDataB[n] = String.valueOf(data2[n]);
		        	   }
		        	   //计算最后一列的值
		        	   tempDataB = data2[0] + data2[1] - data2[2] + data2[3]
		        	                         + data2[4] + data2[5] + data2[6];
		        	   valueDataB[7] = String.valueOf(tempDataB);
		        	   
		        	   valueData8[0] = valueDataA;
		        	   valueData8[1] = valueDataB;
		        	   valueMap.put(fncConfDefFormat.getItemId() ,valueData8);
		           }else{
		        	   String[] valueData = new String[2];
			           valueData[0] = String.valueOf(fncConfDefFormat.getData1() );
			           valueData[1] = String.valueOf(fncConfDefFormat.getData2() );
			           valueMap.put(fncConfDefFormat.getItemId() ,valueData);
		           }
		         }

		       }
		     }

		     calBbFormula(pfncConfStyles.getFncConfTyp());//伪码公式转换为带值的四则运算公式

		     for(int i=0;i<items.size() ;i++){
		       FncConfDefFormat rfd = (FncConfDefFormat) items.get(i);
		       if (rfd.getFncItemEditTyp().equalsIgnoreCase("2")) {
		         if(reValueMap.containsKey(rfd.getItemId() )){
		           if("05".equals(pfncConfStyles.getFncConfTyp())){
		        	   Object[] calValue = (Object[])reValueMap.get(rfd.getItemId());
		        	   String[] dataA = (String[])calValue[0];
			           String[] dataB = (String[])calValue[1];
			           double[] dataAvalue = new double[8];
			           double[] dataBvalue = new double[8];
			           double tempDataA = 0.0;
			           double tempDataB = 0.0;
			           for(int k=0;k<dataA.length;k++){
			        	   dataAvalue[k] = Double.parseDouble(dataA[k]);
			           }
			         //计算最后一列的值
		        	   tempDataA = dataAvalue[0] + dataAvalue[1] - dataAvalue[2] + dataAvalue[3]
		        	                         + dataAvalue[4] + dataAvalue[5] + dataAvalue[6];
		        	   dataAvalue[7] = tempDataA;
			           
			           for(int l=0;l<dataB.length;l++){
			        	   dataBvalue[l] = Double.parseDouble(dataB[l]);
			           }
			           
			         //计算最后一列的值
		        	   tempDataB = dataBvalue[0] + dataBvalue[1] - dataBvalue[2] + dataBvalue[3]
		        	                         + dataBvalue[4] + dataBvalue[5] + dataBvalue[6];
		        	   dataBvalue[7] = tempDataB;
			           
			           rfd.setDataA(dataAvalue);
			           rfd.setDataB(dataBvalue);
		           }else{
		        	   String[] calValue = (String[])reValueMap.get(rfd.getItemId());
			           rfd.setData1(Double.parseDouble(calValue[0]));//计算
			           rfd.setData2(Double.parseDouble(calValue[1]));
		           }
		           items.remove(i);
		           items.add(i,rfd);
		         }
		       }else{
		    	   //计算非计算公式项目????????????????????????????????????????
		    	   if("05".equals(pfncConfStyles.getFncConfTyp())){
		    		   double[] dataAA = rfd.getDataA();
			    	   double[] dataBB = rfd.getDataB();
			    	   dataAA[7] = dataAA[0] + dataAA[1] - dataAA[2] + dataAA[3] 
			    	                                            + dataAA[4] + dataAA[5] + dataAA[6];
			    	   dataBB[7] = dataBB[0] + dataBB[1] - dataBB[2] + dataBB[3] 
			    			    	                                            + dataBB[4] + dataBB[5] + dataBB[6];
		    	   }
		    	   
		       }
		     }

		   }
		   return items;
		 }
	    
	    private void calBbFormula(String fncConfTyp) throws Exception {
	    	   int cs =0;
	    	   while (!valiFormalu.isEmpty() ){//公式非空
	    	     Enumeration en = valiFormalu.keys() ;//枚举存ItemId集合
	    	     while(en.hasMoreElements() ){
	    	       String itemId = en.nextElement().toString();
	    	       String[] formula  = (String[])valiFormalu.get(itemId);//对应ItemId公式存入数组，因为放人的是2个一样的;
	    	       boolean finish = true;
	    	       while (!StringUtil.getParamString(formula[0], '{', '}').equals("")){
	    	           String param = StringUtil.getParamString(formula[0], '{', '}');//取一个中括号间的字符串（表.科目.期初/期末）
	    	           String[] str = StringUtil.strToArray(param,'.');//资产负债表和损益配置不含‘.’

	    	           if (str.length == 1) {
	    	             String tmpId = StringUtil.getParamString(str[0], '[', ']');//取出itemId
	    	             tmpId = tmpId.trim();
	    	             if(valueMap.containsKey(tmpId)){//data里面含有itemId的值 
	    	            	 if("05".equals(fncConfTyp)){
	    	            		 Object[] obj = (Object[])valueMap.get(tmpId);
	    	            		 String[] dataA = (String[])obj[0];
	    	            		 String[] dataB = (String[])obj[1];
	    	            		 formula[0] = StringUtil.replace(formula[0], '{' + param + '}', dataA[0]);
	    	            		 formula[1] = StringUtil.replace(formula[1], '{' + param + '}', dataA[1]);
	    	            		 formula[2] = StringUtil.replace(formula[2], '{' + param + '}', dataA[2]);
	    	            		 formula[3] = StringUtil.replace(formula[3], '{' + param + '}', dataA[3]);
	    	            		 formula[4] = StringUtil.replace(formula[4], '{' + param + '}', dataA[4]);
	    	            		 formula[5] = StringUtil.replace(formula[5], '{' + param + '}', dataA[5]);
	    	            		 formula[6] = StringUtil.replace(formula[6], '{' + param + '}', dataA[6]); 
	    	            		 formula[7] = StringUtil.replace(formula[7], '{' + param + '}', dataA[7]);
	    	            		 
	    	            		 formula[8] = StringUtil.replace(formula[8], '{' + param + '}', dataB[0]);
	    	            		 formula[9] = StringUtil.replace(formula[9], '{' + param + '}', dataB[1]);
	    	            		 formula[10] = StringUtil.replace(formula[10], '{' + param + '}', dataB[2]);
	    	            		 formula[11] = StringUtil.replace(formula[11], '{' + param + '}', dataB[3]);
	    	            		 formula[12] = StringUtil.replace(formula[12], '{' + param + '}', dataB[4]);
	    	            		 formula[13] = StringUtil.replace(formula[13], '{' + param + '}', dataB[5]);
	    	            		 formula[14] = StringUtil.replace(formula[14], '{' + param + '}', dataB[6]);
	    	            		 formula[15] = StringUtil.replace(formula[15], '{' + param + '}', dataB[7]);
	    	            		 
	    	            	 }else{
	    	            		 String[] valueArray = (String[])valueMap.get(tmpId);//取值，期初、期末
	  	    	                 formula[0] = StringUtil.replace(formula[0], '{' + param + '}', valueArray[0]);//替换公式中的伪码，形成真正的公司
	  	    	                 formula[1] = StringUtil.replace(formula[1], '{' + param + '}', valueArray[1]);
	    	            	 }
	    	             }else{
	    	               finish = false;
	    	               //String[] tempValue = new String[2];
	    	               //tempValue[0] = "0.0";
	    	               //tempValue[1] = "0.0";
	    	               //valueMap.put(tmpId,tempValue);
	    	               break;
	    	             }
	    	           }else if (str.length == 3) {
							String tmpId = StringUtil.getParamString(str[1], '[',
									']');// 取出itemId
							tmpId = tmpId.trim();
							if (valueMap.containsKey(tmpId)) {// data里面含有itemId的值
								if("05".equals(fncConfTyp)){
		    	            		 Object[] obj = (Object[])valueMap.get(tmpId);
		    	            		 String[] dataA = (String[])obj[0];
		    	            		 String[] dataB = (String[])obj[1];
		    	            		 formula[0] = StringUtil.replace(formula[0], '{' + param + '}', dataA[0]);
		    	            		 formula[1] = StringUtil.replace(formula[1], '{' + param + '}', dataA[1]);
		    	            		 formula[2] = StringUtil.replace(formula[2], '{' + param + '}', dataA[2]);
		    	            		 formula[3] = StringUtil.replace(formula[3], '{' + param + '}', dataA[3]);
		    	            		 formula[4] = StringUtil.replace(formula[4], '{' + param + '}', dataA[4]);
		    	            		 formula[5] = StringUtil.replace(formula[5], '{' + param + '}', dataA[5]);
		    	            		 formula[6] = StringUtil.replace(formula[6], '{' + param + '}', dataA[6]); 
		    	            		 formula[7] = StringUtil.replace(formula[7], '{' + param + '}', dataA[7]);
		    	            		 
		    	            		 formula[8] = StringUtil.replace(formula[8], '{' + param + '}', dataB[0]);
		    	            		 formula[9] = StringUtil.replace(formula[9], '{' + param + '}', dataB[1]);
		    	            		 formula[10] = StringUtil.replace(formula[10], '{' + param + '}', dataB[2]);
		    	            		 formula[11] = StringUtil.replace(formula[11], '{' + param + '}', dataB[3]);
		    	            		 formula[12] = StringUtil.replace(formula[12], '{' + param + '}', dataB[4]);
		    	            		 formula[13] = StringUtil.replace(formula[13], '{' + param + '}', dataB[5]);
		    	            		 formula[14] = StringUtil.replace(formula[14], '{' + param + '}', dataB[6]);
		    	            		 formula[15] = StringUtil.replace(formula[15], '{' + param + '}', dataB[7]);
		    	            		 
		    	            	 }else{
									String[] valueArray = (String[]) valueMap
									.get(tmpId);// 取值，期初、期末
									formula[0] = StringUtil.replace(formula[0],
											'{' + param + '}', valueArray[0]);// 替换公式中的伪码，形成真正的公式
									formula[1] = StringUtil.replace(formula[1],
											'{' + param + '}', valueArray[1]);
								}
							} else {
		
								finish = false;
								//String[] tempValue = new String[2];
								//tempValue[0] = "0.0";
								//tempValue[1] = "0.0";
								//valueMap.put(tmpId, tempValue);
								break;
							}
				}else{
	    	             throw new Exception("错误的计算公式配置：" + param);
	    	           }
	    	    }
	    	   if(finish){//将伪码公式转换为四则运算公式计算
	    	         if("05".equals(fncConfTyp)){
	    	        	 Object[] obj = new Object[2];
	    	        	 String[] dataA = new String[8];
	    	        	 String[] dataB = new String[8];
	    	        	 dataA[0] = StringUtil.getFormulaValue4Fnc(formula[0]);
	    	        	 dataA[1] = StringUtil.getFormulaValue4Fnc(formula[1]);
	    	        	 dataA[2] = StringUtil.getFormulaValue4Fnc(formula[2]);
	    	        	 dataA[3] = StringUtil.getFormulaValue4Fnc(formula[3]);
	    	        	 dataA[4] = StringUtil.getFormulaValue4Fnc(formula[4]);
	    	        	 dataA[5] = StringUtil.getFormulaValue4Fnc(formula[5]);
	    	        	 dataA[6] = StringUtil.getFormulaValue4Fnc(formula[6]);
	    	        	 dataA[7] = StringUtil.getFormulaValue4Fnc(formula[7]);
	    	        	 
	    	        	 dataB[0] = StringUtil.getFormulaValue4Fnc(formula[8]);
	    	        	 dataB[1] = StringUtil.getFormulaValue4Fnc(formula[9]);
	    	        	 dataB[2] = StringUtil.getFormulaValue4Fnc(formula[10]);
	    	        	 dataB[3] = StringUtil.getFormulaValue4Fnc(formula[11]);
	    	        	 dataB[4] = StringUtil.getFormulaValue4Fnc(formula[12]);
	    	        	 dataB[5] = StringUtil.getFormulaValue4Fnc(formula[13]);
	    	        	 dataB[6] = StringUtil.getFormulaValue4Fnc(formula[14]);
	    	        	 dataB[7] = StringUtil.getFormulaValue4Fnc(formula[15]);

	    	        	//计算最后一列的值
	    	        	 dataA[7] = Double.parseDouble(dataA[0]) + Double.parseDouble(dataA[1]) - Double.parseDouble(dataA[2]) 
	    	        	 + Double.parseDouble(dataA[3])+ Double.parseDouble(dataA[4]) + Double.parseDouble(dataA[5])
	    	        	 + Double.parseDouble(dataA[6])+"";
	    	        	 
	    	        	 dataB[7] = Double.parseDouble(dataB[0]) + Double.parseDouble(dataB[1]) - Double.parseDouble(dataB[2]) 
	    	        	 + Double.parseDouble(dataB[3])+ Double.parseDouble(dataB[4]) + Double.parseDouble(dataB[5])
	    	        	 + Double.parseDouble(dataB[6])+"";
			        	   
	    	        	 
	    	        	 obj[0] = dataA;
	    	        	 obj[1] = dataB;
	    	        	 valiFormalu.remove(itemId);
		    	         reValueMap.put(itemId,obj);
		    	         valueMap.put(itemId,obj);
	    	        	 
	    	         }else{
	    	        	 String[] reValue = new String[2];
		    	         reValue[0] = StringUtil.getFormulaValue4Fnc(formula[0]);
		    	         reValue[1] = StringUtil.getFormulaValue4Fnc(formula[1]);
		    	         valiFormalu.remove(itemId);
		    	         reValueMap.put(itemId,reValue);
		    	         valueMap.put(itemId,reValue);
	    	         }
	    	   }else{
	    	         valiFormalu.put(itemId,formula);
	    	   }
	    	}

	    	cs++;
	    	if(cs > 50){
	    	       throw new Exception("计算公式嵌套层次错误"+valiFormalu);
	    	}
	      }
	  }
	    /**
	     * 用来处理现金流量表或财务指标表
	     * @param pfncConfStyles
	     * @param pfncStatBase
	     * @param rptType
	     * @return
	     * @throws Exception
	     */
	    public List calculateRptData(FncConfStyles pfncConfStyles,FncStatBase pfncStatBase,String rptType) throws Exception {
	        List items = null;
	        int dataCol = pfncConfStyles.getFncConfDataCol();
	        if(pfncConfStyles != null && pfncConfStyles.getItems()!=null){
	          items = pfncConfStyles.getItems() ;
	          Hashtable formulaMap = new Hashtable();

	          for(int i=0;i<items.size() ;i++){
	        	  String[] fncConfCalFrm = new String[dataCol];
	        	  String[] dataValue = new String[dataCol];
	        	  FncConfDefFormat rfd = (FncConfDefFormat)items.get(i);
	            if(rfd.getFncItemEditTyp().equalsIgnoreCase("2")){//计算标志
	              try{
	            	for(int m=0;m<dataCol;m++){
	            		fncConfCalFrm[m] = rfd.getFncConfCalFrm();
	            	}
	            	
	            	if(dataCol==2){
	            		fncConfCalFrm[0] = String.valueOf(rfd.getData1());
	            	}
	            	
	                formulaMap.put(rfd.getItemId(),fncConfCalFrm);//将计算公式放入Map中
	                //formulaMap.put(rfd.getItemId(),rfd.getFncConfCalFrm();//将计算公式放入Map中
	              }catch(Exception ex){
	                throw new Exception(rfd.getItemName() +" 类型错误("+ex.getMessage() +")；");
	              }
	            }else{//非计算标志，非标题项
	              if(rfd.getFncItemEditTyp().equalsIgnoreCase("1")){
	            	  dataValue[0] = String.valueOf(rfd.getData1());
	            	  if(dataCol==2){
	            		  dataValue[1] = String.valueOf(rfd.getData2());
	            	  }
	            	  formulaMap.put(rfd.getItemId(),dataValue );//讲数据放入Map中
	                //formulaMap.put(rfd.getItemId(),String.valueOf(rfd.getData1()) );//讲数据放入Map中
	              }
	            }
	          }

	          /**
	           * 开始准备资产负债表，损益表，现金流量表，财务指标表的数据，然后放到对应的Map中，供下面使用
	           */
	          this.initRptMap(pfncStatBase);
	          /**
	           * 处理伪码公式，转变成正常的四则运算，并将计算结果返回
	           */
	          
	          //测试使用
	          /*Iterator iter=formulaMap.keySet().iterator();
	          while(iter.hasNext()){
	        	  String pk = (String)iter.next();
	        	  String[] str = (String[])formulaMap.get(pk);
	        	  for(int k=0;k<str.length;k++){
	        		  System.out.println(pk+"----------date["+k+"]或者formula["+k+"]:"+str[k]);
	        	  }
	          }*/
	          ////////////////////
	          this.formulaExe(formulaMap,rptType);
	          
	          for(int i=0;i<items.size() ;i++){
	        	  FncConfDefFormat rfd = (FncConfDefFormat) items.get(i);
	            if (rfd.getFncItemEditTyp().equalsIgnoreCase("2") || rfd.getFncItemEditTyp().equalsIgnoreCase("1")) {
	              //double value = this.getItemValue(rfd.getItemId(),dataCol ) ;
	              double[] value = this.getItemValue(rfd.getItemId(),dataCol ) ;
	              //rfd.setData1(value);
	              rfd.setData1(value[0]);
	              if(dataCol==2){
	            	  rfd.setData2(value[1]);
	              }else{
	            	  rfd.setData2(value[0]);
	              }
	              items.remove(i);
	              items.add(i,rfd);
	            }
	          }

	        }
	        return items;
	      }
	    
	    public void formulaExe(Hashtable noExEMap,String currRptType) throws Exception {
	        int cs = 0;
	        while(!noExEMap.isEmpty() ){
	          Enumeration en = noExEMap.keys() ;
	          while(en.hasMoreElements() ){
	            String itemId = en.nextElement().toString()  ;
	            //String formula = noExEMap.get(itemId).toString() ;
	            String[] formula = (String[])noExEMap.get(itemId);
	            boolean finish = true;
	            //String[] formulaTemp = (String[])noExEMap.get(itemId);
	            String tempF = "";
	            if(formula.length==1){
	            	tempF = formula[0];
	            }else{
	            	tempF = formula[1];
	            }
	            
	            while (!StringUtil.getParamString(tempF, '{', '}').equals("")){
		                String param = StringUtil.getParamString(tempF, '{', '}');
		                String[] str = StringUtil.strToArray(param,'.');
		                //String value="";
		                String[] value=new String[formula.length];
		                if (str.length == 3) {
		                  String tmpId = StringUtil.getParamString(str[1], '[', ']');

		                  //测试使用
		                  /*if("X01100000".equals(tmpId)){
		                	  System.out.println(tmpId);
		                  }*/
		                  
		                  if(reItemData.containsKey(tmpId)){
		                    //value = reItemData.get(tmpId).toString() ;
		                	  value = (String[])reItemData.get(tmpId);
		                  }else{
		                    if(!StringUtil.getParamString(str[0], '[', ']').equals(currRptType)){
		                      try{
		                        /*value = this.getRptItemValue(StringUtil.getParamString(str[0], '[', ']'),
		                        		StringUtil.getParamString(str[1], '[', ']'),
		                        		StringUtil.getParamString(str[2], '[', ']'));*/
		                        
		                        for(int h=0;h<value.length;h++){
		                        	value[h] = this.getRptItemValue(StringUtil.getParamString(str[0], '[', ']'),
			                        		StringUtil.getParamString(str[1], '[', ']'),
			                        		StringUtil.getParamString(str[2], '[', ']'));
		                        }
		                      }catch(Exception ex){
		                        throw new Exception("错误的公式配置：" + itemId+":"+formula+":"+ex.getMessage() );
		                      }
		                    }else{
		                      finish = false;
		                      //////////////////////2009-06-16 因为现金及现金等价物净增加额/////////////////////////////////
		                      /*String[] temp = new String[formula.length];
		                      for(int g=0;g<temp.length;g++){
		                    	  temp[g] = "0.0";
		                      }
		                      reItemData.put(tmpId,temp);*/
		                      /////////////////////////////////////////////////////////////
		                      //reItemData.put(tmpId,"0.0");
		                      break;
		                    }

		                  }
		                }else{
		                  throw new Exception("错误的公式配置：" + param);
		                }
		                //formula[0] = StringUtil.replace(formula[0], '{' + param + '}', value);
		                for(int s=0;s<formula.length;s++){
		                	formula[s] = StringUtil.replace(formula[s], '{' + param + '}', value[s]);
		                	if(formula.length==1){
		    	            	tempF = formula[0];
		    	            }else{
		    	            	tempF = formula[1];
		    	            }
	                    }
		               /* if(formula.length==1){
		                	formula[0] = StringUtil.replace(formula[0], '{' + param + '}', value[0]);
		                }else{
		                	formula[1] = StringUtil.replace(formula[1], '{' + param + '}', value[1]);
		                }*/
		            }
	            
	            if(finish){
	            	  String[] tempValue = new String[formula.length];
		              for(int p=0;p<tempValue.length;p++){
		            	  tempValue[p] = StringUtil.getFormulaValue4Fnc(formula[p]);
		              }
		              reItemData.put(itemId,tempValue);
		              noExEMap.remove(itemId);
	              
	              /*String formulaValue = StringUtil.getFormulaValue4Fnc(formula);
	              reItemData.put(itemId,formulaValue);
	              noExEMap.remove(itemId);*/
	            }
	          }


	          cs++ ;
	          if(cs > 10){
	            Enumeration en1 = noExEMap.keys() ;
	            while(en1.hasMoreElements() ){
	              String itemId = en1.nextElement().toString();
	              String formula = noExEMap.get(itemId).toString();
	            }
	            //break;
	            //throw new Exception("公式嵌套层次错误或本表间公式引用错误" );
	          }
	        }
	      }
	    
	    public double[] getItemValue(String itemId,int dataCol) throws Exception {
	        double[] value = new double[dataCol];
	        if(reItemData.containsKey(itemId)){
	        	String[] tempData = (String[])reItemData.get(itemId);
	        	for(int i=0;i<tempData.length;i++){
	        		value[i] = Double.parseDouble(tempData[i]);
	        	}
	          
	        }else{
	          throw new Exception("没有此指标数据：" + itemId);
	        }
	        return value;
	      }
	    
	    public void initRptMap(FncStatBase fncStatBase){
	    	String cusId = fncStatBase.getCusId();
	    	String statPrdStyle = fncStatBase.getStatPrdStyle();
	    	String statPrd = fncStatBase.getStatPrd();
	    	String statStyle = fncStatBase.getStatStyle();
	        try{
		    	bsMap = (HashMap)this.getComRptData(cusId,statPrdStyle,statPrd,
		    			fncStatBase.getStatBsStyleId(),"FNC_STAT_BS","01",statStyle);//资产负债表
		   
		    	plMap = (HashMap)this.getComRptData(cusId,statPrdStyle,statPrd,
		    			fncStatBase.getStatPlStyleId(),"FNC_STAT_IS","02",statStyle);//损益表
		    
		    	cfMap = (HashMap)this.getComRptData(cusId,statPrdStyle,statPrd,
		    			fncStatBase.getStatCfStyleId(),"FNC_STAT_CFS","03",statStyle);//现金流量表
		    	
		    	jbMap = (HashMap)this.getComRptData(cusId,statPrdStyle,statPrd,
		    			fncStatBase.getStatSlStyleId(),"FNC_STAT_SL","06",statStyle);//财务简表
		    	
		    	fiMap = (HashMap)this.getComRptData(cusId,statPrdStyle,statPrd,
		    			fncStatBase.getStatFiStyleId(),"FNC_INDEX_RPT","04",statStyle);//财务指标表
		     
		
		        reItemData = new TreeMap();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	     }

	    public HashMap objToMap(FncConfStyles fncConfStyles){
	    	HashMap map = new HashMap();
	    	//报表种类 fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
	    	List itemList = fncConfStyles.getItems();
	    	for(int i=0;i<itemList.size();i++){
	    		FncConfDefFormat format = (FncConfDefFormat)itemList.get(i);
	    		map.put(format.getItemId(), format);
	    	}
	    	return map;
	    }
	    
	    /**
		 * 组装标签样式对象(带数据的)
		 * @param 客户编号 cusId
		 * @param 报表周期类型 statPrdStyle (1:月报 2:季报 3:半年报 4:年报)   
		 * @param 报表期间 statPrd 格式：YYYYMM
		 * @param 表名 tableName 
		 * @param 报表种类 fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
		 * @param Connection
		 * @return FncConfStyles
		 */
	    public FncConfStyles getFncConfStyles(String cusId,String statPrdStyle,String statPrd,String styleId,
	    		String tableName,String fncConfTyp,String statStyle)throws AgentException{
	    	FncConfStyles fncConfStyles = null;
	    	FncStatCommonDao fDao = new FncStatCommonDao();
	    	try {
				fncConfStyles = fDao.queryDetailFncConfStyles0(cusId,statPrdStyle,statPrd,
				        styleId,this.getConnection(),tableName,fncConfTyp,statStyle);
			} catch (ComponentException e) {
				e.printStackTrace();
			}
	    	return fncConfStyles;
	    }
	    
	    public HashMap getComRptData(String cusId,String statPrdStyle,String statPrd,String styleId,
	    		String tableName,String fncConfTyp,String statStyle)throws AgentException{
	    	HashMap map = new HashMap();
	    	FncConfStyles fncConfStyles = this.getFncConfStyles(cusId, statPrdStyle, statPrd, styleId,
	    			tableName, fncConfTyp,statStyle);
	    	map = this.objToMap(fncConfStyles);
	    	return map;
	    }
   
	  private String getRptItemValue(String rptType,String itemId,String dataCol) throws Exception {
			  String itemValue = "0";
			  
			  switch (Integer.parseInt(rptType)){
			    case 1:
			      if(bsMap.containsKey(itemId)){
			    	  FncConfDefFormat rid = (FncConfDefFormat)bsMap.get(itemId);
			        if(dataCol.equals("1")){
			          itemValue = String.valueOf(rid.getData1());
			        }else{
			          itemValue = String.valueOf(rid.getData2());
			        }
			      }
			      break;
			    case 2:
			      if(plMap.containsKey(itemId)){
			    	  FncConfDefFormat rid = (FncConfDefFormat)plMap.get(itemId);
			        if(dataCol.equals("1")){
			          itemValue = String.valueOf(rid.getData1());
			        }else{
			          itemValue = String.valueOf(rid.getData2());
			        }
			      }
			      break;
			    case 3:
			      if(cfMap.containsKey(itemId)){
			    	  FncConfDefFormat rid = (FncConfDefFormat)cfMap.get(itemId);
			        if(dataCol.equals("1")){
			          itemValue = String.valueOf(rid.getData1());
			        }else{
			          itemValue = String.valueOf(rid.getData2());
			        }
			      }
			      break;
			    case 4:
			      if(fiMap.containsKey(itemId)){
			    	  FncConfDefFormat rid = (FncConfDefFormat)fiMap.get(itemId);
			        if(dataCol.equals("1")){
			          itemValue = String.valueOf(rid.getData1());
			        }else{
			          itemValue = String.valueOf(rid.getData2());
			        }
			      }
			      break;
			    case 6:
				      if(jbMap.containsKey(itemId)){
				    	  FncConfDefFormat rid = (FncConfDefFormat)jbMap.get(itemId);
				        if(dataCol.equals("1")){
				          itemValue = String.valueOf(rid.getData1());
				        }else{
				          itemValue = String.valueOf(rid.getData2());
				        }
				      }
				      break;
			    default:
			      throw new Exception("报表类型不匹配！");
			  }
			  return itemValue;
	  }
 
	  /**
	   * 计算公式-----结束
	   */
	  
	  /**
	   * 更新状态位
	   */
	  public String updateFncStatFlg(String tempState,Connection conn,FncStatBase tempBase)throws ComponentException{
		  String flagInfo = CMISMessage.DEFEAT;	//默认失败
		  FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		  Context context = this.getContext();
		try {
			String openDay = (String)context.getDataValue(PUBConstant.OPENDAY);//系统日期
			String curUser = (String)context.getDataValue(PUBConstant.currentUserId);//当前登录人
			tempBase.setLastUpdDate(openDay);
			tempBase.setLastUpdId(curUser);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		  flagInfo = fStatCommonAgent.updateFncStatFlg(tempState,conn,tempBase);
		  return flagInfo;
	  }
	  
	  public Fnc4QueryComponent getFnc4QueryComponent()throws ComponentException{
		  Fnc4QueryComponent fnc4QueryComponent = (Fnc4QueryComponent)this.getComponent(PUBConstant.FNC4QC);
		  return fnc4QueryComponent;
	  }
	  
	  /**
	   * 以下是财报导入导出使用
	   */
	  /**
	   * 根据客户编号获取客户的财务报表类型
	   */
	  public String getComFinRepType(String cusId,Connection connection)throws ComponentException{
			String repType = "";
			FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this
			.getAgentInstance(PUBConstant.FNCSTATCOMMON);
			repType = fStatCommonAgent.getComFinRepType(cusId,connection);
			return repType;
	  }
	  
	  public SheetVO[] dealSheetInfo(List listId,String finRepType,String cusId,String statPrdStyle,
			  String statPrd, Connection connection,String statStyle) throws EMPException{
		  
		  FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this
			.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		  
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
				FncConfStyles pfncConfStyles_data = new FncConfStyles();
				try {
					pfncConfStyles_data = this.findOneFncConfStyles(cusId,
							statPrdStyle, statPrd, styleId, tableName, fncConfTyp,statStyle);
				} catch (ComponentException e) {
					e.printStackTrace();
				}

				//得到该样式下的所有的项目的list
				List itemList = fcs_fromCh.getItems();

				int row = 0;//标示导出文件的行数
				int clo = fncCont*dataCol+2*fncCont;//标示导出文件的列数
				//ExportXLDao exportXLDao = new ExportXLDao();
				int counts = 1;
				
				/**
				 * 根据栏位计算项目的个数,根据个数最大的来确定导出文件的行数 
				 */
				row = this.getRow(fncCont, styleId, connection);
				if(row==0){
					sheets = null;
					return sheets;
				}
				//CellVO 
				cells = new CellVO[row][clo];
				//组合文件头
				cells = this.dealTitle(statPrdStyle, fcs_fromCh, cells, statPrd);
				
				//根据报表类型形成表头int clo = fncCont*dataCol+2*fncCont;//标示导出文件的列数
				cells = this.dealTableTitle(cells, fncConfTyp, fncCont, dataCol);
				
				//开始输入数据行
				cells = this.dealData(cells, itemList, fncCont, dataCol, pfncConfStyles_data);
				sheet.cells = cells;
				sheet.rownum = row;
				sheet.colnum = clo;
				sheet.sheetname = "["+finRepType+"|"+styleId+"]"+disName;
				sheets[iId] = sheet;
				
			}
			return sheets;
	  }
	  /**
		 * 根据栏位计算项目的个数,根据个数最大的来确定导出文件的行数 
		 */
	  public int getRow(int fncCont,String styleId,Connection connection)throws ComponentException{
		  FncStatCommonAgent fStatCommonAgent = (FncStatCommonAgent) this
			.getAgentInstance(PUBConstant.FNCSTATCOMMON);
		  int row = 0;
		  int firstLAN_Count = 0;//标示栏位1的项目的个数
			int seconfLAN_Count = 0;//标示栏位2的项目的个数
			int thLAN_Count = 0;//标示栏位3的项目的个数
			int fLAN_Count = 0;//标示栏位4的项目的个数
			
			/**
			 * 根据栏位计算项目的个数,根据个数最大的来确定导出文件的行数 
			 */
			if(fncCont==1){
				firstLAN_Count = fStatCommonAgent.queryCount(styleId, 1, connection);
				row = firstLAN_Count + 5;
			}else if(fncCont==2){
				firstLAN_Count = fStatCommonAgent.queryCount(styleId, 1, connection);
				seconfLAN_Count = fStatCommonAgent.queryCount(styleId, 2, connection);
				if (firstLAN_Count >= seconfLAN_Count) {
					row = firstLAN_Count + 5;
				} else {
					row = seconfLAN_Count + 5;
				}
			}else if(fncCont==3){
				firstLAN_Count = fStatCommonAgent.queryCount(styleId, 1, connection);
				seconfLAN_Count = fStatCommonAgent.queryCount(styleId, 2, connection);
				thLAN_Count = fStatCommonAgent.queryCount(styleId, 3, connection);
				if(firstLAN_Count>=seconfLAN_Count && firstLAN_Count>=thLAN_Count){
					row = firstLAN_Count + 5;
				}else if(seconfLAN_Count>=firstLAN_Count && seconfLAN_Count>=thLAN_Count){
					row = seconfLAN_Count + 5;
				}else if(thLAN_Count>=firstLAN_Count && thLAN_Count>=seconfLAN_Count){
					row = thLAN_Count + 5;
				}else{
					row = firstLAN_Count + 5;
				}
			}else if(fncCont==4){
				firstLAN_Count = fStatCommonAgent.queryCount(styleId, 1, connection);
				seconfLAN_Count = fStatCommonAgent.queryCount(styleId, 2, connection);
				thLAN_Count = fStatCommonAgent.queryCount(styleId, 3, connection);
				fLAN_Count = fStatCommonAgent.queryCount(styleId, 4, connection);
				if(firstLAN_Count>=seconfLAN_Count && firstLAN_Count>=thLAN_Count
						&& firstLAN_Count>=fLAN_Count){
					row = firstLAN_Count + 5;
				}else if(seconfLAN_Count>=firstLAN_Count && seconfLAN_Count>=thLAN_Count
						&& seconfLAN_Count>=fLAN_Count){
					row = seconfLAN_Count + 5;
				}else if(thLAN_Count>=firstLAN_Count && thLAN_Count>=seconfLAN_Count
						&& thLAN_Count>=fLAN_Count){
					row = thLAN_Count + 5;
				}else if(fLAN_Count>=firstLAN_Count && fLAN_Count>=seconfLAN_Count
						&& fLAN_Count>=thLAN_Count){
					row = fLAN_Count + 5;
				}else{
					row = firstLAN_Count + 5;
				}
			}else{
				row = 0;
			}
		  return row;
	  }
	  
	  public CellVO[][] dealTitle(String statPrdStyle,FncConfStyles fcs_fromCh,CellVO[][] cells,String statPrd){
		  if (statPrdStyle.equals("1")) {
				cells[0][1].cellvalue = fcs_fromCh.getFncConfDisName()
						+ "（月报）";
			} else if (statPrdStyle.equals("2")) {
				cells[0][1].cellvalue = fcs_fromCh.getFncConfDisName()
						+ "（季报）";
			} else if (statPrdStyle.equals("3")) {
				cells[0][1].cellvalue = fcs_fromCh.getFncConfDisName()
						+ "（半年报）";
			} else if (statPrdStyle.equals("4")) {
				cells[0][1].cellvalue = fcs_fromCh.getFncConfDisName()
						+ "（年报）";
			}
			cells[1][1].cellvalue = statPrd.substring(0, 4) + "年"
			+ statPrd.substring(4, 6) + "月";
			cells[2][0].cellvalue = "编制单位：福建农信项目组";
			
			return cells;
	  }
	  
	  public CellVO[][] dealTableTitle(CellVO[][] cells,String fncConfTyp,int fncCont,int dataCol){
		//根据报表类型形成表头int clo = fncCont*dataCol+2*fncCont;//标示导出文件的列数
			if ("01".equals(fncConfTyp)||"05".equals(fncConfTyp)) {
				for(int c=0;c<fncCont;c++){
					if(c==0){
						cells[3][0].cellvalue = "项目";
						cells[3][1].cellvalue = "行次";
						cells[3][2].cellvalue = "期初数";
						cells[3][3].cellvalue = "期末数";
					}else{
						cells[3][dataCol+2+c].cellvalue = "项目";
						cells[3][dataCol+2+c+1].cellvalue = "行次";
						cells[3][dataCol+2+c+2].cellvalue = "期初数";
						cells[3][dataCol+2+c+3].cellvalue = "期末数";
					}
				}
			}else if("02".equals(fncConfTyp)){
				for(int c=0;c<fncCont;c++){
					if(c==0){
						cells[3][0].cellvalue = "项目";
						cells[3][1].cellvalue = "行次";
						cells[3][2].cellvalue = "本期数";
						cells[3][3].cellvalue = "本年累计数";
					}else{
						cells[3][dataCol+2+c].cellvalue = "项目";
						cells[3][dataCol+2+c+1].cellvalue = "行次";
						cells[3][dataCol+2+c+2].cellvalue = "本期数";
						cells[3][dataCol+2+c+3].cellvalue = "本年累计数";
					}
				}
			}else if("03".equals(fncConfTyp)||"06".equals(fncConfTyp)){
				for(int c=0;c<fncCont;c++){
					if(c==0){
						cells[3][0].cellvalue = "项目";
						cells[3][1].cellvalue = "行次";
						cells[3][2].cellvalue = "金额";
					}else{
						cells[3][dataCol+2+c].cellvalue = "项目";
						cells[3][dataCol+2+c+1].cellvalue = "行次";
						cells[3][dataCol+2+c+2].cellvalue = "金额";
					}
				}
			}else if("04".equals(fncConfTyp)){
				for(int c=0;c<fncCont;c++){
					if(c==0){
						cells[3][0].cellvalue = "项目";
						cells[3][1].cellvalue = "行次";
						cells[3][2].cellvalue = "指标比率(%)";
					}else{
						cells[3][dataCol+2+c].cellvalue = "项目";
						cells[3][dataCol+2+c+1].cellvalue = "行次";
						cells[3][dataCol+2+c+2].cellvalue = "指标比率(%)";
					}
				}
			}
			
			return cells;
	  }
	  
	  public CellVO[][] dealData(CellVO[][] cells,List itemList,int fncCont,
			  int dataCol,FncConfStyles pfncConfStyles_data){
		  int counts = 1;
		  for(int i = 0; i < itemList.size(); i++){
				FncConfDefFormat item = (FncConfDefFormat) itemList
						.get(i); // 为报表的一行
				int itemCote = item.getFncConfCotes(); // 项目的栏位，1，2
				int order = item.getFncConfOrder(); // 顺序编号
				int Typ = Integer.parseInt(item.getFncItemEditTyp());
				if (3 != Typ) {
					FncConfDefFormat ojb;
					ojb = (FncConfDefFormat) pfncConfStyles_data.getItems().get(
							i);
					for(int c=0;c<fncCont;c++){
						if(c==0){
							cells[4 + c][0].cellvalue = item.getItemName();
							if("1".equals(item.getFncConfRowFlg())){
								cells[4 + c][1].cellvalue = counts + "";//写入行次
								counts++;
							}
							cells[4 + c][2].cellvalue = ojb.getData1() + "";
							if(dataCol==2){
								cells[4 + i][3].cellvalue = ojb.getData2() + "";
							}
						}else{
							cells[4 + c][dataCol+2+c].cellvalue = item
							.getItemName();
							if("1".equals(item.getFncConfRowFlg())){
								cells[4 + c][dataCol+2+c+1].cellvalue = counts + "";//写入行次
								counts++;
							}
							cells[4 + c][dataCol+2+c+2].cellvalue = ojb.getData1()
									+ "";
							if(dataCol==2){
								cells[4 + c][dataCol+2+c+3].cellvalue = ojb.getData2()
								+ "";
							}
						}
					}

				} else {
					for(int c=0;c<fncCont;c++){
						if(c==0){
							cells[4 + c][0].cellvalue = item.getItemName();	
						}else{
							cells[4 + c][dataCol+2+c].cellvalue = item.getItemName();	
						}
					}
				}
				
			}
		  return cells;
	  }
/*	  
	  public double[] getdata(FncConfDefFormat fncConfDefFormat){
		  
	  }*/
	  
	  public FncIndexRpt getIndexValue(String cusId,String itemId,String statStyle)
	  throws ComponentException{
		  FncIndexRpt fncIndexRpt = null;
		//业务代理类初始化
			FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
						getAgentInstance(PUBConstant.FNCSTATCOMMON);
			fncIndexRpt = fsCommonAgent.getIndexValue(cusId,itemId,statStyle,this.getConnection());
		  return fncIndexRpt;
	  }
	  /**
	   * 统计财报数目
	   * @param cusId
	   * @param statPrdStyle
	   * @param statStyle
	   * @return
	   * @throws EMPException
	   */
	  public HashMap<String, String> queryCountFncStatBase(String cusId,String statPrdStyle,String statStyle)  throws EMPException{
		  HashMap<String, String> rq=new HashMap<String, String>();
		//业务代理类初始化
			FncStatCommonAgent fsCommonAgent = (FncStatCommonAgent)this.
						getAgentInstance(PUBConstant.FNCSTATCOMMON);
			rq = fsCommonAgent.queryCountFncStatBase(cusId,statPrdStyle,statStyle,this.getConnection());
		  return rq;
	  }
}

