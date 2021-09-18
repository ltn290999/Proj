
$(document).ready(function () {
    $("a[name='linkRemoveDetail']").each(function(index){
    	$(this).click(function(){
    		removeDetailByIndex(index);
    	});
    });
});
function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']")
    divDetailsCount = allDivDetails.length;
    nextDivDetailId = divDetailsCount;

    htmlDetailSection = `
    <div class="form-inline" id="divDetails${nextDivDetailId}">
    	<input type="hidden" name="detailIDs" value="0" />
    <label class="m-3">Name:</label> <input type="text" class="form-control w-25" name="detailNames" /> <label
        class="m-3">Value:</label> <input type="text" class="form-control w-25" name="detailValues" />
</div>
    ` ;
    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId = previousDivDetailSection.attr("id");
    htmlLinkRemove = `
    <a class="btn fas fa-times-circle fa-2x icon-dark"
      href="javascript:removeDetailSectionById('${previousDivDetailId}')"
     title="Remove this detail"></a>
    `;

    previousDivDetailSection.append(htmlLinkRemove);
	$("input[name='detailNames']").last().focus();

}
function removeDetailSectionById(id) {
    $("#" + id).remove();
}
function removeDetailByIndex(index){
	$("#divDetail" +index).remove();
}
