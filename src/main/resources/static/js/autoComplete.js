$(function () {
    var availableTags;
    $('#searchInput').on('focus', function () {
        $.post('/catalog/searchProductAutoComplete', null, function (data) {
            availableTags = data.toString().split('*');
            console.log(availableTags);
            console.log("yes,you get the message");
            $("#searchInput").attr('autocomplete', 'on');
            $("#searchInput").autocomplete({
                source: availableTags
            });
        });
    });

});