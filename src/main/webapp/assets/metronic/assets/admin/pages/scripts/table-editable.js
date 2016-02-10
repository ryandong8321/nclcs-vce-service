var TableEditable = function () {

	var saveStatus;
    var handleTable = function (basePath) {
    	alert("basePath="+basePath);

        function restoreRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);

            for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                oTable.fnUpdate(aData[i], nRow, i, false);
            }

            oTable.fnDraw();
        }

        function editRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
            jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
            jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
            //jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[3] + '">';
            jqTds[3].innerHTML = '<a class="edit" href="">Save</a>';
            jqTds[4].innerHTML = '<a class="cancel" href="">Cancel</a>';
        }

        function saveRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            
            $.ajax({
				type : "POST",
				async : false,
				contentType : "application/json; charset=utf-8",
				url : basePath+"sysrolesmanagement/savesysroles.do",
		        data: "{'roleName':'"+jqInputs[1].value+"','roleMemo':'"+jqInputs[2].value+"','status':'"+saveStatus+"'}",
		        dataType: 'json',
		        success: function(result) {
		        	if (result.status==1){
		        		oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
		                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
		                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
		                //oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
		                oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 3, false);
		                oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 4, false);
		                oTable.fnDraw();
		                saveStutes="-1";
		        	}
		        	showMessage(result.data);
		        }
		    });
            
            
//            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
//            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
//            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
//            //oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
//            oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 3, false);
//            oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 4, false);
//            oTable.fnDraw();
        }

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            //oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 3, false);
            oTable.fnDraw();
            saveStatus="-1";
        }

        var table = $('#sysroles_editable');

        var oTable = table.dataTable({

            // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
            // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js). 
            // So when dropdowns used the scrollable div should be removed. 
            //"dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>",

            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],

            // Or you can use remote translation file
            //"language": {
            //   url: '//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/Portuguese.json'
            //},

            // set the initial value
            "pageLength": 10,

            "language": {
                "lengthMenu": " _MENU_ records"
            },
            "columnDefs": [{ // set default column settings
                'orderable': true,
                'targets': [0]
            }, {
                "searchable": true,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] // set first column as a default sort by asc
        });

        var tableWrapper = $("#sysroles_editable_wrapper");

        tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: true //hide search box with special css class
        }); // initialize select2 dropdown

        var nEditing = null;
        var nNew = false;

        $('#sysroles_editable_new').click(function (e) {
            e.preventDefault();

            if (nNew && nEditing) {
                if (confirm("Previose row not saved. Do you want to save it ?")) {
                	saveStatus="0";
                    saveRow(oTable, nEditing); // save
                    $(nEditing).find("td:first").html("Untitled");
                    nEditing = null;
                    nNew = false;

                } else {
                    oTable.fnDeleteRow(nEditing); // cancel
                    nEditing = null;
                    nNew = false;
                    
                    return;
                }
            }

            var aiNew = oTable.fnAddData(['', '', '', '', '']);
            var nRow = oTable.fnGetNodes(aiNew[0]);
            editRow(oTable, nRow);
            nEditing = nRow;
            nNew = true;
        });

        table.on('click', '.delete', function (e) {
            e.preventDefault();

//            if (confirm("Are you sure to delete this row ?") == false) {
//                return;
//            }
            
//            var nRow = $(this).parents('tr')[0];
//            oTable.fnDeleteRow(nRow);
//            alert("Deleted! Do not forget to do some ajax to sync with backend :)");
            
            bootbox.confirm("<font size='3'>You selected row(s) will be deleted.</font>", function (result){
    			if (result==true){
    				
    				var nRow = $(this).parents('tr')[0], jqInputs = $('input', nRow);
    				$.ajax({
						type : "POST",
						async : false,
						contentType : "application/json; charset=utf-8",
						url : basePath+"sysrolesmanagement/deletesysroles.do",
				        data: "{'roleName':'"+jqInputs[1].value+"'}",
				        dataType: 'json',
				        success: function(result) {
				        	if (result.status==1){
				        		oTable.fnDeleteRow(nRow);
				        	}
				        	showMessage(result.data);
				        }
				    });
    				
    				
    				//var nRow = $(this).parents('tr')[0];
    	            //oTable.fnDeleteRow(nRow);
    			}
    		});

            
        });

        table.on('click', '.cancel', function (e) {
            e.preventDefault();
            if (nNew) {
                oTable.fnDeleteRow(nEditing);
                nEditing = null;
                nNew = false;
            } else {
                restoreRow(oTable, nEditing);
                nEditing = null;
            }
        });

        table.on('click', '.edit', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];

            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                restoreRow(oTable, nEditing);
                editRow(oTable, nRow);
                nEditing = nRow;
            } else if (nEditing == nRow && this.innerHTML == "Save") {
                /* Editing this row and want to save it */
            	saveStatus="1";
                saveRow(oTable, nEditing);
                nEditing = null;
                alert("Updated! Do not forget to do some ajax to sync with backend :)");
            } else {
                /* No edit in progress - let's start one */
                editRow(oTable, nRow);
                nEditing = nRow;
            }
        });
    }

    return {

        //main function to initiate the module
        init: function (basePath) {
            handleTable(basePath);
        }

    };

}();