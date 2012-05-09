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
	
	function text_field_accept (id) {
		var input = Ext.get('macro-form-input-' + id);
		var form = Ext.get('macro-form-' + id);
		var action = form.dom.action;
		alert(action);
		// FIXME AJAX call
		// Restore
		text_field_restore(id);
	}
	
	function text_field_cancel (id) {
		var input = Ext.get('macro-form-input-' + id);
		// Restores the old value
		var oldValue = input.dom.getAttribute("oldvalue");
		input.dom.value = oldValue;
		// Restore
		text_field_restore(id);
	}
	
	function text_field_restore (id) {
		var input = Ext.get('macro-form-input-' + id);
		// Disables the text field for edition
		input.dom.setAttribute("readonly", "readonly");
		// Enables the edit action button
		Ext.fly('macro-form-action-edit-' + id).removeClass('action-hidden');
		Ext.fly('macro-form-action-accept-' + id).addClass('action-hidden');
		Ext.fly('macro-form-action-cancel-' + id).addClass('action-hidden');
	}
	
	function text_field_edit (id) {
		
		// Enables the text field for edition
		var input = Ext.get('macro-form-input-' + id);
		input.dom.removeAttribute("readonly");
		
		// Registers event on ENTER
		input.on('keypress', function (e) {
			if (e.getCharCode() == 13) {
				text_field_accept(id);
			}
		});
		
		// Requires the focus
		input.focus();
		input.dom.select();
		
		// Hides the edit action button
		Ext.fly('macro-form-action-edit-' + id).addClass('action-hidden');

		// Validation button
		var acceptButton = Ext.get('macro-form-action-accept-' + id);
		acceptButton.removeClass('action-hidden');
		acceptButton.on('click', function () {
			text_field_accept(id);
		});
		
		// Cancel button
		var cancelButton = Ext.get('macro-form-action-cancel-' + id);
		cancelButton.removeClass('action-hidden');
		cancelButton.on('click', function () {
			text_field_cancel(id);
		});
		
	}
	
	return {
		text_validate_required: text_validate_required,
		text_field_edit: text_field_edit
	};
	
}();