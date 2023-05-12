package lk.ijse.posm.util.ValidationPattern;

import java.util.regex.Pattern;

public class RegExPatterns {
    private static final Pattern namePattern = Pattern.compile("^[a-zA-Z '.-]{4,}$");
    private static final Pattern idPattern = Pattern.compile("^([0-9]{9}[x|X|v|V]|[0-9]{12})$");
    private static final Pattern registrationIdPattern = Pattern.compile("^[I][T][0-1]{1,}$");
    private static final Pattern emailPattern = Pattern.compile("(^[a-zA-Z0-9_.-]+)@([a-zA-Z]+)([\\.])([a-zA-Z]+)$");
    private static final Pattern cityPattern = Pattern.compile("[a-zA-Z]{4,}$");
    private static final Pattern mobilePattern = Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$");
    private static final Pattern addressPattern = Pattern.compile("^[A-Za-z0-9'\\/\\.\\,]{5,}$");
    private static final Pattern postalCodePattern = Pattern.compile("^\\d{5}$");
    private static final Pattern oldIDPattern = Pattern.compile("^[0-9]{9}[vVxX]$");
    private static final Pattern doublePattern = Pattern.compile("^[0-9]+\\.?[0-9]*$");
    private static final Pattern intPattern = Pattern.compile("^[1-9][0-9]?$|^100$");
    private static final Pattern twoStringCheckPattern = Pattern.compile("^\\w+(\\s\\w+)+$");
    private static final Pattern intWithRange100Pattern = Pattern.compile("^(?:100|[1-9]\\d?)$");
    private static final Pattern parcelDescriptionPattern= Pattern.compile("^[1-9]\\d*-[1-9]\\d*$");
    private static final Pattern moneyTypePattern= Pattern.compile("^\\w+(\\s+\\w+)?$");
    private static final Pattern referenceTypePattern= Pattern.compile("^\\w+(\\s+\\w+)?$");
    private static final Pattern userNamePattern=Pattern.compile("^posm@[a-zA-Z]{5}$");
    private static final Pattern userPWPattern=Pattern.compile("^posm@\\d{5}$");
    private static final Pattern imagePathPattern=Pattern.compile("/\\.(jpg|jpeg|png|gif|bmp)$/i");
    private static final Pattern stringPattern=Pattern.compile("r'\\w+'");
    private static final Pattern currencyPricePattern=Pattern.compile("^Rs\\.\\d+(\\.\\d+)?$");
    private static final Pattern itemNamePattern=Pattern.compile("^[a-zA-Z0-9.-]+$");

    public static Pattern getRegistrationIdPattern() {return registrationIdPattern;}
    public static Pattern getIntPattern() {return intPattern;}
    public static Pattern getOldIDPattern() {return oldIDPattern;}
    public static Pattern getPostalCodePattern() {return postalCodePattern;}
    public static Pattern getAddressPattern() {return addressPattern;}
    public static Pattern getMobilePattern() {return mobilePattern;}
    public static Pattern getCityPattern() {return cityPattern;}
    public static Pattern getEmailPattern() {return emailPattern;}
    public static Pattern getNamePattern() {return namePattern;}
    public static Pattern getIdPattern() {return idPattern;}
    public static Pattern getDoublePattern() {return doublePattern;}
    public static Pattern getTwoStringCheckPattern() {
        return twoStringCheckPattern;
    }
    public static Pattern getIntWithRange100Pattern() {
        return intWithRange100Pattern;
    }
    public static Pattern getParcelDescriptionPattern() {
        return parcelDescriptionPattern;
    }
    public static Pattern getMoneyTypePattern() {
        return moneyTypePattern;
    }
    public static Pattern getReferenceTypePattern() {
        return referenceTypePattern;
    }
    public static Pattern getUserNamePattern() {
        return userNamePattern;
    }
    public static Pattern getUserPWPattern() {
        return userPWPattern;
    }
    public static Pattern getImagePathPattern() {
        return imagePathPattern;
    }
    public static Pattern getStringPattern() {
        return stringPattern;
    }
    public static Pattern getCurrencyPricePattern() {
        return currencyPricePattern;
    }
    public static Pattern getItemNamePattern() {
        return itemNamePattern;
    }
}