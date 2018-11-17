var editorElement = document.getElementsByClassName("editable")[0];
var quill = new Quill(".editable", {
    theme: "bubble"
});

// Options behaviour
var optionsButton = document.getElementById("options-button");

// The two containers
var editorContainer = document.getElementById("editor-container");
var options = document.getElementById("options");

function setHeight() {
    /* options.removeAttribute("hidden"); */
    /* options.setAttribute("hidden", true); */
    if (options.hasAttribute("hidden")) {
        editorContainer.style.height = "94%";
    }
    else {
        editorContainer.style.height = "54%";
        options.style.height = "40%";
    }
}

// Set the behaviour for optionsButton
optionsButton.addEventListener("click", function(event) {
    if (options.hasAttribute("hidden")) {
        options.removeAttribute("hidden");
    }
    else {
        options.setAttribute("hidden", true);
    }

    setHeight();
});

/* Activate tooltips */
$(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

/* Activate toggle */
$(function () {
    $('#auto-search-control').bootstrapToggle({
        on: 'Auto-search enabled',
        off: 'Auto-search disabled',
        size: 'small',
        onstyle: 'info',
        offstyle: 'secondary',
        width: '200'
    });
});
$(function () {
    $('#speech-control').bootstrapToggle({
        on: 'Speech-transcription enabled',
        off: 'Speech-transcription disabled',
        size: 'small',
        onstyle: 'info',
        offstyle: 'secondary',
        width: '200'
    });
});
$('#auto-search-control').change(function () {
    console.log($(this).prop('checked'));
});

/* Recorder js */
var stream = null;
var recorder = null;
navigator.mediaDevices.getUserMedia ({audio: true}).then(
    function (str) {
        var audioCtx = new AudioContext();
        stream = audioCtx.createMediaStreamSource(str);
        recorder = new Recorder(stream);
    }
)
$('#start-button').on('click', function (event) {
    console.log('started');
    recorder.record();
});
$('#stop-button').on('click', function (event) {
    recorder.stop();
    recorder.exportWAV(createDownloadLink);
});
function createDownloadLink(blob) {
    var url = URL.createObjectURL(blob);
    var link = document.getElementById('download-button');

    link.src = url;
    link.download = (document.getElementById('recordingname')).value + '.wav';
    link.innerHTML = link.download;
}
 // add listener to the editor

 quill.on("text-change",
 function(event) {
     foo();
 }
);

var previousWordCount = 1;

// handler
function foo() {
 var box = $('#auto-search-tab')[0];

 // get the text
 var content = quill.getText();
 // split it array and get the last ten words
 var words = content.split("\n");
 /* words = content.split(" "); */
 words = words[0].split(" ");

 // save the word count
 var currentWordCount = words.length;

 // get the last ten words
 var lastTenWords = words.splice(words.length - 10, 10);

 // make the query
 var query = lastTenWords.join(" ");

 // make an ajax request
 var formdata = new FormData();
 formdata.append("searchQuery", query);

 var opts = {
     url: 'http://localhost:8080/textSearch',
     data: formdata,
     cache: false,
     contentType: false,
     processData: false,
     method: 'POST',
     success: function(data){

         // iterate over all the documents and acculate the html
         var suggestionsHtml = "";

         data.forEach(
             function(doc) {
                suggestionsHtml += '<li class="list-group-item">';
                
                suggestionsHtml += "<span>";
                suggestionsHtml += '<a data-toggle="collapse" href="#a' + doc['id'] + '">';
                suggestionsHtml += '<i class="fas fa-greater-than"></i></a>';
                suggestionsHtml += "</span>";

                suggestionsHtml += "<span> Title: <strong>";
                suggestionsHtml += doc['title'] ;
                suggestionsHtml += "</strong></span>";
                
                suggestionsHtml += "<span> Date: ";
                suggestionsHtml += doc['date'];
                suggestionsHtml += "</span>";

                suggestionsHtml += "<span> By: ";
                suggestionsHtml += doc['owner'] ;
                suggestionsHtml += "</span>";

                suggestionsHtml += "<span>";
                suggestionsHtml += ' <a href="http://localhost:8080/file?id=' + doc['id'] + '">';
                suggestionsHtml += '<i class="fas fa-download"></i></a>';
                suggestionsHtml += "</span>";

                suggestionsHtml += "<span>";
                suggestionsHtml += ' <a href="http://localhost:8080/viewfile?id=' + doc['id'] + '">';
                suggestionsHtml += "View</a>";
                suggestionsHtml += "</span>";

                suggestionsHtml += '<div class="collapse" id="a' + doc['id'] + '">';
                suggestionsHtml += doc['content'];
                suggestionsHtml += "</div>";

                suggestionsHtml += "</li>";
             }
         );

         // add to the suggestions div
         box.innerHTML = suggestionsHtml;
     },
     error: function (e) {
         console.log(e);
     }
 };

 if (!(currentWordCount === 1) && !(currentWordCount === previousWordCount) && ($('#auto-search-control').prop('checked'))) {
     jQuery.ajax(opts);
 }

 previousWordCount = currentWordCount;
}

$('#save-button').on('click', function (event) {
    var file = new File([quill.getText()], document.getElementById('filename').value);
    var formdata = new FormData();

    formdata.append("file", file);

    $.ajax({
        url: 'http://localhost:8080/upload',
        data: formdata,
        cache: false,
        contentType: false,
        processData: false,
        method: 'POST',
        success: function() {},
        error: function(error) {console.log(error)}
    });
});