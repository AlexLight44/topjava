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
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return `<input type="checkbox" ${data ? "checked" : ""} 
                    onchange="enable(${row.id}, this.checked)"/>`;
                        }
                        return data;
                    }
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
    $.ajax({
        url: userAjaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        const row = $("#" + id);
        row.toggleClass("disabled", !enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        row.find("input[type=checkbox]").prop("checked", !enabled);
    });
}