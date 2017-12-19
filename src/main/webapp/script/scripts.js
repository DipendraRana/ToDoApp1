function toogleNav() {
	var sideNav = document.getElementById("sideNavContent").style.width;
	if (sideNav == "0px")
		openNav();
	else 
		closeNav();
}

/* Set the width of the side navigation to 250px */
function openNav() {
	if(screen.width<=1032){
		document.getElementById("sideNavContent").style.width = "250px";
		document.getElementById("noteContainer").style.marginLeft = "0px";
	}	
	else {
		document.getElementById("sideNavContent").style.width = "250px";
		document.getElementById("noteContainer").style.marginLeft = "250px";
	}
}	

/* Set the width of the side navigation to 0 */
function closeNav() {
	document.getElementById("sideNavContent").style.width = "0px";
	document.getElementById("noteContainer").style.marginLeft = "0px";
}

function cleanFeild() {
	document.getElementById("cleanField").value = '';
}

$(document).mouseup(function (e) {
	if(screen.width<=1032 && (!$('#sideNavContent').is(e.target) && !$('#toggler').is(e.target) && !$('#sidetogglebutton').is(e.target)))
		closeNav();
});

$(document).mouseup(function (e)
	{
	    var container = new Array();
	    container.push($('#note-title'));
	    container.push($('#note_icons'));
	    
	    if(!$('#note-body').is(e.target)){
		    $.each(container, function(key, value) {
		        if (!$(value).is(e.target) && !$(e.target).parents('#note').length) // if the target of the click isn't the container...
		            $(value).hide();
		        else
		        	$(value).show();
		    });
	    }
	    else {
	    	$.each(container, function(key, value) {
		            $(value).show();
		    });
	    }	
	});