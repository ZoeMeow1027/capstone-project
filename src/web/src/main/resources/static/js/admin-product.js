// https://stackoverflow.com/a/11339012
function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    var numberProps = ['manufacturerid', 'categoryid', 'inventorycount', 'id', 'warrantymonth', 'price'];

    $.map(unindexed_array, function(n, i){
        if (numberProps.includes(n['name'])) {
            indexed_array[n['name']] = Number(n['value']);
        } else {
            indexed_array[n['name']] = n['value'];
        }
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

async function addNewProduct() {
    var data = getFormData($('#modal-product-modify'));

    enableFormControl($('#modal-product-modify'), false);
    setInfoBarText(document.getElementById('modal-info-bar'), "Adding product...", 0);

    await fetch('/admin/products/add', {
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
            enableFormControl($('#modal-product-modify'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-product-modify'), true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully added product! Returning to Products page...", 1);
            window.location.href = "/admin/products";
        }
    });
    console.log(data);
}

async function updateProduct() {
    var data = getFormData($('#modal-product-modify'));

    enableFormControl($('#modal-product-modify'), false);
    setInfoBarText(document.getElementById('modal-info-bar'), "Updating product...", 0);

    console.log(data);

    await fetch('/admin/products/update', {
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
            enableFormControl($('#modal-product-modify'), true);
        } else if (result["statusCode"] != 200) {
            setInfoBarText(document.getElementById('modal-info-bar') , result["message"], -1);
            enableFormControl($('#modal-product-modify'), true);
        } else {
            setInfoBarText(document.getElementById('modal-info-bar') , "Successfully updated product! Returning to Products page...", 1);
            window.location.href = "/admin/products";
        }
    });
}
