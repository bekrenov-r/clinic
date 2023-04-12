function getOptions(inputValue) {
    let options = document.getElementById('city-options');
    let dropdownToggle = document.querySelector('.dropdown-toggle');
    let input = document.getElementById('city-input');
    options.innerHTML = '';
    fetch("/addresses/cities?pattern=" + inputValue)
        .then(response => {
            return response.json();
        })
        .then(cities => {
            cities.forEach(city => {
                let option = document.createElement("a");
                option.classList.add("dropdown-item")
                option.href = "#"
                option.textContent = city;
                option.addEventListener("click", () => {
                    input.value = city;
                    input.textContent = city;
                    options.innerHTML = "";
                });
                let option_li = document.createElement("li");
                option_li.appendChild(option);
                options.appendChild(option_li);
            })
            // dropdownToggle.addEventListener('shown.bs.dropdown', () => input.focus());
            let dropdown = new bootstrap.Dropdown(dropdownToggle);
            dropdown.show();
            input.focus();
        })
}

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






