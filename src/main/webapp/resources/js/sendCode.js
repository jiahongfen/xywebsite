function sendCode(type) {
    if ($('#phone').valid()) {
        var value = $('#phone').val();
        if (value && value.length == 11) {
            $.ajax({
                cache : false,
                url : sendCodeUrl,
                data : {
                    type : type,
                    phone : value
                },
                success: function(result){
                    if (result == "success") {
                        // 1分钟内禁止点击
                        for (var i = 0; i < 60; i++) {
                            // 1秒后显示
                            window.setTimeout("updateTime(" + (60 - i) + ")", i * 1000);
                        }
                    } else {
                        var errors;
                        if (result == 'phoneSignedUp') {
                            errors = { phone: messages.phoneSignedUp };
                        } else if (result == "phoneNotSignedUp") {
                            errors = { username: messages.phoneNotSignedUp };
                        }
                        if (type == 2) {
                            phoneCodeSigninValidator.showErrors(errors);
                        } else if (type == 1) {
                            signupFormValidator.showErrors(errors);
                            showForgetPassword();
                        }
                        $("#sendCodeBtn").prop('disabled', false);
                        $("#sendCodeBtn").val(messages.sendCode);
                    }
                }
            });
            $("#sendCodeBtn").prop('disabled', true);
            $("#sendCodeBtn").val(messages.sendingCode);
        } else {
            $('#phone').focus();
        }
    }
}

function updateTime(i) {
    // setTimeout传多个参数到function有点麻烦，只能重新获取对象
    var sendCodeBtn = $("#sendCodeBtn");
    if (i > 1) {
        sendCodeBtn.val(messages.sendCodeAgain.format(i - 1));
        sendCodeBtn.prop('disabled', true);
    } else {
        sendCodeBtn.val(messages.sendCode);
        sendCodeBtn.prop('disabled', false);
    }
}

function checkTime(waitTime) {
    if (waitTime && waitTime > 0) {
        for (var i = 0; i < waitTime; i++) {
            window.setTimeout("updateTime(" + (waitTime - i) + ")", i * 1000);
        }
    }
}