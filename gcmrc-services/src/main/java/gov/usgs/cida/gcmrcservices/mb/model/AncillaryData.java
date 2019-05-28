package gov.usgs.cida.gcmrcservices.mb.model;

public class AncillaryData {
    protected final String groupId;
    protected final String ancillaryGroupId;
    protected final String ancillaryName;
    protected final String ancillaryColumn;

    public AncillaryData() {
        groupId = null;
        ancillaryGroupId = null;
        ancillaryName = null;
        ancillaryColumn = null;
    }

    public AncillaryData(String groupId,
            String ancillaryGroupId,
            String ancillaryName,
            String ancillaryColumn) {
        this.groupId = groupId;
        this.ancillaryGroupId = ancillaryGroupId;
        this.ancillaryName = ancillaryName;
        this.ancillaryColumn = ancillaryColumn;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getAncillaryGroupId() {
        return this.ancillaryGroupId;
    }

    public String getAncillaryName() {
        return this.ancillaryName;
    }

    public String getAncillaryColumn() {
        return this.ancillaryColumn;
    }
}