function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
    
async function addNewUser() {
    function setInfoBarText(text, code) {
        var errLine = document.getElementById('dialog-addnew-errline');
        errLine.classList.remove('bg-danger');
        errLine.classList.remove('bg-primary');
        errLine.classList.remove('bg-success');
        switch (code) {
            case -1:
                errLine.classList.add('bg-danger');
                break;
            case 0:
                errLine.classList.add('bg-primary');
                break;
            case 1:
                errLine.classList.add('bg-success');
                break;
            default:
                break;
        }
        if (text != null) {
            errLine.style.visibility = null;
            errLine.textContent = text;
        } else {
            errLine.style.visibility = 'hidden';
            errLine.textContent = null;
        }
    }

    var btnAdd = document.getElementById('dialog-addnew-btnadd');
    var btnCancel = document.getElementById('dialog-addnew-btncancel');

    var tbName = document.getElementById('dialog-addnew-tbname');
    var tbEmail = document.getElementById('dialog-addnew-tbemail');
    var tbPhone = document.getElementById('dialog-addnew-tbphone');
    var tbUsername = document.getElementById('dialog-addnew-tbusername');
    var tbPassword = document.getElementById('dialog-addnew-tbpassword');
    var tbRePassword = document.getElementById('dialog-addnew-tbrepassword');
    var tbUsertype = document.getElementById('dialog-addnew-usertype');

    function enableControl(enabled) {
        tbName.readonly = !enabled;
        tbEmail.readonly = !enabled;
        tbPhone.readonly = !enabled;
        tbUsername.readonly = !enabled;
        tbPassword.readonly = !enabled;
        tbRePassword.readonly = !enabled;
        tbUsertype.readonly = !enabled;

        btnAdd.disabled = !enabled;
        btnCancel.disabled != enabled;
    }

    if (tbPassword.value !== tbRePassword.value) {
        setInfoBarText("Re-enter password and Password mismatch!", -1);
        return;
    }

    btnAdd.textContent = 'Adding...';
    enableControl(false);
    setInfoBarText("Adding user...", 0);

    await fetch('/admin/users/add', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify({
            "type": "user",
            "username": tbUsername.value,
            "password": tbPassword.value,
            "name": tbName.value,
            "phone": tbPhone.value,
            "email": tbEmail.value,
            "usertype": tbUsertype.value
        })
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        btnAdd.textContent = 'Add';
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else {
            setInfoBarText("Successfully added user! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}

async function updateUser() {
    function setInfoBarText(text, code) {
        var errLine = document.getElementById('dialog-addnew-errline');
        errLine.classList.remove('bg-danger');
        errLine.classList.remove('bg-primary');
        errLine.classList.remove('bg-success');
        switch (code) {
            case -1:
                errLine.classList.add('bg-danger');
                break;
            case 0:
                errLine.classList.add('bg-primary');
                break;
            case 1:
                errLine.classList.add('bg-success');
                break;
            default:
                break;
        }
        if (text != null) {
            errLine.style.visibility = null;
            errLine.textContent = text;
        } else {
            errLine.style.visibility = 'hidden';
            errLine.textContent = null;
        }
    }

    var btnUpdate = document.getElementById('dialog-addnew-btnupdate');
    var btnCancel = document.getElementById('dialog-addnew-btncancel');

    var tbId = document.getElementById('dialog-addnew-tbid');
    var tbName = document.getElementById('dialog-addnew-tbname');
    var tbEmail = document.getElementById('dialog-addnew-tbemail');
    var tbPhone = document.getElementById('dialog-addnew-tbphone');
    var tbUsername = document.getElementById('dialog-addnew-tbusername');
    var tbUsertype = document.getElementById('dialog-addnew-usertype');

    function enableControl(enabled) {
        tbId.readonly = !enabled;
        tbName.readonly = !enabled;
        tbEmail.readonly = !enabled;
        tbPhone.readonly = !enabled;
        tbUsername.readonly = !enabled;
        tbUsertype.readonly = !enabled;
        btnUpdate.disabled = !enabled;
        btnCancel.disabled != enabled;
    }

    btnUpdate.textContent = 'Saving...';
    enableControl(false);
    setInfoBarText("Updating user...", 0);

    await fetch('/admin/users/update', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify({
            "type": "user",
            "id": tbId.value,
            "username": tbUsername.value,
            "name": tbName.value,
            "phone": tbPhone.value,
            "email": tbEmail.value,
            "usertype": tbUsertype.value
        })
    })
    .then(jsondata => jsondata.text())
    .then(body => {
        btnUpdate.textContent = 'Save Changes';
        const result = JSON.parse(body);

        if (!result["isSuccessfulRequest"]) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(result["message"], -1);
            enableControl(true);
        } else {
            setInfoBarText("Successfully updated user! Returning to Users page...", 1);
            window.location.href = "/admin/users";
        }
    });
}

