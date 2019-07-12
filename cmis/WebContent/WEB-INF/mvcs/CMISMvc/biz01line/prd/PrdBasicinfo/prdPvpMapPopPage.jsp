<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>合同映射关系配置</title>
<style type="text/css">
 .emp_field_width {
	border: 1px solid #b7b7b7;
	text-align: left;
	width:210px
}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	window.onload = function(){
         //加载的时候删除默认请选择提示
		 fromList._obj.element.options.remove(0);
		 sysList._obj.element.options.remove(0);
		 toList._obj.element.options.remove(0);
		 mapList._obj.element.options.remove(0);
	}
		
 	function doAddMap() {
		var i = 0;
		if(fromCheckbox._obj.elements[0].checked) {
	       i++;
		}
		if(toCheckbox._obj.elements[0].checked) {
	       i++;
		}
		if(sysCheckbox._obj.elements[0].checked) {
	       i++;
		}
		if(i!=2) {
	       alert("映射时只能在源表模型、目标模型、系统变量之中三选二，请选择正确的映射！");
		   return;
		}
		
	    var nLen;
		var oOption = document.createElement("OPTION");
		if(toCheckbox._obj.elements[0].checked) {
			nLen=toList._obj.element.options.length;
			for(i=0;i<nLen;i++){
		 		if(toList._obj.element.options(i).selected){
					oOption.text=toList._obj.element.options(i).text;
					oOption.value = "<m t=~"+toList._obj.element.options(i).value+"~ ";
					i=-1;
					break;
		  		}
			}
			if(i!=-1) {
				alert("请选择一个字段做映射！");
			    return ;
			}
		}

		if(fromCheckbox._obj.elements[0].checked) {
			nLen = fromList._obj.element.options.length;
			for(i=0;i<nLen;i++){
		 		if(fromList._obj.element.options(i).selected){
			  		if(toCheckbox._obj.elements[0].checked) {
			    		oOption.text = fromList._obj.element.options(i).text + "=" + oOption.text;
			    		oOption.value += "f=~" + fromList._obj.element.options(i).value+"~/>";
			  		}else{
			    		oOption.text = fromList._obj.element.options(i).text;
			    		oOption.value = "<m t=~"+fromList._obj.element.options(i).value+"~ ";
			  		}
					i=-1;
					break;
		  		}
			}
			if(i > -1){
			   alert("请选择一个字段做映射！");
			   return;
			}
		}

		if(sysCheckbox._obj.elements[0].checked) {
			nLen=sysList._obj.element.options.length;
			for(i=0;i<nLen;i++){
		  		if(sysList._obj.element.options(i).selected){
					oOption.text = sysList._obj.element.options(i).text  + "=" + oOption.text;
					oOption.value += "f=~" + sysList._obj.element.options(i).value+"~/>";
					i=-1;
					break;
		  		}
			}
			if(i!=-1) {
				alert("请选择一个系统变量做映射！");
			    return ;
			}
		}

		nLen = mapList._obj.element.options.length;
		for(i=0;i<nLen;i++){
		  //alert(oOption.value+"\n"+document.all.mapList.options(i).value);
		  	if(oOption.value==mapList._obj.element.options(i).value){
				alert("该映射已经存在，请重新选择!");
				return;
		  	}
		}
		delOption('from');
		delOption('to');
		
		mapList._obj.element.options.add(oOption);
		return;
	  }

	  function delMap() {
		var i,j=0;
		var nLen;
		nLen = mapList._obj.element.options.length;
		for(i=0;i<nLen;i++){
		  if(mapList._obj.element.options(i).selected){
			mapList._obj.element.options.remove(i);
			i--;
			nLen--;
			j=-1;
		  }
		}
		if(j > -1){
		   alert("请先选择一个源表-->目的表的应射字段！");
		   return;
		}	
		return;
	  }
	 function doDelMap() {
			var i,j=0;
			var nLen;
			nLen = mapList._obj.element.options.length;
			for(i=0;i<nLen;i++){
			  if(mapList._obj.element.options(i).selected){
				mapList._obj.element.options.remove(i);
				i--;
				nLen--;
				j=-1;
			  }
			}
			if(j > -1){
			   alert("请先选择一个源表-->目的表的应射字段！");
			   return;
			}	
			return;
		  }	

	  function doClose(){
		  window.close();
       
	  }

	function doSave(){
		if(window.opener['${context.returnMethod}']){
			var i;
			var values = "<tabmap>";
			var nLen = document.all.mapList.options.length;
			for(i=0;i<nLen;i++){
				values += document.all.mapList.options(i).value;
			}
	   		values += "</tabmap>";
	   		window.opener['${context.returnMethod}'](values);
	   		//opener.${context.returnMethod}(values);///执行回调方法
	   		window.close();
        }else{
           alert('未配置相关方法');
        }
	}

	  function getXMLDocObj(str){
		  //第一步替换里面的 ~
		  str = str.replace(/~/g,'"');
		  var xmlDoc = null;
		  if (window.ActiveXObject)
	      {
            xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async= false ;
            xmlDoc.loadXML(str);
	      }
	      // code for Mozilla, Firefox, Opera, etc.
	      else if (document.implementation && document.implementation.createDocument)
	      {
	        var oParser=new DOMParser(); 
	        xmlDoc=oParser.parseFromString(str,"text/xml");

	      }
	      
	      if(xmlDoc == null){
                alert('不支持的浏览器');
		  }
          return xmlDoc;
      }
     /**
     *删除选项方法
     *根据下面的配置自动匹配进行删除
     */
     function delOption(type){
       if(type == 'from'){
    	   if(fromFilter._obj.elements[0].checked){
    		   var  nLen = fromList._obj.element.options.length;
	   			for(i=0;i<nLen;i++){
	   			  if(fromList._obj.element.options(i).selected){
	   			      fromList._obj.element.options.remove(i);
	   				  break;
	   			  }
	   			}
           }
		}else if(type == 'to'){
		 	if(toFilter._obj.elements[0].checked){
		  		var nLen = toList._obj.element.options.length;
				for(i=0;i<nLen;i++){
			  		if(toList._obj.element.options(i).selected){
						toList._obj.element.options.remove(i);
				 		break;
			  		}
				}
		    }
		}else{
			alert('不支持的类型');
		   	return;
		}
     };
     /**
     *根据映射关系过滤掉选择过的源表数据
     */
     function filterSelect(type){
		 var attrCol ;
         var options = null;
         var contiFlg = false;
		 if(type == 'from'){
			 options = fromList._obj.element.options;
			 if(fromFilter._obj.elements[0].checked){
				 attrCol= 'f';
				 contiFlg = true;
			 }else{
				  var dics = page.objectsDefine.dataDics['PRD_APP_DIC_TYPE'];
			   	  clearOptions(options);
				  var j=0;
				  for(enname in dics){
			          options[j] = new Option(dics[enname],enname);
			          options[j].title = dics[enname];
				      j++;
				  };          
			 }
	     }else if(type == 'to'){
	    	 options = toList._obj.element.options;
	    	 if(toFilter._obj.elements[0].checked){
	    		 attrCol= 't';
	    		 contiFlg = true;
		     }else{
 	  			var dics = page.objectsDefine.dataDics['PRD_PVP_DIC_TYPE'];
 	  			clearOptions(options);
 				var j=0;
 				for(enname in dics){		    	  
	   				options[j] = new Option(dics[enname],enname);
	    			options[j].title = dics[enname];
	 				j++;
   		  		}
			 }
		 }else{
			 alert('不支持的类型');
	          return;
	     }
	     if(!contiFlg){return;}
	     var values = "<tabmap>";
		 var nLen = document.all.mapList.options.length;
		 for(i=0;i<nLen;i++){
		   	values += document.all.mapList.options(i).value;
		 }
		 values += "</tabmap>";
	     var xmlDoc = getXMLDocObj(values);
	     var nodes = xmlDoc.getElementsByTagName('m');
	     if(nodes.length == 0){return;}
	     var colValue;
	     for(var i = 0; i<nodes.length;i++){
	    	 colValue = nodes[i].getAttribute(attrCol);
	    	 for(var j = 0; j<options.length;j++){
	    		 if(options[j].value==colValue){
	    			 options.remove(j);
	    			 break;
		    	  }
		     }
		 }
     };
     
     function clearOptions(options){
	     if(options && options.length >0){
	    	 for(var i=options.length-1;i>=0;i--){ 
	    		 options.remove(i); 
	    	} 
	     }	  
	  }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCopTypeCfgRecord.do" method="POST">
		<emp:gridLayout id="CopTypeCfgGroup" title="表单映射配置" maxColumn="4">
		    <emp:checkbox id="fromCheckbox" label="源表模型"></emp:checkbox>
		    <emp:checkbox id="toCheckbox" label="目标表"></emp:checkbox>
		    <emp:checkbox id="sysCheckbox" label="系统变量"></emp:checkbox>
		    <emp:text id="fill01" label="占位符" hidden="true"></emp:text>
			<emp:select id="fromList" label="源表模型" dictname="PRD_APP_DIC_TYPE" size="30"></emp:select>
			<emp:select id="toList" label="目标模型" dictname="PRD_PVP_DIC_TYPE" size="30"></emp:select>
			<emp:select id="sysList" label="系统变量" dictname="STD_ZB_SYS_VAR" size="30"></emp:select>
			<emp:select id="mapList" label="映射关系" dictname="STD_ZB_MAP" multiple="TRUE" size="30" cssElementClass="emp_field_width"></emp:select>
			<emp:checkbox id="fromFilter" label="源表过滤" onclick="filterSelect('from');"></emp:checkbox>
			<emp:checkbox id="toFilter" label="目标表过滤" onclick="filterSelect('to');"></emp:checkbox>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addMap" label="新增映射" />
			<emp:button id="delMap" label="删除映射"/>
			<emp:button id="save" label="确定"/>
			<emp:button id="close" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

