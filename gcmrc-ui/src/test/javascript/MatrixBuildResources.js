var MatrixBuildResources = {};

MatrixBuildResources.datas = [
	[0, 0, 0, 0],
	[0, 0.1715097, 0.01715097, 3.024426],
	[0, 0.25726451, 0.025726451, 4.53663831],
	[0, 0.4335484, 0.04335484, 7.347232],
	[0, 0.52169032, 0.052169032, 8.75252929],
	[0, 0.6932, 0.06932, 11.94103],
	[0, 0.77895484, 0.077895484, 13.53527526],
	[0, 0.9504645, 0.09504645, 17.22992],
	[0, 1.03621935, 0.103621935, 19.0772446]
];

MatrixBuildResources.times = [
	1336201200000,
	1336201800000,
	1336202100000,
	1336202700000,
	1336203000000,
	1336203600000,
	1336203900000,
	1336204500000,
	1336204800000
];

MatrixBuildResources.bedloadCoeffDatas = [
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1],
	[1, 1, 1, 1]
];

MatrixBuildResources.addBedloadDatas = [
	[1, 1, 1, 1],
	[1, 1.1715097, 1.01715097, 4.024426],
	[1, 1.25726451, 1.025726451, 5.53663831],
	[1, 1.4335484, 1.04335484, 8.347232],
	[1, 1.52169032, 1.052169032, 9.75252929],
	[1, 1.6932, 1.06932, 12.94103],
	[1, 1.77895484, 1.077895484, 14.53527526],
	[1, 1.9504645, 1.09504645, 18.22992],
	[1, 2.03621935, 1.103621935, 20.0772446]
];

MatrixBuildResources.config = {
	setDataArray: {
		unadjusted: {
			"divId": "data-dygraph",
			"data": MatrixBuildResources.datas,
			"time": MatrixBuildResources.times,
			"endStaticRec": null,
			"newestSuspSed": null,
			"messageType": "setDataArray"
		},
		double: {
			"divId": "data-dygraph",
			"data": MatrixBuildResources.datas,
			"time": MatrixBuildResources.times,
			"endStaticRec": 1336202699999,
			"newestSuspSed": null,
			"messageType": "setDataArray"
		},
		quadruple: {
			"divId": "data-dygraph",
			"data": MatrixBuildResources.datas,
			"time": MatrixBuildResources.times,
			"endStaticRec": 1336203600000,
			"newestSuspSed": 1336203600000,
			"messageType": "setDataArray"
		},
		both: {
			"divId": "data-dygraph",
			"data": MatrixBuildResources.datas,
			"time": MatrixBuildResources.times,
			"endStaticRec": 1336202700000,
			"newestSuspSed": 1336203600000,
			"messageType": "setDataArray"
		}
	},
	transformArray: {
		unadjusted: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"messageType": "transformArray",
			"reqId": 3
		},
		double: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"messageType": "transformArray",
			"reqId": 3
		},
		quadruple: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"messageType": "transformArray",
			"reqId": 3
		},
		both: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"messageType": "transformArray",
			"reqId": 3
		}
	},
	addBedloadToDataArray: {
	    useBedload: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"data": MatrixBuildResources.bedloadCoeffDatas,
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"riverBedload": 0.5,
			"useBedload": true,
			"bedloadPerc" : 0.5,
			"messageType": "addBedloadToDataArray",
			"reqId": 3
		},
		doNotUseBedload: {
			"divId": "data-dygraph",
			"labelDivId": "legend-dygraph",
			"data": MatrixBuildResources.datas,
			"a": 0.05,
			"b": 0.05,
			"c": 0.1,
			"d": 0.5,
			"e": null,
			"f": null,
			"g": null,
			"riverBedload": 0.5,
			"useBedload": false,
			"messageType": "addBedloadToDataArray",
			"reqId": 3
		},
	}
};

MatrixBuildResources.results = {
	unadjusted: [
		[1336201200000, [0, 0, 0], [0, 0, 0], [0, 0, 0]],
		[1336201800000, [-3.1639343850000006, -2.98698663, -2.810038875], [-2.810038875, -2.810038875, -2.810038875], [-3.1639343850000006, -3.1639343850000006, -3.1639343850000006]],
		[1336202100000, [-4.7459008565, -4.4804792645, -4.2150576724999995], [-4.2150576724999995, -4.2150576724999995, -4.2150576724999995], [-4.7459008565, -4.7459008565, -4.7459008565]],
		[1336202700000, [-7.6700842200000015, -7.23769036, -6.8052965], [-6.8052965, -6.8052965, -6.8052965], [-7.6700842200000015, -7.6700842200000015, -7.6700842200000015]],
		[1336203000000, [-9.132176415, -8.616296402500002, -8.10041639], [-8.10041639, -8.10041639, -8.10041639], [-9.132176415, -9.132176415, -9.132176415]],
		[1336203600000, [-12.476593, -11.7755615, -11.07453], [-11.07453, -11.07453, -11.07453], [-12.476593, -12.476593, -12.476593]],
		[1336203900000, [-14.148795688000002, -13.355188699000001, -12.56158171], [-12.56158171, -12.56158171, -12.56158171], [-14.148795688000002, -14.148795688000002, -14.148795688000002]],
		[1336204500000, [-18.049970725, -17.045905050000002, -16.041839375], [-16.041839375, -16.041839375, -16.041839375], [-18.049970725, -18.049970725, -18.049970725]],
		[1336204800000, [-20.0005606775, -18.891265545000003, -17.7819704125], [-17.7819704125, -17.7819704125, -17.7819704125], [-20.0005606775, -20.0005606775, -20.0005606775]]
	],
	double: [
		[1336201200000, [0, 0, 0], [0, 0, 0], [0, 0, 0]],
		[1336201800000, [-3.1639343850000006, -2.98698663, -2.810038875], [-2.810038875, -2.810038875, -2.810038875], [-3.1639343850000006, -3.1639343850000006, -3.1639343850000006]],
		[1336202100000, [-4.7459008565, -4.4804792645, -4.2150576724999995], [-4.2150576724999995, -4.2150576724999995, -4.2150576724999995], [-4.7459008565, -4.7459008565, -4.7459008565]],
		[1336202700000, [-7.687712609000001, -7.23769036, -6.787668111], [-6.787668111, -6.787668111, -6.787668111], [-7.687712609000001, -7.687712609000001, -7.687712609000001]],
		[1336203000000, [-9.158618996, -8.616296402500002, -8.073973809], [-8.073973809, -8.073973809, -8.073973809], [-9.158618996, -9.158618996, -9.158618996]],
		[1336203600000, [-12.520186548999998, -11.7755615, -11.030936451], [-11.030936451, -11.030936451, -11.030936451], [-12.520186548999998, -12.520186548999998, -12.520186548999998]],
		[1336203900000, [-14.200964721, -13.355188699000001, -12.509412677], [-12.509412677, -12.509412677, -12.509412677], [-14.200964721, -14.200964721, -14.200964721]],
		[1336204500000, [-18.119290724, -17.045905050000002, -15.972519376000001], [-15.972519376000001, -15.972519376000001, -15.972519376000001], [-18.119290724, -18.119290724, -18.119290724]],
		[1336204800000, [-20.0784561615, -18.891265545000003, -17.704074928500003], [-17.704074928500003, -17.704074928500003, -17.704074928500003], [-20.0784561615, -20.0784561615, -20.0784561615]]
	],
	quadruple: [
		[1336201200000, [0, 0, 0], [0, 0, 0], [0, 0, 0]],
		[1336201800000, [-3.1639343850000006, -2.98698663, -2.810038875], [-2.810038875, -2.810038875, -2.810038875], [-3.1639343850000006, -3.1639343850000006, -3.1639343850000006]],
		[1336202100000, [-4.7459008565, -4.4804792645, -4.2150576724999995], [-4.2150576724999995, -4.2150576724999995, -4.2150576724999995], [-4.7459008565, -4.7459008565, -4.7459008565]],
		[1336202700000, [-7.6700842200000015, -7.23769036, -6.8052965], [-6.8052965, -6.8052965, -6.8052965], [-7.6700842200000015, -7.6700842200000015, -7.6700842200000015]],
		[1336203000000, [-9.132176415, -8.616296402500002, -8.10041639], [-8.10041639, -8.10041639, -8.10041639], [-9.132176415, -9.132176415, -9.132176415]],
		[1336203600000, [-12.528045903999999, -11.7755615, -11.023077096], [-11.023077096, -11.023077096, -11.023077096], [-12.528045903999999, -12.528045903999999, -12.528045903999999]],
		[1336203900000, [-14.225975044, -13.355188699000001, -12.484402354], [-12.484402354, -12.484402354, -12.484402354], [-14.225975044, -14.225975044, -14.225975044]],
		[1336204500000, [-18.178602979000004, -17.045905050000002, -15.913207121000001], [-15.913207121000001, -15.913207121000001, -15.913207121000001], [-18.178602979000004, -18.178602979000004, -18.178602979000004]],
		[1336204800000, [-20.154919386499998, -18.891265545000003, -17.6276117035], [-17.6276117035, -17.6276117035, -17.6276117035], [-20.154919386499998, -20.154919386499998, -20.154919386499998]]
	],
	both: [
		[1336201200000, [0, 0, 0], [0, 0, 0], [0, 0, 0]],
		[1336201800000, [-3.1639343850000006, -2.98698663, -2.810038875], [-2.810038875, -2.810038875, -2.810038875], [-3.1639343850000006, -3.1639343850000006, -3.1639343850000006]],
		[1336202100000, [-4.7459008565, -4.4804792645, -4.2150576724999995], [-4.2150576724999995, -4.2150576724999995, -4.2150576724999995], [-4.7459008565, -4.7459008565, -4.7459008565]],
		[1336202700000, [-7.687712609000001, -7.23769036, -6.787668111], [-6.787668111, -6.787668111, -6.787668111], [-7.687712609000001, -7.687712609000001, -7.687712609000001]],
		[1336203000000, [-9.158618996, -8.616296402500002, -8.073973809], [-8.073973809, -8.073973809, -8.073973809], [-9.158618996, -9.158618996, -9.158618996]],
		[1336203600000, [-12.580214935999999, -11.7755615, -10.970908064], [-10.970908064, -10.970908064, -10.970908064], [-12.580214935999999, -12.580214935999999, -12.580214935999999]],
		[1336203900000, [-14.278144076, -13.355188699000001, -12.432233322], [-12.432233322, -12.432233322, -12.432233322], [-14.278144076, -14.278144076, -14.278144076]],
		[1336204500000, [-18.230772011000003, -17.045905050000002, -15.861038089000001], [-15.861038089000001, -15.861038089000001, -15.861038089000001], [-18.230772011000003, -18.230772011000003, -18.230772011000003]],
		[1336204800000, [-20.2070884185, -18.891265545000003, -17.5754426715], [-17.5754426715, -17.5754426715, -17.5754426715], [-20.2070884185, -20.2070884185, -20.2070884185]]
	],
};