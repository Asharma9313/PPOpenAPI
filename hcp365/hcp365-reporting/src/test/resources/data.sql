Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(1, 'Last 7 Days');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(2, 'Last 30 Days');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(3, 'Month to Date');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(4, 'Week to Date');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(5, 'Quarter to Date');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(6, 'Flight to Date');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(7, 'Lifetime');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(8, 'Yesterday');
Insert into LifeScheduledReportPeriod ("PeriodId", "PeriodName") values(9, 'Last...');

Insert into HCP365ReportTemplate (id, Name, Status, AccountId, UserId, AdvId, CreatedOn, LastModifiedDate ) values(1, 'Test1', 1, 559878, 4577, 525, '2021-07-30', '2021-07-30');
Insert into HCP365ReportTemplate (id, Name, Status, AccountId, UserId, AdvId, CreatedOn, LastModifiedDate ) values(2, 'Test2', 0, 559878, 4577, 525, '2021-07-30', '2021-07-30');
Insert into HCP365ReportTemplate (id, Name, Status, AccountId, UserId, AdvId, CreatedOn, LastModifiedDate ) values(3, 'Test2', 1, 559879, 4577, 526, '2021-07-30', '2021-07-30');
Insert into HCP365ReportTemplate (id, Name, Status, AccountId, UserId, AdvId, CreatedOn, LastModifiedDate ) values(4, 'Default', 1, -1, -1, -1, '2021-07-30', '2021-07-30');

Insert into Hcp365ReportTemplateColumnGroup(id, Name, Status, Ordinal) values (1, 'Dimensions', 1, 1);
Insert into Hcp365ReportTemplateColumnGroup(id, Name, Status, Ordinal) values (2, 'Metrics', 1, 2);
Insert into Hcp365ReportTemplateColumnGroup(id, Name, Status, Ordinal) values (3, 'Others', 0, 3);

Insert into Hcp365ReportTemplateColumn(id, Name, Status, GroupId, Ordinal) values (1, 'Placement Id', 1, 1, 2);
Insert into Hcp365ReportTemplateColumn(id, Name, Status, GroupId, Ordinal) values (2, 'Adv Id', 1, 1, 1);
Insert into Hcp365ReportTemplateColumn(id, Name, Status, GroupId, Ordinal) values (3, 'CTR', 1, 2, 1);
Insert into Hcp365ReportTemplateColumn(id, Name, Status, GroupId, Ordinal) values (4, 'Adv Id Old', 0, 1, 1);

Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(1,1,1,1,null,2,1);
Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(2,1,2,null,'Custom CTR',1,1);
Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(3,1,1,1,null,3,0);
Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(4,2,1,1,null,3,0);

Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(5,3,1,1,null,2,1);
Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(6,3,2,null,'Custom CTR',1,1);

Insert into Hcp365ReportTemplateColumnDefinition(id,  TemplateId, FieldType, FieldRefId, CustomFieldName, Ordinal, Status) values(7,4,1,1,null,2,1);

Insert into Hcp365ReportFormatSetting(id, AccountId, UserId, AdvId, FileType, FileCustomExtension, FileCustomDelimiter, ArchivationType, ColumnHeader) values (1, 559145, 2456, 543, 2, 'tbt', '#', 1, true);
Insert into Hcp365ReportFormatSetting(id, AccountId, UserId, AdvId, FileType, FileCustomExtension, FileCustomDelimiter, ArchivationType, ColumnHeader) values (2, -1, -1, -1, 2, 'csv', NULL, 1, true);
Insert into Hcp365ReportAttributeRef(id, Name, ReportingAPIColName) values(1, 'TemplateId', null);
Insert into Hcp365ReportAttributeRef(id, Name, ReportingAPIColName) values(2, 'FromDate', 'From');
Insert into Hcp365ReportAttributeRef(id, Name, ReportingAPIColName) values(3, 'ToDate', 'To');
Insert into Hcp365ReportAttributeRef(id, Name, ReportingAPIColName) values(4, 'AdvId', 'Advertiser');
Insert into Hcp365ReportAttributeRef(id, Name, ReportingAPIColName) values(5, 'CollectionId', 'PlacementId');

Insert into Hcp365ReportLog(ReportId, UserId, Query, CreatedOn, LastModifiedDate, Status, DownloadLink, Archive, ErrorMessage, AccountId, RequestId, ScheduleId, downloadType, CustomDestination, filePath, fileName) values (1, 454, null, '2021-08-04', '2021-08-04',3001, null, 0, null, 559145, 'AF8FF971-A203-42FD-B2BA-91E7BD43E656', null, 2, 1, '${adId}/${collectionid}', 'rptfile');

Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (1, 1, 1, '1', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (2, 1, 2, '2021-08-01', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (3, 1, 3, '2021-08-04', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (4, 1, 3, '2021-08-04', 0);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (5, 1, 5, '1', 1);

Insert into Hcp365ReportLog(ReportId, UserId, Query, CreatedOn, LastModifiedDate, Status, DownloadLink, Archive, ErrorMessage, AccountId, RequestId, ScheduleId, downloadType, CustomDestination, filePath, fileName) values (2, 454, null, '2021-08-05', '2021-08-05',3001, null, 0, null, 559145, 'CD8FF971-A203-42FD-G2BA-91E7BD43E625', null, 2, 1, '${adId}/${collectionid}', 'rptfile');

Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (6, 2, 1, '1', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (7, 2, 2, '2021-08-01', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (8, 2, 3, '2021-08-05', 1);
Insert into Hcp365ReportDefinition(id, ReportId, AttributeTypeId, AttributeValue, Status) values (10, 2, 5, '2', 1);


Insert into Hcp365ScheduledReport(id, CustomDestinationId, FrequencyId, FilePath, FileName, AccountId, AdvId, Status, ScheduleStartDate, ScheduleEndDate, GenerateReport_WeekDay, GenerateReport_Time, PeriodId, PeriodicalNumber, PeriodicalNumberType, TimezoneId) values (1, 1, 1, '${AccountId}/${AccountName}', 'test', 559145L, 243L, 1, '2021-08-13', '2021-08-31', 1, '4:00', 1, 1, 1, 1);

