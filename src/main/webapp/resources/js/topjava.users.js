const userAjaxUrl = "admin/users/";

const ctx = {
    ajaxUrl: userAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": userAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {"data": "name"},
                {"data": "email"},
                {"data": "roles"},
                {
                    "data": "enabled",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return `<input type="checkbox" ${data ? "checked" : ""} 
                                    onchange="enable(this, this.checked)"/>`;
                        }
                        return data;
                    }
                },
                {"data": "registered"},
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [[0, "asc"]],
            "createdRow": function (row, data) {
                $(row).attr("id", data.id);          // важно! чтобы был id у строки
                if (!data.enabled) {
                    $(row).addClass("disabled");
                }
            }
        })
    );
});

function enable(checkbox, enabled) {
    const row = $(checkbox).closest("tr");

    $.ajax({
        url: userAjaxUrl + row.attr("id") + "?enabled=" + enabled,
        type: "PATCH"
    }).done(function () {
        row.toggleClass("disabled", !enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !enabled);
    });
}