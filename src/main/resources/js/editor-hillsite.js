// bind on initialization of editor
AJS.bind("init.rte", function() { 
	
	var macroName = 'hillsite';
	
	// create dialog to add macro
	var dialog = new AJS.Dialog(400, 320);
	
	// hide dialog
	dialog.addCancel("Cancel", function() {
		dialog.hide();
	});
	
	// add macro to editor
	dialog.addSubmit("Create Macro", function() {

		// get current selection in editor
		var selection = AJS.Rte.getEditor().selection.getNode();
		var macro = {
				name: macroName
		};
		
		// convert macro and insert in DOM
		tinymce.plugins.Autoconvert.convertMacroToDom(macro, function(data, textStatus, jqXHR ) {
			AJS.$(selection).html(data + "<p><br/></p>");
		}, function(jqXHR, textStatus, errorThrown ) {
			AJS.log("error converting macro to DOM");
		});
		dialog.hide();
	});
	
	// bind event to open macro browser
	AJS.MacroBrowser.setMacroJsOverride(macroName, {opener: function(macro) {
		
		// open custom dialog
        dialog.show();
        
		// we have a macro object here, like the one we're creating later:
//		var macro = {
//			name: macroName
//			params: {
//				title: 'some title'
//			},
//			defaultParameterValue: "",
//			body: ''
//		};
	}});
}); 