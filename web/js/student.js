


function signup() {
    window.location = "signup_student.html";
}


function searchBook() {

    // let myForm = document.getElementById('bookSearchForm');
    // let formData = new FormData(myForm);
    // const data = {};
    // formData.forEach((value, key) => (data[key] = value));
    // var jsonData = JSON.stringify(data);

    let genre = document.getElementById('search_genre').value;
    let title = document.getElementById('search_title').value;
    let author = document.getElementById('search_author').value;
    let fromPage = document.getElementById('fromPageNumber').value;
    let toPage = document.getElementById('toPageNumber').value;
    let fromYear = document.getElementById('fromYear').value;
    let toYear = document.getElementById('toYear').value;

    let data = 'genre=' + genre + '&title=' + title + '&author=' + author + '&fromPage=' + fromPage + '&toPage=' + toPage + '&fromYear=' + fromYear + '&toYear=' + toYear;

    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log("success");
            console.log(xhr.responseText);
        } else if (xhr.status === 403) {
            console.log("error");
        }
    };


    xhr.open('GET', 'FindBookForStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}