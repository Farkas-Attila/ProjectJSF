function checkInputs(form) {
    var jsUsername = form[form.id + ":username"].value.trim();
    var jsPassword = form[form.id + ":password"].value.trim();
    var errMessage = "";
    errMessage += (jsUsername.length < 5 ? "User name: minimum length is 5 characters " : "");
    errMessage += (jsPassword.length < 5 ? "Password: minimum length is 5 characters" : "");
    if (errMessage.length > 0) {
        alert(errMessage);
        return false;
    } else {
        return true;
    }
}

function handleSaveMessage(xhr, status, args) {
    if (!args.saveFailed && !args.validationFailed) {
        PF('msg-dialog').hide();
    }
}