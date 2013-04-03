(function() {

	var macroName = 'box';
	
    /**
     * Resolves the macro body content.
     * 
     * serialize() to ensure that any bogus tinymce tags + dirty browser markup are cleaned up.
     * macro node should be cloned before serialization, as we don't want serialization tampering with the actual DOM element.
     * 
     * Note: we handle with DOM nodes here, not jQuery objects!
     */
	var getMacroBody = function(macroDiv) {
        var macroBodyNode = AJS.$("td.wysiwyg-macro-body", macroDiv).clone()[0];
        var macroBodyHtml = AJS.Rte.getEditor().serializer.serialize(macroBodyNode, {
        	forced_root_block: false // Prevent serialize from wrapping in a <p></p>
        });
        return macroBodyHtml;
	}
	
	/**
	 * get current parameters and split them into a nice object
	 */ 
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
	
	/**
	 * Update macro parameter/body
	 */
    var updateMacro = function(macroId, macroNode, macroParam, param) {
        var $macroDiv = AJS.$(macroNode);
        
        // celect current macro
        AJS.Rte.getEditor().selection.select($macroDiv[0]);
        
        //  Stores the currently selected range and scroll position of the editor
        AJS.Rte.BookmarkManager.storeBookmark();

        // get/set parameters and body of macro
        var currentParams = getCurrentParams($macroDiv);
        currentParams[macroParam] = param;
        var macroBody = getMacroBody($macroDiv);
        
        // create macro request object
        var macroRenderRequest = {
            contentId: Confluence.Editor.getContentId(),
            macro: {
                name: macroId,
                params: currentParams,
                defaultParameterValue: "",
                body : macroBody
            }
        };

        // insert new macro content
        tinymce.confluence.MacroUtils.insertMacro(macroRenderRequest);
    };

    /*
     * Note to myself: This means, that two macros having the same options
     * will have to be treated with caution to not overwrite them with each others content???
     * TODO: check this
     */
    
    // register handlers for different option buttons 
    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("Small", function(e, macroNode) {
        updateMacro(macroName, macroNode, "size", "small");
    });
    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("Medium", function(e, macroNode) {
        updateMacro(macroName, macroNode, "size", "medium");
    });
    AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("Large", function(e, macroNode) {
        updateMacro(macroName, macroNode, "size", "large");
    });
 
})();