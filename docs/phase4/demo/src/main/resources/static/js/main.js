$(document).keydown(function(e){
    if($("#add-comment").is(":visible")){
    } else{
    	$('[href="#add-comment"]').tab('show');
    	$("#addComment").focus()
    }
});

$(document).ready(function () {

    $("#commentForm").submit(function (event) {

        event.preventDefault();

        fire_ajax_submit();

    });

});

$(document).ready(function () {

    $("#ViewComments").on("click", function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_get_submit();
        window.setInterval(function(){
    		fire_ajax_get_submit() 
    	}, 60000);

    });

});



function fire_ajax_get_submit() {   
    var comment = {}
    comment["content"] = "senddata";
    
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/view/" + window.location.search.split("=")[1],
        data: JSON.stringify(comment),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#comments-logout ul").empty();
            $.each(data.result, function(i, Comment){
                var commentHtml = "<li class='media'> \
                <div class='media-body'> \
                  <div class='well well-lg'> \
                      <h4 class='media-heading text-uppercase reviews'>" + Comment.owner+"</h4> \
                      <ul class='media-date text-uppercase reviews list-inline'> \
                        <li class='dd'>"+ Comment.date+"</li> \
                        <li class='aaaa'>"+Comment.time+"</li> \
                      </ul> \
                      <p class='media-comment'>" +
                        Comment.content +  
                      "</p> \
                </div> \
              </li> ";
                $("#comments-logout .media-list").append(commentHtml)
            });
            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}



function fire_ajax_submit() {

    // var search = {}
    // search["username"] = $("#username").val();
    // //search["email"] = $("#email").val();
    var comment = {}
    comment["content"] = $("#addComment").val();

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/save/" + window.location.search.split("=")[1], 
        data: JSON.stringify(comment),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#comments-logout ul").empty();
            $.each(data.result, function(i, Comment){
                var commentHtml = "<li class='media'> \
                <div class='media-body'> \
                  <div class='well well-lg'> \
                      <h4 class='media-heading text-uppercase reviews'>" + Comment.owner+"</h4> \
                      <ul class='media-date text-uppercase reviews list-inline'> \
                        <li class='dd'>"+ Comment.date+"</li> \
                        <li class='aaaa'>"+Comment.time+"</li> \
                      </ul> \
                      <p class='media-comment'>" +
                        Comment.content +  
                      "</p> \
                  </div> \
                </div> \
              </li> ";
              $("#comments-logout .media-list").append(commentHtml)
            });
            

            $("#addComment").val('');
            $('[href="#comments-logout"]').tab('show');
            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}