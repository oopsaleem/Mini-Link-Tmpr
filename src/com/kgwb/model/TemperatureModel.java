package com.kgwb.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TemperatureModel {
    private SimpleStringProperty siteId;
    private SimpleStringProperty ipAddress;
    private SimpleStringProperty comment;

    private SimpleIntegerProperty temp00;
    private SimpleIntegerProperty temp01;
    private SimpleIntegerProperty temp02;
    private SimpleIntegerProperty temp03;
    private SimpleIntegerProperty temp04;
    private SimpleIntegerProperty temp05;
    private SimpleIntegerProperty temp06;
    private SimpleIntegerProperty temp07;
    private SimpleIntegerProperty temp08;
    private SimpleIntegerProperty temp09;
    private SimpleIntegerProperty temp10;
    private SimpleIntegerProperty temp11;
    private SimpleIntegerProperty temp12;
    private SimpleIntegerProperty temp13;
    private SimpleIntegerProperty temp14;
    private SimpleIntegerProperty temp15;
    private SimpleIntegerProperty temp16;
    private SimpleIntegerProperty temp17;
    private SimpleIntegerProperty temp18;
    private SimpleIntegerProperty temp19;
    private SimpleIntegerProperty temp20;
    private SimpleIntegerProperty temp21;

    private SimpleIntegerProperty high00;
    private SimpleIntegerProperty high01;
    private SimpleIntegerProperty high02;
    private SimpleIntegerProperty high03;
    private SimpleIntegerProperty high04;
    private SimpleIntegerProperty high05;
    private SimpleIntegerProperty high06;
    private SimpleIntegerProperty high07;
    private SimpleIntegerProperty high08;
    private SimpleIntegerProperty high09;
    private SimpleIntegerProperty high10;
    private SimpleIntegerProperty high11;
    private SimpleIntegerProperty high12;
    private SimpleIntegerProperty high13;
    private SimpleIntegerProperty high14;
    private SimpleIntegerProperty high15;
    private SimpleIntegerProperty high16;
    private SimpleIntegerProperty high17;
    private SimpleIntegerProperty high18;
    private SimpleIntegerProperty high19;
    private SimpleIntegerProperty high20;
    private SimpleIntegerProperty high21;

    private SimpleIntegerProperty exce00;
    private SimpleIntegerProperty exce01;
    private SimpleIntegerProperty exce02;
    private SimpleIntegerProperty exce03;
    private SimpleIntegerProperty exce04;
    private SimpleIntegerProperty exce05;
    private SimpleIntegerProperty exce06;
    private SimpleIntegerProperty exce07;
    private SimpleIntegerProperty exce08;
    private SimpleIntegerProperty exce09;
    private SimpleIntegerProperty exce10;
    private SimpleIntegerProperty exce11;
    private SimpleIntegerProperty exce12;
    private SimpleIntegerProperty exce13;
    private SimpleIntegerProperty exce14;
    private SimpleIntegerProperty exce15;
    private SimpleIntegerProperty exce16;
    private SimpleIntegerProperty exce17;
    private SimpleIntegerProperty exce18;
    private SimpleIntegerProperty exce19;
    private SimpleIntegerProperty exce20;
    private SimpleIntegerProperty exce21;

    private int slotMax;


    public TemperatureModel() {
    }

    public TemperatureModel(MiniLinkDeviceTmprWrapper data) {

        this.siteId = new SimpleStringProperty(data.getSiteId());
        this.ipAddress = new SimpleStringProperty(data.getIpAddress());
        this.comment = new SimpleStringProperty(data.getComment());

        slotMax = 0;
        if (getComment().isEmpty())
            for (SlotTmprWrapper entry : data.getSlotsTmpr()) {
                slotMax = entry.getSlot() > slotMax ? entry.getSlot() : slotMax;
                switch (entry.getSlot()) {
                    case 0:
                        this.temp00 = new SimpleIntegerProperty(entry.getTemp());
                        this.high00 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce00 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 1:
                        this.temp01 = new SimpleIntegerProperty(entry.getTemp());
                        this.high01 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce01 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 2:
                        this.temp02 = new SimpleIntegerProperty(entry.getTemp());
                        this.high02 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce02 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 3:
                        this.temp03 = new SimpleIntegerProperty(entry.getTemp());
                        this.high03 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce03 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 4:
                        this.temp04 = new SimpleIntegerProperty(entry.getTemp());
                        this.high04 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce04 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 5:
                        this.temp05 = new SimpleIntegerProperty(entry.getTemp());
                        this.high05 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce05 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 6:
                        this.temp06 = new SimpleIntegerProperty(entry.getTemp());
                        this.high06 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce06 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 7:
                        this.temp07 = new SimpleIntegerProperty(entry.getTemp());
                        this.high07 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce07 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 8:
                        this.temp08 = new SimpleIntegerProperty(entry.getTemp());
                        this.high08 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce08 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 9:
                        this.temp09 = new SimpleIntegerProperty(entry.getTemp());
                        this.high09 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce09 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 10:
                        this.temp10 = new SimpleIntegerProperty(entry.getTemp());
                        this.high10 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce10 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 11:
                        this.temp11 = new SimpleIntegerProperty(entry.getTemp());
                        this.high11 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce11 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 12:
                        this.temp12 = new SimpleIntegerProperty(entry.getTemp());
                        this.high12 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce12 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 13:
                        this.temp13 = new SimpleIntegerProperty(entry.getTemp());
                        this.high13 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce13 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 14:
                        this.temp14 = new SimpleIntegerProperty(entry.getTemp());
                        this.high14 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce14 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 15:
                        this.temp15 = new SimpleIntegerProperty(entry.getTemp());
                        this.high15 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce15 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 16:
                        this.temp16 = new SimpleIntegerProperty(entry.getTemp());
                        this.high16 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce16 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 17:
                        this.temp17 = new SimpleIntegerProperty(entry.getTemp());
                        this.high17 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce17 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 18:
                        this.temp18 = new SimpleIntegerProperty(entry.getTemp());
                        this.high18 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce18 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 19:
                        this.temp19 = new SimpleIntegerProperty(entry.getTemp());
                        this.high19 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce19 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 20:
                        this.temp20 = new SimpleIntegerProperty(entry.getTemp());
                        this.high20 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce20 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    case 21:
                        this.temp21 = new SimpleIntegerProperty(entry.getTemp());
                        this.high21 = new SimpleIntegerProperty(entry.getHigh());
                        this.exce21 = new SimpleIntegerProperty(entry.getExce());
                        break;
                    default:
                        System.out.println("Unexpected Extra columns=" + data.getSlotsTmpr().size());
                }
            }
    }

    public String getSiteId() {
        return siteId.get();
    }

    public SimpleStringProperty siteIdProperty() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId.set(siteId);
    }

    public String getIpAddress() {
        return ipAddress.get();
    }

    public SimpleStringProperty ipAddressProperty() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress.set(ipAddress);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public int getSlotMax() {
        return slotMax;
    }

    //Slot00
    public int getTemp00() {
        return temp00.get();
    }

    public SimpleIntegerProperty temp00Property() {
        return temp00;
    }

    public void setTemp00(int temp00) {
        this.temp00.set(temp00);
    }

    public int getHigh00() {
        return high00.get();
    }

    public SimpleIntegerProperty high00Property() {
        return high00;
    }

    public void setHigh00(int high00) {
        this.high00.set(high00);
    }

    public int getExce00() {
        return exce00.get();
    }

    public SimpleIntegerProperty exce00Property() {
        return exce00;
    }

    public void setExce00(int exce00) {
        this.exce00.set(exce00);
    }

    //Slot01
    public int getTemp01() {
        return temp01.get();
    }

    public SimpleIntegerProperty temp01Property() {
        return temp01;
    }

    public void setTemp01(int temp01) {
        this.temp01.set(temp01);
    }

    public int getHigh01() {
        return high01.get();
    }

    public SimpleIntegerProperty high01Property() {
        return high01;
    }

    public void setHigh01(int high01) {
        this.high01.set(high01);
    }

    public int getExce01() {
        return exce01.get();
    }

    public SimpleIntegerProperty exce01Property() {
        return exce01;
    }

    public void setExce01(int exce01) {
        this.exce01.set(exce01);
    }

    //Slot02
    public int getTemp02() {
        return temp02.get();
    }

    public SimpleIntegerProperty temp02Property() {
        return temp02;
    }

    public void setTemp02(int temp02) {
        this.temp02.set(temp02);
    }

    public int getHigh02() {
        return high02.get();
    }

    public SimpleIntegerProperty high02Property() {
        return high02;
    }

    public void setHigh02(int high02) {
        this.high02.set(high02);
    }

    public int getExce02() {
        return exce02.get();
    }

    public SimpleIntegerProperty exce02Property() {
        return exce02;
    }

    public void setExce02(int exce02) {
        this.exce02.set(exce02);
    }

    //Slot03
    public int getTemp03() {
        return temp03.get();
    }

    public SimpleIntegerProperty temp03Property() {
        return temp03;
    }

    public void setTemp03(int temp03) {
        this.temp03.set(temp03);
    }

    public int getHigh03() {
        return high03.get();
    }

    public SimpleIntegerProperty high03Property() {
        return high03;
    }

    public void setHigh03(int high03) {
        this.high03.set(high03);
    }

    public int getExce03() {
        return exce03.get();
    }

    public SimpleIntegerProperty exce03Property() {
        return exce03;
    }

    public void setExce03(int exce03) {
        this.exce03.set(exce03);
    }

    //Slot04
    public int getTemp04() {
        return temp04.get();
    }

    public SimpleIntegerProperty temp04Property() {
        return temp04;
    }

    public void setTemp04(int temp04) {
        this.temp04.set(temp04);
    }

    public int getHigh04() {
        return high04.get();
    }

    public SimpleIntegerProperty high04Property() {
        return high04;
    }

    public void setHigh04(int high04) {
        this.high04.set(high04);
    }

    public int getExce04() {
        return exce04.get();
    }

    public SimpleIntegerProperty exce04Property() {
        return exce04;
    }

    public void setExce04(int exce04) {
        this.exce04.set(exce04);
    }

    //Slot05
    public int getTemp05() {
        return temp05.get();
    }

    public SimpleIntegerProperty temp05Property() {
        return temp05;
    }

    public void setTemp05(int temp05) {
        this.temp05.set(temp05);
    }

    public int getHigh05() {
        return high05.get();
    }

    public SimpleIntegerProperty high05Property() {
        return high05;
    }

    public void setHigh05(int high05) {
        this.high05.set(high05);
    }

    public int getExce05() {
        return exce05.get();
    }

    public SimpleIntegerProperty exce05Property() {
        return exce05;
    }

    public void setExce05(int exce05) {
        this.exce05.set(exce05);
    }

    //Slot06
    public int getTemp06() {
        return temp06.get();
    }

    public SimpleIntegerProperty temp06Property() {
        return temp06;
    }

    public void setTemp06(int temp06) {
        this.temp06.set(temp06);
    }

    public int getHigh06() {
        return high06.get();
    }

    public SimpleIntegerProperty high06Property() {
        return high06;
    }

    public void setHigh06(int high06) {
        this.high06.set(high06);
    }

    public int getExce06() {
        return exce06.get();
    }

    public SimpleIntegerProperty exce06Property() {
        return exce06;
    }

    public void setExce06(int exce06) {
        this.exce06.set(exce06);
    }

    //Slot07
    public int getTemp07() {
        return temp07.get();
    }

    public SimpleIntegerProperty temp07Property() {
        return temp07;
    }

    public void setTemp07(int temp07) {
        this.temp07.set(temp07);
    }

    public int getHigh07() {
        return high07.get();
    }

    public SimpleIntegerProperty high07Property() {
        return high07;
    }

    public void setHigh07(int high07) {
        this.high07.set(high07);
    }

    public int getExce07() {
        return exce07.get();
    }

    public SimpleIntegerProperty exce07Property() {
        return exce07;
    }

    public void setExce07(int exce07) {
        this.exce07.set(exce07);
    }

    //Slot08
    public int getTemp08() {
        return temp08.get();
    }

    public SimpleIntegerProperty temp08Property() {
        return temp08;
    }

    public void setTemp08(int temp08) {
        this.temp08.set(temp08);
    }

    public int getHigh08() {
        return high08.get();
    }

    public SimpleIntegerProperty high08Property() {
        return high08;
    }

    public void setHigh08(int high08) {
        this.high08.set(high08);
    }

    public int getExce08() {
        return exce08.get();
    }

    public SimpleIntegerProperty exce08Property() {
        return exce08;
    }

    public void setExce08(int exce08) {
        this.exce08.set(exce08);
    }

    //Slot09
    public int getTemp09() {
        return temp09.get();
    }

    public SimpleIntegerProperty temp09Property() {
        return temp09;
    }

    public void setTemp09(int temp09) {
        this.temp09.set(temp09);
    }

    public int getHigh09() {
        return high09.get();
    }

    public SimpleIntegerProperty high09Property() {
        return high09;
    }

    public void setHigh09(int high09) {
        this.high09.set(high09);
    }

    public int getExce09() {
        return exce09.get();
    }

    public SimpleIntegerProperty exce09Property() {
        return exce09;
    }

    public void setExce09(int exce09) {
        this.exce09.set(exce09);
    }

    //Slot10
    public int getTemp10() {
        return temp10.get();
    }

    public SimpleIntegerProperty temp10Property() {
        return temp10;
    }

    public void setTemp10(int temp10) {
        this.temp10.set(temp10);
    }

    public int getHigh10() {
        return high10.get();
    }

    public SimpleIntegerProperty high10Property() {
        return high10;
    }

    public void setHigh10(int high10) {
        this.high10.set(high10);
    }

    public int getExce10() {
        return exce10.get();
    }

    public SimpleIntegerProperty exce10Property() {
        return exce10;
    }

    public void setExce10(int exce10) {
        this.exce10.set(exce10);
    }

    //Slot11
    public int getTemp11() {
        return temp11.get();
    }

    public SimpleIntegerProperty temp11Property() {
        return temp11;
    }

    public void setTemp11(int temp11) {
        this.temp11.set(temp11);
    }

    public int getHigh11() {
        return high11.get();
    }

    public SimpleIntegerProperty high11Property() {
        return high11;
    }

    public void setHigh11(int high11) {
        this.high11.set(high11);
    }

    public int getExce11() {
        return exce11.get();
    }

    public SimpleIntegerProperty exce11Property() {
        return exce11;
    }

    public void setExce11(int exce11) {
        this.exce11.set(exce11);
    }

    //Slot12
    public int getTemp12() {
        return temp12.get();
    }

    public SimpleIntegerProperty temp12Property() {
        return temp12;
    }

    public void setTemp12(int temp12) {
        this.temp12.set(temp12);
    }

    public int getHigh12() {
        return high12.get();
    }

    public SimpleIntegerProperty high12Property() {
        return high12;
    }

    public void setHigh12(int high12) {
        this.high12.set(high12);
    }

    public int getExce12() {
        return exce12.get();
    }

    public SimpleIntegerProperty exce12Property() {
        return exce12;
    }

    public void setExce12(int exce12) {
        this.exce12.set(exce12);
    }

    //Slot13
    public int getTemp13() {
        return temp13.get();
    }

    public SimpleIntegerProperty temp13Property() {
        return temp13;
    }

    public void setTemp13(int temp13) {
        this.temp13.set(temp13);
    }

    public int getHigh13() {
        return high13.get();
    }

    public SimpleIntegerProperty high13Property() {
        return high13;
    }

    public void setHigh13(int high13) {
        this.high13.set(high13);
    }

    public int getExce13() {
        return exce13.get();
    }

    public SimpleIntegerProperty exce13Property() {
        return exce13;
    }

    public void setExce13(int exce13) {
        this.exce13.set(exce13);
    }

    //Slot14
    public int getTemp14() {
        return temp14.get();
    }

    public SimpleIntegerProperty temp14Property() {
        return temp14;
    }

    public void setTemp14(int temp14) {
        this.temp14.set(temp14);
    }

    public int getHigh14() {
        return high14.get();
    }

    public SimpleIntegerProperty high14Property() {
        return high14;
    }

    public void setHigh14(int high14) {
        this.high14.set(high14);
    }

    public int getExce14() {
        return exce14.get();
    }

    public SimpleIntegerProperty exce14Property() {
        return exce14;
    }

    public void setExce14(int exce14) {
        this.exce14.set(exce14);
    }

    //Slot15
    public int getTemp15() {
        return temp15.get();
    }

    public SimpleIntegerProperty temp15Property() {
        return temp15;
    }

    public void setTemp15(int temp15) {
        this.temp15.set(temp15);
    }

    public int getHigh15() {
        return high15.get();
    }

    public SimpleIntegerProperty high15Property() {
        return high15;
    }

    public void setHigh15(int high15) {
        this.high15.set(high15);
    }

    public int getExce15() {
        return exce15.get();
    }

    public SimpleIntegerProperty exce15Property() {
        return exce15;
    }

    public void setExce15(int exce15) {
        this.exce15.set(exce15);
    }

    //Slot16
    public int getTemp16() {
        return temp16.get();
    }

    public SimpleIntegerProperty temp16Property() {
        return temp16;
    }

    public void setTemp16(int temp16) {
        this.temp16.set(temp16);
    }

    public int getHigh16() {
        return high16.get();
    }

    public SimpleIntegerProperty high16Property() {
        return high16;
    }

    public void setHigh16(int high16) {
        this.high16.set(high16);
    }

    public int getExce16() {
        return exce16.get();
    }

    public SimpleIntegerProperty exce16Property() {
        return exce16;
    }

    public void setExce16(int exce16) {
        this.exce16.set(exce16);
    }


    //Slot17
    public int getTemp17() {
        return temp17.get();
    }

    public SimpleIntegerProperty temp17Property() {
        return temp17;
    }

    public void setTemp17(int temp17) {
        this.temp17.set(temp17);
    }

    public int getHigh17() {
        return high17.get();
    }

    public SimpleIntegerProperty high17Property() {
        return high17;
    }

    public void setHigh17(int high17) {
        this.high17.set(high17);
    }

    public int getExce17() {
        return exce17.get();
    }

    public SimpleIntegerProperty exce17Property() {
        return exce17;
    }

    public void setExce17(int exce17) {
        this.exce17.set(exce17);
    }

    //Slot18
    public int getTemp18() {
        return temp18.get();
    }

    public SimpleIntegerProperty temp18Property() {
        return temp18;
    }

    public void setTemp18(int temp18) {
        this.temp18.set(temp18);
    }

    public int getHigh18() {
        return high18.get();
    }

    public SimpleIntegerProperty high18Property() {
        return high18;
    }

    public void setHigh18(int high18) {
        this.high18.set(high18);
    }

    public int getExce18() {
        return exce18.get();
    }

    public SimpleIntegerProperty exce18Property() {
        return exce18;
    }

    public void setExce18(int exce18) {
        this.exce18.set(exce18);
    }


    //Slot19
    public int getTemp19() {
        return temp19.get();
    }

    public SimpleIntegerProperty temp19Property() {
        return temp19;
    }

    public void setTemp19(int temp19) {
        this.temp19.set(temp19);
    }

    public int getHigh19() {
        return high19.get();
    }

    public SimpleIntegerProperty high19Property() {
        return high19;
    }

    public void setHigh19(int high19) {
        this.high19.set(high19);
    }

    public int getExce19() {
        return exce19.get();
    }

    public SimpleIntegerProperty exce19Property() {
        return exce19;
    }

    public void setExce19(int exce19) {
        this.exce19.set(exce19);
    }

    //Slot20
    public int getTemp20() {
        return temp20.get();
    }

    public SimpleIntegerProperty temp20Property() {
        return temp20;
    }

    public void setTemp20(int temp20) {
        this.temp20.set(temp20);
    }

    public int getHigh20() {
        return high20.get();
    }

    public SimpleIntegerProperty high20Property() {
        return high20;
    }

    public void setHigh20(int high20) {
        this.high20.set(high20);
    }

    public int getExce20() {
        return exce20.get();
    }

    public SimpleIntegerProperty exce20Property() {
        return exce20;
    }

    public void setExce20(int exce20) {
        this.exce20.set(exce20);
    }

    //Slot21
    public int getTemp21() {
        return temp21.get();
    }

    public SimpleIntegerProperty temp21Property() {
        return temp21;
    }

    public void setTemp21(int temp21) {
        this.temp21.set(temp21);
    }

    public int getHigh21() {
        return high21.get();
    }

    public SimpleIntegerProperty high21Property() {
        return high21;
    }

    public void setHigh21(int high21) {
        this.high21.set(high21);
    }

    public int getExce21() {
        return exce21.get();
    }

    public SimpleIntegerProperty exce21Property() {
        return exce21;
    }

    public void setExce21(int exce21) {
        this.exce21.set(exce21);
    }
}
