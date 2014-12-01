
'use strict';

var fs = require('fs');
if (!process.argv[2]){
	console.log("Usage: 	node 	routeGen.js 	numberOfNodes");
	process.exit(-1);
}
var times = parseInt(process.argv[2]);

var stream = fs.createWriteStream('routes.txt','w+');
for (var i = 0; i < times; i++){
	stream.write((+i+1)+"|"+(+7000+i+1)+"| x ");
	for (var j = 0; j< i;j++)
		stream.write('|localhost:'+(+7000+j+1));
	stream.write('\n');
}
stream.end();