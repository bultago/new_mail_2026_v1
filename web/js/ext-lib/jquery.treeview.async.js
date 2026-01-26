/*
 * Async Treeview 0.1 - Lazy-loading extension for Treeview
 * 
 * http://bassistance.de/jquery-plugins/jquery-plugin-treeview/
 *
 * Copyright (c) 2007 Jörn Zaefferer
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id$
 *
 */

;(function($) {

function load(settings, root, child, container) {
	var isPopup = settings.isPopup;
	$.getJSON(settings.url, {root: root}, function(response) {
		function createNode(parent) {
			var current; 
			if(isPopup == "Y")
				current = $("<li/>").attr("id", this.id || "").html("<div style='margin:0px;'>" + this.text + "</div>").appendTo(parent);
			else
				current = $("<li/>").attr("id", this.id || "").html("<div style='margin:5px 0 5px 0;'>" + this.text + "</div>").appendTo(parent);
			if (this.classes) {
				current.addClass(this.classes);
			}
			if (this.expanded) {
				current.addClass("open");
			}
			if (this.hasChildren || this.children && this.children.length) {
				var branch = $("<ul/>").appendTo(current);
				if (this.hasChildren) {
					current.addClass("hasChildren");
					createNode.call({
						text:"placeholder",
						id:"placeholder",
						children:[]
					}, branch);
				}
				if (this.children && this.children.length) {
					$.each(this.children, createNode, [branch])
				}
				
				if(this.isRoot){
					var id = this.id;
					setTimeout(function(){
						jQuery("#link_"+id).parent().trigger("click");
					},1);
				}
				
			}
		}
		$.each(response, createNode, [child]);
        $(container).treeview({add: child});
    });
}

var proxied = $.fn.treeview;
$.fn.treeview = function(settings) {
	if (!settings || !settings.url) {
		return proxied.apply(this, arguments);
	}
	var container = this;
	load(settings, "", this, container);
	var userToggle = settings.toggle;
	return proxied.call(this, $.extend({}, settings, {
		collapsed: true,
		toggle: function() {
			var $this = $(this);
			if ($this.hasClass("hasChildren")) {
				var childList = $this.removeClass("hasChildren").find("ul");
				childList.empty();
				load(settings, this.id, childList, container);
			}
			if (userToggle) {
				userToggle.apply(this, arguments);
			}
		}
	}));
};

})(jQuery);