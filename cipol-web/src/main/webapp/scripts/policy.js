var Policy = function () {

	function groupAdd (uid, groupName) {
		var tpl = new Ext.Template(
			'<tr id="group_{0}">',
				'<th>',
					'{0}',
					' ',
					'<img src="images/delete.png" class="action" onclick="{1}" />',
				'</th>',
			'</tr>'
		);
		var action = String.format("Policy.groupDelete('{0}', '{1}');", uid, groupName);
		tpl.append('policy-groups', [ groupName, action ]);
	}

	function groupRemove (groupName) {
		var id = 'group_' + groupName;
		Ext.fly(id).remove();
	}

	function groupCreate (uid) {
		var groupName = Ext.fly('policy-group-name').dom.value;
		if (Forms.text_validate_required('policy-group-name')) {
			Ext.Ajax.request({
				url: 'ui/policy/group/' + uid + '/create',
				params: {
					name: Ext.fly('policy-group-name').dom.value
				},
				failure: function (response) {
					alert(String.format('[{0}] {1}', response.status, response.statusText));
				},
				success: function (response) {
					var message = response.responseText;
					if (message == "OK") {
						Ext.fly('policy-group-name').dom.value = '';
						groupAdd(uid, groupName);
					} else {
						alert(message);
					}
				}
			});
		}
		// Prevents submit
		return false;
	}
	
	function groupDelete (uid, name) {
		// Prompting
		if (Forms.askForConfirmation('policy.group.delete.prompt', name)) {
			// Execution
			Ext.Ajax.request({
				url: 'ui/policy/group/' + uid + '/delete/' + name,
				failure: function (response) {
					alert(String.format('[{0}] {1}', response.status, response.statusText));
				},
				success: function (response) {
					var message = response.responseText;
					if (message == "OK") {
						groupRemove(name);
					} else {
						alert(message);
					}
				}
			});
			}
	}

	return {
		groupCreate: groupCreate,
		groupDelete: groupDelete
	};

} ();