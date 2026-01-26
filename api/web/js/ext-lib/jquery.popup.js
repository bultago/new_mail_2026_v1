/*
 * jqDnR - Minimalistic Drag'n'Resize for jQuery.
 *
 * Copyright (c) 2007 Brice Burgess <bhb@iceburg.net>, http://www.iceburg.net
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * $Version: 2007.08.19 +r2
 */

(function(jQuery) {
	var agent	= navigator.userAgent.toUpperCase();	
	var isMsie6	= false;
	var isMsie7 = false;
	if(jQuery.browser.msie && (agent.indexOf("MSIE 6") > 0 && (agent.indexOf("MSIE 7") > 0))){
		isMsie7 = true;
	} else if(jQuery.browser.msie && (agent.indexOf("MSIE 6") > 0)){
		isMsie6	= true;
	}
	
    jQuery.fn.jqDrag = function(h) {
        return i(this, h, "d");
    };
    jQuery.fn.jqResize = function(h,mw,mh) {
        return i(this, h, "r",mw,mh);
    };
    jQuery.jqDnR = {    	
        dnr: {},
        e: 0,
        drag: function(v) {
            if (M.k == "d") {
                E.css({
                    left: M.X + v.pageX - M.pX,
                    top: M.Y + v.pageY - M.pY
                })
            } else {
            	jQuery(E.find(".popupWraaper")[0]).css({
            		width: Math.max((v.pageX - M.pX + M.W)-2, (M.MW-2)),
                    height: Math.max((v.pageY - M.pY + M.H)-2, (M.MH-2))
                });
            	
            	jQuery(E.find(".contentWrapper")[0]).css({
            		width: Math.max((v.pageX - M.pX + M.W)-6, (M.MW-6)),
                    height: Math.max((v.pageY - M.pY + M.H)-8, (M.MH-8))
                });
            	
            	var bh = jQuery(E.find(".btnArea")[0]).height();
            	if(isMsie6){
            		jQuery(E.find(".contentInnerWrapper")[0]).css({
	            		width: Math.max((v.pageX - M.pX + M.W)-8, (M.MW-8)),
	                    height:Math.max((v.pageY - M.pY + M.H)-(bh)+18,(M.MH-(bh)+18))
	                });
            	} else {
	            	jQuery(E.find(".contentInnerWrapper")[0]).css({
	            		width: Math.max((v.pageX - M.pX + M.W)-8, (M.MW-8)),
	                    height:Math.max((v.pageY - M.pY + M.H)-(bh+2),(M.MH-(bh+2)))
	                });
            	}
            	
            	if(isMsie6){
            		jQuery(E.find(".content")[0]).css({
	                    height: Math.max((v.pageY - M.pY + M.H)-(bh+58)+20, (M.MH-(bh+58)+20))
	                });
            	} else {
	            	jQuery(E.find(".content")[0]).css({
	                    height: Math.max((v.pageY - M.pY + M.H)-(bh+58), (M.MH-(bh+58)))
	                });
            	}
            	
                E.css({
                    width: Math.max(v.pageX - M.pX + M.W, M.MW),
                    height: Math.max(v.pageY - M.pY + M.H, M.MH)
                });
            }
            return false
        },
        stop: function() {
        	jQuery(document).unbind("mousemove", J.drag).unbind("mouseup", J.stop)
        }        
    };
    var mw,mh;
    var J = jQuery.jqDnR,
    M = J.dnr,
    E = J.e,    
    i = function(e, h, k, mw, mh) {
        return e.each(function() {        	
        	
            h = (h) ? jQuery(h, e) : e;
            h.bind("mousedown", {
                e: e,
                k: k
            },
            function(v) {
                var d = v.data,
                p = {};
                E = d.e;
                if (E.css("position") != "relative") {
                    try {
                        E.position(p)
                    } catch(e) {}
                }
                M = {
                    X: p.left || f("left") || 0,
                    Y: p.top || f("top") || 0,
                    W: f("width") || E[0].scrollWidth || 0,
                    H: f("height") || E[0].scrollHeight || 0,
                    pX: v.pageX,
                    pY: v.pageY,
                    k: d.k,
                    o: E.css("opacity"),                    
                    MW:mw,
                    MH:mh
                };
                jQuery(document).mousemove(jQuery.jqDnR.drag).mouseup(jQuery.jqDnR.stop).mousedown(jQuery.jqDnR.stop);
                return false
            })
        })
    },
    f = function(k) {
        return parseInt(E.css(k)) || false
    }
})(jQuery);

/* Copyright (c) 2006 Brandon Aaron (http://brandonaaron.net)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * $LastChangedDate: 2007-07-21 18:44:59 -0500 (Sat, 21 Jul 2007) $
 * $Rev: 2446 $
 *
 * Version 2.1.1
 */
(function(jQuery) {
    jQuery.fn.bgIframe = function(s) {
        if (jQuery.browser.msie && !(document.documentMode >= 9) && /6.0/.test(navigator.userAgent)) {
            s = jQuery.extend({
                top: "auto",
                left: "auto",
                width: "auto",
                height: "auto",
                opacity: true,
                src: "javascript:false;"
            },
            s || {});
            var prop = function(n) {
                return n && n.constructor == Number ? n + "px": n;
            },
            html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="' + s.src + '"style="display:block;position:absolute;z-index:-1;' + (s.opacity !== false ? "filter:Alpha(Opacity='0');": "") + "top:" + (s.top == "auto" ? "expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+'px')": prop(s.top)) + ";left:" + (s.left == "auto" ? "expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+'px')": prop(s.left)) + ";width:" + (s.width == "auto" ? "expression(this.parentNode.offsetWidth+'px')": prop(s.width)) + ";height:" + (s.height == "auto" ? "expression(this.parentNode.offsetHeight+'px')": prop(s.height)) + ';"/>';
            return this.each(function() {
                if (jQuery("> iframe.bgiframe", this).length == 0) {
                    this.insertBefore(document.createElement(html), this.firstChild);
                }
            });
        }
        return this;
    };
})(jQuery);

/* jQpopup - jQuery Popup Box v0.1
 *  jQpopup is distributed under the terms of the MIT license
 *  For more information visit http://jqframework.com/jqpopup
 *  Copyright (C) 2009  jqframework.com
 * Do not remove this copyright message
 */

jQuery.fn.jQpopup = function(action,opts){	
	
	function getTemplate(id){
		var a = "<div class='jqpopup' id='"+id+"_p'>"+
		"<div class='popupWraaper'>"+
		"<div class='jq_title' id='"+id+"_ph'>"+
		"<span id='"+id+"_pht'></span>"+
		"<a class='btn_X' href='javascript:;' id='"+id+"_px'>close</a>"+
		"</div>" +
		"<div class='contentWrapper' id='"+id+"_pcwo'>"+
		"<div class='contentInnerWrapper' id='"+id+"_pcw'>"+
		"<div class='content' id='"+id+"_pc'>"+		
		"</div>" +
		"<div class='btnArea' id='" + id + "_jqbtn'></div>"+
		"<div id='" + id + "_ps' class='jq_resize'></div>"+
		"</div>"+
		"</div>"+		
		"</div>";  
		return a;
	}		
	
	function toCenter(g) {
        var c = g + "_p";
        var f = jQuery("#" + c).offset();
        var e = parseInt(jQuery("#" + c).width());
        var d = parseInt(jQuery("#" + c).height());
        var b = parseInt(jQuery(window).width()) / 2 - e / 2;
        var a = parseInt(jQuery(window).height()) / 2 - d / 2;
        jQuery("#" + c).css({
            left: b,
            top: a
        });
    }
	
	function toSideCenter(id,top,left) {
        var c = id + "_p";
        var f = jQuery("#" + c).offset();        
        var e = parseInt(jQuery("#" + c).width());
        var d = parseInt(jQuery("#" + c).height());
        
        var b = parseInt(jQuery(window).width()) / 2 - e / 2;
        var a = parseInt(jQuery(window).height()) / 2 - d / 2;
        
        if(top && top != ""){
        	a = top;
        } else {
        	a = parseInt(jQuery(window).height()) / 2 - d / 2;
        }
        
        if(left && left != ""){
        	b = left; 
        } else {
        	b = parseInt(jQuery(window).width()) / 2 - e / 2;
        }
        
        jQuery("#" + c).css({
            left: b,
            top: a
        });
    }
	
	function close() {
		var beforeCloseFunc = opts.beforeCloseFunc;
        if(beforeCloseFunc){beforeCloseFunc();}
        
		jQuery("#" + fid + "_p").find(".jq_innerTable").css("width","");
        if (jQuery("#" + fid + "_pc").html() != "" && jQuery("#" + fid + "_pc").html() != null) {
            var a = jQuery("#" + fid).clone(true);
            jQuery("#" + fid + "_p").hide();            
            setTimeout(function(){
            	jQuery("#" + fid + "_p").remove();
            	jQuery("body").append(a);
            	jQuery("#" + fid).hide();
            	
            	jQuery.removeBodyMask(fid + "_bodyMask");
                jQuery("object").show();
                jQuery("embed").show();
            	jQuery("select").show();            	
            	
                var closeFunc = opts.closeFunc;
                if(closeFunc){
            		closeFunc();
            	}
            },100);
        }        
    }
	
	function makeBtn(name,func){
		var btn = jQuery("<a class='"+opts.btnClass+"' href='#'></a>");
		btn.append(jQuery("<span></span>").append(name));
		btn.click(function(){func();});
		return btn;			
	}
	
	var frame = jQuery(this);	
	var fid = frame.attr("id");
	
	if(action == "open"){		
		if (jQuery("#" + fid).html() != "") {
			jQuery("#" + fid).show();
			var e = getTemplate(fid);
			jQuery("body").append(jQuery(e));
			
	        jQuery("#" + fid + "_p").bgIframe();
	        jQuery("#" + fid + "_p").jqDrag(".jq_title");
	        if(!opts.notResize){
	        	jQuery("#" + fid + "_p").jqResize(".jq_resize",opts.minWidth,opts.minHeight);
	        	jQuery("#" + fid + "_ps").show();
	        } else {
	        	jQuery("#" + fid + "_ps").hide();	
	        }
	        jQuery("#" + fid + "_px").bind("click", 
	        function(){
	        	close();            	           	
	        });	        
	//            jQuery("#" + f + "_pl").bind("click",
	//            function() {
	//            	toCenter(fid);
	//            });           
	        
	        var a = jQuery("#" + fid).clone(true);
	        var g = jQuery("#" + fid).attr("title");
	        jQuery("#" + fid).remove();
	        jQuery("#" + fid + "_pht").html(g);
	        jQuery("#" + fid + "_pc").html(a.show());
	        jQuery("#" + fid + "_ph").mousedown(function(){
	        	var popup = jQuery(this);
	        	var pid = popup.attr("id");            	
		        var a = 0;
		        jQuery(".jqpopup").each(function(e) {
		            if (jQuery(this).css("zIndex") > a) {
		                a = jQuery(this).css("zIndex")
		            }
		        });
		        var d = parseInt(a) + 1;
		        jQuery("#" + pid).css("zIndex", d)
	        	
	        });
	        
	        var btnList = opts.btnList;
	        if(btnList && btnList.length > 0){
	        	for ( var i = 0; i < btnList.length; i++) {
	        		jQuery("#"+fid+"_jqbtn").append(makeBtn(btnList[i].name, btnList[i].func));
				}
	        }
	        
	        if(!opts.hideCloseBtn)
	        	jQuery("#"+fid+"_jqbtn").append(makeBtn(opts.closeName,close));
		
	        
	        var pwh = jQuery("#" + fid + "_pcw").height();
	        if (pwh < opts.minHeight) {
	            jQuery("#" + fid + "_pcw").css("height", opts.minHeight);
	        }	        
	        pwh = jQuery("#" + fid + "_pcw").height();
	        var bh = jQuery(jQuery("#" + fid + "_pcw").find(".btnArea")[0]).height();
	        jQuery("#" + fid + "_pc").css("height", pwh-(bh+28));
	        
	        if (jQuery("#" + fid + "_pcw").width() < opts.minWidth-8) {
	            jQuery("#" + fid + "_pcw").css("width", opts.minWidth-8);
	        }
	        
	        jQuery("#" + fid + "_p").css("width",opts.minWidth+"px");
	        
	        if(opts.left || opts.top){
	        	toSideCenter(fid,opts.top,opts.left);
	        } else {
	        	toCenter(fid);
	        }
	        
	        jQuery("#" + fid + "_p").css("z-index",10000);
	        
	        if(!opts.noneHideObject){
	        	jQuery("object:not(div#"+fid+" object)").each(function(){
	        		var id = jQuery(this).attr("id");
	        		if(id.indexOf("SWFUpload") < 0){
	        			jQuery(this).hide();	        			
	        		}
	        	});
	        }	        
	        jQuery("embed:not(div#"+fid+" embed)").hide();
	    	jQuery("select:not(div#"+fid+" select)").hide();
	        jQuery.makeBodyMask(fid + "_bodyMask",(opts.isBgTransparent));    
	        
	        
	        var openFunc = opts.openFunc;
	        if(openFunc){
	        	openFunc();
	        }
	        
	        jQuery("#" + fid + "_p").show();	        
	        jQuery("#" + fid + "_p").find(".jq_innerTable").css("width","100%");
	        jQuery("#" + fid).data("status","open");
	    }
	} else if(action == "status"){		
		var status = jQuery("#" + fid).data("status");		
		return (status)?status:"close";
	} else if(action == "close"){
		jQuery("#" + fid + "_px").trigger("click");
		jQuery("#" + fid).data("status","close");
	} else if(action == "resize"){
        var pwh = jQuery("#" + fid + "_pcw").height();
        if (pwh < opts.minHeight) {
            jQuery("#" + fid + "_pcw").css("height", opts.minHeight);
        }	        
        pwh = jQuery("#" + fid + "_pcw").height();
        var bh = jQuery(jQuery("#" + fid + "_pcw").find(".btnArea")[0]).height();
        jQuery("#" + fid + "_pc").css("height", pwh-(bh+28));
        
        if (jQuery("#" + fid + "_pcw").width() < opts.minWidth-8) {
            jQuery("#" + fid + "_pcw").css("width", opts.minWidth-8);
        }
        
        jQuery("#" + fid + "_p").css("width",opts.minWidth+"px");
        
        if(opts.left || opts.top){
        	toSideCenter(fid,opts.top,opts.left);
        } else {
        	toCenter(fid);
        }
        
        jQuery("#" + fid + "_p").show();	        
        jQuery("#" + fid + "_p").find(".jq_innerTable").css("width","100%");
	}
};
	
	   

	
