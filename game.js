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

var myGamePiece;
var myBackGround;
var myTrail = [];
var testNumbers = [];
var currentNum = 0;


function startGame() {
    myGamePiece = new component(0, 16, 16, "gameImages/smiley.gif", 380, 300, "image");
    myBackGround = new component(-1447/6000, 1500,400, "gameImages/graphbackground2.png", 0,0, "image");
    myGameArea.start();
}

var myGameArea = {
    canvas : document.createElement("canvas"),
    start : function() {
        this.canvas.width = 760;
        this.canvas.height = 400;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.canvas, document.body.childNodes[10]);
        this.frameNo = 0;
        this.interval = setInterval(updateGameArea, 20);
        },
    clear : function() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    },
    stop : function() {
        clearInterval(this.interval);
    }
}

function component(speed, width, height, color, x, y, type) {
    this.type = type;
    if (type == "image") {
        this.image = new Image();
        this.image.src = color;
    }
    this.width = width;
    this.height = height;
    this.speedX = speed;
    this.speedY = 0;    
    this.x = x;
    this.y = y;    
    this.update = function() {
        ctx = myGameArea.context;
        if (type == "image") {
            ctx.drawImage(this.image, 
                this.x, 
                this.y,
                this.width, this.height);
        } else {
            ctx.fillStyle = color;
            ctx.fillRect(this.x, this.y, this.width, this.height);
        }
    }
    this.newPos = function() {
        this.x += this.speedX;
        this.y += this.speedY;        
    }
}



function moveup() {
    myGamePiece.speedY -= 1/2;
}

function movedown() {
    myGamePiece.speedY += 1/2; 
}


function updateGameArea() {
    var x, y;
    myGameArea.clear();
    myBackGround.update();
    myBackGround.newPos();
    
    myGameArea.frameNo += 1;
    if (myGameArea.frameNo == 1 || everyinterval(1)) {
        x = myGamePiece.x;
        y = myGamePiece.y;
        myTrail.push(new component(-1447/6000, 10, 10, "gameImages/redTrail.png", x, y, "image"));
    }
    for (i = 0; i < myTrail.length; i += 1) {
        myTrail[i].newPos();
        myTrail[i].update();
    }

    if(myGamePiece.y > testNumbers[currentNum]){
        if(myGamePiece.speedY > 0)
        {
            currentNum += 1;
        }
        myGamePiece.speedY = -6;
    }
    if(myGamePiece.y < testNumbers[currentNum]){
        if(myGamePiece.speedY < 0)
        {
            currentNum += 1;
        }
        myGamePiece.speedY = 6;
    }
    if(currentNum >= testNumbers.length){
        currentNum = 0;
    }

    myGamePiece.newPos();
    myGamePiece.update();   
}

function everyinterval(n) {
    if ((myGameArea.frameNo / n) % 1 == 0) {return true;}
    return false;
}
//about reeding from api!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

let oldId=10;
  function fetchData() {
    fetch('http://localhost:8080/game/extreme')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Process the data
            
            if(oldId != data.id){
                console.log(data);

                if(data.maxBeforeMin){
                    testNumbers.push(data.maxPressure/10);
                    testNumbers.push(data.minPressure/10);
                }else{
                    testNumbers.push(data.minPressure/10);
                    testNumbers.push(data.maxPressure/10);
                }
            }
            oldId=data.id;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

// Fetch data every 0.1 seconds 1000=1s
setInterval(fetchData, 100);

