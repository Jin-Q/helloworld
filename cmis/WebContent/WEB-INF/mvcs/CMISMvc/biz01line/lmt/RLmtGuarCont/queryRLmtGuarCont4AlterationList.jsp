<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String grtOp = "";
	if(context.containsKey("grtOp")){
		grtOp =(String)context.getDataValue("grtOp");
	}      
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_field_readonly .emp_field_select_input {
	display: inline;
	width: 60px;
	border-color: #b7b7b7;
	background-color: #e3e3e3;
	text-align:center;
}
.emp_field_select_select { /***** 固定长度 *****/
	width: 60px;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
	text-align:center;
}

</style>

<script type="text/javascript">
	/*--user code begin--*/
	rowIndex = 0;
	function doViewGrtGuarCont() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no']);   
		var flag ="view";       
		if (paramStr != null) {   
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuIdTab=ybCount&flag=ybWh&rel=sxRel&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow2','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {   
			alert('请先选择一条记录！'); 
		}   
	};
	      
	/*一般担保合同引入 */
	function doGetAddGrtYBGuarPage(){
		var url = '<emp:url action="introYbGrtGuarContList.do"/>?rel=sxRel&limit_code=${context.org_limit_code}&cus_id=${context.cus_id}';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=650,width=1024,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	
    /*一般担保合同新增
     @ws添加参数 
     @serno 业务申请流水号
     @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
    */
    function doGetAddGrtGuarContPage(){
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?isCreditChange=sx&op=add&cus_id=${context.cus_id}&menuIdTab=ybCount&flag=ybWh&rel=sxRel&limit_code=${context.org_limit_code}&serno=${context.serno}';   
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow3','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	/*一般担保合同修改 
    @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
   */  
	function doGetUpdateGrtGuarContPage() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no']);
			if(paramStr != null) {       
				var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
				if(dataRow.corre_rel._getValue() != 2){   
				   alert("变更类型非[新增]不能修改！");      
		         	return;      
				}     
				var flag ="update";
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?isCreditChange=sx&op=update&flag=ybWh&menuIdTab=ybCount&'+paramStr+'&rel=sxRel&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow1','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{  
				alert('请先选择一条记录！');  
			}
	};       
	
	/*一般担保合同删除 */
	function doDeleteGrtLoanRGur() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no','guar_cont_state','guar_lvl']);
		if (paramStr != null) {   
			var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
			if(dataRow.corre_rel._getValue() != 2){
			   alert("变更类型非[新增]不能删除！");      
	           return;     
			} 
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
					var url = '<emp:url action="deleteRLmtGuarContRecord.do"/>?'+paramStr+'&serno=${context.serno}';
					url = EMPTools.encodeURI(url);     
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
    /*最高担保额合同引入*/
	function doGetAddRLmtGuarContZge() {
		var url = '<emp:url action="introGrtGuarContList.do"/>?action=zg&rel=sxRel&cus_id=${context.cus_id}&limit_code=${context.org_limit_code}';
		url=EMPTools.encodeURI(url);   
      	window.open(url,'newwindow','height=650,width=1024,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	/*最高额担保合同新增
    @ws添加参数 
    @serno 业务申请流水号
    @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
   */
   function doGetHighGrtGuarContAddPage(){
		var url = '<emp:url action="getHighGrtGuarContAddPage.do"/>?isCreditChange=sx&op=add&cus_id=${context.cus_id}&menuIdTab=zge&flag=ybWh&rel=sxRel&limit_code=${context.org_limit_code}&serno=${context.serno}';   
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow3','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	/*最高担保额合同修改*/
	function doGetUpdateRLmtGuarContZge() {
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no']); 
		var flag ="update";        
			if (paramStr != null) {
				var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
				if(dataRow.corre_rel._getValue() != 2){
				   alert("变更类型非[新增]不能修改！");      
		           return;
				} 
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?isCreditChange=sx&menuIdTab=zge&op=update&'+paramStr+'&rel=sxRel&flag=ybWh&oper='+flag;
				url = EMPTools.encodeURI(url);        
				window.open(url,'newwindow1','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			} else {            
				alert('请先选择一条记录！');
			} 
	}; 
	
	/*最高额担保合同删除 */
	function doDeleteRLmtGuarContZge() {
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no','guar_lvl','guar_cont_state']);  
		if (paramStr != null) {     
			var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
			if(dataRow.corre_rel._getValue() != 2){
			   alert("变更类型非[新增]不能删除！");      
	           return;     
			}     
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
					var url = '<emp:url action="deleteRLmtGuarContZGE.do"/>?'+paramStr+'&serno=${context.serno}';
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	   
	/*最高额担保合同查看*/   
	function doViewRLmtGuarContZge() {      
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no']);  
		var flag ="view"; 
		if (paramStr != null) {   
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuIdTab=zge&op=view&'+paramStr+'&flag=loan&rel=sxRel&oper='+flag;
			url = EMPTools.encodeURI(url);      
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {   
			alert('请先选择一条记录！'); 
		}
	};   
	
    //提升等级 
	function doUpLvl(){   
		var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
 		var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();   
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {      
			var row = dataRow.displayid._getValue();
			if(row==1){
               alert("已是本次担保合同中最高等级");
               return;  
			}else{  
			  var limit_code_up = RLmtGuarContListYb._obj.data[row-2].limit_code._getValue(); 
			  var guar_cont_no_up = RLmtGuarContListYb._obj.data[row-2].guar_cont_no._getValue(); 
			  var serno_up = RLmtGuarContListYb._obj.data[row-2].serno._getValue(); 
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
				var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_up+'&guar_cont_no_up='+guar_cont_no_up+'&serno_up='+serno_up;  
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    
    //最高额提升等级
	function doUpLvlZge(){
		var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
		var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{
			  var limit_code_up = RLmtGuarContListZge._obj.data[row-2].limit_code._getValue(); 
			  var guar_cont_no_up = RLmtGuarContListZge._obj.data[row-2].guar_cont_no._getValue(); 
			  var serno_up = RLmtGuarContListZge._obj.data[row-2].serno._getValue(); 
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
				var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_up+'&guar_cont_no_up='+guar_cont_no_up+'&serno_up='+serno_up; 
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    
    //最高额降低等级
    function doDownLvlZge(){
    	var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
    	var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			var recordCount = RLmtGuarContListZge._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{ 
				var limit_code_down = RLmtGuarContListZge._obj.data[row].limit_code._getValue();  
				var guar_cont_no_down = RLmtGuarContListZge._obj.data[row].guar_cont_no._getValue();
				var serno_down = RLmtGuarContListZge._obj.data[row].serno._getValue();
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
					var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+senro+'&limit_code_up='+limit_code_down+'&guar_cont_no_up='+guar_cont_no_down+'&serno_up='+serno_down;   
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    }; 
    
    //降低等级
    function doDownLvl(){
    	var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
    	var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			var recordCount = RLmtGuarContListYb._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{
				var limit_code_down = RLmtGuarContListYb._obj.data[row].limit_code._getValue();  
				var guar_cont_no_down = RLmtGuarContListYb._obj.data[row].guar_cont_no._getValue();  
				var serno_down = RLmtGuarContListYb._obj.data[row].serno._getValue();  
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
					var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_down+'&guar_cont_no_up='+guar_cont_no_down+'&serno_up='+serno_down;   
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		    }  
		} else { 
			alert('请先选择一条记录！');
		} 
    };              


    function changeCorreRelYB(){
    	var data = RLmtGuarContListYb._obj.getSelectedData()[0]; 
    	if (data == null) {
            alert("请选择一条记录");
        	return;
    	}
        var corre_rel = data.corre_rel._getValue();   
        var guar_cont_no = data.guar_cont_no._getValue();
        var limit_code = data.limit_code._getValue();
        if(corre_rel == 3 || corre_rel == 4){   
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
    					alert("操作成功!");
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
    		var url = '<emp:url action="updateCorreRelRecord4Lmt.do"/>?guar_cont_no='+guar_cont_no+'&limit_code='+limit_code+'&corre_rel='+corre_rel+'&serno=${context.serno}';  
    		url = EMPTools.encodeURI(url);   
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
        }else{
            alert("变更类型非解除和续作,请检查!");  
        } 
    }; 

    /**变更类型选择删除和续作时改变对应数据关联关系状态**/
    function changeCorreRelZGE(){
        var data = RLmtGuarContListZge._obj.getSelectedData()[0]; 
        if (data == null) {
            alert("请选择一条记录");
        	return;
    	}     
        var corre_rel = data.corre_rel._getValue();
        var guar_cont_no = data.guar_cont_no._getValue();
        var limit_code = data.limit_code._getValue();
        var serno = '${context.serno}';
		if(corre_rel == 4){
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
						setCorreRelForZGE();
					}else {
						alert("担保合同已被申请中其他授信分项引用，无法修改为续作！");
						return;
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

			var url = '<emp:url action="checkCancelForZGE.do"/>?serno='+serno+'&guar_cont_no='+guar_cont_no;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		}
        
        if(corre_rel == 3){   
        	setCorreRelForZGE();
        }
    };

    function setCorreRelForZGE(){
    	var data = RLmtGuarContListZge._obj.getSelectedData()[0]; 
    	var guar_cont_no = data.guar_cont_no._getValue();
    	var limit_code = data.limit_code._getValue();
    	var corre_rel = data.corre_rel._getValue();
        
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
					alert("操作成功!");
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
		var url = '<emp:url action="updateCorreRelRecord4Lmt.do"/>?guar_cont_no='+guar_cont_no+'&limit_code='+limit_code+'&corre_rel='+corre_rel+'&serno=${context.serno}';  
		url = EMPTools.encodeURI(url);   
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
    }
	
	function onLoad(){
    	 var recordCountZGE = RLmtGuarContListZge._obj.recordCount;//取最高额总记录数
         //如果记录数为0则不处理
         if(recordCountZGE>0){
         	for(var m=0;m<recordCountZGE;m++){
         		var options = RLmtGuarContListZge._obj.data[m].corre_rel._obj.element.options;
         		var corre_rel = RLmtGuarContListZge._obj.data[m].corre_rel._getValue();
         		if(corre_rel != 2){
         			for(var i=options.length-1;i>=0;i--){	
                 		if(options[i].value=="" || options[i].value=="1" || options[i].value=="2" || options[i].value=="5" || options[i].value=="6"){
                 			options.remove(i);            
                 		}
                 	}
                 	//如果是查看则全部只读
         			if('${context.grtOp}'=="view"){
         				RLmtGuarContListZge._obj.data[m].corre_rel._obj._renderReadonly(true); 
             		}
             	}else{
             		RLmtGuarContListZge._obj.data[m].corre_rel._obj._renderReadonly(true);   
                 }
             } 
         }  
     	
         var recordCountYB = RLmtGuarContListYb._obj.recordCount;//取一般担保总记录数
         //如果记录数为0则不处理    
         if(recordCountYB>0){ 
         	for(var m=0;m<recordCountYB;m++){ 
         		var options = RLmtGuarContListYb._obj.data[m].corre_rel._obj.element.options;
         		var corre_rel = RLmtGuarContListYb._obj.data[m].corre_rel._getValue();
         		if(corre_rel != 2){
         			for(var i=options.length-1;i>=0;i--){	
                 		if(options[i].value=="" || options[i].value=="1" || options[i].value=="2" || options[i].value=="5" || options[i].value=="6"){
                 			options.remove(i);
                 		} 
                 	}  
         			//如果是查看则全部只读  
         			if('${context.grtOp}'=="view"){
         				RLmtGuarContListYb._obj.data[m].corre_rel._obj._renderReadonly(true); 
             		}  
         		}else{
         			RLmtGuarContListYb._obj.data[m].corre_rel._obj._renderReadonly(true); 
         			//alert(RLmtGuarContListYb._obj.data[m].corre_rel._obj.cssFakeInputClass);
             	}
             }
         }
    }

	//引入最高额担保合同
	function doGetAddGrtZgeGuarPage(){
		//alert('${context.cus_id}');
		var url = '<emp:url action="introGrtGuarContListForLmt.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.org_limit_code}&type=01';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=538,width=1024,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	//引入一般担保合同
	function doGetAddGrtGuarPage(){
		var url = '<emp:url action="introGrtGuarContListForLmt.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.org_limit_code}&type=00';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=650,width=1024,top=100,left=150,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<form  method="POST" action="#" id="queryForm"></form>       
    
	<b>最高额担保合同</b>
	<div align="left">
	   <%if("update".equals(grtOp)){%>
	    <emp:actButton id="getAddGrtZgeGuarPage" label="引入" op="add"/> 
		<emp:button id="getHighGrtGuarContAddPage" label="新增" /> 
		<emp:button id="getUpdateRLmtGuarContZge" label="修改" />  
		<emp:button id="deleteRLmtGuarContZge" label="删除" />
		<%}%> 
		<emp:button id="viewRLmtGuarContZge" label="查看" />   
	</div>     

	<emp:table icollName="RLmtGuarContListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
	    <emp:text id="serno" label="业务流水号" hidden="true"/>
		<emp:text id="limit_code" label="授信额度编号" hidden="true" /> 
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保合同金额" dataType="Currency" hidden="true"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL" flat="true" onchange="changeCorreRelZGE()" />
		<emp:text id="guar_lvl" label="等级 " />
		<%if(!"view".equals(grtOp)){%> 
		<emp:link id="upZge" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvlZge"/>  
		<emp:link id="downZge" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvlZge" hidden="false"/>
	    <%}%>
	</emp:table>    
	   
	<br><br>
	<b>一般担保合同</b>  
	<div align="left">   
		
		<%if("update".equals(grtOp)){%>
		<emp:actButton id="getAddGrtGuarPage" label="引入" op="add"/>
		<emp:button id="getAddGrtGuarContPage" label="新增" /> 
		<emp:button id="getUpdateGrtGuarContPage" label="修改" />
		<emp:button id="deleteGrtLoanRGur" label="删除" /> 
		<%}%> 
		<emp:button id="viewGrtGuarCont" label="查看" />   
	</div> 
	   
	<emp:table icollName="RLmtGuarContListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
	    <emp:text id="serno" label="业务流水号" hidden="true"/>
		<emp:text id="limit_code" label="授信额度编号" hidden="true"/>
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保合同金额" dataType="Currency" hidden="true"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL" flat="false" readonly="false" onchange="changeCorreRelYB()" />
		<emp:text id="guar_lvl" label="等级 "/>  
		<%if(!"view".equals(grtOp)){%>
		    <emp:link id="up" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvl"/>  
		    <emp:link id="down" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvl" hidden="false"/>
	    <%}%>    
	</emp:table>                        
</body>
</html>
</emp:page>
    