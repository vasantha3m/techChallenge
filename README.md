#RUNNING APPLICATION
1. pass the file name "application.conf" as a run time parameter to job in the Edit Configurations section
2. run readers.ReadFromBikeAPI object.

#DESIGN CONSIDERATIONS
1. Fetch Data in parallel -> We can use SparkStreaming/SparkBatch to continously fetch the data from APIs.
2. Teams independent Architecture -> Schema Independency
   -> Define a centralised schema 
   -> Transform all the source API data to this centralized schema and persist 
3. Maintaining versions
   Approach-1: HBASE <Written code but unable to test since set-up isn't available>
    -> Push the data into HBase which will maintain all versions of each record.
   Approach-2: HIVE	
    -> Append all the API source data to a staging_table
    -> Fetch the data from this staging_table and define a new column with version_no calculated based on id, date_stolen desc
    -> Create new table with this staging table data and new column version_no
	
#ABOUT THE APPLICATION
This application read the data from REST API on incremental basis and loads the data into the local hive database.

RESTAPI Summary url:
https://bikeindex.org:443/api/v3/search

RESTAPI Detail url:
https://bikeindex.org:443/api/v3/search?page=1&per_page=25&stolenness=all&access_token=incidents


Current Functionality:
----------------------
1. Define application.conf file with required fields and pass the path as run-time parameter to the job
URL_TEMPLATE=https://bikeindex.org:443/api/v3/search
URL_TYPE=summary
PAGE_NO=1
PER_PAGE=25
STOLENESS=all
ACCESS_TOKEN=incidents
TABLE_NAME=bikes
WRITE_INTO=hive

2. Based on the properties, the job will get the data and write to Hive.
-> For each incident, version number is maintained. 
complete dump of the data from URL is stored in staging_TABLE_NAME.

-> If the same incident receives at later, we will maintain version num as 1 for recent record.
complete dump with additional column of version_no stored in TABLE_NAME
Example:
id,date_stolen,description,version_no
1,6389,black honda,2
1,7896,black honda recovered,1

3. Handling all pages: 
We will get total record count from response header and iterate till all pages are read from API

#Test scenarios for code coverage (High Level)
1. Test UrlBuilderTest
    -> Build the url as per the fields in application.conf and asserting if url is properly formed 
	-> fields: PAGE_NO,PER_PAGE,STOLENESS,ACCESS_TOKEN=
2. Test writeToHiveTest/ReadFromBikeAPITest
    -> Get the dataset from the json test file and insert into Hive based on the configuration
3. Test method ReadFromBikeAPITest
