var returnMethod = "";// 回调方法
var intervalProcess; //自动循环执行时的次数参数
var PAGEOFFICE_PARENT_WINDOW = null;// top打开时的回调使用
// 生成pageoffice页面——用于文档展示
var showInPageOffice = function(jsonData){
	// 文档类型
	var fileType = "doc";
	if(typeof jsonData.fileType != undefined && jsonData.fileType != null && jsonData.fileType != "" && jsonData.fileType != "undefined"){
		fileType = jsonData.fileType;
	}
	
	var url = "EMP_SID=" + empId + "&fileType=" + fileType;
	
	// 要打开的文件在pageoffice_upload_info表中的主键
	if(typeof jsonData.fileId != undefined && jsonData.fileId != null && jsonData.fileId != "" && jsonData.fileId != "undefined"){
		url = url + "&fileId=" + jsonData.fileId;
	}
	// 自定义的文件流获得的.do方法，支持后面跟参数
	if(typeof jsonData.fileAction != undefined && jsonData.fileAction != null && jsonData.fileAction != "" && jsonData.fileAction != "undefined"){
		url = url + "&fileAction=" + escape(jsonData.fileAction);
	}
	// 若要更新打开的数据，则设置此值与fileId的值相同
	if(typeof jsonData.fileInfoId != undefined && jsonData.fileInfoId != null && jsonData.fileInfoId != "" && jsonData.fileInfoId != "undefined"){
		url = url + "&fileInfoId=" + jsonData.fileInfoId;
	}
	
	// 通过文件物理路径加载文件时的文件绝对物理路径
	if(typeof jsonData.fileUrl != undefined && jsonData.fileUrl != null && jsonData.fileUrl != "" && jsonData.fileUrl != "undefined"){
		url = url + "&fileUrl=" + jsonData.fileUrl;
	}
	
	// 文件编辑保存路径
	if(typeof jsonData.filePath != undefined && jsonData.filePath != null && jsonData.filePath != "" && jsonData.filePath != "undefined"){
		url = url + "&filePath=" + jsonData.filePath;
	}
	
	// 文件保存后的回调方法
	if(typeof jsonData.returnMethod != undefined && jsonData.returnMethod != null && jsonData.returnMethod != "" && jsonData.returnMethod != "undefined"){
		returnMethod = jsonData.returnMethod;
		url = url + "&returnMethod=" + jsonData.returnMethod;
	}
	
	// 新的文件名
	if(typeof jsonData.newFileName != undefined && jsonData.newFileName != null && jsonData.newFileName != "" && jsonData.newFileName != "undefined"){
		url = url + "&newFileName=" + jsonData.newFileName;
	}
	
	// 是否只读
	var isReadOnly = false;
	if(typeof jsonData.isReadOnly != undefined && jsonData.isReadOnly != null && jsonData.isReadOnly != "" && jsonData.isReadOnly != "undefined"){
		isReadOnly = jsonData.isReadOnly;
	}
	url = url + "&isReadOnly=" + isReadOnly;
	
	// 是否安全限制
	var isSecurityLimit = false;
	if(typeof jsonData.isSecurityLimit != undefined && jsonData.isSecurityLimit != null && jsonData.isSecurityLimit != "" && jsonData.isSecurityLimit != "undefined"){
		isSecurityLimit = jsonData.isSecurityLimit;
	}
	url = url + "&isSecurityLimit=" + isSecurityLimit;
	
	// 是否全屏差查看
	var isFullScreen = false;
	if(typeof jsonData.isFullScreen != undefined && jsonData.isFullScreen != null && jsonData.isFullScreen != "" && jsonData.isFullScreen != "undefined"){
		isFullScreen = jsonData.isFullScreen;
	}
	url = url + "&isFullScreen=" + isFullScreen;
	
	// 防止url路径中有中文出现乱码，所以进行二次编码，取值时需要进行解码
	url = encodeURI(EMPTools.encodeURI(url));
	// 判断浏览器类型
	if(checkChromeVersion()){
		// chrome 43及以上版本，firefox 50及以上版本的浏览器
		// 如果是需要IE内核打开，则需要定义此属性
		url = url + "&useIECore=true";
		url = url + "&actionId=" + "getPageOfficePage.do";
		url = "doSkipForPOL.do?" + url;
		// 查询数据保存后返回的参数
		if(isReadOnly === true || isReadOnly === 'true' || isSecurityLimit === true || isSecurityLimit === 'true'){
			// 不执行session查询
		} else {
			intervalProcess = setInterval("getSessionAfterSave()", 1000);
		}
		var arr,reg = new RegExp("(^| )" + "JSESSIONID" + "=([^;]*)(;|$)");
		if(arr = document.cookie.match(reg)){
			url = url + "&JSESSIONID=" + unescape(arr[2]);
		}
		url = doForChromeOpen(url, isFullScreen, 0, 300);
		window.location.href = url;
	} else {
		url = "getPageOfficePage.do?" + url;
		// 此方式打开的目的是为了防止无法回到原主页面
		var ob = {
			title : '文件在线处理',
			url : url,
			draggable : true,
			modal : true,
			maximized : true
		};
		top.PAGEOFFICE_PARENT_WINDOW = window;
		top.EMP.createwin(ob);
	}
};

// 生成pageoffice页面--用于文档合并
var createPageOffice = function(jsonData){
	var showInPage = "true";
	if(jsonData.showInPage === false || jsonData.showInPage === 'false'){
		showInPage = "false";
	}
	var url = "EMP_SID=" + empId + "&showInPage=" + showInPage;
	
	if(typeof jsonData.isReadOnly != undefined && jsonData.isReadOnly != null && jsonData.isReadOnly != "" && jsonData.isReadOnly != "undefined"){
		url = url + "&isReadOnly=" + jsonData.isReadOnly;
	}
	
	if(typeof jsonData.isSecurityLimit != undefined && jsonData.isSecurityLimit != null && jsonData.isSecurityLimit != "" && jsonData.isSecurityLimit != "undefined"){
		url = url + "&isSecurityLimit=" + jsonData.isSecurityLimit;
	}
	
	if(typeof jsonData.fileId != undefined && jsonData.fileId != null && jsonData.fileId != "" && jsonData.fileId != "undefined"){
		url = url + "&fileId=" + jsonData.fileId;
	}
	
	// 若要更新打开的数据，则设置此值与fileId的值相同
	if(typeof jsonData.fileInfoId != undefined && jsonData.fileInfoId != null && jsonData.fileInfoId != "" && jsonData.fileInfoId != "undefined"){
		url = url + "&fileInfoId=" + jsonData.fileInfoId;
	}
	
	if(typeof jsonData.mergeTempletUrl != undefined && jsonData.mergeTempletUrl != null && jsonData.mergeTempletUrl != "" && jsonData.mergeTempletUrl != "undefined"){
		url = url + "&mergeTempletUrl=" + jsonData.mergeTempletUrl;
	}
	
	// 自定义的文件流获得的.do方法，支持后面跟参数
	if(typeof jsonData.fileAction != undefined && jsonData.fileAction != null && jsonData.fileAction != "" && jsonData.fileAction != "undefined"){
		url = url + "&fileAction=" + escape(jsonData.fileAction);
	}
	
	var isFullScreen = false;// 是否全屏差查看
	if(typeof jsonData.isFullScreen != undefined && jsonData.isFullScreen != null && jsonData.isFullScreen != "" && jsonData.isFullScreen != "undefined"){
		isFullScreen = jsonData.isFullScreen;
	}
	url = url + "&isFullScreen=" + isFullScreen;
	
	if(typeof jsonData.returnMethod != undefined && jsonData.returnMethod != null && jsonData.returnMethod != "" && jsonData.returnMethod != "undefined"){
		returnMethod = jsonData.returnMethod;
	}
	url = url + "&returnMethod=" + returnMethod;
	
	if(typeof jsonData.newFileName != undefined && jsonData.newFileName != null && jsonData.newFileName != "" && jsonData.newFileName != "undefined"){
		url = url + "&newFileName=" + jsonData.newFileName;
	}
	
	if(typeof jsonData.implClass != undefined && jsonData.implClass != null && jsonData.implClass != "" && jsonData.implClass != "undefined"){
		url = url + "&implClass=" + jsonData.implClass;
	}
	
	if(typeof jsonData.fileUrls != undefined && jsonData.fileUrls != null && jsonData.fileUrls != "" && jsonData.fileUrls != "undefined"){
		var setSessionUrl = "setFileDataToSession.do?EMP_SID=" + empId;
		// 将fileUrls放到session中处理
		$.ajax({
	        type: "POST", 
	        url: setSessionUrl,
	        dataType: "html",
	        async: false,
	        data: {fileUrls:jsonData.fileUrls},
	        success: function(data) {
	            try {
	                var jsonstr = eval("(" + data + ")");
	            } catch(e) {
	                EMP.alertException(data);
	                return;
	            }
	        }
	    })
	} 
	
	// 防止url路径中有中文出现乱码，所以进行二次编码，取值时需要进行解码
	url = encodeURI(EMPTools.encodeURI(url));
	// 判断浏览器类型
	if(checkChromeVersion()){
	    // chrome 43及以上版本，firefox 50及以上版本的浏览器
	    // 如果是需要IE内核打开，则需要定义此属性
	    url = url + "&useIECore=true";
	    url = url + "&actionId=" + "fileMakerMergePage.do";
	    url = "doSkipForPOL.do?" + url;
	    // 查询数据保存后返回的参数
	    if(isReadOnly === true || isReadOnly === 'true' || isSecurityLimit === true || isSecurityLimit === 'true'){
			// 不执行session查询
		} else {
			intervalProcess = setInterval("getSessionAfterSave()", 1000);
		}
	    var arr,reg = new RegExp("(^| )" + "JSESSIONID" + "=([^;]*)(;|$)");
		if(arr = document.cookie.match(reg)){
			url = url + "&JSESSIONID=" + unescape(arr[2]);
		}
	    url = doForChromeOpen(url, isFullScreen, 0, 300);
		window.location.href = url;
	} else {
		url = "fileMakerMergePage.do?" + url;
		if(showInPage === 'true'){
			var ob = {
				title : '文件在线处理',
				url : url,
				draggable : true,
				modal : true,
				maximized : true
			};
			top.EMP.createwin(ob);
			top.PAGEOFFICE_PARENT_WINDOW = window;
		} else {
			// 此方式打开的目的是为了防止无法回到原主页面
			var ob = {
					title : '文件在线处理',
					url : url,
					draggable : true,
					modal : true,
					maximized : true
			};
			top.EMP.createwin(ob);
			top.PAGEOFFICE_PARENT_WINDOW = window;
		}
	}
};

// 局部可写的Pageoffice页面打开
var localProtectInPageOffice = function(jsonData){
	// 文档类型
	var fileType = "doc";
	if(typeof jsonData.fileType != undefined && jsonData.fileType != null && jsonData.fileType != "" && jsonData.fileType != "undefined"){
		fileType = jsonData.fileType;
	}
	
	var url = "EMP_SID=" + empId + "&fileType=" + fileType;
	
	// 要打开的文件在pageoffice_upload_info表中的主键
	if(typeof jsonData.fileId != undefined && jsonData.fileId != null && jsonData.fileId != "" && jsonData.fileId != "undefined"){
		url = url + "&fileId=" + jsonData.fileId;
	}
	
	// 若要更新打开的数据，则设置此值与fileId的值相同
	if(typeof jsonData.fileInfoId != undefined && jsonData.fileInfoId != null && jsonData.fileInfoId != "" && jsonData.fileInfoId != "undefined"){
		url = url + "&fileInfoId=" + jsonData.fileInfoId;
	}
	
	// 通过文件物理路径加载文件时的文件绝对物理路径
	if(typeof jsonData.fileUrl != undefined && jsonData.fileUrl != null && jsonData.fileUrl != "" && jsonData.fileUrl != "undefined"){
		url = url + "&fileUrl=" + jsonData.fileUrl;
	}
	
	// 自定义的文件流获得的.do方法，支持后面跟参数
	if(typeof jsonData.fileAction != undefined && jsonData.fileAction != null && jsonData.fileAction != "" && jsonData.fileAction != "undefined"){
		url = url + "&fileAction=" + escape(jsonData.fileAction);
	}
	
	// 文件编辑保存路径
	if(typeof jsonData.filePath != undefined && jsonData.filePath != null && jsonData.filePath != "" && jsonData.filePath != "undefined"){
		url = url + "&filePath=" + jsonData.filePath;
	}
	
	// 文件保存后的回调方法
	if(typeof jsonData.returnMethod != undefined && jsonData.returnMethod != null && jsonData.returnMethod != "" && jsonData.returnMethod != "undefined"){
		returnMethod = jsonData.returnMethod;
		url = url + "&returnMethod=" + jsonData.returnMethod;
	}
	
	// 新的文件名
	if(typeof jsonData.newFileName != undefined && jsonData.newFileName != null && jsonData.newFileName != "" && jsonData.newFileName != "undefined"){
		url = url + "&newFileName=" + jsonData.newFileName;
	}
	
	// 是否只读
	var isReadOnly = false;
	if(typeof jsonData.isReadOnly != undefined && jsonData.isReadOnly != null && jsonData.isReadOnly != "" && jsonData.isReadOnly != "undefined"){
		isReadOnly = jsonData.isReadOnly;
	}
	url = url + "&isReadOnly=" + isReadOnly;
	
	// 是否安全限制
	var isSecurityLimit = false;
	if(typeof jsonData.isSecurityLimit != undefined && jsonData.isSecurityLimit != null && jsonData.isSecurityLimit != "" && jsonData.isSecurityLimit != "undefined"){
		isSecurityLimit = jsonData.isSecurityLimit;
	}
	url = url + "&isSecurityLimit=" + isSecurityLimit;
	
	// 是否全屏差查看
	var isFullScreen = false;
	if(typeof jsonData.isFullScreen != undefined && jsonData.isFullScreen != null && jsonData.isFullScreen != "" && jsonData.isFullScreen != "undefined"){
		isFullScreen = jsonData.isFullScreen;
	}
	url = url + "&isFullScreen=" + isFullScreen;
	
	if(typeof jsonData.regionData != undefined && jsonData.regionData != null && jsonData.regionData != "" && jsonData.regionData != "undefined"){
		var json = {};
		// 模拟可编辑表格的数据提交
		var i = 0;
    	for(var key in jsonData.regionData){
    		var _newkey = 'all.dg[' + i + '].input_key';
    		json[_newkey] = key;
    		_newkey = 'all.dg[' + i + '].key_value';
    		json[_newkey] = jsonData.regionData[key];
    	    i++;
    	}

		var setSessionUrl = "setLocalProtectDataToSession.do?EMP_SID=" + empId;
		// 将regionData放到session中处理
		$.ajax({
	        type: "POST", 
	        url: setSessionUrl,
	        dataType: "html",
	        async: false,
	        data: json,
	        success: function(data) {
	            try {
	                var jsonstr = eval("(" + data + ")");
	            } catch(e) {
	                EMP.alertException(data);
	                return;
	            }
	        }
	    })
	} 
	
	// 防止url路径中有中文出现乱码，所以进行二次编码，取值时需要进行解码
	url = encodeURI(EMPTools.encodeURI(url));
	// 判断浏览器类型
	if(checkChromeVersion()){
		// chrome 43及以上版本，firefox 50及以上版本的浏览器
		// 如果是需要IE内核打开，则需要定义此属性
		url = url + "&useIECore=true";
		url = url + "&actionId=" + "writeProtectPage.do";
	    url = "doSkipForPOL.do?" + url;
		// 查询数据保存后返回的参数
		if(isReadOnly === true || isReadOnly === 'true' || isSecurityLimit === true || isSecurityLimit === 'true'){
			// 不执行session查询
		} else {
			intervalProcess = setInterval("getSessionAfterSave()", 1000);
		}
	    var arr,reg = new RegExp("(^| )" + "JSESSIONID" + "=([^;]*)(;|$)");
		if(arr = document.cookie.match(reg)){
			url = url + "&JSESSIONID=" + unescape(arr[2]);
		}
	    url = doForChromeOpen(url, isFullScreen, 0, 300);
		window.location.href = url;
	} else {
		url = "writeProtectPage.do?" + url;
		// 此方式打开的目的是为了防止无法回到原主页面
		var ob = {
			title : '文件在线处理',
			url : url,
			draggable : true,
			modal : true,
			maximized : true
		};
		top.PAGEOFFICE_PARENT_WINDOW = window;
		top.EMP.createwin(ob);
	}
};

// POL方式打开pageoffice时，默认添加的自动获得session中的数据的方法
function getSessionAfterSave(){
	var eleUrl = "getSessionAfterSave.do?EMP_SID=" + empId;
    $.ajax({
        type: "GET", 
        dataType: "html",
        url: eleUrl,
        success: function(data) {
            try {
                var jsonstr = eval("(" + data + ")");
            } catch(e) {
                EMP.alertException(data);
                return;
            }
            var flag = jsonstr.flag;
            if(flag === 'success'){
                var pageMessage = jsonstr.pageMessage;
                var pageData = jsonstr.pageData;
                // 关闭循环查询session数据
                if(checkChromeVersion()){
                	clearInterval(intervalProcess);
                	var evalMethod = "window." + returnMethod + "(flag, pageMessage, pageData)";
                	eval(evalMethod);
                	top.EMP.closewin();
                } else {
                	// 非chrome下的关闭
                	var evalMethod = "window." + returnMethod + "(flag, pageMessage, pageData)";
                	eval(evalMethod);
                	top.EMP.closewin();
                }
            }
        }
    })
};

// 判断是否是pageoffice支持的文件类型
function regFileType(fileType){
	fileType = fileType.toLowerCase();
	if(fileType === 'doc' || fileType === 'docx' || fileType === 'xls' 
		|| fileType === 'xlsx' || fileType === 'pdf' || fileType === 'ppt' 
			|| fileType === 'pptx' || fileType === 'mmp' || fileType === 'vsd'){
		return true;
	} else {
		return false;
	}
}
// 判断是否需要针对chrome 43及以上版本的特殊的打开方式
function checkChromeVersion() {
	//alert(navigator.userAgent);
	var nua = navigator.userAgent;
	var chromePos = nua.toLowerCase().indexOf("chrome/");
	if (chromePos > 0) {
		//alert(nua.substring(chromePos + 7, chromePos + 9));
		var version = Number(nua.substring(chromePos + 7, chromePos + 9));
		if(version > 42){
			return true;
		} else {
			return false;
		}
	} else {
		chromePos = nua.toLowerCase().indexOf("firefox/");
		var version = Number(nua.substring(chromePos + 8, chromePos + 10));
		if(version > 49){
			return true;
		} else {
			return false;
		}
    }
}

/**
 * empUrl：<emp:url>标签生成的路径，包含应用名称
 * isFullScreen：是否全屏
 * width：窗口宽度，单位默认px；eg 1000;为0时，默认全屏
 * heigth：窗口高度，单位默认px；eg 300;
 */
function doForChromeOpen(empUrl, isFullScreen, width, heigth){
	var curWwwPath = window.document.location.href;
	var pathName =  window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPath = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
		
	var url = window.location.protocol + "//" + window.location.host + projectName + "/" + empUrl;
	if(isFullScreen === true || isFullScreen === 'true'){
		width = "0";
	}
	return "PageOffice://|" + url + "|" + width + "px;" + heigth + "px||";
}