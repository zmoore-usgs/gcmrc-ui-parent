importScripts("../../js/closure-library/goog/base.js", 
"../../js/closure-library/goog/debug/error.js",
"../../js/closure-library/goog/string/string.js",
"../../js/closure-library/goog/asserts/asserts.js",
"../../js/closure-library/goog/array/array.js",
"../../js/closure-library/goog/math/math.js",
"../../js/closure-library/goog/math/size.js",
"../../js/closure-library/goog/math/matrix.js"
);

importScripts("../../js/sugar/sugar-1.3.9.min.js");
importScripts("../../js/js-utils/binary-search.min.js");

importScripts("../../app/graphing/MatrixWorker.js");

var matrixStuff = new MatrixWorker(MatrixWorker.SAND);

self.addEventListener("message", function(msg) {
	var config = msg.data;
	
	if ("transformArray" === config.messageType) {
		var result = matrixStuff.transformArray(config);
		self.postMessage(result);
	} else if ("setDataArray" === config.messageType) {
		matrixStuff.setDataArray(config);
	} else if ("addBedloadToDataArray" === config.messageType) {
		matrixStuff.addBedloadToDataArray(config);
	} else {
		//damn
	}
});