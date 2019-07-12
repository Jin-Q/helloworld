package com.yucheng.cmis.biz01line.qry.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.qry.agent.QryAgent;
import com.yucheng.cmis.biz01line.qry.domain.QryParam;
import com.yucheng.cmis.biz01line.qry.domain.QryTemplet;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class QryExeComponent extends CMISComponent {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	
public String doQuery(String tempNo,String showColumns,String orderByColumns,int max) throws ComponentException{
		
		HashMap<String,String> conditionMap = null;  //条件数据
		HashMap<String,String> operationMap = null;  //数据库操作类型
		QryTemplet qryTemplet = null;     		 //查询模板信息
		HashMap<String,String> translate = null;     //翻译字典项
		HashMap<String,String> translateNum = null;     //翻译字典项
		
		Collection dbConditionCon = null;   //数据库表中配置的查询条件(小写)
		Collection<HashMap> dbShowCol = null;    //展示字段(小写)
		Collection<String> data = null;          //用于预编译语句的setString,需要使用ArrayList实例化
		String ChineseName;							//字典翻译的中文显示信息
		String sql;								//查询sql语句
		String ExtendClass;						//扩展类名称
		//创建业务代理类
        QryAgent qryAgent;
		try {
			qryAgent = (QryAgent)this.getAgentInstance(QryPubConstant.QRYAGENT);
			qryAgent.getQryParamList(tempNo);
			translate = new HashMap<String,String>();
			translateNum = new HashMap<String,String>();
		
			/*取数据库查询条件字段*/
			dbConditionCon = qryAgent.getQryParamList(tempNo);
			
			 /** 判断是否是通过其他查询结果链接进来   2014-03-31  唐顺岩   */
			Context context = this.getContext();
            if(context.containsKey("link")){
            	String param = (String)context.getDataValue("param");   //获得参数值
            	if(dbConditionCon.size()>1){
        			throw new EMPException("查询结果对应内部链接不能包含多个参数！");
        		}else{
        			for (Iterator<QryParam> itr = dbConditionCon.iterator(); itr.hasNext();) {
                        QryParam qryParam = (QryParam) itr.next();
                        String PAGE_ID = qryParam.getEnname();
                        PAGE_ID = PAGE_ID.toLowerCase();
                        
                        context.put(PAGE_ID+"_VALUE", param);  //参数值
                        context.put(PAGE_ID+"_OPER", "1");
                        context.put(PAGE_ID+"_DATATYPE", "02");  //参数类型 
        			}
        		}
            	
//            	//判断内部链接对应查询分析是否包含多个参数，如果存在多个参数直接抛出异常提示
//            	String param_count = qryExeComponent.selectParamCountByTempno(tempNo);
//            	if(null!=param_count && !"".equals(param_count)){
//            		int param_num = Integer.parseInt(param_count);  //得到参数个数
//            		if(param_num>1){
//            			throw new EMPException("查询结果对应内部链接不能包含多个参数！");
//            		}
//            	}
            }
            /**END*/	
			
            /**如果查询条件为空，在context中绑定 link 标记，用于控制结果页面的返回按钮   2014-04-01 */
			if( dbConditionCon == null || dbConditionCon.size() ==0 ){
				//throw new ComponentException("取得配置条件错！");
				 context.put("link", "Y");  //参数值
			}
			/**END*/
			
			/*取数据库查询结果字段*/
			dbShowCol = qryAgent.getDBShowColumns(tempNo, translate, translateNum);
			if( dbShowCol == null || dbShowCol.size() ==0 ){
				throw new ComponentException("取得展示字段错！");
			}
			
			showColumns = getSelectColumns(showColumns, dbShowCol);
			int i = showColumns.indexOf("$EOF$");
			
			if( i == -1 ){
				throw new ComponentException("解析中文列名错");
			}else{
				ChineseName = showColumns.substring(i+5);
				showColumns = showColumns.substring(0,i);
			}
			
			conditionMap = new HashMap<String,String>();
			operationMap = new HashMap<String,String>();
			
			/*对前台传入信息打包*/
			qryAgent.packData2Map(conditionMap, operationMap,dbConditionCon,max);
			
			/*获取查询分析主信息*/
			qryTemplet = qryAgent.getQryTemplet(tempNo);
			if( qryTemplet == null||qryTemplet.getTempNo()==null){
				throw new ComponentException("未取得查询分析『"+tempNo+"』SQL,CLASS信息");
			}
			/**设置查询模板名称  2014-03-31*/
			context.put("TEMP_NAME", qryTemplet.getTempName());   //查询模板名称
			/**END*/ 
			sql = qryTemplet.getQuerySql();
			i = sql.indexOf("from");
			if( i == -1 ){
			     throw new ComponentException("SQL语句错误<br>『"+sql+"』");
			}else{
				sql = "select "+showColumns+" FROM("+sql+")";
			}
			
			ExtendClass = qryTemplet.getClasspath();
			/*1简单查询 2扩展查询  先执行扩展类插入临时表*/
			if( "2".equals(qryTemplet.getTempPattern()) ){
				qryAgent.invokeExtendClass(conditionMap, operationMap, ExtendClass);
			}
			
			/*解析SQL语句，并做机构权限检查*/
			data = new ArrayList<String>();
			
			sql = qryAgent.analyseSql(conditionMap, operationMap,data, sql);
			
			/*加入排序字段*/
			if( sql.indexOf(" order ")>-1 && sql.indexOf(" by ") > -1 && !"".equals(orderByColumns) ){
				sql = sql + "," + orderByColumns;
			}else if( !"".equals(orderByColumns) ){
				sql = sql + "  order by "+orderByColumns; 
			}
            EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "print sql before check===================================" + sql);
			/*SQL语句合法性检查，暂不考虑传入条件数据不合法情况*/
			if( !isSqlOk(sql) ){
				throw new ComponentException("SQL不合法，请检查SQL中是否含有drop,delete,update,create等语句");
			}
			
			/*执行SQL语句并生成文本文件*/
			//String fileName = qryAgent.genFile(sql, ChineseName, max, translate, translateNum, data);
			String fileName = qryAgent.genFile(sql, ChineseName, max, translate, translateNum, data, tempNo);
			
			/*传入前台所需结果*/
			return fileName;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ComponentException(e);
		} finally{
			
			if( conditionMap != null ) conditionMap.clear();
			if( operationMap != null ) operationMap.clear();
			if( translate != null ) translate.clear();
			if( translateNum != null ) translateNum.clear();
			
			if( dbConditionCon != null ) dbConditionCon.clear();
			if( dbShowCol != null ) dbShowCol.clear();
			if( data != null ) data.clear();
		}
	}
	
    public String getSelectColumns(String showColumns, Collection<HashMap> dbShowCol) throws ComponentException{
 	   String str = " ";
 	   String cnname = "";
 	   HashMap<String,String> hm = null;
 	   Collection col = null;
 	   try {
 		  col = new ArrayList();
			  for( Iterator<HashMap> itr=dbShowCol.iterator(); itr.hasNext(); ){
				  hm = (HashMap<String,String>)itr.next();
				  if( !"".equals(showColumns) ){
					  /*不在展示字段列表里*/
					  if( showColumns.toLowerCase().indexOf("|"+hm.get("ENNAME")+"|") == -1 ){
						  hm.clear();
						  col.add(hm);
						  continue;
					  }
				  }
				  str = str + hm.get("ENNAME")+",";
				  cnname = cnname + hm.get("CNNAME")+"|";
			  }
			  if( "".equals(str.trim()) ){
				  throw new ComponentException("取得查询字段列失败！");
			  }else{
				  str = str.substring(0,str.length()-1);
			  }
			  dbShowCol.removeAll(col);
			  str = str + "$EOF$"+cnname;
		   } catch (Exception e) {
			  logger.error(e.getMessage(),e);
			  throw new ComponentException(e);
		   }
		   return str;
    }
	private boolean isSqlOk(String sql) throws ComponentException{
		if( sql.indexOf("drop ") > 0 || sql.indexOf("delete ")>0 ||
			sql.indexOf("create ") > 0 || sql.indexOf("update ")>0 ||
			sql.indexOf("alter ") > 0 ){
			return false;
		}
		return true;
	}
	
	 /**
     * 判断查询模板下查询条件的数量
     * @param tempno 查询模板编号
     * @return
     */
    public String selectParamCountByTempno(String tempno)throws ComponentException {
    	//创建业务代理类
        QryAgent qryAgent;
		try {
			qryAgent = (QryAgent)this.getAgentInstance(QryPubConstant.QRYAGENT);
			return qryAgent.selectParamCountByTempno(tempno);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}
    }
}
