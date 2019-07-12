/**
 * <p>easyui扩展组件：numtx </p>
 * <ul>扩展于easyui的numberbox组件，通过dType属性，增加数字微调组件在得到/失去焦点时对值的格式化处理
 * 通过扩展$.fn.numtx.dType的数据类型，来丰富处理类型。
 * 注：与formatter属性不冲突。
 * </ul>
 * <ul>属性说明：
 * 	新增属性：
 *  dType：数字组件的数据类型集合，数据类型定义：dTypeName:{focus:function(val{},blur:function(val)}{})}。
 * </ul>
 * @author liucheng3
 * <ul>1、修改百分比，千分比格式化方法. add by zangys at 2014-7-31 18:04:21</ul>
 * <ul>1、增加Rate(利率)格式化方法. add by zangys at 2014-8-5 10:52:21</ul>
 */
(function($){
	$.fn.numtx = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.numtx.methods[options];
			if(fun){
				return fun(this,param);
			}else{//继承numberbox的方法
				return this.numberbox(options,param);
			}
		}
		options=options||{};
		return this.each(function(){
			var state = $.data(this, 'numtx');
			//继承numberbox对象
			if($.fn.numberbox){
				$(this).numberbox(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'numtx', {
					options: $.extend({}, $.fn.numberbox.defaults, options)
				});
			}
			funtionExtend(this);
		});
	};
	
	/**
	 * 功能扩展
	 * 修改改numtx组件的focus、blur事件。
	 */
	function funtionExtend(target){
		var opts=$.data(target,"numberbox").options;
		$(target).unbind("focus.numberbox").unbind("blur.numberbox")
		.unbind(".numtx").bind("focus.numtx",function(){
			var vv=numberboxFocus(target);
			if($(this).val()!=vv){
				$(this).val(vv);
			}
		}).bind("blur.numtx",function(){
			numberboxblur(target,$(this).val());
		})
	}
	
	/**
	 * 得到焦点事件函数
	 */
	function numberboxFocus(target){
		var vv = $.data(target,"numberbox").field.val();
		if(vv){
			var dType=$(target).attr('dType');
			if(dType){
				var dt = $.fn.numtx.dType[dType];
				if(dt)
					vv=dt.focus.call(target,vv);
			}
		}
		return vv;
	}
	
	/**
	 * 失去焦点事件函数
	 */
	function numberboxblur(_3e9,_3ea){
		if(_3ea)
		{
			var dType=$(_3e9).attr('dType');
			if(dType){
				var dt = $.fn.numtx.dType[dType];
				if(dt)
					_3ea=dt.blur.call(_3e9,_3ea);
			}			
		}
		var _3eb=$.data(_3e9,"numberbox");
		var opts=_3eb.options;
		var _3ec=$.data(_3e9,"numberbox").field.val();
		_3ea=opts.parser.call(_3e9,_3ea);
		opts.value=_3ea;
		_3eb.field.val(_3ea);
		$(_3e9).val(opts.formatter.call(_3e9,_3ea));
		if(_3ec!=_3ea){
		opts.onChange.call(_3e9,_3ea,_3ec);
		}
	};
	
	/**
	 * dType类型扩展，用于得到焦点失去焦点时对内容的格式化
	 */
	$.fn.numtx.dType={
		Percent:{//百分比
			focus:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=4;
				}
				return $.mul(val,100,precision);
			},
			blur:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=4;
				}
				return $.div(val,100,precision);
			}
		},
		Permille:{//千分比
			focus:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=6;
				}
				return $.mul(val,1000,precision);
			},
			blur:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=6;
				}
				return $.div(val,1000,precision);
			}
		},
		Rate:{//利率
			focus:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=4;
				}
				return $.mul(val,100,precision);
			},
			blur:function(val){
				var opts=$(this).numberbox('options');
				var precision=opts.precision;
				if(!precision||precision==0){
					precision=4;
				}
				return $.div(val,100,precision);
			}
		}
	};
	$.fn.numtx.methods = {};
	$.parser.plugins.push('numtx');
})(jQuery);
