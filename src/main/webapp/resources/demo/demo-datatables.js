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
        "ajaxUrl":"assets/demo/source-caregivers.json",
        "domTable":"#crudtable",
        "fields":[
            {
                "label":"Name:",
                "name":"name"
            },
			{
                "label":"Sex:",
                "name":"sex",
				"type": "select",
				ipOpts: [
                    { label: "Female", value: "1" },
					{ label: "Male", value: "2" }
                ]
            },
			{
                "label":"Education level:",
                "name":"level"
            },
			{
                "label":"Years of experience:",
                "name":"exp"
            },
			{
                "label":"Availability:",
                "name":"availability",
				"type": "select",
				ipOpts: [
                    { label: "Immediate", value: "1" },
					{ label: "2-3 weeks", value: "2" },
					{ label: "1 month", value: "3" }
                ]
            },
			{
                "label":"Languages spoken additionally to English:",
                "name":"languages",
				"type": "checkbox",
				ipOpts: [
                    { label: "Mandarin", value: "1" },
					{ label: "Malay", value: "2" },
					{ label: "Tamil", value: "3" },
					{ label: "Hindi", value: "1" },
					{ label: "Malayalam", value: "2" },
					{ label: "Sinhalese", value: "3" },
					{ label: "Others", value: "3" }
                ]
            }, 
			{
                "label":"Expected salary (in USD):",
                "name":"salary-usd"
            },
			{
                "label":"Expected salary (in HKD):",
                "name":"salary-hkd"
            },
			{
                "label":"Expected salary (in SGD):",
                "name":"salary-sgd"
            },
			{
                "label":"Expected salary (in TWD):",
                "name":"salary-twd"
            },
			
            {
                "label":"Marital Status:",
                "name":"marital-status",
				"type": "select",
				ipOpts: [
                    { label: "Married", value: "1" },
					{ label: "Un-married", value: "2" }
                ]
            },
            {
                "label":"Age:",
                "name":"age"
            },
			{
                "label":"Date of Birth:",
                "name":"date-of-birth"
            },
			{
                "label":"Religion:",
                "name":"religion",
				"type": "select",
				ipOpts: [
                    { label: "Christian", value: "1" },
					{ label: "Buddhist", value: "2" },
					{ label: "Hindu", value: "3" },
					{ label: "Muslim", value: "4" },
					{ label: "Sikh", value: "5" },
					{ label: "Others", value: "6" }
				]
            },
			{
                "label":"Food choice:",
                "name":"food-choice",
				"type": "select",
				ipOpts: [
                    { label: "No restrictions", value: "1" },
					{ label: "Vegetarian", value: "2" },
					{ label: "Halal", value: "3" }
				]
            },
			{
                "label":"Nationality:",
                "name":"nationality"
            },
			{
                "label":"Place of birth:",
                "name":"place-of-birth"
            },
			{
                "label":"Has children:",
                "name":"has-children",
				"type": "select",
				ipOpts: [
                    { label: "Has Children", value: "1" },
					{ label: "No Children", value: "2" }
				]
            },
			{
                "label":"Height (cm):",
                "name":"height"
            },
			{
                "label":"Weight (kg):",
                "name":"weight"
            },
			{
                "label":"Motivation:",
                "name":"motivation",
				"type": "textarea"
            },
			{
                "label":"About:",
                "name":"about",
				"type": "textarea"
            },
			{
                "label":"Education and experience:",
                "name":"education",
				"type": "textarea"
            },
			{
                "label":"Specialties and special interests:",
                "name":"specialties",
				"type": "textarea"
            },
			{
                "label":"Hobbies:",
                "name":"hobbies",
				"type": "textarea"
            }
        ]
    });

    $('#crudtable').dataTable({
        "sDom":"<'row'<'col-sm-6'T><'col-sm-6'f>r>t<'row'<'col-sm-6'i><'col-sm-6'p>>",
        "sAjaxSource":"assets/demo/source-caregivers.json",
        "bServerSide": false,
        "bAutoWidth": false,
        "bDestroy": true,
        "aoColumns":[
            { "mData":"name" },
            { "mData":"contact" },
            { "mData":"age" },
            { "mData":"level" },
            { "mData":"exp" },
            { "mData":"salary" },
            { "mData":"availability" },
            { "mData":"resume" }
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