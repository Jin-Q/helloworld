package com.yucheng.cmis.biz01line.qry.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.qry.agent.QryAgent;
import com.yucheng.cmis.biz01line.qry.domain.QryParam;
import com.yucheng.cmis.biz01line.qry.domain.QryResult;
import com.yucheng.cmis.biz01line.qry.domain.QryTemplet;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.ResourceUtils;

public class QryGenPageComponent extends CMISComponent {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	
	
	/**
	 * 生成查询界面
	 * @param tempNo
	 * @throws Exception
	 */
	public void generateAnalyseHtml(String tempNo) throws Exception{
        QryAgent qryAgent;
        QryTemplet qryTemplet;
        String orderBy = "";
		try {
			qryAgent = (QryAgent)this.getAgentInstance(QryPubConstant.QRYAGENT);
			qryTemplet = qryAgent.getQryTemplet(tempNo);//获得查询模板domain
			String jspFileName=qryTemplet.getJspFileName();//生成文件路径名
			String jspQueryName = qryTemplet.getTempName();
			String querySql=qryTemplet.getQuerySql();
			querySql = querySql.toLowerCase();
			if( querySql.indexOf(" order ")>-1 && querySql.indexOf(" by ") > -1 ){
				orderBy = querySql.substring(querySql.lastIndexOf(" by ")+4);
			}
			logger.info("\nquerySql:"+querySql+"\norderBy:"+orderBy+"\n");
			
			ArrayList paramList=null;
			try{
				paramList= qryAgent.getQryParamList(tempNo);
			}catch(AgentException e){
				logger.error(e.getMessage(),e);
				throw e;
			}
			if(paramList==null||paramList.size()==0){
				//啥都不做！
			}else{
				//按照查询出来的paramList的条件逐条生成
				int paramListSize=paramList.size();
				StringBuffer sb=new StringBuffer();
				sb.append("<input type=\"hidden\" id=\"IN_TEMPNO\"  name=\"IN_TEMPNO\"  value=\""+tempNo+"\"/>\n");
				sb.append("<input type=\"hidden\" id=\"IN_JSP_FILE_NAME\"  name=\"IN_JSP_FILE_NAME\"  value=\""+jspFileName+"\"/>\n");
				sb.append("<input type=\"hidden\" id=\"TEMP_NAME\"  name=\"TEMP_NAME\"  value=\""+jspQueryName+"\"/>\n");
				for (int i=0;i<paramListSize;i++){
					QryParam qryParam = (QryParam)paramList.get(i);
					String enname =qryParam.getEnname();
					String cnname =qryParam.getCnname();
					String paramType =qryParam.getParamType();
					//String paramDicNo=qryParam.getParamDicNo();//查询字典编号
					String opttype  =qryParam.getOpttype();//标签名称对应字典中OPTTYPE的内容
					String dicQuerySql=qryParam.getQuerySql();//查询Sql语句
					String popname = qryParam.getPopname();//查询pop名称,此字段记录在参数字典表中
					ArrayList dictList=null;//数据字典List
					if(paramType==null||"".equals(paramType)){
						throw new ComponentException("查询分析·生成查询页面：生成页面失败，条件参数类型为空！");
					}else{
						/*
						 *  01-数字型
							02-字符型
							03-日期型
							11-下拉列表框
							21-pop框
						 * 
						 * */
						//cnname = new String(this.gbk2utf8(cnname),"utf-8");
						if(paramType.equals(QryPubConstant.STD_ZB_PARAM_TYPE_01)){
							sb.append(this.genDecimalInput(enname, cnname));
						}else if(paramType.equals(QryPubConstant.STD_ZB_PARAM_TYPE_02)){
							sb.append(this.genTextInput(enname, cnname));
						}else if(paramType.equals(QryPubConstant.STD_ZB_PARAM_TYPE_03)){
							sb.append(this.genPopCalendar(enname, cnname));
						}else if(paramType.equals(QryPubConstant.STD_ZB_PARAM_TYPE_11)){
							if(opttype!=null&&!opttype.equals("")){

								dictList = qryAgent.getDictListFromContext(opttype);
							}else if(dicQuerySql!=null&&!dicQuerySql.equals("")){
								//从数据库中取出来
								dictList = qryAgent.getDictList(dicQuerySql);
							}
							sb.append(this.genCombobox(enname, cnname, dictList));
						}else if(paramType.equals(QryPubConstant.STD_ZB_PARAM_TYPE_21)){
							//pop框
							sb.append(this.genPopInput(enname, cnname,popname));
						}else{
							throw new ComponentException("没出现过的条件参数类型["+paramType+"]");
						}
					}
					
				}
				//将生成的String存入文件。
				this.genFile(tempNo+".jsp",jspFileName, sb.toString(),"jsp");
			}
			//生成查询结果选择页面和查询结果排序界面
			ArrayList resultList=null;
			String toSelectStr=null;
			String toOrderStr=null;
			try{
				resultList = qryAgent.getQryResultList(tempNo);
			}catch(ComponentException e){
				logger.error(e.getMessage(),e);
				throw e;
			}
			if(resultList==null||resultList.size()==0){
				//啥都不做！
				throw new ComponentException("该查询分析未配置查询结果字段。");
			}else{
				toSelectStr = genToSelectPageStr(resultList);
				toOrderStr = genToOrderPageStr(orderBy,resultList);
				//将生成的toSelectStr存入toString文件。
				this.genFile(tempNo+"toSelect.jsp",jspFileName,toSelectStr,"jsp");
				//将生成的toOrderStr存入toString文件。
				this.genFile(tempNo+"toOrder.jsp",jspFileName,toOrderStr,"jsp");
				
			}
			//生成js文件
			String JSStr=this.genJavaScript();
			this.genFile(tempNo+".js", jspFileName, JSStr,"js");
		
		} catch (AgentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new ComponentException(e);
		}		
		
	}

	
	
	
	
	
/**
 * 生成选择显示字段界面	
 * @param paramList
 * @return
 * @throws ComponentException
 */
	private String genToSelectPageStr(ArrayList resultList) throws ComponentException{
		StringBuffer sb=new StringBuffer();
		try{
		 sb.append("<div class='container'>");
	        sb.append("<div class='leftContainer'>");
		sb.append("\t\t\t\t\t\t<div class='seleteSwrap'><select ondblclick=javascript:add2List('formListToSelect','toListToSelect')  size=25 name='formListToSelect' id='formListToSelect'>\n");

		Iterator iter=resultList.iterator();
		while(iter.hasNext()){
			QryResult qryResult=(QryResult)iter.next();
			String cnname=qryResult.getCnname();
			String enname=qryResult.getEnname();
			sb.append("\t\t\t\t\t\t\t<option value=\"").append(enname).append("\">").append(cnname).append("</option>\n");
			
		}
		sb.append("\t\t\t\t\t\t</select></div>\n");
                sb.append("\t\t\t\t\t<div class='leftFooter'>双击可加入</div>\n");
                sb.append("\t\t\t\t</div>\n");
                
                sb.append("\t\t\t\t\t<div class='centerContainer'>\n");
		sb.append("\t\t\t\t\t\t<div id='btnDiv'><input class='btnStyle'  onclick=add2List('formListToSelect','toListToSelect') type=button value=加入 name=addMap1></div>\n");		
		sb.append("\t\t\t\t\t\t\t<div id='btnDiv'><input class='btnStyle'  onclick=del2List('toListToSelect') type=button value=删除 name=delMap1></div>\n");
		sb.append("\t\t\t\t\t\t\t</div>\n");
		
		sb.append("\t\t\t<div class='rightContainer'>\n");
                sb.append("\t\t\t\t\t<div class='seleteSwrap'><select ondblclick=javascript:del2List('toListToSelect')  multiple size=25 name=toListToSelect id='toListToSelect'></select></div>\n");
                sb.append("\t\t\t\t\t<div class='rightFooter'>双击可删除</div>\n");
                sb.append("\t\t</div>\n");
                sb.append("<div class='clear'></div></div>\n");

		
		return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ComponentException("生成排序页面失败");
		}finally{
			sb.delete(0, sb.length());
			sb.setLength(0);			
		}

	}	
	
	/**
	 * toOrderPage使用别名组成生成的结果。
	 * 生成排序页面
	 * @param CXJGicoll
	 * @return
	 * @throws ComponentException
	 */
	private String genToOrderPageStr(String orderBy,ArrayList resultList) throws ComponentException{
		
		StringBuffer sb=new StringBuffer();
		try{
		sb.append("<div class='container'>");
		sb.append("<div class='leftContainer'>");
		sb.append("\t\t\t\t\t\t<div class='seleteSwrap'><select ondblclick=javascript:add2List('formListToOrder','toListToOrder')  size=25 name='formListToOrder' id='formListToOrder'>\n");

		Iterator iter=resultList.iterator();
		while(iter.hasNext()){
			QryResult qryResult=(QryResult)iter.next();
			String cnname=qryResult.getCnname();
			String enname2=qryResult.getEnname2();
			String enname=qryResult.getEnname();
			if( orderBy.indexOf(enname2.toLowerCase()) > -1 ||
			    orderBy.indexOf(enname.toLowerCase()) > -1 ){
				continue;
			}
			sb.append("\t\t\t\t\t\t\t<option value=\"").append(enname2).append("\">").append(cnname).append("</option>\n");
			sb.append("\t\t\t\t\t\t\t<option value=\"").append(enname2+" desc\"").append(">").append(cnname+"(降序)").append("</option>\n");
			
		}
		sb.append("\t\t\t\t\t\t</select></div>\n");
		sb.append("\t\t\t\t\t<div class='leftFooter'>双击可加入</div>\n");
		sb.append("\t\t\t\t</div>\n");
		
		sb.append("\t\t\t\t\t<div class='centerContainer'>\n");
		sb.append("\t\t\t\t\t\t<div id='btnDiv'><input class='btnStyle'   onclick=add2List('formListToOrder','toListToOrder') type=button value=加入 name=addMap1></div>\n");		
		sb.append("\t\t\t\t\t\t\t<div id='btnDiv'><input class='btnStyle'   onclick=del2List('toListToOrder') type=button value=删除 name=delMap1></div>\n");
		sb.append("\t\t\t\t\t\t\t</div>\n");
		
		sb.append("\t\t\t<div class='rightContainer'>\n");
		sb.append("\t\t\t\t\t<div class='seleteSwrap'><select ondblclick=javascript:del2List('toListToOrder')  multiple size=25 name=toListToOrder id='toListToOrder'></select></div>\n");
		sb.append("\t\t\t\t\t<div class='rightFooter'>双击可删除</div>\n");
		sb.append("\t\t</div>\n");
		sb.append("<div class='clear'></div></div>\n");

		
		return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException("生成排序页面失败");
		}finally{
			sb.delete(0, sb.length());
			sb.setLength(0);			
		}

		
	}
	
	
	
	
	/**
	 * 生成文件
	 * 无论是jsp,还是js都通过此方法生成
	 * @param fileName
	 * @param folderName
	 * @param str
	 * @param type
	 * @throws ComponentException
	 * @throws FileNotFoundException
	 */
	private void genFile(String fileName,String folderName,String str,String type) throws ComponentException, FileNotFoundException{

	 	   File file = null;
	 	   OutputStreamWriter writer=null;
		   String folder,dir,jsdir="";
		   ResourceBundle res = ResourceBundle.getBundle("cmis");
		   try{
			   dir = res.getString("qry.jsp.path");  
			   jsdir = res.getString("qry.js.path");
		   }catch (Exception e) {
			   logger.error("未找到统计查询模块配置路径参数[qry.jsp.path]或[qry.js.path]，请联系管理员！");
			   throw new ComponentException("未找到统计查询模块配置路径参数[qry.jsp.path]或[qry.js.path]，请联系管理员！");
		   }
		   if("".equals(dir) || "".equals(jsdir)){
			   logger.error("统计查询模块配置路径参数[qry.jsp.path]或[qry.js.path]为空，请联系管理员！");
			   throw new ComponentException("统计查询模块配置路径参数[qry.jsp.path]或[qry.js.path]为空，请联系管理员！");
		   }
			String path=ResourceUtils.getFile(dir).getAbsolutePath();
			URL url=QryGenPageComponent.class.getResource("");
			path=url.getPath(); 
			path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 
			logger.info("模型生成根路径："+path);
			
			//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
			logger.info("模型生成jsp文件路径："+dir);
			logger.info("模型生成js文件路径:"+jsdir);
		   try {
			   if (type.equals("jsp")){
				   	//folder=path+dir;//生成目录换成共享盘下文件夹不需要获取根路径
				   folder=dir;
				   file = new File(folder);
			   }else{//type.equals("js")
				   //folder=path+jsdir;//生成目录换成共享盘下文件夹不需要获取根路径
				   folder=jsdir;
				   file = new File(folder);
			   }
			  folder=folder+File.separator+folderName;
			  file = new File(folder);
			  //如果文件夹不存在则创建文件夹
			  if(!file.exists() ){
				  if(!file.mkdirs()){
					  throw new ComponentException("创建文件夹["+folderName+"]失败!");
				  }
			  }
			  fileName = folder+File.separator+fileName;//"mvcs"+File.separator+"CMISMvc"+ File.separator +"analyse"+ File.separator +"jsps"+ File.separator + 
			  logger.info("生成文件名:"+fileName);
			  file = new File(fileName);
			  if( file.exists() ){
				  if(type.equals("jsp")){
					  file.delete();
				  }else{
					  //如果是生成js文件的话，发现重名文件则不再生成。
					  return ;
				  }
			  }
			  if(!file.createNewFile()){
				  throw new ComponentException("创建文件失败!");
			  }
			  /*初始化文件生成类*/
			  //writer = new BufferedWriter(new FileWriter(file));
			  writer= new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			  /*写入文件头*/
			  String head="<%@page language=\"java\" contentType=\"text/html; charset=UTF-8\"%>\n";
			  head+="<%@taglib uri=\"/WEB-INF/c-rt.tld\" prefix=\"c\"%>\n";
			  if(type.equals("jsp")){
				  //jsp才需要写头
				  writer.write(head);
			  }
			  
			  writer.write(str);//new String(str.getBytes("ISO-8859-1"),"UTF-8")
			  writer.flush();
		   } catch (Exception e) {
			  logger.error(e.getMessage(), e);
			  throw new ComponentException("生成文件失败!"+e.getMessage());
		   } finally{
			   try {
				   if( writer != null ) writer.close();
			   } catch (Exception e) {}
		   }
			
			
		}
	
	
	/**
	 * 生成页面中的固定javascript
	 * @return
	 * @throws ComponentException
	 */
	private String genJavaScript() throws ComponentException{
		String JSStr="\nfunction onReturn__modify(enname,data){" +
					 "\n\treturn false;"+
				     "\n}"+
				     "\nfunction doPopWindow_modify(){"+
				     "\n\treturn false;"+
				     "\n}" +
				     "\nfunction onblurCombobox_after(){" +
				     "\n\treturn false;" +
				     "\n}" +
				     "\nfunction onblurPopCalendar_after(){" +
				     "\n\treturn false;" +
				     "\n}" +
				     "\nfunction onblurDecimalInput_after(){" +
				     "\n\treturn false;" +
				     "\n}" +
				     "\nfunction onblurTextInput_after(){" +
				     "\n\treturn false;" +
				     "\n}";
		return JSStr;
	}
	
	/**
	 * 生成弹出框html代码
	 * @todo 需要增加一个公用的查询pop有难度哦~
	 * 之前的做法是直接使用popList__tableName
	 * 然后将返回字段的名称修改为onReturn_xxx中的xxx替换为返回字段名称
	 * 现在不产生默认的popList。该怎么好呢~
	 * 修改替换方案，将popList__xxxxx的替换方法修改为将整个action的url进行替换，这样需要做新pop的时候只需要自己做一个action就可以了。
	 * 
	 * 
	 * 
	 * 
	 * 弹出框有普通弹出框和机构弹出框2种 如果字段名为organno则产生
	 * 机构专用的pop选择项，在xx机构中
	 * @param enname 文本框ID
	 * @param cnname 文本框label
	 * @param popName  查询表名pop Action名#字段名
	 * @return
	 * @throws ComponentException
	 */
	private String genPopInput(String enname,String cnname,String popName)throws ComponentException{
		StringBuffer sb;
		sb = new StringBuffer();
		try{
			
			//popName=popName.toLowerCase();
			String popNameList[] =popName.split("#");
			
			String tablename=popNameList[0];
			String selectname=popNameList[1];

			
			
			if(selectname.equals("organno")){
				//对机构码进行特殊处理，增加OPER选项＂在...中＂
				
				sb.append("\n<p class='col'>");				
				sb.append("\n\t\t<span class=\"titleCol \">"+cnname+"</span>");				
				sb.append("\n\t<span class=\"selectCol\">");			
				sb.append("\n\t\t\t<select name=\"_"+enname+"_et\" id=\"_"+enname+"_et\" onchange=\"doSelect__Oper(this,'"+enname+"\')\">");
				sb.append("\n\t\t\t\t<option value='"+QryPubConstant.SYS_DIC_OPER_10+"'>等于</option>");
				sb.append("\n\t\t\t\t<option value='"+QryPubConstant.SYS_DIC_OPER_8+"' selected>在...中</option>\n");
				sb.append("\n\t\t\t</select>");
				sb.append("\n\t\t</span>");
				sb.append("\n\t\t<span class='inputCol'>");
			//	sb.append("\n\t\t\t<input type=\"TEXT\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"${context.organNo}\" />");	
				sb.append("\n\t\t\t<input type=\"hidden\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />");
				sb.append("\n\t\t\t<input type=\"TEXT\" id=\""+enname+"_organname_VALUE\" name=\""+enname+"_organname_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  />");
			
				sb.append("\n\t\t\t<input type=\"button\" name = \""+enname+"_BUTTON\" onClick=\"doPopWindow('"+tablename+"','"+selectname+"','"+enname+"')\" value=\"请选择\"/>");
				sb.append("\n\t</span>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_OPER\" value=\"8\"/>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_DATATYPE\" value=\"01\"/>");
				sb.append("\n</p>");
			
			}else if(selectname.equals("cus_id") || selectname.equals("cusid")){  //加客户码POP 2014-06-12
				sb.append("\n<p class='col'>");				
				sb.append("\n\t\t<span class=\"titleCol \">"+cnname+"</span>");				
				sb.append("\n\t<span class=\"selectCol\">");			
				sb.append("\n\t\t\t<select name=\"_"+enname+"_et\" id=\"_"+enname+"_et\" onchange=\"doSelect__Oper(this,'"+enname+"\')\">");
				sb.append("\n\t\t\t\t<option value='"+QryPubConstant.SYS_DIC_OPER_1+"'>等于</option>");
				sb.append("\n\t\t\t</select>");
				sb.append("\n\t\t</span>");
				sb.append("\n\t\t<span class='inputCol'>");
				sb.append("\n\t\t\t<input type=\"hidden\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />");
				sb.append("\n\t\t\t<input type=\"TEXT\" id=\""+enname+"_cusid_VALUE\" name=\""+enname+"_cusid_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  />");
			
				sb.append("\n\t\t\t<input type=\"button\" name = \""+enname+"_BUTTON\" onClick=\"doPopWindow('"+tablename+"','"+selectname+"','"+enname+"')\" value=\"请选择\"/>");
				sb.append("\n\t</span>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_DATATYPE\" value=\"01\"/>");
				sb.append("\n</p>");
			}else{
				//对于其他的pop只有等于这一种可能，所以直接将OPER赋为1
				sb.append("\n<p class='col'>");
				sb.append("\n\t\t<span class=\"titleCol\">"+cnname+"</span>");
				sb.append("\n\t<span class=\"selectCol\">");
				//添加返回机构名称文本框
				
				sb.append("\n\t\t\t<input type=\"TEXT\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />");
			//	sb.append("\n\t\t\t<input type=\"TEXT\" id=\"organname_VALUE\" name=\"organname_VALUE\" readOnly=\"true\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />");
				sb.append("\n\t\t</span><span class='inputCol'>");
				sb.append("\n\t\t\t<input type=\"button\" name = \""+enname+"_BUTTON\" onClick=\"doPopWindow('"+tablename+"','"+selectname+"','"+enname+"')\" value=\"请选择\"/>");
				sb.append("\n\t\t</span>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>");
				sb.append("\n<input type =\"hidden\" name=\""+enname+"_DATATYPE\" value=\"01\"/>");
				sb.append("\n</p>");
			}
			//添加返回值js
			sb.append("\n<script>");
			sb.append("\nfunction onReturn__"+enname+"(data){");
			/*if("loan_direction".toLowerCase().equals(enname)) {
				sb.append("\n\tdocument.getElementById('"+enname+"_VALUE').value=data."+selectname+";");
			} else {
				sb.append("\n\tdocument.getElementById('"+enname+"_VALUE').value=data."+selectname+"._getValue();");
				sb.append("\n\tdocument.getElementById('"+enname+"_organname_VALUE').value=data.organname._getValue();");
			}*/
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
					"生成Pop框的代码===============================");
			if("organno".toLowerCase().equals(enname) || "fina_br_id".toLowerCase().equals(enname)) {
				sb.append("\n\tdocument.getElementById('"+enname+"_VALUE').value=data."+selectname+"._getValue();");
				sb.append("\n\tdocument.getElementById('"+enname+"_organname_VALUE').value=data.organname._getValue();");
			}else if("cus_id".toLowerCase().equals(enname) || "cusid".toLowerCase().equals(enname)) {  //新加客户码POP返回  2014-06-12 杨志勇
				sb.append("\n\tdocument.getElementById('"+enname+"_VALUE').value=data."+selectname+"._getValue();");
				sb.append("\n\tdocument.getElementById('"+enname+"_cusid_VALUE').value=data.cus_name._getValue();");
			}else {
				sb.append("\n\tdocument.getElementById('"+enname+"_VALUE').value=data."+selectname+";");
			} 
			
			sb.append("\n\tif(onReturn__modify){");
			sb.append("\n\t\tonReturn__modify('"+enname+"',data);");
			sb.append("\n\t}");
			sb.append("\n}");
			sb.append("\n</script>");
			return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException("生成POP标签失败，标签CNNAME:"+cnname);
		}
		finally{
			sb.delete(0, sb.length());
			sb.setLength(0);
		}
		
	}
	
	
	/**
	 * 
	 * text onClick的时候调用Calendar.show(this)方法
	 * 传入1个参数this
	 * 
	 * 
	 * 需要在html的开始include include.jsp
	 * 以及analysexxx.js
	 * 因为需要初始化
	 * @param enname
	 * @param cnname
	 * @return
	 * @throws ComponentException
	 */
	private String genPopCalendar(String enname,String cnname) throws ComponentException {
		StringBuffer sb;
		sb = new StringBuffer();
		try{
			sb.append("\n<p class='col'>\n<span class=\"titleCol\">"+cnname+"</span>");
			sb.append("\n\t<span class=\"selectCol\">");
			sb.append("<select name=\"_"+enname+"_et\" onchange=\"doSelect__Oper(this,'"+enname+"\')\">\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_1+"\" selected>等于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_2+"\">大于</option>\n\t<option value=\"3\">小于</option>\n\t<option value=\"4\">大于等于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_5+"\">小于等于</option>\n\t<option value=\"6\">在...之间</option>\n\t<option value=\"9\">不等于</option>\n</select>\n\n</span>\n");
			sb.append("<span class='inputCol'><input type=\"TEXT\" name= \""+enname+"_VALUE\" id=\""+enname+"_VALUE\" onblur= \"onblurPopCalendar('"+enname+"_VALUE')\" class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" onClick=\"openCalendar(this)\"/>-&nbsp;&nbsp;");
			sb.append("<input type=\"TEXT\" id=\""+enname+"_VALUE2\" name=\""+enname+"_VALUE2\" onblur= \"onblurPopCalendar('"+enname+"_VALUE2')\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\"  onClick=\"openCalendar(this)\"/>\n</span>\n");
			sb.append("<script language='javascript'>\n");
			sb.append("document.getElementById(\""+enname+"_VALUE2\").disabled=true;\n");
			sb.append("\t</script>\n");
			
			sb.append("<input type =\"hidden\" name=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>\n");
			sb.append("<input type =\"hidden\" name=\""+enname+"_DATATYPE\" value=\"01\"/>\n");
			sb.append("\n</p>");
			return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException("生成Calendar标签失败，标签CNNAME:"+cnname);
		}
		finally{
			sb.delete(0, sb.length());
			sb.setLength(0);
		}
	}
	/**
	 * 生成普通input标签
	 * @param enname
	 * @param cnname
	 * @return
	 * @throws ComponentException
	 */
	private String genTextInput(String enname,String cnname) throws ComponentException {
		StringBuffer sb;
		sb = new StringBuffer();
		
		try{
			sb.append("\n<p class='col'>");		
			sb.append("\n\t\t<span class=\"titleCol\">"+cnname+"</span>\n");
			sb.append("\n\t<span class=\"selectCol\">");
			sb.append("<select name=\"_"+enname+"_et\" id=\"_"+enname+"_et\" onchange=\"doSelect__Oper(this,'"+enname+"\')\">\n\t<option value='1' selected>等于</option>\n\t<option value=\"7\">像...</option>\n");
			sb.append("\n</select>\n</span>\n");
			sb.append("\n\t<span class='inputCol'><input type=\"TEXT\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\"   onblur=\"onblurTextInput('"+enname+"_VALUE')\" class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />\n");
			sb.append("<input type =\"hidden\" name=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>");
			sb.append("<input type =\"hidden\" name=\""+enname+"_DATATYPE\" value=\"02\"/>");
			sb.append("</span>\n</p>");
			

			
			return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ComponentException("生成text标签失败，标签CNNAME:"+cnname);
		}
		finally{
			sb.delete(0, sb.length());
			sb.setLength(0);
		}
	}
	/**
	 * 生成金额型input框
	 * @param enname
	 * @param cnname
	 * @return
	 * @throws ComponentException
	 */
	private String genDecimalInput(String enname,String cnname) throws ComponentException {
		StringBuffer sb;
		sb = new StringBuffer();
		try{
			sb.append("\n\n\n<p class='col'>");
			sb.append("\n<span class=\"titleCol\">"+cnname+"</span><span class=\"selectCol\">\n");
			sb.append("<select id=\"_"+enname+"_et\" name=\"_"+enname+"_et\" onchange=\"doSelect__Oper(this,'"+enname+"\')\">\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_1+"\" selected>等于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_2+"\">大于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_3+"\">小于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_4+"\">大于等于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_5+"\">小于等于</option>\n\t<option value = \""+QryPubConstant.SYS_DIC_OPER_6+"\">在...之间</option>\n\t<option value=\"9\">不等于</option>\n</select>\n");
			sb.append("</span>\n<span class='inputCol'>\n\t<input type=\"TEXT\" id=\""+enname+"_VALUE\" name=\""+enname+"_VALUE\"  onblur=\"onblurDecimalInput('"+enname+"_VALUE')\" class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" />&nbsp;&nbsp;-&nbsp;&nbsp;");
			sb.append("<input type=\"TEXT\" id=\""+enname+"_VALUE2\" name=\""+enname+"_VALUE2\"  onblur=\"onblurDecimalInput('"+enname+"_VALUE2')\"  class=\"emp_dfcontainer_content\"  size=\"30\"  value=\"\" /></span>\n");
			sb.append("<script language='javascript'>\n");
			sb.append("\tdocument.getElementById(\""+enname+"_VALUE2\").disabled=true;\n");
			sb.append("</script>\n");
			
			sb.append("<input type =\"hidden\" name=\""+enname+"_OPER\" id=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>");
			sb.append("<input type =\"hidden\" name=\""+enname+"_DATATYPE\" id=\""+enname+"_DATATYPE\" value=\"01\"/>");
			sb.append("</p>");

			
			return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException("生成金额型input标签失败，标签CNNAME:"+cnname);
		}
		finally{
			sb.delete(0, sb.length());
			sb.setLength(0);
		}
	}
	
	/**
	 * 生成下拉列表框
	 * @param enname
	 * @param cnname
	 * @param dictList
	 * @return
	 * @throws ComponentException
	 */
	private String genCombobox(String enname,String cnname,ArrayList dictList) throws ComponentException {
		StringBuffer sb;
		sb = new StringBuffer();
		try{
		        sb.append("\n<p class='col'>");
			sb.append("\n\t\t<span class=\"titleCol\">"+cnname+"</span>");
			sb.append("\n\t<span class=\"selectCol\">");
			sb.append("\n\t\t\t<select name=\""+enname+"_VALUE").append("\" id=\"").append(enname+"_VALUE");
			sb.append("\" class=\"").append("cmistag_text");
			sb.append("\" title=\"").append("请选择"+cnname);
			sb.append("\" onblur= \"onblurCombobox('"+enname+"')\">");
			sb.append("\n\t\t\t\t<option value=''>----请选择----</option>");
			
			if (dictList!=null) {
				for (int i=0; i<dictList.size(); i++) {
					HashMap record = (HashMap)dictList.get(i);
					String value = null;
					value = (String)record.get(CMISDataDicService.ATTR_ENNAME);

					if(value==null){
						//出错
						break;
					}

					String desc = null;
					desc = (String)record.get(CMISDataDicService.ATTR_CNNAME);
					if(desc==null)
					desc = value;

					sb.append("\n\t\t\t\t<option value=\"").append(value).append("\">");
					sb.append(desc).append("</option>");				
				}
				
			}
			sb.append("\n\t\t\t</select>");
			sb.append("\n\t\t\t<input type =\"hidden\" name=\""+enname+"_OPER\" id=\""+enname+"_OPER\" value = \""+QryPubConstant.SYS_DIC_OPER_1+"\"/>");
			sb.append("\n\t\t\t<input type =\"hidden\" name=\""+enname+"_DATATYPE\" id=\""+enname+"_DATATYPE\" value=\"01\"/>");
			sb.append("\n\t\t</span>");
			sb.append("\n</p>\n");
		
		return sb.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException("生成Combobox标签失败，标签CNNAME:"+cnname);
		}
		finally{
		sb.delete(0, sb.length());
		sb.setLength(0);
		}
	}	
	
	public static void main(String[] args) throws Exception {
		QryGenPageComponent qry = new QryGenPageComponent();
		String str = qry.genPopInput("enname", "cnname", "popName#");
		logger.info(str);
	}
	
}
