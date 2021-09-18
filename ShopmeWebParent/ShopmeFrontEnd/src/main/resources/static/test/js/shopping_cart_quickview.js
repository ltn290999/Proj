function getCountItems() {
	var contextPath = "[[@{/}]]";
	//var url = contextPath + "cart/count";
	var url = "http://localhost/Shopme/cart/count";
	$.get(url, function(responseJSON) {
	}).done(function(responseJSON) {
		if (isNaN(parseInt(responseJSON))) {
			$(".cart-count").text(0);
		} else {
			$(".cart-count").text(responseJSON);
		}

	}).fail(function() {
	});
}