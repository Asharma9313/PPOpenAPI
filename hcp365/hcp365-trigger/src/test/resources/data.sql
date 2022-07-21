Insert into HCP365Trigger ("Name", "AdvId", "AccountId", "Active", "FromLife", "Comment") values('Test Trigger 1', 1, 559145, 1, 0, null);
Insert into HCP365Trigger ("Name", "AdvId", "AccountId", "Active", "FromLife", "Comment") values('Test Trigger 2', 1, 559145, 1, 0, null);
Insert into HCP365Trigger ("Name", "AdvId", "AccountId", "Active", "FromLife", "Comment") values('Test Trigger 3', 1, 559145, 0, 0, null);
Insert into HCP365Trigger ("Name", "AdvId", "AccountId", "Active", "FromLife", "Comment") values('Test Trigger 4', 2, 559145, 1, 0, null);
Insert into HCP365Trigger ("Name", "AdvId", "AccountId", "Active", "FromLife", "Comment") values('Test Trigger 6', 2, 559145, 1, 1, 'Test Comment');

Insert into HCP365Trigger_Audience ("TriggerId", "AudienceTypeId", "AudienceValueId", "Operator", "Status") values(1, 1,1, 1, 1);
Insert into HCP365Trigger_Audience ("TriggerId", "AudienceTypeId", "AudienceValueId", "Operator", "Status") values(1, 1,189, 0, 1);
Insert into HCP365Trigger_Audience ("TriggerId", "AudienceTypeId", "AudienceValueId", "Operator", "Status") values(1, 1,18, 0, 0);
Insert into HCP365Trigger_Audience ("TriggerId", "AudienceTypeId", "AudienceValueId", "Operator", "Status") values(2, 1,18, 0, 1);
Insert into HCP365Trigger_Audience ("TriggerId", "AudienceTypeId", "AudienceValueId", "Operator", "Status") values(1, 2,19, 1, 1);

Insert into HCP365Trigger_Action_Visit_Brand_Setting ("TriggerId", "FrequencyControlValue", "CustomUrlParamName", "CustomUrlParamValue", "UrlFilterOperator", "Status") values(1, 2, 'Param1', 'test param value', 1, 1);
Insert into HCP365Trigger_Action_Visit_Brand_Setting ("TriggerId", "FrequencyControlValue", "CustomUrlParamName", "CustomUrlParamValue", "UrlFilterOperator", "Status") values(2, 2, 'Param1', 'test param value', 1, 1);

Insert into HCP365Trigger_Action_Visit_Brand_CollectionXRef ("TriggerId", "CollectionId", "Status", "VisitBrandSettingId") values(2, 1, 1, 2);
Insert into HCP365Trigger_Action_Visit_Brand_CollectionXRef ("TriggerId", "CollectionId", "Status", "VisitBrandSettingId") values(2, 2, 1, 2);

Insert into HCP365Trigger_Action_Visit_Brand_URL_Filters ("TriggerId", "URLCriteria", "VisitBrandSettingId", "Status") values(2, 'url criteria1', 2, 1);
Insert into HCP365Trigger_Action_Visit_Brand_URL_Filters ("TriggerId", "URLCriteria", "VisitBrandSettingId", "Status") values(2, 'url criteria2', 2, 1);

Insert into HCP365Trigger_Action_Click_SearchAd_Setting ("TriggerId", "CustomKeywordQueryOperator", "Status", "CustomUrlParamName", "CustomUrlParamValue") values (1, 1, 1, 'Param1', 'Test Param1');
Insert into HCP365Trigger_Action_Click_SearchAd_Setting ("TriggerId", "CustomKeywordQueryOperator", "Status", "CustomUrlParamName", "CustomUrlParamValue") values (2, 0, 1, 'Param1', 'Test Param1');

Insert into HCP365Trigger_Action_Click_SearchAd_CollectionXRef ("TriggerId", "CollectionId", "Status", "ClickSearchAdSettingId") values(2, 1, 1, 2);
Insert into HCP365Trigger_Action_Click_SearchAd_CollectionXRef ("TriggerId", "CollectionId", "Status", "ClickSearchAdSettingId") values(2, 2, 1, 2);

Insert into HCP365Trigger_Action_Click_SearchAd_KeywordXRef ("TriggerId", "ClickSearchAdSettingId", "Keyword", "Status") values (2, 2, 'Test keyword', 1);
Insert into HCP365Trigger_Action_Click_SearchAd_KeywordXRef ("TriggerId", "ClickSearchAdSettingId", "Keyword", "Status") values (2, 2, 'Test keyword2', 1);

Insert into HCP365Trigger_Action_Media_Expose_Setting ("TriggerId", "FrequencyControlValue", "Status") values (1, 2, 1);
Insert into HCP365Trigger_Action_Media_Expose_Setting ("TriggerId", "FrequencyControlValue", "Status") values (2, 3, 1);

Insert into HCP365Trigger_Action_Media_Expose_CollectionXRef ("TriggerId", "CollectionId", "Status", "MediaExposeSettingId") values (1, 1, 1,1);
Insert into HCP365Trigger_Action_Media_Expose_CollectionXRef ("TriggerId", "CollectionId", "Status", "MediaExposeSettingId") values (1, 2, 1,1);

Insert into HCP365Trigger_Action_Media_Click_Setting ("TriggerId", "FrequencyControlValue", "Status") values (1, 2, 1);
Insert into HCP365Trigger_Action_Media_Click_Setting ("TriggerId", "FrequencyControlValue", "Status") values (2, 3, 1);

Insert into HCP365Trigger_Action_Media_Click_CollectionXRef ("TriggerId", "CollectionId", "Status", "MediaClickSettingId") values (1, 1, 1,1);
Insert into HCP365Trigger_Action_Media_Click_CollectionXRef ("TriggerId", "CollectionId", "Status", "MediaClickSettingId") values (1, 2, 1,1);

Insert into Hcp365Trigger_Action_Open_Email_CollectionXRef ("CollectionId", "Status", "TriggerId") values (1, 1, 1);
Insert into Hcp365Trigger_Action_Open_Email_CollectionXRef ("CollectionId", "Status", "TriggerId") values (2, 1, 1);

Insert into Hcp365Trigger_Action_Click_Email_CollectionXRef ("CollectionId", "Status", "TriggerId") values (1, 1, 1);
Insert into Hcp365Trigger_Action_Click_Email_CollectionXRef ("CollectionId", "Status", "TriggerId") values (2, 1, 1);

Insert into HCP365Trigger_Response_NPISmartList ("TriggerId", "RemoveNPIAfter", "Status", "NPIGroupId") values (1, 20, 1, 2);

Insert into HCP365Trigger_Response_KeywordSmartList ("TriggerId", "RemoveKeywordAfter", "Status", "KeywordGroupId") values (1, 20, 1, 4);

Insert into HCP365Trigger_Response_Webhook ("TriggerId", "Url", "Status") values(1, 'http://www.webhookurl/sample1', 1);