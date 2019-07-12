package com.yucheng.cmis.biz01line.document.cybercusinfo.cybercusdocumentinfo;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPFile;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCyberCusDocumentInfoListOp extends CMISOperation {

private final String modelId = "CyberCusDocumentInfo";
	
/**
 * 查询文件列表
 * @param context
 * @return
 * @throws Exception
 *@description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
 *@version v1.0 yezm

 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String conditionStr = "";
			//文件名称模糊查询
			String index_file_name = "";
			String file_date = "";
			String queryflag = "N";
			if(context.containsKey("file_name") && !"".equals(context.getDataValue("file_name"))){
				conditionStr = " WHERE FILE_NAME LIKE '%"+context.getDataValue("file_name")+"%'";
				index_file_name = (String) context.getDataValue("file_name");
				queryflag = "Y";
			}
			if(context.containsKey("file_date") && !"".equals(context.getDataValue("file_date"))){
				file_date = (String) context.getDataValue("file_date");
				file_date = file_date.replace("-", "");
			}
			
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			//int size = 15;
			//PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			//TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			//IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			
			IndexedCollection iColl = new IndexedCollection("CyberCusDocumentInfo");//从缓存中读出来的总记录集合,特殊场合适用
			IndexedCollection iCollPage = new IndexedCollection();//条件查询时，符合条件的记录集合
			IndexedCollection iCollForPage = new IndexedCollection();//分页时，展示在页面上的记录集合
			int size = 10;
			int RecordSize = 0;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			pageInfo.setRecordSize(String.valueOf(iColl.size()));
			
			//String ldir = "D:/Project/workspace/DOC/2015_07/";  
			//File fdir = new File(ldir);
			//File[] files = fdir.listFiles();
			
			ResourceBundle res = ResourceBundle.getBundle("ftp");
			String ldir = res.getString("ftp.ebanktempfilepath");
			if(file_date!=null&&!"".equals(file_date)){
				ldir = ldir+file_date+"/";
			}
			FTPUtil util = new FTPUtil();
			FTPFile[] files = util.showFiles(ldir);
			if(files!=null){
				this.getShowFiles(files, queryflag, index_file_name, iColl, ldir);
			}
			iColl = this.sort(iColl);
			for(int i=0;i<iColl.size();i++){	
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				iCollPage.addDataElement(kColl);
			}
			
			//页面展示集合
			for(int i=(pageInfo.pageIdx*pageInfo.pageSize-pageInfo.pageSize);i<pageInfo.pageSize*pageInfo.pageIdx;i++){
				if(iCollPage.size()>i){
					   KeyedCollection kColl = (KeyedCollection)iCollPage.get(i);
					   iCollForPage.addDataElement(kColl);
				}else{
					break;
				}
			}
			
			//输入查询条件时，总记录数相应改变
			pageInfo.setRecordSize(String.valueOf(iCollPage.size()));
			
			iCollForPage.setName("CyberCusDocumentInfoList");
			this.putDataElement2Context(iCollForPage, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	public void getShowFiles(FTPFile[] files, String queryflag, String index_file_name, IndexedCollection iColl, String ldir) throws EMPException{
		try {
			String filename;
			for(FTPFile file : files){
				if(file.isFile()){
					KeyedCollection kColl = new KeyedCollection("CyberCusDocumentInfo");
					filename = file.getName();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String fileDate = dateFormat.format(file.getTimestamp().getTime());
					if("Y".equals(queryflag)){
						if(filename.indexOf(index_file_name)>=0){
							kColl.put("file_name", filename);
							kColl.put("file_path", ldir+"@"+filename);
							kColl.put("file_path2", ldir);
							kColl.put("file_date", fileDate);
							iColl.addDataElement(kColl);
						}
					}else{
						kColl.put("file_name", filename);
						kColl.put("file_path", ldir+"@"+filename);
						kColl.put("file_path2", ldir);
						kColl.put("file_date", fileDate);
						iColl.addDataElement(kColl);
					}
				}
				else{
					String directoryName = file.getName();
					String child_ldir = ldir + directoryName + "/";
					FTPUtil util = new FTPUtil();
					FTPFile[] child_files = util.showFiles(child_ldir);
					this.getShowFiles(child_files, queryflag, index_file_name, iColl, child_ldir);
				}
			}
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
	/**
	 * 将IndexedCollection 根据END_DATE进行END_DATE的排序 
	 * @param iColl 需要排序的IndexedCollection
	 * @return 排序好IndexedCollection
	 */
	public static IndexedCollection sort(IndexedCollection iColl){
		 Collections.sort(iColl, new Comparator<KeyedCollection>() {   
	            public int compare(KeyedCollection a, KeyedCollection b) {
	            	int return_int=0;
	              try {
	            	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String one_end_date = a.getDataValue("file_date").toString();
					String two_end_date = b.getDataValue("file_date").toString();
					
					Date date_one = dateFormat.parse(one_end_date);  //将到期日期转换为日期类型
					Date date_tow = dateFormat.parse(two_end_date);
					long compute_value =date_one.getTime() - date_tow.getTime();  //得到两个到期日期想减值
					if(compute_value > 0){   //如果想减值大于0说明第一个大
						return_int = -1;   //返回时按降序排列
					}else if(compute_value < 0){
						return_int = 1;
					}else{
						return_int = 0;
					}
				} catch (ObjectNotFoundException e) {
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	              return return_int ;
	            }
	         }); 
		 return iColl;
	}
}
