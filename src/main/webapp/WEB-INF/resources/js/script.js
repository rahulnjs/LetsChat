/**
 * 
 */
// var apiKey = 'GGZK6ZB6MOTO';
var lng = 77.12;
var lat = 28.38;
var bubbleID = 0;
var startDate='';
var stD = 0;
var cT = '';
var isTyping = false;

const DISABLED = 'disabled';

var $toast = $('#toast'); 
var $modalContent = $('.modal-content');
var $modal = $('.modal');

$('#create-chat-button').click(function() {
	if(!hasSpace()) {
		if($('#user').val() != '' && $('#c-r-name').val() != '') {
			$.ajax({
				method: "post",
				url: "create-chat-room",
				data: $("#create-chat-frm").serialize()
			}).done(function(data) {
				var msg = data;
				$('#all').hide('clip', 400, function() {
					$('#chat-room-id').text(msg);
					$('#cont-butt').attr('href', msg + '.chat');
					window.scrollTo(0, 0);
					$('#cont').show('clip', { /* percent: 100 */ }, 500, function(){
						setCookie($('#user').val(), msg);
						getCookie();
					});			
				});
			}).fail(function() {
				alert('failed');
			});
		}
	}else {
		$('#err-txt').show();
		$('#err-txt').text('Oops, nick name can\'t have spaces.').fadeOut(5000);
	}
});


function hasSpace() {
	var expr = /\s/;
	return expr.test($('#user').val())
}


$('#join-chat-butt').click(function() {
	if(!hasSpace()) {
		if($('#cid').val() != '' && $('#user').val() != '') {
			$.ajax({
				method: "post",
				url: "UserAdder",
				data: $('form').serialize()
			}).done(function(data) {
				if(data === '0') {
					$('#err-txt').text('Oops, Chat Room ID is not valid.');
				} else if (data === '-1') {
					$('#err-txt').show();
					$('#err-txt').text('Oops, someone already has this nick name.').fadeOut(5000);
				} else {
					setCookie($('#user').val(), $('#cid').val());
					window.location.assign($('#cid').val() + '.chat');
				}
			}).fail(function() {
				$('#err-txt').show();
				$('#err-txt').text('Oops, Something went wrong.').fadeOut(5000);
			});
		}
	}else {
		$('#err-txt').show();
		$('#err-txt').text('Oops, nick name can\'t have spaces.').fadeOut(5000);
	}
});

function setCookie(user, chatRoomID) {
    var d = new Date();
    d.setTime(d.getTime() + (3*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = "user=" + user + "; " + expires;
    document.cookie = "cid=" + chatRoomID + "; " + expires;
}

function getCookie() {
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        console.log(ca[i]);
    }
    // return "";
}

function notTyping() {
	isTyping = false;
}

function doPostMsg(e){
	isTyping = $('#text-ip').val().length > 0;
	if(e.which === 13 ){
		postThisMessage();
	}
}

$('#send-butt').click(postThisMessage);

function postThisMessage() {
	if($('#text-ip').val().trim().length > 0) {
//		getTime();
		postMessage(addNewBubble());
	}
}

function checkForNewDate() {
	if((getDatePart(startDate) < getDatePart(cT))  ||  $('.msg-wrapper').length === 0) {
		var monthNames = [
		  "January", "February", "March",
		  "April", "May", "June", "July",
		  "August", "September", "October",
		  "November", "December"
		];
		var parts = cT.split(" ")[0].split("-");
//		alert(parts[0] + ' ' + parts[1] + ' ' + parts[2]);
		var div = getMarkerForText(monthNames[parseInt(parts[1]) - 1] + ' ' + parts[2] + ', ' + parts[0]);
	//	alert(div);
		$('#chat-box').append(div);
	}
}

function getMarkerForText(text) {
	return '<div class="msg-wrapper day"><div class="msg day day1">' + text  + '</div></div>';
}

function getDatePart(date) {
	return parseInt(date.split(" ")[0].split("-")[2]);
}

function addNewBubble() {
	checkForNewDate();
	var sentAt = getFormattedTime(cT + '');
	var dateBubble = '<div class="msg-wrapper day"><div class="msg day day1">' + + '</div></div>';
	var bubble = '<div class="msg-wrapper outgoing">' + 
					'<div class="msg out">' +
					'<div class="main-msg">' + $('#text-ip').val() + '</div>' +
					'<div class="time-bar outgoing">' +
					'<span class="time"> ' + sentAt + '&nbsp;&nbsp;<span id="bubble-' + bubbleID + '" style="display:none;">&#10004;<span></span>' +
					'</div></div></div>';
	var tickID = '#bubble-' + bubbleID;
	bubbleID++;
	checkForNewDate();
	$('#chat-box').append(bubble);
	scrollToChatBoxBottom();
	return tickID;
}


function scrollToChatBoxBottom() {
	var height = 0;
	$('#chat-box div').each(function(i, value){
	    height += parseInt($(this).height());
	});
	height += '';
	$('#chat-box').animate({scrollTop: height});
}

// http://www.pinsho.com/coleccion-de-wallpapers-de-la-app-telegram/

function getFormattedTime(time) {
	time = time.toString().split(" ")[1];
	var parts = time.toString().split(":");
	var hr = parts[0] % 12;
	if(parseInt(parts[0]) % 12 == 0) {
		hr = 12;
	}
	var min = parts[1];
	/* min = parseInt(min) < 10 ? '0' + min : min; */
	hr = parseInt(hr) < 10 ? '0' + hr : hr;
	var a_p = parseInt(parts[0]) >= 12 ? 'pm' : 'am';  
	return hr + ':' + min + ' ' + a_p;
}

function getStatuses() {
	$.ajax({
		method: 'get',
		url: 'get-statuses',
		data: {time: cT}
	}).done(function(msg) {
		//console.log(msg);
		updateStatus(msg);
	});
}

function updateStatus(json) {
	var jsonObj = $.parseJSON(json);
	var i = 0;
	var me = $('.user-details')[$('.user-details').length - 1];
	while(i < jsonObj.length) {
		var name = jsonObj[i].name;
		if($('#usr-' + name).length === 1) {
			$('#usr-' + name).text(name);
			$('#st-' + name).text(jsonObj[i].status);
		}else {
			var div = '<div class="user-details"><div><span class="user-name" id="usr-' + name + '">' + name + '</span></div><div><span class="user-status" id="st-' + name + '">' + jsonObj[i].status + '</span></div></div>'
			$(me).before(div);
			checkForNewDate();
			$('#chat-box').append(getMarkerForText(name + ' joined'));
			scrollToChatBoxBottom();
		}
		i++;
	}
	

}


function getMsgs() {
	console.log('getMsgs called!');
	$.ajax({
		method: 'get',
		url: 'get-msgs',
		data: {}
	}).done(function(msg) {
		var obj = $.parseJSON(msg);
		var i = 0;
		while(i < obj.length) {
			var bubble = '<div class="msg-wrapper incoming">' +
								'<div class="msg in">' +
									'<div class="msg-by-wrapper">' + 
										'<span class="msg-by">' + obj[i].by + '</span></div>' +
								'<div class="main-msg">' + obj[i].msg + '</div>' + 
								'<div class="time-bar incoming">' +
									'<span class="time">' + obj[i].at + '</span>' +
								'</div></div></div>';
			checkForNewDate();
			$('#chat-box').append(bubble);
			scrollToChatBoxBottom();
			i++;
		}
	});
}

function addMessagesToChatBar(msgs) {
	var jsonObj = $.parseJSON(msgs);
	
}

function postStatus() {
	//getTime();
	if ($("#text-ip").is(':focus') && $('#text-ip').val().length === 0) {
		notTyping();
	}
	$.ajax({
		method: 'post',
		url: 'post-status',
		data: {status: isTyping, time: cT}
	}).done(function(msg) {
	});
}

function postMessage(id) {
	var msg = $('#text-ip').val();
	$('#text-ip').val('');
	$.ajax({
		method: 'post',
		url: 'post-msg',
		data: {'msg' : msg, 'time' : cT}
	}).done(function(msg) {
		if(msg === 'ok') {
			$(id).fadeIn(900);
		}
	});
}

function startServices() {
	/*var msgFetcher = setInterval(getMsgs, 500);
	var statusPoster = setInterval(postStatus, 1000);
	var statusFetcher = setInterval(getStatuses, 1000);*/
}

function geoFindMe() {
    getTime();
	startServices();
	if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(success, error, geoOptions);
    } else {
        alert("Geolocation services are not supported by your web browser.");
    }
}

function success(position) {
    lat = position.coords.latitude;
    lng = position.coords.longitude;
    //getTime();
}

function error(error) {
    //alert("Unable to retrieve your location due to " + error.code + ": " + error.message);
	//getTime();
}

var geoOptions = {
    enableHighAccuracy: true,
    maximumAge: 30000,
    timeout: 2700
};


function getQueryString() {
	var req = 'http://api.geonames.org/timezoneJSON?lat=' + lat +'&lng=' + lng + '&username=rahulEE';
	return req;
}

function getTime() {
	$.ajax({
		method: 'get',
		url: getQueryString(),
		data: {}		
	}).done(function(msg) {
		cT = msg.time;
		if(stD == 0) {
			startDate = cT;
			stD++;
			setInterval(clock, 60 * 1000);
		}
		
	});
}

function clock() {
	var parts = cT.split(" ")[1].split(":");
	var hr = parts[0];
	var min = parts[1];
	var needed = true;
	min++;
	hr = parseInt(hr);
	if(min == 60) {
		min = 0;
		hr++;
		if(hr == 24) {
			hr = 0;
			getTime();
			needed = false;
		}
	}
	if(needed) {
		cT = cT.split(" ")[0] + ' ' + (hr > 9 ? hr : '0' + hr) + ':' + (min > 9 ? min : '0' + min);
	}
	console.log(cT);
}


$('#user-butt').click(function() {
	//$('#mem-list').toggleClass('mem-list-class-phn');
	if($('#cc').html().length === 0){
		$('#cc').html($('#mem-list').html());
		$('#cc').show('slide', 600);
		$('#user-butt').css('color', '#3c3c3c');
		$('#user-butt').css('background-color', 'rgba(248, 205, 85, 1)');
		$('#user-butt').css('border', '1px solid rgba(248, 205, 85, 1)');
		$('body').css('overflow', 'hidden');
		//$('#cc').css('position', 'fixed');

	}else {
		$('#cc').html('');
		$('#cc').hide('drop', 600);
		$('#user-butt').css('background-color', '#3c3c3c');
		$('#user-butt').css('color', 'rgba(248, 205, 85, 1)');
		$('#user-butt').css('border', '1px solid gray');
		//$('#cc').css('position', 'absolute');
		$('body').css('overflow', 'auto');
	}
}); 








$('#login-btn').on('click', function() {
	var $btn = $(this);
	$btn.addClass(DISABLED);
	toast('Loging in...');
	$.ajax({
		method: "post",
		url: "login",
		data: $('form').serialize(),
		success: function(data) {
			if(data != 'null') {
				window.localStorage.setItem('fname', data);
				window.localStorage.setItem('user', $('#user').val());
				setToastText('Redirecting...');
				window.location.assign('user/' + $('#user').val());
			} else {
				showError('Invalid username or password');
				$btn.removeClass(DISABLED);
				hideToast();
			}
		}
	});
});

function formToJSON(form) {
	var json = {};
    var arr = $(form).serializeArray();
    $(arr).each(function(i, obj) {
      var $n = obj.name,
        $v = obj.value;
      if (!json[$n]) {
        json[$n] = $v;
      } else {
        var what = json[$n];
        if (typeof what === 'string') {
          json[$n] = [what];
        }
        json[$n].push($v);
      }
    });
    return json;
}


$('#signup-btn').on('click', function() {
	var $butt = $(this);
	var u = formToJSON($butt.attr('data-form'));
	if (valid(u)) {
		$butt.addClass(DISABLED);
		toast('Signing up...');
		$.ajax({
			url : 'me-signup',
			method : 'post',
			data : {
				name : u.fullname,
				user : u.username,
				pass : u.pass
			},
			success : function(data) {
				if (data != 'null') {
					$butt.removeClass(DISABLED);
					hideToast();
					showError('Username is not available.');
				} else {
					window.localStorage.setItem('fname', data);
					window.localStorage.setItem('user', u.username);
					setToastText('Redirecting...');
					window.location.assign('user/' + u.username);
				}
			},
			error : function() {

			}
		});
	}

	function valid(json) {
		var v = true;
		var msg;
		Object.keys(json).forEach(function(key) {
			if (json[key].trim().length == 0) {
				msg = 'Fields can\'t be empty!';
				v = false;
			}
		});

		if (json.pass != json.cpass) {
			v = false;
			msg = 'Passwords do not match!';
		}

		if (!v) {
			showError(msg);
		}

		return v;
	}

}); 

function showError(msg) {
	$('#err-txt').show();
	$('#err-txt').text(msg).fadeOut(5000);
}


function toast(msg) {
	$toast.css({'display': 'inline-block'});
	setToastText(msg);
}

function setToastText(msg) {
	$toast.html(msg);
}

function hideToast() {
	$toast.hide();
}

$('.close').on('click', function() {
	$modal.hide();
});

$('#new-cr-btn').on('click', function() {
	$('.modal-form').hide();
	$('#new-cr-form').show();
	$modal.show();
	
});

$('#join-cr-btn').on('click', function() {
	$('.modal-form').hide();
	$('#join-cr-form').show();
	$modal.show();
});


$('#new-cr-from-btn').on('click', function() {
	var btn = $(this);
	var cr = {
		name: $('#cr-name').val().trim(),
		slug: toSlug($('#cr-name').val()),
		creator: cuser(),
		_cd: new Date(),
		msgs: [],
		users: [{
			user: cuser(),
			status: '',
			fname: fname()
		}]
	};
	
	if(cr.name.trim().length > 0) {
		$.ajax({
			url : cuser() + '/create-cr',
			method : 'post',
			data : {
				cr: JSON.stringify(cr)
			},
			success : function(data) {
				if(data != 'un-auth') {
					if (data == 'e') {
						$butt.removeClass(DISABLED);
					} else {
						//window.location.assign('/chat/' + cr.slug + '.chat');
					}
				}
			},
			error : function() {

			}
		});
	}
	
});


$('#join-cr-form-btn').on('click', function() {
	var btn = $(this);
	var cr = toSlug($('#jcr-name').val());
	var obj = {
		user: cuser(),
		status: '',
		fname: fname()
	};
	$.ajax({
		url : cuser() + '/join-cr',
		method : 'post',
		data : {
			cr: cr,
			u: JSON.stringify(obj)
		},
		success : function(data) {
			if(data == 'true') {
				window.location.assign('/chat/' + cr);
			} else {
				
			}
		},
		error : function() {

		}
	});
});


function cuser() {
	return window.location.pathname.split('/')[2];
}

function toSlug(t) {
	return t.trim().toLowerCase().replace(/\s+/g, '-');
}

function fname() {
	return window.localStorage.getItem('fname');
}


function fetchAllChatRooms() {
	toast('Fetching chat rooms...');
	$.ajax({
		url : cuser() + '/all-chat',
		method : 'get',
		success : function(data) {
			hideToast();
			var json = $.parseJSON(data);
			renderChats(json, '0');
			renderChats(json, '1');
			$('.cr').on('click', function() {
				window.location.assign('/chat/' + $(this).attr('data-slug'));
			});
		},
		error : function() {

		}
	});
	
}


function renderChats(json, no) {
	var $list = $('#cr-list-' + no);
	json[no].forEach(function(cr) {
		$list.append(renderChat(cr));
	});
	if(json[no].length == 0) {
		$('#cr-list-' + no + '-p').hide();
	}
	
	function renderChat(cr) {
		cr.cd = toDate(cr._cd);
		cr.mem = toMemStatus(cr.users);
		var templ = `
			<div class="cr" data-slug="${cr.slug}">
				<div class="title">
					${cr.name}
				</div>
				<div class="mem-count">
					${cr.mem}
				</div>
				<div>
					<span class="creator">@${cr.creator}</span>
					<span class="cd">${cr.cd}</span>
				</div>
			</div>
		`;
		return templ;
	}
	
	
}

function toMemStatus(users) {
	if(users.length < 3) {
		if(users.length === 1) {
			return 'Only you.';
		} else {
			return 'You and 1 other.';
		}
	} else {
		var othr = users.length - 2;
		return 'You, ' + users[users.length - 1].fname.split(/\s+/)[0] + ' and ' +
					othr + ' other.';
	}
}

function toDate(d) {
	var date = new Date(d);
	return date.toDateString();
}


function initChatRoom() {
	var data = $.parseJSON($('#chat-data').text());
	$('#chat-box-header').text(data.name);
	showUsers(data.users, data.creator);
}

function showUsers(users, creator) {
	var me = addMe(creator);
	users.forEach(function(u) {
		if(u.user != window.localStorage.getItem('user')) {
			u.cr = u.user == creator ? 'block' : 'none';
			u.status = 'Online';
			$(me).before(getUserTemplate(u));
		}
	});
	
}

function addMe(creator) {
	var u = {
		fname: 'You',
		user: window.localStorage.getItem('user'),
		status: 'as ' + window.localStorage.getItem('user')
	}
	u.cr = u.user == creator ? 'block' : 'none';
	$('#mem-list').append(getUserTemplate(u));
	return `#ud-${u.user}`;
}

function getUserTemplate(u) {
	return `<div class="user-details" id="ud-${u.user}" title="${u.user}">
				<div>
					<span class="user-name" id="usr-${u.user}">${u.fname}</span>
					<span id="creator" style="display: ${u.cr}"> Creator </span>
				</div>
				<div>
					<span class="user-status" id="st-${u.user}">${u.status}</span>
				</div>
			</div>`;
}










