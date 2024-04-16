
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
   //beror på ifall användaren vill logga in eller signa upp
    if(signUpFlag){
        console.log(username.value);
        console.log(email.value);
        console.log(password.value)
        //sessionStorage.setItem("email",email.value);
        //sessionStorage.setItem("isLoggedIn",true);
        //här ska vi skicka data för att skapa ett nytt konto.

        const userData = {
            username: username.value,
            email: email.value,
            password: password.value
        };
    
        fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
        .then(response => {
            console.log(response);
            
            if (response.ok) {
                // Registration successful, handle accordingly
                //console.log(response.statusText);
                //console.log('User registered successfully');
                sessionStorage.setItem("email", email.value);
                sessionStorage.setItem("isLoggedIn", true);
                // Redirect or perform any other actions as needed
            } else {
                // Registration failed, handle accordingly
                console.error('Failed to register user:', response.statusText);
                // Display error message or take appropriate actions
            }
            
            return response.json(); // Parse the response body as JSON
        })
        .then(data => {
            console.log(data); // Log the response data to the console
            // Handle the response data as needed
        })
        .catch(error => {
            console.error('Error registering user:', error);
            // Handle other errors such as network issues
        });

    }else{
        /*        
        console.log(email.value);
        console.log(password.value)
        let emailameToCheck=email.value;
        let url = `http://localhost:8080/userByEmail?email=${emailameToCheck}`;
        fetch(url)
        .then(response => {
            // This function is called when the response is received
            // You can access the response data here
            return response.json(); // Parse the response body as JSON
        })
        .then(data => {
            // This function is called with the parsed JSON data
            console.log(data.userName); // Log the data to the console
            // You can now work with the data as needed
            if(data.email==email.value){
                storeLoggedIn();
            }
            //stod data här
        })  .catch(error => {
            // This function is called if an error occurs during the fetch
            console.error('Error fetching data:', error);
        });
            
        */
        console.log(email.value);
        console.log(password.value);
        let emailToCheck = email.value;
        let passwordToCheck = password.value;
        let url = `http://localhost:8080/login`;
    
        // Constructing the request body
        let requestBody = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: emailToCheck, password: passwordToCheck })
        };
    
        // Making the fetch request
        fetch(url, requestBody)
            .then(response => {
                return response.json(); // Parse the response body as JSON
            })
            .then(data => {
                console.log(data); // Log the response data to the console
                if (data.success) {
                    storeLoggedIn();
                }
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }
    if(sessionStorage.getItem("isLoggedIn")=="true"){
        console.log(sessionStorage.getItem("isLoggedIn"))
        //signedIn.innerHTML="signed in";
        navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>"
    }

}

//sparar till seccion datat att man är inloggat och med vilken mail
function storeLoggedIn(){
    if(email.value=="calle361@gmail.com"&&password.value=="1234"){
        console.log("du är inloggad!");
        sessionStorage.setItem("email",email.value);
        sessionStorage.setItem("isLoggedIn",true);
    }
}

//visar för sidan att man redan e inloggad
if(sessionStorage.getItem("isLoggedIn")=="true"){
    console.log(sessionStorage.getItem("isLoggedIn"))
    //signedIn.innerHTML="signed in";
    navbarBtn.innerHTML = "<a href='/test5/login.html' class='button'>signed in</a>"
}