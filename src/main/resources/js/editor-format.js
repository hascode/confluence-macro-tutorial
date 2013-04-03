// bind on initialization of editor
AJS.bind("init.rte", function() { 
	$("#my-awesome-format").click(function() {
		/* execCommand
	 	 * c = Command to perform for example Bold.
		 * u = Optional boolean state if a UI should be presented for the command or not.
		 * v = Optional value parameter like for example an URL to a link.
		 * 
		 * How to set a CSS class for that???
		 */
		tinymce.activeEditor.execCommand("FormatBlock", false, "span");
	});
}); 