(() => {
    let form = document.querySelector('.needs-validation');
    let personalDataContainer = document.querySelector('#personal-data-container');
    let userDataContainer = document.querySelector('#user-data-container');

    let submitPersonalData = document.querySelector('#submit-personal-data');
    // let hiddenForm = document.querySelector("#hidden-form");

    submitPersonalData.addEventListener('click', () => {
        if(validatePersonalData()){
            switchTabs();
        }
    });

    form.addEventListener('submit', event => {
        if(!validateUserData()){
            event.preventDefault();
            event.stopPropagation();
        }
        /*else {
            personalDataContainer.classList.remove('active');
            personalDataContainer.classList.add('hidden');

            userDataContainer.classList.remove('hidden');
            userDataContainer.classList.add('active');

            hiddenForm.querySelector('#field-first-name').value = personalDataForm.querySelector('#first-name').value;
            hiddenForm.querySelector('#field-last-name').value = personalDataForm.querySelector('#last-name').value;
            hiddenForm.querySelector('#field-phone-number').value = personalDataForm.querySelector('#phone-number').value;
            hiddenForm.querySelector('#field-pesel').value = personalDataForm.querySelector('#pesel').value;
            hiddenForm.querySelector('#field-city').value = personalDataForm.querySelector('#city-input').value;
            hiddenForm.querySelector('#field-street').value = personalDataForm.querySelector('#street').value;
            hiddenForm.querySelector('#field-building-number').value = personalDataForm.querySelector('#building-number').value;
            hiddenForm.querySelector('#field-flat-number').value = personalDataForm.querySelector('#flat-number').value;
            hiddenForm.querySelector('#field-postal-code').value = personalDataForm.querySelector('#postal-code').value;
        }
        personalDataForm.classList.add('was-validated');*/
    }, false);

    // validate user data form
    /*userDataForm.addEventListener('submit', event => {
        let email = userDataForm.querySelector('#email');
        let password = userDataForm.querySelector('#password');
        let confirmPassword = userDataForm.querySelector('#confirm-password');
        let formIsValid = true;
        if(email.value === ""){
            setInvalid(email);
            formIsValid = false;
        } else {
            setValid(email);
        }
        if(password.value === ""){
            setInvalid(password);
            formIsValid = false;
        } else {
            setValid(password);
        }
        if(confirmPassword.value === ""){
            setTextContent('confirm-password-invalid-feedback', 'To pole nie moe być puste');
            setInvalid(confirmPassword);
            formIsValid = false;
        } else {
            setValid(confirmPassword);
        }
        if(password.value !== confirmPassword.value){
            setTextContent('confirm-password-invalid-feedback', 'Hasła muszą odpowiadać');
            setInvalid(confirmPassword);
            formIsValid = false;
        }
        if(formIsValid){
            hiddenForm.querySelector('#field-email').value = userDataForm.querySelector('#email').value;
            hiddenForm.querySelector('#field-password').value = userDataForm.querySelector('#password').value;
            console.log('submit');
            let submit = document.getElementById('submit-hidden-form');

            console.log('submit');
        } else {
            event.preventDefault();
            event.stopPropagation();
        }
        // userDataForm.classList.add('was-validated');
    }, false);

    hiddenForm.addEventListener('submit', () => {
        console.log("submitted");*/
})();

function validatePersonalData(){
    let items = document.querySelectorAll('#personal-data-container [required]');
    let formIsValid = true;
    Array.from(items).forEach(item => {
        if(item.value === ''){
            setInvalid(item);
            formIsValid = false;
        } else {
            setValid(item);
        }
    });
    return formIsValid;
}

function validateUserData(){
    let items = document.querySelectorAll('#user-data-container [required]');
    let formIsValid = true;
    Array.from(items).forEach(item => {
        if(item.value === ''){
            setInvalid(item);
            formIsValid = false;
        } else {
            setValid(item);
        }
    });

    let password = document.querySelector('#password');
    let confirmPassword = document.querySelector('#confirm-password');
    if(password.value !== confirmPassword.value){
        setTextContent('confirm-password-invalid-feedback', 'Hasła muszą odpowiadać');
        setInvalid(confirmPassword);
        formIsValid = false;
    }

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

