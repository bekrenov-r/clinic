function enableSubmitButton() {
    let submitButton = document.getElementById('submit-button');
    if (submitButton.disabled) {
        submitButton.disabled = false;
    }
}

(() => {
    const form = document.querySelector('.needs-validation');

    // Add event listener for input
    form.addEventListener('input', event => {
        let input = event.target;
        if (input.checkValidity()) {
            input.classList.remove('is-invalid');
        } else {
            input.classList.add('is-invalid');
        }
        form.classList.add('was-validated');
    });

    // Add event listener for form submission
    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
})()

// let requiredElements = form.querySelector('[required]');
// requiredElements.forEach(element => {
//     if(element.checkValidity()){
//         if(element.classList.contains('is-invalid')){
//             element.classList.remove('is-invalid');
//         }
//         element.classList.add('is-valid');
//     } else {
//         element.classList.add('is-invalid');
//     }
// });






