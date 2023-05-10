(() => {
    let form = document.querySelector('.needs-validation');

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
    }, false);
})();

function validatePersonalData(){
    let items = document.querySelectorAll('#personal-data-container [required]');
    let formIsValid = true;
    Array.from(items).forEach(item => {
        if(item.id === 'phone-number'){
            let regex = /^[0-9]{9}$/g;
            if(!regex.test(item.value)){
                setTextContent('phone-number-invalid-feedback', 'Niepoprawny format numeru');
                setInvalid(item);
                formIsValid = false;
            } else {
                setValid(item);
            }
        }
        if(item.id === 'pesel'){
            let regex = /^[0-9]{11}$/g;
            if(!regex.test(item.value)){
                setTextContent('pesel-invalid-feedback', 'Niepoprawny format numeru');
                setInvalid(item);
                formIsValid = false;
            } else {
                setValid(item);
            }
        }
        if(item.id === 'postal-code'){
            let regex = /^[0-9]{2}-[0-9]{3}$/g;
            if(!regex.test(item.value)){
                setTextContent('postal-code-invalid-feedback', 'Niepoprawny format kodu');
                setInvalid(item);
                formIsValid = false;
            } else {
                setValid(item);
            }
        }
        if(item.value === ''){
            setInvalid(item);
            formIsValid = false;
        } else {
            setValid(item);
        }
        if(item.id === 'agreement-checkbox'){
            if(!item.checked){
                console.log('unchecked');
                // setInvalid(item);
                item.classList.add('is-invalid');
                formIsValid = false;
            } else {
                console.log('checked');
                setValid(item);
            }
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

