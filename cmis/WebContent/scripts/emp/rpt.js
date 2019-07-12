
/***********依赖关系************/
if (!EMP.field.Base) {
	alert('脚本[rpt.js]需要[data.js]的支持！');	
} 
if (!EMP.field.Text) {
	alert('脚本[rpt.js]需要[fields.js]的支持！');	
}
if (!EMP.type.Base) {
	alert('脚本[rpt.js]需要[dataType.js]的支持！');	
} 


/****************报表数据项显示样式************************/
if (!EMP.field.RPTBase) {

	EMP.field.RPTBase = function() {
	};
	
	EMP.field.RPTBase.prototype = new EMP.field.Base();
}

/****************报表数据项可编辑样式************************/
if (!EMP.field.RPTText) {

	EMP.field.RPTText = function() {
	};
	
	EMP.field.RPTText.prototype = new EMP.field.Text();
}


