package com.highersun.sign.bean;

/**
 * Created by admin on 2016/8/15.
 */

public class LoginDataBean extends BaseDataBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   public class Data{
        @Override
        public String toString() {
            return "Data{" +
                    "userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
        /**
         * userId : 1
         * name : 1111
         */

        private String userId;
        private String name;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return "LoginDataBean{" +
                "data=" + data +
                '}';
    }
}
