const config = {
    "locale": "pl",
    altFormat : "l, j F Y",
    altInput : true,
    allowInput : true,
    dateFormat : "Y-m-d",
    "disable" : [
        (date) => {
            return (date.getDay() === 0 || date.getDay() === 6);
        }
    ],
    // hooks:
    onChange : function(selectedDates, dateStr, instance){
        console.log(dateStr);
        populateTimeSelect(dateStr);
    }
};
flatpickr("#date-picker", config);