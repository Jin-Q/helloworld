<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>
<%
String pCusId=request.getParameter("parent_cus_id");
String pCusName = request.getParameter("parent_cus_name");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateCusGrpMemberPage() {
	
		var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
		var pCusId = window.parent.window.CusGrpInfo.parent_cus_id._getValue();
		if (paramStr!=null) {
			var url = '<emp:url action="getCusGrpInfoCusGrpMemberUpdatePage.do"/>?'+paramStr+'&parent_cus_id='+pCusId;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddCusGrpMemberPage(){
		var grp_no = window.parent.window.CusGrpInfo.grp_no._getValue();
		var pCusId = window.parent.window.CusGrpInfo.parent_cus_id._getValue();
		var url = '<emp:url action="getCusGrpInfoCusGrpMemberAddPage.do"/>?CusGrpMember.grp_no='+grp_no+'&parent_cus_id='+pCusId;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteCusGrpMember() {
		
		var data = CusGrpMemberList._obj.getSelectedData();
		if (data != null) {
			var type = data[0].grp_corre_type._getValue();

			var genType = data[0].gen_type._getValue();
			if(type=='3'){
              alert("主关联(集团)公司不允许删除！");
			}else {
				var grp_no = window.parent.window.CusGrpInfo.grp_no._getValue();
				var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
				if (paramStr != null) {
					var url = '<emp:url action = "checkLmtApplyAndLmtModApp.do"/>&grp_no=' + grp_no;;
					var handleSuccess = function(o){
						if(o.responseText != undefined){
							try{
								var jsonstr = eval( "(" + o.responseText + ")" );
							}catch (e) {
				 				alert("删除集团成员校验失败!");
				 			}
							var Msg = jsonstr.backMsg;
							if(Msg == "0"){
								alert("该集团有正在做的关联授信业务，请先完成该集团的关联授信业务!");
								return ;
							}
							if(Msg == "1"){
								alert("该集团有正在做的关联授信变更业务，请先完成该集团的关联授信变更业务!");
								return ;
							}
							if(confirm("是否确认要删除？")){
								if(confirm("将会删除该集团成员客户在集团授信协议向下的台账，重新生成独立的法人授信协议！")){
									var url = '<emp:url action="deleteCusGrpInfoCusGrpMemberRecord.do"/>?'+paramStr;
									url = EMPTools.encodeURI(url);
									window.location = url;
								}
							}
						}else{
							alert("删除集团成员校验失败!");
						}
					};
					var handleFailure = function(o){
				
						alert("删除集团成员校验失败!");
					};
						
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}
					YAHOO.util.Connect.asyncRequest('GET',url,callback); 
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpMember() {
		//var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
		var data = CusGrpMemberList._obj.getSelectedData();
		//var type = data[0].grp_corre_type._getValue();
		var cusId = data[0].cus_id._getValue();
		var cusType = data[0].cus_type._getValue();
		if (cusId!=null) {
			if(cusType.substring(0,1)==1){
				var url = '<emp:url action="getCusIndivViewPage.do"/>?cus_id='+cusId+'&flag=query';
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow','height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				//var url = '<emp:url action="queryCusGrpInfoCusGrpMemberDetail.do"/>?'+paramStr;
				var url = '<emp:url action="getCusComViewPage.do"/>?cus_id='+cusId+'&flag=query';
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow','height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}	
		}else {
			alert('请先选择一条记录！');
		}
	};

	function doGetCusRelation(){
		var pCusId = window.parent.window.CusGrpInfo.parent_cus_id._getValue();
		var pCusName = window.parent.window.CusGrpInfo.parent_cus_name._getValue();
		var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+pCusId+'&cus_name='+pCusName;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}
	
	function doViewCusGrpRe(){
		var paramStr = CusGrpMemberList._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="GetCusRelTreeOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" >

	<div align="left">
		<emp:button id="getAddCusGrpMemberPage" label="新增"/>
		<emp:button id="viewCusGrpMember" label="查看"/>
		<emp:button id="getUpdateCusGrpMemberPage" label="修改"/>
		<emp:button id="deleteCusGrpMember" label="删除"/>
		<emp:button id="viewCusGrpRe" label="客户关联信息查看"/>
	</div>
	<emp:table icollName="CusGrpMemberList" pageMode="true" url="pageCusGrpInfoCusGrpMemberQuery.do" reqParams="CusGrpInfo.grp_no=${context.CusGrpInfo.grp_no}&CusGrpInfo.parent_cus_id=${context.CusGrpInfo.parent_cus_id}">

		<emp:text id="cus_id" label="成员客户码" />
		<emp:text id="cus_name" label="成员客户名称" />
		<emp:text id="cert_code" label="组织机构代码" />
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GROUP_TYPE" hidden="false"/>
		<emp:text id="cus_manager" label="客户经理" hidden="true"/>
		<emp:text id="cus_manager_displayname" label="客户经理" />
		<emp:text id="main_br_id" label="主管机构" hidden="true"/>
		<emp:text id="main_br_id_displayname" label="主管机构" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"  hidden="true"/>
		<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
		<emp:text id="gen_type" label="生成类型" dictname="STD_ZB_GEN_TYPE" hidden="false"/>
	</emp:table>
				
</body>
</html>
</emp:page>