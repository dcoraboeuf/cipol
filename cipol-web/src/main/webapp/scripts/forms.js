var Forms = new function () {
	
	var dh = Ext.DomHelper;
	
	function field_invalidate (el) {
		el.addClass('macro-form-error');
	}
	
	function text_validate_required (name) {
		var field = Ext.fly(name);
		var value = field.getValue();
		if (value != "") {
			return true;
		} else {
			field_invalidate(field);
			return false;
		}
	}
	
	function text_field_edit (id) {
		var input = Ext.fly('macro-form-input-' + id);
		input.dom.removeAttribute("readonly");
	}
	
	return {
		text_validate_required: text_validate_required,
		text_field_edit: text_field_edit
	};
	
}();