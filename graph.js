// When the document is ready, set up the event handlers
document.addEventListener('DOMContentLoaded', function () {
    const menu = document.querySelector('#mobile-menu');
    const menuLinks = document.querySelector('.navbar__menu');

    // Toggle mobile menu visibility
    menu.addEventListener('click', function() {
        menu.classList.toggle('is-active');
        menuLinks.classList.toggle('active');
    });

    let navbarBtn = document.querySelector('.navbar__btn');
    if (sessionStorage.getItem("isLoggedIn") == "true") {
        console.log(sessionStorage.getItem("isLoggedIn"));
        navbarBtn.innerHTML = "<a href='/signOut.html' class='button'>Sign out</a>";
    }

    const urlParams = new URLSearchParams(window.location.search);
    const roundId = urlParams.get('roundId');

    if (roundId) {
        fetchPressureData(roundId);
        fetchFrequencyData(roundId);
    } else {
        console.log('Round ID not found in url');
    }


});

function fetchPressureData(roundId) {
    // Fetch data for the specified roundId and render the graph
    fetch(`http://localhost:8080/rounds/pressures/${roundId}`)
        .then(response => response.json())
        .then(pressureData => {
            console.log(`Pressure Data for Round ${roundId}:`, pressureData);
            renderChart(pressureData);
            displayAveragePressure(pressureData);
        })
        .catch(error => console.error(`Error fetching pressure data for Round ${roundId}:`, error));
}
function fetchFrequencyData(roundId) {
    fetch(`http://localhost:8080/rounds/frequencies/${roundId}`)
        .then(response => response.json())
        .then(frequencyData => {
            console.log(`Frequency Data for Round ${roundId}:`, frequencyData);
            displayAverageFrequency(frequencyData);
        })
        .catch(error => console.error(`Error fetching frequency data for Round ${roundId}:`, error));
}

// Render the chart with fetched data
function renderChart(pressureData) {
    // Generate x-axis labels based on the length of the data
    let totalDataPoints = pressureData.length;
    let labels = Array.from({ length: totalDataPoints }, (_, i) => (i + 1) / 10); // Assuming each point represents 0.1 seconds


    var options = {
        series: [{
            name: "Pressure",
            data: pressureData
        }],
        chart: {
            height: 350,
            type: 'line',
            zoom: {
                enabled: true,
                type: 'x',
                autoScaleYaxis: true
            },
            toolbar: {
                autoSelected: 'zoom'
            }
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'straight'
        },
        grid: {
            row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            }
        },
        xaxis: {
            type: 'numeric',
            labels: {
                show: true,
                rotate: -45,
                formatter: function(val, index) {
                    return index % 10 === 0 ? val.toFixed(1) : ''; // Show label for every 10th tick
                }
            }
        }
    };

    var chart = new ApexCharts(document.querySelector("#chart"), options);
    chart.render();
}

function displayAveragePressure(pressureData) {
    if (pressureData.length > 0) {
        const totalPressure = pressureData.reduce((acc, val) => acc + val, 0);
        const averagePressure = Math.round(totalPressure / pressureData.length); // Round to the nearest whole number
        document.getElementById('average-pressure').innerText = `Average Pressure: ${averagePressure}`;
    } else {
        document.getElementById('average-pressure').innerText = 'Average Pressure: N/A';
    }
}

function displayAverageFrequency(frequencyData) {
    if(frequencyData.length > 0) {
        const totalFrequency = frequencyData.reduce((acc, val) => acc + val, 0);
        const averageFrequency = Math.round(totalFrequency / frequencyData.length);
        document.getElementById('average-frequency').innerText = `Average BPM: ${averageFrequency}`;
    } else {
        document.getElementById('average-frequency').innerText = 'Average BPM: N/A';
    }
}