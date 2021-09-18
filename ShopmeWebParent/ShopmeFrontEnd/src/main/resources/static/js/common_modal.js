

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	console.log(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
function showErrorModal(message) {
	showModalDialog("Error", message);
}
function showWarningModal(message) {
	showModalDialog("Warning", message);
}