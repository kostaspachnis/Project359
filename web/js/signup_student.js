
function cancel() {
    window.location = "student.html";
}


function RegisterNewStudent() {
    let myForm = document.getElementById('myForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);
    var xhr = new XMLHttpRequest();
    console.log(jsonData);
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#ajaxContent').html("Successful Student Registration. Welcome! ");
            setTimeout(function(){window.location = "student.html";}, 3000);
        } else if (xhr.status !== 200) {
            $('#ajaxContent').html("ERROR!");
        }
    };
    xhr.open('POST', 'RegisterStudent');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(jsonData);
}