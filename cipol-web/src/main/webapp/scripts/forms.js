var Forms = new function () {
	
	var dh = Ext.DomHelper;
	
	function action_state (id, state) {
		if ("edit" == state) {
			Ext.fly('macro-form-action-edit-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-accept-' + id).removeClass('action-hidden');
			Ext.fly('macro-form-action-cancel-' + id).removeClass('action-hidden');
			Ext.fly('macro-form-action-wait-' + id).addClass('action-hidden');
		} else if ("wait" == state) {
			Ext.fly('macro-form-action-edit-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-accept-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-cancel-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-wait-' + id).removeClass('action-hidden');
		} else {
			Ext.fly('macro-form-action-edit-' + id).removeClass('action-hidden');
			Ext.fly('macro-form-action-accept-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-cancel-' + id).addClass('action-hidden');
			Ext.fly('macro-form-action-wait-' + id).addClass('action-hidden');
		}
	}
	
	function field_invalidate (el) {
		el.addClass('macro-form-error');
	}
	
	function field_clear_validate (el) {
		el.removeClass('macro-form-error');
	}
	
	function text_validate_required (name) {
		var field = Ext.fly(name);
		var value = field.getValue();
		if (value != "") {
			field_clear_validate(field);
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
		// Gets the old value
		var oldValue = input.dom.getAttribute("oldvalue");
		var value = input.dom.value;
		if (oldValue != value) {
			// Disables the field
			input.dom.readonly = "readonly";
			input.addClass("macro-form-disabled");
			action_state(id, "wait");
			// AJAX call
			Ext.Ajax.request({
				form: 'macro-form-' + id,
				failure: function () {
					alert('Could not update the value');
					input.removeClass("macro-form-disabled");
					action_state(id, "edit");
				},
				success: function () {
					// Updates the old value to the new one
					input.dom.setAttribute("oldvalue", value);
					// Restore
					text_field_restore(id);
				}
			});
		} else {
			// Restore
			text_field_restore(id);			
		}
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
		// Delete any additional class
		input.removeClass("macro-form-disabled");
		input.removeClass("macro-form-edited");
		// Disables the text field for edition
		input.dom.setAttribute("readonly", "readonly");
		// Enables the edit action button
		action_state(id, "idle");
	}
	
	function text_field_edit (id) {
		
		// Changes the state of the actions
		action_state(id, "edit");
		
		// Enables the text field for edition
		var input = Ext.get('macro-form-input-' + id);
		input.dom.removeAttribute("readonly");
		
		// Activates the edit state
		input.addClass("macro-form-edited");
		
		// Registers event on ENTER
		input.on('keypress', function (e) {
			if (e.getCharCode() == 13) {
				text_field_accept(id);
			}
		});
		
		// Requires the focus
		input.focus();
		input.dom.select();

		// Validation button
		var acceptButton = Ext.get('macro-form-action-accept-' + id);
		acceptButton.on('click', function () {
			text_field_accept(id);
		});
		
		// Cancel button
		var cancelButton = Ext.get('macro-form-action-cancel-' + id);
		cancelButton.on('click', function () {
			text_field_cancel(id);
		});
		
	}
	
	function loc (code) {
	    var value = l[code];
	    if (value != null) {
	        return value;
	    } else {
	        return '##' + code + '##';
	    }
	}
	
	function askForConfirmation (code, args) {
		var message = String.format(loc(code), args);
		return confirm(message);
	}
	
	return {
		text_validate_required: text_validate_required,
		text_field_edit: text_field_edit,
		askForConfirmation: askForConfirmation,
		loc: loc
	};
	
}();