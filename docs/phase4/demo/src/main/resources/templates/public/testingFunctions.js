function sendEmail() {
	var current_href = window.location.href;
	var bodyMsg = "Please check this file here: " + current_href+ " . That is so useful!"
	var link = "mailto:?subject=Message from email@example.com"
			+ "&body=" + bodyMsg;
	window.location.href = link;
}

