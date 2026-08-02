const mealAjaxUrl = "meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
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
                if (!data.enabled) {
                    $(row).addClass("disabled");
                }
            }
        })
    );
});
function clearFilter() {
    $("#filter")[0].reset();
    updateTable();
}

function deleteRow(id) {
    if (!confirm("Are you sure?")) return;
    $.ajax({
        url: mealAjaxUrl + id,
        type: "DELETE"
    }).done(function () {
        successNoty("Deleted");
        updateTable();
    });
}

function updateTable() {
    $.ajax({
        type: "GET",
        url: "meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}