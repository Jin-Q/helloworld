package com.yucheng.cmis.biz01line.ind.pub;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.agent.IndAgent;
import com.yucheng.cmis.biz01line.ind.dao.IndDao;

public class VelocityUtil {
	
	private static final Logger logger = Logger.getLogger(VelocityUtil.class);
	
	/**
	 * 
	 * @param GroupList 要生成的组集合列表
	 * @param templatefile
	 * @param jspfile
	 */
	public void genCcrJspFile(ArrayList grouplist,String templatefile,String jspfile)throws EMPException{
		try{ 
			URL url=VelocityUtil.class.getResource("");
			String  path=url.getPath(); 
			path=path.replaceAll("/com/yucheng/cmis/biz01line/ind/pub/", IndPubConstant.VM_PATH); 
			logger.info("vm path>>"+path);
			
			Properties p = new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty("file.resource.loader.path", path);
			/**以下三个编码在AIX机器上需要设置为GBK */
			p.setProperty(Velocity.ENCODING_DEFAULT, "GBK");
	        p.setProperty(Velocity.INPUT_ENCODING, "GBK");
	        p.setProperty(Velocity.OUTPUT_ENCODING, "GBK"); 
	        p.setProperty("stringliterals.interpolate", "false"); 
	        Velocity.init(p);
			// 实例化一个Context
			//Velocity.init();  
			Template template = Velocity.getTemplate(templatefile);  
			VelocityContext context = new VelocityContext();
			context.put("GroupList",grouplist);
			StringWriter writer = new StringWriter();
			
			// 开始模版的替换
			template.merge(context, writer);
			// 写到文件中
			PrintWriter filewriter = new PrintWriter(new FileOutputStream(
					IndPubConstant.getVM_JSP_PATH()+jspfile), true);
			
			logger.info("template file_path:>>"+IndPubConstant.getVM_JSP_PATH()+jspfile);
			
			filewriter.println(writer.toString());
			filewriter.close();
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			throw new EMPException("生成评级模型页面错误，错误描述："+ex.getMessage());
		}
	}
	/**
	 * 根据指标集合生成风险分类jsp文件
	 * @param list 指标集合
	 * @param template velocity的模板文件名
	 * @param jspfilename 生成后的jsp文件名
	 */
	public void genRscJspFile(String groupid,String groupname,ArrayList list,String templatefile,String jspfilename){
		try{
			URL url=VelocityUtil.class.getResource("");
			String  path=url.getPath(); 
			path=path.replaceAll("/com/yucheng/cmis/biz01line/ind/pub/", IndPubConstant.VM_PATH); 
			logger.info("vm path>>"+path);
			Properties p = new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty("file.resource.loader.path", path);
			p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
	        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
	        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8"); 
	        p.setProperty("stringliterals.interpolate", "false"); 
	        Velocity.init(p); 
			Template template = Velocity.getTemplate(templatefile);  
			logger.info(template.toString());
			VelocityContext context = new VelocityContext();
			context.put("groupid", groupid);
			context.put("groupname", groupname);
			context.put("IndexesList", list); 
			StringWriter writer = new StringWriter();
			// 开始模版的替换
			template.merge(context, writer);
			/*logger.info("开始打印jsp信息...");
			logger.info(writer.toString());
			logger.info("打印jsp信息结束!");*/
			// 写到文件中
			PrintWriter filewriter = new PrintWriter(new FileOutputStream(
					IndPubConstant.getVM_JSP_PATH()+jspfilename), true);
			filewriter.println(writer.toString());
			filewriter.close();
		}catch(Exception ex){
			 logger.error(ex.getMessage(), ex);
		}
	}
	private Connection getConn(){
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
	}
	public void genjsp(){
		Connection conn=null;
		try{
			conn=this.getConn();
			IndDao dao=new IndDao();
			dao.setConnection(conn);
			String modelNo = "M20090300012";
			ArrayList list = dao.queryModelGroups(modelNo);
			//ArrayList list=dao.queryGroupIndexes("G20090300005");
			//this.genRscJspFile("G20090300005", "其他分析", list, "RscCusComIndex.jsp.vm", "G20090300005.jsp");
			IndAgent agent=new IndAgent();
			//agent.genCcrJspFile("");
			this.genCcrJspFile(list, "CcrIndex.jsp.vm", "");
		}catch(Exception ex){
		//	 logger.error(ex.getMessage(), ex);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
		//		 logger.error(e.getMessage(), e);
			}
		}
	}
	public static void main(String args[]){
		
		try{
			VelocityUtil vu=new VelocityUtil();
			vu.genjsp();
		}catch(Exception ex){
		//	 logger.error(ex.getMessage(), ex);
		}
	}
}
