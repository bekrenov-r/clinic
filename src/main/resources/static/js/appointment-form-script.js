(() => {
    let form = document.querySelector('.needs-validation');
    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
})()

async function populateDepartments() {
    let specializationSelect = document.getElementById("specialization-select");
    let selectedSpecializationName = specializationSelect.value;
    let departmentSelect = document.getElementById("department-select");
    departmentSelect.innerHTML = "";
    let placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.text = "Wybierz oddział";
    placeholderOption.selected = true;
    placeholderOption.disabled = true;
    departmentSelect.add(placeholderOption);
    await fetch("/departments/" + selectedSpecializationName)
        .then(response => {
            return response.json();
        })
        .then(departments => {
            departments.forEach(department => {
                let option = document.createElement("option");
                let optionText = department.departmentName + ", " + department.address.street + " " + department.address.buildingNumber;
                option.value = department.id;
                option.text = optionText;
                departmentSelect.add(option);
            });
        })
        .catch(e => {
            console.log(e.errorText);
        });
}

async function populateDoctors() {
    let departmentSelect = document.getElementById("department-select");
    let selectedDepartmentId = departmentSelect.value;
    let doctorSelect = document.getElementById("doctor-select");
    doctorSelect.innerHTML = "";
    let placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.text = "Wybierz lekarza";
    placeholderOption.selected = true;
    placeholderOption.disabled = true;
    doctorSelect.add(placeholderOption);
    await fetch("/doctors/bydepartment/" + selectedDepartmentId)
        .then(response => {
            return response.json()
        })
        .then(doctors => {
            doctors.forEach(doctor => {
                let option = document.createElement("option");
                option.value = doctor.id;
                option.text = doctor.firstAndLastName;
                doctorSelect.add(option);
            });
        })
        .catch(e => {
            console.log(e.errorText);
        });

}

async function disableDoctorSelect(isChecked) {
    let doctorSelect = document.getElementById("doctor-select");
    doctorSelect.disabled = isChecked;
}

async function populateTimeSelect(dateStr) {
    let doctorSelect = document.getElementById("doctor-select");
    let departmentSelect = document.getElementById("department-select");
    let timeSelect = document.getElementById("time-select");
    timeSelect.innerHTML = "";
    let placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.text = "Wybierz czas";
    placeholderOption.selected = true;
    placeholderOption.disabled = true;
    timeSelect.add(placeholderOption);
    let url;
    if (!doctorSelect.disabled && (doctorSelect.value !== "")) {
        url = "/appointments/available-times-by-doctor?doctorId=" + doctorSelect.value + "&date=" + dateStr;
    } else if (departmentSelect.value !== "") {
        url = "/appointments/available-times-by-department?departmentId=" + departmentSelect.value + "&date=" + dateStr;
    }
    await fetch(url)
        .then(response => {
            return response.json()
        })
        .then(times => {
            times.forEach(time => {
                let option = document.createElement("option");
                option.value = time;
                option.text = time;
                timeSelect.add(option);
            })
        });
}