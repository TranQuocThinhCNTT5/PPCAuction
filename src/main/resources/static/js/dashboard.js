(function(){
  if(!window.Chart||!window.dashboardCharts)return;
  const green='#28a879',blue='#3477b8';
  function chart(id,type,data,color){const canvas=document.getElementById(id);if(!canvas)return;new Chart(canvas,{type,data:{labels:data.labels,datasets:[{label:'',data:data.values,backgroundColor:color+'33',borderColor:color,borderWidth:2,tension:.3,fill:type==='line'}]},options:{responsive:true,maintainAspectRatio:false,plugins:{legend:{display:false}},scales:type==='doughnut'?{}:{y:{beginAtZero:true}}}});}
  chart('costChart','line',window.dashboardCharts.cost,green);chart('clickChart','bar',window.dashboardCharts.clicks,blue);chart('impressionChart','line',window.dashboardCharts.impressions,'#8a70d6');chart('positionChart','doughnut',window.dashboardCharts.positions,green);chart('rankChart','bar',window.dashboardCharts.rank,blue);
})();
