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
                {"data": "dateTime"
                },
                {"data": "description"
                },
                {"data": "calories"
                },
                {"orderable": false
                }
            ],
            "order": [[0, "desc"]]
        })
    );
});