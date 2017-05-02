// ----------------------
// Inline table editor
// ----------------------

$(function () {
    
    $('#editable td').editable({
        closeOnEnter : true,
        event:"click",
        touch : true,
        callback: function(data) {

            if( data.fontSize ) {
                alert('You changed the font size to '+data.fontSize);
            }
        }
    });
});


// -------------------------------
// Initialize Data Tables
// -------------------------------

$(document).ready(function() {
    $('.datatables').dataTable({
        "sDom": "<'row'<'col-xs-6'l><'col-xs-6'f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        "sPaginationType": "bootstrap",
        "oLanguage": {
            "sLengthMenu": "_MENU_ records per page",
            "sSearch": ""
        }
    });
    $('.dataTables_filter input').addClass('form-control').attr('placeholder','Search...');
    $('.dataTables_length select').addClass('form-control');} );

//-------------------------
// With Table Tools Editor
//-------------------------

var editor;

$(function () {
    editor = new $.fn.dataTable.Editor({
        "ajaxUrl":"assets/demo/source.json",
        "domTable":"#crudtable",
        "fields":[
            {
                "label":"Username:",
                "name":"username"
            },
            {
                "label":"Full Name:",
                "name":"fullname"
            },
            {
                "label":"Email:",
                "name":"email"
            },
            {
                "label":"Permission:",
                "name":"permission",
				"type": "select",
				ipOpts: [
                    { label: "Super admin", value: "1" },
					{ label: "Admin", value: "2" },
					{ label: "User", value: "3" }
                ]
            }
        ]
    });

    $('#crudtable').dataTable({
        "sDom":"<'row'<'col-sm-6'T><'col-sm-6'f>r>t<'row'<'col-sm-6'i><'col-sm-6'p>>",
        "sAjaxSource":"assets/demo/source-admin.json",
        "bServerSide": false,
        "bAutoWidth": false,
        "bDestroy": true,
        "aoColumns":[
            { "mData":"username" },
            { "mData":"fullname" },
            { "mData":"email" },
            { "mData":"permission" }
        ],
        "oTableTools":{
            "sRowSelect":"multi",
            "aButtons":[
                { "sExtends":"editor_create", "editor":editor },
                { "sExtends":"editor_edit", "editor":editor },
                { "sExtends":"editor_remove", "editor":editor }
            ]
        }
    });
    $('.dataTables_filter input').addClass('form-control').attr('placeholder','Search...');
    $('.dataTables_length select').addClass('form-control');

    //add icons
    $("#ToolTables_crudtable_0").prepend('<i class="fa fa-plus"/> ');
    $("#ToolTables_crudtable_1").prepend('<i class="fa fa-pencil-square-o"/> ');
    $("#ToolTables_crudtable_2").prepend('<i class="fa fa-times-circle"/> ');
});