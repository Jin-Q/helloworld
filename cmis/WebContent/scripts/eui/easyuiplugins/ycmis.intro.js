/**
 * <p>easyui扩展工具：向导</p>
 * <ul>此向导仅限用于document.body,form,div等容器对象 </ul>
 * <p>属性说明</p>
 * <ul>steps:向导目标,格式：[{element:'选择器:#+id,.+className',intro:'详细信息，支持纯文字和HTML元素，样式可自行定义'},...]</ul>
 * <ul>isInitShow:初始化时是否显示向导信息，默认显示</ul>
 * <ul>onbeforechange:向导改变之前事件方法</ul>
 * <ul>onchange:向导改变时事件方法</ul>
 * <ul>oncomplete:向导完成事件方法</ul>
 * <ul>onexit:向导跳出事件方法</ul>
 * <p>方法说明</p>
 * <ul>options:获取向导属性</ul>
 * <ul>start:开始向导</ul>
 * <ul>goToStep:向导跳转</ul>
 * 
 * @author zangys  2014年9月25日
 * 
 * <p>修改记录</p>
 * <ul>1、修改向导在IE7下生效。add by zangys at 2014-9-28 14:01:36</il>
 */

(function($){
	$.fn.intro = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.intro.methods[options];
			if(fun){
				return fun(this,param);
			}
		}
		options=options||{};
		return this.each(function(){
			var state = $.data(this, 'intro');
			
			if (state){
				$.extend(state.options, options);
			} else {
				state=$.data(this, 'intro', {
					options: $.extend({}, $.fn.intro.defaults, options),
					intro:null
				});
			}
			init(this);
		});
	}
	
	/**
	 * 初始化
	 * @param target
	 * @return
	 */
	function init(target){
		var opts= $.data(target,'intro').options;
		if(!opts.steps){
			return;
		}
		var intro = introJs(target);
		$.data(target,'intro').intro = intro;
		intro.setOption("steps",opts.steps);
		//初始化时是否显示向导
		if(opts.isInitShow){
			intro.goToStep(1).start();
		}
		//设置触发事件
		intro.onbeforechange(function(targetElement) {
			opts.onbeforechange.call(targetElement);
		});
		intro.onchange(function(targetElement) {
			opts.onchange.call(targetElement);
		});
		intro.oncomplete(function(targetElement) {
			opts.oncomplete.call(targetElement);
		});
		intro.onexit(function(targetElement) {
			opts.onexit.call(targetElement);
		});
		
	}
	
	$.fn.intro.methods = {
		options:function(jq){//获取属性值
			return $.data(jq[0],"intro").options;
		},
		start:function(jq){//开始向导
			var intro = $.data(jq[0],"intro");
			intro.intro.start();
		},
		goToStep:function(jq,step){//向导跳转
			var intro = $.data(jq[0],"intro");
			intro.intro.goToStep(step);
		}
		
	};
	
	$.fn.intro.defaults = $.extend({},{
		onbeforechange:function(){},//向导改变之前
		onchange:function(){},//向导改变
		oncomplete:function(){},//向导完成
		onexit:function(){},//向导跳出
		isInitShow:true,//初始化时是否显示向导
		steps:[]		
	});
	

	/**
	 * IntroJs main class
	 *
	 * @class IntroJs
	 */
	function IntroJs(obj) {
		this._targetElement = obj;

		this._options = {
			/* Next button label in tooltip box */
			nextLabel: CusLang.EUIExt.intro.nextLabel,
			/* Previous button label in tooltip box */
			prevLabel: CusLang.EUIExt.intro.prevLabel,
			/* Skip button label in tooltip box */
			skipLabel: CusLang.EUIExt.intro.skipLabel,
			/* Done button label in tooltip box */
			doneLabel: CusLang.EUIExt.intro.doneLabel,
			/* Default tooltip box position */
			tooltipPosition: 'right',
			/* Next CSS class for tooltip boxes */
			tooltipClass: '',
			/* Close introduction when pressing Escape button? */
			exitOnEsc: true,
			/* Close introduction when clicking on overlay layer? */
			exitOnOverlayClick: true,
			/* Show step numbers in introduction? */
			showStepNumbers: true,
			showSkipButton: true,
			showPrevButton: true,
			showNextButton: true
		};
	}

	/**
	 * Initiate a new introduction/guide from an element in the page
	 *
	 * @api private
	 * @method _introForElement
	 * @param {Object} targetElm
	 * @returns {Boolean} Success or not?
	 */
	function _introForElement(targetElm) {
		var introItems = [],
			self = this;

		if (this._options.steps) {
			//use steps passed programmatically
			var allIntroSteps = [];

			for (var i = 0, stepsLength = this._options.steps.length; i < stepsLength; i++) {
				var currentItem = this._options.steps[i];
				//set the step
				currentItem.step = i + 1;
				//use querySelector function only when developer used CSS selector
				if (typeof(currentItem.element) === 'string') {
					//grab the element with given selector from the page
					currentItem.element = $(document).find(currentItem.element)[0];
				}
				introItems.push(currentItem);
			}

		} else {
			//use steps from data-* annotations

			var allIntroSteps = $(targetElm).find('*[data-intro]');
			//if there's no element to intro
			if (allIntroSteps.length < 1) {
				return false;
			}

			for (var i = 0, elmsLength = allIntroSteps.length; i < elmsLength; i++) {
				var currentElement = allIntroSteps[i];
				introItems.push({
					element: currentElement,
					intro: $(currentElement).attr('data-intro'),
					step: parseInt($(currentElement).attr('data-step'), 10),
					tooltipClass: $(currentElement).attr('data-tooltipClass'),
					position: $(currentElement).attr('data-position') || this._options.tooltipPosition
				});
			}
		}

		//Ok, sort all items with given steps
		introItems.sort(function (a, b) {
			return a.step - b.step;
		});

		//set it to the introJs object
		self._introItems = introItems;

		//add overlay layer to the page
		if (_addOverlayLayer.call(self, targetElm)) {
			//then, start the show
			_nextStep.call(self);

			var skipButton = $(targetElm).find('.introjs-skipbutton')[0],
				nextStepButton = $(targetElm).find('.introjs-nextbutton')[0];

			self._onKeyDown = function (e) {
				if (e.keyCode === 27 && self._options.exitOnEsc == true) {
					//escape key pressed, exit the intro
					_exitIntro.call(self, targetElm);
					//check if any callback is defined
					if (self._introExitCallback != undefined) {
						self._introExitCallback.call(self);
					}
				} else if (e.keyCode === 37) {
					//left arrow
					_previousStep.call(self);
				} else if (e.keyCode === 39 || e.keyCode === 13) {
					//right arrow or enter
					_nextStep.call(self);
					//prevent default behaviour on hitting Enter, to prevent steps being skipped in some browsers
					if (e.preventDefault) {
						e.preventDefault();
					} else {
						e.returnValue = false;
					}
				}
			};

			self._onResize = function (e) {
				_setHelperLayerPosition.call(self, $(document).find('.introjs-helperLayer')[0]);
			};

			if (window.addEventListener) {
				window.addEventListener('keydown', self._onKeyDown, true);
				//for window resize
				window.addEventListener("resize", self._onResize, true);
			} else if (document.attachEvent) { //IE
				document.attachEvent('onkeydown', self._onKeyDown);
				//for window resize
				document.attachEvent("onresize", self._onResize);
			}
		}
		return false;
	}

	/**
	 * Go to specific step of introduction
	 *
	 * @api private
	 * @method _goToStep
	 */
	function _goToStep(step) {
		//because steps starts with zero
		this._currentStep = step - 2;
		if (typeof (this._introItems) !== 'undefined') {
			_nextStep.call(this);
		}
	}

	/**
	 * Go to next step on intro
	 *
	 * @api private
	 * @method _nextStep
	 */
	function _nextStep() {
		if (typeof (this._currentStep) === 'undefined') {
			this._currentStep = 0;
		} else {
			++this._currentStep;
		}

		if ((this._introItems.length) <= this._currentStep) {
			//end of the intro
			//check if any callback is defined
			if (typeof (this._introCompleteCallback) === 'function') {
				this._introCompleteCallback.call(this);
			}
			_exitIntro.call(this, this._targetElement);
			return;
		}

		var nextStep = this._introItems[this._currentStep];
		if (typeof (this._introBeforeChangeCallback) !== 'undefined') {
			this._introBeforeChangeCallback.call(this, nextStep.element);
		}

		_showElement.call(this, nextStep);
	}

	/**
	 * Go to previous step on intro
	 *
	 * @api private
	 * @method _nextStep
	 */
	function _previousStep() {
		if (this._currentStep === 0) {
			return false;
		}

		var nextStep = this._introItems[--this._currentStep];
		if (typeof (this._introBeforeChangeCallback) !== 'undefined') {
			this._introBeforeChangeCallback.call(this, nextStep.element);
		}

		_showElement.call(this, nextStep);
	}

	/**
	 * Exit from intro
	 *
	 * @api private
	 * @method _exitIntro
	 * @param {Object} targetElement
	 */
	function _exitIntro(targetElement) {
		//remove overlay layer from the page
		var overlayLayer = $(targetElement).find('.introjs-overlay')[0];
		//for fade-out animation
		overlayLayer.style.opacity = 0;
		setTimeout(function () {
			if (overlayLayer.parentNode) {
				overlayLayer.parentNode.removeChild(overlayLayer);
			}
		}, 500);
		//remove all helper layers
		var helperLayer = $(targetElement).find('.introjs-helperLayer')[0];
		if (helperLayer) {
			var tooltipLayer = $(targetElement).find('.introjs-tooltip')[0];
			$(tooltipLayer).appendTo(helperLayer);
			setTimeout(function () {
				helperLayer.parentNode.removeChild(helperLayer);
			},50);
		}

		//remove `introjs-showElement` class from the element
		var showElement = $(document).find('.introjs-showElement')[0];
		if (showElement) {
			showElement.className = showElement.className.replace(/introjs-[a-zA-Z]+/g, '').replace(/^\s+|\s+$/g, ''); // This is a manual trim.
		}

		//remove `introjs-fixParent` class from the elements
		var fixParents = $(document).find('.introjs-fixParent')[0];
		if (fixParents && fixParents.length > 0) {
			for (var i = fixParents.length - 1; i >= 0; i--) {
				fixParents[i].className = fixParents[i].className.replace(/introjs-fixParent/g, '').replace(/^\s+|\s+$/g, '');
			}
			;
		}
		//clean listeners
		if (window.removeEventListener) {
			window.removeEventListener('keydown', this._onKeyDown, true);
		} else if (document.detachEvent) { //IE
			document.detachEvent('onkeydown', this._onKeyDown);
		}
		//set the step to zero
		this._currentStep = undefined;
	}

	/**
	 * Render tooltip box in the page
	 *
	 * @api private
	 * @method _placeTooltip
	 * @param {Object} targetElement
	 * @param {Object} tooltipLayer
	 * @param {Object} arrowLayer
	 */
	function _placeTooltip(targetElement, tooltipLayer, arrowLayer, numberLayer, helperLayer) {
		//reset the old style
		$(tooltipLayer).css({
			top: "",
			right: "",
			bottom: "",
			left: ""});
		$(arrowLayer).css({
			top: "",
			right: "",
			bottom: "",
			left: ""});
		numberLayer && $(numberLayer).css({
			right: "",
			left: ""
		});

		//prevent error when `this._currentStep` is undefined
		if (!this._introItems[this._currentStep]) return;

		var tooltipCssClass = '';

		//if we have a custom css class for each step
		var currentStepObj = this._introItems[this._currentStep];
		if (typeof (currentStepObj.tooltipClass) === 'string') {
			tooltipCssClass = currentStepObj.tooltipClass;
		} else {
			tooltipCssClass = this._options.tooltipClass;
		}

		tooltipLayer.className = ('introjs-tooltip ' + tooltipCssClass).replace(/^\s+|\s+$/g, '');


		var helperOffset = _getOffset(targetElement), toolTipOffset = _getOffset(tooltipLayer);
		var left = helperOffset.left, top = helperOffset.top, width = helperOffset.width, height = helperOffset.height, winHeight = $(window).height(), winWidth = $(window).width();
		var bottom = winHeight - (top + height), right = winWidth - (left + width);
		var bt = bottom >= top ? 'bottom' : 'top', lr = right >= left ? 'right' : 'left';

		var position, blankWidth = 10;
		if (lr == 'right') {
			if (winWidth >= (left + width + toolTipOffset.width + blankWidth)) {
				position = lr;
			}
		} else {
			if (0 <= (left - toolTipOffset.width - blankWidth)) {
				position = lr;
			}
		}

		if (bt == 'bottom') {
			if (winHeight >= (top + height + toolTipOffset.height + blankWidth)) {
				if (position) {
					if (winWidth >= (left + toolTipOffset.width + blankWidth)) {
						position = bt;
					}
				} else {
					position = bt;
				}
			}
		} else {
			if (0 <= (top - toolTipOffset.height - blankWidth)) {
				if (position) {
					if (winWidth >= (left + toolTipOffset.width + blankWidth)) {
						position = bt;
					}
				} else {
					position = bt;
				}
			}
		}

		if (!position) {
			position = (left >= right ? left : right) >= (top >= bottom ? top : bottom) ? lr : bt;
			//  position = toolTipOffset.width >= toolTipOffset.height ? lr : bt;
		}
		if (currentStepObj.tooltipPosition) {
			$(tooltipLayer).appendTo(window.document.body);
			var arrowDirection = currentStepObj.arrowDirection || position;
			var arrowAlign = currentStepObj.arrowAlign;
			var tooltipPosition = currentStepObj.tooltipPosition;
			var settingPosition = {};

			if (tooltipPosition.left !== undefined) {
				var tl = parseInt(tooltipPosition.left);
				settingPosition.left = (left + tl) + "px";
			}

			if (tooltipPosition.top !== undefined) {
				settingPosition.top = (top + parseInt(tooltipPosition.top)) + "px";
			}

			if (tooltipPosition.right !== undefined) {
				settingPosition.right = (right + parseInt(tooltipPosition.right)) + "px";
			}

			if (tooltipPosition.bottom !== undefined) {
				settingPosition.bottom = (bottom + parseInt(tooltipPosition.bottom)) + "px";
			}

			$(tooltipLayer).css(settingPosition);

			switch (arrowDirection) {
				case 'left':
					if (arrowAlign == "top") {
						arrowLayer.style.top = '10px';

					} else if (arrowAlign == "bottom") {
						arrowLayer.style.bottom = '10px';
					} else {
						arrowLayer.style.top = ((toolTipOffset.height / 2) + 2) + "px";
					}
					if (numberLayer) {
						numberLayer.style.right = '-16px';
					}
					arrowLayer.className = 'introjs-arrow left';
					break;
				case 'right':
					if (arrowAlign == "top") {
						arrowLayer.style.top = '10px';
					} else if (arrowAlign == "bottom") {
						arrowLayer.style.bottom = '10px';
					} else {
						arrowLayer.style.top = ((toolTipOffset.height / 2) + 2) + "px";
					}
					if (numberLayer) {
						numberLayer.style.left = '-16px';
					}
					arrowLayer.className = 'introjs-arrow right';
					break;
				case 'bottom':
					if (arrowAlign == "left") {
						if (numberLayer) {
							numberLayer.style.right = '-16px';
						}
						arrowLayer.style.left = '10px';
					} else if (arrowAlign == "right") {
						if (numberLayer) {
							numberLayer.style.left = '-16px';
						}
						arrowLayer.style.left = '10px';
					} else {
						arrowLayer.style.left = ((toolTipOffset.width / 2) + 2) + "px";
					}

					arrowLayer.className = 'introjs-arrow bottom';
					break;
				case 'top':
					if (arrowAlign == "left") {
						arrowLayer.style.left = '10px';
						if (numberLayer) {
							numberLayer.style.right = '-16px';
						}
					} else if (arrowAlign == "right") {
						arrowLayer.style.right = '10px';
						if (numberLayer) {
							numberLayer.style.left = '-16px';
						}
					} else {
						arrowLayer.style.left = ((toolTipOffset.width / 2) + 2) + "px";
					}
					arrowLayer.className = 'introjs-arrow top';
					break;
			}

			return;
		} else {

			$(tooltipLayer).appendTo(helperLayer);
			var currentTooltipPosition = position;

			switch (currentTooltipPosition) {
				case 'right':
					if (bt == 'top') {
						tooltipLayer.style.top = '1px';
						arrowLayer.style.top = '10px';
					} else {
						tooltipLayer.style.top = '1px';
						arrowLayer.style.top = '10px';
					}
					if (numberLayer) {
						numberLayer.style.right = '-16px'
					}

					tooltipLayer.style.left = (width + 15) + 'px';
					arrowLayer.className = 'introjs-arrow left';
					break;
				case 'left':
					if (bt == 'top') {
						tooltipLayer.style.bottom = '0px';
						arrowLayer.style.bottom = '10px';
					} else {
						tooltipLayer.style.top = '0px';
						arrowLayer.style.top = '10px';
					}
					if (numberLayer) {
						numberLayer.style.left = '-16px';
					}
					tooltipLayer.style.right = (width + 20) + 'px';
					arrowLayer.className = 'introjs-arrow right';
					break;
				case 'top':
					if (lr == 'left') {
						tooltipLayer.style.right = '0px';
						arrowLayer.style.right = '10px';
						if (numberLayer) {
							numberLayer.style.left = '-16px';
						}
					} else {
						tooltipLayer.style.left = '0px';
						arrowLayer.style.left = '10px';
						if (numberLayer) {
							numberLayer.style.right = '-16px';
						}
					}
					tooltipLayer.style.bottom = (height + 20) + 'px';
					arrowLayer.className = 'introjs-arrow bottom';
					break;
				case 'bottom':
					if (lr == 'left') {
						tooltipLayer.style.right = '0px';
						if (numberLayer) {
							numberLayer.style.left = '-16px';
						}
						arrowLayer.style.right = '10px';
					} else {
						tooltipLayer.style.left = '0px';
						arrowLayer.style.left = '10px';
						if (numberLayer) {
							numberLayer.style.right = '-16px'
						}
					}
					tooltipLayer.style.top = (height + 20) + 'px';
					arrowLayer.className = 'introjs-arrow top';
					break;
			}
		}
	}

	/**
	 * Update the position of the helper layer on the screen
	 *
	 * @api private
	 * @method _setHelperLayerPosition
	 * @param {Object} helperLayer
	 */
	function _setHelperLayerPosition(helperLayer) {
		if (helperLayer) {
			//prevent error when `this._currentStep` in undefined
			if (!this._introItems[this._currentStep]) return;

			var elementPosition = _getOffset(this._introItems[this._currentStep].element);
			//set new position to helper layer
			$(helperLayer).attr('style','width: ' + (elementPosition.width + 10) + 'px; ' +
				'height:' + (elementPosition.height + 10) + 'px; ' +
				'top:' + (elementPosition.top - 5) + 'px;' +
				'left: ' + (elementPosition.left - 5) + 'px;');

		}
	}

	/**
	 * Show an element on the page
	 *
	 * @api private
	 * @method _showElement
	 * @param {Object} targetElement
	 */
	function _showElement(targetElement) {

		if (typeof (this._introChangeCallback) !== 'undefined') {
			this._introChangeCallback.call(this, targetElement.element);
		}

		var self = this,
			oldHelperLayer = $(document).find('.introjs-helperLayer')[0],
			elementPosition = _getOffset(targetElement.element);
		if (oldHelperLayer != null) {
			var oldHelperNumberLayer = $(document).find('.introjs-helperNumberLayer')[0],
				oldtooltipLayer = $(document).find('.introjs-tooltiptext')[0],
				oldArrowLayer = $(document).find('.introjs-arrow')[0],
				oldtooltipContainer = $(document).find('.introjs-tooltip')[0];


			//hide the tooltip
			oldtooltipContainer.style.opacity = 0;

			//set new position to helper layer
			//_setHelperLayerPosition.call(self, oldHelperLayer);

			//remove `introjs-fixParent` class from the elements
			var fixParents = $(document).find('.introjs-fixParent');
			if (fixParents && fixParents.length > 0) {
				for (var i = fixParents.length - 1; i >= 0; i--) {
					fixParents[i].className = fixParents[i].className.replace(/introjs-fixParent/g, '').replace(/^\s+|\s+$/g, '');
				}
				;
			}

			//remove old classes
			var oldShowElement = $(document).find('.introjs-showElement')[0];
			oldShowElement.className = oldShowElement.className.replace(/introjs-[a-zA-Z]+/g, '').replace(/^\s+|\s+$/g, '');
			//we should wait until the CSS3 transition is competed (it's 0.3 sec) to prevent incorrect `height` and `width` calculation
			if (self._lastShowElementTimer) {
				clearTimeout(self._lastShowElementTimer);
			}
			self._lastShowElementTimer = setTimeout(function () {
				//set new position to helper layer
				_setHelperLayerPosition.call(self, oldHelperLayer);
				//set current step to the label
				if (oldHelperNumberLayer != null) {
					oldHelperNumberLayer.innerHTML = targetElement.step;
				}
				//set current tooltip text
				oldtooltipLayer.innerHTML = targetElement.intro;
				//set the tooltip position
				_placeTooltip.call(self, targetElement.element, oldtooltipContainer, oldArrowLayer, oldHelperNumberLayer, oldHelperLayer);
				//show the tooltip
				oldtooltipContainer.style.opacity = 1;
			}, 350);

		} else {
			var helperLayer = document.createElement('div'),
				arrowLayer = document.createElement('div'),
				tooltipLayer = document.createElement('div'),
				helperNumberLayer;


			helperLayer.className = 'introjs-helperLayer';

			oldHelperLayer = helperLayer;


			//set new position to helper layer
			_setHelperLayerPosition.call(self, helperLayer);

			//add helper layer to target element
			this._targetElement.appendChild(helperLayer);

			arrowLayer.className = 'introjs-arrow';
			tooltipLayer.innerHTML = '<div class="introjs-tooltiptext">' +
				targetElement.intro +
				'</div><div class="introjs-tooltipbuttons"></div>';

			//add helper layer number
			if (this._options.showStepNumbers) {
				helperNumberLayer = document.createElement('span');
				helperNumberLayer.className = 'introjs-helperNumberLayer';
				helperNumberLayer.innerHTML = targetElement.step;
				tooltipLayer.appendChild(helperNumberLayer);
			}
			tooltipLayer.appendChild(arrowLayer);
			helperLayer.appendChild(tooltipLayer);


			var tooltipButtonsLayer = $(tooltipLayer).find('.introjs-tooltipbuttons')[0];


			//skip button
			var skipTooltipButton = document.createElement('a');
			skipTooltipButton.className = 'introjs-button introjs-skipbutton';
			skipTooltipButton.href = 'javascript:void(0);';
			skipTooltipButton.innerHTML = this._options.skipLabel;

			skipTooltipButton.onclick = function () {
				if (self._introItems.length - 1 == self._currentStep && typeof (self._introCompleteCallback) === 'function') {
					self._introCompleteCallback.call(self);
				}

				if (self._introItems.length - 1 != self._currentStep && typeof (self._introExitCallback) === 'function') {
					self._introExitCallback.call(self);
				}

				_exitIntro.call(self, self._targetElement);
			};

			tooltipButtonsLayer.appendChild(skipTooltipButton);

			//previous button
			var prevTooltipButton = document.createElement('a');
			prevTooltipButton.className = "introjs-button introjs-prevbutton";
			prevTooltipButton.onclick = function () {
				if (self._currentStep != 0) {
					_previousStep.call(self);
				}
			};

			prevTooltipButton.href = 'javascript:void(0);';
			prevTooltipButton.innerHTML = this._options.prevLabel;
			//in order to prevent displaying next/previous button always
			if (this._introItems.length > 1) {
				tooltipButtonsLayer.appendChild(prevTooltipButton);
			}


			//next button
			var nextTooltipButton = document.createElement('a');
			nextTooltipButton.className = "introjs-button introjs-nextbutton";
			nextTooltipButton.onclick = function () {
				if (self._introItems.length - 1 != self._currentStep) {
					_nextStep.call(self);
				}
			};

			nextTooltipButton.href = 'javascript:void(0);';
			nextTooltipButton.innerHTML = this._options.nextLabel;

			//in order to prevent displaying next/previous button always
			if (this._introItems.length > 1) {
				tooltipButtonsLayer.appendChild(nextTooltipButton);
			}


			//set proper position
			_placeTooltip.call(self, targetElement.element, tooltipLayer, arrowLayer, helperNumberLayer, helperLayer);
		}


		var skipTooltipButton = $('.introjs-skipbutton', oldHelperLayer),
			prevTooltipButton = $('.introjs-prevbutton', oldHelperLayer),
			nextTooltipButton = $('.introjs-nextbutton', oldHelperLayer);
		var options = this._options;

		var hashNext = this._introItems.length - 1 == this._currentStep;


		prevTooltipButton.toggleClass('introjs-disabled', this._currentStep == 0);
		nextTooltipButton.toggleClass('introjs-disabled', hashNext);

		skipTooltipButton.css('display', options.showSkipButton ? "inline-block" : "none");
		prevTooltipButton.css('display', options.showPrevButton ? "inline-block" : "none");
		nextTooltipButton.css('display', options.showNextButton ? "inline-block" : "none");

		skipTooltipButton.html(hashNext ? options.doneLabel : options.skipLabel);
		prevTooltipButton.html(options.prevLabel);
		nextTooltipButton.html(options.nextLabel);

		//Set focus on "next" button, so that hitting Enter always moves you onto the next step
		options.showNextButton && nextTooltipButton.focus();
		//add target element position style
		targetElement.element.className += ' introjs-showElement';

		var currentElementPosition = _getPropValue(targetElement.element, 'position');
		if (currentElementPosition !== 'absolute' &&
			currentElementPosition !== 'relative') {
			//change to new intro item
			targetElement.element.className += ' introjs-relativePosition';
		}

		var parentElm = targetElement.element.parentNode;
		while (parentElm != null) {
			if (parentElm.tagName.toLowerCase() === 'body') break;

			var zIndex = _getPropValue(parentElm, 'z-index');
			if (/[0-9]+/.test(zIndex)) {
				parentElm.className += ' introjs-fixParent';
			}
			parentElm = parentElm.parentNode;
		}

		if (!_elementInViewport(targetElement.element)) {
			var rect = targetElement.element.getBoundingClientRect(),
				top = rect.bottom - (rect.bottom - rect.top),
				bottom = rect.bottom - _getWinSize().height;

			// Scroll up
			if (top < 0) {
				window.scrollBy(0, top - 30); // 30px padding from edge to look nice

				// Scroll down
			} else {
				window.scrollBy(0, bottom + 100); // 70px + 30px padding from edge to look nice
			}
		}
	}

	/**
	 * Get an element CSS property on the page
	 * Thanks to JavaScript Kit: http://www.javascriptkit.com/dhtmltutors/dhtmlcascade4.shtml
	 *
	 * @api private
	 * @method _getPropValue
	 * @param {Object} element
	 * @param {String} propName
	 * @returns Element's property value
	 */
	function _getPropValue(element, propName) {
		var propValue = '';
		if (element.currentStyle) { //IE
			propValue = element.currentStyle[propName];
		} else if (document.defaultView && document.defaultView.getComputedStyle) { //Others
			propValue = document.defaultView.getComputedStyle(element, null).getPropertyValue(propName);
		}

		//Prevent exception in IE
		if (propValue && propValue.toLowerCase) {
			return propValue.toLowerCase();
		} else {
			return propValue;
		}
	}

	/**
	 * Provides a cross-browser way to get the screen dimensions
	 * via: http://stackoverflow.com/questions/5864467/internet-explorer-innerheight
	 *
	 * @api private
	 * @method _getWinSize
	 * @returns {Object} width and height attributes
	 */
	function _getWinSize() {
		if (window.innerWidth != undefined) {
			return { width: window.innerWidth, height: window.innerHeight };
		} else {
			var D = document.documentElement;
			return { width: D.clientWidth, height: D.clientHeight };
		}
	}

	/**
	 * Add overlay layer to the page
	 * http://stackoverflow.com/questions/123999/how-to-tell-if-a-dom-element-is-visible-in-the-current-viewport
	 *
	 * @api private
	 * @method _elementInViewport
	 * @param {Object} el
	 */
	function _elementInViewport(el) {
		var rect = el.getBoundingClientRect();

		return (
			rect.top >= 0 &&
			rect.left >= 0 &&
			(rect.bottom + 80) <= window.innerHeight && // add 80 to get the text right
			rect.right <= window.innerWidth
			);
	}

	/**
	 * Add overlay layer to the page
	 *
	 * @api private
	 * @method _addOverlayLayer
	 * @param {Object} targetElm
	 */
	function _addOverlayLayer(targetElm) {
		var overlayLayer = document.createElement('div'),
			styleText = '',
			self = this;

		//set css class name
		overlayLayer.className = 'introjs-overlay';

		//check if the target element is body, we should calculate the size of overlay layer in a better way
		if (targetElm.tagName.toLowerCase() === 'body') {
			styleText += 'top: 0;bottom: 0; left: 0;right: 0;position: fixed;';
			$(overlayLayer).attr("style",styleText);
		} else {
			//set overlay layer position
			var elementPosition = _getOffset(targetElm);
			if (elementPosition) {
				styleText += 'width: ' + elementPosition.width + 'px; height:' + elementPosition.height + 'px; top:' + elementPosition.top + 'px;left: ' + elementPosition.left + 'px;';
				$(overlayLayer).attr("style",styleText);
			}
		}

		targetElm.appendChild(overlayLayer);

		overlayLayer.onclick = function () {
			if (self._options.exitOnOverlayClick == true) {
				_exitIntro.call(self, targetElm);
			}
			//check if any callback is defined
			if (self._introExitCallback != undefined) {
				self._introExitCallback.call(self);
			}
		};

		setTimeout(function () {
			styleText += 'opacity: .8;';
			$(overlayLayer).attr('style',styleText);
		}, 10);
		return true;
	}

	/**
	 * Get an element position on the page
	 * Thanks to `meouw`: http://stackoverflow.com/a/442474/375966
	 *
	 * @api private
	 * @method _getOffset
	 * @param {Object} element
	 * @returns Element's position info
	 */
	function _getOffset(element) {
		var elementPosition = {};

		//set width
		elementPosition.width = element.offsetWidth;

		//set height
		elementPosition.height = element.offsetHeight;

		var offset = $(element).offset();
		//set top
		elementPosition.top = offset.top;
		//set left
		elementPosition.left = offset.left;

		return elementPosition;
	}

	/**
	 * Overwrites obj1's values with obj2's and adds obj2's if non existent in obj1
	 * via: http://stackoverflow.com/questions/171251/how-can-i-merge-properties-of-two-javascript-objects-dynamically
	 *
	 * @param obj1
	 * @param obj2
	 * @returns obj3 a new object based on obj1 and obj2
	 */
	function _mergeOptions(obj1, obj2) {
		var obj3 = {};
		for (var attrname in obj1) {
			obj3[attrname] = obj1[attrname];
		}
		for (var attrname in obj2) {
			obj3[attrname] = obj2[attrname];
		}
		return obj3;
	}

	var introJs = function (targetElm) {
		if (typeof (targetElm) === 'object') {
			//Ok, create a new instance
			return new IntroJs(targetElm);

		} else if (typeof (targetElm) === 'string') {
			//select the target element with query selector
			var targetElement = $(document).find(targetElm)[0];

			if (targetElement) {
				return new IntroJs(targetElement);
			} else {
				throw new Error('There is no element with given selector.');
			}
		} else {
			return new IntroJs(document.body);
		}
	};

	//Prototype
	introJs.fn = IntroJs.prototype = {
		clone: function () {
			return new IntroJs(this);
		},
		setOption: function (option, value) {
			this._options[option] = value;
			return this;
		},
		setOptions: function (options) {
			this._options = _mergeOptions(this._options, options);
			return this;
		},
		start: function () {
			_introForElement.call(this, this._targetElement);
			return this;
		},
		goToStep: function (step) {
			_goToStep.call(this, step);
			return this;
		},
		exit: function () {
			_exitIntro.call(this, this._targetElement);
		},
		refresh: function () {
			_setHelperLayerPosition.call(this, $(document).find('.introjs-helperLayer')[0]);
			return this;
		},
		onbeforechange: function (providedCallback) {
			if (typeof (providedCallback) === 'function') {
				this._introBeforeChangeCallback = providedCallback;
			} else {
				throw new Error('Provided callback for onbeforechange was not a function');
			}
			return this;
		},
		onchange: function (providedCallback) {
			if (typeof (providedCallback) === 'function') {
				this._introChangeCallback = providedCallback;
			} else {
				throw new Error('Provided callback for onchange was not a function.');
			}
			return this;
		},
		oncomplete: function (providedCallback) {
			if (typeof (providedCallback) === 'function') {
				this._introCompleteCallback = providedCallback;
			} else {
				throw new Error('Provided callback for oncomplete was not a function.');
			}
			return this;
		},
		onexit: function (providedCallback) {
			if (typeof (providedCallback) === 'function') {
				this._introExitCallback = providedCallback;
			} else {
				throw new Error('Provided callback for onexit was not a function.');
			}
			return this;
		}
	};
	
})(jQuery);
