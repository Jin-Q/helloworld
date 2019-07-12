/*
 * @author yangfei
 * @since 1.2
 *
 * Create on 2009-3-14
 * Copyrigh 2005 Evergreen International Corp.
 */
package com.yucheng.cmis.biz01line.ind.agent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.dao.IndDao;
import com.yucheng.cmis.biz01line.ind.domain.IndGroupDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndGrpIndexDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndLibDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndModelDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndOptDomain;
import com.yucheng.cmis.biz01line.ind.pub.VelocityUtil;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.TreeDicTools;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;


public class IndAgent extends CMISAgent{
	

	private static final Logger logger = Logger.getLogger(IndAgent.class);
	
	/**
	 * 通用的查询列表操作，提供给子类的查询信息的方法
	 * <p>注意对接的表中的每个主要都要设置
	 * @param modelId	主表表模型ID	
	 * @param conditionValues 主表查询条件
	 * @param Sub_Model_Id  从表表模型ID
	 * @param Sub_Tab_cond 从表查询条件
	 * @return	相应的查询对象
	 * @throws AgentException
	 */
	public <CMISDomain>ArrayList query(String modelId, String conditionValues,
			                       String Sub_Model_Id,String  className ) throws AgentException {
        Connection conn = null;
        IndexedCollection iCol = null;
        List list = new ArrayList();
        ArrayList domainList=null;
        try {
            	conn = this.getConnection();
            	String condition=" a where (1=1 and ";
            	/*
            	Iterator conditionKeyIter =  conditionValues.keySet().iterator();
            	while(conditionKeyIter.hasNext()){
            		key=conditionKeyIter.next().toString();
            		condition=condition+" and "+key+" = ";
            		value=conditionValues.get(key).toString();
            		condition +=value;
            	}*/
            	if(Sub_Model_Id.equals(IndPubConstant.IND_GROUP_INDEX))
            	{
            	  /* 根据模型编号拼装查找组相关信息的SQL*/	
            		condition += " exists (select 1 from IND_GROUP_INDEX  b where b.INDEX_NO = a.INDEX_NO and group_no = '"+conditionValues+"'";
            	}
            	else if(Sub_Model_Id.equals(IndPubConstant.IND_MODEL_GROUP))
            	{
            	  /*根据组编号拼装查找指标相关信息的SQL*/
            		condition += "  exists ( select 1 from IND_MODEL_GROUP b where b.GROUP_NO = a.GROUP_NO and model_no = '"+conditionValues+"'";
            	}
			
			condition += " ) )";

            /* 得到表模型*/
            TableModelDAO tDao = this.getTableModelDAO();

            /* 查询数据*/
            iCol = tDao.queryList(modelId, list, condition, conn);
            ComponentHelper componetHelper = new ComponentHelper();
            domainList =(ArrayList) componetHelper.icol2domainlist(className, iCol);

        } catch (Exception e) {
        	logger.error("方法  <CMISDomain>ArrayList  查询失败 ...."+e.getMessage(), e);
    
            throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
        } finally {
            /* 释放资源*/
            if (conn != null) {
                this.releaseConnection(conn);
            }
        }
        return domainList;
    }
	
	/**
	 * 生成企事业指标分析jsp页面
	 * @param tofile
	 * @param modelNo
	 * @throws AgentException
	 */
	public void genCusComJspFile(String modelNo) throws AgentException{
		IndDao dao=null;
		VelocityUtil vu=null;
		try{
			vu=new VelocityUtil();
			dao=new IndDao();
			dao.setConnection(this.getConnection());
			/*1.根据模型编号查询模型下的组信息*/
			ArrayList grplist=dao.queryModelGroups(modelNo);
			/*2.循环模型 下的组信息,按组生成每个jsp文件*/
			if(grplist!=null&&grplist.size()>0){
				Iterator<HashMap> it= grplist.iterator();
				while(it.hasNext()){
					HashMap hm=it.next();
					String groupno=(String)hm.get("group_no");
					String groupname=(String)hm.get("group_name");
					ArrayList<HashMap> indexlist=dao.queryGroupIndexes(groupno);
					vu.genRscJspFile(groupno, groupname, indexlist, IndPubConstant.RSC_VM_FILE, groupno+".jsp");
				}
			}
		}catch(Exception ex){
			logger.error("方法  genCusComJspFile 查询失败...."+ex.getMessage(), ex);
			throw new AgentException(ex);
		} 
	}
	/**
	 * 生成信用评级评估jsp页面
	 * @param modelNo
	 * @throws AgentException
	 */
	public void genCcrJspFile(String modelNo) throws AgentException{
		IndDao dao=null;
		VelocityUtil vu=null;
		Connection conn=null;
		try{
			vu=new VelocityUtil();
			dao=new IndDao();
			conn=this.getConnection();
			dao.setConnection(conn);
			/*1.根据模型编号查询模型下的组信息 */
			ArrayList grplist=dao.queryModelGroups(modelNo);
			/*2.循环模型 下的组信息,按组生成每个jsp文件*/
			/*System.err.println("grplist="+grplist);*/
			if(grplist!=null&&grplist.size()>0){
				Iterator<HashMap> it= grplist.iterator();
				while(it.hasNext()){
					HashMap hm=it.next();
					String groupno=(String)hm.get("group_no");
					/*tring groupname=(String)hm.get("group_name");*/
					ArrayList<HashMap> indexlist=dao.queryGroupIndexes(groupno);
					Iterator<HashMap> indexIt=indexlist.iterator();
					while(indexIt!=null&&indexIt.hasNext()){
						HashMap index=indexIt.next();
						String indexNo=(String)index.get("index_no");
						ArrayList<HashMap> opts=dao.queryIndexOpts(indexNo);  
						index.put("opts", opts);
					} 
				    hm.put("groupindexes", indexlist); 
				}
				vu.genCcrJspFile(grplist, IndPubConstant.CCR_VM_FILE, modelNo+".jsp");
			}

		}catch(Exception ex){
			logger.error("方法  genCcrJspFile 查询失败..."+ex.getMessage(), ex);
			throw new AgentException(ex);
		} 
	} 
	
	/**
	 * 根据规则生成信用评级评估jsp页面
	 * @param modelNo
	 * @throws AgentException
	 */
	public void genCcrJspFileWithShuffle(String modelNo) throws AgentException{
		IndDao dao=null;
		VelocityUtil vu=null;
		Connection conn=null;
		try{
			vu=new VelocityUtil();
			dao=new IndDao();
			conn=this.getConnection();
			dao.setConnection(conn);
			/*1.根据模型编号查询模型下的组信息 */
			ArrayList grplist=dao.queryModelGroups(modelNo);
			/*2.循环模型 下的组信息,按组生成每个jsp文件*/
			/*System.err.println("grplist="+grplist);*/
			if(grplist!=null&&grplist.size()>0){
				Iterator<HashMap> it= grplist.iterator();
				while(it.hasNext()){
					HashMap hm=it.next();
					//根据组下规则交易获取规则信息

					HashMap map=new HashMap();
					map.put("group_no", hm.get("group_no"));
					map.put("group_name", hm.get("group_name"));
					map.put("trans_id", hm.get("trans_id"));
					map.put("pk_value", "");
					map.put("cus_id", "");
					ArrayList<HashMap> indexlist=queryGroupIndexesWithShuffle(map);
					Iterator<HashMap> indexIt=indexlist.iterator();
					while(indexIt!=null&&indexIt.hasNext()){
						HashMap index=indexIt.next();
						HashMap indexmap=new HashMap();
						indexmap.put("index_no", index.get("index_no"));
						indexmap.put("index_name", index.get("index_name"));
						ArrayList<HashMap> opts=queryIndexOptsWithShuffle(indexmap);  
						index.put("opts", opts);
					} 
				    hm.put("groupindexes", indexlist); 
				}
				vu.genCcrJspFile(grplist, IndPubConstant.CCR_VM_FILE, modelNo+".jsp");
			}

		}catch(Exception ex){
			logger.error("方法  genCcrJspFile 查询失败..."+ex.getMessage(), ex);
			throw new AgentException(ex);
		} 
	}
	
	/**
	 * 查询组下规则交易的所有指标信息
	 * @param paramap
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap> queryGroupIndexesWithShuffle(Map paramap) throws AgentException{
		ArrayList<HashMap> list=null;
		try {
			// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices","shuffle");
			
			String transId=(String) paramap.get("trans_id");
			String groupNo=(String) paramap.get("group_no");
			String groupName=(String) paramap.get("group_name");
			String pkVal=(String) paramap.get("pk_value");
			String cus_id=(String) paramap.get("cusId");
			String fnc_year=(String) paramap.get("fnc_year");
			String fnc_month=(String) paramap.get("fnc_month");
			String stat_prd_style=(String) paramap.get("stat_prd_style");
			//根据交易id取得匹配规则
			Map ruleMap=shuffleService.getRuleMapByTransId(transId);
			list=new ArrayList<HashMap>();
			Iterator it=ruleMap.keySet().iterator();
			List ruleList;
			String rulesetid,ruleid;
			while(it.hasNext()){
				rulesetid=(String)it.next();
				ruleList=(List)ruleMap.get(rulesetid);
				//读取匹配规则并加入list
				for(int i=0;i<ruleList.size();i++){
					ruleid=(String)ruleList.get(i);
					Map map=new HashMap();
					Map inputMap=new HashMap();
					inputMap.put("IN_输入值", "");//指标输入值在模型页面对应的即指标项真实值，这里用于模型产生值为空
					inputMap.put("IN_业务编号", pkVal);//业务主键值
					inputMap.put("IN_客户码", cus_id);//客户ID
					inputMap.put("IN_会计年份", fnc_year);//会计年份
					inputMap.put("IN_会计月份", fnc_month);//会计月份
					inputMap.put("IN_报表周期类型",stat_prd_style);//会计月份
					map = shuffleService.fireTargetRule(rulesetid,ruleid,inputMap);//执行规则取得规则返回值用于构建模型指标项
					System.out.println(map.toString());
					HashMap hm=new HashMap<String,String>(); 
					hm.put("group_no", groupNo);//指标组编号
					hm.put("group_name", groupName);//指标组名称
					hm.put("index_no", rulesetid+"$"+ruleid);//指标编号由规则集ID_规则ID组成，由此在执行模型评分时，可以通过指标编号调用单个规则
					String index_name=(String) map.get("OUT_指标名称");
					hm.put("index_name", index_name);//指标名称
					String index_value=(String) map.get("OUT_指标值");
					hm.put("index_value",index_value);//
					String input_type=(String) map.get("OUT_指标取值方式");
					input_type=IndPubConstant.CCRINPUTTYPE.get(input_type);
					hm.put("index_input_type", input_type);
					hm.put("input_classpath", "");//取值实现类
					hm.put("ind_std_score", "");//标准分
					String ind_dis_type=(String) map.get("OUT_指标显示方式");
					ind_dis_type=IndPubConstant.CCRINDDISTYPE.get(ind_dis_type);
					hm.put("ind_dis_type", ind_dis_type);//指标显示类型
					hm.put("index_type", ""); //指标类别
					hm.put("memo","");//备注
					list.add(hm);
				}
			}		
		} catch (Exception e) {
			EMPLog.log("IntAgent", EMPLog.ERROR, 0,
					"queryGroupIndexesWithShuffle error!", e);
			throw new AgentException(e);
		}
		return list;
	}
	
	/**
	 * 根据规则集查询查询指标下的所有选项信息
	 * 每个规则通过index_name取该参数信息用于构建模型指标
	 * @param paramap
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap> queryIndexOptsWithShuffle(Map paramap) throws AgentException{
		ArrayList<HashMap> list=null;
		try {
			// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices","shuffle");
		
			String indexNO=(String) paramap.get("index_no");
			String indexName=(String) paramap.get("index_name");
			String ruleSetId=indexNO.split("\\$")[0];//指标编号为(规则集ID_规则ID)
			list=new ArrayList<HashMap>();
			
			//根据参数信息产生所有选项信息
			ArrayList parameterlist=new ArrayList();
			parameterlist = shuffleService.getParameterInfo(ruleSetId, indexName);
			Iterator it=parameterlist.iterator();
			while(it.hasNext()){
				String desc=(String) it.next();
				HashMap<String, String> hm=new HashMap<String,String>(); 
				hm.put("index_no", indexNO);
				hm.put("ind_desc", desc.split(":")[0]);//描述
				hm.put("index_value", desc.split(":")[1]); 
				hm.put("value_score", "");
				list.add(hm);
			}
		
		} catch (Exception e) {
			EMPLog.log("IndAgent", EMPLog.ERROR, 0,
					"queryIndexOptsWithShuffle error!", e);
			throw new AgentException(e);
		}
		return list;
	}
	/*private Connection getConn(){
		String user = "cmis";
		String password = "cmis";
		String url = "jdbc:oracle:thin:@192.100.2.129:1521:CMIS";
		String driver = "oracle.jdbc.driver.OracleDriver";
		Connection conn=null;
		try {
			 Class.forName(driver); 
			 conn = DriverManager.getConnection(url, user, password); 
			 
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return conn;
	}*/
	/**
	 * 查询模型下的所有组信息
	 * @param modelNo
	 * @return
	 * @throws AgentException
	 */
	public ArrayList<HashMap> queryModGrpList(String modelNo) throws AgentException{
		IndDao dao=null;
		ArrayList<HashMap> list=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			list=dao.queryModelGroups(modelNo);
		} catch (DaoException e) { 
			logger.error("方法  queryModGrpList 查询失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return list;
	}
	/**
	 * 查询组下的所有指标信息
	 * @param groupNo
	 * @return
	 * @throws AgentException
	 */
	public ArrayList<HashMap> queryGrpIndexesList(String groupNo) throws AgentException{
		IndDao dao=null;
		ArrayList<HashMap> list=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			list=dao.queryGroupIndexes(groupNo);
		} catch (DaoException e) { 
			logger.error("方法  queryGrpIndexesList  查询失败"+e.getMessage(), e);
			throw new AgentException(e);
		}
		return list;
	}
	
	/**
	 * 查询指标下的所有选项信息
	 * @param groupNo
	 * @return 
	 * @throws AgentException
	 */
	public ArrayList<HashMap> queryIndOptsList(String indexNO) throws AgentException{
		IndDao dao=null;
		ArrayList<HashMap> list=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			list=dao.queryIndexOpts(indexNO);
		} catch (DaoException e) { 
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
                    + e.toString());
			throw new AgentException(e);
		}
		return list;
	}
	
	/**
	 * 取该业务编号下所有定量指标值
	 * @param serno 客户号
	 * @return
	 */
	public ArrayList<HashMap> queryIndResValList(String serno) throws AgentException{
		ArrayList<HashMap> arr = null;
		 try {
			 IndDao dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			arr=dao.queryIndResValList(serno);
		} catch (DaoException e) { 
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
                    + e.toString());
			throw new AgentException(e);
		}
		return arr;
	}
	
	public static void main(String args[]){
		
		try{
			 IndAgent ind=new IndAgent();
			 ind.genCcrJspFile("M20090300012");
		}catch(Exception ex){
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "查询失败\n"
                    + ex.toString());
		}
	}
	
	/**
	 * 查询指标下信息
	 * @param modelId 表模型ID
	 * @param conditionStr 指标编号的HASHMAP
	 * @return 
	 * @throws AgentException
	 */
	public CMISDomain queryIndLibDetail(String modelId, HashMap conditionStr) throws AgentException {
		Connection conn = null;
		KeyedCollection kCol = null;

		/*这个domain必须实例化，否则无法返回 */
		CMISDomain domain = null; 
		if(modelId.equals(IndPubConstant.IND_LIB))
		{
		  domain= new IndLibDomain();
		}
		else if(modelId.equals(IndPubConstant.IND_GROUP_INDEX))
		{
			domain = new IndGrpIndexDomain();
		}
		else if(modelId.equals(IndPubConstant.IND_OPT))
		{
			 domain = new IndOptDomain();
		}
     try {
         	conn = this.getConnection();
    
         	/* 得到表模型 */
         	TableModelDAO tDao = this.getTableModelDAO();
         	/* 查询数据 */
         	kCol = tDao.queryAllDetail(modelId, conditionStr, conn);
    
         	/* 将KCOL转换成DOMAIN */
         	ComponentHelper cHelper = new ComponentHelper();
         	domain = cHelper.kcolTOdomain(domain, kCol);
     } catch (Exception e) {
    	 logger.error("方法  queryIndLibDetail 查询失败...."+e.getMessage(), e);
         throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
     } finally {
    /* 释放资源 */
    if (conn != null) {
        this.releaseConnection(conn);
    }
     }
     return domain;
	}
	
	/**
	 * 查询组下信息
	 * @param modelId 表模型ID
	 * @param conditionStr 组编号的HASHMAP
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndGroupDetail(String modelId, HashMap conditionStr) throws AgentException {
		Connection conn = null;
		KeyedCollection kCol = null;

		/*这个domain必须实例化，否则无法返回*/
		CMISDomain domain = null; 
		domain= new IndGroupDomain();

     try {
         	conn = this.getConnection();
    
         	/* 得到表模型 */
         	TableModelDAO tDao = this.getTableModelDAO();
         	/* 查询数据 */
         	kCol = tDao.queryAllDetail(modelId, conditionStr, conn);
    
         	/* 将KCOL转换成DOMAIN */
         	ComponentHelper cHelper = new ComponentHelper();
         	domain = cHelper.kcolTOdomain(domain, kCol);
     } catch (Exception e) {
    	 logger.error("方法  queryIndGroupDetail  查询失败...." + e.getMessage(), e);
         throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
     } finally {
    /* 释放资源 */
    if (conn != null) {
        this.releaseConnection(conn);
    }
     }
     return domain;
	}
	
	
	/**
	 * 查询模型下信息
	 * @param modelId 表模型ID
	 * @param conditionStr 模型编号的HASHMAP
	 * @return 
	 * @throws AgentException
	 */
	
	public CMISDomain queryIndModelDetail(String modelId, HashMap conditionStr) throws AgentException {
		Connection conn = null;
		KeyedCollection kCol = null;

		/*这个domain必须实例化，否则无法返回 */
		CMISDomain domain = null; 
		domain= new IndModelDomain();

     try {
         	conn = this.getConnection();
    
         	/* 得到表模型*/
         	TableModelDAO tDao = this.getTableModelDAO();
         	/* 查询数据*/
         	kCol = tDao.queryAllDetail(modelId, conditionStr, conn);
    
         	/* 将KCOL转换成DOMAIN*/
         	ComponentHelper cHelper = new ComponentHelper();
         	domain = cHelper.kcolTOdomain(domain, kCol);
     } catch (Exception e) {
    	    logger.error("方法 queryIndModelDetail 查询失败...."+e.getMessage(), e);
            throw new AgentException(CMISMessage.QUERYERROR, "查询失败");
     } finally {
    /* 释放资源*/
    if (conn != null) {
        this.releaseConnection(conn);
    }
     }
     return domain;
	}
	
	/**
	 * 模型复制
	 * @param key_model_no  新增模型编号
	 * @param model_no 需要复制的模型编号
	 * @return
	 * @throws AgentException
	 */
	public String modelCopy(String key_model_no,String model_no) throws AgentException{
		IndDao dao=null;
		String strReturnMessage = CMISMessage.ADDDEFEAT;
		int count = 0;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			count = dao.modelCopy(key_model_no,model_no);
		} catch (DaoException e) { 
			logger.error("方法 modelCopy 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		
		if (count == 1)
		{
			strReturnMessage = CMISMessage.ADDSUCCEESS;
		}
		
		return strReturnMessage;
	}
	
	/**
	 * 将指标得分数据插入指标得分结果表中
	 * @param hs
	 * @return
	 * @throws AgentException 
	 */	
	public int insertIndResultVal(HashMap<String,String> hs) throws AgentException
	{
		int count = 0;
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			count = dao.insertIndResultVal(hs);
		} catch (DaoException e) { 
			logger.error("方法 insertIndResultVal 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return count ;
	}
	
	/**
	 * 根据指标值，指标选项值获取指标名称
	 * @param indexNo  指标编号
	 * @param indexValue 指标选项值
	 * @return
	 * @throws AgentException
	 */
	public String queryIndDesc(String indexNo,String indexValue) throws AgentException{
		String Reason = "";
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			Reason = dao.queryIndDesc(indexNo,indexValue);
		} catch (DaoException e) { 
			logger.error("方法 queryIndDesc 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return Reason;
	}
	
	/**
	 * 根据编号，日期删除指标结果表里面的记录
	 * @param serno  编号
	 * @param ind_date 日期
	 * @return
	 * @throws AgentException
	 */
	
	public int deleteIndResultVal(String serno,String ind_date) throws AgentException
	{
		int count = 0;
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			count = dao.deleteIndResultVal(serno,ind_date);
		} catch (DaoException e) { 
			logger.error("方法 deleteIndResultVal 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return count;
	}
	
	/**
	 * 根据组编号，指标编号查询组指标表
	 * @param group_no  组编号
	 * @param index_no 指标编号
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String>queryIndGroupIndexDetail(String group_no,String index_no) throws AgentException
	{
		HashMap<String,String> hm= null;
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			hm = dao.queryIndGroupIndexDetail(group_no,index_no);
		} catch (DaoException e) { 
			logger.error("方法 queryIndGroupIndexDetail 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return hm;
	}
	
	/**
	 * 根据组编号，指标编号查询组指标表
	 * @param index_no  指标编号
	 * @param index_value 指标选项值
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String>queryIndOptDetail(String index_no,String index_value) throws AgentException
	{
		HashMap<String,String> hm = null;
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			hm = dao.queryIndOptDetail(index_no,index_value); 
		} catch (DaoException e) { 
			logger.error("方法 queryIndOptDetail 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return hm;
	}
	/**
	 * 查询指标详细信息
	 * @param index_no  指标编号
	 * @param index_value 指标选项值
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String>queryIndLibDetail(String index_no) throws AgentException
	{
		HashMap<String,String> hm = null;
		IndDao dao=null;
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			hm = dao.queryIndLibDetail(index_no);
		} catch (DaoException e) { 
			logger.error("方法 queryIndLibDetail 执行失败...."+e.getMessage(), e);
			throw new AgentException(e);
		}
		return hm;
	}
	/**
	 * 查询指标结果值表
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws AgentException
	 */
	public String queryIndResVal(String serno,int year,int month,int day,String indexno) throws AgentException{
		IndDao dao=null;
		String retVal="";
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection());
			retVal=dao.queryIndResVal(serno, year, month, day, indexno);
		 }catch(DaoException de){
			 logger.error("方法 queryIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		 } 
		 return retVal;
	}
	/**
	 * 删除指标结果值表数据
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws AgentException
	 */
	public void delIndResVal(String serno,int year,int month,int day,String indexno)throws AgentException{
		IndDao dao=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection());
			dao.delIndResVal(serno, year, month, day, indexno);
		}catch(DaoException de){
			 logger.error("方法 delIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		 } 
	}
	/**
	 * 查询指标结果值表
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws AgentException
	 */
	public String queryIndResValByNo(String serno,String indexno) throws AgentException{
		IndDao dao=null;
		String retVal="";
		 try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection());
			retVal=dao.queryIndResValByNo(serno,indexno);
		 }catch(DaoException de){
			 logger.error("方法 queryIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		 } 
		 return retVal;
	}
	/**
	 * 删除指标结果值表数据
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws AgentException
	 */
	public void delIndResValByNo(String serno ,String indexno)throws AgentException{
		IndDao dao=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection());
			dao.delIndResValByNo(serno, indexno);
		}catch(DaoException de){
			 logger.error("方法 delIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		 } 
	}
	/**
	 * 查询指标下的所有选项得分，按(选项值：选项得分)，放入HashMap中
	 * @param indexno
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String> queryIndexOptScore(String indexno) throws AgentException{
		IndDao dao=null;
		HashMap<String,String>hm=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			hm=dao.queryIndexOptScore(indexno);
		}catch(DaoException de){
			 logger.error("方法 delIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		} 
		return hm;
	}
	/**
	 * 取模型编号
	 * @param hm
	 * @return
	 * @throws AgentException
	 */
	public String getModelNoForCcr(HashMap<String,String> hm) throws AgentException{
		IndDao dao=null;
		String retVal=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			String condstr="";
			String cus_type=hm.get("cus_type");//客户类型
			String com_opt_scale = hm.get("com_opt_scale");//企业规模  
			TreeDicTools treeDicTool = new TreeDicTools();
			Connection conn = this.getConnection();
			String comFldTyp = hm.get("com_fld_typ"); //行业类型
			String fna_type = hm.get("fna_type");//财务报表类型
			String com_scale_ccr = hm.get("com_scale_ccr");	//企业规模  00--待认定--10大型或特大型企业 20--中型企业30--小型企业
			System.out.println("企业规模"+com_scale_ccr);
			String clltype="";
			String locateList [] =null;
			try { 
				locateList = treeDicTool.getLocateNodes(comFldTyp, conn);
			} catch (SQLException e) { 
				throw new AgentException("获取行业类型["+comFldTyp+"]的所有节点分类失败"+e);
			}
			String val="";//行业标准
			if(locateList!=null&&locateList.length!=0){
				for(int i=0;i<locateList.length;i++){
					System.out.println("打印行业类别==============："+locateList[i]);

  				   if("30".equals(com_scale_ccr)){
						//取小企业模型编号
						val=CcrPubConstant.getSmallComLlyType(locateList[i]);	
						System.out.println("取小企业模型编号");

					}else{
						//取一般企业模型编号
						val=CcrPubConstant.getComLlyType(locateList[i]);		
						System.out.println("取企业模型编号");
					} 
					System.out.println("打印行业类别模板===============："+val);

					if(val!=null&&!val.trim().equals("")){
						logger.info("行业类型:"+comFldTyp+",对应行业标准值:"+val);
						clltype=val; 
						break;
					}
					if(i==locateList.length-1){
						logger.error("行业类型:"+comFldTyp+",无对应行业标准值:"+val);
						throw new AgentException("行业类型:"+comFldTyp+",无对应行业标准值:"+val);
					}
				}
			} 
			condstr=condstr+" com_biz_kind='200000'";
			
			if(cus_type!=null&&!cus_type.trim().equals("")){
				condstr=condstr+" and cus_type like '%"+cus_type+"%'";
			}
			if(fna_type!=null&&!fna_type.trim().equals("")){
				condstr=condstr+" and fna_rep_type='"+fna_type+"'";
			}
			if(com_opt_scale!=null&&!com_opt_scale.trim().equals("")){ 
				condstr=condstr+" and com_opt_scale  like '%"+com_opt_scale+"%'";
			} 
			if(clltype!=null&&!clltype.trim().equals("")){ 
				condstr=condstr+" and com_cus_kind  like '%"+clltype+"%'";
			} 
			
			logger.info("评级模型条件:"+condstr); 
			retVal=dao.getModelNoForCcrRsc(condstr);
			logger.info("根据企业规模:" + com_opt_scale + ",客户类型:" + cus_type
					+ ",行业类型:" + comFldTyp +",对应行业标准值:"+val+ ",财报类型:"+fna_type+"取得评级模型:" + retVal);
			if(retVal==null||retVal.trim().equals("")){
				//logger.error("根据企业规模:"+com_opt_scale+ ",财报类型:"+fna_type+",客户类型:"+cus_type+",行业类型:"+comFldTyp+"无法取得对应模型");
				throw new AgentException("根据企业规模:"+com_opt_scale+",客户类型:"
						+cus_type+ ",财报类型:"+fna_type+",行业类型:"+comFldTyp+",对应行业标准值:"+val+",无法取得对应模型");
			}
		}catch(DaoException de){
			 logger.error("方法 getModelNo 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		}
		return retVal;
	}
	
	/**
	 * 目前不好使
	 * @param map
	 * @return
	 * @throws AgentException
	 */
	public String getModelNoForRsc(HashMap<String,String> map) throws AgentException{
		IndDao dao=null;
		String retVal=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			String condstr="";
			String cus_type=map.get("cus_type");//客户类型
			String biz_type = map.get("biz_type");//业务品种
			String amount=map.get("amount");//额度 
			if(amount==null||amount.trim().equals("")){
				amount="0";
			}
			condstr=condstr+" com_biz_kind ='"+biz_type+"'";
			if(cus_type!=null&&!cus_type.trim().equals("")){
				condstr=condstr+" and cus_type like '%"+cus_type.trim()+"%'";
			}
		 
			condstr=condstr+" and ceiling_limit>="+amount+" and lower_limit<="+amount;
			retVal=dao.getModelNoForCcrRsc(condstr);
		}catch(DaoException de){
			 logger.error("方法 getModelNoForRsc 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		}
		return retVal;
	}
	/**
	 * 通过模型编号获取财务指标编号列表
	 * @param modelNo
	 * @return
	 * @throws AgentException
	 */
	public ArrayList queryFncIndexRpt(String modelNo) throws AgentException{
		IndDao dao=null;
		ArrayList retVal=null;
		try {
			dao = new IndDao(); 
			dao.setConnection(this.getConnection()); 
			retVal=dao.queryFncIndexRpt(modelNo);
		}catch(DaoException de){
			 logger.error("方法 queryFncIndexRpt 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		}
		return retVal;
		
	}
	/**
	 * 根据模型编号获取模型得分
	 * @param modelNo
	 * @return
	 * @throws AgentException
	 */	
	public String getModelAllScore(String modelNo) throws AgentException{
		String retValue = "";
		try {
			IndDao dao = new IndDao(); 
			dao.setConnection(this.getConnection());
			retValue = dao.getModelAllScore(modelNo);
		}catch(DaoException de){
			 logger.error("方法 delIndResVal 执行失败...."+de.getMessage(), de);
			 throw new AgentException(de);
		 } 
		return retValue;
	}
	
}
