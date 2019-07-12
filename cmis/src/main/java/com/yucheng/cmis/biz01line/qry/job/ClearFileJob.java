package com.yucheng.cmis.biz01line.qry.job;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ecc.emp.log.EMPLog;
/**
 * 调度清理综合查询缓存文件任务
 * @author MOHEN
 *
 */
public class ClearFileJob  implements Job{
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
			ResourceBundle res = ResourceBundle.getBundle("cmis");
	        String dir = res.getString("qry.result.path");
	        URL url = ClearFileJob.class.getResource("");
	        String path = url.getPath();
	        path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/job/", "");
	        path = path + "/" + dir + "/";
	        try{
	        	EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "开始调度清理综合查询缓存文件....");
	        	File resultDir=new File(path);
	        	int i=0,count=0;
	        	if(resultDir.isDirectory()){
	        		File[] files = resultDir.listFiles();
	        		count=files.length;
	        		for(File file:files){
	        			if(file.isFile()){
	        				if(file.delete())
	        					i++;
	        			}
	        		}
	        	}
	        	EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "调度清理综合查询缓存文件结束.共"+count+"文件,清理"+i+"文件!");
	        }catch (Exception e) {
	        	EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "调度清理综合查询缓存文件异常:"+e.getMessage());
			}
	        
	        
	}
}
