let timerLink;
let hours;
let minutes;
let seconds;
let chosenSubject;
let subjects;

function startTimer() {
    const startTime = new Date().getTime();

    hours = 0;
    minutes = 0;
    seconds = 0;

    function showCurrentTimer() {
        const currentTime = new Date().getTime();
        const difference = (currentTime - startTime) / 1000

        hours = Math.floor(difference / (60 * 60));
        minutes = Math.floor(difference % (60 * 60) / 60);
        seconds = Math.floor(difference % 60);

        document.getElementById("monitor").innerHTML = hours + "h : "
            + minutes + "m : " + seconds + "s ";
    }

    timerLink = setInterval(showCurrentTimer, 1000)
}

function stopTimer() {
    clearInterval(timerLink);

    // add time on server
    const userAndToken = getUserAndToken();
    const user = userAndToken['user'];
    const sessionToken = userAndToken['sessionToken'];
    const data = document.getElementById("monitor").textContent
        .split(" : ")
        .map(x => parseInt(x.slice(0, -1), 10));
    const totalSeconds = data[0] * (60 * 60) + data[1] * (60) + data[2];
    document.getElementById("monitor").innerHTML = "0h : 0m : 0s";

    const jsonToSend = JSON.stringify({
        hours: data[0],
        minutes: data[1],
        seconds: data[2],
        "total-seconds": totalSeconds
    });

    const http = new XMLHttpRequest()
    http.open("POST", `/subjects/update-time?subjectName=${chosenSubject}&user=${user}&sessionToken=${sessionToken}`, false)
    http.setRequestHeader("Content-Type", "application/json");
    const csrfToken =  document.getElementById("stop-timer-button").getAttribute('data-csrf-value');
    http.setRequestHeader('Csrf-Token', csrfToken);
    http.send(jsonToSend);

    // delete previous subjects
    document.querySelectorAll(".subjects-li").forEach(el => el.remove());
    // show updated subjects
    subjects = getSubjects(user, sessionToken);
    showSubjects(subjects);
    addListeners();
}

function showTimer() {
    document.getElementById("timer").style.opacity = "1.0";
}

function getSubjects(user, sessionToken) {
    const http = new XMLHttpRequest()

    http.open("GET", `/subjects?user=${user}&sessionToken=${sessionToken}`, false)
    http.send()

    return JSON.parse(http.response);
}

function getUserAndToken() {
    const http = new XMLHttpRequest()

    http.open("GET", "/username-and-token", false)
    http.send()

    return JSON.parse(http.response);
}

function showSubjects(subjects) {
    document.getElementById("subjects").style.opacity = "1.0";
    const ul = document.getElementById("subjects-ul");
    
    for (let i = 0; i < subjects.length; i++) {
        const li = document.createElement("li");
        //console.log(i);
        //console.log(subjects[i]['name']);
        const subject = document.createTextNode(subjects[i]['name']);

        const time = document.createTextNode(subjects[i]['hours'] + "h : " +
            subjects[i]['minutes'] + "m : " + subjects[i]['seconds'] + "s");

        const subjectParagraph = document.createElement("p");
        const timeParagraph = document.createElement("p");
        const boldSubject = document.createElement("b");

        boldSubject.appendChild(subject);
        subjectParagraph.appendChild(boldSubject);
        timeParagraph.appendChild(time);
        li.appendChild(subjectParagraph);
        li.appendChild(timeParagraph);
        li.setAttribute("class", "subjects-li")
        ul.appendChild(li);

        if (document.querySelectorAll(".subjects-li").length > 0) {
            document.getElementById("show-statistics-button").style.opacity = "1.0";
        }
    }
}

function plotChart() {
    const holder = document.getElementById("canvas-holder");
    holder.innerHTML = "";
    const ctx = document.createElement("canvas");
    ctx.setAttribute("id", "myChart");
    ctx.setAttribute("width", "400");
    ctx.setAttribute("height", "400");
    holder.appendChild(ctx);

    const names = subjects.map(x => x['name']);
    const unitsToShow = subjects.map(x => x['hours'] + "h:" + x['minutes'] + "m:" + x['seconds'] + "s");
    const totalSeconds = subjects.map(x => parseInt(x['totalSeconds'], 10));

    function dynamicColors() {
        let r = Math.floor(Math.random() * 255);
        let g = Math.floor(Math.random() * 255);
        let b = Math.floor(Math.random() * 255);
        return "rgb(" + r + "," + g + "," + b + ", 0.2)";
    }

    const colors = names.map(x => dynamicColors());

    let myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: names,
            datasets: [{
                data: totalSeconds,
                backgroundColor: colors,
                borderColor: colors
            }]
        },
        options: {
            tooltips: {
                callbacks: {
                    label: (tooltipItems, data) => unitsToShow[tooltipItems.index]
                }
            }
        }
    });
}

function selectSubject(li) {
    document.querySelectorAll(".subjects-li").forEach(x => {
            x.style.backgroundColor = "#fff";
            showTimer();
        }
    );
    li.style.backgroundColor = "#80c846";
    //console.log(li);
    chosenSubject = li.getElementsByTagName('p')[0].textContent;
    //console.log("chosen: " + chosenSubject);
}

function addListeners() {
    document.querySelectorAll(".subjects-li").forEach(x => x.addEventListener(
        "click",
        e => selectSubject(x)
        )
    );
}

document.addEventListener("DOMContentLoaded", function() {
    const userAndToken = getUserAndToken();
    //console.log(userAndToken);
    subjects = getSubjects(userAndToken['user'], userAndToken['sessionToken']);
    //console.log(subjects);
    showSubjects(subjects);
    addListeners();
});
