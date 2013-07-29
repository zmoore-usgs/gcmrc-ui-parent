describe('JSL Resource Load', function() {
	describe('error results', function() {
		var errorResults = {
			error : {
				"@rowCount" : 2,
				data : [
					{
						message : "I forget what this looks like"
					},
					{
						message : "like seriously, don't trust this test"
					}
				]
			}
		};
		
		it("doesn't call forEachCallback", function() {
			var shouldBeCalled = false;
			var wasCalled = false;
			JSL.ResourceLoad(function(el) {
				wasCalled = true;
			})(Object.clone(errorResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
		
		it("doesn't call afterAllCallback", function() {
			var shouldBeCalled = false;
			var wasCalled = false;
			JSL.ResourceLoad(null, function(el) {
				wasCalled = true;
			})(Object.clone(errorResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
	});
	describe('empty results', function() {
		var emptyResults = {
			success : {
				"@rowCount" : 0,
				data : null
			}
		};
		
		it("doesn't call forEachCallback", function() {
			var shouldBeCalled = false;
			var wasCalled = false;
			JSL.ResourceLoad(function(el) {
				wasCalled = true;
			})(Object.clone(emptyResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
		
		it("calls afterAllCallback", function() {
			var shouldBeCalled = true;
			var wasCalled = false;
			JSL.ResourceLoad(null, function(data) {
				wasCalled = true;
			})(Object.clone(emptyResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
	});
	describe('one result', function() {
		var oneResult = {
			success : {
				"@rowCount" : -1,
				data : {
					"shortName" : "GCMRC-RG1",
					"displayName" : "Rio Grande above Castolon, TX"
				}
			}
		};
		
		it("calls forEachCallback once", function() {
			var shouldBeCalled = 1;
			var wasCalled = 0;
			JSL.ResourceLoad(function(el) {
				wasCalled++;
			})(Object.clone(oneResult, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
		
		it("calls afterAllCallback with a single length array", function() {
			var shouldBeCalled = true;
			var wasCalled = false;
			JSL.ResourceLoad(null, function(data) {
				wasCalled = true;
			})(Object.clone(oneResult, true));
			expect(wasCalled).toBe(shouldBeCalled);
			
			JSL.ResourceLoad(null, function(data) {
				expect(data).toEqual([oneResult.success.data]);
			})(Object.clone(oneResult, true));
		});
	});
	describe('multiple results', function() {
		var multipleResults = {
			success : {
				"@rowCount" : -1,
				data : [
					{
						"shortName" : "GCMRC-RG1",
						"displayName" : "Rio Grande above Castolon, TX"
					}, {
						"nwisSite" : "08374550",
						"shortName" : "Rio Grande Castolon",
						"displayName" : "Rio Grande near Castolon, TX"
					}, {
						"shortName" : "GCMRC-RG2",
						"displayName" : "Rio Grande above Rio Grande Village, TX"
					},
				]
			}
		};
		
		it("calls forEachCallback once per result", function() {
			var shouldBeCalled = multipleResults.success.data.length;
			var wasCalled = 0;
			JSL.ResourceLoad(function(el) {
				wasCalled++;
			})(Object.clone(multipleResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
		});
		
		it("calls afterAllCallback with all the data", function() {
			var shouldBeCalled = true;
			var wasCalled = false;
			JSL.ResourceLoad(null, function(data) {
				wasCalled = true;
			})(Object.clone(multipleResults, true));
			expect(wasCalled).toBe(shouldBeCalled);
			
			JSL.ResourceLoad(null, function(data) {
				expect(data).toEqual(multipleResults.success.data);
			})(Object.clone(multipleResults, true));
		});
	});
});