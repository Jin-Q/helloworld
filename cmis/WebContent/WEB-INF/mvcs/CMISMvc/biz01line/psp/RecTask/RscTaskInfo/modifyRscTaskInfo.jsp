<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<% 
String op  = request.getParameter("op");
%>
<emp:page>
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
<body class="page_content">
	<emp:tabGroup id="rsc_tab"   mainTab ="maintab" >
		<emp:tab id="maintab" label="风险分类调整">
		<form id="updateDetailForm" method="post">	
			<emp:panel name="p1" title="风险分类调整信息">
				<emp:gridLayout id="detailDataForm" maxColumn="2"                                                                                                              >
					<emp:text id="RscTaskInfo.bill_no" label="借据编号" maxlength="40" required="false" readonly="true" />
						
		   			<emp:text id="RscTaskInfo.cus_id" label="客户编号" maxlength="40" required="true" readonly="true" />
		   			<emp:text id="RscTaskInfo.cus_id_displayname" label="客户名称"  required="false"  readonly="true"/>
		   			<emp:text id="RscTaskInfo.prd_id" label="产品编号" maxlength="40" required="false"  readonly="true"/>
		   			<emp:text id="RscTaskInfo.prd_id_displayname" label="产品名称" required="false"  readonly="true"/>
		   			<emp:select id="RscTaskInfo.cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE" required="true"  readonly="true"/>
		   			<emp:text id="RscTaskInfo.loan_amt" label="贷款金额"  dataType="Currency"  required="true"  readonly="true"/>
		   			<emp:text id="RscTaskInfo.loan_balance" label="贷款余额"  dataType="Currency"  required="false"  readonly="true" />
		   			<emp:date id="RscTaskInfo.start_date" label="起始日期" required="true"  readonly="true"/>
		   			<emp:date id="RscTaskInfo.end_date" label="到期日期" required="true"  readonly="true"/>
		   			<emp:select id="RscTaskInfo.pre_class_rst" label="上期分类结果 " dictname="STD_ZB_NINE_SORT" readonly="true" required="true"  />
		   			<emp:select id="RscTaskInfo.model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" required="true"  readonly="true"/>
					<emp:textarea id="RscTaskInfo.model_class_rea" label="机评理由"   maxlength="400" required="true"   cols="80" rows="5" colSpan="2"/> 
		   			<emp:select id="RscTaskInfo.model_eval_mac" label="模型分类结果  " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter" required="true"  readonly="true" />
		   			<emp:date id="RscTaskInfo.pre_class_date" label="上期分类日期" required="true"  readonly="true" />
		   			<emp:date id="RscTaskInfo.class_date" label="分类日期" required="false"  readonly="true" />
		   			<emp:select id="RscTaskInfo.class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" required="true"  />
		   			<emp:textarea id="RscTaskInfo.remark" label="认定理由" maxlength="400" required="true"   cols="80" rows="5" colSpan="2"/>
		   			<emp:select id="RscTaskInfo.risk_cls_status" label="风险分类状态 " dictname="STD_ZB_RISK_CLS_ST" required="false" hidden="true" />
		   			<emp:text id="RscTaskInfo.serno" label="主键" maxlength="40" required="false"  hidden="true"/>
				</emp:gridLayout>
			</emp:panel>
			<emp:panel name="p2" title="登记信息">
				<emp:gridLayout id="detailDataForm2" maxColumn="2" >
		   			<emp:text id="RscTaskInfo.input_id_displayname" label="登记人"    readonly="true"/>
		   			<emp:text id="RscTaskInfo.input_br_id_displayname" label="登记机构"      readonly="true"/>
					<emp:date id="RscTaskInfo.input_date" label="登记日期"     readonly="true"/>
					<emp:text id="RscTaskInfo.input_id" label="登记人"    readonly="true" hidden="true"/>
		   			<emp:text id="RscTaskInfo.input_br_id" label="登记机构"      readonly="true" hidden="true"/>
		   			<%if("view".equals(op)){ %>
		   			<emp:text id="RscTaskInfo.upd_id_displayname" label="更新人"  readonly="true" hidden="true"/>
		   			<emp:text id="RscTaskInfo.upd_br_id_displayname" label="更新机构"   readonly="true"  hidden="true"/>
		   			<emp:date id="RscTaskInfo.upd_date" label="更新日期"  readonly="true"  hidden="true"/>
		   			<% }%>	
				</emp:gridLayout>
			</emp:panel>
			<div align="center">
			<%if(!"view".equals(op)) {%>
				<emp:button label="保存" id="save"   ></emp:button>
			<% }%>	
				<emp:button label="返回" id="close" ></emp:button>
			</div>
		</form>
		</emp:tab>
		<emp:tab id="RscTaskInfo_Tab" label="分类调整历史" url="getRscTaskInfoHistListPage.do?RscTaskInfoSub.serno=${param.serno}" ></emp:tab>
	</emp:tabGroup>
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var updateDetailForm=$('#updateDetailForm');
	var op = "${param.op}";
	//初始化
	$(function(){
	//		var loadurl="<emp:url action='getRscTaskInfoUpdateData.do'/>?"+ 'RscTaskInfo.serno='+'${param.serno}'+"&op="+'${param.op}'
//						+"&dutyNo="+'${param.dutyNo}' ;
//			loadurl=EMPTools.encodeURI(loadurl);
			if(op=="view"){
				$('#detailDataForm').renderDisabledAll();
			}
		//	url="<emp:url action='updateRscTaskInfoData4kcoll.do'/>";
	});
		/* 	
			贷款形式
			1	新增贷款
			2	收回再贷
			3	借新还旧
			4	资产重组
			5	转入
			6	其他
		 */
	//获取贷款形式		
	updateDetailForm.form({onLoadSuccess:function(){
		var bill_no = $('#RscTaskInfo-bill_no').getValue();
		var loanModal = '';
			//查询贷款形式
			$.ajax({ 
				type: "POST", 
				dataType : 'html',
				url: "<emp:url action='queryLoanModal.do'/>",
				data: {"bill_no":bill_no},
				success: function(data) { 
					try {
						var jsonstr = eval("("+data+")");
					} catch(e) {
						EMP.alertException(data);
						return;
					}
					loanModal = jsonstr.loanModal;
					if(loanModal=="3"){
						makeOptions("RscTaskInfo-class_rst","20,25,30,35,40,45,50",",");
					}
				}
			});
			
			var preClassRst = $('#RscTaskInfo-pre_class_rst').getValue();//上期分类结果
			var modelClassRst = $('#RscTaskInfo-model_class_rst').getValue();//跑批结果
			if(preClassRst=="25"){
				if("30,35,40,45,50".indexOf(modelClassRst)>-1){
					makeOptions("RscTaskInfo-class_rst","25,30,35,40,45,50",",");
				}else{
					var comCde = "";
					var i = parseInt(modelClassRst);
					for(;i<=parseInt(50);){
						comCde +=i+','; 
						i=i+5;
					}
					makeOptions("RscTaskInfo-class_rst",comCde,",");
				}
			}else{
				var comCde = "";
				var i = parseInt(modelClassRst);
				for(;i<=parseInt(50);){
					comCde +=i+','; 
					i=i+5;
				}
				makeOptions("RscTaskInfo-class_rst",comCde,",");
			}
		}
	});
	
	//保存
	function doSave(){
		var result=RscTaskInfo._checkAll() ;
		var duty = '${param.duty}';
		if(!result){
			alert("界面要素未录入完成或格式不合法，请确认！");
			return false;
		} else {
			var form = document.getElementById("updateDetailForm");
			RscTaskInfo._toForm(form);
			var handleSuccess = function(o){
			 
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(e);
					return;
				} 
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("更新成功！");
					window.location.reload();
					 
				} else {
					alert("更新失败！");
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
			var url ="<emp:url action='updateRscTaskInfoData4kcoll.do'/>"+'&duty='+duty+'&type=verify'+'&dutyno=${param.dutyno}';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
 
		}
	}


	
		//关闭
	function doClose(){ 
		var menuId = "${context.menuId}";
		if(menuId=="fxflrd01"){
		    var url = '<emp:url action="getRscTaskInfoListPage.do"/>?menuId='+menuId+'&duty=mgr'; 
		}else{
			 var url = '<emp:url action="getRscTaskInfoUnCusMgrListPage.do"/>?menuId='+menuId; 
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	//查看操作
	function doLookbill(){ 
		var bill_no = $('#RscTaskInfo-bill_no').getValue();
			var url="<emp:url action='getRscTaskInfoListPage.do'/>?menuId=${context.menuId}&duty=mgr";
			url = EMPTools.encodeURI(url);
			window.location = url;
	}
	</script>
</body>
</html>
</emp:page>