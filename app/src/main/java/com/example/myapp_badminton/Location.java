//package com.example.myapp_badminton;
//
//import java.util.List;
//
//public class Location {
//    @XmlRootElement(name = "Details")
//    @XmlType(propOrder = {"detailA", "detailB"})
//    private class Details {
//        private List<String> detailA;
//        private List<String> detailB;
//
//        public void setDetailA(List<String> detailA) {
//            this.detailA = detailA;
//        }
//
//        @XmlElementWrapper(name = "detail-a")
//        @XmlElement(name = "detail")
//        public List<String> getDetailA() {
//            return detailA;
//        }
//
//        public void setDetailB(List<String> detailB) {
//            this.detailB = detailB;
//        }
//
//        @XmlElementWrapper(name = "detail-b")
//        @XmlElement(name = "detail")
//        public List<String> getDetailB() {
//            return detailB;
//        }
//    }
//
//}
