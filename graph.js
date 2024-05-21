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

    const loadGraphButton = document.getElementById('loadGraph-button');
    loadGraphButton.addEventListener('click', function() {
        fetch('http://localhost:8080/rounds/pressures')
            .then(response => response.json())
            .then(pressureData => {
                console.log("Pressure Data:", pressureData); // Debugging: Log fetched data
                renderChart(pressureData);
            })
            .catch(error => console.error('Error fetching the pressure data:', error));
    });
});

// Render the chart with fetched data
function renderChart(pressureData) {
    // Generate x-axis labels based on the length of the data
    let totalDataPoints = pressureData.length;
    let labels = Array.from({ length: totalDataPoints }, (_, i) => (i + 1) / 10); // Assuming each point represents 0.1 seconds

    console.log("X-axis Labels:", labels); // Debugging: Log generated labels

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
        title: {
            text: 'Pressure Trends by Time (Seconds)',
            align: 'left'
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
