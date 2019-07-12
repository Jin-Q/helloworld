package com.yucheng.cmis.biz01line.ind.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class IndDao {
	private Connection conn=null;
	
	private static final Logger logger = Logger.getLogger(IndDao.class);
	private static HashMap INDEX_BUFFER = new HashMap();
	public IndDao(){
		
	}
	/**
	 * 清除掉指标缓存
	 */
	public static void clearBuffer(){
		IndDao.INDEX_BUFFER.clear();
	}
	public IndDao(Connection connection){
		this.conn=connection;
	}
	/**
	 * 设置数据库连接
	 * @param connection
	 */
	public void setConnection(Connection connection){
		this.conn=null;
		this.conn=connection;
	}
	/**
	 * 查询单条记录
	 * @param table 表名
	 * @param fields 字段数组
	 * @param conditionStr 查询条件:( and a='' and b='')
	 * @return
	 * @throws EMPException
	 */
	public HashMap queryRecord(String table,String []fields ,String conditionStr)throws DaoException{
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"IndDao>>>查询单条记录,表:"+table+",条件:"+conditionStr);
		HashMap record=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
		//先组装查询是字段
		  if (fields != null && fields.length > 0) {
				sql = "select ";
				for (int i = 0; i < fields.length; i++) {

					if (i == (fields.length - 1)) {
						sql = sql + fields[i];
					} else {
						sql = sql + fields[i] + ",";
					}
				}
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,"查询单条记录,参数无查询字段集合>>>>="+fields.toString());
				return null;
			}
		    //再组装查询表
			if (sql != null && !sql.trim().equals("") && table != null
					&& !table.trim().equals("")) {
				sql = sql + " from " + table + " where 1=1 ";
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,"查询单条记录,参数无查询表名>>>>="+table);
				return null;
			}
			//再组装查询条件
			if (conditionStr != null && !conditionStr.trim().equals("")) {
				sql = sql + conditionStr;
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,"查询单条记录,参数无查询条件>>>>="+conditionStr);
				return null;
			}
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询单条记录sql>>>>="+sql);
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					record=new HashMap();
					for(int i=0;i<fields.length;i++){
						record.put(fields[i], rs.getString(fields[i]));
					}
					break; 
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryRecord 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryRecord 执行失败...."+e.getMessage(), e);
			}
		}
		return record;
	}
	/**
	 * 查询多条记录集合
	 * @param table
	 * @param fields
	 * @param conditionStr
	 * @return
	 * @throws EMPException
	 */
	public ArrayList queryList(String table,String []fields ,String conditionStr,String orderStr)throws DaoException{
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"IndDao>>>查询多条记录,表:"+table+",条件:"+conditionStr);
		ArrayList list=new ArrayList();
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
		if (fields != null && fields.length > 0) {
				sql = "select ";
				for (int i = 0; i < fields.length; i++) {

					if (i == (fields.length - 1)) {
						sql = sql + fields[i];
					} else {
						sql = sql + fields[i] + ",";
					}
				}
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,"查询多条记录,参数无查询字段集合>>>>="+fields.toString());
				return null;
			}
			if (sql != null && !sql.trim().equals("") && table != null
					&& !table.trim().equals("")) {
				sql = sql + " from " + table + " where 1=1 ";
			}else{
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.DEBUG, 0,"查询多条记录,参数无查询表名>>>>="+table);
				return null;
			}
			if (conditionStr != null && !conditionStr.trim().equals("")) {
				sql = sql + conditionStr;
			}
			if(orderStr!=null&&!orderStr.trim().equals("")){
				sql=sql+orderStr;
			}
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询多条记录sql>>>>="+sql);
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					HashMap hm=new HashMap();
					for(int i=0;i<fields.length;i++){
						hm.put(fields[i], rs.getString(fields[i]));
					}
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryList 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryList 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	/**
	 * 查询模型下的所有组信息
	 * @param modelNo
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap> queryModelGroups(String modelNo) throws DaoException{
		ArrayList<HashMap> list=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="select a.model_no,a.model_name,a.ceiling_limit,a.lower_limit,c.group_no,c.group_name,c.rating_rules," +
										"c.sup_group_no,c.group_kind,b.weight,b.seqno,c.trans_id" +
										" from IND_MODEL a," +
										"IND_MODEL_GROUP b,IND_GROUP c where a.model_no='"+
										modelNo+"' and a.MODEL_NO=b.MODEL_NO and b.group_no=c.group_no " +
										"order by b.seqno asc ";
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询模型下的所有组信息sql>>>>="+sql);
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				list=new ArrayList<HashMap>();
				while(rs.next()){
					HashMap<String, String> hm=new HashMap<String,String>();
					hm.put("model_no", rs.getString("model_no"));//模型编号
					hm.put("model_name", rs.getString("model_name"));//模型名称
					hm.put("ceiling_limit", rs.getString("ceiling_limit"));//上限额度
					hm.put("lower_limit", rs.getString("lower_limit"));//下限额度
					hm.put("group_no", rs.getString("group_no"));//组别编号
					hm.put("group_name", rs.getString("group_name"));//组别名称
					hm.put("rating_rules", rs.getString("rating_rules"));//组评分规则
					hm.put("sup_group_no", rs.getString("sup_group_no"));//上级组编号
					hm.put("group_kind", rs.getString("group_kind"));//组性质
					hm.put("trans_id", rs.getString("trans_id"));//规则交易ID
					hm.put("weight", rs.getString("weight"));//权重
					hm.put("seqno", rs.getString("seqno"));//顺序号
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryModelGroups 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryModelGroups 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	/**
	 * 查询财务报表指标编号(嘉兴 只要是定量指标都会被读取 )
	 * @param modelNo
	 * @return
	 * @throws DaoException
	 */
	public ArrayList queryFncIndexRpt(String modelNo) throws DaoException{
		ArrayList fncIndRptArray = new ArrayList();
		Statement stmt=null;
		ResultSet rs = null;
		String sql = "";
		try{
			stmt=this.conn.createStatement();
			sql="select FNC_INDEX_RPT,INDEX_NAME,INDEX_NO from ind_lib where INDEX_NO in " +
					"(select INDEX_NO from IND_GROUP_INDEX where GROUP_NO in " +
					"(select GROUP_NO from IND_MODEL_GROUP where MODEL_NO ='"+modelNo+
					"') and FNC_INDEX_RPT is not null)";
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					String INDEX_NO = rs.getString("INDEX_NO");
					String INDEX_NAME = rs.getString("INDEX_NAME");
					String FNC_INDEX_RPT = rs.getString("FNC_INDEX_RPT");
					String []fncIndexRpt ={INDEX_NO,INDEX_NAME,FNC_INDEX_RPT} ;
					fncIndRptArray.add(fncIndexRpt);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryFncIndexRpt 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryFncIndexRpt 执行失败...."+e.getMessage(), e);
			}
		}
		
		return fncIndRptArray;
	}
	
	/**
	 * 查询组下的所有指标信息
	 * @param groupNo
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap> queryGroupIndexes(String groupNo) throws DaoException{
		ArrayList<HashMap> list=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="select a.group_no,a.group_name,c.index_no,c.index_name,c.par_index_no," +
					"c.index_property,c.index_type,c.input_type,c.input_classpath," +
					"b.ind_std_score,b.weight,b.rule_classpath,b.dis_property,b.ind_dis_type,b.memo " +
					" from ind_group a,ind_group_index b,ind_lib c where a.group_no='"+
					groupNo+"' and a.group_no=b.group_no and b.index_no=c.index_no order by b.seq_no asc ";
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询组下的所有指标信息sql>>>>="+sql);
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				list=new ArrayList<HashMap>();
				while(rs.next()){
					HashMap hm=new HashMap<String,String>(); 
					hm.put("group_no", rs.getString("group_no"));
					hm.put("group_name", rs.getString("group_name"));
					String indexNo=rs.getString("index_no");
					hm.put("index_no", indexNo);
					hm.put("index_name", rs.getString("index_name"));
					hm.put("par_index_no", rs.getString("par_index_no"));
					hm.put("index_property", rs.getString("index_property"));
					hm.put("index_type", rs.getString("index_type"));
					hm.put("input_classpath", rs.getString("input_classpath"));
					hm.put("ind_std_score", rs.getString("ind_std_score"));
					hm.put("weight", rs.getString("weight"));
					hm.put("rule_classpath", rs.getString("rule_classpath"));
					hm.put("dis_property", rs.getString("dis_property"));
					hm.put("ind_dis_type", rs.getString("ind_dis_type"));
					hm.put("index_type", rs.getString("index_type")); 
					hm.put("memo", rs.getString("memo"));
					/**
					 * hm.put("subindexes", this.querySubIndexes(groupNo, indexNo));
					 */
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryGroupIndexes 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryGroupIndexes 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	
	
	/**
	 * 取该业务编号下所有定量指标值
	 * @param serno 客户号
	 * @return
	 */
	public ArrayList<HashMap> queryIndResValList(String serno) throws DaoException{
		ArrayList <HashMap> arr = new ArrayList<HashMap>();
		Statement stmt=null;
		ResultSet rs=null;
		String sql="select index_no,index_value from ind_result_val where serno='"+serno+"'";
		logger.info("ExecuteSql:"+sql);
		try{
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				HashMap<String,String> hm = new HashMap<String,String>();
				
				String indexNo = rs.getString("index_no");
				String indexValue = rs.getString("index_value");
				
				hm.put("index_no", indexNo);
				hm.put("index_value", indexValue);
				arr.add(hm);
			}
			
		}catch(SQLException ex){
			logger.error("方法  queryIndexOpts 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndexOpts 执行失败...."+e.getMessage(), e);
			}
		}
		return arr;
	}
	

	/**
	 * 查询指标下的所有选项信息
	 * @param groupNo
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap> queryIndexOpts(String indexNO) throws DaoException{
		ArrayList<HashMap> list=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="select index_no,ind_desc,index_value,value_score from ind_opt where index_no='"+indexNO+"' order by index_value asc ";
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询指标下的所有选项信息sql>>>>="+sql);
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				list=new ArrayList<HashMap>();
				while(rs.next()){
					HashMap<String, String> hm=new HashMap<String,String>(); 
					hm.put("index_no", rs.getString("index_no"));
					hm.put("ind_desc", rs.getString("ind_desc"));
					hm.put("index_value", rs.getString("index_value")); 
					hm.put("value_score", rs.getString("value_score"));
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndexOpts 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndexOpts 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	/**
	 * 查询指标下的所有参数信息
	 * @param groupNo
	 * @return
	 * @throws EMPException
	 */
	public ArrayList<HashMap<String,String>> queryIndexParas(String indexNO) throws DaoException{
		ArrayList<HashMap<String,String>> list=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="select index_no,para_enname,para_cnname,para_val_type from ind_para where index_no='"+indexNO+"' order by index_value asc ";
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询指标下的所有参数信息sql>>>>="+sql);
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				list=new ArrayList<HashMap<String,String>>();
				while(rs.next()){
					HashMap<String, String> hm=new HashMap<String,String>(); 
					hm.put("index_no", rs.getString("index_no"));
					hm.put("para_enname", rs.getString("para_enname"));
					hm.put("para_cnname", rs.getString("para_cnname")); 
					hm.put("para_val_type", rs.getString("para_val_type")); 
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndexParas 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndexParas 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	
	/**
	 * 复制模型
	 * @param key_model_no  新生成的主键
	 * @param model_no 需要复制的模型编号
	 * @return
	 * @throws EMPException
	 */	
	
	public int modelCopy(String key_model_no,String model_no) throws DaoException
	{
	  int count = 0;	
	  String v_sql_1 = "";
	  String v_sql_2 = "";
	  Statement stmt=null;
	  try{
			stmt=this.conn.createStatement();
			
			v_sql_1 = "insert into ind_model select '" + key_model_no + "',model_name, rating_rules,tsk_gralaty,tsk_tim_gray ,"
								+"stk_tsk_exe_dt,inc_tsk_exe_dt,com_cus_kind ,cus_type  ,com_opt_scale ,r_m_number,ceiling_limit ,"
								+"lower_limit, com_biz_kind ,use_branchs ,use_flag ,remark from ind_model where model_no ='"+model_no+"'";
			
			v_sql_2 = "insert into ind_model_group select '"+ key_model_no + "',group_no,weight,seqno from ind_model_group where model_no='"+model_no+"'";
			
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询指标下的所有参数信息sql>>>>="+v_sql_1);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询指标下的所有参数信息sql>>>>="+v_sql_2);
			stmt.execute(v_sql_1);
			stmt.execute(v_sql_2);
			count = 1;
		}catch(SQLException ex){
			logger.error("方法  modelCopy 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  modelCopy 执行失败...."+e.getMessage(), e);
			}
		}
	  
	  return count;
	}
	/**
	 * 查询指标下的所有下级指标
	 * @param groupno
	 * @param indexno
	 * @return
	 * @throws DaoException
	 */
	public ArrayList<HashMap> querySubIndexes(String groupno,String indexno) throws DaoException{
		ArrayList<HashMap> list=null;
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="select a.group_no,a.group_name,c.index_no,c.index_name,c.par_index_no," +
					"c.index_property,c.index_type,c.input_type,c.input_classpath," +
					"b.ind_std_score,b.weight,b.rule_classpath,b.dis_property,b.ind_dis_type " +
					" from ind_group a,ind_group_index b,ind_lib c where a.group_no='"+
					groupno+"' and a.group_no=b.group_no and b.index_no='"+indexno+"' and b.index_no=c.index_no order by b.seq_no asc ";
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"查询指标下的所有下级指标sql>>>>="+sql);
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				list=new ArrayList<HashMap>();
				while(rs.next()){
					HashMap hm=new HashMap<String,String>(); 
					hm.put("group_no", rs.getString("group_no"));
					hm.put("group_name", rs.getString("group_name"));
					String indexNo=rs.getString("index_no");
					hm.put("index_no", indexNo);
					hm.put("index_name", rs.getString("index_name"));
					hm.put("par_index_no", rs.getString("par_index_no"));
					hm.put("index_property", rs.getString("index_property"));
					hm.put("index_type", rs.getString("index_type"));
					hm.put("input_classpath", rs.getString("input_classpath"));
					hm.put("ind_std_score", rs.getString("ind_std_score"));
					hm.put("weight", rs.getString("weight"));
					hm.put("rule_classpath", rs.getString("rule_classpath"));
					hm.put("dis_property", rs.getString("dis_property"));
					hm.put("ind_dis_type", rs.getString("ind_dis_type"));
					hm.put("index_type", rs.getString("index_type")); 
					list.add(hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  querySubIndexes 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  querySubIndexes 执行失败...."+e.getMessage(), e);
			}
		}
		return list;
	}
	
	
	/**
	 * 将指标得分数据插入指标得分结果表中
	 * @param hs
	 * @param conn
	 * @return
	 * @throws DaoException
	 */	
	public int insertIndResultVal(HashMap<String,String> hs) throws DaoException
	{
		
		int count =0; 
		Statement stmt=null;
		String sql="";
		try
		  {
			stmt=this.conn.createStatement();
			String indday="0";
			indday=hs.get("ind_day");
			if(indday==null||indday.trim().equals("")){
				indday="0";
			}
			sql = "insert into ind_result_val(serno, ind_year,ind_month,ind_day, index_no, index_name, index_value)"
				  + "values('"+hs.get("serno")+"',"+hs.get("ind_year")+","+hs.get("ind_month")
				  +","+indday+",'"+hs.get("index_no")+"','"+hs.get("index_name")+"','"+hs.get("index_value")+"')";
		 
			stmt.execute(sql);
			count = 1;
		  }catch(SQLException ex){
				logger.error("方法  insertIndResultVal 执行失败...."+ex.getMessage(), ex);
				throw new DaoException(ex.getMessage());
		  }
		  finally
		  {
			  try{
					if(stmt!=null){
						stmt.close();
					}
				}catch(SQLException e){
					logger.error("方法  querySubIndexes 执行失败...."+e.getMessage(), e);
				}
		  }
		  
		  return count;
		
	}
	
	/**
	 * 根据指标值，指标选项值获取指标名称
	 * @param indexNo  指标编号
	 * @param indexValue 指标选项值
	 * @return
	 * @throws DaoException
	 */
	public String queryIndDesc(String indexNo,String indexValue) throws DaoException{
		Statement stmt=null;
		ResultSet rs=null;
		String Reason="";
		
		/*将选项值按逗号分开，得到查询条件*/
		String sql="";
		String[] v_indexValue = indexValue.split(",");
		String condStr = "'";
		int sign = 0;
		for (int i = 0;i < v_indexValue.length; i++)
		{
			if(sign == 0)
			{
			  condStr = "'"+v_indexValue[i]+"'";
			  sign = 1;
			}
		   else 
		  {
			condStr += ",'"+v_indexValue[i]+"'";
		  }
		}
		sign = 0;
		
		/*获取查询结果*/
		try{
			stmt=this.conn.createStatement();
			sql = "select a.index_name,b.ind_desc from ind_lib a,ind_opt b where a.index_no = b.index_no";
			sql = sql+" and b.index_no = '"+indexNo+"' and b.index_value in ("+condStr+")";
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					if(sign == 0)
					{
						Reason = rs.getString("index_name")+" : "+rs.getString("ind_desc");
						sign = 1;
					}
					else 
					{
						Reason += " , "+rs.getString("ind_desc");
					}
				}
				Reason = Reason+".";
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndDesc 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndDesc 执行失败...."+e.getMessage(), e);
			}
		}
		
		return Reason;
	}
	
	/**
	 * 根据编号，日期删除指标结果表里面的记录
	 * @param serno  编号
	 * @param ind_date 日期
	 * @return
	 * @throws DaoException
	 */
	
	public int deleteIndResultVal(String serno,String ind_date) throws DaoException
	{
		int count =0; 
		Statement stmt=null;
		String sql="";
		try
		  {
			stmt=this.conn.createStatement();
			sql = "delete from ind_result_val where serno='"+serno+"' and ind_date='"+ind_date+"'";
			stmt.execute(sql);
			count = 1;
		  }catch(SQLException ex){
				logger.error("方法  deleteIndResultVal 执行失败...."+ex.getMessage(), ex);
				throw new DaoException(ex.getMessage());
		  }
		  finally
		  {
			  try{
					if(stmt!=null){
						stmt.close();
					}
				}catch(SQLException e){
					logger.error("方法  deleteIndResultVal 执行失败...."+e.getMessage(), e);
				}
		  }
		  
		  return count;
	}
	
	/**
	 * 根据组编号，指标编号查询组指标表
	 * @param group_no  组编号
	 * @param index_no 指标编号
	 * @return
	 * @throws DaoException
	 */
	public HashMap<String,String>queryIndGroupIndexDetail(String group_no,String index_no) throws DaoException
	{
		HashMap<String, String> hm = null;
		if(IndDao.INDEX_BUFFER.get("ind_gi_"+group_no+"_"+index_no)!=null){
			hm=(HashMap <String,String>)IndDao.INDEX_BUFFER.get("ind_gi_"+group_no+"_"+index_no);
			return hm;
		}
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		/*获取查询结果*/
		try{
			stmt=this.conn.createStatement();
			sql = "select group_no, index_no,ind_std_score, weight, rule_classpath, category, ";
            sql +=" sub_category, dis_property, ind_dis_type, seq_no, index_name,full_score,score_way";
			sql += ",REFERENCE_VALUE,LIMIT_VALUE,MEMO,limit_flag from ind_group_index where group_no='"+group_no+"' and index_no='"+index_no+"'";
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				hm=new HashMap<String,String>();
				if(rs.next()){
                   hm.put("group_no", rs.getString("group_no"));
                   hm.put("index_no", rs.getString("index_no"));
                   hm.put("ind_std_score", rs.getString("ind_std_score"));
                   hm.put("weight", rs.getString("weight"));
                   hm.put("rule_classpath", rs.getString("rule_classpath"));
                   hm.put("category", rs.getString("category"));
                   hm.put("sub_category", rs.getString("sub_category"));
                   hm.put("dis_property",rs.getString("dis_property"));
                   hm.put("ind_dis_type", rs.getString("ind_dis_type"));
                   hm.put("seq_no", rs.getString("seq_no"));
                   hm.put("index_name",rs.getString("index_name"));
                   hm.put("full_score", rs.getString("full_score"));
                   hm.put("score_way",rs.getString("score_way"));
                   hm.put("reference_value", rs.getString("REFERENCE_VALUE"));
                   hm.put("limit_value", rs.getString("LIMIT_VALUE"));
                   hm.put("memo", rs.getString("MEMO"));
                   hm.put("limit_flag", rs.getString("limit_flag"));
                   IndDao.INDEX_BUFFER.put("ind_gi_"+group_no+"_"+index_no, hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndGroupIndex 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndGroupIndex 执行失败...."+e.getMessage(), e);
			}
		}
		
		return hm;
	}
	
	/**
	 * 根据组编号，指标编号查询组指标表
	 * @param index_no  指标编号
	 * @param index_value 指标选项值
	 * @return
	 * @throws DaoException
	 */
	public HashMap<String,String>queryIndOptDetail(String index_no,String index_value) throws DaoException
	{
		HashMap <String,String> hm =null;
		if(IndDao.INDEX_BUFFER.get("ind_opt_"+index_no)!=null){
			hm=(HashMap <String,String>)IndDao.INDEX_BUFFER.get("ind_opt_"+index_no+"_val_"+index_value);
			return hm;
		}
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		/*获取查询结果*/
		try{
			stmt=this.conn.createStatement();
			sql = "select index_no, ind_desc, index_value, value_score from ind_opt";
			sql += " where index_no='"+index_no+"' and index_value='"+index_value+"'";
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				hm=new HashMap<String,String>();
				if(rs.next()){
                   hm.put("index_no", rs.getString("index_no"));
                   hm.put("ind_desc", rs.getString("ind_desc"));
                   hm.put("index_value", rs.getString("index_value"));
                   hm.put("value_score", rs.getString("value_score"));
                   IndDao.INDEX_BUFFER.put("ind_opt_"+index_no+"_val_"+index_value, hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndOptDetail 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndOptDetail 执行失败...."+e.getMessage(), e);
			}
		}
		return hm;
	}
	/**
	 * 查询指标详细信息
	 * @param indexno
	 * @return
	 * @throws DaoException
	 */
	public HashMap<String,String>queryIndLibDetail(String index_no) throws DaoException{
		HashMap <String,String> hm =null;
		if(IndDao.INDEX_BUFFER.get("ind_lib_"+index_no)!=null){
			hm=(HashMap <String,String>)IndDao.INDEX_BUFFER.get("ind_lib_"+index_no);
			return hm;
		}
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		/*获取查询结果*/
		try{
			stmt=this.conn.createStatement();
			sql = " select index_no,index_name,par_index_no,index_property," +
					"index_type,input_type,nvl(input_classpath,'') as input_classpath,exe_cycle,index_level"+
					" from ind_lib where index_no='"+index_no+"'"; 
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				hm=new HashMap<String,String>();
				if(rs.next()){
                   hm.put("index_no", rs.getString("index_no"));
                   hm.put("index_name", rs.getString("index_name"));
                   hm.put("par_index_no", rs.getString("par_index_no"));
                   hm.put("index_property", rs.getString("index_property"));
                   hm.put("index_type", rs.getString("index_type"));
                   hm.put("input_classpath", rs.getString("input_classpath"));
                   hm.put("input_type", rs.getString("input_type"));
                   hm.put("exe_cycle", rs.getString("exe_cycle"));
                   hm.put("index_level", rs.getString("index_level"));
                   IndDao.INDEX_BUFFER.put("ind_lib_"+index_no, hm);
				}
			}
		}catch(SQLException ex){
			logger.error("方法  queryIndLibDetail 执行失败...."+ex.getMessage(), ex);
			throw new DaoException(ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				logger.error("方法  queryIndLibDetail 执行失败...."+e.getMessage(), e);
			}
		}
		return hm;
	}
	/**
	 * 查询指标结果值
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws DaoException
	 */
	public String queryIndResVal(String serno,int year,int month,int day,String indexno) throws DaoException{
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		String retVal="";
		try{
			stmt=this.conn.createStatement(); 
			sql="select index_value from ind_result_val where serno='"+serno+
				"' and ind_year="+year+" and ind_month="+month+" and ind_day="+day+
				" and index_no='"+indexno+"'"; 
			rs=stmt.executeQuery(sql);
			if(rs!=null&&rs.next()){
				retVal=rs.getString("index_value"); 
			}
		}catch(SQLException se){
			logger.error("方法  queryIndResVal 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		}finally{
		    if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs = null;
			 }
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt = null;
			}
		}
		return retVal;
	}
	
	/**
	 * 查询指标结果值
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @return
	 * @throws DaoException
	 */
	public String queryIndResValByNo(String serno,String indexno) throws DaoException{
		Statement stmt=null;
		ResultSet rs=null;
		String sql="";
		String retVal="";
		try{
			stmt=this.conn.createStatement(); 
			sql="select index_value from ind_result_val where serno='"+serno+
				"' and index_no='"+indexno+"'"; 
			rs=stmt.executeQuery(sql);
			if(rs!=null&&rs.next()){
				retVal=rs.getString("index_value"); 
			}
		}catch(SQLException se){
			logger.error("方法  queryIndResVal 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		}finally{
			this.closeResource(stmt,rs);
		}
		return retVal;
	}
	
	/**
	 * 删除指标指标结果值表
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws DaoException
	 */
	public void delIndResVal(String serno,int year,int month,int day,String indexno) throws DaoException{
		Statement stmt=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="delete from ind_result_val where serno='"+serno+
				"' and ind_year="+year+" and ind_month="+month+" and ind_day="+
				day+ " and index_no='"+indexno+"'";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			logger.error("方法  delIndResVal 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		}finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt = null;
			}
		}
	}
	/**
	 * 删除指标指标结果值表
	 * @param serno
	 * @param year
	 * @param month
	 * @param day
	 * @param indexno
	 * @throws DaoException
	 */
	public void delIndResValByNo(String serno,String indexno) throws DaoException{
		Statement stmt=null;
		String sql="";
		try{
			stmt=this.conn.createStatement();
			sql="delete from ind_result_val where serno='"+serno+
				"' and index_no='"+indexno+"'";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			logger.error("方法  delIndResVal 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		}finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt = null;
			}
		}
	}
	/**
	 * 查询指标下的所有选项得分，按(选项值：选项得分)，放入HashMap中
	 * @param indexno
	 * @return
	 * @throws DaoException
	 */
	public HashMap<String,String> queryIndexOptScore(String indexno) throws DaoException{
		HashMap<String,String> hm=null;
		if(this.INDEX_BUFFER.get(indexno+"_opt")!=null){
			hm=(HashMap<String,String>)IndDao.INDEX_BUFFER.get(indexno+"_opt");
		}
		Statement st=null;
		ResultSet rs=null;
		String sql="";
		try {
			st=this.conn.createStatement();
			sql="select index_value,value_score from ind_opt where index_no='"+indexno+"' order by index_value asc ";
			rs=st.executeQuery(sql);
			if(rs!=null){ 
				hm=new HashMap<String,String>(); 
				while(rs.next()){  
					hm.put(rs.getString("index_value"), rs.getString("value_score")); 
				}
				IndDao.INDEX_BUFFER.put(indexno+"_opt", hm);
			}
		} catch (SQLException se) { 
			logger.error("方法  queryIndexOptions 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		}finally{
			 this.closeResource(st, rs);
		}
		
		return hm;
	}
	/**
	 * 取模型编号
	 * @param hm
	 * @return
	 * @throws DaoException
	 */
	public String getModelNoForCcrRsc(String condstr) throws DaoException{
		Statement st=null;
		ResultSet rs=null;
		String sql="";
		String retVal=null;
		try {
			st=this.conn.createStatement();
			sql="select model_no,model_name from ind_model where 1=2 or "+condstr;
			this.logger.info("getModelNoForCcr>>>>>>>>sql="+sql);
			rs=st.executeQuery(sql);
			if(rs!=null&&rs.next()){
				String modelno= rs.getString("model_no");
				String modelname=rs.getString("model_name");
				retVal=modelno.trim()+"#"+modelname.trim();
			}
		} catch (SQLException se) { 
			logger.error("方法  getModelNo 执行失败...."+se.getMessage(), se);
			throw new DaoException(se.getMessage());
		} finally{
			this.closeResource(st, rs);
		}
		return retVal;
	}
	
	private void closeResource(Statement st,ResultSet rs){
		try{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
		}catch(SQLException se){
			logger.error("方法  closeResource 执行失败...."+se.getMessage(), se);
		}
	}
	/**
	 * 根据模型编号获取模型得分
	 * @param modelNo
	 * @return
	 * @throws AgentException
	 */		
	public String getModelAllScore(String modelNo) throws DaoException{
		String retValue = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		String sql = "select sum(ind_std_score) score from ind_group_index i ,ind_model_group g where " +
				"i.group_no=g.group_no and g.model_No='"+modelNo+"'";
		this.logger.info(sql);
		try {
			psmt = this.conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			if(rs.next()){
				retValue = rs.getString("score");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		    if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs = null;
			 }
			if (psmt != null) {
				try {
					psmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				psmt = null;
			}
		}
		
		return retValue ;
	}
}
