/**
 * 工具类组件
 * 
 * @type
 */
cextUtils = {
	/**
	 * 指定页面加载完成后 执行
	 */
	onDomReady : function(callbackFn, param, win, scope) {
		win = win ? win : window;
		if (Ext.isIE) {
			win.onreadystatechange = function() {
				if (win.readyState && win.readyState == 'complete') {
					callbackFn.apply(scope, param);
				}
			}
		} else {
			win.onload = function() {
				callbackFn.apply(scope, param);
			}
		}
	},
	/**
	 * 错误提示
	 * 
	 * @param{String} title
	 * @param {String}
	 *            msg
	 * @param {enum}
	 *            icon default ERROR
	 *            [OK,INFO,CANCEL,OKCANCEL,QUESTION,YESNO,YESNOCANCEL]
	 */
	MessageBoxError : function(title, msg, icon, fn) {
		var ico = Ext.MessageBox.ERROR;
		if (icon) {
			ico = Ext.MessageBox[icon];
		}
		Ext.MessageBox.show({
					title : title,
					minWidth : 400,
					msg : msg,
					buttons : Ext.MessageBox.OK,
					fn : fn,
					icon : ico
				});
	},
	MessageBoxConfirm : function(title, msg, fn, scope) {
		title = title ? title : '系统提示信息';
		Ext.MessageBox.show({
					title : title,
					msg : msg,
					buttons : Ext.MessageBox.YESNO,
					fn : fn,
					scope : scope,
					icon : Ext.MessageBox.QUESTION,
					minWidth : 400
				});
	},
	/**
	 * 一般提示信息
	 * 
	 * @param {String}
	 *            title
	 * @param {String}
	 *            msg
	 * @param {enum}
	 *            icon default INFO
	 *            [OK,INFO,CANCEL,OKCANCEL,QUESTION,YESNO,YESNOCANCEL]
	 */
	MessageBoxAlert : function(title, msg, icon, fn, width) {
		var ico = Ext.MessageBox.INFO;
		if (icon) {
			ico = Ext.MessageBox[icon];
		}
		width = width ? width : 400
		Ext.MessageBox.show({
					title : title,
					msg : msg,
					buttons : Ext.MessageBox.OK,
					fn : fn,
					width : width,
					icon : ico
				});
	},
	/**
	 * 创建 树过滤 bbar
	 */
	createTreeFilterBar : function(tree, width) {
		width = width ? width : 200;
		var bar = null;
		try {
			bar = new Ext.Toolbar(['-', {
						xtype : "treeFliter",
						tree : tree,
						width : width,
						emptyText : "请输入过滤关键字...."
					}, '->', {
						iconCls : 'icon-expand-all',
						tooltip : '全部展开',
						handler : function() {
							this.root.expand(true);
						},
						scope : tree
					}, {
						iconCls : 'icon-collapse-all',
						tooltip : '全部关闭',
						handler : function() {
							this.root.collapse(true);
						},
						scope : tree
					}, '-']);
		} catch (e) {

		}
		return bar;

	},
	/**
	 * 创建系统 定时 任务提示
	 */
	CreateTaskMgrMsg : function(msg, second, fn, btnfn) {
		var daoshu = second;
		var task;
		var box = Ext.MessageBox.alert('提示信息', msg + '<font color=red><b>'
						+ daoshu + '</b></font> 秒后,将退到登陆页面!', function() {
					Ext.TaskMgr.stop(task);
					Ext.callback(btnfn);
				});
		task = {
			run : function() {
				var tem = daoshu--;
				box.updateText(msg + '<font color=red><b>' + tem
						+ '</b></font> 秒后,将退到登陆页面!');
				if (daoshu == 0) {
					box.hide();
					Ext.TaskMgr.stop(task);
					Ext.callback(fn);
				}
			},
			interval : 1000
		}
		Ext.TaskMgr.start(task);
	},
	/**
	 * 设置cookie 值 及生存期
	 * 
	 * @argument name 名字
	 * @argument value 值
	 * @argument expires 天 note： 为 0时 生存期为浏览器进程
	 * @argument path 存放路径 eg： set_cookie('name', 'user', 0, '/', '', '');
	 */
	setCookie : function(name, value, expires, path, domain, secure) {
		// set time, it's in milliseconds
		var today = new Date();
		today.setTime(today.getTime());
		expires = expires * 1000 * 60 * 60 * 24;
		var expires_date = new Date(today.getTime() + (expires));
		document.cookie = name + "=" + escape(value)
				+ ((expires) ? ";expires=" + expires_date.toGMTString() : "")
				+ ((path) ? ";path=" + path : "")
				+ ((domain) ? ";domain=" + domain : "")
				+ ((secure) ? ";secure" : "");
	},
	/**
	 * 根据cookie 名 得到其 值
	 */
	getCookie : function(name) {
		var start = document.cookie.indexOf(name + "=");
		var len = start + name.length + 1;
		if ((!start) && (name != document.cookie.substring(0, name.length))) {
			return null;
		}
		if (start == -1)
			return null;
		var end = document.cookie.indexOf(";", len);
		if (end == -1)
			end = document.cookie.length;

		return unescape(document.cookie.substring(len, end));
	},
	theme : [{
				id : 0,
				title : "天蓝基调",
				css : "ext-all.css",
				skin : 'default.css',
				subTheme : 'infoBlue.css'
			}, {
				id : 1,
				title : "灰蓝基调",
				css : "xtheme-gray.css",
				skin : 'default-blue.css',
				subTheme : 'infoGray.css'
			}, {
				id : 2,
				title : "墨绿基调",
				css : "xtheme-green.css",
				skin : 'default-green.css',
				subTheme : 'infoGreen.css'
			}],
	changeCss : function(extCss, baseUrl) {
		var basePath = baseUrl + "/scripts/ext/resources/css/";
		var skinPath = baseUrl + "/styles/ext/";
		var change = function(i) {
			Ext.util.CSS.swapStyleSheet("topic", basePath + this.theme[i].css);
			Ext.util.CSS.swapStyleSheet("skin", skinPath + this.theme[i].skin);
			this.setCookie('topic', this.theme[i].css);
			this.setCookie('skin', this.theme[i].skin);
			this.setCookie('extCss', this.theme[i].id, 24);
			this.setCookie("mainSkin", this.theme[i].subTheme);
			try {
				// 更新子页面样式
				var frames = document.getElementsByTagName('iframe');
				if (frames)
					for (var i = 0; i < frames.length; i++) {
						var frame = frames[i];
						if (frame.contentWindow) {
							if (typeof(frame.contentWindow.initCss) == "function") {
								frame.contentWindow.initCss();
							} else if (typeof(frame.contentWindow.Ext) != "undefined") {
								var extCss = this.getCookie('extCss');
								if (extCss) {
									frame.contentWindow.Ext.util.CSS
											.swapStyleSheet(
													"topic",
													basePath
															+ this.theme[extCss].css);
								}
							}
						}
					}
			} catch (e) {
				// console.log(e);
			}
		};
		change.call(this, extCss);
	},
	applyTheme : function(baseUrl) {
		var extCss = this.getCookie('extCss');
		if (extCss) {
			this.changeCss(extCss, baseUrl);
		} else {
			this.changeCss(2, baseUrl);
		}
		return extCss;
	}
}

/**
 * 重载 节点创建方法
 * 
 * @param {}
 *            attr
 * @return {}
 */
Ext.tree.TreeLoader.prototype.createNode = function(attr) {
	try {
		attr.text = attr.label ? attr.label : attr.text;
		attr.leaf = attr.children ? false : true;
	} catch (e) {
	}
	if (this.baseAttrs) {
		Ext.applyIf(attr, this.baseAttrs);
	}
	if (this.applyLoader !== false && !attr.loader) {
		attr.loader = this;
	}
	if (Ext.isString(attr.uiProvider)) {
		attr.uiProvider = this.uiProviders[attr.uiProvider]
				|| eval(attr.uiProvider);
	}
	if (attr.nodeType) {
		return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
	} else {
		return attr.leaf
				? new Ext.tree.TreeNode(attr)
				: new Ext.tree.AsyncTreeNode(attr);
	}
}