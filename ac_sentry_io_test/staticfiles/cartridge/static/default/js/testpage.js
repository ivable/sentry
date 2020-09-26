/**
 * Controller for Sentry Test Page
 */

var endpoint = "/INTERSHOP/rest/WFS/"+channel+"/"+app+"/sentrytest";

$("#errormsgbtn").on("click", function() {
    $('#alert-form').hide();
    $('#alert-form-error').hide();
    
    if(!$('#errormsgfield').val()){
        $('#alert-form').show();
        return;
    }
    
    //check test rest apis present
    $.ajax({
        url: endpoint
    }).done(function(data) {
       $('#output').append("api available : "+data+"<br/>");
    })
    .fail(function() {
        $('#alert-form-error').show();
    });
    
    $.ajax({
        method: "POST",
        url: endpoint+"?msg="+$('#errormsgfield').val()
      }).done(function( data ) {
          $('#output').append("error logged : "+data+"<br/>");
      }).fail(function() {
          $('#alert-form-error').show();
      });
});

$("#client-errormsgbtn").on("click", function() {
    if($('#errormsgfield').val()){
        $('#output').append("client side error send <br/>");
        throw new Error("Client error:"+$('#errormsgfield').val())
    }
    else{
        $('#alert-form').show();
    }
});

