package com.yucheng.cmis.pub.sequence;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.ecc.emp.component.xml.GeneralComponentParser;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.domain.CmisSequence;
import com.yucheng.cmis.pub.util.ComponentFactoryForSeq;
import com.yucheng.cmis.pub.util.ResourceUtils;

public class CMISSequenceService4BSBXD extends CMISSequenceService {

	private String factoryName = "seqFactory";
	
	private String tagName = "seq";
	
	private final static String m_fileType = ".xml"; // 文件类型
	
	private  HashMap<String, CmisSequence> seqtTable = null;
	
	/**
	 * 获取指定类型序列服务，兼容以前还是使用    类型 _拥有者  来确定
	 */
	public String getSequence(String aType, String owner, Context context, Connection connection) throws EMPException{
		if(aType == null || "".equals(aType))
			throw new EMPException("The aType should not be null for CMISSequenceService!");
		if(owner == null || "".equals(owner))
			throw new EMPException("The owner should not be null for CMISSequenceService!");
		
		long begin = System.currentTimeMillis();
		
		CmisSequence  curSeq = seqtTable.get(aType+"_"+owner);
		
		if(curSeq == null){
			throw new EMPException("序列服务id["+aType+"_"+owner+"]还未配置！请检查");
		}
		String result = null;
		try {
			result = curSeq.getFormatSeqStr(context, connection);
		} catch (Exception e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"序列服务id["+aType+"_"+owner+"]获取格式化字符串错误！",e);
			throw new EMPException(e);
		}
		long end = System.currentTimeMillis();
		EMPLog.log(PUBConstant.EMPSEQ,EMPLog.INFO, 0,"获取序列共耗时:"+(end-begin)+"毫秒");
		
		return  result;

	}
	
	/**
	 * 序列服务启动加载的实现方法，EMP自动加载
	 */
	public void initialize(){
		try{
			if(seqtTable != null){
				seqtTable.clear();
			}else{
				seqtTable = new HashMap<String, CmisSequence>();
			}
			
			ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("seq.config.file.dir");
			String config_film_dir = ResourceUtils.getFile(dir).getAbsolutePath();
			
			//String config_film_dir="E:/workspace_bj/cmis-main/src/main/config/com/yucheng/cmis/seq";
			
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "开始从目录"+config_film_dir+"加载序列服务");
			// 清空数据
			ArrayList<String> al = new ArrayList<String>();
			
			searchFiles(al,config_film_dir);
			
			if(al.size() == 0){
				EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0, "指定的目录["+config_film_dir+"]还未不存在序列服务不需要配置");
				return;
			}
			
			//初始化解析文件组件，只能单个文件解析
			ComponentFactoryForSeq compFactory = new ComponentFactoryForSeq();
			GeneralComponentParser parser = new GeneralComponentParser();
		     
			
			compFactory.setComponentParser(parser);
			
			for(String fileName:al){
				compFactory.initializeComponentFactory(factoryName, fileName);
				
				compFactory.parseBeansByTagName(seqtTable, tagName);
			}
           
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "序列服务加载结束！共加载序列服务"+seqtTable.size());
		 }catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "加载序列服务错误", e);
		}
		
	}
	
	  /**
     * 获取指定目录下的所有文件	
     * @param dir
     * @return
     * @throws Exception
     */
	private  void searchFiles(ArrayList<String> al,String dir) throws Exception {
		

		File root = new File(dir);
		// 得到该文件夹下的所有类型文件名称
		File[] filesOrDirs = root.listFiles();

		for (int i = 0; i < filesOrDirs.length; i++) {
			
			/*
			 * 判断该文件是否是文件夹，是则继续读取下一个
			 */
			   if(filesOrDirs[i].isDirectory()){
				   continue;
			   }
				// 得到文件名
				String fileName = filesOrDirs[i].getName();
				
				/*
				 * 匹配文件名，看看该文件是否是需要找的文件
				 */
				boolean IsValidfileEnd = m_fileType.equals(fileName.substring(
						fileName.length() - 4, fileName.length()));

				// 完成匹配的把该文件路径存起来
				if (IsValidfileEnd) {
					al.add(filesOrDirs[i].getAbsolutePath());
				}
		}
	}


	/**
	 * 为了兼容以前，这个方法现在已经废弃，不需要这个了
	 */
	@Override
	protected String querySequenceFromDB(String aType, String owner,
			Connection connection) throws EMPException {
		// TODO Auto-generated method stub
		return null;
	}
}
