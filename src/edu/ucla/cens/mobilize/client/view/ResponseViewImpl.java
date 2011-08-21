package edu.ucla.cens.mobilize.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.ucla.cens.mobilize.client.AwConstants;
import edu.ucla.cens.mobilize.client.common.Privacy;
import edu.ucla.cens.mobilize.client.model.SurveyResponse;
import edu.ucla.cens.mobilize.client.model.UserParticipationInfo;
import edu.ucla.cens.mobilize.client.ui.MessageWidget;
import edu.ucla.cens.mobilize.client.ui.ResponseDisplayWidget;
import edu.ucla.cens.mobilize.client.ui.ResponseWidgetBasic;
import edu.ucla.cens.mobilize.client.utils.DateUtils;

public class ResponseViewImpl extends Composite implements ResponseView {
  
  private static ResponseViewUiBinder uiBinder = GWT
      .create(ResponseViewUiBinder.class);

  @UiTemplate("ResponseView.ui.xml")
  interface ResponseViewUiBinder extends UiBinder<Widget, ResponseViewImpl> {
  }

  public interface ResponseViewStyles extends CssResource {
    String selectedTopNav();
    String leaderboardHeaderRow();
  }

  @UiField ResponseViewStyles style;
  @UiField HTMLPanel editResponsesMenuItem; // used for showing/hiding
  @UiField Anchor viewLinkEdit;
  @UiField Anchor viewLinkBrowse;
  @UiField Anchor viewLinkLeaderboard;
  @UiField Label singleParticipantLabel;
  @UiField ListBox participantFilter;
  @UiField HTMLPanel optionalFilters;
  @UiField ListBox campaignFilter;
  @UiField ListBox surveyFilter;
  @UiField ListBox privacyFilter;
  @UiField DateBox fromDateBox;
  @UiField DateBox toDateBox;
  @UiField CheckBox hasPhotoCheckBox;
  @UiField Button applyFiltersButton;
  @UiField MessageWidget messageWidget;
  @UiField Label sectionHeaderTitle;
  @UiField Label sectionHeaderDetail;
  @UiField FlowPanel responseList;
  @UiField Button shareButtonTop;
  @UiField Button makePrivateButtonTop;
  @UiField Button deleteButtonTop;
  @UiField Button shareButtonBottom;
  @UiField Button makePrivateButtonBottom;
  @UiField Button deleteButtonBottom;
  @UiField Anchor selectAllLinkTop;
  @UiField Anchor selectNoneLinkTop;
  @UiField Anchor selectAllLinkBottom;
  @UiField Anchor selectNoneLinkBottom;
  @UiField HTMLPanel expandCollapseLinksTop;
  @UiField Anchor expandLinkTop;
  @UiField Anchor collapseLinkTop;
  @UiField HTMLPanel expandCollapseLinksBottom;
  @UiField Anchor expandLinkBottom;
  @UiField Anchor collapseLinkBottom;
  @UiField HTMLPanel buttonPanelTop;
  @UiField HTMLPanel buttonPanelBottom;
  
  ResponseView.Presenter presenter;
  Privacy selectedPrivacy = Privacy.UNDEFINED;
  private Subview selectedSubview;
  private String emptyParticipantListString = "None visible.";
  
  public ResponseViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    setEventHandlers();
    
    // set up date pickers
    DateBox.Format fmt = new DateBox.DefaultFormat(DateUtils.getDateBoxDisplayFormat());
    fromDateBox.setFormat(fmt);
    toDateBox.setFormat(fmt);
  }
  
  private void setEventHandlers() {
    
    selectAllLinkTop.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        selectAll();
      }
    });
    
    selectNoneLinkTop.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        selectNone();
      }
    });
   
    selectAllLinkBottom.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        selectAll();
      }
    });
    
    selectNoneLinkBottom.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        selectNone();
      }
    });
    
    expandLinkTop.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        expandAll();
      }
    });
    
    expandLinkBottom.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        expandAll();
      }
    });
    
    collapseLinkTop.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        collapseAll();
      }
    });
    
    collapseLinkBottom.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        collapseAll();
      }
    });
  }

  private void selectAll() {
    for (int i = 0; i < responseList.getWidgetCount(); i++) {
        ResponseDisplayWidget widget = (ResponseDisplayWidget)responseList.getWidget(i);
        widget.setSelected(true); // TODO: test for null?
    }
  }
  
  private void selectNone() {
    for (int i = 0; i < responseList.getWidgetCount(); i++) {
      ResponseDisplayWidget widget = (ResponseDisplayWidget)responseList.getWidget(i);
      widget.setSelected(false); // TODO: test for null?
    }
  }
  
  private void expandAll() {
    for (int i = 0; i < responseList.getWidgetCount(); i++) {
      if (responseList.getWidget(i).getClass() == ResponseWidgetBasic.class) {
        ResponseDisplayWidget widget = (ResponseDisplayWidget)responseList.getWidget(i);
        widget.expand();
      }
    }
  }
  
  private void collapseAll() {
    for (int i = 0; i < responseList.getWidgetCount(); i++) {
      if (responseList.getWidget(i).getClass() == ResponseWidgetBasic.class) {
        ResponseDisplayWidget widget = (ResponseDisplayWidget)responseList.getWidget(i);
        widget.collapse();
      }
    }    
  }
  
  private void setExpandCollapseLinksVisible(boolean isVisible) {
    this.expandCollapseLinksTop.setVisible(isVisible);
    this.expandCollapseLinksBottom.setVisible(isVisible);
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void setParticipantList(SortedSet<String> participantNames, boolean makeFirstItemAll) {
    if (participantNames == null || participantNames.size() == 0) {
      singleParticipantLabel.setVisible(true);
      singleParticipantLabel.setText(this.emptyParticipantListString);
      participantFilter.setVisible(false);
    } else if (participantNames.size() == 1) {
      singleParticipantLabel.setVisible(true);
      singleParticipantLabel.setText(participantNames.first());
      participantFilter.setVisible(false);
    } else {
      singleParticipantLabel.setVisible(false);
      participantFilter.setVisible(true);
      participantFilter.clear();
      if (makeFirstItemAll) participantFilter.addItem("All", AwConstants.specialAllValuesToken);
      for (String name : participantNames) {
        participantFilter.addItem(name, name); 
      }
      // default is "All" if it's an option or no selection otherwise
      participantFilter.setSelectedIndex(makeFirstItemAll ? 0 : -1);
    }
  } 

  @Override
  public void setCampaignList(Map<String, String> campaignIdToNameMap) {
    campaignFilter.clear();
    if (campaignIdToNameMap == null) return;
    
    // sort campaigns by name then by id
    List<String> nameKeyPairs = new ArrayList<String>();
    for (String campaignId : campaignIdToNameMap.keySet()) {
      String name = campaignIdToNameMap.get(campaignId);
      nameKeyPairs.add(name + "###" + campaignId);
    }
    Collections.sort(nameKeyPairs);
    for (String nameKeyPair : nameKeyPairs) {
      String[] arr = nameKeyPair.split("###"); // 0 = name, 1 = id
      campaignFilter.addItem(arr[0], arr[1]); // name is visible text, id is value
    }
    
    campaignFilter.setSelectedIndex(-1);
  }

  @Override
  public void setSurveyList(List<String> surveyNames) {
    surveyFilter.clear();
    if (surveyNames == null) return;
    surveyFilter.addItem("All", "");
    for (String name : surveyNames) {
      surveyFilter.addItem(name);
    }
  }
  
  @Override
  public void setPrivacyStates(List<Privacy> privacyStates) {
    privacyFilter.clear();
    if (privacyStates == null) return;
    privacyFilter.addItem("All", "");
    for (Privacy privacy : privacyStates) {
      privacyFilter.addItem(privacy.toUserFriendlyString(), privacy.toServerString());
    }
  }

  @Override
  public void selectParticipant(String participantName) {
    for (int i = 0; i < participantFilter.getItemCount(); i++) {
      if (participantFilter.getValue(i).equals(participantName)) {
        participantFilter.setSelectedIndex(i);
        return;
      }
    }
    // if not found, select none
    participantFilter.setSelectedIndex(-1);
  }

  @Override
  public void selectCampaign(String campaignId) {
    for (int i = 0; i < campaignFilter.getItemCount(); i++) {
      if (campaignFilter.getValue(i).equals(campaignId)) {
        campaignFilter.setSelectedIndex(i);
        return;
      }
    }
    // if not found, select nothing
    campaignFilter.setSelectedIndex(-1);
  }

  @Override
  public void selectSurvey(String surveyName) {
    for (int i = 0; i < surveyFilter.getItemCount(); i++) {
      if (surveyFilter.getItemText(i).equals(surveyName)) {
        surveyFilter.setSelectedIndex(i);
        return;
      }
    }
    // if not found, select first item ("All")
    surveyFilter.setSelectedIndex(0);
  }
  
  @Override 
  public void selectPrivacyState(Privacy privacy) {
    if (privacy == null) {
      privacyFilter.setSelectedIndex(-1);
      return;
    }
    
    String serverString = privacy.toServerString();
    for (int i = 0; i < privacyFilter.getItemCount(); i++) {
      if (privacyFilter.getValue(i).equals(serverString)) {
        privacyFilter.setSelectedIndex(i);
        return;
      }
    }
    // if not found, select first item ("All")
    privacyFilter.setSelectedIndex(0);
  }
  
  @Override
  public void selectStartDate(Date fromDate) {
    fromDateBox.setValue(fromDate);
  }

  @Override
  public void selectEndDate(Date toDate) {
    toDateBox.setValue(toDate);
  }
  
  @Override
  public void setPhotoFilter(boolean showOnlyResponsesWithPhotos) {
    hasPhotoCheckBox.setValue(showOnlyResponsesWithPhotos);
  }
  
  @Override
  public void renderResponses(List<SurveyResponse> responses) {
    if (Subview.EDIT.equals(selectedSubview)) {
      renderResponsesEditView(responses);
    } else if (Subview.BROWSE.equals(selectedSubview)) {
      renderResponsesBrowseView(responses);
    } else { // default to browse
      renderResponsesBrowseView(responses);
    }
  }
  
  private void renderResponsesEditView(List<SurveyResponse> responses) {
    this.responseList.clear();
    for (SurveyResponse response : responses) {
      ResponseWidgetBasic responseWidget = new ResponseWidgetBasic();
      responseWidget.setSelectable(true);
      responseWidget.setResponse(response);
      this.responseList.add(responseWidget);
    }
  }
  
  private void renderResponsesBrowseView(List<SurveyResponse> responses) {
    this.responseList.clear();
    for (SurveyResponse response : responses) {
      ResponseWidgetBasic responseWidget = new ResponseWidgetBasic();
      responseWidget.setSelectable(false);
      responseWidget.setResponse(response);
      this.responseList.add(responseWidget);
    }
  }
  
  @Override
  public void renderLeaderboardView(List<UserParticipationInfo> participationInfo) {
    this.responseList.clear();
    Grid leaderboard = new Grid();
    int numRows = participationInfo.size() + 1; // +1 for the header row
    int numCols = 4; // username, total, private, shared // FIXME: invisible?
    leaderboard.resize(numRows, numCols);
    leaderboard.setText(0, 0, "Username");
    leaderboard.setText(0, 1, "Total Responses");
    leaderboard.setText(0, 2, "Private Responses");
    leaderboard.setText(0, 3, "Shared Responses");
    leaderboard.getRowFormatter().addStyleName(0, style.leaderboardHeaderRow());
    int row = 1; // first row is header
    for (UserParticipationInfo info : participationInfo) {
      leaderboard.setText(row, 0, info.getUsername());
      leaderboard.setText(row, 1, Integer.toString(info.getTotalResponseCount()));
      leaderboard.setText(row, 2, Integer.toString(info.getResponseCount(Privacy.PRIVATE)));
      leaderboard.setText(row, 3, Integer.toString(info.getResponseCount(Privacy.SHARED)));
      row++;
    }
    this.responseList.add(leaderboard);
  }

  private void setEditControlsVisible(boolean isVisible) {
    this.buttonPanelTop.setVisible(isVisible);
    this.buttonPanelBottom.setVisible(isVisible);
  }
  
  @Override
  public String getSelectedParticipant() {
    String selectedUser = null;
    if (this.singleParticipantLabel.isVisible()) {
      // when only one user is visible, that is the selected user
      selectedUser = this.singleParticipantLabel.getText();
      // check for special case where there are no participants
      if (selectedUser.equals(this.emptyParticipantListString)) {
        selectedUser = "";
      }
    } else { 
      // otherwise, get selection from dropdown
      int index = this.participantFilter.getSelectedIndex();
      selectedUser = (index > -1) ? this.participantFilter.getValue(index) : "";
    }
    return selectedUser;
  }

  @Override
  public String getSelectedCampaign() {
    int index = this.campaignFilter.getSelectedIndex();
    return (index > -1) ? this.campaignFilter.getValue(index) : "";
  }

  @Override
  public String getSelectedSurvey() {
    int index = this.surveyFilter.getSelectedIndex();
    return (index > -1) ? this.surveyFilter.getValue(index) : "";
  }

  @Override
  public Privacy getSelectedPrivacyState() {
    int index = this.privacyFilter.getSelectedIndex();
    return (index > -1) ? Privacy.fromServerString(this.privacyFilter.getValue(index)) : null;
  }

  @Override
  public Date getSelectedStartDate() {
    return this.fromDateBox.getValue();
  }

  @Override
  public Date getSelectedEndDate() {
    return this.toDateBox.getValue();
  }
  
  @Override
  public boolean getHasPhotoToggleValue() {
    return this.hasPhotoCheckBox.getValue();
  }

  @Override
  public List<HasClickHandlers> getShareButtons() {
    List<HasClickHandlers> retval = new ArrayList<HasClickHandlers>();
    retval.add(this.shareButtonTop);
    retval.add(this.shareButtonBottom);
    return retval;
  }

  @Override
  public List<HasClickHandlers> getMakePrivateButtons() {
    List<HasClickHandlers> retval = new ArrayList<HasClickHandlers>();
    retval.add(this.makePrivateButtonTop);
    retval.add(this.makePrivateButtonBottom);
    return retval;
  }

  @Override
  public List<HasClickHandlers> getDeleteButtons() {
    List<HasClickHandlers> retval = new ArrayList<HasClickHandlers>();
    retval.add(this.deleteButtonTop);
    retval.add(this.deleteButtonBottom);
    return retval;
  }
  
  @Override
  public HasClickHandlers getApplyFiltersButton() {
    return this.applyFiltersButton;
  }

  @Override
  public HasChangeHandlers getCampaignFilter() {
    return this.campaignFilter;
  }

  @Override
  public HasChangeHandlers getSurveyFilter() {
    return this.surveyFilter;
  }

  @Override
  public HasChangeHandlers getParticipantFilter() {
    return this.participantFilter;
  }
  

  @Override
  public HasChangeHandlers getPrivacyFilter() {
    return this.privacyFilter;
  }

  @Override
  public HasValueChangeHandlers<Date> getStartDateFilter() {
    return this.fromDateBox;
  }

  @Override
  public HasValueChangeHandlers<Date> getEndDateFilter() {
    return this.toDateBox;
  }

  @Override
  public List<String> getSelectedSurveyResponseKeys() {
    List<String> keys = new ArrayList<String>();
    for (int i = 0; i < responseList.getWidgetCount(); i++) {
      ResponseDisplayWidget widget = (ResponseDisplayWidget)responseList.getWidget(i);
      if (widget != null && widget.isSelected()) {
        keys.add(Integer.toString(widget.getResponseKey()));
        
      }
    }    
    return keys;
  }
  
  @Override
  public void clearSelectedSurveyResponseKeys() {
    selectNone();
  }

  @Override
  public void clearResponseList() {
    this.responseList.clear();
  }

  @Override
  public void markShared(int responseKey) {
    int numWidgets = this.responseList.getWidgetCount();
    for (int i = 0; i < numWidgets; i++) {
      ResponseDisplayWidget responseWidget = (ResponseDisplayWidget)responseList.getWidget(i);
      if (responseWidget.getResponseKey() == responseKey) {
        responseWidget.setPrivacy(Privacy.SHARED);
        break;
      }
    }
  }

  @Override
  public void markPrivate(int responseKey) {
    int numWidgets = this.responseList.getWidgetCount();
    for (int i = 0; i < numWidgets; i++) {
      ResponseDisplayWidget responseWidget = (ResponseDisplayWidget)responseList.getWidget(i);
      if (responseWidget.getResponseKey() == responseKey) {
        responseWidget.setPrivacy(Privacy.PRIVATE);
        break;
      }
    }
  }
  
  @Override
  public void removeResponse(int responseKey) {
    int numWidgets = this.responseList.getWidgetCount();
    for (int i = 0; i < numWidgets; i++) {
      ResponseDisplayWidget responseWidget = (ResponseDisplayWidget)responseList.getWidget(i);
      if (responseWidget.getResponseKey() == responseKey) {
        this.responseList.remove(i);
        break;
      }
    }    
  }

  @Override
  public void showInfoMessage(String info) {
    this.messageWidget.showInfoMessage(info);
  }

  @Override
  public void addErrorMessage(String error, String detail) {
    this.messageWidget.addError(error, detail);
  }
  
  @Override 
  public void clearErrorMessages() {
    this.messageWidget.clearErrors();
    this.messageWidget.hide();
  }

  @Override
  public void showConfirmDelete(final ClickHandler onConfirmDelete) {
    final DialogBox dialog = new DialogBox();
    dialog.setGlassEnabled(true);
    dialog.setText("Are you sure you want to delete the selected responses? " +
                   "This action cannot be undone.");
    dialog.setModal(true);
    Button deleteButton = new Button("Delete");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (onConfirmDelete != null) onConfirmDelete.onClick(event);
        dialog.hide();
      }
    });
    Button cancelButton = new Button("Cancel");
    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        dialog.hide();
      }
    });
    FlowPanel panel = new FlowPanel(); 
    panel.add(deleteButton);
    panel.add(cancelButton);
    dialog.add(panel);
    dialog.center();
    
  }

  @Override
  public void enableSurveyFilter() {
    this.surveyFilter.setEnabled(true);
  }

  @Override
  public void disableSurveyFilter() {
    this.surveyFilter.setSelectedIndex(-1);
    this.surveyFilter.setEnabled(false);
  }

  @Override
  public void clearSurveyList() {
    this.surveyFilter.clear();
    this.surveyFilter.setEnabled(false);
  }
  
  @Override 
  public void clearParticipantList() {
    this.participantFilter.clear();
    this.singleParticipantLabel.setText("");
  }

  @Override
  public void setSectionHeader(String headerText) {
    this.sectionHeaderTitle.setText(headerText);
  }

  @Override
  public void setSectionHeaderDetail(String detailText) {
    this.sectionHeaderDetail.setText(detailText);
  }
  
  @Override
  public void showResponseCountInSectionHeader(String username, int responseCount) {
    String userDisplayName = username.equals(AwConstants.specialAllValuesToken) ? "all users" : username;
    setSectionHeader("Showing " + Integer.toString(responseCount) + " responses from " + userDisplayName);
  }
  
  @Override
  public Subview getSelectedSubview() {
    return selectedSubview != null ? selectedSubview : Subview.BROWSE; // default to browse view
  }
  

  @Override
  public void setSelectedSubview(Subview subview) {
    clearSelectedView();
    selectedSubview = (subview != null) ? subview : Subview.BROWSE;
    if (Subview.BROWSE.equals(selectedSubview)) {
      viewLinkBrowse.addStyleName(style.selectedTopNav());
      setEditControlsVisible(false);
      setExpandCollapseLinksVisible(true);
    } else if (Subview.EDIT.equals(selectedSubview)) {
      viewLinkEdit.addStyleName(style.selectedTopNav());
      setEditControlsVisible(true);
      setExpandCollapseLinksVisible(true);
    } else if (Subview.LEADERBOARD.equals(selectedSubview)) {
      viewLinkLeaderboard.addStyleName(style.selectedTopNav());
      setEditControlsVisible(false);
      setExpandCollapseLinksVisible(false);
    }
  }  
  
  @Override
  public void setEditMenuItemVisible(boolean isVisible) {
    this.editResponsesMenuItem.setVisible(isVisible);
  }

  private void clearSelectedView() {
    selectedSubview = null;
    viewLinkEdit.removeStyleName(style.selectedTopNav());
    viewLinkBrowse.removeStyleName(style.selectedTopNav());
    viewLinkLeaderboard.removeStyleName(style.selectedTopNav());
  }

  @Override
  public HasClickHandlers getViewLinkBrowse() {
    return this.viewLinkBrowse;
  }

  @Override
  public HasClickHandlers getViewLinkEdit() {
    return this.viewLinkEdit;
  }

  @Override
  public HasClickHandlers getViewLinkLeaderboard() {
    return this.viewLinkLeaderboard;
  }

  @Override
  public void showAllFilters() {
    this.optionalFilters.setVisible(true);
  }
  
  @Override
  public void hideOptionalFilters() {
    this.optionalFilters.setVisible(false);
  }
  
}
