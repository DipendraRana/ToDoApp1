var ToDo = angular.module("ToDo");
ToDo.filter('labelExtratorFilter', function() {
	return function(notes, navBarName) {
		var array = [];
		var length = notes.length;
		for (var noteNo = 0; noteNo < length; noteNo++) {
			var labels = notes[noteNo].labels;
			for (var labelNo = 0; labelNo < labels.length; labelNo++) {
				if (labels[labelNo].labelName == navBarName) {
					array.push(notes[noteNo]);
				}
			}
		}
		return array;
	}
});