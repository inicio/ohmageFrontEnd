package edu.ucla.cens.AndWellnessVisualizations.client.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Storage class to hold basic user information.
 * 
 * @author jhicks
 *
 */
public class UserInfo implements Comparable<UserInfo> {
    private String userName;
    private String authToken;
    private int privileges = -1;
    private List<String> campaignMembershipList = new ArrayList<String>();
    //private int selectedCampaign = -1;
    // Default to the first campaign selected for now
    private int selectedCampaign = 0;
    
    public UserInfo() {};
    
    public UserInfo(String userName) {
        this.userName = userName;
        // We do not know the privileges, set to invalid
        this.privileges = -1;
    }
    
    public UserInfo(String userName, int privileges) {
        this.userName = userName;
        this.privileges = privileges;
    }
    
    // Return whether the user is an admin
    public boolean isAdmin() {
        if (privileges == 1) {
            return true;
        }
        
        return false;
    }
    
    // Return whether the user is a researcher
    public boolean isResearcher() {
        if (privileges == 3) {
            return true;
        }
        
        return false;
    }
    
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public int getPrivileges() { return privileges; }
    public void setPrivileges(int privileges) { this.privileges = privileges; }
    public List<String> getCampaignMembershipList() { return campaignMembershipList; };
    public void setCampaignMembershipList(List<String> campaignMembershipList) { this.campaignMembershipList = campaignMembershipList; };
    public int getSelectedCampaign() { return selectedCampaign; };
    public void setSelectedCampaign(int selectedCampaign) { this.selectedCampaign = selectedCampaign; };

    /**
     * Returns the ID of the selected campaign.
     * 
     * @return The ascii name of the currently selected campaign.  Returns null
     * if there if no campaign selected.
     */
    public String getSelectedCampaignId() {
        int selectedCampaign = getSelectedCampaign();
        String selectedCampaignId;
        
        try {
            selectedCampaignId = getCampaignMembershipList().get(selectedCampaign);
        }
        catch (IndexOutOfBoundsException err) {
            // There is no selected campaign, return null.
            return null;
        }
        
        return selectedCampaignId;
    }
    
    // Allows this model to be sorted by Collections.sort() (be userName only)
    public int compareTo(UserInfo arg0) {
        return this.userName.compareTo(arg0.userName);
    }
}
