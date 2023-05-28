(() => {
    let patientGender = document.getElementById("gender").value;
    let patientName = document.getElementById("name").value;
    let greetingHeading = document.querySelector('.greeting-title');
    if(patientGender === "MALE"){
        greetingHeading.textContent = "Witam, Panie " + patientName + "!";
    } else {
        greetingHeading.textContent = "Witam, Pani " + patientName + "!";
    }
})()
