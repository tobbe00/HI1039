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
    navbarBtn.innerHTML = "<a href='/signOut.html' class='button'>sign out</a>"
}

//page specific

var myArray = []
let searchButton=document.getElementById("searchButton");
let username = sessionStorage.getItem("username");
console.log(sessionStorage.getItem("username"))


//https://reqres.in/api/users
$.ajax({
    method: 'GET',
    url: 'http://localhost:8080/rounds/leaderboard',
    success: function(response) {
        console.log("denna aropar jag");
        myArray = response;
        buildTable(myArray,'myTable');
    }
});

// AJAX call for recentArray
$.ajax({
    method: 'GET',
    url: `http://localhost:8080/rounds/getUsersRounds?username=${username}`,
    success: function(response) {
        console.log("denna anropar jag")
        usersRoundsArray = response;
        console.log(usersRoundsArray);
        buildTable(usersRoundsArray, 'usersRoundTable');
    }
});

//https://reqres.in/api/users
$.ajax({
    method:'GET',
    url:'http://localhost:8080/rounds/pressures',
    success:function(response){
        console.log('Pressures data:', response);
        
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
    //console.log(filteredData);
    return filteredData
}



$('#search-input').on('keyup', function(){
    var value=$(this).val()
    //console.log('value',value)
    var data=searchTable(value,myArray)
    buildTable(data)
})





function buildTable(data,tableId) {
    var table = document.getElementById(tableId);
    table.innerHTML = '';
    console.log("test");
    for (var i = 0; i < data.length; i++) {
        var row = document.createElement('tr');
        if(tableId=='myTable'){
            row.innerHTML = `
            <td>${data[i].roundId}</td>
            <td>${data[i].username}</td>
            <td>${data[i].rank}</td>
            <td>${data[i].points}</td>
        `;
        }else if(tableId==`usersRoundTable`){
            
            row.innerHTML = `
            <td>${data[i].roundId}</td>
            <td>${data[i].username}</td>
            <td>${data[i].points}</td>
            `;
        }
        
        
        row.addEventListener('click', function() {
            var roundId = this.cells[0].innerText;
            window.location.href = `graph.html?roundId=${roundId}`;
        });
        
        table.appendChild(row);
    }
}



searchButton.onclick= async function(){
    let roundId = document.getElementById('searchInput').value
    console.log("Round input: "+ roundId);

    try{
        let roundExists = await checkIfRoundExists(roundId);
        console.log('Round exists:', roundExists);
        if(roundExists){
            window.location.href = `graph.html?roundId=${roundId}`;
        }else{
            alert("Please enter a valid Round ID")
        }
    }catch(error ){
        console.log(error);
    }
    
   
}

 function checkIfRoundExists(roundId) {
    return fetch(`http://localhost:8080/rounds/getRoundById?roundId=${roundId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}





//<td>${data[i].last_name}</td>    <td>${data[i].first_name}</td> <td>${data[i].email}</td>



