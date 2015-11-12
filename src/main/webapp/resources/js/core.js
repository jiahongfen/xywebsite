
String.prototype.format = function() {
    var str = this;
    for (var i = 0; i < arguments.length; i++) {
        var reg = new RegExp("\\{" + i + "\\}", "gm");
        str = str.replace(reg, arguments[i]);
    }
    return str;
}

String.prototype.endsWith = function(suffix) {
    return (this.substr(this.length - suffix.length) === suffix);
}

String.prototype.startsWith = function(prefix) {
    return (this.substr(0, prefix.length) === prefix);
}