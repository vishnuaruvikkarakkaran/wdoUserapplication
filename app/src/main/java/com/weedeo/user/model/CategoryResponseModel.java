package com.weedeo.user.model;

import com.weedeo.user.ui.filter.ParentListItem;

import java.util.List;

public class CategoryResponseModel {

    /**
     * data : [{"name":"Men's Fashion","tags":["adad","dadad"],"order":20,"imageUrl":"","id":"5dc928a97483ec0008a9859c","subCategories":[{"name":"1","category_id":"5dc928a97483ec0008a9859c","tags":["adad","dadad"],"id":"5dcd301b1356d0000817952c"},{"name":"2","category_id":"5dc928a97483ec0008a9859c","tags":[],"id":"5dcd301e1356d0000817952d"},{"name":"3","category_id":"5dc928a97483ec0008a9859c","tags":[],"id":"5dcd30211356d0000817952e"}]},{"name":"Women's Fashion","tags":[],"order":20,"imageUrl":"","id":"5dc928c17483ec0008a9859e","subCategories":[{"name":"A1","category_id":"5dc928c17483ec0008a9859e","tags":[],"id":"5dcd30271356d0000817952f"},{"name":"A2","category_id":"5dc928c17483ec0008a9859e","tags":[],"id":"5dcd302a1356d00008179530"},{"name":"A3","category_id":"5dc928c17483ec0008a9859e","tags":[],"id":"5dcd302f1356d00008179531"}]},{"name":"Toys, Kids &babies","tags":[],"order":20,"imageUrl":"","id":"5dc928ca7483ec0008a9859f","subCategories":[{"name":"B1","category_id":"5dc928ca7483ec0008a9859f","tags":[],"id":"5dcd303d1356d00008179532"},{"name":"B2","category_id":"5dc928ca7483ec0008a9859f","tags":[],"id":"5dcd30411356d00008179533"},{"name":"B3","category_id":"5dc928ca7483ec0008a9859f","tags":[],"id":"5dcd30461356d00008179534"}]},{"name":"Health & Wellness","tags":["ghfhhfh"],"order":20,"id":"5dc928d27483ec0008a985a0","subCategories":[{"name":"Ddf","category_id":"5dc928d27483ec0008a985a0","tags":["sdfsf","fghhf"],"id":"5dcb85002b975d00087ab548"},{"name":"Ffdg","category_id":"5dc928d27483ec0008a985a0","tags":["dgdgfdg","ghhfh"],"id":"5dcb85092b975d00087ab549"},{"name":"Dfsffsdfs","category_id":"5dc928d27483ec0008a985a0","tags":["hkyukyukhuk"],"id":"5dcb85162b975d00087ab54a"}]},{"name":"Test Today","tags":["fffffffffffffffffff"],"order":1,"imageUrl":"category_images/sample_1573705011115.jpg","id":"5dccc16e7bf5bb00070a52d8","subCategories":[{"name":"Tett","category_id":"5dccc16e7bf5bb00070a52d8","tags":[],"id":"5dcce044a7d4ac0008183094"},{"name":"Zczxc","category_id":"5dccc16e7bf5bb00070a52d8","tags":["zxcz","adad"],"id":"5dcce090e17a370008385c7b"}]}]
     * message : get category list successfully
     * status : success
     * statusCode : 200
     */

    private String message;
    private String status;
    private int statusCode;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements ParentListItem {
        /**
         * name : Men's Fashion
         * tags : ["adad","dadad"]
         * order : 20
         * imageUrl :
         * id : 5dc928a97483ec0008a9859c
         * subCategories : [{"name":"1","category_id":"5dc928a97483ec0008a9859c","tags":["adad","dadad"],"id":"5dcd301b1356d0000817952c"},{"name":"2","category_id":"5dc928a97483ec0008a9859c","tags":[],"id":"5dcd301e1356d0000817952d"},{"name":"3","category_id":"5dc928a97483ec0008a9859c","tags":[],"id":"5dcd30211356d0000817952e"}]
         */

        private String name;
        private int order;
        private String imageUrl;
        private String id;
        private List<String> tags;
        private List<SubCategoriesBean> subCategories;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<SubCategoriesBean> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(List<SubCategoriesBean> subCategories) {
            this.subCategories = subCategories;
        }

        @Override
        public List<?> getChildItemList() {
            return subCategories;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
        public static class SubCategoriesBean {
            /**
             * name : 1
             * category_id : 5dc928a97483ec0008a9859c
             * tags : ["adad","dadad"]
             * id : 5dcd301b1356d0000817952c
             */

            private String name;
            private String category_id;
            private String id;
            private List<String> tags;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
