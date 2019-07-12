<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag= "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	String flagCs= "";
	if(context.containsKey("flagCs")){
		flagCs = (String)context.getDataValue("flagCs");
	}
	String tab= "";
	if(context.containsKey("tab")){
		tab = (String)context.getDataValue("tab");
	}
	String op= "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	String stay= "";
	if(context.containsKey("stay")){
		stay = (String)context.getDataValue("stay");
	}
	//需求编号：【XD141107075】获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
	/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
	String lrisk_type="";
	if(context.containsKey("lrisk_type")){//判断授信申请风险类型，用于控制【押品所有权是否为非关联方第三人】字段
		lrisk_type = (String)context.getDataValue("lrisk_type");
	}
	/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
%>
<html>
<head>
<title>新增页面</title>
<%
	request.setAttribute("canwrite","");
%>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">

	rowIndex = 0;
	var tmp_var = "";
	//增加共有人信息记录
	function doGetAddMortCommenOwner(){
		var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
		MortCommenOwnerList._obj._addRow();
		MortCommenOwnerList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
		MortCommenOwnerList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var row = recordCount-1;
		var id = row + '_view';
		MortCommenOwnerList._obj.data[recordCount-1].commen_owner_no._obj.addOneButton(id,'选择',queryCus);
	}
	function doReturn(){
		<%if("hwdj".equals(flag)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwdj';
		<%}else if("view".equals(flagCs)){%>
		var guaranty_no = MortGuarantyBaseInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantyBaseInfoTabForChkStoreList.do"/>?menuId=IqpChkStoreSet&subMenuId=MortGuarantyBaseInfoTab&op=update&task_set_type=${context.task_set_type}&cus_id=${context.cus_id}&oversee_agr_no=${context.oversee_agr_no}&op=view&flagCs=view&guaranty_no='+guaranty_no;
		<%}else if("to_storage".equals(op)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?status=finish&menuId=stay_storage';
		<%}else{%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	//加载共有人选择页面
	function queryCus(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var url = '<emp:url action="queryAllCusPop.do?cusTypCondition=Com"/>&returnMethod=returnCusCom';
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	//删除共有人记录
	function doDeleteMortCommenOwner(){
		var dataRow =  MortCommenOwnerList._obj.getSelectedData()[0];
		var idx = MortCommenOwnerList._obj.getSelectedIdx();  //得到选中行的下标
		var cus_id = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
		var guaranty_no = MortGuarantyBaseInfo.guaranty_no._getValue();//得到押品编号
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var delet = jsonstr.delet;
						if("true" == delet){
							
							var data = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
							tmp_var.replace(data,"");
							MortCommenOwnerList._obj._deleteRow(idx);   //删除选中行
							MortCommenOwnerList._obj.recordCount -=1; 	//减少总记录数
							alert("已成功删除！");
						}else{
							alert("操作失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteCommenOwnerRecord.do"/>?commen_owner_no='+cus_id+'&guaranty_no='+guaranty_no;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				
				
			} else {
				alert('请先选择一条记录！');
			}
		}
	}
	
	//选择客户POP框返回方法（共有人信息）
	function returnCusCom(data){
		var rowIndexStr=rowIndex;
		var dat = data.cus_id._getValue();
		var ret = tmp_var.indexOf(dat);
		if(ret==-1){
			MortCommenOwnerList._obj.data[rowIndexStr].commen_owner_no._setValue(data.cus_id._getValue());
			MortCommenOwnerList._obj.data[rowIndexStr].commen_owner_no_displayname._setValue(data.cus_name._getValue());
			MortCommenOwnerList._obj.data[rowIndexStr].cert_type._setValue(data.cert_type._getValue());
			MortCommenOwnerList._obj.data[rowIndexStr].cert_code._setValue(data.cert_code._getValue());
			MortCommenOwnerList._obj.data[rowIndexStr].commen_owner_no._obj._renderReadonly(true) ;
			var indx = rowIndexStr+"_view";
			document.getElementById(indx).disabled="disabled";
			MortCommenOwnerList._obj.data[rowIndexStr]
			tmp_var+=data.cus_id._getValue();
		}else{
			alert("此共有人已经存在，请重新选择！！");
			}
	}
	//选择客户POP框返回方法
	function returnCus(data){
		MortGuarantyBaseInfo.cus_id._setValue(data.cus_id._getValue());
		MortGuarantyBaseInfo.cus_id_displayname._setValue(data.cus_name._getValue());
		MortGuarantyBaseInfo.cert_type._setValue(data.cert_type._getValue());
		MortGuarantyBaseInfo.cert_code._setValue(data.cert_code._getValue());
	}
	//选择机构信息返回方法
	function getOrgID(data){
		MortGuarantyBaseInfo.manager_br_id._setValue(data.organno._getValue());
		MortGuarantyBaseInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	};	
	//选择保管机构信息返回方法
	function getOrgIDKeep(data){
		MortGuarantyBaseInfo.MortGuarantyCertiInfo.keep_org_no._setValue(data.organno._getValue());
		MortGuarantyBaseInfo.MortGuarantyCertiInfo.keep_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择经办机构信息返回方法
	function getOrgIDHand(data){
		MortGuarantyBaseInfo.MortGuarantyCertiInfo.hand_org_no._setValue(data.organno._getValue());
		MortGuarantyBaseInfo.MortGuarantyCertiInfo.hand_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择责任人返回方法
	function setconId(data){
		MortGuarantyBaseInfo.manager_id_displayname._setValue(data.actorname._getValue());
		MortGuarantyBaseInfo.manager_id._setValue(data.actorno._getValue());
	};	
	//选择押品类型
	function getReturnValueForGuarantyType(data){
		MortGuarantyBaseInfo.guaranty_type_displayname._setValue(data.label);
		MortGuarantyBaseInfo.guaranty_type._setValue(data.id);
	}
	function changeHeight(){
		var iframeid = document.getElementById("rightframe");
		iframeid.height = "90px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	};
	/*处理权证信息*/
	function doChangeFull(){
		if(MortGuarantyBaseInfo.is_warrant_full._getValue() == '1'){
			//权利完整的情况 处理
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(true);
			MortGuarantyBaseInfo.not_full_resn._obj._renderRequired(false);
			MortGuarantyBaseInfo.not_full_resn._setValue('');
		//	MortGuarantyCertiInfo.guaranty_no._obj._renderHidden(false);						
			MortGuarantyCertiInfo.warrant_type._obj._renderHidden(false);						
			MortGuarantyCertiInfo.warrant_no._obj._renderHidden(false);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderHidden(false);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderHidden(false);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderHidden(false);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderHidden(false);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderHidden(false);	
		//	MortGuarantyCertiInfo.keep_org_no._obj._renderHidden(false);							
		//	MortGuarantyCertiInfo.hand_org_no._obj._renderHidden(false);

		//	MortGuarantyCertiInfo.guaranty_no._obj._renderRequired(true); 							
			MortGuarantyCertiInfo.warrant_type._obj._renderRequired(true); 						
			MortGuarantyCertiInfo.warrant_no._obj._renderRequired(true); 							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderRequired(true); 			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderRequired(true); 			
			MortGuarantyCertiInfo.warrant_trem._obj._renderRequired(true); 						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderRequired(true); 	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderRequired(true); 	
			MortGuarantyCertiInfo.keep_org_no._obj._renderRequired(true); 							
			MortGuarantyCertiInfo.hand_org_no._obj._renderRequired(true); 		
			MortGuarantyCertiInfo.is_main_warrant._setValue("1");					
		}else{
			//权利信息不完整，需要填写原因。其他隐藏
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(false);
			MortGuarantyBaseInfo.not_full_resn._obj._renderRequired(false);
			MortGuarantyBaseInfo.not_full_resn._setValue('');
		//	MortGuarantyCertiInfo.guaranty_no._obj._renderHidden(false);						
			MortGuarantyCertiInfo.warrant_type._obj._renderHidden(true);						
			MortGuarantyCertiInfo.warrant_no._obj._renderHidden(true);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderHidden(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderHidden(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderHidden(true);	
			MortGuarantyCertiInfo.keep_org_no._obj._renderHidden(true);							
			MortGuarantyCertiInfo.hand_org_no._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_name._obj._renderHidden(true);
		//	MortGuarantyCertiInfo.guaranty_no._obj._renderRequired(true); 							
			MortGuarantyCertiInfo.warrant_type._obj._renderRequired(false); 						
			MortGuarantyCertiInfo.warrant_no._obj._renderRequired(false); 							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderRequired(false); 			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderRequired(false); 			
			MortGuarantyCertiInfo.warrant_trem._obj._renderRequired(false); 						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderRequired(false); 	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderRequired(false); 
			MortGuarantyCertiInfo.is_main_warrant._setValue("");				
		//	MortGuarantyCertiInfo.keep_org_no._obj._renderRequired(false); 							
		//	MortGuarantyCertiInfo.hand_org_no._obj._renderRequired(false); 	
		}
	}
	function doChange(){
		 var table = document.getElementById('emp_table_MortCommenOwnerList_table');
		 var button = document.getElementById('tempButton');
		 var div = document.getElementById('commen');
         if(MortGuarantyBaseInfo.is_common_owner._getValue()=='1'){
        	 table.style.display="";
        	 button.style.display="";
        	 div.style.display="";
        	 var add = document.getElementById('button_getAddMortCommenOwner');
        	 var detele = document.getElementById('button_deleteMortCommenOwner');
        	 add.disabled="true";
        	 detele.disabled="true";
         }else{
        	 table.style.display="none";
        	 button.style.display="none";
        	 div.style.display="none";
        	 
         }
	}
	
	function doNext(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("保存成功！");
					var form = document.getElementById("submitForm");
					var guaranty_no = MortGuarantyBaseInfo.guaranty_no._getValue();
					var url = '<emp:url action="getMortGuarantyBaseInfoUpdateTabPage.do"/>?guaranty_no='+guaranty_no;
					url = EMPTools.encodeURI(url);
					form.action = url;
				//	window.location.reload();
				}else{
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById('submitForm');
		var result = MortGuarantyBaseInfo._checkAll();
	    if(result){
		    var is_common_owner = MortGuarantyBaseInfo.is_common_owner._getValue();
		    var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
		    if(is_common_owner==1){
				if(recordCount<1){
					alert("还没有输入共有人信息！");
				}
		    }
			var message = "";
			//循环校验记录的必输项是否录入完成
			for(var i=0;i<recordCount;i++){
				var commen_owner_no = MortCommenOwnerList._obj.data[i].commen_owner_no._getValue();

				if(commen_owner_no == "" ){
					message += "第"+ (i+1) + "条记录";
				}
				if("" != message){
					message = message+"为空！\n";
				}
			}
			if("" != message){
				alert(message);
				return false;
			}

			MortGuarantyBaseInfo._toForm(form);
	    	MortCommenOwnerList._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
	    	var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	   
	
	};
	function doCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var check = jsonstr.check;
				if("false" == check){
					alert("此权证编号已经存在，请重新录入!");
					MortGuarantyBaseInfo.MortGuarantyCertiInfo.warrant_no._setValue("");
				}else{
					alert("此权证编号可用");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var warrant_no = MortGuarantyBaseInfo.MortGuarantyCertiInfo.warrant_no._getValue();

		//权证编号中文传输会乱码，所以使用编码传输
		warrant_no = encodeURIComponent(warrant_no);
		
		var url = '<emp:url action="checkWarrantNo.do"/>?warrant_no='+warrant_no;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}

	function canNotChange(){
		if(MortGuarantyBaseInfo.is_warrant_full._getValue() == '1'){
			//权利完整的情况 处理
			MortGuarantyBaseInfo.is_warrant_full._obj._renderReadonly(true);
	//		MortGuarantyBaseInfo.not_full_resn._obj._renderReadonly(true);
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(true);
		//	MortGuarantyCertiInfo.guaranty_no._obj._renderHidden(false);						
			MortGuarantyCertiInfo.warrant_type._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.warrant_no._obj._renderReadonly(true);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderReadonly(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderReadonly(true);	
		}else{
			//权利信息不完整，需要填写原因。其他隐藏
			MortGuarantyBaseInfo.not_full_resn._obj._renderReadonly(true);
			MortGuarantyCertiInfo.warrant_type._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.warrant_no._obj._renderReadonly(true);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderReadonly(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderReadonly(true);	

		//	MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_type._obj._renderHidden(true);						
			MortGuarantyCertiInfo.warrant_no._obj._renderHidden(true);	
			MortGuarantyCertiInfo.warrant_name._obj._renderHidden(true);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderHidden(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderHidden(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderHidden(true);	

			MortGuarantyBaseInfo.not_full_resn._obj._renderRequired(false);
			MortGuarantyCertiInfo.warrant_type._obj._renderRequired(false); 						
			MortGuarantyCertiInfo.warrant_no._obj._renderRequired(false); 
			MortGuarantyCertiInfo.warrant_name._obj._renderRequired(false);								
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderRequired(false); 			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderRequired(false); 			
			MortGuarantyCertiInfo.warrant_trem._obj._renderRequired(false); 						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderRequired(false); 	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderRequired(false); 	
			MortGuarantyCertiInfo.keep_org_no._obj._renderRequired(false); 							
			MortGuarantyCertiInfo.hand_org_no._obj._renderRequired(false); 
				
			MortGuarantyCertiInfo.warrant_type._setValue("");						
			MortGuarantyCertiInfo.warrant_no._setValue(""); 		
			MortGuarantyCertiInfo.warrant_name._setValue(""); 						
			MortGuarantyCertiInfo.warrant_appro_unit._setValue(""); 			
			MortGuarantyCertiInfo.warrant_appro_date._setValue("");			
			MortGuarantyCertiInfo.warrant_trem._setValue("");						
			MortGuarantyCertiInfo.keep_org_no_displayname._setValue("");
			MortGuarantyCertiInfo.hand_org_no_displayname._setValue("");	
			MortGuarantyCertiInfo.keep_org_no._setValue("");						
			MortGuarantyCertiInfo.hand_org_no._setValue("");
			MortGuarantyCertiInfo.is_main_warrant._setValue("");	
			//MortGuarantyCertiInfo.warrant_cls._setValue("");		
		}
	}

	function doLoad(){
		doChange();
		canNotChange();
		<%if("stay_storage".equals(stay)){%>
		main_tabs.tabs.qz._clickLink();
		<%}%>
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
		var lrisk_type ="<%=lrisk_type%>";
		if(lrisk_type =="20"){//当授信申请为非低风险时，该字段隐藏，默认值为空
			MortGuarantyBaseInfo.non_affi_third._obj._renderReadonly(false);
			MortGuarantyBaseInfo.non_affi_third._obj._renderHidden(true);
			MortGuarantyBaseInfo.non_affi_third._setValue("");
		}
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
	}

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		var image_guaranty_no = MortGuarantyBaseInfo.image_guaranty_no._getValue();
		if(image_guaranty_no != null && image_guaranty_no !=''){
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口，押品共用此接口
		}else{
			alert('影像押品编号不能为空，请确认!');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = MortGuarantyBaseInfo.guaranty_no._getValue();	//押品编号
		data['cus_id'] = MortGuarantyBaseInfo.cus_id._getValue();	//客户编号
		data['prd_id'] = 'ASSURE';	//业务品种
		data['prd_stage'] = MortGuarantyBaseInfo.image_guaranty_no._getValue();	//押品要在prd_stage传影像押品编号
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =MortGuarantyBaseInfo.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup mainTab="main_tab" id="main_tabs">
<emp:tab label="基本信息" id="main_tab" needFlush="true" initial="true">	
	<emp:form id="submitForm" action="updateMortGuarantyBaseInfoRecord.do" method="POST">
		
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="记录抵质押物基本信息" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.image_guaranty_no" label="影像押品编号" maxlength="40" required="false" hidden="true" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_name" label="押品名称" maxlength="100" required="true" colSpan="2"/>
			<emp:pop id="MortGuarantyBaseInfo.cus_id" label="抵质押人客户码" required="true" url="queryAllCusPop.do?cusTypCondition=Com&returnMethod=returnCus" />
			<emp:text id="MortGuarantyBaseInfo.cus_name" label="抵质押人客户名称" maxlength="60" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/> 
			<emp:select id="MortGuarantyBaseInfo.cert_type" label="抵质押人证件类型 " required="true"  dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.cert_code" label="抵质押人证件号码 " required="true" readonly="true"/>
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" required="true" dictname="STD_GUARANTY_TYPE" colSpan="2"/>
			<emp:pop id="MortGuarantyBaseInfo.guaranty_type_displayname" label="押品类型"  readonly="true" url="showDicTree.do?dicTreeTypeId=MORT_TYPE" returnMethod="getReturnValueForGuarantyType" required="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_type" label="押品类型" required="true" hidden="true"/>
			<!-- modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:select id="MortGuarantyBaseInfo.is_common_owner" label="是否有共有人" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()" defvalue="1" colSpan="2"/>
			<emp:select id="MortGuarantyBaseInfo.non_affi_third" label="押品所有权是否为非关联方第三人" hidden="true" required="false" dictname="STD_ZX_YES_NO" />
			<!-- modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
		</emp:gridLayout>
		<div class='emp_gridlayout_title' id="commen">共有人信息&nbsp;</div>
		<div id="tempButton" align="left">
			<emp:button id="getAddMortCommenOwner" label="新增"/>
			<emp:button id="deleteMortCommenOwner" label="删除" op="remove" locked="false"/>
		</div>
		<emp:table icollName="MortCommenOwnerList" pageMode="false" editable="true" url="">
			<emp:text id="optType" label="操作方式" hidden="true" />
			<emp:text id="guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" hidden="false" defvalue="${context.MortGuarantyBaseInfo.guaranty_no}"/>
			<emp:text id="commen_owner_no" label="共有人编号" required="true" readonly="true"/>
			<emp:text id="commen_owner_no_displayname" label="共有人客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:select id="cert_type" label="共有人证件类型 " required="true"  dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="cert_code" label="共有人证件号码 " required="true" readonly="true"/>
		</emp:table>
		
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="权证信息" maxColumn="2">
			<emp:select id="MortGuarantyBaseInfo.is_warrant_full" label="权证信息是否完整" required="true" dictname="STD_ZX_YES_NO" onchange="doChangeFull()"/>
			<emp:textarea id="MortGuarantyBaseInfo.not_full_resn" label="不完整原因" maxlength="600" required="true" colSpan="2" />
			<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true" defvalue="${context.MortGuarantyBaseInfo.guaranty_no}"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type" label="权利证明类型" required="true" dictname="STD_WRR_PROVE_TYPE"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" maxlength="100" required="true" readonly="true" />
			<emp:text id="MortGuarantyCertiInfo.warrant_name" label="权证名称" maxlength="100" required="true"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_appro_unit" label="权利凭证核发单位" maxlength="100" required="false" />
			<emp:date id="MortGuarantyCertiInfo.warrant_appro_date" label="权利凭证核发日期" required="false" />
			<emp:text id="MortGuarantyCertiInfo.warrant_trem" label="权利凭证期限" maxlength="10" />
			<emp:select id="MortGuarantyCertiInfo.is_main_warrant" label="是否主权证" dictname="STD_ZX_YES_NO" hidden="true"/>
			
			<emp:pop id="MortGuarantyCertiInfo.keep_org_no_displayname" label="保管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDKeep" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:pop id="MortGuarantyCertiInfo.hand_org_no_displayname" label="经办机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDHand" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:text id="MortGuarantyCertiInfo.keep_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
			<emp:text id="MortGuarantyCertiInfo.hand_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="其他信息" maxColumn="2">
		    <emp:select id="MortGuarantyBaseInfo.is_takeover" label="是否可转让" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:textarea id="MortGuarantyBaseInfo.other_memo" label="其他说明" maxlength="600" required="true" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="押品状态" maxColumn="2">
		    <emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" required="true" dictname="STD_MORT_STATE" readonly="true" defvalue="1"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" maxColumn="2" title="登记信息">
			<emp:pop id="MortGuarantyBaseInfo.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="MortGuarantyBaseInfo.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="MortGuarantyBaseInfo.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="MortGuarantyBaseInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="MortGuarantyBaseInfo.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="MortGuarantyBaseInfo.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
			<emp:text id="MortGuarantyBaseInfo.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"   />
			<emp:text id="MortGuarantyBaseInfo.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"  />
		</emp:gridLayout>
	</emp:form>
  </emp:tab>
<emp:ExtActTab></emp:ExtActTab>
 	<div align="center">
		<br>
		<%if(!"".equals(one_key) && one_key != null) {%>
			<emp:button id="returnByOneKey" label="返回" />
		<%}else if("tab".equals(tab)||"stay_storage".equals(stay)){ %>
		<%}else{ %>
		<emp:button id="return" label="返回"/>
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</emp:tabGroup>
</body>
</html>
</emp:page>