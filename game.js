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
//page specific!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

/*
var myArray = [
    
    {'name':'Michael', 'age':'30', 'birthdate':'11/10/1989'},
    {'name':'Mila', 'age':'32', 'birthdate':'10/1/1989'},
    {'name':'Paul', 'age':'29', 'birthdate':'10/14/1990'},
    {'name':'Dennis', 'age':'25', 'birthdate':'11/29/1993'},
    {'name':'Tim', 'age':'27', 'birthdate':'3/12/1991'},
    {'name':'Erik', 'age':'24', 'birthdate':'10/31/1995'},
]
*/
var myArray = []


//https://reqres.in/api/users
$.ajax({
    method:'GET',
    url:'http://localhost:8080/rounds',
    success:function(response){
        myArray=response
        console.log(myArray);
        buildTable(myArray)
        console.log(response);
        
    }
})


function searchTable(value, data ){
    var filteredData=[]
    for(var i=0;i<data.length;i++){
        value=value.toLowerCase()
        //var name=data[i].name.toLowerCase()
        var name = (data[i].username || '').toLowerCase();
        if(name.includes(value)){
            filteredData.push(data[i])
        }
    }

    return filteredData
}



$('#search-input').on('keyup', function(){
    var value=$(this).val()
    console.log('value',value)
    var data=searchTable(value,myArray)
    buildTable(data)
})





function buildTable(data){
    var table = document.getElementById('myTable')

    for (var i = 0; i < data.length; i++){
        var row = `<tr>
                        <td>${data[i].username}</td>
                        <td>${i+1}</td>
                        <td>${data[i].points}</td>
                  </tr>`
        table.innerHTML += row


    }
}


//<td>${data[i].last_name}</td>    <td>${data[i].first_name}</td> <td>${data[i].email}</td>