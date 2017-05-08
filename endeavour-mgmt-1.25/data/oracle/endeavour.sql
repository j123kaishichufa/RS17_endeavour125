/*
* Paul Haller (08/24/2010 - for Endeavour 1.17)
* starting with the file Harman created for End. 1.7 I modified it for v1.17
* major change to his file is the removal of the ENDV_ prefix of all table names and some changes in the SQL syntax, so the script now can run from PL/SQL Developer
* added the changes for 1.17
*
* following are the comments of Harman for his version of the script:
*
*
/*
* ORACLE_Generic.SQL (These comments optimally view-able in MONOSPACED font)
* Harman Bajwa (04/22/2010 - for Endeavour Rel 1.7)
* Changed 04/27/2010 - Replaced the LONG RAW in favour of BLOB type
* Changed 04/27/2010 - Updated Comments
* Changed 04/28/2010 - Updated for release 1.8 
* 
* Source: Script originally devised using SQUIRREL SQL client on a running version and modified by hand to address Oracle specific issues and put in comments
* NOTE: The included source scripts can also be used to reconstruct for Oracle
*
* Purpose: To "relocate" the Endeavour database to Oracle 10g 
*          The original uses the HSQL DB in-memory engine for persistence (using Hibernate)
*
* eg: It may become desirable to transfer the objects and data necessary into Enterprise level databases, if not for 
* security/sharing/structuring purposes, then at least for backup/redundancy purposes. It is worth noting that at any given time, 
* ONE and only ONE app server instance should be connecting to this database (This should be strictly enforced, considering 
* anyone on the network with the correct username/password can reach the Oracle server and try to connect concurrently from 
* different instances). In addition, altering the endeavour database directly (or going behind the back of the app server) should be done 
* only if the app server container is NOT RUNNING
* For example: To re-order some use case events/scenarios/extensions, shut the app server down, apply the re-sort (drop the rows and 
* recreate using different record IDs etc.), taking into account any relational dependency issues, and then start up the app server 
* to start using Endeavour 
*
* This script transfers into Oracle, the data and objects expected by a fresh installed Endeavour 1.8 copy 
*
*
* ***************************************************************
* Database Changes:
* Prefixes: The original tables used do not have any prefix. A prefix of  was added to allow distinguishing Endeavour objects 
* (ie when the schema is shared). Ideally, a separate schema is desirable and this may become a non-issue, but absent that, this may be handy. 
* Applying suitable prefix also removes possibility of table names conflicting  with Oracle reserved keywords.
*
* Column names: The original seems to use FILE, NUMBER and DATE as column names. These have been changed (eg FILE_DATA, VER_NUMBER and DATE_COMMENT etc.) 
*               The new name change is also to be applied to the hibernate config (.xml) files (see "Related Changes" comment below)
*               Additionaly, FWIW, all column names/definitions/keywords in this script are in UPPER CASE only as a matter of personal choice, since Oracle does not care.
*
* Column types: 
*              - Record IDs and expected foreign keys are converted to NUMBER(10). 
*              - Variable Character columns are designated as VARCHAR2(500)
*              - All DATE/TIME holding columns are designated as the Oracle TIMESTAMP data type. 
*              - The BLOB columns used should ideally be set up properly (instead of using the default tablespace)
*              - The BOOLEAN data type is represented as "NUMBER(1) DEFAULT 0". Defaulting to 0 (= FALSE) ensures the field is never "tri"-stated. 
*                Hibernate takes care of mapping TRUE to 1 and FALSE to 0, as long as the correct "dialect" is specified
*                **NOTE** : Reports relying on database level BOOLEAN, instead of 0/1 values or Hibernate, may present design challenges
* ***************************************************************
*
*
*
* ***************************************************************
* Related Changes (not included in this file) ******************
* Hibernate: Changes are needed to the original Hibernate files:
*            1. *.hbm.xml - These files will need to be updated with the new table names (for attaching prefix ), as well as changed column names
*            2. hibernate.cfg.xml - This needs two changes -
*               A: Change to the Oracle Driver (Class name), connection url, user name and password
*               B: Change the hibernate "dialect" to Oracle 9i/10G
*               **NOTE**: Ideally the hibernate should be set up to use app server connection pools, if possible, instead of using the Driver classname directly
*            3. The app server class loader container needs the Oracle library. For example - ojdbc14.jar (if using thin client driver)
* ***************************************************************
* 
* *** NOTE *** Not all features of Endeavour were tested.
*/

DROP TABLE ACTOR ;
DROP TABLE ATTACHMENT ;
DROP TABLE CHANGE_REQUEST ;
DROP TABLE WORK_PRODUCT_COMMENT ;
DROP TABLE DEFECT ;
DROP TABLE "DOCUMENT" ;
DROP TABLE EVENT ;
DROP TABLE GLOSSARY_TERM ;
DROP TABLE "ITERATION" ;
DROP TABLE "PRIVILEGE" ;
DROP TABLE "PROJECT" ;
DROP TABLE PROJECT_MEMBER ;
DROP TABLE PROJECT_PROJECT_MEMBER ;
DROP TABLE SECURITY_GROUP ;
DROP TABLE TASK ;
DROP TABLE TEST_CASE ;
DROP TABLE TEST_PLAN ;
DROP TABLE TEST_RUN ;
DROP TABLE TEST_RUN_PROJECT_MEMBER ;
DROP TABLE USE_CASE ;
DROP TABLE USE_CASE_ACTOR ;
DROP TABLE "VERSION" ;
DROP TABLE WORK_PRODUCT ;
DROP TABLE WORK_PRODUCT_DOCUMENT ;
DROP TABLE WORK_PRODUCT_PROJECT_MEMBER ;
DROP TABLE DEPENDENCY;


CREATE TABLE ACTOR (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2(500) NOT NULL,
   DESCRIPTION VARCHAR2(500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL
);

CREATE TABLE ATTACHMENT (
   -- ** WARNING ** Specify separate storage clause for the BLOB type
   -- Using the default storage clause may cause fragmentation
   ID NUMBER(10) PRIMARY KEY,
   WORK_PRODUCT_ID NUMBER(10) NOT NULL,
   -- Oracle reserved word: FILE
   FILE_DATA BLOB,
   NAME VARCHAR2(500) NOT NULL
);

CREATE TABLE CHANGE_REQUEST (
   ID NUMBER(10) PRIMARY KEY,
   TYPE VARCHAR2(500) NOT NULL
);


CREATE TABLE DEFECT (
   ID NUMBER(10) PRIMARY KEY
);

CREATE TABLE DEPENDENCY (
  ID NUMBER(10) PRIMARY KEY,
  TYPE VARCHAR2(500) NOT NULL,
  PREDECESSOR_ID NUMBER(10),
  SUCESSOR_ID NUMBER(10)
)

CREATE TABLE "DOCUMENT" (
   ID NUMBER(10) PRIMARY KEY,
   DESCRIPTION VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL,
   FILE_NAME VARCHAR2 (500) NOT NULL
);

CREATE TABLE EVENT (
   ID NUMBER(10) PRIMARY KEY,
   IDX VARCHAR2 (500) NOT NULL,
   TEXT VARCHAR2 (500) NOT NULL,
   WORK_PRODUCT_ID number(10),
   EVENT_ID NUMBER(10),
   TEST_CASE_ID NUMBER(10)
);

CREATE TABLE GLOSSARY_TERM (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   DESCRIPTION VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL,
   CREATED_BY VARCHAR2 (500) NOT NULL
);

CREATE TABLE "ITERATION" (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL,
   START_DATE TIMESTAMP NOT NULL,
   END_DATE TIMESTAMP NOT NULL,
   CREATED_BY VARCHAR2 (500) NOT NULL
);

CREATE TABLE "PRIVILEGE" (
   ID NUMBER(10) PRIMARY KEY,
   VALUE VARCHAR2 (500) NOT NULL,
   SECURITY_GROUP_ID NUMBER(10) NOT NULL
);

CREATE TABLE "PROJECT" (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   DESCRIPTION VARCHAR2 (500) NOT NULL,
   START_DATE TIMESTAMP NOT NULL,
   STATUS VARCHAR2 (500) NOT NULL,
   END_DATE TIMESTAMP NOT NULL,
   CREATED_BY VARCHAR2 (500) NOT NULL
);

CREATE TABLE PROJECT_MEMBER (
   ID NUMBER(10) PRIMARY KEY,
   USER_ID VARCHAR2 (500) NOT NULL,
   PASSWORD VARCHAR2 (500) NOT NULL,
   FIRST_NAME VARCHAR2 (500) NOT NULL,
   LAST_NAME VARCHAR2 (500) NOT NULL,
   ROLE VARCHAR2 (500) NOT NULL,
   STATUS VARCHAR2 (500),
   STATUS_DATE TIMESTAMP,
   SECURITY_GROUP_ID NUMBER(10),
   -- NOTE: Oracle has no BOOLEAN - default to 0 (FALSE) and use HIBERNATE to translate using ORACLE dialect
   ACCEPT_NOTIFICATIONS NUMBER(1) DEFAULT 0,
   EMAIL VARCHAR(500) NOT NULL
);

CREATE TABLE PROJECT_PROJECT_MEMBER (
   PROJECT_ID NUMBER(10) NOT NULL,
   MEMBER_ID NUMBER(10) NOT NULL,
   CONSTRAINT SYS_PK_73 PRIMARY KEY (PROJECT_ID,MEMBER_ID)
);

CREATE TABLE SECURITY_GROUP (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL
);

CREATE TABLE TASK (
   ID NUMBER(10) PRIMARY KEY,
   WORK_PRODUCT_ID NUMBER(10)
);

CREATE TABLE TEST_CASE (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL,
   CREATED_BY VARCHAR2 (500) NOT NULL,
   PURPOSE VARCHAR2 (500) NOT NULL,
   PREREQUISITES VARCHAR2 (500),
   TEST_DATA VARCHAR2 (500)
);

CREATE TABLE TEST_PLAN (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   FOLDERS VARCHAR2 (500),
   CREATED_BY VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL
);

CREATE TABLE TEST_RUN (
   ID NUMBER(10) PRIMARY KEY,
   STATUS VARCHAR2 (500),
   EXECUTION_DATE TIMESTAMP,
   FOLDER VARCHAR2 (500) NOT NULL,
   TEST_CASE_ID NUMBER(10) NOT NULL,
   TEST_PLAN_ID NUMBER(10) NOT NULL
);

CREATE TABLE TEST_RUN_PROJECT_MEMBER (
   TEST_RUN_ID NUMBER(10) NOT NULL,
   MEMBER_ID NUMBER(10) NOT NULL,
   CONSTRAINT SYS_PK_79 PRIMARY KEY (TEST_RUN_ID,MEMBER_ID)
);

CREATE TABLE USE_CASE (
   ID NUMBER(10) PRIMARY KEY,
   EXTEND_ID NUMBER(10),
   INCLUDE_ID NUMBER(10),
   PRECONDITIONS VARCHAR2 (500),
   POSTCONDITIONS VARCHAR2 (500),
   TYPE VARCHAR2 (500) NOT NULL
);

CREATE TABLE USE_CASE_ACTOR (
   USE_CASE_ID NUMBER(10) NOT NULL,
   ACTOR_ID NUMBER(10) NOT NULL,
   CONSTRAINT SYS_PK_83 PRIMARY KEY (USE_CASE_ID,ACTOR_ID)
);

CREATE TABLE "VERSION" (
   -- ** WARNING ** Specify separate storage clause for the BLOB type
   -- Using the default storage clause may cause fragmentation
   ID NUMBER(10) PRIMARY KEY,
   -- Oracle reserved word: NUMBER
   VERSION_NUMBER NUMBER(10) NOT NULL,
   -- Oracle reserved word: FILE
   FILE_DATA BLOB,
   DOCUMENT_ID NUMBER(10) NOT NULL
);

CREATE TABLE WORK_PRODUCT (
   ID NUMBER(10) PRIMARY KEY,
   NAME VARCHAR2 (500) NOT NULL,
   PROJECT_ID NUMBER(10) NOT NULL,
   ITERATION_ID NUMBER(10),
   START_DATE TIMESTAMP NOT NULL,
   END_DATE TIMESTAMP NOT NULL,
   PROGRESS NUMBER(10) NOT NULL,
   CREATED_BY VARCHAR2 (500) NOT NULL,
   DESCRIPTION VARCHAR2 (500) NOT NULL,
   STATUS VARCHAR2 (500) NOT NULL,
   PRIORITY VARCHAR2 (500) NOT NULL,
   LABEL VARCHAR2 (500) NOT NULL
);


CREATE TABLE WORK_PRODUCT_COMMENT (
   ID NUMBER(10) PRIMARY KEY,
   WORK_PRODUCT_ID NUMBER(10),
   TEST_CASE_ID NUMBER(10),
   TEST_RUN_ID NUMBER(10),
   AUTHOR VARCHAR2 (500) NOT NULL,
   TEXT VARCHAR2 (500) NOT NULL,
   -- Oracle reserved word: DATE
   COMMENT_DATE TIMESTAMP NOT NULL
);

CREATE TABLE WORK_PRODUCT_DOCUMENT (
   WORK_PRODUCT_ID NUMBER(10) NOT NULL,
   DOCUMENT_ID NUMBER(10) NOT NULL,
   CONSTRAINT SYS_PK_49 PRIMARY KEY (WORK_PRODUCT_ID,DOCUMENT_ID)
);

CREATE TABLE WORK_PRODUCT_PROJECT_MEMBER (
   WORK_PRODUCT_ID NUMBER(10) NOT NULL,
   MEMBER_ID NUMBER(10) NOT NULL,
   CONSTRAINT SYS_PK_51 PRIMARY KEY (WORK_PRODUCT_ID,MEMBER_ID)
);


INSERT INTO ACTOR (ID,NAME,DESCRIPTION,PROJECT_ID) VALUES (0,'Cashier','Operates the Point of Sale terminal',0);
INSERT INTO ACTOR (ID,NAME,DESCRIPTION,PROJECT_ID) VALUES (1,'POS Manager','Is on charge of the Point Of Sale terminal start up and closing operation.',0);

INSERT INTO CHANGE_REQUEST (ID,TYPE) VALUES (3,'New Requirement');

INSERT INTO WORK_PRODUCT_COMMENT (ID,WORK_PRODUCT_ID,TEST_CASE_ID,TEST_RUN_ID,AUTHOR,TEXT,COMMENT_DATE) VALUES (0,0,null,null,'rsmith','It was decided that the passwords must contain at least 1 upper case letter, 1 number and should have a maximum length of 8 characters',timestamp '2009-07-11 00:00:00.0 US/Eastern');
INSERT INTO WORK_PRODUCT_COMMENT  (ID,WORK_PRODUCT_ID,TEST_CASE_ID,TEST_RUN_ID,AUTHOR,TEXT,COMMENT_DATE) VALUES (1,3,null,null,'rpeters','It was decided that the scope of this request is too big for the current POS system schedule, therefore it will be treated as a separate subsystem.',timestamp '2009-07-11 00:00:00.0 US/Eastern');
INSERT INTO WORK_PRODUCT_COMMENT  (ID,WORK_PRODUCT_ID,TEST_CASE_ID,TEST_RUN_ID,AUTHOR,TEXT,COMMENT_DATE) VALUES (2,4,null,null,'fcarlson','I was not able to reproduce this error. Please provide more information.',timestamp '2009-07-11 00:00:00.0 US/Eastern');

INSERT INTO DEFECT (ID) VALUES (4);
INSERT INTO DEFECT (ID) VALUES (6);
INSERT INTO DEFECT (ID) VALUES (7);

INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (0,'1.','Start the POS System',null,null,0);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (1,'2.','Enter the User Id',null,null,0);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (2,'3.','Enter the password',null,null,0);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (3,'1.','User enters userid',0,null,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (4,'2.','User enters password',0,null,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (5,'3.','System validates user id',0,null,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (6,'4.','System validates password',0,null,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (7,'3.1','System fails to validate user id',null,5,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (8,'3.2','System shows "Invalid user id" message',null,5,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (9,'4.1','System fails to validate password',null,6,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (10,'4.2','System shows "Invalid password" message',null,6,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (11,'5.','System grants user access to the system',0,null,null);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (12,'1.','Enter user information',null,null,1);
INSERT INTO EVENT (ID,IDX,TEXT,WORK_PRODUCT_ID,EVENT_ID,TEST_CASE_ID) VALUES (13,'2.','Save the user information',null,null,1);

INSERT INTO GLOSSARY_TERM (ID,NAME,CREATED_BY,PROJECT_ID,DESCRIPTION) VALUES (0,'POS','Admin',0,'Point Of Sale');
INSERT INTO GLOSSARY_TERM (ID,NAME,CREATED_BY,PROJECT_ID,DESCRIPTION) VALUES (1,'Backordered','Admin',0,'Any item with a negative available quantity');
INSERT INTO GLOSSARY_TERM (ID,NAME,CREATED_BY,PROJECT_ID,DESCRIPTION) VALUES (2,'Card Verification Code','Admin',0,'A three or four-digit code printed on a credit card used in manually entered credit card transactions to help verify that the customer actually has the card in their possession');

INSERT INTO "ITERATION" (ID,NAME,CREATED_BY,PROJECT_ID,START_DATE,END_DATE) VALUES (0,'POS Iteration 1','Admin',0,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern');

INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (1,'PLANNING_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (2,'PLANNING_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (3,'PLANNING_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (4,'REQUIREMENTS_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (5,'REQUIREMENTS_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (6,'REQUIREMENTS_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (7,'DEFECT_TRACKING_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (8,'DEFECT_TRACKING_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (9,'DEFECT_TRACKING_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (10,'TEST_MANAGEMENT_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (11,'TEST_MANAGEMENT_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (12,'TEST_MANAGEMENT_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (13,'DOCUMENT_MANAGEMENT_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (14,'DOCUMENT_MANAGEMENT_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (15,'DOCUMENT_MANAGEMENT_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (16,'REPORTS_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (17,'SECURITY_EDIT',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (18,'SECURITY_DELETE',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (19,'SECURITY_VIEW',0);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (20,'PLANNING_EDIT',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (21,'PLANNING_DELETE',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (22,'PLANNING_VIEW',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (23,'REQUIREMENTS_EDIT',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (24,'REQUIREMENTS_DELETE',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (25,'REQUIREMENTS_VIEW',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (26,'DEFECT_TRACKING_EDIT',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (27,'DEFECT_TRACKING_DELETE',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (28,'DEFECT_TRACKING_VIEW',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (29,'TEST_MANAGEMENT_EDIT',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (30,'TEST_MANAGEMENT_DELETE',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (31,'TEST_MANAGEMENT_VIEW',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (32,'DOCUMENT_MANAGEMENT_EDIT',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (33,'DOCUMENT_MANAGEMENT_DELETE',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (34,'DOCUMENT_MANAGEMENT_VIEW',1);
INSERT INTO "PRIVILEGE" (ID,VALUE,SECURITY_GROUP_ID) VALUES (35,'REPORTS_VIEW',0);

INSERT INTO "PROJECT" (ID,NAME,CREATED_BY,DESCRIPTION,START_DATE,STATUS,END_DATE) VALUES (0,'POS Project','Admin','Point of Sale system is an application used to record sales and handle payments.',timestamp'2009-07-05 00:00:00.0 US/Eastern','Construction',timestamp '2010-02-12 00:00:00.0 US/Eastern');

INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (0,'Admin','cGFzc3dvcmQ=','System','Administrator','System Administrator','Performing several system updates',timestamp '2009-09-17 22:01:52.758000000 US/Eastern',0,0,'admin@mail.com');
INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (1,'rsmith','cGFzc3dvcmQ=','Robert','Smith','Developer','Developing the login use case',timestamp '2009-09-17 22:05:23.79000000 US/Eastern',1,0,'rsmith@mail.com');
INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (2,'cwebb','cGFzc3dvcmQ=','Carl','Webb','Project Manager',null,null,1,0,'cwebb@mail.com');
INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (3,'djohnson','cGFzc3dvcmQ=','Daniel','Jonhson','Database Administrator',null,null,1,0,'djohnson@mail.com');
INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (4,'fcarlson','cGFzc3dvcmQ=','Flora','Carlson','Tester','Working on the login defect',timestamp '2009-09-17 22:04:03.50000000 US/Eastern',1,0,'fcarlson@mail.com');
INSERT INTO PROJECT_MEMBER (ID,USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE,STATUS,STATUS_DATE,SECURITY_GROUP_ID,ACCEPT_NOTIFICATIONS,EMAIL) VALUES (5,'rpeters','cGFzc3dvcmQ=','Rita','Peters','Business Analyst',null,null,1,0,'rpeters@mail.com');

INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,4);
INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,5);
INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,2);
INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,3);
INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,0);
INSERT INTO PROJECT_PROJECT_MEMBER (PROJECT_ID,MEMBER_ID) VALUES (0,1);

INSERT INTO SECURITY_GROUP (ID,NAME) VALUES (0,'Administrators');
INSERT INTO SECURITY_GROUP (ID,NAME) VALUES (1,'Developers');

INSERT INTO TASK (ID,WORK_PRODUCT_ID) VALUES (1,0);
INSERT INTO TASK VALUES(2,NULL);
INSERT INTO TASK VALUES(5,NULL);

INSERT INTO TEST_CASE (ID,NAME,CREATED_BY,PROJECT_ID,PURPOSE,PREREQUISITES,TEST_DATA) VALUES (0,'Login Test Case','Admin',0,'Verify the correct functionality of the Log in to the POS System','The POS Operator must have a profile created','userid:joe\npassword:possystem');
INSERT INTO TEST_CASE (ID,NAME,CREATED_BY,PROJECT_ID,PURPOSE,PREREQUISITES,TEST_DATA) VALUES (1,'User Registration Test Case','Admin',0,'Verify the correct functionality of the User Registration Module','','');

INSERT INTO TEST_PLAN (ID,NAME,CREATED_BY,FOLDERS,PROJECT_ID) VALUES (0,'POS Project Plan','Admin','Login|Application Modules|',0);

INSERT INTO TEST_RUN (ID,STATUS,EXECUTION_DATE,FOLDER,TEST_CASE_ID,TEST_PLAN_ID) VALUES (1,'Fail',timestamp '2009-11-15 12:48:07.854000000 US/Eastern','Login',0,0);
INSERT INTO TEST_RUN (ID,STATUS,EXECUTION_DATE,FOLDER,TEST_CASE_ID,TEST_PLAN_ID) VALUES (2,'Pass',timestamp '2009-11-15 12:48:13.283000000 US/Eastern','Application Modules',1,0);

INSERT INTO USE_CASE (ID,EXTEND_ID,INCLUDE_ID,PRECONDITIONS,POSTCONDITIONS,TYPE) VALUES (0,null,null,'User has an existing login profile','User is logged to the system','Business Use Case');

INSERT INTO USE_CASE_ACTOR (USE_CASE_ID,ACTOR_ID) VALUES (0,0);
INSERT INTO USE_CASE_ACTOR (USE_CASE_ID,ACTOR_ID) VALUES (0,1);

INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (0,'Login Use Case','Pending','High','Use Case','Admin',0,0,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',55,'Login a user to the system');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (1,'Database modifications','Pending','High','Task','Admin',0,null,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',100,'Request new USER_ID field in the USER table\nRequest new PASSWORD field in the USER table');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (2,'Database privileges request','Pending','High','Task','Admin',0,0,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',25,'Assign read, write and delete privileges to the software developers');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (3,'Inventory System','Pending','High','Change Request','Admin',0,0,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',0,'The POS project should interact with the Inventory System updating its information.');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (4,'Login defect','Pending','High','Defect','Admin',0,0,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',15,'Log in to the system does not validate the password correctly');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (5,'System Documentation','Pending','High','Task','Admin',0,null,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2009-08-05 00:00:00.0 US/Eastern',10,'Documentation for the POS System must be written');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (6,'Login icons','In Progress','Medium','Defect','Admin',0,null,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2010-02-12 00:00:00.0 US/Eastern',0,'The button on the login screen does not have icons');
INSERT INTO WORK_PRODUCT (ID,NAME,STATUS,PRIORITY,LABEL,CREATED_BY,PROJECT_ID,ITERATION_ID,START_DATE,END_DATE,PROGRESS,DESCRIPTION) VALUES (7,'Main menu spelling error','Closed','Low','Defect','Admin',0,null,timestamp '2009-07-05 00:00:00.0 US/Eastern',timestamp '2010-02-12 00:00:00.0 US/Eastern',0,'Tracking is mispelled as traking in the main menu');

INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (0,0);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (0,1);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (2,0);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (2,3);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (3,1);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (4,1);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (4,4);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (4,0);
INSERT INTO WORK_PRODUCT_PROJECT_MEMBER (WORK_PRODUCT_ID,MEMBER_ID) VALUES (5,5);

commit ;
