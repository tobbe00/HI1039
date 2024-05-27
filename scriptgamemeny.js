document.addEventListener('DOMContentLoaded', function () {
    const menu = document.querySelector('#mobile-menu');
    const menuLinks = document.querySelector('.navbar__menu');
    const modeSelect = document.getElementById('mode');
    const zeroPointInput = document.getElementById('zero-point');
    const avgPointInput = document.getElementById('avg-point');
    const enterButton = document.getElementById('btnStart-game');
    const navbarBtn = document.querySelector('.navbar__btn');

    menu.addEventListener('click', function () {
        menu.classList.toggle('is-active');
        menuLinks.classList.toggle('active');
    });

    if (sessionStorage.getItem("isLoggedIn") === "true") {
        console.log(sessionStorage.getItem("isLoggedIn"));
        navbarBtn.innerHTML = "<a href='/signOut.html' class='button'>sign out</a>";
    }

    function updateValues() {
        let zeroPoint, avgPoint;
        switch (modeSelect.value) {
            case 'normal':
                zeroPoint = 500;
                avgPoint = 200;
                break;
            case 'child':
                zeroPoint = 500; 
                avgPoint = 150;  
                break;
            case 'elderly':
                zeroPoint = 500; 
                avgPoint = 125; 
                break;
            default:
                zeroPoint = 500;
                avgPoint = 200;
                break;
        }
        zeroPointInput.value = zeroPoint;
        avgPointInput.value = avgPoint;
    }

    modeSelect.addEventListener('change', updateValues);
    updateValues(); 

    enterButton.onclick = function () {
        if (!sessionStorage.getItem("isLoggedIn")) {
            const confirmed = window.confirm('You need to log in first. Press OK to go to the login page.');
            if (confirmed) {
                window.location.href = '/login.html';
                return;  // Förhindra ytterligare exekvering
            }
        }

        let data = {
            zero: zeroPointInput.value,
            mode: modeSelect.value,
            avg: avgPointInput.value
        };

        fetch('http://localhost:8080/game/zeropoint', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            console.log('Response:', response);
            if (response.ok) {
                sessionStorage.setItem("zeroPoint", data.zero);
                sessionStorage.setItem("avg", data.avg);
                sessionStorage.setItem("mode", data.mode);
                window.location.href = "/game.html";
            } else {
                window.confirm('Something went wrong! Try again, maybe the server is not working?');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    };
});
