

if (!EMP.widget.RelatedSelectGroup) {
	
	EMP.widget.RelatedSelectGroup = function(groupName, url, needCache) {
		
		this.id = groupName;
		this.url = url;
		
		this.selects = [];
		this.firstSelect = null;
		this.needCache = needCache;
		this.cache = {};
	};
	
	EMP.widget.RelatedSelectGroup.prototype.registRelatedSelect = function(selectName){
		
		var selectObj = eval(selectName+"._obj");
		if(selectObj == null || typeof(selectObj) == 'EMP.field.Select'){
			alert("当前页面中不存在["+selectName+"]下拉框!");
			return;
		}
		
		selectObj.relatedSelectDefine = {};
		selectObj.relatedSelectDefine.group = this;
		if(this.firstSelect == null){
			this.firstSelect = selectObj;
		}
		if(this.selects.length > 0){
			this.selects[this.selects.length - 1].relatedSelectDefine.next = selectObj;
		}
		this.selects[this.selects.length] = selectObj;
		
		//定义缓存的信息
		this.cache[selectObj.dataName] = {initiate : false};
		if(this.needCache){
			this.cache[selectObj.dataName].content = {};
		}
	};
	
	/**
	 * 初始化下拉框的内容
	 */
	EMP.widget.RelatedSelectGroup.prototype.doInitSelectContent = function(selectObj){
		
		var param = '';
		for(var i=0; i<this.selects.length; i++){
			var aSelect = this.selects[i];
			if( aSelect == selectObj )
				break;
			
			var aBox = this.selects[i];
			
			if( param != ''){
				param += "&";
			}
			param += aSelect.dataName + '=' + aSelect.getValue();
		}
		
		if(this.needCache){
			var content = this.cache[selectObj.dataName].content[param];
			if(content != null){
				this.updateSelectInnerHTML(selectObj, content, param);
				return;
			}
		}
		
		var url = this.url;
		if(param != ''){
			url += "?" + param;
		}
		url = EMPTools.encodeURI(url);
		
		var handleSuccess = function(responseText, callback){	
			callback.selectGroup.updateSelectInnerHTML(callback.selectObj, responseText, callback.param);
		};
		var callback = {
			success : handleSuccess,
			isJSON : true,
			handleError : true,
			param : param,
			selectGroup : this,
			selectObj : selectObj
		};
		EMP.util.Tools.ajaxRequest('GET', url, callback);
	};
	
	/**
	 * 更新selectObj下拉框对象的选项
	 */
	EMP.widget.RelatedSelectGroup.prototype.updateSelectInnerHTML = function(selectObj, selectSrc, param){
		var element = selectObj.element;
		
		//保留第一个"请选择"的选项
		element.length = selectSrc.length+1;
		for(var i=0;i<selectSrc.length; i++){
			element.options[i+1].value = selectSrc[i].id;
			element.options[i+1].text = selectSrc[i].label;
		}
		
		//将选择框置到"请选择"的选项上
		var selectCache = this.cache[selectObj.dataName];
		if(this.needCache){//将当前内容进行缓存，如果再次选择时就不需要重新发起请求了
			if(param != null && selectCache.content[param] == null)
				selectCache.content[param] = selectSrc;
		}
		
		if(selectCache.initiate == false){//初始化下拉框的选择(通常用于页面刚刚载入时，显示相应的值)
			selectObj.setValue(selectObj.initValue);
			selectCache.initiate = true;
		}else{
			selectObj.setValue("");
		}
	};
}	