<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFINodeVO"%>
<%
/**
1.满足节点流向类型为一般类型与并行的两种情况；
2.满足办理人员为单人与多人的两种情况；
节点流向类型为并行时，选择节点办理人时还是一个一个单独选择，最后在提交时显示出总的下一步骤及办理人选择的结果视图，并提示确认信息
*/
%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	List<WFINodeVO> wfNodeList = null;
	String noderoutertype = null;
	try {
		wfNodeList = (List<WFINodeVO>) context.getDataValue(WorkFlowConstance.WF_NEXT_NODE_LIST);
		noderoutertype = (String) context.getDataValue(WorkFlowConstance.NODE_PROPERTY_ROUTERTYPE);
	} catch(Exception e) {
		e.printStackTrace();
		out.println("<center>提交出错，请联系管理员！错误信息："+e.getMessage()+"</center>");
		return;
	}
	String curNodeName = (String)context.getDataValue("CurrentNodeName");
	String curOrgName = (String)context.getDataValue(CMISConstance.ATTR_ORGNAME);
	String curUserName = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME);
	//String curUserId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
	//String curOrgId = (String)context.getDataValue(CMISConstance.ATTR_ORGID);
%>

<emp:page>
<HTML>
<HEAD>

<TITLE>下一步骤、办理人选择</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<jsp:include page="/include.jsp" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/forum-search.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/jquery-1.4.4.js"></script>


<script type="text/javascript">

var globalComb=null;
var usersCount=null;
var loadFirstInit=null;
var loadMoreInit = null;
var checkNodeObj;
var criticalValue = 5; //如果总条数小于等于此值,生成HTML
//EXT 动态计算某角色下人员
function ready(){
	try{
		//数据准备
		var ds = new Ext.data.Store({
		    proxy: new Ext.data.HttpProxy({
		    	url: '<%=request.getContextPath()%>/getNodeUserList.do?rd='+Math.random()
		    }),
		    reader: new Ext.data.JsonReader({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        id: 'userid'
		    }, [
		        {name: 'userid', mapping: 'userId'},
	            {name: 'username', mapping: 'userName'},
	            {name: 'userismu', mapping: 'userIsmu'},
	            {name: 'orgid', mapping: 'orgId'},
	            {name: 'orgname', mapping: 'orgName'}
		    ]),
		    baseParams : {  EMP_SID:'<%=request.getParameter("EMP_SID")%>',
		    				instanceId:'<%=request.getParameter("instanceId")%>',
		    				nodeId:'<%=request.getParameter("nodeId")%>'
            }
		});
		Ext.QuickTips.init();
		var comb = new Ext.form.ComboBox
	    ({
	    	tpl: '<tpl for="."><div ext:qtip="{username} {userid}/{orgname}" class="x-combo-list-item">{username} {userid}/{orgname}</div></tpl>',
	        id:"ComboBox_selUser",
	        fieldLabel:'username',
	        editable:true,//默认为true，false为禁止手写和联想功能
	        store:ds,
	        emptyText:'请下拉选择或输入姓名,拼音查询',
	        mode: 'remote',//指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载则为remote.默认值为：remote
	        triggerAction: 'all',
	        valueField:'userid', 
	        displayField:'username',
	        pageSize:10,
	        selectOnFocus:true,
	        renderTo:'selUsers',
	        width:240,
	        typeAhead:false,
	        frame:true,
	        resizable:true,
	        minChars : 0, //查询框的默认数目
	        queryDelay:300,//查询框输入完成半秒后,自动提交查询
	        map:new JHashMap(),
	        onSelect: function(record){
		        //判断是多选还是单选,单选的话,清除原数据,多选的话,append数据
		        if(record.data.userismu=="true"){ //多选
			        if(!validateInput(this.getValue().split(";"),record.data.username+"("+record.data.userid+")")){
			        	this.setValue(this.getValue()+record.data.username+"("+record.data.userid+")"+";");
			        	if(!this.map.containsKey(record.data.userid)){
			        		this.map.put(record.data.userid,record.data.userid);
				        }
					}
				}else{ //单选
			        this.setValue(record.data.username+"("+record.data.userid+")"+";");
			        this.map.clear();
			        this.map.put(record.data.userid,record.data.userid);
				}
				this.collapse();
				
				var tmp = this.getValue();
				var tmpOb_ = tmp.split(";");
				var usertmp = null;
				var usernametmp = null;
				for(var m=0;m<tmpOb_.length-1;m++){
					var useridCo = tmpOb_[m].substr(tmpOb_[m].indexOf("(")+1,tmpOb_[m].indexOf(")")-tmpOb_[m].indexOf("(")-1);
					var usernameCo = tmpOb_[m].substr(0,tmpOb_[m].indexOf("("));
					if(usertmp == null){
						usertmp = globalComb.map.get(useridCo)+";";
						usernametmp = usernameCo+";";
					}else{
						usertmp = usertmp+globalComb.map.get(useridCo)+";";
						usernametmp = usernametmp+usernameCo+";";
					}
				}
		        var innerHtml="";
				innerHtml += "<input type='text' id='"+checkNodeObj.value+"_username' name='"+checkNodeObj.value+"_username' value='"+usernametmp+"' readonly=true style='width:300px' onclick=\"showExtDiv('"+checkNodeObj.value+"')\" title=\"点击重新选择\"/>";
				innerHtml += "<input type='hidden' id='"+checkNodeObj.value+"_userid' name='"+checkNodeObj.value+"_userid' value='"+usertmp+"'/>";
				document.getElementById(checkNodeObj.value+"_useridDiv").innerHTML=innerHtml;
				document.getElementById(checkNodeObj.value+"_useridDiv").style.display='none';//在comb失去焦点时再显示
	    	}
	    });
		//初始化的时候隐藏
	    comb.hide();
		globalComb = comb;
		
        //在失去焦点的时候,判断所选用户是否正确
		comb.on('blur',function(c){
			    //首先去除value值最后一个;后的字符
			    var origValue = c.getValue();
			    var newValue = origValue.substr(0,origValue.lastIndexOf(";")+1);
			    c.setValue(newValue);
			    //alert(c.getValue());
			    document.getElementById("selUsersMessage").innerText=""; //查询提示清空
				//校验用户的合法性
				var allUsers = newValue.split(";");
				var valiString = "";
				var flag =false ;
				for(var i=0;i<allUsers.length-1;i++){
					var temp = allUsers[i];
					var regu = /(\([A-Za-z0-9]+\)\s*){1,2}$/; //;一个或者两个括弧都可以
					var re = new RegExp(regu);
					if (!re.test(temp)) {
						valiString = valiString+temp+";";
						flag = true ;
					} else {
						valiString = temp+";";
					}
				}
				if(flag){
					alert("所选用户"+valiString+"不合法,请重新选择!");
					return;
				}
				//失去焦点时将结果显示出来，并隐藏comb
				if(origValue!=null && origValue.length>0) {
					document.getElementById(checkNodeObj.value+"_useridDiv").style.display='block';
					comb.hide();
				}
			}
	    );

		loadFirstInit = function(store,records,options){
			//先将comb隐藏
			globalComb.hide();
			usersCount = store.getTotalCount();
			document.getElementById('selUsersMessage').innerHTML="";
			var checkNodeId = checkNodeObj.value;
			var userDivObj=document.getElementById(checkNodeId+"NodeDiv");
			userDivObj.style.display='block';
			if(store.getTotalCount()==0){
				document.getElementById("selUsers_html").style.display = "block";
				document.getElementById(checkNodeId+"_useridDiv").innerHTML="<font color=red>查询未找到合适用户</font>";
				document.getElementById(checkNodeId+"_useridDiv").style.display='block';
				document.getElementById("selUsers_ext").style.display = "none";
			}else if(store.getTotalCount()<=criticalValue ){ //如果总条数小于等于10,生成HTML
				document.getElementById("selUsers_html").style.display = "block";
				document.getElementById("selUsers_ext").style.display = "none";
				var userIdTest=records[0].data.userid;
				if(userIdTest.indexOf("T.")==0){ //项目池
					var innerHtml="<input type='hidden' id='"+checkNodeId+"_userid' name='"+checkNodeId+"_userid' value='"+userIdTest+"'/>";
					innerHtml+="<input type='text' id='"+checkNodeId+"_username' name='"+checkNodeId+"_username' value='系统' disabled style='width:300px'/>";
					document.getElementById(checkNodeId+"_useridDiv").innerHTML = innerHtml;
					document.getElementById(checkNodeId+"_useridDiv").style.display='block';
				}else if(userIdTest=="$\{alluser\}"){ //所有用户
					var innerHtml="";
					innerHtml = "<input type='text' id='"+checkNodeId+"_username' name='"+checkNodeId+"_username' readonly='readonly' style='width:300px'/>";
					if(records[0].data.userismu=="true"){
						innerHtml += "&nbsp;&nbsp;<span id='"+checkNodeId+"_aid'><a href='#' onclick=\"selUser('"+checkNodeId+"', 'n')\"/>选择</a></span><br/>";
					}else{
						innerHtml += "&nbsp;&nbsp;<span id='"+checkNodeId+"_aid'><a href='#' onclick=\"selUser('"+checkNodeId+"', '1')\"/>选择</a></span><br/>";
					}
					innerHtml += "<input type='hidden' id='"+checkNodeId+"_userid' name='"+checkNodeId+"_userid'/>";
					document.getElementById(checkNodeId+"_useridDiv").innerHTML = innerHtml;
					document.getElementById(checkNodeId+"_useridDiv").style.display='block';
				}else{
					var radiohtml="";
					if(records[0].data.userismu == "true"){ //多选
		                for(var m=0;m<store.getTotalCount();m++){
		                	radiohtml+="<input type='checkbox' name='"+checkNodeId+"_userid' value='"+records[m].data.userid+"'>"
		                	         +records[m].data.username+"</input><br/>";
		                }
					}else{ //单选
		                for(var m=0;m<store.getTotalCount();m++){
		                	if(store.getTotalCount() == 1) { //一个办理人，默认选择
		                		radiohtml+="<input type='radio' name='"+checkNodeId+"_userid' value='"+records[m].data.userid+"' checked='true'>"
	                	         +records[m].data.username+"</input><br/>";
		                	} else {
		                		radiohtml+="<input type='radio' name='"+checkNodeId+"_userid' value='"+records[m].data.userid+"'>"
	                	         +records[m].data.username+"</input><br/>";
		                	}
		                }
					}
					document.getElementById(checkNodeId+"_useridDiv").innerHTML=radiohtml;
					document.getElementById(checkNodeId+"_useridDiv").style.display='block';
				}
			}else{
				document.getElementById("selUsers_ext").style.display = "block";
				document.getElementById("selUsersMessage").innerHTML="<div/>";
				document.getElementById("selUsersMessage").style.display="block";
				globalComb.expand();
				globalComb.show();
				document.getElementById("selUsers").style.display="block";
				//一旦生成comb框的时候,即刻将load事件给修改为普通行为,除非重新从节点单选按钮发起
				store.un('load',loadFirstInit);
				store.on('load',loadMoreInit);
			}
        };
	
		loadMoreInit = function(store,records,options){
			if(store.getTotalCount()==0){
				document.getElementById("selUsersMessage").innerHTML="<div><font color=red>查询未找到合适用户</font></div>";	
				document.getElementById("selUsersMessage").style.display="block";
				globalComb.expand();
				globalComb.show();
				document.getElementById("selUsers").style.display="block";
			}else{
				document.getElementById("selUsersMessage").innerHTML="<div/>";
				document.getElementById("selUsersMessage").style.display="block";
				globalComb.expand();
				globalComb.show();
				document.getElementById("selUsers").style.display="block";
			}
        };
	}catch(e){
		alert(e);
	}
}
Ext.onReady(ready);

//点击选择好的办理人text执行，用于将触发重新选择
function showExtDiv(checkNodeId) {
	document.getElementById("selUsers_ext").style.display='block';
	document.getElementById("selUsers").style.display="block";
	document.getElementById(checkNodeId+"_useridDiv").style.display='none';
	//清空原来选择的值（避免没有再次选择，原来的存在但不可见）
	document.getElementById(checkNodeId+"_userid").value='';
	document.getElementById(checkNodeId+"_username").value='';
	var nodeList = document.getElementsByName("nodeidInput");
	for(var i=0; i<nodeList.length; i++) {
		var nodeVal = nodeList[i].value;
		if(nodeVal == checkNodeId) {
			showNodeDiv(nodeList[i]);
			break;
		}
	}
	globalComb.map.clear();
	/*
	var unames = document.getElementById(checkNodeId+"_username").value;
	var uids = document.getElementById(checkNodeId+"_userid").value;
	if(uids != null) {
		var uidArr = uids.split(";");
		var unameArr = unames.split(";");
		var valTmp = "";
		for(var i=0; i<uidArr.length; i++) {
			var uid = uidArr[i];
			if(uid==null || uid=='')
				continue;
			var uname = unameArr[i];
			valTmp += uname+"("+uid+");";
			if(!globalComb.map.containsKey(uid)){
				globalComb.map.put(uid, uid);
		    }
		}
		globalComb.setValue(valTmp);
	}
	*/
}

//选择用户
function selUser(nodeid,count){
	var contextPath="<%=request.getContextPath()%>";
	//打开选择处理人的界面
	var url = contextPath+'/selectAllUser.do?count='+count+'&EMP_SID=<%=request.getParameter("EMP_SID")%>&rd='+Math.random();
	var retObj = window.showModalDialog(url,'selectPage','dialogHeight:500px;dialogWidth:750px;help:no;resizable:no;status:no;');
		
	//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
	if(retObj == null)
		return;
	var status = retObj[0];
	if(status != true)
		return;
	if(retObj[1] != null){
		if(retObj[1].indexOf(";")!=-1){//返回多值
			var list=retObj[1].split(";");
			var seluser="";
			for(var i=0;i<list.length;i++){
				if(i>0)
					seluser+=";U."+list[i];
				else
					seluser+="U."+list[i];
			}
			document.getElementById(nodeid+"_userid").value=seluser;
		}else{
			document.getElementById(nodeid+"_userid").value="U."+retObj[1];
		}
	}
	if(retObj[2] != null){
		document.getElementById(nodeid+"_username").value = retObj[2];		
	}
}

function validateInput(a,id){
    for(var i=0;i<a.length;i++){
    	if(a[i]==id){
        	return true;
    	}
    }
	return false;
}

//点击节点选择办理人
function showNodeDiv(nodeObj){
	checkNodeObj = nodeObj;
	document.getElementById("selUsersMessage").innerText="";
	document.getElementById("selUsersMessage").style.display="none";
	var nodeList = document.getElementsByName("nodeidInput");
	//并行节点，重置选择结束节点时其他节点设置可选；重置普通节点时将其人员div隐藏
	if(nodeObj.type=='checkbox' && !nodeObj.checked) {
		if(nodeObj.value.indexOf('e') != -1) { //结束节点，其他节点设置可选
			for(var i=0; i<nodeList.length; i++) {
				nodeList[i].disabled = false;
			}
		} else {
			document.getElementById(nodeObj.value+'NodeDiv').style.display='none';
		}
		//return;
	}
	//并行节点，结束节点且被选中，则重置其他选中的节点且设置为disabled（结束节点不可与其他节点并行）
	if(nodeObj.value.indexOf('e')!=-1 && nodeObj.type=='checkbox' && nodeObj.checked) {
		for(var i=0; i<nodeList.length; i++) {
			if(nodeList[i].value != nodeObj.value) {
				nodeList[i].checked = false;
				nodeList[i].disabled = true;
			}
		}
	}
	//结束节点被选中，人员选择相关的隐藏
	if(nodeObj.value.indexOf('e')!=-1 && nodeObj.checked) {
		document.getElementById("selUsers_html").style.display='none';
		document.getElementById("selUsers_ext").style.display='none';
		globalComb.hide();
		return;
	}
	//遍历所有节点，选中的节点对应的办理人div显示出来（除了当前点击的节点），没有选中的隐藏
	for(var i=0; i<nodeList.length; i++) {
		if(nodeList[i].checked) {
			document.getElementById("selUsers_html").style.display='block';
			document.getElementById(nodeList[i].value+'NodeDiv').style.display='block';
			if(nodeList[i].value != nodeObj.value) { //当前点击的节点先不用在此控制始终显示，应该先隐藏。数据获取成功后在显示
				document.getElementById(nodeList[i].value+'_useridDiv').style.display='block';
				//当comb选择方式时但没有去选择，可能没有生成办理人div，则自动生成空结果，提高操作流畅性
				var objTmp = document.getElementById(nodeList[i].value+'_userid');
				if(objTmp==null || objTmp==undefined) {
					var innerHtml="";
					innerHtml += "<input type='text' id='"+nodeList[i].value+"_username' name='"+nodeList[i].value+"_username' value='' readonly=true style='width:300px' onclick=\"showExtDiv('"+nodeList[i].value+"')\" title=\"点击重新选择\"/>";
					innerHtml += "<input type='hidden' id='"+nodeList[i].value+"_userid' name='"+nodeList[i].value+"_userid' value=''/>";
					document.getElementById(nodeList[i].value+"_useridDiv").innerHTML=innerHtml;
				}
			} else {
				document.getElementById(nodeList[i].value+'_useridDiv').style.display='none';
			}
		} else {
			try {
				document.getElementById(nodeList[i].value+'NodeDiv').style.display='none';
			} catch(e){}
			//清空可能已经选择的值
			try {
				document.getElementById(nodeList[i].value+"_username").value='';
			} catch(e){}
			try {
				document.getElementById(nodeList[i].value+"_userid").value='';
			} catch(e){}
		}
	}
	//始终先隐藏
	document.getElementById("selUsers_ext").style.display='none';
	//动态更新数据
	if(nodeObj.checked) {
		if(nodeObj.type=='checkbox') {
			//将ext选择div挂到当前点击节点对应的办理人div下
			document.getElementById(nodeObj.value+'_selUsersExtDiv').appendChild(document.getElementById("selUsers_ext"));
		}
		//单选已经选择过，再次点击则直接返回
		try {
			var oldVal = document.getElementById(nodeObj.value+"_userid").value;
			if(oldVal!=null && oldVal!='') {
				document.getElementById(nodeObj.value+'_useridDiv').style.display='block';
				return;
			}
		} catch(e) {}
		changeUserData(globalComb, nodeObj);
	}
};

function changeUserData(comb, nodeObj){
	//判断选择的是哪个节点ID
	var nodeidValue = nodeObj.value;
	if(nodeidValue == null){
		alert("请选择节点");
		return;
	}
	//先将原数据清空
	comb.store.removeAll();
	comb.clearValue();
	comb.map.clear();
	comb.store.baseParams={EMP_SID:'<%=request.getParameter("EMP_SID")%>',
						   instanceId: '<%=request.getParameter("instanceId")%>',
						   nodeId: '<%=request.getParameter("nodeId")%>',
			               toNodeId: nodeidValue
			               };
	comb.store.un('load',loadMoreInit);
	comb.store.on('load', loadFirstInit);
	document.getElementById("selUsersMessage").innerHTML="<div><img width=25 heigth=25 src='<%=request.getContextPath()%>/images/workflow/loadingAnimation.gif'>&nbsp;&nbsp;&nbsp;&nbsp;<font color=red>系统正在计算办理人员，请稍候...</font></div>";
	document.getElementById("selUsersMessage").style.display="block";
	comb.store.load();
}

//选择抄送人员
function doSelAnnouces(){
	var contextPath="<%=request.getContextPath()%>";
	var url = contextPath+'/selectAllUser.do?&count=n&EMP_SID=<%=request.getParameter("EMP_SID")%>&rd='+Math.random();
	//打开选择处理人的界面
	var retObj = window.showModalDialog(url,'selectPage','dialogHeight:500px;dialogWidth:750px;help:no;resizable:no;status:no;');
	//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
	if(retObj == null)
		return;
	var status = retObj[0];
	if(status != true)
		return;
	if(retObj[1] != null){
		if(retObj[1].indexOf(";")!=-1){//返回多值
			var list=retObj[1].split(";");
			var seluser="";
			for(var i=0;i<list.length;i++){
				if(i>0)
					seluser+=";U."+list[i];
				else
					seluser+="U."+list[i];
			}
			document.getElementById("annouceUserID").value =seluser;
		}else{
			document.getElementById("annouceUserID").value = "U."+retObj[1];
		}
	}
	if(retObj[2] != null){
		var annNames = retObj[2];
		document.getElementById("annouceUserName").value = annNames;
	}
}

//展开抄送人员DIV
function doOpenAnn(){
	var imgAnn = document.getElementById("openAnnounce");
	var alt = imgAnn.alt;
	var isOpen = imgAnn.isOpen;
	//alert(document.getElementById("announceUserDiv").style.display);
	if(isOpen=="false"){
		imgAnn.alt="收起抄送人员选择项";
		imgAnn.src="<%=request.getContextPath()%>/images/workflow/up.gif";
		document.getElementById("announceUserDiv").style.display="block";
		imgAnn.isOpen="true";
	}else{
		imgAnn.alt="展开抄送人员选择项";
		imgAnn.src="<%=request.getContextPath()%>/images/workflow/down.gif";
		document.getElementById("announceUserDiv").style.display="none";
		imgAnn.isOpen="false";
	}
		
}

function retSuc(){
	var retObj = [];
	retObj[0] = true;
	var node = null;
	var usertmp = null;
	var user =null;
	//取所有节点
	var list = document.getElementsByName("nodeidInput");
	var listuser=null;
	var userFlag = true;
	var nrt = '<%=noderoutertype%>';
	if("2"==nrt||"4"==nrt) {
		//document.getElementById("selUsers_ext").style.display='none';
		//document.getElementById("selUsers_html").style.display='block';
	}
	for(var i=0;i<list.length;i++){
		usertmp = null;
		//alert('list['+i+']='+list[i].value);
		var nodeTmp = list[i].value;
		if(!list[i].checked){
			document.getElementById(nodeTmp+"NodeDiv").style.display='none';
			continue;
		}
		if(node == null){
			node = nodeTmp;
		} else{
			node = node + "@" + nodeTmp;
		}
		if(nodeTmp.indexOf('e') != -1) {
			break; //结束节点无需设置办理人
		}
		document.getElementById(nodeTmp+"NodeDiv").style.display='block';
		if("2"==nrt||"4"==nrt) {
			var aObj = document.getElementById(nodeTmp+"_aid");
			if(aObj && aObj!='undefined' && aObj!=null) {
				//aObj.style.display='none'; //隐藏选择的链接
			}
		}
		listuser = document.getElementsByName(nodeTmp+"_userid");
		for(var j=0;j<listuser.length;j++){
			var userTmpObj = listuser[j];
			if("2"==nrt||"4"==nrt) {
				//userTmpObj.readonly = true; //设置为只读
				//userTmpObj.disabled = 'disabled';
			}
			//项目池或所有用户选择值存放在隐藏文本域,无需判断checked
			if(userTmpObj.type!='hidden' && !userTmpObj.checked)
				continue;
			if(usertmp == null)
				usertmp = userTmpObj.value;
			else
				usertmp = usertmp + ";" + userTmpObj.value;
		}
		if(usertmp==null||usertmp==""){
			userFlag = false;
			usertmp = 'null';
			//alert("请选择好办理人员！");
		}
		if(user==null||user==""){
			user=usertmp;
		}else{
			user=user+"@"+usertmp;
		}
	}
	if(node == null){
		alert("请选择下一节点");
		return;
	}
	if(!userFlag) {
		alert("请选择好办理人员！");
		return;
	}
	//if("2".equals(noderoutertype)||"4".equals(noderoutertype)) {//多选处理、条件多选处理
	if(("2"==nrt||"4"==nrt) && node.indexOf('e')==-1) {
		if(!confirm('请再次确定节点及办理人选择是否正确；正确请点击确定，否则点击重置！')) {
			return;
		}
	}
	retObj[1] = node;
	retObj[2] = user;
	retObj[3] = "";
	var annouceUser = document.getElementById("annouceUserID").value;
	if(annouceUser!=null && annouceUser!=""){
		retObj[3] = annouceUser;
	}
	/*
	alert('node='+node);
	alert('user='+user);
	alert('annouceUser='+annouceUser);
	return;
	*/
	window.returnValue = retObj;
	window.close();
};

function retFail(){
	var ret = [];
	ret[0] = false;
	window.returnValue = ret;
	window.close();
};
	

function doOnUnLoad() {
	
	//restoreStyle();
};
	
</script>
</HEAD>

<BODY>
<input type="hidden" id="seluser" value=""/>
<div class="selectNextNodeStyle1">

<table class=tablemain2 cellspacing=0 cellpadding=0 border=0>
<tr>
<td class=trtitle width="30%">当前审批步骤</td>
<td class=trtitle>当前办理人</td>
<td class=trtitle>当前处理机构</td>
</tr>
<tr>
<td>【 <%=curNodeName%>】</td>
<td>【<%=curUserName %> 】</td>
<td>【<%=curOrgName %>】</td>
</tr>
</table> 
<br>

<table class=tablemain2 cellspacing=0 cellpadding=0 border=0>
<tr>
<td class=trtitle>下一处理步骤</td>
<td class=trtitle>下一处理人</td>
</tr>
<tr height="60%">
<td width="30%">
<%
String[] nodeIds = new String[wfNodeList.size()];
String[] nodeNames = new String[wfNodeList.size()];
String nodeid;
String nodename;
if("2".equals(noderoutertype)||"4".equals(noderoutertype)) {//多选处理、条件多选处理
	for (int i = 0; i < wfNodeList.size(); i++) {
		WFINodeVO wfNode = wfNodeList.get(i);
		nodeid=wfNode.getNodeId();
		nodeIds[i]=nodeid;
		nodename=wfNode.getRouteName();
		if(nodename==null||nodename.length()==0) {
			nodename=wfNode.getNodeName();
		}
		nodeNames[i]=nodename;
		out.println("<input type='checkbox' name='nodeidInput' value='" 
				+ wfNode.getNodeId()
				+ "' onclick='showNodeDiv(this)' >"
				+ nodename+"<br/>");
	}
} else {
	for (int i = 0; i < wfNodeList.size(); i++) {
		WFINodeVO wfNode = wfNodeList.get(i);
		nodeid=wfNode.getNodeId();
		nodeIds[i]=nodeid;
		nodename=wfNode.getRouteName();
		if(nodename==null||nodename.length()==0) {
			nodename=wfNode.getNodeName();
		}
		out.println("<input type='radio' name='nodeidInput' value='" 
				+ wfNode.getNodeId() 
				+ "' onclick='showNodeDiv(this)' >"+nodename+"<br/>");
	}
}
%>
</td>
<td>&nbsp;
<div id="selUsers_html" style="">
<%
if("2".equals(noderoutertype)||"4".equals(noderoutertype)) {//多选处理、条件多选处理
	for(int j=0;j<nodeIds.length;j++){
		nodeid=nodeIds[j];
		out.println("<div id='"+nodeid+"NodeDiv' style='display:none'>");
		//out.println("<fieldset style='border-color:red'><legend style='color:blue'>"+nodeNames[j]+"办理人</legend><div id='"+nodeid+"_useridDiv'></div><div id='"+nodeid+"_selUsersExtDiv'></div>");
		out.println("<fieldset><legend style='color:blue'>"+nodeNames[j]+"办理人</legend><div id='"+nodeid+"_useridDiv'></div><div id='"+nodeid+"_selUsersExtDiv'></div>");
		out.println("</fieldset><br/></div>");
	}
} else {
	for(int j=0;j<nodeIds.length;j++){
		nodeid=nodeIds[j];
		out.println("<div id='"+nodeid+"NodeDiv' style='display:none'><div id='"+nodeid+"_useridDiv'></div>");
		out.println("</div>");
	}
}
%>
</div>
<div id="selUsers_ext" style="display:none">
    <div id="selUsersMessage" style="display:none"></div>
	<div id="selUsers" style="display:none"></div>
	<div id="selTen" style="display:none"></div>
</div>
	
</td>
</tr>
</table>
<img id="openAnnounce" alt="展开抄送人员选择选项" isOpen="false" style="cursor:hand;" src="<%=request.getContextPath()%>/images/workflow/down.gif" onclick="doOpenAnn()">
<div id="announceUserDiv" style="display:none">
	<table class=tablemain2 cellspacing=0 cellpadding=0 border=0>
		<tr>
			<td class="trtitle" width="30%">请选择抄送人员</td>
			<td>
				<input type="text" id="annouceUserName" name="annouceUserName" style="width:300px" readonly="readonly">
					<a href="#" onclick="doSelAnnouces()">选择</a>
				<input type="hidden" id="annouceUserID" name="annouceUserID">
			</td>
		</tr>
	</table>
</div>
<br>
</div>
<center>
<input type="button" class="button" value="确  定" onclick="retSuc()">&nbsp;&nbsp;
<input type="button" class="button" value="取  消" onclick="retFail()"></center>
</BODY>
</HTML>
</emp:page>
