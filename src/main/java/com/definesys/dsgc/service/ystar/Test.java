package com.definesys.dsgc.service.ystar;

import com.definesys.dsgc.service.utils.MsgCompressUtil;
import com.definesys.dsgc.service.ystar.report.util.ReportUtils;
import com.definesys.dsgc.service.ystar.utils.FileUtils;
import com.definesys.dsgc.service.ystar.utils.MuleXmlConfUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException, DocumentException, IOException {
//        String str = "update \"C##JAC\".\"PART\" set \"PARTTYPE\" = '15' where \"OID\" = 13220671 and \"OTYPE\" = 'HustCAD.IntePLM4.Access.Part.CTyPart' and \"CREATOR\" = 'esb1' and \"CREATED\" = TIMESTAMP ' 2013-12-27 10:08:46' and \"MODIFIER\" IS NULL and \"UPDATED\" = TIMESTAMP ' 2013-12-27 10:08:46' and \"UPDATECOUNT\" = 0 and \"MARKFORDELETE\" = 0 and \"REMARK\" IS NULL and \"PARTCLASSOID\" = 14 and \"PARTMASTEROID\" = 9922271 and \"PARTMASTEROTYPE\" IS NULL and \"VALIDSTATUS\" = 1 and \"VERSIONINNER\" = '1' and \"VERSIONINFO\" = 'A.1' and \"CNAME\" = '后桥总成' and \"ENAME\" = '2400010LD083-5.571' and \"SECURITYLEVEL\" IS NULL and \"PARTTYPE\" = '15' and \"PRINCIPAL\" = 'admin' and \"ORGANIZATIONOID\" = 0 and \"ORGANIZATIONOTYPE\" IS NULL and \"PRODUCTOID\" = 0 and \"PRODUCTOTYPE\" IS NULL and \"MODULEOID\" = 0 and \"MODULEOTYPE\" IS NULL and \"SPECIAL\" IS NULL and \"PHASE\" IS NULL and \"LFCSTATUS\" IS NULL and \"VARIATION1\" IS NULL and \"VARIATION2\" IS NULL and \"IMPORTANT\" = 0 and \"VIRTUAL\" = 0 and \"CHKSTATUS\" = 0 and \"CHECKOUTUSER\" = '20121770' and \"CHECKOUTDATE\" = TIMESTAMP ' 0001-01-01 12:56:48' and \"CHECKINUSER\" = '20121770' and \"CHECKINDATE\" = TIMESTAMP ' 2016-06-27 11:57:52' and \"BOMTYPE\" IS NULL and \"VERSIONVALIDATE\" = TIMESTAMP ' 2000-01-01 12:56:48' and \"BEGINDATE\" = TIMESTAMP ' 2000-01-01 12:56:48' and \"ENDDATE\" = TIMESTAMP ' 9000-01-01 00:00:00' and \"VERSIONVIEW\" = 1000 and \"VIEWISNULL\" = 0 and \"CLASSNAMEKEYVIEW\" = 'HustCAD.IntePLM4.TyView' and \"VIEWOID\" = 0 and \"VERSIONCONTEXT\" = 0 and \"UPDATE_DATE\" = TIMESTAMP ' 2021-01-08 11:23:14'";
//        str.equals("");
//        System.out.println(str);

//        Date date = new Date(1618911014052L);
        String str = "eJx9UstKw0AU3fcrwqzbZmbIVBNqIZlEDNgmphNFREJoQ7tok9JEUaR7ceUfFATXfpXFz/BmWuvUhZt5nMe9Zy7TLYt0keX3llOMH7WH+SwvrR10gqZVtbB0vRxNs3latoGtqXaxnOj1QQdRNisWmY56Tw1NQ8sq58U4Q5aGhqi5Q/rlpAY26/evj7fN8+vny3rLjYq7vKopsr2P0yqF6w2cNe1JroBGoUhcW3i1kGJKMKMGMVmHUumSGpvzIB4IkJDmX18i/L40E9NiHUu1ndpcBNF1wgNXKkyM8S/ru4CZHePooKRjC36WSA7tkzCsVHW9IXT2wjoOckJC0EGBn2YOVXqFUeDGXPjBILn0oiHsMrFi7cNTIt8+3/sFYS0GgTFR6sQD/yL2dvEOpwVKahwbShj76p+YcnTq3FuYtaiBpGYF621j1dXVz9P7BrN6jl0=";
        str = MsgCompressUtil.deCompress(str);
        System.out.println(str);

        //System.out.println("{NullPayload}");
    }


}
