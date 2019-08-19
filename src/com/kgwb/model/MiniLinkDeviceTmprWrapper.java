package com.kgwb.model;

import java.util.List;

public class MiniLinkDeviceTmprWrapper {
    private String siteId;
    private String ipAddress;
    private String softwareVersion;
    private List<SlotTmprWrapper> slotsTmpr;
    private String comment;
    public MiniLinkDeviceTmprWrapper(String siteId,
                                     String ipAddress,
                                     String softwareVersion,
                                     List<SlotTmprWrapper> slotTmprList,
                                     String comment) {
        this.siteId = siteId;
        this.ipAddress = ipAddress;
        this.softwareVersion = softwareVersion;
        this.slotsTmpr = slotTmprList;
        this.comment = comment;
    }

    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public List<SlotTmprWrapper> getSlotsTmpr() { return slotsTmpr; }
    public void setSlotsTmpr(List<SlotTmprWrapper> slotsTmpr) {
        this.slotsTmpr = slotsTmpr;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
