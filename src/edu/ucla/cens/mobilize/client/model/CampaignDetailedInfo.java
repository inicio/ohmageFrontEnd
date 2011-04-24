package edu.ucla.cens.mobilize.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Window;

import edu.ucla.cens.mobilize.client.common.Privacy;
import edu.ucla.cens.mobilize.client.common.RunningState;
import edu.ucla.cens.mobilize.client.common.UserRole;
import edu.ucla.cens.mobilize.client.utils.XmlConfigTranslator;

public class CampaignDetailedInfo {
  private String campaignId;
  private String campaignName;
  private String description;
  private RunningState runningState;
  private Privacy privacy;
  private List<String> classes;
  private List<UserRole> userRoles; // roles for logged in user
  private List<String> authors;
  private List<String> supervisors;
  private Date creationTime;
  
  private String xmlConfig;
  private XmlConfigTranslator configTranslator;
  //private List<SurveyInfo> surveys;
  
  public CampaignDetailedInfo() {
    classes = new ArrayList<String>();
    authors = new ArrayList<String>();
    supervisors = new ArrayList<String>();
    userRoles = new ArrayList<UserRole>();
    campaignId = "";
    campaignName = "";
    description = "";
    runningState = RunningState.STOPPED;
    privacy = Privacy.PRIVATE;
    xmlConfig = "";  
    configTranslator = new XmlConfigTranslator();
    //surveys = new ArrayList<SurveyInfo>();
  };

  /************ ACCESS CONTROL **************/
  public boolean canEdit(String username) {
    return authors.contains(username) || supervisors.contains(username);
  }
  
  public boolean canDelete(String username) {
    return authors.contains(username) || supervisors.contains(username); // && no responses FIXME
  }
  
  public boolean canUpload(UserInfo userInfo) {
    boolean retval = false;
    for (String classUserIsMemberOf : userInfo.getClasses()) {
      if (classes.contains(classUserIsMemberOf)) {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  /************ CONVENIENCE METHODS **************/
  public boolean isActive() {
    return this.runningState.equals(RunningState.RUNNING);
  }
  
  /************ GETTERS **************/
  
  public String getCampaignId() {
    return campaignId;
  }
  
  public String getCampaignName() {
    return campaignName;
  }
  
  public String getDescription() {
    return description;
  }
  
  // returns list of group ids
  public List<String> getClasses() {
    return classes;
  }
  
  // returns list of usernames
  public List<String> getAuthors() {
    return authors;
  }
  
  // returns list of usernames
  public List<String> getSupervisors() {
    return supervisors;
  }
  
  public RunningState getRunningState() {
    return runningState;
  }
  
  public Privacy getPrivacy() {
    return privacy;
  }

  // gets one survey from config. returns null if not found
  public SurveyInfo getSurvey(String surveyId) {
    return this.configTranslator.getSurveyInfo(surveyId);
  }
  
  // gets all surveys from xml config
  public List<SurveyInfo> getSurveys() {
    return this.configTranslator.getSurveyInfos();
  }
  
  // gets list of all survey ids. useful so you can get ids for data point queries
  // without having to build surveyinfo/promptinfo objects
  public List<String> getSurveyIds() {
    return this.configTranslator.getSurveyIds();
  }
  
  public List<SurveyInfo> getSurveys(List<String> surveyIds) {
    return this.configTranslator.getSurveyInfos(surveyIds);
  }
  
  // permissions 
  public List<UserRole> getUserRoles() {
    return this.userRoles != null ? this.userRoles : new ArrayList<UserRole>();
  }

  // returns xml config as a string, useful for when user wants to view config
  public String getXmlConfig() {
    return this.xmlConfig != null ? this.xmlConfig : "xml config not loaded";
  }
  
  public Date getCreationTime() {
    return this.creationTime;
  }
  
  /************ SETTERS ***************/
  
  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }
  
  public void setCampaignName(String campaignName) {
    this.campaignName = campaignName;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }

  public void addClass(String classId) {
    this.classes.add(classId);
  }
  
  public void removeClass(String classId) {
    if (this.classes.contains(classId)) {
      this.classes.remove(classId);
    }
  }
  
  public void clearClasses() {
    this.classes.clear();
  }

  public void addAuthor(String author) {
    this.authors.add(author);
  }
  
  public void removeAuthor(String author) {
    if (this.authors.contains(author)) {
      this.authors.remove(author);
    }
  }
  
  public void clearAuthors() {
    this.authors.clear();
  }
  
  public void addSupervisor(String supervisor) {
    supervisors.add(supervisor);
  }
  
  public void removeSupervisor(String supervisor) {
    if (supervisors.contains(supervisor)) {
      supervisors.remove(supervisor);
    }
  }
  
  public void clearSupervisors() {
    supervisors.clear();
  }
  
  public void setXmlConfig(String xmlConfig) {
    if (this.configTranslator.loadFromXml(xmlConfig)) {
      this.xmlConfig = xmlConfig;
    } else {
      // TODO: how should error be handled?
      this.xmlConfig = "there was a problem loading the xml config";
      // DELETEME
      //Window.alert(this.xmlConfig);
    }
  }

  // FIXME: add/remove user role
  public void setUserRoles (List<UserRole> userRoles) {
    this.userRoles = userRoles;
  }
  
  public void setRunningState (RunningState runningState) {
    this.runningState = runningState;
  }
  
  public void setPrivacy(Privacy privacy) {
    this.privacy = privacy;
  }
  
  public void setCreationTime(Date time) {
    this.creationTime = time;
  }

}
