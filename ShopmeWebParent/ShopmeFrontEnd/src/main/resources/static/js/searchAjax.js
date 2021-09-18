(function($) {

	var $project = $("#searchInput");

	$project.autocomplete({
		minLength: 3,
		source : function(request, response) {
			$.getJSON("http://localhost/Shopme/searchJquery?term="
				+ request.term, function(data) {

					response($.map(data, function(value, key) {
						return {
							label: value.name,
							value: value.alias,
							alias: value.alias,
							mainImage: value.mainImagePath
						};
					}));
				});
		},

		select: function(event, ui) {
			window.location.href = "/Shopme/p/" + ui.item.alias;
		},
		focus: function(event, ui) {
			$project.val(ui.item.label);
			return false;
		}
	});

	$project.data("ui-autocomplete")._renderItem = function(ul, item) {

		var $li = $('<li>'), $img = $('<img>');

		$img.attr({
			src: '/Shopme' + item.mainImage,
			alt : item.label
		});
		$img.css({
			'width': '32px',
			'height': '32px'
		});

		$li.attr('data-value', item.label);
		$li.append('<a href="#">');
		$li.find('a').append($img).append(item.label);

		return $li.appendTo(ul);
	};

})(jQuery);
