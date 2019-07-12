<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">	
	function doSelect(){
		var data = BizModifyAppList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var cont_cur_type = data[0].cont_cur_type._getValue();//合同币种
			if(cont_cur_type =="CNY"){
				var serno = data[0].serno._getValue();
				var cont_no = data[0].cont_no._getValue();
				var biz_cate = data[0].biz_cate._getValue();
				var form = document.getElementById("submitForm"); 
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
							doSave();
						}else if(flag == "forbidden"){
							alert("只允许出账申请审批状态为【退回】的信息进行修改！");
							return;
						}else {
							alert("存在在途的业务信息维护申请！");
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

				var url = '<emp:url action="queryExistBizModifyInfo.do"/>?serno='+serno+"&cont_no="+cont_no+"&biz_cate="+biz_cate;
				url = EMPTools.encodeURI(url);
				var postData = YAHOO.util.Connect.setForm(form);	
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
			}else{
				alert('打回业务信息维护申请仅限于币种为【人民币】的业务！');
			}
		}else {
			alert('请先选择一条记录！');
		}
		
	};	

	function doSave(){
		var buttonObjWait = document.getElementById('button_select');
		buttonObjWait.disabled=true;
		buttonObjWait.innerHTML='请稍等..';
		var data = BizModifyAppList._obj.getSelectedData();
		var cont_no = data[0].cont_no._getValue(); 
		var biz_cate = data[0].biz_cate._getValue();
		var form = document.getElementById("submitForm"); 
		data[0]._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					buttonObjWait.disabled=false;
					buttonObjWait.innerHTML='下一步';
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var modify_rel_serno = jsonstr.modify_rel_serno;
				if(flag == "success"){
					if(biz_cate == "0011" ||biz_cate == "0012"){
						var url = '<emp:url action="getBizModifyUpdatePage.do"/>?modify_rel_serno='+modify_rel_serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(biz_cate == "016"){
						var url = '<emp:url action="getBizModifyUpdate4IEAPage.do"/>?modify_rel_serno='+modify_rel_serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}else {
					buttonObjWait.disabled=false;
					buttonObjWait.innerHTML='下一步';
					alert("异步请求出错！");
				}
			}
		};
		var handleFailure = function(o){
			buttonObjWait.disabled=false;
			buttonObjWait.innerHTML='下一步';
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url = '<emp:url action="addBizModifyRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
		
</script>
</head>
<body class="page_content">
<emp:form id="submitForm" action="" method="POST">
	<div  class='emp_gridlayout_title'>打回业务信息维护新增操作  </div> 
	</emp:form>
		<emp:table icollName="BizModifyAppList" pageMode="true" url="pageBizModifyAppQuery.do" >
		<emp:text id="serno" label="出账编号" />
		<emp:text id="fount_serno" label="业务申请流水号" hidden="true" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="cont_balance" label="合同余额" dataType="Currency" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="biz_cate" label="业务类型" dictname="ZB_BIZ_CATE"/>
		<emp:text id="modify_rel_serno" label="打回业务修改操作流水号" hidden="true"/>
	</emp:table> 
		<div align="center">
			<br>
			<emp:button id="select" label="下一步" />
		</div>
	
</body>
</html>
</emp:page>

