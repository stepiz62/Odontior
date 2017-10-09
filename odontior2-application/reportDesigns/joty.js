importPackage(Packages.java.text);
importPackage(Packages.java.util);

function currencyFormat(value, lang, country) {
	var loc = new Locale(lang, country);
	var frmt = NumberFormat.getCurrencyInstance(loc);
	return frmt.format(value);
}

function cyFormatByPrm(value) {
	return currencyFormat(value, params["loc_lang"].value, params["loc_country"].value);
}
