package com.weedeo.user.model;

public class FcmPushRequestModel {

    /**
     * to : dyT2SDIyt2s:APA91bGnIKO3NP7NiJgBvkGUiDOllhPs3YDc2qrDOA705jaRuJ39lM9p5N0W7nz5DBSGyNDeh-pUec-aQHlolv0xjj8glTmUVreN0Ha02mqR2XJrEghikZZ6zQT-fO_ts9tdN6frcXHN
     * priority : high
     * time_to_live : 0
     * data : {"token":"006e9083a06af214c29a75562b62e1b1833IAC5llXhCWtklUm9NHyRLjkiJbEjVdZN+5ExIjxABJrQ3K8ejsYh39v0IgCeqbj5E8vDXQQAAQAAAAAAAgAAAAAAAwAAAAAABAAAAAAA","channel":"dGQS97GDzfZQHqoN5h2TWcc0aM5vDRdjYkUBcUXyL3LxuMdWdfuvcsXhM2erXgnQ"}
     */

    private String to;
    private String priority;
    private int time_to_live;
    private DataBean data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getTime_to_live() {
        return time_to_live;
    }

    public void setTime_to_live(int time_to_live) {
        this.time_to_live = time_to_live;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : 006e9083a06af214c29a75562b62e1b1833IAC5llXhCWtklUm9NHyRLjkiJbEjVdZN+5ExIjxABJrQ3K8ejsYh39v0IgCeqbj5E8vDXQQAAQAAAAAAAgAAAAAAAwAAAAAABAAAAAAA
         * channel : dGQS97GDzfZQHqoN5h2TWcc0aM5vDRdjYkUBcUXyL3LxuMdWdfuvcsXhM2erXgnQ
         */

        private String token;
        private String channel;
        private String user_name;
        private String user_Image;
        private boolean is_call_active;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_Image() {
            return user_Image;
        }

        public void setUser_Image(String user_Image) {
            this.user_Image = user_Image;
        }

        public boolean isIs_call_active() {
            return is_call_active;
        }

        public void setIs_call_active(boolean is_call_active) {
            this.is_call_active = is_call_active;
        }
    }
}
