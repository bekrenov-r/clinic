(() => {

    let form = document.querySelector('.needs-validation');

    form.addEventListener('submit', event => {
        event.preventDefault();
        event.stopPropagation();
        validate(form)
            .then(() => {
                if(isValid(form)){
                    form.submit();
                }
            });
    }, false);

})()

async function validate(form) {
    let items = form.querySelectorAll('[required]');
    Array.from(items).forEach(item => {
        if(item.value === ''){
            let invalidFeedbackId = item.dataset.invalidFeedbackId;
            setTextContent(invalidFeedbackId, 'To pole nie może być puste');
            setInvalid(item);
        } else {
            setValid(item);
        }
    });

    let newPassword = document.querySelector('#new-password');
    let confirmNewPassword = document.querySelector('#confirm-new-password');
    if(newPassword.value !== confirmNewPassword.value){
        /* let invalidFeedbackId = confirmNewPassword.getAttribute('data-invalid-fb-id');*/
        setTextContent('confirm-new-password-invalid-feedback', 'Hasła nie są jednakowe');
        setInvalid(confirmNewPassword);
    }

    let currentPassword = form.querySelector('#current-password');
    if(currentPassword.value !== '') {
        let username = document.querySelector('#email').value;
        await passwordIsCorrect(username, currentPassword.value).then(passwordIsCorrect => {
            if(!passwordIsCorrect) {
                setTextContent('current-password-invalid-feedback', 'Niepoprawne hasło');
                setInvalid(currentPassword);
            }
        });
    }
}

function isValid(form){
    let items = form.querySelectorAll('[required]');
    let isValid = true;
    Array.from(items).forEach(item => {
        if(item.classList.contains('is-invalid')){
           isValid = false;
       }
    });
    return isValid;
}

async function passwordIsCorrect(username, password){
    let url = '/passwords/is-correct?username=' + username + '&password=' + password;
    return await fetch(url).then(response => {
        return response.json();
    });
}

/*todo: pull these two methods to separate file and import */
function setValid(element) {
    if(element.classList.contains('is-invalid')){
        element.classList.remove('is-invalid');
    }
    element.classList.add('is-valid');
}

function setInvalid(element) {
    if(element.classList.contains('is-valid')){
        element.classList.remove('is-valid');
    }
    element.classList.add('is-invalid');
}

function setTextContent(id, text){
    let element = document.querySelector('#' + id);
    element.textContent = text;
}

