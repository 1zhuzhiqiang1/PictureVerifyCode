package com.zzq.pictureverifycode.entry;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class Top_upCommitInfo {

    private List<Top_upCommitBean> orderDetails;

    public OrderForm getOrderForm() {
        return orderForm;
    }

    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }

    public List<Top_upCommitBean> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Top_upCommitBean> orderDetails) {
        this.orderDetails = orderDetails;
    }

    private OrderForm orderForm;


    static class OrderForm {
        private String ChannelName;
        private String MemberID;
        private String MemberPhone;

        public String getChannelName() {
            return ChannelName;
        }

        public void setChannelName(String channelName) {
            ChannelName = channelName;
        }

        public String getMemberID() {
            return MemberID;
        }

        public void setMemberID(String memberID) {
            MemberID = memberID;
        }

        public String getMemberPhone() {
            return MemberPhone;
        }

        public void setMemberPhone(String memberPhone) {
            MemberPhone = memberPhone;
        }


    }

}
