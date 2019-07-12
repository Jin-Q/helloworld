/**
 * 修改历史
 * 1. 消费合并:添加getCookie方法
 */
if(!EMPTools){
	var EMPTools = {
		/**
		 * 前端页面最外围的框架
		 */
		topPage : window.top,
		
		/**
		 * 获得当前弹出窗口的父窗口(同时兼容window.open和window.showModelDialog)
		 */
		getWindowOpener : function(winObj) {
			if (!winObj) {
				winObj = window;
			}
			var windowOpener = null;
			if (EMPTools.Browser.ie && EMPTools.useModelDialog) { // IE
				windowOpener = winObj.dialogArguments;
			} else {
				windowOpener = winObj.opener;
			}
			return windowOpener;
		},

		/**
		 * 当前浏览器信息
		 */
		Browser : {
			'name' : 'unknown',
			'version' : ''
		},

		Features : {},
		
		//是否需要校验用户权限[由于按钮升级之后，权限通过jsp标签解析时进行控制，此处不需要再控制权限]
		haveToCheckButtonPermission:false,
		//按钮权限
		renderButtons:function(){},
		/**
		 * 对url进行规整并使用js的encodeURI进行编码
		 */
		encodeURI : function(url) {
			if (url) {
				var reg = new RegExp("(.*)\\?(.*)\\?(.*)");
				url = url.replace(reg, "$1?$2&$3");
				return encodeURI(url);
			}
		},
		/**
		 * 对url进行规整并使用js的encodeURI进行反编码
		 */
		decodeURI : function(url) {
			if (url) {
				return decodeURI(url);
			}
		},
		doExtOrFold : function(div,div2) {
			if (div.style.display == '') {
				div.style.display = 'none';
				div2.className='emp_gridlayout_title_expand';
			} else {
				div.style.display = '';
				div2.className='emp_gridlayout_title';
			}
		},
		getCookie : function(name) {
            var strCookie = document.cookie;
            var arrCookie = strCookie.split("; ");

            for ( var i = 0; i < arrCookie.length; i++) {
                var arr = arrCookie[i].split("=");
                if (arr[0] == name) {
                    return arr[1];
                }
            }
            return "";
        }
	};
	
	/**
	 * 设置当前浏览器信息
	 */
	EMPTools.Features.xhr = !!(window.XMLHttpRequest);
	EMPTools.Features.xpath = !!(document.evaluate);

	if (window.opera)
		EMPTools.Browser.name = 'opera';
	else if (window.ActiveXObject)
		EMPTools.Browser = {
			'name' : 'ie',
			'version' : (EMPTools.Features.xhr) ? 7 : 6
		};
	else if (!navigator.taintEnabled)
		EMPTools.Browser = {
			'name' : 'webkit',
			'version' : (EMPTools.Features.xpath) ? 420 : 419
		};
	else if (document.getBoxObjectFor != null)
		EMPTools.Browser.name = 'gecko';
	EMPTools.Browser[EMPTools.Browser.name] = EMPTools.Browser[EMPTools.Browser.name
			+ EMPTools.Browser.version] = true;
	
	/**
	 * 找到前端页面最外围的框架
	 * 在应用的主框架页面需要添加window.opener = null;用以解决通过其它页面弹出框打开该应用找到错误的父页面，进而按钮权限失效
	 */
	
/*	while (EMPTools.topPage) {
		var openWindow = EMPTools.getWindowOpener(EMPTools.topPage);
		if (openWindow) {
			EMPTools.topPage = openWindow.top;
		} else {
			// alert('未找到主布局页面！');
			break;
		}
	}; */
	
	/**
	 *  设置页面按钮权限，跟据button的op属性来判断
	 */
	EMPTools.renderButtons = function(){
		var buttonList = document.getElementsByTagName("A");
		for (var i = 0; i < buttonList.length; i++) {
			var buttonObj = buttonList[i];
			var op = buttonObj.getAttribute("op");
			if (!op)
				continue;
			if (!EMPTools.haveToCheckButtonPermission) {
				buttonObj.style.display = "";
				continue;
			}
			if (EMPTools.topPage.checkPermission) {
				// 如果页面强制决定了menuId，则由页面定义的menuId决定权限，否则由菜单上的menuId决定
				// (便于极其特殊的情况：一个页面在不同模块中要求拥有不同的权限)
				// alert(this.menuId+";"+op);
				var check = EMPTools.topPage.checkPermission(page.menuId, op);
				if (check)
					buttonObj.style.display = "";
			}
		}
	};
	
	
	 //  获取指定对象的类型。
	EMPTools.type = $.type;

    //  测试对象是否是窗口（有可能是Frame）。
	EMPTools.isWindow = $.isWindow;
	
	/**
	 *  检测一个对象是否为一个数组对象或者类似于数组对（具有数组的访问方式：具有 length 属性、且具有属性名为数字的索引访问器）
	 *  注意：此方法传入 字符串 时执行，也会返回 true，因为 字符串 是一个字符数组。
	 */
	EMPTools.likeArray = function (obj) {
        if (obj == null || obj == undefined || EMPTools.isWindow(obj)) {
            return false;
        }
        if (obj.nodeType === 1 && obj.length) {
            return true;
        }
        var type = EMPTools.type(obj);
        return type === "array" || type !== "function" && $.isNumeric(obj.length) && obj.length >= 0;
    };

    /**
	 *  检测一个对象是否为一个数组对象或者类似于数组对（具有数组的访问方式：具有 length 属性、且具有属性名为数字的索引访问器）且不是字符串
	 */
    EMPTools.likeArrayNotString = function (obj) {
        return EMPTools.likeArray(obj) && !EMPTools.isString(obj);
    };
    
    //  测试对象是否是字符串（String）类型值。
    EMPTools.isString = function (obj) { return EMPTools.type(obj) == "string"; };
    
    //  在指定的毫秒数后调用函数或计算表达式；该函数定义如下参数：
    //      code:       必需。要调用的函数后要执行的 JavaScript 代码串。
    //      millisec:   可选。在执行代码前需等待的毫秒数。
    //  模拟 setTimeout/setImmediate 方法。
    //  备注：如果不传入参数 millisec 或该参数值为 0，则自动调用 setImmediate(该方法相对于 setTimeout 可以有效降低浏览器资源开销) 方法；
    EMPTools.exec = function (code, millisec) {
        if (!code) { return; }
        return !millisec && window.setImmediate ? window.setImmediate(code) : window.setTimeout(code, millisec);
    };
    
    //展示遮罩层
    EMPTools.mask = function() {
        var bd = EMPTools.topPage.$('body');
        $("<div id=\"cmisMaskDiv\" class=\"window-mask\" style=\"display:block;z-index:99998\"></div>").appendTo(bd);
        var msg=$("<div id=\"cmisMaskMsgDiv\" class=\"datagrid-mask-msg\" style=\"display:block;left:50%;z-index:99999\"></div>").html("正在处理，请稍待。。。").appendTo(bd);
        msg.css("marginLeft",-msg.outerWidth()/2);
    };
    
    //去掉遮罩层
    EMPTools.unmask = function() {
        try{
            EMPTools.topPage.$('#cmisMaskMsgDiv').remove();
            EMPTools.topPage.$('#cmisMaskDiv').remove();
        }catch(e){}
    };
    
    /**
     *  根据配置是否禁用鼠标右键，如果禁用了鼠标右键则全局默认禁用，并将 EMPTools.disableRightClickMenu设置为true
     */
    EMPTools.disableRightClickMenu = false ;
    if (typeof (_disableRightClickMenu) != "undefined" && _disableRightClickMenu == "Y") {
        EMPTools.disableRightClickMenu = true;
        document.oncontextmenu=function(){
            return false;
        };
    }
}


