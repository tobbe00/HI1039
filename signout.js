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
    navbarBtn.innerHTML = "<a href='/HI1039/login.html' class='button'>signed in</a>"
}
//page specific-----------------------------------------------------------------------------------
if(sessionStorage.getItem("isLoggedIn")){
    var userEmail = sessionStorage.getItem("email");
    console.log(userEmail);

    document.getElementById("user-email").textContent = userEmail;
    let signOutButton=document.getElementById("sign-out-button");
    signOutButton.onclick=function(){
        signOut();
        popUpHome();
    }
    
    
}else{
    popUp();
}


function popUp(){
    
    const confirmed = window.confirm('You need to login before you shuld be able to acess this page');
    if (confirmed) {
        location.reload();
    }
}
function signOut(){
    // Remove email and isLoggedIn from sessionStorage
    sessionStorage.removeItem("email");
    sessionStorage.removeItem("isLoggedIn");

}
function popUpHome(){
    
    const confirmed = window.confirm('You are signed out. go to home page?');
    if (confirmed) {
        window.location.href="/HI1039/index.html"
    }
}