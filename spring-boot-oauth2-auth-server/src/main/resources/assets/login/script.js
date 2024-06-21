$('.message a').click(function(){
   $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});

const inputs = document.getElementById("inputs");
var otpInputShow = false;

inputs.addEventListener("input", function (e) {
	const target = e.target;
	const val = target.value;

	if (isNaN(val)) {
		target.value = "";
		return;
	}

	if (val != "") {
		const next = target.nextElementSibling;
		if (next) {
			next.focus();
		}
	}
});

inputs.addEventListener("keyup", function (e) {
	const target = e.target;
	const key = e.key.toLowerCase();

	if (key == "backspace" || key == "delete") {
		target.value = "";
		const prev = target.previousElementSibling;
		if (prev) {
			prev.focus();
		}
		return;
	}
});

function submitOtp() {
    if(!otpInputShow) {
        var otp = "";
        for(var i=0; i<document.getElementById("inputs").getElementsByTagName("input").length; i++) {
          otp += document.getElementById("inputs").getElementsByTagName("input")[i].value;
        }
        document.getElementById("otp-input").value = otp;
    }
    return true;
}

function toggleOtpInput(e) {
    if(!otpInputShow) {
        document.getElementById("otp-input").style.display = "block";
        document.getElementById("container-otp").style.display = "none";
        document.getElementById("message").innerHTML = "Enter Backup Code";
        document.getElementById("otp-switch-btn").innerHTML = "Enter OTP Instead";
    } else {
        document.getElementById("otp-input").style.display = "none";
        document.getElementById("container-otp").style.display = "block";
        document.getElementById("message").innerHTML = "Enter OTP";
        document.getElementById("otp-switch-btn").innerHTML = "Enter Backup Code";
    }
    otpInputShow = !otpInputShow;
}