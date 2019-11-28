package com.weedeo.user.model;

import java.util.List;

public class UserProfileModel {

    /**
     * data : {"name":"athul","mobile":"+917012181745","email":"athul@yopmail.com","role":4,"online_status":false,"block_status":false,"gender":"male","profile_pic":"default.jpg","fcm_uid":"l6ktW9DZAXX6y8iTffDriZn6zKn1","email_verified":false,"id":"5da41692605227000724b902","createdAt":"2019-10-14T06:32:50.960Z","updatedAt":"2019-10-22T11:07:45.492Z","address":[{"name":"cjcjcjcjcjf","pincode":"868686","address_line1":"fjfjfjfjcjfjckc","address_line2":"fjfjdjfjfjfjfj","mobile_number":"6890906868","city":{"createdAt":"2019-10-22T07:00:36.344Z","id":"5d96e3d60c737f4ca185a4cb","latitude":"17.375278","longitude":"78.474444","name":"hyderabad","state":"maharashtra","status":"1"},"state":"Maharashtra","land_mark":"cjcjcjcjcd","secondary_number":"9898986868","user_id":"5da41692605227000724b902","primary_status":true,"status":false,"id":"5daea9232989630007b9c768","createdAt":"2019-10-22T07:00:51.438Z","updatedAt":"2019-10-22T10:08:36.903Z"}]}
     * message : default loopback api
     * status : success
     * statusCode : 200
     */

    private DataBean data;
    private String message;
    private String status;
    private String statusCode;

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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static class DataBean {
        /**
         * name : athul
         * mobile : +917012181745
         * email : athul@yopmail.com
         * dob : 14/04/1991
         * role : 4
         * online_status : false
         * block_status : false
         * gender : male
         * profile_pic : default.jpg
         * fcm_uid : l6ktW9DZAXX6y8iTffDriZn6zKn1
         * email_verified : false
         * profile_completion : 50
         * id : 5da41692605227000724b902
         * createdAt : 2019-10-14T06:32:50.960Z
         * updatedAt : 2019-10-22T11:07:45.492Z
         * address : [{"name":"cjcjcjcjcjf","pincode":"868686","address_line1":"fjfjfjfjcjfjckc","address_line2":"fjfjdjfjfjfjfj","mobile_number":"6890906868","city":{"createdAt":"2019-10-22T07:00:36.344Z","id":"5d96e3d60c737f4ca185a4cb","latitude":"17.375278","longitude":"78.474444","name":"hyderabad","state":"maharashtra","status":"1"},"state":"Maharashtra","land_mark":"cjcjcjcjcd","secondary_number":"9898986868","user_id":"5da41692605227000724b902","primary_status":true,"status":false,"id":"5daea9232989630007b9c768","createdAt":"2019-10-22T07:00:51.438Z","updatedAt":"2019-10-22T10:08:36.903Z"}]
         */

        private String name;
        private String mobile;
        private String email;
        private String dob;
        private int role;
        private int profile_completion;
        private boolean online_status;
        private boolean block_status;
        private String gender;
        private String profile_pic;
        private String fcm_uid;
        private boolean email_verified;
        private String id;
        private String createdAt;
        private String updatedAt;
        private List<AddressBean> address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public boolean isOnline_status() {
            return online_status;
        }

        public void setOnline_status(boolean online_status) {
            this.online_status = online_status;
        }

        public boolean isBlock_status() {
            return block_status;
        }

        public void setBlock_status(boolean block_status) {
            this.block_status = block_status;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getFcm_uid() {
            return fcm_uid;
        }

        public void setFcm_uid(String fcm_uid) {
            this.fcm_uid = fcm_uid;
        }

        public boolean isEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(boolean email_verified) {
            this.email_verified = email_verified;
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

        public List<AddressBean> getAddress() {
            return address;
        }

        public void setAddress(List<AddressBean> address) {
            this.address = address;
        }

        public int getProfile_completion() {
            return profile_completion;
        }

        public void setProfile_completion(int profile_completion) {
            this.profile_completion = profile_completion;
        }

        public static class AddressBean {
            /**
             * name : cjcjcjcjcjf
             * pincode : 868686
             * address_line1 : fjfjfjfjcjfjckc
             * address_line2 : fjfjdjfjfjfjfj
             * mobile_number : 6890906868
             * city : {"createdAt":"2019-10-22T07:00:36.344Z","id":"5d96e3d60c737f4ca185a4cb","latitude":"17.375278","longitude":"78.474444","name":"hyderabad","state":"maharashtra","status":"1"}
             * state : Maharashtra
             * land_mark : cjcjcjcjcd
             * secondary_number : 9898986868
             * user_id : 5da41692605227000724b902
             * primary_status : true
             * status : false
             * id : 5daea9232989630007b9c768
             * createdAt : 2019-10-22T07:00:51.438Z
             * updatedAt : 2019-10-22T10:08:36.903Z
             */

            private String name;
            private String pincode;
            private String address_line1;
            private String address_line2;
            private String mobile_number;
            private CityBean city;
            private String state;
            private String land_mark;
            private String secondary_number;
            private String user_id;
            private boolean primary_status;
            private boolean status;
            private String id;
            private String createdAt;
            private String updatedAt;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }

            public String getAddress_line1() {
                return address_line1;
            }

            public void setAddress_line1(String address_line1) {
                this.address_line1 = address_line1;
            }

            public String getAddress_line2() {
                return address_line2;
            }

            public void setAddress_line2(String address_line2) {
                this.address_line2 = address_line2;
            }

            public String getMobile_number() {
                return mobile_number;
            }

            public void setMobile_number(String mobile_number) {
                this.mobile_number = mobile_number;
            }

            public CityBean getCity() {
                return city;
            }

            public void setCity(CityBean city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getLand_mark() {
                return land_mark;
            }

            public void setLand_mark(String land_mark) {
                this.land_mark = land_mark;
            }

            public String getSecondary_number() {
                return secondary_number;
            }

            public void setSecondary_number(String secondary_number) {
                this.secondary_number = secondary_number;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public boolean isPrimary_status() {
                return primary_status;
            }

            public void setPrimary_status(boolean primary_status) {
                this.primary_status = primary_status;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
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

            public static class CityBean {
                /**
                 * createdAt : 2019-10-22T07:00:36.344Z
                 * id : 5d96e3d60c737f4ca185a4cb
                 * latitude : 17.375278
                 * longitude : 78.474444
                 * name : hyderabad
                 * state : maharashtra
                 * status : 1
                 */

                private String createdAt;
                private String id;
                private String latitude;
                private String longitude;
                private String name;
                private String state;
                private String status;

                public String getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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
            }
        }
    }
}
