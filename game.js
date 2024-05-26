var backgroundImages = [
    "gameImages/CPRbackground1.png",
    "gameImages/CPRbackground2.png",
    "gameImages/CPRbackground3.png",
    "gameImages/CPRbackground4.png"
];

var toastMessage = "";
var toastDisplayDuration = 100; // Duration in milliseconds
var toastTimestamp = 0;
var currentBackgroundIndex = 0;
var myGamePiece, myBackGround, myButton;
var testNumbers = [], myTrail = [], greenZoneUpper = [], greenZoneLower = [];
var currentNum = 0, currentGreenZone = 0, currentGreenZoneLower = 0;
var gameStarted = false, gameEnded = false, needsReset = true;
var navbarHeight = document.getElementById('navbarTot').offsetHeight;
var canvasHeight = window.innerHeight - navbarHeight;

zeroPoint = 500;
var upperGreenZoneY = 250; 
var lowerGreenZoneY = 430;


function startGame() {
    
    myGamePiece = new component(0, 30, 30, "gameImages/Heart.png", 500, zeroPoint, "image");
    myBackGround = new backgroundComponent(-3, window.innerWidth, canvasHeight, "gameImages/path12.png", 0, 0, "image");

    greenZoneUpper.push(new component(-3, window.innerWidth*3, 100, "rgba(0, 255, 0, 0.7)", 0, upperGreenZoneY, "color  "));
    greenZoneLower.push(new component(-3, window.innerWidth*3, 70, "rgba(0, 255, 0, 0.7)", 0, lowerGreenZoneY, "color"));
    
    myGameArea.start();

}
function resetGame() {
    document.getElementById('gameOverPopup').style.display = 'none';
    myGameArea.stop();

    myTrail = [];
    greenZoneUpper = [];
    greenZoneLower = [];
    testNumbers = [];
    currentNum = 0;
    currentGreenZone = 0;
    currentGreenZoneLower = 0;
    freq = 0;
    upperPoints = 0;
    lowerPoints = 0;
    totPoints = 0;

    startGame();
}

function saveGame() {
    saveRound();
}
function restartGame() {
    resetGame(); 
}

function exitGame() {
    resetGame(); 
    window.location.href = 'menuGame.html'; // Adjust the path as necessary
}

var myGameArea = {
    canvas : document.createElement("canvas"),
    start : function() {
        this.canvas.width = window.innerWidth;
        this.canvas.height = canvasHeight;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.canvas, document.body.childNodes[10]);
        this.frameNo = 0;
        this.interval = setInterval(updateGameArea, 20);
        },
    clear : function() {
        this.context.save();
        this.context.setTransform(1, 0, 0, 1, 0, 0);
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.context.translate(0, -zeroPoint);
        this.context.restore();
    },
    stop : function() {
        clearInterval(this.interval);
    }
}

function backgroundComponent(speed, width, height, src, x, y) {
    this.image = new Image();
    this.image.src = src;
    this.width = width;
    this.height = height;
    this.speedX = speed;
    this.x = x;
    this.y = y;
    this.Image = new Image();
    //this.nextImage.src = backgroundImages[(currentBackgroundIndex + 1) % backgroundImages.length];

    this.update = function () {
        let ctx = myGameArea.context;

        // Draw the current image
        ctx.drawImage(this.image, this.x, this.y, this.width, this.height);

        // Draw the next image adjacent to the current image
        ctx.drawImage(this.image, this.x + this.width, this.y, this.width, this.height);
    }

    this.newPos = function () {
        this.x += this.speedX;

        // Check if the current background has moved enough to bring in the next image
        if (this.x <= -this.width) {
            // Move to the next image
            this.x = 0;
            //currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundImages.length;

            // Swap images
            //this.image.src = this.nextImage.src;
            //this.nextImage.src = backgroundImages[(currentBackgroundIndex + 1) % backgroundImages.length];
        }
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
    this.update = function () {
        let ctx = myGameArea.context;
        if (type == "image") {
            let adjustedY = this.y - this.height / 2;
            ctx.drawImage(this.image, this.x, adjustedY, this.width, this.height);
        } else {
            ctx.fillStyle = color;
            ctx.fillRect(this.x, this.y, this.width, this.height);
        }
    }
    this.newPos = function () {
        this.x += this.speedX;
        this.y += this.speedY;
    }

}


function updateGameArea() {
    var x, y;
    myGameArea.clear();
    
    if (gameStarted) {
        needsReset = false;
        myBackGround.newPos();
        myBackGround.update();
        myGameArea.frameNo += 1;
        drawLine(zeroPoint);
        
        if (currentNum < testNumbers.length) {
            let targetY = zeroPoint - testNumbers[currentNum];  // Calculate the target Y position from testNumbers
            console.log(targetY);
            if (myGamePiece.y < targetY) {
                myGamePiece.speedY = 10;  // Move down if the current Y is less than the target Y
            } else if (myGamePiece.y > targetY) {
                myGamePiece.speedY = -10; // Move up if the current Y is greater than the target Y
            } else {
                myGamePiece.speedY = 0;  // Stop moving if the target Y is reached
                currentNum++;  // Move to the next number in the array
                if (currentNum >= testNumbers.length) {
                    currentNum = 0;  // Reset to start if all positions have been reached
                }
            }

            //myGamePiece.y=targetY;
        }
        
        if (myGameArea.frameNo == 1 || everyinterval(2)) {
            x = myGamePiece.x;
            y = myGamePiece.y;
            myTrail.push(new component(-3, 20, 20, "gameImages/Heart.png", x, y, "image"));
        }

        
        if (myGameArea.frameNo == 1 || everyinterval(500)) {
            currentGreenZone += 1;
        
            
            let lastUpperGreenZone = greenZoneUpper[greenZoneUpper.length - 1];
            let newUpperGreenZoneX = lastUpperGreenZone.x + lastUpperGreenZone.width;
            let newUpperGreenZoneHeight = Math.max(lastUpperGreenZone.height - 15, 15);
            let newUpperGreenZoneY = lastUpperGreenZone.y + (15 / 2);
            let lastLowerGreenZone = greenZoneLower[greenZoneLower.length - 1];
            let newLowerGreenZoneX = lastLowerGreenZone.x + lastLowerGreenZone.width;
            let newLowerGreenZoneHeight = Math.max(lastLowerGreenZone.height - 10, 10);
            
            greenZoneUpper.push(new component(-3, window.innerWidth*3, newUpperGreenZoneHeight, "rgba(0, 255, 0, 0.7)", newUpperGreenZoneX, newUpperGreenZoneY, "zone"));
            greenZoneLower.push(new component(-3, window.innerWidth*3, newLowerGreenZoneHeight, "rgba(0, 255, 0, 0.7)", newLowerGreenZoneX, lowerGreenZoneY + (70 - newLowerGreenZoneHeight), "zone"));
        }
        
        greenZoneUpper = greenZoneUpper.filter(zone => zone.x + zone.width > 0);
        greenZoneLower = greenZoneLower.filter(zone => zone.x + zone.width > 0);
        myTrail = myTrail.filter(trail => trail.x + trail.width > 0);

        // Update remaining elements
        greenZoneUpper.forEach(zone => {
            zone.newPos();
            zone.update();
        });
        greenZoneLower.forEach(zone => {
            zone.newPos();
            zone.update();
        });
        myTrail.forEach(trail => {
            trail.newPos();
            trail.update();
        });
       


    } else{
        myBackGround.update();
        drawLine(zeroPoint);
        greenZoneUpper.forEach(zone => zone.update());
        greenZoneLower.forEach(zone => zone.update());
        myTrail.forEach(trail => trail.update());
        
        
    }
    
    myGamePiece.newPos();
    myGamePiece.update();
    showTotPoints();
    showFrequency();
    drawText(`Y: ${zeroPoint - myGamePiece.y}`, 10, 20, 'blue', '14px');
    drawToast();
    

    if( gameEnded && !needsReset){
        console.log("hit kommer vi");
        showGameOverPopup();
    
        needsReset = true;
        myGamePiece.speedY = 0;
        

    }  
}

function everyinterval(n) {
    return (myGameArea.frameNo / n) % 1 === 0;
}

function drawLine(yPosition) {
    let ctx = myGameArea.context;
    ctx.beginPath();
    ctx.moveTo(0, yPosition); 
    ctx.lineTo(ctx.canvas.width, yPosition);
    ctx.strokeStyle = 'rgba(0, 0, 0, 0.7)'; 
    ctx.lineWidth = 2; 
    ctx.stroke(); 
}

function drawText(text, x, y, color = 'black', fontSize = '16px', font = 'Arial') {
    var ctx = myGameArea.context;
    ctx.save();
    ctx.font = `${fontSize} ${font}`;
    ctx.fillStyle = color;
    ctx.fillText(text, x, y);
    ctx.restore();
}

function showFrequency(){
    drawText("BPM: "+freq,window.innerWidth-150, 20, color="black",fontSize = '20px', font = 'Arial');
    //drawText(freq,100,80,color="black",fontSize = '20px', font = 'Arial')
}
function showTotPoints(){
    drawText("Total Points: "+totPoints, window.innerWidth-350  , 20,color="black",fontSize = '20px', font = 'Arial');
}
function showToast(message) {
    toastMessage = message;
    toastTimestamp = Date.now();
    
}

function drawToast() {
    if (toastMessage && Date.now() - toastTimestamp < toastDisplayDuration) {
        var ctx = myGameArea.context;
        ctx.save();
        ctx.font = '20px Arial';
        
        ctx.fillStyle = 'red';
        ctx.fillText(toastMessage, window.innerWidth / 2 - 90, 80);
        ctx.restore();
    } else {
        toastMessage = "";
    }
}

