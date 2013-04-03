AJS.MacroBrowser.getMacroJsOverride("box").fields.string = {
  "willbehidden": function(param) {
    var parameterField = AJS.MacroBrowser.ParameterFields["_hidden"] (param, {});
    if (!parameterField.getValue()) {
      parameterField.setValue('hidden field value');
    }
    return parameterField;
  }
};