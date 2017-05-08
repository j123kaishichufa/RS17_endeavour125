RUNNING ENDEAVOUR

Note: <INSTALLATION-DIR> is the location where endeavour was unzipped.

========RUNNING THE SAMPLE APPLICATION (HSQLDB)========

1. Start the database (HSQLDB) by running the following script:

<INSTALLATION-DIR>/data/start_hypersonic.bat

2. Start the application server (Tomcat) by running the following script:

<INSTALLATION-DIR>start_endeavour.bat

3. Enter the following URL in your Web Browser:

http://localhost:8080/endeavour/

4. Enter the following login credentials:

Username:Admin
Password:password

========DEPLOYING THE APPLICATION UNDER MYSQL==========

Note: Endeavour provides support for different database management systems. Each database 
script and its related hibernate file can be located under <INSTALLATION-DIR>/data/

1. Download and install MySQL 

2. Restore the database using the backup file available at:

<INSTALLATION-DIR>/data/mysql/endeavour.sql

3. Copy the file "hibernate.cfg.xml" located at:

<INSTALLATION-DIR>/data/mysql/hibernate.cfg.xml

in to:

<INSTALLATION-DIR>/tomcat/webapps/endeavour/WEB-INF/classes/

Note: This will overwrite the original "hibernate.cfg.xml" for HSQLDB and 
      will direct the application to use MySQL instead. 
      The original "hibernate.cfg.xml" for HSQLDB is located at:
      <INSTALLATION-DIR>/data/hsqldb/hibernate.cfg.xml
      
4. Provide your MySQL database credentials (username and password) where indicated by
USER_NAME_GOES_HERE and PASSWORD_GOES_HERE      

5. Start the application server (Tomcat) by running the following script:

<INSTALLATION-DIR>start_endeavour.bat

6. Enter the following URL in your Web Browser:

http://localhost:8080/endeavour/

7. Enter the following login credentials:

Username:Admin
Password:password

===========================================================================

NOTE ABOUT RUNNING ENDEAVOUR ON LINUX

To run Endeavour on Linux the steps mentioned above also apply however it is 
first necesary to first set the execution properties to all .sh scripts 
through the chmod command

This can be done by executing the following command at the root of 
the <INSTALLATION-DIR>

find . -name '*.sh' -print -exec chmod +x {} \;

===========================================================================

WIKI INFORMATION

Wiki support is provided by JAMWiki (http://www.jamwiki.org) under GPL 2.1

Link to acceess the Wiki outside Endeavour: 
http://localhost:8080/endeavour-wiki/

The configuration process begins automatically with the first JAMWiki pageview after setup. 
Configuration will request the following information:

1. A directory (accessible to the application server) into which JAMWiki files can be written.
2. A directory (accessible to the web/application server) into which images and other files can be uploaded.
3. The relative path (with respect to the web/application server doc root) to the image upload directory.
4. The login and password of an administrative user.
5. (Optional) If using an external database for persistency then the database settings must be provided. 
   See the Database Configuration section below for additional details.
6. (Optional) Once setup is complete, JAMWiki can be customized by using the Special:Admin page, 
   accessible to admins by clicking on the "Admin" link on the top right portion of all JAMWiki pages.

===========================================================================

EMAIL NOTIFICATION CONFIGURATION

In order to enable email notifications for assignment and unassigment of software artifacts to project members
the following steps must be followed:

1. Edit the "endeavour.settings" located at <INSTALLATION-DIR>/tomcat/webapps/endeavour/WEB-INF/classes
   and under the EMAIL CONFIGURATION section provide values for the configuration properties.

2. In Endeavour go to Security->Users and for the desired Project Member provide value for the
   "Email" field and set the "Email Assignments" checkbox to true. "Email" notifications can be set off
   for an specific Project Member by setting the "Email Assignments" to false.

===========================================================================

INTERNATIONALIZATION

Endeavour Agile ALM provides support for Internationalization (i18n).

Under the location <INSTALLATION-DIR>/tomcat/webapps/endeavour/WEB-INF/classes
are located the language translation files. English is the default language supported.

endeavour_en.properties (English translation)
endeavour_es.properties (Spanish translation)

To change Endeavour from English to Spanish simply edit the start script located at:

<INSTALLATION-DIR>/start_endeavour.bat (for Windows)
<INSTALLATION-DIR>/start-endeavour.sh (for Linux and Mac)

and change the following parameters in the CATALINA_OPTS
from
-Duser.language=en -Duser.country=EN
to
-Duser.language=es -Duser.country=ES

and start Endeavour as normal.

To translate Endeavour to a different language simply create a new translation file for
the desired locale and specify that locale in the CATALINA_OPTS.

For more information on Internationalization please visit the Java Internationalization tutorial
available at:

http://download.oracle.com/docs/cd/E17409_01/javase/tutorial/i18n/

===========================================================================

LDAP INTEGRATION

Edit the "endeavour.settings" located at <INSTALLATION-DIR>/tomcat/webapps/endeavour/WEB-INF/classes
and under the LDAP CONFIGURATION section provide values for the configuration properties.

# Enables or disables the LDAP authentication.
ldap.autentication.enabled=false

# LDAP location. For example "ldap://localhost:10389".
ldap.location=

# LDAP organizational unit. For example "ou=users,ou=system".
ldap.ou=

For more information refer to the LDAP Integration wiki page.
http://sourceforge.net/apps/mediawiki/endeavour-mgmt/index.php?title=LDAP_Integration

===========================================================================

CONTINUOS INTEGRATION INFORMATION

Continuous Integration support by Hudson (http://hudson-ci.org) under The MIT License

===========================================================================

SUVBERSION INTEGRATION INFORMATION

Subversion Browsing support by Sventon (http://www.sventon.org) under the Sventon License

===========================================================================

FORUMS INTEGRATION INFORMATION

Forums support by JForum (http://jforum.net/) under the BSD license















