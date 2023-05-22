async function toggleUser(id, enabled) {
    function setInfoBarText(text, code) {
        var infoLine = document.getElementById('modal-toggleuser-infoline');
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

    var btnEnable = document.getElementById('modal-toggleuser-toggle');
    var btnCancel = document.getElementById('modal-toggleuser-btncancel');

    function enableControl(enabled) {
        btnEnable.disabled = !enabled;
        btnCancel.disabled = !enabled;
    }

    btnEnable.textContent = enabled == '1' ? 'Enabling...' : 'Disabling...';
    enableControl(false);
    setInfoBarText(enabled == '1' ? 'Enabling' : 'Disabling' + " user...", 0);

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
        btnEnable.textContent = enabled == '1' ? 'Enable' : 'Disable';
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else {
            setInfoBarText("Successfully " + (enabled == '1' ? 'enabled' : 'disabled') + " user! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}