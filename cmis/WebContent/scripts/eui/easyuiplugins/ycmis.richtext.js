/**
 * <p>easyui扩展组件：richtext（大文本框）</p>
 * <ul>$fn.richtext：使用ueditor来实现富文本框</ul>
 * @author wangbin 2014-4-22 10:17:40
 *
 */
(function($){
	$.fn.richtext = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.richtext.methods[options];
			if(fun){
			return fun(this,param);
			}else{//继承validatebox的方法
			return this.validatebox(options,param);
			}
			}
			options=options||{};
		return this.each(function(){
			var state = $.data(this, 'richtext');
			//继承validatebox对象
			if($.fn.validatebox){
				$(this).validatebox(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'richtext', {
					options: $.extend({}, $.fn.richtext.defaults, $.fn.richtext.parseOptions(this), options)
				});
			}
			init(this);
		});
		function init(target){
			var opts=$.data(target, 'richtext').options
			var id=$(target).attr('id');
			var editor=opts.editor;
			//若未指定编辑器则初始化富文本编辑器
			if(!editor){
				editor=new baidu.editor.ui.Editor();
				//设置高度
				editor.setOpt({autoHeightEnabled:false});
				if(opts.onchange){
					editor.addListener("contentChange",function(){
						opts.onchange(editor);
					});
				}
				editor.ready( function( state ) {
					editor.setHeight(opts.height);
					if(opts.disabled||opts.readonly){
						editor.setDisabled();
					}else{
						editor.setEnabled();
					}					
			    } );				
			}
			editor.render(id);
			opts.editor=editor;
			
		}
	}
	/**
	 * richtext继承和扩展的方法
	 */
	$.fn.richtext.methods = {options:function(jq){//取得属性对象
			return $.data(jq[0],"richtext").options;
		},destroy:function(jq){//销毁文本对象
			return jq.each(function(){
				var opts=$.data(this,"richtext").options;
				var editor=opts.editor;
				editor.destroy();
				$(this).validatebox("destroy");
				$(this).remove();
			});
		},
		setValue:function(jq,value){//赋值
			var target=jq[0];
			var opts=$.data(target,"richtext").options;
			var editor=opts.editor;
			editor.ready( function( state ) {
				editor.setContent(value);
				//是否可编辑
				if(opts.disabled||opts.readonly){
					editor.setDisabled();
				}
		    } );
		},
		getValue:function(jq){//取值
			var target=jq[0];
			var opts=$.data(target,"richtext").options;
			var editor=opts.editor;
			return editor.getContent();
		},
		enable:function(jq){//文本靠可用
		return jq.each(function(){
			var opts=$.data(this,"richtext").options;
			var editor=opts.editor;
			return editor.setEnabled();
		});
		},disable:function(jq){//文本框不可用
			return jq.each(function(){
				var opts=$.data(this,"richtext").options;
				var editor=opts.editor;
				return editor.setDisabled();
			});
		},validate:function(jq){//必输检查
			var target=jq[0];
			var opts=$.data(target,"richtext").options;
			var editor=opts.editor;
			var res=true;
			if(opts.required){
				res=editor.hasContents();
			}
			if(!res){
				 $(editor.body).addClass('validatebox-invalid');
			}else{
				 $(editor.body).removeClass('validatebox-invalid');
			}
			return res;
		},focus:function(jq){//获得焦点
			var target =jq[0];
			var opts=$.data(target,"richtext").options;
			var editor=opts.editor;
			editor.focus();
		}
	};


	$.fn.richtext.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.fn.validatebox.parseOptions(target), {required:(t.attr("required")?true:undefined),disabled:(t.attr("disabled")?true:undefined),readonly:(t.attr("readonly")?true:undefined)});
	};
	/**
	 * richtext继承自validatebox
	 */
	$.fn.richtext.defaults = $.extend({}, $.fn.validatebox.defaults, {
		editor:null//富文本编辑器对象
	});
})(jQuery);

