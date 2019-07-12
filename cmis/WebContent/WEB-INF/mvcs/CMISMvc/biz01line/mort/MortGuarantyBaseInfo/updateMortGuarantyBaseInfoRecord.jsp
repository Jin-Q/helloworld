<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String guar_cont_no = "";
	String tab ="";
	if(context.containsKey("tab")){//用来标识不同菜单：hwdj--则为从货物登记菜单来的操作。
		tab = (String)context.getDataValue("tab");
	}
	String flag= "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	String menuId = (String)context.getDataValue("menuId");
	//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 start
	String is_warrant_upflag= "";
	if(context.containsKey("is_warrant_upflag")){
		is_warrant_upflag = (String)context.getDataValue("is_warrant_upflag");
	}
	//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 end
	/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
	String lrisk_type="";
	if(context.containsKey("lrisk_type")){//判断授信申请风险类型，用于控制【押品所有权是否为非关联方第三人】字段
		lrisk_type = (String)context.getDataValue("lrisk_type");
	}
	/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
%>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	rowIndex = 0;
	var tmp_var = "";
	var warrantNoOld="";//修改之前的权证编号
	var warrantTypeOld=""//修改之前的权证类型
	//增加共有人信息记录
	function doGetAddMortCommenOwner(){
		var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
		MortCommenOwnerList._obj._addRow();
		MortCommenOwnerList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
		MortCommenOwnerList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var row = recordCount-1;
		var id = row + '_view';//每一个按钮id都是固定的
		MortCommenOwnerList._obj.data[recordCount-1].commen_owner_no._obj.addOneButton(id,'选择',queryCus);
	}

	//加载共有人选择页面
	function queryCus(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var url = '<emp:url action="queryAllCusPop.do?cusTypCondition=(cus_status=20 or cus_status=04)"/>&returnMethod=returnCusCom';
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'queryCus',param);
	}
	//删除共有人记录
	function doDeleteMortCommenOwner(){
		var dataRow =  MortCommenOwnerList._obj.getSelectedData()[0];
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var optType = dataRow.optType._getValue() ;

				var idx = MortCommenOwnerList._obj.getSelectedIdx();  //得到选中行的下标
				var data = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
				tmp_var=tmp_var.replace(data,"");
				dataRow.guaranty_no._obj._renderHidden(true) ;
				dataRow.commen_owner_no._obj._renderHidden(true) ;
				dataRow.commen_owner_no_displayname._obj._renderHidden(true) ;
				dataRow.cert_type._obj._renderHidden(true) ;
				dataRow.cert_code._obj._renderHidden(true) ;
				dataRow.displayid._obj._renderHidden(true) ;
				
				if(optType == 'add'){
					dataRow.optType._setValue("del") ;
				}else if(optType == 'del'){
					dataRow.optType._setValue("del") ;
				}else if(optType == ''){
					dataRow.optType._setValue("del");
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doDeleteMortCommenOwner1(){
		
		var dataRow =  MortCommenOwnerList._obj.getSelectedData()[0];
		var idx = MortCommenOwnerList._obj.getSelectedIdx();  //得到选中行的下标
		var cus_id = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
		var guaranty_no = MortGuarantyBaseInfo.guaranty_no._getValue();//得到押品编号
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				if(cus_id==""){
					var data = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
					tmp_var=tmp_var.replace(data,"");
					MortCommenOwnerList._obj._deleteRow(idx);   //删除选中行
					MortCommenOwnerList._obj.recordCount -=1; 	//减少总记录数
				}else{
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
								tmp_var=tmp_var.replace(data,"");
								MortCommenOwnerList._obj._deleteRow(idx);   //删除选中行
								MortCommenOwnerList._obj.recordCount -=1; 	//减少总记录数
								alert("已成功删除！");
							}else{
								var data = MortCommenOwnerList._obj.data[idx].commen_owner_no._getValue();//得到选中行的客户码
								tmp_var=tmp_var.replace(data,"");
								MortCommenOwnerList._obj._deleteRow(idx);   //删除选中行
								MortCommenOwnerList._obj.recordCount -=1; 	//减少总记录数
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
				}
			}
		} else {
			alert('请先选择一条记录！');
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
		MortGuarantyBaseInfo.cus_name._setValue(data.cus_name._getValue());
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
		MortGuarantyCertiInfo.keep_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.keep_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择经办机构信息返回方法
	function getOrgIDHand(data){
		MortGuarantyCertiInfo.hand_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.hand_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择责任人返回方法
	function setconId(data){
		MortGuarantyBaseInfo.manager_id_displayname._setValue(data.actorname._getValue());
		MortGuarantyBaseInfo.manager_id._setValue(data.actorno._getValue());
		MortGuarantyBaseInfo.manager_br_id._setValue(data.orgid._getValue());
		MortGuarantyBaseInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//MortGuarantyBaseInfo.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};	

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					MortGuarantyBaseInfo.manager_br_id._setValue(jsonstr.org);
					MortGuarantyBaseInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					MortGuarantyBaseInfo.manager_br_id._setValue("");
					MortGuarantyBaseInfo.manager_br_id_displayname._setValue("");
					MortGuarantyBaseInfo.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = MortGuarantyBaseInfo.manager_id._getValue();
					MortGuarantyBaseInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam" == flag){
					MortGuarantyBaseInfo.manager_br_id._setValue("");
					MortGuarantyBaseInfo.manager_br_id_displayname._setValue("");
					MortGuarantyBaseInfo.manager_br_id_displayname._obj._renderReadonly(false);
					MortGuarantyBaseInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = MortGuarantyBaseInfo.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
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
	function doChangeFull(){
		if(MortGuarantyBaseInfo.is_warrant_full._getValue() == '1'){
			//权利完整的情况 处理
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(true);
			MortGuarantyBaseInfo.not_full_resn._obj._renderRequired(false);
			MortGuarantyBaseInfo.not_full_resn._setValue('');
			MortGuarantyCertiInfo.warrant_type._obj._renderHidden(false);						
			MortGuarantyCertiInfo.warrant_no._obj._renderHidden(false);
			MortGuarantyCertiInfo.warrant_name._obj._renderHidden(false);							
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
			MortGuarantyCertiInfo.warrant_name._obj._renderRequired(false);								
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderRequired(true); 			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderRequired(true); 			
			MortGuarantyCertiInfo.warrant_trem._obj._renderRequired(false); 						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderRequired(true); 	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderRequired(true); 	
			MortGuarantyCertiInfo.keep_org_no._obj._renderRequired(true); 							
			MortGuarantyCertiInfo.hand_org_no._obj._renderRequired(true); 		
			MortGuarantyCertiInfo.is_main_warrant._setValue("1");	
			MortGuarantyCertiInfo.warrant_cls._setValue("1");				
		}else{
			//权利信息不完整，需要填写原因。其他隐藏
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(false);
			MortGuarantyBaseInfo.not_full_resn._obj._renderRequired(false);
			MortGuarantyBaseInfo.not_full_resn._setValue('');
			MortGuarantyCertiInfo.warrant_type._obj._renderHidden(true);						
		    MortGuarantyCertiInfo.warrant_no._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_name._obj._renderHidden(true);								
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderHidden(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderHidden(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderHidden(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderHidden(true);	
			MortGuarantyCertiInfo.keep_org_no._obj._renderHidden(true);							
			MortGuarantyCertiInfo.hand_org_no._obj._renderHidden(true);

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
			MortGuarantyCertiInfo.warrant_cls._setValue("");		
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
         }else{
        	 table.style.display="none";
        	 button.style.display="none";
        	 div.style.display="none";
         }
	}
	function doLoad(){
		//将所有共有人客户码赋值给tmp_var
		var num = MortCommenOwnerList._getSize();
		for(var i=0;i<num;i++){
			tmp_var += MortCommenOwnerList[i].commen_owner_no._getValue();
			MortCommenOwnerList.emp_field_displayid
		}
		MortCommenOwnerList.emp_field_displayid
		var flag ="<%=flag%>";
		if(flag=="hwdj")
		{
			MortGuarantyBaseInfo.guaranty_cls._setValue("2");
			MortGuarantyBaseInfo.guaranty_cls._obj._renderReadonly(true);
			MortGuarantyBaseInfo.guaranty_type_displayname._obj.config.url="<emp:url action='showCatalogManaTree.do'/>&isMin=N&value=N";
		}
		warrantNoOld=MortGuarantyCertiInfo.warrant_no._getValue();
		warrantTypeOld=MortGuarantyCertiInfo.warrant_type._getValue();
		doChange();
		canNotChange();
	//	doChangeFull();
		document.getElementById("main_tabs").href="javascript:reLoad();";
		//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 start
		var is_warrant_upflag ="<%=is_warrant_upflag%>";
		if(is_warrant_upflag=="2")
		{
			MortGuarantyCertiInfo.warrant_type._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.warrant_no._obj._renderReadonly(true);	
			MortGuarantyCertiInfo.warrant_name._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderReadonly(true);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderReadonly(true);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderReadonly(true);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderReadonly(true);	
		}
		//added by yangzy 2015/05/19 押品信息修改页面关于主权证修改校验 end
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
		var lrisk_type ="<%=lrisk_type%>";
		if(lrisk_type =="20"){//当授信申请为非低风险时，该字段隐藏，默认值为空
			MortGuarantyBaseInfo.non_affi_third._obj._renderReadonly(false);
			MortGuarantyBaseInfo.non_affi_third._obj._renderHidden(true);
			MortGuarantyBaseInfo.non_affi_third._setValue("");
		}
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
	}
	function reLoad(){
		var agr_type = '${context.agr_type}';
		var agr_no;
		if("00"==agr_type){//保兑仓
			agr_no='${context.depot_agr_no}';
		}else if("01"==agr_type){//银企商
			agr_no='${context.coop_agr_no}';
		}else if("02"==agr_type){//监管协议
			agr_no='${context.oversee_agr_no}';
		}
		<%if("tab".equals(tab)){ %>
		var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?menuIdTab=${context.menuIdTab}&guaranty_no=${context.MortGuarantyBaseInfo.guaranty_no}&op=update&tab=tab';
		<%}else if("hwgl".equals(menuId)||"hwdj".equals(menuId)){%>
		var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?menuIdTab=${context.menuIdTab}&guaranty_no=${context.MortGuarantyBaseInfo.guaranty_no}&op=update&agr_type='+agr_type+'&agr_no='+agr_no;
		<%}else{%>
		var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?menuIdTab=${context.menuIdTab}&guaranty_no=${context.MortGuarantyBaseInfo.guaranty_no}&op=update';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location = url;
		//window.location.reload();
	}
	function canNotChange(){
		if(MortGuarantyBaseInfo.is_warrant_full._getValue() == '1'){
			//权利完整的情况 处理
			MortGuarantyBaseInfo.is_warrant_full._obj._renderReadonly(true);
	//		MortGuarantyBaseInfo.not_full_resn._obj._renderReadonly(true);
			MortGuarantyBaseInfo.not_full_resn._obj._renderHidden(true);
		//	MortGuarantyCertiInfo.guaranty_no._obj._renderHidden(false);						
		//	MortGuarantyCertiInfo.warrant_type._obj._renderReadonly(true);						
		//	MortGuarantyCertiInfo.warrant_no._obj._renderReadonly(true);							
		//	MortGuarantyCertiInfo.warrant_appro_unit._obj._renderReadonly(true);			
		//	MortGuarantyCertiInfo.warrant_appro_date._obj._renderReadonly(true);			
		//	MortGuarantyCertiInfo.warrant_trem._obj._renderReadonly(true);						
		//	MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderReadonly(true);	
		//	MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderReadonly(true);	
		}else{
			//权利信息不完整，需要填写原因。其他隐藏
			MortGuarantyBaseInfo.not_full_resn._obj._renderReadonly(false);
			MortGuarantyCertiInfo.warrant_type._obj._renderReadonly(false);						
			MortGuarantyCertiInfo.warrant_no._obj._renderReadonly(false);							
			MortGuarantyCertiInfo.warrant_appro_unit._obj._renderReadonly(false);			
			MortGuarantyCertiInfo.warrant_appro_date._obj._renderReadonly(false);			
			MortGuarantyCertiInfo.warrant_trem._obj._renderReadonly(false);						
			MortGuarantyCertiInfo.keep_org_no_displayname._obj._renderReadonly(false);	
			MortGuarantyCertiInfo.hand_org_no_displayname._obj._renderReadonly(false);	

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
			MortGuarantyCertiInfo.warrant_cls._setValue("");		
		}
	}
	function doNext(){
		doCheck();
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
			    	window.location.reload();
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
		var result1 = MortGuarantyCertiInfo._checkAll();
	    if(result&&result1){
	    	var cus_id = MortGuarantyBaseInfo.cus_id._getValue();
		    var is_common_owner = MortGuarantyBaseInfo.is_common_owner._getValue();

		    var recordCount = MortCommenOwnerList._obj.recordCount;//取总记录数
	    	//循环校验记录的必输项是否录入完成
			/*  检查有效记录的字段否为空 */
			var count = 0;
			for(var i=0;i<recordCount;i++){
				var optType = MortCommenOwnerList._obj.data[i].optType._getValue();
				if(optType == "" || optType == "add" ){
					count++;
					var commen_owner_no = MortCommenOwnerList._obj.data[i].commen_owner_no._getValue();
					if( commen_owner_no == "" ){
						alert("第"+ count + "条记录为空！");
						return;
					}
				}
			}
		    if(is_common_owner==1){
				if(count<1){
					alert("还没有输入共有人信息！");
					return;
				}
				if(tmp_var.indexOf(cus_id)!=-1){
					alert("新增共有人与所有权人相同，请重新选择！");
					return;
			    }
		    }
			

			MortGuarantyBaseInfo._toForm(form);
	    	MortCommenOwnerList._toForm(form);
	    	MortGuarantyCertiInfo._toForm(form);
	    	page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var url = '<emp:url action="updateMortGuarantyBaseInfoRecord.do"/>?warrantNoOld='+warrantNoOld+'&warrantTypeOld='+warrantTypeOld;
			url = EMPTools.encodeURI(url);
	    	var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	   
	
	};

	function trim(str)
	{
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	function doCheck(){
		var warrant_no = MortGuarantyCertiInfo.warrant_no._getValue();
		MortGuarantyCertiInfo.warrant_no._setValue(trim(warrant_no));
		
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var check = jsonstr.check;
				var flag = jsonstr.flag;
				if("false" == check){
					alert("此权证类型下权证编号已经存在，请重新录入!");
					MortGuarantyCertiInfo.warrant_no._setValue("");
					MortGuarantyCertiInfo.warrant_type._setValue("");
					return;
				}else{
					if("old"==flag){
						MortGuarantyCertiInfo.flag._setValue("old");
					}else if("new"==flag){
						MortGuarantyCertiInfo.flag._setValue("new");
					}
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var warrant_type = MortGuarantyCertiInfo.warrant_type._getValue();
		//added by yangzy 2015/05/28 押品维护权证信息校验改造 start
		var guaranty_no = MortGuarantyBaseInfo.guaranty_no._getValue();
		//added by yangzy 2015/05/28 押品维护权证信息校验改造 end
		//权证编号中文传输会乱码，所以使用编码传输
		warrantNoOld = encodeURIComponent(warrantNoOld);
		warrant_no = encodeURIComponent(warrant_no);
		//modified by yangzy 2015/05/28 押品维护权证信息校验改造 start
		var url = '<emp:url action="checkWarrantNo.do"/>?warrantNoOld='+warrantNoOld+'&warrantTypeOld='+warrantTypeOld+'&warrant_type='+warrant_type+'&warrant_no='+warrant_no+'&flag=update&guaranty_no='+guaranty_no;
		//modified by yangzy 2015/05/28 押品维护权证信息校验改造 end
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	//返回按钮事件
	function doReturn(){
		<%if("hwdj".equals(flag)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwdj';
		<%}else{%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	//控制关闭按钮
	function window.onbeforeunload(){        
		  //用户点击浏览器右上角关闭按钮
		  if((event.clientX > (document.body.clientWidth-15) && event.clientY<0) || event.altKey){   
			  doClose();
		  }
		  //用户点击任务栏，右键关闭
		  else if(event.clientY > document.body.clientHeight || event.altKey){
			  doClose();
		  }
		} 
	function doClose(){
		window.opener.location.reload();
        window.close();
	}
	//日期校验
	function checkDt(date){
		var start = date._obj.element.value;
		//alert(start);
		var openDay='${context.OPENDAY}';
		if(start!=null && start!="" ){
			var flag = CheckDate1BeforeDate2(start,openDay);
			if(!flag){
				alert("输入的日期要小于等于当前日期！");
			    MortGuarantyCertiInfo.warrant_appro_date._setValue("");
			}else{
		    }
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
	<emp:form id="submitForm" action="updateMortGuarantyBaseInfoRecord.do" method="POST">
		
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="记录抵质押物基本信息" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.image_guaranty_no" label="影像押品编号" maxlength="40" required="false" hidden="true" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_name" label="押品名称" maxlength="100" required="true" colSpan="2"/>
			<emp:pop id="MortGuarantyBaseInfo.cus_id" label="抵质押人客户码" required="true" readonly="true" url="queryAllCusPop.do?cusTypCondition=cus_status in ('20','04')&returnMethod=returnCus" />
			<emp:text id="MortGuarantyBaseInfo.cus_name" label="抵质押人客户名称" maxlength="60" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/> 
			<emp:select id="MortGuarantyBaseInfo.cert_type" label="抵质押人证件类型 " required="true"  dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.cert_code" label="抵质押人证件号码 " required="true" readonly="true"/>
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" required="true" readonly="true" dictname="STD_GUARANTY_TYPE" colSpan="2" />
			<emp:pop id="MortGuarantyBaseInfo.guaranty_type_displayname" label="押品类型"  url="showDicTree.do?dicTreeTypeId=MORT_TYPE&parentNodeId=Z090100" returnMethod="getReturnValueForGuarantyType" required="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_type" label="押品类型" required="true" hidden="true"/>
			<!-- modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:select id="MortGuarantyBaseInfo.is_common_owner" label="是否有共有人" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()" defvalue="1" colSpan="2"/>
			<emp:select id="MortGuarantyBaseInfo.non_affi_third" label="押品所有权是否为非关联方第三人" hidden="true" required="false" dictname="STD_ZX_YES_NO" />
			<!-- modified by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:select id="MortGuarantyBaseInfo.agr_type" label="协议类型" required="false" dictname="STD_ZB_AGR_TYPE" hidden="true" defvalue="${context.agr_type}"/>
			<emp:text id="MortGuarantyBaseInfo.depot_agr_no" label="保兑仓协议编号" maxlength="40" required="false" hidden="true" defvalue="${context.depot_agr_no}"/>
			<emp:text id="MortGuarantyBaseInfo.coop_agr_no" label="银企商协议编号" maxlength="40" required="false" hidden="true" defvalue="${context.coop_agr_no}"/>
			<emp:text id="MortGuarantyBaseInfo.oversee_agr_no" label="监管协议编号" maxlength="40" required="false" hidden="true" defvalue="${context.oversee_agr_no}"/>
			
		</emp:gridLayout>
		<div class='emp_gridlayout_title' id="commen">共有人信息&nbsp;</div>
		<div id="tempButton" align="left">
			<emp:button id="getAddMortCommenOwner" label="新增"/>
			<emp:button id="deleteMortCommenOwner" label="删除" op="remove" locked="false"/>
		</div>
		<emp:table icollName="MortCommenOwnerList" pageMode="false" editable="true" url="">
			<emp:text id="optType" label="操作方式" hidden="true" />
			<emp:text id="guaranty_no" label="押品编号" maxlength="40" required="false" readonly="true" hidden="false" defvalue="${context.MortGuarantyBaseInfo.guaranty_no}"/>
			<emp:text id="commen_owner_no" label="共有人编号" required="false" readonly="true"/>
			<emp:text id="commen_owner_no_displayname" label="共有人客户名称"  required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:select id="cert_type" label="共有人证件类型 " required="false"  dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="cert_code" label="共有人证件号码 " required="false" readonly="true"/>
		</emp:table>
		
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="权证信息" maxColumn="2">
			<emp:select id="MortGuarantyBaseInfo.is_warrant_full" label="权证信息是否完整" required="true" dictname="STD_ZX_YES_NO" onchange="doChangeFull()"/>
			<emp:textarea id="MortGuarantyBaseInfo.not_full_resn" label="不完整原因" maxlength="600" required="false" colSpan="2" />
			<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true" defvalue="${context.MortGuarantyBaseInfo.guaranty_no}"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type" label="权利证明类型" required="true" dictname="STD_WRR_PROVE_TYPE" onchange="doCheck()"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" maxlength="100" required="true" readonly="false" onchange="doCheck()"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_name" label="权证名称" maxlength="100" required="true" readonly="false"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_appro_unit" label="权利凭证核发单位" maxlength="100" required="false" />
			<emp:date id="MortGuarantyCertiInfo.warrant_appro_date" label="权利凭证核发日期" required="false" onblur="checkDt(MortGuarantyCertiInfo.warrant_appro_date)"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_trem" label="权利凭证期限" maxlength="10" />
			<emp:select id="MortGuarantyCertiInfo.is_main_warrant" label="是否主权证" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_cls" label="权证类别" required="false" dictname="STD_WARRANT_TYPE" defvalue="1" hidden="true"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_state" label="权证状态" required="true" dictname="STD_WARRANT_STATUS" readonly="true" defvalue="1" colSpan="2" hidden="true"/>
			<emp:text id="MortGuarantyCertiInfo.flag" label="是否有更改权证编号" maxlength="100" hidden="true"/>
			
			<emp:pop id="MortGuarantyCertiInfo.keep_org_no_displayname" label="保管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDKeep" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:pop id="MortGuarantyCertiInfo.hand_org_no_displayname" label="经办机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDHand" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:text id="MortGuarantyCertiInfo.keep_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
			<emp:text id="MortGuarantyCertiInfo.hand_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="其他信息" maxColumn="2">
		    <emp:select id="MortGuarantyBaseInfo.is_takeover" label="是否可转让" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:textarea id="MortGuarantyBaseInfo.other_memo" label="其他说明" maxlength="600" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="押品状态" maxColumn="2">
		    <emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" required="true" dictname="STD_MORT_STATE" readonly="true" defvalue="1"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortGuarantyBaseInfoGroup" maxColumn="2" title="登记信息">
			<emp:pop id="MortGuarantyBaseInfo.manager_id_displayname" label="责任人" required="true" readonly="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="MortGuarantyBaseInfo.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="false"/>
			<emp:text id="MortGuarantyBaseInfo.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="MortGuarantyBaseInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="MortGuarantyBaseInfo.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:text id="MortGuarantyBaseInfo.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="MortGuarantyBaseInfo.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
			<emp:text id="MortGuarantyBaseInfo.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"   />
			<emp:text id="MortGuarantyBaseInfo.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"  />
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="next" label="修改" />
			<%if("tab".equals(tab)){ %>
			<emp:button id="close" label="关闭"/>
			<%}else{ %>
			<emp:button id="return" label="返回"/>
			<%} %>
			
		</div>
	</emp:form>
</emp:tab>
<emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
	
	 
	
</body>
</html>
</emp:page>

