(() => {
    let form = document.querySelector('.needs-validation');

    let submitPersonalData = document.querySelector('#submit-personal-data');

    submitPersonalData.addEventListener('click', () => {
        validatePersonalData()
            .then(() => {
                if(personalDataIsValid()){
                    switchTabs();
                }
            });
    });

    form.addEventListener('submit', event => {
        event.preventDefault();
        event.stopPropagation();
        validateUserData(form)
            .then(() => {
                let valid = userDataIsValid();
                console.log('user data vaild: ' + valid);
                if(userDataIsValid()){
                    form.submit();
                    /*event.preventDefault();
                    event.stopPropagation();*/
                }
            });

    }, false);
})();

async function validatePersonalData(){
    let items = document.querySelectorAll('#personal-data-container [required]');
    Array.from(items).forEach(item => {
        // generic check on empty value
        if(item.value === ''){
            setInvalid(item);
        } else {
            setValid(item);
        }

        if(item.id === 'phone-number'){
            // check phone number against regex
            let regex = /^[0-9]{9}$/g;
            if(item.value !== '' && !regex.test(item.value)){
                setTextContent('phone-number-invalid-feedback', 'Niepoprawny format numeru');
                setInvalid(item);
            } /*else {
                // check if phone number already exists
                phoneNumberExists(item.value)
                    .then(response => {
                        if(response){
                            setTextContent('phone-number-invalid-feedback', 'Podany numer telefonu już jest zarejestrowany');
                            setInvalid(item);
                        }
                    });
            }*/
        }
        if(item.id === 'pesel'){
            let regex = /^[0-9]{11}$/g;
            // check pesel against regex
            if(item.value !== '' && !regex.test(item.value)){
                setTextContent('pesel-invalid-feedback', 'Niepoprawny format numeru');
                setInvalid(item);
            } /*else {
                // check if pesel already exists
                peselExists(item.value)
                    .then(response => {
                        if(response){
                            setTextContent('pesel-invalid-feedback', 'Podany PESEL już jest zarejestrowany');
                            setInvalid(item);
                        }
                    });
            }*/
        }
        if(item.id === 'postal-code'){
            let regex = /^[0-9]{2}-[0-9]{3}$/g;
            if(!regex.test(item.value)){
                setTextContent('postal-code-invalid-feedback', 'Niepoprawny format kodu');
                setInvalid(item);
            } else {
                setValid(item);
            }
        }
        if(item.id === 'agreement-checkbox'){
            if(!item.checked){
                setInvalid(item);
            } else {
                setValid(item);
            }
        }
    });

    // check if phone number already exists
    let phoneNumber = document.querySelector('#phone-number');
    await phoneNumberExists(phoneNumber.value)
        .then(response => {
            if(response){
                setTextContent('phone-number-invalid-feedback', 'Podany numer telefonu już jest zarejestrowany');
                setInvalid(phoneNumber);
            }
        });

    // check if pesel already exists
    let pesel = document.querySelector('#pesel');
    await peselExists(pesel.value)
        .then(response => {
            if(response){
                setTextContent('pesel-invalid-feedback', 'Podany PESEL już jest zarejestrowany');
                setInvalid(pesel);
            }
        });
}

function personalDataIsValid(){
    let formIsValid = true;
    let items = document.querySelectorAll('#personal-data-container [required]')
    Array.from(items).forEach(item => {
       if(item.classList.contains('is-invalid')){
           formIsValid = false;
       }
    });
    return formIsValid;
}

async function validateUserData(form){
    console.log('validating user data');
    let items = document.querySelectorAll('#user-data-container [required]');
    Array.from(items).forEach(item => {
        // generic check on empty value
        if(item.value === ''){
            setInvalid(item);
        } else {
            setValid(item);
        }
    });

    let password = document.querySelector('#password');
    let confirmPassword = document.querySelector('#confirm-password');
    if(password.value !== confirmPassword.value){
        setTextContent('confirm-password-invalid-feedback', 'Hasła muszą odpowiadać');
        setInvalid(confirmPassword);
    }

    let email = form.querySelector('#email');
    await emailExists(email.value)
        .then(response => {
            if(response) {
                setTextContent('email-invalid-feedback', 'Podany email już jest zarejestrowany');
                setInvalid(email);
            }
        });
    console.log('user data validated');
}

function userDataIsValid(){
    let formIsValid = true;
    let items = document.querySelectorAll('#user-data-container [required]')
    Array.from(items).forEach(item => {
        // console.log(item.id + ': ' + item.classList.contains('is-invalid'));
        if(item.classList.contains('is-invalid')){
            formIsValid = false;
        }
    });
    return formIsValid;
}

function switchTabs(){
    let personalDataContainer = document.querySelector('#personal-data-container');
    let userDataContainer = document.querySelector('#user-data-container');

    if(personalDataContainer.classList.contains('active')){
        personalDataContainer.classList.remove('active');
        personalDataContainer.classList.add('hidden');

        userDataContainer.classList.remove('hidden');
        userDataContainer.classList.add('active');
    } else {
        userDataContainer.classList.remove('active');
        userDataContainer.classList.add('hidden');

        personalDataContainer.classList.remove('hidden');
        personalDataContainer.classList.add('active');
    }
}

function setTextContent(id, text){
    let element = document.querySelector('#' + id);
    element.textContent = text;
}

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

async function emailExists(email){
    return await fetch('/patients/email-exists?email=' + email).then(response => {
            return response.json();
        });
}

async function peselExists(pesel){
    return await fetch('/patients/pesel-exists?pesel=' + pesel).then(response => {
        return response.json();
    });
}

async function phoneNumberExists(phoneNumber){
    return await fetch('/patients/phone-number-exists?phone-number=' + phoneNumber).then(response => {
        return response.json();
    });
}

