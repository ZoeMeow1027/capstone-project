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

async function register() {
    var form = $('#register-form');
    var data = getFormData(form);

    enableFormControl(form, false);
    setInfoBarText(document.getElementById('modal-info-bar'), "Registering you to system...", 0);

    await fetch('/auth/register', {
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
            enableFormControl(form, true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl(form, true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully registered!", 1);
            window.location.href = "/";
        }
    });
    console.log(data);
}