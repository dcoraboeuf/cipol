var Policy = function () {

	function groupCreate (uid) {
		if (Forms.text_validate_required('policy-group-name')) {
			Ext.Ajax.request({
				url: 'ui/policy/group/' + uid + '/create',
				params: {
					name: Ext.fly('policy-group-name').dom.value
				},
				failure: function () {
					alert('Could not create the group');
				},
				success: function () {
					alert('Updating the group');
				}
			});
		}
	}

	return {
		groupCreate: groupCreate
	};

} ();