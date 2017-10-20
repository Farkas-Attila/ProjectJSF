function refreshAllDataJS() {
    refreshAllData();
    PF('user-dt').filter();
}

function handleSaveRequest(xhr, status, args) {
    if (!args.saveFailed && !args.validationFailed) {
        PF('user-dialog').hide();
    }
};

function handleSaveDepartmentRequest(xhr, status, args) {
    if (!args.saveFailed && !args.validationFailed) {
        PF('dep-dialog').hide();
    }
};

window.onload = function() {
    PF('user-dt').filter();
};
