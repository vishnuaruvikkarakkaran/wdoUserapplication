package com.weedeo.user.model;

import java.util.List;

public class ShopListResponseModel {

    /**
     * data : {"total":{"value":2,"relation":"eq"},"max_score":null,"hits":[{"_index":"shop","_type":"_doc","_id":"5db146afd9176b0007613f2e","_score":null,"_source":{"shop_name":"Test sho","city":{"name":"Nagpur","state":"state","status":"string","latitude":"8.506944","longitude":"76.956944","id":"5da9aeb6c42779000772fdd2","createdAt":"2019-10-18T12:23:18.973Z","updatedAt":"2019-10-18T12:23:18.973Z"},"primary_mobile":9486533865,"secondary_mobile":9486533865,"status":true,"shop_status":1,"shop_online_status":false,"categories":[{"name":"Test Jerin 123","tags":["TAG1","tag1","Tag3"],"status":1,"id":"5db123180b868f0008414820","createdAt":"2019-10-24T04:05:44.366Z","updatedAt":"2019-10-24T04:36:57.045Z"}],"sub_categories":[],"customer_rating":0,"createdAt":"2019-10-24T06:37:35.822Z","updatedAt":"2019-11-07T11:05:00.522Z","primary_image":"wall-of-china_0.jpg","brands":{"name":"Samsung","id":"5da047a15caef700082ff711","createdAt":"2019-10-11T09:12:16.997Z","updatedAt":"2019-10-11T09:13:05.250Z"},"gps_location":{"lat":45,"lon":72}},"sort":[2681.678022852833]},{"_index":"shop","_type":"_doc","_id":"5da6f3962c005a000765a171","_score":null,"_source":{"shop_name":"vishnu aruvikkara","city":{"name":"Thiruvananthapuram","state":"kerala","status":"1","latitude":"8.506944","longitude":"76.956944","id":"5d96e3910c737f4ca185a4ca","createdAt":"2019-11-01T05:15:20.912Z","updatedAt":"2019-10-28T06:48:07.617Z"},"primary_mobile":9497572861,"secondary_mobile":9497572861,"status":true,"shop_status":1,"shop_online_status":false,"categories":[{"name":"electronics","tags":["tv","fan","computer"],"status":1,"id":"5d9d7d850f01e2000714926f","createdAt":"2019-10-16T10:38:21.546Z","updatedAt":"2019-10-14T10:45:32.457Z","itemName":"electronics"},{"name":"books","tags":["physics","maths","cs","botony","chemistry"],"status":1,"id":"5d9da8ee37b28800083aa320","createdAt":"2019-10-16T10:38:21.546Z","itemName":"books"},{"name":"Test Log","tags":["1","2","3","4"],"status":1,"id":"5dbc0ef05b0e440008845617","createdAt":"2019-11-01T10:54:40.515Z","updatedAt":"2019-11-01T10:54:40.515Z"},{"name":"New2","tags":["4","dfgdgdgfgdgdfg","dfgdgertetertert","dfgdfgdfgdfgfdg","ertertertertert","wewerwerwerwerwer","eerwererwewre","we","rwer","er"],"status":1,"id":"5db95c3c6acbf6000802ca3a","createdAt":"2019-10-30T09:47:40.725Z","updatedAt":"2019-10-30T10:38:03.736Z"},{"name":"Test","tags":[],"status":1,"id":"5dc2acf2420a3300072162ce","createdAt":"2019-11-06T11:22:26.485Z","updatedAt":"2019-11-06T11:22:26.485Z"},{"name":"Test Log","tags":[],"status":1,"id":"5dc2aa60ad6238000807aacf","createdAt":"2019-11-06T11:11:28.064Z","updatedAt":"2019-11-06T11:11:28.064Z"}],"sub_categories":[{"name":"New 1","category_id":"5db95c3c6acbf6000802ca3a","tags":["testing","fhsdkjfhjd sjdfhjdhsf jdbjdhfjhsdf"],"status":1,"id":"5db95c4d6acbf6000802ca3b","createdAt":"2019-10-30T09:47:57.673Z","updatedAt":"2019-10-30T10:38:22.102Z"}],"customer_rating":0,"gps_location":{"lat":8.55699034289702,"lon":76.88204862177372},"primary_image":"IMG_20191024_1618294713300464299044798.jpg","createdAt":"2019-10-16T10:40:22.704Z","updatedAt":"2019-11-07T08:38:02.501Z","brands":{"name":"Hewlett Packard","id":"5da6fee481c03853dce38109","createdAt":"2019-10-16T11:28:36.659Z","updatedAt":"2019-10-16T11:28:36.660Z"}},"sort":[5118.568036336773]}]}
     * message : Search successful
     * status : success
     * statusCode : 200
     */

    private DataBean data;
    private String message;
    private String status;
    private int statusCode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static class DataBean {
        /**
         * total : {"value":2,"relation":"eq"}
         * max_score : null
         * hits : [{"_index":"shop","_type":"_doc","_id":"5db146afd9176b0007613f2e","_score":null,"_source":{"shop_name":"Test sho","city":{"name":"Nagpur","state":"state","status":"string","latitude":"8.506944","longitude":"76.956944","id":"5da9aeb6c42779000772fdd2","createdAt":"2019-10-18T12:23:18.973Z","updatedAt":"2019-10-18T12:23:18.973Z"},"primary_mobile":9486533865,"secondary_mobile":9486533865,"status":true,"shop_status":1,"shop_online_status":false,"categories":[{"name":"Test Jerin 123","tags":["TAG1","tag1","Tag3"],"status":1,"id":"5db123180b868f0008414820","createdAt":"2019-10-24T04:05:44.366Z","updatedAt":"2019-10-24T04:36:57.045Z"}],"sub_categories":[],"customer_rating":0,"createdAt":"2019-10-24T06:37:35.822Z","updatedAt":"2019-11-07T11:05:00.522Z","primary_image":"wall-of-china_0.jpg","brands":{"name":"Samsung","id":"5da047a15caef700082ff711","createdAt":"2019-10-11T09:12:16.997Z","updatedAt":"2019-10-11T09:13:05.250Z"},"gps_location":{"lat":45,"lon":72}},"sort":[2681.678022852833]},{"_index":"shop","_type":"_doc","_id":"5da6f3962c005a000765a171","_score":null,"_source":{"shop_name":"vishnu aruvikkara","city":{"name":"Thiruvananthapuram","state":"kerala","status":"1","latitude":"8.506944","longitude":"76.956944","id":"5d96e3910c737f4ca185a4ca","createdAt":"2019-11-01T05:15:20.912Z","updatedAt":"2019-10-28T06:48:07.617Z"},"primary_mobile":9497572861,"secondary_mobile":9497572861,"status":true,"shop_status":1,"shop_online_status":false,"categories":[{"name":"electronics","tags":["tv","fan","computer"],"status":1,"id":"5d9d7d850f01e2000714926f","createdAt":"2019-10-16T10:38:21.546Z","updatedAt":"2019-10-14T10:45:32.457Z","itemName":"electronics"},{"name":"books","tags":["physics","maths","cs","botony","chemistry"],"status":1,"id":"5d9da8ee37b28800083aa320","createdAt":"2019-10-16T10:38:21.546Z","itemName":"books"},{"name":"Test Log","tags":["1","2","3","4"],"status":1,"id":"5dbc0ef05b0e440008845617","createdAt":"2019-11-01T10:54:40.515Z","updatedAt":"2019-11-01T10:54:40.515Z"},{"name":"New2","tags":["4","dfgdgdgfgdgdfg","dfgdgertetertert","dfgdfgdfgdfgfdg","ertertertertert","wewerwerwerwerwer","eerwererwewre","we","rwer","er"],"status":1,"id":"5db95c3c6acbf6000802ca3a","createdAt":"2019-10-30T09:47:40.725Z","updatedAt":"2019-10-30T10:38:03.736Z"},{"name":"Test","tags":[],"status":1,"id":"5dc2acf2420a3300072162ce","createdAt":"2019-11-06T11:22:26.485Z","updatedAt":"2019-11-06T11:22:26.485Z"},{"name":"Test Log","tags":[],"status":1,"id":"5dc2aa60ad6238000807aacf","createdAt":"2019-11-06T11:11:28.064Z","updatedAt":"2019-11-06T11:11:28.064Z"}],"sub_categories":[{"name":"New 1","category_id":"5db95c3c6acbf6000802ca3a","tags":["testing","fhsdkjfhjd sjdfhjdhsf jdbjdhfjhsdf"],"status":1,"id":"5db95c4d6acbf6000802ca3b","createdAt":"2019-10-30T09:47:57.673Z","updatedAt":"2019-10-30T10:38:22.102Z"}],"customer_rating":0,"gps_location":{"lat":8.55699034289702,"lon":76.88204862177372},"primary_image":"IMG_20191024_1618294713300464299044798.jpg","createdAt":"2019-10-16T10:40:22.704Z","updatedAt":"2019-11-07T08:38:02.501Z","brands":{"name":"Hewlett Packard","id":"5da6fee481c03853dce38109","createdAt":"2019-10-16T11:28:36.659Z","updatedAt":"2019-10-16T11:28:36.660Z"}},"sort":[5118.568036336773]}]
         */

        private TotalBean total;
        private Object max_score;
        private List<HitsBean> hits;

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public Object getMax_score() {
            return max_score;
        }

        public void setMax_score(Object max_score) {
            this.max_score = max_score;
        }

        public List<HitsBean> getHits() {
            return hits;
        }

        public void setHits(List<HitsBean> hits) {
            this.hits = hits;
        }

        public static class TotalBean {
            /**
             * value : 2
             * relation : eq
             */

            private int value;
            private String relation;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }
        }

        public static class HitsBean {
            /**
             * _index : shop
             * _type : _doc
             * _id : 5db146afd9176b0007613f2e
             * _score : null
             * _source : {"shop_name":"Test sho","city":{"name":"Nagpur","state":"state","status":"string","latitude":"8.506944","longitude":"76.956944","id":"5da9aeb6c42779000772fdd2","createdAt":"2019-10-18T12:23:18.973Z","updatedAt":"2019-10-18T12:23:18.973Z"},"primary_mobile":9486533865,"secondary_mobile":9486533865,"status":true,"shop_status":1,"shop_online_status":false,"categories":[{"name":"Test Jerin 123","tags":["TAG1","tag1","Tag3"],"status":1,"id":"5db123180b868f0008414820","createdAt":"2019-10-24T04:05:44.366Z","updatedAt":"2019-10-24T04:36:57.045Z"}],"sub_categories":[],"customer_rating":0,"createdAt":"2019-10-24T06:37:35.822Z","updatedAt":"2019-11-07T11:05:00.522Z","primary_image":"wall-of-china_0.jpg","brands":{"name":"Samsung","id":"5da047a15caef700082ff711","createdAt":"2019-10-11T09:12:16.997Z","updatedAt":"2019-10-11T09:13:05.250Z"},"gps_location":{"lat":45,"lon":72}}
             * sort : [2681.678022852833]
             */

            private String _index;
            private String _type;
            private String _id;
            private Object _score;
            private SourceBean _source;
            private List<Double> sort;

            public String get_index() {
                return _index;
            }

            public void set_index(String _index) {
                this._index = _index;
            }

            public String get_type() {
                return _type;
            }

            public void set_type(String _type) {
                this._type = _type;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public Object get_score() {
                return _score;
            }

            public void set_score(Object _score) {
                this._score = _score;
            }

            public SourceBean get_source() {
                return _source;
            }

            public void set_source(SourceBean _source) {
                this._source = _source;
            }

            public List<Double> getSort() {
                return sort;
            }

            public void setSort(List<Double> sort) {
                this.sort = sort;
            }

            public static class SourceBean {
                /**
                 * shop_name : Test sho
                 * city : {"name":"Nagpur","state":"state","status":"string","latitude":"8.506944","longitude":"76.956944","id":"5da9aeb6c42779000772fdd2","createdAt":"2019-10-18T12:23:18.973Z","updatedAt":"2019-10-18T12:23:18.973Z"}
                 * primary_mobile : 9486533865
                 * secondary_mobile : 9486533865
                 * status : true
                 * shop_status : 1
                 * shop_online_status : false
                 * categories : [{"name":"Test Jerin 123","tags":["TAG1","tag1","Tag3"],"status":1,"id":"5db123180b868f0008414820","createdAt":"2019-10-24T04:05:44.366Z","updatedAt":"2019-10-24T04:36:57.045Z"}]
                 * sub_categories : []
                 * customer_rating : 0
                 * createdAt : 2019-10-24T06:37:35.822Z
                 * updatedAt : 2019-11-07T11:05:00.522Z
                 * primary_image : wall-of-china_0.jpg
                 * brands : {"name":"Samsung","id":"5da047a15caef700082ff711","createdAt":"2019-10-11T09:12:16.997Z","updatedAt":"2019-10-11T09:13:05.250Z"}
                 * gps_location : {"lat":45,"lon":72}
                 */

                private String shop_name;
                private CityBean city;
                private long primary_mobile;
                private long secondary_mobile;
                private boolean status;
                private int shop_status;
                private boolean shop_online_status;
                private int customer_rating;
                private String createdAt;
                private String updatedAt;
                private String primary_image;
                private int primary_image_status;
                private BrandsBean brands;
                private GpsLocationBean gps_location;
                private List<CategoriesBean> categories;
                private List<?> sub_categories;

                public String getShop_name() {
                    return shop_name;
                }

                public void setShop_name(String shop_name) {
                    this.shop_name = shop_name;
                }

                public CityBean getCity() {
                    return city;
                }

                public void setCity(CityBean city) {
                    this.city = city;
                }

                public long getPrimary_mobile() {
                    return primary_mobile;
                }

                public void setPrimary_mobile(long primary_mobile) {
                    this.primary_mobile = primary_mobile;
                }

                public long getSecondary_mobile() {
                    return secondary_mobile;
                }

                public void setSecondary_mobile(long secondary_mobile) {
                    this.secondary_mobile = secondary_mobile;
                }

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public int getShop_status() {
                    return shop_status;
                }

                public void setShop_status(int shop_status) {
                    this.shop_status = shop_status;
                }

                public boolean isShop_online_status() {
                    return shop_online_status;
                }

                public void setShop_online_status(boolean shop_online_status) {
                    this.shop_online_status = shop_online_status;
                }

                public int getCustomer_rating() {
                    return customer_rating;
                }

                public void setCustomer_rating(int customer_rating) {
                    this.customer_rating = customer_rating;
                }

                public String getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

                public String getUpdatedAt() {
                    return updatedAt;
                }

                public void setUpdatedAt(String updatedAt) {
                    this.updatedAt = updatedAt;
                }

                public String getPrimary_image() {
                    return primary_image;
                }

                public void setPrimary_image(String primary_image) {
                    this.primary_image = primary_image;
                }

                public BrandsBean getBrands() {
                    return brands;
                }

                public void setBrands(BrandsBean brands) {
                    this.brands = brands;
                }

                public GpsLocationBean getGps_location() {
                    return gps_location;
                }

                public void setGps_location(GpsLocationBean gps_location) {
                    this.gps_location = gps_location;
                }

                public List<CategoriesBean> getCategories() {
                    return categories;
                }

                public void setCategories(List<CategoriesBean> categories) {
                    this.categories = categories;
                }

                public List<?> getSub_categories() {
                    return sub_categories;
                }

                public void setSub_categories(List<?> sub_categories) {
                    this.sub_categories = sub_categories;
                }

                public int getPrimary_image_status() {
                    return primary_image_status;
                }

                public void setPrimary_image_status(int primary_image_status) {
                    this.primary_image_status = primary_image_status;
                }

                public static class CityBean {
                    /**
                     * name : Nagpur
                     * state : state
                     * status : string
                     * latitude : 8.506944
                     * longitude : 76.956944
                     * id : 5da9aeb6c42779000772fdd2
                     * createdAt : 2019-10-18T12:23:18.973Z
                     * updatedAt : 2019-10-18T12:23:18.973Z
                     */

                    private String name;
                    private String state;
                    private String status;
                    private String latitude;
                    private String longitude;
                    private String id;
                    private String createdAt;
                    private String updatedAt;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getState() {
                        return state;
                    }

                    public void setState(String state) {
                        this.state = state;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getLatitude() {
                        return latitude;
                    }

                    public void setLatitude(String latitude) {
                        this.latitude = latitude;
                    }

                    public String getLongitude() {
                        return longitude;
                    }

                    public void setLongitude(String longitude) {
                        this.longitude = longitude;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getCreatedAt() {
                        return createdAt;
                    }

                    public void setCreatedAt(String createdAt) {
                        this.createdAt = createdAt;
                    }

                    public String getUpdatedAt() {
                        return updatedAt;
                    }

                    public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                    }
                }

                public static class BrandsBean {
                    /**
                     * name : Samsung
                     * id : 5da047a15caef700082ff711
                     * createdAt : 2019-10-11T09:12:16.997Z
                     * updatedAt : 2019-10-11T09:13:05.250Z
                     */

                    private String name;
                    private String id;
                    private String createdAt;
                    private String updatedAt;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getCreatedAt() {
                        return createdAt;
                    }

                    public void setCreatedAt(String createdAt) {
                        this.createdAt = createdAt;
                    }

                    public String getUpdatedAt() {
                        return updatedAt;
                    }

                    public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                    }
                }

                public static class GpsLocationBean {
                    /**
                     * lat : 45
                     * lon : 72
                     */

                    private double lat;
                    private double lon;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLon() {
                        return lon;
                    }

                    public void setLon(double lon) {
                        this.lon = lon;
                    }
                }

                public static class CategoriesBean {
                    /**
                     * name : Test Jerin 123
                     * tags : ["TAG1","tag1","Tag3"]
                     * status : 1
                     * id : 5db123180b868f0008414820
                     * createdAt : 2019-10-24T04:05:44.366Z
                     * updatedAt : 2019-10-24T04:36:57.045Z
                     */

                    private String name;
                    private int status;
                    private String id;
                    private String createdAt;
                    private String updatedAt;
                    private List<String> tags;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getCreatedAt() {
                        return createdAt;
                    }

                    public void setCreatedAt(String createdAt) {
                        this.createdAt = createdAt;
                    }

                    public String getUpdatedAt() {
                        return updatedAt;
                    }

                    public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                    }

                    public List<String> getTags() {
                        return tags;
                    }

                    public void setTags(List<String> tags) {
                        this.tags = tags;
                    }
                }
            }
        }
    }
}
