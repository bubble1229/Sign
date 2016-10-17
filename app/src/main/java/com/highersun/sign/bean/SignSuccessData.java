package com.highersun.sign.bean;

/**
 * Author: ShenDanLai on 2016/9/14.
 * Email: 17721129316@163.com
 */

public class SignSuccessData extends BaseDataBean {
    private SignSuccess data;

    public SignSuccess getData() {
        return data;
    }

    public void setData(SignSuccess data) {
        this.data = data;
    }

    public class SignSuccess {
        String signIn;
        String date;
        String signOut;

        public String getSignIn() {
            return signIn;
        }

        public void setSignIn(String signIn) {
            this.signIn = signIn;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSignOut() {
            return signOut;
        }

        public void setSignOut(String signOut) {
            this.signOut = signOut;
        }
    }
}
