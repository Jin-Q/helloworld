<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.base.CMISConstance" %>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.yucheng.cmis.pub.CMISModualServiceFactory"%>
<%@page import="com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@page import="com.ecc.emp.core.EMPException"%>
<%@page import="com.ecc.emp.log.EMPLog"%>
<%@page import="com.yucheng.cmis.pub.CMISModualServiceFactory"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String EMP_SID = (String)context.getParent().getDataValue("EMP_SID");
	String cus_id = "";
	if(context.containsKey("cus_id")){
		cus_id = (String)context.getDataValue("cus_id");
	}
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>

<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_label {
	vertical-align: top;
	padding-top: 4px;
	text-align: left;
	width: 500px;
}

.emp_field_textarea_textarea { /****** 长度、高度固定 ******/
	width: 500px;
	height: 50px;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
}
</style>

<script type="text/javascript">
function doSave(){
	var result = true;
	var form = document.getElementById("submitForm");
	var f = document.forms[0];
	for(var i=0;i<f.length;i++){
		var id = f[i].id.substr((f[i].id.indexOf(".")+1),f[i].id.length);
		var msg = "MSG."+id;
		
		if(f[i].type == 'checkbox' || f[i].type == 'radio'){
			var boxValue = document.getElementsByName(f[i].name);
			var must = f[i].lang;
			var mValue = false;
			for(var j=0;j<boxValue.length;j++){
				if(boxValue[j].checked == true){
					mValue = true;
				}
			}
			if(must=='true'){
				if(!mValue){
					result = false;
					document.getElementById(msg).innerHTML = "<font color='red' >*请输入必输项</font>";
				}
			}
		}
		/**
		else if(f[i].type == 'select-one'){
			var selValue = document.getElementsByName(f[i].name);
			var must = f[i].lang;
			var mValue = false;
			for(var j=0;j<selValue.length;j++){
				if(selValue[j].checked == 'selected'){
					mValue = true;
				}
			}
			if(must&&mValue){
				result = false;
				document.getElementById(msg).innerHTML = "<font color='red' >*请输入必输项</font>";
			}
		}
		*/
		else {
			if(f[i].type != 'button'){
				//alert(f[i].name+"----"+f[i].type+"----"+f[i].value);
				var must = f[i].lang;
				if(f[i].type != 'hidden'){
					if(must=='true'){
						if(f[i].value == null || f[i].value == '' || f[i].value == 'undefined'){
							result = false;
							document.getElementById(msg).innerHTML = "<font color='red' >*请输入必输项</font>";
						}
					}
				}else {
					if(must=='true'){
						if(f[i].value == null || f[i].value == '' || f[i].value == 'undefined'){
							result = false;
							//alert("请检查隐藏字段"+f[i].name+"必输项控制是否正确！");
						}
					}
				}
			}
		}
	}
	if(result){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功");
				}else {
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	}
};


function doResert(){
	var form = document.getElementById("submitForm");
	form.reset();
};	
/** --------转换多选框值-------- */
function checkValue(value){
	var returnValue = "";
	var cvalue = document.getElementsByName(value);
	for(var i=0;i<cvalue.length;i++){
		if(cvalue[i].checked == true){
			returnValue += (cvalue[i].value+",");
		}
	}
	var s = value.indexOf("_displayname");
	document.getElementById(value.substr(0,s)).value=returnValue.substr(0,returnValue.length-1);
};
/** --------必输提示信息设置-------- */
function checkValueMsg(data,lang){
	var id = data.substr((data.indexOf(".")+1),data.length);
	if(id.indexOf("_displayname") >= 0){
		id = id.substr(0,id.indexOf("_displayname"));
	}
	if(lang=="true"){
		document.getElementById("MSG."+id).innerHTML = "<font color='red' >*</font>";
	}else {
		document.getElementById("MSG."+id).innerHTML = "";
	}
	
};

function openAlertWin(url){
};

function doEvent(data,lang,value,cond,item){
	var id = data.substr((data.indexOf(".")+1),data.length);
	if(id.indexOf("_displayname") >= 0){
		id = id.substr(0,id.indexOf("_displayname"));
	}
	//alert(value);
	//alert(cond);
	//alert(document.getElementById("PspCheckItemResult."+id).value)
	var condArray = cond.split("|");
	var flag = false;
	for(var i=0;i<condArray.length;i++){
		if(condArray[i] == null || condArray[i] == "")
			continue;
		
		if(value==condArray[i]){
			flag=true;
			break;
		}
	}
	if(flag){
		//alert(cond);
		document.getElementById("PspCheckItemResult."+item+"_textdiv").style.display="none";
		document.getElementById("PspCheckItemResult."+item+"_valdiv").style.display="none";
		document.getElementById("PspCheckItemResult."+item).style.display="none";
		document.getElementById("MSG."+item).innerHTML = "";
		document.getElementById("PspCheckItemResult."+item).lang="false";
		document.getElementById("PspCheckItemResult."+item).value="";
	}else{
		document.getElementById("PspCheckItemResult."+item+"_textdiv").style.display="";
		document.getElementById("PspCheckItemResult."+item+"_valdiv").style.display="";
		document.getElementById("PspCheckItemResult."+item).style.display="";
		//alert(lang);
		if(lang=='true'){
			document.getElementById("MSG."+item).innerHTML = "<font color='red' >*</font>";
			document.getElementById("PspCheckItemResult."+item).lang="true";
		}
	}
	
}
</script>
</head>
<body class="page_content" >
	<form  id='submitForm' method='POST' action='/cmis/savePspCheckResult.do'>
		<input name="task_id" type="hidden" value="<%=(String)context.getDataValue("task_id") %>" />
		<input name="scheme_id" type="hidden" value="<%=(String)context.getDataValue("scheme_id") %>" />
		<DIV id='dataGroup_in_formsubmitForm' class='emp_group_div' style='background-color=transparent;border=0px'>
		<%
			KeyedCollection returnKColl = (KeyedCollection)context.getDataElement("returnKColl");
			IndexedCollection returnIColl = (IndexedCollection)context.getDataElement("returnIColl");
			if(returnIColl != null && returnIColl.size() > 0){
				for(int i=0;i<returnIColl.size();i++){
					KeyedCollection returnCatKColl = (KeyedCollection)returnIColl.get(i);
					KeyedCollection catKColl = (KeyedCollection)returnCatKColl.getDataElement("PspCheckCatalog");
					String catName = (String)catKColl.getDataValue("catalog_name");
					String catalogId = (String)catKColl.getDataValue("catalog_id");
				%>
					<div  class='emp_gridlayout_title'><%=catName %></div>
					<div  id='PspCheckItemGroup' class='emp_group_div'>
						<table maxColumn='2' border="0" width="100%" class='emp_gridLayout_table 2'>
				<% 
					IndexedCollection returnItemIColl = (IndexedCollection)returnCatKColl.getDataElement("returnItemIColl");
					if(returnItemIColl != null && returnItemIColl.size() > 0){
						for(int j=0;j<returnItemIColl.size();j++){
							KeyedCollection itemKColl = (KeyedCollection)returnItemIColl.get(j);
							String item_id = (String)itemKColl.getDataValue("item_id");
							String item_name = (String)itemKColl.getDataValue("item_name");
							String tag_type = (String)itemKColl.getDataValue("tag_type");
							String tag_attr = (String)itemKColl.getDataValue("tag_attr");
							String default_value = "";
							if(itemKColl.getDataValue("default_value")!=null){
								default_value = (String)itemKColl.getDataValue("default_value");
							}
							String msg = (String)itemKColl.getDataValue("msg");
							String url = (String)itemKColl.getDataValue("url");
							if(url == null){
								url = "";
							}
							if(url.indexOf("?") != -1){
								url = (url + "&EMP_SID=" + EMP_SID);
							}else if(url.trim().length() > 0){
								url = (url+"?EMP_SID="+EMP_SID);
							}else {
								url = "";
							}
							String url_desc = (String)itemKColl.getDataValue("url_desc");
							String is_null = (String)itemKColl.getDataValue("is_null");
							String is_judge = (String)itemKColl.getDataValue("is_judge");
							String rule = (String)itemKColl.getDataValue("rule");
							String is_need_event =  (String)itemKColl.getDataValue("is_need_event");
							String event_type =  (String)itemKColl.getDataValue("event_type");
							String hpp_cond =  (String)itemKColl.getDataValue("hpp_cond");
							String imp_item_id =  (String)itemKColl.getDataValue("imp_item_id");
							String is_hidden =  (String)itemKColl.getDataValue("is_hidden");
							
							/** --------------获取规则值------------- */
							String ruleCode = "";
							String ruleMsg = "";
							if(is_judge.equals("1") && rule != null && rule.trim().length() > 0){
								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			    				ShuffleServiceInterface shuffleService = null;
			    				try {
			    					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			    				} catch (Exception e) {
			    					EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			    					throw new EMPException(e);
			    				}
			    				/**规则输入参数封装*/
	    						Map inputValueMap=new HashMap();
	    			    		inputValueMap.put("IN_客户码", cus_id);
	    			    		/**执行规则检查，获得规则检查结果  */
	    			    		Map<String, String> returnMap = new HashMap<String, String>();
	    			    		returnMap=shuffleService.fireTargetRule(rule.split("_")[0],rule.split("_")[1] , inputValueMap);	
	    			    		ruleCode = returnMap.get("OUT_返回码");
	    			    		ruleMsg = returnMap.get("OUT_提示信息");
							}
							
							/** --------------获取录入值------------- */
							String inValue = "";
							if(returnKColl.containsKey(item_id)){
								inValue = (String)returnKColl.getDataValue(item_id);
								if(inValue == null){
									inValue = "";
								}
							}
							/** --------------获取显示值------------- */
							String defValue = default_value;
							if(ruleCode != null && ruleCode.trim().length() > 0){
								defValue = ruleCode;
							}
							if(inValue != null && inValue.trim().length() > 0){
								defValue = inValue;
							}
							/** --------------获取字典项------------- */
							IndexedCollection dicIColl = null;
							if(tag_attr != null && tag_attr.trim().length() > 0){
								KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
								dicIColl = (IndexedCollection)dictColl.getDataElement(tag_attr);
							}
							
							/** --------------判断标签必输------------- */
							boolean must = false;
							if(is_null.equals("1")){
								must = true;
							}
							
							boolean event = false;//判断是否增加事件
							if(is_need_event.equals("1")){
								event = true;
							}
							
							boolean hidden = false;//判断是否隐藏
							if(is_hidden.equals("1")){
								hidden = true;
							}
							
							/** ----------------------------------------------判断标签类型-------------------------------------- */
								/** --------------文本框标签------------- */
							if(tag_type.equals("10")){
							%>
							
							<tr>
								<% 
									if(url != null && url.trim().length() > 0){
										if(url_desc != null && url_desc.trim().length() > 0){
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %><a href="<%=url%>" target="_blank"><font color="blue" ><%=url_desc %></font></a>
								</div></td>
								<%			
										}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<a href="<%=url%>" target="_blank"><font color="blue" ><%=item_name %></font></a>
								</div></td>
								<%			
										}
									}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %>
								</div></td>
								<%		
									}
								%>
								<td class='emp_field_td' colspan='3'>
								<div id="PspCheckItemResult.<%=item_id %>_valdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
									<span id='emp_field_PspCheckItemResult.<%=item_id %>' title='<%=item_name %>' type='Text' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='<%=must %>' hidden='true' colSpan='1' readonly='false' onlyControlElement='false' value=''  maxlength='40' rendered='false'>
										<input id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' readonly="readonly" onblur="checkValueMsg(this.id,this.lang);" value='<%=defValue %>' lang="<%=must %>" maxlength='40' class='emp_field_text_input'
										<%
										if(event){
											if(event_type.equals("01")){//隐藏事件
												%>
												onchange="doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>')" 
												<%
											}
										}
										%>
										/>
									</span>
									<span id='MSG.<%=item_id %>'>
									<% 
									if(must&&!hidden){
									%>
									<font color='red' >*</font>
									<%	
									}
									%>
									</span>
									</div>
								</td>
							</tr>
							<%
							}else if(tag_type.equals("20")){
								/** --------------单选框标签------------- */	
							%>
							<tr>
								<% 
									if(url != null && url.trim().length() > 0){
										if(url_desc != null && url_desc.trim().length() > 0){
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %><a href="<%=url%>" target="_blank"><font color="blue" ><%=url_desc %></font></a>
								</div></td>
								<%			
										}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<a href="<%=url%>" target="_blank"><font color="blue" ><%=item_name %></font></a>
								</div></td>
								<%			
										}
									}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %>
								</div></td>
								<%		
									}
								%>
								<td class='emp_field_td' colspan='3'>
								<div id="PspCheckItemResult.<%=item_id %>_valdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
									<span id='emp_field_PspCheckItemResult.<%=item_id %>' title='<%=item_name %>' type='Radio' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='<%=must %>' colSpan='1' readonly='false' onlyControlElement='false' dictname='STD_ZB_BUSILINE' value=''  rendered='false'>
									<% 
										if(dicIColl != null && dicIColl.size() > 0){
											for(int k=0;k<dicIColl.size();k++){
												KeyedCollection dicKColl = (KeyedCollection)dicIColl.get(k);
												String enname = (String)dicKColl.getDataValue("enname");
												String cnname = (String)dicKColl.getDataValue("cnname");
												if(defValue.trim().length() > 0 && defValue.equals(enname)){
												%>
													<input id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' disabled="disabled" onblur="checkValueMsg(this.id,this.lang);" lang="<%=must %>" checked="checked" type='radio' value='<%=enname %>' class='emp_field_checkbox_input'
													<%
														if(event){
															if(event_type.equals("01")){//隐藏事件
													%>
													 onclick="doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>')" 
													<%
															}
														}
													%>
													/><%=cnname %>
												<%
												}else {
												%>
													<input id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' disabled="disabled" onblur="checkValueMsg(this.id,this.lang);" lang="<%=must %>" type='radio' value='<%=enname %>' class='emp_field_checkbox_input'
													<%
														if(event){
															if(event_type.equals("01")){//隐藏事件
													%>
													 onclick="doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>')" 
													<%
															}
														}
													%>
													/><%=cnname %>
												<%	
												}
											}
										}
									%>
									</span>
									<span id='MSG.<%=item_id %>'>
									<% 
									if(must&&!hidden){
									%>
									<font color='red' >*</font>
									<%	
									}
									%>
									</span>
									</div>
								</td>
							</tr>
							<%
							}else if(tag_type.equals("30")){
								/** --------------多选框标签------------- */	
							%>
							<tr>
								<% 
									if(url != null && url.trim().length() > 0){
										if(url_desc != null && url_desc.trim().length() > 0){
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %><a href="<%=url%>" target="_blank"><font color="blue" ><%=url_desc %></font></a>
								</div></td>
								<%			
										}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<a href="<%=url%>" target="_blank"><font color="blue" ><%=item_name %></font></a>
								</div></td>
								<%			
										}
									}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %>
								</div></td>
								<%		
									}
								%>
								<td class='emp_field_td' colspan='3'>
								<div id="PspCheckItemResult.<%=item_id %>_valdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
									<span id='emp_field_PspCheckItemResult.<%=item_id %>' title='<%=item_name %>' type='CheckBox' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='<%=must %>' colSpan='1' readonly='false' onlyControlElement='false' dictname='STD_ZB_BUSILINE' value=''  rendered='false'>
									<% 
										if(dicIColl != null && dicIColl.size() > 0){
											for(int k=0;k<dicIColl.size();k++){
												KeyedCollection dicKColl = (KeyedCollection)dicIColl.get(k);
												String enname = (String)dicKColl.getDataValue("enname");
												String cnname = (String)dicKColl.getDataValue("cnname");
												
												if(defValue.trim().length() > 0 ){
													String arr[] = inValue.split(",");
													boolean flag = false;
													for(int l=0;l<arr.length;l++){
														if(arr[l].equals(enname)){
															flag = true;
														}
													}
													if(flag){
													%>
														<input id='PspCheckItemResult.<%=item_id %>_displayname' name='PspCheckItemResult.<%=item_id %>_displayname' disabled="disabled" checked="checked" onclick="checkValue(this.id);checkValueMsg(this.id,this.lang);
															<%
															if(event){
																if(event_type.equals("01")){//隐藏事件
															%>
															 doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>');
															<%
																}
															}
															%>
															" lang="<%=must %>" type='checkbox'  value='<%=enname %>' class='emp_field_checkbox_input'/><%=cnname %>
													<%
													}else {
													%>
														<input id='PspCheckItemResult.<%=item_id %>_displayname' name='PspCheckItemResult.<%=item_id %>_displayname' disabled="disabled" onclick="checkValue(this.id);checkValueMsg(this.id,this.lang);
															<%
															if(event){
																if(event_type.equals("01")){//隐藏事件
															%>
															 doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>');
															<%
																}
															}
															%>
															" lang="<%=must %>" type='checkbox' value='<%=enname %>' class='emp_field_checkbox_input'/><%=cnname %>
													<%
													}
												}else {
												%>
													<input id='PspCheckItemResult.<%=item_id %>_displayname' name='PspCheckItemResult.<%=item_id %>_displayname' disabled="disabled" onclick="checkValue(this.id);checkValueMsg(this.id,this.lang);
														<%
														if(event){
															if(event_type.equals("01")){//隐藏事件
														%>
														 doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>'); 
														<%
																}
															}
														%>
														" lang="<%=must %>" type='checkbox'  value='<%=enname %>' class='emp_field_checkbox_input'/><%=cnname %>
												<%	
												}
											}
										}
									%>
									<input id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' lang="<%=must %>"  type=hidden value='<%=defValue %>' class='emp_field_checkbox_input'/>
									</span>
									<span id='MSG.<%=item_id %>'>
									<% 
									if(must&&!hidden){
									%>
									<font color='red' >*</font>
									<%	
									}
									%>
									</span>
									</div>
								</td>
							</tr>
							<%	
							}else if(tag_type.equals("40")){
								/** --------------下拉框标签------------- */
							
							%>
							<tr>
								<% 
									if(url != null && url.trim().length() > 0){
										if(url_desc != null && url_desc.trim().length() > 0){
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %><a href="<%=url%>" target="_blank"><font color="blue" ><%=url_desc %></font></a>
								</div></td>
								<%			
										}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<a href="<%=url%>" target="_blank"><font color="blue" ><%=item_name %></font></a>
								</div></td>
								<%			
										}
									}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %>
								</div></td>
								<%		
									}
								%>
								<td class='emp_field_td' colspan='3'>
								<div id="PspCheckItemResult.<%=item_id %>_valdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
									<span id='emp_field_PspCheckItemResult.<%=item_id %>' title='<%=item_name %>' type='Select' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='<%=must %>' colSpan='1' readonly='false' onlyControlElement='false' dictname='STD_ZX_YES_NO' value=''  defMsg='-----请选择-----' rendered='false'>
										<select id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' disabled="disabled" onblur="checkValueMsg(this.id,this.lang);" lang="<%=must %>" value='<%=defValue %>' class='emp_field_select_select'
											<%
												if(event){
													if(event_type.equals("01")){//隐藏事件
											%>
											 onchange="doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>')" 
											<%
													}
												}
											%>
											>
											<option value=''>-----请选择-----</option>
											<% 
												if(dicIColl != null && dicIColl.size() > 0){
													for(int k=0;k<dicIColl.size();k++){
														KeyedCollection dicKColl = (KeyedCollection)dicIColl.get(k);
														String enname = (String)dicKColl.getDataValue("enname");
														String cnname = (String)dicKColl.getDataValue("cnname");
														if(defValue.trim().length() > 0 && defValue.equals(enname)){
														%>
															<option value='<%=enname %>' selected="selected"><%=cnname %></option>
														<%
														}else {
														%>
															<option value='<%=enname %>'><%=cnname %></option>
														<%	
														}
													}
												}
											%>
										</select>
									</span>
									<span id='MSG.<%=item_id %>'>
									<% 
									if(must&&!hidden){
									%>
									<font color='red' >*</font>
									<%	
									}
									%>
									</span>
									</div>
								</td>
							</tr>	
							<%
							}else if(tag_type.equals("50")){
								/** --------------文本域标签------------- */
							%>
							<tr>
								<% 
									if(url != null && url.trim().length() > 0){
										if(url_desc != null && url_desc.trim().length() > 0){
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %><a href="<%=url%>" target="_blank"><font color="blue" ><%=url_desc %></font></a>
								</div></td>
								<%			
										}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<a href="<%=url%>" target="_blank"><font color="blue" ><%=item_name %></font></a>
								</div></td>
								<%			
										}
									}else {
								%>
								<td class='emp_field_label' title="<%=msg %>">
								<div id="PspCheckItemResult.<%=item_id %>_textdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<%=item_name %>
								</div></td>
								<%		
									}
								%>
								<td class='emp_field_td' colspan='1'>
								<div id="PspCheckItemResult.<%=item_id %>_valdiv"
								<%if(hidden){ %>
							 	 style="display:none"
								<%} %> 
								>
								<span id='emp_field_PspCheckItemResult.<%=item_id %>' title='<%=item_name %>' type='TextArea' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='false' colSpan='1' readonly='false' onlyControlElement='false' value=''  maxlength='200' rendered='false'>
									<textarea id='PspCheckItemResult.<%=item_id %>' name='PspCheckItemResult.<%=item_id %>' readonly="readonly" onblur="checkValueMsg(this.id,this.lang);" lang="<%=must %>" class='emp_field_textarea_textarea'
											<%
												if(event){
													if(event_type.equals("01")){//隐藏事件
											%>
											 onchange="doEvent(this.id,this.lang,this.value,'<%=hpp_cond %>','<%=imp_item_id %>')" 
											<%
													}
												}
											%>
									><%=defValue %></textarea>
								</span>
								<span id='MSG.<%=item_id %>'>
									<% 
									if(must&&!hidden){
									%>
									<font color='red' >*</font>
									<%	
									}
									%>
									</span>
									</div>
							</tr>
							<%	
							}
						
						}
					}
				%>	
						</table>
					</div>
				<%			
					
					
				}
			}
			
		%>
		<%if(!op.equals("view")){ %>
			<div align="center">
				<br>
				<button id="button_submit" class="btn_mouseout" onmouseover="this.className='btn_mouseover'" onmouseout="this.className='btn_mouseout'" onmousedown="this.className='btn_mousedown'" onmouseup="this.className='btn_mouseup'" onclick="doSave()">确定</button>
				<button id="button_reset" class="btn_mouseout" onmouseover="this.className='btn_mouseover'" onmouseout="this.className='btn_mouseout'" onmousedown="this.className='btn_mousedown'" onmouseup="this.className='btn_mouseup'" onclick="doResert()">重置</button>
			</div>
		<%} %>
		</DIV>
		<input type="hidden" name="EMP_SID" value="<%=EMP_SID %>"/>
		</form>
</body>
</html>
</emp:page>
    