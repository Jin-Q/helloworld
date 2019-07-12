<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	String cus_id="";
	String limit_acc_no="";
	String limit_credit_no="";
	String op = "";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}
	if(context.containsKey("cus_id")){
		cus_id =(String)context.getDataValue("cus_id");
	}
	if(context.containsKey("limit_acc_no")){
		limit_acc_no =(String)context.getDataValue("limit_acc_no");
	}      
	if(context.containsKey("limit_credit_no")){
		limit_credit_no =(String)context.getDataValue("limit_credit_no");
	}      
	if(context.containsKey("op")){
		op =(String)context.getDataValue("op");
	}      
	//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
	String modify_rel_serno = "";
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno =(String)context.getDataValue("modify_rel_serno");
	} 
	String modiflg = "";
	if(context.containsKey("modiflg")){
		modiflg =(String)context.getDataValue("modiflg");
	} 
	//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"> 
   rowIndex = 0;
	/*一般担保合同查看
	@flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	@oper=view 查看按钮标识
	@menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	 */
	function doViewGrtGuarCont() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);   
		var flag ="view";
		if (paramStr != null) {
			//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuIdTab=ybCount&flag=ybWh&rel=ywRel&'+paramStr+'&oper='+flag+'&modify_rel_serno='+'<%=modify_rel_serno%>';
			//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {   
			alert('请先选择一条记录！');
		}   
	};

	      
	/*一般担保合同引入 */
	function doGetAddGrtYBGuarPage(){
		if(('<%=limit_acc_no%>' == "" || '<%=limit_acc_no%>' == "null")&&('<%=limit_credit_no%>' == "" || '<%=limit_credit_no%>' == "null")){  
			//var url = '<emp:url action="introYbGrtGuarContList.do"/>?serno='+'<%=serno%>'+'&cus_id='+'<%=cus_id%>';
		    alert("未使用授信，没有担保合同引用项");
		}else{
			var url = '<emp:url action="grtGuarYbList.do"/>?cus_id='+'<%=cus_id%>'+'&limit_acc_no='+'<%=limit_acc_no%>'+'&limit_credit_no='+'<%=limit_credit_no%>'+'&serno='+'<%=serno%>'+'&guar_cont_type=00';  
			url=EMPTools.encodeURI(url);  
	      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	    }
	};
    /*一般担保合同新增
     @ws添加参数 
     @serno 业务申请流水号
     @rel 担保新增后调用接口判断 （ywRel：来自业务模块)
    */
    function doGetAddGrtGuarContPage(){
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?op=add&cus_id=${context.cus_id}&menuIdTab=ybCount&flag=ybWh&rel=ywRel&serno='+'<%=serno%>';   
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow3','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}; 
	/*一般担保合同修改 
    @ws添加参数 
    @pk_id 业务申请流水号
    @rel 担保新增后调用接口判断 （ywRel：来自业务模块)
   */  
	function doGetUpdateGrtGuarContPage() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);  
			if(paramStr != null) {           
				var flag ="update";
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?serno=${context.serno}&op=update&flag=ybWh&menuIdTab=ybCount&'+paramStr+'&rel=ywRel&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{  
				alert('请先选择一条记录！');
			}
	};       
	
	/*一般担保合同删除 */
	function doDeleteGrtLoanRGur() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no','guar_cont_state','guar_lvl','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){   
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
								alert("删除成功!");
								window.location.reload();  
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);  
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
    /*最高担保额合同引入*/
	function doGetAddGrtLoanRGurZge() { 
		if(('<%=limit_acc_no%>' == "" || '<%=limit_acc_no%>' == "null")&&('<%=limit_credit_no%>' == "" || '<%=limit_credit_no%>' == "null")){      
			var url = '<emp:url action="introGrtGuarContList.do"/>?action=zg&cus_id='+'<%=cus_id%>'+'&serno='+'<%=serno%>';
		}else{
			var url = '<emp:url action="grtGuarZgeList.do"/>?cus_id='+'<%=cus_id%>'+'&limit_acc_no='+'<%=limit_acc_no%>'+'&limit_credit_no='+'<%=limit_credit_no%>'+'&serno='+'<%=serno%>'+'&guar_cont_type=01'; 
		} 
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};    
	/*最高担保额合同修改*/
	function doGetUpdateGrtLoanRGurZge() {   
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']); 
		var flag ="update";       
			if (paramStr != null) {
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?serno=${context.serno}&menuIdTab=zge&op=view&'+paramStr+'&rel=ywRel&flag=ybWh&oper='+flag;
				url = EMPTools.encodeURI(url);    
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			} else {
				alert('请先选择一条记录！');
			}
	};
	/*最高额担保合同删除 */
	function doDeleteGrtLoanRGurZge() {  
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_lvl','serno']);  
		if (paramStr != null) {     
			if(confirm("是否确认要删除？")){ 
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
								alert("删除成功!");
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurZGE.do"/>?'+paramStr;  
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
	   
	/*最高额担保合同查看*/   
	function doViewGrtLoanRGurZge() { 
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']);  
		var flag ="view";   
		if (paramStr != null) {
			//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuIdTab=zge&op=view&'+paramStr+'&flag=loan&rel=ywRel&oper='+flag+'&modify_rel_serno='+'<%=modify_rel_serno%>';
			//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
			url = EMPTools.encodeURI(url);  
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {
			alert('请先选择一条记录！');     
		}
	};   
	
	function doReset(){
		page.dataGroups.GrtLoanRGurGroup.reset();
	};
    //提升等级
	function doUpLvl(){   
		var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
 		var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {      
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{ 
			  var pk = GrtLoanRGurListYb._obj.data[row-2].pk_id._getValue(); 
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
							alert("提升等级成功!");
							window.location.reload(); 
						}else {
							alert("发生异常!");
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
				var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk;  
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    //最高额提升等级
	function doUpLvlZge(){
		var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
		var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{
			  var pk = GrtLoanRGurListZge._obj.data[row-2].pk_id._getValue(); 
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
							alert("提升等级成功!");
							window.location.reload(); 
						}else {
							alert("发生异常!");
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
				var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    //最高额降低等级
    function doDownLvlZge(){
    	var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
    	var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			var recordCount = GrtLoanRGurListZge._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{ 
				var pk = GrtLoanRGurListZge._obj.data[row].pk_id._getValue();  
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
								alert("降低等级成功!");  
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    };  
    //降低等级
    function doDownLvl(){
    	var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
    	var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			var recordCount = GrtLoanRGurListYb._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{
				var pk = GrtLoanRGurListYb._obj.data[row].pk_id._getValue();  
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
								alert("降低等级成功!");  
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)  
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    };   

    function onload(){
        var res = '${context.res}'
        if(res == "have"){
           alert("请注意授信中有关联的追加担保合同!");
        }         
    }

    /*MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START*/
	/*最高担保额合同修改*/
	function doGetMoUpdateGrtLoanRGurZge() {   
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']); 
		var flag ="update";       
			if (paramStr != null) {
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?serno=${context.serno}&menuIdTab=zge&op=view&'+paramStr+'&rel=ywRel&flag=ybWh&oper='+flag+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				url = EMPTools.encodeURI(url);    
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			} else {
				alert('请先选择一条记录！');
			}
	};
	function doGetMoUpdateGrtGuarContPage() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);  
			if(paramStr != null) {           
				var flag ="update";
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?serno=${context.serno}&op=update&flag=ybWh&menuIdTab=ybCount&'+paramStr+'&rel=ywRel&oper='+flag+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{  
				alert('请先选择一条记录！');
			}
	}; 
	/*MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START*/
	/*--user code begin--*/

	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" onload="onload()">
	<form  method="POST" action="#" id="queryForm">
	</form>       
    
	<b>最高额担保</b>
	<div align="left">
	<%//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
	   if("".equals(modiflg)){%>
		<emp:actButton id="getAddGrtLoanRGurZge" label="引入" op="add"/>
		<emp:actButton id="getUpdateGrtLoanRGurZge" label="修改" op="update"/>  
		<emp:actButton id="deleteGrtLoanRGurZge" label="重置引入" op="remove"/>
		<emp:actButton id="viewGrtLoanRGurZge" label="查看" op="view"/>   
	<%}else{%>
		<emp:actButton id="getMoUpdateGrtLoanRGurZge" label="修改" op="update"/> 
		<emp:actButton id="viewGrtLoanRGurZge" label="查看" op="view"/>  
	<%}
	//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END%>
	</div>     

	<emp:table icollName="GrtLoanRGurListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>
	     <emp:text id="serno" label="业务流水号" hidden="true"/>    
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>		
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL"/>
		<emp:text id="guar_lvl" label="等级 " />
		<%if("".equals(modiflg)){%>
		<%if(!"view".equals(op)){%>
		<emp:link id="upZge" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvlZge"/>  
		<emp:link id="downZge" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvlZge" hidden="false"/>
	    <%}%>
	    <%}%>
	</emp:table>
	   
	<br><br>
	<b>一般担保合同</b>
	<div align="left">   
	<%//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
	if("".equals(modiflg)){%>
		<emp:actButton id="getAddGrtYBGuarPage" label="引入" op="add"/>
		<emp:actButton id="getAddGrtGuarContPage" label="新增" op="add"/> 
		<emp:actButton id="getUpdateGrtGuarContPage" label="修改" op="update"/>
		<emp:actButton id="deleteGrtLoanRGur" label="删除" op="remove"/>
		<emp:actButton id="viewGrtGuarCont" label="查看" op="view"/>   
	<%}else{%>
		<emp:actButton id="getMoUpdateGrtGuarContPage" label="修改" op="update"/> 
		<emp:actButton id="viewGrtGuarCont" label="查看" op="view"/> 
	<%}
	//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END%>
	</div>
	   
	<emp:table icollName="GrtLoanRGurListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>   
	    <emp:text id="serno" label="业务流水号" hidden="true"/>    
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>   
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>		
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL"/> 
		<emp:text id="guar_lvl" label="等级 "/>
		<%
		//MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
		if("".equals(modiflg)){%>
		<%if(!"view".equals(op)){%>  
		    <emp:link id="up" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvl"/>  
		    <emp:link id="down" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvl" hidden="false"/>
	    <%}%> 
	    <%}
	    //MODIFIED by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END%>
	</emp:table>     
</body>
</html>
</emp:page>
    