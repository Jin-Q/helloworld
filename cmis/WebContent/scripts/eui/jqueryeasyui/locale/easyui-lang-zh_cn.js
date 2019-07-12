/**
 * 1.增加dateymbox、numberspinner、timespinner本地化提示信息。
 * 
 * 
 */

if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '共{pages}页';
	$.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = '正在处理，请稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = '确定';
	$.messager.defaults.cancel = '取消';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
	$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
	$.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combo){
	$.fn.combo.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
	$.fn.calendar.defaults.months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.closeText = '关闭';
	$.fn.datebox.defaults.okText = '确定';
	$.fn.datebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.datebox.defaults.formatter = function(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	};
	//解析输入的内容并格式化为yyyy-mm-dd
	$.fn.datebox.defaults.parser = function(s){
		if (!s) return new Date();
		var ss,y,m,d;
		ss = s.split('-');
		if(ss.length==3){
			y = parseInt(ss[0],10);
			m = parseInt(ss[1],10);
			d = parseInt(ss[2],10);
		}else if(s.length>=7){
			y=parseInt(s.substring(0, 4),10);
			m = parseInt(s.substring(4,6),10);
			d = parseInt(s.substring(6),10);
		}else if(s.length==6){
			y=parseInt(s.substring(0, 4),10);
			m = parseInt(s.substring(4,5),10);
			d = parseInt(s.substring(5),10);
		}else if(s.length==5){
			y=parseInt(s.substring(0, 4),10);
			m = parseInt(s.substring(4),10);
			d = 1;
		}
		
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	};
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}
if ($.fn.numberspinner){
	$.fn.numberspinner.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.timespinner){
	$.fn.timespinner.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.dateymbox){
	$.fn.dateymbox.defaults.missingMessage = '该输入项为必输项';
}