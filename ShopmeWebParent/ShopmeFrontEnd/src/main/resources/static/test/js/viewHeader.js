$(document).ready(
	function() {
		$(".link-detail")
			.on(
				"click",
				function(e) {
					e.preventDefault();
					linkDetailURL = $(this).attr("href");
					$(".quickview-wrapper")
						.addClass("open").show();
					$(".quick-modal").addClass("show")
						.find("#quickview-content")
						.load(linkDetailURL);

				});

		$("#cartQuickview").on("click", function(e) {
			e.preventDefault();
			linkDetailURL = $("#cartQuickview a").attr("href");
			$("#cartView").load(linkDetailURL);

		});
		getCountItems();

		$(".addToCart").on("click", function() {
			addToCart(this);
			return false;
		});
		
	});


function addToCart(link) {
	quantity = 1;
	var productId = $(link).attr("pid");
	url = contextPath + "cart/add/" + productId + "/" + quantity;
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		getCountItems();
		showModalDialog("Shopping Cart",response);
	}).fail(function() {
		showErrorModal("Error while adding product to shopping cart.");
	});

}