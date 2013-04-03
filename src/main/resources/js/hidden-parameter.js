// bind on initialization of editor
AJS.bind("init.rte", function() { 
	
	var macroName = "box";
	
	AJS.log("BIND a HIDE FIELD");
	
	AJS.MacroBrowser.getMacroJsOverride(macroName).fields.string = {"willbehidden": function(param) {
		AJS.log("HIDE FIELD");
	    var parameterField = AJS.MacroBrowser.ParameterFields["_hidden"] (param, {});
	    if (!parameterField.getValue()) {
	      parameterField.setValue('hidden field value');
	    }
	    return parameterField;
	  }
	};
});