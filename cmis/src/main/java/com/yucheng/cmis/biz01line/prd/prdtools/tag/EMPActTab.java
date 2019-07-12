package com.yucheng.cmis.biz01line.prd.prdtools.tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.ext.tag.EMPExtTab;
import com.ecc.emp.ext.tag.EMPExtTabGroup;
import com.ecc.emp.ext.tag.page.EMPExtPageObjects;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;

public class EMPActTab extends EMPExtTab {
	private static final long serialVersionUID = 1L;
	protected String url;
	protected String reqParams;
	protected String label;
	protected boolean initial;
	protected boolean needFlush;
	private Tag parent;
	
	public EMPActTab(){
		
	}
	/**
	 * 标签生成规则：根据当前资源ID查找系统tab标签页动态挂接的符合tab页的动态数据
	 * 通过tab标签页表中的资源ID查找资源表S_RESOURCE中资源的相关信息，将满足条件的
	 * tab数据封装到显示层，存入pageObjs中，在前台jsp页面显示
	 * @author Pansq
	 * @create_time 2013-07-13
	 */
	public int doStartTag() throws JspException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//ResultSet rsTerm = null;
		ResultSet rsSub = null;
		String menuId =null;
		try {
			/** 获取资源 */
			Context context = (Context)this.pageContext.getRequest().getAttribute(EMPConstance.ATTR_CONTEXT);
			TableModelLoader modelLoader = (TableModelLoader)context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
			HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			menuId = (String)request.getAttribute("menuIdTab");
			if(menuId == null || menuId.trim().length() == 0){
				menuId = (String)context.getDataValue("menuId");
				if(menuId == null || menuId.trim().length() == 0){
					menuId = (String)context.getParent().getDataValue("menuId");
				}
			}
			String EMP_SID = (String)context.getDataValue("EMP_SID");
			String op = "";
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = ConnectionManager.getConnection(dataSource);
			/** URL标签封装所需参数 */
			String urlProject = "/cmis/";
			String urlEMP = "";
			/** 封装显示的从标签表模型名称集合 */
			String modelCollection = "";
			if(context.containsKey("op")){
				op = (String)context.getDataValue("op");
			}
			String querySql = "SELECT * FROM PRD_SUB_TAB_ACTIVITY WHERE MAINID=? ORDER BY NUM";
			pstmt = connection.prepareStatement(querySql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, menuId);
			rs = pstmt.executeQuery();
			
			int count = 0; 
			if(rs.last()){   //将游标移到最后行
				count = rs.getRow();
				rs.beforeFirst();   //游标回滚到第一行
			}
			
			if(count < 1){
				EMPLog.log("MESSAGE", EMPLog.DEBUG, 0, "动态加载ExtActTab标签，menuId:"+menuId+"，未找到TAB页签配置项。", null);
			}
			while(rs.next()){
				StringBuffer sb = new StringBuffer();
				boolean ifSee = true;
				boolean ifSee1 = true;
				boolean ifSee2 = true;
				boolean ifSee3 = true;
				boolean mutiResult = true;
				String mainModel = (String)rs.getString("mainmodel");
				String mainTerm = (String)rs.getString("mainterm");
				String subModel = (String)rs.getString("submodel");
				String subNameNew = (String)rs.getString("subname");
				System.out.println(subNameNew);
				TableModel searchModel = modelLoader.getTableModel(mainModel);
				KeyedCollection kModel = new KeyedCollection();
				if(searchModel == null){
					//表模型未录入或者查询不到
					EMPLog.log("MESSAGE", EMPLog.DEBUG, 0, "动态加载ExtActTab标签时，表模型未录入或者查询不到,过滤条件不做判断依据", null);		
				}else {
					if(context.containsKey(mainModel)){
						kModel = (KeyedCollection)context.getDataElement(mainModel);
					}
					
					if(mainTerm != null && mainTerm.trim().length() > 0){
						/** 取得配置主子源过滤条件，进行解析，对获得的页面表模型数据进行判断，筛选记录 */
						if(mainTerm.indexOf(";") != -1){//多字段条件
							String[] terms = mainTerm.split(";");
							for(int i=0;i<terms.length;i++){
								String term = terms[i];
								if(term.indexOf(",") != -1 || term.contains("sql")){//一个字段多条件,符合其一则满足 ,或者包含sql语句
									ifSee = false;
									String columnName = term.substring(0,term.indexOf("=")).trim();
									String columnValue = term.substring(term.indexOf("=")+1,term.length());
									/****************添加sql条件查询 start*/
									if(columnValue.startsWith("sql")){
										String res = "";
										String subSql = columnValue.substring(columnValue.indexOf("sql")+4, columnValue.length());
										pstmt = connection.prepareStatement(subSql);
										rsSub = pstmt.executeQuery();
										while(rsSub.next()){
											res += rsSub.getString(1)+",";	
										}
										if(res.length()>0){
											columnValue = res.substring(0, res.length()-1);
										}else{
											columnValue = "";
										}
									}
									/****************添加sql条件查询 end*/
									String[] columnValues = columnValue.split(",");
									for(int j=0;j<columnValues.length;j++){
										String columnOne = columnValues[j].trim();
										String kFieldValue = "";
										if(kModel.containsKey(columnName)){
											kFieldValue = (String)kModel.getDataValue(columnName);
										}else if(context.containsKey(columnName)){
											kFieldValue = (String)context.getDataValue(columnName);
										}else {
										}
										if(kFieldValue.indexOf(",") != -1){
											String[] kFields = kFieldValue.split(",");
											for(int k=0;k<kFields.length;k++){
												if(kFields[k].equals(columnOne)){
													ifSee = true;
												}
											}
										}else {
											if(columnOne.equals(kFieldValue)){
												ifSee = true;
											}
										}
									}
									if(!ifSee){
										mutiResult = false;
									}
								}else {//一个字段一个条件
									ifSee1 = false;
									String columnName = term.substring(0,term.indexOf("=")).trim(); 
									String columnValue = term.substring(term.indexOf("=")+1,term.length()).trim();
									/****************添加sql条件查询 start*/
									if(columnValue.startsWith("sql")){
										String res = "";
										String subSql = columnValue.substring(columnValue.indexOf("sql")+4, columnValue.length());
										pstmt = connection.prepareStatement(subSql);
										rsSub = pstmt.executeQuery();
										while(rsSub.next()){
											res += rsSub.getString(1)+",";	
										}
										if(res.length()>0){
											columnValue = res.substring(0, res.length()-1);
										}else{
											columnValue = "";
										}
									}
									/****************添加sql条件查询 end*/
									String kFieldValue = "";
									if(kModel.containsKey(columnName)){
										kFieldValue = (String)kModel.getDataValue(columnName);
									}else if(context.containsKey(columnName)){
										kFieldValue = (String)context.getDataValue(columnName);
									}else {
									}
									if(null==kFieldValue||"".equals(kFieldValue)){
										kFieldValue = "null";
									}
									if(kFieldValue.indexOf(",") != -1){
										String[] kFields = kFieldValue.split(",");
										for(int k=0;k<kFields.length;k++){
											if(kFields[k].trim().equals(columnValue)){
												ifSee1 = true;
											}
										}
									}else {
										if(columnValue.equals(kFieldValue)){
											ifSee1 = true;
										}
									}
									if(!ifSee1){
										mutiResult = false;
									}
								}
							}
						}else {//单字段条件
							if(mainTerm.indexOf(",") != -1 || mainTerm.contains("sql")){//多条件 ,或者包含sql语句
								ifSee2 = false;
								String columnName = mainTerm.substring(0,mainTerm.indexOf("=")).trim();
								String columnValue = mainTerm.substring(mainTerm.indexOf("=")+1,mainTerm.length());
								/****************添加sql条件查询 start*/
								if(columnValue.startsWith("sql")){
									String res = "";
									String subSql = columnValue.substring(columnValue.indexOf("sql")+4, columnValue.length());
									pstmt = connection.prepareStatement(subSql);
									rsSub = pstmt.executeQuery();
									while(rsSub.next()){
										res += rsSub.getString(1)+",";	
									}
									if(res.length()>0){
										columnValue = res.substring(0, res.length()-1);
									}else{
										columnValue = "";
									}
								}
								/****************添加sql条件查询 end*/
								String[] columnValues = columnValue.split(",");
								for(int j=0;j<columnValues.length;j++){
									String columnOne = columnValues[j].trim();
									String kFieldValue = "";
									if(kModel.containsKey(columnName)){
										kFieldValue = (String)kModel.getDataValue(columnName);
									}else if(context.containsKey(columnName)){
										kFieldValue = (String)context.getDataValue(columnName);
									}else {
									}
									if(kFieldValue.indexOf(",") != -1){
										String[] kFields = kFieldValue.split(",");
										for(int k=0;k<kFields.length;k++){
											if(kFields[k].trim().equals(columnOne)){
												ifSee2 = true;
											}
										}
									}else {
										if(columnOne.equals(kFieldValue)){
											ifSee2 = true;
										}
									}
								}
							}else {//单条件
								ifSee3 = false;
								String columnName = mainTerm.substring(0,mainTerm.indexOf("=")).trim(); 
								String columnValue = mainTerm.substring(mainTerm.indexOf("=")+1,mainTerm.length()).trim();
								/****************添加sql条件查询 start*/
								if(columnValue.startsWith("sql")){
									String res = "";
									String subSql = columnValue.substring(columnValue.indexOf("sql")+4, columnValue.length());
									pstmt = connection.prepareStatement(subSql);
									rsSub = pstmt.executeQuery();
									while(rsSub.next()){
										res += rsSub.getString(1)+",";	
									}
									if(res.length()>0){
										columnValue = res.substring(0, res.length()-1);
									}else{
										columnValue = "";
									}
								}
								/****************添加sql条件查询 end*/
								String kFieldValue = "";
								if(kModel.containsKey(columnName)){
									kFieldValue = (String)kModel.getDataValue(columnName);
								}else if(context.containsKey(columnName)){
									kFieldValue = (String)context.getDataValue(columnName);
								}else {
								}
								if(null==kFieldValue||"".equals(kFieldValue)){
									kFieldValue = "null";
								}
								if(kFieldValue.indexOf(",") != -1){
									String[] kFields = kFieldValue.split(",");
									for(int k=0;k<kFields.length;k++){
										if(kFields[k].trim().equals(columnValue)){
											ifSee3 = true;
										}
									}
								}else {
									if(columnValue.equals(kFieldValue)){
										ifSee3 = true;
									}
								}
							}
						}
					}else {
						//不做任何处理，无条件默认显示
					}
				}
				if(ifSee&&ifSee1&&ifSee2&&ifSee3&mutiResult){
					/** 将满足要求的tab框封装成页面可显示的tab页签 */
					String subConndition = "&subConndition= ";
					String subConnditionHelp = "";
					String subName = "";
					String sbuUrl = "";
					String subId = (String)rs.getString("subid");//从资源ID
					//String subNum = (String)rs.getString("subnum");//从资源标识
					String subTerm = "";
					if(context.getParent().containsKey("subMenuId")){
						context.getParent().setDataValue("subMenuId", subId);
					}else {
						context.getParent().addDataField("subMenuId", subId);
					}
					if(context.getParent().containsKey("op")){
						context.getParent().setDataValue("op", op);
					}else {
						context.getParent().addDataField("op", op);
					}
					subTerm = (String)rs.getString("subterm");//从资源过滤条件
					if(subTerm != null && subTerm.trim().length() > 0){
						String[] subTerms = subTerm.split(";");
						for(int i=0;i<subTerms.length;i++){
							if(subTerms[i].indexOf("=") != -1){//方法预留
								String subColumnName = subTerms[i].substring(0,subTerms[i].indexOf("=")).trim();
								String mainColumnName = subTerms[i].substring(subTerms[i].indexOf("=")+1,subTerms[i].length()).trim();
								String subColumnDefault = "";
								if(mainColumnName.indexOf("{") != -1){
									subColumnDefault = mainColumnName.substring(mainColumnName.indexOf("{")+1,mainColumnName.indexOf("}"));
									if(subColumnDefault.indexOf(",") != -1){
										String[] subColumns = subColumnDefault.split(",");
										String subColumnIn = "";
										for(int j=0;j<subColumns.length;j++){
											subColumnIn += " '"+subColumns[j].trim()+"' ,";
										}
										subColumnIn = " ("+subColumnIn.substring(0,subColumnIn.length()-1)+") ";
										subConndition += (subColumnName +" in "+subColumnIn+" ");
									}else {
										subConndition += (subColumnName +" = '"+subColumnDefault+"' ");
									}
								}else {
									String kField = (String)kModel.getDataValue(mainColumnName);
									if(kField.indexOf(",") != -1){
										String[] subColumns = kField.split(",");
										String subColumnIn = "";
										for(int j=0;j<subColumns.length;j++){
											subColumnIn += " '"+subColumns[j].trim()+"' ,";
										}
										subColumnIn = " ("+subColumnIn.substring(0,subColumnIn.length()-1)+") ";
										subConndition += (subColumnName +" in "+subColumnIn+" ");
									}else {
										subConndition += (subColumnName +" = '"+kField+"' ");
									}
								}
								subConndition = subConndition + " and ";
							}else {
								String columnName = subTerms[i].trim();
								String kField = "";
								
								if(kModel.containsKey(columnName)){
									kField = kModel.getDataValue(columnName)+"";
								}else if(context.containsKey(columnName)){
									kField = context.getDataValue(columnName)+"";
								}else if(kField == null || "".equals(kField)){
									for(int m=0;m<kModel.size();m++){
										DataElement element = (DataElement)kModel.getDataElement(m);
										if (element instanceof KeyedCollection) {
											if(((KeyedCollection) element).containsKey(columnName)){
												kField = ((KeyedCollection) element).getDataValue(columnName)+"";
												break;
											}
										}
									}
								}else {
									//throw new Exception("主表页面未定义该条件字段！");
									EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "动态加载ExtActTab标签时，主表单页面未定义["+columnName+"]该条件字段！", null);	
								}
								
								subConnditionHelp += "&"+columnName+"="+kField;
							}
						}
						boolean flagHelp = subConndition.endsWith("and ");
						if(flagHelp){
							subConndition = subConndition.substring(0, subConndition.length()-4);
						}
						subConndition = subConndition.substring(0,subConndition.length())+subConnditionHelp;
					}
					/** 封装显示的从标签表模型名称集合 */
					if(subModel != null && subModel != ""){
						modelCollection += subModel+",";
					}
					
					String subSql = "SELECT RESOURCEID,CNNAME,URL FROM S_RESOURCE  WHERE RESOURCEID=?";
					pstmt = connection.prepareStatement(subSql);
					pstmt.setString(1, subId);
					rsSub = pstmt.executeQuery();
					if(rsSub.next()){
						subName = rsSub.getString("CNNAME");
						sbuUrl = rsSub.getString("URL");
					}
					/** 排除资源定义中传递参数 */
					if(sbuUrl.indexOf("?") != -1){
						urlEMP = "&EMP_SID="+EMP_SID;	
					}else {
						urlEMP = "?EMP_SID="+EMP_SID;
					}
					try {
						EMPExtPageObjects pageObjs = (EMPExtPageObjects)this.pageContext.getRequest().getAttribute("EMP_PAGE_OBJECTS");
						if((pageObjs != null) && (this.parent instanceof EMPExtTabGroup)){
							pageObjs.addRelatedTabs(((EMPExtTabGroup)this.parent).getId()+"."+subId);
						}
						sb.append("<div ");
						writeAttribute(sb, "id", subId+"_div");
						if(subNameNew != null && subNameNew.length() > 0){
							writeAttribute(sb, "label", subNameNew);
						}else {
							writeAttribute(sb, "label", subName);
						}
						writeAttribute(sb, "initial", "false");
						writeAttribute(sb, "needFlush", "true");
						writeAttribute(sb, "url", urlProject+sbuUrl+urlEMP+subConndition+"&menuIdTab="+menuId+"&subMenuId="+subId+"&op="+op);
						sb.append(" >");
						sb.append("</div>");
						outputContent(sb.toString());
						System.out.println(sb.toString());
						//this.doEndTag();
					} catch (Exception e) {
						EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "动态加载ExtActTab标签时，组装TAB页签错误，错误描述："+e.getMessage(), null);
						//e.printStackTrace();
					}
				}
			}
			/** 从资源显示表模型集合 */
			if(modelCollection!=null&&modelCollection.length()>0){
				if(context.containsKey("modelCollection")){
					context.setDataValue("modelCollection", modelCollection.substring(0, modelCollection.length()-1));
				}else {
					context.addDataField("modelCollection", modelCollection.substring(0, modelCollection.length()-1));
				}
				
			}else {
				if(context.containsKey("modelCollection")){
					context.setDataValue("modelCollection", "");
				}else {
					context.addDataField("modelCollection", "");
				}
			}
			
		} catch (Exception e) {
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "动态加载ExtActTab标签错误，错误描述："+e.getMessage(), null);
			e.printStackTrace();
		} finally {
			try {
				if(rsSub != null){
					rsSub.close();
					rsSub = null;
				}
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				if(connection != null){
					connection.close();
					connection = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} 
		return 1;
	}
	
	public int doEndTag() throws JspException { 
		//outputContent("</div>");
		return 0;
	}
	public Tag getParent() {
		return parent;
	}
	public void setParent(Tag parent) {
		this.parent = parent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReqParams() {
		return reqParams;
	}
	public void setReqParams(String reqParams) {
		this.reqParams = reqParams;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isInitial() {
		return initial;
	}
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	public boolean isNeedFlush() {
		return needFlush;
	}
	public void setNeedFlush(boolean needFlush) {
		this.needFlush = needFlush;
	}
}
