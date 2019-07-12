<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head> 
<jsp:include page="/EUIInclude.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
<body class="page_content" >
		<form id="updateDetailForm" method="post">
	<emp:panel id="p1" title="风险分类认定从表">
		<emp:gridLayout id="detailDataForm" maxColumn="2" hiddenBorder="true">
   			<emp:text id="RscTaskInfoSub.pk_id" label="主键" maxLength="40" required="true"  />
   			<emp:text id="RscTaskInfoSub.serno" label="风险分类认定编号" maxLength="40" required="true"  />
   			<emp:text id="RscTaskInfoSub.identy_duty" label="认定岗位" maxLength="80" required="false"  />
   			<emp:select id="RscTaskInfoSub.class_adjust_rst" label="分类调整结果  STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT" required="false"  />
   			<emp:text id="RscTaskInfoSub.remark" label="调整理由" maxLength="400" required="false"  />
   			<emp:date id="RscTaskInfoSub.class_date" label="分类日期" required="false"  />
		</emp:gridLayout>
	</emp:panel>
	</form>	
	<div id="dlg-buttons" align="center">        
		<emp:button text="保存" id="save" iconCls="icon-ok" op="add"></emp:button>
		<emp:button text="取消" id="close" iconCls="icon-cancel"></emp:button>
	</div>
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var updateDetailForm=$('#updateDetailForm');
	
	
	//初始化
	$(function(){
						$('#RscTaskInfoSub\\.pk_id').renderReadonly(true);
						var loadurl="<emp:url action='getRscTaskInfoSubUpdateData.do'/>?"+ 'RscTaskInfoSub.pk_id='+'${param.pk_id}' 
 ;
			loadurl=EMPTools.encodeURI(loadurl);
			updateDetailForm.form('iload',loadurl);	// load from URL
			url="<emp:url action='updateRscTaskInfoSubData4kcoll.do'/>";
	});
	
	//保存
	function doSave(){
		var result=$('#detailDataForm').checkAll();
		if(!result){
			return false;
		} else {
			var formData = $('#updateDetailForm').toJsonData();
			$.ajax({ 
				type: "POST", 
				dataType : 'html',
				url: "<emp:url action='updateRscTaskInfoSubData4kcoll.do'/>",
				data: formData, 
				success: function(data) { 
					try {
						var jsonstr = eval("("+data+")");
					} catch(e) {
						EMP.alertException(data);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("更新成功！"); 
						window.location.reload();
					} else {
						alert("更新失败！");
					}
				}
			});
		}
	}
	
	//关闭
	function doClose(){
		window.parent.EMP.closewin(); 
	}

	</script>
</body>
</html>
</emp:page>