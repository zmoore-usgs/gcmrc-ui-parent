
describe("Matrix Worker", function() {

	it("can make a matrix", function() {
		var actual = new goog.math.Matrix([
			[1, 1, 1],
			[1, 1, 1],
			[1, 1, 1],
			[-1, -1, -1]
		]);

		expect(actual).toBeTruthy();
	});

	it("can build a MatrixWorker", function() {
		var expected = new goog.math.Matrix([
			[1, 1, 1],
			[1, 1, 1],
			[1, 1, 1],
			[-1, -1, -1]
		]);

		var actual = new MatrixWorker().xformMatrices.sign;

		expect(actual).toEqual(expected);
	});
	
	it("likes what it sees", function() {
		var expectedDivId = "data-dygraph";
		var expectedDatas = MatrixBuildResources.datas;
		var expectedTimes = MatrixBuildResources.times;
		
		var matrixWorker = new MatrixWorker();
		matrixWorker.setDataArray(MatrixBuildResources.config.setDataArray.unadjusted);
		
		expect(matrixWorker.dataArrays[expectedDivId]).toEqual(expectedDatas);
		expect(matrixWorker.times[expectedDivId]).toEqual(expectedTimes);
	});
	
	it("computes stuff", function() {
		var expectedDatas = MatrixBuildResources.results.unadjusted;
		
		var matrixWorker = new MatrixWorker();
		matrixWorker.setDataArray(MatrixBuildResources.config.setDataArray.unadjusted);
		
		var actual = matrixWorker.transformArray(MatrixBuildResources.config.transformArray.unadjusted).dataArray;
		
		expect(actual).toEqual(expectedDatas);
	});
	
	describe("Uncertainty adjustment", function() {
		///TODO!!!!!! Fix this!
		it("doubles uncertainty after endStaticRec", function() {
			var expectedDatas = MatrixBuildResources.results.double;
			
			var matrixWorker = new MatrixWorker();
			matrixWorker.setDataArray(MatrixBuildResources.config.setDataArray.double);

			var actual = matrixWorker.transformArray(MatrixBuildResources.config.transformArray.double).dataArray;

			expect(actual).toEqual(expectedDatas);
		});
		
		it("quadruples uncertainty after newestSuspSed", function() {
			var expectedDatas = MatrixBuildResources.results.quadruple;

			var matrixWorker = new MatrixWorker();
			matrixWorker.setDataArray(MatrixBuildResources.config.setDataArray.quadruple);

			var actual = matrixWorker.transformArray(MatrixBuildResources.config.transformArray.quadruple).dataArray;

			expect(actual).toEqual(expectedDatas);
		});
		
		it("does both well", function() {
			var expectedDatas = MatrixBuildResources.results.both;
			
			var matrixWorker = new MatrixWorker();
			matrixWorker.setDataArray(MatrixBuildResources.config.setDataArray.both);

			var actual = matrixWorker.transformArray(MatrixBuildResources.config.transformArray.both).dataArray;

			expect(actual).toEqual(expectedDatas);
		});
		
	});
});