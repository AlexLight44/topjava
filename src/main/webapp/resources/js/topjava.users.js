const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled",
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ],
            "createdRow": function (row, data) {
                if (!data.enabled) {
                    $(row).addClass("disabled");
                }
            }
        })
    );
});

function enable(id, enabled) {
    const row = $("#" + id);
    const checkbox = row.find("input[type=checkbox]");

    $.ajax({
        url: userAjaxUrl + id + "?enabled=" + enabled,
        type: "PATCH"
    }).done(function () {
        row.toggleClass("disabled", !enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        checkbox.prop("checked", !enabled);
    });
}