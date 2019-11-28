package com.weedeo.user.Utils;

public class Constants {

    public static final String BASE_URL = "https://y3ifkxtem7.execute-api.ap-south-1.amazonaws.com/dev/api/" ;
    public static final String FCM_PUSH_URL = "https://fcm.googleapis.com/fcm/send" ;
    public static final int PROFILE_ASPECT_RATIO_X = 1;
    public static final int PROFILE_ASPECT_RATIO_Y = 1;
    public static final String KEY_ADDRESS_DATA = "address_data";
    public static final String KEY_MALE = "male" ;
    public static final String KEY_FEMALE = "female" ;
    public static final String KEY_OTHER = "other" ;
    public static final String PROFILE_IMAGE_EXTENTION = "https://weedeo-dev-ap-south-1.s3.ap-south-1.amazonaws.com/profile_image/";
    public static final String KEY_NEW_ADDRESS = "new_address";
    public static final String KEY_SET_CURRENT_LOCATION = "current_location" ;
    public static final String LOG_FILE_PATH = "logs/users/";
    public static final String FCM_USER_TABLE_NAME = "users" ;
    public static final String FCM_ADMIN_TABLE_NAME = "shop_admin" ;
    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_SHOP_IMAGE = "shop_image";
    public static final String SHOP_IMAGE_EXTENTION ="https://weedeo-dev-ap-south-1.s3.ap-south-1.amazonaws.com/shop/";
    public static final String CATEGORY_IMAGE_EXTENTION ="https://weedeo-dev-ap-south-1.s3.ap-south-1.amazonaws.com/";
    public static final String KEY_MEMBER_CHECK_SUCCESS = "success" ;
    public static final String KEY_MEMBER_CHECK_FAILED = "failed" ;
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CLASS_REFERENCE = "class_reference";
    public static final String KEY_CITY = "city";
    public static final String KEY_SHOP_NAME = "shop_name";
    public static final String KEY_CATEGORY_ID = "category_id" ;
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String HOME_FRAGMENT_TAG = "home";
    public static final String SEARCH_FRAGMENT_TAG = "search";
    public static final String CART_FRAGMENT_TAG = "cart";
    public static final String KEY_IS_PROFILE_UPDATE = "isProfileUpdate";


    public static String LOGGING_URL = "https://eneviq771l4b7.x.pipedream.net/";

    public static String UPLOAD_ENCRYPT_ACCESS_KEY = "sfmhQRF9har+N4GQu/PESn+iH/dteubT1X+isBOYdn8=";
    public static String UPLOAD_ENCRYPT_SECRET_KEY = "kUwtKGLB2IWCV8Dg20CtLK/elGttLnDQBMcopzIg2KX7lb6SQ6o6Gnix063PCOOE";




    public static String FCM_LEGACY_KEY = "key=AIzaSyArmd07d63g6NM1TSUjR02qEMGHfwYkEws";


    /*Intent Keys*/
    public static String PLACE = "place";


    public static String USER_TYPE = "user";


    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 400;
    public static final int ERROR_UNAUTHORIZED_CODE = 401;
    public static final int ERROR_BAD_GATEWAY_CODE = 502;

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    //Database Keys
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weedeo_user";
    public static final String TABLE_USER = "user";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_FCM_UID = "fcmUid";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_PHONE_NUMBER = "userNumber";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_ACCESS_TOKEN = "accessToken";

    //Shared Preference Name
    public static final String PREFS_NAME = "user_data";


    public static final String CACHE_PROFILE_IMAGE_PATH_EXTENSION = "/profile_image/";
    public static final String PROFILE_IMAGE_PATH_S3 = "profile_image/";
    public static final String EXTENSION_PNG = ".png";
    public static final int CAMERA_FLAG = 0;
    public static final int GALLERY_FLAG = 1;
    public static final int IMAGE_COMPRESSION = 80;


}
