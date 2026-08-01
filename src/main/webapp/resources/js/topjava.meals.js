const mealAjaxUrl = "meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date) {
                        return date.replace("T", " ").substring(0, 16);
                    }
                },
                {"data": "description"},
                {"data": "calories"},
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": function (data, type, row) {
                        return '<a class="delete" onclick="deleteRow(' + row.id + ')">' +
                            '<span class="fa fa-remove"></span></a>';
                    }
                }
            ],
            "order": [[0, "desc"]],
            "createdRow": function (row, data) {
                $(row).attr("id", data.id);
                if (data.excess) {
                    $(row).addClass("excess");
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