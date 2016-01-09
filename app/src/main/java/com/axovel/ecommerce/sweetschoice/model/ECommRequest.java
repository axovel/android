package com.axovel.ecommerce.sweetschoice.model;

/**
 * Created by Umesh Chauhan on 04-01-2016.
 * Axovel Private Limited
 */
public class ECommRequest {
        private String timestamp;
        private RequestResponse parameter;

        /**
         * @return the timestamp
         */
        public String getTimestamp() {
            return timestamp;
        }
        /**
         * @param timestamp the timestamp to set
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        /**
         * @return the parameter
         */
        public RequestResponse getParameter() {
            return parameter;
        }
        /**
         * @param parameter the parameter to set
         */
        public void setParameter(RequestResponse parameter) {
            this.parameter = parameter;
        }
}
