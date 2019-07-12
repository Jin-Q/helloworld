/**
 * 根据指定的scene=pageUrl找到配置信息，再将配置信息转换成类似：CusIndiv.phone._obj._renderRequired(true)
 * 的JS代码
 * @param pageUrl 场景
 * @param sessionId EMP_SID
 * @return
 */

function getExpressInfo(pageUrl,sessionId){
	var url = "getFieldControlExpression.do?&EMP_SID=" + sessionId + "&scene="+pageUrl ;
	
	var handleSuccess = function(o){
		
		if(o.responseText != undefined){
			try {
				var jsonstr = new Array();
				jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("检查审批扩展权限失败："+o.responseText);
				return;
			}

			var iColl = jsonstr["ExpList"];
			
			for(var n=0; n<iColl.length; n++){
				//alert(iColl[n].field_type + "  " + iColl[n].field_name + " " + iColl[n].expr +" "+iColl[n].field_belong);  
				//字段级控制：必填  、只读 、 隐藏
				eval(iColl[n].expr);

				//是否征信字段 1：是 、2：否
				var field_belong = iColl[n].field_belong

				////如果是征信字段，在后面加上*号
				if(typeof(field_belong)!="undefined" && field_belong=="1"){
					ele = document.getElementById(iColl[n].table_model+"."+iColl[n].field_name);  
					ele.insertAdjacentHTML('AfterEnd','<span>*</span>');     
				}
			}
		}
	};
	var handleFailure = function(o){	
	};
	var callback = {
			success:handleSuccess,
			failure:handleFailure
	};
	var obj1 = YAHOO.util.Connect.asyncRequest('post', url, callback);	
};