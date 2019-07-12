package com.yucheng.cmis.biz01line.ccr.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.dao.CcrAppInfoDao;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppFinGuar;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrMGroupScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrModelScore;
import com.yucheng.cmis.biz01line.ind.domain.IndGroupDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndLibDomain;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CcrAgent extends CMISAgent {
	
	/**
	 * 新增一个ccrAppInfo数据
	 * @param ccrAppInfo
	 * @return String
	 * @throws AgentException
	 */
		public String addCcrAppInfo(CcrAppInfo ccrAddInf) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
			int count = this.insertCMISDomain(ccrAddInf, CcrPubConstant.CCR_APPINFO);	// 1成功  其他失败
	
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
	  	  	
			return flagInfo;
		}
		/**
		 * 新增一个ccrAppFinGuar数据
		 * @param ccrAppFinGuar
		 * @return String
		 * @throws AgentException
		 */
		public String addCcrAppFinGuar(CcrAppFinGuar ccrAppFinGuar) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
			int count = this.insertCMISDomain(ccrAppFinGuar, CcrPubConstant.CCR_APPFINGUAR);	// 1成功  其他失败
	
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
	  	  	
			return flagInfo;
		}
		/**
		 * 修改一条信用评级申请主表信息
		 * @param ccrAppInfo
		 * @return String 
		 * @throws AgentException
		 */
		public String updateCcrAppInfo(CcrAppInfo ccrAppInfo) throws AgentException {
	       String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			
			//更新信息
			int count = this.modifyCMISDomain(ccrAppInfo, CcrPubConstant.CCR_APPINFO);// 1成功  其他失败
				
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
	  	  	
			return flagInfo;
		}
		/**
		 * 查询信用评级申请主表信息
		 * @param serno
		 * @return ccrAppInfo
		 * @throws AgentException
		 */
		public CcrAppInfo queryCcrAppInfo(String serno) throws AgentException {
	        //创建评级申请信息容器
			CcrAppInfo ccrAppInfo = new CcrAppInfo();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			
			//得到查询结果
			ccrAppInfo = (CcrAppInfo)this.findCMISDomainByKeywords(ccrAppInfo,
							CcrPubConstant.CCR_APPINFO, pk_values);
			
			return ccrAppInfo;
		}
		/**
		 * 查询信用评级申请模型得分信息
		 * @param serno
		 * @return CcrModelScore
		 * @throws Exception 
		 */
		public CcrModelScore queryCcrModelScore(String serno,String modelNo) throws Exception {
			//创建评级申请信息容器
			CcrModelScore ccrModelScore = new CcrModelScore();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			pk_values.put("model_no", modelNo);	
			
			//得到查询结果
			TableModelDAO dao = this.getTableModelDAO();
			String conditionStr= "where ( serno = '"+serno+"' and model_no ='"+modelNo+"')";
			PageInfo pageInfo = new PageInfo();
			pageInfo.beginIdx=0;
			pageInfo.endIdx=10;
			pageInfo.pageIdx=1;
			pageInfo.pageSize=10;
			pageInfo.recordSize=10;
			
			Connection connection = this.getConnection();
			try {
				
				IndexedCollection iColl = dao.queryList(CcrPubConstant.CCR_MODELSCORE, null,conditionStr,pageInfo,connection);
				
				if (iColl.size()==0){
					ccrModelScore= new CcrModelScore();
				}else{
				KeyedCollection ccrModelScoreKcoll =(KeyedCollection)iColl.get(0);
				ComponentHelper cHelper =new ComponentHelper();
				cHelper.kcolTOdomain(ccrModelScore, ccrModelScoreKcoll);
				}
			} catch (Exception e) {
				throw e;
			}
			

			//ccrModelScore = (CcrModelScore)this.findCMISDomainByKeywords(ccrModelScore,
			//		CcrPubConstant.CCR_MODELSCORE, pk_values);
			
			return ccrModelScore;
			
		}
		/**
		 * 查询信用评级申请模型得分信息
		 * @param serno
		 * @return CcrModelScore
		 * @throws CMISException 
		 */
		public CcrModelScore queryCcrModelScoreHis(String serno,String modelNo) throws CMISException {
			//创建评级申请信息容器
			CcrModelScore ccrModelScore = new CcrModelScore();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			pk_values.put("model_no", modelNo);	
			
			//得到查询结果
			TableModelDAO dao = this.getTableModelDAO();
			String conditionStr= "where ( serno = '"+serno+"' and model_no ='"+modelNo+"')";
			PageInfo pageInfo = new PageInfo();
			pageInfo.beginIdx=0;
			pageInfo.endIdx=10;
			pageInfo.pageIdx=1;
			pageInfo.pageSize=10;
			pageInfo.recordSize=10;
			
			Connection connection = this.getConnection();
			try {
				
				IndexedCollection iColl = dao.queryList(CcrPubConstant.CCR_MODGRPSCRHIS, null,conditionStr,pageInfo,connection);				
				
				if (iColl.size()==0){
					ccrModelScore= new CcrModelScore();
				}else{
					KeyedCollection ccrModelScoreKcoll =(KeyedCollection)iColl.get(0);
					ComponentHelper cHelper =new ComponentHelper();
					cHelper.kcolTOdomain(ccrModelScore, ccrModelScoreKcoll);
				}
			} catch (EMPJDBCException e) {
				e.printStackTrace();
			}
			//ccrModelScore = (CcrModelScore)this.findCMISDomainByKeywords(ccrModelScore,
			//		CcrPubConstant.CCR_MODELSCORE, pk_values);
			
			return ccrModelScore;
		}
		
		/**
		 * 删除一条评级申请主表信息
		 * @param serno
		 * @return String
		 * @throws AgentException
		 */
		public String deleteCcrAppInfo(String serno) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			//创建MAP存储 业务代码
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			
			//根据主键删除评级申请主表信息
			int count = this.removeCMISDomainByKeywords(CcrPubConstant.CCR_APPINFO, pk_values);	// 1成功  其他失败
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
			
			return flagInfo;
		}
		
		/**
		 * 删除一条批量评级申请主表信息
		 * @param serno
		 * @return String
		 * @throws AgentException
		 */
		public String deleteCcrBatchAppInfo(String serno) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			//创建MAP存储 业务代码
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			
			//根据主键删除评级申请主表信息
			int count = this.removeCMISDomainByKeywords(CcrPubConstant.CCR_BATCHAPPINFO, pk_values);	// 1成功  其他失败
			
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
			
			return flagInfo;
		}
		/**
		 * 新增一个信用评级明细数据
		 * @param ccrAppInfo
		 * @return String
		 * @throws AgentException
		 */
		public String addCcrAppDetail(CcrAppDetail ccrAppDetail) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			//新增记录
			int count = this.insertCMISDomain(ccrAppDetail, CcrPubConstant.CCR_APPDETAIL);	// 1成功  其他失败
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
			
			return flagInfo;
		}

		/**
		 * 修改一条信用评级申请明细信息
		 * @param CcrAppDetail
		 * @return String 
		 * @throws AgentException
		 */
		public String updateCcrAppDetail(CcrAppDetail ccrAppDetail) throws AgentException {
	       String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			
			//更新信息
			int count = this.modifyCMISDomain(ccrAppDetail, CcrPubConstant.CCR_APPDETAIL);// 1成功  其他失败
				
			//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}
	  	  	
			return flagInfo;
		}
		/**
		 * 查询信用评级申请明细信息(单比)
		 * @param serno
		 * @return ccrAppInfo
		 * @throws AgentException
		 */
		public CcrAppDetail queryCcrAppDetail(String serno) throws AgentException {
	        //创建评级申请信息容器
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			
			//得到查询结果
			ccrAppDetail = (CcrAppDetail)this.findCMISDomainByKeywords(ccrAppDetail,
							CcrPubConstant.CCR_APPDETAIL, pk_values);
			
			return ccrAppDetail;
		}
		
		/**
		 * 查询信用评级申请明细信息(批量)
		 * @param serno
		 * @return ccrAppInfo
		 * @throws AgentException
		 */
		public CcrAppDetail queryCcrAppDetail(String serno,String cusId) throws AgentException {
			//创建评级申请信息容器
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			pk_values.put("cus_id", cusId);
			
			//得到查询结果
			ccrAppDetail = (CcrAppDetail)this.findCMISDomainByKeywords(ccrAppDetail,
					CcrPubConstant.CCR_APPDETAIL, pk_values);
			
			return ccrAppDetail;
		}
		
		/**
		 * 查询信用评级申请明细历史信息(批量)
		 * @param serno
		 * @return ccrAppInfo
		 * @throws AgentException
		 */
		public CcrAppDetail queryCcrAppDetailHis(String serno,String cusId) throws AgentException {
			//创建评级申请信息容器
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			
			//设置emp查询参数
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("serno", serno);	
			pk_values.put("cus_id", cusId);
			
			//得到查询结果
			ccrAppDetail = (CcrAppDetail)this.findCMISDomainByKeywords(ccrAppDetail,
					CcrPubConstant.CCR_APPDETAILHIS, pk_values);
			
			return ccrAppDetail;
		}
		
		/**
		 * 删除一条评级申请明细信息
		 * @param serno
		 * @return String
		 * @throws ComponentException 
		 */
		public String deleteCcrAppDetail(String serno,String cusId) throws ComponentException {
			Connection conn = this.getConnection();
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			CcrAppInfoDao dao= new CcrAppInfoDao();
			/**
			 * 借用CcrAppInfoDao中的deleteScoreBySerno这个方法.删掉Ccr_App_Detail表中的该客户数据.
			 */
			flagInfo =dao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_APPDETAIL, serno, cusId, conn);
			
			return flagInfo;
			
		}
		/**
		 * 删除评级申请明细信息
		 * @param serno
		 * @return String
		 * @throws ComponentException 
		 */
		public String deleteCcrAppDetail(String serno) throws ComponentException {
			Connection conn = this.getConnection();
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			CcrAppInfoDao dao= new CcrAppInfoDao();
			/**
			 * 借用CcrAppInfoDao中的deleteScoreBySerno这个方法.删掉Ccr_App_Detail表中的该客户数据.
			 */
			flagInfo =dao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_APPDETAIL, serno, conn);
			
			return flagInfo;
			
			
		}
		/**
		 * 初始化模型得分数据
		 * @param indList
		 * @return String
		 * @throws AgentException
		 */
		public String initModelScore(String serno,String cusId,String modelNo,String scoringManager) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			/**
			 *
			 * 从 groupList 可以获得所有要初始化的指标得分信息
			 *
			 *
			 * 
			 */
			CcrModelScore ccrModelScore= new CcrModelScore();
			ccrModelScore.setSerno(serno);
			ccrModelScore.setCusId(cusId);
			ccrModelScore.setModelNo(modelNo);
			ccrModelScore.setNatureScore("0");
			//ccrModelScore.setModelName(modelName);
			ccrModelScore.setScoringManager(scoringManager);
			
			int count = this.insertCMISDomain(ccrModelScore, CcrPubConstant.CCR_MODELSCORE);	// 1成功  其他失败
			
			//如果成功，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}

			return flagInfo;
		}
		
		/**
		 * 初始化分组得分数据
		 * @param serno
		 * @return String
		 * @throws AgentException
		 */
		public String initGroupScore(ArrayList groupList,String modelNo,String serno,String cusId, String scoringManager) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			/**
			 *
			 * 从 groupList 可以获得所有要初始化的指标分组得分信息
			 */
			Iterator groupListIter = groupList.iterator();
			int count = 0;
			//System.out.println("=====================================================================");
			while(groupListIter.hasNext()){
				//获取group 的 Domain对象。获取id，
				//再通过id换成
				IndGroupDomain indGroupDomain = (IndGroupDomain) groupListIter.next();
				
				CcrMGroupScore ccrMGroupScore= new CcrMGroupScore();
				ccrMGroupScore.setSerno(serno);
				ccrMGroupScore.setCusId(cusId);
				ccrMGroupScore.setModelNo(modelNo);
				//ccrMGroupScore.setModelName(modelName);
				ccrMGroupScore.setGroupNo(indGroupDomain.getGroupNo());
				ccrMGroupScore.setGroupName(indGroupDomain.getGroupName());
				
				ccrMGroupScore.setScoringManager(scoringManager);
				//System.out.println("GroupNo: "+indGroupDomain.getGroupNo()+"\nGroupName:"+indGroupDomain.getGroupName());
				
				
				int result = this.insertCMISDomain(ccrMGroupScore, CcrPubConstant.CCR_MGROUPSCORE);	// 1成功  其他失败
				
				//如果成功，给标志信息赋值
				if(1 == result){
					count =count+1;	//成功
				}
			}
			if(count==groupList.size()){
				flagInfo=CMISMessage.SUCCESS;	//成功
			}
			return flagInfo;
		}
		/**
		 * <h1>初始化指标得分数据</h1>
		 * <p>根据输入参数对表(ccr_g_ind_score)进行初始化，将在该表中插入indList.size()条数据</p>
		 * @param indList
		 * <pre>
		 * indList中包含
		 *  IndLibDomain型对象,其中
		 * 	 IndexNo		保存指标编号
		 *	 InputClasspath	保存指标得分
		 *   IndexLevel		保存指标原始值
		 * </pre>
		 * @param serno				业务编号
		 * @param cusId				客户码
		 * @param groupNo 			组别编码
		 * @param groupName			组别名称
		 * @param modelNo			模型编号
		 * @param modelName 		模型名称
		 * @param scoringManager	打分人编码
		 * @return String
		 * @throws AgentException
		 */
		public String initIndScore(ArrayList indList,String serno,String cusId,String groupNo,String groupName,String modelNo,String modelName,String scoringManager) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			int count=0;
			/**
			 * 从 indList 可以获得所有要初始化的指标得分信息
			 */
			Iterator indListIter = indList.iterator();
			while(indListIter.hasNext()){
				/**
				 * 获取index 的 Domain对象。获取id，
				 * 以及指标得分
				 */
				IndLibDomain indLibDomain = (IndLibDomain) indListIter.next();
				CcrGIndScore ccrGIndScore = new CcrGIndScore();
				ccrGIndScore.setSerno(serno);
				ccrGIndScore.setCusId(cusId);
				ccrGIndScore.setGroupNo(groupNo);
				ccrGIndScore.setIndexNo(indLibDomain.getIndexNo());
				ccrGIndScore.setScoringManager(scoringManager);
				ccrGIndScore.setIndexValue(indLibDomain.getInputClasspath());
				ccrGIndScore.setIndOrgVal(indLibDomain.getIndexLevel());
				/**
				 * 向表ccr_g_ind_score中插入指标得分
				 */
				int result = this.insertCMISDomain(ccrGIndScore, CcrPubConstant.CCR_GINDSCORE);	// 1成功  其他失败
				//如果成功，给标志信息赋值
				if(1 == result){
					count =count+1;	//成功
				}
			}
			if(count==indList.size()){
				flagInfo=CMISMessage.SUCCESS;	//成功
			}
			return flagInfo;
		}
		/**
		 * <h1>初始化指标得分数据（规则）</h1>
		 * <p>根据输入参数对表(ccr_g_ind_score)进行初始化，将在该表中插入indList.size()条数据</p>
		 * @param indList
		 * <pre>
		 * indList中包含
		 *  IndLibDomain型对象,其中
		 * 	 IndexNo		保存指标编号
		 *	 InputClasspath	保存指标得分
		 *   IndexLevel		保存指标原始值
		 * </pre>
		 * @param serno				业务编号
		 * @param cusId				客户码
		 * @param groupNo 			组别编码
		 * @param groupName			组别名称
		 * @param modelNo			模型编号
		 * @param modelName 		模型名称
		 * @param scoringManager	打分人编码
		 * @return String
		 * @throws AgentException
		 */
		public String initIndScoreWithShuffle(ArrayList<HashMap> indList,String serno,String cusId,String groupNo,String groupName,String modelNo,String scoringManager) throws AgentException {
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			int count=0;
			/**
			 * 从 indList 可以获得所有要初始化的指标得分信息
			 */
			Iterator<HashMap> indListIter = indList.iterator();
			while(indListIter.hasNext()){
				/**
				 * 获取index 的 Domain对象。获取id，
				 * 以及指标得分
				 */
				HashMap index=indListIter.next(); 
				CcrGIndScore ccrGIndScore = new CcrGIndScore();
				ccrGIndScore.setSerno(serno);
				ccrGIndScore.setCusId(cusId);
				ccrGIndScore.setGroupNo(groupNo);
				ccrGIndScore.setIndexNo((String) index.get("index_no"));
				ccrGIndScore.setScoringManager(scoringManager);
				ccrGIndScore.setIndexValue((String) index.get("index_value"));
				ccrGIndScore.setIndOrgVal((String) index.get("index_value"));
				/**
				 * 向表ccr_g_ind_score中插入指标得分
				 */
				int result = this.insertCMISDomain(ccrGIndScore, CcrPubConstant.CCR_GINDSCORE);	// 1成功  其他失败
				//如果成功，给标志信息赋值
				if(1 == result){
					count =count+1;	//成功
				}
			}
			if(count==indList.size()){
				flagInfo=CMISMessage.SUCCESS;	//成功
			}
			return flagInfo;
		}
		
		/**
		 * <h1>通过业务编号查询指标得分(批量,单比)</h1>
		 * <p>从指标得分表(ccr_g_ind_score)中查询出所有业务编号同输入参数serno相同的指标得分数据，组合成数组返回</p>
		 * @param serno 指标编号
		 * @return 指标得分Domain数组
		 * @throws AgentException
		 */
		
		public ArrayList queryGIndDomainList(String serno,String cusId,String modelNo) 
		throws AgentException{
			Connection conn = null;
			ArrayList domainList=null;
			try {
				conn = this.getConnection();
				String condition="where (1=1 and serno = '"+serno+"' and cus_id = '"+cusId+"')order by group_no,index_no";
				//得到表模型
				TableModelDAO tDao = this.getTableModelDAO();
				
				//查询数据
				IndexedCollection icol= tDao.queryList(CcrPubConstant.CCR_GINDSCORE, condition, conn);
				
				/**
				 * 把查询结果中的kCol数据结构转换成对象
				 */
				ComponentHelper componetHelper = new ComponentHelper();
				
				/**
				 * todo:
				 * 通过componetHelper将icoll转成domainList;
				 */
				if(icol.size()==0){
					domainList = new ArrayList();
				}else{
					domainList =(ArrayList) componetHelper.icol2domainlist(CcrPubConstant.CLASSPATH_CCR_GINDSCORE, icol);
				}
				
			} catch(Exception e){
				e.printStackTrace();
				throw new AgentException(CMISMessage.QUERYERROR,"查询失败"+e.toString());
			}finally{
				//释放资源
				if(conn!=null){
					this.releaseConnection(conn);
				}
			}
			
			return domainList;
		}

		/**
		 * <h1>通过业务编号查询指标历史得分</h1>
		 * <p>从指标得分表(ccr_g_ind_score)中查询出所有业务编号同输入参数serno相同的指标得分数据，组合成数组返回</p>
		 * @param serno 指标编号
		 * @return 指标得分Domain数组
		 * @throws AgentException
		 */
		
		public ArrayList queryGIndDomainListHis(String serno,String cusId,String modelNo) 
		throws AgentException{
			Connection conn = null;
			ArrayList domainList=null;
			try {
				conn = this.getConnection();
				String condition="where (1=1 and serno = '"+serno+"' and cus_id = '"+cusId+"') order by group_no,index_no";
				//得到表模型
				TableModelDAO tDao = this.getTableModelDAO();
				
				//查询数据
				IndexedCollection icol= tDao.queryList(CcrPubConstant.CCR_GRPINDSCRHIS, condition, conn);
				
				/**
				 * 把查询结果中的kCol数据结构转换成对象
				 */
				ComponentHelper componetHelper = new ComponentHelper();
				
				/**
				 * todo:
				 * 通过componetHelper将icoll转成domainList;
				 */
				if(icol.size()==0){
					domainList = new ArrayList();
				}else{
					domainList =(ArrayList) componetHelper.icol2domainlist(CcrPubConstant.CLASSPATH_CCR_GINDSCORE, icol);
				}
				
			} catch(Exception e){
				e.printStackTrace();
				throw new AgentException(CMISMessage.QUERYERROR,"查询失败"+e.toString());
			}finally{
				//释放资源
				if(conn!=null){
					this.releaseConnection(conn);
				}
			}
			
			return domainList;
		}
		
		/**
		 * <h1>获取组得分</h1>
		 * @param modelNo
		 * @param serno
		 * @return
		 * @throws AgentException
		 */
		public ArrayList queryMGroupDomainList(String modelNo,String serno,String cusId) 
		throws AgentException{
		Connection conn = null;
		ArrayList domainList=null;
		try {
			conn = this.getConnection();
			String condition="where (1=1 and model_no='"+modelNo+"' and serno = '"+serno+"' and cus_id = '"+cusId+"')";
			//得到表模型
			TableModelDAO tDao = this.getTableModelDAO();
			
			//查询数据
			IndexedCollection icol= tDao.queryList(CcrPubConstant.CCR_MGROUPSCORE, condition, conn);
			
			/*
			 * 把查询结果中的kCol数据结构转换成对象
			 */
			ComponentHelper componetHelper = new ComponentHelper();
			if (icol.size()==0){
				domainList = new ArrayList();
			}else{
				domainList =(ArrayList) componetHelper.icol2domainlist(CcrPubConstant.CLASSPATH_CCR_MGROUPSCORE, icol);
			}
		} catch(Exception e){
	  	  	e.printStackTrace();
			throw new AgentException(CMISMessage.QUERYERROR,"查询失败\n"+e.toString());
		  	}finally{
		  		//释放资源
		  		if(conn!=null){
		  			this.releaseConnection(conn);
		  		}
		  	}
		  	
		  	return domainList;
		}
		
		/**
		 * <h1>获取历史组得分</h1>
		 * @param modelNo
		 * @param serno
		 * @return
		 * @throws AgentException
		 */
		public ArrayList queryMGroupDomainListHis(String modelNo,String serno) 
		throws AgentException{
			Connection conn = null;
			ArrayList domainList=null;
			try {
				conn = this.getConnection();
				String condition="where (1=1 and model_no='"+modelNo+"' and serno = '"+serno+"')";
				//得到表模型
				TableModelDAO tDao = this.getTableModelDAO();
				
				//查询数据
				IndexedCollection icol= tDao.queryList(CcrPubConstant.CCR_MODGRPSCRHIS, condition, conn);
				
				/*
				 * 把查询结果中的kCol数据结构转换成对象
				 */
				ComponentHelper componetHelper = new ComponentHelper();
				
				/**
				 * todo:
				 * 通过componetHelper将icoll转成domainList;
				 */
				if (icol.size()==0){
					domainList = new ArrayList();
				}else{
					domainList =(ArrayList) componetHelper.icol2domainlist(CcrPubConstant.CLASSPATH_CCR_MGROUPSCORE, icol);
				}
			} catch(Exception e){
				e.printStackTrace();
				throw new AgentException(CMISMessage.QUERYERROR,"查询失败\n"+e.toString());
			}finally{
				//释放资源
				if(conn!=null){
					this.releaseConnection(conn);
				}
			}
			return domainList;
		}
			
		/**
		 * <h1>删除评级申请评分信息</h1>
		 * <p>删除以下表对应信息
		 * <ul>
		 * 	<li>模型得分表(CCR_MODEL_SCORE)</li>
		 * 	<li>模型组得分(CCR_M_GROUP_SCORE)</li>
		 * 	<li>组别指标得分表(CCR_G_IND_SCORE)</li>
		 * </ul>
		 * </p>
		 * @param serno 业务编号
		 * @param cusId 客户码
		 * @return String
		 * @throws ComponentException 
		 */
		public String deleteCcrAppScore(String serno,String cusId) throws ComponentException {
			Connection conn = this.getConnection();
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			//创建MAP存储 业务代码
			CcrAppInfoDao ccrAppInfoDao = new CcrAppInfoDao();
			String result = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_MODELSCORE, serno,cusId, conn);
			String result2 = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_MGROUPSCORE, serno,cusId, conn);
			String result3 = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_GINDSCORE, serno,cusId, conn);
			if(result.equals(CMISMessage.DEFEAT)||
					result2.equals(CMISMessage.DEFEAT)||
					result3.equals(CMISMessage.DEFEAT)){
				
				String errmsg ="删除表: ";
				if (result.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_MODELSCORE ";
				}
				if (result2.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_MGROUPSCORE ";
				}
				if (result3.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_GINDSCORE ";
				}
				errmsg=errmsg+"失败";
				throw new AgentException(errmsg);
			}
			
			return flagInfo;
		}
		
		/**
		 * <h1>删除评级申请评分信息</h1>
		 * <p>删除以下表对应信息
		 * <ul>
		 * 	<li>模型得分表(CCR_MODEL_SCORE)</li>
		 * 	<li>模型组得分(CCR_M_GROUP_SCORE)</li>
		 * 	<li>组别指标得分表(CCR_G_IND_SCORE)</li>
		 * </ul>
		 * </p>
		 * @param serno
		 * @return String
		 * @throws ComponentException 
		 */
		public String deleteCcrAppScore(String serno) throws ComponentException {
			Connection conn = this.getConnection();
			String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
			//创建MAP存储 业务代码
			CcrAppInfoDao ccrAppInfoDao = new CcrAppInfoDao();
			String result = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_MODELSCORE, serno, conn);
			String result2 = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_MGROUPSCORE, serno, conn);
			String result3 = ccrAppInfoDao.deleteScoreBySerno(CcrPubConstant.TABLENAME_CCR_GINDSCORE, serno, conn);
			if(result.equals(CMISMessage.DEFEAT)||
			   result2.equals(CMISMessage.DEFEAT)||
			   result3.equals(CMISMessage.DEFEAT)){
				
				String errmsg ="删除表: ";
				if (result.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_MODELSCORE ";
				}
				if (result2.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_MGROUPSCORE ";
				}
				if (result3.equals(CMISMessage.DEFEAT)){
					errmsg=errmsg+"CcrPubConstant.TABLENAME_CCR_GINDSCORE ";
				}
				errmsg=errmsg+"失败";
				throw new AgentException(errmsg);
			}
			
			return flagInfo;
		}
			
		public String updateCcrGIndScore(ArrayList ccrGIndScoreList) throws ComponentException{
			//得到connection
			Connection connection = this.getConnection();
			//得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			//得到ComponentHelper对象
			ComponentHelper componetHelper = new ComponentHelper();
			
			Iterator indIter = ccrGIndScoreList.iterator();
			while(indIter.hasNext()){
				CcrGIndScore ccrGIndScore = (CcrGIndScore)indIter.next();
				KeyedCollection ccrGIndScoreKcol;
				try {
					ccrGIndScoreKcol = componetHelper.domain2kcol(ccrGIndScore, CcrPubConstant.CCR_GINDSCORE);
					tDao.update(ccrGIndScoreKcol, connection);
				} catch (CMISException e) {
					e.printStackTrace();
					throw new ComponentException(e);	
				} catch (EMPJDBCException e) {
					e.printStackTrace();
					throw new ComponentException(e);	
				}
			}
			return CMISMessage.SUCCESS;//信息编码
		}
		
		public String updateCcrModelScore(CcrModelScore ccrModelScore) throws ComponentException{
			//得到connection
			Connection connection = this.getConnection();
			//得到表模型
			TableModelDAO tDao = this.getTableModelDAO();
			KeyedCollection ccrModelScoreKcol = null;

			//得到ComponentHelper对象
			ComponentHelper componetHelper = new ComponentHelper();
			try {
				ccrModelScoreKcol = componetHelper.domain2kcol(ccrModelScore, CcrPubConstant.CCR_MODELSCORE);							
				tDao.update(ccrModelScoreKcol, connection);
			} catch (CMISException e) {
				e.printStackTrace();
				throw new ComponentException(e);
			} catch (EMPJDBCException e) {
				e.printStackTrace();
				throw new ComponentException(e);					
			}
			return CMISMessage.SUCCESS;//信息编码
		}
		
		public String updateCcrMGroupScore(ArrayList ccrMGroupScoreList) throws ComponentException{
			//得到connection
			Connection connection = this.getConnection();
			//得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			//得到ComponentHelper对象
			ComponentHelper componetHelper = new ComponentHelper();
			
			Iterator groupIter = ccrMGroupScoreList.iterator();
			while(groupIter.hasNext()){
				CcrMGroupScore ccrMGroupScore = (CcrMGroupScore)groupIter.next();
				KeyedCollection ccrMGroupScoreKcol;
				try {
					ccrMGroupScoreKcol = componetHelper.domain2kcol(ccrMGroupScore, CcrPubConstant.CCR_MGROUPSCORE);
					tDao.update(ccrMGroupScoreKcol, connection);
				} catch (CMISException e) {
					e.printStackTrace();
					throw new ComponentException(e);	
				} catch (EMPJDBCException e) {
					e.printStackTrace();
					throw new ComponentException(e);	
				}
			}
			return CMISMessage.SUCCESS;//信息编码
		}
		
		/**
		 * 修改申请状态(单笔)
		 * @param serno
		 * @param status
		 * @return
		 * @throws ComponentException
		 */
		public String changeAppStatue(String serno,String status,String appDate) throws ComponentException{
			Connection conn = this.getConnection();
			CcrAppInfoDao dao = new CcrAppInfoDao();
			dao.changeAppStatue(serno, status,appDate, conn);
			return CMISMessage.SUCCESS;//信息编码
			
		}
		
		/**
		 * 修改申请状态(批量)
		 * @param serno
		 * @param status
		 * @return
		 * @throws ComponentException
		 */
		public String changeBatchAppStatue(String serno,String status,String CrdDt) throws ComponentException{
			Connection conn = this.getConnection();
			CcrAppInfoDao dao = new CcrAppInfoDao();
			dao.changeBatchAppStatue(serno, status,CrdDt, conn);
			return CMISMessage.SUCCESS;//信息编码
		}
		
		/**
		 * <h1>将申请数据，过渡到历史表(单笔)</h1>
		 * <p>过渡
		 * ccr_app_info
		 * ccr_app_detail
		 * ccr_g_ind_score
		 * ccr_m_group_score
		 * ccr_model_score
		 * 表中的数据转入历史表
		 * </p>
		 * @param serno
		 * @return
		 * @throws ComponentException
		 */
		public String app2His(String serno) throws ComponentException{
			Connection conn = this.getConnection();
			CcrAppInfoDao dao = new CcrAppInfoDao();
			dao.app2His(serno, conn);
			return CMISMessage.SUCCESS;//信息编码
		}
		
		/**
		 * <h1>将申请数据，过渡到历史表(批量)</h1>
		 * <p>过渡
		 * ccr_batch_app_info
		 * ccr_app_detail
		 * ccr_g_ind_score
		 * ccr_m_group_score
		 * ccr_model_score
		 * 表中的数据转入历史表
		 * </p>
		 * @param serno
		 * @return
		 * @throws ComponentException
		 */
		public String app2HisBatch(String serno) throws ComponentException{
			Connection conn = this.getConnection();
			CcrAppInfoDao dao = new CcrAppInfoDao();
			dao.app2HisBatch(serno, conn);
			return CMISMessage.SUCCESS;//信息编码
		}
		/**
		 * <h1>将申请数据删除(单笔)</h1>
		 * <p>删除
		 * ccr_app_info
		 * ccr_app_detail
		 * ccr_g_ind_score
		 * ccr_m_group_score
		 * ccr_model_score
		 * </p>
		 * @param serno
		 * @return
		 * @throws ComponentException
		 */
		public String delAllApp(String serno) throws ComponentException{
			this.deleteCcrAppInfo(serno);
			this.deleteCcrAppDetail(serno);
			this.deleteCcrAppScore(serno);
			return CMISMessage.SUCCESS;//信息编码
		}
		/**
		 * <h1>将申请数据删除(批量)</h1>
		 * <p>删除
		 * ccr_batch_app_info
		 * ccr_app_detail
		 * ccr_g_ind_score
		 * ccr_m_group_score
		 * ccr_model_score
		 * </p>
		 * @param serno
		 * @return
		 * @throws ComponentException
		 */
		public String delAllAppBatch(String serno) throws ComponentException{
			this.deleteCcrBatchAppInfo(serno);
			this.deleteCcrAppDetail(serno);
			this.deleteCcrAppScore(serno);
			return CMISMessage.SUCCESS;//信息编码
		}
		/**
		 * <h1>将批量申请数据删除</h1>
		 * <p>删除
		 * ccr_batch_app_info
		 * ccr_app_detail
		 * ccr_g_ind_score
		 * ccr_m_group_score
		 * ccr_model_score
		 * </p>
		 * @param serno
		 * @return
		 * @throws ComponentException
		 */
		public String delBatchAllApp(String serno) throws ComponentException{
			this.deleteCcrAppDetail(serno);
			this.deleteCcrAppScore(serno);
			return CMISMessage.SUCCESS;//信息编码
		}
		
		/**
		 * <h1>通过业务号查询评级明细信息</h1>
		 * @param serno
		 * @return ArrayList 其中包含 CcrAppDetail类型对象
		 * @throws ComponentException
		 */
		public ArrayList getAppDetailList(String serno) throws ComponentException{
			//最终的返回值
			ArrayList appDetailList=null;
			//得到connection
			Connection connection = this.getConnection();
			//得到表模型
			TableModelDAO tDao = this.getTableModelDAO();

			//得到ComponentHelper对象
			ComponentHelper componetHelper = new ComponentHelper();
			
			String condition = "where serno = '"+serno+"'";
			CcrAppDetail domain = new CcrAppDetail();
			IndexedCollection iCol = null;
			try {
				iCol = tDao.queryList(CcrPubConstant.CCR_APPDETAIL, condition, connection);
			} catch (EMPJDBCException e) {
				throw new ComponentException("通过业务编号"+serno+"查询评级明细失败："+e.toString());
			}
			try {
				appDetailList = (ArrayList) componetHelper.icol2domainlist(CcrPubConstant.CLASSPATH_CCR_APPDETAIL, iCol);
			} catch (CMISException e) {
				throw new ComponentException(e);
			}
			return appDetailList;
		}

		/**
		* 交易是否该客户存在未通过的业务申请
		* @param cusId
		* @throws ComponentException
		*/
		public int checkCusApp(String cusId) throws AgentException{
			String sql = " select count(*) ct from CCR_RAT_DIRECT where APPROVE_STATUS not in('997','998') and cus_id='"+cusId+"'";
			HashMap<String,String> hm = this.queryDataOfMapByCondition(sql);
			int iCount = Integer.parseInt(hm.get("ct"));
			return iCount;
		}
}
		

