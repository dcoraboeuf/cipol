var Policy = function () {

	function groupAdd (groupName) {
		var tpl = new Ext.Template(
			'<tr>',
				'<th>{0}</th>',
			'</tr>'
		);
		tpl.append('policy-groups', [ groupName ]);
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
						groupAdd(groupName);
					} else {
						alert(message);
					}
				}
			});
		}
		// Prevents submit
		return false;
	}

	return {
		groupCreate: groupCreate
	};

} ();