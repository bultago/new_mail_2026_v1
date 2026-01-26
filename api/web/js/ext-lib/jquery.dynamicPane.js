/*
 * jquery.splitter.js - two-pane splitter window plugin
 *
 * version 1.01 (01/05/2007) 
 * 
 * Dual licensed under the MIT and GPL licenses: 
 *   http://www.opensource.org/licenses/mit-license.php 
 *   http://www.gnu.org/licenses/gpl.html 
 */

/**
 * The splitter() plugin implements a two-pane resizable splitter window.
 * The selected elements in the jQuery object are converted to a splitter;
 * each element should have two child elements which are used for the panes
 * of the splitter. The plugin adds a third child element for the splitbar.
 * 
 * For more details see: http://methvin.com/jquery/splitter/
 *
 *
 * @example $('#MySplitter').splitter();
 * @desc Create a vertical splitter with default settings 
 *
 * @example $('#MySplitter').splitter({direction: 'h', accessKey: 'M'});
 * @desc Create a horizontal splitter resizable via Alt+Shift+M
 *
 * @name splitter
 * @type jQuery
 * @param String options Options for the splitter
 * @cat Plugins/Splitter
 * @return jQuery
 * @author Dave Methvin (dave.methvin@gmail.com)
 */



 jQuery.fn.splitter = function(opts){
	opts = jQuery.extend({
		type: 'v',				// v=vertical, h=horizontal split
		activeClass: 'active',	// class name for active splitter
		pxPerKey: 5,			// splitter px moved per keypress
		tabIndex: 0,			// tab order indicator
		accessKey: ''			// accelerator key for splitter
//		initA  initB			// initial A/B size (pick ONE)
//		minA maxA  minB maxB	// min/max pane sizes
//		paneAID paneBID			// pane A/B ID splitter ID
//		splitterID	splitterClass // splitter ID, splitter StyleClass
	},{
		v: {					// Vertical splitters:
			keyGrowA: 39,		//	left arrow key
			keyShrinkA: 37,		//	right arrow key
			cursor: "e-resize",	//	double-arrow horizontal
			splitbarClass: "vsplitbar",
			eventPos: "pageX", set: "left", 
			adjust: "width",  offsetAdjust: "offsetWidth",  adjSide1: "Left", adjSide2: "Right",
			fixed:  "height", offsetFixed:  "offsetHeight", fixSide1: "Top",  fixSide2: "Bottom"
		},
		h: {					// Horizontal splitters:
			keyGrowA: 40,		//	down arrow key
			keyShrinkA: 38,		//	up arrow key
			cursor: "n-resize",	//	double-arrow vertical
			splitbarClass: "hsplitbar",
			eventPos: "pageY", set: "top", 
			adjust: "height", offsetAdjust: "offsetHeight", adjSide1: "Top",  adjSide2: "Bottom",
			fixed:  "width",  offsetFixed:  "offsetWidth",  fixSide1: "Left", fixSide2: "Right"
		},
		n: {			
			adjust: "width",  offsetAdjust: "offsetWidth",  adjSide1: "Left", adjSide2: "Right",
			fixed:  "height", offsetFixed:  "offsetHeight", fixSide1: "Top",  fixSide2: "Bottom"
		}
	}[((opts||{}).type||'v').charAt(0).toLowerCase()], opts||{});	

	return this.each(function() {
		function startSplit(e) {
			makeWrapping();
			splitbar.addClass(opts.activeClass);
			if ( e.type == "mousedown" ) {
				paneA._posAdjust = paneA[0][opts.offsetAdjust] - e[opts.eventPos];
				jQuery(document)
					.bind("mousemove", doSplitMouse)
					.bind("mouseup", endSplit);
				paneA.css("overflow","hidden");
				paneB.css("overflow","hidden");
			}
			return true;	// required???
		}
		function doSplitKey(e) {
			var key = e.which || e.keyCode;
			var dir = key==opts.keyGrowA? 1 : key==opts.keyShrinkA? -1 : 0;
			if ( dir )
				moveSplitter(paneA[0][opts.offsetAdjust]+dir*opts.pxPerKey);
			return true;	// required???
		}
		function doSplitMouse(e) {
			moveSplitter(paneA._posAdjust+e[opts.eventPos]);
		}
		function endSplit(e) {
			removeWrapping();
			splitbar.removeClass(opts.activeClass);
			jQuery(document)
				.unbind("mousemove", doSplitMouse)
				.unbind("mouseup", endSplit);

			var initAdjust, initFixed;
			initAdjust = parseInt(paneA.css(opts.adjust).replace("px",''));
			initAdjust = initAdjust+paneA._padAdjust;			
			initFixed = paneB.css(opts.adjust).replace("px",'');
			
			if(opts.cookieSave){
				jQuery.cookie(opts.lid+"Cookie", opts.type + "|" + initAdjust+ "|" + initFixed, { path: '/'});
			}
			cacheAdjustPos = initAdjust;
			
			if(!jQuery.browser.opera){
				setTimeout(function(){
					paneA.css("overflow","auto");
					paneB.css("overflow","auto");
				},100);
			}
			
			jQuery(window).trigger("resize");			
			//showBodyScrollBar();
		}
		function moveSplitter(np) {
			
			//hiddenBodyScrollBar();
			// Constrain new position to fit pane size limits; 16=scrollbar fudge factor
			// TODO: enforce group width in IE6 since it lacks min/max css properties?
			np = Math.max(paneA._min+paneA._padAdjust, group._adjust - (paneB._max||9999), 16,
				Math.min(np, paneA._max||9999, group._adjust - splitbar._adjust - 
					Math.max(paneB._min+paneB._padAdjust, 16)));

			// Resize/position the two panes and splitbar
			splitbar.css(opts.set, (np+paneA._borderAdjust)+"px");
			paneA.css(opts.adjust, np-paneA._padAdjust+"px");
			paneB.css(opts.set, (np+splitbar._adjust+paneA._borderAdjust)+"px")
				.css(opts.adjust, (group._adjust-splitbar._adjust-paneB._padAdjust-np-paneA._borderAdjust)+"px");
			
		}		
		function resizeSplitter() {
			// Constrain new position to fit pane size limits; 16=scrollbar fudge factor
			// TODO: enforce group width in IE6 since it lacks min/max css properties?
			var fixPos, adJustPos, setPos;			
			
			fixPos = (cacheAdjustPos)?cacheAdjustPos:paneA._init;
			setPos = parseInt(fixPos)+splitbar._size;			
			
			if(!jQuery.browser.opera){
				paneA.css("overflow","hidden");
				paneB.css("overflow","hidden");
			}
			
			if(opts.type == 'h' || opts.type == 'n'){				
				adJustPos = parseInt(group.height()) - setPos;				
			} else if(opts.type == 'v'){				
				adJustPos = parseInt(group.width()) - setPos;				
			} else {
				adJustPos = 0;
			}
			
			
			/*adJustPos = group._adjust - Math.max(paneB._min+paneB._padAdjust, 16);
			adJustPos = (adJustPos-fixPos)+ splitbar._adjust;*/
			
			// Resize/position the two panes and splitbar
			splitbar.css(opts.set, (fixPos+paneA._borderAdjust)+"px");			
			paneA.css(opts.adjust, fixPos-paneA._padAdjust+"px");			
			paneB.css(opts.set, (setPos+paneA._borderAdjust)+"px")
				.css(opts.adjust, (adJustPos-paneA._borderAdjust)+"px");
			
			if(!jQuery.browser.opera){
				setTimeout(function(){
					paneA.css("overflow","auto");
					paneB.css("overflow","auto");
				},1000);
			}
			
		}
		function cssCache(jq, n, pf, m1, m2) {
			// IE backCompat mode thinks width/height includes border and padding
			jq[n] = jQuery.boxModel? (parseInt(jq.css(pf+m1))||0) + (parseInt(jq.css(pf+m2))||0) : 0;
		}
		function optCache(jq, pane) {
			// Opera returns -1px for min/max dimensions when they're not there!
			jq._min = Math.max(0, opts["min"+pane] || parseInt(jq.css("min-"+opts.adjust)) || 0);
			jq._max = Math.max(0, opts["max"+pane] || parseInt(jq.css("max-"+opts.adjust)) || 0);
		}
		function splitterRemove(){
			jQuery("#"+opts["splitterID"]).remove();
			paneA.css({position: "absolute", margin: "0",top: "0", left: "0"});
			paneB.css({position: "relative", margin: "0",top: "0", left: "0"});
		}
		function makeWrapping(){
			if(opts.splitterID == "sm")
				// main splitter "sm"
				jQuery("#m_contentBodyWapper").loadWorkMaskOnly(false);
			else
				// sub splitter "sc"
				jQuery("#m_contentSub").loadWorkMaskOnly(false);
		}
		function removeWrapping(){
			if(opts.splitterID == "sm")
				// main splitter "sm"
				jQuery("#m_contentBodyWapper").removeWorkMask();
			else
				// sub splitter "sc"
				jQuery("#m_contentSub").removeWorkMask();
		}
		// Create jQuery object closures for splitter group and both panes
		
		var group = jQuery(this).css({position: "relative"});
		var paneCss = {
			position: "absolute", 			// positioned inside splitter container
			margin: "0", 					// remove any stylesheet margin or ...			
			"-moz-user-focus": "ignore"		// disable focusability in Firefox
		};
		
		var paneA = jQuery("#"+opts.paneAID);		// left  or top
		var paneB = jQuery("#"+opts.paneBID);		// right or bottom
		var cacheAdjustPos;
		
		splitterRemove();
		paneA.css(paneCss);
		paneB.css(paneCss);		

		var isNormal = (opts.type != "n")?false:true;
		if(!isNormal){			
			paneB.show();
			
			if(opts.cookieSave){			
				jQuery.cookie(opts.lid+"Cookie", opts.type + "|" + opts.initA + "|" + opts.initB, { path: '/'});
			}
			
			// Focuser element, provides keyboard support
			var focuser = jQuery('<a href="javascript:void(0)"></a>')
				.bind("focus", startSplit).bind("keydown", doSplitKey).bind("blur", endSplit)
				.attr({accessKey: opts.accessKey, tabIndex: opts.tabIndex});

			// Splitbar element, displays actual splitter bar
			// The select-related properties prevent unintended text highlighting
			var splitbar = jQuery('<div></div>')
				.insertAfter(paneA).append(focuser)
				.attr({"class": (opts.splitterClass || opts.splitbarClass), unselectable: "on", id: opts.splitterID})
				.css({position: "absolute", "-khtml-user-select": "none",
					"-moz-user-select": "none", "user-select": "none"})
				.bind("mousedown", startSplit);
			if ( /^(auto|default)$/.test(splitbar.css("cursor") || "auto") )
				splitbar.css("cursor", opts.cursor);			
			
			// Cache several dimensions for speed--assume these don't change
			splitbar._adjust = splitbar[0][opts.offsetAdjust];
			splitbar._size = parseInt(splitbar.css(opts.adjust).replace("px",''));			
			cssCache(group, "_borderAdjust", "border", opts.adjSide1+"Width", opts.adjSide2+"Width");
			cssCache(group, "_borderFixed",  "border", opts.fixSide1+"Width", opts.fixSide2+"Width");
			cssCache(paneA, "_padAdjust", "padding", opts.adjSide1, opts.adjSide2);
			cssCache(paneA, "_padFixed",  "padding", opts.fixSide1, opts.fixSide2);
			cssCache(paneA, "_borderAdjust", "border", opts.adjSide1+"Width", opts.adjSide2+"Width");
			cssCache(paneB, "_padAdjust", "padding", opts.adjSide1, opts.adjSide2);
			cssCache(paneB, "_padFixed",  "padding", opts.fixSide1, opts.fixSide2);
			cssCache(paneA, "_borderAdjust", "border", opts.adjSide1+"Width", opts.adjSide2+"Width");
			optCache(paneA, 'A');
			optCache(paneB, 'B');			
			
			// Initial splitbar position as measured from left edge of splitter
			paneA._init = (opts.initA==true? parseInt(jQuery.curCSS(paneA[0],opts.adjust)) : opts.initA) || 0;			
			paneB._init = (opts.initB==true? parseInt(jQuery.curCSS(paneB[0],opts.adjust)) : opts.initB) || 0;
			
			if ( paneB._init )
				paneB._init = group[0][opts.offsetAdjust] - group._borderAdjust - paneB._init - splitbar._adjust;			

			// Set up resize event handler and trigger immediately to set initial position
			group.unbind("resize");
			group.bind("resize", function(e){						
				
				// Determine new width/height of splitter container				
				group._fixed  = group[0][opts.offsetFixed]  - group._borderFixed;
				group._adjust = group[0][opts.offsetAdjust] - group._borderAdjust;
				// Bail if splitter isn't visible or content isn't there yet
				if ( group._fixed <= 0 || group._adjust <= 0 ) return;
				// Set the fixed dimension (e.g., height on a vertical splitter)
				//alert(opts.fixed);
				paneA.css(opts.fixed, (group._fixed-paneA._padFixed)+"px");
				paneB.css(opts.fixed, (group._fixed-paneB._padFixed)+"px");
				splitbar.css(opts.fixed, group._fixed+"px");				
				// Re-divvy the adjustable dimension; maintain size of the preferred pane
				
				resizeSplitter();				
			});
			group.trigger("resize");
		} else {			
			paneB.hide();			
			if(opts.cookieSave){
				jQuery.cookie(opts.lid+"Cookie", opts.type + "|0|0" , { path: '/'});
			}

			cssCache(group, "_borderAdjust", "border", opts.adjSide1+"Width", opts.adjSide2+"Width");
			cssCache(group, "_borderFixed",  "border", opts.fixSide1+"Width", opts.fixSide2+"Width");
			cssCache(paneA, "_padAdjust", "padding", opts.adjSide1, opts.adjSide2);
			cssCache(paneA, "_padFixed",  "padding", opts.fixSide1, opts.fixSide2);			

			group._fixed  = group[0][opts.offsetFixed]  - group._borderFixed;
			group._adjust = group[0][opts.offsetAdjust] - group._borderAdjust;			
			
			
			/*paneA.css({position: "absolute", 
						margin: "0", top: "0", left: "0",
						width: (group._adjust-paneA._padAdjust)+"px", 
						height: (group._fixed-paneA._padFixed)+"px"});*/
			
			optCache(paneA, 'A');
			group.unbind("resize");
			group.bind("resize", function(e){				
				// Determine new width/height of splitter container
				group._fixed  = group[0][opts.offsetFixed]  - group._borderFixed;
				group._adjust = group[0][opts.offsetAdjust] - group._borderAdjust;
				// Bail if splitter isn't visible or content isn't there yet
				if ( group._wfixed <= 0 || group._hfixed <= 0 ) return;
				// Set the fixed dimension (e.g., height on a vertical splitter)
				paneA.css(opts.adjust, group._adjust-paneA._padAdjust+"px");
				paneA.css(opts.fixed, group._fixed-paneA._padFixed+"px");				
				
				
			});			
			group.trigger("resize");
		}		
	});
};

jQuery.fn.autoResize = function(resizeObj, bottomObj,isOverflow, isNotTrigger){

	// This must be re-done any time the browser window is resized.
	var bindObj = jQuery(this);	
	bindObj.bind("resize", function(){		
		
		var $ms = jQuery(resizeObj);
		if(agent.indexOf("WEBKIT") < 0){
			bindObj.css("overflow","hidden");
			$ms.css("overflow","hidden");
		}
				
				
		var top = ($ms.offset())?$ms.offset().top:0;		// from dimensions.js
		var wh = bindObj.height();
		// Account for margin or border on the splitter container
		var mrg = parseInt($ms.css("marginBottom")) || 0;
		var brd = parseInt($ms.css("borderBottomWidth")) || 0;
		var resizeHeight = wh-top-mrg-brd;
		var overflow = "";
		if(bottomObj){
			var jq =  jQuery(bottomObj);	
			var paddingTop = parseInt(jq.css("paddingTop").replace("px",''))||0;
			var paddingBottom = parseInt(jq.css("paddingBottom").replace("px",''))||0;
			var bottomHeight = jq.height()||0;
			var marginTop = parseInt(jq.css("marginTop")) || 0;
			var marginBottom = parseInt(jq.css("marginBottom")) || 0;			
			resizeHeight  = resizeHeight - 
							(paddingTop + paddingBottom + bottomHeight + 
								marginTop + marginBottom + 2);			
		}
		
		if(isOverflow){
			overflow = "auto";
		}
		
		jQuery(resizeObj).css("height", resizeHeight+"px");
		if (browser_resize() )
			jQuery(resizeObj).trigger("resize");
		
		
		setTimeout(function(){			
			bindObj.css("overflow","auto");	
			$ms.css("overflow",overflow);
		},100);
				
		
	});
	if(!isNotTrigger){
		bindObj.trigger("resize");
	}
};

jQuery.fn.resizeInnerFrame = function(opts){
	// This must be re-done any time the browser window is resized.	
	var bindObj = jQuery(this);
	if(opts.extHeight){
		jQuery(opts.resizeId).data("extSize",opts.extHeight);
	}
	
	var $ms = jQuery(opts.resizeId);
	var overFlow = "";
	var eventFunc = function(bindObj, $ms, onlyLoad){
		if(agent.indexOf("WEBKIT") < 0){
			bindObj.css("overflow","hidden");
			$ms.css("overflow","hidden");
		}
		jQuery(opts.mainId).css("overflow","hidden");
		
		var iheight = $ms.children().height();		
		var top = ($ms.offset())?$ms.offset().top:0;		// from dimensions.js
		
		var wh;
		if(!opts.isMainHeight){
			wh = bindObj.height();
		} else {
			wh = jQuery(opts.mainId).height();
		}
		var resizeWidth = jQuery(opts.mainId).width()-2;
		// Account for margin or border on the splitter container
		var mrg = parseInt($ms.css("marginBottom")) || 0;
		var brd = parseInt($ms.css("borderBottomWidth")) || 0;
		var resizeHeight = wh-top-mrg-brd;
		var jq;		
		if(opts.sideObjId){
			var sideObjs = opts.sideObjId;
			for ( var i = 0; i < sideObjs.length; i++) {				
				jq =  jQuery(sideObjs[i]);				
				if(jq.size() > 0){
					var paddingTop = parseInt(jq.css("paddingTop").replace("px",''))||0;
					var paddingBottom = parseInt(jq.css("paddingBottom").replace("px",''))||0;
					var bottomHeight = jq.height()||0;
					var marginTop = parseInt(jq.css("marginTop")) || 0;
					var marginBottom = parseInt(jq.css("marginBottom")) || 0;			
					resizeHeight  = resizeHeight - 
									(paddingTop + paddingBottom + bottomHeight + 
									marginTop + marginBottom + 2);
				}
			}
			
		}
		
		if(opts.extObjSize){
			resizeHeight  = resizeHeight - opts.extObjSize;			
		}
		
		if(iheight >= resizeHeight && !opts.wrapperMode){
			resizeHeight = iheight;
			resizeWidth = resizeWidth - 17;
		} else if(opts.wrapperMode){
			resizeHeight = jQuery(opts.mainId).height();
			overFlow = "auto";
		}
		
		if(jQuery(opts.resizeId).data("extSize")){
			resizeHeight = resizeHeight - jQuery(opts.resizeId).data("extSize");
		}
		
		$ms.css("height", resizeHeight+"px");		
		if(!opts.isNoneWidthChk){
			$ms.css("width", resizeWidth+"px");
			
		}
		
		if(onlyLoad){
			setTimeout(function(){			
				bindObj.css("overflow","auto");
				$ms.css("overflow",overFlow);
				jQuery(opts.mainId).css("overflow","auto");
			},50);
		}
		
	};
	bindObj.bind("resize", function(){		
		eventFunc(jQuery(this),jQuery(opts.resizeId));
		
		if ( browser_resize() )
			$ms.trigger("resize");		
		
		setTimeout(function(){			
			bindObj.css("overflow","auto");
			$ms.css("overflow",overFlow);
			jQuery(opts.mainId).css("overflow","auto");
		},50);
	});
	if(!opts.notCheckTrigger){
		bindObj.trigger("resize");
	}
	
	return eventFunc;
};

function hiddenBodyScrollBar(){
	jQuery("body").css("overflow","hidden");
}

function showBodyScrollBar(){
	jQuery("body").css("overflow","auto");
}

var LayerPane = Class.create({
	initialize: function(id,styleClass,initSize,minSize,maxSize){		
		this.id = id;		
		this.styleClass = styleClass;		
		this.initSize = initSize;
		this.minSize = minSize;
		this.maxSize = maxSize;		
		this.parsePane();
	},
	getId: function(){
		return this.id;
	},
	setId: function(id){
		this.id = id;
	},
	getStyleClass: function(){
		return this.styleClass;
	},
	setStyleClass: function(styleClass){
		this.styleClass = styleClass;
	},
	getInitSize: function(){
		return this.initSize;
	},
	setInitSize: function(initSize){
		this.initSize = initSize;
	},
	getMinSize: function(){
		return this.minSize;
	},
	setMinSize: function(minSize){
		this.minSize = minSize;
	},
	getMaxSize: function(){
		return this.maxSize;
	},
	setMaxSize: function(maxSize){
		this.maxSize = maxSize;
	},
	parsePane: function(){
		if(this.styleClass && this.styleClass != ""){			
			jQuery("#"+this.id).addClass(this.styleClass);
		}
	}
});

var SplitterManager = Class.create({
	initialize: function(mainPane, adjustSplitPane, fixedSplitPane,
						splitterId,vsplitBarClass, hsplitBarClass){
		this.mainPane = mainPane;
		this.adjustSplitPane = adjustSplitPane;
		this.fixedSplitPane = fixedSplitPane; 
		this.splitterId = splitterId;
		this.vsplitBarClass = vsplitBarClass;
		this.hsplitBarClass = hsplitBarClass;
		this.oldOpts = false;		
		this.referSplitPanes;
		this.currentMode = null;
		this.isNormalMode = false;
		
	},
	
	setReferencePane: function(paneIds){
		this.referSplitPanes = paneIds;
	},	
	setNormalSplitter: function(){
		var setLayerId = "#" + this.mainPane.getId();		
		var adjustSplitPaneId = this.adjustSplitPane.getId();
		var fixedSplitPaneId = this.fixedSplitPane.getId();
		var initASize = this.adjustSplitPane.getInitSize();
		var initBSize = this.fixedSplitPane.getInitSize();		
				
		var mode = "n";	
		this.previousMode = this.currentMode;
		this.currentMode = mode;		

					
		jQuery(setLayerId).splitter({
			lid : this.mainPane.getId(),
			type: mode,
			paneAID : adjustSplitPaneId, 
			paneBID : fixedSplitPaneId,
			paneRID : this.referSplitPanes,
			splitterID : this.splitterId,
			cookieSave:false
		});	
		
		this.isNormalMode = true;
			
	},
	setReloadSplitter: function(){
		if(this.isNormalMode){
			this.setSplitter("n", true, false);
		}		
	},
	setSplitter: function(mode, isInit, isInitSet){
		
		var setLayerId = "#" + this.mainPane.getId();		
		var adjustSplitPaneId = this.adjustSplitPane.getId();
		var fixedSplitPaneId = this.fixedSplitPane.getId();
		var initASize = this.adjustSplitPane.getInitSize();
		var initBSize = this.fixedSplitPane.getInitSize();		
				
		this.checkCookie();
		var bheight = jQuery(setLayerId).height();
		var bwidth = jQuery(setLayerId).width();
		if(isInit && this.oldOpts){
			mode = this.oldOpts.type;			
			initASize = (this.oldOpts.adjustSize > 0)?this.oldOpts.adjustSize:this.adjustSplitPane.getInitSize();
			initBSize = (this.oldOpts.fixedSize > 0)?this.oldOpts.fixedSize: this.fixedSplitPane.getInitSize();			
		} else {
			
			if(isInitSet){
				if(mode == "h"){					
					initBSize = this.fixedSplitPane.getInitSize();
					initASize = bheight - initBSize;
				}				
			} else if(!isInit){
				if(mode == "v"){
					initASize = bwidth/2;
					initBSize = bwidth - (bwidth/2);
				} else if(mode == "h"){
					initASize = bheight/2;
					initBSize = bheight - (bheight/2);				
				}
			}				
		}
		
		this.currentMode = mode;
		this.isNormalMode = false;

		if(mode == "n"){			
			jQuery(setLayerId).splitter({
				lid : this.mainPane.getId(),
				type: mode,
				paneAID : adjustSplitPaneId, 
				paneBID : fixedSplitPaneId,
				paneRID : this.referSplitPanes,
				splitterID : this.splitterId,
				cookieSave:true
			});			
		} else if(mode == "v"){
			jQuery(setLayerId).splitter({
				lid : this.mainPane.getId(),
				type: mode,
				paneAID : adjustSplitPaneId, 
				paneBID : fixedSplitPaneId,
				paneRID : this.referSplitPanes,
				splitterID : this.splitterId,
				splitterClass : this.vsplitBarClass,				
				initA: initASize, 
				initB: initBSize, 
				minA: this.adjustSplitPane.getMinSize(), 
				maxA: this.adjustSplitPane.getMaxSize(),
				cookieSave:true
			});
		} else if(mode == "h"){
			jQuery(setLayerId).splitter({
				lid : this.mainPane.getId(),
				type: mode,
				paneAID : adjustSplitPaneId, 
				paneBID : fixedSplitPaneId,
				paneRID : this.referSplitPanes,
				splitterID : this.splitterId,
				splitterClass : this.hsplitBarClass,				
				initA: initASize, 
				initB: initBSize, 
				minB: this.fixedSplitPane.getMinSize(), 
				maxB: this.fixedSplitPane.getMaxSize(),
				cookieSave:true
			});
		}
	},
	checkCookie: function(){
		var cookieHandler = new SplitterCookie();	
		if(cookieHandler.isExist(this.mainPane.getId()+"Cookie")){
			this.oldOpts = cookieHandler.getOldOpt();			
		}
	},
	getMode:function(){
		return this.currentMode;
	}
});

var SplitterCookie = Class.create({
	initialize: function(){
		this.cookieName;
		this.val;
		this.exist = false;
	},	
	isExist : function(cookieName){
		this.cookieName = cookieName;
		this.val = jQuery.cookie(this.cookieName);
		this.exist = false;
		if (this.val && this.val != ""){
			this.exist = true;
		}

		return this.exist;
	},
	getOldOpt: function(){
		var vals = this.val.split("|");
		var opt = {type : vals[0], adjustSize: parseInt(vals[1]), fixedSize: parseInt(vals[2])};
		return opt;
	}
	
});
function browser_resize() {    
	if (!jQuery.browser.msie) {
		return true;
	}
	if (parseInt(jQuery.browser.version)<9)	{
		return false;
	}   
	return true;
} 