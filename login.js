
const menu=document.querySelector('#mobile-menu');
const menuLinks=document.querySelector('.navbar__menu');

menu.addEventListener('click',function(){
    menu.classList.toggle('is-active');
    menuLinks.classList.toggle('active');
})




let signupBtn=document.getElementById("signupBtn");
let signinBtn=document.getElementById("signinBtn");
let nameField=document.getElementById("nameField");
let title=document.getElementById("title");
let enterBtn=document.getElementById("enterBtn");

let signUpFlag=true;
let username=document.getElementById("tbUsername");
let password=document.getElementById("tbPassword");
let email=document.getElementById("tbEmail");

signinBtn.onclick=function(){
    nameField.style.maxHeight="0";
    title.innerHTML= "Sign In";
    signupBtn.classList.add("disable");
    signinBtn.classList.remove("disable");
    signUpFlag=false;
}

signupBtn.onclick=function(){
    nameField.style.maxHeight="60px";
    title.innerHTML= "Sign Up";
    signupBtn.classList.remove("disable");
    signinBtn.classList.add("disable");
    signUpFlag=true;
}
let signedIn=document.getElementById("signupBtn2");
let navbarBtn = document.querySelector('.navbar__btn');
enterBtn.onclick=function(){
   
    if(signUpFlag){
        console.log(username.value);
        console.log(email.value);
        console.log(password.value)
        sessionStorage.setItem("email",email.value);
        sessionStorage.setItem("isLoggedIn",true);

    }else{
        console.log(email.value);
        console.log(password.value)
        storeLoggedIn();
        
    }
    if(sessionStorage.getItem("isLoggedIn")=="true"){
        console.log(sessionStorage.getItem("isLoggedIn"))
        //signedIn.innerHTML="signed in";
        navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>"
    }

}

function storeLoggedIn(){
    if(email.value=="calle361@gmail.com"&&password.value=="1234"){
        console.log("du är inloggad!");
        sessionStorage.setItem("email",email.value);
        sessionStorage.setItem("isLoggedIn",true);
    }
}


if(sessionStorage.getItem("isLoggedIn")=="true"){
    console.log(sessionStorage.getItem("isLoggedIn"))
    //signedIn.innerHTML="signed in";
    navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>"
}