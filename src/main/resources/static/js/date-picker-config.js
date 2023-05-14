const config = {
    "locale": "pl",
    altFormat : "l, j F Y",
    altInput : true,
    allowInput : true,
    dateFormat : "Y-m-d",
    enable : [
        /*{
            from: today(),
            to: addDays(today(), 90)
        }*/
        (date) => {
            return (date >= today() && date <= addDays(today(), 90)) &&
                (date.getDay() !== 0 && date.getDay() !== 6);
        }
    ],
/*    disable : [
        (date) => {
            return (date.getDay() === 0 || date.getDay() === 6);
        }
    ],*/
    // hooks:
    onChange : function(selectedDates, dateStr, instance){
        console.log(dateStr);
        populateTimeSelect(dateStr);
    }
};
flatpickr("#date-picker", config);

function today(){
    return new Date();
}

function addDays(date, days){
    let millisToAdd = days*24*60*60*1000;
    date.setTime(date.getTime() + millisToAdd);
    return date;
}