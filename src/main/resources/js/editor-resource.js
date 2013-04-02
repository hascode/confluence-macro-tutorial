(function() {
	
	
	
	var getMacroBody = function(macroDiv) {
        /**
         * serialize() to ensure that any bogus tinymce tags + dirty browser markup are cleaned up.
         * macro node should be cloned before serialization, as we don't want serialization tampering with the actual DOM element.
         */
        var macroBodyNode = AJS.$("td.wysiwyg-macro-body", macroDiv).clone()[0];
        var macroBodyHtml = AJS.Rte.getEditor().serializer.serialize(macroBodyNode, {
        	forced_root_block: false // Prevent serialize from wrapping in a <p></p>
        });
        return macroBodyHtml;
	}
	
	var getCurrentParams = function(macroDiv) {
        var currentParams = {};
        if (macroDiv.attr("data-macro-parameters")) {
            AJS.$.each(macroDiv.attr("data-macro-parameters").split("|"), function(idx, item) {
                var param = item.split("=");
                currentParams[param[0]] = param[1];
            });
        }
        return currentParams;
	}
	
    var updateMacro = function(macroName, macroParam, macroNode, param) {
        var $macroDiv = AJS.$(macroNode);
        AJS.Rte.getEditor().selection.select($macroDiv[0]);
        AJS.Rte.BookmarkManager.storeBookmark();

        var currentParams = getCurrentParams($macroDiv);
        currentParams[macroParam] = param;
        var macroBody = getMacroBody($macroDiv);
        
        var macroRenderRequest = {
            contentId: Confluence.Editor.getContentId(),
            macro: {
                name: macroName,
                params: currentParams,
                defaultParameterValue: "",
                body : macroBody
            }
        };

        tinymce.confluence.MacroUtils.insertMacro(macroRenderRequest);
    };

    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("box", "size", "Small", function(e, macroNode) {
        updateMacro(macroNode, "small");
    });

    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("box", "size", "Medium", function(e, macroNode) {
        updateMacro(macroNode, "medium");
    });

    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("box", "size", "Large", function(e, macroNode) {
        updateMacro(macroNode, "large");
    });
 
})();


AJS.bind("init.rte", function() { 
//	AJS.MacroBrowser.setMacroJsOverride('box', {opener: function() {
//		AJS.log("DUMMY OPENER");
//	    var popup = new AJS.Dialog(860, 530);
//        popup.show();
//	}})
});