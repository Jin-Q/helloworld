<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="java.util.List"%>
<%@ page import="com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat"%>
<emp:page>

<html>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	List itemList = (List)context.getDataValue("itemList");
	String styleId = (String)context.getDataValue("style_id");
	String fnc_conf_type = (String)context.getDataValue("fnc_conf_typ");
	
	int lh = itemList.size();
	String strValue = "";
	for(int i=0;i<itemList.size();i++){
		FncConfDefFormat format = (FncConfDefFormat)itemList.get(i);
		strValue = strValue+"["+format.getItemId()+"]"+format.getItemName()+",";
	}

 %>
 
<head>
<jsp:include page="/include.jsp" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>
<style type="text/css">
._select {
border: 1px solid #b7b7b7;
text-align:left;
width:240px;
}
</style>
<script type="text/javascript"><!--
	var page = new EMP.util.Page();
	function doOnLoad() {
		setItems();
		closeParan();
		
		//检查公式1 加监听
		EMPTools.addEvent(FncConfDefFmt.fnc_conf_chk_frm._obj.element, "click", showCheckDialog, FncConfDefFmt.fnc_conf_chk_frm);
		//计算公式1 加监听
		EMPTools.addEvent(FncConfDefFmt.fnc_conf_cal_frm._obj.element, "click", showDialog, FncConfDefFmt.fnc_conf_cal_frm);
		
	};
	function showDialog() {
		features ="dialogHeight:650px;dialogWidth:720px;center:1;help:no;";
		gs = FncConfDefFmt.fnc_conf_cal_frm._obj.element.value;
		gs = replaceAll(gs,"+","$");
		//项目编辑方式
		typeStr = FncConfDefFmt.fnc_item_edit_typ._obj.element.value;
		if(typeStr==""){
			alert("请选择项目编辑方式！");
			FncConfDefFmt.fnc_item_edit_typ._obj.element.focus();
		}
		//rptType = "03";
		rptType = '<%=fnc_conf_type%>';
		//alert("*@@@rptType*"+rptType);
		if("01"==rptType || "02"==rptType){
			if("01"==rptType ){
				rptType = "1";
				}
			if("02"==rptType ){
				rptType = "2";
				}
		}	
		if(typeStr == "2"){
			  if(rptType == "1" || rptType == "2"||rptType == "01"||rptType == "02"){
				  var form = document.getElementById('hiddenForm');
		    	  FncConfDefFmt._toForm(form);
		    	  var url = "<emp:url action='showSelfformula.do'/>&rptType="+rptType;
		    	  //url = EMP.util.Tools.encodeURI(url);
				  toSubmitForm(form,url,gs,'fnc_conf_cal_frm');
				  
			  }else{
				  var form = document.getElementById('hiddenForm');
		    	  FncConfDefFmt._toForm(form);
			       var url = "<emp:url action='showFormula.do'/>&rptType="+rptType;
			      // url = EMPTools.encodeURI(url);
			       //var returnValue=window.showModalDialog(url,"",features);
			  	   //if(returnValue == undefined)
					 // return;
				   //FncConfDefFmt.fnc_conf_cal_frm._obj.element.value =  returnValue;
			       toSubmitForm(form,url,gs);
	          }
		}else{
		  return;
		}
	}
	function toSubmitForm(form,urlV,gs,fType){
		  var handleSuccess = function(o){ EMPTools.unmask();
				var returnValue=window.showModalDialog(urlV+"&gs="+gs+"&typeFg=1","",features);
			  	if(returnValue == undefined)
					  return;

				if(fType=='fnc_conf_chk_frm'){
					FncConfDefFmt.fnc_conf_chk_frm._obj.element.value = returnValue;
				}else{
						FncConfDefFmt.fnc_conf_cal_frm._obj.element.value = returnValue;
				}
				
			};
			var handleFailure = function(o){ EMPTools.unmask();	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',urlV+"&typeFg=0", callback,postData); 
		  }

function showCheckDialog() {
	features ="dialogHeight:650px;dialogWidth:720px;center:1;help:no;";
	gs = FncConfDefFmt.fnc_conf_chk_frm._obj.element.value;
	gs = replaceAll(gs,"+","$");
	//项目编辑方式
	var typeStr = FncConfDefFmt.fnc_item_edit_typ._obj.element.value;
	//var rptType = "01";
	var rptType = '<%=fnc_conf_type%>';
	//alert("???"+rptType)
	if(rptType=="1"){
		rptType = "01";
	}else if(rptType=="2"){
		rptType = "02";
	}else if(rptType=="3"){
		rptType = "03";
	}else if(rptType=="4"){
		rptType = "04";
	}else if(rptType=="5"){
		rptType = "05";
	}else if(rptType=="6"){
		rptType = "06";
	}
	//如果编辑项不是标题项
	if(typeStr != "3"){
	var url = "<emp:url action='showCheckFormula.do'/>&rptType="+rptType;
	var form = document.getElementById('hiddenForm');
	FncConfDefFmt._toForm(form);
	  toSubmitForm(form,url,gs,'fnc_conf_chk_frm');
	}else{
	  return;
	}
}
    function replaceAll(str,str1,str2){
	while(str.indexOf(str1)>=0)
		str=str.replace(str1,str2);
    
    return str;
}
	function setItems(){

		var sValue = '${context.style_id}'; 
		var iValue = '${context.item_id}'; 
		var styleId = '<%=styleId %>';
		var strValue = '<%=strValue%>';
		var lh = '<%=lh%>';

		//报表样式编号
		var styleOP = FncConfDefFmt.style_id._obj.element;
		//项目编号
		var itemsOP = FncConfDefFmt.item_id._obj.element;
		var itemOP = FncConfDefFmt.item_id._obj.element;
		
		var s = 1;
		var ss = 0;
		var j = 1;
		var jj = 0;
		
		if(sValue == styleId){
			styleOP.options[ss] = new Option();
			styleOP.options[ss].value = styleId;
			styleOP.options[ss].text = styleId;
			styleOP.options[ss].selected = true;
		}else{
			styleOP.options[s] = new Option();
			styleOP.options[s].value = styleId;
			styleOP.options[s].text = styleId;
		}
		var pp = strValue.indexOf(iValue);
		for(var i=0;i<lh;i++){
			
			var post = strValue.indexOf(",");
			var tempStr = strValue.substring(0,post);
			strValue = strValue.substring(post+1);
			
			var sPost = tempStr.indexOf("[");
			var ePost = tempStr.indexOf("]");
			
			if(pp>0){
				if(iValue == tempStr.substring(sPost+1,ePost)){
					itemOP.options[jj] = new Option();
					itemOP.options[jj].value = tempStr.substring(sPost+1,ePost);
					itemOP.options[jj].text = "["+tempStr.substring(sPost+1,ePost)+"]"+tempStr.substring(ePost+1);
					itemOP.options[jj].selected = true;
				}
			}else{
				itemsOP.options[j] = new Option();
				itemsOP.options[j].value = tempStr.substring(sPost+1,ePost);
				itemsOP.options[j].text = "["+tempStr.substring(sPost+1,ePost)+"]"+tempStr.substring(ePost+1);
				j++;
			}
			
			
		}
	}
	
		
	function doReset(){
		page.dataGroups.FncConfDefFmtGroup.reset();
	};
	 function doAddFncConfDefFmt(){
		 var flg = checkEditType();
		 if(flg){	
	        var form = document.getElementById('submitForm');
	        var result = FncConfDefFmt._checkAll(); 
	        if(result){
				FncConfDefFmt._toForm(form);
				var url="<emp:url action='addFncConfDefFormatRecord.do'/>";
				var handleSuccess = function(o){ EMPTools.unmask();
						var paramStr = FncConfDefFmt.style_id._obj.element.value;
						var sUrl = "<emp:url action='dealFncConfDefFormat.do'/>?style_id="+paramStr+"&fnc_conf_typ="+<%=fnc_conf_type%>+"&test="+'1';
						sUrl = EMP.util.Tools.encodeURI(sUrl);
						window.location = sUrl;
				};	
				var handleFailure = function(o){ EMPTools.unmask();
						alert("出现错误！");
						return false;
			    };	
				var callback = {
						success:handleSuccess,
						failure:handleFailure
				}; 
				var postData = YAHOO.util.Connect.setForm(submitForm);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
			}else{
				alert("请输入必填项！");
				return;
			}
			
			
		 }else{
			 FncConfDefFmt.fnc_conf_cal_frm._obj.element.focus();
		 }
    }

	    
    function doGetUpdateFncConfDefFormat(styleId,itemId){
    	var form = document.getElementById('submitForm');
        var result = FncConfDefFmt._checkAll();
        if(result){
			FncConfDefFmt._toForm(form);
		}
		
		var url="<emp:url action='getFncConfDefFormatUpdatePage.do'/>?style_id="+styleId+"&item_id="+itemId+"&fnc_conf_typ="+<%=fnc_conf_type%>+"&test="+'1';

		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
    }
    
    function doUpdateFncConfDefFmt(styleId,itemId){
    	var flg = checkEditType();
    	if(flg){
	        var form = document.getElementById('submitForm');
	        var result = FncConfDefFmt._checkAll();
	        if(result){
				FncConfDefFmt._toForm(form);
			}
			var url="<emp:url action='updateFncConfDefFormatRecord.do'/>";
			var handleSuccess = function(o){ EMPTools.unmask();
					var paramStr = FncConfDefFmt.style_id._obj.element.value;
					var sUrl = "<emp:url action='dealFncConfDefFormat.do'/>?style_id="+paramStr+"&fnc_conf_typ="+<%=fnc_conf_type%>+"&test="+'1';
					sUrl = EMP.util.Tools.encodeURI(sUrl);
					window.location = sUrl;
			};	
			var handleFailure = function(o){ EMPTools.unmask();
					alert("出现错误！");
					return false;
		    };	
			var callback = {
					success:handleSuccess,
					failure:handleFailure
			}; 
			var postData = YAHOO.util.Connect.setForm(submitForm);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
    	}else{
			 FncConfDefFmt.fnc_conf_cal_frm._obj.element.focus();
			 }
    }
    
    function doDeleteFncConfDefFmt(){
    	var styleId = FncConfDefFmt.style_id._obj.element.value;
		var itemId = FncConfDefFmt.item_id._obj.element.value;
        if(itemId!=null && itemId !=""){
        	var form = document.getElementById('submitForm');
            var result = FncConfDefFmt._checkAll();
            if(result){
    			FncConfDefFmt._toForm(form);
    		}
            if(confirm("是否确认要删除？")){
	    		var url="<emp:url action='deleteFncConfDefFormatRecord.do'/>&item_id="+itemId+"&style_id="+styleId+"&test="+'1';
	    		var handleSuccess = function(o){ EMPTools.unmask();
	    				var paramStr = FncConfDefFmt.style_id._obj.element.value;
	    				var sUrl = "<emp:url action='dealFncConfDefFormat.do'/>?style_id="+styleId+"&fnc_conf_typ="+<%=fnc_conf_type%>;
	    				sUrl = EMP.util.Tools.encodeURI(sUrl);
	    				window.location = sUrl;
	    		};	
	    		var handleFailure = function(o){ EMPTools.unmask();
	    				alert("出现错误！");
	    				return false;
	    	    };	
	    		var callback = {
	    				success:handleSuccess,
	    				failure:handleFailure
	    		}; 
	    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) ;
            }else{
	            	var paramStr = FncConfDefFmt.style_id._obj.element.value;
					var sUrl = "<emp:url action='dealFncConfDefFormat.do'/>?style_id="+styleId+"&fnc_conf_typ="+<%=fnc_conf_type%>;
					sUrl = EMP.util.Tools.encodeURI(sUrl);
					window.location = sUrl;
                }
        } else {
			alert('请先选择一个项目！');
		}
    }
    
    function switch_updown() {
   		if (mspan.title=="关闭上栏") {
      		  mspan.title="打开上栏";
      		  document.all("mtop").style.display="none";
      		  document.all("divID").style.display="none";
   		} else {
       		 mspan.title="关闭上栏";
        	document.all("mtop").style.display="";
        	document.all("divID").style.display="";
  		 }
	}
	
	function doNew(){
		document.all("newDiv").style.display="";//让"新增的保存"可见
		document.all("updateDiv").style.display="none";//让"修改的保存"不可见
		document.all("tolDiv").style.display="none";//导向按钮不可见
		document.all("divID").style.display="none";//
		document.getElementById('mtop').style.backgroundColor="";
		openParan();
	}
	function doUpdate(){
		var itemId = FncConfDefFmt.item_id._obj.element.value;
		if(itemId!=null && itemId !=""){
			document.all("newDiv").style.display="none";
			document.all("updateDiv").style.display="";
			document.all("tolDiv").style.display="none";
			document.all("divID").style.display="none";
			document.getElementById('mtop').style.backgroundColor="";
			openParan();
		}else {
			alert('请先选择一个项目！');
		}
	}
	function closeParan(){
      	if(document.getElementById('divID').style.display==''){
      		document.getElementById('mtop').style.backgroundColor="#fff";	
        }	      	
      }
    function openParan(){	
        document.all("divID").style.display="none";
		document.getElementById('mtop').style.backgroundColor="";	
    }
    function doReturn(){
    	var styleId = FncConfDefFmt.style_id._obj.element.value;
    	var sUrl = "<emp:url action='queryFncConfDefFormatList.do'/>?style_id="+styleId;//dealFncConfDefFormat.do
		sUrl = EMP.util.Tools.encodeURI(sUrl);
		window.location = sUrl;
    }

    function checkEditType(){
        var flg = "true";
        var fnc_item_edit_typ = FncConfDefFmt.fnc_item_edit_typ._obj.element.value;
        var fnc_conf_cal_frm = FncConfDefFmt.fnc_conf_cal_frm._obj.element.value;
        if(fnc_item_edit_typ=='2'){
            if(fnc_conf_cal_frm==""){
                alert('请输入计算公式！');
                FncConfDefFmt.fnc_conf_cal_frm._obj.element.focus();
                flg = false;
            }
        }
        return flg;
    }
--></script>
</head>

<body onload="doOnLoad()">
	<form id="submitForm" method="POST">
	</form>
	<div id="mtop" >
		<div id="FncConfDefFmtGroup" class="emp_group_div">
			<emp:gridLayout id="FncConfDefFmtGroup" maxColumn="2" title="报表配置定义表" >
				<emp:select id="FncConfDefFmt.style_id" label="报表样式编号" required="true" hidden="true"/>
				<emp:select id="FncConfDefFmt.item_id" label="项目编号" required="true" cssElementClass="_select"/>
				<emp:text id="FncConfDefFmt.fnc_conf_order" label="顺序编号" maxlength="10" required="true" />
				<emp:text id="FncConfDefFmt.fnc_conf_cotes" label="栏位" maxlength="38" required="true" />
				<emp:select id="FncConfDefFmt.fnc_conf_row_flg" label="行次标识" required="true" dictname="STD_ZB_FNCCONFROW" />
				<emp:text id="FncConfDefFmt.fnc_conf_indent" label="层次" maxlength="38" required="false" />
				<emp:text id="FncConfDefFmt.fnc_conf_prefix" label="前缀" maxlength="20" required="false" colSpan="2" />
				<emp:select id="FncConfDefFmt.fnc_item_edit_typ" label="项目编辑方式" required="true" dictname="STD_ZB_FNCITEMEDT" />
				<emp:text id="FncConfDefFmt.fnc_conf_disp_amt" label="显示数值" maxlength="16" required="false" />
				<emp:text id="FncConfDefFmt.fnc_cnf_app_row" label="追加行数" maxlength="38" required="false" />
				<emp:text id="FncConfDefFmt.fnc_conf_disp_tpy" label="默认现实类型" maxlength="2" required="false" hidden="true"/>
				<emp:textarea id="FncConfDefFmt.fnc_conf_chk_frm" label="检查公式"  required="false" colSpan="2" />
				<emp:textarea id="FncConfDefFmt.fnc_conf_cal_frm" label="计算公式"  required="false" colSpan="2" />
			</emp:gridLayout>
		</div>
		<div id="newDiv" align="center" style="display:none">
			<br>
			<emp:button id="addFncConfDefFmt" label="新增-保存"/>
			<emp:button id="return" label="返回"/>
		</div>
		<div id="updateDiv" align="center" style="display:none">
			<br>
			<emp:button id="updateFncConfDefFmt" label="修改-保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
		<div id="tolDiv" align="center">
			<br>
			<emp:button id="new" label="新增" locked="false"/>
			<emp:button id="update" label="修改" locked="false"/>
			<emp:button id="deleteFncConfDefFmt" label="删除" locked="false"/>
		</div>
		<div id="divID" style="z-index: 2;">
			<div style="z-index:3;position:absolute;left: 0px;top: 0px;width=862;height=100;">
			</div>
			<div style="z-index:4;position:absolute;left: 250px;top: 200px;">
			</div>
		</div>
	</div>
	<div align="center" style="width:100%; height:1px">
		<table width="101%" height="100%">
			<tr>
    			<td height="3" align="center" bgcolor="#C70505" style="cursor:hand" title="关闭上栏" id="mspan" onClick="switch_updown()"></td>
  			</tr>
		</table>
	</div>
	<div align="center">
		<emp:fnc id="formatList"/>
    </div>
    
    <form id="hiddenForm" method="post" style="display:none">
    	<emp:textarea id="FncConfDefFmt.fnc_conf_cal_frm" label="计算公式" colSpan="2" hidden="true" required="false" defvalue='${context.FncConfDefFmt.fnc_conf_cal_frm}'/>
    	<emp:textarea id="FncConfDefFmt.fnc_conf_chk_frm" label="检查公式"  required="false" hidden="true" colSpan="2" defvalue='${context.FncConfDefFmt.fnc_conf_chk_frm}'/>
    </form>

</body>
</html>
</emp:page>
