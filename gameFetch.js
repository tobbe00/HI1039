let oldId=10;

let pointsId=10
let freq=0;
let upperPoints=0;
let lowerPoints=0;
let totPoints=0;
function fetchFromApi(url) {
    return fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Network response was not ok for ${url}`);
        }
        return response.json();
      })
      .catch(error => {
        console.error('Fetch error:', error);
      });
  }
  
  function fetchData() {
    fetchFromApi('http://localhost:8080/game/extreme').then(data => {
      if (data && oldId !== data.id) {
        console.log(data);
        testNumbers.push(data.pressure);
        oldId = data.id;
        currentNum = data.id;
        if (currentNum >= testNumbers.length) {
          currentNum = testNumbers.length - 1;
        }
      }
    });
  
    fetchFromApi('http://localhost:8080/game/points').then(data => {
      if (data && pointsId !== data.id) {
        console.log(data);
        freq = data.frequency;
        upperPoints = data.pointsMax;
        lowerPoints = data.pointsMin;
        totPoints += upperPoints + lowerPoints;
        pointsId = data.id;
      }
    });
  
    fetchFromApi('http://localhost:8080/game/gamestart').then(data => {
      gameStarted = !!data;
    });
  
    fetchFromApi('http://localhost:8080/game/gameEnd').then(data => {
      gameEnded = !!data;
    });
  }
  // Fetch data every 0.1 seconds 1000=1s
  setInterval(fetchData, 100);
  


function saveRound(){
    // Define the data to send
    const data = {
       points: totPoints, 
       email: sessionStorage.getItem("email") 
   };
   fetch('http://localhost:8080/rounds/saveRound', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
})
.then(response => {
    if (!response.ok) {
    throw new Error('Network response was not ok');
    }
    return response.json();
})
.then(data => {
    console.log('Response:', data);
})
.catch(error => {
    console.error('Error:', error);
});

}


//freq=data.frequency;
                //upperPoints=data.pointsMax;//ska flyttas
                //lowerPoints=data.pointsMin;//ska flyttas
                //totPoints += upperPoints+lowerPoints//totpoints ska flyttas
                /*
                if(data.maxBeforeMin){
                    testNumbers.push(data.maxPressure);
                    testNumbers.push(data.minPressure);
                }else{
                    testNumbers.push(data.minPressure);
                    testNumbers.push(data.maxPressure);
                }
                */