package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection; 
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
 
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.ExcelImportUtil; 
/**
 * 风险分类调整-Excel导入方法
 * @author speckle
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RscTaskInfoUtil extends CMISOperation{
	 /**
		 * 
		 * <p>
		 * <h2>简述</h2>
		 * 		<ol>Excel导入,调整分类认定结果,只用主键serno去更新该字段</ol>
		 * <h2>功能描述</h2>
		 * 		<ol>请添加功能详细的描述</ol>
		 * </p>
		 * @param list
		 * @param connection
		 * @return
		 */
		public String importRscTaskInfoExcelDataValue(ExcelVO evo,Context context,Connection connection) throws Exception{
			List<String> errorList = new ArrayList<String>();
	    	String message = null;
	    	/**读取Excel文件 begin **/
	    	SheetVO[] sheets = evo.sheets;
			SheetVO sheet = null;
			sheet = sheets[0];
			int row = sheet.rownum;
			CellVO[][] cells;
			/** 读取文件 end **/
			cells = sheet.cells;		
			
			/**主键**/
			String serno = "";
			/**认定结果**/
			String class_rst = "";
			
			/**  需要更新数据存放的集合       **/
			IndexedCollection lists = new IndexedCollection();
	    	try {
	    	    		
	    		for (int i = 1; i < row; i++) {
	    			KeyedCollection domain = new KeyedCollection();
	    			serno  = ((String) cells[i][0].cellvalue).trim();
	    			class_rst  = ((String) cells[i][10].cellvalue).trim();
	    			String cus_id =  ((String) cells[i][2].cellvalue).trim();
	    			String remark =  ((String) cells[i][11].cellvalue).trim();
	    			domain.put("remark",remark);
	    			domain.put("serno",serno);
	    			domain.put("cusId",cus_id);
	    			domain.put("class_rst",class_rst);
	    			lists.add(domain);
	    		}
				int rowNumber = 2;
				int count = 0;
				/**岗位默客户主管 **/
				String duty="S0404";
				String risk_cls_status = "02";
				/**分类认定结果字典项cde**/
				String com_cde = "";
				Map map = new HashMap();
				/**获取当前岗位**/
				String  dutyNo = "";
				if(context.containsKey("content")){
					dutyNo=(String) context.getDataValue("content");
				}
				String[] dutyNoArray = dutyNo.split(",");
				String s = dutyNoArray[0];
				if(!"mgr".equals(duty)){
					if(!"".equals(dutyNo) && dutyNo!=null){
						if(!"".equals(s)&& s !=null){
							if("S0404".equals(s)){//二级支行售后复合岗
								duty="S0404";
								risk_cls_status = "02";
							}else if("D0102".equals(s)){//总行分类认定岗
								duty="D0102";
								risk_cls_status = "03";
							}else if("D0103".equals(s)){//总行分类审核岗
								duty="D0103";
								risk_cls_status = "04";
							}else if("Q0000".equals(s)){//已完成
								duty="Q0000";
								risk_cls_status = "05";
							}
						}
					}
				}
				TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
				connection.setAutoCommit(false);
				Map paramMap = new HashMap();
				if(!duty.equals("D0102")&&!duty.equals("D0103")&&!duty.equals("Q0000")){//总行都可以看到
					paramMap.put("usr_cde",(String) context.getDataValue("currentUserId"));
					paramMap.put("bch_cde",(String) context.getDataValue("organNo")); 
					paramMap.put("yesduty", "1");//控制去除异常风险分类列表数据有记录级
				}else{
					paramMap.put("noduty", "1");//控制去除异常风险分类列表数据无记录级
				}
				paramMap.put("risk_cls_status",risk_cls_status );
				paramMap.put("identy_duty",duty );
			    IndexedCollection infoList = null;
				if("One".equals(dutyNoArray[1])){
					paramMap.put("doubt", "1");//有疑义
					paramMap.put("doubt_status", risk_cls_status);
					infoList = (IndexedCollection) SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap, connection);
				}else if("Two".equals(dutyNoArray[1])){
					paramMap.put("nodoubt", "1");//无疑义
					paramMap.put("doubt_status", risk_cls_status);
					infoList = (IndexedCollection) SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap, connection);
				}else if("Thr".equals(dutyNoArray[1])){
					if(!duty.equals("D0102")&&!duty.equals("D0103")){//总行都可以看到
						paramMap.put("doubt", "1");//有记录级
					}else{
						paramMap.put("nodoubt", "1");//无记录级
					}
					infoList = (IndexedCollection) SqlClient.queryList4IColl("queryRscTaskInfoLike", paramMap, connection);
				}
				/**步骤1 判断是否符`**/
				if(infoList.size()>0){
					String[] pkIds = new String[infoList.size()];
					for(int i = 0;i<infoList.size();i++){
						pkIds[i]=(String) ((KeyedCollection)infoList.get(i)).getDataValue("serno");
					}
					for(int l=0;lists.size()>l;l++){
						KeyedCollection rKColl = (KeyedCollection)lists.get(l);
						int checkNum = 0;
						for(int j = 0;j<pkIds.length;j++){
							if(pkIds[l].equals(rKColl.getDataValue("serno"))){
								checkNum += 1;
							}
						}
						if(checkNum==0){
							//message = "Excel表格第" + rowNumber + "行记录非本用户或非本岗位记录，无法调整分类认定结果!";
							errorList.add("Excel表格第" + rowNumber + "行记录非本用户或非本岗位本列表记录，无法调整分类认定结果!");
							rowNumber++;
							connection.rollback();
							count = 0;
							break;
						}
						
						
						/**需要对主键与认定结果两个字段进行处理
						 * 1.判断主键该条记录是否存在;**/						
						KeyedCollection rr = (KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoLike", rKColl, null, connection) ;
						/** 2.认定结果需要转换成code,是否ok?字典项STD_ZB_NINE_SORT**/
						KeyedCollection rrr = (KeyedCollection) SqlClient.queryFirst ("querySComCdeByComDesc", rKColl.getDataValue("class_rst"),null, connection);
						
						if(null == rr){
							/*errorList.add("Excel表格第" + rowNumber + "行记录无法找到，无法调整分类认定结果!");
							//message = "Excel表格第" + rowNumber + "行记录无法找到，无法调整分类认定结果!";
							connection.rollback();
							count = 0;
							break;*/
							continue;
						}else if(null == rrr){
							/*errorList.add("Excel表格第" + rowNumber + "行分类认定结果为【"+r.getClassRst()+"】,输入不合法,无法调整分类认定结果!");
							//message = "Excel表格第" + rowNumber + "行分类认定结果为【"+r.getClassRst()+"】,输入不合法,无法调整分类认定结果!";
							connection.rollback();
							count = 0;
							break;*/
							continue;
						}else{
							/**可以调整分类认定
							 * 处理2个步骤:1.更新主表RSC_TASK_INFO中的认定结果,2.子表RSC_TASK_INFO_SUB中插入一条认定结果(分类历史查询)**/
							/**步骤1**/
							//判断如果上期分类结果"关注-"，本期跑批为"不良"，则客户经理初分最高调整为"关注-"，否则不能高于跑批结果
							if("25".equals(rr.getDataValue("pre_class_rst"))&&new Integer((String)rr.getDataValue("t_model_class_rst"))>25){
								if(new Integer((String) rrr.getDataValue("com_cde"))<25){
									errorList.add("Excel表格第" + rowNumber + "行分类认定结果为【"+rKColl.getDataValue("class_rst")+"】,输入不合法,无法调整分类认定结果!");
									//message = "Excel表格第" + rowNumber + "行分类认定结果为【"+r.getClassRst()+"】,输入不合法,无法调整分类认定结果!";
									connection.rollback();
									count = 0;
									break;
								}
							}else{
								if(new Integer((String)rr.getDataValue("class_rst"))>new Integer((String) rrr.getDataValue("enname"))){
									errorList.add("Excel表格第" + rowNumber + "行分类认定结果为【"+rKColl.getDataValue("class_rst")+"】,输入不合法,无法调整分类认定结果!");
									//message = "Excel表格第" + rowNumber + "行分类认定结果为【"+r.getClassRst()+"】,输入不合法,无法调整分类认定结果!";
									connection.rollback();
									count = 0;
									break;
								}
							}
							
							
							paramMap.put("cus_id",  rKColl.getDataValue("cusId"));
							KeyedCollection subDomain = new KeyedCollection();
							subDomain.setName("RscTaskInfoSub");
							subDomain.put("serno",rKColl.getDataValue("serno"));
							String remark = (String) rKColl.getDataValue("remark");
							if(!NewStringUtils.isNotEmpty(remark)){
								if("S0404".equals(s)){//二级支行售后复合岗
									subDomain.put("identy_duty","S0403");
								}else if("D0102".equals(s)){//总行分类认定岗 
									subDomain.put("identy_duty","S0404");
								}else if("D0103".equals(s)){//总行分类审核岗
									subDomain.put("identy_duty","D0102");
								}else if("Q0000".equals(s)){//已完成岗
									subDomain.put("identy_duty","D0103");
								}
								if(SqlClient.queryList("queryRscTaskInfoSubLike", subDomain, connection)!=null){
									
								
								if("D0102".equals(s)){
									subDomain.put("identy_duty","S0403");
									remark =(String)((KeyedCollection)SqlClient.queryFirst("queryRscTaskInfoSubLike", subDomain,null, connection)) .getDataValue("remark");
								}else{ 
									remark = (String)((KeyedCollection)SqlClient.queryFirst("queryRscTaskInfoSubLike", subDomain,null, connection)).getDataValue("remark");
								}
								}
								
							}
							//遍历所有条进行更新
							IndexedCollection rscTaskInfoList  = SqlClient.queryList4IColl("queryRscTaskInfoSynchr", paramMap , connection);
							for(int k = 0;k<rscTaskInfoList.size();k++){
								KeyedCollection rscTask = (KeyedCollection)rscTaskInfoList.get(k);
								KeyedCollection rscTaskInfo = new KeyedCollection();
								rscTaskInfo.put("serno",rscTask.getDataValue("serno"));
								com_cde = (String) rrr.getDataValue("enname");
								rscTaskInfo.put("upd_id",(String) context.getDataValue("currentUserId"));
								rscTaskInfo.put("upd_br_id",(String) context.getDataValue("organNo"));
								rscTaskInfo.put("upd_date",(String) context.getDataValue("OPENDAY"));
								 
								rscTaskInfo.put("class_rst",com_cde);
								rscTaskInfo.setName("RscTaskInfo");
								int j = dao.update(rscTaskInfo, connection);
								if(j == 1){
									/**步骤2**/					
									/**获取当前登入用户的岗位**/
									//String  dutyNo=(String) context.get(CMISConstance.ATTR_DUTYNO_LIST);
									
									/**子表新增一条记录-判断该岗位是否已经认定过,若有更新,若无重新新增一条记录**/
									map.put("serno",rscTask.getDataValue("serno"));
									map.put("identy_duty", duty);
									KeyedCollection subResult=(KeyedCollection) SqlClient.queryFirst("queryRscTaskInfoSubLike", map,null, connection);
									
									if(subResult!=null){//已有数据更新
										subResult.put("class_adjust_rst",com_cde);
										//subResult.setClassDate((String) context.getDataValue(CMISConstance.OPENDAY));
										subResult.put("class_date",TimeUtil.getDateTime(context));
										subResult.put("remark",remark);
										subResult.setName("RscTaskInfoSub");
										dao.update(subResult, connection);
									}else{
										/**从表新增数据**/
										subDomain.put("pk_id",CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context));
										//subDomain.setClassDate((String) context.getDataValue(CMISConstance.OPENDAY));
										subDomain.put("class_date",TimeUtil.getDateTime(context));
										subDomain.put("serno",rscTaskInfo.getDataValue("serno"));
										subDomain.put("class_adjust_rst",com_cde);
										subDomain.put("identy_duty",duty);
										subDomain.put("remark",remark);
										dao.insert(subDomain, connection);
									}
								}else{
									errorList.add("Excel表格第" + rowNumber + "行值输入异常,无法调整分类认定结果!");
									connection.rollback();
									count = 0;
									break;
								}
							}
						}
						rowNumber++;
						count++;
					}
					
				}else{
					errorList.add("请选择存在数据的岗位");
					connection.rollback();
				}
				
				
				
				connection.commit();
				connection.setAutoCommit(true);
				String fileName = "";
				/** 获取记录信息的文件名 **/
				fileName = ExcelImportUtil.getFileNamePath("CMIS", context);
				/** 文件记录无法导入的信息原因 **/
				ExcelImportUtil.fileUtil(errorList, fileName);

				/** 对此次导入的结果进行判断 **/
				message = ExcelImportUtil.checkImportResult(count, errorList);
			} catch (Exception e) {
				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
			return message;
		}

	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		return null;
	}
		

}
