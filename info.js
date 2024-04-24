const menu=document.querySelector('#mobile-menu');
const menuLinks=document.querySelector('.navbar__menu');



menu.addEventListener('click',function(){
    menu.classList.toggle('is-active');
    menuLinks.classList.toggle('active');
})

let signedIn=document.getElementById("signupBtn2");
let navbarBtn = document.querySelector('.navbar__btn');
if(sessionStorage.getItem("isLoggedIn")=="true"){
    console.log(sessionStorage.getItem("isLoggedIn"));
    //signedIn.innerHTML="signed in";
    navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>";
}

//page specific




