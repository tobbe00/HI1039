const menu=document.querySelector('#mobile-menu');
const menuLinks=document.querySelector('.navbar__menu');

menu.addEventListener('click',function(){
    menu.classList.toggle('is-active');
    menuLinks.classList.toggle('active');
});

let signedIn=document.getElementById("signupBtn2");
let navbarBtn = document.querySelector('.navbar__btn');
if(sessionStorage.getItem("isLoggedIn")=="true"){
    console.log(sessionStorage.getItem("isLoggedIn"))
    //signedIn.innerHTML="signed in";
    navbarBtn.innerHTML = "<a href='/test5/signOut.html' class='button'>sign out</a>"
}
//page specific-----------------------------------------------------------------------------------------------------






// Add event listener to the button
let enterButton = document.getElementById('btnStart-game');
enterButton.onclick=function(){
// Get the zero point value
let zeroPoint = document.getElementById('zero-point').value;
//get the avg value
let avgValue=document.getElementById("avg-point").value;

// Get the difficulty level
let difficultySelect = document.getElementById('mode');
let difficulty = difficultySelect.options[difficultySelect.selectedIndex].value;

// Example usage
console.log('Zero Point:', zeroPoint);
console.log('Mode:', difficulty);
console.log("avg:",avgValue);
if(!sessionStorage.getItem("isLoggedIn")){
    const confirmed = window.confirm('You need to log in first. Press OK to go to the login page.');

    // If the user confirms, redirect to the login page
    if (confirmed) {
        window.location.href = '/test5/login.html'; // Replace '/login' with your login page URL
    }
   
}
    

// Add your logic here based on the retrieved values
let data = {
    zero: zeroPoint,
    mode: difficulty,
    avg: avgValue
};

// Make a POST request to your backend
fetch('http://localhost:8080/game/zeropoint', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
})
.then(response => {
    // Handle the response
    console.log('Response:', response);
    sessionStorage.setItem("zeroPoint",zeroPoint);
    sessionStorage.setItem("avg",avgValue);
    sessionStorage.setItem("mode",difficulty);
   
    if(response.ok){
        // Navigate to a new page
        window.location.href = "/test5/game.html";

    }else{
        const confirmed = window.confirm('Something went wrong! try again, maybe the server is not working?');
    }


})
.catch(error => {
    // Handle errors
    console.error('Error:', error);
});
};

