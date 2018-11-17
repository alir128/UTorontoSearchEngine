// create for saving future reading feature
function saveOnClick(x) {
  x.classList.toggle('glyphicon glyphicon-heart');
}

$(document).ready(function () {

    $("#searchQuery").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        search_ajax_submit();

    });

});


function search_ajax_submit() {
	var searchQuery = {}
	searchQuery["query"] = $("#searchBox").val();
	var query=$("#searchBox").val();
	var url = "/searchajax/";
	console.log(url);
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: url,
		data: {searchQuery: query},
		cache: false,
		timeout: 600000,
		success: function(data) {
			$("#resultsSection").empty();
			$("#resultsSection").append(data);
		},
		error: function(e) {
			$("#resultsSection").empty();
			$("#resultsSection").append(e["responseText"]);
		}
	})
}
