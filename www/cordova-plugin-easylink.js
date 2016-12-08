var exec = require('cordova/exec');
var easyLink = {
	getWifiSSid:function(success,error) {
		exec(success, error, "EasyLink", "getWifiSSid", []);
	},
	startSearch:function(wifiSSid,wifiPsw,success,error) {
		exec(success, error, "EasyLink", "startSearch", [wifiSSid,wifiPsw]);
	},
	stopSearch:function(success,error) {
		exec(success, error, "EasyLink", "stopSearch", []);
	},
};
module.exports = easyLink;