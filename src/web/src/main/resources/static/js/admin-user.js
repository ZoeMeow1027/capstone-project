// https://stackoverflow.com/a/11339012
function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function setInfoBarText($infoBarElement, text, code) {
    $infoBarElement.classList.remove('bg-danger');
    $infoBarElement.classList.remove('bg-primary');
    $infoBarElement.classList.remove('bg-success');
    switch (code) {
        case -1:
            $infoBarElement.classList.add('bg-danger');
            break;
        case 0:
            $infoBarElement.classList.add('bg-primary');
            break;
        case 1:
            $infoBarElement.classList.add('bg-success');
            break;
        default:
            break;
    }
    if (text != null) {
        $infoBarElement.style.visibility = null;
        $infoBarElement.textContent = text;
    } else {
        $infoBarElement.style.visibility = 'hidden';
        $infoBarElement.textContent = null;
    }
}

function enableFormControl(form, enabled) {
    if (enabled == true) {
        form.find('input, textarea, select, button').attr('disabled', null);
    } else {
        form.find('input, textarea, select, button').attr('disabled', 'disabled');
    }
}

async function addNewUser() {
    var data = getFormData($('#modal-user-modify'));
    data['type'] = 'user';

    if (data['password'] !== data['repassword']) {
        setInfoBarText(document.getElementById('modal-info-bar'), "Re-enter password and Password mismatch!", -1);
        return;
    }

    enableFormControl($('#modal-user-addnew'), false);
    setInfoBarText(document.getElementById('modal-info-bar'), "Adding user...", 0);

    await fetch('/admin/users/add', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify(data)
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-addnew'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-addnew'), true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully added user! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}

async function updateUser() {
    var data = getFormData($('#modal-user-modify'));
    data['type'] = 'user';

    enableFormControl($('#modal-user-addnew'), false);
    setInfoBarText(document.getElementById('modal-info-bar') , "Updating user...", 0);

    await fetch('/admin/users/update', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify(data)
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-addnew'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-addnew'), true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully updated user! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}

async function resetPassword() {
    var data = getFormData($('#modal-user-resetpass'));
    data['type'] = 'user';

    if (data['password'] !== data['repassword']) {
        setInfoBarText(document.getElementById('modal-info-bar') , "Re-enter password and Password mismatch!", -1);
        return;
    }

    enableFormControl($('#modal-user-resetpass'), false);
    setInfoBarText(document.getElementById('modal-info-bar'), "Reseting user password...", 0);

    await fetch('/admin/users/resetpassword', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify(data)
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-resetpass'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-resetpass'), true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully reset user password! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}

async function toggleUser(id, enabled) {
    enableFormControl($('#modal-user-toggle'), false);
    setInfoBarText(
        document.getElementById('modal-info-bar'),
        (enabled == '1' ? 'Enabling' : 'Disabling') + " user...",
        0
    );

    await fetch('/admin/users/toggle', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify({
            "type": "user",
            "id": id,
            "enabled": enabled
        })
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-toggle'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-user-toggle'), true);
        } else {
            setInfoBarText(
                document.getElementById('modal-info-bar') ,
                "Successfully " + (enabled == '1' ? 'enabled' : 'disabled') + " user! Returning to Users page...",
                1
            );
            window.location.href = "/admin/users";
        }
    });
}