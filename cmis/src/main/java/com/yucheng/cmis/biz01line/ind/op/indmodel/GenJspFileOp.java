package com.yucheng.cmis.biz01line.ind.op.indmodel;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.domain.IndModelDomain;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.ResourceUtils;
/**
 * M01090300001  企事业分类
M02090300002  微型企业贷款分类
M03090300003  自然人其他贷款分类
M04090300004  自然人一般农户分类
M05090300005  住房按揭/信用证分类
M06090300006  银承分类
M07090300007  贴现分类
 * @author Administrator
 *
 */
public class GenJspFileOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(GenJspFileOp.class);
	
	public String doExecute(Context context) throws EMPException { 
		String bizKind=""; 
		String modelNo="";
		String genResult="页成已成功生成!";
		Connection connection = null;
		try{ 
			connection = this.getConnection(context);
			if(context.containsKey("genResult")){
				context.setDataValue("genResult", genResult);
			}else{
				context.addDataField("genResult", genResult);
			} 
			/*ResourceBundle res = ResourceBundle.getBundle("cmis");
			String dir = res.getString("ind.file.path");  
			String path=ResourceUtils.getFile(dir).getAbsolutePath();
			//URL url=Thread.currentThread().getContextClassLoader().getResource("/"); 
		//	String path=url.getPath(); 
			path=path.replaceAll("classes", ""); 
			path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
			logger.info("模型生成jsp文件路径："+path);
			IndPubConstant.VM_JSP_PATH=path;*/
			IndComponent indcom=(IndComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(IndPubConstant.IND_COMPONENT, context,connection); 
			modelNo=(String)context.getDataValue("model_no");
			IndModelDomain modelDomain=(IndModelDomain)indcom.queryIndModelDetail(modelNo);
//			bizKind=modelDomain.getComBizKind();
//			if(bizKind.equals("100000")//企事业分类分析页面生成 
//					||bizKind.equals("100001")//银承分类
//					||bizKind.equals("100002")
//					||bizKind.equals("100003")
//					||bizKind.equals("100004")
//					||bizKind.equals("100005")
//					||bizKind.equals("100006")//贴现分类 
//					||bizKind.equals("400000")){//贷后检查
//				indcom.genCusComJspFile(modelNo); 
//			}else if(bizKind.equals("200000")){//信用评级 
//				indcom.genCcrJspFile(modelNo);
				indcom.genCcrJspFileWithShuffle(modelNo);
//			}else{
//				genResult="非分类，非评级模型暂不支持生成页面!";
//			}
			context.setDataValue("genResult", genResult);
		}catch(Exception ce){
			logger.error(ce.getMessage(), ce); 
			genResult="页面生成失败,错误信息:"+ce.getMessage();
			throw new EMPException(ce);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "1";
	}

}
