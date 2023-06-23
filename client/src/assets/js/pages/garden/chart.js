'use strict';

let myChart;

async function lineChartInGarden() {
    const chartDataFromServer = await scansByPlantId(parseInt(localStorage.getItem("selectedOwnedPlantId")));

    let xChartRealData = [];
    let yChartRealData = [];

    chartDataFromServer.forEach(scan => {
        yChartRealData.push(scan.expectedYield);
        const date = new Date(scan.scanTime).toLocaleDateString("be-NL");
        const time = new Date(scan.scanTime).toLocaleTimeString("be-NL");
        xChartRealData.push(date + " " + time);
    });

    if (myChart !== undefined) {
        myChart.destroy();

    }

    const data = {
        labels: xChartRealData,
        datasets: [{
            label: 'Yield',
            fill: true,
            data: yChartRealData,
            borderColor: 'rgb(102, 255, 102)',
            tension: 0.1
        }]
    };
    const config = {
        type: 'line',
        options: {
            animation: true,
        },
        data: data,
    };

    myChart = new Chart(
        document.querySelector('#chart'), config
    );
}

