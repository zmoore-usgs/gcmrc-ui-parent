describe("Matrix Uncertainty", function() {
	it("does what DT wants", function() {
		var expectedDatas = MatrixUncertaintyResources.results.fixedToDT;
		
		var matrixWorker = new MatrixWorker();
		matrixWorker.setDataArray(MatrixUncertaintyResources.config.setDataArray.unadjusted);
		
		var actual = matrixWorker.transformArray(MatrixUncertaintyResources.config.transformArray.unadjusted).dataArray;
		
		expect(actual).toEqual(expectedDatas);
	});
});