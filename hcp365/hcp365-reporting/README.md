# HCP365 Reporting API
This API contains all the code related to HCP 365 reporting.

### Setup

- Make sure you setup & run `Config Manager` before running this App.
- Create environment variable with name `APP_ENV` and set the environment name as value(eg. `dev`). Make sure you have
  the `hcp365reportingapi.yml` and environment specific properties file(eg. `hcp365reportingapi-{APP_ENV}.yml`) exist in
  the config manager's file path. You can find the properties
  file [here](https://github.com/pulsepointinc/ad-serving-configuration/blob/master/Prod/web/openapi/API2.0/hcp/hcp365reportingapi.yml)
  .
- Create environment variable `APP_CONFIG_SERVER` and set the Config Manager App url (eg. `http://localhost:1114`).
- Create environment variable `APP_API_HOST_NAME` and set the Security API host name or IP (eg. `localhost:1111`).
- Create a file to maintain hikari config (eg. `hikari.yml`) and set the file-location as value
  for `hikari.config-file-path` in the `hcp365reportingapi-{APP_ENV}.yml` file. Sample config for the hikari config file
  is below.
  - driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
  - jdbcUrl=jdbc:sqlserver://<dbhost>;databaseName=ContextAd
  - maximumPoolSize=200
  - username=username
  - password=password
  - minimumIdle=30
  - connectionTestQuery= select 1
- Run the App.
