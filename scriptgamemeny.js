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
    navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>"
}
//page specific-----------------------------------------------------------------------------------------------------






// Add event listener to the button
let enterButton = document.getElementById('btnStart-game');
enterButton.onclick=function(){
// Get the zero point value
let zeroPoint = document.getElementById('zero-point').value;

// Get the difficulty level
let difficultySelect = document.getElementById('mode');
let difficulty = difficultySelect.options[difficultySelect.selectedIndex].value;

// Example usage
console.log('Zero Point:', zeroPoint);
console.log('Mode:', difficulty);

// Add your logic here based on the retrieved values
let data = {
    zero: zeroPoint,
    mode: difficulty
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
})
.catch(error => {
    // Handle errors
    console.error('Error:', error);
});
};

