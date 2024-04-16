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

// Specify the id you want to fetch
let userId = 2;

// Construct the URL with the id
let url = `http://localhost:8080/user?id=${userId}`;

// Fetch data from the URL
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

    //stod data här
  })
  .catch(error => {
    // This function is called if an error occurs during the fetch
    console.error('Error fetching data:', error);
  });
  




  /*
let httpRequest=new XMLHttpRequest();
httpRequest.open("GET", "http://localhost:8080/user?id=1");
httpRequest.send();
httpRequest.onload=console.log(httpRequest);
*/


