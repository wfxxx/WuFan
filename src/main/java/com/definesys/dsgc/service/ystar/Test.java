package com.definesys.dsgc.service.ystar;


import com.definesys.dsgc.service.ystar.svcgen.util.WsdlUtil;


public class Test {

    public static void main(String[] args) {
//        String str = "update \"C##JAC\".\"PART\" set \"PARTTYPE\" = '15' where \"OID\" = 13220671 and \"OTYPE\" = 'HustCAD.IntePLM4.Access.Part.CTyPart' and \"CREATOR\" = 'esb1' and \"CREATED\" = TIMESTAMP ' 2013-12-27 10:08:46' and \"MODIFIER\" IS NULL and \"UPDATED\" = TIMESTAMP ' 2013-12-27 10:08:46' and \"UPDATECOUNT\" = 0 and \"MARKFORDELETE\" = 0 and \"REMARK\" IS NULL and \"PARTCLASSOID\" = 14 and \"PARTMASTEROID\" = 9922271 and \"PARTMASTEROTYPE\" IS NULL and \"VALIDSTATUS\" = 1 and \"VERSIONINNER\" = '1' and \"VERSIONINFO\" = 'A.1' and \"CNAME\" = '后桥总成' and \"ENAME\" = '2400010LD083-5.571' and \"SECURITYLEVEL\" IS NULL and \"PARTTYPE\" = '15' and \"PRINCIPAL\" = 'admin' and \"ORGANIZATIONOID\" = 0 and \"ORGANIZATIONOTYPE\" IS NULL and \"PRODUCTOID\" = 0 and \"PRODUCTOTYPE\" IS NULL and \"MODULEOID\" = 0 and \"MODULEOTYPE\" IS NULL and \"SPECIAL\" IS NULL and \"PHASE\" IS NULL and \"LFCSTATUS\" IS NULL and \"VARIATION1\" IS NULL and \"VARIATION2\" IS NULL and \"IMPORTANT\" = 0 and \"VIRTUAL\" = 0 and \"CHKSTATUS\" = 0 and \"CHECKOUTUSER\" = '20121770' and \"CHECKOUTDATE\" = TIMESTAMP ' 0001-01-01 12:56:48' and \"CHECKINUSER\" = '20121770' and \"CHECKINDATE\" = TIMESTAMP ' 2016-06-27 11:57:52' and \"BOMTYPE\" IS NULL and \"VERSIONVALIDATE\" = TIMESTAMP ' 2000-01-01 12:56:48' and \"BEGINDATE\" = TIMESTAMP ' 2000-01-01 12:56:48' and \"ENDDATE\" = TIMESTAMP ' 9000-01-01 00:00:00' and \"VERSIONVIEW\" = 1000 and \"VIEWISNULL\" = 0 and \"CLASSNAMEKEYVIEW\" = 'HustCAD.IntePLM4.TyView' and \"VIEWOID\" = 0 and \"VERSIONCONTEXT\" = 0 and \"UPDATE_DATE\" = TIMESTAMP ' 2021-01-08 11:23:14'";
//        str.equals("");
//        System.out.println(str);

//        Date date = new Date(1618911014052L);
        String str = "MWZmOXBwYmlqamJwazA5Yil/P9+EsGGUPF6FkxXi4EX6bDUMjuhZa1vzlLhbnHRAh4Gk05eRmpItjRQnDUD2ndlaR7oq7ipYTX2Txr5kKlSvs7F5sEdg7PdRyufbGjSQ";
//        str = MsgCompressUtil.deCompress(str);
        //System.out.println(str);

        //System.out.println("{NullPayload}");

//        str = "\n" +
//                "AH20004$000000000007000000$炭罐电磁阀出气管总成$1.000$9000$\n" +
//                "AH11002$155000600AA$炭罐电磁阀出气管总成$3.000$9000$\n" +
//                "AH11003$155000600AA$炭罐电磁阀出气管总成$226.000$9000$\n" +
//                "<>";
//        String count = str.split("\\$\n").length + "";
//        System.out.println(count);
//        str = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
//        WsdlUtil.resolveWsdl2(str);

        str = "https://esbdev.zacmotor.com/YStar/Soap/QueryUser?wsdl";
//        WsdlUtil.resolveWsdl2(str);

        //str="https://ipaas-dev.gacmotor.com/YStar/Test?wsdl";
//        WsdlUtil.resolveWsdl2(str);
//        System.out.println(WsdlUtil.resolveWsdlByUrl(str,null,null));
    }


}
