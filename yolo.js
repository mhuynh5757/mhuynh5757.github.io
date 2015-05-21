var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

function createRow()
{
	var row = document.createElement('tr');
	document.body.appendChild(row);
}

function createData()
{
	var data = document.createElement('td');
	document.body.appendChild('td');
	var canvas = document.createElement('canvas');
	canvas.height = 100%;
	canvas.width = 100%;
	
}