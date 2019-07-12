<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.organization.domains.SOrg"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
SOrg rootOrg = (SOrg)context.getDataValue("rootOrg");
String count=request.getParameter("count");//1单选；n多选
%>

<emp:page>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/include.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/scripts/workflow/xtree/css/xtree2.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/jquery-1.4.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/xtree/js/xtree2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/xtree/js/xloadtree2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/forum-search.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/selectuser.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/common.js"></script>

<style type='text/css'>
#search_id{font-size:12px; color:#333333}
#search_id div{ float:left}
#search_body{ border:1px solid #999999; padding-top:0px; width:210px; }
#search_body input{ height:18px; margin-top:0px; width:100%;}
#search_body #searchkey_id{ width:210px; border:1px; border-bottom:1px solid #999999;}
#result_id{ padding-top:0px; width:100%;display:none;}
#result_id table{ width:100%; }
#result_id table tr td:hover{background-color:#208FD8; cursor:pointer }
#linkman_body{ border:1px solid #999999; padding-top:0px; width:325px; }
</style>
<script type="text/javascript">
var cout="<%=count %>";
function retSuc(){
	var retObj = [];
	retObj[0] = true;
	var userid = null;
	var username = null;
	var attribute= document.form1.formid;
	for (var i = 0; i < attribute.length; i++){
		if(userid==null){
			userid= attribute.options[i].value;
			username= attribute.options[i].text;
		}
	    else{
			userid=userid+";"+attribute.options[i].value;
			username=username+";"+attribute.options[i].text;
		}
	}
	if(userid==null||userid==""){
		alert("您没有选择用户");
		return;
	}
	retObj[1] = userid;
	retObj[2] = username;
	window.returnValue = retObj;
	window.close();
};
	
function retFail(){
	var retObj = [];
	retObj[0] = false;
	window.returnValue = retObj;
	window.close();
};

function move(side){
	var temp1 = new Array();
	var tempa = new Array();
	var current1 = 0;
	var y=0;
	attribute1 = document.form1.PrepareSelected;
    attribute2 = document.form1.formid;
	if (side == "in"){
		for (var i = 0; i < attribute2.length; i++){
	 		y=current1++;
	 		temp1[y] = attribute2.options[i].value;
	 		tempa[y] = attribute2.options[i].text;
        }		
	 	for (var i = 0; i < attribute1.length; i++){  
	        if(cout=='1'){
				if(attribute1.options[i].selected){
					temp1 = new Array();
					tempa = new Array();
					temp1[0]=attribute1.options[i].value;
					tempa[0]=attribute1.options[i].text;
					break;
				}
	        }else{
				var m=1;
				for(var j=0;j<temp1.length;j++){
					if(!attribute1.options[i].selected||attribute1.options[i].value==temp1[j] ){
						 m=0;
						break;
					}
				}
				if(m==1&&attribute1.options[i].selected){
					y=current1++;
					temp1[y] = attribute1.options[i].value;
					tempa[y] = attribute1.options[i].text;
				}
	       }
	    }
	}else{
		for (var i = 0; i < attribute2.length; i++){
		   if(!attribute2.options[i].selected ){
				y=current1++;
				temp1[y] = attribute2.options[i].value;
				tempa[y] = attribute2.options[i].text;
		   }
	    }
		attribute2.length=0;
	}
	for (var i = 0; i < temp1.length; i++){
	 	  attribute2.options[i] = new Option();
	 	  attribute2.options[i].value = temp1[i];
	 	  attribute2.options[i].text =  tempa[i];
	}
}

function getUser(id){
	var handleSuccess = function(o){
		if(o.responseText != undefined){
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert(o.responseText);
				return;
			}
			var returnData = jsonstr.users;
			var attribute=document.form1.PrepareSelected;
			attribute.length=0;
		    for(var i=0;i<returnData.length;i++){
			    attribute.options[i] = new Option();
			    attribute.options[i].value=returnData[i].actorno;
			    attribute.options[i].text=returnData[i].actorname;
		    }
		}
	};
	var handleFailure = function(o){
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};
	var postData = null;
	var url='getUserByOrg4WF.do?getyhOrgid='+id+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	var obj1 = YAHOO.util.Connect.asyncRequest('get', url, callback, postData);
	
}

//去后台取匹配的用户信息，使用同步，否则展示的下拉框会有延迟并在数据量
//较大情况下展示的是上一次查询结果
/*
function doSearchUser(){
	alert('doSearchUser');
	var name = document.getElementById("searchkey_id").value;
	$.ajax( {
		type:"post",
		url : '<%=request.getContextPath()%>/getSearchUsers.do?actionType=search&EMP_SID=<%=request.getParameter("EMP_SID")%>',
		data : {
			userName : name
		},
		error : function() {
			//alert("error occured!!!");
		},
		success : function(data) {
			var html = data.resultStr;
			doSearchOptions(html);
		},
		async: false
	});
}
*/

//动态生成选用户下拉框
/*
function doSearchOptions(data){
	var close = false;
		$("#searchkey_id").keyup(function() {
			var nkey = $.trim($(this).val());
			if ("" == nkey || null == nkey) {
				$("#result_id").hide();
			} else {
				//data 为ajax请求 返回的 字符串
				var html = data;
				$("#result_id").show();
				$("#result_id").html(html);
				$("#result_id table tr td").click(function() {
					$("#searchkey_id").val($(this).html());
					$("#result_id").hide();
				}).hover(function() {
						close = false;
					}, function() {
						close = false;
					});
				;
			}
		}).blur(function() {
			if (close) {
				$("#result_id").hide();
			}
		});
}
*/

function doSubmit(){
	//var name_value = document.getElementById("searchkey_id").value;
	
	var name_userid_value = globalComb.getValue();

	//check data is valid
	if(name_userid_value.indexOf(" ")<0 || name_userid_value.indexOf("/") <0 ){
		return;
	}
	
	var name_ = name_userid_value.split(" ")[0];
	var userid_value=name_userid_value.split(" ")[1].split("/")[0];
	var orgname_=name_userid_value.split(" ")[1].split("/")[1];
	
	var name_value = globalComb.map.get(userid_value);

	if(name_value == null){
		return;
	}
	
	if(name_value!=null){
		//var text = name_+"_"+userid_value;
		var text = name_;
		var value = name_value;
		var temp1 = new Array();
		var tempa = new Array();
		var current1 = 0;
		var y=0;
	    attribute2 = document.form1.formid;
		for (var i = 0; i < attribute2.length; i++){
	 		y=current1++;
	 		temp1[y] = attribute2.options[i].value;
	 		tempa[y] = attribute2.options[i].text;
        }		
        if(cout=="1"){
 			temp1[0]=value;
			tempa[0]=text;
        }else{
        	var m=1;
        	for(var j=0;j<temp1.length;j++){
				if(value==temp1[j] ){
					 m=0;
					break;
				}
			}
        	
			if(m==1){
				y=current1++;
				temp1[y] = value;
				tempa[y] = text;
			}
        }
        
	 	for (var i = 0; i < temp1.length; i++){
			  attribute2.options[i] = new Option();
		 	  attribute2.options[i].value = temp1[i];
		 	  attribute2.options[i].text =  tempa[i];
		}
	}
	globalComb.clearValue();
}

Ext.onReady(function(){
	var url_ = '<%=request.getContextPath()%>/getSearchUsers.do?actionType=init&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	initpage(url_);
});

</script>
<body>

<table class=tablemain2 cellspacing=0 cellpadding=0 border=0 align="center">
<tr>
<td class=trtitle width="15%">用户查询</td>
<td width="35%">
<!-- 
<div id="search_body" style="display:none">
	<input name="searchkey" type="text" id="searchkey_id" size="10" onpropertychange="doSearchUser()" />
	<div id="result_id">
	</div>
</div>
 -->
<div style="display:block">
	<div id="selUsers"></div>
</div>

</td>
<td align="left">
	<input type="button" class="button" value="确定" onclick="doSubmit()"/>
</td>
</tr>
</table>
<!-- 左侧菜单+主区域 -->
<table width="95%" border="1" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td width="200" align="center" valign="top">
			<div id="leftMenuBar" style="height:350px; width:100%; overflow: auto; text-align: left;">
				<script type="text/javascript">
	  //xtree
	    var path='<%=request.getContextPath()%>/scripts/workflow/xtree/images/';
      	webFXTreeConfig.rootIcon        =path+"folder.png",
	    webFXTreeConfig.openRootIcon    =path+"openfolder.png",
	    webFXTreeConfig.folderIcon      = path+"folder.png",
	    webFXTreeConfig.openFolderIcon  = path+"openfolder.png",
	    webFXTreeConfig.fileIcon        = path+"dot.png",
	    webFXTreeConfig.iIcon           = path+"I.png",
	    webFXTreeConfig.lIcon           = path+"L.png",
	    webFXTreeConfig.lMinusIcon      = path+"Lminus.png",
	    webFXTreeConfig.lPlusIcon       = path+"Lplus.png",
	    webFXTreeConfig.tIcon           = path+"T.png",
	    webFXTreeConfig.tMinusIcon      = path+"Tminus.png",
	    webFXTreeConfig.tPlusIcon       = path+"Tplus.png",
	    webFXTreeConfig.plusIcon        = path+"plus.png",
	    webFXTreeConfig.minusIcon       = path+"minus.png",
	    webFXTreeConfig.blankIcon       = path+"blank.png",
        webFXTreeConfig.loadingIcon     =path+"loading.gif";
        var tree = new WebFXTree("root");
<%    
		out.print("tree.add(new WebFXLoadTreeItem('"+rootOrg.getOrganname()+"','"+request.getContextPath()+"/getSubOrg4Xtree.do?getOrgId="+rootOrg.getOrganno()+"&EMP_SID="+request.getParameter("EMP_SID")+"',\"javascript:getUser('"+rootOrg.getOrganno()+"');\"));");	 
%>;
	     tree.indentWidth = 19;
         tree.open = true;//节点打开状态，true为打开
         tree._selectedItem = null;
         tree._fireChange = true;
         tree.rendered = false;
         tree.suspendRedraw = false;
         tree.showLines = true;//连线显示状态，true为显示
         tree.showExpandIcons = true;//扩展和收缩图标显示状态，true为显示
         tree.showRootNode = false;//根节点显示状态，true为显示
         tree.showRootLines = false;//根节点连线显示状态，true为显示
         tree.write();
				</script>
			</div>
		</td>

		<!-- 右侧的主工作区域 -->
		<td align="left" valign="top"  bgcolor="#ffffff">
		<form NAME="form1">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						<td width="45%" align="center" valign="top">
						<fieldset><legend align="center" style="font-size:9pt">可选用户</legend>
								<select name='PrepareSelected' multiple size='12' style='height:320px;width:100%' onDblclick="move('in');"></select>
						</fieldset>
						</td>
						<td width="10%" align="center">
						    <!-- <input name="addlinkman" type="button" title="添加至常用联系人" class="button" value="∧" onclick="add();"><br><br> -->
							<input name="save2" type="button" class="button" value=">>>" onclick="move('in');"><br><br>
							<input name="save22" type="button" class="button" value="<<<" onclick="move('out');">
						</td>
						<td width="45%" align="center" valign="top">
						<fieldset><legend align="center" style="font-size:9pt">已选用户</legend>
							<select name="formid" multiple size="12" style="height:320px;width:100%" onDblclick="move('out');"></select>
						</fieldset>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
</table>
<br>
<center>
	<input type="button" class="button" value="确　　定" onclick="retSuc()">&nbsp;&nbsp; 
	<input type="button" class="button" value="取　　消" onclick="retFail()">
</center>
</body>
</html>
</emp:page>