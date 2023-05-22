async function resetPassword() {
    function setInfoBarText(text, code) {
        var infoLine = document.getElementById('modal-resetpass-infoline');
        infoLine.classList.remove('bg-danger');
        infoLine.classList.remove('bg-primary');
        infoLine.classList.remove('bg-success');
        switch (code) {
            case -1:
                infoLine.classList.add('bg-danger');
                break;
            case 0:
                infoLine.classList.add('bg-primary');
                break;
            case 1:
                infoLine.classList.add('bg-success');
                break;
            default:
                break;
        }
        if (text != null) {
            infoLine.style.visibility = null;
            infoLine.textContent = text;
        } else {
            infoLine.style.visibility = 'hidden';
            infoLine.textContent = null;
        }
    }

    var btnUpdate = document.getElementById('modal-resetpass-btnok');
    var btnCancel = document.getElementById('modal-resetpass-btncancel');

    var tbId = document.getElementById('modal-resetpass-tbid');
    var tbPassword = document.getElementById('modal-resetpass-tbnewpassword');
    var tbRePassword = document.getElementById('modal-resetpass-tbrepassword');

    function enableControl(enabled) {
        tbId.readonly = !enabled;
        tbPassword.disabled = !enabled;
        tbRePassword.disabled = !enabled;
        btnCancel.disabled = !enabled;
    }

    if (tbPassword.value !== tbRePassword.value) {
        setInfoBarText("Re-enter password and Password mismatch!", -1);
        return;
    }

    btnUpdate.textContent = 'Reseting...';
    enableControl(false);
    setInfoBarText("Reseting user password...", 0);

    await fetch('/admin/users/resetpassword', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify({
            "type": "user",
            "id": tbId.value,
            "password": tbPassword.value,
            "repassword": tbRePassword.value
        })
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        btnUpdate.textContent = 'Reset Password';
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else {
            setInfoBarText("Successfully reset user password! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}